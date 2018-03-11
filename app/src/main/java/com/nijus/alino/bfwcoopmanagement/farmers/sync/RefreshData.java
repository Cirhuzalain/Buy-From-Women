package com.nijus.alino.bfwcoopmanagement.farmers.sync;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.nijus.alino.bfwcoopmanagement.BuildConfig;
import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RefreshData extends IntentService {

    public RefreshData() {
        super("");
    }

    public RefreshData(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(240, TimeUnit.SECONDS)
                .writeTimeout(240, TimeUnit.SECONDS)
                .readTimeout(240, TimeUnit.SECONDS)
                .build();

        SharedPreferences prefGoog = getApplicationContext().
                getSharedPreferences(getResources().getString(R.string.application_key), Context.MODE_PRIVATE);

        String appToken = prefGoog.getString(getResources().getString(R.string.app_key), "123");

        prefetchSeason(client, appToken);

        //Check non sync data and sync
        startService(new Intent(this, SyncBackground.class));

        //Check non update data and update
        startService(new Intent(this, UpdateSyncFarmer.class));


    }

    private boolean prefetchSeason(OkHttpClient client, String token) {
        try {
            Response seasonData = Utils.getServerData(client, token, BuildConfig.DEV_API_URL + "harvest.season");
            if (seasonData != null) {
                ResponseBody seasonInfo = seasonData.body();
                if (seasonInfo != null) {
                    String seasonsList = seasonInfo.string();

                    JSONObject harvestSeasonData = new JSONObject(seasonsList);
                    JSONArray harvestSeasonArray = harvestSeasonData.getJSONArray("results");

                    JSONObject harvestSeasonObjectInfo;

                    for (int i = 0; i < harvestSeasonArray.length(); i++) {
                        harvestSeasonObjectInfo = harvestSeasonArray.getJSONObject(i);
                        int serverId = harvestSeasonObjectInfo.getInt("id");
                        String name = harvestSeasonObjectInfo.getString("name");
                        String start = harvestSeasonObjectInfo.getString("date_start");
                        String end = harvestSeasonObjectInfo.getString("date_end");
                        boolean isActive = harvestSeasonObjectInfo.getBoolean("active");

                        // convert data
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        Date startDate = df.parse(start);
                        Date endDate = df.parse(end);

                        // Get New Harvest Season and save them
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(BfwContract.HarvestSeason.COLUMN_NAME, name);
                        contentValues.put(BfwContract.HarvestSeason.COLUMN_START_DATE, startDate.getTime());
                        contentValues.put(BfwContract.HarvestSeason.COLUMN_END_DATE, endDate.getTime());
                        contentValues.put(BfwContract.HarvestSeason.COLUMN_SERVER_ID, serverId);
                        if (isActive) {
                            contentValues.put(BfwContract.HarvestSeason.COLUMN_ACTIVE, 1);
                        } else {
                            contentValues.put(BfwContract.HarvestSeason.COLUMN_ACTIVE, 0);
                        }


                        String seasonSelection = BfwContract.HarvestSeason.TABLE_NAME + "." +
                                BfwContract.HarvestSeason.COLUMN_SERVER_ID + " =  ? ";
                        Cursor serverSeasonCursor = null;

                        int serverSeasonId = 0;

                        try {
                            serverSeasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null,
                                    seasonSelection, new String[]{Long.toString(serverId)}, null);

                            if (serverSeasonCursor != null && serverSeasonCursor.moveToFirst()) {
                                serverSeasonId = serverSeasonCursor.getInt(serverSeasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_SERVER_ID));
                            }
                        } finally {
                            if (serverSeasonCursor != null) {
                                serverSeasonCursor.close();
                            }
                        }
                        if (serverSeasonId == 0)
                            getContentResolver().insert(BfwContract.HarvestSeason.CONTENT_URI, contentValues);

                    }

                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (IOException | ParseException | JSONException exp) {
            return false;
        }
        return true;
    }
}

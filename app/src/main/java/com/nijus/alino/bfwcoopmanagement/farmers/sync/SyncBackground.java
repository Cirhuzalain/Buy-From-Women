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
import com.nijus.alino.bfwcoopmanagement.events.SyncDataEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SyncBackground extends IntentService {

    public final String LOG_TAG = SyncBackground.class.getSimpleName();

    public static final MediaType JSON
            = MediaType.parse("text/html; charset=utf-8");

    public SyncBackground() {
        super("");
    }

    public SyncBackground(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        SharedPreferences prefGoog = getApplicationContext().
                getSharedPreferences(getResources().getString(R.string.application_key), Context.MODE_PRIVATE);

        String appToken = prefGoog.getString(getResources().getString(R.string.app_key), "123");

        //get non sync farmer to the server (is_sync)
        int dataCount = 0;
        int farmerServerId;
        long id;
        Cursor cursor = null, landCursor = null;
        String selection = BfwContract.Farmer.TABLE_NAME + "." +
                BfwContract.Farmer.COLUMN_IS_SYNC + " =  0 ";
        String farmerSelection = BfwContract.Farmer.TABLE_NAME + "." +
                BfwContract.Farmer._ID + " =  ? ";

        String landSelection = BfwContract.LandPlot.TABLE_NAME + "." +
                BfwContract.LandPlot.COLUMN_FARMER_ID + " = ? ";

        String landInfo = BfwContract.LandPlot.TABLE_NAME + "." +
                BfwContract.LandPlot._ID + " = ? ";

        String bankInfos = "\"bank_ids\": [],";
        try {
            cursor = getContentResolver().query(BfwContract.Farmer.CONTENT_URI, null, selection, null, null);
            if (cursor != null) {
                dataCount = cursor.getCount();

                while (cursor.moveToNext()) {
                    //get bank info
                    id = cursor.getLong(cursor.getColumnIndex(BfwContract.Farmer._ID));

                    farmerServerId = 0;
                    //get land info

                    landCursor = getContentResolver().query(BfwContract.LandPlot.CONTENT_URI, null, landSelection, new String[]{Long.toString(id)}, null);


                    String name = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_NAME));
                    String phoneNumber = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_PHONE));
                    String gender = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_GENDER));

                    boolean houseHold = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_HOUSEHOLD_HEAD)) == 1;
                    int houseMember = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_HOUSE_MEMBER));
                    String sFirstName = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_FIRST_NAME));
                    String sLastName = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_LAST_NAME));
                    String cellPhone = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_CELL_PHONE));
                    String cellCarrier = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_CELL_CARRIER));
                    String membershipId = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_MEMBER_SHIP));
                    boolean isOther = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_OTHER)) == 1;
                    boolean tractors = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_TRACTORS)) == 1;
                    boolean harvester = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_HARVESTER)) == 1;
                    boolean dryer = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_DRYER)) == 1;
                    boolean tresher = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_TRESHER)) == 1;
                    boolean safeStorage = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_SAFE_STORAGE)) == 1;
                    boolean otherInfo = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_OTHER_INFO)) == 1;
                    boolean isDam = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_DAM)) == 1;
                    boolean isWell = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_WELL)) == 1;
                    boolean isBorehole = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_BOREHOLE)) == 1;
                    boolean isPipeBorne = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_PIPE_BORNE)) == 1;
                    boolean isRiverStream = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_RIVER_STREAM)) == 1;
                    boolean isIrrigation = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_IRRIGATION)) == 1;
                    boolean isNone = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_NONE)) == 1;
                    boolean isOtherSource = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_OTHER)) == 1;
                    int coopUserId = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_COOP_USER_ID));

                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(240, TimeUnit.SECONDS)
                            .writeTimeout(240, TimeUnit.SECONDS)
                            .readTimeout(240, TimeUnit.SECONDS)
                            .build();
                    String API = BuildConfig.API_URL + "res.partner";

                    String bodyContent = "{}";


                    RequestBody body = RequestBody.create(JSON, bodyContent);

                    Request request = new Request.Builder()
                            .url(API)
                            .header("Content-Type", "text/html")
                            .header("Access-Token", appToken)
                            .method("POST", body)
                            .build();

                    try {
                        Response responseFarmer = client.newCall(request).execute();
                        ResponseBody responseBody = responseFarmer.body();
                        if (responseBody != null) {
                            String farmerDataInfo = responseBody.string();
                            JSONObject farmerInfo = new JSONObject(farmerDataInfo);
                            if (farmerInfo.has("id")) {
                                farmerServerId = farmerInfo.getInt("id");

                                //update localId
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(BfwContract.Farmer.COLUMN_FARMER_SERVER_ID, farmerServerId);
                                contentValues.put(BfwContract.Farmer.COLUMN_IS_SYNC, 1);
                                contentValues.put(BfwContract.Farmer.COLUMN_IS_UPDATE, 1);
                                getContentResolver().update(BfwContract.Farmer.CONTENT_URI, contentValues, farmerSelection, new String[]{Long.toString(id)});
                            }
                        }

                        if (landCursor != null) {
                            long landId;
                            while (landCursor.moveToNext()) {
                                landId = landCursor.getInt(landCursor.getColumnIndex(BfwContract.LandPlot._ID));
                                double plotSize = landCursor.getDouble(landCursor.getColumnIndex(BfwContract.LandPlot.COLUMN_PLOT_SIZE));

                                if (farmerServerId != 0) {
                                    String plotInfo = "{" +
                                            "\"plot_size\": " + plotSize + "," +
                                            "\"partner_id\": " + farmerServerId + "" +
                                            "}";
                                    String API_INFO = BuildConfig.API_URL + "res.partner.land.plot";

                                    RequestBody bodyLand = RequestBody.create(JSON, plotInfo);

                                    Request requestLand = new Request.Builder()
                                            .url(API_INFO)
                                            .header("Content-Type", "text/html")
                                            .header("Access-Token", appToken)
                                            .method("POST", bodyLand)
                                            .build();

                                    Response responseLand = client.newCall(requestLand).execute();
                                    ResponseBody responseBodyLand = responseLand.body();

                                    if (responseBodyLand != null) {
                                        String bodyLandInfo = responseBodyLand.string();

                                        //set server id land in local db
                                        JSONObject bodyLandObject = new JSONObject(bodyLandInfo);
                                        if (bodyLandObject.has("id")) {
                                            int landServerId = bodyLandObject.getInt("id");

                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put(BfwContract.LandPlot.COLUMN_SERVER_ID, landServerId);
                                            contentValues.put(BfwContract.LandPlot.COLUMN_IS_SYNC, 1);
                                            contentValues.put(BfwContract.LandPlot.COLUMN_IS_UPDATE, 1);

                                            getContentResolver().update(BfwContract.LandPlot.CONTENT_URI, contentValues, landInfo, new String[]{Long.toString(landId)});
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException | JSONException exp) {
                        EventBus.getDefault().post(new SyncDataEvent(getResources().getString(R.string.syncing_error), false));
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (landCursor != null) {
                landCursor.close();
            }
        }

        //post event sync after
        if (dataCount > 0)
            EventBus.getDefault().post(new SyncDataEvent("", true));
    }
}

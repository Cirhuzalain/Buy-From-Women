package com.nijus.alino.bfwcoopmanagement.coopAgent.sync;


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

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class UpdateSyncAgentBkgrnd extends IntentService {

    public static final MediaType JSON
            = MediaType.parse("text/html; charset=utf-8");
    public final String LOG_TAG = UpdateSyncAgentBkgrnd.class.getSimpleName();

    public UpdateSyncAgentBkgrnd() {
        super("");
    }

    public UpdateSyncAgentBkgrnd(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        SharedPreferences prefGoog = getApplicationContext().
                getSharedPreferences(getResources().getString(R.string.application_key), Context.MODE_PRIVATE);

        String appToken = prefGoog.getString(getResources().getString(R.string.app_key), "123");

        //get non sync farmer to the server (is_sync)
        int dataCount = 0;
        int agentServerId;
        long id;
        Cursor cursor = null;

        String selection = BfwContract.CoopAgent.TABLE_NAME + "." +
                BfwContract.CoopAgent.COLUMN_IS_SYNC + " =  1 AND " +
                BfwContract.CoopAgent.TABLE_NAME + "." +
                BfwContract.CoopAgent.COLUMN_IS_UPDATE + " = 0";

        String selection_id = BfwContract.CoopAgent.TABLE_NAME + "." +
                BfwContract.CoopAgent._ID + " =  ? ";

        String bankInfos = "\"bank_ids\": [],";
        try {
            cursor = getContentResolver().query(BfwContract.CoopAgent.CONTENT_URI, null, selection, null, null);
            if (cursor != null) {
                dataCount = cursor.getCount();

                while (cursor.moveToNext()) {
                    id = cursor.getLong(cursor.getColumnIndex(BfwContract.CoopAgent._ID));
                    agentServerId = cursor.getInt(cursor.getColumnIndex(BfwContract.CoopAgent.COLUMN_AGENT_SERVER_ID));

                    String name = cursor.getString(cursor.getColumnIndex(BfwContract.CoopAgent.COLUMN_AGENT_NAME));
                    String phone = cursor.getString(cursor.getColumnIndex(BfwContract.CoopAgent.COLUMN_AGENT_PHONE));
                    String email = cursor.getString(cursor.getColumnIndex(BfwContract.CoopAgent.COLUMN_AGENT_EMAIL));
                    int coop = cursor.getInt(cursor.getColumnIndex(BfwContract.CoopAgent.COLUMN_COOP_ID));

                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(240, TimeUnit.SECONDS)
                            .writeTimeout(240, TimeUnit.SECONDS)
                            .readTimeout(240, TimeUnit.SECONDS)
                            .build();

                    //Construct body
                    String bodyContent = "{}";

                    bodyContent = "{" +
                            "\"name\": \"" + name + "\", " +
                            "\"phone\": \"" + phone + "\"," +
                            "\"email\": \"" + email + "\" ," +
                            "\"coop_id\": " + coop + " " +
                            "}";

                    String API_INFO = BuildConfig.DEV_API_URL + "coop.agent" + "/"+agentServerId;

                    RequestBody bodyLoan = RequestBody.create(JSON, bodyContent);

                    Request requesLoan = new Request.Builder()
                            .url(API_INFO)
                            .header("Content-Type", "text/html")
                            .header("Access-Token", appToken)
                            .method("PUT", bodyLoan)
                            .build();
                    try {
                        Response responseLoan = client.newCall(requesLoan).execute();
                        ResponseBody responseBodyLoan = responseLoan.body();
                        if (responseBodyLoan != null) {
                            String farmerDataInfo = responseBodyLoan.string();
                            if (farmerDataInfo.equals("{}")) {
                                //productServerId = productInfo.getInt("id");

                                //update localId
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(BfwContract.CoopAgent.COLUMN_AGENT_SERVER_ID, agentServerId);
                                contentValues.put(BfwContract.CoopAgent.COLUMN_IS_SYNC, 1);
                                contentValues.put(BfwContract.CoopAgent.COLUMN_IS_UPDATE, 1);

                                getContentResolver().update(BfwContract.CoopAgent.CONTENT_URI, contentValues, selection_id, new String[]{Long.toString(id)});
                            }
                        }

                    } catch (IOException e) {
                        EventBus.getDefault().post(new SyncDataEvent("An Error occur while updating Coop Agent data", false));
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        //post event sync after
        if (dataCount > 0)
            EventBus.getDefault().post(new SyncDataEvent(getResources().getString(R.string.add_agent_msg_sync), true));
    }
}

package com.nijus.alino.bfwcoopmanagement.coopAgent.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.nijus.alino.bfwcoopmanagement.BuildConfig;
import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.DeleteAgentEvent;
import com.nijus.alino.bfwcoopmanagement.events.ProcessingAgentEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DeleteSyncAgentBkgrnd extends IntentService {

    public static final MediaType JSON
            = MediaType.parse("text/html; charset=utf-8");
    public final String LOG_TAG = DeleteSyncAgentBkgrnd.class.getSimpleName();

    public DeleteSyncAgentBkgrnd() {
        super("");
    }

    public DeleteSyncAgentBkgrnd(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        SharedPreferences prefGoog = getApplicationContext().
                getSharedPreferences(getResources().getString(R.string.application_key), Context.MODE_PRIVATE);

        String appToken = prefGoog.getString(getResources().getString(R.string.app_key), "123");

        int dataCount = 0;
        int agentServerId;
        long id;
        Cursor cursor = null;


        Bundle bundle = intent.getBundleExtra("agent_data");
        ArrayList<Integer> selected_item_list = bundle.getIntegerArrayList("agent_del");

        String c = "";
        for (int id_loan_from_list : selected_item_list) {


            String selectionCoopAgent = BfwContract.CoopAgent.TABLE_NAME + "." +
                    BfwContract.CoopAgent._ID + " =  ? ";


            String bankInfos = "\"bank_ids\": [],";
            try {
                cursor = getContentResolver().query(BfwContract.CoopAgent.CONTENT_URI, null, selectionCoopAgent,
                        new String[]{Long.toString(id_loan_from_list)}, null);
                if (cursor != null) {
                    dataCount = cursor.getCount();

                    while (cursor.moveToNext()) {

                        EventBus.getDefault().post(new ProcessingAgentEvent("Processing your request ..."));

                        id = cursor.getLong(cursor.getColumnIndex(BfwContract.CoopAgent._ID));
                        agentServerId = cursor.getInt(cursor.getColumnIndex(BfwContract.CoopAgent.COLUMN_AGENT_SERVER_ID));

                        OkHttpClient client = new OkHttpClient.Builder()
                                .connectTimeout(240, TimeUnit.SECONDS)
                                .writeTimeout(240, TimeUnit.SECONDS)
                                .readTimeout(240, TimeUnit.SECONDS)
                                .build();

                        //Construct body
                        String bodyContent = "{}";

                        String API_INFO = BuildConfig.DEV_API_URL + "coop.agent" + "/" + agentServerId;

                        RequestBody bodyCoopAgent = RequestBody.create(JSON, bodyContent);

                        Request requesCoopAgent = new Request.Builder()
                                .url(API_INFO)
                                .header("Content-Type", "text/html")
                                .header("Access-Token", appToken)
                                .method("DELETE", bodyCoopAgent)
                                .build();
                        try {
                            Response responseCoopAgent = client.newCall(requesCoopAgent).execute();
                            ResponseBody responseBodyCoopAgent = responseCoopAgent.body();
                            if (responseBodyCoopAgent != null) {
                                String farmerDataInfo = responseBodyCoopAgent.string();
                                if (farmerDataInfo.equals("{}")) {
                                    getContentResolver().delete(BfwContract.CoopAgent.CONTENT_URI, selectionCoopAgent, new String[]{Long.toString(id)});
                                }
                            }
                        } catch (IOException e) {
                            EventBus.getDefault().post(new DeleteAgentEvent("An Error occur while delete buyer data", false));
                        }
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        //post event sync after
        if (dataCount > 0)
            EventBus.getDefault().post(new DeleteAgentEvent("CoopAgent(s) Removed Successfully", true));
    }
}

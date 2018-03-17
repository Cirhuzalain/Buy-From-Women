package com.nijus.alino.bfwcoopmanagement.buyers.sync;


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

public class UpdateSyncBuyerBkgrnd extends IntentService {

    public static final MediaType JSON
            = MediaType.parse("text/html; charset=utf-8");
    public final String LOG_TAG = UpdateSyncBuyerBkgrnd.class.getSimpleName();

    public UpdateSyncBuyerBkgrnd() {
        super("");
    }

    public UpdateSyncBuyerBkgrnd(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        SharedPreferences prefGoog = getApplicationContext().
                getSharedPreferences(getResources().getString(R.string.application_key), Context.MODE_PRIVATE);

        String appToken = prefGoog.getString(getResources().getString(R.string.app_key), "123");

        //get non sync buyer to the server (is_sync)
        int dataCount = 0;
        int buyerServerId;
        long id;
        Cursor cursor = null;

        String selection = BfwContract.Buyer.TABLE_NAME + "." +
                BfwContract.Buyer.COLUMN_IS_SYNC + " =  1 AND " +
                BfwContract.Buyer.TABLE_NAME + "." +
                BfwContract.Buyer.COLUMN_IS_UPDATE + " = 0";

        String selectionLoan_id = BfwContract.Buyer.TABLE_NAME + "." +
                BfwContract.Buyer._ID + " =  ? ";

        String bankInfos = "\"bank_ids\": [],";
        try {
            cursor = getContentResolver().query(BfwContract.Buyer.CONTENT_URI, null, selection, null, null);
            if (cursor != null) {
                dataCount = cursor.getCount();

                while (cursor.moveToNext()) {
                    id = cursor.getLong(cursor.getColumnIndex(BfwContract.Buyer._ID));
                    buyerServerId = cursor.getInt(cursor.getColumnIndex(BfwContract.Buyer.COLUMN_BUYER_SERVER_ID));

                    String name = cursor.getString(cursor.getColumnIndex(BfwContract.Buyer.COLUMN_BUYER_NAME));
                    String phone = cursor.getString(cursor.getColumnIndex(BfwContract.Buyer.COLUMN_BUYER_PHONE));
                    String email = cursor.getString(cursor.getColumnIndex(BfwContract.Buyer.COLUMN_BUYER_EMAIL));

                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(240, TimeUnit.SECONDS)
                            .writeTimeout(240, TimeUnit.SECONDS)
                            .readTimeout(240, TimeUnit.SECONDS)
                            .build();

                    //Construct body
                    String bodyContent = "{}";

                    bodyContent = "{" +
                            "\"name\": \"" + name + "\", " +
                            "\"buyer_phone\": \"" + phone + "\"," +
                            "\"buyer_email\": \"" + email + "\"" +
                            "}";

                    String API_INFO = BuildConfig.DEV_API_URL + "buyer" + "/" + buyerServerId;

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
                                //update localId
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(BfwContract.Buyer.COLUMN_BUYER_SERVER_ID, buyerServerId);
                                contentValues.put(BfwContract.Buyer.COLUMN_IS_SYNC, 1);
                                contentValues.put(BfwContract.Buyer.COLUMN_IS_UPDATE, 1);

                                getContentResolver().update(BfwContract.Buyer.CONTENT_URI, contentValues, selectionLoan_id, new String[]{Long.toString(id)});
                            }
                        }

                    } catch (IOException e) {
                        EventBus.getDefault().post(new SyncDataEvent("An Error occur while updating buyer data", false));
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
            EventBus.getDefault().post(new SyncDataEvent(getResources().getString(R.string.add_buyer_msg_sync), true));
    }
}

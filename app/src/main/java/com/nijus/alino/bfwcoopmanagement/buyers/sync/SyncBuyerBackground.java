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

public class SyncBuyerBackground extends IntentService {

    public static final MediaType JSON
            = MediaType.parse("text/html; charset=utf-8");
    public final String LOG_TAG = SyncBuyerBackground.class.getSimpleName();

    public SyncBuyerBackground() {
        super("");
    }

    public SyncBuyerBackground(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        SharedPreferences prefGoog = getApplicationContext().
                getSharedPreferences(getResources().getString(R.string.application_key), Context.MODE_PRIVATE);

        String appToken = prefGoog.getString(getResources().getString(R.string.app_key), "123");

        //get non sync farmer to the server (is_sync)
        int dataCount = 0;
        int buyerServerId;
        long id;
        Cursor cursor = null;

        String selectionBuyer = BfwContract.Buyer.TABLE_NAME + "." +
                BfwContract.Buyer.COLUMN_IS_SYNC + " =  0 ";

        String selectionBuyer_id = BfwContract.Buyer.TABLE_NAME + "." +
                BfwContract.Buyer._ID + " =  ? ";

        String bankInfos = "\"bank_ids\": [],";
        try {
            cursor = getContentResolver().query(BfwContract.Buyer.CONTENT_URI, null, selectionBuyer, null, null);
            if (cursor != null) {
                dataCount = cursor.getCount();

                while (cursor.moveToNext()) {
                    id = cursor.getLong(cursor.getColumnIndex(BfwContract.Buyer._ID));

                    String name = cursor.getString(cursor.getColumnIndex(BfwContract.Buyer.COLUMN_BUYER_NAME));
                    String phone = cursor.getString(cursor.getColumnIndex(BfwContract.Buyer.COLUMN_BUYER_PHONE));
                    String email = cursor.getString(cursor.getColumnIndex(BfwContract.Buyer.COLUMN_BUYER_EMAIL));

                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(240, TimeUnit.SECONDS)
                            .writeTimeout(240, TimeUnit.SECONDS)
                            .readTimeout(240, TimeUnit.SECONDS)
                            .build();

                    String bodyContent = "{}";

                        bodyContent = "{" +
                                "\"name\": \"" + name + "\", " +
                                "\"buyer_phone\": \"" + phone + "\"," +
                                "\"buyer_email\": \"" + email + "\"" +

                                "}";

                    String API_INFO = BuildConfig.DEV_API_URL + "buyer";

                    RequestBody bodyProduct = RequestBody.create(JSON, bodyContent);

                    Request requesProduct = new Request.Builder()
                            .url(API_INFO)
                            .header("Content-Type", "text/html")
                            .header("Access-Token", appToken)
                            .method("POST", bodyProduct)
                            .build();
                    try {
                        Response responseProduct = client.newCall(requesProduct).execute();
                        ResponseBody responseBodyProduct = responseProduct.body();
                        if (responseBodyProduct != null) {
                            String productDataInfo = responseBodyProduct.string();
                            JSONObject productInfo = new JSONObject(productDataInfo);
                            if (productInfo.has("id")) {
                                buyerServerId = productInfo.getInt("id");

                                //update localId
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(BfwContract.Buyer.COLUMN_BUYER_SERVER_ID, buyerServerId);
                                contentValues.put(BfwContract.Buyer.COLUMN_IS_SYNC, 1);
                                contentValues.put(BfwContract.Buyer.COLUMN_IS_UPDATE, 1);
                                getContentResolver().update(BfwContract.Buyer.CONTENT_URI, contentValues, selectionBuyer_id, new String[]{Long.toString(id)});
                            }
                        }

                    } catch (IOException | JSONException exp) {
                        EventBus.getDefault().post(new SyncDataEvent(getResources().getString(R.string.syncing_error_buyer), false));
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

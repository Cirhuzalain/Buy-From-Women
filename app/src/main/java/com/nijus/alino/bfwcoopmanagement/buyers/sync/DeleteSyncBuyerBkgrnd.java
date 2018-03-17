package com.nijus.alino.bfwcoopmanagement.buyers.sync;

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
import com.nijus.alino.bfwcoopmanagement.events.DeleteBuyerEvent;
import com.nijus.alino.bfwcoopmanagement.events.ProcessingBuyerEvent;

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

public class DeleteSyncBuyerBkgrnd extends IntentService {

    public static final MediaType JSON
            = MediaType.parse("text/html; charset=utf-8");
    public final String LOG_TAG = DeleteSyncBuyerBkgrnd.class.getSimpleName();

    public DeleteSyncBuyerBkgrnd() {
        super("");
    }

    public DeleteSyncBuyerBkgrnd(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        SharedPreferences prefGoog = getApplicationContext().
                getSharedPreferences(getResources().getString(R.string.application_key), Context.MODE_PRIVATE);

        String appToken = prefGoog.getString(getResources().getString(R.string.app_key), "123");

        int dataCount = 0;
        int buyerServerId;
        long id;
        Cursor cursor = null;


        Bundle bundle = intent.getBundleExtra("buyer_data");
        ArrayList<Integer> selected_item_list = bundle.getIntegerArrayList("buyer_del");

        String c = "";
        for (int id_loan_from_list : selected_item_list) {


            String selectionBuyer = BfwContract.Buyer.TABLE_NAME + "." +
                    BfwContract.Buyer._ID + " =  ? ";


            String bankInfos = "\"bank_ids\": [],";
            try {
                cursor = getContentResolver().query(BfwContract.Buyer.CONTENT_URI, null, selectionBuyer,
                        new String[]{Long.toString(id_loan_from_list)}, null);
                if (cursor != null) {
                    dataCount = cursor.getCount();

                    while (cursor.moveToNext()) {
                        EventBus.getDefault().post(new ProcessingBuyerEvent("Processing your request ..."));

                        id = cursor.getLong(cursor.getColumnIndex(BfwContract.Buyer._ID));
                        buyerServerId = cursor.getInt(cursor.getColumnIndex(BfwContract.Buyer.COLUMN_BUYER_SERVER_ID));


                        OkHttpClient client = new OkHttpClient.Builder()
                                .connectTimeout(240, TimeUnit.SECONDS)
                                .writeTimeout(240, TimeUnit.SECONDS)
                                .readTimeout(240, TimeUnit.SECONDS)
                                .build();

                        //Construct body
                        String bodyContent = "{}";

                        String API_INFO = BuildConfig.DEV_API_URL + "buyer" + "/" + buyerServerId;

                        RequestBody bodyBuyer = RequestBody.create(JSON, bodyContent);

                        Request requesBuyer = new Request.Builder()
                                .url(API_INFO)
                                .header("Content-Type", "text/html")
                                .header("Access-Token", appToken)
                                .method("DELETE", bodyBuyer)
                                .build();
                        try {
                            Response responseBuyer = client.newCall(requesBuyer).execute();
                            ResponseBody responseBodyBuyer = responseBuyer.body();
                            if (responseBodyBuyer != null) {
                                String farmerDataInfo = responseBodyBuyer.string();
                                if (farmerDataInfo.equals("{}")) {
                                    getContentResolver().delete(BfwContract.Buyer.CONTENT_URI, selectionBuyer, new String[]{Long.toString(id)});
                                }
                            }
                        } catch (IOException e) {
                            EventBus.getDefault().post(new DeleteBuyerEvent("An Error occur while delete buyer data", false));
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
            EventBus.getDefault().post(new DeleteBuyerEvent("Buyer(s) Removed Successfully", true));
    }
}

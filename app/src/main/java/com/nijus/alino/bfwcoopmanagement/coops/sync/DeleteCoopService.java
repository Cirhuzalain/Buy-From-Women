package com.nijus.alino.bfwcoopmanagement.coops.sync;


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
import com.nijus.alino.bfwcoopmanagement.events.DeleteCoopEvent;
import com.nijus.alino.bfwcoopmanagement.events.ProcessingCoopEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DeleteCoopService extends IntentService {

    private String coopSelect = BfwContract.Coops.TABLE_NAME + "." +
            BfwContract.Coops._ID + " = ? ";

    private String yieldSelect = BfwContract.YieldCoop.TABLE_NAME + "." +
            BfwContract.YieldCoop.COLUMN_COOP_ID + " = ? ";

    private String baselineSalesSelect = BfwContract.BaselineSalesCoop.TABLE_NAME + "." +
            BfwContract.BaselineSalesCoop.COLUMN_COOP_ID + " = ? ";

    private String forecastSalesSelect = BfwContract.SalesCoop.TABLE_NAME + "." +
            BfwContract.SalesCoop.COLUMN_COOP_ID + " = ? ";

    private String financeInfoCoopSelect = BfwContract.FinanceInfoCoop.TABLE_NAME + "." +
            BfwContract.FinanceInfoCoop.COLUMN_COOP_ID + " = ? ";

    private String coopAccessInfoSelect = BfwContract.CoopInfo.TABLE_NAME + "." +
            BfwContract.CoopInfo.COLUMN_COOP_ID + " = ? ";

    private int isSync;
    private int serverId;
    private int localId;

    public DeleteCoopService() {
        super("");
    }

    public DeleteCoopService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {

            Bundle bundle = intent.getBundleExtra("coop_data");

            ArrayList<Integer> coopIds = bundle.getIntegerArrayList("coop_del");

            if (coopIds != null) {
                // Iterate on each farmer id and perform appropriate action (delete farmer)
                for (Integer coopId : coopIds) {
                    Cursor coopDataCursor = null;
                    try {
                        coopDataCursor = getContentResolver().query(BfwContract.Coops.CONTENT_URI, null, coopSelect,
                                new String[]{Integer.toString(coopId)}, null);

                        if (coopDataCursor != null && coopDataCursor.moveToFirst()) {

                            EventBus.getDefault().post(new ProcessingCoopEvent("Processing your request ..."));

                            isSync = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.Coops.COLUMN_IS_SYNC));

                            if (isSync == 1) {
                                serverId = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.Coops.COLUMN_COOP_SERVER_ID));

                                boolean isServerSuccess = deleteCoopServer(serverId);
                                boolean isLocalSuccess = deleteCoopLocal(coopId);
                                if (!isServerSuccess || !isLocalSuccess) {
                                    // dispatch error message
                                    EventBus.getDefault().post(new DeleteCoopEvent("An Error occur while delete coop data", false));
                                }
                            } else {
                                boolean isLocalSuccess = deleteCoopLocal(coopId);
                                if (!isLocalSuccess) {
                                    // dispatch error message
                                    EventBus.getDefault().post(new DeleteCoopEvent("An Error occur while delete coop data", false));
                                }
                            }
                        }
                    } finally {
                        if (coopDataCursor != null) {
                            coopDataCursor.close();
                        }
                    }
                }
                // dispatch action to restart loader after data get delete
                EventBus.getDefault().post(new DeleteCoopEvent("Coop Remove Successfully", true));
            } else {
                EventBus.getDefault().post(new DeleteCoopEvent("Coop Id not available", false));
            }
        } else {
            // dispatch action to restart loader after data get delete
            EventBus.getDefault().post(new DeleteCoopEvent("No Data available", false));
        }

    }

    private boolean deleteCoopLocal(int coopLocalId) {
        //delete land if available
        getContentResolver().delete(BfwContract.YieldCoop.CONTENT_URI, yieldSelect, new String[]{Integer.toString(coopLocalId)});
        //delete baseline if available
        getContentResolver().delete(BfwContract.BaselineSalesCoop.CONTENT_URI, baselineSalesSelect, new String[]{Integer.toString(coopLocalId)});
        //delete forecast if available
        getContentResolver().delete(BfwContract.SalesCoop.CONTENT_URI, forecastSalesSelect, new String[]{Integer.toString(coopLocalId)});
        //delete finance data if available
        getContentResolver().delete(BfwContract.FinanceInfoCoop.CONTENT_URI, financeInfoCoopSelect, new String[]{Integer.toString(coopLocalId)});
        //delete  access to info if available
        getContentResolver().delete(BfwContract.CoopInfo.CONTENT_URI, coopAccessInfoSelect, new String[]{Integer.toString(coopLocalId)});
        //delete coop inside coop table
        getContentResolver().delete(BfwContract.Coops.CONTENT_URI, coopSelect, new String[]{Integer.toString(coopLocalId)});
        return true;
    }


    private boolean deleteCoopServer(int coopServerId) {

        SharedPreferences prefGoog = getApplicationContext().
                getSharedPreferences(getResources().getString(R.string.application_key), Context.MODE_PRIVATE);

        String appToken = prefGoog.getString(getResources().getString(R.string.app_key), "123");


        String coopSelect = BfwContract.Coops.TABLE_NAME + "." +
                BfwContract.Coops.COLUMN_COOP_SERVER_ID + " = ? ";

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(240, TimeUnit.SECONDS)
                .writeTimeout(240, TimeUnit.SECONDS)
                .readTimeout(240, TimeUnit.SECONDS)
                .build();

        Request requestLand;
        Response responseLand1;
        ResponseBody responseBodyLand1;
        String farmerDataInf;

        Cursor farmerDataInfo = null;

        try {

            farmerDataInfo = getContentResolver().query(BfwContract.Coops.CONTENT_URI, null, coopSelect, new String[]{Integer.toString(coopServerId)}, null);

            if (farmerDataInfo != null && farmerDataInfo.moveToFirst()) {

                localId = farmerDataInfo.getInt(farmerDataInfo.getColumnIndex(BfwContract.Coops._ID));

                //delete yield coop if is sync
                farmerDataInfo = getContentResolver().query(BfwContract.YieldCoop.CONTENT_URI, null, yieldSelect, new String[]{Integer.toString(localId)}, null);

                if (farmerDataInfo != null) {

                    while (farmerDataInfo.moveToNext()) {

                        isSync = farmerDataInfo.getInt(farmerDataInfo.getColumnIndex(BfwContract.YieldCoop.COLUMN_IS_SYNC));

                        if (isSync == 1) {

                            serverId = farmerDataInfo.getInt(farmerDataInfo.getColumnIndex(BfwContract.YieldCoop.COLUMN_SERVER_ID));

                            String API_INFO = BuildConfig.DEV_API_URL + "baseline.yield.coop" + "/" + serverId;

                            requestLand = new Request.Builder()
                                    .url(API_INFO)
                                    .header("Content-Type", "text/html")
                                    .header("Access-Token", appToken)
                                    .delete()
                                    .build();

                            responseLand1 = client.newCall(requestLand).execute();
                            responseBodyLand1 = responseLand1.body();

                            if (responseBodyLand1 != null) {
                                farmerDataInf = responseBodyLand1.string();
                                if (!farmerDataInf.equals("{}")) {
                                    return false;
                                }
                            }
                        }
                    }
                }

                //delete baseline sales coop if is sync
                farmerDataInfo = getContentResolver().query(BfwContract.BaselineSalesCoop.CONTENT_URI, null, baselineSalesSelect, new String[]{Integer.toString(localId)}, null);

                if (farmerDataInfo != null) {

                    while (farmerDataInfo.moveToNext()) {

                        isSync = farmerDataInfo.getInt(farmerDataInfo.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_IS_SYNC));

                        if (isSync == 1) {

                            serverId = farmerDataInfo.getInt(farmerDataInfo.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_SERVER_ID));

                            String API_INFO = BuildConfig.DEV_API_URL + "baseline.sales.coop" + "/" + serverId;

                            requestLand = new Request.Builder()
                                    .url(API_INFO)
                                    .header("Content-Type", "text/html")
                                    .header("Access-Token", appToken)
                                    .delete()
                                    .build();

                            responseLand1 = client.newCall(requestLand).execute();
                            responseBodyLand1 = responseLand1.body();

                            if (responseBodyLand1 != null) {
                                farmerDataInf = responseBodyLand1.string();
                                if (!farmerDataInf.equals("{}")) {
                                    return false;
                                }
                            }
                        }
                    }
                }

                //delete forecast if is sync
                farmerDataInfo = getContentResolver().query(BfwContract.SalesCoop.CONTENT_URI, null, forecastSalesSelect, new String[]{Integer.toString(localId)}, null);

                if (farmerDataInfo != null) {

                    while (farmerDataInfo.moveToNext()) {
                        isSync = farmerDataInfo.getInt(farmerDataInfo.getColumnIndex(BfwContract.SalesCoop.COLUMN_IS_SYNC));

                        if (isSync == 1) {
                            serverId = farmerDataInfo.getInt(farmerDataInfo.getColumnIndex(BfwContract.SalesCoop.COLUMN_SERVER_ID));

                            String API_INFO = BuildConfig.DEV_API_URL + "forecast.sales.coop" + "/" + serverId;

                            requestLand = new Request.Builder()
                                    .url(API_INFO)
                                    .header("Content-Type", "text/html")
                                    .header("Access-Token", appToken)
                                    .delete()
                                    .build();

                            responseLand1 = client.newCall(requestLand).execute();
                            responseBodyLand1 = responseLand1.body();

                            if (responseBodyLand1 != null) {
                                farmerDataInf = responseBodyLand1.string();
                                if (!farmerDataInf.equals("{}")) {
                                    return false;
                                }
                            }
                        }
                    }
                }

                //delete finance data if is sync
                farmerDataInfo = getContentResolver().query(BfwContract.FinanceInfoCoop.CONTENT_URI, null, financeInfoCoopSelect, new String[]{Integer.toString(localId)}, null);
                if (farmerDataInfo != null) {

                    while (farmerDataInfo.moveToNext()) {
                        isSync = farmerDataInfo.getInt(farmerDataInfo.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_IS_SYNC));

                        if (isSync == 1) {

                            serverId = farmerDataInfo.getInt(farmerDataInfo.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_SERVER_ID));

                            String API_INFO = BuildConfig.DEV_API_URL + "baseline.finance.info.coop" + "/" + serverId;

                            requestLand = new Request.Builder()
                                    .url(API_INFO)
                                    .header("Content-Type", "text/html")
                                    .header("Access-Token", appToken)
                                    .delete()
                                    .build();

                            responseLand1 = client.newCall(requestLand).execute();
                            responseBodyLand1 = responseLand1.body();

                            if (responseBodyLand1 != null) {
                                farmerDataInf = responseBodyLand1.string();
                                if (!farmerDataInf.equals("{}")) {
                                    return false;
                                }
                            }

                        }
                    }
                }

                //delete access to info if available
                farmerDataInfo = getContentResolver().query(BfwContract.CoopInfo.CONTENT_URI, null, coopAccessInfoSelect, new String[]{Integer.toString(localId)}, null);
                if (farmerDataInfo != null) {

                    while (farmerDataInfo.moveToNext()) {
                        isSync = farmerDataInfo.getInt(farmerDataInfo.getColumnIndex(BfwContract.CoopInfo.COLUMN_IS_SYNC));

                        if (isSync == 1) {
                            serverId = farmerDataInfo.getInt(farmerDataInfo.getColumnIndex(BfwContract.CoopInfo.COLUMN_SERVER_ID));

                            String API_INFO = BuildConfig.DEV_API_URL + "cooperative.access.info" + "/" + serverId;

                            requestLand = new Request.Builder()
                                    .url(API_INFO)
                                    .header("Content-Type", "text/html")
                                    .header("Access-Token", appToken)
                                    .delete()
                                    .build();

                            responseLand1 = client.newCall(requestLand).execute();
                            responseBodyLand1 = responseLand1.body();

                            if (responseBodyLand1 != null) {
                                farmerDataInf = responseBodyLand1.string();
                                if (!farmerDataInf.equals("{}")) {
                                    return false;
                                }
                            }
                        }
                    }
                }

                //delete farmer data in it table
                String API_INFO = BuildConfig.DEV_API_URL + "coop" + "/" + coopServerId;

                requestLand = new Request.Builder()
                        .url(API_INFO)
                        .header("Content-Type", "text/html")
                        .header("Access-Token", appToken)
                        .delete()
                        .build();

                responseLand1 = client.newCall(requestLand).execute();
                responseBodyLand1 = responseLand1.body();

                if (responseBodyLand1 != null) {
                    farmerDataInf = responseBodyLand1.string();
                    if (!farmerDataInf.equals("{}")) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        } catch (IOException exp) {
            return false;
        } finally {
            if (farmerDataInfo != null) {
                farmerDataInfo.close();
            }
        }
        return true;
    }
}

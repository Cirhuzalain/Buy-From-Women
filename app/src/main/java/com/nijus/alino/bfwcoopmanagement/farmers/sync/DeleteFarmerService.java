package com.nijus.alino.bfwcoopmanagement.farmers.sync;

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
import com.nijus.alino.bfwcoopmanagement.events.DeleteFarmerEvent;
import com.nijus.alino.bfwcoopmanagement.events.ProcessingFarmerEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DeleteFarmerService extends IntentService {

    private String farmerSelect = BfwContract.Farmer.TABLE_NAME + "." +
            BfwContract.Farmer._ID + " = ? ";

    private String landSelect = BfwContract.LandPlot.TABLE_NAME + "." +
            BfwContract.LandPlot.COLUMN_FARMER_ID + " = ? ";

    private String baselineSelect = BfwContract.BaselineFarmer.TABLE_NAME + "." +
            BfwContract.BaselineFarmer.COLUMN_FARMER_ID + " = ? ";

    private String forecastSelect = BfwContract.ForecastFarmer.TABLE_NAME + "." +
            BfwContract.ForecastFarmer.COLUMN_FARMER_ID + " = ? ";

    private String financeSelect = BfwContract.FinanceDataFarmer.TABLE_NAME + "." +
            BfwContract.FinanceDataFarmer.COLUMN_FARMER_ID + " = ? ";
    private String infoSelect = BfwContract.FarmerAccessInfo.TABLE_NAME + "." +
            BfwContract.FarmerAccessInfo.COLUMN_FARMER_ID + " = ? ";

    private int isSync;
    private int serverId;
    private int localId;

    public DeleteFarmerService() {
        super("");
    }

    public DeleteFarmerService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {

            Bundle bundle = intent.getBundleExtra("farmer_data");

            ArrayList<Integer> farmerIds = bundle.getIntegerArrayList("farmer_del");

            if (farmerIds != null) {
                // Iterate on each farmer id and perform appropriate action (delete farmer)
                for (Integer farmerId : farmerIds) {

                    Cursor farmerDataCursor = null;

                    try {
                        farmerDataCursor = getContentResolver().query(BfwContract.Farmer.CONTENT_URI, null, farmerSelect,
                                new String[]{Integer.toString(farmerId)}, null);

                        if (farmerDataCursor != null && farmerDataCursor.moveToFirst()) {

                            EventBus.getDefault().post(new ProcessingFarmerEvent("Processing your request ..."));

                            isSync = farmerDataCursor.getInt(farmerDataCursor.getColumnIndex(BfwContract.Farmer.COLUMN_IS_SYNC));

                            if (isSync == 1) {
                                serverId = farmerDataCursor.getInt(farmerDataCursor.getColumnIndex(BfwContract.Farmer.COLUMN_FARMER_SERVER_ID));

                                boolean isServerSuccess = deleteFarmerServer(serverId);
                                boolean isLocalSuccess = deleteFarmerLocal(farmerId);
                                if (!isServerSuccess || !isLocalSuccess) {
                                    // dispatch error message
                                    EventBus.getDefault().post(new DeleteFarmerEvent("An Error occur while delete farmer data", false));
                                }
                            } else {
                                boolean isLocalSuccess = deleteFarmerLocal(farmerId);
                                if (!isLocalSuccess) {
                                    // dispatch error message
                                    EventBus.getDefault().post(new DeleteFarmerEvent("An Error occur while delete farmer data", false));
                                }
                            }
                        }
                    } finally {
                        if (farmerDataCursor != null) {
                            farmerDataCursor.close();
                        }
                    }
                }
                // dispatch action to restart loader after data get delete
                EventBus.getDefault().post(new DeleteFarmerEvent("Farmer Remove Successfully", true));
            } else {
                EventBus.getDefault().post(new DeleteFarmerEvent("Farmer Id not available", false));
            }
        } else {
            // dispatch action to restart loader after data get delete
            EventBus.getDefault().post(new DeleteFarmerEvent("No Data available", false));
        }
    }

    private boolean deleteFarmerLocal(int farmerLocalId) {
        //delete land if available
        getContentResolver().delete(BfwContract.LandPlot.CONTENT_URI, landSelect, new String[]{Integer.toString(farmerLocalId)});
        //delete baseline if available
        getContentResolver().delete(BfwContract.BaselineFarmer.CONTENT_URI, baselineSelect, new String[]{Integer.toString(farmerLocalId)});
        //delete forecast if available
        getContentResolver().delete(BfwContract.ForecastFarmer.CONTENT_URI, forecastSelect, new String[]{Integer.toString(farmerLocalId)});
        //delete finance data if available
        getContentResolver().delete(BfwContract.FinanceDataFarmer.CONTENT_URI, financeSelect, new String[]{Integer.toString(farmerLocalId)});
        //delete  access to info if available
        getContentResolver().delete(BfwContract.FarmerAccessInfo.CONTENT_URI, infoSelect, new String[]{Integer.toString(farmerLocalId)});
        //delete farmer inside farmer table
        getContentResolver().delete(BfwContract.Farmer.CONTENT_URI, farmerSelect, new String[]{Integer.toString(farmerLocalId)});
        return true;
    }

    private boolean deleteFarmerServer(int farmerServerId) {

        SharedPreferences prefGoog = getApplicationContext().
                getSharedPreferences(getResources().getString(R.string.application_key), Context.MODE_PRIVATE);

        String appToken = prefGoog.getString(getResources().getString(R.string.app_key), "123");


        String farmerSelect = BfwContract.Farmer.TABLE_NAME + "." +
                BfwContract.Farmer.COLUMN_FARMER_SERVER_ID + " = ? ";

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

            farmerDataInfo = getContentResolver().query(BfwContract.Farmer.CONTENT_URI, null, farmerSelect, new String[]{Integer.toString(farmerServerId)}, null);

            if (farmerDataInfo != null && farmerDataInfo.moveToFirst()) {

                localId = farmerDataInfo.getInt(farmerDataInfo.getColumnIndex(BfwContract.Farmer._ID));

                //delete land if is sync
                farmerDataInfo = getContentResolver().query(BfwContract.LandPlot.CONTENT_URI, null, landSelect, new String[]{Integer.toString(localId)}, null);

                if (farmerDataInfo != null) {

                    while (farmerDataInfo.moveToNext()) {

                        isSync = farmerDataInfo.getInt(farmerDataInfo.getColumnIndex(BfwContract.LandPlot.COLUMN_IS_SYNC));

                        if (isSync == 1) {

                            serverId = farmerDataInfo.getInt(farmerDataInfo.getColumnIndex(BfwContract.LandPlot.COLUMN_SERVER_ID));

                            String API_INFO = BuildConfig.DEV_API_URL + "farmer.land" + "/" + serverId;

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

                //delete baseline if is sync
                farmerDataInfo = getContentResolver().query(BfwContract.BaselineFarmer.CONTENT_URI, null, baselineSelect, new String[]{Integer.toString(localId)}, null);

                if (farmerDataInfo != null) {

                    while (farmerDataInfo.moveToNext()) {

                        isSync = farmerDataInfo.getInt(farmerDataInfo.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_IS_SYNC));

                        if (isSync == 1) {

                            serverId = farmerDataInfo.getInt(farmerDataInfo.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_SERVER_ID));

                            String API_INFO = BuildConfig.DEV_API_URL + "baseline.farmer" + "/" + serverId;

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
                farmerDataInfo = getContentResolver().query(BfwContract.ForecastFarmer.CONTENT_URI, null, forecastSelect, new String[]{Integer.toString(localId)}, null);

                if (farmerDataInfo != null) {

                    while (farmerDataInfo.moveToNext()) {
                        isSync = farmerDataInfo.getInt(farmerDataInfo.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_IS_SYNC));

                        if (isSync == 1) {
                            serverId = farmerDataInfo.getInt(farmerDataInfo.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_SERVER_ID));

                            String API_INFO = BuildConfig.DEV_API_URL + "forecast.farmer" + "/" + serverId;

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
                farmerDataInfo = getContentResolver().query(BfwContract.FinanceDataFarmer.CONTENT_URI, null, financeSelect, new String[]{Integer.toString(localId)}, null);
                if (farmerDataInfo != null) {

                    while (farmerDataInfo.moveToNext()) {
                        isSync = farmerDataInfo.getInt(farmerDataInfo.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_IS_SYNC));

                        if (isSync == 1) {

                            serverId = farmerDataInfo.getInt(farmerDataInfo.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_SERVER_ID));

                            String API_INFO = BuildConfig.DEV_API_URL + "finance.data.farmer" + "/" + serverId;

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
                farmerDataInfo = getContentResolver().query(BfwContract.FarmerAccessInfo.CONTENT_URI, null, infoSelect, new String[]{Integer.toString(localId)}, null);
                if (farmerDataInfo != null) {

                    while (farmerDataInfo.moveToNext()) {
                        isSync = farmerDataInfo.getInt(farmerDataInfo.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_IS_SYNC));

                        if (isSync == 1) {
                            serverId = farmerDataInfo.getInt(farmerDataInfo.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_SERVER_ID));

                            String API_INFO = BuildConfig.DEV_API_URL + "access.info.farmer" + "/" + serverId;

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
                String API_INFO = BuildConfig.DEV_API_URL + "farmer" + "/" + farmerServerId;

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

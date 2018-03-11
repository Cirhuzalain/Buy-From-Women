package com.nijus.alino.bfwcoopmanagement.vendors.sync;

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
import com.nijus.alino.bfwcoopmanagement.events.DeleteVendorEvent;
import com.nijus.alino.bfwcoopmanagement.events.ProcessingVendorEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DeleteVendorService extends IntentService {

    private String vendorSelect = BfwContract.Vendor.TABLE_NAME + "." +
            BfwContract.Vendor._ID + " = ? ";

    /**
     * Attention here**/
    // TODO : Check well if vendor required PLOT LAND
    private String landSelect = BfwContract.VendorLand.TABLE_NAME + "." +
            BfwContract.VendorLand.COLUMN_VENDOR_ID + " = ? ";

    private String baselineSelect = BfwContract.BaseLineVendor.TABLE_NAME + "." +
            BfwContract.BaseLineVendor.COLUMN_VENDOR_ID + " = ? ";

    private String forecastSelect = BfwContract.ForecastVendor.TABLE_NAME + "." +
            BfwContract.ForecastVendor.COLUMN_VENDOR_ID + " = ? ";

    private String financeSelect = BfwContract.FinanceDataVendor.TABLE_NAME + "." +
            BfwContract.FinanceDataVendor.COLUMN_VENDOR_ID + " = ? ";
    private String infoSelect = BfwContract.VendorAccessInfo.TABLE_NAME + "." +
            BfwContract.VendorAccessInfo.COLUMN_VENDOR_ID + " = ? ";

    private int isSync;
    private int serverId;
    private int localId;

    public DeleteVendorService() {
        super("");
    }

    public DeleteVendorService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {

            Bundle bundle = intent.getBundleExtra("vendor_data");

            ArrayList<Integer> vendorIds = bundle.getIntegerArrayList("vendor_del");

            if (vendorIds != null) {
                // Iterate on each vendor id and perform appropriate action (delete vendor)
                for (Integer vendorId : vendorIds) {

                    Cursor vendorDataCursor = null;

                    try {
                        vendorDataCursor = getContentResolver().query(BfwContract.Vendor.CONTENT_URI, null, vendorSelect,
                                new String[]{Integer.toString(vendorId)}, null);

                        if (vendorDataCursor != null && vendorDataCursor.moveToFirst()) {

                            EventBus.getDefault().post(new ProcessingVendorEvent("Processing your request ..."));

                            isSync = vendorDataCursor.getInt(vendorDataCursor.getColumnIndex(BfwContract.Vendor.COLUMN_IS_SYNC));

                            if (isSync == 1) {
                                serverId = vendorDataCursor.getInt(vendorDataCursor.getColumnIndex(BfwContract.Vendor.COLUMN_VENDOR_SERVER_ID));

                                boolean isServerSuccess = deleteVendorServer(serverId);
                                boolean isLocalSuccess = deleteVendorLocal(vendorId);
                                if (!isServerSuccess || !isLocalSuccess) {
                                    // dispatch error message
                                    EventBus.getDefault().post(new DeleteVendorEvent("An Error occur while delete vendor data", false));
                                }
                            } else {
                                boolean isLocalSuccess = deleteVendorLocal(vendorId);
                                if (!isLocalSuccess) {
                                    // dispatch error message
                                    EventBus.getDefault().post(new DeleteVendorEvent("An Error occur while delete vendor data", false));
                                }
                            }
                        }
                    } finally {
                        if (vendorDataCursor != null) {
                            vendorDataCursor.close();
                        }
                    }
                }
                // dispatch action to restart loader after data get delete
                EventBus.getDefault().post(new DeleteVendorEvent("Vendor Remove Successfully", true));
            } else {
                EventBus.getDefault().post(new DeleteVendorEvent("Vendor Id not available", false));
            }
        } else {
            // dispatch action to restart loader after data get delete
            EventBus.getDefault().post(new DeleteVendorEvent("No Data available", false));
        }
    }

    private boolean deleteVendorLocal(int vendorLocalId) {
        //delete land if available
        getContentResolver().delete(BfwContract.VendorLand.CONTENT_URI, landSelect, new String[]{Integer.toString(vendorLocalId)});
        //delete baseline if available
        getContentResolver().delete(BfwContract.BaseLineVendor.CONTENT_URI, baselineSelect, new String[]{Integer.toString(vendorLocalId)});
        //delete forecast if available
        getContentResolver().delete(BfwContract.ForecastVendor.CONTENT_URI, forecastSelect, new String[]{Integer.toString(vendorLocalId)});
        //delete finance data if available
        getContentResolver().delete(BfwContract.FinanceDataVendor.CONTENT_URI, financeSelect, new String[]{Integer.toString(vendorLocalId)});
        //delete  access to info if available
        getContentResolver().delete(BfwContract.VendorAccessInfo.CONTENT_URI, infoSelect, new String[]{Integer.toString(vendorLocalId)});
        //delete vendor inside vendor table
        getContentResolver().delete(BfwContract.Vendor.CONTENT_URI, vendorSelect, new String[]{Integer.toString(vendorLocalId)});
        return true;
    }

    private boolean deleteVendorServer(int vendorServerId) {

        SharedPreferences prefGoog = getApplicationContext().
                getSharedPreferences(getResources().getString(R.string.application_key), Context.MODE_PRIVATE);

        String appToken = prefGoog.getString(getResources().getString(R.string.app_key), "123");


        String vendorSelect = BfwContract.Vendor.TABLE_NAME + "." +
                BfwContract.Vendor.COLUMN_VENDOR_SERVER_ID + " = ? ";

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(240, TimeUnit.SECONDS)
                .writeTimeout(240, TimeUnit.SECONDS)
                .readTimeout(240, TimeUnit.SECONDS)
                .build();

        Request requestLand;
        Response responseLand1;
        ResponseBody responseBodyLand1;
        String vendorDataInf;

        Cursor vendorDataInfo = null;

        try {

            vendorDataInfo = getContentResolver().query(BfwContract.Vendor.CONTENT_URI, null, vendorSelect, new String[]{Integer.toString(vendorServerId)}, null);

            if (vendorDataInfo != null && vendorDataInfo.moveToFirst()) {

                localId = vendorDataInfo.getInt(vendorDataInfo.getColumnIndex(BfwContract.Vendor._ID));

                //delete land if is sync
                vendorDataInfo = getContentResolver().query(BfwContract.VendorLand.CONTENT_URI, null, landSelect, new String[]{Integer.toString(localId)}, null);

                if (vendorDataInfo != null) {

                    while (vendorDataInfo.moveToNext()) {

                        isSync = vendorDataInfo.getInt(vendorDataInfo.getColumnIndex(BfwContract.VendorLand.COLUMN_IS_SYNC));

                        if (isSync == 1) {

                            serverId = vendorDataInfo.getInt(vendorDataInfo.getColumnIndex(BfwContract.VendorLand.COLUMN_SERVER_ID));

                            String API_INFO = BuildConfig.DEV_API_URL + "vendor.land" + "/" + serverId;

                            requestLand = new Request.Builder()
                                    .url(API_INFO)
                                    .header("Content-Type", "text/html")
                                    .header("Access-Token", appToken)
                                    .delete()
                                    .build();

                            responseLand1 = client.newCall(requestLand).execute();
                            responseBodyLand1 = responseLand1.body();

                            if (responseBodyLand1 != null) {
                                vendorDataInf = responseBodyLand1.string();
                                if (!vendorDataInf.equals("{}")) {
                                    return false;
                                }
                            }

                        }

                    }

                }

                //delete baseline if is sync
                vendorDataInfo = getContentResolver().query(BfwContract.BaseLineVendor.CONTENT_URI, null, baselineSelect, new String[]{Integer.toString(localId)}, null);

                if (vendorDataInfo != null) {

                    while (vendorDataInfo.moveToNext()) {

                        isSync = vendorDataInfo.getInt(vendorDataInfo.getColumnIndex(BfwContract.BaseLineVendor.COLUMN_IS_SYNC));

                        if (isSync == 1) {

                            serverId = vendorDataInfo.getInt(vendorDataInfo.getColumnIndex(BfwContract.BaseLineVendor.COLUMN_SERVER_ID));

                            String API_INFO = BuildConfig.DEV_API_URL + "baseline.vendor" + "/" + serverId;

                            requestLand = new Request.Builder()
                                    .url(API_INFO)
                                    .header("Content-Type", "text/html")
                                    .header("Access-Token", appToken)
                                    .delete()
                                    .build();

                            responseLand1 = client.newCall(requestLand).execute();
                            responseBodyLand1 = responseLand1.body();

                            if (responseBodyLand1 != null) {
                                vendorDataInf = responseBodyLand1.string();
                                if (!vendorDataInf.equals("{}")) {
                                    return false;
                                }
                            }
                        }
                    }
                }

                //delete forecast if is sync
                vendorDataInfo = getContentResolver().query(BfwContract.ForecastVendor.CONTENT_URI, null, forecastSelect, new String[]{Integer.toString(localId)}, null);

                if (vendorDataInfo != null) {

                    while (vendorDataInfo.moveToNext()) {
                        isSync = vendorDataInfo.getInt(vendorDataInfo.getColumnIndex(BfwContract.ForecastVendor.COLUMN_IS_SYNC));

                        if (isSync == 1) {
                            serverId = vendorDataInfo.getInt(vendorDataInfo.getColumnIndex(BfwContract.ForecastVendor.COLUMN_SERVER_ID));

                            String API_INFO = BuildConfig.DEV_API_URL + "forecast.vendor" + "/" + serverId;

                            requestLand = new Request.Builder()
                                    .url(API_INFO)
                                    .header("Content-Type", "text/html")
                                    .header("Access-Token", appToken)
                                    .delete()
                                    .build();

                            responseLand1 = client.newCall(requestLand).execute();
                            responseBodyLand1 = responseLand1.body();

                            if (responseBodyLand1 != null) {
                                vendorDataInf = responseBodyLand1.string();
                                if (!vendorDataInf.equals("{}")) {
                                    return false;
                                }
                            }
                        }
                    }
                }

                //delete finance data if is sync
                vendorDataInfo = getContentResolver().query(BfwContract.FinanceDataVendor.CONTENT_URI, null, financeSelect, new String[]{Integer.toString(localId)}, null);
                if (vendorDataInfo != null) {

                    while (vendorDataInfo.moveToNext()) {
                        isSync = vendorDataInfo.getInt(vendorDataInfo.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_IS_SYNC));

                        if (isSync == 1) {

                            serverId = vendorDataInfo.getInt(vendorDataInfo.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_SERVER_ID));

                            String API_INFO = BuildConfig.DEV_API_URL + "finance.data.vendor" + "/" + serverId;

                            requestLand = new Request.Builder()
                                    .url(API_INFO)
                                    .header("Content-Type", "text/html")
                                    .header("Access-Token", appToken)
                                    .delete()
                                    .build();

                            responseLand1 = client.newCall(requestLand).execute();
                            responseBodyLand1 = responseLand1.body();

                            if (responseBodyLand1 != null) {
                                vendorDataInf = responseBodyLand1.string();
                                if (!vendorDataInf.equals("{}")) {
                                    return false;
                                }
                            }

                        }
                    }
                }

                //delete access to info if available
                vendorDataInfo = getContentResolver().query(BfwContract.VendorAccessInfo.CONTENT_URI, null, infoSelect, new String[]{Integer.toString(localId)}, null);
                if (vendorDataInfo != null) {

                    while (vendorDataInfo.moveToNext()) {
                        isSync = vendorDataInfo.getInt(vendorDataInfo.getColumnIndex(BfwContract.VendorAccessInfo.COLUMN_IS_SYNC));

                        if (isSync == 1) {
                            serverId = vendorDataInfo.getInt(vendorDataInfo.getColumnIndex(BfwContract.VendorAccessInfo.COLUMN_SERVER_ID));

                            String API_INFO = BuildConfig.DEV_API_URL + "access.info.vendor" + "/" + serverId;

                            requestLand = new Request.Builder()
                                    .url(API_INFO)
                                    .header("Content-Type", "text/html")
                                    .header("Access-Token", appToken)
                                    .delete()
                                    .build();

                            responseLand1 = client.newCall(requestLand).execute();
                            responseBodyLand1 = responseLand1.body();

                            if (responseBodyLand1 != null) {
                                vendorDataInf = responseBodyLand1.string();
                                if (!vendorDataInf.equals("{}")) {
                                    return false;
                                }
                            }
                        }
                    }
                }

                //delete vendor data in it table
                String API_INFO = BuildConfig.DEV_API_URL + "vendor.farmer" + "/" + vendorServerId;

                requestLand = new Request.Builder()
                        .url(API_INFO)
                        .header("Content-Type", "text/html")
                        .header("Access-Token", appToken)
                        .delete()
                        .build();

                responseLand1 = client.newCall(requestLand).execute();
                responseBodyLand1 = responseLand1.body();

                if (responseBodyLand1 != null) {
                    vendorDataInf = responseBodyLand1.string();
                    if (!vendorDataInf.equals("{}")) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        } catch (IOException exp) {
            return false;
        } finally {
            if (vendorDataInfo != null) {
                vendorDataInfo.close();
            }
        }
        return true;
    }
}

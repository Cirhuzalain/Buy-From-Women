package com.nijus.alino.bfwcoopmanagement.vendors.sync;

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
import org.json.JSONArray;
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

public class UpdateSyncVendor extends IntentService {

    public static final MediaType JSON
            = MediaType.parse("text/html; charset=utf-8");

    public UpdateSyncVendor() {
        super("");
    }

    public UpdateSyncVendor(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SharedPreferences prefGoog = getApplicationContext().
                getSharedPreferences(getResources().getString(R.string.application_key), Context.MODE_PRIVATE);

        String appToken = prefGoog.getString(getResources().getString(R.string.app_key), "123");

        String selection = BfwContract.Vendor.TABLE_NAME + "." +
                BfwContract.Vendor.COLUMN_IS_SYNC + " =  1 AND " +
                BfwContract.Vendor.TABLE_NAME + "." +
                BfwContract.Vendor.COLUMN_IS_UPDATE + " = 0";

        String farmerSelection = BfwContract.Vendor.TABLE_NAME + "." +
                BfwContract.Vendor._ID + " =  ? ";

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(240, TimeUnit.SECONDS)
                .writeTimeout(240, TimeUnit.SECONDS)
                .readTimeout(240, TimeUnit.SECONDS)
                .build();

        //get non sync farmer to the server
        int seasonId, serverSeasonId = 1;
        int farmerServerId, isSync;
        long id;
        Cursor cursor = null, farmerInfoCursor = null, serverSeasonCursor = null;
        int dataCount = 0;

        String seasonSelection = BfwContract.HarvestSeason.TABLE_NAME + "." +
                BfwContract.HarvestSeason._ID + " =  ? ";

        String landInfo = BfwContract.VendorLand.TABLE_NAME + "." +
                BfwContract.VendorLand._ID + " = ? ";

        String forecastFarmerInfo = BfwContract.ForecastVendor.TABLE_NAME + "." +
                BfwContract.ForecastVendor._ID + " = ? ";

        String financeDataInfo = BfwContract.FinanceDataVendor.TABLE_NAME + "." +
                BfwContract.FinanceDataVendor._ID + " = ? ";

        String accessInfo = BfwContract.VendorAccessInfo.TABLE_NAME + "." +
                BfwContract.VendorAccessInfo._ID + " = ? ";

        String baselineFarmerInfo = BfwContract.BaseLineVendor.TABLE_NAME + "." +
                BfwContract.BaseLineVendor._ID + " = ? ";

        // query for sync and non sync vendor data

        String landSelection = BfwContract.VendorLand.TABLE_NAME + "." +
                BfwContract.VendorLand.COLUMN_VENDOR_ID + " = ?  AND ( " + BfwContract.VendorLand.TABLE_NAME + "." + BfwContract.VendorLand.COLUMN_IS_SYNC + " = 0 OR "
                + BfwContract.VendorLand.TABLE_NAME + "." + BfwContract.VendorLand.COLUMN_IS_SYNC + " = 1 OR " +
                BfwContract.VendorLand.TABLE_NAME + "." + BfwContract.VendorLand.COLUMN_IS_UPDATE + " = 0 )";


        String baselineFarmerSelection = BfwContract.BaseLineVendor.TABLE_NAME + "." +
                BfwContract.BaseLineVendor.COLUMN_VENDOR_ID + " = ?  AND ( " + BfwContract.BaseLineVendor.TABLE_NAME + "." + BfwContract.BaseLineVendor.COLUMN_IS_SYNC + " = 0 OR "
                + BfwContract.BaseLineVendor.TABLE_NAME + "." + BfwContract.BaseLineVendor.COLUMN_IS_SYNC + " = 1 OR " +
                BfwContract.BaseLineVendor.TABLE_NAME + "." + BfwContract.BaseLineVendor.COLUMN_IS_UPDATE + " = 0 )";

        String forecastFarmerSelection = BfwContract.ForecastVendor.TABLE_NAME + "." +
                BfwContract.ForecastVendor.COLUMN_VENDOR_ID + " = ?  AND ( " + BfwContract.ForecastVendor.TABLE_NAME + "." + BfwContract.ForecastVendor.COLUMN_IS_SYNC + " = 0 OR "
                + BfwContract.ForecastVendor.TABLE_NAME + "." + BfwContract.ForecastVendor.COLUMN_IS_SYNC + " = 1 OR " +
                BfwContract.ForecastVendor.TABLE_NAME + "." + BfwContract.ForecastVendor.COLUMN_IS_UPDATE + " = 0 )";

        String financeDataSelection = BfwContract.FinanceDataVendor.TABLE_NAME + "." +
                BfwContract.FinanceDataVendor.COLUMN_VENDOR_ID + " = ?  AND ( " + BfwContract.FinanceDataVendor.TABLE_NAME + "." + BfwContract.FinanceDataVendor.COLUMN_IS_SYNC + " = 0 OR "
                + BfwContract.FinanceDataVendor.TABLE_NAME + "." + BfwContract.FinanceDataVendor.COLUMN_IS_SYNC + " = 1 OR " +
                BfwContract.FinanceDataVendor.TABLE_NAME + "." + BfwContract.FinanceDataVendor.COLUMN_IS_UPDATE + " = 0 )";

        String accessInfoSelection = BfwContract.VendorAccessInfo.TABLE_NAME + "." +
                BfwContract.VendorAccessInfo.COLUMN_VENDOR_ID + " = ?  AND ( " + BfwContract.VendorAccessInfo.TABLE_NAME + "." + BfwContract.VendorAccessInfo.COLUMN_IS_SYNC + " = 0 OR "
                + BfwContract.VendorAccessInfo.TABLE_NAME + "." + BfwContract.VendorAccessInfo.COLUMN_IS_SYNC + " = 1 OR " +
                BfwContract.VendorAccessInfo.TABLE_NAME + "." + BfwContract.VendorAccessInfo.COLUMN_IS_UPDATE + " = 0 )";


        try {
            cursor = getContentResolver().query(BfwContract.Vendor.CONTENT_URI, null, selection, null, null);
            if (cursor != null) {
                dataCount = cursor.getCount();

                while (cursor.moveToNext()) {
                    //get bank info
                    id = cursor.getLong(cursor.getColumnIndex(BfwContract.Vendor._ID));
                    farmerServerId = cursor.getInt(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_VENDOR_SERVER_ID));


                    String name = cursor.getString(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_NAME));
                    String phoneNumber = cursor.getString(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_PHONE));
                    String gender = cursor.getString(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_GENDER));
                    String address = cursor.getString(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_ADDRESS));

                    Boolean houseHold = cursor.getInt(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_HOUSEHOLD_HEAD)) == 1;
                    Integer houseMember = cursor.getInt(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_HOUSE_MEMBER));
                    String sFirstName = cursor.getString(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_FIRST_NAME));
                    String sLastName = cursor.getString(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_LAST_NAME));
                    String cellPhone = cursor.getString(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_CELL_PHONE));
                    String cellCarrier = cursor.getString(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_CELL_CARRIER));

                    Boolean tractors = cursor.getInt(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_TRACTORS)) == 1;
                    Boolean harvester = cursor.getInt(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_HARVESTER)) == 1;
                    Boolean dryer = cursor.getInt(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_DRYER)) == 1;
                    Boolean tresher = cursor.getInt(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_TRESHER)) == 1;
                    Boolean safeStorage = cursor.getInt(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_SAFE_STORAGE)) == 1;
                    Boolean otherInfo = cursor.getInt(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_OTHER_INFO)) == 1;

                    Boolean isDam = cursor.getInt(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_DAM)) == 1;
                    Boolean isWell = cursor.getInt(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_WELL)) == 1;
                    Boolean isBorehole = cursor.getInt(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_BOREHOLE)) == 1;
                    Boolean isPipeBorne = cursor.getInt(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_PIPE_BORNE)) == 1;
                    Boolean isRiverStream = cursor.getInt(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_RIVER_STREAM)) == 1;
                    Boolean isIrrigation = cursor.getInt(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_IRRIGATION)) == 1;
                    Boolean isNone = cursor.getInt(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_NONE)) == 1;
                    Boolean isOtherSource = cursor.getInt(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_OTHER)) == 1;

                    String storageDetails = cursor.getString(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_STORAGE_DETAIL));
                    String newResourcesDetails = cursor.getString(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_OTHER_INFO));
                    String waterSourceDetails = cursor.getString(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_WATER_SOURCE_DETAILS));


                    String API = BuildConfig.DEV_API_URL + "vendor.farmer" + "/" + farmerServerId;

                    String bodyInfo = "{" +
                            "\"name\" : \"" + name + "\"," +
                            "\"phone\" : \"" + phoneNumber + "\"," +
                            "\"gender\" : \"" + gender + "\",";

                    if (address != null) {
                        bodyInfo = bodyInfo + "\"address\" : \"" + address + "\",";
                    } else {
                        bodyInfo = bodyInfo + "\"address\" : " + address + ",";
                    }

                    if (sFirstName != null) {
                        bodyInfo = bodyInfo + "\"spouse_firstname\" : \"" + sFirstName + "\",";
                    } else {
                        bodyInfo = bodyInfo + "\"spouse_firstname\" : " + sFirstName + ",";
                    }

                    if (sLastName != null) {
                        bodyInfo = bodyInfo + "\"spouse_lastname\" : \"" + sLastName + "\",";
                    } else {
                        bodyInfo = bodyInfo + "\"spouse_lastname\" : " + sLastName + ",";
                    }

                    if (cellPhone != null) {
                        bodyInfo = bodyInfo + "\"cellphone_alt\" : \"" + cellPhone + "\",";
                    } else {
                        bodyInfo = bodyInfo + "\"cellphone_alt\" : " + cellPhone + ",";
                    }

                    if (cellCarrier != null) {
                        bodyInfo = bodyInfo + "\"cell_carrier\" : \"" + cellCarrier + "\",";
                    } else {
                        bodyInfo = bodyInfo + "\"cell_carrier\" : " + cellCarrier + ",";
                    }

                    if (storageDetails != null) {
                        bodyInfo = bodyInfo + "\"storage_details\" : \"" + storageDetails + "\",";
                    } else {
                        bodyInfo = bodyInfo + "\"storage_details\" : " + storageDetails + ",";
                    }

                    if (newResourcesDetails != null) {
                        bodyInfo = bodyInfo + "\"other_details\" : \"" + newResourcesDetails + "\",";
                    } else {
                        bodyInfo = bodyInfo + "\"other_details\" : " + newResourcesDetails + ",";
                    }

                    if (waterSourceDetails != null) {
                        bodyInfo = bodyInfo + "\"other_water_source\" : \"" + waterSourceDetails + "\",";
                    } else {
                        bodyInfo = bodyInfo + "\"other_water_source\" : " + waterSourceDetails + ",";
                    }

                    bodyInfo = bodyInfo + "\"ar_tractors\" : " + tractors + "," +
                            "\"head_of_household\" : " + houseHold + "," +
                            "\"num_household_members\" : " + houseMember + "," +
                            "\"ar_harverster\" : " + harvester + "," +
                            "\"ar_dryer\" : " + dryer + "," +
                            "\"ar_thresher\" : " + tresher + "," +
                            "\"ar_safestorage\" : " + safeStorage + "," +
                            "\"ar_other\" : " + otherInfo + "," +
                            "\"mws_dam\" : " + isDam + "," +
                            "\"mws_well\" : " + isWell + "," +
                            "\"mws_borehole\" : " + isBorehole + "," +
                            "\"mws_rs\" : " + isRiverStream + "," +
                            "\"mws_pb\" : " + isPipeBorne + "," +
                            "\"mws_irrigation\" : " + isIrrigation + "," +
                            "\"mws_none\" : " + isNone + "," +
                            "\"mws_other\" : " + isOtherSource + "" +
                            "}";


                    RequestBody body = RequestBody.create(JSON, bodyInfo);

                    Request request = new Request.Builder()
                            .url(API)
                            .header("Content-Type", "text/html")
                            .header("Access-Token", appToken)
                            .method("PUT", body)
                            .build();

                    try {
                        Response responseFarmer = client.newCall(request).execute();
                        ResponseBody responseBody = responseFarmer.body();
                        if (responseBody != null) {
                            String farmerDataInfo = responseBody.string();
                            if (farmerDataInfo.equals("{}")) {

                                //update localId
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(BfwContract.Vendor.COLUMN_IS_SYNC, 1);
                                contentValues.put(BfwContract.Vendor.COLUMN_IS_UPDATE, 1);
                                getContentResolver().update(BfwContract.Vendor.CONTENT_URI, contentValues, farmerSelection, new String[]{Long.toString(id)});

                                //update land if available
                                farmerInfoCursor = getContentResolver().query(BfwContract.VendorLand.CONTENT_URI, null, landSelection, new String[]{Long.toString(id)}, null);

                                if (farmerInfoCursor != null) {
                                    long landId;
                                    long landServerInfo;
                                    while (farmerInfoCursor.moveToNext()) {

                                        landId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.VendorLand._ID));
                                        landServerInfo = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.VendorLand.COLUMN_SERVER_ID));
                                        double plotSize = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.VendorLand.COLUMN_PLOT_SIZE));
                                        double lat = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.VendorLand.COLUMN_LAT_INFO));
                                        double lng = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.VendorLand.COLUMN_LNG_INFO));
                                        isSync = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.VendorLand.COLUMN_IS_SYNC));

                                        seasonId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.VendorLand.COLUMN_SEASON_ID));

                                        serverSeasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null,
                                                seasonSelection, new String[]{Long.toString(seasonId)}, null);

                                        if (serverSeasonCursor != null && serverSeasonCursor.moveToFirst()) {
                                            serverSeasonId = serverSeasonCursor.getInt(serverSeasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_SERVER_ID));
                                        }

                                        if (isSync == 1) {
                                            String plotInfo = "{" +
                                                    "\"plot_size\": " + plotSize + "," +
                                                    "\"lat\": " + lat + "," +
                                                    "\"lng\": " + lng + "," +
                                                    "\"harvest_id\": " + serverSeasonId + "," +
                                                    "\"vendor_id\": " + farmerServerId + "" +
                                                    "}";
                                            String API_INFO = BuildConfig.DEV_API_URL + "vendor.land" + "/" + landServerInfo;

                                            RequestBody bodyLand = RequestBody.create(JSON, plotInfo);

                                            Request requestLand = new Request.Builder()
                                                    .url(API_INFO)
                                                    .header("Content-Type", "text/html")
                                                    .header("Access-Token", appToken)
                                                    .method("PUT", bodyLand)
                                                    .build();

                                            Response responseLand1 = client.newCall(requestLand).execute();
                                            ResponseBody responseBodyLand1 = responseLand1.body();

                                            if (responseBodyLand1 != null) {
                                                String farmerDataInf = responseBodyLand1.string();
                                                if (farmerDataInf.equals("{}")) {
                                                    ContentValues contentValue = new ContentValues();
                                                    contentValue.put(BfwContract.VendorLand.COLUMN_IS_SYNC, 1);
                                                    contentValue.put(BfwContract.VendorLand.COLUMN_IS_UPDATE, 1);

                                                    getContentResolver().update(BfwContract.VendorLand.CONTENT_URI, contentValue, landInfo, new String[]{Long.toString(landId)});
                                                }
                                            }
                                        } else {
                                            String plotInfo = "{" +
                                                    "\"plot_size\": " + plotSize + "," +
                                                    "\"lat\": " + lat + "," +
                                                    "\"lng\": " + lng + "," +
                                                    "\"harvest_id\": " + serverSeasonId + "," +
                                                    "\"vendor_id\": " + farmerServerId + "" +
                                                    "}";

                                            String API_INFO = BuildConfig.DEV_API_URL + "vendor.land";

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

                                                JSONObject bodyLandObject = new JSONObject(bodyLandInfo);
                                                if (bodyLandObject.has("id")) {
                                                    int landServerId = bodyLandObject.getInt("id");

                                                    ContentValues contentValue = new ContentValues();
                                                    contentValue.put(BfwContract.VendorLand.COLUMN_SERVER_ID, landServerId);
                                                    contentValue.put(BfwContract.VendorLand.COLUMN_IS_SYNC, 1);
                                                    contentValue.put(BfwContract.VendorLand.COLUMN_IS_UPDATE, 1);

                                                    getContentResolver().update(BfwContract.VendorLand.CONTENT_URI, contentValue, landInfo, new String[]{Long.toString(landId)});
                                                }
                                            }
                                        }
                                    }
                                }

                                //Update baseline if available
                                farmerInfoCursor = getContentResolver().query(BfwContract.BaseLineVendor.CONTENT_URI, null, baselineFarmerSelection, new String[]{Long.toString(id)}, null);
                                if (farmerInfoCursor != null) {

                                    int baselineId;
                                    int baselineServerId;
                                    Double seasonharvest;
                                    Double lostharvesttotal;
                                    Double soldharvesttotal;
                                    Double totalqtycoops;
                                    Double pricesoldcoops;
                                    Double totalqtymiddlemen;
                                    Double pricesoldmiddlemen;
                                    int baselineSeasonId;
                                    while (farmerInfoCursor.moveToNext()) {

                                        baselineId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.BaseLineVendor._ID));
                                        baselineServerId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.BaseLineVendor.COLUMN_SERVER_ID));

                                        seasonharvest = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.BaseLineVendor.COLUMN_TOT_PROD_B_KG));
                                        lostharvesttotal = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.BaseLineVendor.COLUMN_TOT_LOST_KG));
                                        soldharvesttotal = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.BaseLineVendor.COLUMN_TOT_SOLD_KG));
                                        totalqtycoops = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.BaseLineVendor.COLUMN_TOT_VOL_SOLD_COOP));
                                        pricesoldcoops = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.BaseLineVendor.COLUMN_PRICE_SOLD_COOP_PER_KG));
                                        totalqtymiddlemen = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.BaseLineVendor.COLUMN_TOT_VOL_SOLD_IN_KG));
                                        pricesoldmiddlemen = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.BaseLineVendor.COLUMN_PRICE_SOLD_KG));

                                        baselineSeasonId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.BaseLineVendor.COLUMN_SEASON_ID));
                                        isSync = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.BaseLineVendor.COLUMN_IS_SYNC));

                                        serverSeasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null,
                                                seasonSelection, new String[]{Long.toString(baselineSeasonId)}, null);

                                        if (serverSeasonCursor != null && serverSeasonCursor.moveToFirst()) {
                                            serverSeasonId = serverSeasonCursor.getInt(serverSeasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_SERVER_ID));
                                        }

                                        if (isSync == 1) {
                                            String accInFoData = "{" +
                                                    "\"seasona_harvest\": " + seasonharvest + "," +
                                                    "\"lost_harvest_total\": " + lostharvesttotal + "," +
                                                    "\"sold_harvest_total\": " + soldharvesttotal + "," +
                                                    "\"total_qty_coops\": " + totalqtycoops + "," +
                                                    "\"price_sold_coops\": " + pricesoldcoops + "," +
                                                    "\"total_qty_middlemen\": " + totalqtymiddlemen + "," +
                                                    "\"price_sold_middlemen\": " + pricesoldmiddlemen + "," +
                                                    "\"harvest_id\": " + serverSeasonId + "," +
                                                    "\"vendor_id\": " + farmerServerId + "" +
                                                    "}";

                                            String API_INFO = BuildConfig.DEV_API_URL + "baseline.vendor" + "/" + baselineServerId;

                                            RequestBody bodyLand = RequestBody.create(JSON, accInFoData);

                                            Request requestLand = new Request.Builder()
                                                    .url(API_INFO)
                                                    .header("Content-Type", "text/html")
                                                    .header("Access-Token", appToken)
                                                    .method("PUT", bodyLand)
                                                    .build();

                                            Response responseLand = client.newCall(requestLand).execute();
                                            ResponseBody responseBodyLand = responseLand.body();

                                            if (responseBodyLand != null) {
                                                String bodyLandInfo = responseBodyLand.string();

                                                if (bodyLandInfo.equals("{}")) {

                                                    ContentValues baselineValues = new ContentValues();
                                                    baselineValues.put(BfwContract.BaseLineVendor.COLUMN_IS_SYNC, 1);
                                                    baselineValues.put(BfwContract.BaseLineVendor.COLUMN_IS_UPDATE, 1);

                                                    getContentResolver().update(BfwContract.BaseLineVendor.CONTENT_URI, baselineValues, baselineFarmerInfo,
                                                            new String[]{Long.toString(baselineId)});
                                                }
                                            }
                                        } else {
                                            String accInFoData = "{" +
                                                    "\"seasona_harvest\": " + seasonharvest + "," +
                                                    "\"lost_harvest_total\": " + lostharvesttotal + "," +
                                                    "\"sold_harvest_total\": " + soldharvesttotal + "," +
                                                    "\"total_qty_coops\": " + totalqtycoops + "," +
                                                    "\"price_sold_coops\": " + pricesoldcoops + "," +
                                                    "\"total_qty_middlemen\": " + totalqtymiddlemen + "," +
                                                    "\"price_sold_middlemen\": " + pricesoldmiddlemen + "," +
                                                    "\"harvest_id\": " + serverSeasonId + "," +
                                                    "\"vendor_id\": " + farmerServerId + "" +
                                                    "}";

                                            String API_INFO = BuildConfig.DEV_API_URL + "baseline.vendor";

                                            RequestBody bodyLand = RequestBody.create(JSON, accInFoData);

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

                                                JSONObject bodyLandObject = new JSONObject(bodyLandInfo);
                                                if (bodyLandObject.has("id")) {
                                                    int infoServerId = bodyLandObject.getInt("id");

                                                    ContentValues baselineValues = new ContentValues();
                                                    baselineValues.put(BfwContract.BaseLineVendor.COLUMN_SERVER_ID, infoServerId);
                                                    baselineValues.put(BfwContract.BaseLineVendor.COLUMN_IS_SYNC, 1);
                                                    baselineValues.put(BfwContract.BaseLineVendor.COLUMN_IS_UPDATE, 1);

                                                    getContentResolver().update(BfwContract.BaseLineVendor.CONTENT_URI, baselineValues, baselineFarmerInfo,
                                                            new String[]{Long.toString(baselineId)});
                                                }
                                            }
                                        }

                                    }

                                }

                                //update access to information if available
                                farmerInfoCursor = getContentResolver().query(BfwContract.VendorAccessInfo.CONTENT_URI, null, accessInfoSelection, new String[]{Long.toString(id)}, null);
                                if (farmerInfoCursor != null) {

                                    int infoId;
                                    Integer infoServerId;

                                    Boolean aes;
                                    Boolean cri;
                                    Boolean seeds;
                                    Boolean orgFert;
                                    Boolean inorgFert;
                                    Boolean labour;
                                    Boolean iwp;
                                    Boolean ss;

                                    while (farmerInfoCursor.moveToNext()) {

                                        infoId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.VendorAccessInfo._ID));
                                        aes = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.VendorAccessInfo.COLUMN_AGRI_EXTENSION_SERV)) == 1;
                                        cri = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.VendorAccessInfo.COLUMN_CLIMATE_RELATED_INFO)) == 1;
                                        seeds = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.VendorAccessInfo.COLUMN_SEEDS)) == 1;
                                        orgFert = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.VendorAccessInfo.COLUMN_ORGANIC_FERTILIZER)) == 1;
                                        inorgFert = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.VendorAccessInfo.COLUMN_INORGANIC_FERTILIZER)) == 1;
                                        labour = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.VendorAccessInfo.COLUMN_LABOUR)) == 1;
                                        iwp = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.VendorAccessInfo.COLUMN_WATER_PUMPS)) == 1;
                                        ss = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.VendorAccessInfo.COLUMN_SPRAYERS)) == 1;

                                        infoServerId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.VendorAccessInfo.COLUMN_SERVER_ID));
                                        isSync = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.VendorAccessInfo.COLUMN_IS_SYNC));

                                        seasonId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.VendorAccessInfo.COLUMN_SEASON_ID));

                                        serverSeasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null,
                                                seasonSelection, new String[]{Long.toString(seasonId)}, null);

                                        if (serverSeasonCursor != null && serverSeasonCursor.moveToFirst()) {
                                            serverSeasonId = serverSeasonCursor.getInt(serverSeasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_SERVER_ID));

                                        }

                                        if (isSync == 1) {

                                            String accInFoData = "{" +
                                                    "\"ar_aes\": " + aes + "," +
                                                    "\"ar_cri\": " + cri + "," +
                                                    "\"ar_seeds\": " + seeds + "," +
                                                    "\"ar_of\": " + orgFert + "," +
                                                    "\"ar_if\": " + inorgFert + "," +
                                                    "\"ar_labour\": " + labour + "," +
                                                    "\"ar_iwp\": " + iwp + "," +
                                                    "\"ar_ss\": " + ss + "," +
                                                    "\"harvest_id\": " + serverSeasonId + "," +
                                                    "\"vendor_id\": " + farmerServerId + "" +
                                                    "}";

                                            String API_INFO = BuildConfig.DEV_API_URL + "access.info.vendor" + "/" + infoServerId;

                                            RequestBody bodyLand = RequestBody.create(JSON, accInFoData);

                                            Request requestLand = new Request.Builder()
                                                    .url(API_INFO)
                                                    .header("Content-Type", "text/html")
                                                    .header("Access-Token", appToken)
                                                    .method("PUT", bodyLand)
                                                    .build();

                                            Response responseLand = client.newCall(requestLand).execute();
                                            ResponseBody responseBodyLand = responseLand.body();

                                            if (responseBodyLand != null) {
                                                String bodyLandInfo = responseBodyLand.string();
                                                if (bodyLandInfo.equals("{}")) {

                                                    ContentValues infoContentValues = new ContentValues();
                                                    infoContentValues.put(BfwContract.VendorAccessInfo.COLUMN_IS_SYNC, 1);
                                                    infoContentValues.put(BfwContract.VendorAccessInfo.COLUMN_IS_UPDATE, 1);

                                                    getContentResolver().update(BfwContract.VendorAccessInfo.CONTENT_URI, infoContentValues, accessInfo,
                                                            new String[]{Long.toString(infoId)});
                                                }
                                            }

                                        } else {
                                            String accInFoData = "{" +
                                                    "\"ar_aes\": " + aes + "," +
                                                    "\"ar_cri\": " + cri + "," +
                                                    "\"ar_seeds\": " + seeds + "," +
                                                    "\"ar_of\": " + orgFert + "," +
                                                    "\"ar_if\": " + inorgFert + "," +
                                                    "\"ar_labour\": " + labour + "," +
                                                    "\"ar_iwp\": " + iwp + "," +
                                                    "\"ar_ss\": " + ss + "," +
                                                    "\"harvest_id\": " + serverSeasonId + "," +
                                                    "\"vendor_id\": " + farmerServerId + "" +
                                                    "}";

                                            String API_INFO = BuildConfig.DEV_API_URL + "access.info.vendor";

                                            RequestBody bodyLand = RequestBody.create(JSON, accInFoData);

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

                                                JSONObject bodyLandObject = new JSONObject(bodyLandInfo);
                                                if (bodyLandObject.has("id")) {
                                                    int serverId = bodyLandObject.getInt("id");

                                                    ContentValues infoContentValues = new ContentValues();
                                                    infoContentValues.put(BfwContract.VendorAccessInfo.COLUMN_SERVER_ID, serverId);
                                                    infoContentValues.put(BfwContract.VendorAccessInfo.COLUMN_IS_SYNC, 1);
                                                    infoContentValues.put(BfwContract.VendorAccessInfo.COLUMN_IS_UPDATE, 1);

                                                    getContentResolver().update(BfwContract.VendorAccessInfo.CONTENT_URI, infoContentValues, accessInfo,
                                                            new String[]{Long.toString(infoId)});
                                                }
                                            }
                                        }
                                    }
                                }

                                //update forecast farmer if available
                                farmerInfoCursor = getContentResolver().query(BfwContract.ForecastVendor.CONTENT_URI, null, forecastFarmerSelection, new String[]{Long.toString(id)}, null);
                                if (farmerInfoCursor != null) {

                                    int forecastId;
                                    int forecastServerId;

                                    Double totalArableLandPlots;
                                    Double farmerexpectedminppp;
                                    Double minimumflowprice;

                                    Double expectedProductionInMt = null;
                                    Double forecastedyieldmt = null;
                                    Double forecastedharvestsalevalue = null;
                                    Double totalcooplandsize = null;
                                    Double farmerpercentageland = null;
                                    Double currentpppcommitment = null;
                                    Double farmercontributionppp = null;

                                    int forecastSeasonId;


                                    while (farmerInfoCursor.moveToNext()) {

                                        forecastId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.ForecastVendor._ID));

                                        totalArableLandPlots = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.ForecastVendor.COLUMN_ARABLE_LAND_PLOT));
                                        farmerexpectedminppp = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.ForecastVendor.COLUMN_EXPECTED_MIN_PPP));
                                        minimumflowprice = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.ForecastVendor.COLUMN_FLOW_PRICE));

                                        forecastServerId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.ForecastVendor.COLUMN_SERVER_ID));
                                        isSync = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.ForecastVendor.COLUMN_IS_SYNC));

                                        forecastSeasonId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.ForecastVendor.COLUMN_SEASON_ID));

                                        serverSeasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null,
                                                seasonSelection, new String[]{Long.toString(forecastSeasonId)}, null);

                                        if (serverSeasonCursor != null && serverSeasonCursor.moveToFirst()) {
                                            serverSeasonId = serverSeasonCursor.getInt(serverSeasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_SERVER_ID));

                                        }

                                        if (isSync == 1) {
                                            String accInFoData = "{" +
                                                    "\"total_arable_land_plots\": " + totalArableLandPlots + "," +
                                                    "\"vendor_expected_min_ppp\": " + farmerexpectedminppp + "," +
                                                    "\"minimum_flow_price\": " + minimumflowprice + "," +
                                                    "\"harvest_id\": " + serverSeasonId + "," +
                                                    "\"vendor_id\": " + farmerServerId + "" +
                                                    "}";
                                            String API_INFO = BuildConfig.DEV_API_URL + "forecast.vendor" + "/" + forecastServerId;

                                            RequestBody bodyLand = RequestBody.create(JSON, accInFoData);

                                            Request requestLand = new Request.Builder()
                                                    .url(API_INFO)
                                                    .header("Content-Type", "text/html")
                                                    .header("Access-Token", appToken)
                                                    .method("PUT", bodyLand)
                                                    .build();

                                            Response responseLand = client.newCall(requestLand).execute();
                                            ResponseBody responseBodyLand = responseLand.body();

                                            if (responseBodyLand != null) {

                                                String bodyLandInfo = responseBodyLand.string();

                                                if (bodyLandInfo.equals("{}")) {
                                                    ContentValues forecastContentValues = new ContentValues();
                                                    forecastContentValues.put(BfwContract.ForecastVendor.COLUMN_IS_SYNC, 1);
                                                    forecastContentValues.put(BfwContract.ForecastVendor.COLUMN_IS_UPDATE, 1);

                                                    getContentResolver().update(BfwContract.ForecastVendor.CONTENT_URI, forecastContentValues, forecastFarmerInfo,
                                                            new String[]{Long.toString(forecastId)});
                                                }
                                            }
                                        } else {


                                            // get all the forecast for a given farmer and check if there's one that match with serverSeasonId
                                            String proxyUrl = BuildConfig.DEV_PROXY_URL + "?model=forecast.vendor&attr=vendor_id&value=" + farmerServerId + "&token=" + appToken;
                                            Request requestLand = new Request.Builder()
                                                    .url(proxyUrl)
                                                    .header("Content-Type", "text/html")
                                                    .header("Access-Token", appToken)
                                                    .get()
                                                    .build();

                                            Response responseLand = client.newCall(requestLand).execute();
                                            ResponseBody responseBodyLand = responseLand.body();
                                            if (responseBodyLand != null) {
                                                String forecastsList = responseBodyLand.string();
                                                JSONObject farmersJsonObject = new JSONObject(forecastsList);


                                                String filterServerResponse = farmersJsonObject.getString("response");
                                                JSONObject filterServerObject = new JSONObject(filterServerResponse);
                                                JSONArray forecastArrayLists = filterServerObject.getJSONArray("results");
                                                JSONObject forecastObject;
                                                int availableSeasonId = 0;

                                                if (forecastArrayLists.length() > 0) {
                                                    for (int f = 0; f < forecastArrayLists.length(); f++) {

                                                        forecastObject = forecastArrayLists.getJSONObject(f);

                                                        int forecastServerNewId = forecastObject.getInt("id");
                                                        if (forecastObject.has("harvest_id")) {
                                                            if (!forecastObject.getString("harvest_id").equals("null")) {
                                                                availableSeasonId = forecastObject.getInt("harvest_id");
                                                            }
                                                        }

                                                        if (serverSeasonId == availableSeasonId) {

                                                            // there's forecast for a given farmer on season X

                                                            if (forecastObject.has("expected_production_in_mt")) {
                                                                if (!forecastObject.getString("expected_production_in_mt").equals("null")) {
                                                                    expectedProductionInMt = forecastObject.getDouble("expected_production_in_mt");
                                                                }
                                                            }

                                                            if (forecastObject.has("forecasted_yield_mt")) {
                                                                if (!forecastObject.getString("forecasted_yield_mt").equals("null")) {
                                                                    forecastedyieldmt = forecastObject.getDouble("forecasted_yield_mt");
                                                                }
                                                            }
                                                            if (forecastObject.has("forecasted_harvest_sale_value")) {
                                                                if (!forecastObject.getString("forecasted_harvest_sale_value").equals("null")) {
                                                                    forecastedharvestsalevalue = forecastObject.getDouble("forecasted_harvest_sale_value");
                                                                }
                                                            }
                                                            if (forecastObject.has("total_coop_land_size")) {
                                                                if (!forecastObject.getString("total_coop_land_size").equals("null")) {
                                                                    totalcooplandsize = forecastObject.getDouble("total_coop_land_size");
                                                                }
                                                            }
                                                            if (forecastObject.has("vendor_percentage_land")) {
                                                                if (!forecastObject.getString("vendor_percentage_land").equals("null")) {
                                                                    farmerpercentageland = forecastObject.getDouble("vendor_percentage_land");
                                                                }
                                                            }
                                                            if (forecastObject.has("current_ppp_commitment")) {
                                                                if (!forecastObject.getString("current_ppp_commitment").equals("null")) {
                                                                    currentpppcommitment = forecastObject.getDouble("current_ppp_commitment");
                                                                }
                                                            }

                                                            if (forecastObject.has("vendor_contribution_ppp")) {
                                                                if (!forecastObject.getString("vendor_contribution_ppp").equals("null")) {
                                                                    farmercontributionppp = forecastObject.getDouble("vendor_contribution_ppp");
                                                                }
                                                            }

                                                            String accInFoData = "{" +
                                                                    "\"total_arable_land_plots\": " + totalArableLandPlots + "," +
                                                                    "\"vendor_expected_min_ppp\": " + farmerexpectedminppp + "," +
                                                                    "\"minimum_flow_price\": " + minimumflowprice + "," +
                                                                    "\"harvest_id\": " + serverSeasonId + "," +
                                                                    "\"vendor_id\": " + farmerServerId + "" +
                                                                    "}";
                                                            String API_INFO = BuildConfig.DEV_API_URL + "forecast.vendor" + "/" + forecastServerId;

                                                            RequestBody bodyLand = RequestBody.create(JSON, accInFoData);

                                                            requestLand = new Request.Builder()
                                                                    .url(API_INFO)
                                                                    .header("Content-Type", "text/html")
                                                                    .header("Access-Token", appToken)
                                                                    .method("PUT", bodyLand)
                                                                    .build();

                                                            responseLand = client.newCall(requestLand).execute();
                                                            responseBodyLand = responseLand.body();

                                                            if (responseBodyLand != null) {

                                                                String bodyLandInfo = responseBodyLand.string();
                                                                if (bodyLandInfo.equals("{}")) {

                                                                    ContentValues forecastValues = new ContentValues();
                                                                    forecastValues.put(BfwContract.ForecastVendor.COLUMN_SERVER_ID, forecastServerNewId);
                                                                    forecastValues.put(BfwContract.ForecastVendor.COLUMN_PRODUCTION_MT, expectedProductionInMt);
                                                                    forecastValues.put(BfwContract.ForecastVendor.COLUMN_YIELD_MT, forecastedyieldmt);
                                                                    forecastValues.put(BfwContract.ForecastVendor.COLUMN_HARVEST_SALE_VALUE, forecastedharvestsalevalue);
                                                                    forecastValues.put(BfwContract.ForecastVendor.COLUMN_COOP_LAND_SIZE, totalcooplandsize);
                                                                    forecastValues.put(BfwContract.ForecastVendor.COLUMN_PERCENT_FARMER_LAND_SIZE, farmerpercentageland);
                                                                    forecastValues.put(BfwContract.ForecastVendor.COLUMN_PPP_COMMITMENT, currentpppcommitment);
                                                                    forecastValues.put(BfwContract.ForecastVendor.COLUMN_CONTRIBUTION_PPP, farmercontributionppp);
                                                                    forecastValues.put(BfwContract.ForecastVendor.COLUMN_IS_SYNC, 1);
                                                                    forecastValues.put(BfwContract.ForecastVendor.COLUMN_IS_UPDATE, 1);

                                                                    getContentResolver().update(BfwContract.ForecastVendor.CONTENT_URI, forecastValues, forecastFarmerInfo,
                                                                            new String[]{Long.toString(forecastId)});
                                                                }
                                                            }


                                                        } else {
                                                            // there's no forecast for a given farmer on season X
                                                            String accInFoData = "{" +
                                                                    "\"total_arable_land_plots\": " + totalArableLandPlots + "," +
                                                                    "\"vendor_expected_min_ppp\": " + farmerexpectedminppp + "," +
                                                                    "\"minimum_flow_price\": " + minimumflowprice + "," +
                                                                    "\"harvest_id\": " + serverSeasonId + "," +
                                                                    "\"vendor_id\": " + farmerServerId + "" +
                                                                    "}";
                                                            String API_INFO = BuildConfig.DEV_API_URL + "forecast.vendor";

                                                            RequestBody bodyLand = RequestBody.create(JSON, accInFoData);

                                                            requestLand = new Request.Builder()
                                                                    .url(API_INFO)
                                                                    .header("Content-Type", "text/html")
                                                                    .header("Access-Token", appToken)
                                                                    .method("POST", bodyLand)
                                                                    .build();

                                                            responseLand = client.newCall(requestLand).execute();
                                                            responseBodyLand = responseLand.body();

                                                            if (responseBodyLand != null) {

                                                                String bodyLandInfo = responseBodyLand.string();

                                                                JSONObject bodyLandObject = new JSONObject(bodyLandInfo);
                                                                if (bodyLandObject.has("id")) {
                                                                    int infoServerId = bodyLandObject.getInt("id");

                                                                    expectedProductionInMt = bodyLandObject.getDouble("expected_production_in_mt");
                                                                    forecastedyieldmt = bodyLandObject.getDouble("forecasted_yield_mt");
                                                                    forecastedharvestsalevalue = bodyLandObject.getDouble("forecasted_harvest_sale_value");
                                                                    totalcooplandsize = bodyLandObject.getDouble("total_coop_land_size");
                                                                    farmerpercentageland = bodyLandObject.getDouble("vendor_percentage_land");
                                                                    currentpppcommitment = bodyLandObject.getDouble("current_ppp_commitment");
                                                                    farmercontributionppp = bodyLandObject.getDouble("vendor_contribution_ppp");

                                                                    ContentValues forecastValues = new ContentValues();
                                                                    forecastValues.put(BfwContract.ForecastVendor.COLUMN_SERVER_ID, infoServerId);
                                                                    forecastValues.put(BfwContract.ForecastVendor.COLUMN_PRODUCTION_MT, expectedProductionInMt);
                                                                    forecastValues.put(BfwContract.ForecastVendor.COLUMN_YIELD_MT, forecastedyieldmt);
                                                                    forecastValues.put(BfwContract.ForecastVendor.COLUMN_HARVEST_SALE_VALUE, forecastedharvestsalevalue);
                                                                    forecastValues.put(BfwContract.ForecastVendor.COLUMN_COOP_LAND_SIZE, totalcooplandsize);
                                                                    forecastValues.put(BfwContract.ForecastVendor.COLUMN_PERCENT_FARMER_LAND_SIZE, farmerpercentageland);
                                                                    forecastValues.put(BfwContract.ForecastVendor.COLUMN_PPP_COMMITMENT, currentpppcommitment);
                                                                    forecastValues.put(BfwContract.ForecastVendor.COLUMN_CONTRIBUTION_PPP, farmercontributionppp);
                                                                    forecastValues.put(BfwContract.ForecastVendor.COLUMN_IS_SYNC, 1);
                                                                    forecastValues.put(BfwContract.ForecastVendor.COLUMN_IS_UPDATE, 1);

                                                                    getContentResolver().update(BfwContract.ForecastVendor.CONTENT_URI, forecastValues, forecastFarmerInfo,
                                                                            new String[]{Long.toString(forecastId)});
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    // there's no forecast for a given farmer
                                                    String accInFoData = "{" +
                                                            "\"total_arable_land_plots\": " + totalArableLandPlots + "," +
                                                            "\"vendor_expected_min_ppp\": " + farmerexpectedminppp + "," +
                                                            "\"minimum_flow_price\": " + minimumflowprice + "," +
                                                            "\"harvest_id\": " + serverSeasonId + "," +
                                                            "\"vendor_id\": " + farmerServerId + "" +
                                                            "}";
                                                    String API_INFO = BuildConfig.DEV_API_URL + "forecast.vendor";

                                                    RequestBody bodyLand = RequestBody.create(JSON, accInFoData);

                                                    requestLand = new Request.Builder()
                                                            .url(API_INFO)
                                                            .header("Content-Type", "text/html")
                                                            .header("Access-Token", appToken)
                                                            .method("POST", bodyLand)
                                                            .build();

                                                    responseLand = client.newCall(requestLand).execute();
                                                    responseBodyLand = responseLand.body();

                                                    if (responseBodyLand != null) {

                                                        String bodyLandInfo = responseBodyLand.string();

                                                        JSONObject bodyLandObject = new JSONObject(bodyLandInfo);
                                                        if (bodyLandObject.has("id")) {
                                                            int infoServerId = bodyLandObject.getInt("id");

                                                            expectedProductionInMt = bodyLandObject.getDouble("expected_production_in_mt");
                                                            forecastedyieldmt = bodyLandObject.getDouble("forecasted_yield_mt");
                                                            forecastedharvestsalevalue = bodyLandObject.getDouble("forecasted_harvest_sale_value");
                                                            totalcooplandsize = bodyLandObject.getDouble("total_coop_land_size");
                                                            farmerpercentageland = bodyLandObject.getDouble("vendor_percentage_land");
                                                            currentpppcommitment = bodyLandObject.getDouble("current_ppp_commitment");
                                                            farmercontributionppp = bodyLandObject.getDouble("vendor_contribution_ppp");

                                                            ContentValues forecastValues = new ContentValues();
                                                            forecastValues.put(BfwContract.ForecastVendor.COLUMN_SERVER_ID, infoServerId);
                                                            forecastValues.put(BfwContract.ForecastVendor.COLUMN_PRODUCTION_MT, expectedProductionInMt);
                                                            forecastValues.put(BfwContract.ForecastVendor.COLUMN_YIELD_MT, forecastedyieldmt);
                                                            forecastValues.put(BfwContract.ForecastVendor.COLUMN_HARVEST_SALE_VALUE, forecastedharvestsalevalue);
                                                            forecastValues.put(BfwContract.ForecastVendor.COLUMN_COOP_LAND_SIZE, totalcooplandsize);
                                                            forecastValues.put(BfwContract.ForecastVendor.COLUMN_PERCENT_FARMER_LAND_SIZE, farmerpercentageland);
                                                            forecastValues.put(BfwContract.ForecastVendor.COLUMN_PPP_COMMITMENT, currentpppcommitment);
                                                            forecastValues.put(BfwContract.ForecastVendor.COLUMN_CONTRIBUTION_PPP, farmercontributionppp);
                                                            forecastValues.put(BfwContract.ForecastVendor.COLUMN_IS_SYNC, 1);
                                                            forecastValues.put(BfwContract.ForecastVendor.COLUMN_IS_UPDATE, 1);

                                                            getContentResolver().update(BfwContract.ForecastVendor.CONTENT_URI, forecastValues, forecastFarmerInfo,
                                                                    new String[]{Long.toString(forecastId)});
                                                        }
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }

                                //update finance data if available
                                farmerInfoCursor = getContentResolver().query(BfwContract.FinanceDataVendor.CONTENT_URI, null, financeDataSelection, new String[]{Long.toString(id)}, null);
                                if (farmerInfoCursor != null) {

                                    int financeId;
                                    int financeDataServerId;
                                    Boolean outstandingloan;
                                    Boolean loanPurposeI;
                                    Boolean loanPurposeA;
                                    Boolean loanPurposeO;
                                    Boolean mobileMoneyAccount;

                                    Double totalLoanAmount;
                                    Double totaloutstanding;
                                    Double interestrate;
                                    Integer duration;
                                    String loanProvider;
                                    int financeSeasonId;

                                    while (farmerInfoCursor.moveToNext()) {
                                        financeId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor._ID));

                                        outstandingloan = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_OUTSANDING_LOAN)) == 1;
                                        loanPurposeI = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_LOANPROVIDER_INPUT)) == 1;
                                        loanPurposeA = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_LOANPROVIDER_AGGREG)) == 1;
                                        loanPurposeO = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_LOANPROVIDER_OTHER)) == 1;
                                        mobileMoneyAccount = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_MOBILE_MONEY_ACCOUNT)) == 1;

                                        totalLoanAmount = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_TOT_LOAN_AMOUNT));
                                        totaloutstanding = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_TOT_OUTSTANDING));
                                        interestrate = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_INTEREST_RATE));
                                        duration = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_DURATION));
                                        loanProvider = farmerInfoCursor.getString(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_LOAN_PROVIDER));

                                        isSync = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_IS_SYNC));
                                        financeDataServerId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_SERVER_ID));

                                        financeSeasonId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_SEASON_ID));

                                        serverSeasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null,
                                                seasonSelection, new String[]{Long.toString(financeSeasonId)}, null);

                                        if (serverSeasonCursor != null && serverSeasonCursor.moveToFirst()) {
                                            serverSeasonId = serverSeasonCursor.getInt(serverSeasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_SERVER_ID));
                                        }

                                        if (isSync == 1) {
                                            String accInFoData = "{" +
                                                    "\"outstanding_loan\": " + outstandingloan + "," +
                                                    "\"total_loan_amount\": " + totalLoanAmount + "," +
                                                    "\"total_outstanding\": " + totaloutstanding + "," +
                                                    "\"interest_rate\": " + interestrate + "," +
                                                    "\"duration\": " + duration + "," +
                                                    "\"loan_provider\": \"" + loanProvider + "\"," +
                                                    "\"loan_purpose_i\": " + loanPurposeA + "," +
                                                    "\"loan_purpose_a\": " + loanPurposeI + "," +
                                                    "\"loan_purpose_o\": " + loanPurposeO + "," +
                                                    "\"mobile_money_account\": " + mobileMoneyAccount + "," +
                                                    "\"harvest_id\": " + serverSeasonId + "," +
                                                    "\"vendor_id\": " + farmerServerId + "" +
                                                    "}";

                                            String API_INFO = BuildConfig.DEV_API_URL + "finance.data.vendor" + "/" + financeDataServerId;

                                            RequestBody bodyLand = RequestBody.create(JSON, accInFoData);

                                            Request requestLand = new Request.Builder()
                                                    .url(API_INFO)
                                                    .header("Content-Type", "text/html")
                                                    .header("Access-Token", appToken)
                                                    .method("PUT", bodyLand)
                                                    .build();

                                            Response responseLand = client.newCall(requestLand).execute();
                                            ResponseBody responseBodyLand = responseLand.body();

                                            if (responseBodyLand != null) {

                                                String bodyLandInfo = responseBodyLand.string();
                                                if (bodyLandInfo.equals("{}")) {

                                                    ContentValues financeDataValues = new ContentValues();
                                                    financeDataValues.put(BfwContract.FinanceDataVendor.COLUMN_IS_SYNC, 1);
                                                    financeDataValues.put(BfwContract.FinanceDataVendor.COLUMN_IS_UPDATE, 1);

                                                    getContentResolver().update(BfwContract.FinanceDataVendor.CONTENT_URI, financeDataValues, financeDataInfo,
                                                            new String[]{Long.toString(financeId)});
                                                }
                                            }

                                        } else {
                                            String accInFoData = "{" +
                                                    "\"outstanding_loan\": " + outstandingloan + "," +
                                                    "\"total_loan_amount\": " + totalLoanAmount + "," +
                                                    "\"total_outstanding\": " + totaloutstanding + "," +
                                                    "\"interest_rate\": " + interestrate + "," +
                                                    "\"duration\": " + duration + "," +
                                                    "\"loan_provider\": \"" + loanProvider + "\"," +
                                                    "\"loan_purpose_i\": " + loanPurposeA + "," +
                                                    "\"loan_purpose_a\": " + loanPurposeI + "," +
                                                    "\"loan_purpose_o\": " + loanPurposeO + "," +
                                                    "\"mobile_money_account\": " + mobileMoneyAccount + "," +
                                                    "\"harvest_id\": " + serverSeasonId + "," +
                                                    "\"vendor_id\": " + farmerServerId + "" +
                                                    "}";

                                            String API_INFO = BuildConfig.DEV_API_URL + "finance.data.vendor";

                                            RequestBody bodyLand = RequestBody.create(JSON, accInFoData);

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

                                                JSONObject bodyLandObject = new JSONObject(bodyLandInfo);
                                                if (bodyLandObject.has("id")) {
                                                    int infoServerId = bodyLandObject.getInt("id");

                                                    ContentValues financeDataValues = new ContentValues();
                                                    financeDataValues.put(BfwContract.FinanceDataVendor.COLUMN_SERVER_ID, infoServerId);
                                                    financeDataValues.put(BfwContract.FinanceDataVendor.COLUMN_IS_SYNC, 1);
                                                    financeDataValues.put(BfwContract.FinanceDataVendor.COLUMN_IS_UPDATE, 1);

                                                    getContentResolver().update(BfwContract.FinanceDataVendor.CONTENT_URI, financeDataValues, financeDataInfo,
                                                            new String[]{Long.toString(financeId)});
                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    } catch (IOException | JSONException exp) {
                        EventBus.getDefault().post(new SyncDataEvent(getResources().getString(R.string.syncing_error_vendor), false));
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (farmerInfoCursor != null) {
                farmerInfoCursor.close();
            }
            if (serverSeasonCursor != null) {
                serverSeasonCursor.close();
            }
        }

        //post event sync after
        if (dataCount > 0)
            EventBus.getDefault().post(new SyncDataEvent(getResources().getString(R.string.update_msg_vendor), true));
    }
}

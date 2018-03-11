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
import com.nijus.alino.bfwcoopmanagement.events.ProcessingCoopEvent;
import com.nijus.alino.bfwcoopmanagement.events.SyncDataEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

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

        String API = BuildConfig.DEV_API_URL + "farmer";

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(240, TimeUnit.SECONDS)
                .writeTimeout(240, TimeUnit.SECONDS)
                .readTimeout(240, TimeUnit.SECONDS)
                .build();

        //get non sync farmer to the server
        int dataCount = 0;
        int coopServerId = 0;
        int seasonId, serverSeasonId = 1;
        int farmerServerId;
        long id;
        Cursor cursor = null, farmerInfoCursor = null, cursorFarmerServerId = null, serverSeasonCursor = null;

        String seasonSelection = BfwContract.HarvestSeason.TABLE_NAME + "." +
                BfwContract.HarvestSeason._ID + " =  ? ";

        String selection = BfwContract.Farmer.TABLE_NAME + "." +
                BfwContract.Farmer.COLUMN_IS_SYNC + " =  0 ";

        String farmerSelection = BfwContract.Farmer.TABLE_NAME + "." +
                BfwContract.Farmer._ID + " =  ? ";

        String farmerCoopServerId = BfwContract.Coops.TABLE_NAME + "." +
                BfwContract.Coops._ID + " =  ? ";

        String landSelection = BfwContract.LandPlot.TABLE_NAME + "." +
                BfwContract.LandPlot.COLUMN_FARMER_ID + " = ?  AND " + BfwContract.LandPlot.COLUMN_IS_SYNC + " = 0 ";

        String landInfo = BfwContract.LandPlot.TABLE_NAME + "." +
                BfwContract.LandPlot._ID + " = ? ";

        String accessInfoSelection = BfwContract.FarmerAccessInfo.TABLE_NAME + "." +
                BfwContract.FarmerAccessInfo.COLUMN_FARMER_ID + " = ? AND " + BfwContract.FarmerAccessInfo.COLUMN_IS_SYNC + " = 0 ";

        String accessInfo = BfwContract.FarmerAccessInfo.TABLE_NAME + "." +
                BfwContract.FarmerAccessInfo._ID + " = ? ";

        String baselineFarmerSelection = BfwContract.BaselineFarmer.TABLE_NAME + "." +
                BfwContract.BaselineFarmer.COLUMN_FARMER_ID + " = ? AND " + BfwContract.BaselineFarmer.COLUMN_IS_SYNC + " = 0 ";

        String baselineFarmerInfo = BfwContract.BaselineFarmer.TABLE_NAME + "." +
                BfwContract.BaselineFarmer._ID + " = ? ";

        String forecastFarmerSelection = BfwContract.ForecastFarmer.TABLE_NAME + "." +
                BfwContract.ForecastFarmer.COLUMN_FARMER_ID + " = ? AND " + BfwContract.ForecastFarmer.COLUMN_IS_SYNC + " = 0 ";

        String forecastFarmerInfo = BfwContract.ForecastFarmer.TABLE_NAME + "." +
                BfwContract.ForecastFarmer._ID + " = ? ";

        String financeDataSelection = BfwContract.FinanceDataFarmer.TABLE_NAME + "." +
                BfwContract.FinanceDataFarmer.COLUMN_FARMER_ID + " = ? AND " + BfwContract.FinanceDataFarmer.COLUMN_IS_SYNC + " = 0 ";

        String financeDataInfo = BfwContract.FinanceDataFarmer.TABLE_NAME + "." +
                BfwContract.FinanceDataFarmer._ID + " = ? ";

        try {
            cursor = getContentResolver().query(BfwContract.Farmer.CONTENT_URI, null, selection, null, null);
            if (cursor != null) {
                dataCount = cursor.getCount();

                while (cursor.moveToNext()) {

                    id = cursor.getLong(cursor.getColumnIndex(BfwContract.Farmer._ID));

                    farmerServerId = 0;

                    String name = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_NAME));
                    String phoneNumber = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_PHONE));
                    String gender = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_GENDER));
                    String address = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_ADDRESS));

                    Boolean houseHold = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_HOUSEHOLD_HEAD)) == 1;
                    Integer houseMember = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_HOUSE_MEMBER));
                    String sFirstName = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_FIRST_NAME));
                    String sLastName = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_LAST_NAME));
                    String cellPhone = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_CELL_PHONE));
                    String cellCarrier = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_CELL_CARRIER));

                    Boolean tractors = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_TRACTORS)) == 1;
                    Boolean harvester = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_HARVESTER)) == 1;
                    Boolean dryer = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_DRYER)) == 1;
                    Boolean tresher = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_TRESHER)) == 1;
                    Boolean safeStorage = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_SAFE_STORAGE)) == 1;
                    Boolean otherInfo = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_OTHER_INFO)) == 1;

                    Boolean isDam = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_DAM)) == 1;
                    Boolean isWell = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_WELL)) == 1;
                    Boolean isBorehole = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_BOREHOLE)) == 1;
                    Boolean isPipeBorne = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_PIPE_BORNE)) == 1;
                    Boolean isRiverStream = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_RIVER_STREAM)) == 1;
                    Boolean isIrrigation = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_IRRIGATION)) == 1;
                    Boolean isNone = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_NONE)) == 1;
                    Boolean isOtherSource = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_OTHER)) == 1;

                    String storageDetails = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_STORAGE_DETAIL));
                    String newResourcesDetails = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_NEW_SOURCE_DETAIL));
                    String waterSourceDetails = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_WATER_SOURCE_DETAILS));

                    int coopUserId = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_COOP_USER_ID));

                    cursorFarmerServerId = getContentResolver().query(BfwContract.Coops.CONTENT_URI, null,
                            farmerCoopServerId, new String[]{Integer.toString(coopUserId)}, null);

                    if (cursorFarmerServerId != null) {
                        while (cursorFarmerServerId.moveToNext()) {

                            coopServerId = cursorFarmerServerId.getInt(cursorFarmerServerId.getColumnIndex(BfwContract.Coops.COLUMN_COOP_SERVER_ID));

                        }
                    }
                    EventBus.getDefault().post(new ProcessingCoopEvent("Processing your request ..."));

                    String bodyInfo = "{" +
                            "\"name\" : \"" + name + "\"," +
                            "\"cell_phone\" : \"" + phoneNumber + "\"," +
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
                            "\"mws_other\" : " + isOtherSource + "," +
                            "\"coop_id\" : " + coopServerId + "" +
                            "}";

                    RequestBody body = RequestBody.create(JSON, bodyInfo);

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
                                String membershipId = farmerInfo.getString("membership_id");

                                //set membership id
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(BfwContract.Farmer.COLUMN_FARMER_SERVER_ID, farmerServerId);
                                contentValues.put(BfwContract.Farmer.COLUMN_MEMBER_SHIP, membershipId);
                                contentValues.put(BfwContract.Farmer.COLUMN_IS_SYNC, 1);
                                contentValues.put(BfwContract.Farmer.COLUMN_IS_UPDATE, 1);
                                getContentResolver().update(BfwContract.Farmer.CONTENT_URI, contentValues, farmerSelection, new String[]{Long.toString(id)});
                            }
                        }

                        //sync access to information if available
                        farmerInfoCursor = getContentResolver().query(BfwContract.FarmerAccessInfo.CONTENT_URI, null, accessInfoSelection,
                                new String[]{Long.toString(id)}, null);

                        if (farmerInfoCursor != null) {
                            int infoId;

                            Boolean aes;
                            Boolean cri;
                            Boolean seeds;
                            Boolean orgFert;
                            Boolean inorgFert;
                            Boolean labour;
                            Boolean iwp;
                            Boolean ss;

                            while (farmerInfoCursor.moveToNext()) {

                                infoId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FarmerAccessInfo._ID));
                                aes = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_AGRI_EXTENSION_SERV)) == 1;
                                cri = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_CLIMATE_RELATED_INFO)) == 1;
                                seeds = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_SEEDS)) == 1;
                                orgFert = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_ORGANIC_FERTILIZER)) == 1;
                                inorgFert = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_INORGANIC_FERTILIZER)) == 1;
                                labour = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_LABOUR)) == 1;
                                iwp = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_WATER_PUMPS)) == 1;
                                ss = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_SPRAYERS)) == 1;

                                seasonId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_SEASON_ID));

                                serverSeasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null,
                                        seasonSelection, new String[]{Long.toString(seasonId)}, null);

                                if (serverSeasonCursor != null) {
                                    while (serverSeasonCursor.moveToNext()) {
                                        serverSeasonId = serverSeasonCursor.getInt(serverSeasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_SERVER_ID));
                                    }
                                }

                                if (farmerServerId > 0) {

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
                                            "\"farmer_id\": " + farmerServerId + "" +
                                            "}";

                                    String API_INFO = BuildConfig.DEV_API_URL + "access.info.farmer";

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

                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put(BfwContract.FarmerAccessInfo.COLUMN_SERVER_ID, infoServerId);
                                            contentValues.put(BfwContract.FarmerAccessInfo.COLUMN_IS_SYNC, 1);
                                            contentValues.put(BfwContract.FarmerAccessInfo.COLUMN_IS_UPDATE, 1);

                                            getContentResolver().update(BfwContract.FarmerAccessInfo.CONTENT_URI, contentValues, accessInfo,
                                                    new String[]{Long.toString(infoId)});
                                        }
                                    }
                                }
                            }
                        }

                        //sync baseline information
                        farmerInfoCursor = getContentResolver().query(BfwContract.BaselineFarmer.CONTENT_URI, null, baselineFarmerSelection,
                                new String[]{Long.toString(id)}, null);

                        if (farmerInfoCursor != null) {

                            int baselineId;
                            Double seasonharvest;
                            Double lostharvesttotal;
                            Double soldharvesttotal;
                            Double totalqtycoops;
                            Double pricesoldcoops;
                            Double totalqtymiddlemen;
                            Double pricesoldmiddlemen;
                            int baselineSeasonId;

                            while (farmerInfoCursor.moveToNext()) {

                                baselineId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.BaselineFarmer._ID));

                                seasonharvest = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_TOT_PROD_B_KG));
                                lostharvesttotal = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_TOT_LOST_KG));
                                soldharvesttotal = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_TOT_SOLD_KG));
                                totalqtycoops = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_TOT_VOL_SOLD_COOP));
                                pricesoldcoops = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_PRICE_SOLD_COOP_PER_KG));
                                totalqtymiddlemen = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_TOT_VOL_SOLD_IN_KG));
                                pricesoldmiddlemen = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_PRICE_SOLD_KG));

                                baselineSeasonId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_SEASON_ID));

                                serverSeasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null,
                                        seasonSelection, new String[]{Long.toString(baselineSeasonId)}, null);

                                if (serverSeasonCursor != null) {
                                    while (serverSeasonCursor.moveToNext()) {
                                        serverSeasonId = serverSeasonCursor.getInt(serverSeasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_SERVER_ID));
                                    }
                                }

                                if (farmerServerId > 0) {

                                    String accInFoData = "{" +
                                            "\"seasona_harvest\": " + seasonharvest + "," +
                                            "\"lost_harvest_total\": " + lostharvesttotal + "," +
                                            "\"sold_harvest_total\": " + soldharvesttotal + "," +
                                            "\"total_qty_coops\": " + totalqtycoops + "," +
                                            "\"price_sold_coops\": " + pricesoldcoops + "," +
                                            "\"total_qty_middlemen\": " + totalqtymiddlemen + "," +
                                            "\"price_sold_middlemen\": " + pricesoldmiddlemen + "," +
                                            "\"harvest_id\": " + serverSeasonId + "," +
                                            "\"farmer_id\": " + farmerServerId + "" +
                                            "}";

                                    String API_INFO = BuildConfig.DEV_API_URL + "baseline.farmer";

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

                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put(BfwContract.BaselineFarmer.COLUMN_SERVER_ID, infoServerId);
                                            contentValues.put(BfwContract.BaselineFarmer.COLUMN_IS_SYNC, 1);
                                            contentValues.put(BfwContract.BaselineFarmer.COLUMN_IS_UPDATE, 1);

                                            getContentResolver().update(BfwContract.BaselineFarmer.CONTENT_URI, contentValues, baselineFarmerInfo,
                                                    new String[]{Long.toString(baselineId)});
                                        }
                                    }
                                }
                            }
                        }

                        //sync finance data information
                        farmerInfoCursor = getContentResolver().query(BfwContract.FinanceDataFarmer.CONTENT_URI, null, financeDataSelection,
                                new String[]{Long.toString(id)}, null);

                        if (farmerInfoCursor != null) {

                            int financeId;
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

                                financeId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataFarmer._ID));

                                outstandingloan = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_OUTSANDING_LOAN)) == 1;
                                loanPurposeI = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_LOANPROVIDER_INPUT)) == 1;
                                loanPurposeA = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_LOANPROVIDER_AGGREG)) == 1;
                                loanPurposeO = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_LOANPROVIDER_OTHER)) == 1;
                                mobileMoneyAccount = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_MOBILE_MONEY_ACCOUNT)) == 1;

                                totalLoanAmount = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_TOT_LOAN_AMOUNT));
                                totaloutstanding = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_TOT_OUTSTANDING));
                                interestrate = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_INTEREST_RATE));
                                duration = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_DURATION));
                                loanProvider = farmerInfoCursor.getString(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_LOAN_PROVIDER));

                                financeSeasonId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_SEASON_ID));

                                serverSeasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null,
                                        seasonSelection, new String[]{Long.toString(financeSeasonId)}, null);

                                if (serverSeasonCursor != null) {
                                    while (serverSeasonCursor.moveToNext()) {
                                        serverSeasonId = serverSeasonCursor.getInt(serverSeasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_SERVER_ID));
                                    }
                                }

                                if (farmerServerId > 0) {

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
                                            "\"farmer_id\": " + farmerServerId + "" +
                                            "}";

                                    String API_INFO = BuildConfig.DEV_API_URL + "finance.data.farmer";

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

                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put(BfwContract.FinanceDataFarmer.COLUMN_SERVER_ID, infoServerId);
                                            contentValues.put(BfwContract.FinanceDataFarmer.COLUMN_IS_SYNC, 1);
                                            contentValues.put(BfwContract.FinanceDataFarmer.COLUMN_IS_UPDATE, 1);

                                            getContentResolver().update(BfwContract.FinanceDataFarmer.CONTENT_URI, contentValues, financeDataInfo,
                                                    new String[]{Long.toString(financeId)});
                                        }
                                    }
                                }
                            }
                        }

                        //sync land information if available
                        farmerInfoCursor = getContentResolver().query(BfwContract.LandPlot.CONTENT_URI, null, landSelection,
                                new String[]{Long.toString(id)}, null);

                        if (farmerInfoCursor != null) {
                            long landId;
                            while (farmerInfoCursor.moveToNext()) {

                                landId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.LandPlot._ID));
                                double plotSize = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.LandPlot.COLUMN_PLOT_SIZE));
                                double lat = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.LandPlot.COLUMN_LAT_INFO));
                                double lng = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.LandPlot.COLUMN_LNG_INFO));

                                seasonId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.LandPlot.COLUMN_SEASON_ID));

                                serverSeasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null,
                                        seasonSelection, new String[]{Long.toString(seasonId)}, null);

                                if (serverSeasonCursor != null) {
                                    while (serverSeasonCursor.moveToNext()) {
                                        serverSeasonId = serverSeasonCursor.getInt(serverSeasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_SERVER_ID));
                                    }
                                }

                                if (farmerServerId != 0) {
                                    String plotInfo = "{" +
                                            "\"plot_size\": " + plotSize + "," +
                                            "\"lat\": " + lat + "," +
                                            "\"lng\": " + lng + "," +
                                            "\"harvest_id\": " + serverSeasonId + "," +
                                            "\"farmer_id\": " + farmerServerId + "" +
                                            "}";

                                    String API_INFO = BuildConfig.DEV_API_URL + "farmer.land";

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

                        //sync forecast information
                        farmerInfoCursor = getContentResolver().query(BfwContract.ForecastFarmer.CONTENT_URI, null, forecastFarmerSelection,
                                new String[]{Long.toString(id)}, null);

                        if (farmerInfoCursor != null) {

                            int forecastId;

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

                                forecastId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.ForecastFarmer._ID));

                                totalArableLandPlots = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_ARABLE_LAND_PLOT));
                                farmerexpectedminppp = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_EXPECTED_MIN_PPP));
                                minimumflowprice = farmerInfoCursor.getDouble(farmerInfoCursor.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_FLOW_PRICE));

                                forecastSeasonId = farmerInfoCursor.getInt(farmerInfoCursor.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_SEASON_ID));

                                serverSeasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null,
                                        seasonSelection, new String[]{Long.toString(forecastSeasonId)}, null);

                                if (serverSeasonCursor != null) {
                                    while (serverSeasonCursor.moveToNext()) {
                                        serverSeasonId = serverSeasonCursor.getInt(serverSeasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_SERVER_ID));
                                    }
                                }

                                if (farmerServerId > 0) {

                                    // get all the forecast for a given farmer and check if there's one that match with serverSeasonId
                                    String proxyUrl = BuildConfig.DEV_PROXY_URL + "?model=forecast.farmer&attr=farmer_id&value=" + farmerServerId + "&token=" + appToken;
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

                                        String resultsResponse = filterServerObject.getString("results");
                                        Object json = new JSONTokener(resultsResponse).nextValue();

                                        boolean isResponse = false;

                                        if(json instanceof JSONArray){
                                            isResponse = true;
                                        }

                                        JSONObject forecastObject;
                                        int availableSeasonId = 0;
                                        if (isResponse) {
                                            JSONArray forecastArrayLists = filterServerObject.getJSONArray("results");
                                            for (int f = 0; f < forecastArrayLists.length(); f++) {

                                                forecastObject = forecastArrayLists.getJSONObject(f);

                                                int forecastServerId = forecastObject.getInt("id");
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
                                                    if (forecastObject.has("farmer_percentage_land")) {
                                                        if (!forecastObject.getString("farmer_percentage_land").equals("null")) {
                                                            farmerpercentageland = forecastObject.getDouble("farmer_percentage_land");
                                                        }
                                                    }
                                                    if (forecastObject.has("current_ppp_commitment")) {
                                                        if (!forecastObject.getString("current_ppp_commitment").equals("null")) {
                                                            currentpppcommitment = forecastObject.getDouble("current_ppp_commitment");
                                                        }
                                                    }

                                                    if (forecastObject.has("farmer_contribution_ppp")) {
                                                        if (!forecastObject.getString("farmer_contribution_ppp").equals("null")) {
                                                            farmercontributionppp = forecastObject.getDouble("farmer_contribution_ppp");
                                                        }
                                                    }

                                                    String accInFoData = "{" +
                                                            "\"total_arable_land_plots\": " + totalArableLandPlots + "," +
                                                            "\"farmer_expected_min_ppp\": " + farmerexpectedminppp + "," +
                                                            "\"minimum_flow_price\": " + minimumflowprice + "," +
                                                            "\"harvest_id\": " + serverSeasonId + "," +
                                                            "\"farmer_id\": " + farmerServerId + "" +
                                                            "}";
                                                    String API_INFO = BuildConfig.DEV_API_URL + "forecast.farmer" + "/" + forecastServerId;

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

                                                            ContentValues contentValues = new ContentValues();
                                                            contentValues.put(BfwContract.ForecastFarmer.COLUMN_SERVER_ID, forecastServerId);
                                                            contentValues.put(BfwContract.ForecastFarmer.COLUMN_PRODUCTION_MT, expectedProductionInMt);
                                                            contentValues.put(BfwContract.ForecastFarmer.COLUMN_YIELD_MT, forecastedyieldmt);
                                                            contentValues.put(BfwContract.ForecastFarmer.COLUMN_HARVEST_SALE_VALUE, forecastedharvestsalevalue);
                                                            contentValues.put(BfwContract.ForecastFarmer.COLUMN_COOP_LAND_SIZE, totalcooplandsize);
                                                            contentValues.put(BfwContract.ForecastFarmer.COLUMN_PERCENT_FARMER_LAND_SIZE, farmerpercentageland);
                                                            contentValues.put(BfwContract.ForecastFarmer.COLUMN_PPP_COMMITMENT, currentpppcommitment);
                                                            contentValues.put(BfwContract.ForecastFarmer.COLUMN_CONTRIBUTION_PPP, farmercontributionppp);
                                                            contentValues.put(BfwContract.ForecastFarmer.COLUMN_IS_SYNC, 1);
                                                            contentValues.put(BfwContract.ForecastFarmer.COLUMN_IS_UPDATE, 1);

                                                            getContentResolver().update(BfwContract.ForecastFarmer.CONTENT_URI, contentValues, forecastFarmerInfo,
                                                                    new String[]{Long.toString(forecastId)});
                                                        }
                                                    }
                                                } else {
                                                    // there's no forecast for a given farmer on season X
                                                    String accInFoData = "{" +
                                                            "\"total_arable_land_plots\": " + totalArableLandPlots + "," +
                                                            "\"farmer_expected_min_ppp\": " + farmerexpectedminppp + "," +
                                                            "\"minimum_flow_price\": " + minimumflowprice + "," +
                                                            "\"harvest_id\": " + serverSeasonId + "," +
                                                            "\"farmer_id\": " + farmerServerId + "" +
                                                            "}";
                                                    String API_INFO = BuildConfig.DEV_API_URL + "forecast.farmer";

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
                                                            farmerpercentageland = bodyLandObject.getDouble("farmer_percentage_land");
                                                            currentpppcommitment = bodyLandObject.getDouble("current_ppp_commitment");
                                                            farmercontributionppp = bodyLandObject.getDouble("farmer_contribution_ppp");

                                                            ContentValues contentValues = new ContentValues();
                                                            contentValues.put(BfwContract.ForecastFarmer.COLUMN_SERVER_ID, infoServerId);
                                                            contentValues.put(BfwContract.ForecastFarmer.COLUMN_PRODUCTION_MT, expectedProductionInMt);
                                                            contentValues.put(BfwContract.ForecastFarmer.COLUMN_YIELD_MT, forecastedyieldmt);
                                                            contentValues.put(BfwContract.ForecastFarmer.COLUMN_HARVEST_SALE_VALUE, forecastedharvestsalevalue);
                                                            contentValues.put(BfwContract.ForecastFarmer.COLUMN_COOP_LAND_SIZE, totalcooplandsize);
                                                            contentValues.put(BfwContract.ForecastFarmer.COLUMN_PERCENT_FARMER_LAND_SIZE, farmerpercentageland);
                                                            contentValues.put(BfwContract.ForecastFarmer.COLUMN_PPP_COMMITMENT, currentpppcommitment);
                                                            contentValues.put(BfwContract.ForecastFarmer.COLUMN_CONTRIBUTION_PPP, farmercontributionppp);
                                                            contentValues.put(BfwContract.ForecastFarmer.COLUMN_IS_SYNC, 1);
                                                            contentValues.put(BfwContract.ForecastFarmer.COLUMN_IS_UPDATE, 1);

                                                            getContentResolver().update(BfwContract.ForecastFarmer.CONTENT_URI, contentValues, forecastFarmerInfo,
                                                                    new String[]{Long.toString(forecastId)});
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            // there's no forecast for a given farmer
                                            String accInFoData = "{" +
                                                    "\"total_arable_land_plots\": " + totalArableLandPlots + "," +
                                                    "\"farmer_expected_min_ppp\": " + farmerexpectedminppp + "," +
                                                    "\"minimum_flow_price\": " + minimumflowprice + "," +
                                                    "\"harvest_id\": " + serverSeasonId + "," +
                                                    "\"farmer_id\": " + farmerServerId + "" +
                                                    "}";
                                            String API_INFO = BuildConfig.DEV_API_URL + "forecast.farmer";

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
                                                    farmerpercentageland = bodyLandObject.getDouble("farmer_percentage_land");
                                                    currentpppcommitment = bodyLandObject.getDouble("current_ppp_commitment");
                                                    farmercontributionppp = bodyLandObject.getDouble("farmer_contribution_ppp");

                                                    ContentValues contentValues = new ContentValues();
                                                    contentValues.put(BfwContract.ForecastFarmer.COLUMN_SERVER_ID, infoServerId);
                                                    contentValues.put(BfwContract.ForecastFarmer.COLUMN_PRODUCTION_MT, expectedProductionInMt);
                                                    contentValues.put(BfwContract.ForecastFarmer.COLUMN_YIELD_MT, forecastedyieldmt);
                                                    contentValues.put(BfwContract.ForecastFarmer.COLUMN_HARVEST_SALE_VALUE, forecastedharvestsalevalue);
                                                    contentValues.put(BfwContract.ForecastFarmer.COLUMN_COOP_LAND_SIZE, totalcooplandsize);
                                                    contentValues.put(BfwContract.ForecastFarmer.COLUMN_PERCENT_FARMER_LAND_SIZE, farmerpercentageland);
                                                    contentValues.put(BfwContract.ForecastFarmer.COLUMN_PPP_COMMITMENT, currentpppcommitment);
                                                    contentValues.put(BfwContract.ForecastFarmer.COLUMN_CONTRIBUTION_PPP, farmercontributionppp);
                                                    contentValues.put(BfwContract.ForecastFarmer.COLUMN_IS_SYNC, 1);
                                                    contentValues.put(BfwContract.ForecastFarmer.COLUMN_IS_UPDATE, 1);

                                                    getContentResolver().update(BfwContract.ForecastFarmer.CONTENT_URI, contentValues, forecastFarmerInfo,
                                                            new String[]{Long.toString(forecastId)});
                                                }
                                            }
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
            if (farmerInfoCursor != null) {
                farmerInfoCursor.close();
            }
            if (cursorFarmerServerId != null) {
                cursorFarmerServerId.close();
            }
            if (serverSeasonCursor != null) {
                serverSeasonCursor.close();
            }
        }

        //post event sync after
        if (dataCount > 0)
            EventBus.getDefault().post(new SyncDataEvent("Farmer Added Successfully", true));
    }
}

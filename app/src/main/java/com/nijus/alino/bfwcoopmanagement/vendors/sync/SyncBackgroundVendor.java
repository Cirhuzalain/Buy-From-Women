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
import org.json.JSONTokener;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SyncBackgroundVendor extends IntentService {

    public final String LOG_TAG = SyncBackgroundVendor.class.getSimpleName();

    public static final MediaType JSON
            = MediaType.parse("text/html; charset=utf-8");

    public SyncBackgroundVendor() {
        super("");
    }

    public SyncBackgroundVendor(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        SharedPreferences prefGoog = getApplicationContext().
                getSharedPreferences(getResources().getString(R.string.application_key), Context.MODE_PRIVATE);

        String appToken = prefGoog.getString(getResources().getString(R.string.app_key), "123");

        String API = BuildConfig.DEV_API_URL + "vendor.farmer";

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(240, TimeUnit.SECONDS)
                .writeTimeout(240, TimeUnit.SECONDS)
                .readTimeout(240, TimeUnit.SECONDS)
                .build();

        //get non sync vendor to the server
        int dataCount = 0;
        int coopServerId = 0;
        int seasonId, serverSeasonId = 1;
        int vendorServerId;
        long id;
        Cursor cursor = null, vendorInfoCursor = null, cursorVendorServerId = null, serverSeasonCursor = null;

        String seasonSelection = BfwContract.HarvestSeason.TABLE_NAME + "." +
                BfwContract.HarvestSeason._ID + " =  ? ";

        String selection = BfwContract.Vendor.TABLE_NAME + "." +
                BfwContract.Vendor.COLUMN_IS_SYNC + " =  0 ";

        String vendorSelection = BfwContract.Vendor.TABLE_NAME + "." +
                BfwContract.Vendor._ID + " =  ? ";

       /* String farmerCoopServerId = BfwContract.Coops.TABLE_NAME + "." +
                BfwContract.Coops._ID + " =  ? ";*/

        String landSelection = BfwContract.VendorLand.TABLE_NAME + "." +
                BfwContract.VendorLand.COLUMN_VENDOR_ID + " = ?  AND " + BfwContract.VendorLand.COLUMN_IS_SYNC + " = 0 ";

        String landInfo = BfwContract.VendorLand.TABLE_NAME + "." +
                BfwContract.VendorLand._ID + " = ? ";

        String accessInfoSelection = BfwContract.VendorAccessInfo.TABLE_NAME + "." +
                BfwContract.VendorAccessInfo.COLUMN_VENDOR_ID + " = ? AND " + BfwContract.VendorAccessInfo.COLUMN_IS_SYNC + " = 0 ";

        String accessInfo = BfwContract.VendorAccessInfo.TABLE_NAME + "." +
                BfwContract.VendorAccessInfo._ID + " = ? ";

        String baselineFarmerSelection = BfwContract.BaseLineVendor.TABLE_NAME + "." +
                BfwContract.BaseLineVendor.COLUMN_VENDOR_ID + " = ? AND " + BfwContract.BaseLineVendor.COLUMN_IS_SYNC + " = 0 ";

        String baselineFarmerInfo = BfwContract.BaseLineVendor.TABLE_NAME + "." +
                BfwContract.BaseLineVendor._ID + " = ? ";

        String forecastFarmerSelection = BfwContract.ForecastVendor.TABLE_NAME + "." +
                BfwContract.ForecastVendor.COLUMN_VENDOR_ID + " = ? AND " + BfwContract.ForecastVendor.COLUMN_IS_SYNC + " = 0 ";

        String forecastFarmerInfo = BfwContract.ForecastVendor.TABLE_NAME + "." +
                BfwContract.ForecastVendor._ID + " = ? ";

        String financeDataSelection = BfwContract.FinanceDataVendor.TABLE_NAME + "." +
                BfwContract.FinanceDataVendor.COLUMN_VENDOR_ID + " = ? AND " + BfwContract.FinanceDataVendor.COLUMN_IS_SYNC + " = 0 ";

        String financeDataInfo = BfwContract.FinanceDataVendor.TABLE_NAME + "." +
                BfwContract.FinanceDataVendor._ID + " = ? ";

        try {
            cursor = getContentResolver().query(BfwContract.Vendor.CONTENT_URI, null, selection, null, null);
            if (cursor != null) {
                dataCount = cursor.getCount();

                while (cursor.moveToNext()) {
                    //get bank info
                    id = cursor.getLong(cursor.getColumnIndex(BfwContract.Vendor._ID));

                    vendorServerId = 0;
                    String email = "null";
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
                    String newResourcesDetails = cursor.getString(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_NEW_SOURCE_DETAIL));
                    String waterSourceDetails = cursor.getString(cursor.getColumnIndex(BfwContract.Vendor.COLUMN_WATER_SOURCE_DETAILS));

                    String bodyInfo = "{" +
                            "\"name\" : \"" + name + "\"," +
                            "\"phone\" : \"" + phoneNumber + "\"," +
                            "\"email\" : \"" + email + "\"," +
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
                            .method("POST", body)
                            .build();

                    try {
                        Response responseFarmer = client.newCall(request).execute();
                        ResponseBody responseBody = responseFarmer.body();

                        if (responseBody != null) {
                            String farmerDataInfo = responseBody.string();
                            JSONObject farmerInfo = new JSONObject(farmerDataInfo);
                            if (farmerInfo.has("id")) {
                                vendorServerId = farmerInfo.getInt("id");
                                //String membershipId = farmerInfo.getString("membership_id");
                                String membershipId = "";

                                //set membership id
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(BfwContract.Vendor.COLUMN_VENDOR_SERVER_ID, vendorServerId);
                                contentValues.put(BfwContract.Vendor.COLUMN_MEMBER_SHIP, membershipId);
                                contentValues.put(BfwContract.Vendor.COLUMN_IS_SYNC, 1);
                                contentValues.put(BfwContract.Vendor.COLUMN_IS_UPDATE, 1);
                                getContentResolver().update(BfwContract.Vendor.CONTENT_URI, contentValues, vendorSelection, new String[]{Long.toString(id)});
                            }
                        }

                        //sync access to information if available
                        vendorInfoCursor = getContentResolver().query(BfwContract.VendorAccessInfo.CONTENT_URI, null, accessInfoSelection,
                                new String[]{Long.toString(id)}, null);

                        if (vendorInfoCursor != null) {
                            int infoId;

                            Boolean aes;
                            Boolean cri;
                            Boolean seeds;
                            Boolean orgFert;
                            Boolean inorgFert;
                            Boolean labour;
                            Boolean iwp;
                            Boolean ss;

                            while (vendorInfoCursor.moveToNext()) {

                                infoId = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.VendorAccessInfo._ID));
                                aes = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.VendorAccessInfo.COLUMN_AGRI_EXTENSION_SERV)) == 1;
                                cri = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.VendorAccessInfo.COLUMN_CLIMATE_RELATED_INFO)) == 1;
                                seeds = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.VendorAccessInfo.COLUMN_SEEDS)) == 1;
                                orgFert = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.VendorAccessInfo.COLUMN_ORGANIC_FERTILIZER)) == 1;
                                inorgFert = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.VendorAccessInfo.COLUMN_INORGANIC_FERTILIZER)) == 1;
                                labour = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.VendorAccessInfo.COLUMN_LABOUR)) == 1;
                                iwp = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.VendorAccessInfo.COLUMN_WATER_PUMPS)) == 1;
                                ss = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.VendorAccessInfo.COLUMN_SPRAYERS)) == 1;

                                seasonId = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.VendorAccessInfo.COLUMN_SEASON_ID));

                                serverSeasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null,
                                        seasonSelection, new String[]{Long.toString(seasonId)}, null);

                                if (serverSeasonCursor != null) {
                                    while (serverSeasonCursor.moveToNext()) {
                                        serverSeasonId = serverSeasonCursor.getInt(serverSeasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_SERVER_ID));
                                    }
                                }

                                if (vendorServerId > 0) {

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
                                            "\"vendor_id\": " + vendorServerId + "" +
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
                                            int infoServerId = bodyLandObject.getInt("id");

                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put(BfwContract.VendorAccessInfo.COLUMN_SERVER_ID, infoServerId);
                                            contentValues.put(BfwContract.VendorAccessInfo.COLUMN_IS_SYNC, 1);
                                            contentValues.put(BfwContract.VendorAccessInfo.COLUMN_IS_UPDATE, 1);

                                            getContentResolver().update(BfwContract.VendorAccessInfo.CONTENT_URI, contentValues, accessInfo,
                                                    new String[]{Long.toString(infoId)});
                                        }
                                    }

                                }

                            }
                        }

                        //sync baseline information
                        vendorInfoCursor = getContentResolver().query(BfwContract.BaseLineVendor.CONTENT_URI, null, baselineFarmerSelection,
                                new String[]{Long.toString(id)}, null);

                        if (vendorInfoCursor != null) {

                            int baselineId;
                            Double seasonharvest;
                            Double lostharvesttotal;
                            Double soldharvesttotal;
                            Double totalqtycoops;
                            Double pricesoldcoops;
                            Double totalqtymiddlemen;
                            Double pricesoldmiddlemen;
                            int baselineSeasonId;

                            while (vendorInfoCursor.moveToNext()) {

                                baselineId = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.BaseLineVendor._ID));

                                seasonharvest = vendorInfoCursor.getDouble(vendorInfoCursor.getColumnIndex(BfwContract.BaseLineVendor.COLUMN_TOT_PROD_B_KG));
                                lostharvesttotal = vendorInfoCursor.getDouble(vendorInfoCursor.getColumnIndex(BfwContract.BaseLineVendor.COLUMN_TOT_LOST_KG));
                                soldharvesttotal = vendorInfoCursor.getDouble(vendorInfoCursor.getColumnIndex(BfwContract.BaseLineVendor.COLUMN_TOT_SOLD_KG));
                                totalqtycoops = vendorInfoCursor.getDouble(vendorInfoCursor.getColumnIndex(BfwContract.BaseLineVendor.COLUMN_TOT_VOL_SOLD_COOP));
                                pricesoldcoops = vendorInfoCursor.getDouble(vendorInfoCursor.getColumnIndex(BfwContract.BaseLineVendor.COLUMN_PRICE_SOLD_COOP_PER_KG));
                                totalqtymiddlemen = vendorInfoCursor.getDouble(vendorInfoCursor.getColumnIndex(BfwContract.BaseLineVendor.COLUMN_TOT_VOL_SOLD_IN_KG));
                                pricesoldmiddlemen = vendorInfoCursor.getDouble(vendorInfoCursor.getColumnIndex(BfwContract.BaseLineVendor.COLUMN_PRICE_SOLD_KG));

                                baselineSeasonId = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.BaseLineVendor.COLUMN_SEASON_ID));

                                serverSeasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null,
                                        seasonSelection, new String[]{Long.toString(baselineSeasonId)}, null);

                                if (serverSeasonCursor != null) {
                                    while (serverSeasonCursor.moveToNext()) {
                                        serverSeasonId = serverSeasonCursor.getInt(serverSeasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_SERVER_ID));
                                    }
                                }

                                if (vendorServerId > 0) {

                                    String accInFoData = "{" +
                                            "\"seasona_harvest\": " + seasonharvest + "," +
                                            "\"lost_harvest_total\": " + lostharvesttotal + "," +
                                            "\"sold_harvest_total\": " + soldharvesttotal + "," +
                                            "\"total_qty_coops\": " + totalqtycoops + "," +
                                            "\"price_sold_coops\": " + pricesoldcoops + "," +
                                            "\"total_qty_middlemen\": " + totalqtymiddlemen + "," +
                                            "\"price_sold_middlemen\": " + pricesoldmiddlemen + "," +
                                            "\"harvest_id\": " + serverSeasonId + "," +
                                            "\"vendor_id\": " + vendorServerId + "" +
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

                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put(BfwContract.BaseLineVendor.COLUMN_SERVER_ID, infoServerId);
                                            contentValues.put(BfwContract.BaseLineVendor.COLUMN_IS_SYNC, 1);
                                            contentValues.put(BfwContract.BaseLineVendor.COLUMN_IS_UPDATE, 1);

                                            getContentResolver().update(BfwContract.BaseLineVendor.CONTENT_URI, contentValues, baselineFarmerInfo,
                                                    new String[]{Long.toString(baselineId)});
                                        }
                                    }
                                }
                            }
                        }

                        //sync finance data information
                        vendorInfoCursor = getContentResolver().query(BfwContract.FinanceDataVendor.CONTENT_URI, null, financeDataSelection,
                                new String[]{Long.toString(id)}, null);

                        if (vendorInfoCursor != null) {

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

                            while (vendorInfoCursor.moveToNext()) {

                                financeId = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor._ID));

                                outstandingloan = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_OUTSANDING_LOAN)) == 1;
                                loanPurposeI = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_LOANPROVIDER_INPUT)) == 1;
                                loanPurposeA = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_LOANPROVIDER_AGGREG)) == 1;
                                loanPurposeO = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_LOANPROVIDER_OTHER)) == 1;
                                mobileMoneyAccount = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_MOBILE_MONEY_ACCOUNT)) == 1;

                                totalLoanAmount = vendorInfoCursor.getDouble(vendorInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_TOT_LOAN_AMOUNT));
                                totaloutstanding = vendorInfoCursor.getDouble(vendorInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_TOT_OUTSTANDING));
                                interestrate = vendorInfoCursor.getDouble(vendorInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_INTEREST_RATE));
                                duration = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_DURATION));
                                loanProvider = vendorInfoCursor.getString(vendorInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_LOAN_PROVIDER));

                                financeSeasonId = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_SEASON_ID));

                                serverSeasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null,
                                        seasonSelection, new String[]{Long.toString(financeSeasonId)}, null);

                                if (serverSeasonCursor != null) {
                                    while (serverSeasonCursor.moveToNext()) {
                                        serverSeasonId = serverSeasonCursor.getInt(serverSeasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_SERVER_ID));
                                    }
                                }

                                if (vendorServerId > 0) {

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
                                            "\"vendor_id\": " + vendorServerId + "" +
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

                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put(BfwContract.FinanceDataVendor.COLUMN_SERVER_ID, infoServerId);
                                            contentValues.put(BfwContract.FinanceDataVendor.COLUMN_IS_SYNC, 1);
                                            contentValues.put(BfwContract.FinanceDataVendor.COLUMN_IS_UPDATE, 1);

                                            getContentResolver().update(BfwContract.FinanceDataVendor.CONTENT_URI, contentValues, financeDataInfo,
                                                    new String[]{Long.toString(financeId)});
                                        }
                                    }
                                }
                            }
                        }

                        //sync land information if available
                        vendorInfoCursor = getContentResolver().query(BfwContract.VendorLand.CONTENT_URI, null, landSelection,
                                new String[]{Long.toString(id)}, null);

                        if (vendorInfoCursor != null) {
                            long landId;
                            while (vendorInfoCursor.moveToNext()) {

                                landId = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.VendorLand._ID));
                                double plotSize = vendorInfoCursor.getDouble(vendorInfoCursor.getColumnIndex(BfwContract.VendorLand.COLUMN_PLOT_SIZE));
                                double lat = vendorInfoCursor.getDouble(vendorInfoCursor.getColumnIndex(BfwContract.VendorLand.COLUMN_LAT_INFO));
                                double lng = vendorInfoCursor.getDouble(vendorInfoCursor.getColumnIndex(BfwContract.VendorLand.COLUMN_LNG_INFO));

                                seasonId = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.VendorLand.COLUMN_SEASON_ID));

                                serverSeasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null,
                                        seasonSelection, new String[]{Long.toString(seasonId)}, null);

                                if (serverSeasonCursor != null) {
                                    while (serverSeasonCursor.moveToNext()) {
                                        serverSeasonId = serverSeasonCursor.getInt(serverSeasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_SERVER_ID));
                                    }
                                }

                                if (vendorServerId != 0) {
                                    String plotInfo = "{" +
                                            "\"plot_size\": " + plotSize + "," +
                                            "\"lat\": " + lat + "," +
                                            "\"lng\": " + lng + "," +
                                            "\"harvest_id\": " + serverSeasonId + "," +
                                            "\"vendor_id\": " + vendorServerId + "" +
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

                                            ContentValues contentValues = new ContentValues();
                                            contentValues.put(BfwContract.VendorLand.COLUMN_SERVER_ID, landServerId);
                                            contentValues.put(BfwContract.VendorLand.COLUMN_IS_SYNC, 1);
                                            contentValues.put(BfwContract.VendorLand.COLUMN_IS_UPDATE, 1);

                                            getContentResolver().update(BfwContract.VendorLand.CONTENT_URI, contentValues, landInfo, new String[]{Long.toString(landId)});
                                        }
                                    }
                                }
                            }
                        }

                        //sync forecast information
                        vendorInfoCursor = getContentResolver().query(BfwContract.ForecastVendor.CONTENT_URI, null, forecastFarmerSelection,
                                new String[]{Long.toString(id)}, null);

                        if (vendorInfoCursor != null) {

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


                            while (vendorInfoCursor.moveToNext()) {

                                forecastId = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.ForecastVendor._ID));

                                totalArableLandPlots = vendorInfoCursor.getDouble(vendorInfoCursor.getColumnIndex(BfwContract.ForecastVendor.COLUMN_ARABLE_LAND_PLOT));
                                farmerexpectedminppp = vendorInfoCursor.getDouble(vendorInfoCursor.getColumnIndex(BfwContract.ForecastVendor.COLUMN_EXPECTED_MIN_PPP));
                                minimumflowprice = vendorInfoCursor.getDouble(vendorInfoCursor.getColumnIndex(BfwContract.ForecastVendor.COLUMN_FLOW_PRICE));

                                forecastSeasonId = vendorInfoCursor.getInt(vendorInfoCursor.getColumnIndex(BfwContract.ForecastVendor.COLUMN_SEASON_ID));

                                serverSeasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null,
                                        seasonSelection, new String[]{Long.toString(forecastSeasonId)}, null);

                                if (serverSeasonCursor != null) {
                                    while (serverSeasonCursor.moveToNext()) {
                                        serverSeasonId = serverSeasonCursor.getInt(serverSeasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_SERVER_ID));
                                    }
                                }

                                if (vendorServerId > 0) {

                                    // get all the forecast for a given vendor and check if there's one that match with serverSeasonId
                                    //String proxyUrl = BuildConfig.DEV_PROXY_URL + "?model=vendor.farmer&attr=id&value=" + vendorServerId + "&token=" + access_token;

                                    String proxyUrl = BuildConfig.DEV_PROXY_URL + "?model=forecast.vendor&attr=vendor_id&value=" + vendorServerId + "&token=" + appToken;
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

                                        Boolean isResponse = false;

                                        if (json instanceof JSONArray){
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

                                                    // there's forecast for a given vendor on season X
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
                                                            "\"vendor_id\": " + vendorServerId + "" +
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

                                                            ContentValues contentValues = new ContentValues();
                                                            contentValues.put(BfwContract.ForecastVendor.COLUMN_SERVER_ID, forecastServerId);
                                                            contentValues.put(BfwContract.ForecastVendor.COLUMN_PRODUCTION_MT, expectedProductionInMt);
                                                            contentValues.put(BfwContract.ForecastVendor.COLUMN_YIELD_MT, forecastedyieldmt);
                                                            contentValues.put(BfwContract.ForecastVendor.COLUMN_HARVEST_SALE_VALUE, forecastedharvestsalevalue);
                                                            contentValues.put(BfwContract.ForecastVendor.COLUMN_COOP_LAND_SIZE, totalcooplandsize);
                                                            contentValues.put(BfwContract.ForecastVendor.COLUMN_PERCENT_FARMER_LAND_SIZE, farmerpercentageland);
                                                            contentValues.put(BfwContract.ForecastVendor.COLUMN_PPP_COMMITMENT, currentpppcommitment);
                                                            contentValues.put(BfwContract.ForecastVendor.COLUMN_CONTRIBUTION_PPP, farmercontributionppp);
                                                            contentValues.put(BfwContract.ForecastVendor.COLUMN_IS_SYNC, 1);
                                                            contentValues.put(BfwContract.ForecastVendor.COLUMN_IS_UPDATE, 1);

                                                            getContentResolver().update(BfwContract.ForecastVendor.CONTENT_URI, contentValues, forecastFarmerInfo,
                                                                    new String[]{Long.toString(forecastId)});
                                                        }
                                                    }
                                                } else {
                                                    // there's no forecast for a given vendor on season X
                                                    String accInFoData = "{" +
                                                            "\"total_arable_land_plots\": " + totalArableLandPlots + "," +
                                                            "\"vendor_expected_min_ppp\": " + farmerexpectedminppp + "," +
                                                            "\"minimum_flow_price\": " + minimumflowprice + "," +
                                                            "\"harvest_id\": " + serverSeasonId + "," +
                                                            "\"vendor_id\": " + vendorServerId + "" +
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

                                                            ContentValues contentValues = new ContentValues();
                                                            contentValues.put(BfwContract.ForecastVendor.COLUMN_SERVER_ID, infoServerId);
                                                            contentValues.put(BfwContract.ForecastVendor.COLUMN_PRODUCTION_MT, expectedProductionInMt);
                                                            contentValues.put(BfwContract.ForecastVendor.COLUMN_YIELD_MT, forecastedyieldmt);
                                                            contentValues.put(BfwContract.ForecastVendor.COLUMN_HARVEST_SALE_VALUE, forecastedharvestsalevalue);
                                                            contentValues.put(BfwContract.ForecastVendor.COLUMN_COOP_LAND_SIZE, totalcooplandsize);
                                                            contentValues.put(BfwContract.ForecastVendor.COLUMN_PERCENT_FARMER_LAND_SIZE, farmerpercentageland);
                                                            contentValues.put(BfwContract.ForecastVendor.COLUMN_PPP_COMMITMENT, currentpppcommitment);
                                                            contentValues.put(BfwContract.ForecastVendor.COLUMN_CONTRIBUTION_PPP, farmercontributionppp);
                                                            contentValues.put(BfwContract.ForecastVendor.COLUMN_IS_SYNC, 1);
                                                            contentValues.put(BfwContract.ForecastVendor.COLUMN_IS_UPDATE, 1);

                                                            getContentResolver().update(BfwContract.ForecastVendor.CONTENT_URI, contentValues, forecastFarmerInfo,
                                                                    new String[]{Long.toString(forecastId)});
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            // there's no forecast for a given vendor
                                            String accInFoData = "{" +
                                                    "\"total_arable_land_plots\": " + totalArableLandPlots + "," +
                                                    "\"vendor_expected_min_ppp\": " + farmerexpectedminppp + "," +
                                                    "\"minimum_flow_price\": " + minimumflowprice + "," +
                                                    "\"harvest_id\": " + serverSeasonId + "," +
                                                    "\"vendor_id\": " + vendorServerId + "" +
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

                                                    ContentValues contentValues = new ContentValues();
                                                    contentValues.put(BfwContract.ForecastVendor.COLUMN_SERVER_ID, infoServerId);
                                                    contentValues.put(BfwContract.ForecastVendor.COLUMN_PRODUCTION_MT, expectedProductionInMt);
                                                    contentValues.put(BfwContract.ForecastVendor.COLUMN_YIELD_MT, forecastedyieldmt);
                                                    contentValues.put(BfwContract.ForecastVendor.COLUMN_HARVEST_SALE_VALUE, forecastedharvestsalevalue);
                                                    contentValues.put(BfwContract.ForecastVendor.COLUMN_COOP_LAND_SIZE, totalcooplandsize);
                                                    contentValues.put(BfwContract.ForecastVendor.COLUMN_PERCENT_FARMER_LAND_SIZE, farmerpercentageland);
                                                    contentValues.put(BfwContract.ForecastVendor.COLUMN_PPP_COMMITMENT, currentpppcommitment);
                                                    contentValues.put(BfwContract.ForecastVendor.COLUMN_CONTRIBUTION_PPP, farmercontributionppp);
                                                    contentValues.put(BfwContract.ForecastVendor.COLUMN_IS_SYNC, 1);
                                                    contentValues.put(BfwContract.ForecastVendor.COLUMN_IS_UPDATE, 1);

                                                    getContentResolver().update(BfwContract.ForecastVendor.CONTENT_URI, contentValues, forecastFarmerInfo,
                                                            new String[]{Long.toString(forecastId)});
                                                }
                                            }
                                        }

                                    } //end of if "responseBodyLand != null"
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
            if (vendorInfoCursor != null) {
                vendorInfoCursor.close();
            }
            if (cursorVendorServerId != null) {
                cursorVendorServerId.close();
            }
            if (serverSeasonCursor != null) {
                serverSeasonCursor.close();
            }
        }

        //post event sync after
        if (dataCount > 0)
            EventBus.getDefault().post(new SyncDataEvent(getResources().getString(R.string.add_vend_msg_sync ), true));
    }
}

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

public class UpdateSyncFarmer extends IntentService {

    public static final MediaType JSON
            = MediaType.parse("text/html; charset=utf-8");

    public UpdateSyncFarmer() {
        super("");
    }

    public UpdateSyncFarmer(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SharedPreferences prefGoog = getApplicationContext().
                getSharedPreferences(getResources().getString(R.string.application_key), Context.MODE_PRIVATE);

        String appToken = prefGoog.getString(getResources().getString(R.string.app_key), "123");

        //get non sync farmer to the server (is_sync)
        int dataCount = 0;
        int farmerServerId = 0;
        long id;
        Cursor cursor = null, landCursor = null;
        String selection = BfwContract.Farmer.TABLE_NAME + "." +
                BfwContract.Farmer.COLUMN_IS_SYNC + " =  1 AND " +
                BfwContract.Farmer.TABLE_NAME + "." +
                BfwContract.Farmer.COLUMN_IS_UPDATE + " = 0";


        String farmerSelection = BfwContract.Farmer.TABLE_NAME + "." +
                BfwContract.Farmer._ID + " =  ? ";

        String landSelection = BfwContract.LandPlot.TABLE_NAME + "." +
                BfwContract.LandPlot.COLUMN_FARMER_ID + " = ? ";
        String bankInfos = "\"bank_ids\": [],";
        try {
            cursor = getContentResolver().query(BfwContract.Farmer.CONTENT_URI, null, selection, null, null);
            if (cursor != null) {
                dataCount = cursor.getCount();

                while (cursor.moveToNext()) {
                    //get bank info
                    id = cursor.getLong(cursor.getColumnIndex(BfwContract.Farmer._ID));
                    farmerServerId = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_FARMER_SERVER_ID));

                    //get land info
                    landCursor = getContentResolver().query(BfwContract.LandPlot.CONTENT_URI, null, landSelection, new String[]{Long.toString(id)}, null);


                    String name = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_NAME));
                    String phoneNumber = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_PHONE));
                    String gender = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_GENDER));

                    double arableLandPlot = cursor.getDouble(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_ARABLE_LAND_PLOT));

                    boolean houseHold = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_HOUSEHOLD_HEAD)) == 1;
                    int houseMember = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_HOUSE_MEMBER));
                    String sFirstName = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_FIRST_NAME));
                    String sLastName = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_LAST_NAME));
                    String cellPhone = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_CELL_PHONE));
                    String cellCarrier = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_CELL_CARRIER));
                    String membershipId = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_MEMBER_SHIP));

                    int totProdBKg = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_TOT_PROD_B_KG));
                    int totLostKg = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_TOT_LOST_KG));
                    int totSoldKg = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_TOT_SOLD_KG));
                    int totVolSoldCoop = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_TOT_VOL_SOLD_COOP));
                    int priceSoldPerKg = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_PRICE_SOLD_COOP_PER_KG));
                    int totVolSoldKg = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_TOT_VOL_SOLD_IN_KG));
                    int priceSoldKg = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_PRICE_SOLD_KG));

                    boolean isOutstanding = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_OUTSANDING_LOAN)) == 1;
                    int totLoanAmount = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_TOT_LOAN_AMOUNT));
                    int totOutstanding = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_TOT_OUTSTANDING));
                    double interestRate = cursor.getDouble(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_INTEREST_RATE));
                    int duration = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_DURATION));
                    String loanProvider = cursor.getString(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_LOAN_PROVIDER));
                    boolean isMobileMoney = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_MOBILE_MONEY_ACCOUNT)) == 1;
                    boolean isAggregation = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_LP_AGGREG)) == 1;
                    boolean isInput = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_LP_INPUT)) == 1;
                    boolean isOther = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_OTHER)) == 1;

                    boolean isAggriExtension = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_AGRI_EXTENSION_SERV)) == 1;
                    boolean isClimate = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_CLIMATE_RELATED_INFO)) == 1;
                    boolean seeds = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_SEEDS)) == 1;
                    boolean isOrganicFertilizer = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_ORGANIC_FERTILIZER)) == 1;
                    boolean isInorganicFertilizer = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_INORGANIC_FERTILIZER)) == 1;
                    boolean labour = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_LABOUR)) == 1;
                    boolean waterPumps = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_WATER_PUMPS)) == 1;
                    boolean tractors = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_TRACTORS)) == 1;
                    boolean harvester = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_HARVESTER)) == 1;
                    boolean sprayers = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_SPRAYERS)) == 1;
                    boolean dryer = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_DRYER)) == 1;
                    boolean tresher = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_TRESHER)) == 1;
                    boolean safeStorage = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_SAFE_STORAGE)) == 1;
                    boolean otherInfo = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_OTHER_INFO)) == 1;
                    boolean isDam = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_DAM)) == 1;
                    boolean isWell = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_WELL)) == 1;
                    boolean isBorehole = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_BORHOLE)) == 1;
                    boolean isPipeBorne = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_PIPE_BORNE)) == 1;
                    boolean isRiverStream = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_RIVER_STREAM)) == 1;
                    boolean isIrrigation = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_IRRIGATION)) == 1;
                    boolean isNone = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_NONE)) == 1;
                    boolean isOtherSource = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_OTHER)) == 1;
                    int coopUserId = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_COOP_USER_ID));
                    int fServerId = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer.COLUMN_FARMER_SERVER_ID));

                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(240, TimeUnit.SECONDS)
                            .writeTimeout(240, TimeUnit.SECONDS)
                            .readTimeout(240, TimeUnit.SECONDS)
                            .build();
                    String API = BuildConfig.API_URL + "res.partner" + "/" + fServerId;

                    loanProvider = loanProvider == null ? "\"other\"" : "\"" + loanProvider + "\"";

                    String bodyContent = "{" +
                            "\"ar_safestorage\": " + safeStorage + "," +
                            "\"num_household_members\": " + houseMember + "," +
                            "\"cellphone_alt\": \"" + cellPhone + "\"," +
                            "\"mws_well\": " + isWell + "," +
                            "\"sex\": \"" + gender + "\"," +
                            "\"total_outstanding\": " + totOutstanding + "," +
                            "\"loan_provider\": " + loanProvider + "," +
                            "\"duration\": " + duration + "," +
                            "\"ar_dryer\": " + dryer + "," +
                            bankInfos +
                            "\"ar_labour\": " + labour + "," +
                            "\"ar_thresher\": " + tresher + "," +
                            "\"interest_rate\": " + interestRate + "," +
                            "\"ar_other\": " + otherInfo + "," +
                            "\"mobile_money_account\": " + isMobileMoney + "," +
                            "\"head_of_household\": " + houseHold + "," +
                            "\"ar_iwp\": " + waterPumps + "," +
                            "\"seasona_2016_harvest\": " + totProdBKg + "," +
                            "\"loan_purpose_i\": " + isInput + "," +
                            "\"cell_carrier\": \"" + cellCarrier + "\"," +
                            "\"spouse_lastname\": \"" + sLastName + "\"," +
                            "\"total_loan_amount\": " + totLoanAmount + "," +
                            "\"mws_other\": " + isOtherSource + "," +
                            "\"ar_cri\": " + isClimate + "," +
                            "\"loan_purpose_a\": " + isAggregation + "," +
                            "\"price_sold_middlemen\": " + priceSoldKg + "," +
                            "\"total_qty_coops\": " + totVolSoldCoop + "," +
                            "\"mws_none\": " + isNone + "," +
                            "\"mws_rs\": " + isRiverStream + "," +
                            "\"spouse_firstname\": \"" + sFirstName + "\"," +
                            "\"ar_of\": " + isOrganicFertilizer + "," +
                            "\"mws_pb\": " + isPipeBorne + "," +
                            "\"ar_tractors\": " + tractors + "," +
                            "\"loan_purpose_o\": " + isOther + "," +
                            "\"user_id\": " + coopUserId + "," +
                            "\"ar_seeds\": " + seeds + "," +
                            "\"lost_harvest_total\": " + totLostKg + "," +
                            "\"outstanding_loan\": " + isOutstanding + "," +
                            "\"mws_dam\": " + isDam + "," +
                            "\"cellphone\": \"" + phoneNumber + "\"," +
                            "\"total_arable_land_plots\": " + arableLandPlot + "," +
                            "\"ar_if\": " + isInorganicFertilizer + "," +
                            "\"name\": \"" + name + "\"," +
                            "\"contact_type\": \"farmer\"," +
                            "\"price_sold_coops\": " + priceSoldPerKg + "," +
                            "\"mws_irrigation\": " + isIrrigation + "," +
                            "\"mws_borehole\": " + isBorehole + "," +
                            "\"ar_aes\": " + isAggriExtension + "," +
                            "\"membership_id\": \"" + membershipId + "\"," +
                            "\"sold_harvest_total\": " + totSoldKg + "," +
                            "\"ar_ss\": " + sprayers + "," +
                            "\"ar_harverster\": " + harvester + "," +
                            "\"total_qty_middlemen\": " + totVolSoldKg + "}";


                    RequestBody body = RequestBody.create(JSON, bodyContent);

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
                                contentValues.put(BfwContract.Farmer.COLUMN_IS_SYNC, 1);
                                contentValues.put(BfwContract.Farmer.COLUMN_IS_UPDATE, 1);
                                getContentResolver().update(BfwContract.Farmer.CONTENT_URI, contentValues, farmerSelection, new String[]{Long.toString(id)});

                                String landSelec = BfwContract.LandPlot.TABLE_NAME + "." +
                                        BfwContract.LandPlot.COLUMN_FARMER_ID + " = ? ";

                                landCursor = getContentResolver().query(BfwContract.LandPlot.CONTENT_URI, null, landSelec, new String[]{Long.toString(id)}, null);
                                if (landCursor != null) {
                                    long landId;
                                    long landServerInfo;
                                    String landInfo = BfwContract.LandPlot.TABLE_NAME + "." +
                                            BfwContract.LandPlot._ID + " = ? ";
                                    while (landCursor.moveToNext()) {

                                        landId = landCursor.getInt(landCursor.getColumnIndex(BfwContract.LandPlot._ID));
                                        landServerInfo = landCursor.getInt(landCursor.getColumnIndex(BfwContract.LandPlot.COLUMN_SERVER_ID));
                                        double plotSize = landCursor.getDouble(landCursor.getColumnIndex(BfwContract.LandPlot.COLUMN_PLOT_SIZE));

                                        if (landServerInfo != 0) {
                                            String plotInfo = "{" +
                                                    "\"plot_size\": " + plotSize + "," +
                                                    "\"partner_id\": " + farmerServerId + "" +
                                                    "}";
                                            String API_INFO = BuildConfig.API_URL + "res.partner.land.plot" + "/" + landServerInfo;

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
                                                    contentValue.put(BfwContract.LandPlot.COLUMN_IS_SYNC, 1);
                                                    contentValue.put(BfwContract.LandPlot.COLUMN_IS_UPDATE, 1);

                                                    getContentResolver().update(BfwContract.LandPlot.CONTENT_URI, contentValue, landInfo, new String[]{Long.toString(landId)});
                                                }
                                            }
                                        } else {
                                            String plotInfo = "{" +
                                                    "\"plot_size\": " + plotSize + "," +
                                                    "\"partner_id\": " + farmerServerId + "" +
                                                    "}";
                                            String API_INFO = BuildConfig.API_URL + "res.partner.land.plot";

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

                                                //set server id land in local db
                                                JSONObject bodyLandObject = new JSONObject(bodyLandInfo);
                                                if (bodyLandObject.has("id")) {
                                                    int landServerId = bodyLandObject.getInt("id");

                                                    ContentValues contentValue = new ContentValues();
                                                    contentValue.put(BfwContract.LandPlot.COLUMN_SERVER_ID, landServerId);
                                                    contentValue.put(BfwContract.LandPlot.COLUMN_IS_SYNC, 1);
                                                    contentValue.put(BfwContract.LandPlot.COLUMN_IS_UPDATE, 1);

                                                    getContentResolver().update(BfwContract.LandPlot.CONTENT_URI, contentValue, landInfo, new String[]{Long.toString(landId)});
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
            if (landCursor != null) {
                landCursor.close();
            }
        }

        //post event sync after
        if (dataCount > 0)
            EventBus.getDefault().post(new SyncDataEvent("", true));
    }
}

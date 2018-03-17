package com.nijus.alino.bfwcoopmanagement.coops.sync;

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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.nijus.alino.bfwcoopmanagement.farmers.sync.SyncBackground.JSON;


public class SyncCoopInfo extends IntentService {
    private long mFarmerId;

    public SyncCoopInfo() {
        super("");
    }

    public SyncCoopInfo(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        SharedPreferences prefGoog = getApplicationContext().
                getSharedPreferences(getResources().getString(R.string.application_key), Context.MODE_PRIVATE);

        String appToken = prefGoog.getString(getResources().getString(R.string.app_key), "123");

        String API = BuildConfig.DEV_API_URL + "coop";

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(240, TimeUnit.SECONDS)
                .writeTimeout(240, TimeUnit.SECONDS)
                .readTimeout(240, TimeUnit.SECONDS)
                .build();

        //get non sync farmer to the server
        int dataCount = 0;
        int coopServerId = 0;
        int serverSeasonId = 1;
        long id;
        Cursor cursor = null, coopInfoCursor = null, serverSeasonCursor = null;

        String seasonSelection = BfwContract.HarvestSeason.TABLE_NAME + "." +
                BfwContract.HarvestSeason._ID + " =  ? ";

        String selection = BfwContract.Coops.TABLE_NAME + "." +
                BfwContract.Coops.COLUMN_IS_SYNC + " =  0 ";

        String coopSelection = BfwContract.Coops.TABLE_NAME + "." +
                BfwContract.Coops._ID + " =  ? ";

        // baseline yield yield coop info

        String baselineYieldSelection = BfwContract.YieldCoop.TABLE_NAME + "." +
                BfwContract.YieldCoop.COLUMN_COOP_ID + " = ?  AND " + BfwContract.YieldCoop.COLUMN_IS_SYNC + " = 0 ";

        String baselineYieldInfo = BfwContract.YieldCoop.TABLE_NAME + "." +
                BfwContract.YieldCoop._ID + " = ? ";

        // access info coop
        String accessInfoSelection = BfwContract.CoopInfo.TABLE_NAME + "." +
                BfwContract.CoopInfo.COLUMN_COOP_ID + " = ? AND " + BfwContract.CoopInfo.COLUMN_IS_SYNC + " = 0 ";

        String accessCoopInfo = BfwContract.CoopInfo.TABLE_NAME + "." +
                BfwContract.CoopInfo._ID + " = ? ";

        // baseline sales coop info
        String baselineSalesCoopSelection = BfwContract.BaselineSalesCoop.TABLE_NAME + "." +
                BfwContract.BaselineSalesCoop.COLUMN_COOP_ID + " = ? AND " + BfwContract.BaselineSalesCoop.COLUMN_IS_SYNC + " = 0 ";

        String baselineSalesCoopInfo = BfwContract.BaselineSalesCoop.TABLE_NAME + "." +
                BfwContract.BaselineSalesCoop._ID + " = ? ";

        // baseline finance info coop
        String financeCoopDataSelection = BfwContract.FinanceInfoCoop.TABLE_NAME + "." +
                BfwContract.FinanceInfoCoop.COLUMN_COOP_ID + " = ? AND " + BfwContract.FinanceInfoCoop.COLUMN_IS_SYNC + " = 0 ";

        String financeCoopDataInfo = BfwContract.FinanceInfoCoop.TABLE_NAME + "." +
                BfwContract.FinanceInfoCoop._ID + " = ? ";

        // forecast sales coop
        String forecastSalesCoopSelection = BfwContract.SalesCoop.TABLE_NAME + "." +
                BfwContract.SalesCoop.COLUMN_COOP_ID + " = ? AND " + BfwContract.SalesCoop.COLUMN_IS_SYNC + " = 0 ";

        String forecastSalesCoopInfo = BfwContract.SalesCoop.TABLE_NAME + "." +
                BfwContract.SalesCoop._ID + " = ? ";


        try {
            cursor = getContentResolver().query(BfwContract.Coops.CONTENT_URI, null, selection, null, null);
            if (cursor != null) {
                dataCount = cursor.getCount();

                while (cursor.moveToNext()) {

                    id = cursor.getLong(cursor.getColumnIndex(BfwContract.Coops._ID));

                    //general information
                    String name = cursor.getString(cursor.getColumnIndex(BfwContract.Coops.COLUMN_COOP_NAME));
                    String phoneNumber = cursor.getString(cursor.getColumnIndex(BfwContract.Coops.COLUMN_PHONE));
                    String address = cursor.getString(cursor.getColumnIndex(BfwContract.Coops.COLUMN_ADDRESS));
                    String email = cursor.getString(cursor.getColumnIndex(BfwContract.Coops.COLUMN_EMAIL));

                    //internal information
                    String chairName = cursor.getString(cursor.getColumnIndex(BfwContract.Coops.COLUMN_CHAIR_NAME));
                    String viceChairName = cursor.getString(cursor.getColumnIndex(BfwContract.Coops.COLUMN_VICECHAIR_NAME));
                    String secName = cursor.getString(cursor.getColumnIndex(BfwContract.Coops.COLUMN_SECRETARY_NAME));

                    String chairGender = cursor.getString(cursor.getColumnIndex(BfwContract.Coops.COLUMN_CHAIR_GENDER));
                    String viceChairGender = cursor.getString(cursor.getColumnIndex(BfwContract.Coops.COLUMN_VICECHAIR_GENDER));
                    String secGender = cursor.getString(cursor.getColumnIndex(BfwContract.Coops.COLUMN_SECRETARY_GENDER));

                    String chairCell = cursor.getString(cursor.getColumnIndex(BfwContract.Coops.COLUMN_CHAIR_CELL));
                    String viceChairCell = cursor.getString(cursor.getColumnIndex(BfwContract.Coops.COLUMN_VICECHAIR_CELL));
                    String secCell = cursor.getString(cursor.getColumnIndex(BfwContract.Coops.COLUMN_SECRETARY_CELL));

                    String yearRcaRegistration = cursor.getString(cursor.getColumnIndex(BfwContract.Coops.COLUMN_RCA_REGISTRATION));

                    //Available resource
                    Boolean isOfficeSpace = cursor.getInt(cursor.getColumnIndex(BfwContract.Coops.COLUMN_OFFICE_SPACE)) == 1;
                    Boolean isMoistureMeter = cursor.getInt(cursor.getColumnIndex(BfwContract.Coops.COLUMN_MOISTURE_METER)) == 1;
                    Boolean isWeightingScales = cursor.getInt(cursor.getColumnIndex(BfwContract.Coops.COLUMN_WEIGHTNING_SCALES)) == 1;
                    Boolean isQualityInput = cursor.getInt(cursor.getColumnIndex(BfwContract.Coops.COLUMN_QUALITY_INPUT)) == 1;
                    Boolean isTractor = cursor.getInt(cursor.getColumnIndex(BfwContract.Coops.COLUMN_TRACTORS)) == 1;
                    Boolean isHarvester = cursor.getInt(cursor.getColumnIndex(BfwContract.Coops.COLUMN_HARVESTER)) == 1;
                    Boolean isDryer = cursor.getInt(cursor.getColumnIndex(BfwContract.Coops.COLUMN_DRYER)) == 1;
                    Boolean isTresher = cursor.getInt(cursor.getColumnIndex(BfwContract.Coops.COLUMN_THRESHER)) == 1;
                    Boolean isSafeStorage = cursor.getInt(cursor.getColumnIndex(BfwContract.Coops.COLUMN_THRESHER)) == 1;
                    Boolean isOtherResourceInfo = cursor.getInt(cursor.getColumnIndex(BfwContract.Coops.COLUMN_THRESHER)) == 1;

                    String textSafeStorage = cursor.getString(cursor.getColumnIndex(BfwContract.Coops.COLUMN_STORAGE_DETAILS));
                    String textOtherResourceInfo = cursor.getString(cursor.getColumnIndex(BfwContract.Coops.COLUMN_OTHER_DETAILS));

                    EventBus.getDefault().post(new ProcessingCoopEvent("Processing your request ..."));

                    // handle coops table
                    String bodyInfo = "{" +
                            "\"name\" : \"" + name + "\"," +
                            "\"phone\" : \"" + phoneNumber + "\"," +
                            "\"address\" : \"" + address + "\"," +
                            "\"email\" : \"" + email + "\",";

                    if (chairName != null) {
                        bodyInfo = bodyInfo + "\"chair_name\" : \"" + chairName + "\",";
                    } else {
                        bodyInfo = bodyInfo + "\"chair_name\" : " + null + ",";
                    }

                    if (viceChairName != null) {
                        bodyInfo = bodyInfo + "\"vicechair_name\" : \"" + viceChairName + "\",";
                    } else {
                        bodyInfo = bodyInfo + "\"vicechair_name\" : " + null + ",";
                    }

                    if (secName != null) {
                        bodyInfo = bodyInfo + "\"secretary_name\" : \"" + secName + "\",";
                    } else {
                        bodyInfo = bodyInfo + "\"secretary_name\" : " + null + ",";
                    }

                    if (chairGender != null) {
                        bodyInfo = bodyInfo + "\"chair_gender\" : \"" + chairGender + "\",";
                    } else {
                        bodyInfo = bodyInfo + "\"chair_gender\" : " + null + ",";
                    }

                    if (viceChairGender != null) {
                        bodyInfo = bodyInfo + "\"vicechair_gender\" : \"" + viceChairGender + "\",";
                    } else {
                        bodyInfo = bodyInfo + "\"vicechair_gender\" : " + null + ",";
                    }

                    if (secGender != null) {
                        bodyInfo = bodyInfo + "\"secretary_gender\" : \"" + secGender + "\",";
                    } else {
                        bodyInfo = bodyInfo + "\"secretary_gender\" : " + null + ",";
                    }

                    if (chairCell != null) {
                        bodyInfo = bodyInfo + "\"chair_cell\" : \"" + chairCell + "\",";
                    } else {
                        bodyInfo = bodyInfo + "\"chair_cell\" : " + null + ",";
                    }

                    if (viceChairCell != null) {
                        bodyInfo = bodyInfo + "\"vicechair_cell\" : \"" + viceChairCell + "\",";
                    } else {
                        bodyInfo = bodyInfo + "\"vicechair_cell\" : " + null + ",";
                    }

                    if (secCell != null) {
                        bodyInfo = bodyInfo + "\"secretary_cell\" : \"" + secCell + "\",";
                    } else {
                        bodyInfo = bodyInfo + "\"secretary_cell\" : " + null + ",";
                    }

                    if (yearRcaRegistration != null) {
                        bodyInfo = bodyInfo + "\"rca_year_registration\" : \"" + yearRcaRegistration + "\",";
                    } else {
                        bodyInfo = bodyInfo + "\"rca_year_registration\" : " + null + ",";
                    }

                    if (textSafeStorage != null) {
                        bodyInfo = bodyInfo + "\"storage_details\" : \"" + textSafeStorage + "\",";
                    } else {
                        bodyInfo = bodyInfo + "\"storage_details\" : " + null + ",";
                    }

                    if (textOtherResourceInfo != null) {
                        bodyInfo = bodyInfo + "\"other_details\" : \"" + textOtherResourceInfo + "\",";
                    } else {
                        bodyInfo = bodyInfo + "\"other_details\" : " + null + ",";
                    }

                    bodyInfo = bodyInfo + "\"cr_office_space\" : " + isOfficeSpace + "," +
                            "\"cr_moisture_meter\" : " + isMoistureMeter + "," +
                            "\"cr_weighting_scales\" : " + isWeightingScales + "," +
                            "\"cr_quality_inputs\" : " + isQualityInput + "," +
                            "\"cr_tractors\" : " + isTractor + "," +
                            "\"cr_harvesters\" : " + isHarvester + "," +
                            "\"cr_dryer\" : " + isDryer + "," +
                            "\"cr_thresher\" : " + isTresher + "," +
                            "\"cr_safe_storage\" : " + isSafeStorage + "," +
                            "\"cr_other\" : " + isOtherResourceInfo + "" +
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
                                coopServerId = farmerInfo.getInt("id");

                                ContentValues contentValues = new ContentValues();
                                contentValues.put(BfwContract.Coops.COLUMN_COOP_SERVER_ID, coopServerId);
                                contentValues.put(BfwContract.Coops.COLUMN_IS_SYNC, 1);
                                contentValues.put(BfwContract.Coops.COLUMN_IS_UPDATE, 1);
                                getContentResolver().update(BfwContract.Coops.CONTENT_URI, contentValues, coopSelection, new String[]{Long.toString(id)});
                            }
                        }


                        // baseline finance info table
                        coopInfoCursor = getContentResolver().query(BfwContract.FinanceInfoCoop.CONTENT_URI, null, financeCoopDataSelection,
                                new String[]{Long.toString(id)}, null);

                        if (coopInfoCursor != null) {

                            int financeInfoId;

                            String input_loan;

                            Boolean isInput_loan_prov_bank;
                            Boolean isInput_loan_prov_cooperative;
                            Boolean isInput_loan_prov_sacco;
                            Boolean isInput_loan_prov_other;

                            Double input_loan_amount;
                            Double input_loan_interest_rate;
                            Integer input_loan_duration;

                            Boolean sInput_loan_purpose_labour;
                            Boolean sInput_loan_purpose_seed;
                            Boolean sInput_loan_purpose_input;
                            Boolean sInput_loan_purpose_machinery;
                            Boolean sInput_loan_purpose_other;

                            Boolean isCash_provided_purchase_inputs;

                            String aggrgation_post_harvset_loan;

                            Boolean isAgg_post_harv_loan_prov_bank;
                            Boolean isAgg_post_harv_loan_prov_cooperative;
                            Boolean isAgg_post_harv_loan_prov_sacco;
                            Boolean isAgg_post_harv_loan_prov_other;

                            Double aggrgation_post_harvset_amount;
                            Double aggrgation_post_harvset_loan_interest;
                            Integer aggrgation_post_harvset_loan_duration;

                            Boolean isAgg_post_harv_loan_purpose_labour;
                            Boolean isAgg_post_harv_loan_purpose_input;
                            Boolean isAgg_post_harv_loan_purpose_machinery;
                            Boolean isAgg_post_harv_loan_purpose_other;

                            String aggrgation_post_harvset_laon_disbursement_method;

                            int baselineFinanceInfoSeasonId;

                            while (coopInfoCursor.moveToNext()) {

                                financeInfoId = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop._ID));
                                input_loan = coopInfoCursor.getString(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_CYCLE_LOAN));

                                isInput_loan_prov_bank = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PROVIDER_BANK)) == 1;
                                isInput_loan_prov_cooperative = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PROVIDER_COOPERATIVE)) == 1;
                                isInput_loan_prov_sacco = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PROVIDER_SACCO)) == 1;
                                isInput_loan_prov_other = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PROVIDER_OTHER)) == 1;

                                input_loan_amount = coopInfoCursor.getDouble(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_CYCLE_LOAN_AMOUNT));
                                input_loan_interest_rate = coopInfoCursor.getDouble(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_CYCLE_INTEREST_RATE));
                                input_loan_duration = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_CYCLE_LOAN_DURATION));

                                sInput_loan_purpose_labour = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_LABOUR)) == 1;
                                sInput_loan_purpose_seed = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_SEEDS)) == 1;
                                sInput_loan_purpose_machinery = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_MACHINERY)) == 1;
                                sInput_loan_purpose_input = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_INPUT)) == 1;
                                sInput_loan_purpose_other = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_OTHER)) == 1;

                                isCash_provided_purchase_inputs = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_DISBURSEMENT_METHOD)) == 1;

                                aggrgation_post_harvset_loan = coopInfoCursor.getString(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN));

                                isAgg_post_harv_loan_prov_bank = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PROVIDER_BANK)) == 1;
                                isAgg_post_harv_loan_prov_cooperative = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PROVIDER_COOPERATIVE)) == 1;
                                isAgg_post_harv_loan_prov_sacco = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PROVIDER_SACCO)) == 1;
                                isAgg_post_harv_loan_prov_other = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PROVIDER_OTHER)) == 1;

                                aggrgation_post_harvset_amount = coopInfoCursor.getDouble(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_AMOUNT));
                                aggrgation_post_harvset_loan_interest = coopInfoCursor.getDouble(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_INTEREST_RATE));
                                aggrgation_post_harvset_loan_duration = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_DURATION));

                                isAgg_post_harv_loan_purpose_labour = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PURPOSE_LABOUR)) == 1;
                                isAgg_post_harv_loan_purpose_input = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PURPOSE_INPUT)) == 1;
                                isAgg_post_harv_loan_purpose_machinery = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PURPOSE_MACHINERY)) == 1;
                                isAgg_post_harv_loan_purpose_other = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PURPOSE_OTHER)) == 1;

                                aggrgation_post_harvset_laon_disbursement_method = coopInfoCursor.getString(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_CYCLE_LOAN_DISB_METHOD));

                                baselineFinanceInfoSeasonId = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_SEASON_ID));

                                serverSeasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null,
                                        seasonSelection, new String[]{Long.toString(baselineFinanceInfoSeasonId)}, null);

                                if (serverSeasonCursor != null && serverSeasonCursor.moveToFirst()) {
                                    serverSeasonId = serverSeasonCursor.getInt(serverSeasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_SERVER_ID));
                                }

                                if (coopServerId > 0) {

                                    String baselineInFoData = "{" +
                                            "\"pclp_bankl\": " + isInput_loan_prov_bank + "," +
                                            "\"pclp_cooperative\": " + isInput_loan_prov_cooperative + "," +
                                            "\"pclp_sacco\": " + isInput_loan_prov_sacco + "," +
                                            "\"pclp_other\": " + isInput_loan_prov_other + "," +

                                            "\"prev_cycle_loan_amount\": " + input_loan_amount + "," +
                                            "\"prev_cycle_interest_rate\": " + input_loan_interest_rate + "," +
                                            "\"prev_cycle_loan_duration\": " + input_loan_duration + "," +

                                            "\"pc_loan_purpose_labour\": " + sInput_loan_purpose_labour + "," +
                                            "\"pc_loan_purpose_seeds\": " + sInput_loan_purpose_seed + "," +
                                            "\"pc_loan_purpose_input\": " + sInput_loan_purpose_input + "," +
                                            "\"pc_loan_purpose_machinery\": " + sInput_loan_purpose_machinery + "," +
                                            "\"pc_loan_purpose_other\": " + sInput_loan_purpose_other + "," +

                                            "\"aphlp_bank\": " + isAgg_post_harv_loan_prov_bank + "," +
                                            "\"aphlp_cooperative\": " + isAgg_post_harv_loan_prov_cooperative + "," +
                                            "\"aphlp_sacco\": " + isAgg_post_harv_loan_prov_sacco + "," +
                                            "\"aphlp_other\": " + isAgg_post_harv_loan_prov_other + "," +

                                            "\"aphl_amount\": " + aggrgation_post_harvset_amount + "," +
                                            "\"aphl_interest_rate\": " + aggrgation_post_harvset_loan_interest + "," +
                                            "\"aphl_duration\": " + aggrgation_post_harvset_loan_duration + "," +

                                            "\"aph_loan_purpose_labour\": " + isAgg_post_harv_loan_purpose_labour + "," +
                                            "\"aph_loan_purpose_input\": " + isAgg_post_harv_loan_purpose_input + "," +
                                            "\"aph_loan_purpose_machinery\": " + isAgg_post_harv_loan_purpose_machinery + "," +
                                            "\"aph_loan_purpose_other\": " + isAgg_post_harv_loan_purpose_other + ",";


                                    if (input_loan != null) {
                                        baselineInFoData = baselineInFoData + "\"previous_cycle_loan\": \"" + input_loan + "\",";
                                    } else {
                                        baselineInFoData = baselineInFoData + "\"previous_cycle_loan\": " + null + ",";
                                    }

                                    if (aggrgation_post_harvset_loan != null) {
                                        baselineInFoData = baselineInFoData + "\"agregation_post_harvest_loan\": \"" + aggrgation_post_harvset_loan + "\",";
                                    } else {
                                        baselineInFoData = baselineInFoData + "\"agregation_post_harvest_loan\": " + null + ",";
                                    }

                                    if (aggrgation_post_harvset_laon_disbursement_method != null) {
                                        baselineInFoData = baselineInFoData + "\"aph_loan_disb_method\": \"" + aggrgation_post_harvset_laon_disbursement_method + "\",";
                                    } else {
                                        baselineInFoData = baselineInFoData + "\"aph_loan_disb_method\": " + null + ",";
                                    }

                                    if (isCash_provided_purchase_inputs) {
                                        baselineInFoData = baselineInFoData + "\"prev_cycle_loan_disb_method\": \"cash\",";
                                    } else {
                                        baselineInFoData = baselineInFoData + "\"prev_cycle_loan_disb_method\":  \"kind\",";
                                    }


                                    baselineInFoData = baselineInFoData + "\"harvest_id\": " + serverSeasonId + "," +
                                            "\"coop_id\": " + coopServerId + "" +
                                            "}";

                                    String API_INFO = BuildConfig.DEV_API_URL + "baseline.finance.info.coop";

                                    RequestBody bodyLand = RequestBody.create(JSON, baselineInFoData);

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
                                            contentValues.put(BfwContract.FinanceInfoCoop.COLUMN_SERVER_ID, infoServerId);
                                            contentValues.put(BfwContract.FinanceInfoCoop.COLUMN_IS_SYNC, 1);
                                            contentValues.put(BfwContract.FinanceInfoCoop.COLUMN_IS_UPDATE, 1);

                                            getContentResolver().update(BfwContract.FinanceInfoCoop.CONTENT_URI, contentValues, financeCoopDataInfo,
                                                    new String[]{Long.toString(financeInfoId)});
                                        }
                                    }
                                }
                            }
                        }

                        // baseline sales table
                        coopInfoCursor = getContentResolver().query(BfwContract.BaselineSalesCoop.CONTENT_URI, null, baselineSalesCoopSelection,
                                new String[]{Long.toString(id)}, null);

                        if (coopInfoCursor != null) {

                            int baselineSalesId;
                            int baselineSalesSeasonId;

                            String rgccContractUnderFtma;

                            Integer qtyAgregatedFromMember;
                            Integer cycleHarvsetAtPricePerKg;
                            Integer qtyPurchaseFromNonMember;
                            Integer nonMemberAtPricePerKg;

                            Double qtyOfRgccContract;
                            Double qtySoldToRgcc;
                            Double pricePerKgSoldToRgcc;
                            Double qtySoldOutOfRgcc;
                            Double pricePerKkSoldOutFtma;

                            Boolean isFormalBuyer;
                            Boolean isInformalBuyer;
                            Boolean isOtherB;

                            while (coopInfoCursor.moveToNext()) {

                                baselineSalesId = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.BaselineSalesCoop._ID));

                                rgccContractUnderFtma = coopInfoCursor.getString(coopInfoCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_BUYER_CONTRACT));

                                qtyAgregatedFromMember = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_CYCLE_HARVEST));
                                cycleHarvsetAtPricePerKg = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_CYCLE_HARVEST_PRICE));
                                qtyPurchaseFromNonMember = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_NON_MEMBER_PURCHASE));
                                nonMemberAtPricePerKg = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_NON_MEMBER_PURCHASE_COST));

                                qtyOfRgccContract = coopInfoCursor.getDouble(coopInfoCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_QUANTITY_SOLD_RGCC));
                                qtySoldToRgcc = coopInfoCursor.getDouble(coopInfoCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_CONTRACT_VOLUME));
                                pricePerKgSoldToRgcc = coopInfoCursor.getDouble(coopInfoCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_PRICE_SOLD_RGCC));
                                qtySoldOutOfRgcc = coopInfoCursor.getDouble(coopInfoCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_QUANTITY_SOLD_OUTSIDE_RGCC));
                                pricePerKkSoldOutFtma = coopInfoCursor.getDouble(coopInfoCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_PRICE_SOLD_OUTSIDE_RGCC));

                                isFormalBuyer = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_RGCC_BUYER_FORMAL)) == 1;
                                isInformalBuyer = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_RGCC_INFORMAL)) == 1;
                                isOtherB = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_BUYER_OTHER)) == 1;

                                baselineSalesSeasonId = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_SEASON_ID));

                                serverSeasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null,
                                        seasonSelection, new String[]{Long.toString(baselineSalesSeasonId)}, null);

                                if (serverSeasonCursor != null && serverSeasonCursor.moveToFirst()) {
                                    serverSeasonId = serverSeasonCursor.getInt(serverSeasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_SERVER_ID));
                                }

                                if (coopServerId > 0) {

                                    String baselineSalesInfoData = "{" +
                                            "\"prev_cycle_harvest\": " + qtyAgregatedFromMember + "," +
                                            "\"prev_cycle_harvest_price\": " + cycleHarvsetAtPricePerKg + "," +
                                            "\"prev_non_members_purchase\": " + qtyPurchaseFromNonMember + "," +
                                            "\"prev_non_members_purchase_cost\": " + nonMemberAtPricePerKg + "," +

                                            "\"rgcc_contract_volume\": " + qtyOfRgccContract + "," +
                                            "\"quantity_sold_rgcc\": " + qtySoldToRgcc + "," +
                                            "\"price_sold_rgcc\": " + pricePerKgSoldToRgcc + "," +
                                            "\"quantity_sold_outside_rgcc\": " + qtySoldOutOfRgcc + "," +
                                            "\"price_sold_outside_rgcc\": " + pricePerKkSoldOutFtma + "," +

                                            "\"rgcc_buyer_formal\": " + isFormalBuyer + "," +
                                            "\"rgcc_buyer_informal\": " + isInformalBuyer + "," +
                                            "\"rgcc_buyer_other\": " + isOtherB + ",";

                                    if (rgccContractUnderFtma != null) {
                                        baselineSalesInfoData = baselineSalesInfoData + "\"rgcc_buyer_contract\": \"" + rgccContractUnderFtma + "\",";
                                    } else {
                                        baselineSalesInfoData = baselineSalesInfoData + "\"rgcc_buyer_contract\": " + null + ",";
                                    }

                                    baselineSalesInfoData = baselineSalesInfoData + "\"harvest_id\": " + serverSeasonId + "," +
                                            "\"coop_id\": " + coopServerId + "" +
                                            "}";

                                    String API_INFO = BuildConfig.DEV_API_URL + "baseline.sales.coop";

                                    RequestBody bodyLand = RequestBody.create(JSON, baselineSalesInfoData);

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
                                            contentValues.put(BfwContract.BaselineSalesCoop.COLUMN_SERVER_ID, infoServerId);
                                            contentValues.put(BfwContract.BaselineSalesCoop.COLUMN_IS_SYNC, 1);
                                            contentValues.put(BfwContract.BaselineSalesCoop.COLUMN_IS_UPDATE, 1);

                                            getContentResolver().update(BfwContract.BaselineSalesCoop.CONTENT_URI, contentValues, baselineSalesCoopInfo,
                                                    new String[]{Long.toString(baselineSalesId)});
                                        }
                                    }

                                }
                            }
                        }

                        // handle baseline yield table
                        coopInfoCursor = getContentResolver().query(BfwContract.YieldCoop.CONTENT_URI, null, baselineYieldSelection,
                                new String[]{Long.toString(id)}, null);

                        if (coopInfoCursor != null) {

                            int baselineYieldId;
                            int baselineYieldSeasonId;
                            Boolean isMaize;
                            Boolean isBean;
                            Boolean isSoy;
                            Boolean isOther;

                            while (coopInfoCursor.moveToNext()) {

                                baselineYieldId = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.YieldCoop._ID));
                                isMaize = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.YieldCoop.COLUMN_MAIZE)) == 1;
                                isBean = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.YieldCoop.COLUMN_BEAN)) == 1;
                                isSoy = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.YieldCoop.COLUMN_SOY)) == 1;
                                isOther = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.YieldCoop.COLUMN_OTHER)) == 1;
                                baselineYieldSeasonId = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.YieldCoop.COLUMN_SEASON_ID));

                                serverSeasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null,
                                        seasonSelection, new String[]{Long.toString(baselineYieldSeasonId)}, null);

                                if (serverSeasonCursor != null && serverSeasonCursor.moveToFirst()) {
                                    serverSeasonId = serverSeasonCursor.getInt(serverSeasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_SERVER_ID));
                                }

                                if (coopServerId > 0) {

                                    String yieldInFoData = "{" +
                                            "\"mp_maize\": " + isMaize + "," +
                                            "\"mp_bean\": " + isBean + "," +
                                            "\"mp_soy\": " + isSoy + "," +
                                            "\"mp_other\": " + isOther + "," +
                                            "\"harvest_id\": " + serverSeasonId + "," +
                                            "\"coop_id\": " + coopServerId + "" +
                                            "}";

                                    String API_INFO = BuildConfig.DEV_API_URL + "baseline.yield.coop";

                                    RequestBody bodyLand = RequestBody.create(JSON, yieldInFoData);

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
                                            contentValues.put(BfwContract.YieldCoop.COLUMN_SERVER_ID, infoServerId);
                                            contentValues.put(BfwContract.YieldCoop.COLUMN_IS_SYNC, 1);
                                            contentValues.put(BfwContract.YieldCoop.COLUMN_IS_UPDATE, 1);

                                            getContentResolver().update(BfwContract.YieldCoop.CONTENT_URI, contentValues, baselineYieldInfo,
                                                    new String[]{Long.toString(baselineYieldId)});
                                        }
                                    }

                                }
                            }
                        }

                        // handle forecast sales table
                        coopInfoCursor = getContentResolver().query(BfwContract.SalesCoop.CONTENT_URI, null, forecastSalesCoopSelection,
                                new String[]{Long.toString(id)}, null);

                        if (coopInfoCursor != null) {

                            int forecastSaleId;
                            int forecastSeasonId;

                            String minFloorPerGrade;
                            String grade;

                            Integer commitedContractQty;

                            Boolean rgcc;
                            Boolean prodev;
                            Boolean sarura;
                            Boolean aif;
                            Boolean eax;
                            Boolean none;
                            Boolean other;

                            while (coopInfoCursor.moveToNext()) {

                                forecastSaleId = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.SalesCoop._ID));
                                forecastSeasonId = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.SalesCoop.COLUMN_SEASON_ID));

                                grade = coopInfoCursor.getString(coopInfoCursor.getColumnIndex(BfwContract.SalesCoop.COLUMN_COOP_GRADE));
                                minFloorPerGrade = coopInfoCursor.getString(coopInfoCursor.getColumnIndex(BfwContract.SalesCoop.COLUMN_FLOOR_GRADE));

                                commitedContractQty = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.SalesCoop.COLUMN_CONTRACT_VOLUME));

                                rgcc = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.SalesCoop.COLUMN_RGCC)) == 1;
                                prodev = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.SalesCoop.COLUMN_PRODEV)) == 1;
                                sarura = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.SalesCoop.COLUMN_SAKURA)) == 1;
                                aif = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.SalesCoop.COLUMN_AIF)) == 1;
                                eax = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.SalesCoop.COLUMN_EAX)) == 1;
                                none = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.SalesCoop.COLUMN_NONE)) == 1;
                                other = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.SalesCoop.COLUMN_OTHER)) == 1;

                                serverSeasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null,
                                        seasonSelection, new String[]{Long.toString(forecastSeasonId)}, null);

                                if (serverSeasonCursor != null && serverSeasonCursor.moveToFirst()) {
                                    serverSeasonId = serverSeasonCursor.getInt(serverSeasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_SERVER_ID));
                                }

                                if (coopServerId > 0) {

                                    String accInFoData = "{" +
                                            "\"ebc_rgcc\": " + rgcc + "," +
                                            "\"ebc_prodev\": " + prodev + "," +
                                            "\"ebc_sakura\": " + sarura + "," +
                                            "\"ebc_aif\": " + aif + "," +
                                            "\"ebc_eax\": " + eax + "," +
                                            "\"ebc_none\": " + none + "," +
                                            "\"ebc_other\": " + other + "," +
                                            "\"contract_volume\": " + commitedContractQty + ",";

                                    if (grade != null) {
                                        accInFoData = accInFoData + "\"coop_grade\": \"" + grade + "\",";
                                    } else {
                                        accInFoData = accInFoData + "\"coop_grade\": " + null + ",";
                                    }

                                    if (minFloorPerGrade != null) {
                                        accInFoData = accInFoData + "\"coop_minimum_floor_grade\": \"" + minFloorPerGrade + "\",";
                                    } else {
                                        accInFoData = accInFoData + "\"coop_minimum_floor_grade\": " + null + ",";
                                    }

                                    accInFoData = accInFoData + "\"harvest_id\": " + serverSeasonId + "," +
                                            "\"coop_id\": " + coopServerId + "" +
                                            "}";

                                    String API_INFO = BuildConfig.DEV_API_URL + "forecast.sales.coop";

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
                                            contentValues.put(BfwContract.SalesCoop.COLUMN_SERVER_ID, infoServerId);
                                            contentValues.put(BfwContract.SalesCoop.COLUMN_IS_SYNC, 1);
                                            contentValues.put(BfwContract.SalesCoop.COLUMN_IS_UPDATE, 1);

                                            getContentResolver().update(BfwContract.SalesCoop.CONTENT_URI, contentValues, forecastSalesCoopInfo,
                                                    new String[]{Long.toString(forecastSaleId)});
                                        }
                                    }
                                }
                            }
                        }

                        // handle access  to information table
                        coopInfoCursor = getContentResolver().query(BfwContract.CoopInfo.CONTENT_URI, null, accessInfoSelection,
                                new String[]{Long.toString(id)}, null);

                        if (coopInfoCursor != null) {

                            int accessInfoId;
                            int accessInfoSeasonId;

                            Boolean isAgriculture;
                            Boolean isClimateInfo;
                            Boolean isSeeds;
                            Boolean organicFertilizers;

                            Boolean inorganicFertilizers;
                            Boolean labour;
                            Boolean waterPumps;
                            Boolean isSpreader;

                            while (coopInfoCursor.moveToNext()) {

                                accessInfoId = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.CoopInfo._ID));

                                accessInfoSeasonId = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.CoopInfo.COLUMN_SEASON_ID));

                                isAgriculture = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.CoopInfo.COLUMN_AGRI_EXTENSION)) == 1;
                                isClimateInfo = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.CoopInfo.COLUMN_CLIMATE_INFO)) == 1;
                                isSeeds = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.CoopInfo.COLUMN_SEEDS)) == 1;

                                organicFertilizers = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.CoopInfo.COLUMN_ORGANIC_FERT)) == 1;
                                inorganicFertilizers = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.CoopInfo.COLUMN_INORGANIC_FERT)) == 1;
                                labour = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.CoopInfo.COLUMN_LABOUR)) == 1;
                                waterPumps = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.CoopInfo.COLUMN_WATER_PUMPS)) == 1;
                                isSpreader = coopInfoCursor.getInt(coopInfoCursor.getColumnIndex(BfwContract.CoopInfo.COLUMN_SPREADER)) == 1;

                                serverSeasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null,
                                        seasonSelection, new String[]{Long.toString(accessInfoSeasonId)}, null);

                                if (serverSeasonCursor != null && serverSeasonCursor.moveToNext()) {
                                    serverSeasonId = serverSeasonCursor.getInt(serverSeasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_SERVER_ID));
                                }

                                if (coopServerId > 0) {

                                    String accInFoData = "{" +
                                            "\"cr_agricultural_extension\": " + isAgriculture + "," +
                                            "\"cr_climate_related_info\": " + isClimateInfo + "," +
                                            "\"car_seeds\": " + isSeeds + "," +
                                            "\"car_of\": " + organicFertilizers + "," +
                                            "\"car_if\": " + inorganicFertilizers + "," +
                                            "\"car_labour\": " + labour + "," +
                                            "\"car_iwp\": " + waterPumps + "," +
                                            "\"car_ss\": " + isSpreader + "," +
                                            "\"harvest_id\": " + serverSeasonId + "," +
                                            "\"coop_id\": " + coopServerId + "" +
                                            "}";

                                    String API_INFO = BuildConfig.DEV_API_URL + "cooperative.access.info";

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
                                            contentValues.put(BfwContract.CoopInfo.COLUMN_SERVER_ID, infoServerId);
                                            contentValues.put(BfwContract.CoopInfo.COLUMN_IS_SYNC, 1);
                                            contentValues.put(BfwContract.CoopInfo.COLUMN_IS_UPDATE, 1);

                                            getContentResolver().update(BfwContract.CoopInfo.CONTENT_URI, contentValues, accessCoopInfo,
                                                    new String[]{Long.toString(accessInfoId)});
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException | JSONException exp) {
                        EventBus.getDefault().post(new SyncDataEvent(getResources().getString(R.string.syncing_error_coop), false));
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (coopInfoCursor != null) {
                coopInfoCursor.close();
            }
            if (serverSeasonCursor != null) {
                serverSeasonCursor.close();
            }
        }

        //post event sync after
        if (dataCount > 0)
            EventBus.getDefault().post(new SyncDataEvent("Coop Added successfully", true));
    }
}

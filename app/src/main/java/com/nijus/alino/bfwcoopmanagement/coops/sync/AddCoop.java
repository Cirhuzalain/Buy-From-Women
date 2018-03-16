package com.nijus.alino.bfwcoopmanagement.coops.sync;

import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.AccessToInformation;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.AvailableResources;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.BaselineFinanceInfo;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.BaselineSales;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.BaselineYield;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.ForecastSales;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.GeneralInformation;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.InternalInformation;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.UUID;

public class AddCoop extends IntentService {

    public final String LOG_TAG = AddCoop.class.getSimpleName();

    public AddCoop() {
        super("");
    }

    public AddCoop(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {
            Bundle farmerData = intent.getBundleExtra("coop_data");

            // grab data
            GeneralInformation general = farmerData.getParcelable("general");
            InternalInformation internalInformation = farmerData.getParcelable("internalInformation");
            AvailableResources availableResources = farmerData.getParcelable("availableResources");

            HashMap<String, AccessToInformation> accessToInformation = (HashMap<String, AccessToInformation>) farmerData.getSerializable("accessToInformation");
            HashMap<String, ForecastSales> forecastSales = (HashMap<String, ForecastSales>) farmerData.getSerializable("forecast_sales");
            HashMap<String, BaselineYield> baseLineYield = (HashMap<String, BaselineYield>) farmerData.getSerializable("baselines_yield");
            HashMap<String, BaselineSales> baselineSales = (HashMap<String, BaselineSales>) farmerData.getSerializable("baseline_sales");
            HashMap<String, BaselineFinanceInfo> baselineFinanceInfo = (HashMap<String, BaselineFinanceInfo>) farmerData.getSerializable("baseline_finance_info");

            Cursor cursor = null;
            String seasonName;

            //general information
            String name = null, phoneNumber = null, address = null, email = null;

            //internal information
            String chairName = null;
            String viceChairName = null;
            String secName = null;

            Boolean isChairGender = false;
            Boolean isViceChairGender = false;
            Boolean isSecGender = false;

            String chairCell = null;
            String viceChairCell = null;
            String secCell = null;

            String yearRcaRegistration = null;

            //Available resource
            Boolean isOfficeSpace = false;
            Boolean isMoistureMeter = false;
            Boolean isWeightingScales = false;
            Boolean isQualityInput = false;
            Boolean isTractor = false;
            Boolean isHarvester = false;
            Boolean isDryer = false;
            Boolean isTresher = false;
            Boolean isSafeStorage = false;
            Boolean isOtherResourceInfo = false;

            String textSafeStorage = null;
            String textOtherResourceInfo = null;

            //Access to information
            Boolean isAgriculture = false, isClimateInfo = false, isSeeds = false, organicFertilizers = false,
                    inorganicFertilizers = false, labour = false,
                    waterPumps = false, isSpreader = false;

            //Forecast sales
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

            //Baseline Yield
            Boolean isMaize;
            Boolean isBean;
            Boolean isSoy;
            Boolean isOther;

            //Baseline Sales
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

            //Baseline finance info
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

            // handle coops table
            if (general != null) {
                name = general.getName();
                phoneNumber = general.getPhone();
                address = general.getAddress();
                email = general.getMail();
            }

            if (internalInformation != null) {

                chairName = internalInformation.getChairName();
                viceChairName = internalInformation.getViceChairName();
                secName = internalInformation.getSecName();

                isChairGender = internalInformation.isChairGender();
                isViceChairGender = internalInformation.isViceChairGender();
                isSecGender = internalInformation.isSecGender();

                chairCell = internalInformation.getChairCell();
                viceChairCell = internalInformation.getViceChairCell();
                secCell = internalInformation.getSecCell();

                yearRcaRegistration = internalInformation.getYearRcaRegistration();

            }

            if (availableResources != null) {

                isOfficeSpace = availableResources.isOfficeSpace();
                isMoistureMeter = availableResources.isMoistureMeter();
                isWeightingScales = availableResources.isWeightingScales();
                isQualityInput = availableResources.isQualityInput();
                isTractor = availableResources.isTractor();
                isHarvester = availableResources.isHarvester();
                isDryer = availableResources.isDryer();
                isTresher = availableResources.isTresher();
                isSafeStorage = availableResources.isSafeStorage();
                isOtherResourceInfo = availableResources.isOtherResourceInfo();

                textSafeStorage = availableResources.getTextSafeStorage();
                textOtherResourceInfo = availableResources.getTextOtherResourceInfo();

            }

            ContentValues contentValues = new ContentValues();
            contentValues.put(BfwContract.Coops.COLUMN_COOP_NAME, name);
            contentValues.put(BfwContract.Coops.COLUMN_EMAIL, email);
            contentValues.put(BfwContract.Coops.COLUMN_PHONE, phoneNumber);
            contentValues.put(BfwContract.Coops.COLUMN_ADDRESS, address);

            contentValues.put(BfwContract.Coops.COLUMN_CHAIR_NAME, chairName);
            contentValues.put(BfwContract.Coops.COLUMN_CHAIR_GENDER, isChairGender ? "male" : "female");
            contentValues.put(BfwContract.Coops.COLUMN_CHAIR_CELL, chairCell);

            contentValues.put(BfwContract.Coops.COLUMN_VICECHAIR_NAME, viceChairName);
            contentValues.put(BfwContract.Coops.COLUMN_VICECHAIR_CELL, viceChairCell);
            contentValues.put(BfwContract.Coops.COLUMN_VICECHAIR_GENDER, isViceChairGender ? "male" : "female");

            contentValues.put(BfwContract.Coops.COLUMN_SECRETARY_NAME, secName);
            contentValues.put(BfwContract.Coops.COLUMN_SECRETARY_GENDER, isSecGender ? "male" : "female");
            contentValues.put(BfwContract.Coops.COLUMN_SECRETARY_CELL, secCell);

            contentValues.put(BfwContract.Coops.COLUMN_RCA_REGISTRATION, yearRcaRegistration);
            contentValues.put(BfwContract.Coops.COLUMN_LAND_SIZE_CIP, 0);
            contentValues.put(BfwContract.Coops.COLUMN_LAND_SIZE_CIP2, 0);

            contentValues.put(BfwContract.Coops.COLUMN_OFFICE_SPACE, isOfficeSpace ? 1 : 0);
            contentValues.put(BfwContract.Coops.COLUMN_MOISTURE_METER, isMoistureMeter ? 1 : 0);
            contentValues.put(BfwContract.Coops.COLUMN_WEIGHTNING_SCALES, isWeightingScales ? 1 : 0);
            contentValues.put(BfwContract.Coops.COLUMN_QUALITY_INPUT, isQualityInput ? 1 : 0);
            contentValues.put(BfwContract.Coops.COLUMN_TRACTORS, isTractor ? 1 : 0);
            contentValues.put(BfwContract.Coops.COLUMN_HARVESTER, isHarvester ? 1 : 0);
            contentValues.put(BfwContract.Coops.COLUMN_DRYER, isDryer ? 1 : 0);
            contentValues.put(BfwContract.Coops.COLUMN_THRESHER, isTresher ? 1 : 0);
            contentValues.put(BfwContract.Coops.COLUMN_SAFE_STORAGE, isSafeStorage ? 1 : 0);
            contentValues.put(BfwContract.Coops.COLUMN_OTHER, isOtherResourceInfo ? 1 : 0);
            contentValues.put(BfwContract.Coops.COLUMN_MALE_COOP, 0);
            contentValues.put(BfwContract.Coops.COLUMN_FEMALE_COOP, 0);
            contentValues.put(BfwContract.Coops.COLUMN_MEMBER, 0);
            contentValues.put(BfwContract.Coops.COLUMN_OTHER_DETAILS, textOtherResourceInfo);
            contentValues.put(BfwContract.Coops.COLUMN_STORAGE_DETAILS, textSafeStorage);
            contentValues.put(BfwContract.Coops.COLUMN_IS_SYNC, 0);
            contentValues.put(BfwContract.Coops.COLUMN_IS_UPDATE, 0);

            Uri uri = getContentResolver().insert(BfwContract.Coops.CONTENT_URI, contentValues);
            long coopId = ContentUris.parseId(uri);

            try {
                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));


                        // handle baseline finance info table
                        if (baselineFinanceInfo != null && baselineFinanceInfo.containsKey(seasonName)) {

                            BaselineFinanceInfo baselineFinanceInfoData = baselineFinanceInfo.get(seasonName);

                            input_loan = baselineFinanceInfoData.getInput_loan();

                            isInput_loan_prov_bank = baselineFinanceInfoData.isInput_loan_prov_bank();
                            isInput_loan_prov_cooperative = baselineFinanceInfoData.isInput_loan_prov_cooperative();
                            isInput_loan_prov_sacco = baselineFinanceInfoData.isInput_loan_prov_sacco();
                            isInput_loan_prov_other = baselineFinanceInfoData.isInput_loan_prov_other();

                            input_loan_amount = baselineFinanceInfoData.getInput_loan_amount();
                            input_loan_interest_rate = baselineFinanceInfoData.getInput_loan_interest_rate();
                            input_loan_duration = baselineFinanceInfoData.getInput_loan_duration();

                            sInput_loan_purpose_labour = baselineFinanceInfoData.issInput_loan_purpose_labour();
                            sInput_loan_purpose_seed = baselineFinanceInfoData.issInput_loan_purpose_seed();
                            sInput_loan_purpose_input = baselineFinanceInfoData.issInput_loan_purpose_input();
                            sInput_loan_purpose_machinery = baselineFinanceInfoData.issInput_loan_purpose_machinery();
                            sInput_loan_purpose_other = baselineFinanceInfoData.issInput_loan_purpose_other();

                            isCash_provided_purchase_inputs = baselineFinanceInfoData.isCash_provided_purchase_inputs();

                            aggrgation_post_harvset_loan = baselineFinanceInfoData.getAggrgation_post_harvset_loan();

                            isAgg_post_harv_loan_prov_bank = baselineFinanceInfoData.isAgg_post_harv_loan_prov_bank();
                            isAgg_post_harv_loan_prov_cooperative = baselineFinanceInfoData.isAgg_post_harv_loan_prov_cooperative();
                            isAgg_post_harv_loan_prov_sacco = baselineFinanceInfoData.isAgg_post_harv_loan_prov_sacco();
                            isAgg_post_harv_loan_prov_other = baselineFinanceInfoData.isAgg_post_harv_loan_prov_other();

                            aggrgation_post_harvset_amount = baselineFinanceInfoData.getAggrgation_post_harvset_amount();
                            aggrgation_post_harvset_loan_interest = baselineFinanceInfoData.getAggrgation_post_harvset_loan_interest();
                            aggrgation_post_harvset_loan_duration = baselineFinanceInfoData.getAggrgation_post_harvset_loan_duration();

                            isAgg_post_harv_loan_purpose_labour = baselineFinanceInfoData.isAgg_post_harv_loan_purpose_labour();
                            isAgg_post_harv_loan_purpose_input = baselineFinanceInfoData.isAgg_post_harv_loan_purpose_input();
                            isAgg_post_harv_loan_purpose_machinery = baselineFinanceInfoData.isAgg_post_harv_loan_purpose_machinery();
                            isAgg_post_harv_loan_purpose_other = baselineFinanceInfoData.isAgg_post_harv_loan_purpose_other();

                            aggrgation_post_harvset_laon_disbursement_method = baselineFinanceInfoData.getAggrgation_post_harvset_laon_disbursement_method();


                            ContentValues financesValues = new ContentValues();
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_CYCLE_LOAN, input_loan);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PROVIDER_BANK, isInput_loan_prov_bank ? 1 : 0);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PROVIDER_SACCO, isInput_loan_prov_cooperative ? 1 : 0);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PROVIDER_COOPERATIVE, isInput_loan_prov_sacco ? 1 : 0);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PROVIDER_OTHER, isInput_loan_prov_other ? 1 : 0);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_CYCLE_LOAN_AMOUNT, input_loan_amount);

                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_CYCLE_INTEREST_RATE, input_loan_interest_rate);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_CYCLE_LOAN_DURATION, input_loan_duration);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_INPUT, sInput_loan_purpose_input ? 1 : 0);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_LABOUR, sInput_loan_purpose_labour ? 1 : 0);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_SEEDS, sInput_loan_purpose_seed ? 1 : 0);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_MACHINERY, sInput_loan_purpose_machinery ? 1 : 0);

                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_OTHER, sInput_loan_purpose_other ? 1 : 0);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_CYCLE_LOAN_DISB_METHOD, aggrgation_post_harvset_laon_disbursement_method);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN, aggrgation_post_harvset_loan);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PROVIDER_BANK, isAgg_post_harv_loan_prov_bank ? 1 : 0);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PROVIDER_SACCO, isAgg_post_harv_loan_prov_cooperative ? 1 : 0);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PROVIDER_COOPERATIVE, isAgg_post_harv_loan_prov_sacco ? 1 : 0);

                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PROVIDER_OTHER, isAgg_post_harv_loan_prov_other ? 1 : 0);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_AMOUNT, aggrgation_post_harvset_amount);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_INTEREST_RATE, aggrgation_post_harvset_loan_interest);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_DURATION, aggrgation_post_harvset_loan_duration);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PURPOSE_INPUT, isAgg_post_harv_loan_purpose_input ? 1 : 0);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PURPOSE_LABOUR, isAgg_post_harv_loan_purpose_labour ? 1 : 0);

                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PURPOSE_MACHINERY, isAgg_post_harv_loan_purpose_machinery ? 1 : 0);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PURPOSE_OTHER, isAgg_post_harv_loan_purpose_other ? 1 : 0);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_DISBURSEMENT_METHOD, isCash_provided_purchase_inputs ? 1 : 0);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_SEASON_ID, baselineFinanceInfoData.getSeasonId());
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_COOP_ID, coopId);

                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_IS_SYNC, 0);
                            financesValues.put(BfwContract.FinanceInfoCoop.COLUMN_IS_UPDATE, 0);

                            getContentResolver().insert(BfwContract.FinanceInfoCoop.CONTENT_URI, financesValues);

                        }

                        // handle baseline sales table
                        if (baselineSales != null && baselineSales.containsKey(seasonName)) {

                            BaselineSales baselineSalesData = baselineSales.get(seasonName);

                            rgccContractUnderFtma = baselineSalesData.getRgccContractUnderFtma();

                            qtyAgregatedFromMember = baselineSalesData.getQtyAgregatedFromMember();
                            cycleHarvsetAtPricePerKg = baselineSalesData.getCycleHarvsetAtPricePerKg();
                            qtyPurchaseFromNonMember = baselineSalesData.getQtyPurchaseFromNonMember();
                            nonMemberAtPricePerKg = baselineSalesData.getNonMemberAtPricePerKg();

                            qtyOfRgccContract = baselineSalesData.getQtyOfRgccContract();
                            qtySoldToRgcc = baselineSalesData.getQtySoldToRgcc();
                            pricePerKgSoldToRgcc = baselineSalesData.getPricePerKgSoldToRgcc();
                            qtySoldOutOfRgcc = baselineSalesData.getQtySoldOutOfRgcc();
                            pricePerKkSoldOutFtma = baselineSalesData.getPricePerKkSoldOutFtma();

                            isFormalBuyer = baselineSalesData.isFormalBuyer();
                            isInformalBuyer = baselineSalesData.isInformalBuyer();
                            isOtherB = baselineSalesData.isOther();

                            ContentValues saleLinesValues = new ContentValues();
                            saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_CYCLE_HARVEST, qtyAgregatedFromMember);
                            saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_CYCLE_HARVEST_PRICE, cycleHarvsetAtPricePerKg);
                            saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_NON_MEMBER_PURCHASE, qtyPurchaseFromNonMember);
                            saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_NON_MEMBER_PURCHASE_COST, nonMemberAtPricePerKg);

                            saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_CONTRACT_VOLUME, qtyOfRgccContract);
                            saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_QUANTITY_SOLD_RGCC, qtySoldToRgcc);
                            saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_PRICE_SOLD_RGCC, pricePerKgSoldToRgcc);
                            saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_QUANTITY_SOLD_OUTSIDE_RGCC, qtySoldOutOfRgcc);
                            saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_PRICE_SOLD_OUTSIDE_RGCC, pricePerKkSoldOutFtma);
                            saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_BUYER_CONTRACT, rgccContractUnderFtma);
                            saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_RGCC_INFORMAL, isInformalBuyer ? 1 : 0);
                            saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_RGCC_BUYER_FORMAL, isFormalBuyer ? 1 : 0);
                            saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_BUYER_OTHER, isOtherB ? 1 : 0);
                            saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_COOP_ID, coopId);
                            saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_SEASON_ID, baselineSalesData.getSeasonId());
                            saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_IS_SYNC, 0);
                            saleLinesValues.put(BfwContract.BaselineSalesCoop.COLUMN_IS_UPDATE, 0);

                            getContentResolver().insert(BfwContract.BaselineSalesCoop.CONTENT_URI, saleLinesValues);
                        }

                        // handle baseline yield table
                        if (baseLineYield != null && baseLineYield.containsKey(seasonName)) {

                            BaselineYield baselineYieldData = baseLineYield.get(seasonName);

                            isMaize = baselineYieldData.isMaize();
                            isBean = baselineYieldData.isBean();
                            isSoy = baselineYieldData.isSoy();
                            isOther = baselineYieldData.isOther();

                            ContentValues baseLineYiedValues = new ContentValues();
                            baseLineYiedValues.put(BfwContract.YieldCoop.COLUMN_BEAN, isBean ? 1 : 0);
                            baseLineYiedValues.put(BfwContract.YieldCoop.COLUMN_MAIZE, isMaize ? 1 : 0);
                            baseLineYiedValues.put(BfwContract.YieldCoop.COLUMN_SOY, isSoy ? 1 : 0);
                            baseLineYiedValues.put(BfwContract.YieldCoop.COLUMN_OTHER, isOther ? 1 : 0);
                            baseLineYiedValues.put(BfwContract.YieldCoop.COLUMN_SEASON_ID, baselineYieldData.getSeasonId());
                            baseLineYiedValues.put(BfwContract.YieldCoop.COLUMN_COOP_ID, coopId);
                            baseLineYiedValues.put(BfwContract.YieldCoop.COLUMN_IS_SYNC, 0);
                            baseLineYiedValues.put(BfwContract.YieldCoop.COLUMN_IS_UPDATE, 0);

                            getContentResolver().insert(BfwContract.YieldCoop.CONTENT_URI, baseLineYiedValues);
                        }

                        // handle forecast sales table

                        if (forecastSales != null && forecastSales.containsKey(seasonName)) {

                            ForecastSales forecastSalesData = forecastSales.get(seasonName);

                            minFloorPerGrade = forecastSalesData.getMinFloorPerGrade();
                            grade = forecastSalesData.getGrade();
                            commitedContractQty = forecastSalesData.getCommitedContractQty();


                            rgcc = forecastSalesData.isRgcc();
                            prodev = forecastSalesData.isProdev();
                            sarura = forecastSalesData.isSarura();
                            aif = forecastSalesData.isAif();
                            eax = forecastSalesData.isEax();
                            none = forecastSalesData.isNone();
                            other = forecastSalesData.isOther();

                            ContentValues forecastValues = new ContentValues();
                            forecastValues.put(BfwContract.SalesCoop.COLUMN_RGCC, rgcc ? 1 : 0);
                            forecastValues.put(BfwContract.SalesCoop.COLUMN_PRODEV, prodev ? 1 : 0);
                            forecastValues.put(BfwContract.SalesCoop.COLUMN_SAKURA, sarura ? 1 : 0);
                            forecastValues.put(BfwContract.SalesCoop.COLUMN_AIF, aif ? 1 : 0);
                            forecastValues.put(BfwContract.SalesCoop.COLUMN_EAX, eax ? 1 : 0);
                            forecastValues.put(BfwContract.SalesCoop.COLUMN_NONE, none ? 1 : 0);
                            forecastValues.put(BfwContract.SalesCoop.COLUMN_OTHER, other ? 1 : 0);
                            forecastValues.put(BfwContract.SalesCoop.COLUMN_CONTRACT_VOLUME, commitedContractQty);
                            forecastValues.put(BfwContract.SalesCoop.COLUMN_COOP_GRADE, grade);
                            forecastValues.put(BfwContract.SalesCoop.COLUMN_FLOOR_GRADE, minFloorPerGrade);
                            forecastValues.put(BfwContract.SalesCoop.COLUMN_SEASON_ID, forecastSalesData.getSeasonId());
                            forecastValues.put(BfwContract.SalesCoop.COLUMN_COOP_ID, coopId);
                            forecastValues.put(BfwContract.SalesCoop.COLUMN_IS_SYNC, 0);
                            forecastValues.put(BfwContract.SalesCoop.COLUMN_IS_UPDATE, 0);

                            getContentResolver().insert(BfwContract.SalesCoop.CONTENT_URI, forecastValues);

                        }

                        // handle access  to information table
                        if (accessToInformation != null && accessToInformation.containsKey(seasonName)) {

                            AccessToInformation accessToInformationData = accessToInformation.get(seasonName);

                            isAgriculture = accessToInformationData.isAgricultureExtension();
                            isClimateInfo = accessToInformationData.isClimateRelatedInformation();
                            isSeeds = accessToInformationData.isSeed();
                            organicFertilizers = accessToInformationData.isOrganicFertilizers();
                            inorganicFertilizers = accessToInformationData.isInorganicFertilizers();
                            labour = accessToInformationData.isLabour();
                            waterPumps = accessToInformationData.isWaterPumps();
                            isSpreader = accessToInformationData.isSpreaderOrSprayer();

                            ContentValues infoValues = new ContentValues();
                            infoValues.put(BfwContract.CoopInfo.COLUMN_AGRI_EXTENSION, isAgriculture ? 1 : 0);
                            infoValues.put(BfwContract.CoopInfo.COLUMN_CLIMATE_INFO, isClimateInfo ? 1 : 0);
                            infoValues.put(BfwContract.CoopInfo.COLUMN_SEEDS, isSeeds ? 1 : 0);
                            infoValues.put(BfwContract.CoopInfo.COLUMN_ORGANIC_FERT, organicFertilizers ? 1 : 0);
                            infoValues.put(BfwContract.CoopInfo.COLUMN_INORGANIC_FERT, inorganicFertilizers ? 1 : 0);
                            infoValues.put(BfwContract.CoopInfo.COLUMN_LABOUR, labour ? 1 : 0);
                            infoValues.put(BfwContract.CoopInfo.COLUMN_WATER_PUMPS, waterPumps ? 1 : 0);
                            infoValues.put(BfwContract.CoopInfo.COLUMN_SPREADER, isSpreader ? 1 : 0);
                            infoValues.put(BfwContract.CoopInfo.COLUMN_SEASON_ID, accessToInformationData.getSeasonId());
                            infoValues.put(BfwContract.CoopInfo.COLUMN_COOP_ID, coopId);
                            infoValues.put(BfwContract.CoopInfo.COLUMN_IS_SYNC, 0);
                            infoValues.put(BfwContract.CoopInfo.COLUMN_IS_UPDATE, 0);

                            getContentResolver().insert(BfwContract.CoopInfo.CONTENT_URI, infoValues);
                        }
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            //Post event after saving data
            EventBus.getDefault().post(new SaveDataEvent("", true));

            //sync if network available
            if (Utils.isNetworkAvailable(getApplicationContext())) {
                //start job service
                startService(new Intent(this, SyncCoopInfo.class));
            } else {
                //schedule a job if not network is available
                FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));

                Job job = dispatcher.newJobBuilder()
                        .setService(SyncCoop.class)
                        .setTag(UUID.randomUUID().toString())
                        .setConstraints(Constraint.ON_ANY_NETWORK)
                        .build();

                dispatcher.mustSchedule(job);
            }

        } else {
            EventBus.getDefault().post(new SaveDataEvent("No data Available", false));
        }

    }
}

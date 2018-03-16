package com.nijus.alino.bfwcoopmanagement.farmers.sync;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.AccessToInformation;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.BaseLine;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.Demographic;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.Finance;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.Forecast;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.General;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.LandInformation;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.ServiceAccess;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.UUID;

public class UpdateFarmerService extends IntentService {
    private int mFarmerId;

    public UpdateFarmerService() {
        super("");
    }

    public UpdateFarmerService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {

            String farmerSelect = BfwContract.Farmer.TABLE_NAME + "." +
                    BfwContract.Farmer._ID + " =  ? ";
            mFarmerId = intent.getIntExtra("farmerId", 0);

            int isSyncFarmerId = 0;
            Cursor farmCursor = null;

            try {
                farmCursor = getContentResolver().query(BfwContract.Farmer.CONTENT_URI, null, farmerSelect, new String[]{Long.toString(mFarmerId)}, null);
                if (farmCursor != null) {
                    farmCursor.moveToFirst();
                    isSyncFarmerId = farmCursor.getInt(farmCursor.getColumnIndex(BfwContract.Farmer.COLUMN_IS_SYNC));

                }
            } finally {
                if (farmCursor != null) farmCursor.close();
            }

            Bundle farmerData = intent.getBundleExtra("farmer_data");

            General general = farmerData.getParcelable("general");
            ServiceAccess serviceAccess = farmerData.getParcelable("serviceAccess");
            Demographic demographic = farmerData.getParcelable("demographic");


            HashMap<String, Forecast> forecast = (HashMap<String, Forecast>) farmerData.getSerializable("forecast");
            HashMap<String, BaseLine> baseLine = (HashMap<String, BaseLine>) farmerData.getSerializable("baseline");
            HashMap<String, Finance> finance = (HashMap<String, Finance>) farmerData.getSerializable("finance");
            HashMap<String, AccessToInformation> accessToInformation = (HashMap<String, AccessToInformation>) farmerData.getSerializable("accessToInformation");
            HashMap<String, LandInformation> landInformations = (HashMap<String, LandInformation>) farmerData.getSerializable("land_info");

            Cursor cursor = null;

            //General page field
            String name = null, phoneNumber = null, address = null;
            Boolean gender = false;
            int coopServerId = 1;

            //Access to information page field
            Boolean isTractors = null, isHarvester = null, isDryer = null, isTresher = null, isSafeStorage = null, isOtherResourceInfo = null,
                    isDam = null, isWell = null, isBoreHole = null, isPipeBorne = null, isRiverStream = null, isIrrigation = null,
                    hasNoWaterSource = null, isOtherInfo = null;
            String storageInfo = null, otherAvailableRes = null, otherWaterSource = null;

            //Demographic data
            Boolean isHouseHoldHead = false;
            Integer houseHoldMember = null;
            String fName = null, lName = null, cellPhoneAlt = null, cellCarrier = null;

            //Forecast data field
            Double numArableLandPlot;
            Double farmerexpectedminppp;
            Double minimumflowprice;

            //Baseline data field
            Double totProdInKg;
            Double totLostInKg;
            Double totSoldInKg;
            Double totVolumeSoldCoopInKg;
            Double priceSoldToCoopPerKg;
            Double totVolSoldInKg;
            Double priceSoldInKg;

            //Finance data field;
            Boolean outstandingLoan, isInput, isAggregation, isOther, isMobileMoneyAccount;
            Double interestRate, totLoanAmount, totOutstanding;
            Integer duration;
            String loanProvider;

            //Access to information data field
            Boolean isAgriculture, isClimateInfo, isSeeds, organicFertilizers,
                    inorganicFertilizers, labour,
                    waterPumps, isSpreader;


            //Land information data field
            Double landSize, lat, lng;

            // farmer table
            if (general != null) {

                name = general.getName();
                phoneNumber = general.getPhoneNumber();
                gender = general.isGender();
                address = general.getAddress();
                coopServerId = general.getCoopId();
            }

            if (demographic != null) {
                isHouseHoldHead = demographic.isHouseHold();
                houseHoldMember = demographic.getHouseHoldMember();
                fName = demographic.getSpouseFirstName();
                lName = demographic.getSpouseLastName();
                cellPhoneAlt = demographic.getCellPhoneAlt();
                cellCarrier = demographic.getCellCarrier();
            }

            if (serviceAccess != null) {

                isTractors = serviceAccess.isTractor();
                isHarvester = serviceAccess.isHarvester();
                isDryer = serviceAccess.isDryer();
                isTresher = serviceAccess.isTresher();
                isSafeStorage = serviceAccess.isSafeStorage();
                isOtherResourceInfo = serviceAccess.isOtherResourceInfo();


                isDam = serviceAccess.isDam();
                isWell = serviceAccess.isWell();
                isBoreHole = serviceAccess.isBoreHole();
                isPipeBorne = serviceAccess.isPipeBorne();
                isRiverStream = serviceAccess.isRiverStream();
                isIrrigation = serviceAccess.isIrrigation();
                hasNoWaterSource = serviceAccess.isHasNoWaterSource();
                isOtherInfo = serviceAccess.isOtherInfo();

                storageInfo = serviceAccess.getStorageDetails();
                otherAvailableRes = serviceAccess.getNewResourcesDetails();
                otherWaterSource = serviceAccess.getMainWaterSourceDetails();
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put(BfwContract.Farmer.COLUMN_NAME, name);
            contentValues.put(BfwContract.Farmer.COLUMN_GENDER, gender ? "male" : "female");
            contentValues.put(BfwContract.Farmer.COLUMN_PHONE, phoneNumber);
            contentValues.put(BfwContract.Farmer.COLUMN_ADDRESS, address);

            contentValues.put(BfwContract.Farmer.COLUMN_HOUSEHOLD_HEAD, isHouseHoldHead ? 1 : 0);
            contentValues.put(BfwContract.Farmer.COLUMN_HOUSE_MEMBER, houseHoldMember);
            contentValues.put(BfwContract.Farmer.COLUMN_FIRST_NAME, fName);
            contentValues.put(BfwContract.Farmer.COLUMN_LAST_NAME, lName);
            contentValues.put(BfwContract.Farmer.COLUMN_CELL_PHONE, cellPhoneAlt);
            contentValues.put(BfwContract.Farmer.COLUMN_CELL_CARRIER, cellCarrier);
            contentValues.put(BfwContract.Farmer.COLUMN_TRACTORS, isTractors);
            contentValues.put(BfwContract.Farmer.COLUMN_HARVESTER, isHarvester);
            contentValues.put(BfwContract.Farmer.COLUMN_DRYER, isDryer);
            contentValues.put(BfwContract.Farmer.COLUMN_TRESHER, isTresher);
            contentValues.put(BfwContract.Farmer.COLUMN_SAFE_STORAGE, isSafeStorage);
            contentValues.put(BfwContract.Farmer.COLUMN_OTHER_INFO, isOtherInfo);
            contentValues.put(BfwContract.Farmer.COLUMN_DAM, isDam);
            contentValues.put(BfwContract.Farmer.COLUMN_BOREHOLE, isBoreHole);
            contentValues.put(BfwContract.Farmer.COLUMN_STORAGE_DETAIL, storageInfo);
            contentValues.put(BfwContract.Farmer.COLUMN_NEW_SOURCE_DETAIL, otherAvailableRes);
            contentValues.put(BfwContract.Farmer.COLUMN_WATER_SOURCE_DETAILS, otherWaterSource);
            contentValues.put(BfwContract.Farmer.COLUMN_WELL, isWell);
            contentValues.put(BfwContract.Farmer.COLUMN_PIPE_BORNE, isPipeBorne);
            contentValues.put(BfwContract.Farmer.COLUMN_RIVER_STREAM, isRiverStream);
            contentValues.put(BfwContract.Farmer.COLUMN_IRRIGATION, isIrrigation);
            contentValues.put(BfwContract.Farmer.COLUMN_NONE, hasNoWaterSource);
            contentValues.put(BfwContract.Farmer.COLUMN_OTHER, isOtherResourceInfo);
            contentValues.put(BfwContract.Farmer.COLUMN_IS_SYNC, isSyncFarmerId);
            contentValues.put(BfwContract.Farmer.COLUMN_IS_UPDATE, 0);
            contentValues.put(BfwContract.Farmer.COLUMN_COOP_USER_ID, coopServerId);

            getContentResolver().update(BfwContract.Farmer.CONTENT_URI, contentValues, farmerSelect, new String[]{Long.toString(mFarmerId)});

            String seasonName;
            Cursor isDataAvailable = null;
            String forecastSelect = BfwContract.ForecastFarmer.TABLE_NAME
                    + "." + BfwContract.ForecastFarmer._ID + " = ? ";
            String baselineSelect = BfwContract.BaselineFarmer.TABLE_NAME
                    + "." + BfwContract.BaselineFarmer._ID + " = ? ";
            String financeSelect = BfwContract.FinanceDataFarmer.TABLE_NAME
                    + "." + BfwContract.FinanceDataFarmer._ID + " = ? ";
            String accessToInfoSelect = BfwContract.FarmerAccessInfo.TABLE_NAME
                    + "." + BfwContract.FarmerAccessInfo._ID + " = ? ";
            String landSelect = BfwContract.LandPlot.TABLE_NAME
                    + "." + BfwContract.LandPlot._ID + " = ? ";
            int farmerTabId, isSync;

            try {
                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                        // check if forecast is available for this season and save it
                        if (forecast != null && forecast.containsKey(seasonName)) {

                            Forecast forecastInfo = forecast.get(seasonName);

                            numArableLandPlot = forecastInfo.getArableLandPlot();
                            minimumflowprice = forecastInfo.getMinimumflowprice();
                            farmerexpectedminppp = forecastInfo.getFarmerexpectedminppp();

                            farmerTabId = forecastInfo.getForecastId();

                            if (farmerTabId > 0) {

                                isDataAvailable = getContentResolver().query(BfwContract.ForecastFarmer.CONTENT_URI, null, forecastSelect, new String[]{Integer.toString(farmerTabId)}, null);
                                if (isDataAvailable != null && isDataAvailable.moveToFirst()) {
                                    isSync = isDataAvailable.getInt(isDataAvailable.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_IS_SYNC));

                                    ContentValues forecastFarmerValues = new ContentValues();
                                    forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_ARABLE_LAND_PLOT, numArableLandPlot);
                                    forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_EXPECTED_MIN_PPP, farmerexpectedminppp);
                                    forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_FLOW_PRICE, minimumflowprice);
                                    forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_SEASON_ID, forecastInfo.getHarvestSeason());
                                    forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_FARMER_ID, mFarmerId);
                                    forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_IS_SYNC, isSync);
                                    forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_IS_UPDATE, 0);

                                    getContentResolver().update(BfwContract.ForecastFarmer.CONTENT_URI, forecastFarmerValues, forecastSelect, new String[]{Integer.toString(farmerTabId)});
                                }

                            } else {
                                ContentValues forecastFarmerValues = new ContentValues();
                                forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_ARABLE_LAND_PLOT, numArableLandPlot);
                                forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_EXPECTED_MIN_PPP, farmerexpectedminppp);
                                forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_FLOW_PRICE, minimumflowprice);
                                forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_SEASON_ID, forecastInfo.getHarvestSeason());
                                forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_FARMER_ID, mFarmerId);
                                forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_IS_SYNC, 0);
                                forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_IS_UPDATE, 0);

                                getContentResolver().insert(BfwContract.ForecastFarmer.CONTENT_URI, forecastFarmerValues);
                            }

                        }

                        // check if baseline is available for this season,  update or save it
                        if (baseLine != null && baseLine.containsKey(seasonName)) {

                            BaseLine baseLineInfo = baseLine.get(seasonName);

                            totProdInKg = baseLineInfo.getTotProdInKg();
                            totLostInKg = baseLineInfo.getTotLostInKg();
                            totSoldInKg = baseLineInfo.getTotSoldInKg();
                            totVolumeSoldCoopInKg = baseLineInfo.getTotVolumeSoldCoopInKg();
                            priceSoldToCoopPerKg = baseLineInfo.getPriceSoldToCoopPerKg();
                            totVolSoldInKg = baseLineInfo.getTotVolSoldInKg();
                            priceSoldInKg = baseLineInfo.getPriceSoldInKg();

                            farmerTabId = baseLineInfo.getBaselineId();
                            if (farmerTabId > 0) {

                                isDataAvailable = getContentResolver().query(BfwContract.BaselineFarmer.CONTENT_URI, null, baselineSelect, new String[]{Integer.toString(farmerTabId)}, null);
                                if (isDataAvailable != null && isDataAvailable.moveToFirst()) {
                                    isSync = isDataAvailable.getInt(isDataAvailable.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_IS_SYNC));

                                    ContentValues baselineValues = new ContentValues();
                                    baselineValues.put(BfwContract.BaselineFarmer.COLUMN_TOT_PROD_B_KG, totProdInKg);
                                    baselineValues.put(BfwContract.BaselineFarmer.COLUMN_TOT_LOST_KG, totLostInKg);
                                    baselineValues.put(BfwContract.BaselineFarmer.COLUMN_TOT_SOLD_KG, totSoldInKg);
                                    baselineValues.put(BfwContract.BaselineFarmer.COLUMN_TOT_VOL_SOLD_COOP, totVolumeSoldCoopInKg);

                                    baselineValues.put(BfwContract.BaselineFarmer.COLUMN_PRICE_SOLD_COOP_PER_KG, priceSoldToCoopPerKg);
                                    baselineValues.put(BfwContract.BaselineFarmer.COLUMN_TOT_VOL_SOLD_IN_KG, totVolSoldInKg);
                                    baselineValues.put(BfwContract.BaselineFarmer.COLUMN_PRICE_SOLD_KG, priceSoldInKg);
                                    baselineValues.put(BfwContract.BaselineFarmer.COLUMN_SEASON_ID, baseLineInfo.getHarvestSeason());

                                    baselineValues.put(BfwContract.BaselineFarmer.COLUMN_IS_SYNC, isSync);
                                    baselineValues.put(BfwContract.BaselineFarmer.COLUMN_IS_UPDATE, 0);
                                    baselineValues.put(BfwContract.BaselineFarmer.COLUMN_FARMER_ID, mFarmerId);

                                    getContentResolver().update(BfwContract.BaselineFarmer.CONTENT_URI, baselineValues, baselineSelect, new String[]{Integer.toString(farmerTabId)});

                                }

                            } else {
                                ContentValues baselineValues = new ContentValues();
                                baselineValues.put(BfwContract.BaselineFarmer.COLUMN_TOT_PROD_B_KG, totProdInKg);
                                baselineValues.put(BfwContract.BaselineFarmer.COLUMN_TOT_LOST_KG, totLostInKg);
                                baselineValues.put(BfwContract.BaselineFarmer.COLUMN_TOT_SOLD_KG, totSoldInKg);
                                baselineValues.put(BfwContract.BaselineFarmer.COLUMN_TOT_VOL_SOLD_COOP, totVolumeSoldCoopInKg);

                                baselineValues.put(BfwContract.BaselineFarmer.COLUMN_PRICE_SOLD_COOP_PER_KG, priceSoldToCoopPerKg);
                                baselineValues.put(BfwContract.BaselineFarmer.COLUMN_TOT_VOL_SOLD_IN_KG, totVolSoldInKg);
                                baselineValues.put(BfwContract.BaselineFarmer.COLUMN_PRICE_SOLD_KG, priceSoldInKg);
                                baselineValues.put(BfwContract.BaselineFarmer.COLUMN_SEASON_ID, baseLineInfo.getHarvestSeason());

                                baselineValues.put(BfwContract.BaselineFarmer.COLUMN_IS_SYNC, 0);
                                baselineValues.put(BfwContract.BaselineFarmer.COLUMN_IS_UPDATE, 0);
                                baselineValues.put(BfwContract.BaselineFarmer.COLUMN_FARMER_ID, mFarmerId);

                                getContentResolver().insert(BfwContract.BaselineFarmer.CONTENT_URI, baselineValues);
                            }

                        }

                        // check if finance is available for this season, update or save it
                        if (finance != null && finance.containsKey(seasonName)) {

                            Finance financeInfo = finance.get(seasonName);

                            outstandingLoan = financeInfo.isOutstandingLoan();
                            isMobileMoneyAccount = financeInfo.isHasMobileMoneyAccount();
                            isInput = financeInfo.isInput();
                            isAggregation = financeInfo.isAggregation();
                            isOther = financeInfo.isOtherLp();

                            totLoanAmount = financeInfo.getTotLoanAmount();
                            totOutstanding = financeInfo.getTotOutstanding();
                            interestRate = financeInfo.getInterestRate();
                            duration = financeInfo.getDurationInMonth();
                            loanProvider = financeInfo.getLoanProvider();

                            farmerTabId = financeInfo.getFinanceId();

                            if (farmerTabId > 0) {

                                isDataAvailable = getContentResolver().query(BfwContract.FinanceDataFarmer.CONTENT_URI, null, financeSelect, new String[]{Integer.toString(farmerTabId)}, null);
                                if (isDataAvailable != null && isDataAvailable.moveToFirst()) {
                                    isSync = isDataAvailable.getInt(isDataAvailable.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_IS_SYNC));

                                    ContentValues financeValues = new ContentValues();
                                    financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_OUTSANDING_LOAN, outstandingLoan);
                                    financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_TOT_LOAN_AMOUNT, totLoanAmount);
                                    financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_TOT_OUTSTANDING, totOutstanding);
                                    financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_INTEREST_RATE, interestRate);

                                    financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_DURATION, duration);
                                    financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_LOAN_PROVIDER, loanProvider);
                                    financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_LOANPROVIDER_AGGREG, isAggregation);
                                    financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_LOANPROVIDER_INPUT, isInput);

                                    financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_LOANPROVIDER_OTHER, isOther);
                                    financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_MOBILE_MONEY_ACCOUNT, isMobileMoneyAccount);
                                    financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_SEASON_ID, financeInfo.getHarvestSeason());
                                    financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_FARMER_ID, mFarmerId);
                                    financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_IS_SYNC, isSync);
                                    financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_IS_UPDATE, 0);

                                    getContentResolver().update(BfwContract.FinanceDataFarmer.CONTENT_URI, financeValues, financeSelect, new String[]{Integer.toString(farmerTabId)});

                                }

                            } else {
                                ContentValues financeValues = new ContentValues();
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_OUTSANDING_LOAN, outstandingLoan);
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_TOT_LOAN_AMOUNT, totLoanAmount);
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_TOT_OUTSTANDING, totOutstanding);
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_INTEREST_RATE, interestRate);

                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_DURATION, duration);
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_LOAN_PROVIDER, loanProvider);
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_LOANPROVIDER_AGGREG, isAggregation);
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_LOANPROVIDER_INPUT, isInput);

                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_LOANPROVIDER_OTHER, isOther);
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_MOBILE_MONEY_ACCOUNT, isMobileMoneyAccount);
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_SEASON_ID, financeInfo.getHarvestSeason());
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_FARMER_ID, mFarmerId);
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_IS_SYNC, 0);
                                financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_IS_UPDATE, 0);

                                getContentResolver().insert(BfwContract.FinanceDataFarmer.CONTENT_URI, financeValues);
                            }
                        }

                        // check if access o Information is available for this season, update or save it
                        if (accessToInformation != null && accessToInformation.containsKey(seasonName)) {

                            AccessToInformation accessInfo = accessToInformation.get(seasonName);

                            isAgriculture = accessInfo.isAgricultureExtension();
                            isClimateInfo = accessInfo.isClimateRelatedInformation();
                            isSeeds = accessInfo.isSeed();
                            organicFertilizers = accessInfo.isOrganicFertilizers();
                            inorganicFertilizers = accessInfo.isInorganicFertilizers();
                            labour = accessInfo.isLabour();
                            waterPumps = accessInfo.isWaterPumps();
                            isSpreader = accessInfo.isSpreaderOrSprayer();

                            farmerTabId = accessInfo.getAccessInfoId();

                            if (farmerTabId > 0) {

                                isDataAvailable = getContentResolver().query(BfwContract.FarmerAccessInfo.CONTENT_URI, null, accessToInfoSelect, new String[]{Integer.toString(farmerTabId)}, null);
                                if (isDataAvailable != null && isDataAvailable.moveToFirst()) {
                                    isSync = isDataAvailable.getInt(isDataAvailable.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_IS_SYNC));

                                    ContentValues farmerInfoValues = new ContentValues();
                                    farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_AGRI_EXTENSION_SERV, isAgriculture);
                                    farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_CLIMATE_RELATED_INFO, isClimateInfo);
                                    farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_SEEDS, isSeeds);
                                    farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_ORGANIC_FERTILIZER, organicFertilizers);

                                    farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_INORGANIC_FERTILIZER, inorganicFertilizers);
                                    farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_LABOUR, labour);
                                    farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_WATER_PUMPS, waterPumps);
                                    farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_SPRAYERS, isSpreader);

                                    farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_IS_SYNC, isSync);
                                    farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_IS_UPDATE, 0);
                                    farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_SEASON_ID, accessInfo.getHarvestSeason());
                                    farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_FARMER_ID, mFarmerId);

                                    getContentResolver().update(BfwContract.FarmerAccessInfo.CONTENT_URI, farmerInfoValues, accessToInfoSelect, new String[]{Integer.toString(farmerTabId)});

                                }

                            } else {

                                ContentValues farmerInfoValues = new ContentValues();
                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_AGRI_EXTENSION_SERV, isAgriculture);
                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_CLIMATE_RELATED_INFO, isClimateInfo);
                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_SEEDS, isSeeds);
                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_ORGANIC_FERTILIZER, organicFertilizers);

                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_INORGANIC_FERTILIZER, inorganicFertilizers);
                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_LABOUR, labour);
                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_WATER_PUMPS, waterPumps);
                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_SPRAYERS, isSpreader);

                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_IS_SYNC, 0);
                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_IS_UPDATE, 0);
                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_SEASON_ID, accessInfo.getHarvestSeason());
                                farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_FARMER_ID, mFarmerId);

                                getContentResolver().insert(BfwContract.FarmerAccessInfo.CONTENT_URI, farmerInfoValues);

                            }

                        }

                        // check if land information is available for this season, update or save it
                        if (landInformations != null && landInformations.containsKey(seasonName)) {

                            LandInformation landInfo = landInformations.get(seasonName);

                            landSize = landInfo.getLandSize();
                            lat = landInfo.getLat();
                            lng = landInfo.getLng();
                            farmerTabId = landInfo.getLandId();

                            if (farmerTabId > 0) {

                                isDataAvailable = getContentResolver().query(BfwContract.LandPlot.CONTENT_URI, null, landSelect, new String[]{Integer.toString(farmerTabId)}, null);
                                if (isDataAvailable != null && isDataAvailable.moveToFirst()) {

                                    isSync = isDataAvailable.getInt(isDataAvailable.getColumnIndex(BfwContract.LandPlot.COLUMN_IS_SYNC));

                                    ContentValues farmerLandValues = new ContentValues();
                                    farmerLandValues.put(BfwContract.LandPlot.COLUMN_PLOT_SIZE, landSize);
                                    farmerLandValues.put(BfwContract.LandPlot.COLUMN_LAT_INFO, lat);
                                    farmerLandValues.put(BfwContract.LandPlot.COLUMN_LNG_INFO, lng);
                                    farmerLandValues.put(BfwContract.LandPlot.COLUMN_SEASON_ID, landInfo.getHarvestSeason());
                                    farmerLandValues.put(BfwContract.LandPlot.COLUMN_IS_SYNC, isSync);
                                    farmerLandValues.put(BfwContract.LandPlot.COLUMN_IS_UPDATE, 0);
                                    farmerLandValues.put(BfwContract.LandPlot.COLUMN_FARMER_ID, mFarmerId);

                                    getContentResolver().update(BfwContract.LandPlot.CONTENT_URI, farmerLandValues, landSelect, new String[]{Integer.toString(farmerTabId)});

                                }

                            } else {
                                ContentValues farmerLandValues = new ContentValues();
                                farmerLandValues.put(BfwContract.LandPlot.COLUMN_PLOT_SIZE, landSize);
                                farmerLandValues.put(BfwContract.LandPlot.COLUMN_LAT_INFO, lat);
                                farmerLandValues.put(BfwContract.LandPlot.COLUMN_LNG_INFO, lng);
                                farmerLandValues.put(BfwContract.LandPlot.COLUMN_SEASON_ID, landInfo.getHarvestSeason());
                                farmerLandValues.put(BfwContract.LandPlot.COLUMN_IS_SYNC, 0);
                                farmerLandValues.put(BfwContract.LandPlot.COLUMN_IS_UPDATE, 0);
                                farmerLandValues.put(BfwContract.LandPlot.COLUMN_FARMER_ID, mFarmerId);

                                getContentResolver().insert(BfwContract.LandPlot.CONTENT_URI, farmerLandValues);
                            }

                        }
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                if (isDataAvailable != null) {
                    isDataAvailable.close();
                }
            }

            //Post event after saving data
            EventBus.getDefault().post(new SaveDataEvent());

            String farmerSelect1 = BfwContract.Farmer.TABLE_NAME + "." +
                    BfwContract.Farmer.COLUMN_IS_SYNC + " =  1 AND " +
                    BfwContract.Farmer.TABLE_NAME + "." +
                    BfwContract.Farmer.COLUMN_IS_UPDATE + " = 0 ";

            Cursor farmCursor1 = null;

            try {
                farmCursor1 = getContentResolver().query(BfwContract.Farmer.CONTENT_URI, null, farmerSelect1, null, null);
                if (farmCursor1 != null && farmCursor1.getCount() > 0) {

                    //sync if network available
                    if (Utils.isNetworkAvailable(getApplicationContext())) {
                        //start job service
                        startService(new Intent(this, UpdateSyncFarmer.class));
                    } else {
                        //schedule a job if no network is available
                        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));

                        Job job = dispatcher.newJobBuilder()
                                .setService(UpdateSyncFarmerService.class)
                                .setTag(UUID.randomUUID().toString())
                                .setConstraints(Constraint.ON_ANY_NETWORK)
                                .build();

                        dispatcher.mustSchedule(job);
                    }
                }
            } finally {
                if (farmCursor1 != null) farmCursor1.close();
            }
        }
    }
}

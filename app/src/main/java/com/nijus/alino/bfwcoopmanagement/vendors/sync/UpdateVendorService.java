package com.nijus.alino.bfwcoopmanagement.vendors.sync;

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
import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.AccessToInformationVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.BaseLineVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.DemographicVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.FinanceVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.ForecastVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.GeneralVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.LandInformationVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.ServiceAccessVendor;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.UUID;

public class UpdateVendorService extends IntentService {
    private long mFarmerId;

    public UpdateVendorService() {
        super("");
    }

    public UpdateVendorService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {

            String farmerSelect = BfwContract.Vendor.TABLE_NAME + "." +
                    BfwContract.Vendor._ID + " =  ? ";
            mFarmerId = intent.getLongExtra("vendorId", 0);

            int isSyncFarmerId = 0;
            Cursor farmCursor = null;

            try {
                farmCursor = getContentResolver().query(BfwContract.Vendor.CONTENT_URI, null, farmerSelect, new String[]{Long.toString(mFarmerId)}, null);
                if (farmCursor != null) {
                    farmCursor.moveToFirst();
                    isSyncFarmerId = farmCursor.getInt(farmCursor.getColumnIndex(BfwContract.Vendor.COLUMN_IS_SYNC));

                }
            } finally {
                if (farmCursor != null) farmCursor.close();
            }

            Bundle farmerData = intent.getBundleExtra("vendor_data");

            GeneralVendor general = farmerData.getParcelable("generalVendor");
            ServiceAccessVendor serviceAccess = farmerData.getParcelable("serviceAccessVendor");
            DemographicVendor demographic = farmerData.getParcelable("demographicVendor");


            HashMap<String, ForecastVendor> forecast = (HashMap<String, ForecastVendor>) farmerData.getSerializable("forecastVendor");
            HashMap<String, BaseLineVendor> baseLine = (HashMap<String, BaseLineVendor>) farmerData.getSerializable("baselineVendor");
            HashMap<String, FinanceVendor> finance = (HashMap<String, FinanceVendor>) farmerData.getSerializable("financeVendor");
            HashMap<String, AccessToInformationVendor> accessToInformation = (HashMap<String, AccessToInformationVendor>) farmerData.getSerializable("accessToInformationVendor");
            HashMap<String, LandInformationVendor> landInformations = (HashMap<String, LandInformationVendor>) farmerData.getSerializable("land_infoVendor");

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

            // Vendor table
            if (general != null) {

                name = general.getName();
                phoneNumber = general.getPhoneNumber();
                gender = general.isGender();
                address = general.getAddress();
                //coopServerId = general.getCoopId();
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
            contentValues.put(BfwContract.Vendor.COLUMN_NAME, name);
            contentValues.put(BfwContract.Vendor.COLUMN_GENDER, gender ? "male" : "female");
            contentValues.put(BfwContract.Vendor.COLUMN_PHONE, phoneNumber);
            contentValues.put(BfwContract.Vendor.COLUMN_ADDRESS, address);

            contentValues.put(BfwContract.Vendor.COLUMN_HOUSEHOLD_HEAD, isHouseHoldHead ? 1 : 0);
            contentValues.put(BfwContract.Vendor.COLUMN_HOUSE_MEMBER, houseHoldMember);
            contentValues.put(BfwContract.Vendor.COLUMN_FIRST_NAME, fName);
            contentValues.put(BfwContract.Vendor.COLUMN_LAST_NAME, lName);
            contentValues.put(BfwContract.Vendor.COLUMN_CELL_PHONE, cellPhoneAlt);
            contentValues.put(BfwContract.Vendor.COLUMN_CELL_CARRIER, cellCarrier);
            contentValues.put(BfwContract.Vendor.COLUMN_TRACTORS, isTractors);
            contentValues.put(BfwContract.Vendor.COLUMN_HARVESTER, isHarvester);
            contentValues.put(BfwContract.Vendor.COLUMN_DRYER, isDryer);
            contentValues.put(BfwContract.Vendor.COLUMN_TRESHER, isTresher);
            contentValues.put(BfwContract.Vendor.COLUMN_SAFE_STORAGE, isSafeStorage);
            contentValues.put(BfwContract.Vendor.COLUMN_OTHER_INFO, isOtherInfo);
            contentValues.put(BfwContract.Vendor.COLUMN_DAM, isDam);
            contentValues.put(BfwContract.Vendor.COLUMN_BOREHOLE, isBoreHole);
            contentValues.put(BfwContract.Vendor.COLUMN_STORAGE_DETAIL, storageInfo);
            contentValues.put(BfwContract.Vendor.COLUMN_NEW_SOURCE_DETAIL, otherAvailableRes);
            contentValues.put(BfwContract.Vendor.COLUMN_WATER_SOURCE_DETAILS, otherWaterSource);
            contentValues.put(BfwContract.Vendor.COLUMN_WELL, isWell);
            contentValues.put(BfwContract.Vendor.COLUMN_PIPE_BORNE, isPipeBorne);
            contentValues.put(BfwContract.Vendor.COLUMN_RIVER_STREAM, isRiverStream);
            contentValues.put(BfwContract.Vendor.COLUMN_IRRIGATION, isIrrigation);
            contentValues.put(BfwContract.Vendor.COLUMN_NONE, hasNoWaterSource);
            contentValues.put(BfwContract.Vendor.COLUMN_OTHER, isOtherResourceInfo);
            contentValues.put(BfwContract.Vendor.COLUMN_IS_SYNC, isSyncFarmerId);
            contentValues.put(BfwContract.Vendor.COLUMN_IS_UPDATE, 0);
            //contentValues.put(BfwContract.Vendor.COLUMN_COOP_USER_ID, coopServerId);

            getContentResolver().update(BfwContract.Vendor.CONTENT_URI, contentValues, farmerSelect, new String[]{Long.toString(mFarmerId)});

            String seasonName;
            Cursor isDataAvailable = null;
            String forecastSelect = BfwContract.ForecastVendor.TABLE_NAME
                    + "." + BfwContract.ForecastVendor._ID + " = ? ";
            String baselineSelect = BfwContract.BaseLineVendor.TABLE_NAME
                    + "." + BfwContract.BaseLineVendor._ID + " = ? ";
            String financeSelect = BfwContract.FinanceDataVendor.TABLE_NAME
                    + "." + BfwContract.FinanceDataVendor._ID + " = ? ";
            String accessToInfoSelect = BfwContract.VendorAccessInfo.TABLE_NAME
                    + "." + BfwContract.VendorAccessInfo._ID + " = ? ";
            String landSelect = BfwContract.VendorLand.TABLE_NAME
                    + "." + BfwContract.VendorLand._ID + " = ? ";
            int farmerTabId, isSync;

            try {
                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                        // check if forecast is available for this season and save it
                        if (forecast != null && forecast.containsKey(seasonName)) {

                            ForecastVendor forecastInfo = forecast.get(seasonName);

                            numArableLandPlot = forecastInfo.getArableLandPlot();
                            minimumflowprice = forecastInfo.getMinimumflowprice();
                            farmerexpectedminppp = forecastInfo.getFarmerexpectedminppp();

                            farmerTabId = forecastInfo.getForecastId();

                            if (farmerTabId > 0) {

                                isDataAvailable = getContentResolver().query(BfwContract.ForecastVendor.CONTENT_URI, null, forecastSelect, new String[]{Integer.toString(farmerTabId)}, null);
                                if (isDataAvailable != null && isDataAvailable.moveToFirst()) {
                                    isSync = isDataAvailable.getInt(isDataAvailable.getColumnIndex(BfwContract.ForecastVendor.COLUMN_IS_SYNC));

                                    ContentValues forecastFarmerValues = new ContentValues();
                                    forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_ARABLE_LAND_PLOT, numArableLandPlot);
                                    forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_EXPECTED_MIN_PPP, farmerexpectedminppp);
                                    forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_FLOW_PRICE, minimumflowprice);
                                    forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_SEASON_ID, forecastInfo.getHarvestSeason());
                                    forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_VENDOR_ID, mFarmerId);
                                    forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_IS_SYNC, isSync);
                                    forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_IS_UPDATE, 0);

                                    getContentResolver().update(BfwContract.ForecastVendor.CONTENT_URI, forecastFarmerValues, forecastSelect, new String[]{Integer.toString(farmerTabId)});
                                }

                            } else {
                                ContentValues forecastFarmerValues = new ContentValues();
                                forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_ARABLE_LAND_PLOT, numArableLandPlot);
                                forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_EXPECTED_MIN_PPP, farmerexpectedminppp);
                                forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_FLOW_PRICE, minimumflowprice);
                                forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_SEASON_ID, forecastInfo.getHarvestSeason());
                                forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_VENDOR_ID, mFarmerId);
                                forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_IS_SYNC, 0);
                                forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_IS_UPDATE, 0);

                                getContentResolver().insert(BfwContract.ForecastVendor.CONTENT_URI, forecastFarmerValues);
                            }

                        }

                        // check if baseline is available for this season,  update or save it
                        if (baseLine != null && baseLine.containsKey(seasonName)) {

                            BaseLineVendor baseLineInfo = baseLine.get(seasonName);

                            totProdInKg = baseLineInfo.getTotProdInKg();
                            totLostInKg = baseLineInfo.getTotLostInKg();
                            totSoldInKg = baseLineInfo.getTotSoldInKg();
                            totVolumeSoldCoopInKg = baseLineInfo.getTotVolumeSoldCoopInKg();
                            priceSoldToCoopPerKg = baseLineInfo.getPriceSoldToCoopPerKg();
                            totVolSoldInKg = baseLineInfo.getTotVolSoldInKg();
                            priceSoldInKg = baseLineInfo.getPriceSoldInKg();

                            farmerTabId = baseLineInfo.getBaselineId();
                            if (farmerTabId > 0) {

                                isDataAvailable = getContentResolver().query(BfwContract.BaseLineVendor.CONTENT_URI, null, baselineSelect, new String[]{Integer.toString(farmerTabId)}, null);
                                if (isDataAvailable != null && isDataAvailable.moveToFirst()) {
                                    isSync = isDataAvailable.getInt(isDataAvailable.getColumnIndex(BfwContract.BaseLineVendor.COLUMN_IS_SYNC));

                                    ContentValues baselineValues = new ContentValues();
                                    baselineValues.put(BfwContract.BaseLineVendor.COLUMN_TOT_PROD_B_KG, totProdInKg);
                                    baselineValues.put(BfwContract.BaseLineVendor.COLUMN_TOT_LOST_KG, totLostInKg);
                                    baselineValues.put(BfwContract.BaseLineVendor.COLUMN_TOT_SOLD_KG, totSoldInKg);
                                    baselineValues.put(BfwContract.BaseLineVendor.COLUMN_TOT_VOL_SOLD_COOP, totVolumeSoldCoopInKg);

                                    baselineValues.put(BfwContract.BaseLineVendor.COLUMN_PRICE_SOLD_COOP_PER_KG, priceSoldToCoopPerKg);
                                    baselineValues.put(BfwContract.BaseLineVendor.COLUMN_TOT_VOL_SOLD_IN_KG, totVolSoldInKg);
                                    baselineValues.put(BfwContract.BaseLineVendor.COLUMN_PRICE_SOLD_KG, priceSoldInKg);
                                    baselineValues.put(BfwContract.BaseLineVendor.COLUMN_SEASON_ID, baseLineInfo.getHarvestSeason());

                                    baselineValues.put(BfwContract.BaseLineVendor.COLUMN_IS_SYNC, isSync);
                                    baselineValues.put(BfwContract.BaseLineVendor.COLUMN_IS_UPDATE, 0);
                                    baselineValues.put(BfwContract.BaseLineVendor.COLUMN_VENDOR_ID, mFarmerId);

                                    getContentResolver().update(BfwContract.BaseLineVendor.CONTENT_URI, baselineValues, baselineSelect, new String[]{Integer.toString(farmerTabId)});

                                }

                            } else {
                                ContentValues baselineValues = new ContentValues();
                                baselineValues.put(BfwContract.BaseLineVendor.COLUMN_TOT_PROD_B_KG, totProdInKg);
                                baselineValues.put(BfwContract.BaseLineVendor.COLUMN_TOT_LOST_KG, totLostInKg);
                                baselineValues.put(BfwContract.BaseLineVendor.COLUMN_TOT_SOLD_KG, totSoldInKg);
                                baselineValues.put(BfwContract.BaseLineVendor.COLUMN_TOT_VOL_SOLD_COOP, totVolumeSoldCoopInKg);

                                baselineValues.put(BfwContract.BaseLineVendor.COLUMN_PRICE_SOLD_COOP_PER_KG, priceSoldToCoopPerKg);
                                baselineValues.put(BfwContract.BaseLineVendor.COLUMN_TOT_VOL_SOLD_IN_KG, totVolSoldInKg);
                                baselineValues.put(BfwContract.BaseLineVendor.COLUMN_PRICE_SOLD_KG, priceSoldInKg);
                                baselineValues.put(BfwContract.BaseLineVendor.COLUMN_SEASON_ID, baseLineInfo.getHarvestSeason());

                                baselineValues.put(BfwContract.BaseLineVendor.COLUMN_IS_SYNC, 0);
                                baselineValues.put(BfwContract.BaseLineVendor.COLUMN_IS_UPDATE, 0);
                                baselineValues.put(BfwContract.BaseLineVendor.COLUMN_VENDOR_ID, mFarmerId);

                                getContentResolver().insert(BfwContract.BaseLineVendor.CONTENT_URI, baselineValues);
                            }

                        }

                        // check if finance is available for this season, update or save it
                        if (finance != null && finance.containsKey(seasonName)) {

                            FinanceVendor financeInfo = finance.get(seasonName);

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

                                isDataAvailable = getContentResolver().query(BfwContract.FinanceDataVendor.CONTENT_URI, null, financeSelect, new String[]{Integer.toString(farmerTabId)}, null);
                                if (isDataAvailable != null && isDataAvailable.moveToFirst()) {
                                    isSync = isDataAvailable.getInt(isDataAvailable.getColumnIndex(BfwContract.FinanceDataVendor.COLUMN_IS_SYNC));

                                    ContentValues financeValues = new ContentValues();
                                    financeValues.put(BfwContract.FinanceDataVendor.COLUMN_OUTSANDING_LOAN, outstandingLoan);
                                    financeValues.put(BfwContract.FinanceDataVendor.COLUMN_TOT_LOAN_AMOUNT, totLoanAmount);
                                    financeValues.put(BfwContract.FinanceDataVendor.COLUMN_TOT_OUTSTANDING, totOutstanding);
                                    financeValues.put(BfwContract.FinanceDataVendor.COLUMN_INTEREST_RATE, interestRate);

                                    financeValues.put(BfwContract.FinanceDataVendor.COLUMN_DURATION, duration);
                                    financeValues.put(BfwContract.FinanceDataVendor.COLUMN_LOAN_PROVIDER, loanProvider);
                                    financeValues.put(BfwContract.FinanceDataVendor.COLUMN_LOANPROVIDER_AGGREG, isAggregation);
                                    financeValues.put(BfwContract.FinanceDataVendor.COLUMN_LOANPROVIDER_INPUT, isInput);

                                    financeValues.put(BfwContract.FinanceDataVendor.COLUMN_LOANPROVIDER_OTHER, isOther);
                                    financeValues.put(BfwContract.FinanceDataVendor.COLUMN_MOBILE_MONEY_ACCOUNT, isMobileMoneyAccount);
                                    financeValues.put(BfwContract.FinanceDataVendor.COLUMN_SEASON_ID, financeInfo.getHarvestSeason());
                                    financeValues.put(BfwContract.FinanceDataVendor.COLUMN_VENDOR_ID, mFarmerId);
                                    financeValues.put(BfwContract.FinanceDataVendor.COLUMN_IS_SYNC, isSync);
                                    financeValues.put(BfwContract.FinanceDataVendor.COLUMN_IS_UPDATE, 0);

                                    getContentResolver().update(BfwContract.FinanceDataVendor.CONTENT_URI, financeValues, financeSelect, new String[]{Integer.toString(farmerTabId)});

                                }

                            } else {
                                ContentValues financeValues = new ContentValues();
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_OUTSANDING_LOAN, outstandingLoan);
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_TOT_LOAN_AMOUNT, totLoanAmount);
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_TOT_OUTSTANDING, totOutstanding);
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_INTEREST_RATE, interestRate);

                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_DURATION, duration);
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_LOAN_PROVIDER, loanProvider);
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_LOANPROVIDER_AGGREG, isAggregation);
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_LOANPROVIDER_INPUT, isInput);

                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_LOANPROVIDER_OTHER, isOther);
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_MOBILE_MONEY_ACCOUNT, isMobileMoneyAccount);
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_SEASON_ID, financeInfo.getHarvestSeason());
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_VENDOR_ID, mFarmerId);
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_IS_SYNC, 0);
                                financeValues.put(BfwContract.FinanceDataVendor.COLUMN_IS_UPDATE, 0);

                                getContentResolver().insert(BfwContract.FinanceDataVendor.CONTENT_URI, financeValues);
                            }
                        }

                        // check if access o Information is available for this season, update or save it
                        if (accessToInformation != null && accessToInformation.containsKey(seasonName)) {

                            AccessToInformationVendor accessInfo = accessToInformation.get(seasonName);

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

                                isDataAvailable = getContentResolver().query(BfwContract.VendorAccessInfo.CONTENT_URI, null, accessToInfoSelect, new String[]{Integer.toString(farmerTabId)}, null);
                                if (isDataAvailable != null && isDataAvailable.moveToFirst()) {
                                    isSync = isDataAvailable.getInt(isDataAvailable.getColumnIndex(BfwContract.VendorAccessInfo.COLUMN_IS_SYNC));

                                    ContentValues farmerInfoValues = new ContentValues();
                                    farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_AGRI_EXTENSION_SERV, isAgriculture);
                                    farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_CLIMATE_RELATED_INFO, isClimateInfo);
                                    farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_SEEDS, isSeeds);
                                    farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_ORGANIC_FERTILIZER, organicFertilizers);

                                    farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_INORGANIC_FERTILIZER, inorganicFertilizers);
                                    farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_LABOUR, labour);
                                    farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_WATER_PUMPS, waterPumps);
                                    farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_SPRAYERS, isSpreader);

                                    farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_IS_SYNC, isSync);
                                    farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_IS_UPDATE, 0);
                                    farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_SEASON_ID, accessInfo.getHarvestSeason());
                                    farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_VENDOR_ID, mFarmerId);

                                    getContentResolver().update(BfwContract.VendorAccessInfo.CONTENT_URI, farmerInfoValues, accessToInfoSelect, new String[]{Integer.toString(farmerTabId)});

                                }

                            } else {

                                ContentValues farmerInfoValues = new ContentValues();
                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_AGRI_EXTENSION_SERV, isAgriculture);
                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_CLIMATE_RELATED_INFO, isClimateInfo);
                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_SEEDS, isSeeds);
                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_ORGANIC_FERTILIZER, organicFertilizers);

                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_INORGANIC_FERTILIZER, inorganicFertilizers);
                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_LABOUR, labour);
                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_WATER_PUMPS, waterPumps);
                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_SPRAYERS, isSpreader);

                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_IS_SYNC, 0);
                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_IS_UPDATE, 0);
                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_SEASON_ID, accessInfo.getHarvestSeason());
                                farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_VENDOR_ID, mFarmerId);

                                getContentResolver().insert(BfwContract.VendorAccessInfo.CONTENT_URI, farmerInfoValues);

                            }

                        }

                        // check if land information is available for this season, update or save it
                        if (landInformations != null && landInformations.containsKey(seasonName)) {

                            LandInformationVendor landInfo = landInformations.get(seasonName);

                            landSize = landInfo.getLandSize();
                            lat = landInfo.getLat();
                            lng = landInfo.getLng();
                            farmerTabId = landInfo.getLandId();

                            if (farmerTabId > 0) {

                                isDataAvailable = getContentResolver().query(BfwContract.VendorLand.CONTENT_URI, null, landSelect, new String[]{Integer.toString(farmerTabId)}, null);
                                if (isDataAvailable != null && isDataAvailable.moveToFirst()) {

                                    isSync = isDataAvailable.getInt(isDataAvailable.getColumnIndex(BfwContract.VendorLand.COLUMN_IS_SYNC));

                                    ContentValues farmerLandValues = new ContentValues();
                                    farmerLandValues.put(BfwContract.VendorLand.COLUMN_PLOT_SIZE, landSize);
                                    farmerLandValues.put(BfwContract.VendorLand.COLUMN_LAT_INFO, lat);
                                    farmerLandValues.put(BfwContract.VendorLand.COLUMN_LNG_INFO, lng);
                                    farmerLandValues.put(BfwContract.VendorLand.COLUMN_SEASON_ID, landInfo.getHarvestSeason());
                                    farmerLandValues.put(BfwContract.VendorLand.COLUMN_IS_SYNC, isSync);
                                    farmerLandValues.put(BfwContract.VendorLand.COLUMN_IS_UPDATE, 0);
                                    farmerLandValues.put(BfwContract.VendorLand.COLUMN_VENDOR_ID, mFarmerId);

                                    getContentResolver().update(BfwContract.VendorLand.CONTENT_URI, farmerLandValues, landSelect, new String[]{Integer.toString(farmerTabId)});

                                }

                            } else {
                                ContentValues farmerLandValues = new ContentValues();
                                farmerLandValues.put(BfwContract.VendorLand.COLUMN_PLOT_SIZE, landSize);
                                farmerLandValues.put(BfwContract.VendorLand.COLUMN_LAT_INFO, lat);
                                farmerLandValues.put(BfwContract.VendorLand.COLUMN_LNG_INFO, lng);
                                farmerLandValues.put(BfwContract.VendorLand.COLUMN_SEASON_ID, landInfo.getHarvestSeason());
                                farmerLandValues.put(BfwContract.VendorLand.COLUMN_IS_SYNC, 0);
                                farmerLandValues.put(BfwContract.VendorLand.COLUMN_IS_UPDATE, 0);
                                farmerLandValues.put(BfwContract.VendorLand.COLUMN_VENDOR_ID, mFarmerId);

                                getContentResolver().insert(BfwContract.VendorLand.CONTENT_URI, farmerLandValues);
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
            EventBus.getDefault().post(new SaveDataEvent(getResources().getString(R.string.update_msg_vendor), true));

            String farmerSelect1 = BfwContract.Vendor.TABLE_NAME + "." +
                    BfwContract.Vendor.COLUMN_IS_SYNC + " =  1 AND " +
                    BfwContract.Vendor.TABLE_NAME + "." +
                    BfwContract.Vendor.COLUMN_IS_UPDATE + " = 0 ";

            Cursor farmCursor1 = null;

            try {
                farmCursor1 = getContentResolver().query(BfwContract.Vendor.CONTENT_URI, null, farmerSelect1, null, null);
                if (farmCursor1 != null && farmCursor1.getCount() > 0) {

                    //sync if network available
                    if (Utils.isNetworkAvailable(getApplicationContext())) {
                        //start job service
                        startService(new Intent(this, UpdateSyncVendor.class));
                    } else {
                        //schedule a job if no network is available
                        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));

                        Job job = dispatcher.newJobBuilder()
                                .setService(UpdateSyncVendorService.class)
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

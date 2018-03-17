package com.nijus.alino.bfwcoopmanagement.vendors.sync;


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

public class AddVendor extends IntentService {

    public final String LOG_TAG = AddVendor.class.getSimpleName();

    public AddVendor() {
        super("");
    }

    public AddVendor(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            Bundle vendorData = intent.getBundleExtra("vendor_data");

            GeneralVendor general = vendorData.getParcelable("generalVendor");
            ServiceAccessVendor serviceAccess = vendorData.getParcelable("serviceAccessVendor");
            DemographicVendor demographic = vendorData.getParcelable("demographicVendor");

            HashMap<String, ForecastVendor> forecast = (HashMap<String, ForecastVendor>) vendorData.getSerializable("forecastVendor");
            HashMap<String, BaseLineVendor> baseLine = (HashMap<String, BaseLineVendor>) vendorData.getSerializable("baselineVendor");
            HashMap<String, FinanceVendor> finance = (HashMap<String, FinanceVendor>) vendorData.getSerializable("financeVendor");
            HashMap<String, AccessToInformationVendor> accessToInformation = (HashMap<String, AccessToInformationVendor>) vendorData.getSerializable("accessToInformationVendor");
            HashMap<String, LandInformationVendor> landInformations = (HashMap<String, LandInformationVendor>) vendorData.getSerializable("land_infoVendor");

            Cursor cursor = null;

            //General page field
            String name = null, phoneNumber = null, address = null;
            Boolean gender = false;

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

            // vendor table
            if (general != null) {

                name = general.getName();
                phoneNumber = general.getPhoneNumber();
                gender = general.isGender();
                address = general.getAddress();
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
            contentValues.put(BfwContract.Vendor.COLUMN_IS_SYNC, 0);
            contentValues.put(BfwContract.Vendor.COLUMN_IS_UPDATE, 0);

            Uri uri = getContentResolver().insert(BfwContract.Vendor.CONTENT_URI, contentValues);
            long farmerId = ContentUris.parseId(uri);
            String seasonName = null;

            try {
                cursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {

                        seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                        // check if forecast is available for this season and save it
                        if (forecast != null && forecast.containsKey(seasonName)) {

                            numArableLandPlot = forecast.get(seasonName).getArableLandPlot();
                            minimumflowprice = forecast.get(seasonName).getMinimumflowprice();
                            farmerexpectedminppp = forecast.get(seasonName).getFarmerexpectedminppp();

                            ContentValues forecastFarmerValues = new ContentValues();
                            forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_ARABLE_LAND_PLOT, numArableLandPlot);
                            forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_EXPECTED_MIN_PPP, farmerexpectedminppp);
                            forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_FLOW_PRICE, minimumflowprice);
                            forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_SEASON_ID, forecast.get(seasonName).getHarvestSeason());
                            forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_VENDOR_ID, farmerId);
                            forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_IS_SYNC, 0);
                            forecastFarmerValues.put(BfwContract.ForecastVendor.COLUMN_IS_UPDATE, 0);

                            getContentResolver().insert(BfwContract.ForecastVendor.CONTENT_URI, forecastFarmerValues);

                        }

                        // check if baseline is available for this season and save it
                        if (baseLine != null && baseLine.containsKey(seasonName)) {

                            totProdInKg = baseLine.get(seasonName).getTotProdInKg();
                            totLostInKg = baseLine.get(seasonName).getTotLostInKg();
                            totSoldInKg = baseLine.get(seasonName).getTotSoldInKg();
                            totVolumeSoldCoopInKg = baseLine.get(seasonName).getTotVolumeSoldCoopInKg();
                            priceSoldToCoopPerKg = baseLine.get(seasonName).getPriceSoldToCoopPerKg();
                            totVolSoldInKg = baseLine.get(seasonName).getTotVolSoldInKg();
                            priceSoldInKg = baseLine.get(seasonName).getPriceSoldInKg();

                            ContentValues baselineValues = new ContentValues();
                            baselineValues.put(BfwContract.BaseLineVendor.COLUMN_TOT_PROD_B_KG, totProdInKg);
                            baselineValues.put(BfwContract.BaseLineVendor.COLUMN_TOT_LOST_KG, totLostInKg);
                            baselineValues.put(BfwContract.BaseLineVendor.COLUMN_TOT_SOLD_KG, totSoldInKg);
                            baselineValues.put(BfwContract.BaseLineVendor.COLUMN_TOT_VOL_SOLD_COOP, totVolumeSoldCoopInKg);

                            baselineValues.put(BfwContract.BaseLineVendor.COLUMN_PRICE_SOLD_COOP_PER_KG, priceSoldToCoopPerKg);
                            baselineValues.put(BfwContract.BaseLineVendor.COLUMN_TOT_VOL_SOLD_IN_KG, totVolSoldInKg);
                            baselineValues.put(BfwContract.BaseLineVendor.COLUMN_PRICE_SOLD_KG, priceSoldInKg);
                            baselineValues.put(BfwContract.BaseLineVendor.COLUMN_SEASON_ID, baseLine.get(seasonName).getHarvestSeason());

                            baselineValues.put(BfwContract.BaseLineVendor.COLUMN_IS_SYNC, 0);
                            baselineValues.put(BfwContract.BaseLineVendor.COLUMN_IS_UPDATE, 0);
                            baselineValues.put(BfwContract.BaseLineVendor.COLUMN_VENDOR_ID, farmerId);

                            getContentResolver().insert(BfwContract.BaseLineVendor.CONTENT_URI, baselineValues);

                        }

                        // check if finance is available for this season and save it
                        if (finance != null && finance.containsKey(seasonName)) {
                            outstandingLoan = finance.get(seasonName).isOutstandingLoan();
                            isMobileMoneyAccount = finance.get(seasonName).isHasMobileMoneyAccount();
                            isInput = finance.get(seasonName).isInput();
                            isAggregation = finance.get(seasonName).isAggregation();
                            isOther = finance.get(seasonName).isOtherLp();

                            totLoanAmount = finance.get(seasonName).getTotLoanAmount();
                            totOutstanding = finance.get(seasonName).getTotOutstanding();
                            interestRate = finance.get(seasonName).getInterestRate();
                            duration = finance.get(seasonName).getDurationInMonth();
                            loanProvider = finance.get(seasonName).getLoanProvider();

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
                            financeValues.put(BfwContract.FinanceDataVendor.COLUMN_SEASON_ID, finance.get(seasonName).getHarvestSeason());
                            financeValues.put(BfwContract.FinanceDataVendor.COLUMN_VENDOR_ID, farmerId);
                            financeValues.put(BfwContract.FinanceDataVendor.COLUMN_IS_SYNC, 0);
                            financeValues.put(BfwContract.FinanceDataVendor.COLUMN_IS_UPDATE, 0);

                            getContentResolver().insert(BfwContract.FinanceDataVendor.CONTENT_URI, financeValues);
                        }

                        // check if access o Information is available for this season and save it
                        if (accessToInformation != null && accessToInformation.containsKey(seasonName)) {

                            isAgriculture = accessToInformation.get(seasonName).isAgricultureExtension();
                            isClimateInfo = accessToInformation.get(seasonName).isClimateRelatedInformation();
                            isSeeds = accessToInformation.get(seasonName).isSeed();
                            organicFertilizers = accessToInformation.get(seasonName).isOrganicFertilizers();
                            inorganicFertilizers = accessToInformation.get(seasonName).isInorganicFertilizers();
                            labour = accessToInformation.get(seasonName).isLabour();
                            waterPumps = accessToInformation.get(seasonName).isWaterPumps();
                            isSpreader = accessToInformation.get(seasonName).isSpreaderOrSprayer();

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
                            farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_SEASON_ID, accessToInformation.get(seasonName).getHarvestSeason());
                            farmerInfoValues.put(BfwContract.VendorAccessInfo.COLUMN_VENDOR_ID, farmerId);

                            getContentResolver().insert(BfwContract.VendorAccessInfo.CONTENT_URI, farmerInfoValues);

                        }

                        // check if land information is available for this season and save it
                        if (landInformations != null && landInformations.containsKey(seasonName)) {

                            landSize = landInformations.get(seasonName).getLandSize();
                            lat = landInformations.get(seasonName).getLat();
                            lng = landInformations.get(seasonName).getLng();

                            ContentValues farmerLandValues = new ContentValues();
                            farmerLandValues.put(BfwContract.VendorLand.COLUMN_PLOT_SIZE, landSize);
                            farmerLandValues.put(BfwContract.VendorLand.COLUMN_LAT_INFO, lat);
                            farmerLandValues.put(BfwContract.VendorLand.COLUMN_LNG_INFO, lng);
                            farmerLandValues.put(BfwContract.VendorLand.COLUMN_SEASON_ID, landInformations.get(seasonName).getHarvestSeason());
                            farmerLandValues.put(BfwContract.VendorLand.COLUMN_IS_SYNC, 0);
                            farmerLandValues.put(BfwContract.VendorLand.COLUMN_IS_SYNC, 0);
                            farmerLandValues.put(BfwContract.VendorLand.COLUMN_VENDOR_ID, farmerId);

                            getContentResolver().insert(BfwContract.VendorLand.CONTENT_URI, farmerLandValues);
                        }
                    }
                }

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            //Post event after saving data
            EventBus.getDefault().post(new SaveDataEvent(getResources().getString(R.string.add_vendor_msg), true));

            //sync if network available
            if (Utils.isNetworkAvailable(getApplicationContext())) {
                //start job service
                startService(new Intent(this, SyncBackgroundVendor.class));
            } else {
                //schedule a job if not network is available
                FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));

                Job job = dispatcher.newJobBuilder()
                        .setService(SyncVendor.class)
                        .setTag(UUID.randomUUID().toString())
                        .setConstraints(Constraint.ON_ANY_NETWORK)
                        .build();

                dispatcher.mustSchedule(job);
            }
        } else {
            //If no data available
            EventBus.getDefault().post(new SaveDataEvent(getResources().getString(R.string.farm_no_data_av), false));
        }
    }
}

package com.nijus.alino.bfwcoopmanagement.farmers.sync;


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

public class AddFarmer extends IntentService {

    public final String LOG_TAG = AddFarmer.class.getSimpleName();

    public AddFarmer() {
        super("");
    }

    public AddFarmer(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (intent != null) {
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
            Double priceSoldInKg ;

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
            contentValues.put(BfwContract.Farmer.COLUMN_IS_SYNC, 0);
            contentValues.put(BfwContract.Farmer.COLUMN_IS_UPDATE, 0);
            contentValues.put(BfwContract.Farmer.COLUMN_COOP_USER_ID, coopServerId);

            Uri uri = getContentResolver().insert(BfwContract.Farmer.CONTENT_URI, contentValues);
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
                            forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_ARABLE_LAND_PLOT, numArableLandPlot);
                            forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_EXPECTED_MIN_PPP, farmerexpectedminppp);
                            forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_FLOW_PRICE, minimumflowprice);
                            forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_SEASON_ID, forecast.get(seasonName).getHarvestSeason());
                            forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_FARMER_ID, farmerId);
                            forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_IS_SYNC, 0);
                            forecastFarmerValues.put(BfwContract.ForecastFarmer.COLUMN_IS_UPDATE, 0);

                            getContentResolver().insert(BfwContract.ForecastFarmer.CONTENT_URI, forecastFarmerValues);

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
                            baselineValues.put(BfwContract.BaselineFarmer.COLUMN_TOT_PROD_B_KG, totProdInKg);
                            baselineValues.put(BfwContract.BaselineFarmer.COLUMN_TOT_LOST_KG, totLostInKg);
                            baselineValues.put(BfwContract.BaselineFarmer.COLUMN_TOT_SOLD_KG, totSoldInKg);
                            baselineValues.put(BfwContract.BaselineFarmer.COLUMN_TOT_VOL_SOLD_COOP, totVolumeSoldCoopInKg);

                            baselineValues.put(BfwContract.BaselineFarmer.COLUMN_PRICE_SOLD_COOP_PER_KG, priceSoldToCoopPerKg);
                            baselineValues.put(BfwContract.BaselineFarmer.COLUMN_TOT_VOL_SOLD_IN_KG, totVolSoldInKg);
                            baselineValues.put(BfwContract.BaselineFarmer.COLUMN_PRICE_SOLD_KG, priceSoldInKg);
                            baselineValues.put(BfwContract.BaselineFarmer.COLUMN_SEASON_ID, baseLine.get(seasonName).getHarvestSeason());

                            baselineValues.put(BfwContract.BaselineFarmer.COLUMN_IS_SYNC, 0);
                            baselineValues.put(BfwContract.BaselineFarmer.COLUMN_IS_UPDATE, 0);
                            baselineValues.put(BfwContract.BaselineFarmer.COLUMN_FARMER_ID, farmerId);

                            getContentResolver().insert(BfwContract.BaselineFarmer.CONTENT_URI, baselineValues);

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
                            financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_SEASON_ID, finance.get(seasonName).getHarvestSeason());
                            financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_FARMER_ID, farmerId);
                            financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_IS_SYNC, 0);
                            financeValues.put(BfwContract.FinanceDataFarmer.COLUMN_IS_UPDATE, 0);

                            getContentResolver().insert(BfwContract.FinanceDataFarmer.CONTENT_URI, financeValues);
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
                            farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_SEASON_ID, accessToInformation.get(seasonName).getHarvestSeason());
                            farmerInfoValues.put(BfwContract.FarmerAccessInfo.COLUMN_FARMER_ID, farmerId);

                            getContentResolver().insert(BfwContract.FarmerAccessInfo.CONTENT_URI, farmerInfoValues);

                        }

                        // check if land information is available for this season and save it
                        if (landInformations != null && landInformations.containsKey(seasonName)) {

                            landSize = landInformations.get(seasonName).getLandSize();
                            lat = landInformations.get(seasonName).getLat();
                            lng = landInformations.get(seasonName).getLng();

                            ContentValues farmerLandValues = new ContentValues();
                            farmerLandValues.put(BfwContract.LandPlot.COLUMN_PLOT_SIZE, landSize);
                            farmerLandValues.put(BfwContract.LandPlot.COLUMN_LAT_INFO, lat);
                            farmerLandValues.put(BfwContract.LandPlot.COLUMN_LNG_INFO, lng);
                            farmerLandValues.put(BfwContract.LandPlot.COLUMN_SEASON_ID, landInformations.get(seasonName).getHarvestSeason());
                            farmerLandValues.put(BfwContract.LandPlot.COLUMN_IS_SYNC, 0);
                            farmerLandValues.put(BfwContract.LandPlot.COLUMN_IS_SYNC, 0);
                            farmerLandValues.put(BfwContract.LandPlot.COLUMN_FARMER_ID, farmerId);

                            getContentResolver().insert(BfwContract.LandPlot.CONTENT_URI, farmerLandValues);
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
                startService(new Intent(this, SyncBackground.class));
            } else {
                //schedule a job if not network is available
                FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));

                Job job = dispatcher.newJobBuilder()
                        .setService(SyncFarmer.class)
                        .setTag(UUID.randomUUID().toString())
                        .setConstraints(Constraint.ON_ANY_NETWORK)
                        .build();

                dispatcher.mustSchedule(job);
            }
        }

    }
}

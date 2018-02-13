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

        //long locationId = ContentUris.parseId(insertedUri);
        if (intent != null) {
            Bundle farmerData = intent.getBundleExtra("farmer_data");

            General general = farmerData.getParcelable("general");
            Forecast forecast = farmerData.getParcelable("forecast");
            Demographic demographic = farmerData.getParcelable("demographic");
            BaseLine baseLine = farmerData.getParcelable("baseline");
            Finance finance = farmerData.getParcelable("finance");
            ServiceAccess serviceAccess = farmerData.getParcelable("serviceAccess");
            AccessToInformation accessToInformation = farmerData.getParcelable("access_information");
            HashMap map = (HashMap) farmerData.getSerializable("land_info");

            //save data
            int duration = 0, coopServerId = 43, houseHoldMember = 0, totProductionKg = 0, totLostKg = 0, totSolddKg = 0,
                    totSoldVolCoop = 0, totPriceSoldCoopPerKG = 0, priceSoldKg = 0, totVolSideSold = 0, totLoanAmount = 0, totOutstanding = 0;
            String name = "", phoneNumber = "", coopName = "", fName = null, lName = null, cellPhoneAlt = null, cellCarrier = null, memeberShipId = null, loanProvider = null;

            boolean gender = false, isHouseHoldHead = false, isMobileMoneyAccount = false, isInput = false, isAggregation = false, isOther = false, outstandingLoan = false,
                    isAgriculture = false, isClimateInfo = false, isSeeds = false, organicFertilizers = false, inorganicFertilizers = false, labour = false,
                    waterPumps = false, isTractors = false, isHarvester = false, isSpreader = false, isDryer = false, isTresher = false, isSafeStorage = false, isOtherResourceInfo = false,
                    isDam = false, isWell = false, isBoreHole = false, isPipeBorne = false, isRiverStream = false, isIrrigation = false, hasNoWaterSource = false, isOtherInfo = false;

            double arableLandPlot = 0.0, interestRate = 0.0;

            if (general != null) {

                name = general.getName();
                phoneNumber = general.getPhoneNumber();
                coopName = general.getCoopsName();
                gender = general.isGender();

                Cursor cursor = null;

                String selection = BfwContract.Coops.TABLE_NAME + "." +
                        BfwContract.Coops.COLUMN_COOP_NAME + " =  ? ";

                String[] selectionArgs = {coopName};

                try {
                    cursor = getContentResolver().query(BfwContract.Coops.CONTENT_URI, null, selection, selectionArgs, null);

                    if (cursor != null && cursor.moveToFirst()) {
                        cursor.moveToFirst();
                        coopServerId = cursor.getInt(cursor.getColumnIndex(BfwContract.Coops.COLUMN_COOP_SERVER_ID));

                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }

            if (forecast != null) {
                arableLandPlot = forecast.getArableLandPlot();
            }

            if (demographic != null) {
                isHouseHoldHead = demographic.isHouseHold();
                houseHoldMember = demographic.getHouseHoldMember();
                fName = demographic.getSpouseFirstName();
                lName = demographic.getSpouseLastName();
                cellPhoneAlt = demographic.getCellPhoneAlt();
                cellCarrier = demographic.getCellCarrier();
                memeberShipId = demographic.getMemberShipId();
            }

            if (baseLine != null) {

                totProductionKg = baseLine.getTotProdInKg();
                totLostKg = baseLine.getTotLostInKg();
                totSolddKg = baseLine.getTotSoldInKg();
                totSoldVolCoop = baseLine.getTotVolumeSoldCoopInKg();
                totPriceSoldCoopPerKG = baseLine.getPriceSoldToCoopPerKg();
                totVolSideSold = baseLine.getTotVolSoldInKg();
                priceSoldKg = baseLine.getPriceSoldInKg();

            }

            if (finance != null) {

                outstandingLoan = finance.isOutstandingLoan();
                isMobileMoneyAccount = finance.isHasMobileMoneyAccount();
                isInput = finance.isInput();
                isAggregation = finance.isAggregation();
                isOther = finance.isOtherLp();

                totLoanAmount = finance.getTotLoanAmount();
                totOutstanding = finance.getTotOutstanding();
                interestRate = finance.getInterestRate();
                duration = finance.getDurationInMonth();
                loanProvider = finance.getLoanProvider();
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
            }
            if (accessToInformation != null){
                isAgriculture = accessToInformation.isAgricultureExtension();
                isClimateInfo = accessToInformation.isClimateRelatedInformation();
                isSeeds = accessToInformation.isSeed();
                organicFertilizers = accessToInformation.isOrganicFertilizers();
                inorganicFertilizers = accessToInformation.isInorganicFertilizers();
                labour = accessToInformation.isLabour();
                waterPumps = accessToInformation.isWaterPumps();
                isSpreader = accessToInformation.isSpreaderOrSprayer();
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put(BfwContract.Farmer.COLUMN_NAME, name);
            contentValues.put(BfwContract.Farmer.COLUMN_GENDER, gender ? "male" : "female");
            contentValues.put(BfwContract.Farmer.COLUMN_PHONE, phoneNumber);

            contentValues.put(BfwContract.Farmer.COLUMN_HOUSEHOLD_HEAD, isHouseHoldHead ? 1 : 0);
            contentValues.put(BfwContract.Farmer.COLUMN_HOUSE_MEMBER, houseHoldMember);
            contentValues.put(BfwContract.Farmer.COLUMN_FIRST_NAME, fName);
            contentValues.put(BfwContract.Farmer.COLUMN_LAST_NAME, lName);
            contentValues.put(BfwContract.Farmer.COLUMN_CELL_PHONE, cellPhoneAlt);
            contentValues.put(BfwContract.Farmer.COLUMN_CELL_CARRIER, cellCarrier);
            contentValues.put(BfwContract.Farmer.COLUMN_MEMBER_SHIP, memeberShipId);
            contentValues.put(BfwContract.Farmer.COLUMN_TRACTORS, isTractors ? 1 : 0);
            contentValues.put(BfwContract.Farmer.COLUMN_HARVESTER, isHarvester ? 1 : 0);
            contentValues.put(BfwContract.Farmer.COLUMN_DRYER, isDryer ? 1 : 0);
            contentValues.put(BfwContract.Farmer.COLUMN_TRESHER, isTresher ? 1 : 0);
            contentValues.put(BfwContract.Farmer.COLUMN_SAFE_STORAGE, isSafeStorage ? 1 : 0);
            contentValues.put(BfwContract.Farmer.COLUMN_OTHER_INFO, isOtherInfo ? 1 : 0);
            contentValues.put(BfwContract.Farmer.COLUMN_DAM, isDam ? 1 : 0);
            contentValues.put(BfwContract.Farmer.COLUMN_WELL, isWell ? 1 : 0);
            contentValues.put(BfwContract.Farmer.COLUMN_PIPE_BORNE, isPipeBorne ? 1 : 0);
            contentValues.put(BfwContract.Farmer.COLUMN_RIVER_STREAM, isRiverStream ? 1 : 0);
            contentValues.put(BfwContract.Farmer.COLUMN_IRRIGATION, isIrrigation ? 1 : 0);
            contentValues.put(BfwContract.Farmer.COLUMN_NONE, hasNoWaterSource ? 1 : 0);
            contentValues.put(BfwContract.Farmer.COLUMN_OTHER, isOtherResourceInfo ? 1 : 0);
            contentValues.put(BfwContract.Farmer.COLUMN_IS_SYNC, 0);
            contentValues.put(BfwContract.Farmer.COLUMN_IS_UPDATE, 0);
            contentValues.put(BfwContract.Farmer.COLUMN_COOP_USER_ID, coopServerId);

            Uri uri = getContentResolver().insert(BfwContract.Farmer.CONTENT_URI, contentValues);

            long id = ContentUris.parseId(uri);

            if (map != null) {
                //save land information
                ContentValues landFarmer;
                double landInfo;

                for (Object landSize : map.keySet()) {
                    landFarmer = new ContentValues();
                    landInfo = (double) map.get(landSize.toString());
                    landFarmer.put(BfwContract.LandPlot.COLUMN_PLOT_SIZE, landInfo);
                    landFarmer.put(BfwContract.LandPlot.COLUMN_FARMER_ID, id);
                    landFarmer.put(BfwContract.LandPlot.COLUMN_LAND_ID, UUID.randomUUID().toString());
                    landFarmer.put(BfwContract.LandPlot.COLUMN_IS_SYNC, 0);
                    landFarmer.put(BfwContract.LandPlot.COLUMN_SERVER_ID, 0);
                    landFarmer.put(BfwContract.LandPlot.COLUMN_IS_UPDATE, 0);

                    getContentResolver().insert(BfwContract.LandPlot.CONTENT_URI, landFarmer);
                }
            }

            //Post event after saving data
            EventBus.getDefault().post(new SaveDataEvent());

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

package com.nijus.alino.bfwcoopmanagement.farmers.ui.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.farmers.adapter.AccessToInformationFarmerAdapter;
import com.nijus.alino.bfwcoopmanagement.farmers.adapter.BaselineFarmerAdapter;
import com.nijus.alino.bfwcoopmanagement.farmers.adapter.FinanceDataFarmerAdapter;
import com.nijus.alino.bfwcoopmanagement.farmers.adapter.ForecastDataFarmerAdapter;
import com.nijus.alino.bfwcoopmanagement.farmers.adapter.LandFarmerDataAdapter;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.AccessToInformation;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.BaseLine;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.Finance;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.Forecast;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.LandInformation;
import com.riyagayasen.easyaccordion.AccordionExpansionCollapseListener;
import com.riyagayasen.easyaccordion.AccordionView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


public class DetailFarmerActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private Long mFarmerId;
    private Uri mUri;
    private String name;
    public static final String ARG_KEY = "key";
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ListView infoListView;
    private ListView baselineListView;
    private ListView financeDataListView;
    private ListView landDataListView;
    private ListView forecastListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_farmer);

        getSupportLoaderManager().initLoader(0, null, this);

        Intent intent = this.getIntent();
        if (intent.hasExtra("farmerId")) {
            mFarmerId = intent.getLongExtra("farmerId", 0);
            mUri = BfwContract.Farmer.buildFarmerUri(mFarmerId);
        }

        collapsingToolbarLayout = findViewById(R.id.name_farmer);
        infoListView = findViewById(R.id.info_list);
        baselineListView = findViewById(R.id.baseline_list);
        financeDataListView = findViewById(R.id.finance_data_list);
        landDataListView = findViewById(R.id.land_data_list);
        forecastListView = findViewById(R.id.forecast_data_list);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab_coop);
        ImageView imageView = findViewById(R.id.coop_image);
        imageView.setImageResource(R.mipmap.farmer_bg);
        fab.setImageResource(R.drawable.ic_edit_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), UpdateFarmer.class);
                intent1.putExtra("farmerId", mFarmerId);
                startActivity(intent1);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String farmerSelection = BfwContract.Farmer.TABLE_NAME + "." +
                BfwContract.Farmer._ID + " =  ? ";

        if (mUri != null) {
            return new CursorLoader(
                    this,
                    mUri,
                    null,
                    farmerSelection,
                    new String[]{Long.toString(mFarmerId)},
                    null
            );
        }
        return null;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveDataEvent(SaveDataEvent saveDataEvent) {
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {

            name = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_NAME));
            String phone = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_PHONE));
            String gender = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_GENDER));
            String address = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_ADDRESS));

            double farmerLandSize = 0.0;
            String coopName = "";
            Cursor farmerCursor = null;
            Cursor landFarmerCursor = null;

            String farmerCoop = BfwContract.Coops.TABLE_NAME + "." +
                    BfwContract.Coops._ID + " = ? ";

            String landFarmerInfo = BfwContract.LandPlot.TABLE_NAME + "." +
                    BfwContract.LandPlot._ID + " = ? ";

            String farmerSelection = BfwContract.Farmer.TABLE_NAME + "." +
                    BfwContract.Farmer._ID + " =  ? ";

            int coopId = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_COOP_USER_ID));

            try {

                farmerCursor = getContentResolver().query(BfwContract.Coops.CONTENT_URI, null, farmerCoop, new String[]{Integer.toString(coopId)}, null);
                if (farmerCursor != null) {
                    while (farmerCursor.moveToNext()) {
                        coopName = farmerCursor.getString(farmerCursor.getColumnIndex(BfwContract.Coops.COLUMN_COOP_NAME));
                    }
                }

                landFarmerCursor = getContentResolver().query(BfwContract.LandPlot.buildLandPlotUri(mFarmerId), null, farmerSelection, new String[]{Long.toString(mFarmerId)}, null);

                if (landFarmerCursor != null) {
                    while (landFarmerCursor.moveToNext()) {
                        farmerLandSize += landFarmerCursor.getDouble(landFarmerCursor.getColumnIndex(BfwContract.LandPlot.COLUMN_PLOT_SIZE));
                    }
                }

            } finally {
                if (farmerCursor != null) {
                    farmerCursor.close();
                }

                if (landFarmerCursor != null) {
                    landFarmerCursor.close();
                }
            }


            int house_h_h = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_HOUSEHOLD_HEAD));
            int house_h_member = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_HOUSE_MEMBER));
            String first_name = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_FIRST_NAME));
            String last_name = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_LAST_NAME));
            String cell_phone = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_CELL_PHONE));
            String cell_carrer = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_CELL_CARRIER));
            String membership = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_MEMBER_SHIP));

            //available resources
            int tractor = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_TRACTORS));
            int harvester = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_HARVESTER));
            int dryer = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_DRYER));
            int thresher = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_TRESHER));
            int safe_storage = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_SAFE_STORAGE));
            int other_info = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_OTHER_INFO));

            //main water source
            int dam = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_DAM));
            int well = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_WELL));
            int borehole = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_BOREHOLE));
            int river_stream = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_RIVER_STREAM));
            int pipe_borne = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_PIPE_BORNE));
            int irrigation = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_IRRIGATION));
            int none = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_NONE));
            int others = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_OTHER));

            toolbar.setTitle(name);
            collapsingToolbarLayout.setTitle(name);

            ImageView gen_info_pic = findViewById(R.id.gen_info_pic);
            gen_info_pic.setImageResource(R.drawable.profile);

            TextView nom_f_details = findViewById(R.id.name_f_details);
            nom_f_details.setText(name);

            TextView phone_f_details = findViewById(R.id.phone_f_details);
            phone_f_details.setText(phone);

            TextView sex_f_details = findViewById(R.id.sex_f_details);
            sex_f_details.setText(gender);

            TextView address_f_details = findViewById(R.id.address_f_details);
            if (address == null || address.equals("null")) {
                address_f_details.setText("");
            } else {
                address_f_details.setText(address);
            }


            TextView coop_f_details = findViewById(R.id.coop_f_details);
            coop_f_details.setText(coopName);

            TextView tot_land_f_details = findViewById(R.id.tot_land_f_details);

            String landFarmInfo = farmerLandSize + " ha ";
            tot_land_f_details.setText(landFarmInfo);

            //demographic area
            ImageView demo_pic_f_details = findViewById(R.id.demo_pic_f_details);
            demo_pic_f_details.setImageResource(R.drawable.profile);

            ImageView pic_house_hold = findViewById(R.id.pic_house_hold);
            pic_house_hold.setImageResource((house_h_h == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            TextView household_member_f_details = findViewById(R.id.household_member_f_details1);
            String houseHoldMember = house_h_member + "";
            household_member_f_details.setText(houseHoldMember);

            TextView spouse_f_name_f_details = findViewById(R.id.spouse_f_name_f_details1);
            if (first_name == null || first_name.equals("null")) {
                spouse_f_name_f_details.setText("");
            } else {
                spouse_f_name_f_details.setText(first_name);
            }

            TextView spouse_last_name_f_details = findViewById(R.id.spouse_last_name_f_details1);
            if (last_name == null || last_name.equals("null")) {
                spouse_last_name_f_details.setText("");
            } else {
                spouse_last_name_f_details.setText(last_name);
            }


            TextView cell_phone_alt_f_details = findViewById(R.id.cell_phone_alt_f_details1);
            if (cell_phone == null || cell_phone.equals("null")) {
                cell_phone_alt_f_details.setText("");
            } else {
                cell_phone_alt_f_details.setText(cell_phone);
            }


            TextView cell_carrier_f_details = findViewById(R.id.cell_carrier_f_details1);
            if (cell_carrer == null || cell_carrer.equals("null")) {
                cell_carrier_f_details.setText("");
            } else {
                cell_carrier_f_details.setText(cell_carrer);
            }


            TextView membership_f_details = findViewById(R.id.membership_f_details1);
            membership_f_details.setText(membership);

            //Available resources
            ImageView pic_tractor_details = findViewById(R.id.pic_tractor_details);
            pic_tractor_details.setImageResource((tractor == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            ImageView pic_harv_details = findViewById(R.id.pic_harv_details);
            pic_harv_details.setImageResource((harvester == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            ImageView pic_dryer_details = findViewById(R.id.pic_dryer_details);
            pic_dryer_details.setImageResource((dryer == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            ImageView pic_thresher_details = findViewById(R.id.pic_thresher_details);
            pic_thresher_details.setImageResource((thresher == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            ImageView pic_safe_storage_details = findViewById(R.id.pic_safe_storage_details);
            pic_safe_storage_details.setImageResource((safe_storage == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            ImageView pic_other_details = findViewById(R.id.pic_other_details);
            pic_other_details.setImageResource((other_info == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            //Main water sources
            ImageView pic_dam_details = findViewById(R.id.pic_dam_details);
            pic_dam_details.setImageResource((dam == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            ImageView pic_well_details = findViewById(R.id.pic_well_details);
            pic_well_details.setImageResource((well == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            ImageView pic_borehole_details = findViewById(R.id.pic_borehole_details);
            pic_borehole_details.setImageResource((borehole == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            ImageView pic_river_Stream_details = findViewById(R.id.pic_river_Stream_details);
            pic_river_Stream_details.setImageResource((river_stream == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            ImageView pic_pipe_borne_details = findViewById(R.id.pic_pipe_borne_details);
            pic_pipe_borne_details.setImageResource((pipe_borne == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            ImageView pic_irrigation_details = findViewById(R.id.pic_irrigation_details);
            pic_irrigation_details.setImageResource((irrigation == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            ImageView pic_none_details = findViewById(R.id.pic_none_details);
            pic_none_details.setImageResource((none == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            ImageView pic_other2_details = findViewById(R.id.pic_other2_details);
            pic_other2_details.setImageResource((others == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            //1. ACCESS TO INFORMATION
            final AccordionView access_info_accordion = findViewById(R.id.access_info_accordion);
            access_info_accordion.setHeadingString("ACCESS TO INFORMATION");

            //2. ACCORDION BASELINE
            final AccordionView baseline_accordion = findViewById(R.id.baseline_accordion);
            baseline_accordion.setHeadingString("BASELINE FARMER");


            //3. ACCORDION FIMANCE
            final AccordionView finace_accordion = findViewById(R.id.finance_accordion);
            finace_accordion.setHeadingString("FINANCE DATA");


            //4. ACCORDION FARMER LAND
            final AccordionView farmer_land_accordion = findViewById(R.id.farmer_land_accordion);
            farmer_land_accordion.setHeadingString("FARMER LAND DATA");

            //5. ACCORDION FORECAST
            final AccordionView forecast_accordion = findViewById(R.id.forecast_accordion);
            forecast_accordion.setHeadingString("FORECAST DATA");

            // create URI
            Uri infoUri = BfwContract.FarmerAccessInfo.buildFarmerAccessInfoUri(mFarmerId);
            Uri baselineUri = BfwContract.BaselineFarmer.buildBaselineFarmerUri(mFarmerId);
            Uri financeUri = BfwContract.FinanceDataFarmer.buildFinanceDataFarmerUri(mFarmerId);
            Uri landUri = BfwContract.LandPlot.buildLandPlotUri(mFarmerId);
            Uri forecastUri = BfwContract.ForecastFarmer.buildForecastFarmerUri(mFarmerId);

            String farmerSelectionLand = BfwContract.Farmer.TABLE_NAME + "." +
                    BfwContract.Farmer._ID + " =  ? ";
            String seasonSelection = BfwContract.HarvestSeason.TABLE_NAME
                    + "." + BfwContract.HarvestSeason._ID
                    + " = ?";


            // Construct info list
            Cursor farmerDataCursor = null, seasonCursor = null;
            ArrayList<AccessToInformation> accessToInformations = new ArrayList<>();
            ArrayList<BaseLine> baseLines = new ArrayList<>();
            ArrayList<Finance> finances = new ArrayList<>();
            ArrayList<LandInformation> landInformations = new ArrayList<>();
            ArrayList<Forecast> forecasts = new ArrayList<>();

            try {
                farmerDataCursor = getContentResolver().query(infoUri, null, farmerSelectionLand, new String[]{Long.toString(mFarmerId)}, null);

                //Access to information
                if (farmerDataCursor != null) {
                    int isAgricultureExtension;
                    int isClimateRelatedInformation;
                    int isSeed;
                    int isOrganicFertilizers;
                    int isInorganicFertilizers;
                    int isLabour;
                    int isWaterPumps;
                    int isSpreaderOrSprayer;
                    int seasonId;

                    String seasonName = "";

                    while (farmerDataCursor.moveToNext()) {

                        isAgricultureExtension = farmerDataCursor.getInt(farmerDataCursor.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_AGRI_EXTENSION_SERV));
                        isClimateRelatedInformation = farmerDataCursor.getInt(farmerDataCursor.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_CLIMATE_RELATED_INFO));
                        isSeed = farmerDataCursor.getInt(farmerDataCursor.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_SEEDS));
                        isOrganicFertilizers = farmerDataCursor.getInt(farmerDataCursor.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_ORGANIC_FERTILIZER));
                        isInorganicFertilizers = farmerDataCursor.getInt(farmerDataCursor.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_INORGANIC_FERTILIZER));
                        isLabour = farmerDataCursor.getInt(farmerDataCursor.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_LABOUR));
                        isWaterPumps = farmerDataCursor.getInt(farmerDataCursor.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_WATER_PUMPS));
                        isSpreaderOrSprayer = farmerDataCursor.getInt(farmerDataCursor.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_SPRAYERS));

                        seasonId = farmerDataCursor.getInt(farmerDataCursor.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_SEASON_ID));

                        seasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, seasonSelection,
                                new String[]{Integer.toString(seasonId)}, null);

                        if (seasonCursor != null) {
                            seasonCursor.moveToFirst();
                            seasonName = seasonCursor.getString(seasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                        }
                        AccessToInformation accessToInformation = new AccessToInformation(isAgricultureExtension == 1, isClimateRelatedInformation == 1,
                                isSeed == 1, isOrganicFertilizers == 1, isInorganicFertilizers == 1,
                                isLabour == 1, isWaterPumps == 1, isSpreaderOrSprayer == 1, seasonId);
                        accessToInformation.setSeasonName(seasonName);

                        accessToInformations.add(accessToInformation);

                    }

                }

                //Baseline
                farmerDataCursor = getContentResolver().query(baselineUri, null, farmerSelection, new String[]{Long.toString(mFarmerId)}, null);
                if (farmerDataCursor != null) {
                    double totProd;
                    double totLost;
                    double totSolKg;
                    double totVSoldCoop;
                    double priceSoldCoop;
                    double totVolSKg;
                    double priceSold;

                    int seasonId;

                    String seasonName = "";

                    while (farmerDataCursor.moveToNext()) {

                        seasonId = farmerDataCursor.getInt(farmerDataCursor.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_SEASON_ID));

                        totProd = farmerDataCursor.getDouble(farmerDataCursor.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_TOT_PROD_B_KG));
                        totLost = farmerDataCursor.getDouble(farmerDataCursor.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_TOT_LOST_KG));
                        totSolKg = farmerDataCursor.getDouble(farmerDataCursor.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_TOT_SOLD_KG));
                        totVSoldCoop = farmerDataCursor.getDouble(farmerDataCursor.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_TOT_VOL_SOLD_COOP));
                        priceSoldCoop = farmerDataCursor.getDouble(farmerDataCursor.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_PRICE_SOLD_COOP_PER_KG));
                        totVolSKg = farmerDataCursor.getDouble(farmerDataCursor.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_TOT_VOL_SOLD_IN_KG));
                        priceSold = farmerDataCursor.getDouble(farmerDataCursor.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_PRICE_SOLD_KG));

                        seasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, seasonSelection,
                                new String[]{Integer.toString(seasonId)}, null);

                        if (seasonCursor != null) {
                            seasonCursor.moveToFirst();
                            seasonName = seasonCursor.getString(seasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                        }

                        BaseLine baseLine = new BaseLine(totProd, totLost, totSolKg, totVSoldCoop, priceSoldCoop, totVolSKg, priceSold, seasonId);
                        baseLine.setSeasonName(seasonName);
                        baseLines.add(baseLine);

                    }
                }

                //Finance data farmer
                farmerDataCursor = getContentResolver().query(financeUri, null, farmerSelection, new String[]{Long.toString(mFarmerId)}, null);

                if (farmerDataCursor != null) {

                    int lOutstandingLoan;
                    double lTotLoanAmount;
                    double lTotOutstanding;
                    double lInterestRate;
                    int lDuration;
                    String lProvider;
                    int isMMAccount;
                    int lInput;
                    int lAggregation;
                    int lOther;
                    int seasonId;
                    String seasonName = "";

                    while (farmerDataCursor.moveToNext()) {

                        seasonId = farmerDataCursor.getInt(farmerDataCursor.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_SEASON_ID));
                        lOutstandingLoan = farmerDataCursor.getInt(farmerDataCursor.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_OUTSANDING_LOAN));
                        lTotLoanAmount = farmerDataCursor.getDouble(farmerDataCursor.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_TOT_LOAN_AMOUNT));
                        lTotOutstanding = farmerDataCursor.getDouble(farmerDataCursor.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_TOT_OUTSTANDING));
                        lInterestRate = farmerDataCursor.getDouble(farmerDataCursor.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_INTEREST_RATE));
                        lDuration = farmerDataCursor.getInt(farmerDataCursor.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_DURATION));
                        lProvider = farmerDataCursor.getString(farmerDataCursor.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_LOAN_PROVIDER));
                        isMMAccount = farmerDataCursor.getInt(farmerDataCursor.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_MOBILE_MONEY_ACCOUNT));
                        lInput = farmerDataCursor.getInt(farmerDataCursor.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_LOANPROVIDER_INPUT));
                        lAggregation = farmerDataCursor.getInt(farmerDataCursor.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_LOANPROVIDER_AGGREG));
                        lOther = farmerDataCursor.getInt(farmerDataCursor.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_LOANPROVIDER_OTHER));

                        seasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, seasonSelection,
                                new String[]{Integer.toString(seasonId)}, null);

                        if (seasonCursor != null) {
                            seasonCursor.moveToFirst();
                            seasonName = seasonCursor.getString(seasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                        }
                        Finance finance = new Finance(lOutstandingLoan == 1, isMMAccount == 1, lInput == 1,
                                lAggregation == 1, lOther == 1, lTotOutstanding, lInterestRate, lDuration, lProvider, lTotLoanAmount, seasonId);
                        finance.setSeasonName(seasonName);
                        finances.add(finance);
                    }

                }

                //Land information
                farmerDataCursor = getContentResolver().query(landUri, null, farmerSelection, new String[]{Long.toString(mFarmerId)}, null);

                if (farmerDataCursor != null) {

                    double landSize;
                    double lat;
                    double lng;
                    int seasonId;

                    String seasonName = "";

                    while (farmerDataCursor.moveToNext()) {

                        seasonId = farmerDataCursor.getInt(farmerDataCursor.getColumnIndex(BfwContract.LandPlot.COLUMN_SEASON_ID));
                        lat = farmerDataCursor.getDouble(farmerDataCursor.getColumnIndex(BfwContract.LandPlot.COLUMN_LAT_INFO));
                        lng = farmerDataCursor.getDouble(farmerDataCursor.getColumnIndex(BfwContract.LandPlot.COLUMN_LNG_INFO));
                        landSize = farmerDataCursor.getDouble(farmerDataCursor.getColumnIndex(BfwContract.LandPlot.COLUMN_PLOT_SIZE));

                        seasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, seasonSelection,
                                new String[]{Integer.toString(seasonId)}, null);

                        if (seasonCursor != null) {
                            seasonCursor.moveToFirst();
                            seasonName = seasonCursor.getString(seasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                        }
                        LandInformation landInformation = new LandInformation(landSize, lat, lng, seasonId);
                        landInformation.setSeasonName(seasonName);
                        landInformations.add(landInformation);
                    }

                }

                //Forecast Farmer
                farmerDataCursor = getContentResolver().query(forecastUri, null, farmerSelection, new String[]{Long.toString(mFarmerId)}, null);

                if (farmerDataCursor != null) {

                    double arableLandPlot;
                    double totProdKg;
                    double salesOutsidePpp;
                    double postHarvestLossInKg;
                    double totCoopLandSize;
                    double farmerPercentCoopLandSize;

                    double currentPppContrib;
                    double farmerContributionPpp;

                    double farmerexpectedmin;
                    double minimumprice;

                    int seasonId;
                    String seasonName = "";

                    while (farmerDataCursor.moveToNext()) {

                        seasonId = farmerDataCursor.getInt(farmerDataCursor.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_SEASON_ID));
                        arableLandPlot = farmerDataCursor.getDouble(farmerDataCursor.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_ARABLE_LAND_PLOT));
                        totProdKg = farmerDataCursor.getDouble(farmerDataCursor.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_PRODUCTION_MT));
                        salesOutsidePpp = farmerDataCursor.getDouble(farmerDataCursor.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_YIELD_MT));
                        postHarvestLossInKg = farmerDataCursor.getDouble(farmerDataCursor.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_HARVEST_SALE_VALUE));
                        totCoopLandSize = farmerDataCursor.getDouble(farmerDataCursor.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_COOP_LAND_SIZE));
                        farmerPercentCoopLandSize = farmerDataCursor.getDouble(farmerDataCursor.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_PERCENT_FARMER_LAND_SIZE));
                        currentPppContrib = farmerDataCursor.getDouble(farmerDataCursor.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_PPP_COMMITMENT));
                        farmerContributionPpp = farmerDataCursor.getDouble(farmerDataCursor.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_CONTRIBUTION_PPP));


                        farmerexpectedmin = farmerDataCursor.getDouble(farmerDataCursor.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_EXPECTED_MIN_PPP));
                        minimumprice = farmerDataCursor.getDouble(farmerDataCursor.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_FLOW_PRICE));

                        seasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, seasonSelection,
                                new String[]{Integer.toString(seasonId)}, null);

                        if (seasonCursor != null && seasonCursor.moveToFirst()) {
                            seasonName = seasonCursor.getString(seasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                        }

                        Forecast forecast = new Forecast(arableLandPlot, totProdKg, salesOutsidePpp, postHarvestLossInKg, totCoopLandSize,
                                farmerPercentCoopLandSize, seasonId, farmerexpectedmin, minimumprice, currentPppContrib, farmerContributionPpp);

                        forecast.setSeasonName(seasonName);
                        forecasts.add(forecast);
                    }

                }

            } finally {
                if (farmerDataCursor != null) {
                    farmerDataCursor.close();
                }
                if (seasonCursor != null) {
                    seasonCursor.close();
                }
            }
            // Pass list to adapters
            AccessToInformationFarmerAdapter accessToInformationFarmerAdapter = new AccessToInformationFarmerAdapter(getApplicationContext(), accessToInformations);
            BaselineFarmerAdapter baselineFarmerAdapter = new BaselineFarmerAdapter(getApplicationContext(), baseLines);
            FinanceDataFarmerAdapter financeDataFarmerAdapter = new FinanceDataFarmerAdapter(getApplicationContext(), finances);
            LandFarmerDataAdapter landFarmerDataAdapter = new LandFarmerDataAdapter(getApplicationContext(), landInformations);
            ForecastDataFarmerAdapter forecastDataFarmerAdapter = new ForecastDataFarmerAdapter(getApplicationContext(), forecasts);

            infoListView.setAdapter(accessToInformationFarmerAdapter);
            baselineListView.setAdapter(baselineFarmerAdapter);
            financeDataListView.setAdapter(financeDataFarmerAdapter);
            landDataListView.setAdapter(landFarmerDataAdapter);
            forecastListView.setAdapter(forecastDataFarmerAdapter);


            //SET ANIMATION FOR each accordion
            access_info_accordion.setAnimated(true);
            baseline_accordion.setAnimated(true);
            finace_accordion.setAnimated(true);
            farmer_land_accordion.setAnimated(true);
            forecast_accordion.setAnimated(true);

            //ADD LISTENER TO ACCORDIONS
            access_info_accordion.setOnExpandCollapseListener(new AccordionExpansionCollapseListener() {
                @Override
                public void onExpanded(AccordionView view) {
                    access_info_accordion.setHeadingBackGround(R.color.bg_detail);
                }

                @Override
                public void onCollapsed(AccordionView view) {
                    access_info_accordion.setHeadingBackGround(R.color.default_color);


                }
            });

            finace_accordion.setOnExpandCollapseListener(new AccordionExpansionCollapseListener() {
                @Override
                public void onExpanded(AccordionView view) {
                    finace_accordion.setHeadingBackGround(R.color.bg_detail);
                }

                @Override
                public void onCollapsed(AccordionView view) {
                    finace_accordion.setHeadingBackGround(R.color.default_color);
                }
            });

            baseline_accordion.setOnExpandCollapseListener(new AccordionExpansionCollapseListener() {
                @Override
                public void onExpanded(AccordionView view) {
                    baseline_accordion.setHeadingBackGround(R.color.bg_detail);
                }

                @Override
                public void onCollapsed(AccordionView view) {
                    baseline_accordion.setHeadingBackGround(R.color.default_color);
                }
            });

            farmer_land_accordion.setOnExpandCollapseListener(new AccordionExpansionCollapseListener() {
                @Override
                public void onExpanded(AccordionView view) {
                    farmer_land_accordion.setHeadingBackGround(R.color.bg_detail);
                }

                @Override
                public void onCollapsed(AccordionView view) {
                    farmer_land_accordion.setHeadingBackGround(R.color.default_color);
                }
            });

            forecast_accordion.setOnExpandCollapseListener(new AccordionExpansionCollapseListener() {
                @Override
                public void onExpanded(AccordionView view) {
                    forecast_accordion.setHeadingBackGround(R.color.bg_detail);
                }

                @Override
                public void onCollapsed(AccordionView view) {
                    forecast_accordion.setHeadingBackGround(R.color.default_color);
                }
            });


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

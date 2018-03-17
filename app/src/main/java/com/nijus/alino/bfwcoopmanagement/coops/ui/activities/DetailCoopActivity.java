package com.nijus.alino.bfwcoopmanagement.coops.ui.activities;

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

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.adapter.AccessToInformationAdapter;
import com.nijus.alino.bfwcoopmanagement.coops.adapter.BaselineFinanceInfoAdapter;
import com.nijus.alino.bfwcoopmanagement.coops.adapter.BaselineSalesAdapter;
import com.nijus.alino.bfwcoopmanagement.coops.adapter.BaselineYieldAdapter;
import com.nijus.alino.bfwcoopmanagement.coops.adapter.ExpectedYieldAdapter;
import com.nijus.alino.bfwcoopmanagement.coops.adapter.ForecastSalesAdapter;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.AccessToInformation;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.BaselineFinanceInfo;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.BaselineSales;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.BaselineYield;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.ExpectedYield;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.ForecastSales;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.GeneralInformation;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.ui.PageFragmentCallbacks;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.riyagayasen.easyaccordion.AccordionExpansionCollapseListener;
import com.riyagayasen.easyaccordion.AccordionView;

import java.util.ArrayList;


public class DetailCoopActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String ARG_KEY = "key";
    Long mCoopId;
    private Uri mUri;
    private int coopId;
    private String name;
    private Page mPage;
    private String mKey;
    private PageFragmentCallbacks mCallbacks;
    private GeneralInformation general;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView coop_image_details;

    private ListView accessToInfoListView;
    private ListView forecastSalesListView;
    private ListView baseLineYieldListView;

    private ListView baseLineSalesListView;
    private ListView baseLineFinanceInfoListView;
    private ListView expectedYieldListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_coop);

        getSupportLoaderManager().initLoader(0, null, this);

        //get Coop Id
        Intent intent = this.getIntent();
        if (intent.hasExtra("coopId")) {
            mCoopId = intent.getLongExtra("coopId", 0);
            mUri = BfwContract.Coops.buildCoopUri(mCoopId);
        }

        collapsingToolbarLayout = findViewById(R.id.name_coop);
        toolbar = findViewById(R.id.toolbar_coop);

        expectedYieldListView = findViewById(R.id.expected_yield_list);
        baseLineFinanceInfoListView = findViewById(R.id.baseline_finance_info_list);
        baseLineSalesListView = findViewById(R.id.baseline_sales_list);

        accessToInfoListView = findViewById(R.id.access_info_list);
        forecastSalesListView = findViewById(R.id.forecast_sales_list);
        baseLineYieldListView = findViewById(R.id.baseline_yield_list);

        coop_image_details = findViewById(R.id.coop_image_details);
        coop_image_details.setImageResource(R.mipmap.coop_bg);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab_edit_coop);
        ImageView imageView = findViewById(R.id.coop_image_details);
        imageView.setImageResource(R.mipmap.coop_bg);
        fab.setVisibility(View.GONE);
        fab.setImageResource(R.drawable.ic_edit_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent(getApplicationContext(), UpdateCoop.class);
                intent1.putExtra("coopId", mCoopId);
                startActivity(intent1);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String coopSelection = BfwContract.Coops.TABLE_NAME + "." +
                BfwContract.Coops._ID + " =  ? ";

        if (mUri != null) {
            return new CursorLoader(
                    this,
                    mUri,
                    null,
                    coopSelection,
                    new String[]{Long.toString(mCoopId)},
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.moveToFirst()) {

            name = data.getString(data.getColumnIndex(BfwContract.Coops.COLUMN_COOP_NAME));
            String phone = data.getString(data.getColumnIndex(BfwContract.Coops.COLUMN_PHONE));
            String address = data.getString(data.getColumnIndex(BfwContract.Coops.COLUMN_ADDRESS));
            String email = data.getString(data.getColumnIndex(BfwContract.Coops.COLUMN_EMAIL));

            String chairName = data.getString(data.getColumnIndex(BfwContract.Coops.COLUMN_CHAIR_NAME));
            String chairGender = data.getString(data.getColumnIndex(BfwContract.Coops.COLUMN_CHAIR_GENDER));
            String chairPhone = data.getString(data.getColumnIndex(BfwContract.Coops.COLUMN_CHAIR_CELL));

            String viceChairName = data.getString(data.getColumnIndex(BfwContract.Coops.COLUMN_VICECHAIR_NAME));
            String viceChairGender = data.getString(data.getColumnIndex(BfwContract.Coops.COLUMN_VICECHAIR_GENDER));
            String viceChairPhone = data.getString(data.getColumnIndex(BfwContract.Coops.COLUMN_VICECHAIR_CELL));

            String secName = data.getString(data.getColumnIndex(BfwContract.Coops.COLUMN_SECRETARY_NAME));
            String secGender = data.getString(data.getColumnIndex(BfwContract.Coops.COLUMN_SECRETARY_GENDER));
            String secPhone = data.getString(data.getColumnIndex(BfwContract.Coops.COLUMN_SECRETARY_CELL));

            String rcaRegistration = data.getString(data.getColumnIndex(BfwContract.Coops.COLUMN_RCA_REGISTRATION));

            String landInfo = data.getDouble(data.getColumnIndex(BfwContract.Coops.COLUMN_LAND_SIZE_CIP)) + "";
            String maleCoop = data.getInt(data.getColumnIndex(BfwContract.Coops.COLUMN_MALE_COOP)) + "";
            String femaleCoop = data.getInt(data.getColumnIndex(BfwContract.Coops.COLUMN_FEMALE_COOP)) + "";
            String totMembers = data.getInt(data.getColumnIndex(BfwContract.Coops.COLUMN_MEMBER)) + "";

            toolbar.setTitle(name);
            collapsingToolbarLayout.setTitle(name);

            TextView name_c_details = findViewById(R.id.name_c_details);
            name_c_details.setText(name);
            TextView phone_c_details = findViewById(R.id.phone_c_details);
            phone_c_details.setText(phone);
            TextView address_c_details = findViewById(R.id.address_c_details);

            if (address == null || address.equals("null")) {
                address_c_details.setText("");
            } else {
                address_c_details.setText(address);
            }


            TextView mail_c_details = findViewById(R.id.mail_c_details);
            mail_c_details.setText(email);

            TextView chair_name_c_details = findViewById(R.id.chair_name_c_details);
            if (chairName != null && !chairName.equals("null")) {
                chair_name_c_details.setText(chairName);
            } else {
                chair_name_c_details.setText("");
            }

            TextView chair_gender_c_details = findViewById(R.id.chair_gender_c_details);
            if (chairGender != null && !chairGender.equals("null")) {
                chair_gender_c_details.setText(chairGender);
            } else {
                chair_gender_c_details.setText("");
            }

            TextView chair_cel_c_details = findViewById(R.id.chair_cel_c_details);
            if (chairPhone != null && !chairPhone.equals("null")) {
                chair_cel_c_details.setText(chairPhone);
            } else {
                chair_cel_c_details.setText("");
            }


            TextView v_chair_name_c_details = findViewById(R.id.v_chair_name_c_details);
            if (viceChairName != null && !viceChairName.equals("null")) {
                v_chair_name_c_details.setText(viceChairName);
            } else {
                v_chair_name_c_details.setText("");
            }

            TextView v_chair_gender_c_details = findViewById(R.id.v_chair_gender_c_details);
            if (viceChairGender != null && !viceChairGender.equals("null")) {
                v_chair_gender_c_details.setText(viceChairGender);
            } else {
                v_chair_gender_c_details.setText("");
            }

            TextView v_chair_cel_c_details = findViewById(R.id.v_chair_cel_c_details);
            if (viceChairPhone != null && !viceChairPhone.equals("null")) {
                v_chair_cel_c_details.setText(viceChairPhone);
            } else {
                v_chair_cel_c_details.setText("");
            }


            TextView sec_name_c_details = findViewById(R.id.sec_name_c_details);
            if (secName != null && !secName.equals("null")) {
                sec_name_c_details.setText(secName);
            } else {
                sec_name_c_details.setText("");
            }

            TextView sec_gender_c_details = findViewById(R.id.sec_gender_c_details);
            if (secGender != null && !secGender.equals("null")) {
                sec_gender_c_details.setText(secGender);
            } else {
                sec_gender_c_details.setText("");
            }

            TextView sec_cel_c_details = findViewById(R.id.sec_cel_c_details);
            if (secPhone != null && !secPhone.equals("null")) {
                sec_cel_c_details.setText(secPhone);
            } else {
                sec_cel_c_details.setText("");
            }

            TextView year_rca_registration = findViewById(R.id.year_rca_registration);
            if (rcaRegistration != null && !rcaRegistration.equals("null")) {
                year_rca_registration.setText(rcaRegistration);
            } else {
                year_rca_registration.setText("");
            }


            TextView measured_total_land_size = findViewById(R.id.measured_total_land_size);
            measured_total_land_size.setText(landInfo);
            TextView male_in_coop = findViewById(R.id.male_in_coop);
            male_in_coop.setText(maleCoop);
            TextView female_in_coop = findViewById(R.id.female_in_coop);
            female_in_coop.setText(femaleCoop);
            TextView total_members = findViewById(R.id.total_members);
            total_members.setText(totMembers);

            ImageView gen_info_pic = findViewById(R.id.gen_info_pic);
            gen_info_pic.setImageResource(R.drawable.profile);

            ImageView internal_pic_c_details = findViewById(R.id.internal_pic_c_details);
            internal_pic_c_details.setImageResource(R.drawable.profile);


            Integer officeSpace = data.getInt(data.getColumnIndex(BfwContract.Coops.COLUMN_OFFICE_SPACE));
            Integer moistureMeter = data.getInt(data.getColumnIndex(BfwContract.Coops.COLUMN_MOISTURE_METER));
            Integer weightningScales = data.getInt(data.getColumnIndex(BfwContract.Coops.COLUMN_WEIGHTNING_SCALES));
            Integer qualityInputs = data.getInt(data.getColumnIndex(BfwContract.Coops.COLUMN_QUALITY_INPUT));
            Integer tractors = data.getInt(data.getColumnIndex(BfwContract.Coops.COLUMN_TRACTORS));
            Integer harvester = data.getInt(data.getColumnIndex(BfwContract.Coops.COLUMN_HARVESTER));
            Integer dryer = data.getInt(data.getColumnIndex(BfwContract.Coops.COLUMN_DRYER));
            Integer thresher = data.getInt(data.getColumnIndex(BfwContract.Coops.COLUMN_THRESHER));
            Integer safeStorageFacilities = data.getInt(data.getColumnIndex(BfwContract.Coops.COLUMN_SAFE_STORAGE));
            Integer other = data.getInt(data.getColumnIndex(BfwContract.Coops.COLUMN_OTHER));


            ImageView pic_office_space_details = findViewById(R.id.pic_office_space_details);
            pic_office_space_details.setImageResource((officeSpace == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);


            ImageView pic_moisture_meter_details = findViewById(R.id.pic_moisture_meter_details);
            pic_moisture_meter_details.setImageResource((moistureMeter == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            ImageView pic_weighting_scales_details = findViewById(R.id.pic_weighting_scales_details);
            pic_weighting_scales_details.setImageResource((weightningScales == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            ImageView pic_qlt_inputs_details = findViewById(R.id.pic_qlt_inputs_details);
            pic_qlt_inputs_details.setImageResource((qualityInputs == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            ImageView pic_tractors_details = findViewById(R.id.pic_tractors_details);
            pic_tractors_details.setImageResource((tractors == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            ImageView pic_harvs_details = findViewById(R.id.pic_harvs_details);
            pic_harvs_details.setImageResource((harvester == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            ImageView pic_dryer_details = findViewById(R.id.pic_dryer_details);
            pic_dryer_details.setImageResource((dryer == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            ImageView pic_thresher_details = findViewById(R.id.pic_thresher_details);
            pic_thresher_details.setImageResource((thresher == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            ImageView pic_safe_storage_facilities_details = findViewById(R.id.pic_safe_storage_facilities_details);
            pic_safe_storage_facilities_details.setImageResource((safeStorageFacilities == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            ImageView pic_other_details = findViewById(R.id.pic_other_details);
            pic_other_details.setImageResource((other == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);


            // create URI
            Uri infoUri = BfwContract.CoopInfo.buildCoopInfoUri(mCoopId);
            Uri forecastSalesUri = BfwContract.SalesCoop.buildSalesCoopInfoUri(mCoopId);
            Uri baselineYieldUri = BfwContract.YieldCoop.buildYieldCoopUri(mCoopId);
            Uri baselineSalesUri = BfwContract.BaselineSalesCoop.buildBaselineSalesCoppUri(mCoopId);
            Uri baselineFinanceInfoUri = BfwContract.FinanceInfoCoop.buildBaselineFinanceInfoCoopUri(mCoopId);
            Uri expectedYieldUri = BfwContract.ExpectedYieldCoop.buildExpectedYieldCoopUri(mCoopId);


            // Construct info list
            Cursor coopDataCursor = null, seasonCursor = null;

            String coopSelection = BfwContract.Coops.TABLE_NAME + "." +
                    BfwContract.Coops._ID + " =  ? ";
            String seasonSelection = BfwContract.HarvestSeason.TABLE_NAME
                    + "." + BfwContract.HarvestSeason._ID
                    + " = ?";
            String seasonName = "";

            ArrayList<AccessToInformation> accessToInformations = new ArrayList<>();
            ArrayList<ForecastSales> forecastSales = new ArrayList<>();
            ArrayList<BaselineYield> baselineYields = new ArrayList<>();
            ArrayList<BaselineSales> baselineSales = new ArrayList<>();
            ArrayList<BaselineFinanceInfo> baselineFinanceInfos = new ArrayList<>();
            ArrayList<ExpectedYield> expectedYields = new ArrayList<>();

            try {


                //Access to information
                coopDataCursor = getContentResolver().query(infoUri, null, coopSelection, new String[]{Long.toString(mCoopId)}, null);
                if (coopDataCursor != null) {
                    int isAgricultureExtension;
                    int isClimateRelatedInformation;
                    int isSeed;
                    int isOrganicFertilizers;
                    int isInorganicFertilizers;
                    int isLabour;
                    int isWaterPumps;
                    int isSpreaderOrSprayer;
                    int seasonId;

                    while (coopDataCursor.moveToNext()) {

                        isAgricultureExtension = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.CoopInfo.COLUMN_AGRI_EXTENSION));
                        isClimateRelatedInformation = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.CoopInfo.COLUMN_CLIMATE_INFO));
                        isSeed = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.CoopInfo.COLUMN_SEEDS));
                        isOrganicFertilizers = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.CoopInfo.COLUMN_ORGANIC_FERT));
                        isInorganicFertilizers = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.CoopInfo.COLUMN_INORGANIC_FERT));
                        isLabour = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.CoopInfo.COLUMN_LABOUR));
                        isWaterPumps = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.CoopInfo.COLUMN_WATER_PUMPS));
                        isSpreaderOrSprayer = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.CoopInfo.COLUMN_SPREADER));

                        seasonId = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.CoopInfo.COLUMN_SEASON_ID));

                        seasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, seasonSelection,
                                new String[]{Integer.toString(seasonId)}, null);

                        if (seasonCursor != null && seasonCursor.moveToFirst()) {
                            seasonName = seasonCursor.getString(seasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                        }
                        AccessToInformation accessToInformation = new AccessToInformation();
                        accessToInformation.setHarvsetSeason(seasonName);
                        accessToInformation.setAgricultureExtension(isAgricultureExtension == 1);
                        accessToInformation.setClimateRelatedInformation(isClimateRelatedInformation == 1);
                        accessToInformation.setSeed(isSeed == 1);
                        accessToInformation.setOrganicFertilizers(isOrganicFertilizers == 1);
                        accessToInformation.setInorganicFertilizers(isInorganicFertilizers == 1);
                        accessToInformation.setLabour(isLabour == 1);
                        accessToInformation.setWaterPumps(isWaterPumps == 1);
                        accessToInformation.setSpreaderOrSprayer(isSpreaderOrSprayer == 1);

                        accessToInformations.add(accessToInformation);

                    }
                }


                //forecast sales
                coopDataCursor = getContentResolver().query(forecastSalesUri, null, coopSelection, new String[]{Long.toString(mCoopId)}, null);
                if (coopDataCursor != null) {

                    String minFloorPerGrade;
                    String grade;

                    int commitedContractQty;

                    int rgcc;
                    int prodev;
                    int sarura;
                    int aif;
                    int eax;
                    int none;
                    int otherForecastInfo;
                    int seasonId;

                    while (coopDataCursor.moveToNext()) {

                        rgcc = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.SalesCoop.COLUMN_RGCC));
                        prodev = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.SalesCoop.COLUMN_PRODEV));
                        sarura = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.SalesCoop.COLUMN_SAKURA));
                        aif = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.SalesCoop.COLUMN_AIF));
                        eax = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.SalesCoop.COLUMN_EAX));
                        none = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.SalesCoop.COLUMN_NONE));
                        otherForecastInfo = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.SalesCoop.COLUMN_OTHER));

                        commitedContractQty = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.SalesCoop.COLUMN_CONTRACT_VOLUME));
                        minFloorPerGrade = coopDataCursor.getString(coopDataCursor.getColumnIndex(BfwContract.SalesCoop.COLUMN_FLOOR_GRADE));
                        grade = coopDataCursor.getString(coopDataCursor.getColumnIndex(BfwContract.SalesCoop.COLUMN_COOP_GRADE));

                        seasonId = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.CoopInfo.COLUMN_SEASON_ID));

                        seasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, seasonSelection,
                                new String[]{Integer.toString(seasonId)}, null);

                        if (seasonCursor != null && seasonCursor.moveToFirst()) {
                            seasonName = seasonCursor.getString(seasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                        }

                        ForecastSales forecastSalesInfo = new ForecastSales();
                        forecastSalesInfo.setSeasonName(seasonName);
                        forecastSalesInfo.setRgcc(rgcc == 1);
                        forecastSalesInfo.setRgcc(prodev == 1);
                        forecastSalesInfo.setSarura(sarura == 1);
                        forecastSalesInfo.setAif(aif == 1);
                        forecastSalesInfo.setEax(eax == 1);
                        forecastSalesInfo.setNone(none == 1);
                        forecastSalesInfo.setOther(otherForecastInfo == 1);
                        forecastSalesInfo.setCommitedContractQty(commitedContractQty);
                        forecastSalesInfo.setMinFloorPerGrade(minFloorPerGrade);
                        forecastSalesInfo.setGrade(grade);

                        forecastSales.add(forecastSalesInfo);
                    }

                }

                //baseline yield
                coopDataCursor = getContentResolver().query(baselineYieldUri, null, coopSelection, new String[]{Long.toString(mCoopId)}, null);
                if (coopDataCursor != null) {

                    int seasonId;
                    int isMaize;
                    int isBean;
                    int isSoy;
                    int isOther;

                    while (coopDataCursor.moveToNext()) {

                        isBean = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.YieldCoop.COLUMN_BEAN));
                        isMaize = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.YieldCoop.COLUMN_MAIZE));
                        isSoy = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.YieldCoop.COLUMN_SOY));
                        isOther = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.YieldCoop.COLUMN_OTHER));

                        seasonId = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.YieldCoop.COLUMN_SEASON_ID));

                        seasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, seasonSelection,
                                new String[]{Integer.toString(seasonId)}, null);

                        if (seasonCursor != null && seasonCursor.moveToFirst()) {
                            seasonName = seasonCursor.getString(seasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                        }

                        BaselineYield baselineYield = new BaselineYield();

                        baselineYield.setSeasonName(seasonName);
                        baselineYield.setMaize(isMaize == 1);
                        baselineYield.setBean(isBean == 1);
                        baselineYield.setSoy(isSoy == 1);
                        baselineYield.setOther(isOther == 1);

                        baselineYields.add(baselineYield);

                    }

                }

                //baseline sales
                coopDataCursor = getContentResolver().query(baselineSalesUri, null, coopSelection, new String[]{Long.toString(mCoopId)}, null);
                if (coopDataCursor != null) {

                    int seasonId;
                    String rgccContractUnderFtma;

                    int qtyAgregatedFromMember;
                    int cycleHarvsetAtPricePerKg;
                    int qtyPurchaseFromNonMember;
                    int nonMemberAtPricePerKg;

                    double qtyOfRgccContract;
                    double qtySoldToRgcc;
                    double pricePerKgSoldToRgcc;
                    double qtySoldOutOfRgcc;
                    double pricePerKkSoldOutFtma;

                    int isFormalBuyer;
                    int isInformalBuyer;
                    int isOther;

                    while (coopDataCursor.moveToNext()) {

                        seasonId = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_SEASON_ID));
                        rgccContractUnderFtma = coopDataCursor.getString(coopDataCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_BUYER_CONTRACT));

                        qtyAgregatedFromMember = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_CYCLE_HARVEST));
                        cycleHarvsetAtPricePerKg = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_CYCLE_HARVEST_PRICE));
                        qtyPurchaseFromNonMember = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_NON_MEMBER_PURCHASE));
                        nonMemberAtPricePerKg = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_NON_MEMBER_PURCHASE_COST));

                        qtyOfRgccContract = coopDataCursor.getDouble(coopDataCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_CONTRACT_VOLUME));
                        qtySoldToRgcc = coopDataCursor.getDouble(coopDataCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_QUANTITY_SOLD_RGCC));
                        pricePerKgSoldToRgcc = coopDataCursor.getDouble(coopDataCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_PRICE_SOLD_RGCC));
                        qtySoldOutOfRgcc = coopDataCursor.getDouble(coopDataCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_QUANTITY_SOLD_OUTSIDE_RGCC));
                        pricePerKkSoldOutFtma = coopDataCursor.getDouble(coopDataCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_PRICE_SOLD_OUTSIDE_RGCC));

                        isFormalBuyer = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_RGCC_BUYER_FORMAL));
                        isInformalBuyer = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_RGCC_INFORMAL));
                        isOther = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.BaselineSalesCoop.COLUMN_BUYER_OTHER));

                        seasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, seasonSelection,
                                new String[]{Integer.toString(seasonId)}, null);

                        if (seasonCursor != null && seasonCursor.moveToFirst()) {
                            seasonName = seasonCursor.getString(seasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                        }

                        BaselineSales baselineSalesInfo = new BaselineSales();
                        baselineSalesInfo.setRgccContractUnderFtma(rgccContractUnderFtma);
                        baselineSalesInfo.setQtyAgregatedFromMember(qtyAgregatedFromMember);
                        baselineSalesInfo.setCycleHarvsetAtPricePerKg(cycleHarvsetAtPricePerKg);
                        baselineSalesInfo.setQtyPurchaseFromNonMember(qtyPurchaseFromNonMember);
                        baselineSalesInfo.setNonMemberAtPricePerKg(nonMemberAtPricePerKg);
                        baselineSalesInfo.setPricePerKkSoldOutFtma(pricePerKkSoldOutFtma);
                        baselineSalesInfo.setQtyOfRgccContract(qtyOfRgccContract);
                        baselineSalesInfo.setQtySoldToRgcc(qtySoldToRgcc);
                        baselineSalesInfo.setPricePerKgSoldToRgcc(pricePerKgSoldToRgcc);
                        baselineSalesInfo.setQtySoldOutOfRgcc(qtySoldOutOfRgcc);

                        baselineSalesInfo.setFormalBuyer(isFormalBuyer == 1);
                        baselineSalesInfo.setInformalBuyer(isInformalBuyer == 1);
                        baselineSalesInfo.setOther(isOther == 1);
                        baselineSalesInfo.setSeasonName(seasonName);

                        baselineSales.add(baselineSalesInfo);
                    }

                }

                //expected yield
                coopDataCursor = getContentResolver().query(expectedYieldUri, null, coopSelection, new String[]{Long.toString(mCoopId)}, null);
                if (coopDataCursor != null) {

                    while (coopDataCursor.moveToNext()) {
                        int seasonId = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.ExpectedYieldCoop.COLUMN_SEASON_ID));
                        double expectedCoopProductionInMt = coopDataCursor.getDouble(coopDataCursor.getColumnIndex(BfwContract.ExpectedYieldCoop.COLUMN_COOP_PRODUCTION));
                        double totalCoopLandSize = coopDataCursor.getDouble(coopDataCursor.getColumnIndex(BfwContract.ExpectedYieldCoop.COLUMN_COOP_LAND_SIZE));
                        double expectedTotalProduction = coopDataCursor.getDouble(coopDataCursor.getColumnIndex(BfwContract.ExpectedYieldCoop.COLUMN_PRODUCTION_MT));

                        seasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, seasonSelection,
                                new String[]{Integer.toString(seasonId)}, null);

                        if (seasonCursor != null && seasonCursor.moveToFirst()) {
                            seasonName = seasonCursor.getString(seasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                        }

                        ExpectedYield expectedYield = new ExpectedYield();
                        expectedYield.setHarvestSeason(seasonName);
                        expectedYield.setExpectedCoopProductionInMt(expectedCoopProductionInMt);
                        expectedYield.setTotalCoopLandSize(totalCoopLandSize);
                        expectedYield.setExpectedTotalProduction(expectedTotalProduction);

                        expectedYields.add(expectedYield);
                    }
                }

                //baseline finance info
                coopDataCursor = getContentResolver().query(baselineFinanceInfoUri, null, coopSelection, new String[]{Long.toString(mCoopId)}, null);
                if (coopDataCursor != null) {

                    while (coopDataCursor.moveToNext()) {

                        String input_loan = coopDataCursor.getString(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_CYCLE_LOAN));

                        int isInput_loan_prov_bank = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PROVIDER_BANK));
                        int isInput_loan_prov_cooperative = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PROVIDER_COOPERATIVE));
                        int isInput_loan_prov_sacco = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PROVIDER_SACCO));
                        int isInput_loan_prov_other = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PROVIDER_OTHER));

                        double input_loan_amount = coopDataCursor.getDouble(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_CYCLE_LOAN_AMOUNT));
                        double input_loan_interest_rate = coopDataCursor.getDouble(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_CYCLE_INTEREST_RATE));
                        int input_loan_duration = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_CYCLE_LOAN_DURATION));

                        int sInput_loan_purpose_labour = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_LABOUR));
                        int sInput_loan_purpose_seed = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_SEEDS));
                        int sInput_loan_purpose_input = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_INPUT));
                        int sInput_loan_purpose_machinery = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_MACHINERY));
                        int sInput_loan_purpose_other = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_INPUT_LOAN_PURPOSE_OTHER));

                        int isCash_provided_purchase_inputs = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_DISBURSEMENT_METHOD));

                        String aggrgation_post_harvset_loan = coopDataCursor.getString(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN));

                        int isAgg_post_harv_loan_prov_bank = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PROVIDER_BANK));
                        int isAgg_post_harv_loan_prov_cooperative = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PROVIDER_COOPERATIVE));
                        int isAgg_post_harv_loan_prov_sacco = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PROVIDER_SACCO));
                        int isAgg_post_harv_loan_prov_other = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PROVIDER_OTHER));

                        double aggrgation_post_harvset_amount = coopDataCursor.getDouble(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_AMOUNT));
                        double aggrgation_post_harvset_loan_interest = coopDataCursor.getDouble(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_INTEREST_RATE));
                        int aggrgation_post_harvset_loan_duration = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_DURATION));

                        int isAgg_post_harv_loan_purpose_labour = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PURPOSE_LABOUR));
                        int isAgg_post_harv_loan_purpose_input = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PURPOSE_INPUT));
                        int isAgg_post_harv_loan_purpose_machinery = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PURPOSE_MACHINERY));
                        int isAgg_post_harv_loan_purpose_other = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_POST_HARVEST_LOAN_PURPOSE_OTHER));

                        String aggrgation_post_harvset_laon_disbursement_method = coopDataCursor.getString(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_CYCLE_LOAN_DISB_METHOD));
                        int seasonId = coopDataCursor.getInt(coopDataCursor.getColumnIndex(BfwContract.FinanceInfoCoop.COLUMN_SEASON_ID));

                        seasonCursor = getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, seasonSelection,
                                new String[]{Integer.toString(seasonId)}, null);

                        if (seasonCursor != null && seasonCursor.moveToFirst()) {
                            seasonName = seasonCursor.getString(seasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                        }

                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setSeasonName(seasonName);
                        baselineFinanceInfo.setInput_loan(input_loan);
                        baselineFinanceInfo.setInput_loan_prov_bank(isInput_loan_prov_bank == 1);
                        baselineFinanceInfo.setInput_loan_prov_cooperative(isInput_loan_prov_cooperative == 1);
                        baselineFinanceInfo.setInput_loan_prov_sacco(isInput_loan_prov_sacco == 1);
                        baselineFinanceInfo.setInput_loan_prov_other(isInput_loan_prov_other == 1);

                        baselineFinanceInfo.setInput_loan_amount(input_loan_amount);
                        baselineFinanceInfo.setInput_loan_interest_rate(input_loan_interest_rate);
                        baselineFinanceInfo.setInput_loan_duration(input_loan_duration);

                        baselineFinanceInfo.setsInput_loan_purpose_labour(sInput_loan_purpose_labour == 1);
                        baselineFinanceInfo.setsInput_loan_purpose_seed(sInput_loan_purpose_seed == 1);
                        baselineFinanceInfo.setsInput_loan_purpose_input(sInput_loan_purpose_input == 1);
                        baselineFinanceInfo.setsInput_loan_purpose_machinery(sInput_loan_purpose_machinery == 1);
                        baselineFinanceInfo.setAgg_post_harv_loan_purpose_other(sInput_loan_purpose_other == 1);

                        baselineFinanceInfo.setCash_provided_purchase_inputs(isCash_provided_purchase_inputs == 1);

                        baselineFinanceInfo.setAggrgation_post_harvset_loan(aggrgation_post_harvset_loan);
                        baselineFinanceInfo.setAgg_post_harv_loan_prov_bank(isAgg_post_harv_loan_prov_bank == 1);
                        baselineFinanceInfo.setAgg_post_harv_loan_prov_cooperative(isAgg_post_harv_loan_prov_cooperative == 1);
                        baselineFinanceInfo.setAgg_post_harv_loan_prov_sacco(isAgg_post_harv_loan_prov_sacco == 1);
                        baselineFinanceInfo.setAgg_post_harv_loan_prov_other(isAgg_post_harv_loan_prov_other == 1);

                        baselineFinanceInfo.setAggrgation_post_harvset_amount(aggrgation_post_harvset_amount);
                        baselineFinanceInfo.setAggrgation_post_harvset_loan_interest(aggrgation_post_harvset_loan_interest);
                        baselineFinanceInfo.setAggrgation_post_harvset_loan_duration(aggrgation_post_harvset_loan_duration);

                        baselineFinanceInfo.setAgg_post_harv_loan_purpose_labour(isAgg_post_harv_loan_purpose_labour == 1);
                        baselineFinanceInfo.setAgg_post_harv_loan_purpose_input(isAgg_post_harv_loan_purpose_input == 1);
                        baselineFinanceInfo.setAgg_post_harv_loan_purpose_machinery(isAgg_post_harv_loan_purpose_machinery == 1);
                        baselineFinanceInfo.setAgg_post_harv_loan_purpose_other(isAgg_post_harv_loan_purpose_other == 1);

                        baselineFinanceInfo.setAggrgation_post_harvset_laon_disbursement_method(aggrgation_post_harvset_laon_disbursement_method);

                        baselineFinanceInfos.add(baselineFinanceInfo);
                    }
                }

            } finally {
                if (coopDataCursor != null) {
                    coopDataCursor.close();
                }
                if (seasonCursor != null) {
                    seasonCursor.close();
                }
            }
            // Pass list to adapters
            AccessToInformationAdapter accessToInformationAdapter = new AccessToInformationAdapter(getApplicationContext(), accessToInformations);
            ForecastSalesAdapter forecastSalesAdapter = new ForecastSalesAdapter(getApplicationContext(), forecastSales);
            BaselineYieldAdapter baselineYieldAdapter = new BaselineYieldAdapter(getApplicationContext(), baselineYields);
            BaselineSalesAdapter baselineSalesAdapter = new BaselineSalesAdapter(getApplicationContext(), baselineSales);
            BaselineFinanceInfoAdapter baselineFinanceInfoAdapter = new BaselineFinanceInfoAdapter(getApplicationContext(), baselineFinanceInfos);
            ExpectedYieldAdapter expectedYieldAdapter = new ExpectedYieldAdapter(getApplicationContext(), expectedYields);

            accessToInfoListView.setAdapter(accessToInformationAdapter);
            forecastSalesListView.setAdapter(forecastSalesAdapter);
            baseLineYieldListView.setAdapter(baselineYieldAdapter);
            baseLineSalesListView.setAdapter(baselineSalesAdapter);
            baseLineFinanceInfoListView.setAdapter(baselineFinanceInfoAdapter);
            expectedYieldListView.setAdapter(expectedYieldAdapter);


            //1. ACCESS TO INFORMATION
            final AccordionView access_info_accordion = findViewById(R.id.access_info_accordion_c);
            access_info_accordion.setHeadingString("ACCESS TO INFORMATION");


            //2. FORECAST SALES
            final AccordionView forecast_sales = findViewById(R.id.forecast_s_accordion);
            forecast_sales.setHeadingString("FORECAST SALES");

            //3. BASELINE YIELDS
            final AccordionView baseline_y = findViewById(R.id.baseline_y_accordion);
            baseline_y.setHeadingString("BASELINE YIELD");

            //4. BASELINE SALES
            final AccordionView baseline_s = findViewById(R.id.baseline_s_accordion);
            baseline_s.setHeadingString("BASELINE SALES");


            //5. BASELINE FINANCE
            final AccordionView baseline_fin = findViewById(R.id.baseline_fin_accordion);
            baseline_fin.setHeadingString("BASELINE FINANCE INFO");

            //6 expected yield
            final AccordionView expected_yiel = findViewById(R.id.expected_y_accordion);
            expected_yiel.setHeadingString("EXPECTED YIELD");


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

            forecast_sales.setOnExpandCollapseListener(new AccordionExpansionCollapseListener() {
                @Override
                public void onExpanded(AccordionView view) {
                    forecast_sales.setHeadingBackGround(R.color.bg_detail);
                }

                @Override
                public void onCollapsed(AccordionView view) {
                    forecast_sales.setHeadingBackGround(R.color.default_color);
                }
            });

            baseline_y.setOnExpandCollapseListener(new AccordionExpansionCollapseListener() {
                @Override
                public void onExpanded(AccordionView view) {
                    baseline_y.setHeadingBackGround(R.color.bg_detail);
                }

                @Override
                public void onCollapsed(AccordionView view) {
                    baseline_y.setHeadingBackGround(R.color.default_color);
                }
            });

            baseline_s.setOnExpandCollapseListener(new AccordionExpansionCollapseListener() {
                @Override
                public void onExpanded(AccordionView view) {
                    baseline_s.setHeadingBackGround(R.color.bg_detail);
                }

                @Override
                public void onCollapsed(AccordionView view) {
                    baseline_s.setHeadingBackGround(R.color.default_color);
                }
            });
            baseline_fin.setOnExpandCollapseListener(new AccordionExpansionCollapseListener() {
                @Override
                public void onExpanded(AccordionView view) {
                    baseline_fin.setHeadingBackGround(R.color.bg_detail);
                }

                @Override
                public void onCollapsed(AccordionView view) {
                    baseline_fin.setHeadingBackGround(R.color.default_color);
                }
            });

            expected_yiel.setOnExpandCollapseListener(new AccordionExpansionCollapseListener() {
                @Override
                public void onExpanded(AccordionView view) {
                    expected_yiel.setHeadingBackGround(R.color.bg_detail);
                }

                @Override
                public void onCollapsed(AccordionView view) {
                    expected_yiel.setHeadingBackGround(R.color.default_color);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}



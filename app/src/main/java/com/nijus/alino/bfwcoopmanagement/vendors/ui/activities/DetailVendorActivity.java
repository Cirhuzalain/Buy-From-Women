package com.nijus.alino.bfwcoopmanagement.vendors.ui.activities;

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
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.PageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.GeneralVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui.PageFragmentCallbacksVendor;
import com.riyagayasen.easyaccordion.AccordionExpansionCollapseListener;
import com.riyagayasen.easyaccordion.AccordionView;


public class DetailVendorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    Long mFarmerId;
    private Uri mUri;
    private int coopId;
    private String name;
    private PageVendorVendor mPageVendor;
    private String mKey;
    private PageFragmentCallbacksVendor mCallbacks;
    public static final String ARG_KEY = "key";
    private GeneralVendor generalVendor;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView coop_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_farmer);

        getSupportLoaderManager().initLoader(0, null, this);

        //get extra from user profile activity
        //get farmer Id
        Intent intent = this.getIntent();
        if (intent.hasExtra("farmerId")) {
            mFarmerId = intent.getLongExtra("farmerId", 0);
            mUri = BfwContract.Farmer.buildFarmerUri(mFarmerId);
        }

        collapsingToolbarLayout = findViewById(R.id.name_farmer);
        //collapsingToolbarLayout.setBackgroundResource(R.mipmap.vendor);


        //collapsingToolbarLayout.setScrollBarSize(10);
        //collapsingToolbarLayout.setScrollBarStyle(R.style.route_title);
        toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab_coop);
        ImageView imageView = findViewById(R.id.coop_image);
        imageView.setImageResource(R.mipmap.vendor);
        fab.setImageResource(R.drawable.ic_edit_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), UpdateVendor.class);
                intent1.putExtra("farmerId", mFarmerId);
                startActivity(intent1);
                //Snackbar.make(view, mFarmerId+" Edit Coop Profile coming soon", Snackbar.LENGTH_LONG)
                //.setAction("Action", null).show();
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {

            name = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_NAME));
            String phone = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_PHONE));
            String gender = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_GENDER));
            int house_h_h = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_HOUSEHOLD_HEAD));
            int house_h_member = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_HOUSE_MEMBER));
            String first_name = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_FIRST_NAME));
            String last_name = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_LAST_NAME));
            String cell_phone = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_CELL_PHONE));
            String cell_carrer = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_CELL_CARRIER));
            String membership = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_MEMBER_SHIP));

            //available ressouces
            int tractor = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_TRACTORS));
            int harverster = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_HARVESTER));
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


            coopId = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_COOP_USER_ID));
            //Toast.makeText(this,"rep7 = "+name, Toast.LENGTH_LONG).show();

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

            //recup static data

            TextView address_f_details = findViewById(R.id.address_f_details);
            address_f_details.setText("NYABUGOGO KIGALI");

            //IGNORER LA COOPERATIVE DU VENDOR
            TextView coop_f_details = findViewById(R.id.coop_f_details);
            //coop_f_details.setText("COOPCUMA");
            coop_f_details.setVisibility(View.GONE);


            TextView tot_land_f_details = findViewById(R.id.tot_land_f_details);

            //demographic area
            ImageView demo_pic_f_details = findViewById(R.id.demo_pic_f_details);
            demo_pic_f_details.setImageResource(R.drawable.profile);

            TextView house_hold_f_details = findViewById(R.id.house_hold_f_details);
            //house_hold_f_details.setText(house_h_h);

            ImageView pic_house_hold = findViewById(R.id.pic_house_hold);
            pic_house_hold.setImageResource((house_h_h == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            TextView household_member_f_details = findViewById(R.id.household_member_f_details1);
            household_member_f_details.setText(house_h_member + "");

            TextView spouse_f_name_f_details = findViewById(R.id.spouse_f_name_f_details1);
            spouse_f_name_f_details.setText(first_name);

            TextView spouse_last_name_f_details = findViewById(R.id.spouse_last_name_f_details1);
            spouse_last_name_f_details.setText(last_name);

            TextView cell_phone_alt_f_details = findViewById(R.id.cell_phone_alt_f_details1);
            cell_phone_alt_f_details.setText(cell_phone);

            TextView cell_carrier_f_details = findViewById(R.id.cell_carrier_f_details1);
            cell_carrier_f_details.setText(cell_carrer);

            TextView membership_f_details = findViewById(R.id.membership_f_details1);
            membership_f_details.setText(membership);

            //Availabkeressoucres
            ImageView pic_tractor_details = findViewById(R.id.pic_tractor_details);
            pic_tractor_details.setImageResource((tractor == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

            ImageView pic_harv_details = findViewById(R.id.pic_harv_details);
            pic_harv_details.setImageResource((harverster == 1) ? R.mipmap.icon_sm_ok : R.mipmap.icon_sm_error);

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

            //ACCORDION DETAILS FOR FARMERS
            //1. ACCESS TO INFORMATION
            final AccordionView access_info_accordion = findViewById(R.id.access_info_accordion);
            access_info_accordion.setHeadingString("ACCESS TO INFORMATION");

            ImageView pic_agri_extension_details2 = findViewById(R.id.pic_agri_extension_details2);

            ImageView pic_clim_rel_details2 = findViewById(R.id.pic_clim_rel_details2);

            ImageView pic_seed_details2 = findViewById(R.id.pic_seed_details2);

            ImageView pic_org_fert_details2 = findViewById(R.id.pic_org_fert_details2);

            ImageView pic_inorg_fert_details2 = findViewById(R.id.pic_inorg_fert_details2);

            ImageView pic_labour_details2 = findViewById(R.id.pic_labour_details2);

            ImageView pic_irr_w_p_details2 = findViewById(R.id.pic_irr_w_p_details2);


            //2. ACCORDION BASELINE
            final AccordionView baseline_accordion = findViewById(R.id.baseline_accordion);
            baseline_accordion.setHeadingString("BASELINE FARMER");

            //3. ACCORDION FIMANCE
            final AccordionView finace_accordion = findViewById(R.id.finance_accordion);
            finace_accordion.setHeadingString("FINANCE DATA");


            //4. ACCORDION FARMER LAND
            final AccordionView farmer_land_accordion = findViewById(R.id.farmer_land_accordion);
            farmer_land_accordion.setHeadingString("VENDOR LAND DATA");

            //5. ACCORDION FPRECAST
            final AccordionView forecast_accordion = findViewById(R.id.forecast_accordion);
            forecast_accordion.setHeadingString("FORECAST DATA");

            //SET ANIMATION FOR ALL
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
                    //access_info_accordion.setExpanded(true);
                    //Toast.makeText(getApplicationContext(),"ouvert",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCollapsed(AccordionView view) {
                    //Toast.makeText(getApplicationContext(),"fermer",Toast.LENGTH_LONG).show();
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

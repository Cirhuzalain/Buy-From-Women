package com.nijus.alino.bfwcoopmanagement.vendors.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.ui.activities.BaseActivity;
import com.nijus.alino.bfwcoopmanagement.ui.activities.SettingsActivity;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;
import com.nijus.alino.bfwcoopmanagement.vendors.adapter.VendorRecyclerViewAdapter;
import com.nijus.alino.bfwcoopmanagement.vendors.sync.RefreshDataVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.fragment.VendorFragment;

import static com.nijus.alino.bfwcoopmanagement.data.BfwContract.Vendor.CONTENT_URI;

public class VendorActivity extends BaseActivity implements View.OnClickListener,
        VendorFragment.OnListFragmentInteractionListener, LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {
    private VendorRecyclerViewAdapter vendorRecyclerViewAdapter;
    private SwipeRefreshLayout mRefreshData;
    private CoordinatorLayout coordinatorLayout;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.activity_open_translate_from_bottom, R.anim.activity_no_animation);

        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_vendor);

        FloatingActionButton fab = findViewById(R.id.fab_edit_vendor);

        fab.setImageResource(R.drawable.ic_edit_black_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),UpdateVendor.class);
                intent1.putExtra("farmerId", 1);
                startActivity(intent1);
            }
        });


        ImageView gen_info_pic = findViewById(R.id.gen_info_pic);
        gen_info_pic.setImageResource(R.drawable.profile);

        TextView nom_f_details = findViewById(R.id.name_f_details);
        nom_f_details.setText("name");

        TextView phone_f_details = findViewById(R.id.phone_f_details);
        phone_f_details.setText("phone");

        TextView sex_f_details = findViewById(R.id.sex_f_details);
        sex_f_details.setText("gender");

        TextView address_f_details = findViewById(R.id.address_f_details);
        address_f_details.setText("NYABUGOGO KIGALI");

        TextView coop_f_details = findViewById(R.id.coop_f_details);
        //coop_f_details.setText("COOPCUMA");

        coop_f_details.setVisibility(View.GONE);

        TextView tot_land_f_details = findViewById(R.id.tot_land_f_details);
        tot_land_f_details.setText(tot_land_f_details.getText()+": 2 ha");

        //demographic area
        ImageView demo_pic_f_details = findViewById(R.id.demo_pic_f_details);
        demo_pic_f_details.setImageResource(R.drawable.profile);

        TextView house_hold_f_details  = findViewById(R.id.house_hold_f_details);
        //house_hold_f_details.setText(house_h_h);

        ImageView pic_house_hold = findViewById(R.id.pic_house_hold);
        pic_house_hold.setImageResource(R.mipmap.icon_sm_error);

        TextView household_member_f_details  = findViewById(R.id.household_member_f_details1);
        household_member_f_details.setText("5");

        TextView spouse_f_name_f_details  = findViewById(R.id.spouse_f_name_f_details1);
        spouse_f_name_f_details.setText("first name");

        TextView spouse_last_name_f_details  = findViewById(R.id.spouse_last_name_f_details1);
        spouse_last_name_f_details.setText("last name");

        TextView cell_phone_alt_f_details  = findViewById(R.id.cell_phone_alt_f_details1);
        cell_phone_alt_f_details.setText("cell_phone");

        TextView cell_carrier_f_details  = findViewById(R.id.cell_carrier_f_details1);
        cell_carrier_f_details.setText("cell_carrer");

        TextView membership_f_details  = findViewById(R.id.membership_f_details1);
        membership_f_details.setText("membership");

        //Availabkeressoucres
        ImageView pic_tractor_details = findViewById(R.id.pic_tractor_details);
        pic_tractor_details.setImageResource(R.mipmap.icon_sm_error);

        ImageView pic_harv_details = findViewById(R.id.pic_harv_details);
        pic_harv_details.setImageResource(R.mipmap.icon_sm_error);

        ImageView pic_dryer_details = findViewById(R.id.pic_dryer_details);
        pic_dryer_details.setImageResource(R.mipmap.icon_sm_error);

        ImageView pic_thresher_details = findViewById(R.id.pic_thresher_details);
        pic_thresher_details.setImageResource(R.mipmap.icon_sm_error);

        ImageView pic_safe_storage_details = findViewById(R.id.pic_safe_storage_details);
        pic_safe_storage_details.setImageResource(R.mipmap.icon_sm_error);

        ImageView pic_other_details = findViewById(R.id.pic_other_details);
        pic_other_details.setImageResource(R.mipmap.icon_sm_error);

        //Main water sources
        ImageView pic_dam_details = findViewById(R.id.pic_dam_details);
        pic_dam_details.setImageResource(R.mipmap.icon_sm_error);

        ImageView pic_well_details = findViewById(R.id.pic_well_details);
        pic_well_details.setImageResource(R.mipmap.icon_sm_error);

        ImageView pic_borehole_details = findViewById(R.id.pic_borehole_details);
        pic_borehole_details.setImageResource(R.mipmap.icon_sm_error);

        ImageView pic_river_Stream_details = findViewById(R.id.pic_river_Stream_details);
        pic_river_Stream_details.setImageResource(R.mipmap.icon_sm_error);

        ImageView pic_pipe_borne_details = findViewById(R.id.pic_pipe_borne_details);
        pic_pipe_borne_details.setImageResource(R.mipmap.icon_sm_error);

        ImageView pic_irrigation_details = findViewById(R.id.pic_irrigation_details);
        pic_irrigation_details.setImageResource(R.mipmap.icon_sm_error);

        ImageView pic_none_details = findViewById(R.id.pic_none_details);
        pic_none_details.setImageResource(R.mipmap.icon_sm_error);

        ImageView pic_other2_details = findViewById(R.id.pic_other2_details);
        pic_other2_details.setImageResource(R.mipmap.icon_sm_error);

        //ACCORDION DETAILS FOR FARMERS
        //1. ACCESS TO INFORMATION
        final AccordionView access_info_accordion = findViewById(R.id.access_info_accordion);
        access_info_accordion.setHeadingString("ACCESS TO INFORMATION");

        //ImageView pic_agri_extension_details2 = findViewById(R.id.pic_agri_extension_details2);
        //pic_agri_extension_details2.setImageResource(R.mipmap.icon_sm_error);

        ImageView pic_clim_rel_details2 = findViewById(R.id.pic_clim_rel_details2);
        pic_clim_rel_details2.setImageResource(R.mipmap.icon_sm_error);

        ImageView pic_seed_details2 = findViewById(R.id.pic_seed_details2);
        pic_seed_details2.setImageResource(R.mipmap.icon_sm_error);

        ImageView pic_org_fert_details2 = findViewById(R.id.pic_org_fert_details2);
        pic_org_fert_details2.setImageResource(R.mipmap.icon_sm_error);

        ImageView pic_inorg_fert_details2 = findViewById(R.id.pic_inorg_fert_details2);
        pic_inorg_fert_details2.setImageResource(R.mipmap.icon_sm_error);

        ImageView pic_labour_details2 = findViewById(R.id.pic_labour_details2);
        pic_labour_details2.setImageResource(R.mipmap.icon_sm_error);

        ImageView pic_irr_w_p_details2 = findViewById(R.id.pic_irr_w_p_details2);
        pic_irr_w_p_details2.setImageResource(R.mipmap.icon_sm_error);

        ImageView pic_spread_or_spray_details2 = findViewById(R.id.pic_spread_or_spray_details2);
        pic_spread_or_spray_details2.setImageResource(R.mipmap.icon_sm_error);
=======*/
        setContentView(R.layout.activity_main2);

        getSupportLoaderManager().initLoader(0, null, this);

        View emptyView = findViewById(R.id.recyclerview_empty_farmer);
        Context context = this;
        RecyclerView recyclerView = findViewById(R.id.farmers_list);
        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        vendorRecyclerViewAdapter = new VendorRecyclerViewAdapter(this, emptyView, new VendorRecyclerViewAdapter.VendorAdapterOnClickHandler() {
            @Override
            public void onClick(Long farmerId, VendorRecyclerViewAdapter.ViewHolder vh) {
            }
        }, new VendorRecyclerViewAdapter.VendorAdapterOnLongClickListener() {
            @Override
            public void onLongClick(long item, long position, VendorRecyclerViewAdapter.ViewHolder vh) {

            }
        });

        coordinatorLayout = findViewById(R.id.coordinator_layout);
        mRefreshData = findViewById(R.id.refresh_data_done);
        mRefreshData.setOnRefreshListener(this);

        recyclerView.setAdapter(vendorRecyclerViewAdapter);

        FloatingActionButton fab = findViewById(R.id.fab_edit_vendor);
        fab.setImageResource(R.drawable.ic_edit_black_24dp);
        fab.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab)
            startActivity(new Intent(this, CreateVendorActivity.class));
    }

    @Override
    public void onListFragmentInteraction(long item, VendorRecyclerViewAdapter.ViewHolder vh) {

        Intent intent = new Intent(this, DetailVendorActivity.class);
        intent.putExtra("vendorId", item);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = super.getDrawerLayout();

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        getSupportLoaderManager().restartLoader(0, null, this);

        if (Utils.isNetworkAvailable(this)) {
            this.startService(new Intent(this, RefreshDataVendor.class));
        } else {
            Toast.makeText(this, getResources().getString(R.string.connectivity_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, CONTENT_URI, null, null, null,
                null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        vendorRecyclerViewAdapter.swapCursor(data);
        mRefreshData.post(new Runnable() {
            @Override
            public void run() {
                mRefreshData.setRefreshing(false);
            }
        });
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        vendorRecyclerViewAdapter.swapCursor(null);
    }
}

package com.nijus.alino.bfwcoopmanagement.ui.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.view.ActionMode;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.adapter.CoopAdapter;
import com.nijus.alino.bfwcoopmanagement.coops.helper.FlipAnimator;
import com.nijus.alino.bfwcoopmanagement.coops.ui.activities.DetailCoopActivity;
import com.nijus.alino.bfwcoopmanagement.farmers.adapter.NavigationRecyclerViewAdapter;
import com.nijus.alino.bfwcoopmanagement.farmers.adapter.ViewPagerAdapter;
import com.nijus.alino.bfwcoopmanagement.buyers.ui.fragment.BuyerFragment;
import com.nijus.alino.bfwcoopmanagement.coopAgent.ui.fragment.CoopAgentFragment;
import com.nijus.alino.bfwcoopmanagement.coops.ui.fragment.CoopFragment;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.activities.DetailFarmerActivity;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.fragment.NavigationFragment;
import com.nijus.alino.bfwcoopmanagement.vendors.adapter.VendorRecyclerViewAdapter;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.activities.DetailVendorActivity;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.fragment.VendorFragment;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivityAdmin extends BaseActivity implements NavigationFragment.OnListFragmentInteractionListener, CoopFragment.OnCoopFragmentInteractionListener {

    private MenuInflater inflater;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    //Fragments
    BuyerFragment buyerFragment;
    VendorFragment vendorFragment;
    CoopFragment coopFragment;
    NavigationFragment farmer_fragment;
    CoopAgentFragment coopAgentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_prolile_activity);

        //Initializing viewPager
        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(5);
        setupViewPager(viewPager);

        //Initializing the tablayout
        tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position, false);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        buyerFragment = new BuyerFragment();
        vendorFragment = new VendorFragment();
        coopFragment = new CoopFragment();
        coopAgentFragment = new CoopAgentFragment();
        farmer_fragment = new NavigationFragment();

        adapter.addFragment(farmer_fragment, "Farmer");
        adapter.addFragment(coopFragment, "Coop");
        adapter.addFragment(coopAgentFragment, "Agent");
        adapter.addFragment(buyerFragment, "Buyer");
        adapter.addFragment(vendorFragment, "Vendor");

        viewPager.setAdapter(adapter);
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
    public void onListFragmentInteraction(long item, NavigationRecyclerViewAdapter.ViewHolder vh) {
        Intent intent = new Intent(this, DetailFarmerActivity.class);
        intent.putExtra("farmerId", item);
        startActivity(intent);
    }

    @Override
    public void onCoopFragmentInteraction(long item, CoopAdapter.ViewHolder vh) {
        Intent intent = new Intent(this, DetailCoopActivity.class);
        intent.putExtra("coopId", item);
        startActivity(intent);
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    public void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY).trim();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.navigation, menu);

        inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);//.xml file name
        menu.findItem(R.id.action_delete).setVisible(false);

        return super.onCreateOptionsMenu(menu);
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
}

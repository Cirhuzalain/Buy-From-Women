package com.nijus.alino.bfwcoopmanagement.ui.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.adapter.CoopAdapter;
import com.nijus.alino.bfwcoopmanagement.coops.ui.activities.DetailCoopActivity;
import com.nijus.alino.bfwcoopmanagement.events.DeleteFarmerEvent;
import com.nijus.alino.bfwcoopmanagement.events.DisableFarmerSwipeEvent;
import com.nijus.alino.bfwcoopmanagement.events.EventFarmerResetItems;
import com.nijus.alino.bfwcoopmanagement.events.ProcessingFarmerEvent;
import com.nijus.alino.bfwcoopmanagement.events.RefreshFarmerLoader;
import com.nijus.alino.bfwcoopmanagement.events.RequestEventFarmerToDelete;
import com.nijus.alino.bfwcoopmanagement.events.ResponseEventFarmerToDelete;
import com.nijus.alino.bfwcoopmanagement.events.ToggleFarmerRequestEvent;
import com.nijus.alino.bfwcoopmanagement.events.ToggleFarmerResponseEvent;
import com.nijus.alino.bfwcoopmanagement.farmers.adapter.NavigationRecyclerViewAdapter;
import com.nijus.alino.bfwcoopmanagement.farmers.adapter.ViewPagerAdapter;
import com.nijus.alino.bfwcoopmanagement.buyers.ui.fragment.BuyerFragment;
import com.nijus.alino.bfwcoopmanagement.coopAgent.ui.fragment.CoopAgentFragment;
import com.nijus.alino.bfwcoopmanagement.coops.ui.fragment.CoopFragment;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.activities.DetailFarmerActivity;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.fragment.DeleteFarmerDialog;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.fragment.NavigationFragment;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.fragment.VendorFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class UserProfileActivityAdmin extends BaseActivity implements NavigationFragment.OnListFragmentInteractionListener, CoopFragment.OnCoopFragmentInteractionListener,
        NavigationFragment.OnLongClickFragmentInteractionListener {

    private MenuInflater inflater;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    BuyerFragment buyerFragment;
    VendorFragment vendorFragment;
    CoopFragment coopFragment;
    NavigationFragment farmer_fragment;
    CoopAgentFragment coopAgentFragment;

    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_prolile_activity);

        //Initializing viewPager
        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(5);
        setupViewPager(viewPager);

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
        actionModeCallback = new ActionModeCallback();
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
    public void onLongClickFragmentInteractionListener(long item, long position, NavigationRecyclerViewAdapter.ViewHolder vh) {
        enableActionMode((int) position);
    }

    @Subscribe
    public void onToggleFarmerResponseEvent(ToggleFarmerResponseEvent farmerResponseEvent) {
        int count = farmerResponseEvent.getCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count) + " selected");
            actionMode.invalidate();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteFarmerEvent(DeleteFarmerEvent deleteFarmerEvent) {
        if (deleteFarmerEvent.isSuccess()) {
            Toast.makeText(this, deleteFarmerEvent.getMessage(), Toast.LENGTH_LONG).show();
            EventBus.getDefault().post(new RefreshFarmerLoader());
        } else {
            Toast.makeText(this, deleteFarmerEvent.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProcessingFarmerEvent(ProcessingFarmerEvent processingFarmerEvent) {
        Toast.makeText(this, processingFarmerEvent.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onResponseEventFarmerToDelete(ResponseEventFarmerToDelete eventFarmerToDelete) {
        if (Utils.isNetworkAvailable(getApplicationContext())) {
            ArrayList<Integer> farmerIds = eventFarmerToDelete.getFarmerIds();
            DeleteFarmerDialog farmerDeleteDialog = new DeleteFarmerDialog();
            Bundle bundle = new Bundle();
            bundle.putIntegerArrayList("farmer_ids", farmerIds);
            farmerDeleteDialog.setArguments(bundle);
            farmerDeleteDialog.show(getSupportFragmentManager(), "dialogLoanTag");

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.connectivity_error), Toast.LENGTH_LONG).show();
        }
    }

    private void toggleSelection(int position) {
        // dispatch event to toggle action
        EventBus.getDefault().post(new ToggleFarmerRequestEvent(position));
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
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

        inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);

            // dispatch event to disable  swipe refresh
            EventBus.getDefault().post(new DisableFarmerSwipeEvent());
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    // dispatch event to request data to delete
                    // Use Tab Position to call the correct event  tabLayout.getSelectedTabPosition()
                    EventBus.getDefault().post(new RequestEventFarmerToDelete());
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            // dispatch event to disable  swipe and clear adapter
            EventBus.getDefault().post(new EventFarmerResetItems());
        }
    }

}

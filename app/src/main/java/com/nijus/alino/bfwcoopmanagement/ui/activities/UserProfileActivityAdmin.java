package com.nijus.alino.bfwcoopmanagement.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.nijus.alino.bfwcoopmanagement.coops.ui.fragment.DeleteCoopDialog;
import com.nijus.alino.bfwcoopmanagement.events.DeleteCoopEvent;
import com.nijus.alino.bfwcoopmanagement.events.DeleteFarmerEvent;
import com.nijus.alino.bfwcoopmanagement.events.DisableCoopSwipeEvent;
import com.nijus.alino.bfwcoopmanagement.events.DisableFarmerSwipeEvent;
import com.nijus.alino.bfwcoopmanagement.events.EventAgentResetItems;
import com.nijus.alino.bfwcoopmanagement.events.EventBuyerResetItems;
import com.nijus.alino.bfwcoopmanagement.events.EventCoopResetItems;
import com.nijus.alino.bfwcoopmanagement.events.EventFarmerResetItems;
import com.nijus.alino.bfwcoopmanagement.events.ProcessingCoopEvent;
import com.nijus.alino.bfwcoopmanagement.events.ProcessingFarmerEvent;
import com.nijus.alino.bfwcoopmanagement.events.RefreshCoopLoader;
import com.nijus.alino.bfwcoopmanagement.events.RefreshFarmerLoader;
import com.nijus.alino.bfwcoopmanagement.events.RequestEventCoopToDelete;
import com.nijus.alino.bfwcoopmanagement.events.RequestEventFarmerToDelete;
import com.nijus.alino.bfwcoopmanagement.events.ResponseEventCoopToDelete;
import com.nijus.alino.bfwcoopmanagement.events.ResponseEventFarmerToDelete;
import com.nijus.alino.bfwcoopmanagement.events.ToggleCoopResponseEvent;
import com.nijus.alino.bfwcoopmanagement.events.ToggleFarmerRequestEvent;
import com.nijus.alino.bfwcoopmanagement.events.ToggleFarmerResponseEvent;
import com.nijus.alino.bfwcoopmanagement.events.ToggleRequestCoopEvent;
import com.nijus.alino.bfwcoopmanagement.farmers.adapter.NavigationRecyclerViewAdapter;
import com.nijus.alino.bfwcoopmanagement.farmers.adapter.ViewPagerAdapter;
import com.nijus.alino.bfwcoopmanagement.buyers.adapter.BuyerAdapter;
import com.nijus.alino.bfwcoopmanagement.buyers.ui.activities.DetailBuyerActivity;
import com.nijus.alino.bfwcoopmanagement.buyers.ui.fragment.BuyerFragment;
import com.nijus.alino.bfwcoopmanagement.buyers.ui.fragment.DeleteBuyerDialog;
import com.nijus.alino.bfwcoopmanagement.coopAgent.adapter.CoopAgentAdapter;
import com.nijus.alino.bfwcoopmanagement.coopAgent.ui.activities.DetailCoopAgentActivity;
import com.nijus.alino.bfwcoopmanagement.coopAgent.ui.fragment.CoopAgentFragment;
import com.nijus.alino.bfwcoopmanagement.coopAgent.ui.fragment.DeleteAgentDialog;
import com.nijus.alino.bfwcoopmanagement.coops.ui.fragment.CoopFragment;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.activities.DetailFarmerActivity;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.activities.NavigationActivity;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.fragment.DeleteFarmerDialog;
import com.nijus.alino.bfwcoopmanagement.events.DeleteAgentEvent;
import com.nijus.alino.bfwcoopmanagement.events.DeleteBuyerEvent;
import com.nijus.alino.bfwcoopmanagement.events.DisableAgentSwipeEvent;
import com.nijus.alino.bfwcoopmanagement.events.DisableBuyerSwipeEvent;
import com.nijus.alino.bfwcoopmanagement.events.RefreshAgentLoader;
import com.nijus.alino.bfwcoopmanagement.events.RefreshBuyerLoader;
import com.nijus.alino.bfwcoopmanagement.events.RequestEventAgentToDelete;
import com.nijus.alino.bfwcoopmanagement.events.RequestEventBuyerToDelete;
import com.nijus.alino.bfwcoopmanagement.events.ResponseEventAgentToDelete;
import com.nijus.alino.bfwcoopmanagement.events.ResponseEventBuyerToDelete;
import com.nijus.alino.bfwcoopmanagement.events.ToggleAgentRequestEvent;
import com.nijus.alino.bfwcoopmanagement.events.ToggleAgentResponseEvent;
import com.nijus.alino.bfwcoopmanagement.events.ToggleBuyerRequestEvent;
import com.nijus.alino.bfwcoopmanagement.events.ToggleBuyerResponseEvent;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.fragment.NavigationFragment;
import com.nijus.alino.bfwcoopmanagement.products.ui.activities.ProductActivity;
import com.nijus.alino.bfwcoopmanagement.sales.ui.activities.SaleOrderInfoActivity;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.fragment.VendorFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class UserProfileActivityAdmin extends BaseActivity implements NavigationFragment.OnListFragmentInteractionListener, CoopFragment.OnCoopFragmentInteractionListener,
        NavigationFragment.OnLongClickFragmentInteractionListener, BuyerFragment.OnListFragmentInteractionListener,
        BuyerFragment.OnLongClickFragmentInteractionListener, CoopAgentFragment.OnListFragmentInteractionListener,
        CoopAgentFragment.OnLongClickFragmentInteractionListener, CoopFragment.OnCoopFragmentLongClick {

    BuyerFragment buyerFragment;
    VendorFragment vendorFragment;
    CoopFragment coopFragment;
    NavigationFragment farmer_fragment;
    CoopAgentFragment coopAgentFragment;

    private ActionModeCallback actionModeCallback;
    private MenuInflater inflater;
    private TabLayout tabLayout;
    private ViewPager viewPager;
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


    @Override
    public void onCoopLongClick(long item, int position) {
        enableActionMode(position);
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

    @Subscribe
    public void onToggleCoopResponseEvent(ToggleCoopResponseEvent coopResponseEvent) {
        int count = coopResponseEvent.getCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count) + " selected");
            actionMode.invalidate();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Utils.checkAlreadyLogin(getApplicationContext())) {

            SharedPreferences prefs = getApplicationContext().getSharedPreferences(getResources().getString(R.string.application_key),
                    Context.MODE_PRIVATE);
            String groupName = prefs.getString(getResources().getString(R.string.g_name), "123");

            if (groupName.equals("Agent")) {
                Intent intent = new Intent(this, NavigationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else if (groupName.equals("Buyer")){
                Intent intent = new Intent(this, ProductActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else if (groupName.equals("Vendor")){
                Intent intent = new Intent(this, SaleOrderInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteCoopEvent(DeleteCoopEvent deleteCoopEvent) {
        if (deleteCoopEvent.isSuccess()) {
            Toast.makeText(this, deleteCoopEvent.getMessage(), Toast.LENGTH_LONG).show();
            EventBus.getDefault().post(new RefreshCoopLoader());
        } else {
            Toast.makeText(this, deleteCoopEvent.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProcessingCoopEvent(ProcessingCoopEvent processingCoopEvent) {
        Toast.makeText(this, processingCoopEvent.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onResponseEventFarmerToDelete(ResponseEventFarmerToDelete eventFarmerToDelete) {
        if (Utils.isNetworkAvailable(getApplicationContext())) {
            ArrayList<Integer> farmerIds = eventFarmerToDelete.getFarmerIds();
            DeleteFarmerDialog farmerDeleteDialog = new DeleteFarmerDialog();
            Bundle bundle = new Bundle();
            bundle.putIntegerArrayList("farmer_ids", farmerIds);
            farmerDeleteDialog.setArguments(bundle);
            farmerDeleteDialog.show(getSupportFragmentManager(), "dialogFarmerTag");

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.connectivity_error), Toast.LENGTH_LONG).show();
        }
    }

    @Subscribe
    public void onResponseEventCoopToDelete(ResponseEventCoopToDelete eventCoopToDelete) {
        if (Utils.isNetworkAvailable(getApplicationContext())) {
            ArrayList<Integer> coopIds = eventCoopToDelete.getFarmerIds();
            DeleteCoopDialog coopDeleteDialog = new DeleteCoopDialog();
            Bundle bundle = new Bundle();
            bundle.putIntegerArrayList("coop_ids", coopIds);
            coopDeleteDialog.setArguments(bundle);
            coopDeleteDialog.show(getSupportFragmentManager(), "dialogCoopTag");
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.connectivity_error), Toast.LENGTH_LONG).show();
        }
    }

    private void toggleSelection(int position) {
        // dispatch event to toggle action
        EventBus.getDefault().post(new ToggleFarmerRequestEvent(position));
    }

    private void toggleCoopSelection(int position) {
        EventBus.getDefault().post(new ToggleRequestCoopEvent(position));
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        if (tabLayout.getSelectedTabPosition() == 2) {
            //agent
            toggleSelectionAgent(position);
        } else if (tabLayout.getSelectedTabPosition() == 3) {
            //buyer
            toggleSelectionBuyer(position);
        } else if (tabLayout.getSelectedTabPosition() == 0) {
            //farmer
            toggleSelection(position);
        } else if (tabLayout.getSelectedTabPosition() == 1) {
            //coop
            toggleCoopSelection(position);
        }
    }

    @Override
    public void onCoopFragmentInteraction(long item, CoopAdapter.ViewHolder vh) {
        Intent intent = new Intent(this, DetailCoopActivity.class);
        intent.putExtra("coopId", item);
        startActivity(intent);
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

    @Override
    public void onListFragmentInteraction(long item, BuyerAdapter.ViewHolder vh) {
        Intent intent = new Intent(this, DetailBuyerActivity.class);
        intent.putExtra("buyerId", item);
        startActivity(intent);
    }

    @Override
    public void onLongClickFragmentInteractionListener(long item, long position, BuyerAdapter.ViewHolder vh) {
        enableActionMode((int) position);
    }


    @Override
    public void onListFragmentInteraction(long item, CoopAgentAdapter.ViewHolder vh) {
        Intent intent = new Intent(this, DetailCoopAgentActivity.class);
        intent.putExtra("coopAgentId", item);
        startActivity(intent);
    }

    @Override
    public void onLongClickFragmentInteractionListener(long item, long position, CoopAgentAdapter.ViewHolder vh) {
        enableActionMode((int) position);
    }


    @Subscribe
    public void onToggleBuyerResponseEvent(ToggleBuyerResponseEvent buyerResponseEvent) {
        int count = buyerResponseEvent.getCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count) + " selected");
            actionMode.invalidate();
        }
    }

    @Subscribe
    public void onDeleteBuyerEvent(DeleteBuyerEvent deleteBuyerEvent) {
        if (deleteBuyerEvent.isSuccess()) {
            Toast.makeText(this, deleteBuyerEvent.getMessage(), Toast.LENGTH_LONG).show();
            EventBus.getDefault().post(new RefreshBuyerLoader());
        } else {
            Toast.makeText(this, deleteBuyerEvent.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Subscribe
    public void onResponseEventBuyerToDelete(ResponseEventBuyerToDelete eventBuyerToDelete) {

        if (Utils.isNetworkAvailable(getApplicationContext())) {
            ArrayList<Integer> buyerIds = eventBuyerToDelete.getBuyerIds();

            DeleteBuyerDialog buyerDeleteDialog = new DeleteBuyerDialog();
            Bundle bundle = new Bundle();
            bundle.putIntegerArrayList("buyer_ids", buyerIds);
            buyerDeleteDialog.setArguments(bundle);
            buyerDeleteDialog.show(getSupportFragmentManager(), "dialogLoanTag");

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.connectivity_error), Toast.LENGTH_LONG).show();
        }
    }

    @Subscribe
    public void onToggleAgentResponseEvent(ToggleAgentResponseEvent agentResponseEvent) {
        int count = agentResponseEvent.getCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count) + " selected");
            actionMode.invalidate();
        }
    }

    @Subscribe
    public void onDeleteAgentEvent(DeleteAgentEvent deleteAgentEvent) {
        if (deleteAgentEvent.isSuccess()) {
            Toast.makeText(this, deleteAgentEvent.getMessage(), Toast.LENGTH_LONG).show();
            EventBus.getDefault().post(new RefreshAgentLoader());
        } else {
            Toast.makeText(this, deleteAgentEvent.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Subscribe
    public void onResponseEventAgentToDelete(ResponseEventAgentToDelete eventAgentToDelete) {

        if (Utils.isNetworkAvailable(getApplicationContext())) {
            ArrayList<Integer> agentIds = eventAgentToDelete.getAgentIds();

            DeleteAgentDialog agentDeleteDialog = new DeleteAgentDialog();
            Bundle bundle = new Bundle();
            bundle.putIntegerArrayList("agent_ids", agentIds);
            agentDeleteDialog.setArguments(bundle);
            agentDeleteDialog.show(getSupportFragmentManager(), "dialogLoanTag");

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.connectivity_error), Toast.LENGTH_LONG).show();
        }
    }


    private void toggleSelectionBuyer(int position) {
        // dispatch event to toggle action
        EventBus.getDefault().post(new ToggleBuyerRequestEvent(position));
    }

    private void toggleSelectionAgent(int position) {
        // dispatch event to toggle action
        EventBus.getDefault().post(new ToggleAgentRequestEvent(position));

    }

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
            // dispatch event to disable  swipe refresh
            if (tabLayout.getSelectedTabPosition() == 2) {
                //agent
                EventBus.getDefault().post(new DisableAgentSwipeEvent());
            } else if (tabLayout.getSelectedTabPosition() == 3) {
                //buyer
                EventBus.getDefault().post(new DisableBuyerSwipeEvent());
            } else if (tabLayout.getSelectedTabPosition() == 0) {
                //farmer
                EventBus.getDefault().post(new DisableFarmerSwipeEvent());
            } else if (tabLayout.getSelectedTabPosition() == 1) {
                //coop
                EventBus.getDefault().post(new DisableCoopSwipeEvent());
            }
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
                    if (tabLayout.getSelectedTabPosition() == 2) {
                        //agent
                        EventBus.getDefault().post(new RequestEventAgentToDelete());
                    } else if (tabLayout.getSelectedTabPosition() == 3) {
                        //buyer
                        EventBus.getDefault().post(new RequestEventBuyerToDelete());
                    } else if (tabLayout.getSelectedTabPosition() == 0) {
                        EventBus.getDefault().post(new RequestEventFarmerToDelete());
                    } else if (tabLayout.getSelectedTabPosition() == 1) {
                        EventBus.getDefault().post(new RequestEventCoopToDelete());
                    }
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
            if (tabLayout.getSelectedTabPosition() == 0) {
                EventBus.getDefault().post(new EventFarmerResetItems());
            } else if (tabLayout.getSelectedTabPosition() == 1) {
                EventBus.getDefault().post(new EventCoopResetItems());
            } else if (tabLayout.getSelectedTabPosition() == 2) {
                EventBus.getDefault().post(new EventAgentResetItems());
            } else if (tabLayout.getSelectedTabPosition() == 3) {
                EventBus.getDefault().post(new EventBuyerResetItems());
            }

        }
    }
}

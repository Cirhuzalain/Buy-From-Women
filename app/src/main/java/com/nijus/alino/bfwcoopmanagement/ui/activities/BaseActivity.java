package com.nijus.alino.bfwcoopmanagement.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.buyers.ui.activities.BuyerActivity;
import com.nijus.alino.bfwcoopmanagement.coopAgent.ui.activities.CoopAgentActivity;
import com.nijus.alino.bfwcoopmanagement.events.DeleteTokenEvent;
import com.nijus.alino.bfwcoopmanagement.farmers.sync.DeleteTokenService;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.activities.NavigationActivity;
import com.nijus.alino.bfwcoopmanagement.loans.ui.activities.LoanActivity;
import com.nijus.alino.bfwcoopmanagement.products.ui.activities.ProductActivity;
import com.nijus.alino.bfwcoopmanagement.purchases.ui.activities.PurchaseOrderActivity;
import com.nijus.alino.bfwcoopmanagement.sales.ui.activities.SaleOrderInfoActivity;
import com.nijus.alino.bfwcoopmanagement.ui.fragment.ErrorMessageDialog;
import com.nijus.alino.bfwcoopmanagement.ui.fragment.ProgressDialog;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.activities.VendorActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Base Activity for all activities using navigation drawer
 */
public class BaseActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private NavigationView mNavigationView;
    private ProgressDialog mProgressDialog;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private TextView mUsernameView;
    private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserProfileActivityAdmin u = new UserProfileActivityAdmin();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mDrawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.base_layout, null);

        FrameLayout activityContainer = mDrawerLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);

        super.setContentView(mDrawerLayout);

        mToolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbar);

        mNavigationView = findViewById(R.id.nav_view_base);


        setUpNavigationView();
    }

    public void setUpNavigationView() {
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(this);
            View header = LayoutInflater.from(this).inflate(R.layout.nav_header_navigation, null);
            mNavigationView.addHeaderView(header);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);

            mDrawerLayout.addDrawerListener(toggle);
            SharedPreferences prefs = getApplicationContext().getSharedPreferences(getResources().getString(R.string.application_key),
                    Context.MODE_PRIVATE);
            String uName = prefs.getString(getResources().getString(R.string.user_name), "123");
            mUsernameView = header.findViewById(R.id.username);
            mUsernameView.setText(uName);
            mImageView = header.findViewById(R.id.imageView);
            mImageView.setImageResource(R.drawable.ic_account_circle_black_24dp);

            prefs = getApplicationContext().getSharedPreferences(getResources().getString(R.string.application_key),
                    Context.MODE_PRIVATE);
            String groupName = prefs.getString(getResources().getString(R.string.g_name), "123");
            if (groupName.equals("Admin")) {
                // remove buyer, vendor, navigation, coop agent activity
                Menu menu = mNavigationView.getMenu();
                menu.removeItem(R.id.profile_buyer);
                menu.removeItem(R.id.profile_vendor);
                menu.removeItem(R.id.profile_farmer);
                menu.removeItem(R.id.profile_coop);
            } else if (groupName.equals("Agent")) {
                // remove userProfile, vendor, buyer
                Menu menu = mNavigationView.getMenu();
                menu.removeItem(R.id.user_profile);
                menu.removeItem(R.id.profile_vendor);
                menu.removeItem(R.id.profile_buyer);
            } else if (groupName.equals("Vendor")) {
                // remove buyer, navigation, coop agent, user profile activity
                Menu menu = mNavigationView.getMenu();
                menu.removeItem(R.id.profile_buyer);
                menu.removeItem(R.id.user_profile);
                menu.removeItem(R.id.profile_farmer);
                menu.removeItem(R.id.profile_coop);
            } else if (groupName.equals("Buyer")) {
                // remove user profile,navigation, coop agent, vendor, sale,purchase, loan
                Menu menu = mNavigationView.getMenu();
                menu.removeItem(R.id.user_profile);
                menu.removeItem(R.id.profile_vendor);
                menu.removeItem(R.id.profile_farmer);
                menu.removeItem(R.id.profile_coop);
                menu.removeItem(R.id.sales);
                menu.removeItem(R.id.purchases);
                menu.removeItem(R.id.loan);
            }

            toggle.syncState();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //item.removeItem(R.id.action_logout);

        if (id == R.id.user_profile) {
            startActivity(new Intent(this, UserProfileActivityAdmin.class));
        } else if (id == R.id.profile_buyer) {
            startActivity(new Intent(this, BuyerActivity.class));
        } else if (id == R.id.profile_farmer) {
            startActivity(new Intent(this, NavigationActivity.class));
        } else if (id == R.id.profile_coop) {
            startActivity(new Intent(this, CoopAgentActivity.class));
        } else if (id == R.id.profile_vendor) {
            startActivity(new Intent(this, VendorActivity.class));
        } else if (id == R.id.sales) {
            startActivity(new Intent(this, SaleOrderInfoActivity.class));
        } else if (id == R.id.purchases) {
            startActivity(new Intent(this, PurchaseOrderActivity.class));
        } else if (id == R.id.loan) {
            startActivity(new Intent(this, LoanActivity.class));
        } else if (id == R.id.product) {
            startActivity(new Intent(this, ProductActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
        } else if (id == R.id.logout_user) {
            logoutUser();
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logoutUser() {

        if (Utils.isNetworkAvailable(getApplicationContext())) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            mProgressDialog = new ProgressDialog();
            mProgressDialog.show(getSupportFragmentManager(), null);
            startService(new Intent(getApplicationContext(), DeleteTokenService.class));
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.connectivity_error), Toast.LENGTH_LONG).show();
        }
    }

    @Subscribe
    public void onTokenDeletion(DeleteTokenEvent eventToken) {
        mProgressDialog.dismiss();
        if (eventToken.isTokenDeleted()) {
            deleteSettingsInfo();
            startMainActivity();
        } else {
            ErrorMessageDialog errorMessageDialog = new ErrorMessageDialog();
            errorMessageDialog.show(getSupportFragmentManager(), "errorTag");
        }
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void deleteSettingsInfo() {
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(getResources().getString(R.string.application_key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(getResources().getString(R.string.app_id), false);
        editor.remove(getResources().getString(R.string.app_key));
        editor.remove(getResources().getString(R.string.app_refresh_token));
        editor.remove(getResources().getString(R.string.g_name));
        editor.remove(getResources().getString(R.string.user_name));
        editor.remove(getResources().getString(R.string.user_server_id));
        editor.apply();
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

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }
}
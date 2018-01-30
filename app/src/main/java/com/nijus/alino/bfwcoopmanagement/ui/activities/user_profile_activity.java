package com.nijus.alino.bfwcoopmanagement.ui.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.ui.fragment.SaleOrderDialogFragment;
import com.nijus.alino.bfwcoopmanagement.ui.fragment.SaleOrderFragment;
import com.nijus.alino.bfwcoopmanagement.ui.fragment.dummy.DummyCont;

public class SaleOrderInfoActivity extends BaseActivity implements SaleOrderFragment.OnListFragmentInteractionListener, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_order_fragment);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_sale_order);
        fab.setImageResource(R.drawable.ic_add_black_24dp);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_sale_order) {
            SaleOrderDialogFragment dialogFragment = new SaleOrderDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "saleDialog");
        }
    }

    @Override
    public void onListFragmentInteraction(DummyCont.DummyItem item) {

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
    public void onBackPressed() {

        DrawerLayout drawerLayout = super.getDrawerLayout();

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            NavUtils.navigateUpFromSameTask(this);
        }
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
}

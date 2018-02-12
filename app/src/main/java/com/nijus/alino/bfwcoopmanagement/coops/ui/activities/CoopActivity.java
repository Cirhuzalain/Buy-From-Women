package com.nijus.alino.bfwcoopmanagement.coops.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.adapter.CoopAdapter;
import com.nijus.alino.bfwcoopmanagement.farmers.adapter.NavigationRecyclerViewAdapter;
import com.nijus.alino.bfwcoopmanagement.ui.activities.SettingsActivity;
import com.nijus.alino.bfwcoopmanagement.coops.ui.fragment.CoopFragment;
import com.nijus.alino.bfwcoopmanagement.coops.ui.fragment.ViewCoopsSalePurchase;
import com.nijus.alino.bfwcoopmanagement.ui.activities.BaseActivity;

public class CoopActivity extends BaseActivity implements CoopFragment.OnListFragmentInteractionListener, CoopFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coop);
    }

  /*  @Override
    public void onFragmentInteraction(long id, CoopAdapter.ViewHolder vh) {
        ViewCoopsSalePurchase coopsSalePurchase = new ViewCoopsSalePurchase();
        coopsSalePurchase.show(getFragmentManager(), "viewCoopSalePurchase");
    }
*/
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
            NavUtils.navigateUpFromSameTask(this);
        }
    }


   @Override
    public void onFragmentInteraction(long itemId, CoopAdapter.ViewHolder vh) {
        Toast.makeText(this,"breee 1",Toast.LENGTH_LONG).show();
        //Intent intent = new Intent(this, DetailVendorActivity.class);
        //intent.putExtra("farmerId", itemId);
        //startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(long item, CoopAdapter.ViewHolder vh) {
        Toast.makeText(this,"breee 2",Toast.LENGTH_LONG).show();

    }
}

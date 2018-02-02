package com.nijus.alino.bfwcoopmanagement.ui.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.adapter.CoopAdapter;
import com.nijus.alino.bfwcoopmanagement.adapter.NavigationRecyclerViewAdapter;
import com.nijus.alino.bfwcoopmanagement.adapter.ViewPagerAdapter;
import com.nijus.alino.bfwcoopmanagement.ui.fragment.BuyerFragment;
import com.nijus.alino.bfwcoopmanagement.ui.fragment.CoopAgentFragment;
import com.nijus.alino.bfwcoopmanagement.ui.fragment.CoopFragment;
import com.nijus.alino.bfwcoopmanagement.ui.fragment.NavigationFragment;
import com.nijus.alino.bfwcoopmanagement.ui.fragment.SaleOrderDialogFragment;
import com.nijus.alino.bfwcoopmanagement.ui.fragment.SaleOrderFragment;
import com.nijus.alino.bfwcoopmanagement.ui.fragment.VendorFragment;
import com.nijus.alino.bfwcoopmanagement.ui.fragment.dummy.DummyCont;

public class user_profile_activity extends BaseActivity implements CoopFragment.OnFragmentInteractionListener, NavigationFragment.OnListFragmentInteractionListener {
    @Override
    public void onFragmentInteraction(long item, CoopAdapter.ViewHolder vh) {

    }

    @Override
    public void onListFragmentInteraction(long itemId, NavigationRecyclerViewAdapter.ViewHolder vh) {
        Intent intent = new Intent(this, DetailFarmerActivity.class);
        intent.putExtra("farmerId", itemId);
        startActivity(intent);
    }
    //This is our tablayout
    private TabLayout tabLayout;
    //This is our viewPager
    private ViewPager viewPager;

    //Fragments
    BuyerFragment buyerFragment;
    VendorFragment vendorFragment;
    CoopFragment coopFragment;
    NavigationFragment farmer_fragment;
    CoopAgentFragment coopAgentFragment;

    String[] tabTitle={"vendor","Coops","Buyer"};
    int[] unreadCount={5,6,8};

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
                viewPager.setCurrentItem(position,false);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //}
        //catch (Exception e)
        //{
           // Toast.makeText(this, "error "+e.getMessage(), Toast.LENGTH_LONG).show();
            //e.printStackTrace();
       // }
    }


    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        buyerFragment=new BuyerFragment();
        vendorFragment = new VendorFragment();
        coopFragment=new CoopFragment();
        coopAgentFragment = new CoopAgentFragment();
        farmer_fragment = new NavigationFragment();

        adapter.addFragment(farmer_fragment,"Farmer");
        adapter.addFragment(coopFragment,"Coop");
        adapter.addFragment(coopAgentFragment,"Agent");
        adapter.addFragment(buyerFragment,"Buyer");
        adapter.addFragment(vendorFragment,"Vendor");

        viewPager.setAdapter(adapter);
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

 /*   @Override
    public void onBackPressed() {

        DrawerLayout drawerLayout = super.getDrawerLayout();

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            NavUtils.navigateUpFromSameTask(this);
        }
    }*/

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
    private View prepareTabView(int pos) {
        View view = getLayoutInflater().inflate(R.layout.custom_tab_u_p,null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_count = (TextView) view.findViewById(R.id.tv_count);
        tv_title.setText(tabTitle[pos]);
        if(unreadCount[pos]>0)
        {
            tv_count.setVisibility(View.VISIBLE);
            tv_count.setText(""+unreadCount[pos]);
        }
        else
            tv_count.setVisibility(View.GONE);
        return view;
    }




    private void setupTabIcons()
    {

        for(int i=0;i<tabTitle.length;i++)
        {
            /*TabLayout.Tab tabitem = tabLayout.newTab();
            tabitem.setCustomView(prepareTabView(i));
            tabLayout.addTab(tabitem);*/

            tabLayout.getTabAt(i).setCustomView(prepareTabView(i));
        }


    }
}

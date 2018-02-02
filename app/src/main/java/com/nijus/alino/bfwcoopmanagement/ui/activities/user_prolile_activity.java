package com.nijus.alino.bfwcoopmanagement.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.adapter.ViewPagerAdapter;
import com.nijus.alino.bfwcoopmanagement.ui.fragment.BuyerFragment;
import com.nijus.alino.bfwcoopmanagement.ui.fragment.CoopAgentFragment;
import com.nijus.alino.bfwcoopmanagement.ui.fragment.CoopFragment;
import com.nijus.alino.bfwcoopmanagement.ui.fragment.NavigationFragment;
import com.nijus.alino.bfwcoopmanagement.ui.fragment.VendorFragment;

public class user_prolile_activity extends BaseActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_prolile_activity);
        //Initializing viewPager
         viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(5);
        //viewPager.set
        setupViewPager(viewPager);

        //Initializing the tablayout
         tabLayout = (TabLayout) findViewById(R.id.tablayout);
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
    }
    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        /*buyerFragment=new BuyerFragment();
        vendorFragment = new VendorFragment();
        coopFragment=new CoopFragment();
        coopAgentFragment = new CoopAgentFragment();
        farmer_fragment = new NavigationFragment();

        adapter.addFragment(farmer_fragment,"Farmer");
        adapter.addFragment(coopFragment,"Coop");
        adapter.addFragment(coopAgentFragment,"Agent");
        adapter.addFragment(buyerFragment,"Buyer");
        adapter.addFragment(vendorFragment,"Vendor");*/



        viewPager.setAdapter(adapter);
    }
}

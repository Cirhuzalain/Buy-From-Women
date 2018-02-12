package com.nijus.alino.bfwcoopmanagement.ui.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.view.ActionMode;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coopAgent.adapter.CoopAgentAdapter;
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
import com.nijus.alino.bfwcoopmanagement.ui.activities.BaseActivity;
import com.nijus.alino.bfwcoopmanagement.ui.activities.SettingsActivity;
import com.nijus.alino.bfwcoopmanagement.vendors.adapter.VendorRecyclerViewAdapter;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.activities.DetailVendorActivity;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.fragment.VendorFragment;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivityAdmin extends BaseActivity implements  VendorFragment.OnListFragmentInteractionListener, CoopFragment.OnFragmentInteractionListener, CoopFragment.OnListFragmentInteractionListener, NavigationFragment.OnListFragmentInteractionListener {

    // array used to perform multiple animation at once
    //private SparseBooleanArray selectedItems;
    //private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;

    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private SparseBooleanArray animationItemsIndex = new SparseBooleanArray();
    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;
    private MenuItem del_menu;
    private MenuInflater inflater;

    @Override
    public void onFragmentInteraction(long item, CoopAdapter.ViewHolder vh) {
        //Toast.makeText(this," position"+item ,Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, DetailCoopActivity.class);
        intent.putExtra("coopId", item);
        startActivity(intent);

    }
    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }

    public List<Integer> listsSelectedItem = new ArrayList<>();


    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }
    @Override
    public void onListFragmentInteraction(long item, CoopAdapter.ViewHolder vh) {

        //listsSelectedItem.add(Integer.valueOf(vh.getAdapterPosition()));
        if(!listsSelectedItem.contains(Integer.valueOf(vh.getAdapterPosition()))){
            //Toast.makeText(this," "+listsSelectedItem.size(), Toast.LENGTH_LONG).show();
        //if(currentSelectedIndex != vh.getAdapterPosition()){
            //actionMode = startSupportActionMode(actionModeCallback);
        //if (selectedItems.get(vh.getAdapterPosition(), false)) {
            //Toast.makeText(this,vh.mUname.getText().toString(),Toast.LENGTH_LONG).show();
            vh.iconFront.setVisibility(View.GONE);
            vh.view_foreground.setBackgroundColor(Color.argb(20,0,0,0));
            resetIconYAxis(vh.iconBack);
            vh.iconBack.setVisibility(View.VISIBLE);
            vh.iconBack.setAlpha(1);
            //if (currentSelectedIndex == vh.getAdapterPosition()) {
            FlipAnimator.flipView(getApplicationContext(), vh.iconBack, vh.iconFront, true);
            //resetCurrentIndex();
            currentSelectedIndex = vh.getAdapterPosition();
            listsSelectedItem.add(Integer.valueOf(vh.getAdapterPosition()));

        }
        else {
            vh.iconBack.setVisibility(View.GONE);
            resetIconYAxis(vh.iconFront);
            vh.view_foreground.setBackgroundColor(Color.argb(2,0,0,0));
            vh.iconFront.setVisibility(View.VISIBLE);
            vh.iconFront.setAlpha(1);
            //if ((reverseAllAnimations && animationItemsIndex.get(vh.getAdapterPosition(), false)) ||
              //      currentSelectedIndex == vh.getAdapterPosition()) {
                FlipAnimator.flipView(this, vh.iconBack, vh.iconFront, false);
                //resetCurrentIndex();
            //currentSelectedIndex = vh.getAdapterPosition();
            //}

            listsSelectedItem.remove(Integer.valueOf(vh.getAdapterPosition()));
            //Toast.makeText(this," REMOVE "+Integer.valueOf(vh.getAdapterPosition()), Toast.LENGTH_LONG).show();

        }


    }
    @Override
    public void onListFragmentInteraction(long item, VendorRecyclerViewAdapter.ViewHolder vh) {
        Intent intent = new Intent(this, DetailVendorActivity.class);
        intent.putExtra("farmerId", item);
        startActivity(intent);
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
       /* selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();*/
        //actionModeCallback = new ActionModeCallback();

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

       /* selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
*/
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




    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
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
                    // delete all the selected messages
                    //deleteMessages();
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            //mAdapter.clearSelections();
            //swipeRefreshLayout.setEnabled(true);
            //actionMode = null;
            //recyclerView.post(new Runnable() {
                //@Override
                //public void run() {
                    //mAdapter.resetAnimationIndex();
                    // mAdapter.notifyDataSetChanged();
                //}
            //});
        }
    }


}

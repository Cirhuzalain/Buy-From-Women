package com.nijus.alino.bfwcoopmanagement.farmers.ui.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.events.SyncDataEvent;
import com.nijus.alino.bfwcoopmanagement.farmers.adapter.NavigationRecyclerViewAdapter;
import com.nijus.alino.bfwcoopmanagement.farmers.adapter.RecyclerItemTouchHelper;
import com.nijus.alino.bfwcoopmanagement.farmers.sync.RefreshData;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.fragment.NavigationFragment;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.fragment.NavigationFragment.OnListFragmentInteractionListener;
import com.nijus.alino.bfwcoopmanagement.ui.activities.BaseActivity;
import com.nijus.alino.bfwcoopmanagement.ui.activities.SettingsActivity;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.nijus.alino.bfwcoopmanagement.data.BfwContract.Farmer.CONTENT_URI;

public class NavigationActivity extends BaseActivity implements
        View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, LoaderCallbacks<Cursor>,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener  {

    private OnListFragmentInteractionListener mListener;
    private NavigationRecyclerViewAdapter navigationRecyclerViewAdapter;
    private SwipeRefreshLayout mRefreshData;
    private CoordinatorLayout coordinatorLayout;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.activity_open_translate_from_bottom, R.anim.activity_no_animation);

        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main2);

        //getLoaderManager().initLoader(0, null, "");
        //getSupportLoaderManager(0, null, this);
        getSupportLoaderManager().initLoader(0,null,this);


        View emptyView = findViewById(R.id.recyclerview_empty_farmer);
        Context context = this;
        RecyclerView recyclerView = findViewById(R.id.farmers_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);

        //recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

      /*  navigationRecyclerViewAdapter = new NavigationRecyclerViewAdapter(this, emptyView,
                new NavigationRecyclerViewAdapter.FarmerAdapterOnClickHandler() {
            @Override
            public void onClick(Long farmerId, NavigationRecyclerViewAdapter.ViewHolder vh) {
                //((CoopAgentFragment.OnFragmentInteractionListener) getActivity()).onFragmentInteraction(farmerId, vh);
            }

        }, new NavigationRecyclerViewAdapter.FarmerAdapterOnClickHandler() {
            @Override
            public void onClick(Long farmerId, NavigationRecyclerViewAdapter.ViewHolder vh) {

            }
            @Override
            public boolean onLongClick(Long farmerId, NavigationRecyclerViewAdapter.ViewHolder vh) {
                //return ((CoopAgentFragment.OnListFragmentInteractionListener) getActivity()).onLong2FragmentInteraction(farmerId, vh);
                return true;
            }
        });*/
        navigationRecyclerViewAdapter = new NavigationRecyclerViewAdapter(this, emptyView, new NavigationRecyclerViewAdapter.FarmerAdapterOnClickHandler() {
            @Override
            public void onClick(Long farmerId, NavigationRecyclerViewAdapter.ViewHolder vh) {
                //(OnListFragmentInteractionListener .onListFragmentInteraction(farmerId, vh);
            }
        });

        coordinatorLayout = findViewById(R.id.coordinator_layout);

        mRefreshData = findViewById(R.id.refresh_data_done);
        mRefreshData.setOnRefreshListener(this);

        recyclerView.setAdapter(navigationRecyclerViewAdapter);

   /*     //add on 1 febrary 2018 listener to recycleview when touching it
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT *//*| ItemTouchHelper.RIGHT | ItemTouchHelper.UP*//*) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);
*/
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add_black_24dp);
        fab.setOnClickListener(this);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab)
            startActivity(new Intent(this, CreateFarmerActivity.class));
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
        getSupportLoaderManager().restartLoader(0, null,this);

        if (Utils.isNetworkAvailable(this)) {
            this.startService(new Intent(this, RefreshData.class));
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
        navigationRecyclerViewAdapter.swapCursor(data);
        mRefreshData.post(new Runnable() {
            @Override
            public void run() {
                mRefreshData.setRefreshing(false);
            }
        });
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        navigationRecyclerViewAdapter.swapCursor(null);
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

    }
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(long item, NavigationRecyclerViewAdapter.ViewHolder vh);
    }
}

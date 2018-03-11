package com.nijus.alino.bfwcoopmanagement.farmers.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.events.DeleteFarmerEvent;
import com.nijus.alino.bfwcoopmanagement.events.ProcessingCoopEvent;
import com.nijus.alino.bfwcoopmanagement.events.ProcessingFarmerEvent;
import com.nijus.alino.bfwcoopmanagement.events.RefreshFarmerLoader;
import com.nijus.alino.bfwcoopmanagement.farmers.adapter.NavigationRecyclerViewAdapter;
import com.nijus.alino.bfwcoopmanagement.farmers.adapter.RecyclerItemTouchHelper;
import com.nijus.alino.bfwcoopmanagement.farmers.sync.RefreshData;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.fragment.DeleteFarmerDialog;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.fragment.NavigationFragment;
import com.nijus.alino.bfwcoopmanagement.ui.activities.BaseActivity;
import com.nijus.alino.bfwcoopmanagement.ui.activities.SettingsActivity;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import static com.nijus.alino.bfwcoopmanagement.data.BfwContract.Farmer.CONTENT_URI;

public class NavigationActivity extends BaseActivity implements
        View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, LoaderCallbacks<Cursor>,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, NavigationFragment.OnListFragmentInteractionListener,
        NavigationFragment.OnLongClickFragmentInteractionListener {

    private NavigationRecyclerViewAdapter navigationRecyclerViewAdapter;
    private SwipeRefreshLayout mRefreshData;
    private CoordinatorLayout coordinatorLayout;
    private LinearLayoutManager mLayoutManager;
    private ActionMode actionMode;
    private ActionModeCallback actionModeCallback;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.activity_open_translate_from_bottom, R.anim.activity_no_animation);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        getSupportLoaderManager().initLoader(0, null, this);


        View emptyView = findViewById(R.id.recyclerview_empty_farmer);
        Context context = this;
        recyclerView = findViewById(R.id.farmers_list);
        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        navigationRecyclerViewAdapter = new NavigationRecyclerViewAdapter(this, emptyView, new NavigationRecyclerViewAdapter.FarmerAdapterOnClickHandler() {
            @Override
            public void onClick(Long farmerId, NavigationRecyclerViewAdapter.ViewHolder vh) {
                onListFragmentInteraction(farmerId, vh);
            }
        }, new NavigationRecyclerViewAdapter.FarmerAdapterOnLongClickListener() {
            @Override
            public void onLongClick(long item, long position, NavigationRecyclerViewAdapter.ViewHolder vh) {
                onLongClickFragmentInteractionListener(item, position, vh);
            }
        });

        coordinatorLayout = findViewById(R.id.coordinator_layout);

        mRefreshData = findViewById(R.id.refresh_data_done);
        mRefreshData.setOnRefreshListener(this);

        recyclerView.setAdapter(navigationRecyclerViewAdapter);

        fab = findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add_black_24dp);
        fab.setOnClickListener(this);
        actionModeCallback = new ActionModeCallback();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProcessingFarmerEvent(ProcessingFarmerEvent processingFarmerEvent) {
        Toast.makeText(this, processingFarmerEvent.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onLongClickFragmentInteractionListener(long item, long position, NavigationRecyclerViewAdapter.ViewHolder vh) {
        enableActionMode((int) position);
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        navigationRecyclerViewAdapter.toggleSelection(position);
        int count = navigationRecyclerViewAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count) + " selected");
            actionMode.invalidate();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab)
            startActivity(new Intent(this, CreateFarmerActivity.class));
    }

    @Override
    public void onListFragmentInteraction(long item, NavigationRecyclerViewAdapter.ViewHolder vh) {

        Intent intent = new Intent(this, DetailFarmerActivity.class);
        intent.putExtra("farmerId", item);
        startActivity(intent);
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
        getSupportLoaderManager().restartLoader(0, null, this);
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

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
            // dispatch event to disable  swipe refresh
            mRefreshData.setEnabled(false);
            fab.setVisibility(View.INVISIBLE);
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
                    if (Utils.isNetworkAvailable(getApplicationContext())) {
                        ArrayList<Integer> farmerIds = navigationRecyclerViewAdapter.getSelectedItems();
                        DeleteFarmerDialog farmerDeleteDialog = new DeleteFarmerDialog();
                        Bundle bundle = new Bundle();
                        bundle.putIntegerArrayList("farmer_ids", farmerIds);
                        farmerDeleteDialog.setArguments(bundle);
                        farmerDeleteDialog.show(getSupportFragmentManager(), "dialogFarmerTag");

                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.connectivity_error), Toast.LENGTH_LONG).show();
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
            navigationRecyclerViewAdapter.clearSelections();
            mRefreshData.setEnabled(true);
            fab.setVisibility(View.VISIBLE);

            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    navigationRecyclerViewAdapter.resetAnimationIndex();
                }
            });

        }
    }
}

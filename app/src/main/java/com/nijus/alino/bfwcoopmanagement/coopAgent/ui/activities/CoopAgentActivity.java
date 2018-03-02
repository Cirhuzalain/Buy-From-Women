package com.nijus.alino.bfwcoopmanagement.coopAgent.ui.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coopAgent.adapter.CoopAgentAdapter;
import com.nijus.alino.bfwcoopmanagement.coopAgent.ui.fragment.CoopAgentFragment;
import com.nijus.alino.bfwcoopmanagement.farmers.sync.RefreshData;
import com.nijus.alino.bfwcoopmanagement.ui.activities.BaseActivity;
import com.nijus.alino.bfwcoopmanagement.ui.activities.SettingsActivity;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import static com.nijus.alino.bfwcoopmanagement.data.BfwContract.Buyer.CONTENT_URI;

public class CoopAgentActivity extends BaseActivity implements CoopAgentFragment.OnListFragmentInteractionListener,
        LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private CoopAgentAdapter agentRecyclerViewAdapter;
    private SwipeRefreshLayout mRefreshData;
    private CoordinatorLayout coordinatorLayout;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.activity_open_translate_from_bottom, R.anim.activity_no_animation);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coop_agent);

        getSupportLoaderManager().initLoader(0, null, this);

        View emptyView = findViewById(R.id.recyclerview_empty_coop_Agent);
        Context context = this;
        RecyclerView recyclerView = findViewById(R.id.coopsagent_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new LinearLayoutManager(context);


        agentRecyclerViewAdapter = new CoopAgentAdapter(this, emptyView,
                new CoopAgentAdapter.CoopAgentAdapterOnClickHandler() {
                    @Override
                    public void onClick(Long farmerId, CoopAgentAdapter.ViewHolder vh) {

                    }
                }, new CoopAgentAdapter.CoopAgentAdapterOnLongClickHandler() {
            @Override
            public void onLongClick(long agentId, long position, CoopAgentAdapter.ViewHolder vh) {

            }
        }, mLayoutManager);

        coordinatorLayout = findViewById(R.id.coordinator_layout);

        mRefreshData = findViewById(R.id.refresh_data_done);
        mRefreshData.setOnRefreshListener(this);

        recyclerView.setAdapter(agentRecyclerViewAdapter);

        FloatingActionButton fab = findViewById(R.id.fab_edit_coop_agent);

        fab.setImageResource(R.drawable.ic_edit_black_24dp);
        fab.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab)
            startActivity(new Intent(this, CreateCoopAgentActivity.class));
    }

    @Override
    public void onListFragmentInteraction(long item, CoopAgentAdapter.ViewHolder vh) {
        Intent intent1 = new Intent(this, DetailCoopAgentActivity.class);
        intent1.putExtra("coooAgentId", item);
        startActivity(intent1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, CONTENT_URI, null, null, null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        agentRecyclerViewAdapter.swapCursor(data);
        mRefreshData.post(new Runnable() {
            @Override
            public void run() {
                mRefreshData.setRefreshing(false);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        agentRecyclerViewAdapter.swapCursor(null);

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


}

package com.nijus.alino.bfwcoopmanagement.loans.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
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
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.events.SyncDataEvent;
import com.nijus.alino.bfwcoopmanagement.loans.adapter.LoanAdapter;
import com.nijus.alino.bfwcoopmanagement.loans.ui.fragment.DeleteLoanDialogFragment;
import com.nijus.alino.bfwcoopmanagement.ui.activities.BaseActivity;
import com.nijus.alino.bfwcoopmanagement.ui.activities.SettingsActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class LoanActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private LoanAdapter loanRecyclerViewAdapter;
    private SwipeRefreshLayout mRefreshData;
    private MenuItem item_delete;
    private FloatingActionButton fab;
    private boolean state;
    private RecyclerView recyclerView;
    public List<Long> listsSelectedItem = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);
        state = false;
        getSupportLoaderManager().initLoader(0, null, this);

        View emptyView = findViewById(R.id.recyclerview_empty_loan);
        Context context = this;
        recyclerView = findViewById(R.id.loan_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        loanRecyclerViewAdapter = new LoanAdapter(this, emptyView, new LoanAdapter.LoanAdapterOnClickHandler() {
            @Override
            public void onClick(Long farmerId, LoanAdapter.ViewHolder vh) {
            }

        }, new LoanAdapter.LoanAdapterOnLongClickHandler() {
            @Override
            public boolean onLongClick(Long farmerId, LoanAdapter.ViewHolder vh) {
                return true;
            }
        });

        mRefreshData = findViewById(R.id.refresh_data_done);
        mRefreshData.setOnRefreshListener(this);

        recyclerView.setAdapter(loanRecyclerViewAdapter);

        //fab loan fragment
        fab = findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add_black_24dp);
        fab.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (!state) {
            DrawerLayout drawerLayout = super.getDrawerLayout();

            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                NavUtils.navigateUpFromSameTask(this);
            }
        } else {
            startActivity(new Intent(getApplicationContext(), LoanActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        item_delete = menu.findItem(R.id.action_delete);
        return true;
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
        if (id == R.id.action_delete) {
            Bundle bundle = new Bundle();
            long[] tab = new long[listsSelectedItem.size()];
            int i = 0;
            for (long l : listsSelectedItem) {
                tab[i] = l;
                i++;
            }

            bundle.putLongArray("list_items_to_delete", tab);

            DeleteLoanDialogFragment dialogFragment = new DeleteLoanDialogFragment();
            dialogFragment.setArguments(bundle);
            dialogFragment.show(getSupportFragmentManager(), "dialogLoanTag");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, BfwContract.Loan.CONTENT_URI, null, null, null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        loanRecyclerViewAdapter.swapCursor(data);
        mRefreshData.post(new Runnable() {
            @Override
            public void run() {
                mRefreshData.setRefreshing(false);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loanRecyclerViewAdapter.swapCursor(null);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.fab) {
            startActivity(new Intent(this, CreateLoanActivity.class));
        }
    }

    @Override
    public void onRefresh() {
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveDataEvent(SaveDataEvent saveDataEvent) {
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSyncDataEvent(SyncDataEvent syncDataEvent) {
        if (syncDataEvent.isSuccess()) {
            getSupportLoaderManager().restartLoader(0, null, this);
        } else {
            Toast.makeText(this, syncDataEvent.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectedItemsEvent(SelectedItems selectedItems) {
        if (selectedItems.getCountSelctedItem() > 0) {
            state = true;
            item_delete.setVisible(true);
            mRefreshData.setEnabled(false);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
            getSupportActionBar().setTitle(selectedItems.getListsSelectedItem().size() + " selected");
            fab.setVisibility(View.GONE);
            listsSelectedItem = selectedItems.getListsSelectedItem();
            getSupportActionBar().setDisplayShowHomeEnabled(false);

            getSupportActionBar().setHomeButtonEnabled(false);
        } else {
            onReset();
        }
    }

    public void onReset() {

        loanRecyclerViewAdapter.reset();
        item_delete.setVisible(false);
        mRefreshData.setEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        getSupportActionBar().setTitle("Loan");
        fab.setVisibility(View.VISIBLE);
        state = false;

    }

}

package com.nijus.alino.bfwcoopmanagement.loans.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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


public class LoanActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener, Button.OnClickListener {
    private LoanAdapter loanRecyclerViewAdapter;
    private SwipeRefreshLayout mRefreshData;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;

    private ActionModeCallback actionModeCallback;
    private ActionMode actionMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);
        getSupportLoaderManager().initLoader(0, null, this);

        View emptyView = findViewById(R.id.recyclerview_empty_loan);
        Context context = this;
        recyclerView = findViewById(R.id.loan_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        loanRecyclerViewAdapter = new LoanAdapter(this, emptyView, new LoanAdapter.LoanAdapterOnClickHandler() {
            @Override
            public void onItemClick(Long loanId, LoanAdapter.ViewHolder vh) {
                Intent intent = new Intent(getApplicationContext(), DetailLoanActivity.class);
                intent.putExtra("loanId", loanId);
                startActivity(intent);
            }

        }, new LoanAdapter.LoanAdapterOnLongClickHandler() {
            @Override
            public boolean onLongClick(int position, LoanAdapter.ViewHolder vh) {
                enableActionMode(position);
                return true;
            }
        });

        mRefreshData = findViewById(R.id.refresh_data_done);
        mRefreshData.setOnRefreshListener(this);

        recyclerView.setAdapter(loanRecyclerViewAdapter);

        fab = findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add_black_24dp);
        fab.setOnClickListener(this);

        actionModeCallback = new ActionModeCallback();
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
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            startActivity(new Intent(this, CreateLoanActivity.class));
        }
    }

    private void enableActionMode(int position) {
        if (actionMode == null) {
            actionMode = startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        // dispatch event to toggle action
        loanRecyclerViewAdapter.toggleSelection(position);
        int count = loanRecyclerViewAdapter.getSelectedItemCount();
        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count) + " selected");
            actionMode.invalidate();
        }
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

    private class ActionModeCallback implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);

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
                    // dispatch event to request data to delete
                    Bundle bundle = new Bundle();
                    long[] tab = new long[loanRecyclerViewAdapter.getSelectedItems().size()];
                    int i = 0;
                    for (Integer l : loanRecyclerViewAdapter.getSelectedItems()) {
                        tab[i] = (long) l;
                        i++;
                    }

                    bundle.putLongArray("list_items_to_delete", tab);

                    DeleteLoanDialogFragment dialogFragment = new DeleteLoanDialogFragment();
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getSupportFragmentManager(), "dialogLoanTag");
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            loanRecyclerViewAdapter.clearSelections();
            mRefreshData.setEnabled(true);
            fab.setVisibility(View.VISIBLE);

            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    loanRecyclerViewAdapter.resetAnimationIndex();
                }
            });
        }
    }

}

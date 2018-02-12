package com.nijus.alino.bfwcoopmanagement.loans.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.loans.adapter.LoanAdapter;
import com.nijus.alino.bfwcoopmanagement.loans.ui.fragment.ScheduleBottomSheetDialogFragment;
import com.nijus.alino.bfwcoopmanagement.ui.activities.BaseActivity;
import com.nijus.alino.bfwcoopmanagement.ui.activities.SettingsActivity;

public class LoanActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener{
    private BottomSheetBehavior mBottomSheetBehavior;
    private LoanAdapter loanRecyclerViewAdapter;
    private SwipeRefreshLayout mRefreshData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportLoaderManager().initLoader(0,null,this);


        View emptyView = findViewById(R.id.recyclerview_empty_loan);
        Context context = this;
        RecyclerView recyclerView = findViewById(R.id.loan_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        loanRecyclerViewAdapter = new LoanAdapter(this, emptyView, new LoanAdapter.LoanAdapterOnClickHandler() {
            @Override
            public void onClick(Long farmerId, LoanAdapter.ViewHolder vh) {
                //((CoopAgentFragment.OnFragmentInteractionListener) getActivity()).onFragmentInteraction(farmerId, vh);
            }

        }, new LoanAdapter.LoanAdapterOnLongClickHandler() {
            @Override
            public boolean onLongClick(Long farmerId, LoanAdapter.ViewHolder vh) {
                //return ((CoopAgentFragment.OnListFragmentInteractionListener) getActivity()).onLong2FragmentInteraction(farmerId, vh);
                return true;
            }
        });

        mRefreshData = findViewById(R.id.refresh_data_done);
        //mRefreshData.setOnRefreshListener(this);

        recyclerView.setAdapter(loanRecyclerViewAdapter);

        //fab coop fragment
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add_black_24dp);
        fab.setOnClickListener(this);



        //Button button3 = (Button) findViewById( R.id.button_3 );

        //button3.setOnClickListener(this);


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
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, BfwContract.Coops.CONTENT_URI, null, null, null,
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
            //Toast.makeText(this, "breee clic fab voir enregistrer", Toast.LENGTH_LONG).show();
            //Intent intent  = new Intent(this, CreateLoanActivity.class);
            startActivity(new Intent(this, CreateLoanActivity.class));
        }
    }

    @Override
    public void onRefresh() {
        //getLoaderManager().restartLoader(0, null, this);
        getSupportLoaderManager().restartLoader(0,null,this);

    }
}

package com.nijus.alino.bfwcoopmanagement.farmers.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.events.SyncDataEvent;
import com.nijus.alino.bfwcoopmanagement.farmers.adapter.NavigationRecyclerViewAdapter;
import com.nijus.alino.bfwcoopmanagement.farmers.sync.RefreshData;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.activities.CreateFarmerActivity;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class NavigationFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private NavigationRecyclerViewAdapter navigationRecyclerViewAdapter;
    private SwipeRefreshLayout mRefreshData;
    private CoordinatorLayout coordinatorLayout;

    public NavigationFragment() {
    }

    @SuppressWarnings("unused")
    public static NavigationFragment newInstance(int columnCount) {
        NavigationFragment fragment = new NavigationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main2, container, false);
        //View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        View emptyView = view.findViewById(R.id.recyclerview_empty_farmer);
        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.farmers_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        coordinatorLayout = view.findViewById(R.id.coordinator_layout);

        navigationRecyclerViewAdapter = new NavigationRecyclerViewAdapter(getContext(), emptyView, new NavigationRecyclerViewAdapter.FarmerAdapterOnClickHandler() {
            @Override
            public void onClick(Long farmerId, NavigationRecyclerViewAdapter.ViewHolder vh) {
                // ((OnListFragmentInteractionListener) getActivity()).onListFragmentInteraction(farmerId, vh);
            }
        });

        mRefreshData = view.findViewById(R.id.refresh_data_done);
        mRefreshData.setOnRefreshListener(this);

        recyclerView.setAdapter(navigationRecyclerViewAdapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add_black_24dp);
        fab.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle data) {
        super.onActivityCreated(data);
        getLoaderManager().initLoader(0, null, this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveDataEvent(SaveDataEvent saveDataEvent) {
        if (saveDataEvent.isSuccess())
            getLoaderManager().restartLoader(0, null, this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSyncDataEvent(SyncDataEvent syncDataEvent) {
        if (syncDataEvent.isSuccess()) {
            getLoaderManager().restartLoader(0, null, this);
        } else {
            Toast.makeText(getContext(), syncDataEvent.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onRefresh() {
        getLoaderManager().restartLoader(0, null, this);

        if (Utils.isNetworkAvailable(getContext())) {
            getActivity().startService(new Intent(getContext(), RefreshData.class));
        } else {
            Toast.makeText(getContext(), getResources().getString(R.string.connectivity_error), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), BfwContract.Farmer.CONTENT_URI, null, null, null,
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
    public void onClick(View view) {
        //Toast.makeText(getContext(), "breee clic fab", Toast.LENGTH_LONG).show();
        if (view.getId() == R.id.fab) {
            //Intent intent  = new Intent(getActivity(), CreateVendorActivity.class);
            //startActivity(new Intent(this, CreateVendorActivity.class));
            startActivity(new Intent(getActivity(), CreateFarmerActivity.class));
        }

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(long item, NavigationRecyclerViewAdapter.ViewHolder vh);
    }
}

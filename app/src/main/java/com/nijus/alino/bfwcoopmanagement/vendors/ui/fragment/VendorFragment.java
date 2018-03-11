package com.nijus.alino.bfwcoopmanagement.vendors.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.DisableVendorSwipeEvent;
import com.nijus.alino.bfwcoopmanagement.events.EventVendorResetItems;
import com.nijus.alino.bfwcoopmanagement.events.RefreshVendorLoader;
import com.nijus.alino.bfwcoopmanagement.events.RequestEventVendorToDelete;
import com.nijus.alino.bfwcoopmanagement.events.ResponseEventVendorToDelete;
import com.nijus.alino.bfwcoopmanagement.events.SaveLocalVendorEvent;
import com.nijus.alino.bfwcoopmanagement.events.SyncDataEvent;
import com.nijus.alino.bfwcoopmanagement.events.ToggleVendorRequestEvent;
import com.nijus.alino.bfwcoopmanagement.events.ToggleVendorResponseEvent;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;
import com.nijus.alino.bfwcoopmanagement.vendors.adapter.VendorRecyclerViewAdapter;
import com.nijus.alino.bfwcoopmanagement.vendors.sync.RefreshDataVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.activities.CreateVendorActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class VendorFragment extends Fragment implements LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private VendorRecyclerViewAdapter vendorRecyclerViewAdapter;
    private SwipeRefreshLayout mRefreshData;
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private FloatingActionButton fab;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VendorFragment() {
    }

    @SuppressWarnings("unused")
    public static VendorFragment newInstance(int columnCount) {
        VendorFragment fragment = new VendorFragment();
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
        View emptyView = view.findViewById(R.id.recyclerview_empty_farmer);
        TextView textView = view.findViewById(R.id.recyclerview_empty_farmer);
        textView.setText(R.string.there_s_no_vendor);
        Context context = view.getContext();
        mRecyclerView = view.findViewById(R.id.farmers_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        coordinatorLayout = view.findViewById(R.id.coordinator_layout);


        vendorRecyclerViewAdapter = new VendorRecyclerViewAdapter(getContext(), emptyView, new VendorRecyclerViewAdapter.VendorAdapterOnClickHandler() {
            @Override
            public void onClick(Long vendorId, VendorRecyclerViewAdapter.ViewHolder vh) {
                ((OnListFragmentInteractionListener) getActivity()).onListFragmentInteraction(vendorId, vh);
            }
        }, new VendorRecyclerViewAdapter.VendorAdapterOnLongClickListener() {
            @Override
            public void onLongClick(long item, long position, VendorRecyclerViewAdapter.ViewHolder vh) {
                ((OnLongClickFragmentInteractionListener) getActivity()).onLongClickFragmentInteractionListener(item, position, vh);
            }
        });

        mRefreshData = view.findViewById(R.id.refresh_data_done);
        mRefreshData.setOnRefreshListener(this);

        mRecyclerView.setAdapter(vendorRecyclerViewAdapter);
        fab = view.findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add_black_24dp);
        fab.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle data) {
        super.onActivityCreated(data);
        getLoaderManager().initLoader(0, null, this);

    }


    @Subscribe
    public void onToggleVendorRequestEvent(ToggleVendorRequestEvent farmerRequestEvent) {

        vendorRecyclerViewAdapter.toggleSelection(farmerRequestEvent.getPosition());
        int count = vendorRecyclerViewAdapter.getSelectedItemCount();

        EventBus.getDefault().post(new ToggleVendorResponseEvent(count));

    }

    @Subscribe
    public void onRequestVendorToDelete(RequestEventVendorToDelete farmerToDelete) {

        EventBus.getDefault().post(new ResponseEventVendorToDelete(vendorRecyclerViewAdapter.getSelectedItems()));

    }

    @Subscribe
    public void onDisableVendorSwipeEvent(DisableVendorSwipeEvent disableVendorSwipeEvent) {

        mRefreshData.setEnabled(false);
        fab.setVisibility(View.INVISIBLE);

    }

    @Subscribe
    public void onEventVendorResetItems(EventVendorResetItems eventVendorResetItems) {
        vendorRecyclerViewAdapter.clearSelections();
        mRefreshData.setEnabled(true);
        fab.setVisibility(View.VISIBLE);

        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                vendorRecyclerViewAdapter.resetAnimationIndex();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveLocalVendorEvent(SaveLocalVendorEvent saveLocalVendorEvent) {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Subscribe
    public void onRefreshVendorLoader(RefreshVendorLoader farmerLoader) {
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
            getActivity().startService(new Intent(getContext(), RefreshDataVendor.class));
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
        return new CursorLoader(getActivity(), BfwContract.Vendor.CONTENT_URI, null, null, null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        vendorRecyclerViewAdapter.swapCursor(data);
        mRefreshData.post(new Runnable() {
            @Override
            public void run() {
                mRefreshData.setRefreshing(false);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        vendorRecyclerViewAdapter.swapCursor(null);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            startActivity(new Intent(getActivity(), CreateVendorActivity.class));
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
        void onListFragmentInteraction(long item, VendorRecyclerViewAdapter.ViewHolder vh);
    }

    public interface OnLongClickFragmentInteractionListener {
        void onLongClickFragmentInteractionListener(long item, long position, VendorRecyclerViewAdapter.ViewHolder vh);
    }

}

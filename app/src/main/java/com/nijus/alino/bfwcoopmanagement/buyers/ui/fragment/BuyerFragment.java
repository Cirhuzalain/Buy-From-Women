package com.nijus.alino.bfwcoopmanagement.buyers.ui.fragment;

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
import com.nijus.alino.bfwcoopmanagement.buyers.adapter.BuyerAdapter;
import com.nijus.alino.bfwcoopmanagement.buyers.sync.RefreshData;
import com.nijus.alino.bfwcoopmanagement.buyers.ui.activities.CreateBuyerActivity;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.DisableBuyerSwipeEvent;
import com.nijus.alino.bfwcoopmanagement.events.EventBuyerResetItems;
import com.nijus.alino.bfwcoopmanagement.events.RefreshBuyerLoader;
import com.nijus.alino.bfwcoopmanagement.events.RequestEventBuyerToDelete;
import com.nijus.alino.bfwcoopmanagement.events.ResponseEventBuyerToDelete;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.events.SyncDataEvent;
import com.nijus.alino.bfwcoopmanagement.events.ToggleBuyerRequestEvent;
import com.nijus.alino.bfwcoopmanagement.events.ToggleBuyerResponseEvent;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class BuyerFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private BuyerAdapter buyerRecyclerViewAdapter;
    private SwipeRefreshLayout mRefreshData;
    private LinearLayoutManager mLayoutManager;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private CoordinatorLayout coordinatorLayout;

    public BuyerFragment() {
    }

    @SuppressWarnings("unused")
    public static BuyerFragment newInstance(int columnCount) {
        BuyerFragment fragment = new BuyerFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buyer, container, false);
        View emptyView = view.findViewById(R.id.recyclerview_empty_bayer);
        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.buyer_list);
        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        coordinatorLayout = view.findViewById(R.id.coordinator_layout);

        buyerRecyclerViewAdapter = new BuyerAdapter(getContext(), emptyView, new BuyerAdapter.BuyerAdapterOnClickHandler() {
            @Override
            public void onClick(Long buyerId, BuyerAdapter.ViewHolder vh) {
                ((OnListFragmentInteractionListener) getActivity()).onListFragmentInteraction(buyerId, vh);
            }
        }, new BuyerAdapter.BuyerAdapterOnLongClickHandler() {
            @Override
            public void onLongClick(long buyerId, long position, BuyerAdapter.ViewHolder vh) {
                ((OnLongClickFragmentInteractionListener) getActivity()).onLongClickFragmentInteractionListener(buyerId, position, vh);
            }
        }, mLayoutManager);

        mRefreshData = view.findViewById(R.id.refresh_data_done);
        mRefreshData.setOnRefreshListener(this);

        recyclerView.setAdapter(buyerRecyclerViewAdapter);

        //fab buyer fragment
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
    public void onToggleBuyerRequestEvent(ToggleBuyerRequestEvent farmerRequestEvent) {

        buyerRecyclerViewAdapter.toggleSelection(farmerRequestEvent.getPosition());
        int count = buyerRecyclerViewAdapter.getSelectedItemCount();

        EventBus.getDefault().post(new ToggleBuyerResponseEvent(count));

    }

    @Subscribe
    public void onRequestBuyerToDelete(RequestEventBuyerToDelete buyerToDelete) {

        EventBus.getDefault().post(new ResponseEventBuyerToDelete(buyerRecyclerViewAdapter.getSelectedItems()));

    }

    @Subscribe
    public void onDisableBuyerSwipeEvent(DisableBuyerSwipeEvent disableBuyerSwipeEvent) {

        mRefreshData.setEnabled(false);
        fab.setVisibility(View.INVISIBLE);

    }

    @Subscribe
    public void onEventBuyerResetItems(EventBuyerResetItems eventBuyerResetItems) {

        buyerRecyclerViewAdapter.clearSelections();
        mRefreshData.setEnabled(true);
        fab.setVisibility(View.VISIBLE);

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                buyerRecyclerViewAdapter.resetAnimationIndex();
            }
        });

    }

    @Subscribe
    public void onSaveDataEvent(SaveDataEvent saveDataEvent) {
        if (saveDataEvent.isSuccess())
            getLoaderManager().restartLoader(0, null, this);
    }

    @Subscribe
    public void onRefreshBuyerLoader(RefreshBuyerLoader bayerLoader) {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Subscribe
    public void onSyncDataEvent(SyncDataEvent syncDataEvent) {
        if (syncDataEvent.isSuccess()) {
            getLoaderManager().restartLoader(0, null, this);
        } else {
            Toast.makeText(getContext(), syncDataEvent.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), BfwContract.Buyer.CONTENT_URI, null, null, null,
                null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        buyerRecyclerViewAdapter.swapCursor(data);
        mRefreshData.post(new Runnable() {
            @Override
            public void run() {
                mRefreshData.setRefreshing(false);
            }
        });
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        buyerRecyclerViewAdapter.swapCursor(null);
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
    public void onClick(View view) {

        if (view.getId() == R.id.fab) {
            startActivity(new Intent(getActivity(), CreateBuyerActivity.class));
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(long item, BuyerAdapter.ViewHolder vh);
    }

    public interface OnLongClickFragmentInteractionListener {
        void onLongClickFragmentInteractionListener(long item, long position, BuyerAdapter.ViewHolder vh);
    }
}

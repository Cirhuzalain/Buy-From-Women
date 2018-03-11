package com.nijus.alino.bfwcoopmanagement.coops.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import com.nijus.alino.bfwcoopmanagement.coops.adapter.CoopAdapter;
import com.nijus.alino.bfwcoopmanagement.coops.helper.DividerItemDecoration;
import com.nijus.alino.bfwcoopmanagement.coops.ui.activities.CreateCoopActivity;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.DisableCoopSwipeEvent;
import com.nijus.alino.bfwcoopmanagement.events.EventCoopResetItems;
import com.nijus.alino.bfwcoopmanagement.events.RefreshCoopLoader;
import com.nijus.alino.bfwcoopmanagement.events.RequestEventCoopToDelete;
import com.nijus.alino.bfwcoopmanagement.events.ResponseEventCoopToDelete;
import com.nijus.alino.bfwcoopmanagement.events.SyncDataEvent;
import com.nijus.alino.bfwcoopmanagement.events.ToggleCoopResponseEvent;
import com.nijus.alino.bfwcoopmanagement.events.ToggleRequestCoopEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CoopFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private CoopAdapter coopAdapter;
    private SwipeRefreshLayout mRefreshData;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;

    public CoopFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CoopFragment.
     */
    public static CoopFragment newInstance(String param1, String param2) {
        CoopFragment fragment = new CoopFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_coop_agent, container, false);
        View emptyView = view.findViewById(R.id.recyclerview_empty_coop_Agent);

        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.coopsagent_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        coopAdapter = new CoopAdapter(getContext(), emptyView, new CoopAdapter.CoopAdapterOnClickHandler() {
            @Override
            public void onClick(Long farmerId, CoopAdapter.ViewHolder vh) {
                ((OnCoopFragmentInteractionListener) getActivity()).onCoopFragmentInteraction(farmerId, vh);
            }
        }, new CoopAdapter.CoopAdapterOnLongClickHandler() {
            @Override
            public void onLongClick(Long farmerId, int position) {
                ((OnCoopFragmentLongClick) getActivity()).onCoopLongClick(farmerId, position);
            }
        });

        mRefreshData = view.findViewById(R.id.refresh_data_done);
        mRefreshData.setOnRefreshListener(this);

        recyclerView.setAdapter(coopAdapter);

        //fab coop fragment
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
    public void onToggleRequestCoopEvent(ToggleRequestCoopEvent coopRequestEvent) {

        coopAdapter.toggleSelection(coopRequestEvent.getPosition());
        int count = coopAdapter.getSelectedItemCount();

        EventBus.getDefault().post(new ToggleCoopResponseEvent(count));

    }

    @Subscribe
    public void onRequestCoopToDelete(RequestEventCoopToDelete coopToDelete) {
        EventBus.getDefault().post(new ResponseEventCoopToDelete(coopAdapter.getSelectedItems()));
    }

    @Subscribe
    public void onDisableCoopSwipeEvent(DisableCoopSwipeEvent disableCoopSwipeEvent) {

        mRefreshData.setEnabled(false);
        fab.setVisibility(View.INVISIBLE);

    }

    @Subscribe
    public void onEventCoopResetItems(EventCoopResetItems eventCoopResetItems) {
        coopAdapter.clearSelections();
        mRefreshData.setEnabled(true);
        fab.setVisibility(View.VISIBLE);

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                coopAdapter.resetAnimationIndex();
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), BfwContract.Coops.CONTENT_URI, null, null, null,
                null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        coopAdapter.swapCursor(data);
        mRefreshData.post(new Runnable() {
            @Override
            public void run() {
                mRefreshData.setRefreshing(false);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSyncDataEvent(SyncDataEvent syncDataEvent) {
        if (syncDataEvent.isSuccess()) {
            Toast.makeText(getContext(), syncDataEvent.getMessage(), Toast.LENGTH_LONG).show();
            getLoaderManager().restartLoader(0, null, this);
        } else {
            Toast.makeText(getContext(), syncDataEvent.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        coopAdapter.swapCursor(null);
    }

    @Subscribe
    public void onRefreshCoopLoader(RefreshCoopLoader coopLoader) {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onRefresh() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            startActivity(new Intent(getActivity(), CreateCoopActivity.class));
        }
    }

    public interface OnCoopFragmentInteractionListener {
        void onCoopFragmentInteraction(long item, CoopAdapter.ViewHolder vh);
    }

    public interface OnCoopFragmentLongClick {
        void onCoopLongClick(long item, int position);
    }
}

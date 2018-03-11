package com.nijus.alino.bfwcoopmanagement.coopAgent.ui.fragment;

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
;
import com.nijus.alino.bfwcoopmanagement.coopAgent.adapter.CoopAgentAdapter;
import com.nijus.alino.bfwcoopmanagement.coopAgent.sync.RefreshData;
import com.nijus.alino.bfwcoopmanagement.coopAgent.ui.activities.CreateCoopAgentActivity;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.DisableAgentSwipeEvent;
import com.nijus.alino.bfwcoopmanagement.events.EventAgentResetItems;
import com.nijus.alino.bfwcoopmanagement.events.RefreshAgentLoader;
import com.nijus.alino.bfwcoopmanagement.events.RequestEventAgentToDelete;
import com.nijus.alino.bfwcoopmanagement.events.ResponseEventAgentToDelete;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.events.SyncDataEvent;
import com.nijus.alino.bfwcoopmanagement.events.ToggleAgentRequestEvent;
import com.nijus.alino.bfwcoopmanagement.events.ToggleAgentResponseEvent;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.nijus.alino.bfwcoopmanagement.coopAgent.ui.fragment.CoopAgentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoopAgentFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private CoopAgentAdapter agentRecyclerViewAdapter;
    private SwipeRefreshLayout mRefreshData;
    private LinearLayoutManager mLayoutManager;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private CoordinatorLayout coordinatorLayout;

    public CoopAgentFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameter.
     *
     * ARG_COLUMN_COUNT = "column-count";
     * @return A new instance of fragment CoopFragment.
     */
    public static CoopAgentFragment newInstance(int columnCount) {
        CoopAgentFragment fragment = new CoopAgentFragment();
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
        View view = inflater.inflate(R.layout.fragment_coop_agent, container, false);
        View emptyView = view.findViewById(R.id.recyclerview_empty_coop_Agent);
        Context context = view.getContext();

        recyclerView = view.findViewById(R.id.coopsagent_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        agentRecyclerViewAdapter = new CoopAgentAdapter(getContext(), emptyView, new CoopAgentAdapter.CoopAgentAdapterOnClickHandler() {
            @Override
            public void onClick(Long agentId, CoopAgentAdapter.ViewHolder vh) {
                ((CoopAgentFragment.OnListFragmentInteractionListener) getActivity()).onListFragmentInteraction(agentId, vh);
            }
        }, new CoopAgentAdapter.CoopAgentAdapterOnLongClickHandler() {
            @Override
            public void onLongClick(long agentId, long position, CoopAgentAdapter.ViewHolder vh) {
                ((CoopAgentFragment.OnLongClickFragmentInteractionListener) getActivity()).onLongClickFragmentInteractionListener(agentId,position, vh);
            }
        },mLayoutManager);

        mRefreshData = view.findViewById(R.id.refresh_data_done);
        mRefreshData.setOnRefreshListener(this);

        recyclerView.setAdapter(agentRecyclerViewAdapter);

        //ADD LISTENER TO RECYCLEVIEW WHEN SWIPPING IT

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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), BfwContract.CoopAgent.CONTENT_URI, null, null, null,
                null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        agentRecyclerViewAdapter.swapCursor(data);
        mRefreshData.post(new Runnable() {
            @Override
            public void run() {
                mRefreshData.setRefreshing(false);
            }
        });
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        agentRecyclerViewAdapter.swapCursor(null);
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
    public void onClick(View view) {

        if (view.getId() == R.id.fab) {
            startActivity(new Intent(getActivity(), CreateCoopAgentActivity.class));
        }
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void onToggleAgentRequestEvent(ToggleAgentRequestEvent agentRequestEvent) {

        agentRecyclerViewAdapter.toggleSelection(agentRequestEvent.getPosition());
        int count = agentRecyclerViewAdapter.getSelectedItemCount();

        EventBus.getDefault().post(new ToggleAgentResponseEvent(count));

    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void onRequestAgentToDelete(RequestEventAgentToDelete buyerToDelete) {

        EventBus.getDefault().post(new ResponseEventAgentToDelete(agentRecyclerViewAdapter.getSelectedItems()));
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void onDisableAgentSwipeEvent(DisableAgentSwipeEvent disableAgentSwipeEven) {
        mRefreshData.setEnabled(false);
        fab.setVisibility(View.INVISIBLE);
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void onEventAgentResetItems(EventAgentResetItems eventAgentResetItems) {
        agentRecyclerViewAdapter.clearSelections();
        mRefreshData.setEnabled(true);
        fab.setVisibility(View.VISIBLE);

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                agentRecyclerViewAdapter.resetAnimationIndex();
            }
        });
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void onSaveDataEvent(SaveDataEvent saveDataEvent) {
        if (saveDataEvent.isSuccess())
            getLoaderManager().restartLoader(0, null, this);
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void onRefreshAgentLoader(RefreshAgentLoader bayerLoader) {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void onSyncDataEvent(SyncDataEvent syncDataEvent) {
        if (syncDataEvent.isSuccess()) {
            getLoaderManager().restartLoader(0, null, this);
        } else {
            Toast.makeText(getContext(), syncDataEvent.getMessage(), Toast.LENGTH_LONG).show();
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
        void onListFragmentInteraction(long item, CoopAgentAdapter.ViewHolder vh);
    }

    public interface OnLongClickFragmentInteractionListener {
        void onLongClickFragmentInteractionListener(long item, long position, CoopAgentAdapter.ViewHolder vh);
    }
}

package com.nijus.alino.bfwcoopmanagement.vendors.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.events.SyncDataEvent;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;
import com.nijus.alino.bfwcoopmanagement.vendors.adapter.RecyclerItemTouchHelperVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.adapter.VendorRecyclerViewAdapter;
import com.nijus.alino.bfwcoopmanagement.vendors.sync.RefreshDataVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.activities.CreateVendorActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.nijus.alino.bfwcoopmanagement.data.BfwContract.Farmer.CONTENT_URI;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class VendorFragment extends Fragment implements LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, RecyclerItemTouchHelperVendor.RecyclerItemTouchHelperListener {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private VendorRecyclerViewAdapter navigationRecyclerViewAdapter;
    private SwipeRefreshLayout mRefreshData;
    private CoordinatorLayout coordinatorLayout;
    private Uri mUri;


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
        //View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        View emptyView = view.findViewById(R.id.recyclerview_empty_farmer);
        TextView textView = view.findViewById(R.id.recyclerview_empty_farmer);
        textView.setText(R.string.there_s_no_vendor);
        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.farmers_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);

        //recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        coordinatorLayout = view.findViewById(R.id.coordinator_layout);


        navigationRecyclerViewAdapter = new VendorRecyclerViewAdapter(getContext(), emptyView, new VendorRecyclerViewAdapter.VendorAdapterOnClickHandler() {
            @Override
            public void onClick(Long vendorId, VendorRecyclerViewAdapter.ViewHolder vh) {
                ((OnListFragmentInteractionListener) getActivity()).onListFragmentInteraction(vendorId, vh);
            }
        });

        mRefreshData = view.findViewById(R.id.refresh_data_done);
        mRefreshData.setOnRefreshListener(this);

        recyclerView.setAdapter(navigationRecyclerViewAdapter);

        //add on 1 febrary 2018 listener to recycleview when touching it
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelperVendor(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT /*| ItemTouchHelper.RIGHT | ItemTouchHelper.UP*/) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
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
        return new CursorLoader(getActivity(), CONTENT_URI, null, null, null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        navigationRecyclerViewAdapter.swapCursor(data);
        mRefreshData.post(new Runnable() {
            @Override
            public void run() {
                mRefreshData.setRefreshing(false);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        navigationRecyclerViewAdapter.swapCursor(null);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            startActivity(new Intent(getActivity(), CreateVendorActivity.class));
        }

    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof VendorRecyclerViewAdapter.ViewHolder) {
            // get the removed item name to display it in snack bar
            String name = ((VendorRecyclerViewAdapter.ViewHolder) viewHolder).mUname.getText().toString();
            String id3 = ((VendorRecyclerViewAdapter.ViewHolder) viewHolder).mUphone.getText().toString();

            String id = ((VendorRecyclerViewAdapter.ViewHolder) viewHolder).id_cursor_to_delete;


            // backup of removed item for undo purpose
            final long deletedIte = (((VendorRecyclerViewAdapter.ViewHolder) viewHolder).getAdapterPosition());

            //Item deletedItem = NavigationRecyclerViewAdapter.ViewHolder.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();


            String farmerRemove = BfwContract.Farmer.TABLE_NAME + "." +
                    BfwContract.Farmer._ID + " =  ? ";

            mUri = BfwContract.Farmer.buildFarmerUri(Long.valueOf(id));
            getContext().getContentResolver().delete(BfwContract.Farmer.CONTENT_URI, farmerRemove, new String[]{id});

            // remove the item from recycler view
            navigationRecyclerViewAdapter.removeItem(deletedIndex);


            onRefresh();

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, name + " removed! ", Snackbar.LENGTH_LONG);
            snackbar.setAction("", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                   // navigationRecyclerViewAdapter.restoreItem(deletedItem, deletedIndex);
                    //navigationRecyclerViewAdapter.restoreItem(((NavigationRecyclerViewAdapter.ViewHolder) viewHolder).onClick(view),deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
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
}

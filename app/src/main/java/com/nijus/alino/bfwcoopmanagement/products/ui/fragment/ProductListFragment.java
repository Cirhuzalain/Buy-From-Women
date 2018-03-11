package com.nijus.alino.bfwcoopmanagement.products.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.RefreshProductLoader;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.events.SyncDataEvent;
import com.nijus.alino.bfwcoopmanagement.loans.adapter.PaymentAdapter;
import com.nijus.alino.bfwcoopmanagement.pojo.Product;
import com.nijus.alino.bfwcoopmanagement.products.adapter.ProductAdapter;
import com.nijus.alino.bfwcoopmanagement.products.sync.RefreshData;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductListFragment.OnListFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemLongClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Cursor mCursor;

    private ProductAdapter productRecyclerViewAdapter;
    private SwipeRefreshLayout mRefreshData;

    private OnListFragmentInteractionListener mListener;

    public ProductListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductListFragment.
     */
    public static ProductListFragment newInstance(String param1, String param2) {
        ProductListFragment fragment = new ProductListFragment();
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
        getLoaderManager().initLoader(0, null, this);
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.activity_product_order, container, false);

        GridView gridView = root.findViewById(R.id.productGridview);
        View emptyView = root.findViewById(R.id.girdview_empty);

        productRecyclerViewAdapter = new ProductAdapter(getContext(), emptyView, true);
        gridView.setAdapter(productRecyclerViewAdapter);

        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);

        mRefreshData = root.findViewById(R.id.refresh_data_done);
        mRefreshData.setOnRefreshListener(this);
        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        int id = productRecyclerViewAdapter.getServerProductId(i);
        Bundle bundle = new Bundle();
        bundle.putInt("id_product", id); // set Fragment class Arguments
        UpdateProductDialogFragment dialogFragment = new UpdateProductDialogFragment();
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getFragmentManager(), "dialogPurchaseTag");

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        SharedPreferences prefs = getActivity().getSharedPreferences(getResources().getString(R.string.application_key),
                Context.MODE_PRIVATE);
        String groupName = prefs.getString(getResources().getString(R.string.g_name), "123");

        if (!groupName.equals("Buyer")) {
            int id = productRecyclerViewAdapter.getServerProductId(i);
            String name = productRecyclerViewAdapter.getName(i);
            Bundle bundle = new Bundle();
            bundle.putInt("id_product", id);
            bundle.putString("name_product", name);
            DeleteProductDialogFragment dialogFragment = new DeleteProductDialogFragment();
            dialogFragment.setArguments(bundle);
            dialogFragment.show(getFragmentManager(), "dialogPurchaseTag");
        }
        return true;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // filter for agent and vendor
        /*SharedPreferences prefs = getActivity().getSharedPreferences(getResources().getString(R.string.application_key),
                Context.MODE_PRIVATE);
        String groupName = prefs.getString(getResources().getString(R.string.g_name), "123");*/
        return new CursorLoader(
                getContext(),
                BfwContract.ProductTemplate.CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        productRecyclerViewAdapter.swapCursor(data);
        mRefreshData.post(new Runnable() {
            @Override
            public void run() {
                mRefreshData.setRefreshing(false);
            }
        });

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        productRecyclerViewAdapter.swapCursor(null);
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

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void onRefreshProductLoader(RefreshProductLoader productLoader) {
        getLoaderManager().restartLoader(0, null, this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveDataEvent(SaveDataEvent saveDataEvent) {
        getLoaderManager().restartLoader(0, null, this);
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
    }
}

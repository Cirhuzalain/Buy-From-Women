package com.nijus.alino.bfwcoopmanagement.loans.ui.fragment;
//CoopAgentFragment

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
//import com.nijus.alino.bfwcoopmanagement.buyers.ui.activities.CreateBuyerActivity;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.loans.adapter.LoanAdapter;
import com.nijus.alino.bfwcoopmanagement.loans.ui.activities.CreateLoanActivity;


public class LoanFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private LoanAdapter loanRecyclerViewAdapter;
    private SwipeRefreshLayout mRefreshData;

    //private CoopAgentFragment.OnFragmentInteractionListener mListener;

    public LoanFragment() {
        // Required empty public constructor
    }


    public static LoanFragment newInstance(String param1, String param2) {
        LoanFragment fragment = new LoanFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loan, container, false);
        View emptyView = view.findViewById(R.id.recyclerview_empty_loan);
        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.loan_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        loanRecyclerViewAdapter = new LoanAdapter(getContext(), emptyView, new LoanAdapter.LoanAdapterOnClickHandler() {
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

        mRefreshData = view.findViewById(R.id.refresh_data_done);
        mRefreshData.setOnRefreshListener(this);

        recyclerView.setAdapter(loanRecyclerViewAdapter);

        //fab coop fragment
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), BfwContract.Coops.CONTENT_URI, null, null, null,
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
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.fab) {
            Toast.makeText(getContext(), "Comming soon", Toast.LENGTH_LONG).show();
           //Intent intent  = new Intent(getActivity(), CreateLoanActivity.class);
            //startActivity(new Intent(getActivity(), CreateLoanActivity.class));
        }
    }


}

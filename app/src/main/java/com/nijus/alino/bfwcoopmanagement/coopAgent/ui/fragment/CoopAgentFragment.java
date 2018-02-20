package com.nijus.alino.bfwcoopmanagement.coopAgent.ui.fragment;
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
import com.nijus.alino.bfwcoopmanagement.coopAgent.adapter.CoopAgentAdapter;
import com.nijus.alino.bfwcoopmanagement.coopAgent.ui.activities.CreateCoopAgentActivity;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.nijus.alino.bfwcoopmanagement.coops.ui.fragment.CoopFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.nijus.alino.bfwcoopmanagement.coopAgent.ui.fragment.CoopAgentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoopAgentFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private CoopAgentAdapter navigationRecyclerViewAdapter;
    private SwipeRefreshLayout mRefreshData;

    //private CoopAgentFragment.OnFragmentInteractionListener mListener;

    public CoopAgentFragment() {
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
    public static CoopAgentFragment newInstance(String param1, String param2) {
        CoopAgentFragment fragment = new CoopAgentFragment();
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
        View view = inflater.inflate(R.layout.fragment_coop_agent, container, false);
        View emptyView = view.findViewById(R.id.recyclerview_empty_coop_Agent);
        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.coopsagent_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        navigationRecyclerViewAdapter = new CoopAgentAdapter(getContext(), emptyView, new CoopAgentAdapter.CoopAgentAdapterOnClickHandler() {
            @Override
            public void onClick(Long farmerId, CoopAgentAdapter.ViewHolder vh) {
                //((CoopAgentFragment.OnFragmentInteractionListener) getActivity()).onFragmentInteraction(farmerId, vh);
            }

        }, new CoopAgentAdapter.CoopAgentAdapterOnLongClickHandler() {
            @Override
            public boolean onLongClick(Long farmerId, CoopAgentAdapter.ViewHolder vh) {
                //return ((CoopAgentFragment.OnListFragmentInteractionListener) getActivity()).onLong2FragmentInteraction(farmerId, vh);
                return true;
            }
        });

        mRefreshData = view.findViewById(R.id.refresh_data_done);
        mRefreshData.setOnRefreshListener(this);

        recyclerView.setAdapter(navigationRecyclerViewAdapter);

        //ADD LISTENER TO RECYCLEVIEW WHEN SWIPPING IT

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
        return new CursorLoader(getActivity(), BfwContract.CoopAgent.CONTENT_URI, null, null, null,
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
    public void onRefresh() {
        getLoaderManager().restartLoader(0, null, this);
    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CoopAgentFragment.OnFragmentInteractionListener) {
            mListener = (CoopAgentFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
*/
   /* @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.fab) {
            //Toast.makeText(getContext(), "breee clic fab voir enregistrer", Toast.LENGTH_LONG).show();
            //Intent intent  = new Intent(getActivity(), CreateCoopActivity.class);
            startActivity(new Intent(getActivity(), CreateCoopAgentActivity.class));
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
   /* public interface OnFragmentInteractionListener {
        void onFragmentInteraction(long item, CoopAgentAdapter.ViewHolder vh);
        boolean onLongFragmentInteraction(long item, CoopAgentAdapter.ViewHolder vh);
    }
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(long item, CoopAgentAdapter.ViewHolder vh);
        boolean onLong2FragmentInteraction(long item, CoopAgentAdapter.ViewHolder vh);
    }
*/

}

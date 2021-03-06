package com.nijus.alino.bfwcoopmanagement.sales.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.fragment.dummy.DummyCont.DummyItem;
import com.nijus.alino.bfwcoopmanagement.pojo.Product;
import com.nijus.alino.bfwcoopmanagement.sales.adapter.SaleOrderAdapter;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SaleOrderFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private Product[] productArrayList = {
            new Product("MAIZEIII", "250000 RWF", "150000 KG"),
            new Product("MAIZEIII", "250000 RWF", "150000 KG"),
            new Product("MAIZEIII", "250000 RWF", "150000 KG"),
            new Product("MAIZEIII", "250000 RWF", "150000 KG"),
            new Product("MAIZEIII", "250000 RWF", "150000 KG"),
            new Product("MAIZEIII", "250000 RWF", "150000 KG"),
            new Product("MAIZEIII", "250000 RWF", "150000 KG"),
            new Product("MAIZEIII", "250000 RWF", "150000 KG"),
            new Product("MAIZEIII", "250000 RWF", "150000 KG"),
            new Product("MAIZEIII", "250000 RWF", "150000 KG"),
            new Product("MAIZEIII", "250000 RWF", "150000 KG")
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SaleOrderFragment() {
    }

    @SuppressWarnings("unused")
    public static SaleOrderFragment newInstance(int columnCount) {
        SaleOrderFragment fragment = new SaleOrderFragment();
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
        View root = inflater.inflate(R.layout.activity_sale_order_info, container, false);
        GridView gridView = root.findViewById(R.id.saleGridview);

        SaleOrderAdapter adapter = new SaleOrderAdapter(getContext(), productArrayList, false);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Product product = productArrayList[i];
        BottomSheetDialogFragment bottomSheetDialogFragment = new SalesBottomSheetDialogFragment();
        bottomSheetDialogFragment.show(getFragmentManager(), bottomSheetDialogFragment.getTag());
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
        void onListFragmentInteraction(DummyItem item);
    }
}

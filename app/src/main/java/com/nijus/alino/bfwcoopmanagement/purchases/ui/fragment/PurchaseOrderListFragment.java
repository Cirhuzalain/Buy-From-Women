package com.nijus.alino.bfwcoopmanagement.purchases.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.products.adapter.ProductAdapter;
import com.nijus.alino.bfwcoopmanagement.pojo.Product;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PurchaseOrderListFragment.OnListFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PurchaseOrderListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PurchaseOrderListFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

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

    private OnListFragmentInteractionListener mListener;

    public PurchaseOrderListFragment() {
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
    public static PurchaseOrderListFragment newInstance(String param1, String param2) {
        PurchaseOrderListFragment fragment = new PurchaseOrderListFragment();
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
        View root = inflater.inflate(R.layout.activity_purchase_order, container, false);

        GridView gridView = root.findViewById(R.id.productGridview);

        ProductAdapter adapter = new ProductAdapter(getContext(), productArrayList, true);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Product product = productArrayList[i];
        UpdatePurchaseOrderDialogFragment dialogFragment = new UpdatePurchaseOrderDialogFragment();
        dialogFragment.show(getFragmentManager(), "dialogPurchaseTag");
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
        //void onListFragmentInteraction(DummyCont.DummyPurchase purchase);
    }
}

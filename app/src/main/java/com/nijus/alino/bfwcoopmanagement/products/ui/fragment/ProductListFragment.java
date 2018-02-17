package com.nijus.alino.bfwcoopmanagement.products.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.pojo.Product;
import com.nijus.alino.bfwcoopmanagement.products.adapter.ProductAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductListFragment.OnListFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductListFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private Product[] productArrayList = {
            new Product("MAIZEIII", "25000 RWF", "150 KG"),
            new Product("MAIZEII", "2500 RWF", "15000 KG"),
            new Product("MAIZEI", "25000 RWF", "150000 KG"),
            new Product("MAIZEIII", "250000 RWF", "150000 KG"),
            new Product("MAIZEIII", "250000 RWF", "10000 KG"),
            new Product("MAIZEI II", "25000 RWF", "15000 KG"),
            new Product("MAIZEIII", "250000 RWF", "15000 KG"),
            new Product("MAIZEII", "25000 RWF", "150000 KG"),
            new Product("MAIZE", "250000 RWF", "150 KG"),
            new Product("MAIZEIII", "250000 RWF", "1500 KG"),
            new Product("MAIZE II", "250000 RWF", "150000 KG")
    };

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.activity_product_order, container, false);

        GridView gridView = root.findViewById(R.id.productGridview);

        ProductAdapter adapter = new ProductAdapter(getContext(), productArrayList, true);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Product product = productArrayList[i];
        UpdateProductDialogFragment dialogFragment = new UpdateProductDialogFragment();
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

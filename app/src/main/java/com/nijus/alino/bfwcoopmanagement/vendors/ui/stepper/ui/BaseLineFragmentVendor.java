package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.PageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.BaseLineVendor;

public class BaseLineFragmentVendor extends Fragment {

    public static final String ARG_KEY = "key";
    private String mKey;
    private PageVendorVendor mPageVendor;
    private PageFragmentCallbacksVendor mCallbacks;

    private BaseLineVendor baseLineVendor = new BaseLineVendor();

    private AutoCompleteTextView totProdKg;
    private AutoCompleteTextView totLostKg;
    private AutoCompleteTextView totSoldKg;
    private AutoCompleteTextView totVolSoldCoops;
    private AutoCompleteTextView priceSoldToCoop;
    private AutoCompleteTextView totVolSoldKg;
    private AutoCompleteTextView priceSoldKg;

    public BaseLineFragmentVendor() {
        super();
    }

    public static BaseLineFragmentVendor create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        BaseLineFragmentVendor fragment = new BaseLineFragmentVendor();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPageVendor = mCallbacks.onGetPage(mKey);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.baseline_layout, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.base_line));

        totProdKg = rootView.findViewById(R.id.tot_prod_kg);
        totLostKg = rootView.findViewById(R.id.tot_lost);
        totSoldKg = rootView.findViewById(R.id.tot_sold);
        totVolSoldCoops = rootView.findViewById(R.id.tot_vol_coop);
        priceSoldToCoop = rootView.findViewById(R.id.price_sold_kg);
        totVolSoldKg = rootView.findViewById(R.id.tot_vol_side_sold);
        priceSoldKg = rootView.findViewById(R.id.pr_sold_kg);

        totProdKg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    baseLineVendor.setTotProdInKg(Integer.parseInt(charSequence.toString()));
                    mPageVendor.getData().putParcelable("baseline", baseLineVendor);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        totLostKg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    baseLineVendor.setTotLostInKg(Integer.parseInt(charSequence.toString()));
                    mPageVendor.getData().putParcelable("baseline", baseLineVendor);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        totSoldKg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    baseLineVendor.setTotSoldInKg(Integer.parseInt(charSequence.toString()));
                    mPageVendor.getData().putParcelable("baseline", baseLineVendor);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        totVolSoldCoops.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    baseLineVendor.setTotVolumeSoldCoopInKg(Integer.parseInt(charSequence.toString()));
                    mPageVendor.getData().putParcelable("baseline", baseLineVendor);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        priceSoldToCoop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    baseLineVendor.setPriceSoldToCoopPerKg(Integer.parseInt(charSequence.toString()));
                    mPageVendor.getData().putParcelable("baseline", baseLineVendor);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        totVolSoldKg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    baseLineVendor.setTotVolSoldInKg(Integer.parseInt(charSequence.toString()));
                    mPageVendor.getData().putParcelable("baseline", baseLineVendor);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        priceSoldKg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    baseLineVendor.setPriceSoldInKg(Integer.parseInt(charSequence.toString()));
                    mPageVendor.getData().putParcelable("baseline", baseLineVendor);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mCallbacks = (PageFragmentCallbacksVendor) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
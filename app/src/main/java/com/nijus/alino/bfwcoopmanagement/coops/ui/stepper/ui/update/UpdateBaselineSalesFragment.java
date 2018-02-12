package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.ui.update;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.BaselineSales;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.ui.PageFragmentCallbacks;

public class UpdateBaselineSalesFragment extends Fragment {

    public static final String ARG_KEY = "key";
    public final String LOG_TAG = UpdateBaselineSalesFragment.class.getSimpleName();
    private String mKey;

    private BaselineSales baselineSales = new BaselineSales();

    private Page mPage;
    private PageFragmentCallbacks mCallbacks;

    private Spinner harvsetSeason;
    private AutoCompleteTextView qty_agregated_from_members;

    private AutoCompleteTextView cycle_h_at_price_per_kg;
    private AutoCompleteTextView qty_purchaced_from_non_members;
    private AutoCompleteTextView non_member_purchase_at_price_per_kg;
    private Spinner rgcc_contact_under_ftma;
    private AutoCompleteTextView qty_of_rgcc_contact;
    private AutoCompleteTextView qty_sold_to_rgcc;
    private AutoCompleteTextView price_per_kg_sold_to_rgcc;
    private AutoCompleteTextView qty_slod_outside_rgcc;

    private CheckBox formal_buyer;
    private CheckBox informal_buyer;
    private CheckBox other;

    private AutoCompleteTextView price_per_kg_sold_outside_ftma;


    public UpdateBaselineSalesFragment() {
        super();
    }

    public static UpdateBaselineSalesFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UpdateBaselineSalesFragment fragment = new UpdateBaselineSalesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = mCallbacks.onGetPage(mKey);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.baseline_sales, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.baseline_sales));



        harvsetSeason = rootView.findViewById(R.id.harvsetSeason);
        qty_agregated_from_members = rootView.findViewById(R.id.qty_agregated_from_members);
        cycle_h_at_price_per_kg = rootView.findViewById(R.id.cycle_h_at_price_per_kg);
        qty_purchaced_from_non_members = rootView.findViewById(R.id.qty_purchaced_from_non_members);
        non_member_purchase_at_price_per_kg = rootView.findViewById(R.id.non_member_purchase_at_price_per_kg);
        rgcc_contact_under_ftma = rootView.findViewById(R.id.rgcc_contact_under_ftma);
        qty_of_rgcc_contact = rootView.findViewById(R.id.qty_of_rgcc_contact);
        qty_sold_to_rgcc = rootView.findViewById(R.id.qty_sold_to_rgcc);
        price_per_kg_sold_to_rgcc = rootView.findViewById(R.id.price_per_kg_sold_to_rgcc);
        qty_slod_outside_rgcc = rootView.findViewById(R.id.qty_slod_outside_rgcc);
        price_per_kg_sold_outside_ftma = rootView.findViewById(R.id.price_per_kg_sold_outside_ftma);


        formal_buyer = rootView.findViewById(R.id.formal_buyer);
        boolean isFormal_buyer = formal_buyer.isActivated();
        baselineSales.setFormalBuyer(isFormal_buyer);

        informal_buyer = rootView.findViewById(R.id.informal_buyer);
        boolean isInformal_buyer = informal_buyer.isActivated();
        baselineSales.setInformalBuyer(isInformal_buyer);

        other = rootView.findViewById(R.id.other);
        boolean isOther = other.isActivated();
        baselineSales.setOther(isOther);



        qty_agregated_from_members.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                baselineSales.setQtyAgregatedFromMember(Integer.parseInt(charSequence.toString()));
                mPage.setData("baselineSales", baselineSales);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        cycle_h_at_price_per_kg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                baselineSales.setCycleHarvsetAtPricePerKg(Integer.parseInt(charSequence.toString()));
                mPage.setData("baselineSales", baselineSales);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        qty_purchaced_from_non_members.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                baselineSales.setQtyPurchaseFromNonMember(Integer.parseInt(charSequence.toString()));
                mPage.setData("baselineSales", baselineSales);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        non_member_purchase_at_price_per_kg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                baselineSales.setNonMemberAtPricePerKg(Integer.parseInt(charSequence.toString()));
                mPage.setData("baselineSales", baselineSales);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        qty_of_rgcc_contact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                baselineSales.setQtyOfRgccContract(Double.parseDouble(charSequence.toString()));
                mPage.setData("baselineSales", baselineSales);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        qty_sold_to_rgcc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                baselineSales.setQtySoldToRgcc(Double.parseDouble(charSequence.toString()));
                mPage.setData("baselineSales", baselineSales);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        price_per_kg_sold_to_rgcc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                baselineSales.setPricePerKgSoldToRgcc(Integer.parseInt(charSequence.toString()));
                mPage.setData("baselineSales", baselineSales);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        qty_slod_outside_rgcc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                baselineSales.setQtySoldOutOfRgcc(Integer.parseInt(charSequence.toString()));
                mPage.setData("baselineSales", baselineSales);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        price_per_kg_sold_outside_ftma.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                baselineSales.setPricePerKkSoldOutFtma(Integer.parseInt(charSequence.toString()));
                mPage.setData("baselineSales", baselineSales);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //set default formal buyer
        formal_buyer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baselineSales.setFormalBuyer(true);
                } else {
                    baselineSales.setFormalBuyer(false);
                }
                mPage.getData().putParcelable("baselineSales", baselineSales);
            }
        });

        informal_buyer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baselineSales.setFormalBuyer(true);
                } else {
                    baselineSales.setFormalBuyer(false);
                }
                mPage.getData().putParcelable("baselineSales", baselineSales);
            }
        });

        other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baselineSales.setOther(true);
                } else {
                    baselineSales.setOther(false);
                }
                mPage.getData().putParcelable("baselineSales", baselineSales);
            }
        });

        //set default vice chair  gender

        //set default sec  gender

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (PageFragmentCallbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
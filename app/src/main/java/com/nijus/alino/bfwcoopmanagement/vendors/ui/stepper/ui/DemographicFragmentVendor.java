package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.PageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.DemographicVendor;

public class DemographicFragmentVendor extends Fragment {

    public static final String ARG_KEY = "key";
    private String mKey;
    private PageVendorVendor mPageVendor;
    private PageFragmentCallbacksVendor mCallbacks;

    private DemographicVendor demographicVendor = new DemographicVendor();

    private RadioGroup houseHold;
    private AutoCompleteTextView numHouseHoldMember;
    private AutoCompleteTextView sFirstName;
    private AutoCompleteTextView sLastName;
    private AutoCompleteTextView cellPhoneAlt;
    private AutoCompleteTextView cellCarrier;
    private AutoCompleteTextView memberShipId;

    public DemographicFragmentVendor() {
        super();
    }

    public static DemographicFragmentVendor create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        DemographicFragmentVendor fragment = new DemographicFragmentVendor();
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
        View rootView = inflater.inflate(R.layout.demographic_layout, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.demographic));

        houseHold = rootView.findViewById(R.id.houseHold);
        numHouseHoldMember = rootView.findViewById(R.id.num_household_member);
        sFirstName = rootView.findViewById(R.id.s_first_name);
        sLastName = rootView.findViewById(R.id.s_last_name);
        cellPhoneAlt = rootView.findViewById(R.id.cell_phone_alt);
        cellCarrier = rootView.findViewById(R.id.cell_carrier);
        memberShipId = rootView.findViewById(R.id.m_id);

        //set default household
        if (houseHold.getCheckedRadioButtonId() == R.id.household_y) {
            demographicVendor.setHouseHold(true);
            mPageVendor.getData().putParcelable("demographicVendor", demographicVendor);
        } else if (houseHold.getCheckedRadioButtonId() == R.id.household_n) {
            demographicVendor.setHouseHold(false);
            mPageVendor.getData().putParcelable("demographicVendor", demographicVendor);
        }

        //listen for change on Household Head
        houseHold.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.household_y) {
                    demographicVendor.setHouseHold(true);
                    mPageVendor.getData().putParcelable("demographicVendor", demographicVendor);
                } else if (i == R.id.household_n) {
                    demographicVendor.setHouseHold(false);
                    mPageVendor.getData().putParcelable("demographicVendor", demographicVendor);
                }
            }
        });

        numHouseHoldMember.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    demographicVendor.setHouseHoldMember(Integer.parseInt(charSequence.toString()));
                    mPageVendor.getData().putParcelable("demographicVendor", demographicVendor);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                demographicVendor.setSpouseFirstName(charSequence.toString());
                mPageVendor.getData().putParcelable("demographicVendor", demographicVendor);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                demographicVendor.setSpouseLastName(charSequence.toString());
                mPageVendor.getData().putParcelable("demographicVendor", demographicVendor);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cellPhoneAlt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                demographicVendor.setCellPhoneAlt(charSequence.toString());
                mPageVendor.getData().putParcelable("demographicVendor", demographicVendor);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cellCarrier.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                demographicVendor.setCellCarrier(charSequence.toString());
                mPageVendor.getData().putParcelable("demographicVendor", demographicVendor);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        memberShipId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                demographicVendor.setMemberShipId(charSequence.toString());
                mPageVendor.getData().putParcelable("demographicVendor", demographicVendor);
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
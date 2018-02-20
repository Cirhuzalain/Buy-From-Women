package com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.ui;

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
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.Demographic;

public class DemographicFragment extends Fragment {

    public static final String ARG_KEY = "key";
    private String mKey;
    private Page mPage;
    private PageFragmentCallbacks mCallbacks;

    private Demographic demographic = new Demographic();

    private RadioGroup houseHold;
    private AutoCompleteTextView numHouseHoldMember;
    private AutoCompleteTextView sFirstName;
    private AutoCompleteTextView sLastName;
    private AutoCompleteTextView cellPhoneAlt;
    private AutoCompleteTextView cellCarrier;

    public DemographicFragment() {
        super();
    }

    public static DemographicFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        DemographicFragment fragment = new DemographicFragment();
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
        View rootView = inflater.inflate(R.layout.demographic_layout, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.demographic));

        houseHold = rootView.findViewById(R.id.houseHold);
        numHouseHoldMember = rootView.findViewById(R.id.num_household_member);
        sFirstName = rootView.findViewById(R.id.s_first_name);
        sLastName = rootView.findViewById(R.id.s_last_name);
        cellPhoneAlt = rootView.findViewById(R.id.cell_phone_alt);
        cellCarrier = rootView.findViewById(R.id.cell_carrier);

        //set default household
        if (houseHold.getCheckedRadioButtonId() == R.id.household_y) {
            demographic.setHouseHold(true);
            mPage.getData().putParcelable("demographic", demographic);
        } else if (houseHold.getCheckedRadioButtonId() == R.id.household_n) {
            demographic.setHouseHold(false);
        }
        demographic.setHouseHoldMember(0);
        mPage.getData().putParcelable("demographic", demographic);

        //listen for change on Household Head
        houseHold.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.household_y) {
                    demographic.setHouseHold(true);
                    mPage.getData().putParcelable("demographic", demographic);
                } else if (i == R.id.household_n) {
                    demographic.setHouseHold(false);
                    mPage.getData().putParcelable("demographic", demographic);
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
                    demographic.setHouseHoldMember(Integer.parseInt(charSequence.toString()));
                    mPage.getData().putParcelable("demographic", demographic);
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
                demographic.setSpouseFirstName(charSequence.toString());
                mPage.getData().putParcelable("demographic", demographic);
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
                demographic.setSpouseLastName(charSequence.toString());
                mPage.getData().putParcelable("demographic", demographic);
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
                demographic.setCellPhoneAlt(charSequence.toString());
                mPage.getData().putParcelable("demographic", demographic);
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
                demographic.setCellCarrier(charSequence.toString());
                mPage.getData().putParcelable("demographic", demographic);
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
        mCallbacks = (PageFragmentCallbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
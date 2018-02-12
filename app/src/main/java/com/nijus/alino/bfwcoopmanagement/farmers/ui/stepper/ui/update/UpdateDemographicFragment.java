package com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.ui.update;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.Demographic;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.ui.PageFragmentCallbacks;

public class UpdateDemographicFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String ARG_KEY = "key";
    private String mKey;
    private Page mPage;
    private PageFragmentCallbacks mCallbacks;

    private Demographic demographic = new Demographic();

    private RadioGroup houseHold;
    private RadioButton radioY;
    private RadioButton radioN;
    private AutoCompleteTextView numHouseHoldMember;
    private AutoCompleteTextView sFirstName;
    private AutoCompleteTextView sLastName;
    private AutoCompleteTextView cellPhoneAlt;
    private AutoCompleteTextView cellCarrier;
    private AutoCompleteTextView memberShipId;
    private Uri mUri;
    private long mFarmerId;

    public UpdateDemographicFragment() {
        super();
    }

    public static UpdateDemographicFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UpdateDemographicFragment fragment = new UpdateDemographicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = mCallbacks.onGetPage(mKey);

        Intent intent = getActivity().getIntent();

        if (intent.hasExtra("farmerId")) {
            mFarmerId = intent.getLongExtra("farmerId", 0);
            mUri = BfwContract.Farmer.buildFarmerUri(mFarmerId);
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(1, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String farmerSelection = BfwContract.Farmer.TABLE_NAME + "." +
                BfwContract.Farmer._ID + " =  ? ";

        if (mUri != null) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    null,
                    farmerSelection,
                    new String[]{Long.toString(mFarmerId)},
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            int houseHoldHead = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_HOUSEHOLD_HEAD));
            int houseMember = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_HOUSE_MEMBER));
            String sFName = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_FIRST_NAME));
            String sLName = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_LAST_NAME));
            String celPhone = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_CELL_PHONE));
            String celCarrier = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_CELL_CARRIER));
            String mId = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_MEMBER_SHIP));

            if (houseHoldHead == 1) {
                radioY.setChecked(true);
            } else {
                radioN.setChecked(true);
            }
            setDefaultHouseHoldHead();

            String houseMemb = "" + houseMember + "";
            numHouseHoldMember.setText(houseMemb);
            demographic.setHouseHoldMember(houseMember);
            if (sFName != null) {
                sFirstName.setText(sFName);
                demographic.setSpouseFirstName(sFName);
            }
            if (sLName != null) {
                sLastName.setText(sLName);
                demographic.setSpouseLastName(sLName);
            }
            if (celPhone != null) {
                cellPhoneAlt.setText(celPhone);
                demographic.setCellPhoneAlt(celPhone);
            }
            if (celCarrier != null) {
                cellCarrier.setText(celCarrier);
                demographic.setCellCarrier(celCarrier);
            }
            if (mId != null) {
                memberShipId.setText(mId);
                demographic.setMemberShipId(mId);
            }
            mPage.getData().putParcelable("demographic", demographic);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void setDefaultHouseHoldHead() {
        if (houseHold.getCheckedRadioButtonId() == R.id.household_y) {
            demographic.setHouseHold(true);
            mPage.getData().putParcelable("demographic", demographic);
        } else if (houseHold.getCheckedRadioButtonId() == R.id.household_n) {
            demographic.setHouseHold(false);
            mPage.getData().putParcelable("demographic", demographic);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.demographic_layout, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.demographic));

        houseHold = rootView.findViewById(R.id.houseHold);
        radioN = rootView.findViewById(R.id.household_n);
        radioY = rootView.findViewById(R.id.household_y);
        numHouseHoldMember = rootView.findViewById(R.id.num_household_member);
        sFirstName = rootView.findViewById(R.id.s_first_name);
        sLastName = rootView.findViewById(R.id.s_last_name);
        cellPhoneAlt = rootView.findViewById(R.id.cell_phone_alt);
        cellCarrier = rootView.findViewById(R.id.cell_carrier);
        memberShipId = rootView.findViewById(R.id.m_id);

        //set default household
        setDefaultHouseHoldHead();

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

        memberShipId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                demographic.setMemberShipId(charSequence.toString());
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

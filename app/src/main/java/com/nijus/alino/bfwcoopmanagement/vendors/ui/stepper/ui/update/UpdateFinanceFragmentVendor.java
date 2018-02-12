package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui.update;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.PageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.FinanceVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui.PageFragmentCallbacksVendor;

public class UpdateFinanceFragmentVendor extends Fragment implements AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {
    public static final String ARG_KEY = "key";
    private String mKey;
    private PageVendorVendor mPageVendor;
    private PageFragmentCallbacksVendor mCallbacks;
    private Uri mUri;
    private long mFarmerId;

    private FinanceVendor financeVendor = new FinanceVendor();

    private CheckBox outstandingLoan;
    private AutoCompleteTextView totLoanAmount;
    private AutoCompleteTextView totOutstanding;
    private AutoCompleteTextView interestRate;
    private AutoCompleteTextView duration;
    private Spinner loanProvider;
    private CheckBox mobileMoneyAccount;
    private CheckBox inPut;
    private CheckBox aggregation;
    private CheckBox other;

    public UpdateFinanceFragmentVendor() {

    }

    public static UpdateFinanceFragmentVendor create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UpdateFinanceFragmentVendor fragment = new UpdateFinanceFragmentVendor();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPageVendor = mCallbacks.onGetPage(mKey);

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
            int lOutstandingLoan = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_OUTSANDING_LOAN));
            int lTotLoanAmount = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_TOT_LOAN_AMOUNT));
            int lTotOutstanding = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_TOT_OUTSTANDING));
            double lInterestRate = data.getDouble(data.getColumnIndex(BfwContract.Farmer.COLUMN_INTEREST_RATE));
            int lDuration = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_DURATION));
            String lProvider = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_LOAN_PROVIDER));
            int isMMAccount = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_MOBILE_MONEY_ACCOUNT));
            int lInput = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_LP_INPUT));
            int lAggregation = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_LP_AGGREG));
            int lOther = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_LP_OTHER));

            if (lOutstandingLoan == 1) {
                outstandingLoan.setChecked(true);
                financeVendor.setOutstandingLoan(true);
            } else {
                financeVendor.setOutstandingLoan(false);
            }

            if (isMMAccount == 1) {
                mobileMoneyAccount.setChecked(true);
                financeVendor.setHasMobileMoneyAccount(true);
            } else {
                financeVendor.setHasMobileMoneyAccount(false);
            }

            if (lInput == 1) {
                inPut.setChecked(true);
                financeVendor.setInput(true);
            } else {
                financeVendor.setInput(false);
            }

            if (lAggregation == 1) {
                inPut.setChecked(true);
                financeVendor.setAggregation(true);
            } else {
                financeVendor.setAggregation(false);
            }

            if (lOther == 1) {
                other.setChecked(true);
                financeVendor.setOtherLp(true);
            } else {
                financeVendor.setOtherLp(false);
            }

            totLoanAmount.setText("" + lTotLoanAmount + "");
            totOutstanding.setText("" + lTotOutstanding + "");
            interestRate.setText("" + lInterestRate + "");
            duration.setText("" + lDuration + "");
            financeVendor.setTotLoanAmount(lTotLoanAmount);
            financeVendor.setTotOutstanding(lTotOutstanding);
            financeVendor.setInterestRate(lInterestRate);
            financeVendor.setDurationInMonth(lDuration);

            setSpinnerItemByName(loanProvider, lProvider);
            mPageVendor.setData("financeVendor", financeVendor);
        }
    }

    public void setSpinnerItemByName(Spinner spinner, String lpValue) {
        int spinnerCount = spinner.getCount();
        for (int i = 0; i < spinnerCount; i++) {
            String value = (String) spinner.getItemAtPosition(i);
            if (value.equals(lpValue)) {
                spinner.setSelection(i);
                financeVendor.setLoanProvider(value);
                mPageVendor.setData("financeVendor", financeVendor);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.finance_layout, container, false);
        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.finance));

        outstandingLoan = rootView.findViewById(R.id.outstanding_loan);

        totLoanAmount = rootView.findViewById(R.id.tot_loan_amount);
        totOutstanding = rootView.findViewById(R.id.tot_outstanding);
        interestRate = rootView.findViewById(R.id.interest_rate);
        duration = rootView.findViewById(R.id.duration_month);
        loanProvider = rootView.findViewById(R.id.loan_provider);

        mobileMoneyAccount = rootView.findViewById(R.id.mobile_m_account);
        inPut = rootView.findViewById(R.id.l_input);
        aggregation = rootView.findViewById(R.id.l_aggregation);
        other = rootView.findViewById(R.id.l_other);

        //set default outstanding loan
        boolean isOutstandingLoan = outstandingLoan.isChecked();
        financeVendor.setOutstandingLoan(isOutstandingLoan);

        //set default mobile money account and attach a listener
        boolean isMobileMoney = mobileMoneyAccount.isChecked();
        financeVendor.setHasMobileMoneyAccount(isMobileMoney);

        //set default input
        boolean isInput = inPut.isChecked();
        financeVendor.setInput(isInput);

        //set default aggregation
        boolean isAggregation = aggregation.isChecked();
        financeVendor.setAggregation(isAggregation);

        //set other
        boolean isOther = other.isChecked();
        financeVendor.setOtherLp(isOther);

        mPageVendor.getData().putParcelable("financeVendor", financeVendor);

        outstandingLoan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    financeVendor.setOutstandingLoan(true);
                } else {
                    financeVendor.setOutstandingLoan(false);
                }
                mPageVendor.getData().putParcelable("financeVendor", financeVendor);
            }
        });

        mobileMoneyAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    financeVendor.setHasMobileMoneyAccount(true);
                } else {
                    financeVendor.setHasMobileMoneyAccount(false);
                }
                mPageVendor.getData().putParcelable("financeVendor", financeVendor);
            }
        });

        inPut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    financeVendor.setInput(true);
                } else {
                    financeVendor.setInput(false);
                }
                mPageVendor.getData().putParcelable("financeVendor", financeVendor);
            }
        });

        aggregation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    financeVendor.setInput(true);
                } else {
                    financeVendor.setInput(false);
                }
                mPageVendor.getData().putParcelable("financeVendor", financeVendor);
            }
        });


        other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    financeVendor.setOtherLp(true);
                } else {
                    financeVendor.setOtherLp(false);
                }
                mPageVendor.getData().putParcelable("financeVendor", financeVendor);

            }
        });

        totLoanAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    financeVendor.setTotLoanAmount(Integer.parseInt(charSequence.toString()));
                    mPageVendor.getData().putParcelable("financeVendor", financeVendor);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        totOutstanding.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    financeVendor.setTotOutstanding(Integer.parseInt(charSequence.toString()));
                    mPageVendor.getData().putParcelable("financeVendor", financeVendor);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        interestRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    financeVendor.setInterestRate(Double.parseDouble(charSequence.toString()));
                    mPageVendor.getData().putParcelable("financeVendor", financeVendor);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        duration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    financeVendor.setDurationInMonth(Integer.parseInt(charSequence.toString()));
                    mPageVendor.getData().putParcelable("financeVendor", financeVendor);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.loan_provider, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loanProvider.setAdapter(adapter);

        loanProvider.setOnItemSelectedListener(this);

        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        financeVendor.setLoanProvider(adapterView.getItemAtPosition(i).toString());
        mPageVendor.getData().putParcelable("financeVendor", financeVendor);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

package com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.ui.update;


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
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.Finance;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.ui.PageFragmentCallbacks;

public class UpdateFinanceFragment extends Fragment implements AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {
    public static final String ARG_KEY = "key";
    private String mKey;
    private Page mPage;
    private PageFragmentCallbacks mCallbacks;
    private Uri mUri;
    private long mFarmerId;

    private Finance finance = new Finance();

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

    public UpdateFinanceFragment() {

    }

    public static UpdateFinanceFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UpdateFinanceFragment fragment = new UpdateFinanceFragment();
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
            int lOutstandingLoan = 0;
            int lTotLoanAmount = 0;
            int lTotOutstanding = 0;
            double lInterestRate = 0;
            int lDuration = 0;
            String lProvider = "";
            int isMMAccount  = 0;
            int lInput = 0;
            int lAggregation = 0;
            int lOther = 0;

            if (lOutstandingLoan == 1) {
                outstandingLoan.setChecked(true);
                finance.setOutstandingLoan(true);
            } else {
                finance.setOutstandingLoan(false);
            }

            if (isMMAccount == 1) {
                mobileMoneyAccount.setChecked(true);
                finance.setHasMobileMoneyAccount(true);
            } else {
                finance.setHasMobileMoneyAccount(false);
            }

            if (lInput == 1) {
                inPut.setChecked(true);
                finance.setInput(true);
            } else {
                finance.setInput(false);
            }

            if (lAggregation == 1) {
                inPut.setChecked(true);
                finance.setAggregation(true);
            } else {
                finance.setAggregation(false);
            }

            if (lOther == 1) {
                other.setChecked(true);
                finance.setOtherLp(true);
            } else {
                finance.setOtherLp(false);
            }

            totLoanAmount.setText("" + lTotLoanAmount + "");
            totOutstanding.setText("" + lTotOutstanding + "");
            interestRate.setText("" + lInterestRate + "");
            duration.setText("" + lDuration + "");
            finance.setTotLoanAmount(lTotLoanAmount);
            finance.setTotOutstanding(lTotOutstanding);
            finance.setInterestRate(lInterestRate);
            finance.setDurationInMonth(lDuration);

            setSpinnerItemByName(loanProvider, lProvider);
            mPage.setData("finance", finance);
        }
    }

    public void setSpinnerItemByName(Spinner spinner, String lpValue) {
        int spinnerCount = spinner.getCount();
        for (int i = 0; i < spinnerCount; i++) {
            String value = (String) spinner.getItemAtPosition(i);
            if (value.equals(lpValue)) {
                spinner.setSelection(i);
                finance.setLoanProvider(value);
                mPage.setData("finance", finance);
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
        finance.setOutstandingLoan(isOutstandingLoan);

        //set default mobile money account and attach a listener
        boolean isMobileMoney = mobileMoneyAccount.isChecked();
        finance.setHasMobileMoneyAccount(isMobileMoney);

        //set default input
        boolean isInput = inPut.isChecked();
        finance.setInput(isInput);

        //set default aggregation
        boolean isAggregation = aggregation.isChecked();
        finance.setAggregation(isAggregation);

        //set other
        boolean isOther = other.isChecked();
        finance.setOtherLp(isOther);

        mPage.getData().putParcelable("finance", finance);

        outstandingLoan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    finance.setOutstandingLoan(true);
                } else {
                    finance.setOutstandingLoan(false);
                }
                mPage.getData().putParcelable("finance", finance);
            }
        });

        mobileMoneyAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    finance.setHasMobileMoneyAccount(true);
                } else {
                    finance.setHasMobileMoneyAccount(false);
                }
                mPage.getData().putParcelable("finance", finance);
            }
        });

        inPut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    finance.setInput(true);
                } else {
                    finance.setInput(false);
                }
                mPage.getData().putParcelable("finance", finance);
            }
        });

        aggregation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    finance.setInput(true);
                } else {
                    finance.setInput(false);
                }
                mPage.getData().putParcelable("finance", finance);
            }
        });


        other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    finance.setOtherLp(true);
                } else {
                    finance.setOtherLp(false);
                }
                mPage.getData().putParcelable("finance", finance);

            }
        });

        totLoanAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    finance.setTotLoanAmount(Integer.parseInt(charSequence.toString()));
                    mPage.getData().putParcelable("finance", finance);
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
                    finance.setTotOutstanding(Integer.parseInt(charSequence.toString()));
                    mPage.getData().putParcelable("finance", finance);
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
                    finance.setInterestRate(Double.parseDouble(charSequence.toString()));
                    mPage.getData().putParcelable("finance", finance);
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
                    finance.setDurationInMonth(Integer.parseInt(charSequence.toString()));
                    mPage.getData().putParcelable("finance", finance);
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
        finance.setLoanProvider(adapterView.getItemAtPosition(i).toString());
        mPage.getData().putParcelable("finance", finance);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

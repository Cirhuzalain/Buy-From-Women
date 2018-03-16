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
import android.support.v4.widget.SimpleCursorAdapter;
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

import java.util.HashMap;

public class UpdateFinanceFragment extends Fragment implements AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {
    public static final String ARG_KEY = "key";
    private String mKey;
    private Page mPage;
    private PageFragmentCallbacks mCallbacks;
    private Uri mUri;
    private int mFarmerId;

    private Cursor cursor;
    private String seasonName;
    private int seasonId;
    private HashMap<String, Finance> seasonFinance = new HashMap<>();

    private boolean isDataAvailable;

    private Finance finance = new Finance();
    private AutoCompleteTextView totLoanAmount;
    private AutoCompleteTextView totOutstanding;
    private AutoCompleteTextView interestRate;
    private AutoCompleteTextView duration;
    private Spinner loanProvider;
    private Spinner harvsetSeason;
    private CheckBox mobileMoneyAccount;
    private CheckBox inPut;
    private CheckBox aggregation;
    private CheckBox other;
    private CheckBox outstandingLoan;

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
            mFarmerId = intent.getIntExtra("farmerId", 0);
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(1, null, this);
        super.onActivityCreated(savedInstanceState);
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

        harvsetSeason = rootView.findViewById(R.id.harvsetSeason);
        harvsetSeason.setOnItemSelectedListener(this);
        populateSpinner();

        cursor = (Cursor) harvsetSeason.getSelectedItem();
        seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

        finance.setHarvestSeason(seasonId);
        finance.setTotLoanAmount(0.0);
        finance.setTotOutstanding(0.0);
        finance.setInterestRate(0.0);
        finance.setDurationInMonth(0);


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

        seasonFinance.put(seasonName, finance);

        mPage.getData().putSerializable("finance", seasonFinance);

        outstandingLoan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvsetSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                // set season and default value for checkbox listener

                if (b) {
                    if (seasonFinance.containsKey(seasonName)) {
                        seasonFinance.get(seasonName).setOutstandingLoan(true);
                    } else {
                        Finance finance = new Finance();
                        finance.setHarvestSeason(seasonId);
                        finance.setOutstandingLoan(true);
                        seasonFinance.put(seasonName, finance);
                    }
                } else {
                    if (seasonFinance.containsKey(seasonName)) {
                        seasonFinance.get(seasonName).setOutstandingLoan(false);
                    } else {
                        Finance finance = new Finance();
                        finance.setOutstandingLoan(false);
                        seasonFinance.put(seasonName, finance);
                    }
                }
                mPage.getData().putSerializable("finance", seasonFinance);
            }
        });

        mobileMoneyAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cursor = (Cursor) harvsetSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                if (b) {

                    if (seasonFinance.containsKey(seasonName)) {
                        seasonFinance.get(seasonName).setHasMobileMoneyAccount(true);
                    } else {
                        Finance finance = new Finance();
                        finance.setHasMobileMoneyAccount(true);
                        finance.setHarvestSeason(seasonId);
                        seasonFinance.put(seasonName, finance);
                    }
                } else {
                    if (seasonFinance.containsKey(seasonName)) {
                        seasonFinance.get(seasonName).setHasMobileMoneyAccount(false);
                    } else {
                        Finance finance = new Finance();
                        finance.setHasMobileMoneyAccount(false);
                        finance.setHarvestSeason(seasonId);
                        seasonFinance.put(seasonName, finance);
                    }
                }
                mPage.getData().putSerializable("finance", seasonFinance);
            }
        });

        inPut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cursor = (Cursor) harvsetSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                if (b) {
                    if (seasonFinance.containsKey(seasonName)) {
                        seasonFinance.get(seasonName).setInput(true);
                    } else {
                        Finance finance = new Finance();
                        finance.setHarvestSeason(seasonId);
                        finance.setInput(true);
                        seasonFinance.put(seasonName, finance);
                    }
                } else {
                    if (seasonFinance.containsKey(seasonName)) {
                        seasonFinance.get(seasonName).setInput(false);
                    } else {
                        Finance finance = new Finance();
                        finance.setInput(false);
                        finance.setHarvestSeason(seasonId);
                        seasonFinance.put(seasonName, finance);
                    }
                }
                mPage.getData().putSerializable("finance", seasonFinance);
            }
        });

        aggregation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvsetSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {

                    if (seasonFinance.containsKey(seasonName)) {
                        seasonFinance.get(seasonName).setAggregation(true);
                    } else {
                        Finance finance = new Finance();
                        finance.setAggregation(true);
                        finance.setHarvestSeason(seasonId);
                        seasonFinance.put(seasonName, finance);
                    }
                } else {
                    if (seasonFinance.containsKey(seasonName)) {
                        seasonFinance.get(seasonName).setAggregation(false);
                    } else {
                        Finance finance = new Finance();
                        finance.setAggregation(false);
                        finance.setHarvestSeason(seasonId);
                        seasonFinance.put(seasonName, finance);
                    }
                }
                mPage.getData().putSerializable("finance", seasonFinance);
            }
        });


        other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvsetSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {

                    if (seasonFinance.containsKey(seasonName)) {
                        seasonFinance.get(seasonName).setOtherLp(true);
                    } else {
                        Finance finance = new Finance();
                        finance.setOtherLp(true);
                        finance.setHarvestSeason(seasonId);
                        seasonFinance.put(seasonName, finance);
                    }
                } else {
                    if (seasonFinance.containsKey(seasonName)) {
                        seasonFinance.get(seasonName).setOtherLp(false);
                    } else {
                        Finance finance = new Finance();
                        finance.setOtherLp(false);
                        finance.setHarvestSeason(seasonId);
                        seasonFinance.put(seasonName, finance);
                    }
                }
                mPage.getData().putSerializable("finance", seasonFinance);

            }
        });

        totLoanAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {

                    cursor = (Cursor) harvsetSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));


                    if (seasonFinance.containsKey(seasonName)) {
                        seasonFinance.get(seasonName).setTotLoanAmount(Double.parseDouble(charSequence.toString()));
                    } else {
                        Finance finance = new Finance();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        finance.setHarvestSeason(seasonId);
                        finance.setTotLoanAmount(Double.parseDouble(charSequence.toString()));
                        seasonFinance.put(seasonName, finance);
                    }
                    mPage.getData().putSerializable("finance", seasonFinance);
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

                    cursor = (Cursor) harvsetSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                    if (seasonFinance.containsKey(seasonName)) {
                        seasonFinance.get(seasonName).setTotOutstanding(Double.parseDouble(charSequence.toString()));
                    } else {
                        Finance finance = new Finance();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        finance.setHarvestSeason(seasonId);
                        finance.setTotOutstanding(Double.parseDouble(charSequence.toString()));
                        seasonFinance.put(seasonName, finance);
                    }
                    mPage.getData().putSerializable("finance", seasonFinance);

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

                    cursor = (Cursor) harvsetSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                    if (seasonFinance.containsKey(seasonName)) {
                        seasonFinance.get(seasonName).setInterestRate(Double.parseDouble(charSequence.toString()));
                    } else {
                        Finance finance = new Finance();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        finance.setHarvestSeason(seasonId);
                        finance.setInterestRate(Double.parseDouble(charSequence.toString()));
                        seasonFinance.put(seasonName, finance);
                    }

                    mPage.getData().putSerializable("finance", seasonFinance);
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

                    cursor = (Cursor) harvsetSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                    if (seasonFinance.containsKey(seasonName)) {
                        seasonFinance.get(seasonName).setDurationInMonth(Integer.parseInt(charSequence.toString()));
                    } else {
                        Finance finance = new Finance();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        finance.setHarvestSeason(seasonId);
                        finance.setDurationInMonth(Integer.parseInt(charSequence.toString()));
                        seasonFinance.put(seasonName, finance);
                    }

                    mPage.getData().putSerializable("finance", seasonFinance);
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

        loanProvider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                cursor = (Cursor) harvsetSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                if (seasonFinance.containsKey(seasonName)) {
                    seasonFinance.get(seasonName).setLoanProvider(adapterView.getItemAtPosition(i).toString());
                } else {
                    Finance finance = new Finance();
                    seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                    finance.setHarvestSeason(seasonId);
                    finance.setLoanProvider(adapterView.getItemAtPosition(i).toString());
                    seasonFinance.put(seasonName, finance);
                }
                mPage.getData().putSerializable("finance", seasonFinance);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String farmerSelection = BfwContract.Farmer.TABLE_NAME + "." +
                BfwContract.Farmer._ID + " =  ? ";

        mUri = BfwContract.FinanceDataFarmer.buildFinanceDataFarmerUri(mFarmerId);

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
        if (data != null) {
            int lOutstandingLoan;
            double lTotLoanAmount;
            double lTotOutstanding;
            double lInterestRate;
            int lDuration;
            String lProvider;
            int isMMAccount;
            int lInput;
            int lAggregation;
            int lOther;
            int seasonId;
            int financeId;
            String seasonName;

            Cursor seasonCursor = null;
            String farmerSelection = BfwContract.HarvestSeason.TABLE_NAME
                    + "." + BfwContract.HarvestSeason._ID
                    + " = ?";

            while (data.moveToNext()) {

                seasonId = data.getInt(data.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_SEASON_ID));
                financeId = data.getInt(data.getColumnIndex(BfwContract.FinanceDataFarmer._ID));
                lOutstandingLoan = data.getInt(data.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_OUTSANDING_LOAN));
                lTotLoanAmount = data.getDouble(data.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_TOT_LOAN_AMOUNT));
                lTotOutstanding = data.getDouble(data.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_TOT_OUTSTANDING));
                lInterestRate = data.getDouble(data.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_INTEREST_RATE));
                lDuration = data.getInt(data.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_DURATION));
                lProvider = data.getString(data.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_LOAN_PROVIDER));
                isMMAccount = data.getInt(data.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_MOBILE_MONEY_ACCOUNT));
                lInput = data.getInt(data.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_LOANPROVIDER_INPUT));
                lAggregation = data.getInt(data.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_LOANPROVIDER_AGGREG));
                lOther = data.getInt(data.getColumnIndex(BfwContract.FinanceDataFarmer.COLUMN_LOANPROVIDER_OTHER));

                try {
                    seasonCursor = getActivity().getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, farmerSelection,
                            new String[]{Integer.toString(seasonId)}, null);

                    if (seasonCursor != null) {
                        seasonCursor.moveToFirst();
                        seasonName = seasonCursor.getString(seasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                        finance = new Finance(lOutstandingLoan == 1, isMMAccount == 1, lInput == 1,
                                lAggregation == 1, lOther == 1, lTotOutstanding, lInterestRate, lDuration, lProvider, lTotLoanAmount, seasonId);
                        finance.setFinanceId(financeId);
                        seasonFinance.put(seasonName, finance);
                        isDataAvailable = true;
                    }
                } finally {
                    if (seasonCursor != null) {
                        seasonCursor.close();
                    }
                }
            }
            //set field to default value value inside the spinner
            cursor = (Cursor) harvsetSeason.getSelectedItem();
            setFarmerFinanceItem(cursor);
            mPage.getData().putSerializable("finance", seasonFinance);
        }
    }

    private void setFarmerFinanceItem(Cursor cursor) {
        seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

        if (isDataAvailable && seasonFinance.containsKey(seasonName)) {

            boolean isOutstandingLoan = seasonFinance.get(seasonName).isOutstandingLoan();
            boolean hasMobileMoneyAccount = seasonFinance.get(seasonName).isHasMobileMoneyAccount();

            boolean isInput = seasonFinance.get(seasonName).isInput();
            boolean isAggregation = seasonFinance.get(seasonName).isAggregation();
            boolean isOtherLp = seasonFinance.get(seasonName).isOtherLp();

            String loanAmount = seasonFinance.get(seasonName).getTotLoanAmount() + "";
            String outstanding = seasonFinance.get(seasonName).getTotOutstanding() + "";
            String interestrate = seasonFinance.get(seasonName).getInterestRate() + "";
            String durationInMonth = seasonFinance.get(seasonName).getDurationInMonth() + "";
            String lProvider = seasonFinance.get(seasonName).getLoanProvider();

            outstandingLoan.setChecked(isOutstandingLoan);
            mobileMoneyAccount.setChecked(hasMobileMoneyAccount);
            inPut.setChecked(isInput);
            other.setChecked(isOtherLp);
            aggregation.setChecked(isAggregation);

            totLoanAmount.setText(loanAmount);
            totOutstanding.setText(outstanding);
            interestRate.setText(interestrate);
            duration.setText(durationInMonth);

            setSpinnerItemByName(loanProvider, lProvider, seasonName);


        } else {

            String loanAmount = "";
            String outstanding = "";
            String interestrate = "";
            String durationInMonth = "";

            outstandingLoan.setChecked(false);
            mobileMoneyAccount.setChecked(false);
            inPut.setChecked(false);
            other.setChecked(false);
            aggregation.setChecked(false);

            totLoanAmount.setText(loanAmount);
            totOutstanding.setText(outstanding);
            interestRate.setText(interestrate);
            duration.setText(durationInMonth);
        }
    }

    public void setSpinnerItemByName(Spinner spinner, String lpValue, String seasonName) {
        int spinnerCount = spinner.getCount();
        for (int i = 0; i < spinnerCount; i++) {
            String value = (String) spinner.getItemAtPosition(i);
            if (value.equals(lpValue)) {
                spinner.setSelection(i);
                if (seasonFinance.containsKey(seasonName)) {
                    finance.setLoanProvider(value);
                    seasonFinance.get(seasonName).setLoanProvider(value);
                } else {
                    Finance finance = new Finance();
                    finance.setLoanProvider(value);
                    seasonFinance.put(seasonName, finance);
                }

                mPage.getData().putSerializable("finance", seasonFinance);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


    public void populateSpinner() {
        String[] fromColumns = {BfwContract.HarvestSeason.COLUMN_NAME};

        // View IDs to map the columns (fetched above) into
        int[] toViews = {
                android.R.id.text1
        };
        Cursor cursor = getActivity().getContentResolver().query(
                BfwContract.HarvestSeason.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (cursor != null) {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    getContext(), // context
                    android.R.layout.simple_spinner_item, // layout file
                    cursor, // DB cursor
                    fromColumns, // data to bind to the UI
                    toViews, // views that'll represent the data from `fromColumns`
                    0
            );

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Create the list view and bind the adapter
            harvsetSeason.setAdapter(adapter);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Cursor cursor = (Cursor) harvsetSeason.getSelectedItem();
        setFarmerFinanceItem(cursor);
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

package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.BaselineFinanceInfo;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;

import java.util.HashMap;

public class BaselineFinanceFragment extends Fragment {

    public static final String ARG_KEY = "key";
    public final String LOG_TAG = BaselineFinanceFragment.class.getSimpleName();
    private String mKey;

    private BaselineFinanceInfo baslineFinanceInfo = new BaselineFinanceInfo();

    private Cursor cursor;
    private String seasonName;
    private int seasonId;

    private HashMap<String, BaselineFinanceInfo> baselineFinanceInfoSeason = new HashMap<>();

    private Page mPage;
    private PageFragmentCallbacks mCallbacks;


    private Spinner harvestSeason;
    private Spinner inputLoan;

    private CheckBox input_loan_prov_bank;
    private CheckBox input_loan_prov_cooperative;
    private CheckBox input_loan_prov_sacco;
    private CheckBox input_loan_prov_other;

    private AutoCompleteTextView input_loan_amount;
    private AutoCompleteTextView input_loan_interest_rate;
    private AutoCompleteTextView input_loan_duration;

    private CheckBox input_loan_purpose_labour;
    private CheckBox input_loan_purpose_seed;
    private CheckBox input_loan_purpose_input;
    private CheckBox input_loan_purpose_machinery;
    private CheckBox input_loan_purpose_other;

    private RadioGroup input_loan_disbursement_method;

    private Spinner aggrgation_post_harvset_loan;

    private CheckBox agg_post_harv_loan_prov_bank;
    private CheckBox agg_post_harv_loan_prov_cooperative;
    private CheckBox agg_post_harv_loan_prov_sacco;
    private CheckBox agg_post_harv_loan_prov_other;

    private AutoCompleteTextView aggrgation_post_harvset_amount;
    private AutoCompleteTextView aggrgation_post_harvset_loan_interest;
    private AutoCompleteTextView aggrgation_post_harvset_loan_duration;

    private CheckBox agg_post_harv_loan_purpose_labour;
    private CheckBox agg_post_harv_loan_purpose_input;
    private CheckBox agg_post_harv_loan_purpose_machinery;
    private CheckBox agg_post_harv_loan_purpose_other;

    private AutoCompleteTextView aggrgation_post_harvset_laon_disbursement_method;


    public BaselineFinanceFragment() {
        super();
    }

    public static BaselineFinanceFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        BaselineFinanceFragment fragment = new BaselineFinanceFragment();
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
        View rootView = inflater.inflate(R.layout.baseline_finance_info, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.baseline_fin_info));

        harvestSeason = rootView.findViewById(R.id.harvestSeason);
        inputLoan = rootView.findViewById(R.id.input_loan);
        aggrgation_post_harvset_loan = rootView.findViewById(R.id.aggrgation_post_harvset_loan);

        populateSpinner();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.rgcc_contract, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        inputLoan.setAdapter(adapter);
        aggrgation_post_harvset_loan.setAdapter(adapter);

        aggrgation_post_harvset_loan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                    baselineFinanceInfoSeason.get(seasonName).setAggrgation_post_harvset_loan(adapterView.getItemAtPosition(i).toString());
                } else {
                    BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                    seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                    baselineFinanceInfo.setSeasonId(seasonId);
                    baselineFinanceInfo.setAggrgation_post_harvset_loan(adapterView.getItemAtPosition(i).toString());
                    baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                }
                mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        inputLoan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                    baselineFinanceInfoSeason.get(seasonName).setInput_loan(adapterView.getItemAtPosition(i).toString());
                } else {
                    BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                    seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                    baselineFinanceInfo.setSeasonId(seasonId);
                    baselineFinanceInfo.setInput_loan(adapterView.getItemAtPosition(i).toString());
                    baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                }
                mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        input_loan_prov_bank = rootView.findViewById(R.id.input_loan_prov_bank);
        input_loan_prov_bank.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setInput_loan_prov_bank(true);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setInput_loan_prov_bank(true);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                } else {
                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setInput_loan_prov_bank(false);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setInput_loan_prov_bank(false);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                }
                mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);
            }
        });
        boolean isInput_loan_prov_bank = input_loan_prov_bank.isActivated();
        baslineFinanceInfo.setInput_loan_prov_bank(isInput_loan_prov_bank);

        input_loan_prov_cooperative = rootView.findViewById(R.id.input_loan_prov_cooperative);
        input_loan_prov_cooperative.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setInput_loan_prov_cooperative(true);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setInput_loan_prov_cooperative(true);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                } else {
                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setInput_loan_prov_cooperative(false);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setInput_loan_prov_cooperative(false);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                }
                mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);
            }
        });

        boolean isInput_loan_prov_cooperative = input_loan_prov_cooperative.isActivated();
        baslineFinanceInfo.setInput_loan_prov_cooperative(isInput_loan_prov_cooperative);

        input_loan_prov_sacco = rootView.findViewById(R.id.input_loan_prov_sacco);
        input_loan_prov_sacco.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setInput_loan_prov_sacco(true);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setInput_loan_prov_sacco(true);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                } else {
                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setInput_loan_prov_sacco(false);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setInput_loan_prov_sacco(false);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                }
                mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);
            }
        });


        boolean isInput_loan_prov_sacco = input_loan_prov_sacco.isActivated();
        baslineFinanceInfo.setInput_loan_prov_sacco(isInput_loan_prov_sacco);

        input_loan_prov_other = rootView.findViewById(R.id.input_loan_prov_other);
        input_loan_prov_other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setInput_loan_prov_other(true);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setInput_loan_prov_other(true);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                } else {
                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setInput_loan_prov_other(false);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setInput_loan_prov_other(false);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                }
                mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);

            }
        });

        boolean isinput_loan_prov_other = input_loan_prov_other.isActivated();
        baslineFinanceInfo.setInput_loan_prov_other(isinput_loan_prov_other);

        input_loan_amount = rootView.findViewById(R.id.input_loan_amount);
        input_loan_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {
                    cursor = (Cursor) harvestSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setInput_loan_amount(Double.parseDouble(charSequence.toString()));
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfo.setInput_loan_amount(Double.parseDouble(charSequence.toString()));
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }
                    mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);

                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        input_loan_interest_rate = rootView.findViewById(R.id.input_loan_interest_rate);
        input_loan_interest_rate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {
                    cursor = (Cursor) harvestSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setInput_loan_interest_rate(Double.parseDouble(charSequence.toString()));
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfo.setInput_loan_interest_rate(Double.parseDouble(charSequence.toString()));
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }
                    mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);

                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        input_loan_duration = rootView.findViewById(R.id.input_loan_duration);
        input_loan_duration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {
                    cursor = (Cursor) harvestSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setInput_loan_duration(Integer.parseInt(charSequence.toString()));
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfo.setInput_loan_duration(Integer.parseInt(charSequence.toString()));
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }
                    mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);

                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        input_loan_purpose_labour = rootView.findViewById(R.id.input_loan_purpose_labour);

        input_loan_purpose_labour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setsInput_loan_purpose_labour(true);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setsInput_loan_purpose_labour(true);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                } else {
                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setsInput_loan_purpose_labour(false);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setsInput_loan_purpose_labour(false);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                }
                mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);

            }
        });

        boolean isinput_loan_purpose_labour = input_loan_purpose_labour.isActivated();
        baslineFinanceInfo.setsInput_loan_purpose_labour(isinput_loan_purpose_labour);

        input_loan_purpose_seed = rootView.findViewById(R.id.input_loan_purpose_seed);
        input_loan_purpose_seed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setsInput_loan_purpose_seed(true);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setsInput_loan_purpose_seed(true);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                } else {
                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setsInput_loan_purpose_seed(false);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setsInput_loan_purpose_seed(false);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                }
                mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);

            }
        });
        boolean isinput_loan_purpose_seed = input_loan_purpose_seed.isActivated();
        baslineFinanceInfo.setsInput_loan_purpose_seed(isinput_loan_purpose_seed);

        input_loan_purpose_input = rootView.findViewById(R.id.input_loan_purpose_input);

        input_loan_purpose_input.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setsInput_loan_purpose_input(true);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setsInput_loan_purpose_input(true);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                } else {
                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setsInput_loan_purpose_input(false);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setsInput_loan_purpose_input(false);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                }
                mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);

            }
        });

        boolean isinput_loan_purpose_input = input_loan_purpose_input.isActivated();
        baslineFinanceInfo.setsInput_loan_purpose_input(isinput_loan_purpose_input);

        input_loan_purpose_machinery = rootView.findViewById(R.id.input_loan_purpose_machinery);

        input_loan_purpose_machinery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setsInput_loan_purpose_machinery(true);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setsInput_loan_purpose_machinery(true);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                } else {
                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setsInput_loan_purpose_machinery(false);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setsInput_loan_purpose_machinery(false);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                }
                mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);
            }
        });

        boolean isinput_loan_purpose_machinery = input_loan_purpose_machinery.isActivated();
        baslineFinanceInfo.setsInput_loan_purpose_machinery(isinput_loan_purpose_machinery);

        input_loan_purpose_other = rootView.findViewById(R.id.input_loan_purpose_other);

        input_loan_purpose_other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setsInput_loan_purpose_other(true);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setsInput_loan_purpose_other(true);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                } else {
                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setsInput_loan_purpose_other(false);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setsInput_loan_purpose_other(false);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                }
                mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);

            }
        });

        boolean isinput_loan_purpose_other = input_loan_purpose_other.isActivated();
        baslineFinanceInfo.setsInput_loan_purpose_other(isinput_loan_purpose_other);


        input_loan_disbursement_method = rootView.findViewById(R.id.input_loan_disbursement_method);

        if (input_loan_disbursement_method.getCheckedRadioButtonId() == R.id.cash_provided_purchase_inputs) {
            baslineFinanceInfo.setCash_provided_purchase_inputs(true);
            baselineFinanceInfoSeason.put(seasonName, baslineFinanceInfo);

        } else if (input_loan_disbursement_method.getCheckedRadioButtonId() == R.id.input_prov_in_kind) {
            baslineFinanceInfo.setInput_prov_in_kind(false);
            baselineFinanceInfoSeason.put(seasonName, baslineFinanceInfo);
        }
        input_loan_disbursement_method.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (i == R.id.cash_provided_purchase_inputs) {

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setCash_provided_purchase_inputs(true);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setCash_provided_purchase_inputs(true);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                } else if (i == R.id.input_prov_in_kind) {

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setInput_prov_in_kind(true);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setInput_prov_in_kind(true);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }
                }
                mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);
            }
        });

        agg_post_harv_loan_prov_bank = rootView.findViewById(R.id.agg_post_harv_loan_prov_bank);

        agg_post_harv_loan_prov_bank.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setAgg_post_harv_loan_prov_bank(true);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setAgg_post_harv_loan_prov_bank(true);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                } else {
                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setAgg_post_harv_loan_prov_bank(false);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setAgg_post_harv_loan_prov_bank(false);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }
                }
                mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);
            }
        });


        boolean isAggregPostHarvLoanProvBank = agg_post_harv_loan_prov_bank.isActivated();
        baslineFinanceInfo.setAgg_post_harv_loan_prov_bank(isAggregPostHarvLoanProvBank);

        agg_post_harv_loan_prov_cooperative = rootView.findViewById(R.id.agg_post_harv_loan_prov_cooperative);

        agg_post_harv_loan_prov_cooperative.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setAgg_post_harv_loan_prov_cooperative(true);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setAgg_post_harv_loan_prov_cooperative(true);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                } else {
                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setAgg_post_harv_loan_prov_cooperative(false);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setAgg_post_harv_loan_prov_cooperative(false);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }
                }
                mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);

            }
        });

        boolean isAggregPostHarvLoanProvCooperative = agg_post_harv_loan_prov_cooperative.isActivated();
        baslineFinanceInfo.setAgg_post_harv_loan_prov_cooperative(isAggregPostHarvLoanProvCooperative);

        agg_post_harv_loan_prov_sacco = rootView.findViewById(R.id.agg_post_harv_loan_prov_sacco);

        agg_post_harv_loan_prov_sacco.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setAgg_post_harv_loan_prov_sacco(true);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setAgg_post_harv_loan_prov_sacco(true);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                } else {
                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setAgg_post_harv_loan_prov_sacco(false);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setAgg_post_harv_loan_prov_sacco(false);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }
                }
                mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);

            }
        });

        boolean isAggregPostHarvLoanProvSacco = agg_post_harv_loan_prov_sacco.isActivated();
        baslineFinanceInfo.setAgg_post_harv_loan_prov_sacco(isAggregPostHarvLoanProvSacco);

        agg_post_harv_loan_prov_other = rootView.findViewById(R.id.agg_post_harv_loan_prov_other);

        agg_post_harv_loan_prov_other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setAgg_post_harv_loan_prov_other(true);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setAgg_post_harv_loan_prov_other(true);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                } else {
                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setAgg_post_harv_loan_prov_other(false);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setAgg_post_harv_loan_prov_other(false);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }
                }
                mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);

            }
        });

        boolean isAggregPostHarvLoanProvOther = agg_post_harv_loan_prov_other.isActivated();
        baslineFinanceInfo.setAgg_post_harv_loan_prov_other(isAggregPostHarvLoanProvOther);

        aggrgation_post_harvset_amount = rootView.findViewById(R.id.aggrgation_post_harvset_amount);

        aggrgation_post_harvset_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {
                    cursor = (Cursor) harvestSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setAggrgation_post_harvset_amount(Double.parseDouble(charSequence.toString()));
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfo.setAggrgation_post_harvset_amount(Double.parseDouble(charSequence.toString()));
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }
                    mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);

                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        aggrgation_post_harvset_loan_interest = rootView.findViewById(R.id.aggrgation_post_harvset_loan_interest);

        aggrgation_post_harvset_loan_interest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {
                    cursor = (Cursor) harvestSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setAggrgation_post_harvset_loan_interest(Double.parseDouble(charSequence.toString()));
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfo.setAggrgation_post_harvset_loan_interest(Double.parseDouble(charSequence.toString()));
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }
                    mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);

                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        aggrgation_post_harvset_loan_duration = rootView.findViewById(R.id.aggrgation_post_harvset_loan_duration);

        aggrgation_post_harvset_loan_duration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {
                    cursor = (Cursor) harvestSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setAggrgation_post_harvset_loan_duration(Integer.parseInt(charSequence.toString()));
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfo.setAggrgation_post_harvset_loan_duration(Integer.parseInt(charSequence.toString()));
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }
                    mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);

                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        agg_post_harv_loan_purpose_labour = rootView.findViewById(R.id.agg_post_harv_loan_purpose_labour);

        agg_post_harv_loan_purpose_labour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setAgg_post_harv_loan_purpose_labour(true);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setAgg_post_harv_loan_purpose_labour(true);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                } else {
                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setAgg_post_harv_loan_purpose_labour(false);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setAgg_post_harv_loan_purpose_labour(false);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }
                }
                mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);
            }
        });

        boolean isagg_post_harv_loan_purpose_labour = agg_post_harv_loan_purpose_labour.isActivated();
        baslineFinanceInfo.setAgg_post_harv_loan_purpose_labour(isagg_post_harv_loan_purpose_labour);

        agg_post_harv_loan_purpose_input = rootView.findViewById(R.id.agg_post_harv_loan_purpose_input);

        agg_post_harv_loan_purpose_input.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setAgg_post_harv_loan_purpose_input(true);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setAgg_post_harv_loan_purpose_input(true);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                } else {
                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setAgg_post_harv_loan_purpose_input(false);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setAgg_post_harv_loan_purpose_input(false);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }
                }
                mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);

            }
        });

        boolean isagg_post_harv_loan_purpose_input = agg_post_harv_loan_purpose_input.isActivated();
        baslineFinanceInfo.setAgg_post_harv_loan_purpose_input(isagg_post_harv_loan_purpose_input);

        agg_post_harv_loan_purpose_machinery = rootView.findViewById(R.id.agg_post_harv_loan_purpose_machinery);
        agg_post_harv_loan_purpose_machinery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setAgg_post_harv_loan_purpose_machinery(true);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setAgg_post_harv_loan_purpose_machinery(true);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                } else {
                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setAgg_post_harv_loan_purpose_machinery(false);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setAgg_post_harv_loan_purpose_machinery(false);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }
                }
                mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);

            }
        });

        boolean isagg_post_harv_loan_purpose_machinery = agg_post_harv_loan_purpose_machinery.isActivated();
        baslineFinanceInfo.setAgg_post_harv_loan_purpose_machinery(isagg_post_harv_loan_purpose_machinery);

        agg_post_harv_loan_purpose_other = rootView.findViewById(R.id.agg_post_harv_loan_purpose_other);
        agg_post_harv_loan_purpose_other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {

                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setAgg_post_harv_loan_purpose_other(true);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setAgg_post_harv_loan_purpose_other(true);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }

                } else {
                    if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                        baselineFinanceInfoSeason.get(seasonName).setAgg_post_harv_loan_purpose_other(false);
                    } else {
                        BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                        baselineFinanceInfo.setAgg_post_harv_loan_purpose_other(false);
                        baselineFinanceInfo.setSeasonId(seasonId);
                        baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                    }
                }
                mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);

            }
        });
        boolean isagg_post_harv_loan_purpose_other = agg_post_harv_loan_purpose_other.isActivated();
        baslineFinanceInfo.setAgg_post_harv_loan_purpose_other(isagg_post_harv_loan_purpose_other);

        aggrgation_post_harvset_laon_disbursement_method = rootView.findViewById(R.id.aggrgation_post_harvset_laon_disbursement_method);

        aggrgation_post_harvset_laon_disbursement_method.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                if (baselineFinanceInfoSeason.containsKey(seasonName)) {
                    baselineFinanceInfoSeason.get(seasonName).setAggrgation_post_harvset_laon_disbursement_method(charSequence.toString());
                } else {
                    BaselineFinanceInfo baselineFinanceInfo = new BaselineFinanceInfo();
                    seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                    baselineFinanceInfo.setSeasonId(seasonId);
                    baselineFinanceInfo.setAggrgation_post_harvset_laon_disbursement_method(charSequence.toString());
                    baselineFinanceInfoSeason.put(seasonName, baselineFinanceInfo);
                }
                mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        String inputloanVal = inputLoan.getSelectedItem().toString();
        baslineFinanceInfo.setInput_loan(inputloanVal);

        cursor = (Cursor) harvestSeason.getSelectedItem();
        seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

        baselineFinanceInfoSeason.put(seasonName, baslineFinanceInfo);
        mPage.getData().putSerializable("baseline_finance_info", baselineFinanceInfoSeason);

        return rootView;
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
            harvestSeason.setAdapter(adapter);
        }
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
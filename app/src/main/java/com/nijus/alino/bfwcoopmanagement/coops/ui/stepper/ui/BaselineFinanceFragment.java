package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.ui;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.BaselineFinanceInfo;

public class BaselineFinanceFragment extends Fragment {

    public static final String ARG_KEY = "key";
    public final String LOG_TAG = BaselineFinanceFragment.class.getSimpleName();
    private String mKey;

    private BaselineFinanceInfo baslineFinanceInfo = new BaselineFinanceInfo();

    private Page mPage;
    private PageFragmentCallbacks mCallbacks;


    private Spinner harvsetSeason;
    private Spinner input_loan;

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
    //private cash_provided_purchase_inputs;
    //private input_prov_in_kind;

    private AutoCompleteTextView aggrgation_post_harvset_loan;

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

        harvsetSeason = rootView.findViewById(R.id.harvsetSeason) ;
        input_loan = rootView.findViewById(R.id.input_loan) ;

        input_loan_prov_bank = rootView.findViewById(R.id.input_loan_prov_bank);
        boolean isInput_loan_prov_bank = input_loan_prov_bank.isActivated();
        baslineFinanceInfo.setInput_loan_prov_bank(isInput_loan_prov_bank);

        input_loan_prov_cooperative = rootView.findViewById(R.id.input_loan_prov_cooperative);
        boolean isInput_loan_prov_cooperative = input_loan_prov_cooperative.isActivated();
        baslineFinanceInfo.setInput_loan_prov_cooperative(isInput_loan_prov_cooperative);

        input_loan_prov_sacco = rootView.findViewById(R.id.input_loan_prov_sacco);
        boolean isInput_loan_prov_sacco = input_loan_prov_sacco.isActivated();
        baslineFinanceInfo.setInput_loan_prov_sacco(isInput_loan_prov_sacco);

        input_loan_prov_other = rootView.findViewById(R.id.input_loan_prov_other);
        boolean isinput_loan_prov_other = input_loan_prov_other.isActivated();
        baslineFinanceInfo.setInput_loan_prov_other(isinput_loan_prov_other);

        input_loan_amount = rootView.findViewById(R.id.input_loan_amount) ;
        input_loan_interest_rate = rootView.findViewById(R.id.input_loan_interest_rate) ;
        input_loan_duration = rootView.findViewById(R.id.input_loan_duration) ;

//        input_loan_purpose_labour = rootView.findViewById(R.id.input_loan_purpose_labour) ;
//        input_loan_purpose_seed = rootView.findViewById(R.id.input_loan_purpose_seed) ;
//        input_loan_purpose_input = rootView.findViewById(R.id.input_loan_purpose_input) ;
//        input_loan_purpose_machinery = rootView.findViewById(R.id.input_loan_purpose_machinery) ;
//        input_loan_purpose_other = rootView.findViewById(R.id.input_loan_purpose_other) ;

        input_loan_purpose_labour = rootView.findViewById(R.id.input_loan_purpose_labour);
        boolean isinput_loan_purpose_labour = input_loan_purpose_labour.isActivated();
        baslineFinanceInfo.setsInput_loan_purpose_labour(isinput_loan_purpose_labour);

        input_loan_purpose_seed = rootView.findViewById(R.id.input_loan_purpose_seed);
        boolean isinput_loan_purpose_seed = input_loan_purpose_seed.isActivated();
        baslineFinanceInfo.setsInput_loan_purpose_seed(isinput_loan_purpose_seed);

        input_loan_purpose_input = rootView.findViewById(R.id.input_loan_purpose_input);
        boolean isinput_loan_purpose_input = input_loan_purpose_input.isActivated();
        baslineFinanceInfo.setsInput_loan_purpose_input(isinput_loan_purpose_input);

        input_loan_purpose_machinery = rootView.findViewById(R.id.input_loan_purpose_machinery);
        boolean isinput_loan_purpose_machinery = input_loan_purpose_machinery.isActivated();
        baslineFinanceInfo.setsInput_loan_purpose_machinery(isinput_loan_purpose_machinery);

        input_loan_purpose_other = rootView.findViewById(R.id.input_loan_purpose_other);
        boolean isinput_loan_purpose_other = input_loan_purpose_other.isActivated();
        baslineFinanceInfo.setsInput_loan_purpose_other(isinput_loan_purpose_other);

        //find and set default favue for input loan disbursement buttongroup
        input_loan_disbursement_method = rootView.findViewById(R.id.input_loan_disbursement_method);
        //cash_provided_purchase_inputs = rootView.findViewById(R.id.cash_provided_purchase_inputs) ;
        //input_prov_in_kind = rootView.findViewById(R.id.input_prov_in_kind) ;
        input_loan_disbursement_method.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.cash_provided_purchase_inputs) {
                    baslineFinanceInfo.setCash_provided_purchase_inputs(true);
                    mPage.getData().putParcelable("baslineFinanceInfo", baslineFinanceInfo);
                } else if (i == R.id.input_prov_in_kind) {
                    baslineFinanceInfo.setInput_prov_in_kind(false);
                    mPage.getData().putParcelable("baslineFinanceInfo", baslineFinanceInfo);
                }
            }
        });

        aggrgation_post_harvset_loan = rootView.findViewById(R.id.aggrgation_post_harvset_loan);
        agg_post_harv_loan_prov_bank = rootView.findViewById(R.id.agg_post_harv_loan_prov_bank);
        agg_post_harv_loan_prov_cooperative = rootView.findViewById(R.id.agg_post_harv_loan_prov_cooperative) ;
        agg_post_harv_loan_prov_sacco = rootView.findViewById(R.id.agg_post_harv_loan_prov_sacco);
        agg_post_harv_loan_prov_other = rootView.findViewById(R.id.agg_post_harv_loan_prov_other);

        aggrgation_post_harvset_amount = rootView.findViewById(R.id.aggrgation_post_harvset_amount) ;
        aggrgation_post_harvset_loan_interest = rootView.findViewById(R.id.aggrgation_post_harvset_loan_interest) ;
        aggrgation_post_harvset_loan_duration = rootView.findViewById(R.id.aggrgation_post_harvset_loan_duration) ;

       /* agg_post_harv_loan_purpose_labour = rootView.findViewById(R.id.agg_post_harv_loan_purpose_labour) ;
        agg_post_harv_loan_purpose_input = rootView.findViewById(R.id.agg_post_harv_loan_purpose_input) ;
        agg_post_harv_loan_purpose_machinery = rootView.findViewById(R.id.agg_post_harv_loan_purpose_machinery) ;
        agg_post_harv_loan_purpose_other = rootView.findViewById(R.id.agg_post_harv_loan_purpose_other);
*/

        agg_post_harv_loan_purpose_labour = rootView.findViewById(R.id.agg_post_harv_loan_purpose_labour);
        boolean isagg_post_harv_loan_purpose_labour = agg_post_harv_loan_purpose_labour.isActivated();
        baslineFinanceInfo.setAgg_post_harv_loan_purpose_labour(isagg_post_harv_loan_purpose_labour);

        agg_post_harv_loan_purpose_input = rootView.findViewById(R.id.agg_post_harv_loan_purpose_input);
        boolean isagg_post_harv_loan_purpose_input = agg_post_harv_loan_purpose_input.isActivated();
        baslineFinanceInfo.setAgg_post_harv_loan_purpose_input(isagg_post_harv_loan_purpose_input);

        agg_post_harv_loan_purpose_machinery = rootView.findViewById(R.id.agg_post_harv_loan_purpose_machinery);
        boolean isagg_post_harv_loan_purpose_machinery = agg_post_harv_loan_purpose_machinery.isActivated();
        baslineFinanceInfo.setAgg_post_harv_loan_purpose_machinery(isagg_post_harv_loan_purpose_machinery);

        agg_post_harv_loan_purpose_other = rootView.findViewById(R.id.agg_post_harv_loan_purpose_other);
        boolean isagg_post_harv_loan_purpose_other = agg_post_harv_loan_purpose_other.isActivated();
        baslineFinanceInfo.setAgg_post_harv_loan_purpose_other(isagg_post_harv_loan_purpose_other);

        aggrgation_post_harvset_laon_disbursement_method = rootView.findViewById(R.id.aggrgation_post_harvset_laon_disbursement_method);

       //SET DEFAULT FOR ALL AUTOCOMPLETE TEXT VIEEW

        input_loan_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                baslineFinanceInfo.setInput_loan_amount(Integer.parseInt(charSequence.toString()));
                mPage.setData("baslineFinanceInfo", baslineFinanceInfo);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        input_loan_interest_rate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                baslineFinanceInfo.setInput_loan_interest_rate(Integer.parseInt(charSequence.toString()));
                mPage.setData("baslineFinanceInfo", baslineFinanceInfo);
            } catch (NumberFormatException exp) {
                exp.printStackTrace();
            }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        input_loan_duration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                baslineFinanceInfo.setInput_loan_duration(Integer.parseInt(charSequence.toString()));
                mPage.setData("baslineFinanceInfo", baslineFinanceInfo);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        aggrgation_post_harvset_loan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                baslineFinanceInfo.setAggrgation_post_harvset_loan(charSequence.toString());
                mPage.setData("baslineFinanceInfo", baslineFinanceInfo);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        aggrgation_post_harvset_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                baslineFinanceInfo.setAggrgation_post_harvset_amount(Integer.parseInt(charSequence.toString()));
                mPage.setData("baslineFinanceInfo", baslineFinanceInfo);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        aggrgation_post_harvset_loan_interest.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                baslineFinanceInfo.setAggrgation_post_harvset_loan_interest(Integer.parseInt(charSequence.toString()));
                mPage.setData("baslineFinanceInfo", baslineFinanceInfo);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        aggrgation_post_harvset_loan_duration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                baslineFinanceInfo.setAggrgation_post_harvset_loan_duration(Integer.parseInt(charSequence.toString()));
                mPage.setData("baslineFinanceInfo", baslineFinanceInfo);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        aggrgation_post_harvset_laon_disbursement_method.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                baslineFinanceInfo.setAggrgation_post_harvset_laon_disbursement_method(charSequence.toString());
                mPage.setData("baslineFinanceInfo", baslineFinanceInfo);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        //set default ALL CHECKBUTTONS

        input_loan_prov_bank.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baslineFinanceInfo.setInput_loan_prov_bank(true);
                } else {
                    baslineFinanceInfo.setInput_loan_prov_bank(false);
                }
                mPage.getData().putParcelable("baslineFinanceInfo", baslineFinanceInfo);
            }
        });

        input_loan_prov_cooperative.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baslineFinanceInfo.setInput_loan_prov_cooperative(true);
                } else {
                    baslineFinanceInfo.setInput_loan_prov_cooperative(false);
                }
                mPage.getData().putParcelable("baslineFinanceInfo", baslineFinanceInfo);
            }
        });

        input_loan_prov_sacco.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baslineFinanceInfo.setInput_loan_prov_sacco(true);
                } else {
                    baslineFinanceInfo.setInput_loan_prov_sacco(false);
                }
                mPage.getData().putParcelable("baslineFinanceInfo", baslineFinanceInfo);
            }
        });

        input_loan_prov_other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baslineFinanceInfo.setInput_loan_prov_other(true);
                } else {
                    baslineFinanceInfo.setInput_loan_prov_other(false);
                }
                mPage.getData().putParcelable("baslineFinanceInfo", baslineFinanceInfo);
            }
        });

        input_loan_purpose_labour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baslineFinanceInfo.setsInput_loan_purpose_labour(true);
                } else {
                    baslineFinanceInfo.setsInput_loan_purpose_labour(false);
                }
                mPage.getData().putParcelable("baslineFinanceInfo", baslineFinanceInfo);
            }
        });

        input_loan_purpose_seed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baslineFinanceInfo.setsInput_loan_purpose_seed(true);
                } else {
                    baslineFinanceInfo.setsInput_loan_purpose_seed(false);
                }
                mPage.getData().putParcelable("baslineFinanceInfo", baslineFinanceInfo);
            }
        });

        input_loan_purpose_input.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baslineFinanceInfo.setsInput_loan_purpose_input(true);
                } else {
                    baslineFinanceInfo.setsInput_loan_purpose_input(false);
                }
                mPage.getData().putParcelable("baslineFinanceInfo", baslineFinanceInfo);
            }
        });

        input_loan_purpose_machinery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baslineFinanceInfo.setsInput_loan_purpose_machinery(true);
                } else {
                    baslineFinanceInfo.setsInput_loan_purpose_machinery(false);
                }
                mPage.getData().putParcelable("baslineFinanceInfo", baslineFinanceInfo);
            }
        });

        input_loan_purpose_other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baslineFinanceInfo.setsInput_loan_purpose_other(true);
                } else {
                    baslineFinanceInfo.setsInput_loan_purpose_other(false);
                }
                mPage.getData().putParcelable("baslineFinanceInfo", baslineFinanceInfo);
            }
        });

        agg_post_harv_loan_prov_bank.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baslineFinanceInfo.setAgg_post_harv_loan_prov_bank(true);
                } else {
                    baslineFinanceInfo.setAgg_post_harv_loan_prov_bank(false);
                }
                mPage.getData().putParcelable("baslineFinanceInfo", baslineFinanceInfo);
            }
        });

        agg_post_harv_loan_prov_cooperative.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baslineFinanceInfo.setAgg_post_harv_loan_prov_cooperative(true);
                } else {
                    baslineFinanceInfo.setAgg_post_harv_loan_prov_cooperative(false);
                }
                mPage.getData().putParcelable("baslineFinanceInfo", baslineFinanceInfo);
            }
        });

        agg_post_harv_loan_prov_sacco.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baslineFinanceInfo.setAgg_post_harv_loan_prov_sacco(true);
                } else {
                    baslineFinanceInfo.setAgg_post_harv_loan_prov_sacco(false);
                }
                mPage.getData().putParcelable("baslineFinanceInfo", baslineFinanceInfo);
            }
        });

        agg_post_harv_loan_prov_other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baslineFinanceInfo.setAgg_post_harv_loan_prov_other(true);
                } else {
                    baslineFinanceInfo.setAgg_post_harv_loan_prov_other(false);
                }
                mPage.getData().putParcelable("baslineFinanceInfo", baslineFinanceInfo);
            }
        });

        agg_post_harv_loan_purpose_labour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baslineFinanceInfo.setAgg_post_harv_loan_purpose_labour(true);
                } else {
                    baslineFinanceInfo.setAgg_post_harv_loan_purpose_labour(false);
                }
                mPage.getData().putParcelable("baslineFinanceInfo", baslineFinanceInfo);
            }
        });

        agg_post_harv_loan_purpose_input.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baslineFinanceInfo.setAgg_post_harv_loan_purpose_input(true);
                } else {
                    baslineFinanceInfo.setAgg_post_harv_loan_purpose_input(false);
                }
                mPage.getData().putParcelable("baslineFinanceInfo", baslineFinanceInfo);
            }
        });

        agg_post_harv_loan_purpose_machinery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baslineFinanceInfo.setAgg_post_harv_loan_purpose_machinery(true);
                } else {
                    baslineFinanceInfo.setAgg_post_harv_loan_purpose_machinery(false);
                }
                mPage.getData().putParcelable("baslineFinanceInfo", baslineFinanceInfo);
            }
        });

        agg_post_harv_loan_purpose_other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baslineFinanceInfo.setAgg_post_harv_loan_purpose_other(true);
                } else {
                    baslineFinanceInfo.setAgg_post_harv_loan_purpose_other(false);
                }
                mPage.getData().putParcelable("baslineFinanceInfo", baslineFinanceInfo);
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
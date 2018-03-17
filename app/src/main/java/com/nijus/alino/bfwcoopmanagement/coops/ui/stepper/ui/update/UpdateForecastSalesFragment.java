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
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.ForecastSales;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.ui.PageFragmentCallbacks;

public class UpdateForecastSalesFragment extends Fragment {

    public static final String ARG_KEY = "key";
    public final String LOG_TAG = UpdateForecastSalesFragment.class.getSimpleName();
    private String mKey;

    private ForecastSales forecastSales = new ForecastSales();

    private Page mPage;
    private PageFragmentCallbacks mCallbacks;

    private Spinner harvsetSeason;

    private CheckBox rgcc;
    private CheckBox prodev;
    private CheckBox sasura;
    private CheckBox aif;
    private CheckBox eax;
    private CheckBox none;
    private CheckBox other;

    private AutoCompleteTextView other_text;
    private AutoCompleteTextView commired_contract_qty;
    private Spinner grade;
    private AutoCompleteTextView min_floor_per_grade;

    public UpdateForecastSalesFragment() {
        super();
    }

    public static UpdateForecastSalesFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UpdateForecastSalesFragment fragment = new UpdateForecastSalesFragment();
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
        View rootView = inflater.inflate(R.layout.forecast_sales, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.forecast_sales));

        harvsetSeason = rootView.findViewById(R.id.harvsetSeason);

        rgcc = rootView.findViewById(R.id.rgcc);
        boolean isrgcc = rgcc.isActivated();
        forecastSales.setRgcc(isrgcc);

        prodev = rootView.findViewById(R.id.prodev);
        boolean isprodev = prodev.isActivated();
        forecastSales.setProdev(isprodev);

        sasura = rootView.findViewById(R.id.sasura);
        boolean issasura = sasura.isActivated();
        forecastSales.setSarura(issasura);

        aif = rootView.findViewById(R.id.aif);
        boolean isaif = aif.isActivated();
        forecastSales.setAif(isaif);

        eax = rootView.findViewById(R.id.eax);
        boolean iseax = eax.isActivated();
        forecastSales.setEax(iseax);

        none = rootView.findViewById(R.id.none);
        boolean isnone = none.isActivated();
        forecastSales.setNone(isnone);

        other = rootView.findViewById(R.id.other);
        boolean isother = other.isActivated();
        forecastSales.setOther(isother);

        other_text = rootView.findViewById(R.id.other_text);
        commired_contract_qty = rootView.findViewById(R.id.commired_contract_qty);
        grade = rootView.findViewById(R.id.grade);
        //min_floor_per_grade = rootView.findViewById(R.id.min_floor_per_grade);

        commired_contract_qty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                forecastSales.setCommitedContractQty(Integer.parseInt(charSequence.toString()));
                mPage.setData("forecastSales", forecastSales);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        min_floor_per_grade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                forecastSales.setMinFloorPerGrade(charSequence.toString());
                mPage.setData("forecastSales", forecastSales);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //les checkk button
        rgcc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    forecastSales.setRgcc(true);
                } else {
                    forecastSales.setRgcc(false);
                }
                mPage.getData().putParcelable("forecastSales", forecastSales);
            }
        });

        prodev.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    forecastSales.setProdev(true);
                } else {
                    forecastSales.setProdev(false);
                }
                mPage.getData().putParcelable("forecastSales", forecastSales);
            }
        });

        sasura.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    forecastSales.setSarura(true);
                } else {
                    forecastSales.setSarura(false);
                }
                mPage.getData().putParcelable("forecastSales", forecastSales);
            }
        });

        aif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    forecastSales.setAif(true);
                } else {
                    forecastSales.setAif(false);
                }
                mPage.getData().putParcelable("forecastSales", forecastSales);
            }
        });

        eax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    forecastSales.setEax(true);
                } else {
                    forecastSales.setEax(false);
                }
                mPage.getData().putParcelable("forecastSales", forecastSales);
            }
        });

        none.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    forecastSales.setNone(true);
                } else {
                    forecastSales.setNone(false);
                }
                mPage.getData().putParcelable("forecastSales", forecastSales);
            }
        });

        other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    forecastSales.setOther(true);
                } else {
                    forecastSales.setOther(false);
                }
                mPage.getData().putParcelable("forecastSales", forecastSales);
            }
        });

        //les spiners
        /*harvsetSeason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                forecastSales.setTexteSafeStorage(charSequence.toString());
                mPage.setData("forecastSales", forecastSales);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        grade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                forecastSales.setTextOtherResourceInfo(charSequence.toString());
                mPage.setData("forecastSales", forecastSales);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/


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
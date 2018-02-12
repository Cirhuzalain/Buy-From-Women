package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.BaselineYield;

public class BaselineYieldFragment extends Fragment {

    public static final String ARG_KEY = "key";
    public final String LOG_TAG = BaselineYieldFragment.class.getSimpleName();
    private String mKey;

    private BaselineYield baselineYield = new BaselineYield();

    private Page mPage;
    private PageFragmentCallbacks mCallbacks;

    private Spinner harvsetSeason;

    private CheckBox maize;
    private CheckBox bean;
    private CheckBox soy;
    private CheckBox other;
    
    public BaselineYieldFragment() {
        super();
    }

    public static BaselineYieldFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        BaselineYieldFragment fragment = new BaselineYieldFragment();
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
        View rootView = inflater.inflate(R.layout.baseline_yield, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.baseline_yield));

        harvsetSeason = rootView.findViewById(R.id.harvsetSeason);

        maize = rootView.findViewById(R.id.maize);
        boolean isMaize = maize.isActivated();
        baselineYield.setMaize(isMaize);

        bean = rootView.findViewById(R.id.bean);
        boolean isBean = bean.isActivated();
        baselineYield.setBean(isBean);

        soy = rootView.findViewById(R.id.soy);
        boolean isSoy = soy.isActivated();
        baselineYield.setSoy(isSoy);

        other = rootView.findViewById(R.id.other);
        boolean isother = other.isActivated();
        baselineYield.setOther(isother);

        //les checkk button
        maize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baselineYield.setMaize(true);
                } else {
                    baselineYield.setMaize(false);
                }
                mPage.getData().putParcelable("baselineYield", baselineYield);
            }
        });

        soy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baselineYield.setSoy(true);
                } else {
                    baselineYield.setSoy(false);
                }
                mPage.getData().putParcelable("baselineYield", baselineYield);
            }
        });

        bean.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baselineYield.setBean(true);
                } else {
                    baselineYield.setBean(false);
                }
                mPage.getData().putParcelable("baselineYield", baselineYield);
            }
        });

        other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    baselineYield.setOther(true);
                } else {
                    baselineYield.setOther(false);
                }
                mPage.getData().putParcelable("baselineYield", baselineYield);
            }
        });

        //les spiners



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
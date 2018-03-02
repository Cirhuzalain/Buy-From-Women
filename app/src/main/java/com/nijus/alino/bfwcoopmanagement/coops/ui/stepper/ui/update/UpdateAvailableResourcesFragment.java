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
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.AvailableResources;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.ui.PageFragmentCallbacks;

public class UpdateAvailableResourcesFragment extends Fragment {

    public static final String ARG_KEY = "key";
    public final String LOG_TAG = UpdateAvailableResourcesFragment.class.getSimpleName();
    private String mKey;

    private AvailableResources availableResources = new AvailableResources();

    private Page mPage;
    private PageFragmentCallbacks mCallbacks;

    private CheckBox office_space;
    private CheckBox moisture_meter;
    private CheckBox weighting_scales;
    private CheckBox qlt_inputs;
    private CheckBox tractors;
    private CheckBox harvester;
    private CheckBox dryer;
    private CheckBox thresher;
    private CheckBox safe_storage;
    private CheckBox e_other_av_res;


    private AutoCompleteTextView safe_storage_text;
    private AutoCompleteTextView other_text;

    public UpdateAvailableResourcesFragment() {
        super();
    }

    public static UpdateAvailableResourcesFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UpdateAvailableResourcesFragment fragment = new UpdateAvailableResourcesFragment();
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
        View rootView = inflater.inflate(R.layout.available_ressources, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.available_resources));

        office_space = rootView.findViewById(R.id.office_space) ;
        boolean isoffice_space = office_space.isActivated();
        availableResources.setOfficeSpace(isoffice_space);

        moisture_meter = rootView.findViewById(R.id.moisture_meter) ;
        boolean ismoisture_meter = moisture_meter.isActivated();
        availableResources.setMoistureMeter(ismoisture_meter);

        weighting_scales = rootView.findViewById(R.id.weighting_scales) ;
        boolean isweighting_scales = weighting_scales.isActivated();
        availableResources.setWeightingScales(isweighting_scales);

        qlt_inputs = rootView.findViewById(R.id.qlt_inputs) ;
        boolean isqlt_inputs = qlt_inputs.isActivated();
        availableResources.setQualityInput(isqlt_inputs);

        tractors = rootView.findViewById(R.id.tractors) ;
        boolean istractors = tractors.isActivated();
        availableResources.setTractor(istractors);

        harvester = rootView.findViewById(R.id.harvester) ;
        boolean isharvester = harvester.isActivated();
        availableResources.setHarvester(isharvester);

        dryer = rootView.findViewById(R.id.dryer) ;
        boolean isdryer = dryer.isActivated();
        availableResources.setDryer(isdryer);

        thresher = rootView.findViewById(R.id.thresher) ;
        boolean isthresher = thresher.isActivated();
        availableResources.setTresher(isthresher);

        safe_storage = rootView.findViewById(R.id.safe_storage) ;
        boolean issafe_storage = safe_storage.isActivated();
        availableResources.setSafeStorage(issafe_storage);

        e_other_av_res = rootView.findViewById(R.id.e_other_av_res) ;
        boolean ise_other_av_res = e_other_av_res.isActivated();
        availableResources.setOtherResourceInfo(ise_other_av_res);


        safe_storage_text = rootView.findViewById(R.id.safe_storage_text) ;

        other_text = rootView.findViewById(R.id.other_text) ;


        //les checkk button

        office_space.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    availableResources.setOfficeSpace(true);
                } else {
                    availableResources.setOfficeSpace(false);
                }
                mPage.getData().putParcelable("availableResources", availableResources);
            }
        });

        moisture_meter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    availableResources.setMoistureMeter(true);
                } else {
                    availableResources.setMoistureMeter(false);
                }
                mPage.getData().putParcelable("availableResources", availableResources);
            }
        });

        weighting_scales.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    availableResources.setWeightingScales(true);
                } else {
                    availableResources.setWeightingScales(false);
                }
                mPage.getData().putParcelable("availableResources", availableResources);
            }
        });

        qlt_inputs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    availableResources.setQualityInput(true);
                } else {
                    availableResources.setQualityInput(false);
                }
                mPage.getData().putParcelable("availableResources", availableResources);
            }
        });

        tractors.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    availableResources.setTractor(true);
                } else {
                    availableResources.setTractor(false);
                }
                mPage.getData().putParcelable("availableResources", availableResources);
            }
        });

        harvester.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    availableResources.setHarvester(true);
                } else {
                    availableResources.setHarvester(false);
                }
                mPage.getData().putParcelable("availableResources", availableResources);
            }
        });

        dryer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    availableResources.setDryer(true);
                } else {
                    availableResources.setDryer(false);
                }
                mPage.getData().putParcelable("availableResources", availableResources);
            }
        });

        thresher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    availableResources.setTresher(true);
                } else {
                    availableResources.setTresher(false);
                }
                mPage.getData().putParcelable("availableResources", availableResources);
            }
        });

        safe_storage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    availableResources.setSafeStorage(true);
                } else {
                    availableResources.setSafeStorage(false);
                }
                mPage.getData().putParcelable("availableResources", availableResources);
            }
        });

        e_other_av_res.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    availableResources.setOtherResourceInfo(true);
                } else {
                    availableResources.setOtherResourceInfo(false);
                }
                mPage.getData().putParcelable("availableResources", availableResources);
            }
        });


        //les texts
        safe_storage_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                availableResources.setTextSafeStorage(charSequence.toString());
                mPage.setData("availableResources", availableResources);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        other_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                availableResources.setTextOtherResourceInfo(charSequence.toString());
                mPage.setData("availableResources", availableResources);
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
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
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.AvailableRessources;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.ui.PageFragmentCallbacks;

public class UpdateAvailableRessourcesFragment extends Fragment {

    public static final String ARG_KEY = "key";
    public final String LOG_TAG = UpdateAvailableRessourcesFragment.class.getSimpleName();
    private String mKey;

    private AvailableRessources availableRessources = new AvailableRessources();

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

    public UpdateAvailableRessourcesFragment() {
        super();
    }

    public static UpdateAvailableRessourcesFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UpdateAvailableRessourcesFragment fragment = new UpdateAvailableRessourcesFragment();
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
        availableRessources.setOfficeSpace(isoffice_space);

        moisture_meter = rootView.findViewById(R.id.moisture_meter) ;
        boolean ismoisture_meter = moisture_meter.isActivated();
        availableRessources.setMoistureMeter(ismoisture_meter);

        weighting_scales = rootView.findViewById(R.id.weighting_scales) ;
        boolean isweighting_scales = weighting_scales.isActivated();
        availableRessources.setWeightingScales(isweighting_scales);

        qlt_inputs = rootView.findViewById(R.id.qlt_inputs) ;
        boolean isqlt_inputs = qlt_inputs.isActivated();
        availableRessources.setQualityInput(isqlt_inputs);

        tractors = rootView.findViewById(R.id.tractors) ;
        boolean istractors = tractors.isActivated();
        availableRessources.setTractor(istractors);

        harvester = rootView.findViewById(R.id.harvester) ;
        boolean isharvester = harvester.isActivated();
        availableRessources.setHarvester(isharvester);

        dryer = rootView.findViewById(R.id.dryer) ;
        boolean isdryer = dryer.isActivated();
        availableRessources.setDryer(isdryer);

        thresher = rootView.findViewById(R.id.thresher) ;
        boolean isthresher = thresher.isActivated();
        availableRessources.setTresher(isthresher);

        safe_storage = rootView.findViewById(R.id.safe_storage) ;
        boolean issafe_storage = safe_storage.isActivated();
        availableRessources.setSafeStorage(issafe_storage);

        e_other_av_res = rootView.findViewById(R.id.e_other_av_res) ;
        boolean ise_other_av_res = e_other_av_res.isActivated();
        availableRessources.setOtherResourceInfo(ise_other_av_res);


        safe_storage_text = rootView.findViewById(R.id.safe_storage_text) ;

        other_text = rootView.findViewById(R.id.other_text) ;


        //les checkk button

        office_space.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    availableRessources.setOfficeSpace(true);
                } else {
                    availableRessources.setOfficeSpace(false);
                }
                mPage.getData().putParcelable("availableRessources", availableRessources);
            }
        });

        moisture_meter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    availableRessources.setMoistureMeter(true);
                } else {
                    availableRessources.setMoistureMeter(false);
                }
                mPage.getData().putParcelable("availableRessources", availableRessources);
            }
        });

        weighting_scales.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    availableRessources.setWeightingScales(true);
                } else {
                    availableRessources.setWeightingScales(false);
                }
                mPage.getData().putParcelable("availableRessources", availableRessources);
            }
        });

        qlt_inputs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    availableRessources.setQualityInput(true);
                } else {
                    availableRessources.setQualityInput(false);
                }
                mPage.getData().putParcelable("availableRessources", availableRessources);
            }
        });

        tractors.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    availableRessources.setTractor(true);
                } else {
                    availableRessources.setTractor(false);
                }
                mPage.getData().putParcelable("availableRessources", availableRessources);
            }
        });

        harvester.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    availableRessources.setHarvester(true);
                } else {
                    availableRessources.setHarvester(false);
                }
                mPage.getData().putParcelable("availableRessources", availableRessources);
            }
        });

        dryer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    availableRessources.setDryer(true);
                } else {
                    availableRessources.setDryer(false);
                }
                mPage.getData().putParcelable("availableRessources", availableRessources);
            }
        });

        thresher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    availableRessources.setTresher(true);
                } else {
                    availableRessources.setTresher(false);
                }
                mPage.getData().putParcelable("availableRessources", availableRessources);
            }
        });

        safe_storage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    availableRessources.setSafeStorage(true);
                } else {
                    availableRessources.setSafeStorage(false);
                }
                mPage.getData().putParcelable("availableRessources", availableRessources);
            }
        });

        e_other_av_res.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    availableRessources.setOtherResourceInfo(true);
                } else {
                    availableRessources.setOtherResourceInfo(false);
                }
                mPage.getData().putParcelable("availableRessources", availableRessources);
            }
        });


        //les texts
        safe_storage_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                availableRessources.setTexteSafeStorage(charSequence.toString());
                mPage.setData("availableRessources", availableRessources);
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
                availableRessources.setTextOtherResourceInfo(charSequence.toString());
                mPage.setData("availableRessources", availableRessources);
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
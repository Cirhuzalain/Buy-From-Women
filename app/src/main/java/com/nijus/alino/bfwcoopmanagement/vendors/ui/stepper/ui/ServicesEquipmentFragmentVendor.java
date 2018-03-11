package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui;

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
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.PageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.ServiceAccessVendor;

public class ServicesEquipmentFragmentVendor extends Fragment {

    public static final String ARG_KEY = "key";
    private String mKey;
    private PageVendorVendor mPageVendor;
    private PageFragmentCallbacksVendor mCallbacks;
    private ServiceAccessVendor serviceAccessVendor = new ServiceAccessVendor();

    private CheckBox tractors;
    private CheckBox harvester;
    private CheckBox dryer;
    private CheckBox tresher;
    private CheckBox safeStorage;
    private CheckBox otherResourcesAvailable;

    private CheckBox dam;
    private CheckBox well;
    private CheckBox boreHole;
    private CheckBox pipeBorne;
    private CheckBox river;
    private CheckBox eIrrigation;
    private CheckBox eNone;
    private CheckBox eOther;

    private AutoCompleteTextView safeStorage_text;
    private AutoCompleteTextView newResources;
    private AutoCompleteTextView otherwatersource;


    public ServicesEquipmentFragmentVendor() {
        super();
    }

    public static ServicesEquipmentFragmentVendor create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        ServicesEquipmentFragmentVendor fragment = new ServicesEquipmentFragmentVendor();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPageVendor = mCallbacks.onGetPage(mKey);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.service_equipment_layout, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.access_service));

        tractors = rootView.findViewById(R.id.tractors);
        boolean isTractors = tractors.isActivated();
        serviceAccessVendor.setTractor(isTractors);

        harvester = rootView.findViewById(R.id.harvester);
        boolean isHarvester = harvester.isActivated();
        serviceAccessVendor.setHarvester(isHarvester);

        dryer = rootView.findViewById(R.id.dryer);
        boolean isDryer = dryer.isActivated();
        serviceAccessVendor.setDryer(isDryer);

        tresher = rootView.findViewById(R.id.thresher);
        boolean isTresher = tresher.isActivated();
        serviceAccessVendor.setTresher(isTresher);

        safeStorage = rootView.findViewById(R.id.safe_storage);
        boolean isSafeStorage = safeStorage.isActivated();
        serviceAccessVendor.setSafeStorage(isSafeStorage);

        otherResourcesAvailable = rootView.findViewById(R.id.e_other_av_res);
        boolean isOtherResourceAv = otherResourcesAvailable.isActivated();
        serviceAccessVendor.setOtherResourceInfo(isOtherResourceAv);


        dam = rootView.findViewById(R.id.e_dam);
        boolean isDam = dam.isActivated();
        serviceAccessVendor.setDam(isDam);

        well = rootView.findViewById(R.id.e_well);
        boolean isWell = well.isActivated();
        serviceAccessVendor.setWell(isWell);

        boreHole = rootView.findViewById(R.id.e_borehole);
        boolean isBoreHole = boreHole.isActivated();
        serviceAccessVendor.setBoreHole(isBoreHole);

        pipeBorne = rootView.findViewById(R.id.e_pipe_borne);
        boolean isPipeBorne = pipeBorne.isActivated();
        serviceAccessVendor.setPipeBorne(isPipeBorne);

        river = rootView.findViewById(R.id.e_river);
        boolean isRiver = river.isActivated();
        serviceAccessVendor.setDryer(isRiver);

        eIrrigation = rootView.findViewById(R.id.e_irrigation);
        boolean isEIrrigation = eIrrigation.isActivated();
        serviceAccessVendor.setIrrigation(isEIrrigation);

        eNone = rootView.findViewById(R.id.e_none);
        boolean isENone = eNone.isActivated();
        serviceAccessVendor.setHasNoWaterSource(isENone);

        eOther = rootView.findViewById(R.id.e_other_main);
        boolean isEOther = eOther.isActivated();
        serviceAccessVendor.setOtherInfo(isEOther);

        safeStorage_text = rootView.findViewById(R.id.safe_storage_text);
        newResources = rootView.findViewById(R.id.new_resources);
        otherwatersource = rootView.findViewById(R.id.other_water_source);

        mPageVendor.getData().putParcelable("serviceAccessVendor", serviceAccessVendor);


        tractors.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    serviceAccessVendor.setTractor(true);
                } else {
                    serviceAccessVendor.setTractor(false);
                }
                mPageVendor.getData().putParcelable("serviceAccessVendor", serviceAccessVendor);
            }
        });

        harvester.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    serviceAccessVendor.setHarvester(true);
                } else {
                    serviceAccessVendor.setHarvester(false);
                }
                mPageVendor.getData().putParcelable("serviceAccessVendor", serviceAccessVendor);
            }
        });

        dryer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    serviceAccessVendor.setDryer(true);
                } else {
                    serviceAccessVendor.setDryer(false);
                }
                mPageVendor.getData().putParcelable("serviceAccessVendor", serviceAccessVendor);
            }
        });

        tresher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    serviceAccessVendor.setTresher(true);
                } else {
                    serviceAccessVendor.setTresher(false);
                }
                mPageVendor.getData().putParcelable("serviceAccessVendor", serviceAccessVendor);
            }
        });

        safeStorage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    safeStorage_text.setVisibility(View.VISIBLE);
                    serviceAccessVendor.setSafeStorage(true);
                    safeStorage_text.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            serviceAccessVendor.setStorageDetails(charSequence.toString());
                            mPageVendor.setData("serviceAccessVendor", serviceAccessVendor);
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                } else {
                    serviceAccessVendor.setSafeStorage(false);
                    safeStorage_text.setText("");
                    safeStorage_text.setVisibility(View.GONE);
                }
                mPageVendor.getData().putParcelable("serviceAccessVendor", serviceAccessVendor);
            }
        });
        otherResourcesAvailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    newResources.setVisibility(View.VISIBLE);
                    serviceAccessVendor.setOtherResourceInfo(true);
                    newResources.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            serviceAccessVendor.setNewResourcesDetails(charSequence.toString());
                            mPageVendor.setData("serviceAccessVendor", serviceAccessVendor);
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                } else {
                    serviceAccessVendor.setOtherResourceInfo(false);
                    newResources.setText("");
                    newResources.setVisibility(View.GONE);
                }
                mPageVendor.getData().putParcelable("serviceAccessVendor", serviceAccessVendor);
            }
        });

        dam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    serviceAccessVendor.setDam(true);
                } else {
                    serviceAccessVendor.setDam(false);
                }
                mPageVendor.getData().putParcelable("serviceAccessVendor", serviceAccessVendor);
            }
        });

        well.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    serviceAccessVendor.setWell(true);
                } else {
                    serviceAccessVendor.setWell(true);
                }
                mPageVendor.getData().putParcelable("serviceAccessVendor", serviceAccessVendor);
            }
        });

        boreHole.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    serviceAccessVendor.setBoreHole(true);
                } else {
                    serviceAccessVendor.setBoreHole(false);
                }
                mPageVendor.getData().putParcelable("serviceAccessVendor", serviceAccessVendor);
            }
        });

        pipeBorne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    serviceAccessVendor.setPipeBorne(true);
                } else {
                    serviceAccessVendor.setPipeBorne(false);
                }
                mPageVendor.getData().putParcelable("serviceAccessVendor", serviceAccessVendor);
            }
        });

        river.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    serviceAccessVendor.setRiverStream(true);
                } else {
                    serviceAccessVendor.setRiverStream(false);
                }
                mPageVendor.getData().putParcelable("serviceAccessVendor", serviceAccessVendor);
            }
        });

        eIrrigation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    serviceAccessVendor.setIrrigation(true);
                } else {
                    serviceAccessVendor.setIrrigation(false);
                }
                mPageVendor.getData().putParcelable("serviceAccessVendor", serviceAccessVendor);
            }
        });

        eNone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    serviceAccessVendor.setHasNoWaterSource(true);
                } else {
                    serviceAccessVendor.setHasNoWaterSource(false);
                }
                mPageVendor.getData().putParcelable("serviceAccessVendor", serviceAccessVendor);
            }
        });

        eOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    otherwatersource.setVisibility(View.VISIBLE);
                    serviceAccessVendor.setOtherInfo(true);
                    otherwatersource.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            serviceAccessVendor.setMainWaterSourceDetails(charSequence.toString());
                            mPageVendor.setData("serviceAccessVendor", serviceAccessVendor);
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                } else {
                    otherwatersource.setText("");
                    otherwatersource.setVisibility(View.GONE);
                    serviceAccessVendor.setOtherInfo(false);
                }
                mPageVendor.getData().putParcelable("serviceAccessVendor", serviceAccessVendor);
            }
        });

        return rootView;
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
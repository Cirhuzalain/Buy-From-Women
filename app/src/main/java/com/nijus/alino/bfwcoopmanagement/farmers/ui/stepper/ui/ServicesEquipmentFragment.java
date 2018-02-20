package com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.ui;

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
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.ServiceAccess;

public class ServicesEquipmentFragment extends Fragment {

    public static final String ARG_KEY = "key";
    private String mKey;
    private Page mPage;
    private PageFragmentCallbacks mCallbacks;
    private ServiceAccess serviceAccess = new ServiceAccess();

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


    public ServicesEquipmentFragment() {
        super();
    }

    public static ServicesEquipmentFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        ServicesEquipmentFragment fragment = new ServicesEquipmentFragment();
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
        View rootView = inflater.inflate(R.layout.service_equipment_layout, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.access_service));

        tractors = rootView.findViewById(R.id.tractors);
        boolean isTractors = tractors.isActivated();
        serviceAccess.setTractor(isTractors);

        harvester = rootView.findViewById(R.id.harvester);
        boolean isHarvester = harvester.isActivated();
        serviceAccess.setHarvester(isHarvester);

        dryer = rootView.findViewById(R.id.dryer);
        boolean isDryer = dryer.isActivated();
        serviceAccess.setDryer(isDryer);

        tresher = rootView.findViewById(R.id.thresher);
        boolean isTresher = tresher.isActivated();
        serviceAccess.setTresher(isTresher);

        safeStorage = rootView.findViewById(R.id.safe_storage);
        boolean isSafeStorage = safeStorage.isActivated();
        serviceAccess.setSafeStorage(isSafeStorage);

        otherResourcesAvailable = rootView.findViewById(R.id.e_other_av_res);
        boolean isOtherResourceAv = otherResourcesAvailable.isActivated();
        serviceAccess.setOtherResourceInfo(isOtherResourceAv);


        dam = rootView.findViewById(R.id.e_dam);
        boolean isDam = dam.isActivated();
        serviceAccess.setDam(isDam);

        well = rootView.findViewById(R.id.e_well);
        boolean isWell = well.isActivated();
        serviceAccess.setWell(isWell);

        boreHole = rootView.findViewById(R.id.e_borehole);
        boolean isBoreHole = boreHole.isActivated();
        serviceAccess.setBoreHole(isBoreHole);

        pipeBorne = rootView.findViewById(R.id.e_pipe_borne);
        boolean isPipeBorne = pipeBorne.isActivated();
        serviceAccess.setPipeBorne(isPipeBorne);

        river = rootView.findViewById(R.id.e_river);
        boolean isRiver = river.isActivated();
        serviceAccess.setDryer(isRiver);

        eIrrigation = rootView.findViewById(R.id.e_irrigation);
        boolean isEIrrigation = eIrrigation.isActivated();
        serviceAccess.setIrrigation(isEIrrigation);

        eNone = rootView.findViewById(R.id.e_none);
        boolean isENone = eNone.isActivated();
        serviceAccess.setHasNoWaterSource(isENone);

        eOther = rootView.findViewById(R.id.e_other_main);
        boolean isEOther = eOther.isActivated();
        serviceAccess.setOtherInfo(isEOther);

        safeStorage_text = rootView.findViewById(R.id.safe_storage_text);
        newResources = rootView.findViewById(R.id.new_resources);
        otherwatersource = rootView.findViewById(R.id.other_water_source);

        mPage.getData().putParcelable("serviceAccess", serviceAccess);


        tractors.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    serviceAccess.setTractor(true);
                } else {
                    serviceAccess.setTractor(false);
                }
                mPage.getData().putParcelable("serviceAccess", serviceAccess);
            }
        });

        harvester.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    serviceAccess.setHarvester(true);
                } else {
                    serviceAccess.setHarvester(false);
                }
                mPage.getData().putParcelable("serviceAccess", serviceAccess);
            }
        });

        dryer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    serviceAccess.setDryer(true);
                } else {
                    serviceAccess.setDryer(false);
                }
                mPage.getData().putParcelable("serviceAccess", serviceAccess);
            }
        });

        tresher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    serviceAccess.setTresher(true);
                } else {
                    serviceAccess.setTresher(false);
                }
                mPage.getData().putParcelable("serviceAccess", serviceAccess);
            }
        });

        safeStorage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    safeStorage_text.setVisibility(View.VISIBLE);
                    serviceAccess.setSafeStorage(true);
                    safeStorage_text.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            serviceAccess.setStorageDetails(charSequence.toString());
                            mPage.setData("serviceAccess", serviceAccess);
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                } else {
                    serviceAccess.setSafeStorage(false);
                    safeStorage_text.setText("");
                    safeStorage_text.setVisibility(View.GONE);
                }
                mPage.getData().putParcelable("serviceAccess", serviceAccess);
            }
        });
        otherResourcesAvailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    newResources.setVisibility(View.VISIBLE);
                    serviceAccess.setOtherResourceInfo(true);
                    newResources.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            serviceAccess.setNewResourcesDetails(charSequence.toString());
                            mPage.setData("serviceAccess", serviceAccess);
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                } else {
                    serviceAccess.setOtherResourceInfo(false);
                    newResources.setText("");
                    newResources.setVisibility(View.GONE);
                }
                mPage.getData().putParcelable("serviceAccess", serviceAccess);
            }
        });

        dam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    serviceAccess.setDam(true);
                } else {
                    serviceAccess.setDam(false);
                }
                mPage.getData().putParcelable("serviceAccess", serviceAccess);
            }
        });

        well.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    serviceAccess.setWell(true);
                } else {
                    serviceAccess.setWell(true);
                }
                mPage.getData().putParcelable("serviceAccess", serviceAccess);
            }
        });

        boreHole.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    serviceAccess.setBoreHole(true);
                } else {
                    serviceAccess.setBoreHole(false);
                }
                mPage.getData().putParcelable("serviceAccess", serviceAccess);
            }
        });

        pipeBorne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    serviceAccess.setPipeBorne(true);
                } else {
                    serviceAccess.setPipeBorne(false);
                }
                mPage.getData().putParcelable("serviceAccess", serviceAccess);
            }
        });

        river.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    serviceAccess.setRiverStream(true);
                } else {
                    serviceAccess.setRiverStream(false);
                }
                mPage.getData().putParcelable("serviceAccess", serviceAccess);
            }
        });

        eIrrigation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    serviceAccess.setIrrigation(true);
                } else {
                    serviceAccess.setIrrigation(false);
                }
                mPage.getData().putParcelable("serviceAccess", serviceAccess);
            }
        });

        eNone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    serviceAccess.setHasNoWaterSource(true);
                } else {
                    serviceAccess.setHasNoWaterSource(false);
                }
                mPage.getData().putParcelable("serviceAccess", serviceAccess);
            }
        });

        eOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    otherwatersource.setVisibility(View.VISIBLE);
                    serviceAccess.setOtherInfo(true);
                    otherwatersource.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            serviceAccess.setMainWaterSourceDetails(charSequence.toString());
                            mPage.setData("serviceAccess", serviceAccess);
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                } else {
                    otherwatersource.setText("");
                    otherwatersource.setVisibility(View.GONE);
                    serviceAccess.setOtherInfo(false);
                }
                mPage.getData().putParcelable("serviceAccess", serviceAccess);
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
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
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.ServiceAccess;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.ui.PageFragmentCallbacks;

public class UpdateServiceFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String ARG_KEY = "key";
    private String mKey;
    private Page mPage;
    private PageFragmentCallbacks mCallbacks;
    private ServiceAccess serviceAccess = new ServiceAccess();

    private Uri mUri;
    private int mFarmerId;

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


    public UpdateServiceFragment() {
        super();
    }

    public static UpdateServiceFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UpdateServiceFragment fragment = new UpdateServiceFragment();
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
            int isTract = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_TRACTORS));
            int harv = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_HARVESTER));
            int cDryer = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_DRYER));
            int cTresher = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_TRESHER));
            int safeStor = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_SAFE_STORAGE));
            int otherInfo = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_OTHER_INFO));
            int cDam = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_DAM));
            int cwell = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_WELL));
            int cborehole = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_BOREHOLE));
            int cpipeBorne = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_PIPE_BORNE));
            int criverStream = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_RIVER_STREAM));
            int cirrig = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_IRRIGATION));
            int cnoWater = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_NONE));
            int cother = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_OTHER));

            String storageDetail = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_STORAGE_DETAIL));
            String otherResources = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_NEW_SOURCE_DETAIL));
            String otherWater = data.getString(data.getColumnIndex(BfwContract.Farmer.COLUMN_WATER_SOURCE_DETAILS));

            if (storageDetail == null || storageDetail.equals("null")) {
                safeStorage_text.setText("");
                serviceAccess.setStorageDetails(null);
            } else {
                safeStorage_text.setText(storageDetail);
                serviceAccess.setStorageDetails(storageDetail);
            }

            if (otherResources == null || otherResources.equals("null")) {
                newResources.setText("");
                serviceAccess.setNewResourcesDetails(null);
            } else {
                newResources.setText(otherResources);
                serviceAccess.setNewResourcesDetails(otherResources);
            }

            if (otherWater == null || otherWater.equals("null")) {
                otherwatersource.setText("");
                serviceAccess.setMainWaterSourceDetails(null);
            } else {
                otherwatersource.setText(otherWater);
                serviceAccess.setMainWaterSourceDetails(otherWater);
            }


            if (isTract == 1) {
                tractors.setChecked(true);
                serviceAccess.setTractor(false);
            } else {
                serviceAccess.setTractor(false);
            }

            if (harv == 1) {
                harvester.setChecked(true);
                serviceAccess.setHarvester(true);
            } else {
                serviceAccess.setHarvester(false);
            }

            if (cDryer == 1) {
                dryer.setChecked(true);
                serviceAccess.setDryer(true);
            } else {
                serviceAccess.setDryer(false);
            }

            if (cTresher == 1) {
                tresher.setChecked(true);
                serviceAccess.setTresher(true);
            } else {
                serviceAccess.setTresher(false);
            }

            if (safeStor == 1) {
                safeStorage.setChecked(true);
                serviceAccess.setSafeStorage(true);
            } else {
                serviceAccess.setSafeStorage(false);
            }

            if (otherInfo == 1) {
                otherResourcesAvailable.setChecked(true);
                serviceAccess.setSafeStorage(true);
            } else {
                serviceAccess.setSafeStorage(false);
            }

            if (cDam == 1) {
                dam.setChecked(true);
                serviceAccess.setDam(true);
            } else {
                serviceAccess.setDam(false);
            }

            if (cborehole == 1) {
                boreHole.setChecked(true);
                serviceAccess.setBoreHole(true);
            } else {
                serviceAccess.setBoreHole(false);
            }

            if (cwell == 1) {
                well.setChecked(true);
                serviceAccess.setWell(true);
            } else {
                serviceAccess.setWell(false);
            }

            if (cpipeBorne == 1) {
                pipeBorne.setChecked(true);
                serviceAccess.setPipeBorne(true);
            } else {
                serviceAccess.setWell(false);
            }

            if (criverStream == 1) {
                river.setChecked(true);
                serviceAccess.setRiverStream(true);
            } else {
                serviceAccess.setRiverStream(false);
            }

            if (cirrig == 1) {
                eIrrigation.setChecked(true);
                serviceAccess.setIrrigation(true);
            } else {
                serviceAccess.setIrrigation(false);
            }

            if (cnoWater == 1) {
                eNone.setChecked(true);
                serviceAccess.setHasNoWaterSource(true);
            } else {
                serviceAccess.setHasNoWaterSource(false);
            }

            if (cother == 1) {
                eOther.setChecked(true);
                serviceAccess.setOtherInfo(true);
            } else {
                serviceAccess.setOtherInfo(false);
            }

            mPage.getData().putParcelable("serviceAccess", serviceAccess);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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
                    safeStorage_text.setText("");
                    safeStorage_text.setVisibility(View.GONE);
                    serviceAccess.setSafeStorage(false);
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
                    otherwatersource.setVisibility(View.GONE);
                    otherwatersource.setText("");
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

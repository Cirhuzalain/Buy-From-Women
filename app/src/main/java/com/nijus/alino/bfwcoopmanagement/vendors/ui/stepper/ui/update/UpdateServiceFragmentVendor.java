package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui.update;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.PageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.ServiceAccessVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui.PageFragmentCallbacksVendor;

public class UpdateServiceFragmentVendor extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String ARG_KEY = "key";
    private String mKey;
    private PageVendorVendor mPageVendor;
    private PageFragmentCallbacksVendor mCallbacks;
    private ServiceAccessVendor serviceAccessVendor = new ServiceAccessVendor();

    private Uri mUri;
    private long mFarmerId;

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
    private AutoCompleteTextView other_text;
    private AutoCompleteTextView other_text2;


    public UpdateServiceFragmentVendor() {
        super();
    }

    public static UpdateServiceFragmentVendor create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UpdateServiceFragmentVendor fragment = new UpdateServiceFragmentVendor();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPageVendor = mCallbacks.onGetPage(mKey);

        Intent intent = getActivity().getIntent();

        if (intent.hasExtra("farmerId")) {
            mFarmerId = intent.getLongExtra("farmerId", 0);
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
            /*int aggriExt = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_AGRI_EXTENSION_SERV));
            int cInfo = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_CLIMATE_RELATED_INFO));
            int seedInf = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_SEEDS));
            int organicFert = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_ORGANIC_FERTILIZER));
            int inorganicFert = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_INORGANIC_FERTILIZER));
            int isLabour = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_LABOUR));
            int isWp = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_WATER_PUMPS));*/
            int isTract = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_TRACTORS));
            int harv = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_HARVESTER));
            //int cSprayer = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_SPRAYERS));
            int cDryer = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_DRYER));
            int cTresher = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_TRESHER));
            int safeStor = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_SAFE_STORAGE));
            int otherInfo = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_OTHER_INFO));
            int cDam = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_DAM));
            int cwell = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_WELL));
            int cborehole = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_BORHOLE));
            int cpipeBorne = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_PIPE_BORNE));
            int criverStream = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_RIVER_STREAM));
            int cirrig = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_IRRIGATION));
            int cnoWater = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_NONE));
            int cother = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_OTHER));



            if (isTract == 1) {
                tractors.setChecked(true);
                serviceAccessVendor.setTractor(false);
            } else {
                serviceAccessVendor.setTractor(false);
            }

            if (harv == 1) {
                harvester.setChecked(true);
                serviceAccessVendor.setHarvester(true);
            } else {
                serviceAccessVendor.setHarvester(false);
            }

            if (cDryer == 1) {
                dryer.setChecked(true);
                serviceAccessVendor.setDryer(true);
            } else {
                serviceAccessVendor.setDryer(false);
            }

            if (cTresher == 1) {
                tresher.setChecked(true);
                serviceAccessVendor.setTresher(true);
            } else {
                serviceAccessVendor.setTresher(false);
            }

            if (safeStor == 1) {
                safeStorage.setChecked(true);
                serviceAccessVendor.setSafeStorage(true);
            } else {
                serviceAccessVendor.setSafeStorage(false);
            }

            if (otherInfo == 1) {
                otherResourcesAvailable.setChecked(true);
                serviceAccessVendor.setSafeStorage(true);
            } else {
                serviceAccessVendor.setSafeStorage(false);
            }

            if (cDam == 1) {
                dam.setChecked(true);
                serviceAccessVendor.setDam(true);
            } else {
                serviceAccessVendor.setDam(false);
            }

            if (cborehole == 1) {
                boreHole.setChecked(true);
                serviceAccessVendor.setBoreHole(true);
            } else {
                serviceAccessVendor.setBoreHole(false);
            }

            if (cwell == 1) {
                well.setChecked(true);
                serviceAccessVendor.setWell(true);
            } else {
                serviceAccessVendor.setWell(false);
            }

            if (cpipeBorne == 1) {
                pipeBorne.setChecked(true);
                serviceAccessVendor.setPipeBorne(true);
            } else {
                serviceAccessVendor.setWell(false);
            }

            if (criverStream == 1) {
                river.setChecked(true);
                serviceAccessVendor.setRiverStream(true);
            } else {
                serviceAccessVendor.setRiverStream(false);
            }

            if (cirrig == 1) {
                eIrrigation.setChecked(true);
                serviceAccessVendor.setIrrigation(true);
            } else {
                serviceAccessVendor.setIrrigation(false);
            }

            if (cnoWater == 1) {
                eNone.setChecked(true);
                serviceAccessVendor.setHasNoWaterSource(true);
            } else {
                serviceAccessVendor.setHasNoWaterSource(false);
            }

            if (cother == 1) {
                eOther.setChecked(true);
                serviceAccessVendor.setOtherInfo(true);
            } else {
                serviceAccessVendor.setOtherInfo(false);
            }

            mPageVendor.getData().putParcelable("serviceAccessVendor", serviceAccessVendor);
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
        //serviceAccessVendor.setOrganicFertilizers(isEOther);

        //FIND AUTOCOMPLETE
        safeStorage_text = rootView.findViewById(R.id.safe_storage_text);
        other_text= rootView.findViewById(R.id.other_text);
        other_text2= rootView.findViewById(R.id.other2_text);

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
                } else {
                    safeStorage_text.setText("");
                    safeStorage_text.setVisibility(View.GONE);
                    serviceAccessVendor.setSafeStorage(false);
                }
                mPageVendor.getData().putParcelable("serviceAccessVendor", serviceAccessVendor);
            }
        });
        otherResourcesAvailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    other_text.setVisibility(View.VISIBLE);
                    serviceAccessVendor.setOtherResourceInfo(true);
                } else {
                    serviceAccessVendor.setOtherResourceInfo(false);
                    other_text.setText("");
                    other_text.setVisibility(View.GONE);
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
                    other_text2.setVisibility(View.VISIBLE);
                    serviceAccessVendor.setOtherInfo(true);
                } else {
                    other_text2.setVisibility(View.GONE);
                    other_text2.setText("");
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

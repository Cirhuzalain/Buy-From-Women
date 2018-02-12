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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.PageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.AccessToInformationVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui.PageFragmentCallbacksVendor;

public class UpdateAccessInfoFragmentVendor extends Fragment implements AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String ARG_KEY = "key";
    private String mKey;
    private PageVendorVendor mPageVendor;
    private PageFragmentCallbacksVendor mCallbacks;
    private AccessToInformationVendor accessToInformationVendor = new AccessToInformationVendor();

    private Uri mUri;
    private long mFarmerId;

    private CheckBox agriculturalExtension;
    private CheckBox climateInfo;
    private CheckBox seeds;
    private CheckBox organicFertilizers;
    private CheckBox inorganicFertilizers;
    private CheckBox labour;
    private CheckBox irrigation;
    private CheckBox spreaders;
   // private CheckBox eOther;
    private Spinner harvsetSeason;


    public UpdateAccessInfoFragmentVendor() {
        super();
    }

    public static UpdateAccessInfoFragmentVendor create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UpdateAccessInfoFragmentVendor fragment = new UpdateAccessInfoFragmentVendor();
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
            int aggriExt = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_AGRI_EXTENSION_SERV));
            int cInfo = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_CLIMATE_RELATED_INFO));
            int seedInf = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_SEEDS));
            int organicFert = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_ORGANIC_FERTILIZER));
            int inorganicFert = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_INORGANIC_FERTILIZER));
            int isLabour = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_LABOUR));
            int isWp = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_WATER_PUMPS));

            int cSprayer = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_SPRAYERS));


            if (aggriExt == 1) {
                agriculturalExtension.setChecked(true);
                accessToInformationVendor.setAgricultureExtension(true);
            } else {
                accessToInformationVendor.setAgricultureExtension(false);
            }

            if (cInfo == 1) {
                climateInfo.setChecked(true);
                accessToInformationVendor.setClimateRelatedInformation(true);
            } else {
                accessToInformationVendor.setClimateRelatedInformation(false);
            }

            if (seedInf == 1) {
                seeds.setChecked(true);
                accessToInformationVendor.setSeed(true);
            } else {
                accessToInformationVendor.setSeed(false);
            }

            if (organicFert == 1) {
                organicFertilizers.setChecked(true);
                accessToInformationVendor.setOrganicFertilizers(true);
            } else {
                accessToInformationVendor.setInorganicFertilizers(false);
            }

            if (inorganicFert == 1) {
                inorganicFertilizers.setChecked(true);
                accessToInformationVendor.setInorganicFertilizers(true);
            } else {
                accessToInformationVendor.setInorganicFertilizers(false);
            }

            if (isLabour == 1) {
                labour.setChecked(true);
                accessToInformationVendor.setLabour(true);
            } else {
                accessToInformationVendor.setLabour(false);
            }

            if (isWp == 1) {
                irrigation.setChecked(true);
                accessToInformationVendor.setWaterPumps(true);
            } else {
                accessToInformationVendor.setWaterPumps(false);
            }

            if (cSprayer == 1) {
                spreaders.setChecked(true);
                accessToInformationVendor.setSpreaderOrSprayer(true);
            } else {
                accessToInformationVendor.setSpreaderOrSprayer(false);
            }


            mPageVendor.getData().putParcelable("accessToInformationVendor", accessToInformationVendor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.access_to_infolayout, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.access_to_info));
        harvsetSeason = rootView.findViewById(R.id.harvsetSeason);


        agriculturalExtension = rootView.findViewById(R.id.agricultural_ext);
        boolean isAgriExt = agriculturalExtension.isActivated();
        accessToInformationVendor.setAgricultureExtension(isAgriExt);

        climateInfo = rootView.findViewById(R.id.climate_info);
        boolean isClimateInfo = climateInfo.isActivated();
        accessToInformationVendor.setClimateRelatedInformation(isClimateInfo);

        seeds = rootView.findViewById(R.id.seeds);
        boolean isSeeds = seeds.isActivated();
        accessToInformationVendor.setSeed(isSeeds);

        organicFertilizers = rootView.findViewById(R.id.organic_fertilizers);
        boolean isFertilizer = organicFertilizers.isActivated();
        accessToInformationVendor.setOrganicFertilizers(isFertilizer);

        inorganicFertilizers = rootView.findViewById(R.id.inorganic_fertilizers);
        boolean isInorganicFertilizer = inorganicFertilizers.isActivated();
        accessToInformationVendor.setInorganicFertilizers(isInorganicFertilizer);

        labour = rootView.findViewById(R.id.labour);
        boolean isLabour = labour.isActivated();
        accessToInformationVendor.setLabour(isLabour);

        irrigation = rootView.findViewById(R.id.irrigation);
        boolean isIrrigation = irrigation.isActivated();
        accessToInformationVendor.setWaterPumps(isIrrigation);

        spreaders = rootView.findViewById(R.id.spreader);
        boolean isSpreader = spreaders.isActivated();
        accessToInformationVendor.setSpreaderOrSprayer(isSpreader);
        
       /* eOther = rootView.findViewById(R.id.e_other_main);
        boolean isEOther = eOther.isActivated();
        accessToInformationVendor.setOrganicFertilizers(isEOther);*/

        mPageVendor.getData().putParcelable("accessToInformationVendor", accessToInformationVendor);

        agriculturalExtension.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    accessToInformationVendor.setAgricultureExtension(true);
                } else {
                    accessToInformationVendor.setAgricultureExtension(false);
                }
                mPageVendor.getData().putParcelable("accessToInformationVendor", accessToInformationVendor);
            }
        });

        climateInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    accessToInformationVendor.setClimateRelatedInformation(true);
                } else {
                    accessToInformationVendor.setClimateRelatedInformation(false);
                }
                mPageVendor.getData().putParcelable("accessToInformationVendor", accessToInformationVendor);
            }
        });

        seeds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    accessToInformationVendor.setSeed(true);
                } else {
                    accessToInformationVendor.setSeed(false);
                }
                mPageVendor.getData().putParcelable("accessToInformationVendor", accessToInformationVendor);
            }
        });

        organicFertilizers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    accessToInformationVendor.setOrganicFertilizers(true);
                } else {
                    accessToInformationVendor.setOrganicFertilizers(false);
                }
                mPageVendor.getData().putParcelable("accessToInformationVendor", accessToInformationVendor);
            }
        });

        inorganicFertilizers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    accessToInformationVendor.setInorganicFertilizers(true);
                } else {
                    accessToInformationVendor.setInorganicFertilizers(false);
                }
                mPageVendor.getData().putParcelable("accessToInformationVendor", accessToInformationVendor);
            }
        });

        labour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    accessToInformationVendor.setLabour(true);
                } else {
                    accessToInformationVendor.setLabour(false);
                }
                mPageVendor.getData().putParcelable("accessToInformationVendor", accessToInformationVendor);
            }
        });

        irrigation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    accessToInformationVendor.setWaterPumps(true);
                } else {
                    accessToInformationVendor.setWaterPumps(false);
                }
                mPageVendor.getData().putParcelable("accessToInformationVendor", accessToInformationVendor);
            }
        });

        spreaders.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    accessToInformationVendor.setSpreaderOrSprayer(true);
                } else {
                    accessToInformationVendor.setSpreaderOrSprayer(false);
                }
                mPageVendor.getData().putParcelable("accessToInformationVendor", accessToInformationVendor);
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.harverstSeason, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        harvsetSeason.setAdapter(adapter);

        harvsetSeason.setOnItemSelectedListener(this);
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        accessToInformationVendor.setHarvsetSeason(adapterView.getItemAtPosition(i).toString());
        mPageVendor.getData().putParcelable("accessToInformationVendor", accessToInformationVendor);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

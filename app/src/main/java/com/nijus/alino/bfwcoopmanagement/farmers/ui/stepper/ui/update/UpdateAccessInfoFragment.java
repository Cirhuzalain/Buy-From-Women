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
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.AccessToInformation;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.ui.PageFragmentCallbacks;

import java.util.HashMap;

public class UpdateAccessInfoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener {

    public static final String ARG_KEY = "key";
    private String mKey;
    private Page mPage;
    private PageFragmentCallbacks mCallbacks;
    private AccessToInformation accessToInformation = new AccessToInformation();

    private Uri mUri;
    private int mFarmerId;

    private Cursor cursor;
    private String seasonName;
    private int seasonId;
    private HashMap<String, AccessToInformation> seasonAccessToInfo = new HashMap<>();

    private boolean isDataAvailable;

    private CheckBox agriculturalExtension;
    private CheckBox climateInfo;
    private CheckBox seeds;
    private CheckBox organicFertilizers;
    private CheckBox inorganicFertilizers;
    private CheckBox labour;
    private CheckBox irrigation;
    private CheckBox spreaders;

    private Spinner harvsetSeason;


    public UpdateAccessInfoFragment() {
        super();
    }

    public static UpdateAccessInfoFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UpdateAccessInfoFragment fragment = new UpdateAccessInfoFragment();
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
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(1, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.access_to_infolayout, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.access_to_info));
        harvsetSeason = rootView.findViewById(R.id.harvsetSeason);
        harvsetSeason.setOnItemSelectedListener(this);

        populateSpinner();

        agriculturalExtension = rootView.findViewById(R.id.agricultural_ext);
        boolean isAgriExt = agriculturalExtension.isActivated();
        accessToInformation.setAgricultureExtension(isAgriExt);

        climateInfo = rootView.findViewById(R.id.climate_info);
        boolean isClimateInfo = climateInfo.isActivated();
        accessToInformation.setClimateRelatedInformation(isClimateInfo);

        seeds = rootView.findViewById(R.id.seeds);
        boolean isSeeds = seeds.isActivated();
        accessToInformation.setSeed(isSeeds);

        organicFertilizers = rootView.findViewById(R.id.organic_fertilizers);
        boolean isFertilizer = organicFertilizers.isActivated();
        accessToInformation.setOrganicFertilizers(isFertilizer);

        inorganicFertilizers = rootView.findViewById(R.id.inorganic_fertilizers);
        boolean isInorganicFertilizer = inorganicFertilizers.isActivated();
        accessToInformation.setInorganicFertilizers(isInorganicFertilizer);

        labour = rootView.findViewById(R.id.labour);
        boolean isLabour = labour.isActivated();
        accessToInformation.setLabour(isLabour);

        irrigation = rootView.findViewById(R.id.irrigation);
        boolean isIrrigation = irrigation.isActivated();
        accessToInformation.setWaterPumps(isIrrigation);

        spreaders = rootView.findViewById(R.id.spreader);
        boolean isSpreader = spreaders.isActivated();
        accessToInformation.setSpreaderOrSprayer(isSpreader);

        cursor = (Cursor) harvsetSeason.getSelectedItem();
        seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
        accessToInformation.setHarvestSeason(seasonId);

        seasonAccessToInfo.put(seasonName, accessToInformation);

        mPage.getData().putSerializable("accessToInformation", seasonAccessToInfo);

        agriculturalExtension.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvsetSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setAgricultureExtension(true);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setAgricultureExtension(true);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setAgricultureExtension(false);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setAgricultureExtension(false);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                }
                mPage.getData().putSerializable("accessToInformation", seasonAccessToInfo);
            }
        });

        climateInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                cursor = (Cursor) harvsetSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setClimateRelatedInformation(true);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setClimateRelatedInformation(true);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setClimateRelatedInformation(false);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setClimateRelatedInformation(false);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                }
                mPage.getData().putSerializable("accessToInformation", seasonAccessToInfo);
            }
        });

        seeds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                cursor = (Cursor) harvsetSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setSeed(true);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setSeed(true);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setSeed(false);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setSeed(false);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                }
                mPage.getData().putSerializable("accessToInformation", seasonAccessToInfo);
            }
        });

        organicFertilizers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvsetSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setOrganicFertilizers(true);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setOrganicFertilizers(true);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setOrganicFertilizers(false);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setOrganicFertilizers(false);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                }
                mPage.getData().putSerializable("accessToInformation", seasonAccessToInfo);
            }
        });

        inorganicFertilizers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvsetSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setInorganicFertilizers(true);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setInorganicFertilizers(true);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setInorganicFertilizers(false);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setInorganicFertilizers(false);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                }
                mPage.getData().putSerializable("accessToInformation", seasonAccessToInfo);

            }
        });

        labour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvsetSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setLabour(true);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setLabour(true);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setLabour(false);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setHarvestSeason(seasonId);
                        information.setLabour(false);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                }
                mPage.getData().putSerializable("accessToInformation", seasonAccessToInfo);
            }
        });

        irrigation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvsetSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setWaterPumps(true);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setWaterPumps(true);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setWaterPumps(false);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setWaterPumps(false);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                }
                mPage.getData().putSerializable("accessToInformation", seasonAccessToInfo);
            }
        });

        spreaders.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                cursor = (Cursor) harvsetSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setSpreaderOrSprayer(true);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setSpreaderOrSprayer(true);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setSpreaderOrSprayer(false);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setSpreaderOrSprayer(false);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                }
                mPage.getData().putSerializable("accessToInformation", seasonAccessToInfo);
            }
        });

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String farmerSelection = BfwContract.Farmer.TABLE_NAME + "." +
                BfwContract.Farmer._ID + " =  ? ";


        mUri = BfwContract.FarmerAccessInfo.buildFarmerAccessInfoUri(mFarmerId);

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
        if (data != null) {

            int isAgricultureExtension;
            int isClimateRelatedInformation;
            int isSeed;
            int isOrganicFertilizers;
            int isInorganicFertilizers;
            int isLabour;
            int isWaterPumps;
            int isSpreaderOrSprayer;
            int seasonId;
            int accessInfoId;

            String seasonName;

            Cursor seasonCursor = null;
            String farmerSelection = BfwContract.HarvestSeason.TABLE_NAME
                    + "." + BfwContract.HarvestSeason._ID
                    + " = ?";

            while (data.moveToNext()) {

                isAgricultureExtension = data.getInt(data.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_AGRI_EXTENSION_SERV));
                isClimateRelatedInformation = data.getInt(data.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_CLIMATE_RELATED_INFO));
                isSeed = data.getInt(data.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_SEEDS));
                isOrganicFertilizers = data.getInt(data.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_ORGANIC_FERTILIZER));
                isInorganicFertilizers = data.getInt(data.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_INORGANIC_FERTILIZER));
                isLabour = data.getInt(data.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_LABOUR));
                isWaterPumps = data.getInt(data.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_WATER_PUMPS));
                isSpreaderOrSprayer = data.getInt(data.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_SPRAYERS));

                seasonId = data.getInt(data.getColumnIndex(BfwContract.FarmerAccessInfo.COLUMN_SEASON_ID));
                accessInfoId = data.getInt(data.getColumnIndex(BfwContract.FarmerAccessInfo._ID));

                try {
                    seasonCursor = getActivity().getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, farmerSelection,
                            new String[]{Integer.toString(seasonId)}, null);

                    if (seasonCursor != null) {
                        seasonCursor.moveToFirst();
                        seasonName = seasonCursor.getString(seasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                        accessToInformation = new AccessToInformation(isAgricultureExtension == 1, isClimateRelatedInformation == 1,
                                isSeed == 1, isOrganicFertilizers == 1, isInorganicFertilizers == 1,
                                isLabour == 1, isWaterPumps == 1, isSpreaderOrSprayer == 1, seasonId);
                        accessToInformation.setAccessInfoId(accessInfoId);
                        seasonAccessToInfo.put(seasonName, accessToInformation);
                        isDataAvailable = true;
                    }
                } finally {
                    if (seasonCursor != null) {
                        seasonCursor.close();
                    }
                }

            }

            //set field to default value value inside the spinner
            cursor = (Cursor) harvsetSeason.getSelectedItem();
            setAccessInfoFarmerItem(cursor);
            mPage.getData().putSerializable("accessToInformation", seasonAccessToInfo);
        }
    }

    public void setAccessInfoFarmerItem(Cursor cursor) {

        seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

        if (isDataAvailable && seasonAccessToInfo.containsKey(seasonName)) {

            boolean isAes = seasonAccessToInfo.get(seasonName).isAgricultureExtension();
            boolean isCri = seasonAccessToInfo.get(seasonName).isClimateRelatedInformation();
            boolean isSeed = seasonAccessToInfo.get(seasonName).isSeed();
            boolean isOfert = seasonAccessToInfo.get(seasonName).isOrganicFertilizers();
            boolean isInorgFert = seasonAccessToInfo.get(seasonName).isInorganicFertilizers();
            boolean islabour = seasonAccessToInfo.get(seasonName).isLabour();
            boolean isWp = seasonAccessToInfo.get(seasonName).isWaterPumps();
            boolean isSS = seasonAccessToInfo.get(seasonName).isSpreaderOrSprayer();

            agriculturalExtension.setChecked(isAes);
            climateInfo.setChecked(isCri);
            seeds.setChecked(isSeed);
            organicFertilizers.setChecked(isOfert);
            inorganicFertilizers.setChecked(isInorgFert);
            labour.setChecked(islabour);
            irrigation.setChecked(isWp);
            spreaders.setChecked(isSS);

        } else {
            agriculturalExtension.setChecked(false);
            climateInfo.setChecked(false);
            seeds.setChecked(false);
            organicFertilizers.setChecked(false);
            inorganicFertilizers.setChecked(false);
            labour.setChecked(false);
            irrigation.setChecked(false);
            spreaders.setChecked(false);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        Cursor cursor = (Cursor) harvsetSeason.getSelectedItem();
        setAccessInfoFarmerItem(cursor);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void populateSpinner() {
        String[] fromColumns = {BfwContract.HarvestSeason.COLUMN_NAME};

        // View IDs to map the columns (fetched above) into
        int[] toViews = {
                android.R.id.text1
        };
        Cursor cursor = getActivity().getContentResolver().query(
                BfwContract.HarvestSeason.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        if (cursor != null) {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    getContext(), // context
                    android.R.layout.simple_spinner_item, // layout file
                    cursor, // DB cursor
                    fromColumns, // data to bind to the UI
                    toViews, // views that'll represent the data from `fromColumns`
                    0
            );

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Create the list view and bind the adapter
            harvsetSeason.setAdapter(adapter);
        }
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

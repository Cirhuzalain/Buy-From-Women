package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.AccessToInformation;

import com.nijus.alino.bfwcoopmanagement.data.BfwContract;

import java.util.HashMap;

public class AccessToInformationFragment extends Fragment {

    public static final String ARG_KEY = "key";
    private String mKey;
    private Page mPage;
    private PageFragmentCallbacks mCallbacks;
    private AccessToInformation accessToInformation = new AccessToInformation();

    private Cursor cursor;
    private String seasonName;
    private int seasonId;
    private HashMap<String, AccessToInformation> seasonAccessToInfo = new HashMap<>();

    private CheckBox agriculturalExtension;
    private CheckBox climateInfo;
    private CheckBox seeds;
    private CheckBox organicFertilizers;
    private CheckBox inorganicFertilizers;
    private CheckBox labour;
    private CheckBox irrigation;
    private CheckBox spreaders;
    private Spinner harvestSeason;


    public AccessToInformationFragment() {
        super();
    }

    public static AccessToInformationFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        AccessToInformationFragment fragment = new AccessToInformationFragment();
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
        View rootView = inflater.inflate(R.layout.access_to_info_coop_layout, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.access_to_info));

        harvestSeason = rootView.findViewById(R.id.harvestSeason);

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
        accessToInformation.setSeed(isSpreader);

        cursor = (Cursor) harvestSeason.getSelectedItem();
        seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
        accessToInformation.setSeasonId(seasonId);

        seasonAccessToInfo.put(seasonName, accessToInformation);


        mPage.getData().putSerializable("accessToInformation", seasonAccessToInfo);

        agriculturalExtension.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setAgricultureExtension(true);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setAgricultureExtension(true);
                        information.setSeasonId(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setAgricultureExtension(false);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setAgricultureExtension(false);
                        information.setSeasonId(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                }
                mPage.getData().putSerializable("accessToInformation", seasonAccessToInfo);
            }
        });

        climateInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setClimateRelatedInformation(true);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setClimateRelatedInformation(true);
                        information.setSeasonId(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setClimateRelatedInformation(false);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setClimateRelatedInformation(false);
                        information.setSeasonId(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                }
                mPage.getData().putSerializable("accessToInformation", seasonAccessToInfo);
            }
        });

        seeds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setSeed(true);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setSeed(true);
                        information.setSeasonId(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setSeed(false);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setSeed(false);
                        information.setSeasonId(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                }
                mPage.getData().putSerializable("accessToInformation", seasonAccessToInfo);
            }
        });

        organicFertilizers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setOrganicFertilizers(true);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setOrganicFertilizers(true);
                        information.setSeasonId(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setOrganicFertilizers(false);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setOrganicFertilizers(false);
                        information.setSeasonId(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                }
                mPage.getData().putSerializable("accessToInformation", seasonAccessToInfo);
            }
        });

        inorganicFertilizers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setInorganicFertilizers(true);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setInorganicFertilizers(true);
                        information.setSeasonId(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setInorganicFertilizers(false);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setInorganicFertilizers(false);
                        information.setSeasonId(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                }
                mPage.getData().putSerializable("accessToInformation", seasonAccessToInfo);
            }
        });

        labour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setLabour(true);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setLabour(true);
                        information.setSeasonId(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setLabour(false);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setSeasonId(seasonId);
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

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setLabour(true);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setWaterPumps(true);
                        information.setSeasonId(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setLabour(false);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setSeasonId(seasonId);
                        information.setWaterPumps(false);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                }
                mPage.getData().putSerializable("accessToInformation", seasonAccessToInfo);
            }
        });

        spreaders.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setSpreaderOrSprayer(true);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setSpreaderOrSprayer(true);
                        information.setSeasonId(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setSpreaderOrSprayer(false);
                    } else {
                        AccessToInformation information = new AccessToInformation();
                        information.setSpreaderOrSprayer(false);
                        information.setSeasonId(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                }
                mPage.getData().putSerializable("accessToInformation", seasonAccessToInfo);
            }
        });


        return rootView;
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
            harvestSeason.setAdapter(adapter);
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

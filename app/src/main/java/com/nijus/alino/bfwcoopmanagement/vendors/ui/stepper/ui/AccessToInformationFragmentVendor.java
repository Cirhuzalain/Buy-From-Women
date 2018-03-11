package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui;

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
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.PageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.AccessToInformationVendor;

import java.util.HashMap;

public class AccessToInformationFragmentVendor extends Fragment {

    public static final String ARG_KEY = "key";
    private String mKey;
    private PageVendorVendor mPageVendor;
    private PageFragmentCallbacksVendor mCallbacks;
    private AccessToInformationVendor accessInfo = new AccessToInformationVendor();

    private Cursor cursor;
    private String seasonName;
    private int seasonId;
    private HashMap<String, AccessToInformationVendor> seasonAccessToInfo = new HashMap<>();

    private CheckBox agriculturalExtension;
    private CheckBox climateInfo;
    private CheckBox seeds;
    private CheckBox organicFertilizers;
    private CheckBox inorganicFertilizers;
    private CheckBox labour;
    private CheckBox irrigation;
    private CheckBox spreaders;
    private Spinner harvsetSeason;

    public AccessToInformationFragmentVendor() {
        super();
    }

    public static AccessToInformationFragmentVendor create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        AccessToInformationFragmentVendor fragment = new AccessToInformationFragmentVendor();
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
        View rootView = inflater.inflate(R.layout.access_to_infolayout, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.access_to_info));

        harvsetSeason = rootView.findViewById(R.id.harvsetSeason);

        populateSpinner();


        agriculturalExtension = rootView.findViewById(R.id.agricultural_ext);
        boolean isAgriExt = agriculturalExtension.isActivated();

        accessInfo.setAgricultureExtension(isAgriExt);

        climateInfo = rootView.findViewById(R.id.climate_info);
        boolean isClimateInfo = climateInfo.isActivated();
        accessInfo.setClimateRelatedInformation(isClimateInfo);

        seeds = rootView.findViewById(R.id.seeds);
        boolean isSeeds = seeds.isActivated();
        accessInfo.setSeed(isSeeds);

        organicFertilizers = rootView.findViewById(R.id.organic_fertilizers);
        boolean isFertilizer = organicFertilizers.isActivated();
        accessInfo.setOrganicFertilizers(isFertilizer);

        inorganicFertilizers = rootView.findViewById(R.id.inorganic_fertilizers);
        boolean isInorganicFertilizer = inorganicFertilizers.isActivated();
        accessInfo.setInorganicFertilizers(isInorganicFertilizer);

        labour = rootView.findViewById(R.id.labour);
        boolean isLabour = labour.isActivated();
        accessInfo.setLabour(isLabour);

        irrigation = rootView.findViewById(R.id.irrigation);
        boolean isIrrigation = irrigation.isActivated();
        accessInfo.setWaterPumps(isIrrigation);

        spreaders = rootView.findViewById(R.id.spreader);
        boolean isSpreader = spreaders.isActivated();
        accessInfo.setSpreaderOrSprayer(isSpreader);

        cursor = (Cursor) harvsetSeason.getSelectedItem();
        seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
        accessInfo.setHarvestSeason(seasonId);

        seasonAccessToInfo.put(seasonName, accessInfo);


        mPageVendor.getData().putSerializable("accessToInformationVendor", seasonAccessToInfo);

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
                        AccessToInformationVendor information = new AccessToInformationVendor();
                        information.setAgricultureExtension(true);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setAgricultureExtension(false);
                    } else {
                        AccessToInformationVendor information = new AccessToInformationVendor();
                        information.setAgricultureExtension(false);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                }
                mPageVendor.getData().putSerializable("accessToInformationVendor", seasonAccessToInfo);
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
                        AccessToInformationVendor information = new AccessToInformationVendor();
                        information.setClimateRelatedInformation(true);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setClimateRelatedInformation(false);
                    } else {
                        AccessToInformationVendor information = new AccessToInformationVendor();
                        information.setClimateRelatedInformation(false);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                }
                mPageVendor.getData().putSerializable("accessToInformationVendor", seasonAccessToInfo);
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
                        AccessToInformationVendor information = new AccessToInformationVendor();
                        information.setSeed(true);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setSeed(false);
                    } else {
                        AccessToInformationVendor information = new AccessToInformationVendor();
                        information.setSeed(false);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                }
                mPageVendor.getData().putSerializable("accessToInformationVendor", seasonAccessToInfo);
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
                        AccessToInformationVendor information = new AccessToInformationVendor();
                        information.setOrganicFertilizers(true);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setOrganicFertilizers(false);
                    } else {
                        AccessToInformationVendor information = new AccessToInformationVendor();
                        information.setOrganicFertilizers(false);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                }
                mPageVendor.getData().putSerializable("accessToInformationVendor", seasonAccessToInfo);
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
                        AccessToInformationVendor information = new AccessToInformationVendor();
                        information.setInorganicFertilizers(true);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setInorganicFertilizers(false);
                    } else {
                        AccessToInformationVendor information = new AccessToInformationVendor();
                        information.setInorganicFertilizers(false);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                }
                mPageVendor.getData().putSerializable("accessToInformationVendor", seasonAccessToInfo);

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
                        AccessToInformationVendor information = new AccessToInformationVendor();
                        information.setLabour(true);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setLabour(false);
                    } else {
                        AccessToInformationVendor information = new AccessToInformationVendor();
                        information.setHarvestSeason(seasonId);
                        information.setLabour(false);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                }
                mPageVendor.getData().putSerializable("accessToInformationVendor", seasonAccessToInfo);
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
                        AccessToInformationVendor information = new AccessToInformationVendor();
                        information.setWaterPumps(true);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setWaterPumps(false);
                    } else {
                        AccessToInformationVendor information = new AccessToInformationVendor();
                        information.setWaterPumps(false);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                }
                mPageVendor.getData().putSerializable("accessToInformationVendor", seasonAccessToInfo);
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
                        AccessToInformationVendor information = new AccessToInformationVendor();
                        information.setSpreaderOrSprayer(true);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                } else {
                    if (seasonAccessToInfo.containsKey(seasonName)) {
                        seasonAccessToInfo.get(seasonName).setSpreaderOrSprayer(false);
                    } else {
                        AccessToInformationVendor information = new AccessToInformationVendor();
                        information.setSpreaderOrSprayer(false);
                        information.setHarvestSeason(seasonId);
                        seasonAccessToInfo.put(seasonName, information);
                    }
                }
                mPageVendor.getData().putSerializable("accessToInformationVendor", seasonAccessToInfo);
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
            harvsetSeason.setAdapter(adapter);
        }
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

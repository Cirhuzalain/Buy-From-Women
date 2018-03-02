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
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.BaselineYield;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;

import java.util.HashMap;

public class BaselineYieldFragment extends Fragment {

    public static final String ARG_KEY = "key";
    public final String LOG_TAG = BaselineYieldFragment.class.getSimpleName();
    private String mKey;

    private BaselineYield baselineYield = new BaselineYield();

    private Page mPage;
    private PageFragmentCallbacks mCallbacks;

    private Spinner harvestSeason;

    private Cursor cursor;
    private String seasonName;
    private int seasonId;
    private HashMap<String, BaselineYield> baselineYieldSeason = new HashMap<>();

    private CheckBox maize;
    private CheckBox bean;
    private CheckBox soy;
    private CheckBox other;

    public BaselineYieldFragment() {
        super();
    }

    public static BaselineYieldFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        BaselineYieldFragment fragment = new BaselineYieldFragment();
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
        View rootView = inflater.inflate(R.layout.baseline_yield, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.baseline_yield));

        harvestSeason = rootView.findViewById(R.id.harvestSeason);

        populateSpinner();

        maize = rootView.findViewById(R.id.maize);
        boolean isMaize = maize.isActivated();
        baselineYield.setMaize(isMaize);

        bean = rootView.findViewById(R.id.bean);
        boolean isBean = bean.isActivated();
        baselineYield.setBean(isBean);

        soy = rootView.findViewById(R.id.soy);
        boolean isSoy = soy.isActivated();
        baselineYield.setSoy(isSoy);

        other = rootView.findViewById(R.id.other);
        boolean isother = other.isActivated();
        baselineYield.setOther(isother);

        cursor = (Cursor) harvestSeason.getSelectedItem();
        seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

        baselineYieldSeason.put(seasonName, baselineYield);

        mPage.getData().putSerializable("baselines_yield", baselineYieldSeason);


        maize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (baselineYieldSeason.containsKey(seasonName)) {
                        baselineYieldSeason.get(seasonName).setMaize(true);
                    } else {
                        BaselineYield baselineYield = new BaselineYield();
                        baselineYield.setMaize(true);
                        baselineYield.setSeasonId(seasonId);
                        baselineYieldSeason.put(seasonName, baselineYield);
                    }
                } else {
                    if (baselineYieldSeason.containsKey(seasonName)) {
                        baselineYieldSeason.get(seasonName).setMaize(false);
                    } else {
                        BaselineYield baselineYield = new BaselineYield();
                        baselineYield.setMaize(false);
                        baselineYield.setSeasonId(seasonId);
                        baselineYieldSeason.put(seasonName, baselineYield);
                    }
                }
                mPage.getData().putSerializable("baselines_yield", baselineYieldSeason);
            }
        });

        soy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (baselineYieldSeason.containsKey(seasonName)) {
                        baselineYieldSeason.get(seasonName).setSoy(true);
                    } else {
                        BaselineYield baselineYield = new BaselineYield();
                        baselineYield.setSoy(true);
                        baselineYield.setSeasonId(seasonId);
                        baselineYieldSeason.put(seasonName, baselineYield);
                    }
                } else {
                    if (baselineYieldSeason.containsKey(seasonName)) {
                        baselineYieldSeason.get(seasonName).setSoy(false);
                    } else {
                        BaselineYield baselineYield = new BaselineYield();
                        baselineYield.setSoy(false);
                        baselineYield.setSeasonId(seasonId);
                        baselineYieldSeason.put(seasonName, baselineYield);
                    }
                }
                mPage.getData().putSerializable("baselines_yield", baselineYieldSeason);
            }
        });

        bean.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (baselineYieldSeason.containsKey(seasonName)) {
                        baselineYieldSeason.get(seasonName).setBean(true);
                    } else {
                        BaselineYield baselineYield = new BaselineYield();
                        baselineYield.setBean(true);
                        baselineYield.setSeasonId(seasonId);
                        baselineYieldSeason.put(seasonName, baselineYield);
                    }
                } else {
                    if (baselineYieldSeason.containsKey(seasonName)) {
                        baselineYieldSeason.get(seasonName).setBean(false);
                    } else {
                        BaselineYield baselineYield = new BaselineYield();
                        baselineYield.setBean(false);
                        baselineYield.setSeasonId(seasonId);
                        baselineYieldSeason.put(seasonName, baselineYield);
                    }
                }
                mPage.getData().putSerializable("baselines_yield", baselineYieldSeason);
            }
        });

        other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (baselineYieldSeason.containsKey(seasonName)) {
                        baselineYieldSeason.get(seasonName).setOther(true);
                    } else {
                        BaselineYield baselineYield = new BaselineYield();
                        baselineYield.setOther(true);
                        baselineYield.setSeasonId(seasonId);
                        baselineYieldSeason.put(seasonName, baselineYield);
                    }
                } else {
                    if (baselineYieldSeason.containsKey(seasonName)) {
                        baselineYieldSeason.get(seasonName).setOther(false);
                    } else {
                        BaselineYield baselineYield = new BaselineYield();
                        baselineYield.setOther(false);
                        baselineYield.setSeasonId(seasonId);
                        baselineYieldSeason.put(seasonName, baselineYield);
                    }
                }
                mPage.getData().putSerializable("baselines_yield", baselineYieldSeason);
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
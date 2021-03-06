package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.PageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.BaseLineVendor;

import java.util.HashMap;

public class BaseLineFragmentVendor extends Fragment {

    public static final String ARG_KEY = "key";
    private String mKey;
    private PageVendorVendor mPage;
    private PageFragmentCallbacksVendor mCallbacks;

    private Cursor cursor;
    private String seasonName;
    private int seasonId;
    private HashMap<String, BaseLineVendor> seasonBaseline = new HashMap<>();

    private AutoCompleteTextView totProdKg;
    private AutoCompleteTextView totLostKg;
    private AutoCompleteTextView totSoldKg;
    private AutoCompleteTextView totVolSoldCoops;
    private AutoCompleteTextView priceSoldToCoop;
    private AutoCompleteTextView totVolSoldKg;
    private AutoCompleteTextView priceSoldKg;
    private Spinner harvsetSeason;


    public BaseLineFragmentVendor() {
        super();
    }

    public static BaseLineFragmentVendor create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        BaseLineFragmentVendor fragment = new BaseLineFragmentVendor();
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
        View rootView = inflater.inflate(R.layout.baseline_layout, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.base_line));

        totProdKg = rootView.findViewById(R.id.tot_prod_kg);
        totLostKg = rootView.findViewById(R.id.tot_lost);
        totSoldKg = rootView.findViewById(R.id.tot_sold);
        totVolSoldCoops = rootView.findViewById(R.id.tot_vol_coop);
        priceSoldToCoop = rootView.findViewById(R.id.price_sold_kg);
        totVolSoldKg = rootView.findViewById(R.id.tot_vol_side_sold);
        priceSoldKg = rootView.findViewById(R.id.pr_sold_kg);
        harvsetSeason = rootView.findViewById(R.id.harvsetSeason);

        populateSpinner();

        //set default value
        cursor = (Cursor) harvsetSeason.getSelectedItem();
        seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
        BaseLineVendor baseLine = new BaseLineVendor(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, seasonId);
        seasonBaseline.put(seasonName, baseLine);

        totProdKg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    cursor = (Cursor) harvsetSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                    seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                    if (seasonBaseline.containsKey(seasonName)) {
                        seasonBaseline.get(seasonName).setTotProdInKg(Double.parseDouble(charSequence.toString()));
                    } else {
                        BaseLineVendor baseLine = new BaseLineVendor();
                        baseLine.setTotProdInKg(Double.parseDouble(charSequence.toString()));
                        baseLine.setHarvestSeason(seasonId);
                        seasonBaseline.put(seasonName, baseLine);
                    }

                    mPage.getData().putSerializable("baselineVendor", seasonBaseline);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        totLostKg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {

                    cursor = (Cursor) harvsetSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                    seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                    if (seasonBaseline.containsKey(seasonName)) {
                        seasonBaseline.get(seasonName).setTotLostInKg(Double.parseDouble(charSequence.toString()));
                    } else {
                        BaseLineVendor baseLine = new BaseLineVendor();
                        baseLine.setTotLostInKg(Double.parseDouble(charSequence.toString()));
                        baseLine.setHarvestSeason(seasonId);
                        seasonBaseline.put(seasonName, baseLine);
                    }

                    mPage.getData().putSerializable("baselineVendor", seasonBaseline);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        totSoldKg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {

                    cursor = (Cursor) harvsetSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                    seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                    if (seasonBaseline.containsKey(seasonName)) {
                        seasonBaseline.get(seasonName).setTotSoldInKg(Double.parseDouble(charSequence.toString()));
                    } else {
                        BaseLineVendor baseLine = new BaseLineVendor();
                        baseLine.setTotSoldInKg(Double.parseDouble(charSequence.toString()));
                        baseLine.setHarvestSeason(seasonId);
                        seasonBaseline.put(seasonName, baseLine);
                    }
                    mPage.getData().putSerializable("baselineVendor", seasonBaseline);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        totVolSoldCoops.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {

                    cursor = (Cursor) harvsetSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                    seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                    if (seasonBaseline.containsKey(seasonName)) {
                        seasonBaseline.get(seasonName).setTotVolumeSoldCoopInKg(Double.parseDouble(charSequence.toString()));
                    } else {
                        BaseLineVendor baseLine = new BaseLineVendor();
                        baseLine.setTotVolumeSoldCoopInKg(Double.parseDouble(charSequence.toString()));
                        baseLine.setHarvestSeason(seasonId);
                        seasonBaseline.put(seasonName, baseLine);
                    }
                    mPage.getData().putSerializable("baselineVendor", seasonBaseline);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        priceSoldToCoop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {

                    cursor = (Cursor) harvsetSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                    seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                    if (seasonBaseline.containsKey(seasonName)) {
                        seasonBaseline.get(seasonName).setPriceSoldToCoopPerKg(Double.parseDouble(charSequence.toString()));
                    } else {
                        BaseLineVendor baseLine = new BaseLineVendor();
                        baseLine.setHarvestSeason(seasonId);
                        baseLine.setPriceSoldToCoopPerKg(Double.parseDouble(charSequence.toString()));
                        seasonBaseline.put(seasonName, baseLine);
                    }
                    mPage.getData().putSerializable("baselineVendor", seasonBaseline);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        totVolSoldKg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {

                    cursor = (Cursor) harvsetSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                    seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                    if (seasonBaseline.containsKey(seasonName)) {
                        seasonBaseline.get(seasonName).setTotVolSoldInKg(Double.parseDouble(charSequence.toString()));
                    } else {
                        BaseLineVendor baseLine = new BaseLineVendor();
                        baseLine.setHarvestSeason(seasonId);
                        baseLine.setTotVolSoldInKg(Double.parseDouble(charSequence.toString()));
                        seasonBaseline.put(seasonName, baseLine);
                    }
                    mPage.getData().putSerializable("baselineVendor", seasonBaseline);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        priceSoldKg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    cursor = (Cursor) harvsetSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                    seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                    if (seasonBaseline.containsKey(seasonName)) {
                        seasonBaseline.get(seasonName).setPriceSoldInKg(Double.parseDouble(charSequence.toString()));
                    } else {
                        BaseLineVendor baseLine = new BaseLineVendor();
                        baseLine.setHarvestSeason(seasonId);
                        baseLine.setPriceSoldInKg(Double.parseDouble(charSequence.toString()));
                        seasonBaseline.put(seasonName, baseLine);
                    }
                    mPage.getData().putSerializable("baselineVendor", seasonBaseline);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

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
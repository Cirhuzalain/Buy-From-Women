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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.BaseLine;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.ui.PageFragmentCallbacks;

import java.util.HashMap;

public class UpdateBaseLineFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener {

    public static final String ARG_KEY = "key";
    private String mKey;
    private Page mPage;
    private PageFragmentCallbacks mCallbacks;
    private Uri mUri;
    private long mFarmerId;

    private Cursor cursor;
    private String seasonName;
    private int seasonId;
    private HashMap<String, BaseLine> seasonBaseline = new HashMap<>();

    private BaseLine baseLine = new BaseLine();
    private Spinner harvsetSeason;
    private boolean isDataAvailable;

    private AutoCompleteTextView totProdKg;
    private AutoCompleteTextView totLostKg;
    private AutoCompleteTextView totSoldKg;
    private AutoCompleteTextView totVolSoldCoops;
    private AutoCompleteTextView priceSoldToCoop;
    private AutoCompleteTextView totVolSoldKg;
    private AutoCompleteTextView priceSoldKg;

    public UpdateBaseLineFragment() {
        super();
    }

    public static UpdateBaseLineFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UpdateBaseLineFragment fragment = new UpdateBaseLineFragment();
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
            mFarmerId = intent.getLongExtra("farmerId", 0);
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

        mUri = BfwContract.BaselineFarmer.buildBaselineFarmerUri(mFarmerId);

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
        harvsetSeason.setOnItemSelectedListener(this);

        populateSpinner();

        //set default value
        cursor = (Cursor) harvsetSeason.getSelectedItem();
        seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
        baseLine = new BaseLine(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, seasonId);
        seasonBaseline.put(seasonName, baseLine);

        mPage.getData().putSerializable("baseline", seasonBaseline);

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
                        BaseLine baseLine = new BaseLine();
                        baseLine.setTotProdInKg(Double.parseDouble(charSequence.toString()));
                        baseLine.setHarvestSeason(seasonId);
                        seasonBaseline.put(seasonName, baseLine);
                    }

                    mPage.getData().putSerializable("baseline", seasonBaseline);
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
                        BaseLine baseLine = new BaseLine();
                        baseLine.setTotLostInKg(Double.parseDouble(charSequence.toString()));
                        baseLine.setHarvestSeason(seasonId);
                        seasonBaseline.put(seasonName, baseLine);
                    }

                    mPage.getData().putSerializable("baseline", seasonBaseline);
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
                        BaseLine baseLine = new BaseLine();
                        baseLine.setTotSoldInKg(Double.parseDouble(charSequence.toString()));
                        baseLine.setHarvestSeason(seasonId);
                        seasonBaseline.put(seasonName, baseLine);
                    }
                    mPage.getData().putSerializable("baseline", seasonBaseline);
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
                        BaseLine baseLine = new BaseLine();
                        baseLine.setTotVolumeSoldCoopInKg(Double.parseDouble(charSequence.toString()));
                        baseLine.setHarvestSeason(seasonId);
                        seasonBaseline.put(seasonName, baseLine);
                    }
                    mPage.getData().putSerializable("baseline", seasonBaseline);
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
                        BaseLine baseLine = new BaseLine();
                        baseLine.setHarvestSeason(seasonId);
                        baseLine.setPriceSoldToCoopPerKg(Double.parseDouble(charSequence.toString()));
                        seasonBaseline.put(seasonName, baseLine);
                    }
                    mPage.getData().putSerializable("baseline", seasonBaseline);
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
                        BaseLine baseLine = new BaseLine();
                        baseLine.setHarvestSeason(seasonId);
                        baseLine.setTotVolSoldInKg(Double.parseDouble(charSequence.toString()));
                        seasonBaseline.put(seasonName, baseLine);
                    }
                    mPage.getData().putSerializable("baseline", seasonBaseline);
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
                        BaseLine baseLine = new BaseLine();
                        baseLine.setHarvestSeason(seasonId);
                        baseLine.setPriceSoldInKg(Double.parseDouble(charSequence.toString()));
                        seasonBaseline.put(seasonName, baseLine);
                    }
                    mPage.getData().putSerializable("baseline", seasonBaseline);
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

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            Double totProd;
            Double totLost;
            Double totSolKg;
            Double totVSoldCoop;
            Double priceSoldCoop;
            Double totVolSKg;
            Double priceSold;

            int seasonId;
            int forecastId;
            String seasonName;

            Cursor seasonCursor = null;
            String farmerSelection = BfwContract.HarvestSeason.TABLE_NAME
                    + "." + BfwContract.HarvestSeason._ID
                    + " = ?";

            while (data.moveToNext()) {
                seasonId = data.getInt(data.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_SEASON_ID));
                forecastId = data.getInt(data.getColumnIndex(BfwContract.BaselineFarmer._ID));

                totProd = data.getDouble(data.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_TOT_PROD_B_KG));
                totLost = data.getDouble(data.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_TOT_LOST_KG));
                totSolKg = data.getDouble(data.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_TOT_SOLD_KG));
                totVSoldCoop = data.getDouble(data.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_TOT_VOL_SOLD_COOP));
                priceSoldCoop = data.getDouble(data.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_PRICE_SOLD_COOP_PER_KG));
                totVolSKg = data.getDouble(data.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_TOT_VOL_SOLD_IN_KG));
                priceSold = data.getDouble(data.getColumnIndex(BfwContract.BaselineFarmer.COLUMN_PRICE_SOLD_KG));

                try {
                    seasonCursor = getActivity().getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, farmerSelection,
                            new String[]{Integer.toString(seasonId)}, null);

                    if (seasonCursor != null) {
                        seasonCursor.moveToFirst();
                        seasonName = seasonCursor.getString(seasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                        baseLine = new BaseLine(totProd, totLost, totSolKg, totVSoldCoop, priceSoldCoop, totVolSKg, priceSold, seasonId);
                        baseLine.setBaselineId(forecastId);
                        seasonBaseline.put(seasonName, baseLine);
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
            setBaselineFarmerItem(cursor);
            mPage.getData().putSerializable("baseline", seasonBaseline);
        }
    }

    private void setBaselineFarmerItem(Cursor cursor) {
        seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

        if (isDataAvailable && seasonBaseline.containsKey(seasonName)) {

            String prodInfo = seasonBaseline.get(seasonName).getTotProdInKg() + "";
            String soldInfo = seasonBaseline.get(seasonName).getTotSoldInKg() + "";
            String lostInfo = seasonBaseline.get(seasonName).getTotLostInKg() + "";
            String volCoopInfo = seasonBaseline.get(seasonName).getTotVolumeSoldCoopInKg() + "";
            String priceCoopInfo = seasonBaseline.get(seasonName).getPriceSoldToCoopPerKg() + "";
            String totVolSoldInfo = seasonBaseline.get(seasonName).getTotVolSoldInKg() + "";
            String priceSoldInfo = seasonBaseline.get(seasonName).getPriceSoldInKg() + "";

            totProdKg.setText(prodInfo);
            totLostKg.setText(lostInfo);
            totSoldKg.setText(soldInfo);
            totVolSoldCoops.setText(volCoopInfo);
            priceSoldToCoop.setText(priceCoopInfo);
            totVolSoldKg.setText(totVolSoldInfo);
            priceSoldKg.setText(priceSoldInfo);

        } else {

            String prodInfo = "";
            String soldInfo = "";
            String lostInfo = "";
            String volCoopInfo = "";
            String priceCoopInfo = "";
            String totVolSoldInfo = "";
            String priceSoldInfo = "";

            totProdKg.setText(prodInfo);
            totLostKg.setText(lostInfo);
            totSoldKg.setText(soldInfo);
            totVolSoldCoops.setText(volCoopInfo);
            priceSoldToCoop.setText(priceCoopInfo);
            totVolSoldKg.setText(totVolSoldInfo);
            priceSoldKg.setText(priceSoldInfo);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Cursor cursor = (Cursor) harvsetSeason.getSelectedItem();
        setBaselineFarmerItem(cursor);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.Forecast;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.ui.PageFragmentCallbacks;

import java.util.HashMap;

public class UpdateForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener {

    public static final String ARG_KEY = "key";
    private String mKey;
    private Page mPage;
    private PageFragmentCallbacks mCallbacks;
    private Spinner harvsetSeason;
    private Forecast forecast = new Forecast();

    private HashMap<String, Forecast> forecastHashMap = new HashMap<>();

    private AutoCompleteTextView numLandPlot;
    private AutoCompleteTextView minimumflowprice;
    private AutoCompleteTextView farmerexpectedminppp;

    private TextView expectedTotalProductionInKg;
    private TextView expectedSalesOutsideThePpp;
    private TextView expectedPostHarvsetLossInKg;
    private TextView totalCoopLandSizeInHa;
    private TextView percentCoopLandSize;
    private TextView currentFtmaCommitementKg;
    private TextView farmerContributionFtmaCom;

    private int mFarmerId;
    private Uri mUri;
    private Cursor cursor;
    private int seasonId;
    private String seasonName;
    private boolean isDataAvailable = false;

    public UpdateForecastFragment() {
        super();
    }

    public static UpdateForecastFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UpdateForecastFragment fragment = new UpdateForecastFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.season_forecast, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.forecast));

        numLandPlot = rootView.findViewById(R.id.num_land);
        minimumflowprice = rootView.findViewById(R.id.minimumflowprice);
        farmerexpectedminppp = rootView.findViewById(R.id.farmerexpectedminppp);

        expectedTotalProductionInKg = rootView.findViewById(R.id.expected_total_production_in_kg);
        expectedSalesOutsideThePpp = rootView.findViewById(R.id.expected_sales_outside_the_ppp);
        expectedPostHarvsetLossInKg = rootView.findViewById(R.id.expected_post_harvset_loss_in_kg);
        totalCoopLandSizeInHa = rootView.findViewById(R.id.total_coop_land_size_in_ha);
        percentCoopLandSize = rootView.findViewById(R.id.percent_coop_land_size);
        currentFtmaCommitementKg = rootView.findViewById(R.id.current_ftma_commitement_kg);
        farmerContributionFtmaCom = rootView.findViewById(R.id.farmer_contribution_ftma_com);


        harvsetSeason = rootView.findViewById(R.id.harvestSeason);
        populateSpinner();

        harvsetSeason.setOnItemSelectedListener(this);

        cursor = (Cursor) harvsetSeason.getSelectedItem();
        seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));


        LinearLayout access_info_season_first2 = rootView.findViewById(R.id.access_info_season_first2);
        access_info_season_first2.setVisibility(View.VISIBLE);

        minimumflowprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {
                    cursor = (Cursor) harvsetSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                    seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                    double landInfo = Double.parseDouble(charSequence.toString());
                    if (forecastHashMap.containsKey(seasonName)) {
                        forecastHashMap.get(seasonName).setMinimumflowprice(landInfo);
                    } else {
                        Forecast forecast = new Forecast();
                        forecast.setMinimumflowprice(landInfo);
                        forecast.setHarvestSeason(seasonId);
                        forecastHashMap.put(seasonName, forecast);
                    }
                    mPage.getData().putSerializable("forecast", forecastHashMap);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        farmerexpectedminppp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                try {
                    cursor = (Cursor) harvsetSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                    seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                    double landInfo = Double.parseDouble(charSequence.toString());
                    if (forecastHashMap.containsKey(seasonName)) {
                        forecastHashMap.get(seasonName).setFarmerexpectedminppp(landInfo);
                    } else {
                        Forecast forecast = new Forecast();
                        forecast.setFarmerexpectedminppp(landInfo);
                        forecast.setHarvestSeason(seasonId);
                        forecastHashMap.put(seasonName, forecast);
                    }
                    mPage.getData().putSerializable("forecast", forecastHashMap);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        numLandPlot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    cursor = (Cursor) harvsetSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                    seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                    double landInfo = Double.parseDouble(charSequence.toString());
                    if (forecastHashMap.containsKey(seasonName)) {
                        forecastHashMap.get(seasonName).setArableLandPlot(landInfo);
                    } else {
                        Forecast forecast = new Forecast();
                        forecast.setArableLandPlot(landInfo);
                        forecast.setHarvestSeason(seasonId);
                        forecastHashMap.put(seasonName, forecast);
                    }
                    mPage.getData().putSerializable("forecast", forecastHashMap);
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
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(1, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String farmerSelection = BfwContract.Farmer.TABLE_NAME + "." +
                BfwContract.Farmer._ID + " =  ? ";

        //construct the new URI
        mUri = BfwContract.ForecastFarmer.buildForecastFarmerUri(mFarmerId);

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
            double arableLandPlot;
            double totProdKg;
            double salesOutsidePpp;
            double postHarvestLossInKg;
            double totCoopLandSize;
            double farmerPercentCoopLandSize;

            double currentPppContrib;
            double farmerContributionPpp;

            double farmerexpectedmin;
            double minimumprice;

            int seasonId;
            int forecastId;
            String seasonName = "";

            Cursor seasonCursor = null;
            String farmerSelection = BfwContract.HarvestSeason.TABLE_NAME
                    + "." + BfwContract.HarvestSeason._ID
                    + " = ?";

            //on load finished create hash map with season forecast
            while (data.moveToNext()) {

                seasonId = data.getInt(data.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_SEASON_ID));
                forecastId = data.getInt(data.getColumnIndex(BfwContract.ForecastFarmer._ID));
                arableLandPlot = data.getDouble(data.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_ARABLE_LAND_PLOT));
                totProdKg = data.getDouble(data.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_PRODUCTION_MT));
                salesOutsidePpp = data.getDouble(data.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_YIELD_MT));
                postHarvestLossInKg = data.getDouble(data.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_HARVEST_SALE_VALUE));
                totCoopLandSize = data.getDouble(data.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_COOP_LAND_SIZE));
                farmerPercentCoopLandSize = data.getDouble(data.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_PERCENT_FARMER_LAND_SIZE));
                currentPppContrib = data.getDouble(data.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_PPP_COMMITMENT));
                farmerContributionPpp = data.getDouble(data.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_CONTRIBUTION_PPP));


                farmerexpectedmin = data.getDouble(data.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_EXPECTED_MIN_PPP));
                minimumprice = data.getDouble(data.getColumnIndex(BfwContract.ForecastFarmer.COLUMN_FLOW_PRICE));


                try {
                    seasonCursor = getActivity().getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, farmerSelection,
                            new String[]{Integer.toString(seasonId)}, null);

                    if (seasonCursor != null && seasonCursor.moveToFirst()) {
                        seasonName = seasonCursor.getString(seasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                        forecast = new Forecast(arableLandPlot, totProdKg, salesOutsidePpp, postHarvestLossInKg, totCoopLandSize,
                                farmerPercentCoopLandSize, seasonId, farmerexpectedmin, minimumprice, currentPppContrib, farmerContributionPpp);

                        forecast.setForecastId(forecastId);
                        forecastHashMap.put(seasonName, forecast);
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
            setForecastItem(cursor);

            mPage.getData().putSerializable("forecast", forecastHashMap);
        }
    }

    private void setForecastItem(Cursor cursor) {
        seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

        if (isDataAvailable && forecastHashMap.containsKey(seasonName)) {
            String landPlotInfo = forecastHashMap.get(seasonName).getArableLandPlot() + "";
            String mininumPriceInfo = forecastHashMap.get(seasonName).getMinimumflowprice() + "";
            String expectedInfo = forecastHashMap.get(seasonName).getFarmerexpectedminppp() + "";

            String prodInfo = forecastHashMap.get(seasonName).getTotProdKg() + "";
            String salesInfo = forecastHashMap.get(seasonName).getSalesOutsidePpp() + "";
            String lostInfo = forecastHashMap.get(seasonName).getPostHarvestLossInKg() + "";
            String coopInfo = forecastHashMap.get(seasonName).getTotCoopLandSize() + "";
            String farmerPercentCoopInfo = forecastHashMap.get(seasonName).getFarmerPercentCoopLandSize() + "";
            String currentInfo = forecastHashMap.get(seasonName).getCurrentPppContrib() + "";
            String farmerInfo = forecastHashMap.get(seasonName).getFarmerContributionPpp() + "";

            numLandPlot.setText(landPlotInfo);
            minimumflowprice.setText(mininumPriceInfo);
            farmerexpectedminppp.setText(expectedInfo);

            expectedTotalProductionInKg.setText(prodInfo);
            expectedSalesOutsideThePpp.setText(salesInfo);
            expectedPostHarvsetLossInKg.setText(lostInfo);
            totalCoopLandSizeInHa.setText(coopInfo);
            percentCoopLandSize.setText(farmerPercentCoopInfo);
            currentFtmaCommitementKg.setText(currentInfo);
            farmerContributionFtmaCom.setText(farmerInfo);
        } else {
            String landPlotInfo = "";
            String mininumPriceInfo = "";
            String expectedInfo = "";

            String prodInfo = "";
            String salesInfo = "";
            String lostInfo = "";
            String coopInfo = "";
            String farmerPercentCoopInfo = "";
            String currentInfo = "";
            String farmerInfo = "";

            numLandPlot.setText(landPlotInfo);
            minimumflowprice.setText(mininumPriceInfo);
            farmerexpectedminppp.setText(expectedInfo);

            expectedTotalProductionInKg.setText(prodInfo);
            expectedSalesOutsideThePpp.setText(salesInfo);
            expectedPostHarvsetLossInKg.setText(lostInfo);
            totalCoopLandSizeInHa.setText(coopInfo);
            percentCoopLandSize.setText(farmerPercentCoopInfo);
            currentFtmaCommitementKg.setText(currentInfo);
            farmerContributionFtmaCom.setText(farmerInfo);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Cursor cursor = (Cursor) harvsetSeason.getSelectedItem();
        setForecastItem(cursor);

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

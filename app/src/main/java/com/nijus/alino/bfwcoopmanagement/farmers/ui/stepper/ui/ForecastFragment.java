package com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.ui;

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
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.Forecast;

import java.util.HashMap;

public class ForecastFragment extends Fragment {

    public static final String ARG_KEY = "key";
    private String mKey;
    private Page mPage;
    private PageFragmentCallbacks mCallbacks;

    private Spinner harvsetSeason;
    private Cursor cursor;
    private String seasonName;
    private int seasonId;
    private HashMap<String, Forecast> seasonForecast = new HashMap<>();


    private AutoCompleteTextView numLandPlot;
    private AutoCompleteTextView minimumflowprice;
    private AutoCompleteTextView farmerexpectedminppp;

    public ForecastFragment() {
        super();
    }

    public static ForecastFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        ForecastFragment fragment = new ForecastFragment();
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
        View rootView = inflater.inflate(R.layout.season_forecast, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.forecast));

        harvsetSeason = rootView.findViewById(R.id.harvestSeason);

        numLandPlot = rootView.findViewById(R.id.num_land);
        minimumflowprice = rootView.findViewById(R.id.minimumflowprice);
        farmerexpectedminppp = rootView.findViewById(R.id.farmerexpectedminppp);

        populateSpinner();

        // set default value

        cursor = (Cursor) harvsetSeason.getSelectedItem();
        seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

        Forecast forecast = new Forecast();
        forecast.setMinimumflowprice(0.0);
        forecast.setArableLandPlot(0.0);
        forecast.setFarmerexpectedminppp(0.0);
        forecast.setHarvestSeason(seasonId);
        seasonForecast.put(seasonName, forecast);
        mPage.getData().putSerializable("forecast", seasonForecast);


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
                    if (seasonForecast.containsKey(seasonName)) {
                        seasonForecast.get(seasonName).setMinimumflowprice(landInfo);
                    } else {
                        Forecast forecast = new Forecast();
                        forecast.setMinimumflowprice(landInfo);
                        forecast.setHarvestSeason(seasonId);
                        seasonForecast.put(seasonName, forecast);
                    }
                    mPage.getData().putSerializable("forecast", seasonForecast);
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
                    if (seasonForecast.containsKey(seasonName)) {
                        seasonForecast.get(seasonName).setFarmerexpectedminppp(landInfo);
                    } else {
                        Forecast forecast = new Forecast();
                        forecast.setFarmerexpectedminppp(landInfo);
                        forecast.setHarvestSeason(seasonId);
                        seasonForecast.put(seasonName, forecast);
                    }
                    mPage.getData().putSerializable("forecast", seasonForecast);
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
                    if (seasonForecast.containsKey(seasonName)) {
                        seasonForecast.get(seasonName).setArableLandPlot(landInfo);
                    } else {
                        Forecast forecast = new Forecast();
                        forecast.setArableLandPlot(landInfo);
                        forecast.setHarvestSeason(seasonId);
                        seasonForecast.put(seasonName, forecast);
                    }
                    mPage.getData().putSerializable("forecast", seasonForecast);
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

        mCallbacks = (PageFragmentCallbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}

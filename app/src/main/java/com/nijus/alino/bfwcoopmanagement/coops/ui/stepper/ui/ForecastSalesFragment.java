package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.ui;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.ForecastSales;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;

import java.util.HashMap;

public class ForecastSalesFragment extends Fragment {

    public static final String ARG_KEY = "key";
    public final String LOG_TAG = ForecastSalesFragment.class.getSimpleName();
    private String mKey;

    private ForecastSales forecastSales = new ForecastSales();

    private Page mPage;
    private PageFragmentCallbacks mCallbacks;

    private Spinner harvestSeason;
    private Spinner minFloorPerGrade;
    private Spinner grade;

    private Cursor cursor;
    private String seasonName;
    private int seasonId;
    private HashMap<String, ForecastSales> forecastSalesSeason = new HashMap<>();

    private CheckBox rgcc;
    private CheckBox prodev;
    private CheckBox sasura;
    private CheckBox aif;
    private CheckBox eax;
    private CheckBox none;
    private CheckBox other;

    private AutoCompleteTextView commitedContractQty;

    public ForecastSalesFragment() {
        super();
    }

    public static ForecastSalesFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        ForecastSalesFragment fragment = new ForecastSalesFragment();
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
        View rootView = inflater.inflate(R.layout.forecast_sales, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.forecast_sales));

        harvestSeason = rootView.findViewById(R.id.harvestSeason);
        grade = rootView.findViewById(R.id.grade);
        minFloorPerGrade = rootView.findViewById(R.id.min_floor_per_grade);

        populateSpinner();

        rgcc = rootView.findViewById(R.id.rgcc);
        boolean isrgcc = rgcc.isActivated();
        forecastSales.setRgcc(isrgcc);

        prodev = rootView.findViewById(R.id.prodev);
        boolean isprodev = prodev.isActivated();
        forecastSales.setProdev(isprodev);

        sasura = rootView.findViewById(R.id.sasura);
        boolean issasura = sasura.isActivated();
        forecastSales.setSarura(issasura);

        aif = rootView.findViewById(R.id.aif);
        boolean isaif = aif.isActivated();
        forecastSales.setAif(isaif);

        eax = rootView.findViewById(R.id.eax);
        boolean iseax = eax.isActivated();
        forecastSales.setEax(iseax);

        none = rootView.findViewById(R.id.none);
        boolean isnone = none.isActivated();
        forecastSales.setNone(isnone);

        other = rootView.findViewById(R.id.other);
        boolean isother = other.isActivated();
        forecastSales.setOther(isother);

        commitedContractQty = rootView.findViewById(R.id.commired_contract_qty);

        grade = rootView.findViewById(R.id.grade);
        minFloorPerGrade = rootView.findViewById(R.id.min_floor_per_grade);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.coop_grade, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grade.setAdapter(adapter);
        minFloorPerGrade.setAdapter(adapter);

        String selectedGrade = grade.getSelectedItem().toString();
        String florPrice = minFloorPerGrade.getSelectedItem().toString();
        forecastSales.setGrade(selectedGrade);
        forecastSales.setGrade(florPrice);

        cursor = (Cursor) harvestSeason.getSelectedItem();
        seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
        forecastSales.setSeasonId(seasonId);

        forecastSalesSeason.put(seasonName, forecastSales);

        mPage.getData().putSerializable("forecast_sales", forecastSalesSeason);

        grade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                if (forecastSalesSeason.containsKey(seasonName)) {
                    forecastSalesSeason.get(seasonName).setGrade(adapterView.getItemAtPosition(i).toString());
                } else {
                    ForecastSales forecastSales = new ForecastSales();
                    seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                    forecastSales.setSeasonId(seasonId);
                    forecastSales.setGrade(adapterView.getItemAtPosition(i).toString());
                    forecastSalesSeason.put(seasonName, forecastSales);
                }

                mPage.getData().putSerializable("forecast_sales", forecastSalesSeason);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        minFloorPerGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                if (forecastSalesSeason.containsKey(seasonName)) {
                    forecastSalesSeason.get(seasonName).setMinFloorPerGrade(adapterView.getItemAtPosition(i).toString());
                } else {
                    ForecastSales forecastSales = new ForecastSales();
                    seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                    forecastSales.setSeasonId(seasonId);
                    forecastSales.setMinFloorPerGrade(adapterView.getItemAtPosition(i).toString());
                    forecastSalesSeason.put(seasonName, forecastSales);
                }

                mPage.getData().putSerializable("forecast_sales", forecastSalesSeason);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        commitedContractQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    cursor = (Cursor) harvestSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                    if (forecastSalesSeason.containsKey(seasonName)) {
                        forecastSalesSeason.get(seasonName).setCommitedContractQty(Integer.parseInt(charSequence.toString()));
                    } else {
                        ForecastSales forecastSales = new ForecastSales();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        forecastSales.setSeasonId(seasonId);
                        forecastSales.setCommitedContractQty(Integer.parseInt(charSequence.toString()));
                        forecastSalesSeason.put(seasonName, forecastSales);
                    }

                    mPage.getData().putSerializable("forecast_sales", forecastSalesSeason);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        rgcc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (forecastSalesSeason.containsKey(seasonName)) {
                        forecastSalesSeason.get(seasonName).setRgcc(true);
                    } else {
                        ForecastSales forecastSales = new ForecastSales();
                        forecastSales.setRgcc(true);
                        forecastSales.setSeasonId(seasonId);
                        forecastSalesSeason.put(seasonName, forecastSales);
                    }
                } else {
                    if (forecastSalesSeason.containsKey(seasonName)) {
                        forecastSalesSeason.get(seasonName).setRgcc(false);
                    } else {
                        ForecastSales forecastSales = new ForecastSales();
                        forecastSales.setRgcc(false);
                        forecastSales.setSeasonId(seasonId);
                        forecastSalesSeason.put(seasonName, forecastSales);
                    }
                }
                mPage.getData().putSerializable("forecast_sales", forecastSalesSeason);
            }
        });

        prodev.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (forecastSalesSeason.containsKey(seasonName)) {
                        forecastSalesSeason.get(seasonName).setProdev(true);
                    } else {
                        ForecastSales forecastSales = new ForecastSales();
                        forecastSales.setProdev(true);
                        forecastSales.setSeasonId(seasonId);
                        forecastSalesSeason.put(seasonName, forecastSales);
                    }
                } else {
                    if (forecastSalesSeason.containsKey(seasonName)) {
                        forecastSalesSeason.get(seasonName).setProdev(false);
                    } else {
                        ForecastSales forecastSales = new ForecastSales();
                        forecastSales.setProdev(false);
                        forecastSales.setSeasonId(seasonId);
                        forecastSalesSeason.put(seasonName, forecastSales);
                    }
                }
                mPage.getData().putSerializable("forecast_sales", forecastSalesSeason);
            }
        });

        sasura.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (forecastSalesSeason.containsKey(seasonName)) {
                        forecastSalesSeason.get(seasonName).setSarura(true);
                    } else {
                        ForecastSales forecastSales = new ForecastSales();
                        forecastSales.setSarura(true);
                        forecastSales.setSeasonId(seasonId);
                        forecastSalesSeason.put(seasonName, forecastSales);
                    }
                } else {
                    if (forecastSalesSeason.containsKey(seasonName)) {
                        forecastSalesSeason.get(seasonName).setSarura(false);
                    } else {
                        ForecastSales forecastSales = new ForecastSales();
                        forecastSales.setSarura(false);
                        forecastSales.setSeasonId(seasonId);
                        forecastSalesSeason.put(seasonName, forecastSales);
                    }
                }
                mPage.getData().putSerializable("forecast_sales", forecastSalesSeason);
            }
        });

        aif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (forecastSalesSeason.containsKey(seasonName)) {
                        forecastSalesSeason.get(seasonName).setAif(true);
                    } else {
                        ForecastSales forecastSales = new ForecastSales();
                        forecastSales.setAif(true);
                        forecastSales.setSeasonId(seasonId);
                        forecastSalesSeason.put(seasonName, forecastSales);
                    }
                } else {
                    if (forecastSalesSeason.containsKey(seasonName)) {
                        forecastSalesSeason.get(seasonName).setAif(false);
                    } else {
                        ForecastSales forecastSales = new ForecastSales();
                        forecastSales.setAif(false);
                        forecastSales.setSeasonId(seasonId);
                        forecastSalesSeason.put(seasonName, forecastSales);
                    }
                }
                mPage.getData().putSerializable("forecast_sales", forecastSalesSeason);
            }
        });

        eax.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (forecastSalesSeason.containsKey(seasonName)) {
                        forecastSalesSeason.get(seasonName).setEax(true);
                    } else {
                        ForecastSales forecastSales = new ForecastSales();
                        forecastSales.setEax(true);
                        forecastSales.setSeasonId(seasonId);
                        forecastSalesSeason.put(seasonName, forecastSales);
                    }
                } else {
                    if (forecastSalesSeason.containsKey(seasonName)) {
                        forecastSalesSeason.get(seasonName).setEax(false);
                    } else {
                        ForecastSales forecastSales = new ForecastSales();
                        forecastSales.setEax(false);
                        forecastSales.setSeasonId(seasonId);
                        forecastSalesSeason.put(seasonName, forecastSales);
                    }

                }
                mPage.getData().putSerializable("forecast_sales", forecastSalesSeason);
            }
        });

        none.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (forecastSalesSeason.containsKey(seasonName)) {
                        forecastSalesSeason.get(seasonName).setNone(true);
                    } else {
                        ForecastSales forecastSales = new ForecastSales();
                        forecastSales.setNone(true);
                        forecastSales.setSeasonId(seasonId);
                        forecastSalesSeason.put(seasonName, forecastSales);
                    }
                } else {

                    if (forecastSalesSeason.containsKey(seasonName)) {
                        forecastSalesSeason.get(seasonName).setNone(false);
                    } else {
                        ForecastSales forecastSales = new ForecastSales();
                        forecastSales.setNone(false);
                        forecastSales.setSeasonId(seasonId);
                        forecastSalesSeason.put(seasonName, forecastSales);
                    }
                }
                mPage.getData().putSerializable("forecast_sales", forecastSalesSeason);
            }
        });

        other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (forecastSalesSeason.containsKey(seasonName)) {
                        forecastSalesSeason.get(seasonName).setOther(true);
                    } else {
                        ForecastSales forecastSales = new ForecastSales();
                        forecastSales.setOther(true);
                        forecastSales.setSeasonId(seasonId);
                        forecastSalesSeason.put(seasonName, forecastSales);
                    }
                } else {
                    if (forecastSalesSeason.containsKey(seasonName)) {
                        forecastSalesSeason.get(seasonName).setOther(false);
                    } else {
                        ForecastSales forecastSales = new ForecastSales();
                        forecastSales.setOther(false);
                        forecastSales.setSeasonId(seasonId);
                        forecastSalesSeason.put(seasonName, forecastSales);
                    }
                }
                mPage.getData().putSerializable("forecast_sales", forecastSalesSeason);
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
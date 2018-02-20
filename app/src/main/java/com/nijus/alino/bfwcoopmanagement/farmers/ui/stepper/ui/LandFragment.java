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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.Finance;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.LandInformation;

import java.util.HashMap;
import java.util.UUID;

public class LandFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    public static final String ARG_KEY = "key";

    private String mKey;
    private Page mPage;
    private PageFragmentCallbacks mCallbacks;

    private AutoCompleteTextView landSizeHa;
    private AutoCompleteTextView longitude;
    private AutoCompleteTextView latitude;

    private Cursor cursor;
    private String seasonName;
    private int seasonId;
    private HashMap<String, LandInformation> seasonLand = new HashMap<>();

    private LinearLayout landContainer;
    private Switch switchlocation;
    private Spinner harvsetSeason;
    private HashMap<String, Double> mapLand = new HashMap<>();

    public LandFragment() {
        super();
    }

    public static LandFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        LandFragment fragment = new LandFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.land_stepper_info, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);

        harvsetSeason = rootView.findViewById(R.id.harvsetSeason);

        landSizeHa = rootView.findViewById(R.id.land_size_ha);
        longitude = rootView.findViewById(R.id.longitude);
        latitude = rootView.findViewById(R.id.latitude);

        landContainer = rootView.findViewById(R.id.land_container);

        populateSpinner();

        switchlocation = rootView.findViewById(R.id.switchlocation);
        switchlocation.setTextOff("Manual");
        switchlocation.setTextOn("Automatic");
        if (switchlocation != null) {
            switchlocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        latitude.setText("98,45");
                        longitude.setText("80,04");
                        latitude.setEnabled(false);
                        longitude.setEnabled(false);
                    } else {
                        latitude.setText("");
                        longitude.setText("");
                        latitude.setEnabled(true);
                        longitude.setEnabled(true);
                    }

                }
            });
        }

        longitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {

                    cursor = (Cursor) harvsetSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));


                    if (seasonLand.containsKey(seasonName)) {
                        seasonLand.get(seasonName).setLng(Double.parseDouble(charSequence.toString()));
                    } else {
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        LandInformation landInformation = new LandInformation();
                        landInformation.setLng(Double.parseDouble(charSequence.toString()));
                        landInformation.setHarvestSeason(seasonId);
                        seasonLand.put(seasonName, landInformation);
                    }

                    mPage.getData().putSerializable("land_info", seasonLand);

                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        latitude.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {

                    cursor = (Cursor) harvsetSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                    if (seasonLand.containsKey(seasonName)) {
                        seasonLand.get(seasonName).setLat(Double.parseDouble(charSequence.toString()));
                    } else {
                        LandInformation landInformation = new LandInformation();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        landInformation.setHarvestSeason(seasonId);
                        landInformation.setLat(Double.parseDouble(charSequence.toString()));
                        seasonLand.put(seasonName, landInformation);
                    }

                    mPage.getData().putSerializable("land_info", seasonLand);

                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        landSizeHa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {

                    cursor = (Cursor) harvsetSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                    if (seasonLand.containsKey(seasonName)) {
                        seasonLand.get(seasonName).setLandSize(Double.parseDouble(charSequence.toString()));
                    } else {
                        LandInformation landInformation = new LandInformation();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        landInformation.setHarvestSeason(seasonId);
                        landInformation.setLandSize(Double.parseDouble(charSequence.toString()));
                        seasonLand.put(seasonName, landInformation);
                    }

                    mPage.getData().putSerializable("land_info", seasonLand);

                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textView.setText(getContext().getString(R.string.farmer_land));
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = mCallbacks.onGetPage(mKey);

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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
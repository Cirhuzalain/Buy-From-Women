package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui.update;

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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.PageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.LandInformationVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui.PageFragmentCallbacksVendor;

import java.util.HashMap;

public class UpdateLandFragmentVendor extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener {

    public static final String ARG_KEY = "key";

    private String mKey;
    private PageVendorVendor mPage;
    private PageFragmentCallbacksVendor mCallbacks;

    private Uri mUri;
    private long mFarmerId;

    private Cursor cursor;
    private String seasonName;
    private int seasonId;
    private HashMap<String, LandInformationVendor> seasonLand = new HashMap<>();
    private boolean isDataAvailable;

    private LinearLayout landContainer;
    private LandInformationVendor landInformation = new LandInformationVendor();

    private Switch switchlocationView;
    private AutoCompleteTextView longitude;
    private AutoCompleteTextView latitude;
    private AutoCompleteTextView landSizeHa;
    private Spinner harvsetSeason;

    public UpdateLandFragmentVendor() {
        super();
    }

    public static UpdateLandFragmentVendor create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UpdateLandFragmentVendor fragment = new UpdateLandFragmentVendor();
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

        if (intent.hasExtra("vendorId")) {
            mFarmerId = intent.getLongExtra("vendorId", 0);
            //mUri = BfwContract.VendorLand.buildVendorLandUri(mFarmerId);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(1, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String farmerSelection = BfwContract.Vendor.TABLE_NAME + "." +
                BfwContract.Vendor._ID + " =  ? ";

        mUri = BfwContract.VendorLand.buildVendorLandUri(mFarmerId);

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
        View rootView = inflater.inflate(R.layout.land_stepper_info, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.farmer_land));
        landSizeHa = rootView.findViewById(R.id.land_size_ha);
        longitude = rootView.findViewById(R.id.longitude);
        latitude = rootView.findViewById(R.id.latitude);
        landContainer = rootView.findViewById(R.id.land_container);

        harvsetSeason = rootView.findViewById(R.id.harvsetSeason);

        populateSpinner();

        //switch
        switchlocationView = rootView.findViewById(R.id.switchlocation);
        switchlocationView.setText(getString(R.string.loc_gps));
        switchlocationView.setTextOff(getString(R.string.man_gps));
        switchlocationView.setTextOn(getString(R.string.gps_automatic));

        cursor = (Cursor) harvsetSeason.getSelectedItem();
        seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

        landInformation = new LandInformationVendor(0.0, 0.0, 0.0, seasonId);
        seasonLand.put(seasonName, landInformation);

        mPage.getData().putSerializable("land_infoVendor", seasonLand);

        if (switchlocationView != null) {
            switchlocationView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        latitude.setText("");
                        longitude.setText("");
                        latitude.setEnabled(false);
                        longitude.setEnabled(false);
                        //call fence API for gps update
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
                        LandInformationVendor landInformation = new LandInformationVendor();
                        landInformation.setLng(Double.parseDouble(charSequence.toString()));
                        landInformation.setHarvestSeason(seasonId);
                        seasonLand.put(seasonName, landInformation);
                    }

                    mPage.getData().putSerializable("land_infoVendor", seasonLand);

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
                        LandInformationVendor landInformation = new LandInformationVendor();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        landInformation.setHarvestSeason(seasonId);
                        landInformation.setLat(Double.parseDouble(charSequence.toString()));
                        seasonLand.put(seasonName, landInformation);
                    }

                    mPage.getData().putSerializable("land_infoVendor", seasonLand);

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
                        LandInformationVendor landInformation = new LandInformationVendor();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        landInformation.setHarvestSeason(seasonId);
                        landInformation.setLandSize(Double.parseDouble(charSequence.toString()));
                        seasonLand.put(seasonName, landInformation);
                    }

                    mPage.getData().putSerializable("land_infoVendor", seasonLand);

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
    public void onClick(View view) {

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null) {
            double landSize;
            double lat;
            double lng;
            int seasonId;
            int landId;

            String seasonName;

            Cursor seasonCursor = null;
            String farmerSelection = BfwContract.HarvestSeason.TABLE_NAME
                    + "." + BfwContract.HarvestSeason._ID
                    + " = ?";

            while (data.moveToNext()) {

                landId = data.getInt(data.getColumnIndex(BfwContract.VendorLand._ID));
                seasonId = data.getInt(data.getColumnIndex(BfwContract.VendorLand.COLUMN_SEASON_ID));
                lat = data.getDouble(data.getColumnIndex(BfwContract.VendorLand.COLUMN_LAT_INFO));
                lng = data.getDouble(data.getColumnIndex(BfwContract.VendorLand.COLUMN_LNG_INFO));
                landSize = data.getDouble(data.getColumnIndex(BfwContract.VendorLand.COLUMN_PLOT_SIZE));

                try {
                    seasonCursor = getActivity().getContentResolver().query(BfwContract.HarvestSeason.CONTENT_URI, null, farmerSelection,
                            new String[]{Integer.toString(seasonId)}, null);

                    if (seasonCursor != null) {
                        seasonCursor.moveToFirst();
                        seasonName = seasonCursor.getString(seasonCursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                        landInformation = new LandInformationVendor(landSize, lat, lng, seasonId);
                        landInformation.setLandId(landId);
                        seasonLand.put(seasonName, landInformation);
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
            setLandFarmerItem(cursor);
            mPage.getData().putSerializable("land_infoVendor", seasonLand);
        }
    }

    private void setLandFarmerItem(Cursor cursor) {

        seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

        if (isDataAvailable && seasonLand.containsKey(seasonName)) {

            String landInfo = seasonLand.get(seasonName).getLandSize() + "";
            String latInfo = seasonLand.get(seasonName).getLat() + "";
            String lngInfo = seasonLand.get(seasonName).getLng() + "";

            landSizeHa.setText(landInfo);
            longitude.setText(latInfo);
            latitude.setText(lngInfo);

        } else {

            String landInfo = "";
            String latInfo = "";
            String lngInfo = "";

            landSizeHa.setText(landInfo);
            longitude.setText(latInfo);
            latitude.setText(lngInfo);

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Cursor cursor = (Cursor) harvsetSeason.getSelectedItem();
        setLandFarmerItem(cursor);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

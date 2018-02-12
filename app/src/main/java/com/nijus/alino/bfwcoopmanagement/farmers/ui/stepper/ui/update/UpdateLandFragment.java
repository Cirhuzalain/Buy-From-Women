package com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.ui.update;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.ui.PageFragmentCallbacks;

import java.util.HashMap;
import java.util.UUID;

public class UpdateLandFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener {

    public static final String ARG_KEY = "key";

    private String mKey;
    private Page mPage;
    private PageFragmentCallbacks mCallbacks;

    private AutoCompleteTextView landSizeHa;
    private Uri mUri;
    private long mFarmerId;

    private TextInputLayout textInputLayout;
    private AutoCompleteTextView autoCompleteTextView;
    private String[] newVal;

    //private Button addNewLandLocation;
    private LinearLayout landContainer;

    private Switch switchlocationView;
    private AutoCompleteTextView longitude;
    private AutoCompleteTextView latitude;

    private HashMap<String, String[]> mapLand = new HashMap<>();
    private final String firstSize = UUID.randomUUID().toString();

    public UpdateLandFragment() {
        super();
    }

    public static UpdateLandFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UpdateLandFragment fragment = new UpdateLandFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.land_stepper_info, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);

        landSizeHa = rootView.findViewById(R.id.land_size_ha);
        landContainer = rootView.findViewById(R.id.land_container);

        //switch
        switchlocationView = rootView.findViewById(R.id.switchlocation);
        switchlocationView.setText("Check your location here");
        switchlocationView.setTextOff("Manual");
        switchlocationView.setTextOn("Automatic");

        longitude = rootView.findViewById(R.id.longitude);
        latitude = rootView.findViewById(R.id.latitude);


        final String firstSize = UUID.randomUUID().toString();

        if (switchlocationView != null){
            switchlocationView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        //Toast.makeText(getContext(), "Automatic", Toast.LENGTH_LONG).show();
                        latitude.setText("98,45");
                        longitude.setText("80,04");
                        //latitude.setHint("breeee");
                        latitude.setEnabled(false);
                        longitude.setEnabled(false);
                        //longitude.setError("Invalide");
                    }

                    else {
                        //Toast.makeText(getContext(),"Manual",Toast.LENGTH_LONG).show();
                        latitude.setText("");
                        longitude.setText("");
                        latitude.setEnabled(true);
                        longitude.setEnabled(true);
                    }

                }
            });
        }

        textView.setText(getContext().getString(R.string.farmer_land));
        return rootView;
    }

    @Override
    public void onClick(View view) {

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
            mUri = BfwContract.LandPlot.buildLandPlotUri(mFarmerId);
        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(1, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mUri != null) {
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    null,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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
}

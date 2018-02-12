package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.PageVendorVendor;

import java.util.HashMap;
import java.util.UUID;

public class LandFragmentVendor extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    public static final String ARG_KEY = "key";

    private String mKey;
    private PageVendorVendor mPageVendor;
    private PageFragmentCallbacksVendor mCallbacks;

    private AutoCompleteTextView landSizeHa;
    private AutoCompleteTextView longitude;
    private AutoCompleteTextView latitude;

    //private Button addNewLandLocation;
    private LinearLayout landContainer;
    private Switch switchlocation;
    private Spinner spinner;
    private HashMap<String, Double> mapLand = new HashMap<>();

    public LandFragmentVendor() {
        super();
    }

    public static LandFragmentVendor create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        LandFragmentVendor fragment = new LandFragmentVendor();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.land_stepper_info, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);

        spinner = rootView.findViewById(R.id.harvsetSeason);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.harverstSeason, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        //spinner.setOnItemClickListener(this);

        landSizeHa = rootView.findViewById(R.id.land_size_ha);
        longitude = rootView.findViewById(R.id.longitude);
        latitude = rootView.findViewById(R.id.latitude);

        landContainer = rootView.findViewById(R.id.land_container);

        final String firstSize = UUID.randomUUID().toString();
        switchlocation = rootView.findViewById(R.id.switchlocation);
        switchlocation.setTextOff("Manual");
        switchlocation.setTextOn("Automatic");
        if (switchlocation != null){
            switchlocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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


        landSizeHa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    mapLand.put(firstSize, Double.parseDouble(charSequence.toString()));
                    mPageVendor.getData().putSerializable("land_info", mapLand);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textView.setText(getContext().getString(R.string.vender_land));
        return rootView;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPageVendor = mCallbacks.onGetPage(mKey);

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
        Toast.makeText(getContext(),"okkk "+adapterView.getSelectedItem().toString(),Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.PageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.AccessToInformationVendor;

public class AccessToInformationFragmentVendor extends Fragment implements AdapterView.OnItemSelectedListener {

    public static final String ARG_KEY = "key";
    private String mKey;
    private PageVendorVendor mPageVendor;
    private PageFragmentCallbacksVendor mCallbacks;
    private AccessToInformationVendor accessToInformationVendor = new AccessToInformationVendor();

    private CheckBox agriculturalExtension;
    private CheckBox climateInfo;
    private CheckBox seeds;
    private CheckBox organicFertilizers;
    private CheckBox inorganicFertilizers;
    private CheckBox labour;
    private CheckBox irrigation;
    private Spinner harvsetSeason;


    public AccessToInformationFragmentVendor() {
        super();
    }

    public static AccessToInformationFragmentVendor create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        AccessToInformationFragmentVendor fragment = new AccessToInformationFragmentVendor();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPageVendor = mCallbacks.onGetPage(mKey);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.access_to_infolayout, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.access_to_info));

        harvsetSeason = rootView.findViewById(R.id.harvsetSeason);


        agriculturalExtension = rootView.findViewById(R.id.agricultural_ext);
        boolean isAgriExt = agriculturalExtension.isActivated();
        accessToInformationVendor.setAgricultureExtension(isAgriExt);

        climateInfo = rootView.findViewById(R.id.climate_info);
        boolean isClimateInfo = climateInfo.isActivated();
        accessToInformationVendor.setClimateRelatedInformation(isClimateInfo);

        seeds = rootView.findViewById(R.id.seeds);
        boolean isSeeds = seeds.isActivated();
        accessToInformationVendor.setSeed(isSeeds);

        organicFertilizers = rootView.findViewById(R.id.organic_fertilizers);
        boolean isFertilizer = organicFertilizers.isActivated();
        accessToInformationVendor.setOrganicFertilizers(isFertilizer);

        inorganicFertilizers = rootView.findViewById(R.id.inorganic_fertilizers);
        boolean isInorganicFertilizer = inorganicFertilizers.isActivated();
        accessToInformationVendor.setInorganicFertilizers(isInorganicFertilizer);

        labour = rootView.findViewById(R.id.labour);
        boolean isLabour = labour.isActivated();
        accessToInformationVendor.setLabour(isLabour);

        irrigation = rootView.findViewById(R.id.irrigation);
        boolean isIrrigation = irrigation.isActivated();
        accessToInformationVendor.setWaterPumps(isIrrigation);



        mPageVendor.getData().putParcelable("accessToInformationVendor", accessToInformationVendor);

        agriculturalExtension.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    accessToInformationVendor.setAgricultureExtension(true);
                } else {
                    accessToInformationVendor.setAgricultureExtension(false);
                }
                mPageVendor.getData().putParcelable("accessToInformationVendor", accessToInformationVendor);
            }
        });

        climateInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    accessToInformationVendor.setClimateRelatedInformation(true);
                } else {
                    accessToInformationVendor.setClimateRelatedInformation(false);
                }
                mPageVendor.getData().putParcelable("accessToInformationVendor", accessToInformationVendor);
            }
        });

        seeds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    accessToInformationVendor.setSeed(true);
                } else {
                    accessToInformationVendor.setSeed(false);
                }
                mPageVendor.getData().putParcelable("accessToInformationVendor", accessToInformationVendor);
            }
        });

        organicFertilizers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    accessToInformationVendor.setOrganicFertilizers(true);
                } else {
                    accessToInformationVendor.setOrganicFertilizers(false);
                }
                mPageVendor.getData().putParcelable("accessToInformationVendor", accessToInformationVendor);
            }
        });

        inorganicFertilizers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    accessToInformationVendor.setInorganicFertilizers(true);
                } else {
                    accessToInformationVendor.setInorganicFertilizers(false);
                }
                mPageVendor.getData().putParcelable("accessToInformationVendor", accessToInformationVendor);
            }
        });

        labour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    accessToInformationVendor.setLabour(true);
                } else {
                    accessToInformationVendor.setLabour(false);
                }
                mPageVendor.getData().putParcelable("accessToInformationVendor", accessToInformationVendor);
            }
        });

        irrigation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    accessToInformationVendor.setWaterPumps(true);
                } else {
                    accessToInformationVendor.setWaterPumps(false);
                }
                mPageVendor.getData().putParcelable("accessToInformationVendor", accessToInformationVendor);
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.harverstSeason, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        harvsetSeason.setAdapter(adapter);

        harvsetSeason.setOnItemSelectedListener(this);
        return rootView;
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
        accessToInformationVendor.setHarvsetSeason(adapterView.getItemAtPosition(i).toString());
        mPageVendor.getData().putParcelable("accessToInformationVendor", accessToInformationVendor);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

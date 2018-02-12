package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.ui;

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
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.AccessToInformation;

public class AccessToInformationFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public static final String ARG_KEY = "key";
    private String mKey;
    private Page mPage;
    private com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.ui.PageFragmentCallbacks mCallbacks;
    private AccessToInformation accessToInformation = new AccessToInformation();

    private CheckBox agriculturalExtension;
    private CheckBox climateInfo;
    private CheckBox seeds;
    private CheckBox organicFertilizers;
    private CheckBox inorganicFertilizers;
    private CheckBox labour;
    private CheckBox irrigation;
    private Spinner harvsetSeason;


    public AccessToInformationFragment() {
        super();
    }

    public static AccessToInformationFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        AccessToInformationFragment fragment = new AccessToInformationFragment();
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
        View rootView = inflater.inflate(R.layout.access_to_info_coop_layout, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.access_to_info));

        harvsetSeason = rootView.findViewById(R.id.harvsetSeason);


        agriculturalExtension = rootView.findViewById(R.id.agricultural_ext);
        boolean isAgriExt = agriculturalExtension.isActivated();
        accessToInformation.setAgricultureExtension(isAgriExt);

        climateInfo = rootView.findViewById(R.id.climate_info);
        boolean isClimateInfo = climateInfo.isActivated();
        accessToInformation.setClimateRelatedInformation(isClimateInfo);

        seeds = rootView.findViewById(R.id.seeds);
        boolean isSeeds = seeds.isActivated();
        accessToInformation.setSeed(isSeeds);

        organicFertilizers = rootView.findViewById(R.id.organic_fertilizers);
        boolean isFertilizer = organicFertilizers.isActivated();
        accessToInformation.setOrganicFertilizers(isFertilizer);

        inorganicFertilizers = rootView.findViewById(R.id.inorganic_fertilizers);
        boolean isInorganicFertilizer = inorganicFertilizers.isActivated();
        accessToInformation.setInorganicFertilizers(isInorganicFertilizer);

        labour = rootView.findViewById(R.id.labour);
        boolean isLabour = labour.isActivated();
        accessToInformation.setLabour(isLabour);

        irrigation = rootView.findViewById(R.id.irrigation);
        boolean isIrrigation = irrigation.isActivated();
        accessToInformation.setWaterPumps(isIrrigation);



        mPage.getData().putParcelable("accessToInformation", accessToInformation);

        agriculturalExtension.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    accessToInformation.setAgricultureExtension(true);
                } else {
                    accessToInformation.setAgricultureExtension(false);
                }
                mPage.getData().putParcelable("accessToInformation", accessToInformation);
            }
        });

        climateInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    accessToInformation.setClimateRelatedInformation(true);
                } else {
                    accessToInformation.setClimateRelatedInformation(false);
                }
                mPage.getData().putParcelable("accessToInformation", accessToInformation);
            }
        });

        seeds.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    accessToInformation.setSeed(true);
                } else {
                    accessToInformation.setSeed(false);
                }
                mPage.getData().putParcelable("accessToInformation", accessToInformation);
            }
        });

        organicFertilizers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    accessToInformation.setOrganicFertilizers(true);
                } else {
                    accessToInformation.setOrganicFertilizers(false);
                }
                mPage.getData().putParcelable("accessToInformation", accessToInformation);
            }
        });

        inorganicFertilizers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    accessToInformation.setInorganicFertilizers(true);
                } else {
                    accessToInformation.setInorganicFertilizers(false);
                }
                mPage.getData().putParcelable("accessToInformation", accessToInformation);
            }
        });

        labour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    accessToInformation.setLabour(true);
                } else {
                    accessToInformation.setLabour(false);
                }
                mPage.getData().putParcelable("accessToInformation", accessToInformation);
            }
        });

        irrigation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    accessToInformation.setWaterPumps(true);
                } else {
                    accessToInformation.setWaterPumps(false);
                }
                mPage.getData().putParcelable("accessToInformation", accessToInformation);
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

        mCallbacks = (PageFragmentCallbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        accessToInformation.setHarvsetSeason(adapterView.getItemAtPosition(i).toString());
        mPage.getData().putParcelable("accessToInformation", accessToInformation);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

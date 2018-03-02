package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.ui.update;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.InternalInformation;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.ui.PageFragmentCallbacks;

public class UpdateInternalInfoFragment extends Fragment {

    public static final String ARG_KEY = "key";
    public final String LOG_TAG = UpdateInternalInfoFragment.class.getSimpleName();
    private String mKey;

    private InternalInformation internalInformation = new InternalInformation();

    private Page mPage;
    private PageFragmentCallbacks mCallbacks;

    private AutoCompleteTextView chair_name_c;
    private RadioGroup chair_gender_group;

    private AutoCompleteTextView chair_phone;
    private AutoCompleteTextView v_chair_name_c;
    private RadioGroup v_chair_gender_group;

    private AutoCompleteTextView v_chair_phone;
    private AutoCompleteTextView sec_name_c;
    private RadioGroup sec_gender_group;

    private AutoCompleteTextView sec_phone;
    private AutoCompleteTextView year_rca_registration;



    public UpdateInternalInfoFragment() {
        super();
    }

    public static UpdateInternalInfoFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UpdateInternalInfoFragment fragment = new UpdateInternalInfoFragment();
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
        View rootView = inflater.inflate(R.layout.internal_information, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.internal_title));

        chair_name_c = rootView.findViewById(R.id.chair_name_c);
        chair_gender_group = rootView.findViewById(R.id.chair_gender_group);

        chair_phone = rootView.findViewById(R.id.chair_phone);
        v_chair_name_c = rootView.findViewById(R.id.v_chair_name_c);
        v_chair_gender_group = rootView.findViewById(R.id.v_chair_gender_group);

        v_chair_phone = rootView.findViewById(R.id.v_chair_phone);
        sec_name_c = rootView.findViewById(R.id.sec_name_c);
        sec_gender_group = rootView.findViewById(R.id.sec_gender_group);

        sec_phone = rootView.findViewById(R.id.sec_phone);
        year_rca_registration = rootView.findViewById(R.id.year_rca_registration);

        chair_name_c.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                internalInformation.setChairName(charSequence.toString());
                mPage.setData("internalInformation", internalInformation);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        chair_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                internalInformation.setChairCell(charSequence.toString());
                mPage.setData("internalInformation", internalInformation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        v_chair_name_c.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                internalInformation.setViceChairName(charSequence.toString());
                mPage.setData("internalInformation", internalInformation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        v_chair_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                internalInformation.setViceChairCell(charSequence.toString());
                mPage.setData("internalInformation", internalInformation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sec_name_c.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                internalInformation.setSecName(charSequence.toString());
                mPage.setData("internalInformation", internalInformation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sec_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                internalInformation.setSecCell(charSequence.toString());
                mPage.setData("internalInformation", internalInformation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        year_rca_registration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                internalInformation.setYearRcaRegistration(charSequence.toString().trim());
                mPage.setData("internalInformation", internalInformation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //set default chair gender
        if (chair_gender_group.getCheckedRadioButtonId() == R.id.chair_radio_male) {
            internalInformation.setChairGender(true);
            mPage.getData().putParcelable("internalInformation", internalInformation);
        } else if (chair_gender_group.getCheckedRadioButtonId() == R.id.chair_radio_female) {
            internalInformation.setChairGender(false);
            mPage.getData().putParcelable("internalInformation", internalInformation);
        }

        //listen for change on chair gender
        chair_gender_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.chair_radio_male) {
                    internalInformation.setChairGender(true);
                    mPage.getData().putParcelable("internalInformation", internalInformation);
                } else if (i == R.id.chair_radio_female) {
                    internalInformation.setChairGender(false);
                    mPage.getData().putParcelable("internalInformation", internalInformation);
                }
            }
        });

        //set default vice chair  gender
        if (v_chair_gender_group.getCheckedRadioButtonId() == R.id.v_chair_radio_male) {
            internalInformation.setViceChairGender(true);
            mPage.getData().putParcelable("internalInformation", internalInformation);
        } else if (v_chair_gender_group.getCheckedRadioButtonId() == R.id.v_chair_radio_female) {
            internalInformation.setViceChairGender(false);
            mPage.getData().putParcelable("internalInformation", internalInformation);
        }

        //listen for change on vivce chair gender
        v_chair_gender_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.v_chair_radio_male) {
                    internalInformation.setViceChairGender(true);
                    mPage.getData().putParcelable("internalInformation", internalInformation);
                } else if (i == R.id.v_chair_radio_female) {
                    internalInformation.setViceChairGender(false);
                    mPage.getData().putParcelable("internalInformation", internalInformation);
                }
            }
        });

        //set default sec  gender
        if (sec_gender_group.getCheckedRadioButtonId() == R.id.v_chair_radio_male) {
            internalInformation.setSecGender(true);
            mPage.getData().putParcelable("internalInformation", internalInformation);
        } else if (sec_gender_group.getCheckedRadioButtonId() == R.id.sec_radio_female) {
            internalInformation.setSecGender(false);
            mPage.getData().putParcelable("internalInformation", internalInformation);
        }

        //listen for change on sec gender
        sec_gender_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.sec_radio_male) {
                    internalInformation.setViceChairGender(true);
                    mPage.getData().putParcelable("internalInformation", internalInformation);
                } else if (i == R.id.sec_radio_female) {
                    internalInformation.setSecGender(false);
                    mPage.getData().putParcelable("internalInformation", internalInformation);
                }
            }
        });


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
}
package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.events.DataValidEventB;
import com.nijus.alino.bfwcoopmanagement.events.DataValidEventR;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.PageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.GeneralVendor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralInformationVendor extends Fragment {

    public static final String ARG_KEY = "key";
    public final String LOG_TAG = GeneralInformationVendor.class.getSimpleName();
    /**
     * Make LinearLyaut content_spiner_coop invisible
     **/
    LinearLayout content_spiner_coop;
    private String mKey;
    private GeneralVendor generalVendor = new GeneralVendor();
    private PageVendorVendor mPageVendor;
    private PageFragmentCallbacksVendor mCallbacks;
    private AutoCompleteTextView names;
    private AutoCompleteTextView phoneNumber;
    private AutoCompleteTextView addressTextView;
    private final Pattern phoneRegex = Pattern.compile("^\\+250[0-9]{9}$",
            Pattern.CASE_INSENSITIVE);
    private RadioButton male;
    private RadioButton female;
    private RadioGroup mGenderGroup;

    public GeneralInformationVendor() {
        super();
    }

    public static GeneralInformationVendor create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        GeneralInformationVendor fragment = new GeneralInformationVendor();
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
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public boolean validatePhone(String phone) {
        Matcher match = phoneRegex.matcher(phone);
        return match.find();
    }

    @Subscribe
    public void onDataValidEventB(DataValidEventB validEventB) {

        String nameInfo = names.getText().toString();
        String phoneInfo = phoneNumber.getText().toString();


        if (!TextUtils.isEmpty(nameInfo) && nameInfo.length() >= 4 && !TextUtils.isEmpty(phoneInfo) && validatePhone(phoneInfo)) {
            EventBus.getDefault().post(new DataValidEventR(true));
        } else if (nameInfo.length() < 4) {
            names.setError(getString(R.string.name_info_msg));
            EventBus.getDefault().post(new DataValidEventR(false));
        } else if(!validatePhone(phoneInfo)){
            phoneNumber.setError(getString(R.string.phone_valid_msg));
            EventBus.getDefault().post(new DataValidEventR(false));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.general_information, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.gen_title));

        names = rootView.findViewById(R.id.name_f);
        //address = rootView.findViewById(R.id.address);
        phoneNumber = rootView.findViewById(R.id.name_phone);
        addressTextView = rootView.findViewById(R.id.address);
        male = rootView.findViewById(R.id.radio_male);
        female = rootView.findViewById(R.id.radio_female);

        content_spiner_coop = rootView.findViewById(R.id.content_spiner_coop);
        content_spiner_coop.setVisibility(View.GONE);
        //spinner = rootView.findViewById(R.id.spinner_coops_infos);
        mGenderGroup = rootView.findViewById(R.id.gender_group);

        //set default gender
        if (mGenderGroup.getCheckedRadioButtonId() == R.id.radio_male) {
            generalVendor.setGender(true);
            mPageVendor.getData().putParcelable("general", generalVendor);
        } else if (mGenderGroup.getCheckedRadioButtonId() == R.id.radio_female) {
            generalVendor.setGender(false);
            mPageVendor.getData().putParcelable("generalVendor", generalVendor);
        }

        //listen for change on gender
        mGenderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.radio_male) {
                    generalVendor.setGender(true);
                    mPageVendor.getData().putParcelable("generalVendor", generalVendor);
                } else if (i == R.id.radio_female) {
                    generalVendor.setGender(false);
                    mPageVendor.getData().putParcelable("generalVendor", generalVendor);
                }
            }
        });

        names.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                generalVendor.setName(charSequence.toString().trim());
                mPageVendor.setData("generalVendor", generalVendor);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        addressTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                generalVendor.setAddress(charSequence.toString().trim());
                mPageVendor.setData("generalVendor", generalVendor);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                generalVendor.setPhoneNumber(charSequence.toString().trim());
                mPageVendor.setData("generalVendor", generalVendor);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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

}
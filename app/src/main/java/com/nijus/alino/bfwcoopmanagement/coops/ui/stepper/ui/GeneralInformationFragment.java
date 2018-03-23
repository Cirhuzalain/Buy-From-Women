package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.GeneralInformation;
import com.nijus.alino.bfwcoopmanagement.events.DataValidEventB;
import com.nijus.alino.bfwcoopmanagement.events.DataValidEventR;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralInformationFragment extends Fragment {

    public static final String ARG_KEY = "key";
    public final String LOG_TAG = GeneralInformationFragment.class.getSimpleName();
    private String mKey;

    private GeneralInformation general = new GeneralInformation();

    private Page mPage;
    private PageFragmentCallbacks mCallbacks;

    private AutoCompleteTextView name;
    private AutoCompleteTextView phone;
    private AutoCompleteTextView mail;
    private AutoCompleteTextView address;
    private AutoCompleteTextView landSize;
    private final Pattern phoneRegex = Pattern.compile("^\\+250[0-9]{9}$",
            Pattern.CASE_INSENSITIVE);

    public GeneralInformationFragment() {
        super();
    }

    public static GeneralInformationFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        GeneralInformationFragment fragment = new GeneralInformationFragment();
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

        String nameInfo = name.getText().toString().trim();
        String addressInfo = address.getText().toString().trim();
        String phoneNumber = phone.getText().toString().trim();
        String email = mail.getText().toString().trim();


        if (!TextUtils.isEmpty(nameInfo) && nameInfo.length() >= 4 && !TextUtils.isEmpty(addressInfo) && !TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(email)) {
            EventBus.getDefault().post(new DataValidEventR(true));
        } else if (TextUtils.isEmpty(nameInfo) || nameInfo.length() < 4) {
            name.setError(getString(R.string.name_coop_info));
            EventBus.getDefault().post(new DataValidEventR(false));
        } else if (!validatePhone(phoneNumber)) {
            phone.setError(getString(R.string.phone_info_coop));
            EventBus.getDefault().post(new DataValidEventR(false));
        } else if (TextUtils.isEmpty(addressInfo)) {
            address.setError(getString(R.string.address_info_coop));
            EventBus.getDefault().post(new DataValidEventR(false));
        } else if (TextUtils.isEmpty(email)) {
            mail.setError(getString(R.string.email_info_coop));
            EventBus.getDefault().post(new DataValidEventR(false));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.general_info_coops, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.gen_title));

        name = rootView.findViewById(R.id.name_f);
        phone = rootView.findViewById(R.id.name_phone);
        address = rootView.findViewById(R.id.address);
        mail = rootView.findViewById(R.id.email);
        landSize = rootView.findViewById(R.id.land_size_under_cip);
        landSize.setEnabled(false);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                general.setName(charSequence.toString().trim());
                mPage.setData("general", general);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                general.setAddress(charSequence.toString().trim());

                mPage.setData("general", general);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                general.setPhone(charSequence.toString().trim());
                mPage.setData("general", general);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                general.setMail(charSequence.toString().trim());
                mPage.setData("general", general);
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
        mCallbacks = (PageFragmentCallbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
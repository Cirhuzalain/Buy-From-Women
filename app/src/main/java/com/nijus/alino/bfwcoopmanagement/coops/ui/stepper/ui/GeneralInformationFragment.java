package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.ui;

import android.content.Context;
import android.database.Cursor;
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
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.events.DataValidEventB;
import com.nijus.alino.bfwcoopmanagement.events.DataValidEventR;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class GeneralInformationFragment extends Fragment {

    public static final String ARG_KEY = "key";
    public final String LOG_TAG = GeneralInformationFragment.class.getSimpleName();
    private String mKey;

    private com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.GeneralInformation general =
            new com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.GeneralInformation();

    private Page mPage;
    private PageFragmentCallbacks mCallbacks;

    private AutoCompleteTextView name;
    private AutoCompleteTextView phone;
    private AutoCompleteTextView mail;
    private AutoCompleteTextView adress;
    private AutoCompleteTextView landSize;

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

    @Subscribe
    public void onDataValidEventB(DataValidEventB validEventB) {

        String nameInfo = name.getText().toString();
        String adressInfo = adress.getText().toString();


        //Add Regex
        if (!TextUtils.isEmpty(nameInfo) && nameInfo.length() >= 4 && !TextUtils.isEmpty(adressInfo)) {
            EventBus.getDefault().post(new DataValidEventR(true));
        } else {
            name.setError("Name is Required");
            adress.setError("Adress is required or not Valid");
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
        adress = rootView.findViewById(R.id.address);
        mail= rootView.findViewById(R.id.email);
        landSize = rootView.findViewById(R.id.land_size_under_cip);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                general.setName(charSequence.toString());
                mPage.setData("general", general);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        adress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                general.setAdress(charSequence.toString());
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
package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui.update;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.DataValidEventB;
import com.nijus.alino.bfwcoopmanagement.events.DataValidEventR;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.PageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.GeneralVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui.GeneralInformationVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui.PageFragmentCallbacksVendor;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class UpdateGeneralInformationVendor extends Fragment implements /* AdapterView.OnItemSelectedListener,*/ LoaderManager.LoaderCallbacks<Cursor> {

    public static final String ARG_KEY = "key";
    public final String LOG_TAG = GeneralInformationVendor.class.getSimpleName();
    private String mKey;

    private GeneralVendor generalVendor = new GeneralVendor();

    private PageVendorVendor mPageVendor;
    private PageFragmentCallbacksVendor mCallbacks;
    private AutoCompleteTextView names;
    private AutoCompleteTextView phoneNumber;
    private AutoCompleteTextView address;
    private RadioButton male;
    private RadioButton female;
    private RadioGroup mGenderGroup;
    //private Spinner spinner;
    private Uri mUri;
    private long mFarmerId;
    private LinearLayout content_spiner_coop;


    public UpdateGeneralInformationVendor() {
        super();
    }

    public static UpdateGeneralInformationVendor create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UpdateGeneralInformationVendor fragment = new UpdateGeneralInformationVendor();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPageVendor = mCallbacks.onGetPage(mKey);

        Intent intent = getActivity().getIntent();

        if (intent.hasExtra("vendorId")) {
            mFarmerId = intent.getLongExtra("vendorId", 0);
            mUri = BfwContract.Vendor.buildVendorUri(mFarmerId);
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
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {

            String name = data.getString(data.getColumnIndex(BfwContract.Vendor.COLUMN_NAME));
            String phone = data.getString(data.getColumnIndex(BfwContract.Vendor.COLUMN_PHONE));
            String gender = data.getString(data.getColumnIndex(BfwContract.Vendor.COLUMN_GENDER));
            String address_s = data.getString(data.getColumnIndex(BfwContract.Vendor.COLUMN_ADDRESS));

            if (gender.equals("male")) {
                male.setChecked(true);
            } else {
                female.setChecked(true);
            }
            names.setText(name);
            phoneNumber.setText(phone);

            if (address == null || address.equals("null")) {
                address.setText("");
            } else {
                address.setText(address_s);
            }
            generalVendor.setName(name);
            generalVendor.setPhoneNumber(phone);
            generalVendor.setAddress(address_s);
            mPageVendor.setData("generalVendor", generalVendor);

            setDefaultGender();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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

        String nameInfo = names.getText().toString();
        String phoneInfo = phoneNumber.getText().toString();

        //Add Regex
        if (!TextUtils.isEmpty(nameInfo) && nameInfo.length() >= 4 && !TextUtils.isEmpty(phoneInfo)) {
            EventBus.getDefault().post(new DataValidEventR(true));
        } else {
            names.setError("Name is Required");
            phoneNumber.setError("Phone Number is required or not Valid");
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
        phoneNumber = rootView.findViewById(R.id.name_phone);
        address = rootView.findViewById(R.id.address);
        male = rootView.findViewById(R.id.radio_male);
        female = rootView.findViewById(R.id.radio_female);
        //spinner = rootView.findViewById(R.id.spinner_coops_infos);
        mGenderGroup = rootView.findViewById(R.id.gender_group);
        content_spiner_coop = rootView.findViewById(R.id.content_spiner_coop);
        content_spiner_coop.setVisibility(View.GONE);


        //set default gender
        setDefaultGender();

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
                generalVendor.setName(charSequence.toString());
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
                generalVendor.setPhoneNumber(charSequence.toString());
                mPageVendor.setData("generalVendor", generalVendor);
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
                generalVendor.setAddress(charSequence.toString());
                mPageVendor.setData("generalVendor", generalVendor);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return rootView;
    }

    public void setDefaultGender() {
        if (mGenderGroup.getCheckedRadioButtonId() == R.id.radio_male) {
            generalVendor.setGender(true);
            mPageVendor.getData().putParcelable("generalVendor", generalVendor);
        } else if (mGenderGroup.getCheckedRadioButtonId() == R.id.radio_female) {
            generalVendor.setGender(false);
            mPageVendor.getData().putParcelable("generalVendor", generalVendor);
        }
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

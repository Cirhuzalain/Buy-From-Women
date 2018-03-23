package com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.DataValidEventB;
import com.nijus.alino.bfwcoopmanagement.events.DataValidEventR;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.General;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralInformation extends Fragment implements AdapterView.OnItemSelectedListener {

    public static final String ARG_KEY = "key";
    public final String LOG_TAG = GeneralInformation.class.getSimpleName();
    private String mKey;

    private General general = new General();

    private Page mPage;
    private PageFragmentCallbacks mCallbacks;
    private AutoCompleteTextView names;
    private AutoCompleteTextView phoneNumber;
    private AutoCompleteTextView addressTextView;
    private RadioButton male;
    private RadioButton female;
    private RadioGroup mGenderGroup;
    private Spinner spinner;
    private final Pattern phoneRegex = Pattern.compile("^\\+250[0-9]{9}$",
            Pattern.CASE_INSENSITIVE);

    public GeneralInformation() {
        super();
    }

    public static GeneralInformation create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        GeneralInformation fragment = new GeneralInformation();
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

        String nameInfo = names.getText().toString().trim();
        String phoneInfo = phoneNumber.getText().toString().trim();

        //Add Regex
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
        phoneNumber = rootView.findViewById(R.id.name_phone);
        addressTextView = rootView.findViewById(R.id.address);
        male = rootView.findViewById(R.id.radio_male);
        female = rootView.findViewById(R.id.radio_female);
        spinner = rootView.findViewById(R.id.spinner_coops_infos);
        mGenderGroup = rootView.findViewById(R.id.gender_group);

        //set default gender
        if (mGenderGroup.getCheckedRadioButtonId() == R.id.radio_male) {
            general.setGender(true);
            mPage.getData().putParcelable("general", general);
        } else if (mGenderGroup.getCheckedRadioButtonId() == R.id.radio_female) {
            general.setGender(false);
            mPage.getData().putParcelable("general", general);
        }

        //listen for change on gender
        mGenderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.radio_male) {
                    general.setGender(true);
                    mPage.getData().putParcelable("general", general);
                } else if (i == R.id.radio_female) {
                    general.setGender(false);
                    mPage.getData().putParcelable("general", general);
                }
            }
        });

        names.addTextChangedListener(new TextWatcher() {
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

        addressTextView.addTextChangedListener(new TextWatcher() {
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

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                general.setPhoneNumber(charSequence.toString().trim());
                mPage.setData("general", general);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        populateSpinner();
        spinner.setOnItemSelectedListener(this);
        return rootView;
    }

    public void populateSpinner() {
        String[] fromColumns = {BfwContract.Coops.COLUMN_COOP_NAME};

        // View IDs to map the columns (fetched above) into
        int[] toViews = {
                android.R.id.text1
        };
        Cursor cursor = getActivity().getContentResolver().query(
                BfwContract.Coops.CONTENT_URI,
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
            spinner.setAdapter(adapter);
        }
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

        Cursor cursor = (Cursor) spinner.getSelectedItem();
        general.setCoopId(cursor.getInt(cursor.getColumnIndex(BfwContract.Coops._ID)));
        mPage.setData("general", general);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}
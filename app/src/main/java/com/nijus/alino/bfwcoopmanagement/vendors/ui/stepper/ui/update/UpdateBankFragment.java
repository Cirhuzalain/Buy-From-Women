package com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui.update;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pages.PageVendorVendor;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.model.pojo.BankInformation;
import com.nijus.alino.bfwcoopmanagement.vendors.ui.stepper.ui.PageFragmentCallbacksVendor;

public class UpdateBankFragment extends Fragment {

    public static final String ARG_KEY = "key";
    private String mKey;
    private PageVendorVendor mPageVendor;
    private PageFragmentCallbacksVendor mCallbacks;

    private BankInformation bankInformation = new BankInformation();

    private AutoCompleteTextView bankNumber;
    private AutoCompleteTextView bankName;

    public UpdateBankFragment() {
        super();
    }

    public static UpdateBankFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UpdateBankFragment fragment = new UpdateBankFragment();
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
        View rootView = inflater.inflate(R.layout.bank_fragment, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.bank_info));

        bankNumber = rootView.findViewById(R.id.bank_number);
        bankName = rootView.findViewById(R.id.b_name);

        bankNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bankInformation.setAccountNumber(charSequence.toString());
                mPageVendor.getData().putParcelable("b_info", bankInformation);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        bankName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bankInformation.setBankName(charSequence.toString());
                mPageVendor.getData().putParcelable("b_info", bankInformation);
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

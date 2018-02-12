package com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.ui.update;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.model.pojo.BaseLine;
import com.nijus.alino.bfwcoopmanagement.farmers.ui.stepper.ui.PageFragmentCallbacks;

public class UpdateBaseLineFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String ARG_KEY = "key";
    private String mKey;
    private Page mPage;
    private PageFragmentCallbacks mCallbacks;
    private Uri mUri;
    private long mFarmerId;

    private BaseLine baseLine = new BaseLine();

    private AutoCompleteTextView totProdKg;
    private AutoCompleteTextView totLostKg;
    private AutoCompleteTextView totSoldKg;
    private AutoCompleteTextView totVolSoldCoops;
    private AutoCompleteTextView priceSoldToCoop;
    private AutoCompleteTextView totVolSoldKg;
    private AutoCompleteTextView priceSoldKg;

    public UpdateBaseLineFragment() {
        super();
    }

    public static UpdateBaseLineFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        UpdateBaseLineFragment fragment = new UpdateBaseLineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = mCallbacks.onGetPage(mKey);

        Intent intent = getActivity().getIntent();

        if (intent.hasExtra("farmerId")) {
            mFarmerId = intent.getLongExtra("farmerId", 0);
            mUri = BfwContract.Farmer.buildFarmerUri(mFarmerId);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(1, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String farmerSelection = BfwContract.Farmer.TABLE_NAME + "." +
                BfwContract.Farmer._ID + " =  ? ";

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
            int totProd = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_TOT_PROD_B_KG));
            int totLost = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_TOT_LOST_KG));
            int totSolKg = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_TOT_SOLD_KG));
            int totVSoldCoop = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_TOT_VOL_SOLD_COOP));
            int priceSoldCoop = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_PRICE_SOLD_COOP_PER_KG));
            int totVolSKg = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_TOT_VOL_SOLD_IN_KG));
            int priceSold = data.getInt(data.getColumnIndex(BfwContract.Farmer.COLUMN_PRICE_SOLD_KG));

            totProdKg.setText("" + totProd + "");
            totLostKg.setText("" + totLost + "");
            totSoldKg.setText("" + totSolKg + "");
            totVolSoldCoops.setText("" + totVSoldCoop + "");
            priceSoldToCoop.setText("" + priceSoldCoop + "");
            totVolSoldKg.setText("" + totVolSKg + "");
            priceSoldKg.setText("" + priceSold + "");

            baseLine.setTotProdInKg(totProd);
            baseLine.setTotLostInKg(totLost);
            baseLine.setTotSoldInKg(totSolKg);
            baseLine.setTotVolumeSoldCoopInKg(totVSoldCoop);
            baseLine.setPriceSoldToCoopPerKg(priceSoldCoop);
            baseLine.setTotVolSoldInKg(totVolSKg);
            baseLine.setPriceSoldInKg(priceSold);
            mPage.getData().putParcelable("baseline", baseLine);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.baseline_layout, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.base_line));

        totProdKg = rootView.findViewById(R.id.tot_prod_kg);
        totLostKg = rootView.findViewById(R.id.tot_lost);
        totSoldKg = rootView.findViewById(R.id.tot_sold);
        totVolSoldCoops = rootView.findViewById(R.id.tot_vol_coop);
        priceSoldToCoop = rootView.findViewById(R.id.price_sold_kg);
        totVolSoldKg = rootView.findViewById(R.id.tot_vol_side_sold);
        priceSoldKg = rootView.findViewById(R.id.pr_sold_kg);

        totProdKg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    baseLine.setTotProdInKg(Integer.parseInt(charSequence.toString()));
                    mPage.getData().putParcelable("baseline", baseLine);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        totLostKg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    baseLine.setTotLostInKg(Integer.parseInt(charSequence.toString()));
                    mPage.getData().putParcelable("baseline", baseLine);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        totSoldKg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    baseLine.setTotSoldInKg(Integer.parseInt(charSequence.toString()));
                    mPage.getData().putParcelable("baseline", baseLine);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        totVolSoldCoops.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    baseLine.setTotVolumeSoldCoopInKg(Integer.parseInt(charSequence.toString()));
                    mPage.getData().putParcelable("baseline", baseLine);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        priceSoldToCoop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    baseLine.setPriceSoldToCoopPerKg(Integer.parseInt(charSequence.toString()));
                    mPage.getData().putParcelable("baseline", baseLine);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        totVolSoldKg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    baseLine.setTotVolSoldInKg(Integer.parseInt(charSequence.toString()));
                    mPage.getData().putParcelable("baseline", baseLine);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        priceSoldKg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    baseLine.setPriceSoldInKg(Integer.parseInt(charSequence.toString()));
                    mPage.getData().putParcelable("baseline", baseLine);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }

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

package com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pages.Page;
import com.nijus.alino.bfwcoopmanagement.coops.ui.stepper.model.pojo.BaselineSales;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;

import java.util.HashMap;

public class BaselineSalesFragment extends Fragment {

    public static final String ARG_KEY = "key";
    public final String LOG_TAG = BaselineSalesFragment.class.getSimpleName();
    private String mKey;

    private BaselineSales baselineSales = new BaselineSales();

    private Cursor cursor;
    private String seasonName;
    private int seasonId;

    private HashMap<String, BaselineSales> baselineSalesSeason = new HashMap<>();

    private Page mPage;
    private PageFragmentCallbacks mCallbacks;

    private Spinner harvestSeason;
    private Spinner rgccContactUnderFtma;

    private AutoCompleteTextView qty_agregated_from_members;
    private AutoCompleteTextView cycle_h_at_price_per_kg;
    private AutoCompleteTextView qty_purchaced_from_non_members;
    private AutoCompleteTextView non_member_purchase_at_price_per_kg;
    private AutoCompleteTextView qty_of_rgcc_contact;
    private AutoCompleteTextView qty_sold_to_rgcc;
    private AutoCompleteTextView price_per_kg_sold_to_rgcc;
    private AutoCompleteTextView qty_slod_outside_rgcc;

    private CheckBox formal_buyer;
    private CheckBox informal_buyer;
    private CheckBox other;

    private AutoCompleteTextView price_per_kg_sold_outside_ftma;


    public BaselineSalesFragment() {
        super();
    }

    public static BaselineSalesFragment create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        BaselineSalesFragment fragment = new BaselineSalesFragment();
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
        View rootView = inflater.inflate(R.layout.baseline_sales, container, false);

        TextView textView = rootView.findViewById(R.id.page_title);
        textView.setText(getContext().getString(R.string.baseline_sales));


        harvestSeason = rootView.findViewById(R.id.harvestSeason);
        rgccContactUnderFtma = rootView.findViewById(R.id.rgcc_contact_under_ftma);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.rgcc_contract, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rgccContactUnderFtma.setAdapter(adapter);

        populateSpinner();

        qty_agregated_from_members = rootView.findViewById(R.id.qty_agregated_from_members);
        cycle_h_at_price_per_kg = rootView.findViewById(R.id.cycle_h_at_price_per_kg);
        qty_purchaced_from_non_members = rootView.findViewById(R.id.qty_purchaced_from_non_members);
        non_member_purchase_at_price_per_kg = rootView.findViewById(R.id.non_member_purchase_at_price_per_kg);

        qty_of_rgcc_contact = rootView.findViewById(R.id.qty_of_rgcc_contact);
        qty_sold_to_rgcc = rootView.findViewById(R.id.qty_sold_to_rgcc);
        price_per_kg_sold_to_rgcc = rootView.findViewById(R.id.price_per_kg_sold_to_rgcc);
        qty_slod_outside_rgcc = rootView.findViewById(R.id.qty_slod_outside_rgcc);
        price_per_kg_sold_outside_ftma = rootView.findViewById(R.id.price_per_kg_sold_outside_ftma);


        formal_buyer = rootView.findViewById(R.id.formal_buyer);
        boolean isFormal_buyer = formal_buyer.isActivated();
        baselineSales.setFormalBuyer(isFormal_buyer);

        informal_buyer = rootView.findViewById(R.id.informal_buyer);
        boolean isInformal_buyer = informal_buyer.isActivated();
        baselineSales.setInformalBuyer(isInformal_buyer);

        other = rootView.findViewById(R.id.other);
        boolean isOther = other.isActivated();
        baselineSales.setOther(isOther);

        String rgccContract = rgccContactUnderFtma.getSelectedItem().toString();
        baselineSales.setRgccContractUnderFtma(rgccContract);

        cursor = (Cursor) harvestSeason.getSelectedItem();
        seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
        baselineSales.setSeasonId(seasonId);

        baselineSalesSeason.put(seasonName, baselineSales);
        mPage.getData().putSerializable("baseline_sales", baselineSalesSeason);


        rgccContactUnderFtma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                if (baselineSalesSeason.containsKey(seasonName)) {
                    baselineSalesSeason.get(seasonName).setRgccContractUnderFtma(adapterView.getItemAtPosition(i).toString());
                } else {
                    BaselineSales baselineSales = new BaselineSales();
                    seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                    baselineSales.setSeasonId(seasonId);
                    baselineSales.setRgccContractUnderFtma(adapterView.getItemAtPosition(i).toString());
                    baselineSalesSeason.put(seasonName, baselineSales);
                }
                mPage.getData().putSerializable("baseline_sales", baselineSalesSeason);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        formal_buyer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (baselineSalesSeason.containsKey(seasonName)) {
                        baselineSalesSeason.get(seasonName).setFormalBuyer(true);
                    } else {
                        BaselineSales baselineSales = new BaselineSales();
                        baselineSales.setFormalBuyer(true);
                        baselineSales.setSeasonId(seasonId);
                        baselineSalesSeason.put(seasonName, baselineSales);
                    }
                } else {
                    if (baselineSalesSeason.containsKey(seasonName)) {
                        baselineSalesSeason.get(seasonName).setFormalBuyer(false);
                    } else {
                        BaselineSales baselineSales = new BaselineSales();
                        baselineSales.setFormalBuyer(false);
                        baselineSales.setSeasonId(seasonId);
                        baselineSalesSeason.put(seasonName, baselineSales);
                    }
                }
                mPage.getData().putSerializable("baseline_sales", baselineSalesSeason);
            }
        });

        informal_buyer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (baselineSalesSeason.containsKey(seasonName)) {
                        baselineSalesSeason.get(seasonName).setInformalBuyer(true);
                    } else {
                        BaselineSales baselineSales = new BaselineSales();
                        baselineSales.setInformalBuyer(true);
                        baselineSales.setSeasonId(seasonId);
                        baselineSalesSeason.put(seasonName, baselineSales);
                    }
                } else {
                    if (baselineSalesSeason.containsKey(seasonName)) {
                        baselineSalesSeason.get(seasonName).setInformalBuyer(false);
                    } else {
                        BaselineSales baselineSales = new BaselineSales();
                        baselineSales.setInformalBuyer(false);
                        baselineSales.setSeasonId(seasonId);
                        baselineSalesSeason.put(seasonName, baselineSales);
                    }
                }
                mPage.getData().putSerializable("baseline_sales", baselineSalesSeason);
            }
        });

        other.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));
                seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                if (b) {
                    if (baselineSalesSeason.containsKey(seasonName)) {
                        baselineSalesSeason.get(seasonName).setOther(true);
                    } else {
                        BaselineSales baselineSales = new BaselineSales();
                        baselineSales.setOther(true);
                        baselineSales.setSeasonId(seasonId);
                        baselineSalesSeason.put(seasonName, baselineSales);
                    }
                } else {
                    if (baselineSalesSeason.containsKey(seasonName)) {
                        baselineSalesSeason.get(seasonName).setOther(false);
                    } else {
                        BaselineSales baselineSales = new BaselineSales();
                        baselineSales.setOther(false);
                        baselineSales.setSeasonId(seasonId);
                        baselineSalesSeason.put(seasonName, baselineSales);
                    }
                }
                mPage.getData().putSerializable("baseline_sales", baselineSalesSeason);
            }
        });


        qty_agregated_from_members.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    cursor = (Cursor) harvestSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                    if (baselineSalesSeason.containsKey(seasonName)) {
                        baselineSalesSeason.get(seasonName).setQtyAgregatedFromMember(Integer.parseInt(charSequence.toString()));
                    } else {
                        BaselineSales baselineSales = new BaselineSales();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        baselineSales.setSeasonId(seasonId);
                        baselineSales.setQtyAgregatedFromMember(Integer.parseInt(charSequence.toString()));
                        baselineSalesSeason.put(seasonName, baselineSales);
                    }
                    mPage.getData().putSerializable("baseline_sales", baselineSalesSeason);

                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        cycle_h_at_price_per_kg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {

                    cursor = (Cursor) harvestSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                    if (baselineSalesSeason.containsKey(seasonName)) {
                        baselineSalesSeason.get(seasonName).setCycleHarvsetAtPricePerKg(Integer.parseInt(charSequence.toString()));
                    } else {
                        BaselineSales baselineSales = new BaselineSales();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        baselineSales.setSeasonId(seasonId);
                        baselineSales.setCycleHarvsetAtPricePerKg(Integer.parseInt(charSequence.toString()));
                        baselineSalesSeason.put(seasonName, baselineSales);
                    }
                    mPage.getData().putSerializable("baseline_sales", baselineSalesSeason);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        qty_purchaced_from_non_members.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    cursor = (Cursor) harvestSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                    if (baselineSalesSeason.containsKey(seasonName)) {
                        baselineSalesSeason.get(seasonName).setQtyPurchaseFromNonMember(Integer.parseInt(charSequence.toString()));
                    } else {
                        BaselineSales baselineSales = new BaselineSales();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        baselineSales.setSeasonId(seasonId);
                        baselineSales.setQtyPurchaseFromNonMember(Integer.parseInt(charSequence.toString()));
                        baselineSalesSeason.put(seasonName, baselineSales);
                    }
                    mPage.getData().putSerializable("baseline_sales", baselineSalesSeason);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        non_member_purchase_at_price_per_kg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (baselineSalesSeason.containsKey(seasonName)) {
                        baselineSalesSeason.get(seasonName).setNonMemberAtPricePerKg(Integer.parseInt(charSequence.toString()));
                    } else {
                        BaselineSales baselineSales = new BaselineSales();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        baselineSales.setSeasonId(seasonId);
                        baselineSales.setNonMemberAtPricePerKg(Integer.parseInt(charSequence.toString()));
                        baselineSalesSeason.put(seasonName, baselineSales);
                    }
                    mPage.getData().putSerializable("baseline_sales", baselineSalesSeason);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        qty_of_rgcc_contact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                try {
                    if (baselineSalesSeason.containsKey(seasonName)) {
                        baselineSalesSeason.get(seasonName).setQtyOfRgccContract(Double.parseDouble(charSequence.toString()));
                    } else {
                        BaselineSales baselineSales = new BaselineSales();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        baselineSales.setSeasonId(seasonId);
                        baselineSales.setQtyOfRgccContract(Double.parseDouble(charSequence.toString()));
                        baselineSalesSeason.put(seasonName, baselineSales);
                    }
                    mPage.getData().putSerializable("baseline_sales", baselineSalesSeason);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        qty_sold_to_rgcc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                try {
                    if (baselineSalesSeason.containsKey(seasonName)) {
                        baselineSalesSeason.get(seasonName).setQtySoldToRgcc(Double.parseDouble(charSequence.toString()));
                    } else {
                        BaselineSales baselineSales = new BaselineSales();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        baselineSales.setSeasonId(seasonId);
                        baselineSales.setQtySoldToRgcc(Double.parseDouble(charSequence.toString()));
                        baselineSalesSeason.put(seasonName, baselineSales);
                    }
                    mPage.getData().putSerializable("baseline_sales", baselineSalesSeason);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        price_per_kg_sold_to_rgcc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                cursor = (Cursor) harvestSeason.getSelectedItem();
                seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                try {

                    if (baselineSalesSeason.containsKey(seasonName)) {
                        baselineSalesSeason.get(seasonName).setPricePerKgSoldToRgcc(Double.parseDouble(charSequence.toString()));
                    } else {
                        BaselineSales baselineSales = new BaselineSales();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        baselineSales.setSeasonId(seasonId);
                        baselineSales.setPricePerKgSoldToRgcc(Double.parseDouble(charSequence.toString()));
                        baselineSalesSeason.put(seasonName, baselineSales);
                    }
                    mPage.getData().putSerializable("baseline_sales", baselineSalesSeason);
                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        qty_slod_outside_rgcc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {

                    cursor = (Cursor) harvestSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                    if (baselineSalesSeason.containsKey(seasonName)) {
                        baselineSalesSeason.get(seasonName).setQtySoldOutOfRgcc(Double.parseDouble(charSequence.toString()));
                    } else {
                        BaselineSales baselineSales = new BaselineSales();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        baselineSales.setSeasonId(seasonId);
                        baselineSales.setQtySoldOutOfRgcc(Double.parseDouble(charSequence.toString()));
                        baselineSalesSeason.put(seasonName, baselineSales);
                    }
                    mPage.getData().putSerializable("baseline_sales", baselineSalesSeason);


                } catch (NumberFormatException exp) {
                    exp.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        price_per_kg_sold_outside_ftma.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {

                    cursor = (Cursor) harvestSeason.getSelectedItem();
                    seasonName = cursor.getString(cursor.getColumnIndex(BfwContract.HarvestSeason.COLUMN_NAME));

                    if (baselineSalesSeason.containsKey(seasonName)) {
                        baselineSalesSeason.get(seasonName).setPricePerKkSoldOutFtma(Double.parseDouble(charSequence.toString()));
                    } else {
                        BaselineSales baselineSales = new BaselineSales();
                        seasonId = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));
                        baselineSales.setSeasonId(seasonId);
                        baselineSales.setPricePerKkSoldOutFtma(Double.parseDouble(charSequence.toString()));
                        baselineSalesSeason.put(seasonName, baselineSales);
                    }
                    mPage.getData().putSerializable("baseline_sales", baselineSalesSeason);

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

    public void populateSpinner() {
        String[] fromColumns = {BfwContract.HarvestSeason.COLUMN_NAME};

        // View IDs to map the columns (fetched above) into
        int[] toViews = {
                android.R.id.text1
        };
        Cursor cursor = getActivity().getContentResolver().query(
                BfwContract.HarvestSeason.CONTENT_URI,
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
            harvestSeason.setAdapter(adapter);
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
}
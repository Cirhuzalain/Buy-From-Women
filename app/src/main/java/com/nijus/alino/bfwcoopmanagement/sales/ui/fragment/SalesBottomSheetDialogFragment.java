package com.nijus.alino.bfwcoopmanagement.sales.ui.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SalesBottomSheetDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private LinearLayout mProductContainer;
    private Button addProductItem, ok;
    private Spinner customer;
    private Spinner harvestSeason;
    private Spinner coops;
    private Spinner paymentTerm;
    private DatePicker date;

    private EditText ed_date_value;
    private Button date_btn;

    private Spinner productName;
    private EditText quantity;
    private EditText unitPrice;

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback =
            new BottomSheetBehavior.BottomSheetCallback() {

                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        dismiss();
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    if (slideOffset < 0) {
                        dismiss();
                        //Toast.makeText(getContext()," "+slideOffset, Toast.LENGTH_LONG).show();
                    }
                }

            };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        //setupDialog(dialog,style);

        View viewContainer = View.inflate(getContext(), R.layout.sale_fragment_bottom_sheet, null);

        dialog.setContentView(viewContainer);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) viewContainer.getParent()).getLayoutParams();

        //Make responsive bottom sheet
        int width = getContext().getResources().getDimensionPixelSize(R.dimen.padding_bottom_sheet) / 2;
        params.setMargins(width, 0, width, 0);

        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        mProductContainer = viewContainer.findViewById(R.id.saleOrderContainer);
        addProductItem = viewContainer.findViewById(R.id.add_so_order);
        addProductItem.setOnClickListener(this);

        customer = viewContainer.findViewById(R.id.customer_spinner);
        harvestSeason = viewContainer.findViewById(R.id.so_harv_season);
        coops = viewContainer.findViewById(R.id.so_coop_info);
        paymentTerm = viewContainer.findViewById(R.id.so_payment_term);

        date_btn = viewContainer.findViewById(R.id.date_selected);
        date_btn.setOnClickListener(this);
        ed_date_value = viewContainer.findViewById(R.id.ed_date_value);

        productName = viewContainer.findViewById(R.id.product_so_order);
        quantity = viewContainer.findViewById(R.id.product_quantity_so);
        unitPrice = viewContainer.findViewById(R.id.product_price_so);
        ok = viewContainer.findViewById(R.id.ok);
        ok.setOnClickListener(this);

        populateCustomer();
        populateCoops();
        populateSeason();
        populateProduct();
        populatePaymentTerm();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_so_order) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            Spinner spinner = new Spinner(getContext());

            EditText quantity = new EditText(getContext());
            quantity.setWidth(AppBarLayout.LayoutParams.MATCH_PARENT);
            quantity.setHeight(AppBarLayout.LayoutParams.WRAP_CONTENT);
            quantity.setHint(getResources().getString(R.string.quantity));
            quantity.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);

            EditText unitPrice = new EditText(getContext());
            unitPrice.setWidth(AppBarLayout.LayoutParams.MATCH_PARENT);
            unitPrice.setHeight(AppBarLayout.LayoutParams.WRAP_CONTENT);
            unitPrice.setHint(getResources().getString(R.string.price_rwf));
            unitPrice.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);

            linearLayout.addView(spinner);
            linearLayout.addView(quantity);
            linearLayout.addView(unitPrice);

            populateProductSpinner(spinner);

            mProductContainer.addView(linearLayout, 1);
        }
        if (view == date_btn) {
            final Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0, int year, int month, int day_of_month) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, (month));
                    calendar.set(Calendar.DAY_OF_MONTH, day_of_month);
                    String myFormat = "dd/MM/yyyy";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                    ed_date_value.setText(sdf.format(calendar.getTime()));
                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.YEAR, 2000);
            dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());// TODO: used to hide previous date,month and year
            calendar.set(Calendar.YEAR, 2030);
            dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());// TODO: used to hide future date,month and year

            dialog.show();
        }
        if (view == ok) {
            Toast.makeText(getContext(), "Coming soon !!!", Toast.LENGTH_LONG).show();
        }
    }

    public void populateCustomer() {
        String[] fromColumns = {BfwContract.Buyer.COLUMN_BUYER_NAME};

        // View IDs to map the columns (fetched above) into
        int[] toViews = {
                android.R.id.text1
        };
        Cursor cursor = getActivity().getContentResolver().query(
                BfwContract.Buyer.CONTENT_URI,
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
            customer.setAdapter(adapter);
        }
    }

    public void populateCoops() {
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
            coops.setAdapter(adapter);
        }
    }

    public void populateSeason() {
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

    public void populateProduct() {
        String[] fromColumns = {BfwContract.ProductTemplate.COLUMN_PRODUCT_NAME};

        // View IDs to map the columns (fetched above) into
        int[] toViews = {
                android.R.id.text1
        };
        Cursor cursor = getActivity().getContentResolver().query(
                BfwContract.ProductTemplate.CONTENT_URI,
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
            productName.setAdapter(adapter);
        }
    }

    public void populatePaymentTerm() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.payment_detail, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentTerm.setAdapter(adapter);
    }

    public void populateProductSpinner(Spinner spinner) {
        String[] fromColumns = {BfwContract.ProductTemplate.COLUMN_PRODUCT_NAME};

        // View IDs to map the columns (fetched above) into
        int[] toViews = {
                android.R.id.text1
        };
        Cursor cursor = getActivity().getContentResolver().query(
                BfwContract.ProductTemplate.CONTENT_URI,
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

}

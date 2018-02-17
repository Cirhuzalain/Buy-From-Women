package com.nijus.alino.bfwcoopmanagement.sales.ui.fragment;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SalesBottomSheetDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private LinearLayout mProductContainer;
    private Button addProductItem,ok;
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
            if (slideOffset < 0) {dismiss();
            //Toast.makeText(getContext()," "+slideOffset, Toast.LENGTH_LONG).show();
            }
        }

    };
    //@SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        //setupDialog(dialog,style);

        View viewContainer = View.inflate(getContext(), R.layout.sale_fragment_bottom_sheet, null);

        dialog.setContentView(viewContainer);

        float heightDp = getResources().getDisplayMetrics().widthPixels;

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) viewContainer.getParent()).getLayoutParams();

        //Toast.makeText(getContext()," "+f, Toast.LENGTH_LONG).show();

        //params.setMargins(50,50,50,50);
        int width = getContext().getResources().getDimensionPixelSize(R.dimen.padding_bottom_sheet)/2;
        params.setMargins(width,0,width,0);



        CoordinatorLayout.Behavior behavior = params.getBehavior();



        if( behavior != null && behavior instanceof BottomSheetBehavior ) {
            //((BottomSheetBehavior) behavior).setPeekHeight(100);
            //int f = ((BottomSheetBehavior) behavior).getPeekHeight();
            //oast.makeText(getContext()," "+f, Toast.LENGTH_LONG).show();


            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }


        //View viewContainer = getActivity().getLayoutInflater().inflate(R.layout.add_sales_order, null);

        mProductContainer = viewContainer.findViewById(R.id.saleOrderContainer);
        addProductItem = viewContainer.findViewById(R.id.add_so_order);
        addProductItem.setOnClickListener(this);

        customer = viewContainer.findViewById(R.id.customer_spinner);
        harvestSeason = viewContainer.findViewById(R.id.so_harv_season);
        coops = viewContainer.findViewById(R.id.so_coop_info);
        paymentTerm = viewContainer.findViewById(R.id.so_payment_term);
        //date = viewContainer.findViewById(R.id.so_datePicker);

        date_btn = viewContainer.findViewById(R.id.date_selected);
        date_btn.setOnClickListener(this);
        ed_date_value = viewContainer.findViewById(R.id.ed_date_value);

        productName = viewContainer.findViewById(R.id.product_so_order);
        quantity = viewContainer.findViewById(R.id.product_quantity_so);
        unitPrice = viewContainer.findViewById(R.id.product_price_so);
        ok = viewContainer.findViewById(R.id.ok);
        ok.setOnClickListener(this);


    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_so_order) {
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);

            //add product datastructure
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

            mProductContainer.addView(linearLayout, 1);
        }
        if(view == date_btn){
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
            },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.YEAR, 2000);
            dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());// TODO: used to hide previous date,month and year
            calendar.set(Calendar.YEAR, 2030);
            dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());// TODO: used to hide future date,month and year

            dialog.show();
        }
        if (view == ok){
            Toast.makeText(getContext(),"Save sales comming soon",Toast.LENGTH_LONG).show();
        }
    }


}

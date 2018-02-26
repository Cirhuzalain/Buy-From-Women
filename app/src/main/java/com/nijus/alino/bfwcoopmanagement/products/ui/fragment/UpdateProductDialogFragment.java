package com.nijus.alino.bfwcoopmanagement.products.ui.fragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.products.pojo.PojoProduct;
import com.nijus.alino.bfwcoopmanagement.products.sync.AddProduct;
import com.nijus.alino.bfwcoopmanagement.products.sync.UpdateProduct;

import java.util.Calendar;

public class UpdateProductDialogFragment extends DialogFragment implements DialogInterface.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private LinearLayout mProductContainer;
    private Spinner vendor;
    private Spinner harvsetSeason;
    private Spinner grade;
    private int id_product,product_server_id;
    private Button create_product;

    AutoCompleteTextView product_name;
    AutoCompleteTextView quantity;
    //AutoCompleteTextView cost;
    AutoCompleteTextView sale_price;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        getLoaderManager().initLoader(0,null,this);

        // Get the layout inflater
        View viewContainer = getActivity().getLayoutInflater().inflate(R.layout.product_order_detail, null);

        mProductContainer = viewContainer.findViewById(R.id.productContainer);

        vendor = viewContainer.findViewById(R.id.vendor);
        harvsetSeason = viewContainer.findViewById(R.id.harvsetSeason);
        grade = viewContainer.findViewById(R.id.grade);
        create_product = viewContainer.findViewById(R.id.create_product);
        create_product.setOnClickListener(this);

        product_name = viewContainer.findViewById(R.id.product_name);
        quantity = viewContainer.findViewById(R.id.quantity);
        sale_price = viewContainer.findViewById(R.id.sale_price);

        builder.setView(viewContainer)
                //.setPositiveButton(R.string.msg_ok, this)
                .setNegativeButton(R.string.msg_cancel, this);

        populateSpinnerHarvestSeason();
        populateSpinnerFarmer();

        ArrayAdapter<CharSequence> adapter_grade = ArrayAdapter.createFromResource(getContext(),
                R.array.grade_array, android.R.layout.simple_spinner_item);
        adapter_grade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grade.setAdapter(adapter_grade);

        return builder.create();
    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        id_product = getArguments().getInt("id_product");
        String productSelection = BfwContract.ProductTemplate.TABLE_NAME + "." +
                BfwContract.ProductTemplate.COLUMN_SERVER_ID + " =  ? ";
        return new CursorLoader(
                getContext(),
                BfwContract.ProductTemplate.CONTENT_URI,
                null,
                productSelection,
                new String[]{Long.toString(id_product)},
                null
        );

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            product_server_id = data.getInt(data.getColumnIndex(BfwContract.ProductTemplate._ID));

            product_name.setText(data.getString(data.getColumnIndex(BfwContract.ProductTemplate.COLUMN_PRODUCT_NAME)));
            sale_price.setText(""+data.getInt(data.getColumnIndex(BfwContract.ProductTemplate.COLUMN_PRICE)));
            quantity.setText(""+data.getDouble(data.getColumnIndex(BfwContract.ProductTemplate.COLUMN_VENDOR_QTY)));

            setSpinnerItemByIdHarvSeason(harvsetSeason,data.getInt(data.getColumnIndex(BfwContract.ProductTemplate.COLUMN_HARVEST_SEASON)));
            setSpinnerItemByIdFarmer(vendor,data.getInt(data.getColumnIndex(BfwContract.ProductTemplate.COLUMN_FARMER_ID)));
            setSpinnerItemGrade(grade,data.getString(data.getColumnIndex(BfwContract.ProductTemplate.COLUMN_HARVEST_GRADE)));
        }
    }
    public void setSpinnerItemGrade(Spinner spinner, String grade) {
        int spinnerCount = spinner.getCount();
        for (int i = 0; i < spinnerCount; i++) {
            String value = (String) spinner.getItemAtPosition(i);
            //int id = value.getInt(value.getColumnIndex(BfwContract.Farmer._ID));
            if (value.equals(grade)) {
                spinner.setSelection(i);
            }
        }
    }
    public void setSpinnerItemByIdFarmer(Spinner spinner, int _id) {
        int spinnerCount = spinner.getCount();
        for (int i = 0; i < spinnerCount; i++) {
            Cursor value = (Cursor) spinner.getItemAtPosition(i);
            int id = value.getInt(value.getColumnIndex(BfwContract.Farmer._ID));
            if (id == _id) {
                spinner.setSelection(i);
            }
        }
    }
    public void setSpinnerItemByIdHarvSeason(Spinner spinner, int _id) {
        int spinnerCount = spinner.getCount();
        for (int i = 0; i < spinnerCount; i++) {
            Cursor value = (Cursor) spinner.getItemAtPosition(i);
            int id = value.getInt(value.getColumnIndex(BfwContract.HarvestSeason._ID));
            if (id == _id) {
                spinner.setSelection(i);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void populateSpinnerHarvestSeason() {
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
            harvsetSeason.setAdapter(adapter);
        }
    }

    public void populateSpinnerFarmer() {
        String[] fromColumns = {BfwContract.Farmer.COLUMN_NAME};

        // View IDs to map the columns (fetched above) into
        int[] toViews = {
                android.R.id.text1
        };
        Cursor cursor = getActivity().getContentResolver().query(
                BfwContract.Farmer.CONTENT_URI,
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
            vendor.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View view) {
        try {

            int farmer_spiner_id, harvest_s_spinner_id;
            String grade_spinner_name;

            // Check if value's length entered is > 3 char .
            if (!isValidString(String.valueOf(product_name.getText()))) {
                product_name.setError(getString(R.string.error_invalid_password));
            }

            // Check for a valid qty
            if (TextUtils.isEmpty(quantity.getText())) {
                quantity.setError(getString(R.string.error_field_required));
            }
            if (TextUtils.isEmpty(sale_price.getText())) {
                sale_price.setError(getString(R.string.error_field_required));
            }

            if (isValidString(String.valueOf(product_name.getText())) && !TextUtils.isEmpty(quantity.getText())
                    && !TextUtils.isEmpty(sale_price.getText())) {

                Cursor cursor = (Cursor) vendor.getSelectedItem();
                farmer_spiner_id = cursor.getInt(cursor.getColumnIndex(BfwContract.Farmer._ID));

                cursor = (Cursor) harvsetSeason.getSelectedItem();
                harvest_s_spinner_id = cursor.getInt(cursor.getColumnIndex(BfwContract.HarvestSeason._ID));

                grade_spinner_name = (String) grade.getSelectedItem();

                PojoProduct pojoProduct = new PojoProduct();
                pojoProduct.setName(String.valueOf(product_name.getText()));
                pojoProduct.setFarmer(farmer_spiner_id);
                pojoProduct.setGrade(grade_spinner_name);
                pojoProduct.setHarvest_season(harvest_s_spinner_id);
                pojoProduct.setPrice(Integer.valueOf(sale_price.getText().toString()));
                pojoProduct.setQuantity(Double.valueOf(quantity.getText().toString()));

                Bundle bundle = new Bundle();
                bundle.putParcelable("product", pojoProduct);

                Intent intent = new Intent(getContext(), UpdateProduct.class);
                intent.putExtra("product_data", bundle);
                intent.putExtra("product_id", product_server_id);

                getContext().startService(intent);
                //Toast.makeText(getContext(),"Success "+product_server_id,Toast.LENGTH_LONG).show();
                dismiss();

            }
            else {
                Toast.makeText(getContext(),"Erreur des donnees",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            return;
        }
    }
    private boolean isValidString(String word) {
        return word.length() >= 3;
    }

}

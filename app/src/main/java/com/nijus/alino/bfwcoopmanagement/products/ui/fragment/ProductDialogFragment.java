package com.nijus.alino.bfwcoopmanagement.products.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.products.pojo.PojoProduct;
import com.nijus.alino.bfwcoopmanagement.products.sync.AddProduct;
import com.nijus.alino.bfwcoopmanagement.ui.fragment.ProgressDialog;

public class ProductDialogFragment extends DialogFragment implements DialogInterface.OnClickListener,
        View.OnClickListener {

    AutoCompleteTextView product_name;
    AutoCompleteTextView quantity;
    AutoCompleteTextView sale_price;
    private LinearLayout mProductContainer;
    private Spinner farmer;
    private Spinner harvsetSeason;
    private Spinner grade;
    private Button create_product;
    private ProgressDialog progressDialog = new ProgressDialog();


    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        View viewContainer = getActivity().getLayoutInflater().inflate(R.layout.product_order_detail, null);

        mProductContainer = viewContainer.findViewById(R.id.productContainer);

        farmer = viewContainer.findViewById(R.id.vendor);
        harvsetSeason = viewContainer.findViewById(R.id.harvsetSeason);
        grade = viewContainer.findViewById(R.id.grade);
        create_product = viewContainer.findViewById(R.id.create_product);
        create_product.setOnClickListener(this);

        product_name = viewContainer.findViewById(R.id.product_name);
        quantity = viewContainer.findViewById(R.id.quantity);

        sale_price = viewContainer.findViewById(R.id.sale_price);

        populateSpinnerHarvestSeason();
        populateSpinnerFarmer();
        ArrayAdapter<CharSequence> adapter_grade = ArrayAdapter.createFromResource(getContext(),
                R.array.grade_array, android.R.layout.simple_spinner_item);
        adapter_grade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grade.setAdapter(adapter_grade);

        builder.setView(viewContainer)
                //.setPositiveButton(R.string.msg_ok, this)
                .setNegativeButton(R.string.msg_cancel, this);

        return builder.create();
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
            farmer.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
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

                Cursor cursor = (Cursor) farmer.getSelectedItem();
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

                Intent intent = new Intent(getContext(), AddProduct.class);
                intent.putExtra("product_data", bundle);

                getContext().startService(intent);
                //Toast.makeText(getContext(),"Success",Toast.LENGTH_LONG).show();
                dismiss();

            }
            else {
                Toast.makeText(getContext(),"Data Error",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            return;
        }
    }

    private boolean isValidString(String word) {
        return word.length() >= 3;
    }

}

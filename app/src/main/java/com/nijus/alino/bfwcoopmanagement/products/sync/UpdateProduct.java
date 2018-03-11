package com.nijus.alino.bfwcoopmanagement.products.sync;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.products.pojo.PojoProduct;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.UUID;

/**
 * Created by Guillain-B on 19/02/2018.
 */

public class UpdateProduct extends IntentService {
    public final String LOG_TAG = UpdateProduct.class.getSimpleName();
    private int id_product;

    public UpdateProduct() {
        super("");
    }

    public UpdateProduct(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            id_product = intent.getIntExtra("product_id", 0);
            String productSelect = BfwContract.ProductTemplate.TABLE_NAME + "." +
                    BfwContract.ProductTemplate._ID + " =  ? ";
            Bundle productData = intent.getBundleExtra("product_data");

            int isSyncProduct = 0;
            Cursor productCursor = null;

            try {
                productCursor = getContentResolver().query(BfwContract.ProductTemplate.CONTENT_URI, null, productSelect,
                        new String[]{Long.toString(id_product)}, null);
                if (productCursor != null) {
                    while (productCursor.moveToNext()) {
                        isSyncProduct = productCursor.getInt(productCursor.getColumnIndex(BfwContract.ProductTemplate.COLUMN_IS_SYNC));
                    }
                }
            } finally {
                if (productCursor != null) productCursor.close();
            }

            PojoProduct pojoProduct = productData.getParcelable("product");

            String name = "";
            int price = 0;
            Double quantity = 0.0;
            int harvest_season = 0;
            String grade ="";
            int farmer = 0;


            if (pojoProduct != null) {
                name = pojoProduct.getName();
                price = pojoProduct.getPrice();
                quantity = pojoProduct.getQuantity();
                harvest_season = pojoProduct.getHarvest_season();
                grade = pojoProduct.getGrade();
                farmer = pojoProduct.getFarmer();

                ContentValues contentValues = new ContentValues();
                contentValues.put(BfwContract.ProductTemplate.COLUMN_PRODUCT_NAME, name);
                contentValues.put(BfwContract.ProductTemplate.COLUMN_PRICE, price);
                contentValues.put(BfwContract.ProductTemplate.COLUMN_VENDOR_QTY, quantity);
                contentValues.put(BfwContract.ProductTemplate.COLUMN_HARVEST_SEASON, harvest_season);
                contentValues.put(BfwContract.ProductTemplate.COLUMN_HARVEST_GRADE, grade);
                contentValues.put(BfwContract.ProductTemplate.COLUMN_FARMER_ID, farmer);

                contentValues.put(BfwContract.ProductTemplate.COLUMN_IS_SYNC, isSyncProduct);
                contentValues.put(BfwContract.ProductTemplate.COLUMN_IS_UPDATE, 0);

                getContentResolver().update(BfwContract.ProductTemplate.CONTENT_URI, contentValues,productSelect,new String[]{Integer.toString(id_product)});

                //Post event after saving data
                EventBus.getDefault().post(new SaveDataEvent("Product updated successfully",true));
                //sync if network available
                if (Utils.isNetworkAvailable(getApplicationContext())) {
                    //start job service
                    startService(new Intent(this, UpdateSyncProductBkgrnd.class));
                } else {
                    //schedule a job if not network is available
                    FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));

                    Job job = dispatcher.newJobBuilder()
                            .setService(UpdateSyncProduct.class)
                            .setTag(UUID.randomUUID().toString())
                            .setConstraints(Constraint.ON_ANY_NETWORK)
                            .build();

                    dispatcher.mustSchedule(job);
                }
            }

        }
    }
}

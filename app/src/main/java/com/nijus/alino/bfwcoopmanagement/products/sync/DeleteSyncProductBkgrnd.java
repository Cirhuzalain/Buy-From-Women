package com.nijus.alino.bfwcoopmanagement.products.sync;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.nijus.alino.bfwcoopmanagement.BuildConfig;
import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.ProcessingFarmerEvent;
import com.nijus.alino.bfwcoopmanagement.events.SyncDataEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DeleteSyncProductBkgrnd extends IntentService {

    public static final MediaType JSON
            = MediaType.parse("text/html; charset=utf-8");
    public final String LOG_TAG = DeleteSyncProductBkgrnd.class.getSimpleName();

    public DeleteSyncProductBkgrnd() {
        super("");
    }

    public DeleteSyncProductBkgrnd(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        SharedPreferences prefGoog = getApplicationContext().
                getSharedPreferences(getResources().getString(R.string.application_key), Context.MODE_PRIVATE);

        String appToken = prefGoog.getString(getResources().getString(R.string.app_key), "123");

        EventBus.getDefault().post(new ProcessingFarmerEvent(getResources().getString(R.string.farm_msg)));

        //get non sync product to the server (is_sync)
        int dataCount = 0;
        int productServerId;
        long id;
        Cursor cursor = null;
        int id_product = intent.getIntExtra("product_id", 0);

        String selectionProduct = BfwContract.ProductTemplate.TABLE_NAME + "." +
                BfwContract.ProductTemplate.COLUMN_SERVER_ID + " =  ? ";

        String selectionProduct_id = BfwContract.ProductTemplate.TABLE_NAME + "." +
                BfwContract.ProductTemplate._ID + " =  ? ";

        try {
            cursor = getContentResolver().query(BfwContract.ProductTemplate.CONTENT_URI, null,
                    selectionProduct, new String[]{Long.toString(id_product)}, null);
            if (cursor != null) {
                dataCount = cursor.getCount();

                while (cursor.moveToNext()) {
                    id = cursor.getLong(cursor.getColumnIndex(BfwContract.ProductTemplate._ID));
                    productServerId = cursor.getInt(cursor.getColumnIndex(BfwContract.ProductTemplate.COLUMN_SERVER_ID));


                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(240, TimeUnit.SECONDS)
                            .writeTimeout(240, TimeUnit.SECONDS)
                            .readTimeout(240, TimeUnit.SECONDS)
                            .build();

                    //Construct body
                    String bodyContent = "{}";

                    String API_INFO = BuildConfig.DEV_API_URL + "product.template" + "/" + productServerId;

                    RequestBody bodyProduct = RequestBody.create(JSON, bodyContent);

                    Request requesProduct = new Request.Builder()
                            .url(API_INFO)
                            .header("Content-Type", "text/html")
                            .header("Access-Token", appToken)
                            .method("DELETE", bodyProduct)
                            .build();
                    try {
                        Response responseProduct = client.newCall(requesProduct).execute();
                        ResponseBody responseBodyProduct = responseProduct.body();
                        if (responseBodyProduct != null) {
                            String farmerDataInfo = responseBodyProduct.string();
                            if (farmerDataInfo.equals("{}")) {
                                getContentResolver().delete(BfwContract.ProductTemplate.CONTENT_URI, selectionProduct_id, new String[]{Long.toString(id)});
                            }
                        }

                    } catch (IOException e) {
                        EventBus.getDefault().post(new SyncDataEvent(getResources().getString(R.string.delete_error_product), false));
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        //post event sync after
        if (dataCount > 0) {
            EventBus.getDefault().post(new SyncDataEvent(getString(com.nijus.alino.bfwcoopmanagement.R.string.prod_del_msg), true));
        }
    }
}

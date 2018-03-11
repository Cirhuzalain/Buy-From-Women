package com.nijus.alino.bfwcoopmanagement.loans.sync;


import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.Nullable;

import com.nijus.alino.bfwcoopmanagement.BuildConfig;
import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SyncDataEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class UpdateSyncLoanPaymentBkgrnd extends IntentService {

    public static final MediaType JSON
            = MediaType.parse("text/html; charset=utf-8");
    public final String LOG_TAG = UpdateSyncLoanPaymentBkgrnd.class.getSimpleName();

    public UpdateSyncLoanPaymentBkgrnd() {
        super("");
    }

    public UpdateSyncLoanPaymentBkgrnd(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        SharedPreferences prefGoog = getApplicationContext().
                getSharedPreferences(getResources().getString(R.string.application_key), Context.MODE_PRIVATE);

        String appToken = prefGoog.getString(getResources().getString(R.string.app_key), "123");

        //get non sync farmer to the server (is_sync)
        int dataCount = 0;
        int loanPaymentServerId;
        long id;
        Cursor cursor = null;

        String selection = BfwContract.LoanPayment.TABLE_NAME + "." +
                BfwContract.LoanPayment.COLUMN_IS_SYNC + " =  1 AND " +
                BfwContract.LoanPayment.TABLE_NAME + "." +
                BfwContract.LoanPayment.COLUMN_IS_UPDATE + " = 0";

        String selectionLoanPayment = BfwContract.LoanPayment.TABLE_NAME + "." +
                BfwContract.LoanPayment.COLUMN_IS_SYNC + " =  0 ";

        String selectionLoanPayment_id = BfwContract.LoanPayment.TABLE_NAME + "." +
                BfwContract.LoanPayment._ID + " =  ? ";

        String bankInfos = "\"bank_ids\": [],";
        try {
            cursor = getContentResolver().query(BfwContract.LoanPayment.CONTENT_URI, null, selection, null, null);
            if (cursor != null) {
                dataCount = cursor.getCount();

                while (cursor.moveToNext()) {
                    id = cursor.getLong(cursor.getColumnIndex(BfwContract.LoanPayment._ID));
                    loanPaymentServerId = cursor.getInt(cursor.getColumnIndex(BfwContract.LoanPayment.COLUMN_SERVER_ID));
                    long id_loan_loacal = cursor.getLong(cursor.getColumnIndex(BfwContract.LoanPayment.COLUMN_LOAN_ID));


                    //Date
                    Long start_date_long = cursor.getLong(cursor.getColumnIndex(BfwContract.LoanPayment.COLUMN_PAYMENT_DATE));
                    Date start_date_date = new Date(start_date_long);
                    String date_string = DateFormat.getDateInstance().format(start_date_date);

                    Double amount = cursor.getDouble(cursor.getColumnIndex(BfwContract.LoanPayment.COLUMN_AMOUNT));

                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(240, TimeUnit.SECONDS)
                            .writeTimeout(240, TimeUnit.SECONDS)
                            .readTimeout(240, TimeUnit.SECONDS)
                            .build();

                    //Construct bodyn /*"\"loan_id\": " + id_loan_loacal + ", " +*/
                    String bodyContent = "{}";

                    bodyContent = "{" +
                            "\"amount\": " + amount + "," +
                            "\"payment_date\": \"" + date_string + "\" " +
                            "}";

                    String API_INFO = BuildConfig.DEV_API_URL + "res.partner.loan.payment" + "/"+loanPaymentServerId;

                    RequestBody bodyLoan = RequestBody.create(JSON, bodyContent);

                    Request requesLoan = new Request.Builder()
                            .url(API_INFO)
                            .header("Content-Type", "text/html")
                            .header("Access-Token", appToken)
                            .method("PUT", bodyLoan)
                            .build();
                    try {
                        Response responseLoan = client.newCall(requesLoan).execute();
                        ResponseBody responseBodyLoan = responseLoan.body();
                        if (responseBodyLoan != null) {
                            String farmerDataInfo = responseBodyLoan.string();
                            if (farmerDataInfo.equals("{}")) {
                                //productServerId = productInfo.getInt("id");

                                //update localId
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(BfwContract.LoanPayment.COLUMN_SERVER_ID, loanPaymentServerId);
                                contentValues.put(BfwContract.LoanPayment.COLUMN_IS_SYNC, 1);
                                contentValues.put(BfwContract.LoanPayment.COLUMN_IS_UPDATE, 1);

                                getContentResolver().update(BfwContract.LoanPayment.CONTENT_URI, contentValues, selectionLoanPayment_id, new String[]{Long.toString(id)});
                            }
                        }

                    } catch (IOException e) {
                        EventBus.getDefault().post(new SyncDataEvent(getResources().getString(R.string.syncing_error_payment), false));
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        //post event sync after
        if (dataCount > 0)
            EventBus.getDefault().post(new SyncDataEvent("Payment synchronized successfully", true));
    }
}

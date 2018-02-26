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
import org.json.JSONException;
import org.json.JSONObject;

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

public class SyncLoanPaymentBackground extends IntentService {

    public static final MediaType JSON
            = MediaType.parse("text/html; charset=utf-8");
    public final String LOG_TAG = SyncLoanPaymentBackground.class.getSimpleName();

    public SyncLoanPaymentBackground() {
        super("");
    }

    public SyncLoanPaymentBackground(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        SharedPreferences prefGoog = getApplicationContext().
                getSharedPreferences(getResources().getString(R.string.application_key), Context.MODE_PRIVATE);

        String appToken = prefGoog.getString(getResources().getString(R.string.app_key), "123");

        //get non sync farmer to the server (is_sync)
        int dataCount = 0;
        int LoanServerId;
        long id;
        Cursor cursor = null;

        //int id_loan_server = 0;

        String selectionLoan = BfwContract.LoanPayment.TABLE_NAME + "." +
                BfwContract.LoanPayment.COLUMN_IS_SYNC + " =  0 ";

        String selectionLoan_id = BfwContract.LoanPayment.TABLE_NAME + "." +
                BfwContract.Loan._ID + " =  ? ";

        String bankInfos = "\"bank_ids\": [],";
        try {
            cursor = getContentResolver().query(BfwContract.LoanPayment.CONTENT_URI, null, selectionLoan, null, null);
            if (cursor != null) {
                dataCount = cursor.getCount();

                while (cursor.moveToNext()) {
                    id = cursor.getLong(cursor.getColumnIndex(BfwContract.LoanPayment._ID));
                    long id_loan_loacal = cursor.getLong(cursor.getColumnIndex(BfwContract.LoanPayment.COLUMN_LOAN_ID));


                    /**
                     *
                     * Check if LOAN is sync. If is sync, now begin saving its loanPayment.
                     * Else, Before to Sync LoanPayment we need to sync Loan because an exception of lacking "LOAN_SERVER_ID" can be thrown
                     *
                     * **/
                    /**
                     * Get LOAN_SEVEVER_ID
                     * **/
                    String selectLoanId = BfwContract.Loan.TABLE_NAME + "." +
                            BfwContract.Loan._ID + " =  ? ";

                    Cursor cursorLoan = getContentResolver().query(BfwContract.Loan.CONTENT_URI, null,
                            selectLoanId,
                            new String[]{Long.toString(id_loan_loacal)},
                            null);
                    if (cursorLoan != null) {
                        while (cursorLoan.moveToNext()) {
                            if (cursorLoan.getInt(cursorLoan.getColumnIndex(BfwContract.Loan.COLUMN_IS_SYNC)) == 0)
                            {
                                EventBus.getDefault().post(new SyncDataEvent(getResources().getString(R.string.syncing_error), false));
                            }
                            else
                            id_loan_loacal = cursorLoan.getLong(cursorLoan.getColumnIndex(BfwContract.Loan.COLUMN_SERVER_ID));
                        }
                    }


                    Long start_date_long = cursor.getLong(cursor.getColumnIndex(BfwContract.LoanPayment.COLUMN_PAYMENT_DATE));
                    Date start_date_date = new Date(start_date_long);
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    String date_string = DateFormat.getDateInstance().format(start_date_date);
                    Double amount = cursor.getDouble(cursor.getColumnIndex(BfwContract.LoanPayment.COLUMN_AMOUNT));

                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(240, TimeUnit.SECONDS)
                            .writeTimeout(240, TimeUnit.SECONDS)
                            .readTimeout(240, TimeUnit.SECONDS)
                            .build();

                    //Construct body
                    String bodyContent = "{}";

                        bodyContent = "{" +
                                "\"amount\": " + amount + "," +
                                "\"loan_id\": " + id_loan_loacal + ", " +
                                "\"payment_date\": \"" + date_string + "\" " +
                                "}";

                    String API_INFO = BuildConfig.DEV_API_URL + "res.partner.loan.payment";

                    RequestBody bodyProduct = RequestBody.create(JSON, bodyContent);

                    Request requesProduct = new Request.Builder()
                            .url(API_INFO)
                            .header("Content-Type", "text/html")
                            .header("Access-Token", appToken)
                            .method("POST", bodyProduct)
                            .build();
                    try {
                        Response responseLoan = client.newCall(requesProduct).execute();
                        ResponseBody responseBodyLoan = responseLoan.body();
                        if (responseBodyLoan != null) {
                            String LoanDataInfo = responseBodyLoan.string();
                            JSONObject LoanInfo = new JSONObject(LoanDataInfo);
                            if (LoanInfo.has("id")) {
                                LoanServerId = LoanInfo.getInt("id");

                                //update localId
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(BfwContract.LoanPayment.COLUMN_SERVER_ID, LoanServerId);
                                contentValues.put(BfwContract.LoanPayment.COLUMN_IS_SYNC, 1);
                                contentValues.put(BfwContract.LoanPayment.COLUMN_IS_UPDATE, 1);
                                getContentResolver().update(BfwContract.LoanPayment.CONTENT_URI, contentValues, selectionLoan_id, new String[]{Long.toString(id)});
                            }
                        }

                    } catch (IOException | JSONException exp) {
                        EventBus.getDefault().post(new SyncDataEvent(getResources().getString(R.string.syncing_error), false));
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
            EventBus.getDefault().post(new SyncDataEvent("", true));

    }
}

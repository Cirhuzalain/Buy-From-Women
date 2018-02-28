package com.nijus.alino.bfwcoopmanagement.loans.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.widget.Toast;

import com.nijus.alino.bfwcoopmanagement.BuildConfig;
import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SyncDataEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DeleteSyncLoanBkgrnd extends IntentService {

    public static final MediaType JSON
            = MediaType.parse("text/html; charset=utf-8");
    public final String LOG_TAG = DeleteSyncLoanBkgrnd.class.getSimpleName();

    public DeleteSyncLoanBkgrnd() {
        super("");
    }

    public DeleteSyncLoanBkgrnd(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        SharedPreferences prefGoog = getApplicationContext().
                getSharedPreferences(getResources().getString(R.string.application_key), Context.MODE_PRIVATE);

        String appToken = prefGoog.getString(getResources().getString(R.string.app_key), "123");

        //get non sync farmer to the server (is_sync)
        int dataCount = 0;
        int loanServerId;
        long id;
        Cursor cursor = null;
        int id_loan = intent.getIntExtra("loan_id", 0);
        ArrayList<Integer> selected_item_list = intent.getIntegerArrayListExtra("list_items_to_delete");
        String c = "";
        for (int id_loan_from_list : selected_item_list) {


            String selectionLoan = BfwContract.Loan.TABLE_NAME + "." +
                    BfwContract.Loan._ID + " =  ? ";

            String selectionLoan_id = BfwContract.Loan.TABLE_NAME + "." +
                    BfwContract.Loan._ID + " =  ? ";

            String bankInfos = "\"bank_ids\": [],";
            try {
                cursor = getContentResolver().query(BfwContract.Loan.CONTENT_URI, null, selectionLoan,
                        new String[]{Long.toString(id_loan_from_list)}, null);
                if (cursor != null) {
                    dataCount = cursor.getCount();

                    while (cursor.moveToNext()) {
                        id = cursor.getLong(cursor.getColumnIndex(BfwContract.Loan._ID));
                        loanServerId = cursor.getInt(cursor.getColumnIndex(BfwContract.Loan.COLUMN_SERVER_ID));

                        // select all loan line and delete them one by one
                        String selectionLoanLine = BfwContract.LoanLine.TABLE_NAME + "." +
                                BfwContract.LoanLine.COLUMN_LOAN_ID + " =  ? ";

                        String selectionLoanLine_local = BfwContract.LoanLine.TABLE_NAME + "." +
                                BfwContract.LoanLine._ID + " =  ? ";


                        Cursor cursor_delete_line = getContentResolver().query(BfwContract.LoanLine.CONTENT_URI,
                                null, selectionLoanLine, new String[]{Long.toString(id)},
                                null);
                        if (cursor_delete_line != null) {
                            dataCount = cursor_delete_line.getCount();

                            while (cursor_delete_line.moveToNext()) {
                                int loan_line_id_server = cursor_delete_line.getInt(cursor_delete_line.getColumnIndex(BfwContract.LoanLine.COLUMN_SERVER_ID));
                                int loan_line_id_local = cursor_delete_line.getInt(cursor_delete_line.getColumnIndex(BfwContract.LoanLine._ID));
                                OkHttpClient client = new OkHttpClient.Builder()
                                        .connectTimeout(240, TimeUnit.SECONDS)
                                        .writeTimeout(240, TimeUnit.SECONDS)
                                        .readTimeout(240, TimeUnit.SECONDS)
                                        .build();

                                String bodyContent = "{}";

                                String API_INFO = BuildConfig.DEV_API_URL + "res.partner.loan.line" + "/" + loan_line_id_server;

                                RequestBody bodyLoan = RequestBody.create(JSON, bodyContent);

                                Request requesLoan = new Request.Builder()
                                        .url(API_INFO)
                                        .header("Content-Type", "text/html")
                                        .header("Access-Token", appToken)
                                        .method("DELETE", bodyLoan)
                                        .build();
                                try {
                                    Response responseLoan = client.newCall(requesLoan).execute();
                                    ResponseBody responseBodyLoan = responseLoan.body();
                                    if (responseBodyLoan != null) {
                                        String farmerDataInfo = responseBodyLoan.string();
                                        if (farmerDataInfo.equals("{}")) {
                                            getContentResolver().delete(BfwContract.LoanLine.CONTENT_URI, selectionLoanLine_local, new String[]{Long.toString(loan_line_id_local)});
                                        }
                                    }

                                } catch (IOException e) {
                                    EventBus.getDefault().post(new SyncDataEvent(getResources().getString(R.string.syncing_error), false));
                                }

                            }
                        }

                        // select all loan payment and delete them one by one
                        String selectionLoanPayment = BfwContract.LoanPayment.TABLE_NAME + "." +
                                BfwContract.LoanPayment.COLUMN_LOAN_ID + " =  ? ";

                        String selectionLoanPayment_local = BfwContract.LoanPayment.TABLE_NAME + "." +
                                BfwContract.LoanPayment._ID + " =  ? ";


                        Cursor cursor_delete_payment = getContentResolver().query(BfwContract.LoanPayment.CONTENT_URI,
                                null, selectionLoanPayment, new String[]{Long.toString(id)},
                                null);
                        if (cursor_delete_payment != null) {
                            dataCount = cursor_delete_payment.getCount();

                            while (cursor_delete_payment.moveToNext()) {
                                int loan_payment_id_server = cursor_delete_payment.getInt(cursor_delete_payment.getColumnIndex(BfwContract.LoanPayment.COLUMN_SERVER_ID));
                                int loan_payment_id_local = cursor_delete_payment.getInt(cursor_delete_payment.getColumnIndex(BfwContract.LoanPayment._ID));
                                OkHttpClient client = new OkHttpClient.Builder()
                                        .connectTimeout(240, TimeUnit.SECONDS)
                                        .writeTimeout(240, TimeUnit.SECONDS)
                                        .readTimeout(240, TimeUnit.SECONDS)
                                        .build();

                                String bodyContent = "{}";

                                String API_INFO = BuildConfig.DEV_API_URL + "res.partner.loan.payment" + "/" + loan_payment_id_server;

                                RequestBody bodyLoan = RequestBody.create(JSON, bodyContent);

                                Request requesLoan = new Request.Builder()
                                        .url(API_INFO)
                                        .header("Content-Type", "text/html")
                                        .header("Access-Token", appToken)
                                        .method("DELETE", bodyLoan)
                                        .build();
                                try {
                                    Response responseLoan = client.newCall(requesLoan).execute();
                                    ResponseBody responseBodyLoan = responseLoan.body();
                                    if (responseBodyLoan != null) {
                                        String farmerDataInfo = responseBodyLoan.string();
                                        if (farmerDataInfo.equals("{}")) {
                                            getContentResolver().delete(BfwContract.LoanPayment.CONTENT_URI, selectionLoanPayment_local, new String[]{Long.toString(loan_payment_id_local)});
                                        }
                                    }

                                } catch (IOException e) {
                                    EventBus.getDefault().post(new SyncDataEvent(getResources().getString(R.string.syncing_error), false));
                                }
                            }
                        }


                        OkHttpClient client = new OkHttpClient.Builder()
                                .connectTimeout(240, TimeUnit.SECONDS)
                                .writeTimeout(240, TimeUnit.SECONDS)
                                .readTimeout(240, TimeUnit.SECONDS)
                                .build();

                        //Construct body
                        String bodyContent = "{}";

                        String API_INFO = BuildConfig.DEV_API_URL + "res.partner.loan" + "/" + loanServerId;

                        RequestBody bodyLoan = RequestBody.create(JSON, bodyContent);

                        Request requesLoan = new Request.Builder()
                                .url(API_INFO)
                                .header("Content-Type", "text/html")
                                .header("Access-Token", appToken)
                                .method("DELETE", bodyLoan)
                                .build();
                        try {
                            Response responseLoan = client.newCall(requesLoan).execute();
                            ResponseBody responseBodyLoan = responseLoan.body();
                            if (responseBodyLoan != null) {
                                String farmerDataInfo = responseBodyLoan.string();
                                if (farmerDataInfo.equals("{}")) {
                                    getContentResolver().delete(BfwContract.Loan.CONTENT_URI, selectionLoan_id, new String[]{Long.toString(id)});
                                }
                            }
                        } catch (IOException e) {
                            EventBus.getDefault().post(new SyncDataEvent(getResources().getString(R.string.syncing_error), false));
                        }
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        //post event sync after
        if (dataCount > 0)
            EventBus.getDefault().post(new SyncDataEvent("", true));
    }
}

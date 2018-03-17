package com.nijus.alino.bfwcoopmanagement.loans.sync;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.loans.pojo.PojoLoanPayment;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.UUID;

public class UpdateLoanPayment extends IntentService {
    public final String LOG_TAG = UpdateLoanPayment.class.getSimpleName();
    private long id_loanPayment;

    public UpdateLoanPayment() {
        super("");
    }

    public UpdateLoanPayment(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            id_loanPayment = intent.getLongExtra("loan_payment_id", 0);
            String loanPaymentSelect = BfwContract.LoanPayment.TABLE_NAME + "." +
                    BfwContract.LoanPayment._ID + " =  ? ";
            Bundle loanPaymentData = intent.getBundleExtra("loan_payment_data");

            int isSyncPayment = 0;
            Cursor loanPaymentCursor = null;

            try {
                loanPaymentCursor = getContentResolver().query(BfwContract.LoanPayment.CONTENT_URI, null, loanPaymentSelect,
                        new String[]{Long.toString(id_loanPayment)}, null);
                if (loanPaymentCursor != null) {
                    while (loanPaymentCursor.moveToNext()) {
                        isSyncPayment = loanPaymentCursor.getInt(loanPaymentCursor.getColumnIndex(BfwContract.LoanPayment.COLUMN_IS_SYNC));
                    }
                }
            } finally {
                if (loanPaymentCursor != null) loanPaymentCursor.close();
            }

            Bundle paymentData = intent.getBundleExtra("loan_payment_data");
            PojoLoanPayment pojoLoanPayment = paymentData.getParcelable("loan_payment");

            int id_loan = 0;
            Long paymentdate = null;
            Double amount = 0.0;

            if (pojoLoanPayment != null) {
                id_loan = pojoLoanPayment.getLoan_id();
                paymentdate = pojoLoanPayment.getPayment_date();
                amount = pojoLoanPayment.getAmount();

                ContentValues contentValues = new ContentValues();

                contentValues.put(BfwContract.LoanPayment.COLUMN_LOAN_ID, id_loan);
                contentValues.put(BfwContract.LoanPayment.COLUMN_PAYMENT_DATE, paymentdate);
                contentValues.put(BfwContract.LoanPayment.COLUMN_AMOUNT, amount);

                contentValues.put(BfwContract.LoanPayment.COLUMN_IS_SYNC, isSyncPayment);
                contentValues.put(BfwContract.LoanPayment.COLUMN_IS_UPDATE, 0);

                getContentResolver().update(BfwContract.LoanPayment.CONTENT_URI, contentValues, loanPaymentSelect, new String[]{Long.toString(id_loanPayment)});

                //Post event after saving data
                EventBus.getDefault().post(new SaveDataEvent(getResources().getString(R.string.update_loan_payment), true));
                //sync if network available
                if (Utils.isNetworkAvailable(getApplicationContext())) {
                    //start job service
                    startService(new Intent(this, UpdateSyncLoanPaymentBkgrnd.class));
                } else {
                    //schedule a job if not network is available
                    FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));

                    Job job = dispatcher.newJobBuilder()
                            .setService(UpdateSyncLoanPayment.class)
                            .setTag(UUID.randomUUID().toString())
                            .setConstraints(Constraint.ON_ANY_NETWORK)
                            .build();
                    dispatcher.mustSchedule(job);
                }
            }

        }
    }
}

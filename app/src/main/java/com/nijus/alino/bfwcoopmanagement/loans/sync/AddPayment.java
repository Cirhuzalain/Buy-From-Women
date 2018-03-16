package com.nijus.alino.bfwcoopmanagement.loans.sync;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.loans.pojo.PojoLoanPayment;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.UUID;


public class AddPayment extends IntentService {
    public final String LOG_TAG = AddPayment.class.getSimpleName();

    public AddPayment() {
        super("");
    }

    public AddPayment(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
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

                contentValues.put(BfwContract.LoanPayment.COLUMN_IS_SYNC, 0);
                contentValues.put(BfwContract.LoanPayment.COLUMN_IS_UPDATE, 0);

                Uri uri = getContentResolver().insert(BfwContract.LoanPayment.CONTENT_URI, contentValues);

                //Post event after saving data
                EventBus.getDefault().post(new SaveDataEvent("Payment added successfully", true));
                //sync if network available
                if (Utils.isNetworkAvailable(getApplicationContext())) {
                    //start job service
                    Intent intent1 = new Intent(this, SyncLoanPaymentBackground.class);
                    startService(intent1);
                } else {
                    //schedule a job if not network is available
                    FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));

                    Job job = dispatcher.newJobBuilder()
                            .setService(SyncLoanPayment.class)
                            .setTag(UUID.randomUUID().toString())
                            .setConstraints(Constraint.ON_ANY_NETWORK)
                            .build();
                    dispatcher.mustSchedule(job);
                }
            }

        }
    }
}

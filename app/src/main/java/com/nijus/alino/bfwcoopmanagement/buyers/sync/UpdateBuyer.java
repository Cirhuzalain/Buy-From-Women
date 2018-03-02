package com.nijus.alino.bfwcoopmanagement.buyers.sync;

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
import com.nijus.alino.bfwcoopmanagement.buyers.pojo.PojoBuyer;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.loans.pojo.PojoLoan;
import com.nijus.alino.bfwcoopmanagement.loans.sync.UpdateSyncLoan;
import com.nijus.alino.bfwcoopmanagement.loans.sync.UpdateSyncLoanBkgrnd;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.UUID;

/**
 * Created by Guillain-B on 19/02/2018.
 */

public class UpdateBuyer extends IntentService {
    public final String LOG_TAG = UpdateBuyer.class.getSimpleName();
    private int id_buyer;

    public UpdateBuyer() {
        super("");
    }

    public UpdateBuyer(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            id_buyer = intent.getIntExtra("buyer_id", 0);
            String buyerSelect = BfwContract.Buyer.TABLE_NAME + "." +
                    BfwContract.Loan._ID + " =  ? ";
            Bundle buyerData = intent.getBundleExtra("buyer_data");

            int isSyncProduct = 0;
            Cursor loanCursor = null;

            try {
                loanCursor = getContentResolver().query(BfwContract.Buyer.CONTENT_URI, null, buyerSelect,
                        new String[]{Long.toString(id_buyer)}, null);
                if (loanCursor != null) {
                    while (loanCursor.moveToNext()) {
                        isSyncProduct = loanCursor.getInt(loanCursor.getColumnIndex(BfwContract.Buyer.COLUMN_IS_SYNC));
                    }
                }
            } finally {
                if (loanCursor != null) loanCursor.close();
            }

            PojoBuyer pojoBuyer = buyerData.getParcelable("buyer");

            String name = "";
            String email = "";
            String phone = "";

            if (pojoBuyer != null) {
                name = pojoBuyer.getName();
                email = pojoBuyer.getMail();
                phone = pojoBuyer.getPhone();

                ContentValues contentValues = new ContentValues();
                contentValues.put(BfwContract.Buyer.COLUMN_BUYER_NAME, name);
                contentValues.put(BfwContract.Buyer.COLUMN_BUYER_EMAIL, email);
                contentValues.put(BfwContract.Buyer.COLUMN_BUYER_PHONE, phone);

                contentValues.put(BfwContract.Buyer.COLUMN_IS_SYNC, isSyncProduct);
                contentValues.put(BfwContract.Buyer.COLUMN_IS_UPDATE, 0);

                getContentResolver().update(BfwContract.Buyer.CONTENT_URI, contentValues,buyerSelect,new String[]{Integer.toString(id_buyer)});


                //sync if network available
                if (Utils.isNetworkAvailable(getApplicationContext())) {
                    //start job service
                    startService(new Intent(this, UpdateSyncBuyerBkgrnd.class));
                    EventBus.getDefault().post(new SaveDataEvent("Buyer updated successfully", true));
                } else {
                    //schedule a job if not network is available
                    FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));

                    Job job = dispatcher.newJobBuilder()
                            .setService(UpdateSyncBuyer.class)
                            .setTag(UUID.randomUUID().toString())
                            .setConstraints(Constraint.ON_ANY_NETWORK)
                            .build();

                    dispatcher.mustSchedule(job);
                    EventBus.getDefault().post(new SaveDataEvent("Buyer updated successfully and will be synchronized later", true));

                }
            }

        }
    }
}

package com.nijus.alino.bfwcoopmanagement.buyers.sync;

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
import com.nijus.alino.bfwcoopmanagement.R;
import com.nijus.alino.bfwcoopmanagement.buyers.pojo.PojoBuyer;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.events.SyncDataEvent;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.UUID;

/**
 * Created by Guillain-B on 19/02/2018.
 */

public class AddBuyer extends IntentService {
    public final String LOG_TAG = AddBuyer.class.getSimpleName();

    public AddBuyer() {
        super("");
    }

    public AddBuyer(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            Bundle buyerData = intent.getBundleExtra("Buyer_data");

            PojoBuyer pojoBuyer= buyerData.getParcelable("Buyer");

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

                contentValues.put(BfwContract.Buyer.COLUMN_IS_SYNC, 0);
                contentValues.put(BfwContract.Buyer.COLUMN_IS_UPDATE, 0);

                Uri uri = getContentResolver().insert(BfwContract.Buyer.CONTENT_URI, contentValues);

                //sync if network available
                if (Utils.isNetworkAvailable(getApplicationContext())) {
                    EventBus.getDefault().post(new SaveDataEvent("Buyer added successfully", true));
                    //start job service
                    startService(new Intent(this, SyncBuyerBackground.class));
                }else {
                    //schedule a job if not network is available
                    FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));

                    Job job = dispatcher.newJobBuilder()
                            .setService(SyncBuyer.class)
                            .setTag(UUID.randomUUID().toString())
                            .setConstraints(Constraint.ON_ANY_NETWORK)
                            .build();
                    dispatcher.mustSchedule(job);
                    EventBus.getDefault().post(new SaveDataEvent("Buyer added successfully and will be synchronized later", true));

                }
            }
        }
    }
}

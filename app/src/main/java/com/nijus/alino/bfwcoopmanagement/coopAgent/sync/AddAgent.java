package com.nijus.alino.bfwcoopmanagement.coopAgent.sync;

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
import com.nijus.alino.bfwcoopmanagement.coopAgent.pojo.PojoAgent;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.UUID;

public class AddAgent extends IntentService {
    public final String LOG_TAG = AddAgent.class.getSimpleName();

    public AddAgent() {
        super("");
    }

    public AddAgent(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            Bundle agentData = intent.getBundleExtra("agent_data");

            PojoAgent pojoAgent = agentData.getParcelable("agent");

            String name;
            String phone;
            String mail;
            int coop;


            if (pojoAgent != null) {
                name = pojoAgent.getName();
                phone = pojoAgent.getPhone();
                mail = pojoAgent.getMail();
                coop = pojoAgent.getCoop();

                ContentValues contentValues = new ContentValues();
                contentValues.put(BfwContract.CoopAgent.COLUMN_AGENT_NAME, name);
                contentValues.put(BfwContract.CoopAgent.COLUMN_AGENT_PHONE, phone);
                contentValues.put(BfwContract.CoopAgent.COLUMN_AGENT_EMAIL, mail);
                contentValues.put(BfwContract.CoopAgent.COLUMN_COOP_ID, coop);


                contentValues.put(BfwContract.CoopAgent.COLUMN_IS_SYNC, 0);
                contentValues.put(BfwContract.CoopAgent.COLUMN_IS_UPDATE, 0);

                getContentResolver().insert(BfwContract.CoopAgent.CONTENT_URI, contentValues);


                //sync if network available
                if (Utils.isNetworkAvailable(getApplicationContext())) {
                    //Post event after saving data
                    EventBus.getDefault().post(new SaveDataEvent(getResources().getString(R.string.add_agent_msg), true));
                    //start job service
                    startService(new Intent(this, SyncAgentBackground.class));
                } else {
                    //schedule a job if not network is available
                    FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));
                    Job job = dispatcher.newJobBuilder()
                            .setService(SyncAgent.class)
                            .setTag(UUID.randomUUID().toString())
                            .setConstraints(Constraint.ON_ANY_NETWORK)
                            .build();
                    dispatcher.mustSchedule(job);
                    //Post event after saving data
                    EventBus.getDefault().post(new SaveDataEvent(getResources().getString(R.string.add_agent_msg_later), true));
                }
            }

        }
    }
}

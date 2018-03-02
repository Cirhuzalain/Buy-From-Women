package com.nijus.alino.bfwcoopmanagement.coopAgent.sync;

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
import com.nijus.alino.bfwcoopmanagement.coopAgent.pojo.PojoAgent;
import com.nijus.alino.bfwcoopmanagement.data.BfwContract;
import com.nijus.alino.bfwcoopmanagement.events.SaveDataEvent;
import com.nijus.alino.bfwcoopmanagement.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.UUID;

/**
 * Created by Guillain-B on 19/02/2018.
 */

public class UpdateAgent extends IntentService {
    public final String LOG_TAG = UpdateAgent.class.getSimpleName();
    private int id_agent;

    public UpdateAgent() {
        super("");
    }

    public UpdateAgent(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            id_agent = intent.getIntExtra("agent_id", 0);
            String agentSelect = BfwContract.CoopAgent.TABLE_NAME + "." +
                    BfwContract.CoopAgent._ID + " =  ? ";

            int isSyncProduct = 0;
            Cursor loanCursor = null;

            try {
                loanCursor = getContentResolver().query(BfwContract.CoopAgent.CONTENT_URI, null, agentSelect,
                        new String[]{Long.toString(id_agent)}, null);
                if (loanCursor != null) {
                    while (loanCursor.moveToNext()) {
                        isSyncProduct = loanCursor.getInt(loanCursor.getColumnIndex(BfwContract.CoopAgent.COLUMN_IS_SYNC));
                    }
                }
            } finally {
                if (loanCursor != null) loanCursor.close();
            }
            Bundle agentData = intent.getBundleExtra("agent_data");

            PojoAgent pojoAgent = agentData.getParcelable("agent");

            String name = "";
            String phone = "";
            String mail = "";
            int coop = 0;


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


                contentValues.put(BfwContract.CoopAgent.COLUMN_IS_SYNC, isSyncProduct);
                contentValues.put(BfwContract.CoopAgent.COLUMN_IS_UPDATE, 0);

                //Uri uri = getContentResolver().insert(BfwContract.CoopAgent.CONTENT_URI, contentValues);
                getContentResolver().update(BfwContract.CoopAgent.CONTENT_URI, contentValues,agentSelect,new String[]{Integer.toString(id_agent)});

                //sync if network available
                if (Utils.isNetworkAvailable(getApplicationContext())) {
                    //start job service
                   startService(new Intent(this, UpdateSyncAgentBkgrnd.class));
                    //Post event after saving data
                    EventBus.getDefault().post(new SaveDataEvent("Agent added successfully",true));
                } else {
                    //schedule a job if not network is available
                    FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));
                    Job job = dispatcher.newJobBuilder()
                            .setService(UpdateSyncAgent.class)
                            .setTag(UUID.randomUUID().toString())
                            .setConstraints(Constraint.ON_ANY_NETWORK)
                            .build();
                    dispatcher.mustSchedule(job);
                    //Post event after saving data
                    EventBus.getDefault().post(new SaveDataEvent("Agent added successfully and will be synchronized later",true));
                }
            }

        }
    }
}

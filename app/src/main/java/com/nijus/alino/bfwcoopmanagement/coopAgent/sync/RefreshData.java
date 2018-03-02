package com.nijus.alino.bfwcoopmanagement.coopAgent.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.nijus.alino.bfwcoopmanagement.buyers.sync.SyncBuyerBackground;
import com.nijus.alino.bfwcoopmanagement.buyers.sync.UpdateSyncBuyerBkgrnd;

public class RefreshData extends IntentService {

    public RefreshData() {
        super("");
    }

    public RefreshData(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        //Check non sync data and sync
        startService(new Intent(this, SyncAgentBackground.class));

        //Check non update data and update
        //startService(new Intent(this, UpdateSyncAgentBkgrnd.class));
    }
}

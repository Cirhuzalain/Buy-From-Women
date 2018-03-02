package com.nijus.alino.bfwcoopmanagement.buyers.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.nijus.alino.bfwcoopmanagement.farmers.sync.SyncBackground;
import com.nijus.alino.bfwcoopmanagement.farmers.sync.UpdateSyncFarmer;

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
        startService(new Intent(this, SyncBuyerBackground.class));

        //Check non update data and update
        startService(new Intent(this, UpdateSyncBuyerBkgrnd.class));
    }
}

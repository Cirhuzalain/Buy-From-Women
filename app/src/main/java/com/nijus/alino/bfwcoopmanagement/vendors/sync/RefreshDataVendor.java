package com.nijus.alino.bfwcoopmanagement.vendors.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class RefreshDataVendor extends IntentService {

    public RefreshDataVendor() {
        super("");
    }

    public RefreshDataVendor(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        //Check non sync data and sync
        startService(new Intent(this, SyncBackgroundVendor.class));

        //Check non update data and update
        startService(new Intent(this, UpdateSyncVendor.class));
    }
}

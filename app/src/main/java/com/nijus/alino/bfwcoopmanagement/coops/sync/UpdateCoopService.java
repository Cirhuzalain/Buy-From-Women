package com.nijus.alino.bfwcoopmanagement.coops.sync;

import android.app.IntentService;
import android.content.Intent;

import android.support.annotation.Nullable;


public class UpdateCoopService extends IntentService {
    private long mFarmerId;

    public UpdateCoopService() {
        super("");
    }

    public UpdateCoopService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}

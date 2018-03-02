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

        // grab data

        // prepare and set variable

        // handle coops table

        // handle baseline finance info table

        // handle baseline sales table

        // handle baseline yield table

        // handle forecast sales table

        // handle access  to information table

    }
}

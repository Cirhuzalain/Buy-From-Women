package com.nijus.alino.bfwcoopmanagement.buyers.sync;

import android.content.Intent;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.nijus.alino.bfwcoopmanagement.products.sync.SyncProductBackground;

public class SyncBuyer extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        startService(new Intent(this, SyncBuyerBackground.class));
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}

package com.nijus.alino.bfwcoopmanagement.products.sync;

import android.content.Intent;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class SyncProduct extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        startService(new Intent(this, SyncProductBackground.class));
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}

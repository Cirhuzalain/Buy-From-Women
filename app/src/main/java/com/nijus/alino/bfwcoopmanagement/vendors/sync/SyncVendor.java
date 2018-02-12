package com.nijus.alino.bfwcoopmanagement.vendors.sync;

import android.content.Intent;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class SyncVendor extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        startService(new Intent(this, SyncBackgroundVendor.class));
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}

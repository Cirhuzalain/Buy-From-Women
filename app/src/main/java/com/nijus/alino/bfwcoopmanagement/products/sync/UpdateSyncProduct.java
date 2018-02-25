package com.nijus.alino.bfwcoopmanagement.products.sync;

import android.content.Intent;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class UpdateSyncProduct extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        startService(new Intent(this, UpdateSyncProductBkgrnd.class));
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}

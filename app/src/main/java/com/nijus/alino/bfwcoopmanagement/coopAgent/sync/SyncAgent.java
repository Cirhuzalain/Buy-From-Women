package com.nijus.alino.bfwcoopmanagement.coopAgent.sync;

import android.content.Intent;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.nijus.alino.bfwcoopmanagement.buyers.sync.SyncBuyerBackground;

public class SyncAgent extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        startService(new Intent(this, SyncAgentBackground.class));
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}

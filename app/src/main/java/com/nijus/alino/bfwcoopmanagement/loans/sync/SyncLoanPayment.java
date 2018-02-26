package com.nijus.alino.bfwcoopmanagement.loans.sync;

import android.content.Intent;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class SyncLoanPayment extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        startService(new Intent(this, SyncLoanPaymentBackground.class));
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}

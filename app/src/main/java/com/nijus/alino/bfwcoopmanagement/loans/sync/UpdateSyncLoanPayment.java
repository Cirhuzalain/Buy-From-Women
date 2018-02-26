package com.nijus.alino.bfwcoopmanagement.loans.sync;

import android.content.Intent;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class UpdateSyncLoanPayment extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        startService(new Intent(this, UpdateSyncLoanPaymentBkgrnd.class));
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}

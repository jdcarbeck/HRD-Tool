package com.example.hrdtool;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class DataSendingService extends JobService {
    private static final String TAG = "DataSendingService";
    private boolean jobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters parameters) {
        Log.d(TAG, "Job started");
        doBackgroundWork(parameters);

        return true;
    }

    private void doBackgroundWork(final JobParameters parameters) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String route = parameters.getExtras().getString("route");
                String json = parameters.getExtras().getString("encryptedFormData");
                String encryptedSecretKey = parameters.getExtras().getString("encryptedSecretKey");

                OkHttp.sendPostReq(route, json, encryptedSecretKey);

                if (jobCancelled) {
                    return;
                }

                Log.d(TAG, "data Sent");
                jobFinished(parameters, false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters parameters) {
        Log.d(TAG, "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }
}

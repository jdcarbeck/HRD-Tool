package com.example.hrdtool;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.widget.TextView;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;

import static com.example.hrdtool.MainActivity.unsentFormCount;

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

                String id = parameters.getExtras().getString("id");
                String json = parameters.getExtras().getString("form");
//                String encryptedSecretKey = parameters.getExtras().getString("encryptedSecretKey");


                OkHttp.sendPostReq("key", "", json, id);

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

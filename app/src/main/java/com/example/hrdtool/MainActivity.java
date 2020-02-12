package com.example.hrdtool;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText sampleTextField;
    Button submitButton;


    private static final String TAG = "MainActivity";
    PersistableBundle bundle = new PersistableBundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sampleTextField = findViewById(R.id.editText3);
        submitButton = findViewById(R.id.submitButton);

        
        JSONObject formJson = new JSONObject();           //example json creation for testing
        try {
            formJson.put("username", "Ben");
            formJson.put("password", "12345");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String jsonString = formJson.toString();
        bundle.putString("json", jsonString);
    }

    public void scheduleJob(View v) {
        ComponentName componentName = new ComponentName(this, DataSendingService.class);

        JobInfo info = new JobInfo.Builder(9, componentName)
                .setRequiresCharging(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setExtras(bundle)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");

        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }

}

package com.example.hrdtool;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText sampleTextField;


    private static final String TAG = "MainActivity";
    PersistableBundle bundle = new PersistableBundle();
    int jobId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sampleTextField = findViewById(R.id.editText3);

    }

    public void scheduleJob(View v) {
        ComponentName componentName = new ComponentName(this, DataSendingService.class);

        JSONObject formJson = new JSONObject();
        try{
            formJson.put("enteredText", sampleTextField.getText());
        } catch (JSONException e){
            e.printStackTrace();
        }

        String jsonString = formJson.toString();
        bundle.putString("json", jsonString);

        JobInfo info = new JobInfo.Builder(jobId, componentName)
                .setRequiresCharging(false)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .setExtras(bundle)
                .build();

        jobId += 1;
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");

        } else {
            Log.d(TAG, "Job scheduling failed");
        }
    }

}

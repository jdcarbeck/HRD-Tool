package com.example.hrdtool;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;

import android.content.ComponentName;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;


import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;



import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;

    private static final String TAG = "MainActivity";
    PersistableBundle bundle = new PersistableBundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout mTabLayout = findViewById(R.id.tab_layout);
        ViewPager mViewPager = findViewById(R.id.pager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapterViewPager);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    public void scheduleJob(View v) {
        ComponentName componentName = new ComponentName(this, DataSendingService.class);

        JSONObject formJson = new JSONObject();
        try{
            formJson.put("enteredText", "sample");
        } catch (JSONException e){
            e.printStackTrace();
        }

        String jsonString = formJson.toString();
        bundle.putString("json", jsonString);

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

    public class MyPagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 2;

        private int[] images = new int[]{R.drawable.ic_content_paste_black_24dp, R.drawable.ic_people_outline_black_24dp};
        private String[] titles = new String[]{"Form","Help Info"};

        public MyPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }

        @Override
        public int getCount(){ return NUM_ITEMS; }

        @Override
        public Fragment getItem(int position){
            switch (position){
                case 0:
                    return FormFragment.newInstance(0, titles[0]);
                case 1:
                    return HelpFragment.newInstance(1,titles[1]);
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            Drawable image = getDrawable(images[position]);
//            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//            SpannableString sb = new SpannableString("" + titles[position]);
//            ImageSpan imageSpan = new ImageSpan(image);
//            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            return sb;
            return  titles[position];
        }
    }
}




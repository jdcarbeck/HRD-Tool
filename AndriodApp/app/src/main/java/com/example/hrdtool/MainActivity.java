package com.example.hrdtool;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings.Secure;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.opencsv.CSVReader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.example.hrdtool.CSVForm.AREA;
import static com.example.hrdtool.CSVForm.CITY;
import static com.example.hrdtool.CSVForm.COUNTRY;
import static com.example.hrdtool.CSVForm.TYPES;

import static com.example.hrdtool.CSVHelp.find_user_zone_type;
import static com.example.hrdtool.CSVHelp.index;
import static com.example.hrdtool.CSVHelp.length;
import static com.example.hrdtool.CSVHelp.users;

public class MainActivity extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;

    private static final String TAG = "MainActivity";
    PersistableBundle bundle = new PersistableBundle();
    int jobId = 1;

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

    public void scheduleJob(JSONObject rcvJSON) {
        ComponentName componentName = new ComponentName(this, DataSendingService.class);

        JSONObject formJson = rcvJSON;
        String android_id = Secure.getString(MainActivity.this.getContentResolver(),
                Secure.ANDROID_ID);
        try {
            formJson.put("id", android_id);
        }
        catch (JSONException e){
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

    public void getHelp(CSVReader reader, String type, String area) {
        try {
            if(type.equalsIgnoreCase(
                    TYPES[0]))
                type = TYPES[1];
            if(area.equalsIgnoreCase(AREA[0]))
                area = AREA[1];

            users = find_user_zone_type(reader, area, type);
            length = users.size();

            Button next = findViewById(R.id.button_next);
            Button prev = findViewById(R.id.button_prev);
            TextView page_ratio = (TextView) findViewById(R.id.page_ratio);

            prev.setEnabled(false);
            if(users.size() <= 1)
                next.setEnabled(false);
            else
                next.setEnabled(true);

            if(users.isEmpty()) {
                TextView support_name = (TextView) findViewById(R.id.support_name);
                support_name.setText(R.string.default_support);
                TextView support_address = (TextView) findViewById(R.id.support_address);
                support_address.setText(null);
                TextView support_telephone = (TextView) findViewById(R.id.support_telephone);
                support_telephone.setText(null);
                TextView support_type = (TextView) findViewById(R.id.support_type);
                support_type.setText(null);
                TextView support_email = (TextView) findViewById(R.id.support_email);
                support_email.setText(null);
            }
            else {
                CSVHelp.CSVHelpRow user = users.get(index-1);
                this.display_CSVRow(user);
                String ratio = index+"/"+length;
                page_ratio.setText(ratio);
            }
        } catch  (Exception e) {
            e.printStackTrace();

        }
    }
    private void display_CSVRow(CSVHelp.CSVHelpRow user) {

        Log.d("DEBUG", "Inside the display function");
        TextView support_name = (TextView) findViewById(R.id.support_name);
        support_name.setText(user.getName());
        TextView support_type = (TextView) findViewById(R.id.support_type);
        support_type.setText(user.getType());
        TextView support_address = (TextView) findViewById(R.id.support_address);
        support_address.setText(user.getCompleteAddress());

        // Make the address a link
        Pattern pattern = Pattern.compile(".*", Pattern.DOTALL);
        Linkify.addLinks(support_address, pattern, "geo:0,0?q=");
        support_address.setMovementMethod(LinkMovementMethod.getInstance());

        TextView support_telephone = (TextView) findViewById(R.id.support_telephone);
        support_telephone.setText(user.getCleanTelephone());

        // Make the telephone number a phone link
        Linkify.addLinks(support_telephone, Patterns.PHONE,"tel:",Linkify.sPhoneNumberMatchFilter,Linkify.sPhoneNumberTransformFilter);
        support_telephone.setMovementMethod(LinkMovementMethod.getInstance());

        TextView support_email = (TextView) findViewById(R.id.support_email);
        support_email.setText(user.getEmail());
    }
    public void nextUser(View view) {
        if(index >= length) {
            index = length;
            return;
        }
        else {
            Button next = findViewById(R.id.button_next);
            Button prev = findViewById(R.id.button_prev);
            index++;
            this.display_CSVRow(users.get(index-1));
            if(index == length)
                next.setEnabled(false);
            prev.setEnabled(true);
            TextView page_ratio = (TextView) findViewById(R.id.page_ratio);
            String ratio = index + "/" + length;
            page_ratio.setText(ratio);

        }
        return;
    }
    public void previousUser(View view) {
        if(index <= 1) {
            index = 1;
            return;
        } else {
            Button next = findViewById(R.id.button_next);
            Button prev = findViewById(R.id.button_prev);
            index--;
            this.display_CSVRow(users.get(index-1));
            if(index == 1)
                prev.setEnabled(false);
            next.setEnabled(true);
            TextView page_ratio = (TextView) findViewById(R.id.page_ratio);
            String ratio = index + "/" + length;
            page_ratio.setText(ratio);
        }
    }
    public void toCalculator(View view){
        Intent intent = new Intent(this, Calculator.class);
        startActivity(intent);

    }


//    HomeWatcher mHomeWatcher = new HomeWatcher(this);
//    mHomeWatcher.setOnHomePressedListener(new OnHomePressedListener() {
//        @Override
//        public void onHomePressed() {
//            // do something here...
//        }
//        @Override
//        public void onHomeLongPressed() {
//        }
//    });
//    mHomeWatcher.startWatch();
//    public void onAccessibilityEvent(AccessibilityEvent event) {
//        if (event.getEventType() != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || event.getClassName() == null)
//            return;
//
//        String Calculator = String.valueOf(event.getClassName());
//
//        if (Calculator.equals("com.android.internal.policy.impl.RecentApplicationsDialog")
//                || Calculator.equals("com.android.systemui.recent.RecentsActivity")
//                || Calculator.equals("com.android.systemui.recents.RecentsActivity")){
//            //Recent button was pressed. Do something.
//            Intent intent = new Intent(this, Calculator.class);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        }
//    }

}




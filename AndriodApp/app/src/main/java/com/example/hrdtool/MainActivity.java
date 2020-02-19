package com.example.hrdtool;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    private static class CSVHelpRow {
        private String name;

        private String type;

        private String address;

        private String telephone;

        private String email;

        private String area;

        CSVHelpRow() {
            this.name = null;
            this.type = null;
            this.address = null;
            this.telephone = null;
            this.email = null;
            this.area = null;
        }

        CSVHelpRow(String name, String type, String address,
               String telephone, String email, String area) {
            this.name = name;
            this.type= type;
            this.address= address;
            this.telephone = telephone;
            this.email = email;
            this.area = area;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAddress() {
            return address;
        }

        /*
        Returns a more complete address with details of the city and country so that google maps
        can find the appropiate place. I assume that all the support is in Dublin, Ireland and
        therefore that information may not be present in the CSV. Without it, Maps can find the
        wrong place and needs the precision to be added. For the actual app, the city might
        not be fixed, but area will be known through the form and we can use that. The country,
        Nicaragua, is known and static, so the same assumption applies.
        */
        public String getCompleteAddress() {
            String fullAddress = this.address;
            if(!fullAddress.toLowerCase().contains(CITY.toLowerCase()))
                fullAddress+= ", " + CITY.substring(0,1).toUpperCase() + CITY.substring(1).toLowerCase();
            if(!fullAddress.toLowerCase().contains(COUNTRY.toLowerCase()))
                fullAddress += ", " + COUNTRY.substring(0,1).toUpperCase() + COUNTRY.substring(1).toLowerCase();
            return fullAddress;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTelephone() {
            return telephone;
        }

        // Remove all non numeric character inside the telephone number to make it a phone link.
        public String getCleanTelephone() {
            return telephone.replaceAll("[^0-9]","");
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String toString() {
            String ret = "";
            ret += this.name + ", ";
            ret += this.type + ", ";
            ret += this.address + ", ";
            ret += this.telephone + ", ";
            ret += this.email + ", ";
            ret += this.area + ".";
            return ret;
        }
    }

    private int index = 1;
    private ArrayList<CSVHelpRow> users = new ArrayList<CSVHelpRow>(0);
    private int length = 0;

    private static final String CITY = "DUBLIN";
    private static final String COUNTRY = "IRELAND";

    private static final String[] TYPES = {"Choose type of support needed", "Any", "Legal", "Medical", "Psychological"};
    private static final String[] AREA = {"What area are you located in?", "Any", "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "D10",
            "D11", "D12", "D13", "D14", "D15", "D16", "D17", "D18", "D19", "D20"};

    private static final String[] HELP_HEADERS = {"ID","NAME","TYPE","ADDRESS","TELEPHONE","EMAIL","AREA"};
    private static final String[] FORM_HEADERS = {"DATA_TYPE","INCIDENT_DATE","ATTENTION_DATE","GENDER"
            ,"ATTENTION_TYPE","DETAILS"};

    private static final int HELP_TYPE_COLUMN = 2;
    private static final int HELP_AREA_COLUMN = 6;

    private static ArrayList<CSVHelpRow> find_user_zone_type(CSVReader reader, String area, String type) {
        boolean headers = check_headers(reader);
        ArrayList<CSVHelpRow> list = new ArrayList<CSVHelpRow>();
        try {
            if(headers)
                reader.skip(1);
            String[] read = reader.readNext();
            while(read != null) {
                boolean add_to_list = true;
                if(!area.equalsIgnoreCase("any"))
                    if(!read[HELP_AREA_COLUMN].toLowerCase().contains(area.toLowerCase()))
                        add_to_list = false;
                if(!type.equalsIgnoreCase("any"))
                    if(!read[HELP_TYPE_COLUMN].toLowerCase().contains(type.toLowerCase()))
                        add_to_list = false;
                if(add_to_list) {
                    CSVHelpRow row = new CSVHelpRow(read[1],read[2],read[3],read[4],read[5],read[6]);
                    list.add(row);
                }
                read = reader.readNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static boolean check_headers(CSVReader reader) {
        try {
            String[] first_line = reader.peek();
            for(int i=0; i<first_line.length; i++) {
                for(int j=0; j<HELP_HEADERS.length; j++) {
                    if(first_line[i].equalsIgnoreCase(HELP_HEADERS[j]))
                        return true;
                }
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void display_CSVRow(CSVHelpRow user) {
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

    private static class CSVFormRow {
        private String data_type;

        private String incident_date;

        private String attention_date;

        private String gender;

        private String age;

        private String details;


        CSVFormRow() {
            this.data_type = null;
            this.incident_date = null;
            this.attention_date = null;
            this.gender = null;
            this.age = null;
            this.details = null;
        }

        CSVFormRow(String data_type, String incident_date, String attention_date, String gender,
                   String age, String details) {
            this.data_type = data_type;
            this.incident_date = incident_date;
            this.attention_date = attention_date;
            this.gender = gender;
            this.age = age;
            this.details = details;
        }

        public String getData() {
            return data_type;
        }

        public void setData(String data_type) {
            this.data_type = data_type;
        }

        public String getIncident() {
            return incident_date;
        }

        public void setIncident(String incident_date) {
            this.incident_date = incident_date;
        }

        public String getAttention() {
            return attention_date;
        }

        public void setAttention(String attention_date) {
            this.attention_date = attention_date;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String toString() {
            String ret = "";
            ret += this.data_type + ", ";
            ret += this.incident_date + ", ";
            ret += this.attention_date + ", ";
            ret += this.gender + ", ";
            ret += this.age + ", ";
            ret += this.details + ".";
            return ret;
        }
    }

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

        /*JSONObject formJson = new JSONObject();
        try{
            formJson.put("enteredText", "sample");
        } catch (JSONException e){
            e.printStackTrace();
        }*/
        JSONObject formJson = rcvJSON;
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

    public void getHelp(View view) {
        try {
            CSVReader reader = new CSVReader(new InputStreamReader(getResources().openRawResource(R.raw.help)));

            Spinner dropdown_type = findViewById(R.id.spinner_type);
            Spinner dropdown_area = findViewById(R.id.spinner_area);

            String type = dropdown_type.getSelectedItem().toString();
            String area = dropdown_area.getSelectedItem().toString();

            if(type.equalsIgnoreCase(TYPES[0]))
                type = TYPES[1];
            if(area.equalsIgnoreCase(AREA[0]))
                area = AREA[1];

            this.users = find_user_zone_type(reader, area, type);
            this.length = users.size();

            Button next = findViewById(R.id.button_next);
            Button prev = findViewById(R.id.button_prev);
            TextView page_ratio = (TextView) findViewById(R.id.page_ratio);

            prev.setEnabled(false);
            if(users.size() <= 1)
                next.setEnabled(false);

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
                CSVHelpRow user = users.get(this.index-1);
                display_CSVRow(user);
                String ratio = this.index+"/"+this.length;
                page_ratio.setText(ratio);
            }
        } catch  (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "The specified file was not found", Toast.LENGTH_SHORT).show();
        }
    }

    public void nextUser(View view) {
        if(this.index >= this.length) {
            this.index = this.length;
            return;
        }
        else {
            Button next = findViewById(R.id.button_next);
            Button prev = findViewById(R.id.button_prev);
            this.index++;
            display_CSVRow(this.users.get(this.index-1));
            if(this.index == this.length)
                next.setEnabled(false);
            prev.setEnabled(true);
            TextView page_ratio = (TextView) findViewById(R.id.page_ratio);
            String ratio = this.index + "/" + this.length;
            page_ratio.setText(ratio);

        }
        return;
    }
    public void previousUser(View view) {
        if(this.index <= 1) {
            this.index = 1;
            return;
        } else {
            Button next = findViewById(R.id.button_next);
            Button prev = findViewById(R.id.button_prev);
            this.index--;
            display_CSVRow(this.users.get(index-1));
            if(this.index == 1)
                prev.setEnabled(false);
            next.setEnabled(true);
            TextView page_ratio = (TextView) findViewById(R.id.page_ratio);
            String ratio = this.index + "/" + this.length;
            page_ratio.setText(ratio);
        }
    }
}




package com.example.app_base;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Pattern;

public class Support_Information_Activity extends AppCompatActivity {


    private int index = 1;
    private ArrayList<CSVRow> users = new ArrayList<CSVRow>(0);
    private int length = 0;

    private static final String CITY = "DUBLIN";
    private static final String COUNTRY = "IRELAND";
    private static final String[] HEADERS = {"ID","NAME","TYPE","ADDRESS","TELEPHONE","EMAIL","AREA"};
    private static final int TYPE_COLUMN = 2;
    private static final int AREA_COLUMN = 6;

    /*
    Find the correct support in the csv based on type of help needed and which area the victim is
     */
    private static ArrayList<CSVRow> find_user_zone_type(CSVReader reader, String area, String type) {
        boolean headers = check_headers(reader);
        ArrayList<CSVRow> list = new ArrayList<CSVRow>();
        try {
            if(headers)
                reader.skip(1);
            String[] read = reader.readNext();
            while(read != null) {
                boolean add_to_list = true;
                if(!area.equalsIgnoreCase("any"))
                    if(!read[AREA_COLUMN].toLowerCase().contains(area.toLowerCase()))
                        add_to_list = false;
                if(!type.equalsIgnoreCase("any"))
                    if(!read[TYPE_COLUMN].toLowerCase().contains(type.toLowerCase()))
                        add_to_list = false;
                if(add_to_list) {
                    CSVRow row = new CSVRow(read[1],read[2],read[3],read[4],read[5],read[6]);
                    list.add(row);
                }
                read = reader.readNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /*
    Peek at the first row of the csv file and if one of the column matches with the constant
    then the csv has a header row which we can skip.
    */
    private static boolean check_headers(CSVReader reader) {
        try {
            String[] first_line = reader.peek();
            for(int i=0; i<first_line.length; i++) {
                for(int j=0; j<HEADERS.length; j++) {
                    if(first_line[i].equalsIgnoreCase(HEADERS[j]))
                        return true;
               }
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
    CSVRow class has private fields corresponding to the csv file, minus the id column
    Has Setters/Getters
     */
    private static class CSVRow {
        private String name;

        private String type;

        private String address;

        private String telephone;

        private String email;

        private String area;

        CSVRow() {
            this.name = null;
            this.type = null;
            this.address = null;
            this.telephone = null;
            this.email = null;
            this.area = null;
        }

        CSVRow(String name, String type, String address,
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
        can find the appropiate place. The assumption for this example is that, all the support is
        in Dublin, Ireland and therefore that information may not be present in the CSV. Maps can
        find the wrong place. For the actual app, the city might not be fixed, but area will be known
        through the form and we can use that. The country, Nicaragua, is known and static, so the same
        assumption applies.
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

    // TODO: Change how the phone number is displayed so that the phone link works ( Remove the parathenses )

    private void display_CSVRow(CSVRow user) {
        TextView support_name = (TextView) findViewById(R.id.support_name);
        support_name.setText(user.getName());
        TextView support_type = (TextView) findViewById(R.id.support_type);
        support_type.setText(user.getType());
        TextView support_address = (TextView) findViewById(R.id.support_address);
        support_address.setText(user.getCompleteAddress());

        if(support_address != null){
            Pattern pattern = Pattern.compile(".*", Pattern.DOTALL);
            Linkify.addLinks(support_address, pattern, "geo:0,0?q=");
            support_address.setMovementMethod(LinkMovementMethod.getInstance());
        }

        TextView support_telephone = (TextView) findViewById(R.id.support_telephone);
        support_telephone.setText(user.getCleanTelephone());

        // Make the telephone number a phone link
        if(support_telephone != null){
            Linkify.addLinks(support_telephone, Patterns.PHONE,"tel:",Linkify.sPhoneNumberMatchFilter,Linkify.sPhoneNumberTransformFilter);
            support_telephone.setMovementMethod(LinkMovementMethod.getInstance());
        }

        TextView support_email = (TextView) findViewById(R.id.support_email);
        support_email.setText(user.getEmail());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support__information_);
        Intent intent = getIntent();

        try {
            /*
             getAssets instantiate the assetManager to read files in app/src/main/assets.
             open gives back a raw Inputstream which is converted into a CSVreader object.
             Now its done via the resource manager and the file is in the raw folder.
             TODO: Let user provide path to csv, such that they can change the csv in the future
             Right now the csv is inside the app folder and comes packaged with the app.
             */


            /*
            String uri = intent.getStringExtra("uri");
            CSVReader reader = new CSVReader(new InputStreamReader(getContentResolver().openInputStream(Uri.parse(uri))));
            */

            CSVReader reader = new CSVReader(new InputStreamReader(getResources().openRawResource(R.raw.test)));
            String area = intent.getStringExtra("area");
            String type = intent.getStringExtra("type");

            Log.d("DEBUG", "The passed area and type of support area: " + area + ", " + type);
            this.users = find_user_zone_type(reader, area, type);
            Log.d("DEBUG", "Finished collecting users");

            // FOR DEBUGGING.
            /*StringBuilder sb = new StringBuilder();
            for (CSVRow r : users) {
                sb.append(r.toString());
                sb.append("\n");
            }
            Log.d("DEBUG", "Users found: " + sb.toString());
            */

            this.length = this.users.size();

            Button next = findViewById(R.id.button_next);
            Button prev = findViewById(R.id.button_previous);
            TextView page_ratio = (TextView) findViewById(R.id.page_ratio);

            prev.setEnabled(false);
            if(users.size() <= 1)
                next.setEnabled(false);

            // If no support is available, set text view to null
            if(users.isEmpty()) {
                TextView support_name = (TextView) findViewById(R.id.support_name);
                support_name.setText(R.string.No_available_help);
                TextView support_address = (TextView) findViewById(R.id.support_address);
                support_address.setText(null);
                TextView support_telephone = (TextView) findViewById(R.id.support_telephone);
                support_telephone.setText(null);
                TextView support_type = (TextView) findViewById(R.id.support_type);
                support_type.setText(null);
                TextView support_email = (TextView) findViewById(R.id.support_email);
                support_email.setText(null);
            }
            // Else use the first row and display its information
            else {
                CSVRow user = users.get(this.index-1);
                display_CSVRow(user);
                String ratio = this.index+"/"+this.length;
                page_ratio.setText(ratio);
            }

        }
        catch (Exception e) {
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
            Button prev = findViewById(R.id.button_previous);
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
            Button prev = findViewById(R.id.button_previous);
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


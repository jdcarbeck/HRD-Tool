package com.example.hrdtool;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.opencsv.CSVReader;

import java.util.ArrayList;

public class FormActivity extends AppCompatActivity {

    private static final String[] HEADERS = {"DATA_TYPE","INCIDENT_DATE","ATTENTION_DATE","GENDER"
            ,"ATTENTION_TYPE","DETAILS"};
    private static final int TYPE_COLUMN = 2;
    private static final int AREA_COLUMN = 6;

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

    private static class CSVRow {
        private String data_type;

        private String incident_date;

        private String attention_date;

        private String gender;

        private String age;

        private String details;


        CSVRow() {
            this.data_type = null;
            this.incident_date = null;
            this.attention_date = null;
            this.gender = null;
            this.age = null;
            this.details = null;
        }

        CSVRow(String data_type, String incident_date, String attention_date, String gender,
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_form);
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        Spinner dropdown_gender = findViewById(R.id.gender);
        Spinner dropdown_age = findViewById(R.id.age);
        Spinner dropdown_area = findViewById(R.id.type);

    }
}

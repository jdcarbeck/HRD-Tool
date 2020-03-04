package com.example.hrdtool;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

public class FormFragment extends Fragment {

    private String title;
    private int page;
    private  Button submit_form;
    private EditText i_date;
    private EditText a_date;
    private EditText details;
    private Spinner dropdown_gender;
    private Spinner dropdown_type;
    private Spinner dropdown_age;
    public static FormFragment newInstance(int page, String title){
        FormFragment formFragment = new FormFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        formFragment.setArguments(args);
        return formFragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                Bundle savedInstanceState) {

        final String[] SUPPORT = {"Choose type of support needed",
                "Any", "Legal", "Medical", "Psychological"};
        final String[] GENDER = {"What is your gender?", "Male", "Female", "Other"};
        final String[] AGE = {"Select Age Range", "0-9", "10-19", "20-29", "30-39", "40-49", "50+"};
        final String[] AREA = {"What area are you located in?", "Any", "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "D10",
                "D11", "D12", "D13", "D14", "D15", "D16", "D17", "D18", "D19", "D20"};

        View v = inflater.inflate(R.layout.fragment_form, container, false);

        dropdown_gender = (Spinner) v.findViewById(R.id.spinner_gender);
        Spinner dropdown_type = (Spinner) v.findViewById(R.id.spinner_type);
        dropdown_age = (Spinner) v.findViewById(R.id.spinner_age);
        Spinner dropdown_area = (Spinner) v.findViewById(R.id.spinner_area);
    
        submit_form = v.findViewById(R.id.submit_form);
        i_date = v.findViewById(R.id.i_date);
        a_date = v.findViewById(R.id.a_date);
        details = v.findViewById(R.id.details);
        submit_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jObj = new JSONObject();
                try {

                    jObj.put("incident date", i_date.getText().toString());
                    jObj.put("attention date",  a_date.getText().toString());
                    jObj.put("gender",  dropdown_gender.getSelectedItem().toString());
                    jObj.put("age range",  dropdown_age.getSelectedItem().toString());
                    jObj.put("details",  details.getText().toString());
                } catch (Exception e) {
                    System.out.println("Error:" + e);
                }
                ((MainActivity)getActivity()).scheduleJob(jObj);
            }
        });

        ArrayAdapter<String> gender_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, GENDER) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        ArrayAdapter<String> type_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, SUPPORT) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        ArrayAdapter<String>  age_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, AGE) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        ArrayAdapter<String> area_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, AREA) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        dropdown_age.setAdapter(age_adapter);
        dropdown_type.setAdapter(type_adapter);
        dropdown_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String type = adapterView.getItemAtPosition(position).toString();
                ((MainActivity)getActivity()).getHelp(view, type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        dropdown_gender.setAdapter(gender_adapter);
        dropdown_area.setAdapter(area_adapter);

        return v;
    }
}
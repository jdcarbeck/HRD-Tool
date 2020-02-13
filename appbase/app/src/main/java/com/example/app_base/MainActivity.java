package com.example.app_base;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final String[] TYPES = {"Choose type of support needed", "Any", "Legal", "Medical", "Psychological"};

    private static final String[] AREA = {"What area are you located in?", "Any", "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "D10",
                                            "D11", "D12", "D13", "D14", "D15", "D16", "D17", "D18", "D19", "D20"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner dropdown_type = findViewById(R.id.spinner_type);
        Spinner dropdown_area = findViewById(R.id.spinner_area);

        // Set up the spinners/drop down menu such that the first option is a hint as to what information is needed.
        ArrayAdapter<String> adapter_type = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, TYPES) {
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
        ArrayAdapter<String> adapter_area = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, AREA) {
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

        dropdown_type.setAdapter(adapter_type);
        dropdown_area.setAdapter(adapter_area);

        // Code for the hints, taken online. Dunno fully how it works.
        dropdown_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dropdown_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0){
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /* When button is click, send type and area info to new activity and will display the relevant help.*/
    public void changeScreen(View view) {
        Spinner dropdown_type = findViewById(R.id.spinner_type);
        Spinner dropdown_area = findViewById(R.id.spinner_area);


        /*
        If the user forgets to choose an option on the drop down,
        the hint message will be selected and sent to the next activity
        default behavior should be "any".
         */
        String type = dropdown_type.getSelectedItem().toString();
        if(type.equalsIgnoreCase(TYPES[0]))
            type=TYPES[1];
        String area = dropdown_area.getSelectedItem().toString();
        if(area.equalsIgnoreCase(AREA[0]))
            area=AREA[1];

        Intent intent = new Intent(this, Support_Information_Activity.class);
        intent.putExtra("type", type);
        intent.putExtra("area", area);
        startActivity(intent);

    }
}

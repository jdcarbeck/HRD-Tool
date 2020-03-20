package com.example.hrdtool;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Context;

import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

import com.google.android.material.textfield.TextInputEditText;
import android.widget.Toast;


import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;

public class FormFragment extends Fragment {

    private String title;
    private int page;
    private  Button submit_form;
    private TextInputEditText i_date;
    private EditText a_date;
    private EditText details;
    private AutoCompleteTextView dropdown_gender;
    private AutoCompleteTextView dropdown_type;
    private AutoCompleteTextView dropdown_age;
    private AutoCompleteTextView dropdown_area;
    private ListView listView;
    private List<String> arrayList;
    private List<String> arrayList1;
    private Context context;
    private Context context1;
    private RecyclerViewAdapter adapter;
    private RecyclerViewAdapter1 adapter2;
    private RecyclerViewRadioBtnAdapter adapter1;
    private CheckBox chb;
    private CheckBox chb1;

    DatePickerDialog.OnDateSetListener setListener;

    public static FormFragment newInstance(int page, String title){
        FormFragment formFragment = new FormFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        formFragment.setArguments(args);
        return formFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
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

        final String[] SUPPORT = {"Information", "Legal Attention", "Psychosocial Attention", "Medical Attention","Refuge","Other Attention"};
        final String[] SUPPORT1 = {"Information", "Legal Attention", "Psychosocial Attention", "Medical Attention","Refuge","Other Attention"};
        final String[] SUPPORT2 = { "Legal Attention", "Psychosocial Attention", "Medical Attention","Other Attention"};
        final String[] GENDER = {"Male", "Female"};
        final String[] GENDER1 = {"Male", "Female"};
        final String[] KNOWN = {"Known", "Unknown"};
        final String[] IFKNOWN = {"Current partner","Former partner" ,"Relative" ,"Neighbor" ,"Friend" ,"Association" ,"Other"};

        final String[] AGE = {"0-9", "10-19", "20-29", "30-39", "40-49", "50+"};
        final String[] AREA = {"Any", "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "D10",
                "D11", "D12", "D13", "D14", "D15", "D16", "D17", "D18", "D19", "D20"};

        String[] CLASSIFICATION_PHY = {"Physical (an act of physical violence that is not sexual in nature eg. Hitting)"
                ,
                "Hitting, slapping, punching", "Pushing, shoving", "Choking", "Cutting",
                "Burning", "Shooting or use of any weapons", "Acid attacks", "Any other act that results in pain, discomfort or injury"};


        String[] CLASSIFICATION_EMO = { "Emotional/psychological (infliction of mental or emotional pain or injury eg. threats of physical or sexual violence, intimidation, humiliation)"};

        String[] CLASSIFICATION_SEX = {"Sexual (any form of non-consensual sexual contact)"};

        final String[] CLASSIFICATION_FM = {"Forced Marriage (the marriage of an individual against her or his will)"};

        final String[] DENIAL = {"Denial of rightful access to economic resources",
                "Denial of rightful access to assets or livelihood opportunities",
                "Denial of Access to education",
                "Denial of Access to health care" ,
                "Conflict between neighbours or broader community members over resources" ,
                "Denial of Access to other social services"};

        View v = inflater.inflate(R.layout.fragment_form, container, false);

        dropdown_gender =  v.findViewById(R.id.spinner_gender);
//         dropdown_type = v.findViewById(R.id.spinner_type);
        dropdown_age =  v.findViewById(R.id.spinner_age);
        dropdown_area =  v.findViewById(R.id.municipality);

        submit_form = v.findViewById(R.id.submit_form);
        i_date =  v.findViewById(R.id.i_date);
        a_date = v.findViewById(R.id.a_date);
//        details = v.findViewById(R.id.details);
        chb = v.findViewById(R.id.rowCheckBox);
        chb1 = v.findViewById(R.id.rowCheckBox);


        Calendar cal = Calendar.getInstance();
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        final int day = cal.get(Calendar.DAY_OF_MONTH);

//        setListener = DatePickerDialog.OnDateSetListener(){
//            @Override
//
//        }

        submit_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jObj = new JSONObject();
                try {

                    jObj.put("incident date", i_date.getText().toString());
                    jObj.put("attention date",  a_date.getText().toString());
                    jObj.put("gender",  dropdown_gender.getOnItemSelectedListener().toString());
                    jObj.put("age range",  dropdown_age.getOnItemSelectedListener().toString());
                    jObj.put("details",  details.getText().toString());
                } catch (Exception e) {
                    System.out.println("Error:" + e);
                }
                ((MainActivity)getActivity()).scheduleJob(jObj);
            }
        });



        //------------------------------gender of survivor------------------------------//
        ArrayAdapter<String> gender_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.select_dialog_singlechoice, GENDER) {

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
        //------------------------------age range of survivor------------------------------//
        ArrayAdapter<String>  age_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.select_dialog_singlechoice, AGE) {
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
        //------------------------------municipailty------------------------------//
        ArrayAdapter<String> area_adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.select_dialog_singlechoice, AREA) {
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
        //------------------------------Attention survivor seeking------------------------------//
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
        arrayList = Arrays.asList(SUPPORT);


        adapter = new RecyclerViewAdapter(context, arrayList);
        recyclerView.setAdapter(adapter);

        //------------------------------Attention HRD provided survivor------------------------------//
        RecyclerView recyclerView1 = (RecyclerView) v.findViewById(R.id.recycler_view1);
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
        recyclerView1.setLayoutManager(linearLayoutManager1);
        arrayList = new ArrayList<>();
        arrayList = Arrays.asList(SUPPORT1);

        adapter = new RecyclerViewAdapter(context, arrayList);
        recyclerView1.setAdapter(adapter);


        //------------------------------Attention HRD offered survivor------------------------------//
        RecyclerView recyclerView2 = (RecyclerView) v.findViewById(R.id.recycler_view2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(context);
        recyclerView2.setLayoutManager(linearLayoutManager2);
        arrayList = new ArrayList<>();
        arrayList = Arrays.asList(SUPPORT2);

        adapter = new RecyclerViewAdapter(context, arrayList);
        recyclerView2.setAdapter(adapter);

        //------------------------------gender of perpetrator------------------------------//
        RecyclerView recyclerView3 = (RecyclerView) v.findViewById(R.id.recycler_view3);
        recyclerView3.setHasFixedSize(true);
        recyclerView3.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(context);
        recyclerView3.setLayoutManager(linearLayoutManager3);
        arrayList = new ArrayList<>();
        arrayList = Arrays.asList(GENDER1);

        adapter1 = new RecyclerViewRadioBtnAdapter(context, arrayList);
        recyclerView3.setAdapter(adapter1);

        //------------------------------is perpetrator known------------------------------//
        RecyclerView recyclerView4 = (RecyclerView) v.findViewById(R.id.recycler_view4);
        recyclerView4.setHasFixedSize(true);
        recyclerView4.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager4 = new LinearLayoutManager(context);
        recyclerView4.setLayoutManager(linearLayoutManager4);
        arrayList = new ArrayList<>();
        arrayList = Arrays.asList(KNOWN);

        adapter1 = new RecyclerViewRadioBtnAdapter(context, arrayList);
        recyclerView4.setAdapter(adapter1);

        //------------------------------if perpetrator is known are they------------------------------//
        RecyclerView recyclerView5 = (RecyclerView) v.findViewById(R.id.recycler_view5);
        recyclerView5.setHasFixedSize(true);
        recyclerView5.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager5 = new LinearLayoutManager(context);
        recyclerView5.setLayoutManager(linearLayoutManager5);
        arrayList = new ArrayList<>();
        arrayList = Arrays.asList(IFKNOWN);

        adapter1 = new RecyclerViewRadioBtnAdapter(context, arrayList);
        recyclerView5.setAdapter(adapter1);


        //------------------------------Denial of recources------------------------------//
        RecyclerView recyclerView6 = (RecyclerView) v.findViewById(R.id.recycler_view6);
        recyclerView6.setHasFixedSize(true);
        recyclerView6.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager6 = new LinearLayoutManager(context);
        recyclerView6.setLayoutManager(linearLayoutManager6);
        arrayList = new ArrayList<>();
        arrayList = Arrays.asList(DENIAL);

        adapter = new RecyclerViewAdapter(context, arrayList);
        recyclerView6.setAdapter(adapter);

        //------------------------------Classification PHY------------------------------//
        RecyclerView recyclerView7 = (RecyclerView) v.findViewById(R.id.recycler_view7);
        recyclerView7.setHasFixedSize(true);
        recyclerView7.setNestedScrollingEnabled(false);

        RecyclerView recyclerView8 = (RecyclerView) v.findViewById(R.id.recycler_view8);
        recyclerView8.setHasFixedSize(true);
        recyclerView8.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager7 = new LinearLayoutManager(context);
        LinearLayoutManager linearLayoutManager8 = new LinearLayoutManager(context1);

        recyclerView7.setLayoutManager(linearLayoutManager7);
        recyclerView8.setLayoutManager(linearLayoutManager8);


//
        arrayList = new ArrayList<>();
        arrayList = Arrays.asList(CLASSIFICATION_PHY[0]);
        adapter = new RecyclerViewAdapter(context, arrayList);
        recyclerView7.setAdapter(adapter);

        CLASSIFICATION_PHY = new String[]{
                "Hitting, slapping, punching", "Pushing, shoving", "Choking", "Cutting",
                "Burning", "Shooting or use of any weapons", "Acid attacks", "Any other act that results in pain, discomfort or injury"};
        arrayList = new ArrayList<>();
        arrayList = Arrays.asList(CLASSIFICATION_PHY);

//        for(int i=1;i<arrayList.size();i++) {
//            arrayList = Arrays.asList(CLASSIFICATION_PHY[i]);
        adapter2 = new RecyclerViewAdapter1(context, arrayList);
//        };

        recyclerView8.setAdapter(adapter2);



        //------------------------------Classification EMO------------------------------//

        RecyclerView recyclerView9 = (RecyclerView) v.findViewById(R.id.recycler_view9);
        recyclerView9.setHasFixedSize(true);
        recyclerView9.setNestedScrollingEnabled(false);

        RecyclerView recyclerView10 = (RecyclerView) v.findViewById(R.id.recycler_view10);
        recyclerView10.setHasFixedSize(true);
        recyclerView10.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager9 = new LinearLayoutManager(context);
        LinearLayoutManager linearLayoutManager10 = new LinearLayoutManager(context1);

        recyclerView9.setLayoutManager(linearLayoutManager9);
        recyclerView10.setLayoutManager(linearLayoutManager10);


//
        arrayList = new ArrayList<>();
        arrayList = Arrays.asList(CLASSIFICATION_EMO);
        adapter = new RecyclerViewAdapter(context, arrayList);
        recyclerView9.setAdapter(adapter);

        CLASSIFICATION_EMO = new String[]{
                "Threats of physical or sexual violence","Threat to hurt/kill","Intimidation", "Humiliation",
                "Isolation – eg not allowed to visit family, friends", "Stalking","Verbal harassment – eg. Shouting, remarks ",
                "Unwanted attention","Gestures or written words of a sexual and/or menacing nature", "Destruction of cherished things",
                "Custody/access issues","Threats to children"};
        arrayList1 = new ArrayList<>();
        arrayList1 = Arrays.asList(CLASSIFICATION_EMO);

//        for(int i=0;i<arrayList1.size();i++) {
        adapter2 = new RecyclerViewAdapter1(context1, arrayList1);

//        };

        recyclerView10.setAdapter(adapter2);

//        //------------------------------Classification SEX------------------------------//

        RecyclerView recyclerView11 = (RecyclerView) v.findViewById(R.id.recycler_view11);
        recyclerView11.setHasFixedSize(true);
        recyclerView11.setNestedScrollingEnabled(false);

        RecyclerView recyclerView12 = (RecyclerView) v.findViewById(R.id.recycler_view12);
        recyclerView12.setHasFixedSize(true);
        recyclerView12.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager11 = new LinearLayoutManager(context);
        LinearLayoutManager linearLayoutManager12 = new LinearLayoutManager(context1);

        recyclerView11.setLayoutManager(linearLayoutManager11);
        recyclerView12.setLayoutManager(linearLayoutManager12);


//
        arrayList = new ArrayList<>();
        arrayList = Arrays.asList(CLASSIFICATION_SEX);
        adapter = new RecyclerViewAdapter(context, arrayList);
        recyclerView11.setAdapter(adapter);

        CLASSIFICATION_SEX = new String[]{
                "Unwanted kissing, fondling, or touching of genitalia and buttocks", "Female Genital Mutilation (FGM)",
                "Attempted  rape", "Rape"};

        arrayList1 = new ArrayList<>();
        arrayList1 = Arrays.asList(CLASSIFICATION_SEX);

//        for(int i=0;i<arrayList1.size();i++) {
        adapter2 = new RecyclerViewAdapter1(context1, arrayList1);

//        };

        recyclerView12.setAdapter(adapter2);

//        //------------------------------Classification FM------------------------------//
        RecyclerView recyclerView13 = (RecyclerView) v.findViewById(R.id.recycler_view13);
        recyclerView13.setHasFixedSize(true);
        recyclerView13.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager13 = new LinearLayoutManager(context);
        recyclerView13.setLayoutManager(linearLayoutManager13);
        arrayList = new ArrayList<>();
        arrayList = Arrays.asList(CLASSIFICATION_FM);

        adapter = new RecyclerViewAdapter(context, arrayList);
        recyclerView13.setAdapter(adapter);


        dropdown_age.setAdapter(age_adapter);
//        dropdown_type.setAdapter(type_adapter);

        dropdown_gender.setAdapter(gender_adapter);
        dropdown_area.setAdapter(area_adapter);

        return v;
    }
}


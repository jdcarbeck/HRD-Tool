package com.example.hrdtool;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.opencsv.CSVReader;

import java.io.InputStreamReader;

public class HelpFragment extends Fragment {

    private String title;
    private int page;

    public static HelpFragment newInstance(int page, String title){
        HelpFragment helpFragment = new HelpFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        helpFragment.setArguments(args);
        return helpFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle SavedInstaceState) {
        View v = inflater.inflate(R.layout.fragment_help, container, false);

        return v;
    }


}
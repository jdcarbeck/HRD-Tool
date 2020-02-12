package com.example.hrdtool;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

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
        return inflater.inflate(R.layout.fragment_help, container, false);
    }
}

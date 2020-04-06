package com.example.hrdtool;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.TextView;

import com.opencsv.CSVReader;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterSupport extends RecyclerView.Adapter<RecyclerViewAdapterSupport.RecyclerViewHolder>  {

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView text;
        private CheckBox checkBox;


        RecyclerViewHolder(View view) {
            super(view);
            checkBox = (CheckBox) view.findViewById(R.id.rowCheckBox);
            text = (TextView) view.findViewById(R.id.rowTextView);

        }
    }

    private List<String> arrayList;
    private Context context;
    private SparseBooleanArray mSelectedItemsIds;


    public RecyclerViewAdapterSupport(Context context, List<String> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row, viewGroup, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int i) {
        holder.text.setText(arrayList.get(i));
        holder.checkBox.setChecked(mSelectedItemsIds.get(i));

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCheckBox(i, !mSelectedItemsIds.get(i));
                if (context instanceof MainActivity) {
                    ArrayList<String> types = new ArrayList<String>();
                    for (int i = 0; i<=mSelectedItemsIds.size(); i++)
                    {
                        int j;
                        if(mSelectedItemsIds.valueAt(i))
                        {
                            j = mSelectedItemsIds.keyAt(i);
                            types.add(arrayList.get(j));
                        }
                    }
                    try {
                        ((MainActivity)context).getHelp(types);
                    } catch  (Exception e) {
                        e.printStackTrace();
                    }


                }

            }
        });

        holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCheckBox(i, !mSelectedItemsIds.get(i));
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0 );
    }

    /**
     * Remove all checkbox Selection
     **/
    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();

        notifyDataSetChanged();
    }

    /**
     * Check the Checkbox if not checked
     **/
    public void checkCheckBox(int position, boolean value) {
        if (value) {
            mSelectedItemsIds.put(position, true);
        }
        else{
            mSelectedItemsIds.put(position, false);

        };
        notifyDataSetChanged();
    }

    /**
     * Return the selected Checkbox IDs
     **/
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
}

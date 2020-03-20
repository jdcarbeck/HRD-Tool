package com.example.hrdtool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewRadioBtnAdapter extends RecyclerView.Adapter<RecyclerViewRadioBtnAdapter.ViewHolder> {

    class ViewHolder extends RecyclerView.ViewHolder {

        public RadioButton mRadio;
        public TextView mText;

        public ViewHolder(final View inflate) {
            super(inflate);
            mText = (TextView) inflate.findViewById(R.id.row_text_view);
            mRadio = (RadioButton) inflate.findViewById(R.id.row_radio_btn);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSelectedItem = getAdapterPosition();
                    notifyDataSetChanged();
                }
            };
            mText.setOnClickListener(clickListener);
            mRadio.setOnClickListener(clickListener);
        }
    }

    public int mSelectedItem = -1;
    public List<String> mItems;
    private Context mContext;

    public RecyclerViewRadioBtnAdapter(Context context, List<String> items) {
        mContext = context;
        mItems = items;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View view = inflater.inflate(R.layout.row_radio_button, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewRadioBtnAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.mText.setText(mItems.get(i));
        viewHolder.mRadio.setChecked(i == mSelectedItem);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


}




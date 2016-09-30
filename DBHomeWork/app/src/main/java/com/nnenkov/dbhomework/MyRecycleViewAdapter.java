package com.nnenkov.dbhomework;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nik on 09.09.16.
 */

public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.ViewHolder> {


    private ArrayList<Item> mAdapterData;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mNumberTextView, mItemNameTextView;
        int position;


        public void setItemPosition(int position) {
            this.position = position;
        }


        public ViewHolder(View itemView) {
            super(itemView);
            mNumberTextView = (TextView) itemView.findViewById(R.id.numberTextView);
            mItemNameTextView = (TextView) itemView.findViewById(R.id.itemNameTextView);

        }
    }


    public MyRecycleViewAdapter(ArrayList<Item> data) {
        this.mAdapterData = data;

    }

    @Override
    public int getItemCount() {

        if (mAdapterData != null)
            return mAdapterData.size();
        else
            return 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_template, parent, false);

        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (holder != null && !mAdapterData.isEmpty()) {
            holder.mNumberTextView.setText(String.valueOf(mAdapterData.get(position).getId()));
            holder.mItemNameTextView.setText(mAdapterData.get(position).getItemName().toString());
            holder.setItemPosition(position);
        }
    }

    public void setAdapterData(ArrayList<Item> mAdapterData) {
        this.mAdapterData = mAdapterData;
    }

}

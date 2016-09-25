package com.nnenkov.mymusicplayer;

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

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {


    private ArrayList<SongInfo> mAdapterData;
    public static IRecycleViewSelectedElement mListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mNumberTextView, mMainTextTextView,mSmallTextTextView;
        Toolbar mItemToolbar;
        int position;


        public void setItemPosition(int position)
        {
            this.position = position;
        }




        public ViewHolder(View itemView) {
            super(itemView);
            mNumberTextView = (TextView) itemView.findViewById(R.id.numberTextView);
            mMainTextTextView = (TextView) itemView.findViewById(R.id.mainTextTextView);
            mSmallTextTextView = (TextView) itemView.findViewById(R.id.smallTextTtextView);
           // mItemToolbar = (Toolbar) itemView.findViewById(R.id.itemToolbar);


            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelected(position);
                }
            });
        }
    }


    public RecycleViewAdapter(ArrayList<SongInfo> data, IRecycleViewSelectedElement listener) {
        this.mAdapterData = data;
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        return mAdapterData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_template, parent, false);

        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (holder != null) {
            holder.mNumberTextView.setText(String.valueOf(position + 1 ));
            holder.mMainTextTextView.setText(mAdapterData.get(position).getsTitle().toString());
            holder.mSmallTextTextView.setText(mAdapterData.get(position).getsSinger().toString());
            //holder.mItemToolbar.setEnabled(TRUE);
            //holder.mItemToolbar.setTitle("dddddddd");
            holder.setItemPosition(position);
        }
    }
}

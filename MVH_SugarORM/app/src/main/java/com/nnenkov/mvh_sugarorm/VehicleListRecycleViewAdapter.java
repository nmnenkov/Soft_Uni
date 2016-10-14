package com.nnenkov.mvh_sugarorm;

import android.app.Application;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.nnenkov.mvh_sugarorm.model.MVHVehicle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nik on 05.10.16.
 */

public class VehicleListRecycleViewAdapter extends RecyclerView.Adapter<VehicleListRecycleViewAdapter.VehicleViewHolder> {
//    private static final Context mCtx = this;

    private List<MVHVehicle> mAdapterData;
    public static IVehicleListRecycleViewSelectedElement mListener;


    public void removeItem(int position) {

        mAdapterData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mAdapterData.size());
    }


    public void refreshData(List<MVHVehicle> data){
        this.mAdapterData = data;

    }
    public static class VehicleViewHolder extends RecyclerView.ViewHolder {

        ImageView mVehicleTypeImageView;
        TextView mNickNameTextView,mModelTextView;

        Toolbar mItemToolbar;
        int position;


        public void setItemPosition(int position)
        {
            this.position = position;
        }




        public VehicleViewHolder(View itemView) {
            super(itemView);
            mNickNameTextView = (TextView) itemView.findViewById(R.id.nickNameRVTextView);
            mModelTextView = (TextView) itemView.findViewById(R.id.modelTextView);
            mVehicleTypeImageView = (ImageView)itemView.findViewById(R.id.vehicleTypeImageView);
           // mItemToolbar = (Toolbar) itemView.findViewById(R.id.itemToolbar);


            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    mListener.onVehicleSelected(position);
                }
            });
        }
    }


    public VehicleListRecycleViewAdapter(List<MVHVehicle> data, IVehicleListRecycleViewSelectedElement listener) {
        this.mAdapterData = data;
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        return mAdapterData.size();
    }

    @Override
    public VehicleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_vehicles_recyclerview_item, parent, false);

        VehicleViewHolder vh = new VehicleViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(VehicleViewHolder holder, int position) {

        if (holder != null) {
            holder.mNickNameTextView.setText(mAdapterData.get(position).getNickname().toString());
            holder.mModelTextView.setText(mAdapterData.get(position).getvModel().toString());
            int resID = holder.mVehicleTypeImageView.getContext().getResources().getIdentifier(mAdapterData.get(position).getvType().getIcoName().toString() , "drawable", "com.nnenkov.mvh_sugarorm");
            //int resID = holder.mVehicleTypeImageView.getContext().getResources().getIdentifier("motorbike_icon" , "drawable", "com.nnenkov.mvh_sugarorm");
            holder.mVehicleTypeImageView.setImageResource(resID);
            //holder.mItemToolbar.setEnabled(TRUE);
            //holder.mItemToolbar.setTitle("dddddddd");
            holder.setItemPosition(position);
        }
    }
}

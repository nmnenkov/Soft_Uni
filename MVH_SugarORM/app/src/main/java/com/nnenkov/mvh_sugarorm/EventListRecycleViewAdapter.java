package com.nnenkov.mvh_sugarorm;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nnenkov.mvh_sugarorm.model.MVHEvent;
import com.nnenkov.mvh_sugarorm.model.MVHVehicle;

import java.util.List;

/**
 * Created by nik on 09.10.16.
 */

public class EventListRecycleViewAdapter extends RecyclerView.Adapter<EventListRecycleViewAdapter.EventsViewHolder>  {
//    private static final Context mCtx = this;

    GeneralFunctions gf = new GeneralFunctions();
    private static int expandedPosition = -1;
    private static int expandedPositionPrev = -1;
    private List<MVHEvent> mAdapterData;

    public static IEventListRecycleViewSelectedElement mListener;


    public void removeItem(int position) {

        mAdapterData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mAdapterData.size());
    }


    public void refreshData(List<MVHEvent> data) {
        this.mAdapterData = data;

    }

    public class EventsViewHolder extends RecyclerView.ViewHolder {

        ImageView mEventTypeImageView;
        TextView mVehicleNickNameTextView, mEventPriceTextView, mEventDateTextView, mPassedDistanceTextView;
        LinearLayout mExtendLinearLayout;

        Toolbar mItemToolbar;
        int position;


        public void setItemPosition(int position) {
            this.position = position;
        }


        public EventsViewHolder(View itemView) {
            super(itemView);
            mEventDateTextView = (TextView) itemView.findViewById(R.id.eventDateTextView);
            mVehicleNickNameTextView = (TextView) itemView.findViewById(R.id.vehicleNickName);
            mEventPriceTextView = (TextView) itemView.findViewById(R.id.eventPrice);
            mEventTypeImageView = (ImageView) itemView.findViewById(R.id.eventTypeImageView);
            mExtendLinearLayout = (LinearLayout) itemView.findViewById(R.id.extendLinearLayout);
            mPassedDistanceTextView = (TextView)itemView.findViewById(R.id.passedDistanceRVITextView);
            // mItemToolbar = (Toolbar) itemView.findViewById(R.id.itemToolbar);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //EventsViewHolder holder = (EventsViewHolder) view.getTag();
                    // Check for an expanded view, collapse if you find one
                    if (expandedPosition >= 0) {
                        if (expandedPositionPrev == expandedPosition) {
                            expandedPositionPrev=-2;
                        }else {
                            expandedPositionPrev = expandedPosition;
                        }
                        notifyItemChanged(expandedPositionPrev);
                    }
                    // Set the current position to "expanded"
                    expandedPosition = getAdapterPosition();
                    //expandedPosition = holder.getPosition();
                    notifyItemChanged(expandedPosition);
                    mListener.onEventSelected(position);
                    Log.d("AdapteOnClick---","Click expandedPosition"+expandedPosition + " prev ");
                }
            });
        }

    }


    public EventListRecycleViewAdapter(List<MVHEvent> data, IEventListRecycleViewSelectedElement listener) {
        this.mAdapterData = data;
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        return mAdapterData.size();
    }

    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_events_recyclerview_item, parent, false);

        EventsViewHolder vh = new EventsViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(EventListRecycleViewAdapter.EventsViewHolder holder, int position) {

        if (holder != null) {
            int resID = holder.mEventTypeImageView.getContext().getResources().getIdentifier(mAdapterData.get(position).getEventType().getEventIcon().toString(), "drawable", "com.nnenkov.mvh_sugarorm");
            //int resID = holder.mVehicleTypeImageView.getContext().getResources().getIdentifier("motorbike_icon" , "drawable", "com.nnenkov.mvh_sugarorm");
            holder.mEventTypeImageView.setImageResource(resID);
            holder.mEventDateTextView.setText(gf.convertDateToString(mAdapterData.get(position).getEventDate(), ""));
            holder.mVehicleNickNameTextView.setText(mAdapterData.get(position).getVehicle().getNickname().toString());
            holder.mEventPriceTextView.setText(mAdapterData.get(position).getPrice().toString()+" EU");
            holder.mPassedDistanceTextView.setText(mAdapterData.get(position).getPassedDistance().toString()+ " km");

            if (position == expandedPosition && expandedPosition >-1 && expandedPosition != expandedPositionPrev) {
                holder.mExtendLinearLayout.setVisibility(View.VISIBLE);
            } else {
                holder.mExtendLinearLayout.setVisibility(View.GONE);
            }
            holder.setItemPosition(position);
        }
    }

 /*   @Override
    public void onClick(View v) {
        EventsViewHolder holder = (EventsViewHolder) view.getTag();
        String theString = mDataset.get(holder.getPosition());

        // Check for an expanded view, collapse if you find one
        if (expandedPosition >= 0) {
            int prev = expandedPosition;
            notifyItemChanged(prev);
        }
        // Set the current position to "expanded"
        expandedPosition = holder.getPosition();
        notifyItemChanged(expandedPosition);

        Toast.makeText(mContext, "Clicked: "+theString, Toast.LENGTH_SHORT).show();
    }*/
}

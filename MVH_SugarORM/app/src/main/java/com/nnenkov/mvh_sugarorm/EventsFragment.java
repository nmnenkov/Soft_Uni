package com.nnenkov.mvh_sugarorm;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.common.base.MoreObjects;
import com.nnenkov.mvh_sugarorm.model.MVHEvent;
import com.nnenkov.mvh_sugarorm.model.MVHVehicle;

import static android.app.Activity.RESULT_OK;


/**
 * Created by nik on 09.10.16.
 */

public class EventsFragment extends Fragment implements IEventListRecycleViewSelectedElement {

    public static final Integer ADD_EVENT = 101;
    public static final Integer EDIT_EVENT = 102;

    MVHApplication mMVHApp;
    RecyclerView mEventsRecyclerView;
    EventListRecycleViewAdapter mEventsListRecyclerViewAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private Paint p = new Paint();

    public EventsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMVHApp = ((MVHApplication) getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.events_fragment_layout, container, false);
        mEventsRecyclerView = (RecyclerView) view.findViewById(R.id.eventsRecyclerView);
        mEventsRecyclerView.setHasFixedSize(true);
        mEventsRecyclerView.setEnabled(false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mEventsRecyclerView.setLayoutManager(mLayoutManager);
        mEventsListRecyclerViewAdapter= new EventListRecycleViewAdapter(mMVHApp.mMVHEvents, this);

        mEventsRecyclerView.setAdapter(mEventsListRecyclerViewAdapter);
        initSwipe();


        return view;
    }


    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT) {
                    deleteConfirmDialog(position);
                } else {
                    Intent intent = new Intent(getActivity(), AddEditEvent.class);
                    intent.putExtra("MVHEventPoss", position);
                    startActivityForResult(intent, EDIT_EVENT);



/*                     removeView(viewHolder.itemView.getRootView());
                    edit_position = position;
                    alertDialog.setTitle("Edit Country");
                    et_country.setText(countries.get(position));
                    alertDialog.show();*/
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit_white);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(icon, null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mEventsRecyclerView);
    }


    private void deleteConfirmDialog(final Integer pos) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // set title
        alertDialogBuilder.setTitle("Delete Vehicle");

        // set dialog message
        alertDialogBuilder
                .setMessage("Do you really want to delete [" + mMVHApp.mMVHEvents.get(pos).getEventType().toString() + "]!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        if (((MVHEvent) mMVHApp.mMVHEvents.get(pos)).delete()) {
                            mEventsListRecyclerViewAdapter.removeItem(pos);
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        mEventsListRecyclerViewAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    @Override
     public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to

        if ((requestCode == ADD_EVENT || requestCode == EDIT_EVENT) && resultCode == RESULT_OK) {
            // Add or Edit Event was successful
            //MVHEvent t = ((MVHEvent) intent.getParcelableExtra("MVHEvent"));
            //if (intent.hasExtra("mMVHEventPoss")) {
                //  if (((MVHVehicle) intent.getParcelableExtra("MVHVehicle")).save() > 0)
                refreshData();
            //}
        }
        refreshData();
    }

    private void refreshData() {
        //mVehiclesList = mMVHApp.mMVHVehicles;
        mMVHApp.mMVHEvents = MVHEvent.listAll(MVHEvent.class);
        mEventsListRecyclerViewAdapter.refreshData(mMVHApp.mMVHEvents);
        mEventsListRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onEventSelected(int position) {
        //Toast.makeText(getActivity(),"Event Selected" + String.valueOf(position), Toast.LENGTH_SHORT).show();
    }


}

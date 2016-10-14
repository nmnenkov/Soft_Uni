package com.nnenkov.mvh_sugarorm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.nnenkov.mvh_sugarorm.model.MVHVehicle;


import java.util.ArrayList;
import java.util.List;

import static java.util.logging.Logger.global;

/**
 * Created by nik on 02.10.16.
 */

public class ManageVehicles extends AppCompatActivity implements View.OnClickListener, IVehicleListRecycleViewSelectedElement {

    MVHApplication mMVHApp;


    public static final Integer ADD_VEHICLE = 1;
    public static final Integer EDIT_VEHICLE = 2;
    Context mCtx = this;
    FloatingActionButton addVehicleFab;

    RecyclerView mVehiclesListRecyclerView;
    VehicleListRecycleViewAdapter mVehiclesListRecyclerViewAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    //List<MVHVehicle> mVehiclesList;


    private Paint p = new Paint();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMVHApp = ((MVHApplication) getApplicationContext());
        setContentView(R.layout.manage_vehicles_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Manage Vehicles");
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        //mVehiclesList = mMVHApp.mMVHVehicles;

        addVehicleFab = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_add_vehicle);
        addVehicleFab.setOnClickListener(this);

        mVehiclesListRecyclerView = (RecyclerView) findViewById(R.id.vehiclesListRecyclerView);
        mVehiclesListRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mVehiclesListRecyclerView.setLayoutManager(mLayoutManager);
        initSwipe();

/*        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
//                super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
            }


            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                Toast.makeText(mCtx," swipeDir - "+ swipeDir,Toast.LENGTH_SHORT).show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

        itemTouchHelper.attachToRecyclerView(mVehiclesListRecyclerView);*/

        mVehiclesListRecyclerViewAdapter = new VehicleListRecycleViewAdapter(mMVHApp.mMVHVehicles, this);

        mVehiclesListRecyclerView.setAdapter(mVehiclesListRecyclerViewAdapter);

        //RecycleViewCustomDecoration itemCustomDecoration = new RecycleViewCustomDecoration();
        //mRecyclerView.addItemDecoration(itemCustomDecoration);


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
                    Intent intent = new Intent(mCtx, AddEditVehicle.class);
                    intent.putExtra("MVHVehiclePoss",position);
                    startActivityForResult(intent, EDIT_VEHICLE);

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
        itemTouchHelper.attachToRecyclerView(mVehiclesListRecyclerView);
    }

/*    private void removeView(View view) {
        if (view != null)
            if (view.getParent() != null) {
                ((ViewGroup) view.getParent()).removeView(view);
            }
    }*/

    @Override
    protected void onResume() {
        refreshData();
        super.onPostResume();
    }


    private void refreshData() {
        //mVehiclesList = mMVHApp.mMVHVehicles;
        mMVHApp.mMVHVehicles = MVHVehicle.listAll(MVHVehicle.class);
        mVehiclesListRecyclerViewAdapter.refreshData(mMVHApp.mMVHVehicles);
        mVehiclesListRecyclerViewAdapter.notifyDataSetChanged();
    }
/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_item, menu);
        return true;
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //super.onActivityResult(requestCode, resultCode, data);
        // Check which request we're responding to

        if ((requestCode == ADD_VEHICLE || requestCode == EDIT_VEHICLE) && resultCode == RESULT_OK) {
            // Add or Edit Vehicle was successful
            MVHVehicle t = ((MVHVehicle) intent.getParcelableExtra("MVHVehicle"));
            if (intent.hasExtra("MVHVehicle")) {
              //  if (((MVHVehicle) intent.getParcelableExtra("MVHVehicle")).save() > 0)
                    refreshData();
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.material_design_floating_action_menu_add_vehicle) {
            Intent intent = new Intent(this, AddEditVehicle.class);
            startActivityForResult(intent, ADD_VEHICLE);
        }


    }

    @Override
    public void onVehicleSelected(int position) {
        Toast.makeText(this, "Vehicle selected", Toast.LENGTH_SHORT).show();
    }


    private void deleteConfirmDialog(final Integer pos) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);

        // set title
        alertDialogBuilder.setTitle("Delete Vehicle");

        // set dialog message
        alertDialogBuilder
                .setMessage("Do you really want to delete [" + mMVHApp.mMVHVehicles.get(pos).getNickname().toString() + "]!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        if (((MVHVehicle) mMVHApp.mMVHVehicles.get(pos)).delete()) {
                            mVehiclesListRecyclerViewAdapter.removeItem(pos);
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        mVehiclesListRecyclerViewAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


}

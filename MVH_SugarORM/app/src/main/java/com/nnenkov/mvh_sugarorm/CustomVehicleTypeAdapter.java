package com.nnenkov.mvh_sugarorm;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nnenkov.mvh_sugarorm.model.MVHVehicleTypes;

import java.util.ArrayList;

/**
 * Created by nik on 13.10.16.
 */

public class CustomVehicleTypeAdapter extends ArrayAdapter<MVHVehicleTypes> {

    private Activity activity;
    private ArrayList data;
    public Resources res;
    MVHVehicleTypes tempValues=null;
    LayoutInflater inflater;

    /*************  CustomAdapter Constructor *****************/
    public CustomVehicleTypeAdapter(
            Activity activitySpinner,
            int textViewResourceId,
            ArrayList objects,
            Resources resLocal)
     {
        super(activitySpinner, textViewResourceId, objects);

        /********** Take passed values **********/
        activity = activitySpinner;
        data     = objects;
        res      = resLocal;

        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

      }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This function called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        /********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.vehicle_type_spinner_item, parent, false);

        /***** Get each Model object from Arraylist ********/
        tempValues = null;
        tempValues = (MVHVehicleTypes) data.get(position);

        TextView mVehicleTypeTextView        = (TextView)row.findViewById(R.id.vehicleTypeTextView);
        ImageView mVehicleTypeImageView = (ImageView)row.findViewById(R.id.vehicleTypeImageView);

        if(position==-1){

            // Default selected Spinner item
            mVehicleTypeTextView.setText("Please select type");
            //mEventTypeImageView.
        }
        else
        {
            // Set values for spinner each row
            mVehicleTypeTextView.setText(tempValues.getTypeName().toString());
            mVehicleTypeImageView.setImageResource(res.getIdentifier
                                         ("com.nnenkov.mvh_sugarorm:drawable/"
                                          + tempValues.getIcoName(),null,null));

        }

        return row;
      }
 }
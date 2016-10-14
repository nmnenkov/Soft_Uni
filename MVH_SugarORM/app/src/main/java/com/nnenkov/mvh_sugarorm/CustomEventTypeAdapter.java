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

import com.nnenkov.mvh_sugarorm.model.MVHEventTypes;

import java.util.ArrayList;

/**
 * Created by nik on 13.10.16.
 */

public class CustomEventTypeAdapter extends ArrayAdapter<MVHEventTypes> {

    private Activity activity;
    private ArrayList data;
    public Resources res;
    MVHEventTypes tempValues=null;
    LayoutInflater inflater;

    /*************  CustomAdapter Constructor *****************/
    public CustomEventTypeAdapter(
            Activity activitySpinner,
            int textViewResourceId,
            ArrayList objects,
            Resources resLocal)
     {
        super(activitySpinner, textViewResourceId, objects);

        /********** Take passed values **********/
        activity = activitySpinner;
        data = objects;
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
        View row = inflater.inflate(R.layout.event_type_spinner_item, parent, false);

        /***** Get each Model object from Arraylist ********/
        tempValues = null;
        tempValues = (MVHEventTypes) data.get(position);

        TextView mEventTypeTextView        = (TextView)row.findViewById(R.id.eventTypeTextView);
        ImageView mEventTypeImageView = (ImageView)row.findViewById(R.id.eventTypeImageView);

        if(position==-1){

            // Default selected Spinner item
            mEventTypeTextView.setText("Please select type");
            //mEventTypeImageView.
        }
        else
        {
            // Set values for spinner each row
            mEventTypeTextView.setText(tempValues.getEventName().toString());
            mEventTypeImageView.setImageResource(res.getIdentifier
                                         ("com.nnenkov.mvh_sugarorm:drawable/"
                                          + tempValues.getEventIcon(),null,null));

        }

        return row;
      }



 }
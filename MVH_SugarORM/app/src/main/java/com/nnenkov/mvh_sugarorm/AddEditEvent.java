package com.nnenkov.mvh_sugarorm;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.nnenkov.mvh_sugarorm.model.MVHEvent;
import com.nnenkov.mvh_sugarorm.model.MVHEventTypes;
import com.nnenkov.mvh_sugarorm.model.MVHPart;
import com.nnenkov.mvh_sugarorm.model.MVHVehicle;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by nik on 11.10.16.
 */

public class AddEditEvent extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG  = AddEditEvent.class.getName();
    public final static Integer TAKE_PHOTO_CODE = 3001;
    MVHApplication mMVHApp;
    Context mCtx = this;
    Activity addEditEventActivity;

    GeneralFunctions gf  = new GeneralFunctions();
    MVHEvent mMVHEvent = null;
    Integer mMVHEventPoss = -1;

    GetVehiclesTask mGetVehiclesTask;
    GetEventTypesTask mGetEventTypesTask;
    GetEventPartTask mGetEventPartTask;

    Spinner mEventVehicleSpinner;
    EditText mPassedDistanceEditText;
    EditText mEventDateEditText;
    EditText mEventGPSLocationLatitudeEditText;
    EditText mEventGPSLocationLongitudeEditText;
    EditText mEventPhotosIdEditText;
    Button mTakePicButton;
    ImageView mEventImageView;
    Uri outputFileUri = null;
    Spinner mEventTypeSpinner;
    EditText mAmountEditText;
    EditText mPriceEditText;
    Spinner mPartSpinner;
    EditText mNotesEditText;

    Long mETypeId = (long)-1;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMVHApp = ((MVHApplication) getApplicationContext());
        setContentView(R.layout.add_edit_event_layout);
        addEditEventActivity = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Add Event");
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        mEventVehicleSpinner = (Spinner)findViewById(R.id.eventVehicleSpinner);
        mEventTypeSpinner = (Spinner) findViewById(R.id.eventTypeSpinner);
        mPassedDistanceEditText = (EditText) findViewById(R.id.passedDistanceEditText);
        mEventDateEditText = (EditText) findViewById(R.id.eventDateEditText);
        mEventDateEditText.setOnClickListener(this);
        mEventGPSLocationLatitudeEditText = (EditText) findViewById(R.id.eventGPSLocationLatitudeEditText);
        mEventGPSLocationLongitudeEditText = (EditText) findViewById(R.id.eventGPSLocationLongitudeEditText);
        mEventPhotosIdEditText = (EditText) findViewById(R.id.eventPhotoIdEditText);
        mTakePicButton = (Button) findViewById(R.id.takePicButton);
        mTakePicButton.setOnClickListener(this);
        mEventImageView = (ImageView) findViewById(R.id.eventImageView);
        mEventImageView.setOnClickListener(this);
        mAmountEditText = (EditText) findViewById(R.id.amountEditText);
        mPriceEditText = (EditText) findViewById(R.id.priceEditText);
        mPartSpinner = (Spinner)findViewById(R.id.partSpinner);
        mNotesEditText = (EditText) findViewById(R.id.notesEditText);


        Intent intent = getIntent();
        if (intent.hasExtra("MVHEventPoss")) {
            mMVHEventPoss = intent.getIntExtra("MVHEventPoss", -1);
            mMVHEvent = mMVHApp.mMVHEvents.get(mMVHEventPoss);
            setEditValues(mMVHEvent);
            toolbar.setTitle("Edit Event");
        } else if (intent.hasExtra("EventType")){
            mETypeId = intent.getLongExtra("EventType",(long)-1);
            if ( mETypeId >= 0){
                GetEventTypesTask mGetEventTypesTask = new GetEventTypesTask();
                mGetEventTypesTask.execute();
            }

        }

        mGetVehiclesTask = new GetVehiclesTask();
        mGetVehiclesTask.execute();

        mGetEventTypesTask = new GetEventTypesTask();
        mGetEventTypesTask.execute();

        mGetEventPartTask = new GetEventPartTask();
        mGetEventPartTask.execute();
    }


    private void setEditValues(MVHEvent mEvent) {
        if (mEvent == null) return;

        mPassedDistanceEditText.setText(mEvent.getPassedDistance().toString());
        mEventDateEditText.setText(gf.convertDateToString(mEvent.getEventDate(),""));
        mEventGPSLocationLatitudeEditText.setText(mEvent.getEventGPSLocationLatitude().toString());
        mEventGPSLocationLongitudeEditText.setText(mEvent.getEventGPSLocationLongitude().toString());
        mEventPhotosIdEditText.setText(mEvent.getEventPhotosId().toString());
        mAmountEditText.setText(mEvent.getAmount().toString());
        mPriceEditText.setText(mEvent.getPrice().toString());
        mNotesEditText.setText(mEvent.getNotes().toString());
        if (mEventPhotosIdEditText.getText().length()>0) {

            File image = new File(mEventPhotosIdEditText.getText().toString());

            if(image.exists()) {
                Uri imageFileUri = Uri.fromFile(image);
                mEventPhotosIdEditText.setText(imageFileUri.getEncodedPath().toString());
                //Picasso.with(mEventImageView.getContext()).load(imageFileUri).fit().centerCrop().into(mEventImageView);
                mEventImageView.setImageURI(imageFileUri);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_item_menu, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.vehicleBrandSpinner) {

        } else if (view.getId() == R.id.eventDateEditText) {
            Calendar pdCalendar = Calendar.getInstance();
            try {
                pdCalendar.setTime(gf.convertStringToDate(mEventDateEditText.getText().toString(), ""));
            } catch (ParseException e) {
                pdCalendar.setTime(new Date());
            }
            DatePickerDialog mEventDateDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    mEventDateEditText.setText(gf.convertDateToString(newDate.getTime(), ""));
                }

            }, pdCalendar.get(Calendar.YEAR), pdCalendar.get(Calendar.MONTH), pdCalendar.get(Calendar.DAY_OF_MONTH));
            mEventDateDatePickerDialog.show();
        } else if (view.getId() == R.id.takePicButton){


            if (!checkCameraPermission()) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            }

            if (!checkStoragePermission()) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            }

            if (checkCameraPermission() && checkStoragePermission()) {

                outputFileUri = getImageFileUri();

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

                startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
            }
        } else if (view.getId() == R.id.eventImageView) {

            Intent viewImageIntent = new Intent(this,EventViewPicture.class);
            viewImageIntent.putExtra("imagePath",mEventPhotosIdEditText.getText().toString());
            startActivity(viewImageIntent);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.d("MVHEventTakePhoto", "Pic saved");

            //mEventImageView
            //File savedFile;
            if(outputFileUri != null) {
                //savedFile = new File(outputFileUri.getPath());
//                ThumbPictureConverter thumbPic = new ThumbPictureConverter(savedFile);
//                mImageView.setImageURI(Uri.fromFile(thumbPic.getThumbFile()));
                Picasso.with(this).load(outputFileUri).fit().centerCrop().into(mEventImageView);
                if (mEventPhotosIdEditText != null) {
                    mEventPhotosIdEditText.setText(outputFileUri.getEncodedPath().toString());
                }
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.save_item_btn) {

            if (validateFormData()) {
                if (mMVHEvent.save() > 0) {
                    Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result", RESULT_OK);
                    if (mMVHEventPoss > -1){
                        returnIntent.putExtra("MVHEventPos", mMVHEventPoss);
                        //returnIntent.putExtra("", EventsFragment.EDIT_EVENT);
                    } else{
                        //returnIntent.putExtra("", EventsFragment.ADD_EVENT);
                    }
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show();
                }

            }
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean validateFormData() {
        Date mEventDate = null;


        if (mEventTypeSpinner.getSelectedItemPosition() == -1) {
            Toast.makeText(this, "Please Select Event Type", Toast.LENGTH_SHORT).show();
            mEventTypeSpinner.showContextMenu();
            return false;
        }
        if (mPassedDistanceEditText.getText().length() == 0) {
            Toast.makeText(this, "Please enter Vehicle Passed Distance", Toast.LENGTH_SHORT).show();
            mPassedDistanceEditText.requestFocus();
            return false;
        }

        try {
            mEventDate = gf.convertStringToDate(mEventDateEditText.getText().toString(), "");
        } catch (ParseException e) {
            Toast.makeText(this, "Please enter correct event date", Toast.LENGTH_SHORT).show();
            mEventDateEditText.requestFocus();
            return false;
        }


/*            try {
                mPriceEditText = gf.convertStringToInt(mPriceEditText.getText().toString());
            } catch (NumberFormatException nfe) {
                Toast.makeText(this, "Please enter correct Fuel Tank 2 Volume", Toast.LENGTH_SHORT).show();
                mPriceEditText.requestFocus();
                return false;
            }*/

        // At this point we can assume that all fields are filed properly

        if (mMVHEvent == null) {
            mMVHEvent = new MVHEvent(
                    ((MVHVehicle) mEventVehicleSpinner.getSelectedItem()),
                    gf.convertStringToInt(mPassedDistanceEditText.getText().toString()),
                    mEventDate,
                    mEventGPSLocationLatitudeEditText.getText().toString(),
                    mEventGPSLocationLongitudeEditText.getText().toString(),
                    mEventPhotosIdEditText.getText().toString(),
                    ((MVHEventTypes) mEventTypeSpinner.getSelectedItem()),
                    mAmountEditText.getText().toString(),
                    mPriceEditText.getText().toString(),
                    ((MVHPart) mPartSpinner.getSelectedItem()),
                    mNotesEditText.getText().toString());
        } else {
            mMVHEvent.setVehicle((MVHVehicle) mEventVehicleSpinner.getSelectedItem());
            mMVHEvent.setPassedDistance(gf.convertStringToInt(mPassedDistanceEditText.getText().toString()));
            mMVHEvent.setEventDate(mEventDate);
            mMVHEvent.setEventGPSLocationLatitude(mEventGPSLocationLatitudeEditText.getText().toString());
            mMVHEvent.setEventGPSLocationLongitude(mEventGPSLocationLongitudeEditText.getText().toString());
            mMVHEvent.setEventPhotosId( mEventPhotosIdEditText.getText().toString());
            mMVHEvent.setEventType((MVHEventTypes) mEventTypeSpinner.getSelectedItem());
            mMVHEvent.setAmount(mAmountEditText.getText().toString());
            mMVHEvent.setPrice(mPriceEditText.getText().toString());
            mMVHEvent.setPart((MVHPart) mPartSpinner.getSelectedItem());
            mMVHEvent.setNotes(mNotesEditText.getText().toString());

        }
        return true;
    }







    public class GetVehiclesTask extends AsyncTask<Void, Void, Void> {

        private List<MVHVehicle> mvhVehicles;

        @Override
        protected Void doInBackground(Void... arg0) {
            mvhVehicles = MVHVehicle.find(MVHVehicle.class,null,new String[] {});
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            if (mvhVehicles.size() > 0) {
                ArrayAdapter<MVHVehicle> adapter = new ArrayAdapter<>(
                        mCtx, android.R.layout.simple_list_item_1, mvhVehicles);
                mEventVehicleSpinner.setAdapter(adapter);

                if (mMVHEvent != null) {
                    for (int count = 0; count < mEventVehicleSpinner.getAdapter().getCount(); count++) {
                        MVHVehicle mvhVehicle = (MVHVehicle) mEventVehicleSpinner.getAdapter().getItem(count);
                        if (mMVHEvent.getVehicle().getId().equals(mvhVehicle.getId())) {
                            mEventVehicleSpinner.setSelection(count);
                            break;
                        }
                    }
                }
            }
        }
    }

    public class GetEventTypesTask extends AsyncTask<Void, Void, Void> {

        private List<MVHEventTypes> mvhEventTypes;

        @Override
        protected Void doInBackground(Void... arg0) {

            mvhEventTypes = mMVHApp.mMVHEventsTypes;
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            if (mvhEventTypes.size() > 0) {
/*
                ArrayAdapter<MVHEventTypes> adapter = new ArrayAdapter<>(
                        mCtx, android.R.layout.simple_list_item_1, mvhEventTypes);
                mEventTypeSpinner.setAdapter(adapter);
*/
                CustomEventTypeAdapter adapter = new CustomEventTypeAdapter(addEditEventActivity,R.layout.event_type_spinner_item,(ArrayList<MVHEventTypes>) mvhEventTypes,getResources());
                mEventTypeSpinner.setAdapter(adapter);
                if (mMVHEvent != null) {
                    for (int count = 0; count < mEventTypeSpinner.getAdapter().getCount(); count++) {

                        MVHEventTypes mvhEventType = (MVHEventTypes) mEventTypeSpinner.getAdapter().getItem(count);
                        if (mMVHEvent.getEventType().getId().equals(mvhEventType.getId())) {

                            mEventTypeSpinner.setSelection(count);
                            break;
                        }
                    }
                } else if (mETypeId >=0 ) {
                    for (int count = 0; count < mEventTypeSpinner.getAdapter().getCount(); count++) {
                        MVHEventTypes mvhEventType = (MVHEventTypes) mEventTypeSpinner.getAdapter().getItem(count);
                        if (mETypeId.equals(mvhEventType.getId())) {
                            mEventTypeSpinner.setSelection(count);
                            break;
                        }
                    }

                }
            }
        }
    }

    public class GetEventPartTask extends AsyncTask<Void, Void, Void> {

        private List<MVHPart> mvhParts;

        @Override
        protected Void doInBackground(Void... arg0) {

            mvhParts = mMVHApp.mMVHParts;
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            if (mvhParts.size() > 0) {
                ArrayAdapter<MVHPart> adapter = new ArrayAdapter<>(
                        mCtx, android.R.layout.simple_list_item_1, mvhParts);
                mPartSpinner.setAdapter(adapter);
                if (mMVHEvent != null)
                    if (mMVHEvent.getPart() != null) {
                        for (int count = 0; count < mPartSpinner.getAdapter().getCount(); count++) {
                            MVHPart mvhPart = (MVHPart) mPartSpinner.getAdapter().getItem(count);
                            if (mMVHEvent.getPart().getId().equals(mvhPart.getId())) {
                                mPartSpinner.setSelection(count);
                                break;
                            }
                        }
                }
            }
        }
    }


    public boolean checkCameraPermission() {
        String permission = "android.permission.CAMERA";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public boolean checkStoragePermission() {
        int resWrite = this.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        int resRead = this.checkCallingOrSelfPermission("android.permission.READ_EXTERNAL_STORAGE");

        return (resWrite == PackageManager.PERMISSION_GRANTED) && (resRead == PackageManager.PERMISSION_GRANTED);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private static Uri getImageFileUri(){

        // Create a storage directory for the images
        // To be safe(er), you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this

        File imagePath = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MVH");
        Log.d(TAG,"Find "+imagePath.getAbsolutePath());
        if (! imagePath.exists()){
            if (! imagePath.mkdirs()){
                Log.d("CameraTestIntent", "failed to create directory");
                return null;
            }else{
                Log.d(TAG,"create new Tux folder");
            }
        }

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File image = new File(imagePath,"MVH_"+ timeStamp + ".jpg");

        if(!image.exists()){
            try {
                image.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        //return image;

        // Create an File Uri
        return Uri.fromFile(image);
    }




    private void galleryAddPic() {
        /**
         * copy current image to Galerry
         */
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(outputFileUri);
        this.sendBroadcast(mediaScanIntent);
    }



}

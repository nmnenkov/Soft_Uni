package com.nnenkov.mvh_sugarorm;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nnenkov.mvh_sugarorm.model.MVHFuelTypes;
import com.nnenkov.mvh_sugarorm.model.MVHVehicle;
import com.nnenkov.mvh_sugarorm.model.MVHVehicleBrands;
import com.nnenkov.mvh_sugarorm.model.MVHVehicleModels;
import com.nnenkov.mvh_sugarorm.model.MVHVehicleTypes;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.DataFormatException;

/**
 * Created by nik on 02.10.16.
 */

public class AddEditVehicle extends AppCompatActivity implements View.OnClickListener {

    MVHApplication mMVHApp;
    Context mCtx = this;
    Activity addEditVehicleActivity = this;

    GeneralFunctions gf;
    MVHVehicle mMVHVehicle = null;


    GetVehicleBrandsTask mGetVehicleBrandsTask;
    GetVehicleModelTask mGetVehicleModelTask;
    GetVehicleTypesTask mGetVehicleTypesTask;
    GetFuelTypesTask mGetFuelTypesTask;

    EditText mNicknameEditText;
    EditText mRegNumerEditText;
    Spinner mVehicleTypeSpinner, mVehicleBrandSpinner, mVehicleModelSpinner;
    EditText mEngineEditText;
    EditText mManufactureYearEditText;
    EditText mPurchaseDateEditText;
    EditText mPriceEditText;
    EditText mWarrantyValidityEditText;
    TextView mFuelTank2TextView, mFuelTank2VolumeTextView;
    EditText mFuelTank1VolumeEditText, mFuelTank2VolumeEditText;
    Spinner mFuelTank1FuelSpinner, mFuelTank2FuelSpinner;


    /*
        View mSave_item_menu;
        LinearLayout mSecondFuelTankLinearLayout;
        LinearLayout mSecondFuelTankParentLinearLayout;
        View mSecondFuelTankView;
    */
    SwitchCompat mHasSecondFuelTankSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMVHApp = ((MVHApplication) getApplicationContext());
        gf = new GeneralFunctions();
        setContentView(R.layout.add_edit_vehicle_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Edit Vehicle");
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        mNicknameEditText = (EditText) findViewById(R.id.nicknameEditText);
        mRegNumerEditText = (EditText) findViewById(R.id.regNumerEditText);
        mEngineEditText = (EditText) findViewById(R.id.engineEditText);
        mManufactureYearEditText = (EditText) findViewById(R.id.manufactureYearEditText);
        mPurchaseDateEditText = (EditText) findViewById(R.id.purchaseDateEditText);
        mPurchaseDateEditText.setOnClickListener(this);
        mPriceEditText = (EditText) findViewById(R.id.priceEditText);

        mWarrantyValidityEditText = (EditText) findViewById(R.id.warrantyValidityEditText);
        mWarrantyValidityEditText.setOnClickListener(this);

        mVehicleTypeSpinner = (Spinner) findViewById(R.id.vehicleTypeSpinner);
        mVehicleBrandSpinner = (Spinner) findViewById(R.id.vehicleBrandSpinner);
        mVehicleModelSpinner = (Spinner) findViewById(R.id.vehicleModelSpinner);
        mGetVehicleTypesTask = new GetVehicleTypesTask();
        mGetVehicleTypesTask.execute();

        mVehicleBrandSpinner.setEnabled(false);
        mVehicleModelSpinner.setEnabled(false);

        /*mVehicleBrandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                MVHVehicleBrands selectedBrand = ((MVHVehicleBrands) mVehicleBrandSpinner.getSelectedItem());
                if (selectedBrand != null) {
                    mGetVehicleModelTask = new GetVehicleModelTask();
                    mGetVehicleModelTask.execute(selectedBrand.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        mGetVehicleBrandsTask = new GetVehicleBrandsTask();
        mGetVehicleBrandsTask.execute();
        /*//Inflate the Hidden Layout Information View
        LinearLayout myLayout = (LinearLayout)findViewById(R.id.linearLayout2);
        View hiddenInfo = getLayoutInflater().inflate(R.layout.hidden, myLayout, false);
        myLayout.addView(hiddenInfo);*/
/*
        LayoutInflater inflater = (LayoutInflater)mCtx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSecondFuelTankParentLinearLayout = (LinearLayout) findViewById(R.id.secondFuelTankParenLinearLayout);
        mSecondFuelTankLinearLayout = (LinearLayout) findViewById(R.id.secondFuelTankLinearLayout);
        mSecondFuelTankView = inflater.inflate(R.layout.second_fuel_tank_layout,null);
*/
        mFuelTank1FuelSpinner = (Spinner) findViewById(R.id.fuelTank1FuelSpinner);
        mFuelTank1VolumeEditText = (EditText) findViewById(R.id.fuelTank1VolumeEditText);
        mFuelTank1VolumeEditText = (EditText) findViewById(R.id.fuelTank1VolumeEditText);

        mFuelTank2TextView = (TextView) findViewById(R.id.fuelTank2TextView);
        mFuelTank2TextView.setEnabled(false);
        mFuelTank2VolumeTextView = (TextView) findViewById(R.id.fuelTank2VolumeTextView);
        mFuelTank2VolumeTextView.setEnabled(false);
        mFuelTank2FuelSpinner = (Spinner) findViewById(R.id.fuelTank2FuelSpinner);
        mFuelTank2FuelSpinner.setEnabled(false);
        mFuelTank2VolumeEditText = (EditText) findViewById(R.id.fuelTank2VolumeEditText);
        mFuelTank2VolumeEditText.setEnabled(false);


        mHasSecondFuelTankSwitch = (SwitchCompat) findViewById(R.id.hasSecondFuelTankSwitch);
        mHasSecondFuelTankSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mHasSecondFuelTankSwitch.isChecked()) {
//                    mSecondFuelTankParentLinearLayout.addView(mSecondFuelTankView,1,mSecondFuelTankLinearLayout.getLayoutParams());
                    mFuelTank2TextView.setEnabled(true);
                    mFuelTank2FuelSpinner.setEnabled(true);
                    mFuelTank2FuelSpinner.setVisibility(View.VISIBLE);
                    mFuelTank2VolumeTextView.setEnabled(true);
                    mFuelTank2VolumeEditText.setEnabled(true);
                    mFuelTank2VolumeEditText.setVisibility(View.VISIBLE);
                } else {
//                    mSecondFuelTankParentLinearLayout.removeView(mSecondFuelTankView);
                    mFuelTank2TextView.setEnabled(false);
                    mFuelTank2FuelSpinner.setEnabled(false);
                    mFuelTank2FuelSpinner.setVisibility(View.VISIBLE);
                    mFuelTank2VolumeTextView.setEnabled(false);
                    mFuelTank2VolumeEditText.setEnabled(false);
                    mFuelTank2VolumeEditText.setVisibility(View.VISIBLE);
                }
            }
        });

        mGetFuelTypesTask = new GetFuelTypesTask();
        mGetFuelTypesTask.execute();

        //mGetFuelTypesTask.getStatus()

        Intent intent = getIntent();
        if (intent.hasExtra("MVHVehiclePoss")) {
            mMVHVehicle = mMVHApp.mMVHVehicles.get(intent.getIntExtra("MVHVehiclePoss", 0));
            mGetVehicleModelTask = new GetVehicleModelTask();
            mGetVehicleModelTask.execute(mMVHVehicle.getvBrand().getId());
            new EditSetVehicleInfoTask() {
            }.execute();
        } //else {
        mVehicleBrandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                MVHVehicleBrands selectedBrand = ((MVHVehicleBrands) mVehicleBrandSpinner.getSelectedItem());
                if (selectedBrand != null) {
                    mGetVehicleModelTask = new GetVehicleModelTask();
                    mGetVehicleModelTask.execute(selectedBrand.getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //}
    }


    private void setEditValues(MVHVehicle Vehicle) {
        if (Vehicle == null) return;
        mPurchaseDateEditText.setText(Vehicle.getPurchaseDate().toString());

        mPriceEditText.setText(Vehicle.getPrice().toString());
        mNicknameEditText.setText(Vehicle.getNickname().toString());
        mRegNumerEditText.setText(Vehicle.getRegNumber().toString());

        mEngineEditText.setText(Vehicle.getvEngin().toString());
        mHasSecondFuelTankSwitch.setChecked((Vehicle.getHasSecondaryFuelTank() == 1));
        //((MVHFuelTypes) mFuelTank1FuelSpinner.getSelectedItem()),
        mFuelTank1VolumeEditText.setText(Vehicle.getFuelTank1Volume().toString());

        //((MVHFuelTypes) mFuelTank2FuelSpinner.getSelectedItem()),
        mFuelTank2VolumeEditText.setText(Vehicle.getFuelTank2Volume().toString());
        //1,
        mManufactureYearEditText.setText(Vehicle.getManufactureYear().toString());
        mPurchaseDateEditText.setText(gf.convertDateToString(Vehicle.getPurchaseDate(), ""));
        mPriceEditText.setText(Vehicle.getPrice().toString());
        mWarrantyValidityEditText.setText(gf.convertDateToString(Vehicle.getWarrantyValidityDate(), ""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_item_menu, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.vehicleBrandSpinner) {

        } else if (view.getId() == R.id.purchaseDateEditText) {
            Calendar pdCalendar = Calendar.getInstance();
            try {
                pdCalendar.setTime(gf.convertStringToDate(mPurchaseDateEditText.getText().toString(), ""));
            } catch (ParseException e) {
                pdCalendar.setTime(new Date());
            }
            DatePickerDialog mPurchaseDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    mPurchaseDateEditText.setText(gf.convertDateToString(newDate.getTime(), ""));
                }

            }, pdCalendar.get(Calendar.YEAR), pdCalendar.get(Calendar.MONTH), pdCalendar.get(Calendar.DAY_OF_MONTH));
            mPurchaseDatePickerDialog.show();
        } else if (view.getId() == R.id.warrantyValidityEditText) {
            Calendar wdCalendar = Calendar.getInstance();
            try {
                wdCalendar.setTime(gf.convertStringToDate(mWarrantyValidityEditText.getText().toString(), ""));
            } catch (ParseException e) {
                wdCalendar.setTime(new Date());
            }
            DatePickerDialog mWarrantyValidityPickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    Calendar newDate = Calendar.getInstance();
                    newDate.set(year, monthOfYear, dayOfMonth);
                    mWarrantyValidityEditText.setText(gf.convertDateToString(newDate.getTime(), ""));
                }

            }, wdCalendar.get(Calendar.YEAR), wdCalendar.get(Calendar.MONTH), wdCalendar.get(Calendar.DAY_OF_MONTH));
            mWarrantyValidityPickerDialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.save_item_btn) {

            if (validateFormData()) {
                //if (DBHelper.saveUpdateMVHVehicle(mMVHVehicle) != -1) {


                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", RESULT_OK);
                if (mMVHVehicle.save() > 0) {
                    Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show();
                    //returnIntent.putExtra("MVHVehiclePos", mMVHVehicle);
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
        Date mPurchaseDate = null;
        Date mWarrantyValidity = null;
        Integer mFuelTank1Volume, mFuelTank2Volume;

        if (mNicknameEditText.length() == 0) {
            Toast.makeText(this, "Please enter Vehicle nickname", Toast.LENGTH_SHORT).show();
            mNicknameEditText.requestFocus();
            return false;
        }

        try {
            mPurchaseDate = gf.convertStringToDate(mPurchaseDateEditText.getText().toString(), "");
        } catch (ParseException e) {
            Toast.makeText(this, "Please enter correct date in Purchase Date", Toast.LENGTH_SHORT).show();
            mPurchaseDateEditText.requestFocus();
            return false;
        }

        try {
            mWarrantyValidity = gf.convertStringToDate(mWarrantyValidityEditText.getText().toString(), "");
        } catch (ParseException e) {
            Toast.makeText(this, "Please enter correct date in Warranty Validity Date", Toast.LENGTH_SHORT).show();
            mWarrantyValidityEditText.requestFocus();
            return false;
        }

        try {
            gf.convertStringToDate(mManufactureYearEditText.getText().toString(), "yyyy");
        } catch (ParseException e) {
            Toast.makeText(this, "Please enter correct Manufacture Year", Toast.LENGTH_SHORT).show();
            mManufactureYearEditText.requestFocus();
            return false;
        }

        //mFuelTank1Volume
        try {
            mFuelTank1Volume = gf.convertStringToInt(mFuelTank1VolumeEditText.getText().toString());
        } catch (NumberFormatException nfe) {
            Toast.makeText(this, "Please enter correct Fuel Tank 1 Volume", Toast.LENGTH_SHORT).show();
            mFuelTank1VolumeEditText.requestFocus();
            return false;
        }

        //mFuelTank2Volume
        if (mHasSecondFuelTankSwitch.isChecked()) {
            try {
                mFuelTank2Volume = gf.convertStringToInt(mFuelTank2VolumeEditText.getText().toString());
            } catch (NumberFormatException nfe) {
                Toast.makeText(this, "Please enter correct Fuel Tank 2 Volume", Toast.LENGTH_SHORT).show();
                mFuelTank2VolumeEditText.requestFocus();
                return false;
            }
        } else mFuelTank2Volume=0;

        // At this point we can assume that all fields are filed properly

        if (mMVHVehicle == null) {
            mMVHVehicle = new MVHVehicle(
                    mNicknameEditText.getText().toString(),
                    mRegNumerEditText.getText().toString(),
                    ((MVHVehicleTypes) mVehicleTypeSpinner.getSelectedItem()),
                    ((MVHVehicleBrands) (mVehicleBrandSpinner.getSelectedItem())),
                    ((MVHVehicleModels) (mVehicleModelSpinner.getSelectedItem())),
                    mEngineEditText.getText().toString(),
                    gf.convertBooleanToInt(mHasSecondFuelTankSwitch.isChecked()),
                    ((MVHFuelTypes) mFuelTank1FuelSpinner.getSelectedItem()),
                    mFuelTank1Volume,
                    ((MVHFuelTypes) mFuelTank2FuelSpinner.getSelectedItem()),
                    mFuelTank2Volume,
                    Integer.valueOf(String.valueOf(((MVHFuelTypes) mFuelTank1FuelSpinner.getSelectedItem()).getId())),//Primary fuel
                    mManufactureYearEditText.getText().toString(),
                    mPurchaseDate,
                    mPriceEditText.getText().toString(),
                    mWarrantyValidity);
        } else {
            mMVHVehicle.setNickname(mNicknameEditText.getText().toString());
            mMVHVehicle.setRegNumber(mRegNumerEditText.getText().toString());
            mMVHVehicle.setvType((MVHVehicleTypes) mVehicleTypeSpinner.getSelectedItem());
            mMVHVehicle.setvBrand((MVHVehicleBrands) (mVehicleBrandSpinner.getSelectedItem()));
            mMVHVehicle.setvModel((MVHVehicleModels) (mVehicleModelSpinner.getSelectedItem()));
            mMVHVehicle.setvEngin(mEngineEditText.getText().toString());
            mMVHVehicle.setHasSecondaryFuelTank(gf.convertBooleanToInt(mHasSecondFuelTankSwitch.isChecked()));
            mMVHVehicle.setFuelTank1FuelType((MVHFuelTypes) mFuelTank1FuelSpinner.getSelectedItem());
            mMVHVehicle.setFuelTank1Volume(mFuelTank1Volume);
            mMVHVehicle.setFuelTank2FuelType((MVHFuelTypes) mFuelTank2FuelSpinner.getSelectedItem());
            mMVHVehicle.setFuelTank2Volume(mFuelTank2Volume);
            mMVHVehicle.setPrimaryFuelTypeID(Integer.valueOf(String.valueOf(((MVHFuelTypes) mFuelTank1FuelSpinner.getSelectedItem()).getId())));//Primary fuel
            mMVHVehicle.setManufactureYear(mManufactureYearEditText.getText().toString());
            mMVHVehicle.setPurchaseDate(mPurchaseDate);
            mMVHVehicle.setPrice(mPriceEditText.getText().toString());
            mMVHVehicle.setWarrantyValidityDate(mWarrantyValidity);
        }
        return true;
    }




    public class GetVehicleBrandsTask extends AsyncTask<Void, Void, Void> {

        private List<MVHVehicleBrands> mvhVehicleBrands;

        @Override
        protected Void doInBackground(Void... arg0) {
            mvhVehicleBrands = mMVHApp.mMVHVehicleBrands;
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            if (mvhVehicleBrands.size() > 0) {
                ArrayAdapter<MVHVehicleBrands> adapter = new ArrayAdapter<>(
                        mCtx, android.R.layout.simple_list_item_1, mvhVehicleBrands);
                mVehicleBrandSpinner.setAdapter(adapter);
                mVehicleBrandSpinner.setEnabled(true);

                if (mMVHVehicle != null) {
                    for (int count = 0; count < mVehicleBrandSpinner.getAdapter().getCount(); count++) {
                        MVHVehicleBrands mvhBrand = (MVHVehicleBrands) mVehicleBrandSpinner.getAdapter().getItem(count);
                        if (mMVHVehicle.getvBrand().getId().equals(mvhBrand.getId())) {
                            mVehicleBrandSpinner.setSelection(count);
                            break;
                        }
                    }
                }
            }
        }
    }


    public class GetVehicleModelTask extends AsyncTask<Long, Void, Void> {

        private List<MVHVehicleModels> mvhVehicleModels;

        @Override
        protected Void doInBackground(Long... arg0) {
            Long brandId = arg0[0];
            mvhVehicleModels = MVHVehicleModels.find(MVHVehicleModels.class, "MVH_VEHICLE_BRANDS = ?", new String[]{String.valueOf(brandId)}, null, null, null);
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            if (mvhVehicleModels.size() > 0) {
                ArrayAdapter<MVHVehicleModels> adapter = new ArrayAdapter<>(
                        mCtx, android.R.layout.simple_list_item_1, mvhVehicleModels);
                mVehicleModelSpinner.setAdapter(adapter);
                mVehicleModelSpinner.setEnabled(true);
                if (mMVHVehicle != null) {
                    for (int count = 0; count < mVehicleModelSpinner.getAdapter().getCount(); count++) {
                        MVHVehicleModels mvhModel = (MVHVehicleModels) mVehicleModelSpinner.getAdapter().getItem(count);
                        if (mMVHVehicle.getvModel().getId().equals(mvhModel.getId())) {
                            mVehicleModelSpinner.setSelection(count);
                            break;
                        }
                    }
                }
            }
        }
    }

    public class GetVehicleTypesTask extends AsyncTask<Void, Void, Void> {

        private List<MVHVehicleTypes> mvhVehicleTypes;

        @Override
        protected Void doInBackground(Void... arg0) {

            mvhVehicleTypes = mMVHApp.mMVHVehicleTypes;
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            if (mvhVehicleTypes.size() > 0) {
                /*ArrayAdapter<MVHVehicleTypes> adapter = new ArrayAdapter<>(
                        mCtx, android.R.layout.simple_list_item_1, mvhVehicleTypes);*/
                CustomVehicleTypeAdapter adapter = new CustomVehicleTypeAdapter(addEditVehicleActivity,R.layout.vehicle_type_spinner_item,(ArrayList<MVHVehicleTypes>) mvhVehicleTypes,getResources());
                mVehicleTypeSpinner.setAdapter(adapter);
                if (mMVHVehicle != null)
                    for (int count = 0; count < mVehicleTypeSpinner.getAdapter().getCount(); count++) {
                        if (((MVHVehicleTypes) mVehicleTypeSpinner.getAdapter().getItem(count)).getId() == ((MVHVehicleTypes) mMVHVehicle.getvType()).getId()) {

                            mVehicleTypeSpinner.setSelection(count);
                            break;
                        }
                    }
            }
        }
    }

    public class GetFuelTypesTask extends AsyncTask<Void, Void, Void> {

        private List<MVHFuelTypes> mvhFuelTypes;

        @Override
        protected Void doInBackground(Void... arg0) {

            mvhFuelTypes = mMVHApp.mMVHFuelTypes;
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            if (mvhFuelTypes.size() > 0) {
                ArrayAdapter<MVHFuelTypes> adapter1 = new ArrayAdapter<>(
                        mCtx, android.R.layout.simple_list_item_1, mvhFuelTypes);
                mFuelTank1FuelSpinner.setAdapter(adapter1);
                ArrayAdapter<MVHFuelTypes> adapter2 = new ArrayAdapter<>(
                        mCtx, android.R.layout.simple_list_item_1, mvhFuelTypes);
                mFuelTank2FuelSpinner.setAdapter(adapter2);
                if (mMVHVehicle != null) {
                    if (mMVHVehicle.getFuelTank1FuelType() != null)
                        for (int count = 0; count < mFuelTank1FuelSpinner.getAdapter().getCount(); count++) {
                            if (((MVHFuelTypes) mFuelTank1FuelSpinner.getAdapter().getItem(count)).getId() == ((MVHFuelTypes) mMVHVehicle.getFuelTank1FuelType()).getId()) {

                                mFuelTank1FuelSpinner.setSelection(count);
                                break;
                            }
                        }
                    if (mMVHVehicle.getFuelTank2FuelType() != null)
                        for (int count = 0; count < mFuelTank2FuelSpinner.getAdapter().getCount(); count++) {
                            if (((MVHFuelTypes) mFuelTank2FuelSpinner.getAdapter().getItem(count)).getId() == ((MVHFuelTypes) mMVHVehicle.getFuelTank2FuelType()).getId()) {

                                mFuelTank2FuelSpinner.setSelection(count);
                                break;
                            }
                        }
                }
            }
        }
    }

    private class EditSetVehicleInfoTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {

            if (mGetVehicleBrandsTask != null && mGetVehicleModelTask != null && mGetVehicleTypesTask != null && mGetFuelTypesTask != null)
                while (mGetVehicleBrandsTask.getStatus() != Status.FINISHED && mGetVehicleModelTask.getStatus() != Status.FINISHED && mGetVehicleTypesTask.getStatus() != Status.FINISHED && mGetFuelTypesTask.getStatus() != Status.FINISHED) {
                    // Do nothing
                    try {
                        Thread.currentThread();
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            setEditValues(mMVHVehicle);
        }
    }

}

package com.nnenkov.mvh_sugarorm;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.gesture.GestureOverlayView;
import android.icu.text.RelativeDateTimeFormatter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nnenkov.mvh_sugarorm.model.MVHVehicleModels;
import com.nnenkov.mvh_sugarorm.model.WeatherXML;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by nik on 09.10.16.
 */

public class UsefulInfoFragment extends Fragment implements LocationListener, DownloadResultReceiver.Receiver, View.OnClickListener {

    GeneralFunctions gf = new GeneralFunctions();
    LocationManager locationManager;
    Button mShowOnMapButton;
    TextView mLatitudeTextView, mLongitudeTextView, mTemperatureTextView,mLastUpdatedTextView,mHumidityTextView,mCloudcoverTextView,mWindspeedTextView;
    RelativeLayout mWeatherRelativeLayout;


    private DownloadResultReceiver mReceiver;

    final String url = "http://www.eurometeo.ru/bulgaria/sofia/export/xml/data/";

    public UsefulInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.useful_info_fragment_layout, container, false);

        mLatitudeTextView = (TextView) view.findViewById(R.id.latitudeTextView);
        mLongitudeTextView = (TextView) view.findViewById(R.id.longitudeTextView);
        if (mLatitudeTextView != null) mLatitudeTextView.setText("n/a");
        if (mLongitudeTextView != null) mLongitudeTextView.setText("n/a");
        mWeatherRelativeLayout = (RelativeLayout)view.findViewById(R.id.weatherRelativeLayout);
        mWeatherRelativeLayout.setOnClickListener(this);
        mTemperatureTextView = (TextView) view.findViewById(R.id.temperatureTextView);
        mHumidityTextView = (TextView) view.findViewById(R.id.humidityTextView);
        mCloudcoverTextView = (TextView) view.findViewById(R.id.cloudcoverTextView);
        mWindspeedTextView = (TextView) view.findViewById(R.id.windspeedTextView);
        mLastUpdatedTextView = (TextView) view.findViewById(R.id.lastUpdatedWtextView);
        mShowOnMapButton = (Button) view.findViewById(R.id.showOnMapButton);
        mShowOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:" + mLatitudeTextView.getText().toString() + "," + mLongitudeTextView.getText().toString() + "?z=10&q=petrolstations");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            }
        });

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser();
            mShowOnMapButton.setEnabled(false);
        }
        if (!checkLocationPermissionGPS()) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, this);
        } catch (SecurityException e) {
            Log.d("GPS issue", e.toString());

        }


        /* Starting Download Service */
        Handler handler = new Handler();
        mReceiver = new DownloadResultReceiver(handler);
        mReceiver.setReceiver(this);
        startWeatherService();

        return view;
    }

    public boolean checkLocationPermissionGPS() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = getActivity().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }


    private void startWeatherService(){
        Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), DownloadService.class);

        /* Send optional extras to Download IntentService */
        intent.putExtra("url", url);
        intent.putExtra("receiver", mReceiver);
        intent.putExtra("requestId", 101);

        getActivity().startService(intent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onLocationChanged(Location location) {
        if (mLongitudeTextView != null && mLatitudeTextView != null) {
            if (mShowOnMapButton != null) mShowOnMapButton.setEnabled(true);
            if (mLatitudeTextView != null)
                mLongitudeTextView.setText(String.valueOf(location.getLongitude()));
            if (mLongitudeTextView != null)
                mLatitudeTextView.setText(String.valueOf(location.getLatitude()));
        }
    }

    @Override
    public void onProviderEnabled(String s) {
        if (mLatitudeTextView != null) mLatitudeTextView.setText("n/a");
        if (mLongitudeTextView != null) mLongitudeTextView.setText("n/a");
    }

    @Override
    public void onProviderDisabled(String s) {
        if (mShowOnMapButton != null) mShowOnMapButton.setEnabled(false);
        if (mLatitudeTextView != null) mLatitudeTextView.setText("n/a");
        if (mLongitudeTextView != null) mLongitudeTextView.setText("n/a");

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case DownloadService.STATUS_RUNNING:
                mLastUpdatedTextView.setText(mLastUpdatedTextView.getText().toString()+".");
                //setProgressBarIndeterminateVisibility(true);
                break;
            case DownloadService.STATUS_FINISHED:
                /* Hide progress & extract result from bundle */
                //setProgressBarIndeterminateVisibility(false);

                String result = resultData.getString("result");
                GetUpdateWeatherTask mGetUpdateWeatherTask = new GetUpdateWeatherTask();
                mGetUpdateWeatherTask.execute(result);
                /* Update ListView with result */
                //arrayAdapter = new ArrayAdapter(MyActivity.this, android.R.layout.simple_list_item_2, results);
                //listView.setAdapter(arrayAdapter);

                break;
            case DownloadService.STATUS_ERROR:
                /* Handle the error */
                String error = resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                mTemperatureTextView.setText("n/a");
                mHumidityTextView.setText("n/a");
                mCloudcoverTextView.setText("n/a");
                mWindspeedTextView.setText("n/a");

                break;
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.weatherRelativeLayout) {

            startWeatherService();
        }
    }

    public class GetUpdateWeatherTask extends AsyncTask<String, Void, Void> {

        List<WeatherXML> mWeatherXMLRecords;
        String mXMLData;

        @Override
        protected Void doInBackground(String... arg0) {
            mXMLData = arg0[0];


            if (mXMLData != null) {
                BufferedReader br = new BufferedReader(new StringReader(mXMLData));
                InputSource is = new InputSource(br);

                /************  Parse XML **************/

                WeatherXMLParser parser = new WeatherXMLParser();
                SAXParserFactory factory = SAXParserFactory.newInstance();
                try {
                    SAXParser sp = factory.newSAXParser();
                    XMLReader reader = sp.getXMLReader();
                    reader.setContentHandler(parser);
                    reader.parse(is);
                } catch (Exception e) {
                    // do nothing
                }
                /************* Get Parse data in a ArrayList **********/
                mWeatherXMLRecords = parser.list;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);


            if (mWeatherXMLRecords != null) {
                mTemperatureTextView.setText(mWeatherXMLRecords.get(1).getTemperature());
                mHumidityTextView.setText(mWeatherXMLRecords.get(1).getHumidity());
                mCloudcoverTextView.setText(mWeatherXMLRecords.get(1).getCloudcover());
                mWindspeedTextView.setText(mWeatherXMLRecords.get(1).getWindspeed());
                mLastUpdatedTextView.setText(gf.convertDateToString(new Date(),"hh:mm:ss dd-MM-yyyy"));
            }

        }
    }


}


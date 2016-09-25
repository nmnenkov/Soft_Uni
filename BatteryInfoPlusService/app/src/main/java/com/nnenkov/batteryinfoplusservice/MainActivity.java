package com.nnenkov.batteryinfoplusservice;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    //Intent mServiceIntent;
    Button mStartServiceButton;
    Button mCheckIfServiceIsStartedButton;
    Button mRefreshButton;

    TextView mBatteryStatusInfo, mBatteryStatusInfoCurrentLevel,mBatteryEventsRegisteredTextView;

    BatteryServiceReceiver batteryServiceReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isMyServiceRunning(BatteryMonitorService.class)) {
            setContentView(R.layout.activity_main_show_status);
            setupReceiver();
            Intent mServiceIntent = new Intent(this, BatteryMonitorService.class);
            mServiceIntent.setFlags(Service.START_STICKY);
            mServiceIntent.putExtra("ServiceCmd", "GetStatus");
            mServiceIntent.putExtra("receiver", batteryServiceReceiver);
            startService(mServiceIntent);

            mBatteryStatusInfo = (TextView) findViewById(R.id.batteryStatusInfo);
            mBatteryStatusInfoCurrentLevel = (TextView) findViewById(R.id.batteryStatusInfoCurrentLevel);
            mBatteryEventsRegisteredTextView = (TextView) findViewById(R.id.batteryEventsRegisteredTextView);

            mBatteryEventsRegisteredTextView.setText("n/a");
            mBatteryStatusInfoCurrentLevel.setText("n/a");
            mRefreshButton = (Button) findViewById(R.id.refreshButton);
            mRefreshButton.setOnClickListener(this);

        } else {
            setContentView(R.layout.activity_main_start_service);
            mStartServiceButton = (Button) findViewById(R.id.startServiceButton);
            mStartServiceButton.setOnClickListener(this);

            mCheckIfServiceIsStartedButton = (Button) findViewById(R.id.checkIfServiceIsStartedButton);
            mCheckIfServiceIsStartedButton.setOnClickListener(this);
        }
    }

    public void setupReceiver() {

        batteryServiceReceiver = new BatteryServiceReceiver(new Handler());

        batteryServiceReceiver.setReceiver(new BatteryServiceReceiver.Receiver()

                                           {
                                               @Override
                                               public void onReceiveResult(int resultCode, Bundle resultData) {
                                                   if (resultCode == Activity.RESULT_OK) {
                                                       mBatteryStatusInfo.setText(resultData.get("BatteryDrop").toString());
                                                       mBatteryEventsRegisteredTextView.setText(resultData.get("BatteryEventsRegistered").toString());
                                                       mBatteryStatusInfoCurrentLevel.setText(resultData.get("CurrentLevel").toString());
                                                   }
                                               }
                                           }

        );
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.startServiceButton) {
            if (!isMyServiceRunning(BatteryMonitorService.class)) {
                Intent mServiceIntent = new Intent(this, BatteryMonitorService.class);
                mServiceIntent.setFlags(Service.START_STICKY);
                startService(mServiceIntent);
            } else {
                Toast.makeText(this, "The service is already running!!!", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.checkIfServiceIsStartedButton) {
            if (isMyServiceRunning(BatteryMonitorService.class)) {
                Toast.makeText(this, "The service is started!!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "The service is not started!!!", Toast.LENGTH_SHORT).show();
            }
        }
        if (view.getId() == R.id.refreshButton) {
            if (isMyServiceRunning(BatteryMonitorService.class)) {
                Intent mServiceIntent = new Intent(this, BatteryMonitorService.class);
                mServiceIntent.setFlags(Service.START_STICKY);
                mServiceIntent.putExtra("ServiceCmd", "GetStatus");
                mServiceIntent.putExtra("receiver", batteryServiceReceiver);
                startService(mServiceIntent);
            }

        }
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        batteryServiceReceiver.setReceiver(null);
        batteryServiceReceiver = null;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (batteryServiceReceiver == null) {
            setupReceiver();
        }
    }


}
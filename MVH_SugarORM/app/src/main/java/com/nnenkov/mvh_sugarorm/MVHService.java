package com.nnenkov.mvh_sugarorm;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by nik on 09.10.16.
 */

public class MVHService extends Service {

    private final IBinder mBinder = new MyBinder();
   // private ArrayList<BatteryStatusHistory> eventsList = new ArrayList<>();
    BatteryChargedReceiver batteryChargedReceiver;
   // DBHelper mydb;

    @Nullable
    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        MVHService getService() {
            return MVHService.this;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service " + MVHService.class.getName().toString() + " Started!!!", Toast.LENGTH_SHORT).show();

        if (intent != null)
            if (intent.hasExtra("ServiceCmd") && intent.hasExtra("receiver")) {
                if ((intent.getStringExtra("ServiceCmd").toString().compareTo("GetStatus") == 0)) {
                    //Toast.makeText(this, "intent.getStringExtra ServiceCmd <<<<<", Toast.LENGTH_LONG).show();
                    final ResultReceiver receiver = intent.getParcelableExtra("receiver");
                    if (receiver != null) {
                        Bundle bundle = new Bundle();
                        int batteryDrop = 0;
                        /*BatteryStatusHistory batteryEvent = mydb.getEvent(mydb.getMaxRowID());
                        if (batteryEvent != null) {
                            batteryDrop = -1;
                            bundle.putInt("BatteryDrop", batteryDrop);
                            bundle.putInt("BatteryEventsRegistered", mydb.numberOfRows());
                            bundle.putInt("CurrentLevel", batteryEvent.getLevel());

                            receiver.send(Activity.RESULT_OK, bundle);
                        }*/
                    }
                }
            }

        return START_STICKY; //super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {

        Toast.makeText(this, "Service " + MVHService.class.getName().toString() + " Created!!!", Toast.LENGTH_SHORT).show();
        super.onCreate();

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryChargedReceiver = new BatteryChargedReceiver();
        registerReceiver(batteryChargedReceiver, ifilter);

        //mydb = new DBHelper(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service destroyed!!!", Toast.LENGTH_SHORT).show();
    }


/*
    public class GPSOperations {

    }
*/
    public class BatteryChargedReceiver extends BroadcastReceiver {

        private String action = null;
        private final String TAG = BatteryChargedReceiver.class.getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle = intent.getExtras();

            if (bundle == null) return;

            int plugged = intent.getIntExtra("plugged", -1);
            int scale = intent.getIntExtra("scale", -1);
            int health = intent.getIntExtra("health", 0);
            int status = intent.getIntExtra("status", 0);
            int rawlevel = intent.getIntExtra("level", -1);
            int level = 0;

            Log.d("Debug", "Battery Receiver OnReceive");


            if (rawlevel >= 0 && scale > 0) {
                level = (rawlevel * 100) / scale;

                Log.d("Debug", "BatterReceiver: " + level);

                //Toast.makeText(context, "Battery Receiver: " + level + "\t Status: " + status + "\nRaw: " + rawlevel, Toast.LENGTH_SHORT).show();
                //BatteryStatusHistory batteryEvent = new BatteryStatusHistory(new Date(), plugged, scale, health, status, rawlevel, level);
                // not needed replaced by SQLite DB
                //eventsList.add(batteryEvent);
                //mydb.insertEvent(batteryEvent);
            }
        }
    }

    @Override
    public boolean stopService(Intent name) {
        Context ctx = getApplicationContext();
        Intent mServiceIntent = new Intent(ctx, MVHService.class);
        mServiceIntent.setFlags(Service.START_STICKY);
        ctx.startService(mServiceIntent);
        Log.i("MVHService Service", "Service auto Started");
        //return super.stopService(name);
        return true;
    }
}

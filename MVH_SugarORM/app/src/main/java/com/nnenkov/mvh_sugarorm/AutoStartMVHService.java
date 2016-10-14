package com.nnenkov.mvh_sugarorm;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by nik on 09.10.16.
 */

public class AutoStartMVHService extends BroadcastReceiver {

    public void onReceive(Context ctx, Intent arg1)
    {
        Intent mServiceIntent = new Intent(ctx, MVHService.class);
        mServiceIntent.setFlags(Service.START_STICKY);
        ctx.startService(mServiceIntent);
        Log.i("BatterySH Service", "Service auto Started");
    }
}

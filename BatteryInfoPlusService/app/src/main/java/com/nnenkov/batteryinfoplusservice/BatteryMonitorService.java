package com.nnenkov.batteryinfoplusservice;

import android.app.Activity;
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
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by nik on 24.09.16.
 */

public class BatteryMonitorService extends Service {

    private final IBinder mBinder = new MyBinder();
    private ArrayList<BatteryStatusHistory> eventsList = new ArrayList<>();
    BatteryChargedReceiver batteryChargedReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        BatteryMonitorService getService() {
            return BatteryMonitorService.this;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service " + BatteryMonitorService.class.getName().toString() + " Started!!!", Toast.LENGTH_SHORT).show();

        if (intent != null)
            if (intent.hasExtra("ServiceCmd") && intent.hasExtra("receiver")) {
                if ((intent.getStringExtra("ServiceCmd").toString().compareTo("GetStatus") == 0)) {
                    //Toast.makeText(this, "intent.getStringExtra ServiceCmd <<<<<", Toast.LENGTH_LONG).show();

                    final ResultReceiver receiver = intent.getParcelableExtra("receiver");
                    Bundle bundle = new Bundle();

                    // Nqmam vreme za da napravq neshtata do krai s XML-a za tova prosto shte
                    // napravq da pokazva s kolko e padnala bateriqta ot kakto service ima danni

                    int batteryDrop = 0;
                    if (eventsList.size() > 1) {batteryDrop = eventsList.get(0).getLevel() - eventsList.get(eventsList.size()-1).getLevel();}
                    bundle.putInt("BatteryDrop", batteryDrop);
                    bundle.putInt("BatteryEventsRegistered", eventsList.size());
                    bundle.putInt("CurrentLevel", eventsList.get(eventsList.size()-1).getLevel());
                    if (receiver != null) {
                        receiver.send(Activity.RESULT_OK, bundle);
                    }

                }
            }

        return START_STICKY; //super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        // Ako moje daite joker zashto service se spira sled kato spra app-a i polse se startira na novo...  :(
        // Tova prechi da se pazi vsichko v pametta
        // I poradi tova mai e nujno a se polzva baza danni ili zapis vuv vunshen fail
        Toast.makeText(this, "Service " + BatteryMonitorService.class.getName().toString() + " Created!!!", Toast.LENGTH_SHORT).show();
        super.onCreate();

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryChargedReceiver = new BatteryChargedReceiver();
        registerReceiver(batteryChargedReceiver, ifilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service destroyed!!!", Toast.LENGTH_SHORT).show();
    }


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
                BatteryStatusHistory batteryEvent = new BatteryStatusHistory(new Date(), plugged, scale, health, status, rawlevel, level);
                eventsList.add(batteryEvent);
            }
        }
    }


    void writeToXML(BatteryStatusHistory batteryEvent) {
        final String xmlFile = "batteryData.xml";
        String userName = "username";
        String password = "password";
        try {
            FileOutputStream fos = new FileOutputStream("userData.xml");
            FileOutputStream fileos = getApplicationContext().openFileOutput(xmlFile, Context.MODE_PRIVATE);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);
/*                for (int counter=0;counter <= eventsList.size()-1;counter++) {
                    xmlSerializer.startTag(null, "event");
                    xmlSerializer.startTag(null, "eventDate");xmlSerializer.text(eventsList.get(counter).getEventDate().toString());xmlSerializer.endTag(null, "eventDate");
                    xmlSerializer.startTag(null, "plugged");xmlSerializer.text(String.valueOf(eventsList.get(counter).getPlugged()));xmlSerializer.endTag(null, "plugged");
                    xmlSerializer.startTag(null, "scale");xmlSerializer.text(String.valueOf(eventsList.get(counter).getScale()));xmlSerializer.endTag(null, "scale");
                    xmlSerializer.startTag(null, "health");xmlSerializer.text(String.valueOf(eventsList.get(counter).getHealth()));xmlSerializer.endTag(null, "health");
                    xmlSerializer.startTag(null, "status");xmlSerializer.text(String.valueOf(eventsList.get(counter).getStatus()));xmlSerializer.endTag(null, "status");
                    xmlSerializer.startTag(null, "rawlevel");xmlSerializer.text(String.valueOf(eventsList.get(counter).getRawlevel()));xmlSerializer.endTag(null, "rawlevel");
                    xmlSerializer.startTag(null, "level");xmlSerializer.text(String.valueOf(eventsList.get(counter).getLevel()));xmlSerializer.endTag(null, "level");

                    xmlSerializer.endTag(null, "event");
                }*/
            xmlSerializer.startTag(null, "event");
            xmlSerializer.startTag(null, "eventDate");
            xmlSerializer.text(batteryEvent.getEventDate().toString());
            xmlSerializer.endTag(null, "eventDate");
            xmlSerializer.startTag(null, "plugged");
            xmlSerializer.text(String.valueOf(batteryEvent.getPlugged()));
            xmlSerializer.endTag(null, "plugged");
            xmlSerializer.startTag(null, "scale");
            xmlSerializer.text(String.valueOf(batteryEvent.getScale()));
            xmlSerializer.endTag(null, "scale");
            xmlSerializer.startTag(null, "health");
            xmlSerializer.text(String.valueOf(batteryEvent.getHealth()));
            xmlSerializer.endTag(null, "health");
            xmlSerializer.startTag(null, "status");
            xmlSerializer.text(String.valueOf(batteryEvent.getStatus()));
            xmlSerializer.endTag(null, "status");
            xmlSerializer.startTag(null, "rawlevel");
            xmlSerializer.text(String.valueOf(batteryEvent.getRawlevel()));
            xmlSerializer.endTag(null, "rawlevel");
            xmlSerializer.startTag(null, "level");
            xmlSerializer.text(String.valueOf(batteryEvent.getLevel()));
            xmlSerializer.endTag(null, "level");
            xmlSerializer.endTag(null, "event");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fileos.write(dataWrite.getBytes());
            fileos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void readFromXML() {

        final String xmlFile = "batteryData.xml";
        FileInputStream fis;
        String data = null;
        ArrayList<String> userData = new ArrayList<String>();
        try {
            fis = getApplicationContext().openFileInput(xmlFile);
            InputStreamReader isr = new InputStreamReader(fis);
            char[] inputBuffer = new char[fis.available()];
            isr.read(inputBuffer);
            data = new String(inputBuffer);
            isr.close();
            fis.close();
        } catch (FileNotFoundException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        factory.setNamespaceAware(true);
        XmlPullParser xpp = null;
        try {
            xpp = factory.newPullParser();
        } catch (XmlPullParserException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        try {
            xpp.setInput(new StringReader(data));
        } catch (XmlPullParserException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        int eventType = 0;
        try {
            eventType = xpp.getEventType();
        } catch (XmlPullParserException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_DOCUMENT) {
                System.out.println("Start document");
            } else if (eventType == XmlPullParser.START_TAG) {
                System.out.println("Start tag " + xpp.getName());
            } else if (eventType == XmlPullParser.END_TAG) {
                System.out.println("End tag " + xpp.getName());
            } else if (eventType == XmlPullParser.TEXT) {
                userData.add(xpp.getText());
            }
            try {
                eventType = xpp.next();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }


}

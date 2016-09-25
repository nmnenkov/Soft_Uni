package com.nnenkov.batteryinfoplusservice;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by nik on 25.09.16.
 */

public class BatteryServiceReceiver  extends ResultReceiver {
    private Receiver receiver;

  public BatteryServiceReceiver(Handler handler) {
      super(handler);
  }


  public void setReceiver(Receiver receiver) {
      this.receiver = receiver;
  }


  public interface Receiver {
      public void onReceiveResult(int resultCode, Bundle resultData);
  }


  @Override
  protected void onReceiveResult(int resultCode, Bundle resultData) {
      if (receiver != null) {
        receiver.onReceiveResult(resultCode, resultData);
      }
  }
}

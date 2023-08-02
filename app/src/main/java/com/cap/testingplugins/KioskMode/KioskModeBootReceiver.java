package com.cap.testingplugins.kiosk;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.cap.testingplugins.MainActivity;


public class KioskModeBootReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {


    Log.e("NIK", "onReceive: " + intent.getAction());

    if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
      restartApp(context);
    }
    if (Intent.ACTION_MY_PACKAGE_REPLACED.equals(intent.getAction())) {
      restartApp(context);
    }
    if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
      restartApp(context);
    }

  }

  public static void restartApp(Context context) {
    Intent myIntent = new Intent(context, MainActivity.class);
    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    context.startActivity(myIntent);
    Log.e("NIK", "Restarting app ");
  }
}

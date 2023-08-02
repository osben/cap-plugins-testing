package com.cap.testingplugins.kiosk;

import android.app.admin.DeviceAdminReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class KioskModeDeviceAdminReceiver extends DeviceAdminReceiver {
  private static final String TAG = "KioskModeDeviceAdminReceiver";

  public static ComponentName getComponentName(Context context) {
    return new ComponentName(context, KioskModeDeviceAdminReceiver.class);
  }

//  @Override
//  public void onReceive(Context context, Intent intent) {
//    if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
//     // Toast.makeText(context, "boot_completed", Toast.LENGTH_SHORT).show();
//      Intent activityIntent = new Intent(context, MainActivity.class);
//      activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // runtime warning if not set
//      context.startActivity(activityIntent);
//
//    } else {
//      super.onReceive(context, intent);
//    }
//
//  }

  @Override
  public void onEnabled(Context context, Intent intent) {
    Toast.makeText(context, "device_admin_enabled", Toast.LENGTH_SHORT).show();
  }

  @Override
  public CharSequence onDisableRequested(Context context, Intent intent) {
    return "device_admin_warning";
  }

  @Override
  public void onDisabled(Context context, Intent intent) {
    Toast.makeText(context, "device_admin_disabled", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onLockTaskModeEntering(Context context, Intent intent, String pkg) {
    Toast.makeText(context, "kiosk_mode_enabled", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onLockTaskModeExiting(Context context, Intent intent) {
    Toast.makeText(context, "kiosk_mode_disabled", Toast.LENGTH_SHORT).show();
  }

  @Override
  public void onPasswordChanged(Context context, Intent intent) {
    Toast.makeText(context, "password_changed", Toast.LENGTH_SHORT).show();
  }
}

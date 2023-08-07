package com.cap.testingplugins.KioskMode;

import android.app.admin.DeviceAdminReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.cap.testingplugins.MainActivity;

public class KioskModeDeviceAdminReceiver extends DeviceAdminReceiver {
  private static final String TAG = "KioskModeDeviceAdminReceiver";


  @Override
  public void onReceive(Context context, Intent intent) {
    super.onReceive(context,intent);

      Intent activityIntent = new Intent(context, MainActivity.class);
      activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // runtime warning if not set
      context.startActivity(activityIntent);
  }

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

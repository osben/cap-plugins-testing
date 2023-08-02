package com.cap.testingplugins.KioskMode;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.app.admin.SystemUpdatePolicy;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.UserManager;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.cap.testingplugins.MainActivity;

@CapacitorPlugin(name = "KioskMode")
public class KioskModePlugin extends Plugin {
  boolean isLockTaskModeRunning = false;

  private ActivityManager am;
  private Context context;
  private String PackageName;
  private DevicePolicyManager devicePolicyManager;
  private ComponentName adminComponentName;

  private String TAG = "Capacitor Kiosk Plugin";


  public void load() {
    context = getContext();
    am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);


    PackageName = context.getApplicationContext().getPackageName();
    devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
    adminComponentName = com.cap.testingplugins.KioskMode.KioskModeDeviceAdminReceiver.getComponentName(context);
  }

  private boolean isLockTaskPermittedStatus() {
    Log.i(TAG, "isLockTaskPermittedStatus: " + PackageName);
    return devicePolicyManager.isLockTaskPermitted(PackageName);
  }

  public boolean isDeviceOwnerAppStatus() {
    Log.i(TAG, "isDeviceOwnerAppStatus: " + PackageName);
    return devicePolicyManager.isDeviceOwnerApp(PackageName);
  }


  @PluginMethod(returnType = PluginMethod.RETURN_PROMISE)
  public void isLockTaskPermitted(PluginCall call) {
    Log.i(TAG, "isLockTaskPermitted");
    call.resolve(new JSObject().put("isLockTaskPermitted", isLockTaskPermittedStatus()));
  }

  @PluginMethod(returnType = PluginMethod.RETURN_PROMISE)
  public void isDeviceOwnerApp(PluginCall call) {
    Log.i(TAG, "isDeviceOwnerApp");
    call.resolve(new JSObject().put("isDeviceOwnerApp", isDeviceOwnerAppStatus()));
  }

  @PluginMethod(returnType = PluginMethod.RETURN_PROMISE)
  public void enterKioskMode(PluginCall call) {
    Log.i(TAG, "enterKioskMode");

    setKioskPolicies(true, isDeviceOwnerAppStatus());

    call.resolve();
  }

  @PluginMethod(returnType = PluginMethod.RETURN_PROMISE)
  public void exitKioskMode(PluginCall call) {
    Log.i(TAG, "exitKioskMode");

    setKioskPolicies(false, isDeviceOwnerAppStatus());

    call.resolve();
  }

  private void setKioskPolicies(boolean enable, boolean isAdmin) {

    if (isAdmin) {
      setRestrictions(enable);
      setKeyGuardEnabled(enable);

      enableStayOnWhilePluggedIn(enable);
      setUpdatePolicy(enable);
      setAsHomeApp(enable);
      setLockTaskPackages(enable);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        setImmersiveMode(enable);
      }
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.P)
  private void setImmersiveMode(boolean active) {
    Log.i(TAG, "setImmersiveMode: " + active);
    if (active) {
      devicePolicyManager.setLockTaskFeatures(
        adminComponentName,
        DevicePolicyManager.LOCK_TASK_FEATURE_SYSTEM_INFO |
          DevicePolicyManager.LOCK_TASK_FEATURE_OVERVIEW |
          DevicePolicyManager.LOCK_TASK_FEATURE_HOME |
          DevicePolicyManager.LOCK_TASK_FEATURE_KEYGUARD
      );
    } else {
      devicePolicyManager.setLockTaskFeatures(
        adminComponentName,
        DevicePolicyManager.LOCK_TASK_FEATURE_NONE
      );
    }
  }


  private void setRestrictions(boolean disallow) {
    setUserRestriction(UserManager.DISALLOW_SAFE_BOOT, disallow);
    setUserRestriction(UserManager.DISALLOW_FACTORY_RESET, disallow);
    setUserRestriction(UserManager.DISALLOW_ADD_USER, disallow);
    setUserRestriction(UserManager.DISALLOW_MOUNT_PHYSICAL_MEDIA, disallow);
    setUserRestriction(UserManager.DISALLOW_ADJUST_VOLUME, disallow);
  }

  private void setUserRestriction(String restriction, boolean disallow) {
    if (disallow) {
      devicePolicyManager.addUserRestriction(adminComponentName,
        restriction);
    } else {
      devicePolicyManager.clearUserRestriction(adminComponentName,
        restriction);
    }
  }

  private void setUpdatePolicy(boolean active) {
    if (active) {
      devicePolicyManager.setSystemUpdatePolicy(
        adminComponentName,
        SystemUpdatePolicy.createWindowedInstallPolicy(60, 120)
      );
    } else {
      devicePolicyManager.setSystemUpdatePolicy(adminComponentName, null);
    }
  }

  private void setAsHomeApp(boolean enable) {
    if (enable) {
      IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MAIN);
      intentFilter.addCategory(Intent.CATEGORY_HOME);
      intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

      devicePolicyManager.addPersistentPreferredActivity(
        adminComponentName, intentFilter, new ComponentName(
          PackageName, MainActivity.class.getName()
        )
      );

    } else {
      devicePolicyManager.clearPackagePersistentPreferredActivities(
        adminComponentName, PackageName
      );
    }
  }


  private void setKeyGuardEnabled(boolean enable) {
    devicePolicyManager.setKeyguardDisabled(adminComponentName, enable);
    devicePolicyManager.setStatusBarDisabled(adminComponentName, enable);
  }


  private void setLockTaskPackages(boolean enable) {

    if (enable) {
      devicePolicyManager.setLockTaskPackages(
        adminComponentName,
        new String[]{PackageName}
      );

    } else {
      devicePolicyManager.setLockTaskPackages(
        adminComponentName,
        new String[]{}
      );


    }

  }

  private void enableStayOnWhilePluggedIn(boolean enabled) {
    if (enabled) {
      devicePolicyManager.setGlobalSetting(
        adminComponentName,
        Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
        Integer.toString(BatteryManager.BATTERY_PLUGGED_AC
          | BatteryManager.BATTERY_PLUGGED_USB
          | BatteryManager.BATTERY_PLUGGED_WIRELESS)
      );
    } else {
      devicePolicyManager.setGlobalSetting(
        adminComponentName,
        Settings.Global.STAY_ON_WHILE_PLUGGED_IN,
        "0"
      );
    }
  }
}

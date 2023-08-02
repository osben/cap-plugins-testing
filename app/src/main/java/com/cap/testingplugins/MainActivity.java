package com.cap.testingplugins;

import android.os.Bundle;

import com.getcapacitor.BridgeActivity;
import com.cap.testingplugins.KioskMode.KioskModePlugin;
import com.cap.testingplugins.StorageVolumes.StorageVolumesPlugin;


public class MainActivity extends BridgeActivity {

  private String TAG = "Capacitor::MainActivity";

  @Override
  public void onCreate(Bundle savedInstanceState) {
    registerPlugin(KioskModePlugin.class);
    registerPlugin(StorageVolumesPlugin.class);
    super.onCreate(savedInstanceState);
  }
}

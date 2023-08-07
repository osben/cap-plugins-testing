package com.cap.testingplugins;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.cap.testingplugins.KioskMode.KioskModePlugin;
import com.cap.testingplugins.StorageVolumes.StorageVolumesPlugin;
import com.getcapacitor.BridgeActivity;


public class MainActivity extends BridgeActivity {

    private String TAG = "Capacitor::MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        hideNavBar();//use this to hide bars,or just call hideNavBar from the plugin
        registerPlugin(KioskModePlugin.class);
        registerPlugin(StorageVolumesPlugin.class);

        super.onCreate(savedInstanceState);
    }

    private void hideNavBar() {
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());

        windowInsetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);

        getWindow().getDecorView().setOnApplyWindowInsetsListener((view, windowInsets) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars())) {

                    windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars());
                }
            }
            return view.onApplyWindowInsets(windowInsets);
        });

    }


}


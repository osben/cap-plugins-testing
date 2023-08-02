adb devices
adb install -r -t /to/kiosk/application.apk

adb shell dpm set-device-owner com.cap.testingplugins/.KioskModeDeviceAdminReceiver

adb reboot
adb shell dpm remove-active-admin com.cap.testingplugins/.KioskModeDeviceAdminReceiver

adb shell am broadcast -a android.intent.action.BOOT_COMPLETED

adb shell am broadcast -a android.intent.action.BOOT_COMPLETED -c android.intent.category.HOME -n com.cap.testingplugins/.KioskModeActivity
adb shell am broadcast -a android.intent.action.BOOT_COMPLETED -p com.cap.testingplugins

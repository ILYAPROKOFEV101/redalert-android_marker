package com.red.alert.logic.integration;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;

import com.red.alert.logic.alerts.AlertTypes;
import com.red.alert.logic.integration.devices.MiBandIntegration;
import com.red.alert.logic.settings.AppPreferences;

public class BluetoothIntegration {
    public static void notifyDevices(String alertType, Context context) {
        // Type must be an "alert" or "test"
        if (alertType.equals(AlertTypes.PRIMARY) || alertType.equals(AlertTypes.TEST)) {
            // Check for BLE support + enabled Bluetooth controller
            if (!isBLESupported(context) || !isBluetoothEnabled(context)) {
                // Stop execution
                return;
            }

            // Vibrate + LED for Mi Band (if enabled)
            MiBandIntegration.notifyMiBand(context);
        }
    }

    public static boolean isIntegrationEnabled(Context context) {
        // Check if Mi Band integration is enabled (and add more devices in the future)
        return AppPreferences.getMiBandIntegrationEnabled(context);
    }

    public static boolean isBLESupported(Context context) {
        // Check whether Bluetooth is supported on the device
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
            // No Bluetooth support
            return false;
        }

        // Check whether BLE is supported on the device
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            // No BLE support
            return false;
        }

        // Check Android version (BluetoothGatt requires API Level 18+)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return false;
        }

        // We're good
        return true;
    }

    public static boolean isBluetoothEnabled(Context context) {
        // Must call Looper.prepare() due to ICS bug
        // And we can't call the function from UI thread when we receive a push notification)
        // http://stackoverflow.com/questions/5920578/bluetoothadapter-getdefault-throwing-runtimeexception-while-not-in-activity

        if (Looper.myLooper() == null) {
            Looper.prepare();
        }

        // Get bluetooth adapter
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Null means device doesn't support Bluetooth
        if (bluetoothAdapter == null) {
            // No Bluetooth support
            return false;
        }
        else {
            // Check if user has already granted the necessary Bluetooth runtime permissions
            if (!runtimePermissionsGranted(context)) {
                return false;
            }

            // Check if adapter is enabled and return the result
            return bluetoothAdapter.isEnabled();
        }
    }

    private static boolean runtimePermissionsGranted(Context context) {
        // Android 13 and up also requires the BLUETOOTH_CONNECT and BLUETOOTH_SCAN runtime permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    context.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                    context.checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED;
        }
        // Android 6 and up only requires the ACCESS_FINE_LOCATION runtime permission
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }

        // Previous Android versions below 6 did not require the user to consent to runtime permissions
        return true;
    }

}

package com.red.alert.logic.push;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.red.alert.R;
import com.red.alert.config.Logging;
import com.red.alert.config.push.PushyGateway;
import com.red.alert.utils.caching.Singleton;
import com.red.alert.utils.formatting.StringUtils;

import me.pushy.sdk.Pushy;

public class PushyRegistration {
    public static String registerForPushNotifications(Context context) throws Exception {
        // Acquire a unique registration ID for this device
        String token = Pushy.register(context);

        // Log to logcat
        Log.d(Logging.TAG, "Pushy registration success: " + token);

        // Subscribe to global alerts topic (we need this for location alerts to work)
        Pushy.subscribe(PushyGateway.ALERTS_TOPIC, context);

        // Log it
        Log.d(Logging.TAG, "Pushy subscribe success: " + PushyGateway.ALERTS_TOPIC);

        // Return token to be sent to API
        return token;
    }

    public static String getRegistrationToken(Context context) {
        // Get it from SharedPreferences (may be null)
        return Singleton.getSharedPreferences(context).getString(context.getString(R.string.pushyTokenPref), "");
    }

    public static boolean isRegistered(Context context) {
        // Check whether it's null (in which case we never successfully registered)
        return !StringUtils.stringIsNullOrEmpty(getRegistrationToken(context));
    }

    public static void saveRegistrationToken(Context context, String registrationToken) {
        // Edit shared preferences
        SharedPreferences.Editor editor = Singleton.getSharedPreferences(context).edit();

        // Store boolean value
        editor.putString(context.getString(R.string.pushyTokenPref), registrationToken);

        // Save and flush
        editor.commit();
    }
}

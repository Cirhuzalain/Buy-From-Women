package com.nijus.alino.bfwcoopmanagement.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nijus.alino.bfwcoopmanagement.R;

public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    public static boolean checkAlreadyLogin(Context context) {
        SharedPreferences prefsGoog = context
                .getSharedPreferences(context.getResources().getString(R.string.application_key), Context.MODE_PRIVATE);

        return prefsGoog.getBoolean(context.getResources().getString(R.string.app_id), false);
    }

    public static String getLoginUserToken(Activity context) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getString(R.string.application_key), Context.MODE_PRIVATE);
        return preferences.getString(context.getString(R.string.app_id), "123");
    }
}

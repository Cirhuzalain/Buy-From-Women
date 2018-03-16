package com.nijus.alino.bfwcoopmanagement.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nijus.alino.bfwcoopmanagement.R;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    public static String getUserType(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getResources().getString(R.string.application_key),
                Context.MODE_PRIVATE);
        return prefs.getString(context.getResources().getString(R.string.g_name), "123");
    }

    public static int getVendorServerId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getResources().getString(R.string.application_key),
                Context.MODE_PRIVATE);
        return prefs.getInt(context.getResources().getString(R.string.vendor_buyer_id), 0);
    }

    public static int getCoopServerId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(context.getResources().getString(R.string.application_key),
                Context.MODE_PRIVATE);
        return prefs.getInt(context.getResources().getString(R.string.coop_id), 0);
    }

    public static boolean checkAlreadyLogin(Context context) {
        SharedPreferences prefsGoog = context
                .getSharedPreferences(context.getResources().getString(R.string.application_key), Context.MODE_PRIVATE);

        return prefsGoog.getBoolean(context.getResources().getString(R.string.app_id), false);
    }

    public static Response getServerData(OkHttpClient client, String token, String apiUrl) {
        try {
            Request requestUser = new Request.Builder()
                    .url(apiUrl)
                    .header("Access-Token", token)
                    .get()
                    .build();

            return client.newCall(requestUser).execute();
        } catch (IOException exp) {
            return null;
        } catch (Exception exp) {
            return null;
        }
    }

    public static String getLoginUserToken(Activity context) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getString(R.string.application_key), Context.MODE_PRIVATE);
        return preferences.getString(context.getString(R.string.app_id), "123");
    }
}

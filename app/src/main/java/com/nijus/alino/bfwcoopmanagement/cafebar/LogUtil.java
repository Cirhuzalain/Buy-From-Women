package com.nijus.alino.bfwcoopmanagement.cafebar;

import android.support.annotation.NonNull;
import android.util.Log;

class LogUtil {

    private static final String TAG = "CafeBar";
    static boolean sEnableLogging = false;

    public static void d(@NonNull String message) {
        if (LogUtil.sEnableLogging)
            Log.d(TAG, message);
    }
}

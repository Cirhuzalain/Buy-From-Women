package com.nijus.alino.bfwcoopmanagement.cafebar;

import android.support.annotation.NonNull;
import android.util.Log;

class LogUtil {

    static boolean sEnableLogging = false;

    private static final String TAG = "CafeBar";

    public static void d(@NonNull String message) {
        if (LogUtil.sEnableLogging)
            Log.d(TAG, message);
    }
}

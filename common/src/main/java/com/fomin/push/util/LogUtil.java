package com.fomin.push.util;

import android.util.Log;

/**
 * Created by Fomin on 2018/10/25.
 */
public class LogUtil {
    public static boolean isDebug = false;
    private static final String TAG = "PushTag";

    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, msg);
    }

    public static void i(String msg) {
        if (isDebug)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    public static void e(String msg, Throwable t) {
        Log.e(TAG, msg, t);
    }
}

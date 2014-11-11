package com.andr0day;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by wm on 2014/11/10.
 */
public class Logger {

    public static String Log_PREFIX = "pwd_toast_";

    public static void log(String message) {
        log("default", message);
    }

    public static void log(String pkg, String message) {
//        Log.d(Log_PREFIX + pkg, message);
        XposedBridge.log(Log_PREFIX + pkg + " : " + message);
    }
}

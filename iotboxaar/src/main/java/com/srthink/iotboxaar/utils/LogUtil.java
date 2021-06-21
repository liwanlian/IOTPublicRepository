package com.srthink.iotboxaar.utils;

import android.util.Log;

/**
 * 打印日志工具类
 *
 * @author liwanlian
 * @date 2020/12/31 11:15
 */
public class LogUtil {
    static String LOG_TAG = "IOTBox";

    /**
     * 输出debug的信息
     * 颜色是蓝色
     *
     * @param message
     */
    public static void logDebug(String message) {
        Log.d(LOG_TAG, message);
    }

    /**
     * 输出verbose的信息
     * 颜色为黑色
     *
     * @param msg
     */
    public static void logVerbose(String msg) {
        Log.v(LOG_TAG, msg);
    }

    /**
     * 输出information的信息
     * 颜色为绿色
     *
     * @param msg
     */
    public static void logInfo(String msg) {
        Log.i(LOG_TAG, msg);
    }

    /**
     * 输出warning的信息
     * 颜色为橙色
     *
     * @param msg
     */
    public static void logWarning(String msg) {
        Log.w(LOG_TAG, msg);
    }

    /**
     * 输出error的信息
     * 颜色为红色
     *
     * @param msg
     */
    public static void logError(String msg) {
        Log.e(LOG_TAG, msg);
    }
}

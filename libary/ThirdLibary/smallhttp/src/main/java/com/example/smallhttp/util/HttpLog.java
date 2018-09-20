package com.example.smallhttp.util;

import android.content.Context;
import android.util.Log;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/7/27
 * Desc  :
 */

public class HttpLog {
    public static final String TAG = "HttpLog";

    public static void log(String msg) {
        log(TAG, msg);
    }

    public static void log(String tag, String msg) {
        Log.i(tag, msg);
    }
}

package shiyiliang.cn.basetool.util;

import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/8/2
 * Desc  :
 */

public class LogUtil {
    private static boolean FLAG = true;
    private static final String TAG = "lapisy";

    public static void setFlag(boolean pFlag) {
        FLAG=pFlag;
    }

    public static void i(String tag, String message) {
        if (FLAG) {
            String start = "--------------------Start----------------";
            Log.i(tag, start);
            Log.i(tag, TextUtils.isEmpty(message) ? "内容为空" : message);
            String end = "--------------------End----------------";
            Log.i(tag, end);
        }
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static <T> void i(List<T> list) {
        StringBuilder builder = new StringBuilder();
        builder.append("集合长度是 " + list.size() + "个\n");
        for (T str : list) {
            builder.append(str.toString() + "\n");
        }
        i(TAG, builder.toString());
    }

    public static <T> void i(String tag, List<T> list) {
        StringBuilder builder = new StringBuilder();
        builder.append("The List's Size is " + list.size() + "\n");
        for (T str : list) {
            builder.append(str.toString() + "\n");
        }
        i(tag, builder.toString());
    }

    public static void i(Object object) {
        CommonUtil.checkIsNull(object);
        i(TAG, object.toString());
    }

    public static void i(String tag, Object object) {
        CommonUtil.checkIsNull(object);
        i(tag, object.toString());
    }

}

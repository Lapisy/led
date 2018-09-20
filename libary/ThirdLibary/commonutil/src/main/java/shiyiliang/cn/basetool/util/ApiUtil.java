package shiyiliang.cn.basetool.util;

import android.os.Build;
import android.util.Log;

public class ApiUtil {
    //是否是kitkat以上
    public static boolean isKITKAT() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT;
    }

    /**
     * above kitka
     *
     * @return
     */
    public static boolean aboveKITKAT() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * 判断系统是否在6.0之上
     * sdk=23
     *
     * @return
     */
    public static boolean aboveM() {
        return Build.VERSION.SDK_INT < 23 ? false : true;
    }

    /**
     * Android 3.1.
     * sdk=12
     *
     * @return
     */
    public static boolean aboveHONEYCOMB_MR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }
}

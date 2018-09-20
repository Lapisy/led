package shiyiliang.cn.basetool.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/10/11
 * Desc  :
 */

public class PermissionUtil {
    /**
     * 是否需要动态获取权限
     *
     * @param context
     * @return
     */
    public static boolean needRuntimePermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return true;
        return false;
    }

    public static void requestPermission(Context context, String permissions) {
        if (!needRuntimePermission(context))
            return;
        if (ActivityCompat.checkSelfPermission(context, permissions) != PackageManager.PERMISSION_GRANTED) {

        }
        ActivityCompat.checkSelfPermission(context, permissions);
    }
}

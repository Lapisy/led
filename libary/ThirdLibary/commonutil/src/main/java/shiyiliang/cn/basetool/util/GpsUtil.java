package shiyiliang.cn.basetool.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2018/4/1
 * Desc  :
 */

public class GpsUtil {
    public static final int GPS_REQUEST_CODE = 0x714;

    /**
     * gps是否打开
     *
     * @param pContext
     * @return
     */
    public static boolean isGpsOpen(Context pContext) {
        LocationManager locationManager
                = (LocationManager) pContext.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    /**
     * 打开GPS
     *
     * @param context
     */
    public static void openGps(Activity context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivityForResult(intent, GPS_REQUEST_CODE);
    }


    /**
     * 打开GPS
     *
     * @param context
     * @param requestCode
     */
    public static void openGps(Activity context, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivityForResult(intent, requestCode);
    }
}

package shiyiliang.cn.basetool.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017-07-05
 * Desc  : 主要是获取app相关的信息
 */

public class AppUtil {
    public static final int GO_PERMISSION_PAGE_REQUEST_CODE = 0x123;

    /**
     * 得到app的versioncode
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        int versionCode = -1;
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 返回应用的versionname
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        String versionName = "";
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 判断线程是否是主线程
     *
     * @param pContext
     * @return
     */
    public static boolean isUiThread(Context pContext) {
        checkNull(pContext);
        return Looper.getMainLooper() == Looper.myLooper();
    }

    public static boolean isBackgroundThread(Context pContext) {
        checkNull(pContext);
        return !isUiThread(pContext);
    }


    public static void checkNull(Object pObj) {
        if (null == pObj)
            throw new RuntimeException("the argument obj is null");
    }

    /**
     * 进入到设置界面
     * 当设置网络的时候，最好也是进入到这个界面，因为设置wifi和移动网络是不同的界面
     *
     * @param context
     */
    public static void goSetting(Context context) {
        Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        context.startActivity(intent);
    }

    public static void goNetworkSetting(Context context) {
        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
//        //判断手机系统的版本  即API大于10 就是3.0或以上版本
//        if (android.os.Build.VERSION.SDK_INT > 10) {
//            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
//        } else {
//            intent = new Intent();
//            ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
//            intent.setComponent(component);
//            intent.setAction("android.intent.action.VIEW");
//        }
        context.startActivity(intent);
    }

    /**
     * 得到一个进程最大的内存
     *
     * @param pAM
     * @return
     */
    public static int getApplicationMemorySize(ActivityManager pAM) {
        checkNull(pAM);
        return pAM.getMemoryClass();
    }

    /**
     * 进入指定的activity
     *
     * @param pContext
     * @param pClz
     */
    public static void goActivity(Context pContext, Class pClz) {
        checkNull(pClz);
        try {
            Intent intent = new Intent(pContext, pClz);
            pContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context
     * @param mRequestCode
     */
    public static void gotoPermissionSetting(Activity context, int mRequestCode) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivityForResult(intent, mRequestCode);
    }

    /**
     * @param context
     */
    public static void gotoPermissionSetting(Activity context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivityForResult(intent, GO_PERMISSION_PAGE_REQUEST_CODE);
    }


    /**
     * 彻底杀死应用
     */
    public static void killApplication() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}

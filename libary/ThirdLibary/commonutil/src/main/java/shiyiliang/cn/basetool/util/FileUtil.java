package shiyiliang.cn.basetool.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.util.List;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class FileUtil {
    public static String getFileDir(Context context, String dir) {
        String filePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            filePath = context.getExternalFilesDir(dir).getPath();
        } else {
            filePath = context.getFilesDir().getPath();
        }
        return filePath;
    }

//    /**
//     * 将普通uri转化成适应7.0的content://形式  针对文件格式
//     *
//     * @param context    上下文
//     * @param file       文件路径
//     * @param intent     intent
//     * @param intentType intent.setDataAndType
//     * @return
//     */
//    public static Intent formatFileProviderIntent(
//            Context context, File file, Intent intent, String intentType) {
//
//        Uri uri = FileProvider.getUriForFile(context, GlobalDef.nougatFileProvider, file);
//        // 表示文件类型
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        intent.setDataAndType(uri, intentType);
//
//        return intent;
//    }
//
//    /**
//     * 将普通uri转化成适应7.0的content://形式  针对图片格式
//     *
//     * @param context    上下文
//     * @param file       文件路径
//     * @param intent     intent
//     * @return
//     */
//    public static Intent formatFileProviderPicIntent(Context context, File file, Intent intent) {
//
//        Uri uri = FileProvider.getUriForFile(context, GlobalDef.nougatFileProvider, file);
//        List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(
//                intent, PackageManager.MATCH_DEFAULT_ONLY);
//        for (ResolveInfo resolveInfo : resInfoList) {
//            String packageName = resolveInfo.activityInfo.packageName;
//            context.grantUriPermission(packageName, uri,
//                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        }
//        // 表示图片类型
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        return intent;
//    }

    /**
     * 将普通uri转化成适应7.0的content://形式
     *
     * @return
     */
    public static Uri getUriFromFile(Context context, File file, Intent intent, String provider) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, provider, file);
            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    /**
     * 将普通uri转化成适应7.0的content://形式
     *
     * @return
     */
    public static Uri getUriFromFile(Context context, File file, String provider) {
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, provider, file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }
}

package shiyiliang.cn.basetool.util;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.IdRes;

public class ResourceUtil {
    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    public static Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }
}

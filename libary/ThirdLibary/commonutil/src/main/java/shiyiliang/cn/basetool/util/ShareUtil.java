package shiyiliang.cn.basetool.util;

import android.content.Context;
import android.content.Intent;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/9/4
 * Desc  :
 */

public class ShareUtil {
    /**
     * 调用系统的分享，分享字符串
     *
     * @param context
     * @param content
     */
    public static void shareString(Context context, String content) {
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(Intent.createChooser(textIntent, "分享"));
    }
}

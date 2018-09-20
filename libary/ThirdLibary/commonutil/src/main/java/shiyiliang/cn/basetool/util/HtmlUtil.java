package shiyiliang.cn.basetool.util;

import android.text.Html;
import android.text.Spanned;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/9/4
 * Desc  :
 */

public class HtmlUtil {

    //
    public static Spanned fromHtml(String msg) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(msg, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(msg);
        }

        return result;
    }
}

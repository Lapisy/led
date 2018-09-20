package shiyiliang.cn.basetool.util;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

import java.util.List;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/7/3
 * Desc  :
 */

public class ToastUtil {
    public static void longToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void longToast(Context context, @StringRes int resID){
        String msg=context.getResources().getString(resID);
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void shortToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void shortToast(Context context, @StringRes int resID){
        String msg=context.getResources().getString(resID);
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * <p>
     *     这里要实现T的toString方法
     * </p>
     *
     * @param context
     * @param list
     * @param <T>
     */
    public static <T> void shortToast(Context context, List<T> list) {
        CommonUtil.checkNullAndThrow(list);

        StringBuilder builder = new StringBuilder();
        builder.append("The List's Size is " + list.size() + "\n");
        for (T str : list) {
            builder.append(str.toString() + "\n");
        }
        shortToast(context, builder.toString());
    }


}

package shiyiliang.cn.basetool.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017-07-03
 * Desc  : 尺寸转换类
 */

public class DensityUtil {

    /**
     * dp 转换成px
     *
     * @param context
     * @param value
     * @return
     */
    public static float dip2px(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
    }

    /**
     * px 转换成 dp
     *
     * @param context
     * @param value
     * @return
     */
    public static float px2dip(Context context, float value) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (value / scale);
    }

    /**
     * px转sp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }
}

package shiyiliang.cn.basetool.util;

import android.text.TextUtils;

import java.util.List;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/8/2
 * Desc  :
 */

public class CommonUtil {
    /**
     * 检查对对是否为空
     *
     * @param obj
     * @return
     */
    public static boolean checkIsNull(Object obj) {
        if (obj == null || (obj instanceof String) && TextUtils.isEmpty((String) obj))
            return true;
        return false;
    }

    public static void checkNullAndThrow(Object obj) {
        if (obj == null || (obj instanceof String) && TextUtils.isEmpty((String) obj))
            throw new RuntimeException(obj.getClass().getSimpleName() + " is null");

    }

    public static boolean checkNullForList(List list) {
        if (list == null || list.size() == 0)
            return true;
        return false;
    }

    public static boolean checkNullForListAndThrow(List list) {
        if (list == null || list.size() == 0)
            throw new RuntimeException("List is null");
        return false;
    }





}

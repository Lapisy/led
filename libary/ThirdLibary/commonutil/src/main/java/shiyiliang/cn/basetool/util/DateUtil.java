package shiyiliang.cn.basetool.util;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/8/22
 * Desc  :
 */

public class DateUtil {
    /**
     * 2017-09-04 13:43:24.524
     * 2017-08-29T10:44:19.499+08:00
     */
    public static String getStringFromDate() {
        return getStringFromDate(System.currentTimeMillis());
    }

    public static String getStringFromDate(long time) {
        return getStringFromDate(new Date(System.currentTimeMillis()));
    }

    public static String getStringFromDate(Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.SIMPLIFIED_CHINESE);
        return sdf.format(time);
    }

    /**
     * 2017年09月04日
     *
     * @param time
     * @return
     */
    public static String getYearMonthDay(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.SIMPLIFIED_CHINESE);
        String emg = sdf.format(new Date(System.currentTimeMillis()));
        return emg;
    }

    /**
     * 得到所有的HH:mm:ss
     *
     * @param time
     * @return
     */
    public static String getHourMiniteSecond(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.SIMPLIFIED_CHINESE);
        String emg = sdf.format(new Date(System.currentTimeMillis()));
        return emg;
    }

    public static String getWeekDay(long time) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

//    public static void main(String[] args) {
//
//        System.out.println(getHourMiniteSecond(System.currentTimeMillis()));
//    }
}

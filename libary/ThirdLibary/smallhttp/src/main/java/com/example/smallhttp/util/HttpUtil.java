package com.example.smallhttp.util;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017-06-26
 * Desc  :
 */

public class HttpUtil {

    /**
     * 建立日志拦截器
     * @return
     */
    public static HttpLoggingInterceptor getLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }
}

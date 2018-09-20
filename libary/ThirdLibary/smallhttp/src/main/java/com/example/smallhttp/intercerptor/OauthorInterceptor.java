package com.example.smallhttp.intercerptor;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http2.Header;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/8/18
 * Desc  : 主要是验证是否携带了oauther头部
 */

public class OauthorInterceptor implements Interceptor {
    public static String OAUTH_KEY = "Authorization";
    public static final String OAUTH_URL = "https://www.diycode.cc/oauth/token";

    public OauthorInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        //是否携带有认证的字段
        if (!hasAccessToken(request)) {

        }
        return chain.proceed(request);
    }

    private boolean hasAccessToken(Request request) {
        String accessToken = request.header(OAUTH_KEY);
        return (!TextUtils.isEmpty(accessToken) || request.url().toString().trim()
                .equals(OAUTH_URL));
    }
}

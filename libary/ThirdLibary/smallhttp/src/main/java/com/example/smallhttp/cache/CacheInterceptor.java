package com.example.smallhttp.cache;

import android.content.Context;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import shiyiliang.cn.basetool.util.NetworkUtil;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/8/2
 * Desc  : 有网络时，加载网络，无网络时，加载缓存数据
 * 第一次使用的是有会返回504
 * <code>addInterceptor(new NetworkInterceptor(mContext))</code>
 */

public class CacheInterceptor implements Interceptor {
    private final Context mContext;

    public CacheInterceptor(Context context) {
        this.mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!NetworkUtil.isConnectedByState(mContext)) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }

        Response response = chain.proceed(request);
        if (NetworkUtil.isConnectedByState(mContext)) {
            int maxAge = 60;
            response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 28;
            response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
        return response;
    }
}

package com.example.smallhttp.intercerptor;

import com.example.smallhttp.download.DownloadProgressCallback;
import com.example.smallhttp.download.DownloadResponseBody;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/7/28
 * Desc  : 通过addInterceptor使用
 */

public class DownloadProgressInterceptor implements Interceptor {

    private final DownloadProgressCallback callback;

    public DownloadProgressInterceptor(DownloadProgressCallback callback) {
        this.callback=callback;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response proceed = chain.proceed(chain.request());
        return proceed.newBuilder()
                .body(new DownloadResponseBody(proceed.body(), callback))
                .build();
    }
}

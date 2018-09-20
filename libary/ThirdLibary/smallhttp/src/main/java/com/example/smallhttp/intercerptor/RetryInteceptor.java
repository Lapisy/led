package com.example.smallhttp.intercerptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/7/27
 * Desc  :
 */

public class RetryInteceptor implements Interceptor {
    private final int retryCount;
    private final long retryTime;
    private int currentCount;

    public RetryInteceptor(int retryCount, long retryTime) {
        this.retryCount = retryCount;
        this.retryTime = retryTime;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        if (!response.isSuccessful()) {
            while (currentCount < retryCount) {
                response = chain.proceed(chain.request());
                if (response.isSuccessful())
                    break;
                currentCount++;
            }
        }

        return response;
    }
}

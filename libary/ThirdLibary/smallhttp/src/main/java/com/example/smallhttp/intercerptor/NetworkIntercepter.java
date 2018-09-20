package com.example.smallhttp.intercerptor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.smallhttp.bean.Error;
import com.example.smallhttp.exception.LiteException;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/7/21
 * Desc  : 网络是否连接的拦截器
 * retrofit 捕捉这个异常
 */

public class NetworkIntercepter implements Interceptor {
    private WeakReference<Context> wf;

    public NetworkIntercepter(Context context) {
        this.wf = new WeakReference<Context>(context);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (isNetworkConnected()) {
            return chain.proceed(chain.request());
        } else {
            throw new LiteException(Error.NETWORK_NOT_CONNECT, Error.NETWORK_NOT_CONNECT_MSG);
        }

    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) wf.get().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.getState() == NetworkInfo.State.CONNECTED);
    }
}

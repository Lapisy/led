package com.example.smallhttp.intercerptor;

import android.util.Log;

import com.example.smallhttp.bean.Error;
import com.example.smallhttp.exception.LiteException;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/7/21
 * Desc  :
 */

public abstract class LoginIntercepter implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Log.i("Test", isLogin() + "登录拦截器");

        if (isLogin()) {
            return chain.proceed(chain.request());
        } else {
            throw new LiteException(Error.NOT_LOGIN_ERROR, Error.NOT_LOGIN_ERROR_MSG);
        }

    }

    protected abstract boolean isLogin();
}

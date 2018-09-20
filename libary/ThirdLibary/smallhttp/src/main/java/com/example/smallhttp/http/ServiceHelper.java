package com.example.smallhttp.http;

import android.content.Context;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017-06-26
 * Desc  :
 */

public class ServiceHelper {
    private final Context mContext;
    private static ServiceHelper helper;
    private HashMap<String, Object> services = new HashMap<>();

    public ServiceHelper(Context context) {
        this.mContext = context.getApplicationContext();
    }


    public static ServiceHelper getInstance(Context context) {
        if (helper == null) {
            synchronized (ServiceHelper.class) {
                helper = new ServiceHelper(context);
            }
        }

        return helper;
    }

    public <T> T getService(Class<T> clazz) {
        T t = (T) services.get(clazz.getSimpleName());
        if (t != null) {
            return t;
        }
        return createService(clazz, null);
    }

    public <T> T getService(Class<T> clazz, OkHttpClient client) {
        T t = (T) services.get(clazz.getSimpleName());
        if (t != null) {
            return t;
        }
        return createService(clazz, client);
    }

    public <T> T createService(Class<T> clazz, OkHttpClient client) {
        String baseUrl = null;
        try {
            Field f = clazz.getField("BASE_URL");
            baseUrl = (String) f.get(clazz);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(baseUrl)) {
            throw new RuntimeException(clazz.getSimpleName() + ": BASE_URL can't be null.");
        }

        if (client == null) {
            client = OkHttpClicentFactory.createDefault(mContext);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        T t = retrofit.create(clazz);
        services.put(clazz.getSimpleName(), t);
        return t;

    }
}

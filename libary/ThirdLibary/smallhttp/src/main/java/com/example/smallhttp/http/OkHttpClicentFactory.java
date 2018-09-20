package com.example.smallhttp.http;

import android.content.Context;

import com.example.smallhttp.download.DownloadProgressCallback;
import com.example.smallhttp.intercerptor.DownloadProgressInterceptor;
import com.example.smallhttp.intercerptor.NetworkIntercepter;
import com.example.smallhttp.intercerptor.UploadProgressIntecepter;
import com.example.smallhttp.upload.UploadProgressCallBack;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017-06-26
 * Desc  :
 */

public class OkHttpClicentFactory {

    private static final long CONNECT_TIME_OUT = 10;
    private static final long WRITE_TIMEOUT = 10;
    private static final long READ_TIMEOUT = 10;

    public static OkHttpClient createDefault(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new NetworkIntercepter(context))
                .addInterceptor(logging)
                .build();
        return client;
    }

    public static OkHttpClient createDownloadClient(Context context, DownloadProgressCallback callback) {
        OkHttpClient aDefault = createDefault(context);
        return aDefault.newBuilder().addInterceptor(new DownloadProgressInterceptor(callback)).build();
    }

    public static OkHttpClient createUploadClient(Context context, UploadProgressCallBack callback) {
        OkHttpClient aDefault = createDefault(context);
        return aDefault.newBuilder()
                .addInterceptor(new UploadProgressIntecepter(callback))
                .build();
    }

}

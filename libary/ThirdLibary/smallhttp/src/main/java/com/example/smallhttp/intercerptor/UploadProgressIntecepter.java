package com.example.smallhttp.intercerptor;

import com.example.smallhttp.upload.UploadProgressCallBack;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/8/2
 * Desc  :
 */

public class UploadProgressIntecepter implements Interceptor {
    private final UploadProgressCallBack callback;

    public UploadProgressIntecepter(UploadProgressCallBack callBack){
        this.callback=callBack;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        return null;
    }
}

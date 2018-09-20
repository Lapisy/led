package com.example.smallhttp.http;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.smallhttp.apiservice.DownloadService;
import com.example.smallhttp.download.DownloadProgressCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import shiyiliang.cn.basetool.util.CommonUtil;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/8/2
 * Desc  :
 */

public class LHttp {
    private static LHttp http;
    private final Context mContext;

    public static LHttp getInstance(Context context) {
        if (http == null) {
            synchronized (LHttp.class) {
                http = new LHttp(context);
            }
        }
        return http;
    }

    private LHttp(Context context) {
        this.mContext = context;
    }


    /*************文件下载*******************/
    public void download(@NonNull String url, String path, DownloadProgressCallback callback) {
        CommonUtil.checkNullAndThrow(path);

        File file = new File(path);
        download(url, file, callback);
    }

    public void download(@NonNull String url, String path, String fileName, DownloadProgressCallback callback) {
        CommonUtil.checkNullAndThrow(path);
        CommonUtil.checkNullAndThrow(fileName);

        File file = new File(path, fileName);
        download(url, file, callback);
    }

    public void download(@NonNull String url, final File saveFile, final DownloadProgressCallback callback) {
        OkHttpClient client = OkHttpClicentFactory.createDownloadClient(mContext, callback);
        DownloadService service = ServiceHelper.getInstance(mContext).getService(DownloadService.class, client);

        service.download(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Func1<ResponseBody, InputStream>() {
                    @Override
                    public InputStream call(ResponseBody responseBody) {
                        return responseBody.byteStream();
                    }
                })
                .doOnNext(new Action1<InputStream>() {
                    @Override
                    public void call(InputStream inputStream) {
                        writeFile(inputStream, saveFile);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<InputStream>() {
                    @Override
                    public void onCompleted() {
                        if (callback != null)
                            callback.onSuccess(saveFile);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (callback != null)
                            callback.onFailure(e);
                    }

                    @Override
                    public void onNext(InputStream inputStream) {

                    }
                });
    }

    private void writeFile(InputStream inputStream, File saveFile) {
        FileOutputStream out = null;
        try {
            if (saveFile.exists()) {
                saveFile.delete();
            }

            out = new FileOutputStream(saveFile);
            byte[] buffer = new byte[1024];

            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}

package com.example.smallhttp.apiservice;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/7/28
 * Desc  : 断点下载
 */

public interface DownloadService {

    String BASE_URL = "http://www.baidu.com";

    @Streaming //这是一个标志，标志是否把全部数据存入到内容中，如果是大文件最好加上
    @GET
    Observable<ResponseBody> download(@Url String url);
}

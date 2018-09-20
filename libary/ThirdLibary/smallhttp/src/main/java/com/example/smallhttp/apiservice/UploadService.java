package com.example.smallhttp.apiservice;

import retrofit2.http.Url;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/8/2
 * Desc  :
 */

public interface UploadService {
    void uplaodFile(@Url String path);
}

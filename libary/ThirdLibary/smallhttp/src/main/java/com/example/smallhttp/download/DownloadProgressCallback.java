package com.example.smallhttp.download;

import java.io.File;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/6/25
 * Desc  :
 */

public interface DownloadProgressCallback {
    void updateProgress(long total, long downloadSize, boolean isComplete);

    void onSuccess(File file);

    void onFailure(Throwable throwable);
}

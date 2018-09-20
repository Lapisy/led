package com.example.smallhttp.upload;

import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/6/25
 * Desc  : 上传的回调接口
 */

public class ProgressRequestBody extends RequestBody {
    private UploadProgressCallBack callBack;
    private File file;
    private String fileName;
    private MediaType contentType;

    public ProgressRequestBody(File file, MediaType type, UploadProgressCallBack callBack) {
        this(file, type, file.getName(), callBack);
    }

    public ProgressRequestBody(File file, MediaType type, String fileName, UploadProgressCallBack callback) {
        this.contentType = type;
        this.file = file;
        this.fileName = fileName;
        this.callBack = callback;
    }

    @Override
    public long contentLength() throws IOException {
        return file.length();
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return contentType;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source = null;
        try {
            source = Okio.source(file);
            Buffer buffer = new Buffer();
            long total = contentLength();
            long readCount = 0;
            long len = 0;
            while ((len = source.read(buffer, 2048)) != -1L) {
                sink.write(buffer, len);
                readCount += len;
                //子线程调用
                if (callBack != null)
                    callBack.updateProgress(total, readCount, readCount == total);
            }
            sink.flush();
        } finally {
            Util.closeQuietly(source);
        }
    }
}

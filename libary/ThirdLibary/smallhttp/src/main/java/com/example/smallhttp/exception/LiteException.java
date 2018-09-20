package com.example.smallhttp.exception;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/7/21
 * Desc  : 网络未连接的异常
 */

public class LiteException extends RuntimeException {

    private final int code;
    private final String message;

    public LiteException(int code, String msg) {
        this.code=code;
        this.message=msg;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

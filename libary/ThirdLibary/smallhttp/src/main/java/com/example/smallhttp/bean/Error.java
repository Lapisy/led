package com.example.smallhttp.bean;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/8/23
 * Desc  :
 */

public class Error {
    public static final int NETWORK_NOT_CONNECT = 0X10000;//0x10000 表示网络没有连接的错误
    public static final int UNHNOW_ERROR = 0X10001;//0x10000 表示网络没有连接的错误
    public static final int NOT_LOGIN_ERROR = 0X10002;//0x10000 表示网络没有连接的错误


    public static final String NETWORK_NOT_CONNECT_MSG = "网络没有连接";//表示网络没有连接的错误
    public static final String UNHNOW_ERROR_MSG = "未知错误";//表示网络没有连接的错误
    public static final String NOT_LOGIN_ERROR_MSG = "未登录";//表示网络没有连接的错误

    private int errorCode;
    private String errorMsg;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}

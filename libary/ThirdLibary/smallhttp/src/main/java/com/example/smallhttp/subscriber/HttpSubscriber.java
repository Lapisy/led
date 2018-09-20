package com.example.smallhttp.subscriber;

import com.example.smallhttp.bean.HttpResult;
import com.example.smallhttp.exception.LiteException;

import rx.Subscriber;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017-06-26
 * Desc  :
 */

public abstract class HttpSubscriber<T> extends Subscriber<HttpResult<T>> {
    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
//        e.printStackTrace();
        onFailure(e);
    }

    @Override
    public void onNext(HttpResult<T> tHttpResult) {
//        if (isError(tHttpResult)) {
//            onFailure(new LiteException(tHttpResult.resultCode, tHttpResult.getResultMsg()));
//        } else {
//            onSuccess(tHttpResult.getData());
//        }
    }

    //判断是否错误
    protected boolean isError(HttpResult<T> result) {
        return (result != null && result.getResultCode() >= 1 && result.getResultCode() <= 100);
    }


    public abstract void onSuccess(T t);

    //// TODO: 2017-06-26 这里的错误传入的是 Throwable吗？需要传SmallHttpException不？
    public abstract void onFailure(Throwable e);
}

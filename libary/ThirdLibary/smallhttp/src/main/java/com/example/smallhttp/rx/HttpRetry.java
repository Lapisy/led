package com.example.smallhttp.rx;

import com.example.smallhttp.util.HttpLog;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/7/27
 * Desc  : 重试机制类
 */

public class HttpRetry implements Func1<Observable<? extends Throwable>, Observable<?>> {

    private final int retryCount;
    private final long retryTime;
    private int currentCount;

    public HttpRetry(int retryCount, long retryTime) {
        this.retryCount = retryCount;
        this.retryTime = retryTime;
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> observable) {
        return observable.flatMap(new Func1<Throwable, Observable<?>>() {
            @Override
            public Observable<?> call(Throwable throwable) {
                HttpLog.log("调用了 " + this.getClass().getName());
                currentCount++;
                if (retrySuccess() && currentCount <= retryCount) {
                    return Observable.timer(retryTime, TimeUnit.SECONDS);
                }
                return Observable.error(throwable);
            }
        });
    }

    /**
     * 是否重试成功,默认是一直返回true
     *
     * @return
     */
    protected boolean retrySuccess() {
        return true;
    }
}

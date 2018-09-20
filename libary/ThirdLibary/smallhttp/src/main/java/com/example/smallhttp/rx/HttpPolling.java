package com.example.smallhttp.rx;

import com.example.smallhttp.util.HttpLog;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/7/27
 * Desc  : 这个类主要实现重复请求，类似repeat
 */

public class HttpPolling implements Func1<Observable<? extends Void>, Observable<?>> {
    private final int repeatCount; //重复的次数
    private final long startTime;  //起始的时间

    public HttpPolling(int repeatCount, long startTime) {
        this.repeatCount = repeatCount;
        this.startTime = startTime;
    }

    @Override
    public Observable<?> call(Observable<? extends Void> observable) {
        return observable.zipWith(Observable.range(1, repeatCount + 1), new Func2<Void, Integer, Integer>() {
            @Override
            public Integer call(Void aVoid, Integer attempt) {
                HttpLog.log("zipWith, call, attempt " + attempt);
                return attempt;
            }
        }).flatMap(new Func1<Integer, Observable<?>>() {
            @Override
            public Observable<?> call(Integer repeatAttempt) {
                HttpLog.log("flatMap, call, repeatAttempt " + repeatAttempt);
                // 增加等待时间,这里的算法待定，可以适当的修改
                return Observable.timer(getTryTime(repeatAttempt, startTime), TimeUnit.SECONDS);
            }
        });
    }

    /**
     * 这里可以适当的修改每次生成时间的算法
     * @param tryCount
     * @param startTime
     * @return
     */
    protected long getTryTime(int tryCount, long startTime) {
        return tryCount * startTime;
    }
}

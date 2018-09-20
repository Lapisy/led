package shiyiliang.cn.basetool;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import shiyiliang.cn.basetool.inter.IOTask;
import shiyiliang.cn.basetool.inter.Task;
import shiyiliang.cn.basetool.inter.UITask;


/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/7/8
 * Desc  : 任务的线程切换
 */

public class RxScheduler {
    public static <T> void doOnUiThread(final UITask<T> task) {
        Observable.just(task)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UITask<T>>() {
                    @Override
                    public void call(UITask<T> tuiTask) {
                        task.doOnUIThread();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    public static <T> void doOnIOThread(final IOTask<T> task) {
        Observable.just(task)
                .observeOn(Schedulers.io())
                .subscribe(new Action1<IOTask<T>>() {
                    @Override
                    public void call(IOTask<T> tioTask) {
                        tioTask.doOnIOThread();
                        ;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }


    public static <T> void doTask(final Task<T> task) {
        Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                task.doOnIOThread();
                subscriber.onNext(task.getT());
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<T>() {
                    @Override
                    public void call(T t) {
                        task.doOnUIThread(true, null);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        task.doOnUIThread(false, throwable);
                        throwable.printStackTrace();
                    }
                });
    }


}

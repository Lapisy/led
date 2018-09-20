package shiyiliang.cn.basetool.inter;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/7/8
 * Desc  :
 */

public abstract class Task<T> {
    private T t;

    public Task(T t) {
        this.t = t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public T getT() {
        return t;
    }

    public abstract void doOnIOThread();

    /**
     * @param sucess 表示在IO执行的任务是否成功。true，成功
     */
    public abstract void doOnUIThread(boolean sucess,Throwable e);
}

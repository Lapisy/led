package shiyiliang.me.baselibary.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.RxFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import shiyiliang.me.baselibary.bean.NetworkStatueEvent;
/**
 * @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
 * Bundle savedInstanceState)
 * {
 * Log.i(TAG, "onCreateView");
 * <p>
 * if (rootView == null)
 * {
 * rootView = inflater.inflate(R.layout.fragment_1, null);
 * }
 * // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
 * ViewGroup parent = (ViewGroup) rootView.getParent();
 * if (parent != null)
 * {
 * parent.removeView(rootView);
 * }
 * return rootView;
 * }
 */

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017-06-16
 * Desc  :
 */

public abstract class BaseFragment extends RxFragment {
    protected Activity mContext;
    protected View mView;
    private Unbinder unBinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //// TODO: 2017/6/16 这个地方mview是否可以缓存，原理是什么？
        mView = inflater.inflate(getLayoutID(), container, false);
        unBinder = ButterKnife.bind(this, mView);
        EventBus.getDefault().register(this);
        init(mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listenNetworkStatueChange(NetworkStatueEvent event) {
        if (event.isConnected()) {
            networkConnected();
        } else {
            networkDisconnected();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unBinder.unbind();
    }

    protected abstract int getLayoutID();

    protected abstract void init(View mView);

    protected abstract void networkDisconnected();

    protected abstract void networkConnected();

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }
}

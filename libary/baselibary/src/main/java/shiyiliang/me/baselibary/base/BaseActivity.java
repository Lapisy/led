package shiyiliang.me.baselibary.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import shiyiliang.me.baselibary.bean.NetworkStatueEvent;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017-06-16
 * Desc  :
 */

public abstract class BaseActivity extends RxAppCompatActivity {
    protected Activity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());

        //设置是否显示状态栏
        if(!isShowActionBar()){
            ActionBar supportActionBar = this.getSupportActionBar();
            if(supportActionBar!=null){
                supportActionBar.hide();
            }
        }

        this.mContext = this;
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);

        //这里必须是在 ButterKnife.bind(this);后面调用，否则view的使用都是空
        init();

    }

    protected boolean isShowActionBar(){
        return false;
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    protected abstract int getLayoutID();

    protected abstract void init();

    protected abstract void networkDisconnected();

    protected abstract void networkConnected();
}

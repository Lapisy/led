package shiyiliang.me.baselibary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;

import shiyiliang.me.baselibary.bean.NetworkStatueEvent;
import shiyiliang.cn.basetool.util.NetworkUtil;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017-06-16
 * Desc  : 接受网络状态改变的广播
 *         1. 可以通过service来启动一个receiver
 *         2. 静态的注册一个service
 */

public class NetworkReceiver extends BroadcastReceiver {
    public final static String ANDROID_CUSTOM_ACTION = "android";
    private final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    @Override
    public void onReceive(Context context, Intent intent) {
        handlerIntent(context, intent);
    }

    private void handlerIntent(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION)) {
            NetworkStatueEvent event = new NetworkStatueEvent();
            if (NetworkUtil.isConnectedByState(context)) {
                event.setConnected(true);
            } else {
                event.setConnected(false);
            }
            EventBus.getDefault().post(event);
        }
    }
}

package led.lapisy.com.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import led.lapisy.com.AppConstant;
import led.lapisy.com.bluetooth.BluetoothManager;
import led.lapisy.com.bluetooth.NotifyCallback;
import led.lapisy.com.util.DataUtil;
import shiyiliang.cn.basetool.util.LogUtil;

public class BatteryService extends Service {
    private int power;

    public static void start(Context context) {
        Intent intent = new Intent(context, BatteryService.class);
        context.startService(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.i("启动service了");
        //开始监听
        startNotify();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startNotify();
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startNotify() {
        BluetoothManager.getInstance(this)
                .notify(new NotifyCallback() {
                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        LogUtil.i("startNotify(),"+DataUtil.bytes2String(data));
                        Intent intent = new Intent();
                        intent.setAction(AppConstant.BATTERY_ACTION);
                        intent.putExtra(AppConstant.BATTERY_POWER, DataUtil.bytes2String(data));
                        LocalBroadcastManager.getInstance(BatteryService.this).sendBroadcast(intent);
                    }
                });

    }
}

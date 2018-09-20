package led.lapisy.com.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import led.lapisy.com.AppConstant;
import led.lapisy.com.bluetooth.BluetoothManager;
import led.lapisy.com.led.R;
import led.lapisy.com.service.CopyIntentService;
import led.lapisy.com.util.DialogUtil;
import shiyiliang.cn.basetool.util.AppUtil;
import shiyiliang.cn.basetool.util.LogUtil;
import shiyiliang.cn.basetool.util.SPUtil;
import shiyiliang.cn.basetool.util.ToastUtil;

public class SplashActivity extends BaseActivity {
    private static final String APP_FIRST_RUN = "first_run";
    private static final long DELAY_TIME = 1000;
    private static final int BLUETOOTH_OPEN_CODE = 300;
    private static final String TAG = "SplashActivity";
    private Handler mHandler = new Handler();

    @BindView(R.id.iv_splash_icon)
    ImageView ivSplashIcon;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        //set background
        Glide.with(mContext).load(R.drawable.ic_splash)
                .crossFade()
                .into(ivSplashIcon);

        RxPermissions rxPermissions = new RxPermissions(this);
        Disposable subscribe = rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        LogUtil.i(TAG, permission.granted + "---" + permission.name);
                        if (permission.granted && permission.name.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            //开始复制assets
                            CopyIntentService.startService(mContext, AppConstant.ASSETS_BACKGROUND_A, AppConstant.ASSETS_BACKGROUND_B);
                        } else if (permission.granted && permission.name.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {

                        } else {
                            ToastUtil.shortToast(mContext, R.string.gps_not_open_message);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtil.i("accept");
                        checkAndOpenBluetooth();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        LogUtil.i("Action");
                        checkAndOpenBluetooth();
                    }
                });
        mCompositeDisposable.add(subscribe);

        //打开蓝牙
        //checkAndOpenBluetooth();
    }

    /**
     * 检查蓝牙，打开蓝牙
     */
    private void checkAndOpenBluetooth() {
        if (!BluetoothManager.isSupportBle(mContext)) {
            //弹出不支持ble的弹框
            DialogUtil.showDialog(mContext, 0, R.string.not_support_ble, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AppUtil.killApplication();
                }
            }, null);
        } else {
            if (!BluetoothManager.bluetoothIsOpen()) {
                BluetoothManager.openBluetooth(mContext, BLUETOOTH_OPEN_CODE, false);
            } else {
                //如果蓝牙打开了,就开始跳转
                whetherFirst();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLUETOOTH_OPEN_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                //如果蓝牙没有打开，那么直接关闭应用
                this.finish();
                AppUtil.killApplication();
            } else {
                whetherFirst();
            }
        }
    }


    private void whetherFirst() {
        //跳转判断
        boolean isFirstRun = (boolean) SPUtil.get(mContext, APP_FIRST_RUN, true);
        if (isFirstRun) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SPUtil.put(mContext, APP_FIRST_RUN, false);
                    startGoTo();
                }
            }, DELAY_TIME);
        } else {
            startGoTo();
        }
    }

    private void startGoTo() {
        AppUtil.goActivity(mContext, MainActivity.class);
        this.finish();
    }


}

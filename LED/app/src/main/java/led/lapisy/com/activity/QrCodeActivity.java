package led.lapisy.com.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothGatt;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;


import java.util.List;

import io.reactivex.Observable;
import led.lapisy.com.AppConstant;
import led.lapisy.com.bluetooth.BluetoothManager;
import led.lapisy.com.bluetooth.BluetoothScanAndConnectCallback;
import led.lapisy.com.led.R;
import led.lapisy.com.service.BatteryService;
import led.lapisy.com.util.DataUtil;
import led.lapisy.com.util.DialogUtil;
import led.lapisy.com.util.ScanRecord;
import shiyiliang.cn.basetool.util.LogUtil;
import shiyiliang.cn.basetool.util.ToastUtil;

public class QrCodeActivity extends BaseActivity {
    private static final String TAG = "QrCodeActivity";
    private Dialog mProgressDialog;
    private boolean mLightOn;
    private CaptureFragment captureFragment;
    private Handler mHandler = new Handler();
    private AlertDialog mReScanDialog;
    private Handler mDelayHandler = new Handler();

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_qr_code;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitleText(R.string.scan);
        //执行扫面Fragment的初始化操作
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.scan_fragment);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.id_container, captureFragment).commit();
        //判断当前是否链接
        if (BluetoothManager.getInstance(mContext).isConnected()) {
            DialogUtil.showDialog(mContext, 0, R.string.str_reconnect, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    BluetoothManager.getInstance(mContext).stopConnected();
                    //延迟100ms，重新链接
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mContext.finish();
                }
            });
        }
    }

    /**
     * 二维码回调
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            //ToastUtil.shortToast(mContext.getApplicationContext(), result);
            //captureFragment.getHandler().restartPreviewAndDecode();
            //停止扫描
//            if (captureFragment.getHandler() != null) {
//                captureFragment.getHandler().stopScan();
//            }
            //链接蓝牙
            scanAndConnect(result);
        }

        @Override
        public void onAnalyzeFailed() {
            ToastUtil.shortToast(mContext.getApplicationContext(), "scan failure.");
            mContext.finish();
        }
    };

    //
    private void scanAndConnect(final String result) {
        if (TextUtils.isEmpty(result))
            return;
        //开始扫描设备
        GodenChildBleScanCallback mGodenChildBleScanCallback = new GodenChildBleScanCallback(result);
        BluetoothManager.getInstance(mContext).scan(mGodenChildBleScanCallback);
    }

    private void showDialog() {
        mProgressDialog = DialogUtil.showProgressDialog(mContext, R.string.str_scaning);
        mProgressDialog.setCanceledOnTouchOutside(false);
        //mProgressDialog.setCancelable(false);
    }

    private void dismissDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.flashing) {
            mLightOn = !mLightOn;
            CodeUtils.isLightEnable(!mLightOn);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防止内存泄漏
        if (null != captureFragment) {
            captureFragment.setAnalyzeCallback(null);
            captureFragment = null;
            analyzeCallback = null;
        }

        //退出时，取消所有的没有完成的扫描
        if (!BluetoothManager.getInstance(mContext).isConnected()) {
            //BluetoothManager.getInstance(mContext).stopConnected();
            //BluetoothManager.getInstance(mContext).stopScan();
        }
    }


    //显示是否重新扫描二维码的弹框
    private void showReScanDialog() {
        mReScanDialog = DialogUtil.showDialog(mContext, 0, R.string.re_scan, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mReScanDialog.dismiss();
                //重新扫描
                if (captureFragment.getHandler() != null) {
                    captureFragment.getHandler().startScan();
                }
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mContext.finish();
            }
        });
        mReScanDialog.setCanceledOnTouchOutside(false);
        //mReScanDialog.setCancelable(false);
    }

    /*************************CLASS*******************************/

    /**
     * 蓝牙扫描
     */
    class GodenChildBleScanCallback extends BleScanCallback {
        private String scanQrResult;

        public GodenChildBleScanCallback() {

        }

        public GodenChildBleScanCallback(String result) {
            this.scanQrResult = result;
        }

        @Override
        public void onScanFinished(List<BleDevice> scanResultList) {
            //如果没有找到设备，就不会去链接，则停止dialog
            if (BluetoothManager.getInstance(mContext).getBleDevice() == null) {
                dismissDialog();
                //弹出重新扫描弹框
                showReScanDialog();
            }
        }

        @Override
        public void onScanStarted(boolean success) {
            showDialog();
        }

        @Override
        public void onScanning(BleDevice bleDevice) {
            LogUtil.i(TAG, "GodenChildBleScanCallback ,onScanning," + Thread.currentThread().getName() +
                    ",getScanRecord=" + DataUtil.bytesToHex(bleDevice.getScanRecord()));
            if (bleDevice != null && bleDevice.getScanRecord() != null) {
                String scanRecord = DataUtil.bytesToHex(bleDevice.getScanRecord()).toLowerCase();
                if (scanRecord != null && scanRecord.contains(scanQrResult.toLowerCase())) {
                    //cancle scan
                    BleManager.getInstance().cancelScan();
                    //connect device
                    BluetoothManager.getInstance(mContext).setBleDevice(bleDevice);
                    GodenChildBleGattCallback mGodenChildBleGattCallback = new GodenChildBleGattCallback();
                    BluetoothManager.getInstance(mContext).connect(bleDevice, mGodenChildBleGattCallback);
                }
            }
        }
    }

    /**
     * 蓝牙链接回调类
     */
    class GodenChildBleGattCallback extends BleGattCallback {

        @Override
        public void onStartConnect() {

        }

        @Override
        public void onConnectFail(BleDevice bleDevice, BleException exception) {
            dismissDialog();
            showReScanDialog();
        }

        @Override
        public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
            BluetoothManager.getInstance(mContext).setBluetoothGatt(gatt);
            //扫描成功后，再一次启动电量监听，触发onStartCommand
            BatteryService.start(mContext);
            //这里延时2s，防止立即去操作设备,设备还没有准备好
            mDelayHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismissDialog();
                    ToastUtil.shortToast(mContext, "Connect Success");
                    //finish
                    mContext.finish();
                }
            }, 2000);
        }

        @Override
        public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
            ToastUtil.shortToast(mContext, "Disconnected");
        }
    }

}

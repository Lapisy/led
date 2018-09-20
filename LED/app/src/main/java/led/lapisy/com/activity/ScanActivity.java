//package led.lapisy.com.activity;
//
//import android.app.Dialog;
//import android.bluetooth.BluetoothGatt;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
//import android.view.MenuItem;
//
//import com.clj.fastble.data.BleDevice;
//import com.clj.fastble.exception.BleException;
//
//
//import cn.bingoogolapple.qrcode.core.QRCodeView;
//import cn.bingoogolapple.qrcode.zxing.ZXingView;
//import led.lapisy.com.bluetooth.BluetoothManager;
//import led.lapisy.com.bluetooth.BluetoothScanAndConnectCallback;
//import led.lapisy.com.led.R;
//import led.lapisy.com.service.BatteryService;
//import led.lapisy.com.util.DialogUtil;
//import shiyiliang.cn.basetool.util.LogUtil;
//import shiyiliang.cn.basetool.util.ToastUtil;
//
//public class ScanActivity extends BaseActivity implements QRCodeView.Delegate {
//
//    private static final String TAG = "ScanActivity";
//    private ZXingView mZXingView;
//    private Dialog mProgressDialog;
//    private boolean isOpenFlashing;
//
//    @Override
//    protected int getLayoutResID() {
//        return R.layout.activity_scan;
//    }
//
//    @Override
//    protected void init(Bundle savedInstanceState) {
//        setTitleText(R.string.scan);
//
//        mZXingView = (ZXingView) findViewById(R.id.zxingview);
//        mZXingView.setDelegate(this);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.flashing) {
//            isOpenFlashing = !isOpenFlashing;
//            if (isOpenFlashing)
//                mZXingView.openFlashlight();
//            else
//                mZXingView.closeFlashlight();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_scan, menu);
//        return true;
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        startQR();
//    }
//
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        stopQR();
//    }
//
//    @Override
//    protected void onDestroy() {
//        mZXingView.onDestroy();
//        super.onDestroy();
//    }
//
//    private void startQR() {
//        mZXingView.showScanRect();
//        mZXingView.startCamera();
//        mZXingView.startSpot();
//    }
//
//    private void stopQR() {
//        mZXingView.stopSpot();
//        mZXingView.stopCamera();
//        mZXingView.hiddenScanRect();
//    }
//
//    @Override
//    public void onScanQRCodeSuccess(String result) {
//        LogUtil.i(result);
//        mZXingView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mZXingView.startSpot();
//            }
//        }, 1000);
////        BluetoothManager.getInstance(mContext).setScanDeviceMac(result);
////        showDialog();
////        //如果已经连接，则弹出一个框表示是否需要重新连接
////        if (BluetoothManager.getInstance(mContext).isConnected()) {
////            DialogUtil.showDialog(mContext, 0, R.string.str_reconnect, new DialogInterface.OnClickListener() {
////                @Override
////                public void onClick(DialogInterface dialog, int which) {
////                    scanAndConnect();
////                }
////            }, null);
////        } else {
////            scanAndConnect();
////        }
//    }
//
//    private void scanAndConnect() {
//        if (BluetoothManager.getInstance(mContext).isConnected())
//            BluetoothManager.getInstance(mContext).stopConnected();
//        //开始扫描设备
//        BluetoothManager.getInstance(mContext)
//                .scanAndConnect(new BluetoothScanAndConnectCallback() {
//                    @Override
//                    public void onConnectFail(BleException exception) {
//                        super.onConnectFail(exception);
//                        dismissDialog();
//                        System.out.println("onConnectFail");
//                        //扫描框回复扫描功能
//                        startQR();
//                    }
//
//                    @Override
//                    public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
//                        //停止扫描
//                        stopQR();
//                        dismissDialog();
//                        mContext.finish();
//                        ToastUtil.shortToast(mContext, "Connect Success");
//                        //扫描成功后，再一次启动电量监听，触发onStartCommand
//                        startService(new Intent(mContext, BatteryService.class));
//                    }
//
//                    @Override
//                    public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
//                        ToastUtil.shortToast(mContext, "Disconnected");
//                    }
//                });
//    }
//
//    private void showDialog() {
//        mProgressDialog = DialogUtil.showProgressDialog(mContext, R.string.str_scaning);
//    }
//
//    private void dismissDialog() {
//        if (mProgressDialog != null && mProgressDialog.isShowing())
//            mProgressDialog.dismiss();
//    }
//
//    @Override
//    public void onScanQRCodeOpenCameraError() {
//        ToastUtil.shortToast(mContext, R.string.camera_open_fail);
//    }
//}

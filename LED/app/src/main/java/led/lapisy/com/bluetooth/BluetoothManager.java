package led.lapisy.com.bluetooth;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleScanAndConnectCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.clj.fastble.utils.HexUtil;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import led.lapisy.com.led.R;
import shiyiliang.cn.basetool.util.ToastUtil;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2018/4/1
 * Desc  :
 */

public class BluetoothManager {
    public final static String DEVICE_NAME = "MOOYEE";
    public final static String DEVICE_MAC = "AC:01:78:E6:34:FB";
//  public final static String DEVICE_MAC = "F3:3F:1C:06:82:F1";

    //service uuid
    public final static String SERVICE_UUID = "0000ffe0-0000-1000-8000-00805f9b34fb";
    public final static String WRITE_CHARACTERISTIC_UUID = "0000ffe2-0000-1000-8000-00805f9b34fb";
    public final static String READ_CHARACTERISTIC_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public final static int SCAN_TIME_OUT = 10000;

    private static BluetoothManager manager;
    private static BleDevice mBleDevice;
    private static BluetoothDevice mBluetoothDevice;
    private static BleScanRuleConfig mBleScanRuleConfig;
    private final Context mContext;
    private BluetoothGatt mBluetoothGatt;


    public static BluetoothManager getInstance(Context context) {
        if (null == manager) {
            synchronized (BluetoothManager.class) {
                if (null == manager)
                    manager = new BluetoothManager(context);
            }
        }
        return manager;
    }

    /**
     * 初始化蓝牙库
     *
     * @param pApplication
     */
    public static void init(Application pApplication) {
        BleManager.getInstance().init(pApplication);
        BleManager.getInstance()
                .enableLog(true)
                .setConnectOverTime(5000) //链接超时时间，默认是10s
                .setReConnectCount(1, 100)
                .setOperateTimeout(5000);
    }

    public void setScanDeviceMac(String mac) {
        if (TextUtils.isEmpty(mac))
            return;
        mBleScanRuleConfig = new BleScanRuleConfig.Builder()
                .setDeviceMac(mac)
                .setServiceUuids(new UUID[]{UUID.fromString(SERVICE_UUID)})
                .setScanTimeOut(SCAN_TIME_OUT)
                .build();
        BleManager.getInstance().initScanRule(mBleScanRuleConfig);
    }

    private BluetoothManager(Context context) {
        this.mContext = context.getApplicationContext();
    }

    /**
     * 是否支持ble
     *
     * @param pContext
     * @return
     */
    public static boolean isSupportBle(Context pContext) {
        return BleManager.getInstance().isSupportBle();
    }

    /**
     * 蓝牙是否打开
     *
     * @return
     */
    public static boolean bluetoothIsOpen() {
        return BleManager.getInstance().isBlueEnable();
    }

    /**
     * 打开蓝牙：异步打开或者强制打开
     *
     * @param pContext
     * @param pCode
     * @param pAsync
     */
    public static void openBluetooth(Activity pContext, int pCode, boolean pAsync) {
        if (!bluetoothIsOpen()) {
            if (pAsync) {
                BleManager.getInstance().enableBluetooth();
            } else {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                pContext.startActivityForResult(intent, pCode);
            }
        }
    }

    /**
     * 扫描并连接
     *
     * @param pCallback
     */
    public void scanAndConnect(final BluetoothScanAndConnectCallback pCallback) {
        if (null == pCallback)
            throw new RuntimeException(pCallback.getClass().getSimpleName() + " is null");
        BleManager.getInstance().scanAndConnect(new BleScanAndConnectCallback() {
            @Override
            public void onScanStarted(boolean success) {
                pCallback.onScanStarted(success);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                pCallback.onScanning(bleDevice);
            }

            @Override
            public void onScanFinished(BleDevice scanResult) {
                pCallback.onScanFinished(scanResult);
            }

            @Override
            public void onStartConnect() {
                pCallback.onStartConnect();
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                pCallback.onConnectFail(bleDevice, exception);
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                setBleDevice(bleDevice);
                pCallback.onConnectSuccess(bleDevice, gatt, status);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                pCallback.onDisConnected(isActiveDisConnected, device, gatt, status);
            }
        });
    }

    /**
     * 扫描
     *
     * @param callback
     */
    public void scan(BleScanCallback callback) {
        BleManager.getInstance().scan(callback);
    }


    /**
     * 链接设备
     *
     * @param device
     * @param callback
     */
    public void connect(BleDevice device, BleGattCallback callback) {
        BleManager.getInstance().connect(device, callback);
    }

    /**
     * 链接设备
     *
     * @param device
     * @param callback
     */
    public void connect(String device, BleGattCallback callback) {
        BleManager.getInstance().connect(device, callback);
    }

    /**
     * 写数据
     *
     * @param pData
     * @param pCallback
     */

    public void write(String pData, final WriteCallback pCallback) {
        if (TextUtils.isEmpty(pData))
            return;

        if (!isConnected()) {
            ToastUtil.shortToast(mContext, R.string.str_not_connect);
        } else {
            write(HexUtil.hexStringToBytes(pData), pCallback);
        }
    }

    public void write(byte[] pData, final WriteCallback pCallback) {
        if (null == pCallback)
            throw new RuntimeException(pCallback.getClass().getSimpleName() + "  is null");

        if (!isConnected()) {
            ToastUtil.shortToast(mContext, R.string.str_not_connect);
        } else {
            BleManager.getInstance().write(getBleDevice(), BluetoothManager.SERVICE_UUID, BluetoothManager.WRITE_CHARACTERISTIC_UUID, pData, new BleWriteCallback() {
                @Override
                public void onWriteSuccess(int current, int total, byte[] justWrite) {
                    pCallback.onWriteSuccess(current, total, justWrite);
                }

                @Override
                public void onWriteFailure(BleException exception) {
                    pCallback.onWriteFailure(exception);
                }
            });
        }
    }


    /**
     * 读取数据
     *
     * @param pCallback
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void read(final ReadCallback pCallback) {
        Objects.requireNonNull(pCallback);
        if (!isConnected()) {
            ToastUtil.shortToast(mContext, R.string.str_not_connect);
        } else {
            BleManager.getInstance().read(getBleDevice(), BluetoothManager.SERVICE_UUID, BluetoothManager.READ_CHARACTERISTIC_UUID, new BleReadCallback() {
                @Override
                public void onReadSuccess(byte[] data) {
                    pCallback.onReadSuccess(data);
                }

                @Override
                public void onReadFailure(BleException exception) {
                    onReadFailure(exception);
                }
            });
        }
    }


    /**
     * 订阅消息
     *
     * @param callback
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void notify(final NotifyCallback callback) {
        Objects.requireNonNull(callback);
        if (!isConnected()) {
            ToastUtil.shortToast(mContext, R.string.str_not_connect);
        } else {
            BleManager.getInstance().notify(getBleDevice(), SERVICE_UUID, READ_CHARACTERISTIC_UUID, new BleNotifyCallback() {
                @Override
                public void onNotifySuccess() {
                    callback.onNotifySuccess();
                }

                @Override
                public void onNotifyFailure(BleException exception) {
                    callback.onNotifyFailure(new Exception(exception.getDescription()));
                }

                @Override
                public void onCharacteristicChanged(byte[] data) {
                    callback.onCharacteristicChanged(data);
                }
            });
        }
    }

    public void stopNotify() {
        if (isConnected()) {
            BleManager.getInstance().stopNotify(getBleDevice(), SERVICE_UUID, READ_CHARACTERISTIC_UUID);
        }
    }

    /**
     * @param pGatt
     */
    public void setBluetoothGatt(BluetoothGatt pGatt) {
        if (null == pGatt)
            return;
        mBluetoothGatt = pGatt;
    }

    public BluetoothGatt getBluetoothGatt() {
        return BleManager.getInstance().getBluetoothGatt(getBleDevice());
    }

    /**
     * 设置设备
     *
     * @param pDevice
     */
    public void setBleDevice(BleDevice pDevice) {
        if (null == pDevice)
            return;
        mBleDevice = pDevice;
    }

    public BleDevice getBleDevice() {
        return mBleDevice;
    }

    /**
     * 指定设备是否连接
     *
     * @return
     */
    public boolean isConnected() {
        return BleManager.getInstance().isConnected(getBleDevice());
    }

    /**
     * 停止扫描
     */
    public void stopScan() {
        BleManager.getInstance().cancelScan();
    }

    /**
     * 断开连接
     */
    public void stopConnected() {
        BleManager.getInstance().disconnect(getBleDevice());
        setBleDevice(null);
        setBluetoothGatt(null);
    }

    public void stopAllConnected() {
        BleManager.getInstance().disconnectAllDevice();
    }

    /**
     * 清除所有资源
     */
    public void destroy() {
        BleManager.getInstance().destroy();
    }
}

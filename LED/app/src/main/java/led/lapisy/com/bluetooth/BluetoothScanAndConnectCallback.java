package led.lapisy.com.bluetooth;

import com.clj.fastble.callback.BleScanAndConnectCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2018/4/4
 * Desc  :
 */

public abstract class BluetoothScanAndConnectCallback extends BleScanAndConnectCallback {
    @Override
    public void onScanStarted(boolean success) {

    }

    @Override
    public void onScanFinished(BleDevice scanResult) {

    }

    @Override
    public void onStartConnect() {

    }



}

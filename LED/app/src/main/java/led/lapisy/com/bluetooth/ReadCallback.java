package led.lapisy.com.bluetooth;

import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.exception.BleException;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2018/4/4
 * Desc  :
 */

public class ReadCallback extends BleReadCallback {
    @Override
    public void onReadSuccess(byte[] data) {

    }

    @Override
    public void onReadFailure(BleException exception) {

    }
}

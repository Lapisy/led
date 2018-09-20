package led.lapisy.com.bluetooth;

import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.exception.BleException;

public abstract class NotifyCallback {

    public void onNotifySuccess() {

    }


    public void onNotifyFailure(Exception exception) {

    }

    public abstract void onCharacteristicChanged(byte[] data);
}

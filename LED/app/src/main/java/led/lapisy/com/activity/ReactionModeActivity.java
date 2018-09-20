package led.lapisy.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.clj.fastble.exception.BleException;

import butterknife.BindView;
import led.lapisy.com.AppConstant;
import led.lapisy.com.LedApplication;
import led.lapisy.com.bluetooth.BluetoothManager;
import led.lapisy.com.bluetooth.WriteCallback;
import led.lapisy.com.led.R;
import led.lapisy.com.util.DataUtil;
import led.lapisy.com.view.VerticalSeekBar;
import shiyiliang.cn.basetool.util.LogUtil;
import shiyiliang.cn.basetool.util.SPUtil;
import shiyiliang.cn.basetool.util.ToastUtil;

public class ReactionModeActivity extends BaseActivity {
    private final String TAG = "ReactionModeActivity";
    @BindView(R.id.vsb_color)
    VerticalSeekBar mVerticalSeekBar;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_reaction_mode;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitleText(R.string.reaction_mode);
        initVerticalSeekBar();
    }

    private void initVerticalSeekBar() {
        float persend = (float) SPUtil.get(mContext, AppConstant.SP_VERTICAL_SEEKBAR, 0f);
        //开始的时候请求数据
        int oldValue = 10 - (int) ((10 * persend + 0.5f) % 11);
        writeData(oldValue);
        final byte[] data = DataUtil.packageReactionModeData(oldValue);
        LogUtil.i(DataUtil.bytes2String(data));

        //更新seekbar的值
        mVerticalSeekBar.setProgress(persend);
        mVerticalSeekBar.setOnSlideListener(new VerticalSeekBar.OnSlideListener() {
            @Override
            public void onSlide(float percent) {

            }

            @Override
            public void onSlideComplete(float percent) {
                LogUtil.i(TAG, percent);
                //保存记录
                SPUtil.put(mContext, AppConstant.SP_VERTICAL_SEEKBAR, percent);
                int value = 10 - (int) ((10 * percent + 0.5f) % 11);
                writeData(value);
            }
        });
    }

    //更新状态
    private void updateMenu() {
        LedApplication.openType = 2;
        Intent intent = new Intent(AppConstant.UPDATE_MENU_ACTION);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    private void writeData(int value) {
        if (BluetoothManager.getInstance(mContext).isConnected()) {
            //更新状态
            updateMenu();
            //这里的值只能是0-10变化,开始写数据
            final byte[] data = DataUtil.packageReactionModeData(value);
            LogUtil.i(DataUtil.bytes2String(data));
            BluetoothManager.getInstance(mContext)
                    .write(data, new WriteCallback() {
                        @Override
                        public void onWriteSuccess(int current, int total, byte[] justWrite) {
                            LogUtil.i("current: " + current + " total: " + total + " Data: " +
                                    DataUtil.bytes2String(justWrite));
                        }

                        @Override
                        public void onWriteFailure(BleException exception) {
                            LogUtil.i(exception.getDescription());
                        }
                    });
        } else {
            ToastUtil.shortToast(mContext, R.string.str_not_connect);
        }
    }
}

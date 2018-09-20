package led.lapisy.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.clj.fastble.exception.BleException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import led.lapisy.com.AppConstant;
import led.lapisy.com.LedApplication;
import led.lapisy.com.adapter.LightMenuAdapter;
import led.lapisy.com.adapter.MainMenuAdapter;
import led.lapisy.com.bean.Menu;
import led.lapisy.com.bluetooth.BluetoothManager;
import led.lapisy.com.bluetooth.WriteCallback;
import led.lapisy.com.led.R;
import led.lapisy.com.util.DataUtil;
import led.lapisy.com.view.CircularSlider;
import led.lapisy.com.view.colorpickerview.ColorEnvelope;
import led.lapisy.com.view.colorpickerview.ColorListener;
import led.lapisy.com.view.colorpickerview.ColorPickerView;
import shiyiliang.cn.basetool.util.LogUtil;
import shiyiliang.cn.basetool.util.SPUtil;
import shiyiliang.cn.basetool.util.ToastUtil;

public class LightActivity extends BaseActivity {
    private List<Menu> mData = new ArrayList<>();

    @BindView(R.id.colorPickerView)
    ColorPickerView mColorPickerView;
    @BindView(R.id.gv_light_menu)
    GridView mGridViewLightMenu;
    @BindView(R.id.seekbar)
    SeekBar mSeekBar;

    private int mCurrentSelectedItem = -1;
    private LightMenuAdapter mLightMenuAdapter;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_light;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitleText(R.string.light);
        initLigtMenu();

        //选择点击的颜色
        mColorPickerView.setACTON_UP(true);
        mColorPickerView.setColorListener(new ColorListener() {
            @Override
            public void onColorSelected(ColorEnvelope colorEnvelope) {
                //写入颜色值
                int[] colorRGB = mColorPickerView.getColorRGB();
                System.out.println(colorRGB[0] + "-" + colorRGB[1] + "--" + colorRGB[2]);
                byte[] data = DataUtil.packageLightColor(colorRGB);
                writeData(data);

                //更新状态
                //updateMenu();
            }
        });

        //透明度
        int seekProgress = (int) SPUtil.get(mContext, AppConstant.SP_SEEKBAR_PROGRESS, 0);
        mSeekBar.setProgress(seekProgress);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //保存seekbar进度
                SPUtil.put(mContext, AppConstant.SP_SEEKBAR_PROGRESS, seekBar.getProgress());
                //写入透明度
                double rate = seekBar.getProgress() * 1.0 / mSeekBar.getMax();
                int value = (int) (rate * 100);
                byte[] data = DataUtil.packageLightAlpha(value);
                LogUtil.i("seekbar=" + (new String(data)) + "---" + value);
                writeData(data);
                //更新状态
                //updateMenu();

            }
        });
    }

    private void initLigtMenu() {
        initMenuData();
        mLightMenuAdapter = new LightMenuAdapter(mContext, mData);
        mGridViewLightMenu.setAdapter(mLightMenuAdapter);
        mGridViewLightMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCurrentSelectedItem = i;
                changeData(i);
                //更新图片
                mLightMenuAdapter.updateSelectedDrawable(mData);
                if (BluetoothManager.getInstance(mContext).isConnected()) {
                    //改变标识为的
                    choiceLight(mCurrentSelectedItem, mData.get(i));
                    updateMenu();
                } else {
                    ToastUtil.shortToast(mContext, R.string.str_not_connect);
                }
            }
        });
    }

    //改变数据的状态
    private void changeData(int item) {
        int count = mData.size();
        for (int i = 0; i < count; i++) {
            Menu menu = mData.get(i);
            if (i == item) {
                menu.isSelected = !menu.isSelected;
            } else {
                menu.isSelected = false;
            }
        }
    }

    //更新状态
    private void updateMenu() {
        LedApplication.openType = 1;
        Intent intent = new Intent(AppConstant.UPDATE_MENU_ACTION);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        LogUtil.i("发送了openType = 1");
    }

    /**
     * 选择光的类型
     *
     * @param postion
     */
    private void choiceLight(int postion, Menu menu) {
        byte[] data = DataUtil.packageLightType(mCurrentSelectedItem, menu.isSelected);
        writeData(data);
    }

    /**
     * 加载菜单数据
     */
    private void initMenuData() {
        if (LedApplication.mData != null) {
            mData = LedApplication.mData;
        } else {
            String[] menus = mContext.getResources().getStringArray(R.array.light_menu_array);
            int[] icons = new int[]{R.drawable.ic_light_up, R.drawable.ic_flash, R.drawable.ic_multi_light, R.drawable.ic_candle};
            for (int i = 0; i < menus.length; i++) {
                Menu menu = new Menu(menus[i], icons[i]);
                mData.add(menu);
            }
            LedApplication.mData = mData;
        }
    }

    @Override
    protected void beforeFinishActivity() {
        super.beforeFinishActivity();
        this.finish();
    }

    private void writeData(byte[] data) {
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
    }
}

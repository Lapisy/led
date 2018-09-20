package led.lapisy.com.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.clj.fastble.exception.BleException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import led.lapisy.com.adapter.MainMenuAdapter;
import led.lapisy.com.bean.Menu;
import led.lapisy.com.bluetooth.BluetoothManager;
import led.lapisy.com.bluetooth.WriteCallback;
import led.lapisy.com.led.R;
import led.lapisy.com.util.DataUtil;
import shiyiliang.cn.basetool.util.LogUtil;

public class RotateActivity extends BaseActivity {
    private List<Menu> mData = new ArrayList<>();

    @BindView(R.id.gv_rotate)
    GridView mGridViewSpeedMenu;

    private int mCurrentSelectedItem;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_rotate;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitleText(R.string.rotate);
        initLigtMenu();
    }

    private void initLigtMenu() {
        initMenuData();
        MainMenuAdapter mMainMenuAdapter = new MainMenuAdapter(mContext, mData);
        mGridViewSpeedMenu.setAdapter(mMainMenuAdapter);
        mGridViewSpeedMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCurrentSelectedItem = i;
                choiceRotate(mCurrentSelectedItem);
            }
        });
    }

    /**
     * 加载菜单数据
     */
    private void initMenuData() {
        String[] menus = mContext.getResources().getStringArray(R.array.speed_menu_array);
        int[] icons = new int[]{R.drawable.ic_stop, R.drawable.ic_slow, R.drawable.ic_medium, R.drawable.ic_fast};
        for (int i = 0; i < menus.length; i++) {
            System.out.println(menus[i]);
            Menu menu = new Menu(menus[i], icons[i]);
            mData.add(menu);
        }
    }

    private void choiceRotate(int mCurrentSelectedItem) {
        byte[] data = DataUtil.packageRotate(mCurrentSelectedItem);
        writeData(data);
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

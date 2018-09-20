package led.lapisy.com.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.clj.fastble.exception.BleException;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import led.lapisy.com.AppConstant;
import led.lapisy.com.LedApplication;
import led.lapisy.com.adapter.MainMenuAdapter;
import led.lapisy.com.bean.Menu;
import led.lapisy.com.bluetooth.BluetoothManager;
import led.lapisy.com.bluetooth.WriteCallback;
import led.lapisy.com.led.R;
import led.lapisy.com.service.BatteryService;
import led.lapisy.com.util.DataUtil;
import led.lapisy.com.util.DialogUtil;
import shiyiliang.cn.basetool.util.ApiUtil;
import shiyiliang.cn.basetool.util.AppUtil;
import shiyiliang.cn.basetool.util.GpsUtil;
import shiyiliang.cn.basetool.util.LogUtil;
import shiyiliang.cn.basetool.util.ResourceUtil;
import shiyiliang.cn.basetool.util.SPUtil;
import shiyiliang.cn.basetool.util.ToastUtil;

public class MainActivity extends BaseActivity {
    private static final int REQUEST_BACKGROUND_IMAGE = 0x200;
    private static final String BACKGROUND_IMAGE_URI = "background_uri";
    private static final int QR_REQUEST_CODE = 0x202;
    private List<Integer> specialMenu = new ArrayList<>();
    private static final String TAG = "MainActivity";

    private long mStartTime;
    private MainMenuAdapter mMainMenuAdapter;
    private List<Menu> mData = new ArrayList<>();
    private int mCurrentSelectedItem;
    private RxPermissions mRxPermissions;
    private BatteryReceiver mBatteryReceiver;
    private boolean[] mSelected = new boolean[3];

    @BindView(R.id.gv_menu)
    GridView mGridViewMenu;
    @BindView(R.id.iv_background)
    ImageView ivBackground;

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mRxPermissions = new RxPermissions(mContext);
        //加载背景图片
        loadBackgroundImage((String) SPUtil.get(mContext, BACKGROUND_IMAGE_URI, ""));
        //6.0以上，打开定位权限
        if (ApiUtil.aboveM()) {
            checkGpsAndOpen();
        }
        //初始化菜单
        initMenu();
        //启动电量监听
        //BatteryService.start(mContext);
        //注册电量,更新图片
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstant.BATTERY_ACTION);
        filter.addAction(AppConstant.UPDATE_BACGROUND_ACTION);
        filter.addAction(AppConstant.UPDATE_MENU_ACTION);
        mBatteryReceiver = new BatteryReceiver();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mBatteryReceiver, filter);

        //监听背景图片的宽高，裁剪的宽高
        ivBackground.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (ivBackground.getViewTreeObserver().isAlive()) {
                    ivBackground.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    LogUtil.i("ViewTreeObserver", "getViewTreeObserver run");
                }
                int width = ivBackground.getWidth();
                int heigt = ivBackground.getHeight();
                LogUtil.i("width=" + width + ",height=" + heigt);

                LedApplication.getInstance().setBackgroundImageHeight(heigt);
                LedApplication.getInstance().setBackgroundImageWidth(width);
            }
        });
    }

    //加载背景默认的图片
    public void loadBackgroundImage(String uri) {
        if (TextUtils.isEmpty(uri)) {
            Glide.with(mContext)
                    .load(R.drawable.ic_main_bg_b)
                    .dontAnimate()
                    .into(ivBackground);
        } else {
            Glide.with(mContext)
                    .load(uri)
                    .error(R.drawable.ic_main_bg_b)
                    .dontAnimate()
                    .into(ivBackground);
        }
    }

    /**
     * 初始化主页的菜单
     */
    private void initMenu() {
        initMenuData();
        mMainMenuAdapter = new MainMenuAdapter(mContext, mData);
        mGridViewMenu.setAdapter(mMainMenuAdapter);
        mGridViewMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCurrentSelectedItem = i;
                if ((i == 0 || i == 1 || i == 2) && !BluetoothManager.bluetoothIsOpen()) {
                    ToastUtil.shortToast(mContext, R.string.not_open_bluttooth);
                } else {
                    gotoSelectedSettingActivity(mCurrentSelectedItem);
                }
            }
        });
//        //电量
//        int power = (int) SPUtil.get(mContext, AppConstant.SP_POWER, 100);
//        mMainMenuAdapter.updateBattery(power);
    }

    /**
     * 加载菜单数据
     */
    private void initMenuData() {
        String[] menus = mContext.getResources().getStringArray(R.array.main_menu_array);
        int[] icons = new int[]{R.drawable.ic_search_light, R.drawable.ic_light, R.drawable.ic_reaction_mode,
                R.drawable.ic_image, R.drawable.ic_scan, -1};
        for (int i = 0; i < menus.length; i++) {
            Menu menu = new Menu(menus[i], icons[i]);
            mData.add(menu);
        }

        //点击图片不变色
        specialMenu.add(0);
        specialMenu.add(4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GpsUtil.GPS_REQUEST_CODE) {
            //由于这里返回的时候resultcode是0，所以这里需要重新检查是否打开
            if (!GpsUtil.isGpsOpen(mContext)) {
                LogUtil.i(TAG, TAG + " ,没有打开GPS定位");
                ToastUtil.shortToast(mContext, R.string.gps_not_open_message);
            }
        }
//        if (requestCode == QR_REQUEST_CODE) {
//            if (null != data) {
//                Bundle bundle = data.getExtras();
//                if (bundle == null) {
//                    return;
//                }
//                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
//                    String result = bundle.getString(CodeUtils.RESULT_STRING);
//                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
//
//                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
//                    Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
//                }
//            }
//        }
    }

    //弹出是否打开gps的弹窗
    private void checkGpsAndOpen() {
        if (!GpsUtil.isGpsOpen(mContext)) {
            DialogUtil.showNoCancelDialog(mContext, 0, R.string.str_open_gps, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GpsUtil.openGps(mContext);
                }
            });
        }
    }

    /**
     * 点击菜单，跳转到相应的设置页面
     *
     * @param mCurrentSelectedItem
     */
    private void gotoSelectedSettingActivity(int mCurrentSelectedItem) {
        Class clazz = null;
        switch (mCurrentSelectedItem) {
            case 2:
                clazz = ReactionModeActivity.class;
                AppUtil.goActivity(mContext, clazz);
                break;
            case 1:
                clazz = LightActivity.class;
                AppUtil.goActivity(mContext, clazz);
                break;
            case 0:
                searchLight();
                break;
            case 3:
                ImagePickerActivity.start(mContext);
                break;
            case 4:
                clazz = QrCodeActivity.class;
                openScanActivity(clazz);
                break;
            case 5://
                break;
        }
    }

    private void searchLight() {
        if (BluetoothManager.getInstance(mContext).isConnected()) {
            LedApplication.isSearchLight = !LedApplication.isSearchLight;
            mMainMenuAdapter.notifyDataSetChanged();
            byte[] data = DataUtil.packageSearchLight(LedApplication.isSearchLight);
            writeData(data);
        } else {
            ToastUtil.shortToast(mContext, R.string.str_not_connect);
        }
    }

    private void writeData(byte[] data) {
        BluetoothManager.getInstance(mContext)
                .write(data, new WriteCallback() {
                    @Override
                    public void onWriteSuccess(int current, int total, byte[] justWrite) {
                        LogUtil.i("writeData，current: " + current + " total: " + total + " Data: " +
                                DataUtil.bytes2String(justWrite));
                    }

                    @Override
                    public void onWriteFailure(BleException exception) {
                        LogUtil.i(exception.getDescription());
                    }
                });
    }

    /**
     * 打开扫描页面
     *
     * @param clazz
     */
    private void openScanActivity(final Class clazz) {
        Disposable subscribe = mRxPermissions.requestEach(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            //检查6。0以上是否打开gps
                            if (ApiUtil.aboveM() && !GpsUtil.isGpsOpen(mContext)) {
                                ToastUtil.shortToast(mContext, R.string.gps_not_open_message);
                            } else {
                                AppUtil.goActivity(mContext, clazz);
                            }
                        } else if (permission.shouldShowRequestPermissionRationale) {

                        } else {
                            AppUtil.gotoPermissionSetting(mContext);
                        }
                    }
                });
        mCompositeDisposable.add(subscribe);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清理资源
        BluetoothManager.getInstance(mContext).destroy();
        //取消广播
        if (mBatteryReceiver != null)
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mBatteryReceiver);
    }

    /**
     * 硬件返回退出调用
     */
    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - mStartTime > 2000) {
            ToastUtil.shortToast(mContext, R.string.tap_second_close_msg);
            mStartTime = secondTime;
        } else {
            AppUtil.killApplication();
        }
    }

    /***************************CLASS******************************/
    /**
     * 1. 电量的广播接受者
     * 2. 更新背景图片
     */
    class BatteryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals(AppConstant.BATTERY_ACTION)) {
                //切换电磁的图片 BAT:060
                String batteryStr = intent.getStringExtra(AppConstant.BATTERY_POWER);
                int result = DataUtil.parsePower(batteryStr);
                LogUtil.i("电量是：" + result);
                //SPUtil.put(mContext, AppConstant.SP_POWER, result);
                //更新电量值
                mMainMenuAdapter.updateBattery(result);
            } else if (intent != null && intent.getAction().equals(AppConstant.UPDATE_BACGROUND_ACTION)) {
                Uri uri = intent.getParcelableExtra(AppConstant.BACKGROUND_IMAGE_URI);
                LogUtil.i(TAG, uri.toString());
                Glide.with(mContext).load(uri).into(ivBackground);
                //保存图片的路径,保存之前，先删除之前的图片
                SPUtil.put(mContext, BACKGROUND_IMAGE_URI, uri);
            } else if (intent != null && intent.getAction().equals(AppConstant.UPDATE_MENU_ACTION)) {
                LogUtil.i("更新了类型="+LedApplication.openType);
                mMainMenuAdapter.notifyDataSetChanged();
            }
        }
    }

}

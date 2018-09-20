package led.lapisy.com;

import android.app.Application;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.bumptech.glide.Glide;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.List;

import led.lapisy.com.bean.Menu;
import led.lapisy.com.bluetooth.BluetoothManager;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2018/3/24
 * Desc  :
 */

public class LedApplication extends MultiDexApplication {
    private static LedApplication mLedApplication;
    public static boolean isSearchLight;
    //1:表示light亮了，2表示reaction mode亮了
    public static int openType;
    private int mBackgroundImageWidth;
    private int mBackgroundImageHeight;
    public static List<Menu> mData;

    @Override
    public void onCreate() {
        super.onCreate();
        mLedApplication = this;
        //bluethooth
        BluetoothManager.init(this);
        //zxing
        ZXingLibrary.initDisplayOpinion(this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }

    public static LedApplication getInstance() {
        return mLedApplication;
    }


    /***************记录背景图片的宽高，根据手机来算**************************/
    public int getBackgroundImageWidth() {
        return mBackgroundImageWidth;
    }

    public void setBackgroundImageWidth(int mBackgroundImageWidth) {
        this.mBackgroundImageWidth = mBackgroundImageWidth;
    }

    public int getBackgroundImageHeight() {
        return mBackgroundImageHeight;
    }

    public void setBackgroundImageHeight(int mBackgroundImageHeight) {
        this.mBackgroundImageHeight = mBackgroundImageHeight;
    }


}

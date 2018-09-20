package led.lapisy.com.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import led.lapisy.com.led.R;
import led.lapisy.com.util.DialogUtil;
import shiyiliang.cn.basetool.util.AppUtil;
import shiyiliang.cn.basetool.util.LogUtil;

public abstract class BaseActivity extends AppCompatActivity {

    private Map<Integer, String> requestPermissions = new HashMap<>();

    protected Activity mContext;
    protected Unbinder mUnbinder;
    protected Toolbar tlTitle;
    protected TextView tvTitleContent;
    protected CompositeDisposable mCompositeDisposable=new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        this.mContext = this;
        //butterknife
        mUnbinder = ButterKnife.bind(this);
        initToolBar();
        this.setSupportActionBar(getToolbar());

        init(savedInstanceState);
    }

    /**
     * 动态请求权限
     *
     * @param pPermissions
     * @param code
     */

    protected void checkAndRequestPermission(String pPermissions, int code) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionGranted(pPermissions);
        } else {
            //缓存权限和对应的code
            requestPermissions.put(code, pPermissions);
            if (ActivityCompat.checkSelfPermission(mContext, pPermissions) != PackageManager.PERMISSION_GRANTED) {
                //已经拒绝了，并不在询问
                if (!ActivityCompat.shouldShowRequestPermissionRationale(mContext, pPermissions)) {
                    //permissionDenied(pPermissions, false);
                    ActivityCompat.requestPermissions(mContext, new String[]{pPermissions}, code);
                } else {
                    ActivityCompat.requestPermissions(mContext, new String[]{pPermissions}, code);
                }
            } else {
                permissionGranted(pPermissions);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //todo 这里要处理多个权限，目前只处理了一种权限
        if (permissions != null && permissions.length > 0 && permissions[0].equals(requestPermissions.get(requestCode))) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permissionGranted(permissions[0]);
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, requestPermissions.get(requestCode))) {
                    /**
                     * 这里表示是第一次申请权限，但是被拒绝了
                     */
                    LogUtil.i("这里表示是第一次申请权限，但是被拒绝了");
                    permissionDenied(permissions[0],true);
                } else {
                    /**
                     * 这里表示是用户点击了永远不提示了,应该去应用界面打开权限设置
                     */
                    DialogUtil.showDialog(mContext, 0, R.string.go_to_find_loaction_tip, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //前往定位位置
                            AppUtil.gotoPermissionSetting(mContext);
                        }
                    }, null);
                }
            }
        }
    }

    protected void permissionGranted(String p) {

    }

    protected void permissionDenied(String p, boolean pTips) {

    }

    private void initToolBar() {
        tlTitle = findViewById(R.id.tl_title);
        if (tlTitle != null) {
            tvTitleContent = findViewById(R.id.tv_title_content);
            tlTitle.setTitle("");
            tlTitle.setSubtitle("");
        }
    }

    /**
     * 点击标题栏返回的回调
     */
    protected void beforeFinishActivity() {

    }

    protected void setTitleText(@StringRes int resID) {
        setTitleText(mContext.getResources().getString(resID));
    }

    protected void setTitleText(String pTitle) {
        if (!TextUtils.isEmpty(pTitle)) {
            getTitleView().setText(pTitle);
        }
    }

    public Toolbar getToolbar() {
        return tlTitle;
    }

    public TextView getTitleView() {
        return tvTitleContent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //toolbar中图标的点击事件
        switch (item.getItemId()) {
            case android.R.id.home:
                beforeFinishActivity();
                mContext.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        mCompositeDisposable.clear();
    }

    protected abstract int getLayoutResID();

    protected abstract void init(Bundle savedInstanceState);
}

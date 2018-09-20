package led.lapisy.com.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.OnClick;
import led.lapisy.com.led.R;
import shiyiliang.cn.basetool.util.ToastUtil;

public class SearchLightActivity extends AppCompatActivity {

    private CaptureFragment captureFragment;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_search_light);
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.scan_fragment);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.id_container, captureFragment).commit();

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("start");
                captureFragment.getHandler().startScan();
            }
        });

        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("stop");
                captureFragment.getHandler().stopScan();
            }
        });

    }



    /**
     * 二维码回调
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            ToastUtil.shortToast(mContext.getApplicationContext(), result);
        }

        @Override
        public void onAnalyzeFailed() {
            ToastUtil.shortToast(mContext.getApplicationContext(), "scan failure.");
        }
    };
}

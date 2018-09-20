package led.lapisy.com.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import led.lapisy.com.AppConstant;
import shiyiliang.cn.basetool.util.LogUtil;


public class CopyIntentService extends IntentService {
    public static final String ACTION_FOO = "led.lapisy.com.service.action.copyservice";
    public static final String BUNDLE_EXTRA = "file_name";
    public static final String TAG = "CopyIntentService";

    private boolean mRunning;

    //service start
    public static void startService(Context context, String... files) {
        Intent intent = new Intent(ACTION_FOO);
        intent.putExtra(BUNDLE_EXTRA, files);
        //要么显示调用，要么设置包名
        intent.setPackage(context.getPackageName());
        context.startService(intent);
    }

    public CopyIntentService() {
        super("CopyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LogUtil.i(TAG, "onHandleIntent runing");
        if (mRunning)
            return;
        if (intent != null && ACTION_FOO.equals(intent.getAction())) {
            mRunning = true;
            String[] files = intent.getStringArrayExtra(BUNDLE_EXTRA);
            LogUtil.i(TAG, files);
            startCopyFiles(files);
            finishCopy();
        }
    }

    private void finishCopy() {
        mRunning = false;
        Intent intent = new Intent(AppConstant.COPY_FINISH_ACTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void startCopyFiles(String[] files) {
        if (files == null || files.length == 0)
            return;
        for (String file : files) {
            if (!TextUtils.isEmpty(file)) {
                //创建目录
                boolean successCreateDir = false;
                File dirFile = new File(AppConstant.ASSETS_PATH);
                if (!dirFile.exists()) {
                    successCreateDir = dirFile.mkdir();
                }
                //判断文件是否存在
                if (dirFile.exists()) {
                    File sdFile = new File(AppConstant.ASSETS_PATH, file);
                    if (sdFile.exists())
                        continue;
                    copyFileFromAssets(sdFile, file);
                }
            }
        }
    }

    //复制图片
    private void copyFileFromAssets(File sdFile, String fileName) {
        try {
            InputStream open = this.getAssets().open(fileName);
            FileOutputStream out = new FileOutputStream(sdFile);
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = open.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }
            out.flush();
            out.close();
            open.close();
            LogUtil.i(TAG, "复制成功");
            //重命名
        } catch (IOException e) {
            if (AppConstant.DEBUG)
                e.printStackTrace();
            LogUtil.i(TAG, e.getMessage());
        }
    }


}

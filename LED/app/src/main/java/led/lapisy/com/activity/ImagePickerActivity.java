package led.lapisy.com.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import led.lapisy.com.AppConstant;
import led.lapisy.com.LedApplication;
import led.lapisy.com.led.R;
import led.lapisy.com.service.CopyIntentService;
import led.lapisy.com.util.DialogUtil;
import shiyiliang.cn.basetool.util.FileUtil;
import shiyiliang.cn.basetool.util.LogUtil;
import shiyiliang.cn.basetool.util.ResourceUtil;
import shiyiliang.cn.basetool.util.ToastUtil;

public class ImagePickerActivity extends BaseActivity implements PopupWindow.OnDismissListener, View.OnClickListener {
    private static final int OPEN_ALBUM = 100;
    private static final int REQUEST_CAMERA = 101;
    private static final String TAG = "ImagePickerActivity";

    @BindView(R.id.iv_bg_a)
    ImageView mBackgroundA;
    @BindView(R.id.iv_bg_b)
    ImageView mBackgroundB;
    @BindView(R.id.iv_add)
    ImageView mImageAdd;

    private PopupWindow popupWindow;
    private TextView tvCamera;
    private TextView tvAlbum;
    private TextView tvCancle;
    private RxPermissions mRxPermissions;
    private Uri mCameraPictureUri;
    private CopyBroadcastReceiver mCopyBroadcastReceiver;
    private Dialog mLoadImageDialog;
    private String mCurrentImageName;


    public static void start(Context context) {
        Intent intent = new Intent(context, ImagePickerActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_image;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        mRxPermissions = new RxPermissions(mContext);
        setTitleText(R.string.image_picker);
        //测试代码
//        File file = new File(AppConstant.ASSETS_PATH, AppConstant.ASSETS_BACKGROUND_A);
//        file.delete();
//        LogUtil.i(file.exists() + "----");
//        file = new File(AppConstant.ASSETS_PATH, AppConstant.ASSETS_BACKGROUND_B);
//        file.delete();
//        LogUtil.i(file.exists() + "----");
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeResource(getResources(), R.drawable.ic_image_picker, options);
//        if (options.outHeight != -1 && options.outWidth != -1) {
//            //根据+号图片的宽高来设置图片的宽高
        /**
         * 如果图片的+号图片的宽度，3
         */
//            ViewGroup.LayoutParams layoutParamsA = mBackgroundA.getLayoutParams();
//            layoutParamsA.width = options.outWidth;
//            layoutParamsA.height = options.outHeight;
//
//            ViewGroup.LayoutParams layoutParamsB = mBackgroundB.getLayoutParams();
//            layoutParamsB.width = options.outWidth;
//            layoutParamsB.height = options.outHeight;

        loadImage(mImageAdd, R.drawable.ic_image_picker, null);
        loadImage(mBackgroundA, R.drawable.ic_main_bg_a, null);
        loadImage(mBackgroundB, R.drawable.ic_main_bg_b, null);
//        }

        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstant.COPY_FINISH_ACTION);
        mCopyBroadcastReceiver = new CopyBroadcastReceiver();
        LocalBroadcastManager.getInstance(mContext).registerReceiver(mCopyBroadcastReceiver, filter);
    }


    private void loadImage(ImageView iv, int id, BitmapFactory.Options options) {
        Glide.with(mContext)
                .load(id)
                .crossFade()
                //.override(options.outWidth, options.outHeight)
                .into(iv);
    }


    @OnClick({R.id.iv_bg_a, R.id.iv_bg_b, R.id.iv_add})
    public void clickA(ImageView view) {
        switch (view.getId()) {
            case R.id.iv_add:
                openPopupWindow(view);
                break;
            case R.id.iv_bg_a:
                openCropActivity(AppConstant.ASSETS_BACKGROUND_A);
                break;
            case R.id.iv_bg_b:
                openCropActivity(AppConstant.ASSETS_BACKGROUND_B);
                break;
            default:
                break;
        }
    }

    /**
     * 打开裁剪图片功能
     *
     * @param name
     */
    private void openCropActivity(final String name) {
        //记录当前打开的图片，后面可以直接加载剪裁
        mCurrentImageName = name;

        Disposable subscribe = mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            File file = new File(AppConstant.ASSETS_PATH, name);
                            if (file.exists()) {
                                loadImage(name);
                            } else {
                                mLoadImageDialog = DialogUtil.showProgressDialog(mContext, R.string.load_image);
                                //开始复制assets
                                CopyIntentService.startService(mContext, name);
                            }
                        } else {
                            ToastUtil.shortToast(mContext, R.string.not_write_extern_permission);
                        }
                    }
                });
        mCompositeDisposable.add(subscribe);
    }


    private void loadImage(String name) {
        File file = new File(AppConstant.ASSETS_PATH, name);
        LogUtil.i(file.getAbsoluteFile());
        Uri uri = FileUtil.getUriFromFile(mContext, file, AppConstant.APP_FILE_PROVIDER);
        startCrop(uri);
    }

    private void openPopupWindow(View v) {
        //防止重复按按钮
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        View view = LayoutInflater.from(this).inflate(R.layout.view_popupwindow, null);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置动画
        popupWindow.setAnimationStyle(R.style.PopupWindow);
        //设置位置
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        //设置消失监听
        popupWindow.setOnDismissListener(this);
        //设置PopupWindow的View点击事件
        setOnPopupViewClick(view);
        //设置背景色
        setBackgroundAlpha(0.5f);
    }

    private void setOnPopupViewClick(View view) {
        TextView tv_pick_phone, tv_pick_zone, tv_cancel;
        tvCamera = (TextView) view.findViewById(R.id.tv_camera);
        tvAlbum = (TextView) view.findViewById(R.id.tv_album);
        tvCancle = (TextView) view.findViewById(R.id.tv_cancel);
        tvCamera.setOnClickListener(this);
        tvAlbum.setOnClickListener(this);
        tvCancle.setOnClickListener(this);
    }

    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_album:
                openAlbum();
                popupWindow.dismiss();
                break;
            case R.id.tv_camera:
                openCamera();
                popupWindow.dismiss();
                break;
            case R.id.tv_cancel:
                popupWindow.dismiss();
                break;
        }
    }

    /**
     * 打开相机
     */
    private void openCamera() {
        Disposable subscribe = mRxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    String mFilePath = FileUtil.getFileDir(mContext, AppConstant.CAMERA_FILE);
                    File saveFile = new File(mFilePath, System.currentTimeMillis() + ".jpg");

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
                    mCameraPictureUri = FileUtil.getUriFromFile(mContext, saveFile, intent, AppConstant.APP_FILE_PROVIDER); // 传递路径
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraPictureUri);// 更改系统默认存储路径
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else {
                    ToastUtil.shortToast(mContext, R.string.check_camera_permission);
                }
            }
        });
    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        Disposable subscribe = mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            Matisse.from(mContext)
                                    .choose(MimeType.ofAll())
                                    .countable(false)
                                    .maxSelectable(1)
                                    .theme(R.style.Matisse_Dracula)
                                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                    .thumbnailScale(0.85f)
                                    .capture(true)
                                    .captureStrategy(new CaptureStrategy(false, AppConstant.APP_FILE_PROVIDER))//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
                                    .imageEngine(new GlideEngine())
                                    .forResult(OPEN_ALBUM);
                        } else {
                            LogUtil.i("openAlbum failure,no permission");
                        }
                    }
                });
    }

    /**
     * 处理返回的结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (resultCode == RESULT_OK) {
                    startCrop(mCameraPictureUri);
                }
                break;
            case OPEN_ALBUM:
                if (resultCode == RESULT_OK) {
                    List<Uri> uris = Matisse.obtainResult(data);
                    if (uris != null && uris.size() > 0) {
                        Uri uri = uris.get(0);
                        startCrop(uri);
                    }
                }
                break;
            case UCrop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    final Uri result = UCrop.getOutput(data);
                    //进入到主界面进行显示
                    Intent intent = new Intent(AppConstant.UPDATE_BACGROUND_ACTION);
                    intent.putExtra(AppConstant.BACKGROUND_IMAGE_URI, result);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                    mContext.finish();
                }
                break;
            case UCrop.RESULT_ERROR:
                UCrop.getError(data).printStackTrace();
                break;
        }
    }

    /**
     * 开始裁剪图片
     *
     * @param uri
     */
    private void startCrop(Uri uri) {
        UCrop.Options options = new UCrop.Options();
        //裁剪后图片保存在文件夹中
        Uri destinationUri = Uri.fromFile(new File(getExternalCacheDir(), System.currentTimeMillis() + ".jpg"));
        UCrop uCrop = UCrop.of(uri, destinationUri);//第一个参数是裁剪前的uri,第二个参数是裁剪后的uri

        float x = LedApplication.getInstance().getBackgroundImageWidth();
        float y = LedApplication.getInstance().getBackgroundImageHeight();
        uCrop.withAspectRatio(x, y);//设置裁剪框的宽高比例


        options.setCropGridStrokeWidth(2);//设置裁剪网格线的宽度(我这网格设置不显示，所以没效果)
        options.setCropFrameStrokeWidth(2);//设置裁剪框的宽度
        options.setMaxScaleMultiplier(3);//设置最大缩放比例
        options.setHideBottomControls(true);//隐藏下边控制栏
        options.setShowCropGrid(false);  //设置是否显示裁剪网格
        options.setShowCropFrame(false); //设置是否显示裁剪边框(true为方形边框)

        //设置剪裁图片的格式
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        //设置标题栏
        options.setToolbarTitle(mContext.getResources().getString(R.string.crop));//设置标题栏文字
        options.setToolbarWidgetColor(Color.parseColor("#ffffff"));//标题字的颜色以及按钮颜色
        options.setDimmedLayerColor(Color.parseColor("#AA000000"));//设置裁剪外颜色
        options.setToolbarColor(Color.parseColor("#000000")); // 设置标题栏颜色
        options.setStatusBarColor(Color.parseColor("#000000"));//设置状态栏颜色
        options.setCropGridColor(Color.parseColor("#ffffff"));//设置裁剪网格的颜色
        options.setCropFrameColor(Color.parseColor("#ffffff"));//设置裁剪框的颜色
        //设置支持的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.NONE, UCropActivity.NONE);

        uCrop.withOptions(options);
        uCrop.start(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCopyBroadcastReceiver != null) {
            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mCopyBroadcastReceiver);
        }
    }

    /**********CLASS*************/
    class CopyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && AppConstant.COPY_FINISH_ACTION.equals(intent.getAction())) {
                //TODO
                LogUtil.i(TAG, "CopyBroadcastReceiver receiver");
                if (mLoadImageDialog != null && mLoadImageDialog.isShowing()) {
                    mLoadImageDialog.dismiss();
                    loadImage(mCurrentImageName);
                }
            }
        }
    }
}

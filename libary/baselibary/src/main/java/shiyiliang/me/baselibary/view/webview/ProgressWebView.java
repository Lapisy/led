package shiyiliang.me.baselibary.view.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import shiyiliang.cn.basetool.util.CommonUtil;
import shiyiliang.me.baselibary.R;
import shiyiliang.me.baselibary.ui.activity.ImageShowActivity;
import shiyiliang.cn.basetool.util.AppUtil;
import shiyiliang.me.baselibary.view.titlebar.CustomTitleBar;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/7/4
 * Desc  : webview的封装
 * <p>
 * <p>
 * 实现的功能有几个：
 * 1. 能够进度条加载进度
 * 2. 能够弹出框加载进度
 * 3. 能够点击图片，放大，观看
 * 4. 能够复制和索索
 * 5. 能够点击链接
 * 6. 和js的交互问题
 * </p>
 */

public class ProgressWebView extends WebView {
    private static final int UPDATE_PROGRESS = 0x1;
    private static final int LOAD_COMPLETE = UPDATE_PROGRESS + 1;
    private static final int TITLE_COMPLETE = LOAD_COMPLETE + 1;

    private ProgressBar progressBar;
    private CoustomHandler handler;
    private CustomTitleBar titleBar;
    private TitleLoadCompleteCallback titleLoadCompleteCallback;
    private boolean isImageClick = true;
    private boolean isGetAllImage = true;
    private ArrayList<String> allImageUrls;

    public ProgressWebView(Context context) {
        super(context);
        init();
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
//        addTitleBar();
        //add progressbar
        addProgressBar();
        handler = new CoustomHandler(getContext(), progressBar);

        initSetting();
        setWebViewClient(new DefaultWebViewClient());
        setWebChromeClient(new DefaultWebChromeClient());

        addJavascriptInterface(new JavascriptInterface(getContext()), "connect");
    }

    //添加标题栏
    private void addTitleBar() {
        titleBar = new CustomTitleBar(getContext(), null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        titleBar.setLayoutParams(layoutParams);

        titleBar.setLeftImage(R.drawable.ic_left_arrow);
        addView(titleBar);
    }


    //添加进度条
    private void addProgressBar() {
        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 8);
        progressBar.setLayoutParams(layoutParams);
        progressBar.setBackgroundColor(Color.GRAY);
        addView(progressBar);
    }


    private void initSetting() {

    }

    public void setProgressDrawable(Drawable drawable) {
        CommonUtil.checkIsNull(drawable);
        CommonUtil.checkIsNull(progressBar);

        progressBar.setProgressDrawable(drawable);
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }


    public void addTitleLoadComplete(TitleLoadCompleteCallback callback) {
        this.titleLoadCompleteCallback = callback;
    }

    public interface TitleLoadCompleteCallback {
        void receiveTitle(String title);
    }

    /**
     * 辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等
     */
    class DefaultWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                packageMessage(LOAD_COMPLETE, 100);
            } else {
                packageMessage(UPDATE_PROGRESS, newProgress);
            }
//            Log.i("webview", "进度->" + newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            packageMessage(TITLE_COMPLETE, title);
        }

        private <T> void packageMessage(int what, T value) {
            Message message = handler.obtainMessage();
            message.what = what;
            message.obj = value;

            handler.sendMessage(message);
        }
    }

    class CoustomHandler extends Handler {
        private final ProgressBar bar;
        private WeakReference<Context> wrfContext;

        public CoustomHandler(Context context, ProgressBar bar) {
            super(Looper.getMainLooper());
            wrfContext = new WeakReference<Context>(context);
            this.bar = bar;
        }

        @Override
        public void handleMessage(Message msg) {
            int code = msg.what;
            if (code == UPDATE_PROGRESS) {
                bar.setVisibility(VISIBLE);
                bar.setProgress((Integer) msg.obj);
            } else if (code == LOAD_COMPLETE) {
                bar.setVisibility(GONE);
            } else if (code == TITLE_COMPLETE) {
                String title = (String) msg.obj;
                CommonUtil.checkIsNull(titleLoadCompleteCallback);
                titleLoadCompleteCallback.receiveTitle(title);
            }
        }
    }

    /**
     * WebViewClient就是帮助WebView处理各种通知、请求事件的
     */
    class DefaultWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            Log.i("webview", url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //显示开始加载页面
            Log.i("webview", "开始了");
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //加载结束页面
            Log.i("webview", "结束了");
            if (isImageClick) {
                addImageClickListner(view);
            }
            if (isGetAllImage) {
                parseHtml(view);
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
        }

        //webView默认是不处理https请求的，页面显示空白
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }

    private void parseHtml(WebView view) {
        String js = "javascript:window.connect.getHtmlContent('<head>'+"
                + "document.getElementsByTagName('html')[0].innerHTML+'</head>');";
        view.loadUrl(js);
    }

    private void addImageClickListner(WebView view) {
        view.getSettings().setJavaScriptEnabled(true);
        String imgJS = "javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.connect.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()";
//     view.loadDataWithBaseURL(null,javascript, "text/html",  "utf-8", null);
        view.loadUrl(imgJS);
    }

    // js通信接口
    public class JavascriptInterface {
        // 获取 img 标签正则
        private static final String IMAGE_URL_TAG = "<img.*src=(.*?)[^>]*?>";
        // 获取 src 路径的正则
        private static final String IMAGE_URL_CONTENT = "http(s?):\"?(.*?)(\"|>|\\s+)";

        private Context context;

        public JavascriptInterface(Context context) {
            this.context = context;
        }

        @android.webkit.JavascriptInterface
        public void openImage(String img) {
            Log.i("webview", "imag url:  " + img);
            Intent intent = new Intent();
            intent.setClass(getContext(), ImageShowActivity.class);
            intent.putExtra("ImageUrl", img);
            intent.putStringArrayListExtra("AllImageUrl",allImageUrls);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        }

        @android.webkit.JavascriptInterface
        public void getHtmlContent(String html) {
            allImageUrls = (ArrayList<String>) getAllImageUrlFromHtml(html);
        }

        private List<String> getAllImageUrlFromHtml(String html) {
            Matcher matcher = Pattern.compile(IMAGE_URL_TAG).matcher(html);
            List<String> listImgUrl = new ArrayList<String>();
            while (matcher.find()) {
                listImgUrl.add(matcher.group());
            }
            //从图片对应的地址对象中解析出 src 标签对应的内容
            List<String> urls = getAllImageUrlFormSrcObject(listImgUrl);
            return urls;
        }

        private List<String> getAllImageUrlFormSrcObject(List<String> listImgUrl) {
            List<String> urls=new ArrayList<>();

            for (String image : listImgUrl) {
                Matcher matcher = Pattern.compile(IMAGE_URL_CONTENT).matcher(image);
                while (matcher.find()) {
                    urls.add(matcher.group().substring(0, matcher.group().length() - 1));
                }
            }
            return urls;
        }
    }
}

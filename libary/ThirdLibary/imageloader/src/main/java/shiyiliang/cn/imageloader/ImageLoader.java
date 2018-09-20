package shiyiliang.cn.imageloader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;


/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class ImageLoader implements ImageLoaderstrategy {
    private static final ImageLoader INSTANCE=new ImageLoader();
    private ImageLoaderstrategy loaderstrategy;
    private ImageLoader(){
    }
    public static ImageLoader getInstance(){
        return INSTANCE;
    }

    public  void setImageLoaderStrategy(ImageLoaderstrategy strategy){
        loaderstrategy=strategy;
    }

    /*
     *   可创建默认的Options设置，假如不需要使用ImageView ，
     *    请自行new一个Imageview传入即可
     *  内部只需要获取Context
     */
    public static ImageLoaderOptions getDefaultOptions(@NonNull View container,@NonNull String url){
        return new ImageLoaderOptions.Builder(container,url).isCrossFade(true).build();
    }

    @Override
    public void showImage(@NonNull ImageLoaderOptions options) {
        if (loaderstrategy != null) {
            loaderstrategy.showImage(options);
        }
    }


    @Override
    public void cleanMemory(Context context) {
        loaderstrategy.cleanMemory(context);
    }

    // 在application的oncreate中初始化
    @Override
    public void init(Context context) {
//        loaderstrategy=new FrescoImageLoader();
        loaderstrategy=new GlideImageLoader();
        loaderstrategy.init(context);
    }

}

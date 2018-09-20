package shiyiliang.cn.basetool.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class BitmapUtil {
    /**
     * 计算bitmap对象的内存大小
     */
    public static int getBitmapSize(Bitmap bitmap) {
        if (ApiUtil.aboveKITKAT()) {
            return bitmap.getAllocationByteCount();
        } else if (ApiUtil.aboveHONEYCOMB_MR1()) {
            return bitmap.getByteCount();
        } else {
            return bitmap.getRowBytes() * bitmap.getHeight();
        }
    }

    /**
     * bitmap 转drawable
     *
     * @param bitmap
     * @return
     */
    public static BitmapDrawable bitmap2Drawable(Bitmap bitmap) {
        if (null == bitmap)
            throw new IllegalArgumentException("the Argument " + bitmap + " is null");
        return new BitmapDrawable(bitmap);
    }

    /**
     * drawable 转化为bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (null == drawable)
            throw new IllegalArgumentException("the Argument drawable= " + drawable + " is null");
        if (drawable instanceof BitmapDrawable)
            return ((BitmapDrawable) drawable).getBitmap();
        else {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                    drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }
}

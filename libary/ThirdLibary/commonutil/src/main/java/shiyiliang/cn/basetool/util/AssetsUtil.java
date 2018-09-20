package shiyiliang.cn.basetool.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import okio.BufferedSource;
import okio.Okio;

/**
 * Author: shiyiliang
 * Blog  : http://shiyiliang.cn
 * Time  : 2017/8/30
 * Desc  :
 */

public class AssetsUtil {

    /**
     * 返回文件的内容
     *
     * @param path
     * @return
     */
    public static String getFileContent(Context context, String path) {
        String result = "";
        InputStream open = null;
        try {
            open = context.getAssets().open(path);
            BufferedSource buffer = Okio.buffer(Okio.source(open));
            result = buffer.readUtf8();
            buffer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != open)
                try {
                    open.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return result;
    }

    public static InputStream getFileContentInputstream(String path) {
        return null;
    }


}

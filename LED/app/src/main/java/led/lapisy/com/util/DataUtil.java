package led.lapisy.com.util;

import android.text.TextUtils;
import android.view.TextureView;
import android.widget.Spinner;

import com.clj.fastble.utils.HexUtil;

import java.util.Objects;

import shiyiliang.cn.basetool.util.LogUtil;

public class DataUtil {
    private static String SEPEATOR = " ";

    public static String packageData() {
        StringBuilder builder = new StringBuilder();
        return builder.toString();
    }

    /**
     * 把byte[] 转string
     *
     * @param values
     * @return
     */
    public static String bytes2String(byte[] values) {
        if (values == null)
            throw new RuntimeException("the byte[] is null");
        String hexString = HexUtil.formatHexString(values);
        return new String(HexUtil.hexStringToBytes(hexString));
    }

    static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


    public static byte[] packageReactionModeData(int value) {
        StringBuilder builder = new StringBuilder();
        builder.append("S RE ");
        builder.append(value);
        builder.append(" E");
        return builder.toString().trim().getBytes();
    }

    public static byte[] packageLightAlpha(int value) {
//        S L ALPHA 100 E
        StringBuilder builder = new StringBuilder();
        builder.append("S L ALPHA ");
        builder.append(value);
        builder.append(" E");
        return builder.toString().trim().getBytes();
    }

    public static byte[] packageLightColor(int[] color) {
//        S L RGB R 值G 值B 值 E
        StringBuilder builder = new StringBuilder();
        builder.append("S L RGB ");
        builder.append(encodeInt(color[0]));
        builder.append(encodeInt(color[1]));
        builder.append(encodeInt(color[2]));
        builder.append(" E");
        return builder.toString().trim().getBytes();
    }

    private static String encodeInt(int value) {
        String result = String.valueOf(value);
        if (result.length() == 1)
            result = "00" + value;
        else if (result.length() == 2)
            result = "0" + value;
        return result;
    }

    public static byte[] packageLightType(int mCurrentSelectedItem, boolean select) {
        StringBuilder builder = new StringBuilder();
        builder.append("S L ");

        switch (mCurrentSelectedItem) {
            case 0:
                builder.append("A ");
                builder = select ? builder.append("ON") : builder.append("OFF");
                break;
            case 1:
                builder.append("B ");
                builder = select ? builder.append("ON") : builder.append("OFF");
                break;
            case 2:
                builder.append("C ");
                builder = select ? builder.append("ON") : builder.append("OFF");
                break;
            case 3:
                builder.append("D ");
                builder = select ? builder.append("ON") : builder.append("OFF");
                break;
        }

        builder.append(" E");
        LogUtil.i(builder.toString());
        return builder.toString().trim().getBytes();
    }

    public static byte[] packageRotate(int mCurrentSelectedItem) {
//        ROTATE：S R STOP E(电机关闭)
//                S R SLOW E(电机慢速)
//        S R FAST E(电机中速)
//                S R RAPID E(电机极快)
        StringBuilder builder = new StringBuilder();
        builder.append("S R ");

        switch (mCurrentSelectedItem) {
            case 0:

                builder.append("STOP");
                break;
            case 1:
                builder.append("SLOW");
                break;
            case 2:
                builder.append("FAST");
                break;
            case 3:
                builder.append("RAPID");
                break;
        }

        builder.append(" E");
        return builder.toString().trim().getBytes();
    }

    public static byte[] packageSearchLight(boolean isSearchLight) {
//        S SL ON E(射灯开)
        StringBuilder builder = new StringBuilder();
        builder.append("S SL ");
        builder = isSearchLight ? builder.append("ON") : builder.append("OFF");
        builder.append(" E");
        return builder.toString().trim().getBytes();
    }

    /**
     * 解析电量字符串
     *
     * @param result
     * @return
     */
    public static int parsePower(String result) {
        if (TextUtils.isEmpty(result))
            return 0;
        String[] split = result.split(":");
        if (split.length < 2 || TextUtils.isEmpty(split[1]))
            return 0;
        result = split[1].trim();

        //去掉数字前面的0
        char[] temp = result.toCharArray();
        int i = 0;
        for (; i < temp.length; i++) {
            if (temp[i] != '0')
                break;
        }
        result = result.substring(i);
        return Integer.parseInt(result);
    }
}

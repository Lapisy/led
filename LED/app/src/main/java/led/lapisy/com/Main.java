package led.lapisy.com;

import com.clj.fastble.utils.HexUtil;

public class Main {
    public static void main(String[] args){
        String hexString=HexUtil.formatHexString("S ON 1 2DN END".getBytes());
        byte[] values=HexUtil.hexStringToBytes(hexString);
        System.out.println(hexString);
        System.out.println(values.toString());
        System.out.println(new String(values));
    }

    public static String toHex(byte[] bytes) {
        final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();
        char[] c = new char[bytes.length * 2];
        int index = 0;
        for (byte b : bytes) {
            c[index++] = HEX_DIGITS[(b >> 4) & 0xf];
            c[index++] = HEX_DIGITS[b & 0xf];
        }
        return new String(c);
    }

    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }
}

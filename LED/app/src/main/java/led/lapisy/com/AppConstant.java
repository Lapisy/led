package led.lapisy.com;

import android.os.Environment;

import java.io.File;

public class AppConstant {
    public final static boolean DEBUG = true;
    //SP KEY
    public static final String SP_POWER = "SP_POWER";
    public static final String SP_SEEKBAR_PROGRESS = "SP_SEEKBAR_PROGRESS";
    public static final String SP_VERTICAL_SEEKBAR = "SP_VERTICAL_SEEKBAR";
    //custom broadcast action
    public final static String BATTERY_ACTION = "led.lapisy.com.led.battery";
    public static final String UPDATE_BACGROUND_ACTION = "led.lapisy.com.led.update_background";
    public static final String UPDATE_MENU_ACTION = "led.lapisy.com.led.update_menu";
    public static final String COPY_FINISH_ACTION = "led.lapisy.com.led.finish_copy";
    //intent data key
    public static final String BACKGROUND_IMAGE_URI = "background_image_uri";
    public final static String BATTERY_POWER = "battery_power";

    //file dir
    public static final String CAMERA_FILE = "camera_image";

    //file provider
    public static final String APP_FILE_PROVIDER = "led.lapisy.com.led.fileprovider";

    //assets to sdcard path
    public static final String ASSETS_PATH = new File(Environment.getExternalStorageDirectory(), "GoldenChild").getAbsolutePath();
    public static final String ASSETS_BACKGROUND_A = "ic_main_bg_a.jpg";
    public static final String ASSETS_BACKGROUND_B = "ic_main_bg_b.jpg";

    //mac
    public final static String DEVICE_MAC = "F3:3F:1C:06:82:F1";

//    public final static String DEVICE_MAC = "AC:01:78:E6:34:FB";

}

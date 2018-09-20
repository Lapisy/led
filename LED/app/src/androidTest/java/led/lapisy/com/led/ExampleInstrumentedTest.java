package led.lapisy.com.led;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import led.lapisy.com.AppConstant;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("led.lapisy.com.led", appContext.getPackageName());
    }

    @Test
    public void testImageFile() throws Exception{
        Context context=InstrumentationRegistry.getTargetContext();
        File file=new File(AppConstant.ASSETS_PATH,AppConstant.ASSETS_BACKGROUND_B);
        assertEquals(true,file.exists());
    }
}

package zmn.w.uiutility;

import android.util.Log;

import org.junit.Test;

import wclass.util.ColorUT;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit WindowManagertest, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void verifyColorUT() {
        int color = ColorUT.deSaturationRed(0xffff0000, 0.5f);
//        String format = String.format("%h", color);
//        Log.e("TAG",format);
    }
}
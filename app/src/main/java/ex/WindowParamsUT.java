package ex;

import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.WindowManager.LayoutParams;

/**
 * @作者 做就行了！
 * @时间 2019-05-17下午 11:54
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("WeakerAccess")
public class WindowParamsUT {
    public static LayoutParams params_noFocuse_imAlt() {
        return params(flag_noFocus_imAlt());
    }

    public static LayoutParams params_noFocuse_imAlt_cantTouch() {
        return params(flag_noFocus_imAlt_cantTouch());
    }

    public static LayoutParams params(int flag) {
        int type;
        //安卓8.0以上使用APPLICATION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            type = LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            type = LayoutParams.TYPE_PHONE;
        }
        return new LayoutParams(type, flag, PixelFormat.RGBA_8888);
    }
    //////////////////////////////////////////////////
    public static int flag_noFocus_imAlt_cantTouch() {
        return LayoutParams.FLAG_NOT_FOCUSABLE
                | LayoutParams.FLAG_NOT_TOUCHABLE
                | LayoutParams.FLAG_ALT_FOCUSABLE_IM;
    }

    public static int flag_noFocus_imAlt() {
        return LayoutParams.FLAG_NOT_FOCUSABLE
                | LayoutParams.FLAG_ALT_FOCUSABLE_IM;
    }
}

package wclass.android.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;

import static android.view.WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

/**
 * 方法的汇总
 */
@SuppressWarnings("WeakerAccess")
@SuppressLint("RtlHardcoded")
public class WindowParamsUT {
    /**
     * 是否设置了大小。
     *
     * @param l 参数
     * @return true：已经设置大小了。
     */
    public static boolean hasSize(WindowManager.LayoutParams l) {
        return l.width > 0 && l.height > 0;
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 获取窗口服务。
     */
    public static WindowManager getWindowManager(Context context) {
        return (WindowManager) context.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
    }
    //////////////////////////////////////////////////////////////////////

    public static WindowManager.LayoutParams makeDefaultLeftBottomParams() {
        WindowManager.LayoutParams p = leftBottomParams();
        p.flags = FLAG_NOT_FOCUSABLE
                |FLAG_ALT_FOCUSABLE_IM;
        p.type = WindowManager.LayoutParams.TYPE_TOAST;
        return p;
    }
    public static WindowManager.LayoutParams defaultLeftTopParams() {
        WindowManager.LayoutParams p = leftTopParams();
        p.flags = FLAG_NOT_FOCUSABLE
                |FLAG_ALT_FOCUSABLE_IM;
        p.type = WindowManager.LayoutParams.TYPE_TOAST;
        return p;
    }
    public static WindowManager.LayoutParams defaultLeftTopParams_NoTouch() {
        WindowManager.LayoutParams p = leftTopParams();
        p.flags = FLAG_NOT_FOCUSABLE|FLAG_NOT_TOUCHABLE
                |FLAG_ALT_FOCUSABLE_IM;
        p.type = WindowManager.LayoutParams.TYPE_TOAST;
        return p;
    }
    //////////////////////////////////////////////////////////////////////
    /*domain 以左上角角为参照设置坐标。（屏幕左上角为：0，0。）*/

    /**
     * {@link #makeLeftTopParams(int, int, int, int)}
     */
    public static WindowManager.LayoutParams makeLeftTopParams(int flags, int type) {
        WindowManager.LayoutParams p = leftTopParams();
        p.flags = flags;
        p.type = type;
        return p;
    }

    /**
     * 生成以屏幕左上角为原点的窗口参数。
     *
     * @param flags  {@link WindowManager.LayoutParams#flags}
     * @param type   {@link WindowManager.LayoutParams#type}
     * @param width  窗口宽。
     * @param height 窗口高。
     * @return 窗口参数对象。
     */
    public static WindowManager.LayoutParams makeLeftTopParams(int flags, int type, int width, int height) {
        WindowManager.LayoutParams p = makeLeftTopParams(flags, type);
        p.width = width;
        p.height = height;
        return p;
    }

    public static WindowManager.LayoutParams leftTopParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.gravity = Gravity.LEFT | Gravity.TOP;//以屏幕左下角为原点
        params.format = PixelFormat.RGBA_8888;
        return params;
    }
    //////////////////////////////////////////////////////////////////////
    /*domain 以左下角为参照设置坐标。（屏幕左下角为：0，0。）*/

    /**
     * {@link #makeLeftBottomParams(int, int, int, int)}
     */
    public static WindowManager.LayoutParams makeLeftBottomParams(int flags, int type) {
        WindowManager.LayoutParams p = leftBottomParams();
        p.flags = flags;
        p.type = type;
        return p;
    }

    /**
     * 生成以屏幕左下角为原点的窗口参数。
     *
     * @param flags  {@link WindowManager.LayoutParams#flags}
     * @param type   {@link WindowManager.LayoutParams#type}
     * @param width  窗口宽。
     * @param height 窗口高。
     * @return 窗口参数对象。
     */
    public static WindowManager.LayoutParams makeLeftBottomParams(int flags, int type, int width, int height) {
        WindowManager.LayoutParams p = makeLeftBottomParams(flags, type);
        p.width = width;
        p.height = height;
        return p;
    }

    /**
     * {@link #makeLeftBottomParams(int, int, int, int)}
     */
    public static WindowManager.LayoutParams leftBottomParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.gravity = Gravity.LEFT | Gravity.BOTTOM;//以屏幕左下角为原点
        params.format = PixelFormat.RGBA_8888;
        return params;
    }

    //////////////////////////////////////////////////////////////////////
    /*domain 重塑。*/
    public static void newToLeftBottom(WindowManager.LayoutParams p,
                                       int flags, int type,
                                       int width, int height) {
        p.gravity = Gravity.LEFT | Gravity.BOTTOM;//以屏幕左下角为原点
        p.format = PixelFormat.RGBA_8888;
        p.flags = flags;
        p.type = type;
        p.width = width;
        p.height = height;
    }

}

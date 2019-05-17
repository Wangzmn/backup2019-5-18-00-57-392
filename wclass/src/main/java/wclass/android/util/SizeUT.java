package wclass.android.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import wclass.util.RatioUT;

/**
 * @作者 做就行了！
 * @时间 2019/1/29 0029
 * @使用说明：
 */
public class SizeUT {
    /**
     * 获取屏幕宽。
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高。
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取1厘米的像素点。
     */
    public static int getCMpixel(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) (RatioUT.CM_2_INCH * dm.densityDpi);
    }

    /**
     * 获取1毫米的像素点。
     */
    public static int getMMpixel(Context context) {
        return (int) (getCMpixel(context) / 10f + 0.5f);
    }

    /**
     * 湖区状态栏高度。
     */
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

}

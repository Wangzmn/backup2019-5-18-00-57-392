package wclass.android.util;

import android.annotation.SuppressLint;
import android.content.Context;

import java.lang.reflect.Method;

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
public class SysUT {

    /**
     * 打开状态栏。
     *
     * @param context 上下文。
     */
    @SuppressLint("WrongConstant,PrivateApi")
    public static void showStatusBar(Context context) {
        try {
            Object service = context.getSystemService("statusbar");
            Class<?> statusbarManager = Class
                    .forName("android.app.StatusBarManager");
            Method expand;
            if (service != null) {
                expand = statusbarManager
                        .getMethod("expandNotificationsPanel");
                expand.setAccessible(true);
                expand.invoke(service);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

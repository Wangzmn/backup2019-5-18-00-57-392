package wclass.android.device;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;

import wclass.observer.Observable;

/**
 * @作者 做就行了！
 * @时间 2019-03-27下午 11:09
 * @该类描述： -
 * 1、该类为：屏幕方向改变时的观察者类。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * 1、把该类写在{@link Application}类中！！！
 * 2、不再使用该类时，请调用{@link #unregister()}
 * 大家一起用！！！
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("WeakerAccess")
public class ScreenOrienChangeNotify extends
        Observable<ScreenOrienChangeNotify.OnScreenOrienChangeListener> {
    private Context context;
    private BroadcastReceiver screenOrienChangeReceiver;

    public ScreenOrienChangeNotify(Context context) {
        this.context = context;
        registerScreenOrienChangeReceiver();
    }

    /**
     * 注销广播。
     * <p>
     * 警告：调用完该方法后，禁止使用该类。
     */
    public void unregister() {
        try {
            context.unregisterReceiver(screenOrienChangeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        clearObservers();
        context = null;
        screenOrienChangeReceiver = null;
    }
    //////////////////////////////////////////////////////////////////////
    /*domain 横竖屏*/

    /**
     * 注册屏幕方向信息广播。
     */
    private void registerScreenOrienChangeReceiver() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_CONFIGURATION_CHANGED);
        screenOrienChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Configuration c = context.getResources().getConfiguration();
                int orien = c.orientation;
                switch (orien) {
                    case Configuration.ORIENTATION_LANDSCAPE:
                        onNotifyHoriScreen();
                        break;
                    case Configuration.ORIENTATION_PORTRAIT:
                        onNotifyVertiScreen();
                        break;
                }
            }
        };
        context.registerReceiver(screenOrienChangeReceiver, filter);
    }

    protected void onNotifyHoriScreen() {
        for (OnScreenOrienChangeListener onScreenOrienChangeListener : getObservers()) {
            onScreenOrienChangeListener.onHoriScreen();
        }
    }

    protected void onNotifyVertiScreen() {
        for (OnScreenOrienChangeListener onScreenOrienChangeListener : getObservers()) {
            onScreenOrienChangeListener.onVertiScreen();
        }
    }

    /**
     * 屏幕方向改变时的回调接口。
     */
    public interface OnScreenOrienChangeListener {
        /**
         * 横屏时的回调。
         */
        void onHoriScreen();

        /**
         * 竖屏时的回调。
         */
        void onVertiScreen();
    }
}

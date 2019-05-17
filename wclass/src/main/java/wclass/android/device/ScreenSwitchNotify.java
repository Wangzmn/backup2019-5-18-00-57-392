package wclass.android.device;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.Objects;

import wclass.observer.Observable;

/**
 * @作者 做就行了！
 * @时间 2019-03-27下午 10:53
 * @该类描述： -
 * 1、该类为：屏幕开关屏的观察者类。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * 1、把该类写在{@link Application}类中！！！
 * 大家一起用！！！
 * 2、不再使用该类时，请调用{@link #unregister()}
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 * 1、{@link }
 * todo
 * 1、context如何取消引用。
 */
@SuppressWarnings("WeakerAccess")
public class ScreenSwitchNotify extends
        Observable<ScreenSwitchNotify.OnScreenSwitchListener> {
    private Context context;
    private BroadcastReceiver screenSwitchReceiver;

    public ScreenSwitchNotify(Context context) {
        this.context = context;
        registerScreenSwitchReceiver();
    }

    /**
     * 注销广播。
     * <p>
     * 警告：调用完该方法后，禁止使用该类。
     */
    public void unregister() {
        try {
            context.unregisterReceiver(screenSwitchReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        clearObservers();
        clearObservers();
        context = null;
        screenSwitchReceiver = null;
    }

    //////////////////////////////////////////////////////////////////////
    /*domain 开关屏*/

    /**
     * 注册广播。
     */
    private void registerScreenSwitchReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        screenSwitchReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //熄屏
                if (Objects.equals(intent.getAction(), Intent.ACTION_SCREEN_OFF)) {
                    onNotifyScreenOFF();
                }
                //开屏
                else if (Objects.equals(intent.getAction(), Intent.ACTION_SCREEN_ON)) {
                    onNotifyScreenON();
                }
            }
        };
        context.registerReceiver(screenSwitchReceiver, filter);
    }

    protected void onNotifyScreenON() {
        for (OnScreenSwitchListener onScreenSwitchListener : getObservers()) {
            onScreenSwitchListener.onScreenON();
        }
    }

    protected void onNotifyScreenOFF() {
        for (OnScreenSwitchListener onScreenSwitchListener : getObservers()) {
            onScreenSwitchListener.onScreenOFF();
        }
    }

    /**
     * 屏幕开关回调接口。
     */
    public interface OnScreenSwitchListener {
        /**
         * 开屏时的回调。
         */
        void onScreenON();

        /**
         * 熄屏时的回调。
         */
        void onScreenOFF();
    }
}

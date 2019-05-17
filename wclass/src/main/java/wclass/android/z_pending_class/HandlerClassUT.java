package wclass.android.z_pending_class;

import android.annotation.SuppressLint;
import android.os.Handler;

/**
 * @作者 做就行了！
 * @时间 2019-02-28下午 4:39
 * @该类用途： -
 * 1、{@link Handler}的封装。
 * @注意事项： -
 * 1、只适用于UI线程！！！
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue"})
public class HandlerClassUT {

    /**
     * 警告：不再使用时，请调用该方法释放内存。
     */
    public static void destroy() {
        if (h != null) {
            h.removeCallbacksAndMessages(null);
            h = null;
        }
    }
    private static Handler h;
    public static  void post(Runnable r){
        postDelay(r,0);
    }
    /**
     *
     * 延迟发消息。
     *
     * @param r    回调。
     * @param delay 延迟时间。（单位：毫秒。）
     */
    public static  void postDelay(Runnable r, long delay){
        getHandler().postDelayed(r,delay);
    }

    /**
     * @param r
     */
    public static void postDelay_forTest(Runnable r) {
        postDelay(r,1000);
    }
    //----------------------------------------------------------------------
    public static void removeCallbacks(Runnable r){
        getHandler().removeCallbacks(r);
    }
    public static void removeAll(){
        getHandler().removeCallbacksAndMessages(null);
    }

    @SuppressLint("HandlerLeak")
    private static Handler getHandler() {
        if (h == null) {
            synchronized (HandlerClassUT.class) {
                if (h == null) {
                    h = new Handler();
                }
            }
        }
        return h;
    }

    private HandlerClassUT() {
    }
}

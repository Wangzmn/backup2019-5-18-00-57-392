package zmn.w.uiutility.main_class.window_plugin;

import android.view.View;

/**
 * @作者 做就行了！
 * @时间 2019-01-26下午 9:59
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public interface WindowPlugin {
    /**
     * 获取插件view。
     */
    View getView();

    void onAdjustSize(int windowWidth,int windowHeight);

    /**
     * 被使用时。
     */
    void onAsPlugin();

    /**
     * 被解除使用时。
     */
    void onDismiss();

    /**
     * 显示时。
     */
    void onDisplay();

    /**
     * 隐藏时。
     */
    void onHide();
}

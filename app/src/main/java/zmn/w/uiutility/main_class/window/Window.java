package zmn.w.uiutility.main_class.window;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import wclass.android.ui.WindowParamsUT;
import zmn.w.uiutility.main_class.window_plugin.WindowPlugin;
import zmn.w.uiutility.second_class.InterceptFrameVG;
import zmn.w.uiutility.second_class.OnTouchEventListener;

/**
 * @作者 做就行了！
 * @时间 2019-01-26下午 11:09
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
//@SuppressWarnings({"WeakerAccess", "unused", "SameParameterValue", "UnusedReturnValue"})
public abstract class Window implements OnTouchEventListener {
    private static boolean DEBUG = false;
    //----------------------------------------------------------------------
    protected Context context;
    private WindowManager wm;//窗口管理类。
    private WindowManager.LayoutParams layoutParams;//窗口参数。
    //----------------------------------------------------------------------
    private InterceptFrameVG root;//窗口的rootView。
    //----------------------------------------------------------------------
    private WindowPlugin mPlugin;//放入根view中的插件view。
    private View pluginView;//插件view。
    //----------------------------------------------------------------------
    private boolean isVisible;//该窗口是否显示。
    private boolean isAttach;//是否将rootView附着至窗口。
    //----------------------------------------------------------------------

    /**
     * 运行于 2019年2月23日23:34:36
     *
     * 构造方法。
     */
    public Window(Context context) {
        this.context = context;
        wm = WindowParamsUT.getWindowManager(context);
        root = new InterceptFrameVG(context);
        root.setOnTouchEventListener(this);
        root.setVisibility(View.INVISIBLE);
//        if (DEBUG) {
//            rootView.setBackgroundColor(ColorUT.greenToAlpha(1f));
//        }
    }

    /**
     * 运行于 2019年2月23日23:34:52
     *
     * 设置窗口参数。
     * @param lp 用户传入的窗口参数。
     */
    @SuppressWarnings("WeakerAccess")
    public void setLayoutParams(WindowManager.LayoutParams lp) {
        this.layoutParams = lp;
    }

    @SuppressWarnings({"WeakerAccess", "unused"})
    public WindowManager.LayoutParams getLayoutParams() {
        return layoutParams;
    }

    //----------------------------------------------------------------------
    public abstract boolean onInterceptTouchEvent(MotionEvent ev);

    public abstract boolean onTouchEvent(MotionEvent event);
    //////////////////////////////////////////////////////////////////////
    @SuppressWarnings("all")
    @Deprecated
    public void setOnTouchListener(View.OnTouchListener listener) {
        root.setOnTouchListener(listener);
    }

    public InterceptFrameVG getRoot() {
        return root;
    }
    //////////////////////////////////////////////////////////////////////
    /*domain 设置窗口参数。*/

    /**
     * 运行于 2019年1月30日00:24:59
     * <p>
     * 设置窗口宽高，不更新窗口参数。
     *
     * @param width  窗口宽。
     * @param height 窗口高。
     */
    public Window setSize(int width, int height) {
        try {
            layoutParams.width = width;
            layoutParams.height = height;
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw_notSetLayoutParams();
        }
        if (mPlugin != null) {
            mPlugin.onAdjustSize(width, height);
        }
        return this;
    }

    private static void throw_notSetLayoutParams() {
        throw new NullPointerException("未设置窗口参数。请调用“setLayoutParams()”方法，设置窗口参数。");
    }

    /**
     * 运行于 2019年2月23日23:36:44
     * <p>
     * 设置窗口宽高，并更新窗口。
     *
     * @param width  窗口宽。
     * @param height 窗口高。
     */
    public void applySize(int width, int height) {
        setSize(width, height);
        updateParams();
    }

    /**
     * 运行于 2019年1月29日18:02:04
     * <p>
     * 设置坐标。
     *
     * @param x x坐标
     * @param y y坐标
     */
    public Window setCoor(int x, int y) {
        try {
            layoutParams.x = x;
            layoutParams.y = y;
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw_notSetLayoutParams();
        }
        return this;
    }

    /**
     * 运行于 2019年2月23日23:35:47
     * <p>
     * 设置坐标，并更新窗口参数。
     *
     * @param x x坐标
     * @param y y坐标
     */
    public void applyCoor(int x, int y) {
        try {
            layoutParams.x = x;
            layoutParams.y = y;
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw_notSetLayoutParams();
        }
        updateParams();
    }

    /**
     * 运行于 2019年2月23日23:36:14
     * <p>
     * 更新窗口参数。
     */
    @SuppressWarnings("WeakerAccess")
    public void updateParams() {
        try {
            wm.updateViewLayout(root, layoutParams);
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw_notSetLayoutParams();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("未附着至窗口，请调用“attachToWindow”方法。");
        }
    }

    /**
     * 运行于 2019年2月23日23:35:31
     * <p>
     * 显示窗口。
     */
    public void displayWindow() {
        if (!isAttach) {
            attachToWindow();
        }
        displayView();
    }

    /**
     * 检查于 2019年1月28日16:30:44
     * <p>
     * 隐藏窗口。
     */
    public void hideWindow() {
        hideView();
    }
    //----------------------------------------------------------------------

    /**
     * 运行于 2019年1月29日18:01:50
     * <p>
     * 添加窗口插件。
     *
     * @param plugin 窗口插件。
     */
    @SuppressWarnings("UnusedReturnValue")
    public Window addPlugin(WindowPlugin plugin) {
        if (mPlugin == plugin) {
            return this;
        }
        removePlugin();
        mPlugin = plugin;
        pluginView = plugin.getView();
        root.addView(pluginView);
        plugin.onAsPlugin();
        if (isDisplay()) {
            plugin.onDisplay();
        }
        return this;
    }

    /**
     * 检查于 2019年1月28日16:50:15
     * <p>
     * 删除窗口插件。
     */
    @SuppressWarnings("WeakerAccess")
    public void removePlugin() {
        if (mPlugin != null) {
            root.removeView(pluginView);
            mPlugin.onDismiss();
            mPlugin = null;
            pluginView = null;
        }
    }
    //----------------------------------------------------------------------

    /**
     * 运行于 2019年2月23日23:35:22
     * <p>
     * 将rootView附着至窗口。
     */
    @SuppressWarnings("WeakerAccess")
    public void attachToWindow() {
        if (!isAttach) {
            isAttach = true;
            try {
                wm.addView(root, layoutParams);
            } catch (Exception e) {
                e.printStackTrace();
                throw_notSetLayoutParams();
            }
            if (mPlugin != null) {
                mPlugin.onAdjustSize(layoutParams.width, layoutParams.height);
            }
        }
    }

    /**
     * 检查于 2019年1月28日16:27:44
     * <p>
     * 从窗口中解除rootView。
     */
    public void detachFromWindow() {
        wm.removeView(root);
        isAttach = false;
    }
    //----------------------------------------------------------------------

    /**
     * 检查于 2019年1月28日16:27:44
     * <p>
     * 是否附着插件。
     *
     * @return true：该窗口已经附着了插件view。
     */
    @SuppressWarnings("unused")
    public boolean isAttach() {
        return isAttach;
    }

    /**
     * 检查于 2019年1月28日16:27:44
     * <p>
     * 该窗口是否显示中。
     *
     * @return true：该窗口显示中。
     */
    @SuppressWarnings("WeakerAccess")
    public boolean isDisplay() {
        return isAttach && isVisible;
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 检查于 2019年1月28日16:27:44
     * <p>
     * 显示view。
     */
    private void displayView() {
        if (!isVisible) {
            isVisible = true;
            root.setVisibility(View.VISIBLE);
            if (mPlugin != null) {
                mPlugin.onDisplay();
            }
        }
    }

    /**
     * 检查于 2019年1月28日16:27:44
     * <p>
     * 隐藏view。
     */
    private void hideView() {
        if (isVisible) {
            isVisible = false;
            root.setVisibility(View.INVISIBLE);
            if (mPlugin != null) {
                mPlugin.onHide();
            }
        }
    }
    //////////////////////////////////////////////////////////////////////
    /*domain 获取参数相关。*/
    public int getX() {
        try {
            return layoutParams.x;
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw_notSetLayoutParams();
        }
        return 0;
    }

    public int getY() {
        try {
            return layoutParams.y;
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw_notSetLayoutParams();
        }
        return 0;
    }

    public int getHeight() {
        try {
            return layoutParams.height;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getWidth() {
        try {
            return layoutParams.width;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    //////////////////////////////////////////////////////////////////////
    /*domain DEBUG*/

    /**
     * 打印坐标，宽高。
     */
    @SuppressWarnings("WeakerAccess")
    public String toStr_windowInfo() {
        Rect rect = new Rect();
        root.getGlobalVisibleRect(rect);
        return "[Window:" +
                "x = " + layoutParams.x +
                ", y = " + layoutParams.y +
                ", left = " + rect.left +
                ", top = " + rect.top +
                ", right = " + rect.right +
                ", bottom = " + rect.bottom +
                ", width = " + rect.width() +
                ", height = " + rect.height() + "]";
    }
    //////////////////////////////////////////////////////////////////////

}

package zmn.w.uiutility.main_class.window_plugin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import wclass.y_marks.Study;
import wclass.platform.linux.cpu.CpuInfo;
import wclass.enums.Level3;
import wclass.util.ColorUT;
import zmn.w.uiutility.main_class.Prefer;
import zmn.w.uiutility.main_class.window_plugin_view.CpuViewer;

/**
 * @作者 做就行了！
 * @时间 2019-01-26下午 10:05
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("unused")
public class CpuPlugin implements WindowPlugin {
    private static final boolean DEBUG = false;
    //----------------------------------------------------------------------
    private static final int REFRESH_CPU_READY_RATE_MESSAGE = 1;
    //----------------------------------------------------------------------
    private final CpuViewer viewer;
    private LinearLayout root;
    private TextView cpuLabel;
    private TextView cpuLive;
    //----------------------------------------------------------------------
    private Prefer prefer;
    private Handler handler;
    private CpuInfo cpuInfo;
    //////////////////////////////////////////////////////////////////////

    public CpuPlugin(Context context) {
        prefer = Prefer.getInstance();
        cpuInfo = new CpuInfo();
        handler = getHandler();
        viewer = new CpuViewer(context);
        root = viewer.root;
        cpuLabel = viewer.cpuLabel;
        cpuLive = viewer.cpuLive;
        if (DEBUG) {
            root.setBackgroundColor(ColorUT.BLUE);
            cpuLabel.setBackgroundColor(ColorUT.RED);
            cpuLive.setBackgroundColor(ColorUT.GREEN);
        }
    }

    /**
     * 检查于 2019年1月29日01:29:11
     */
    private Handler getHandler() {
        return new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case REFRESH_CPU_READY_RATE_MESSAGE:
                        int readyRatePercentage = cpuInfo.getReadyRatePercentage();
                        if (readyRatePercentage == 100) {
                            readyRatePercentage = 99;
                        }
                        setCpuPercentage(readyRatePercentage);
                        handler.sendEmptyMessageDelayed(REFRESH_CPU_READY_RATE_MESSAGE, prefer.cpuRefreshInterval);
                        break;
                }
            }
        };
    }

    //----------------------------------------------------------------------

    /**
     * 运行于 2019年1月29日22:11:08
     * <p>
     * 设置cpu显示的百分比。
     * <p>
     * 知识点：
     * 1、“%d”：10进制整数。
     * 2、“%%”：字符串后边+“%”。
     * 3、非常详细的网址：
     * <url>https://blog.csdn.net/lonely_fireworks/article/details/7962171</url>
     *
     * @param percentage 作为百分比的整数。
     */
    @SuppressLint("DefaultLocale")
    @Study
    private void setCpuPercentage(int percentage) {
        cpuLive.setText(String.format("%d%%", percentage));
    }

    //----------------------------------------------------------------------
    public void setBackground(int color) {
        root.setBackgroundColor(color);
    }

    public void setBackground(Drawable drawable) {
        root.setBackground(drawable);
    }

    /**
     * 运行于 2019年1月29日01:43:03
     * <p>
     * 设置字体颜色。
     *
     * @param color 字体颜色。
     *              格式：0xffeeddcc。
     *              ff：透明度。ee：红。dd：绿。cc：蓝。
     *              数值越大，颜色越重。
     */
    public void setTextColor(int color) {
        if (DEBUG) {
            Log.e("TAG", "  " + "  " + "  " + " CpuPlugin:setTextColor()成功！！！ ");
        }
        cpuLabel.setTextColor(color);
        cpuLive.setTextColor(color);
    }

    /**
     * 运行于 2019年1月29日01:37:01
     * <p>
     * 设置字体粗度。
     *
     * @param level 粗度等级。
     */
    public void setFondBold(Level3 level) {
        if (DEBUG) {
            Log.e("TAG", "  " + "  " + "  " + " CpuPlugin:setFondBold()成功！！！ ");
        }
        switch (level) {
            case NORMAL:
                resetPaintBold();
                resetMethodBold();
                break;
            case BETTER:
                resetMethodBold();
                setPaintBold(true);
                isPaintBold = true;
                break;
            case BEST:
                resetPaintBold();
                setMethodBold(Typeface.defaultFromStyle(Typeface.BOLD));
                isMethodBold = true;
                break;
        }
    }
    //////////////////////////////////////////////////////////////////////
    private boolean isPaintBold;//true：用画笔设置过字体粗度。
    private boolean isMethodBold;//true：用指定方法设置过字体粗度。

    /**
     * 重置 方法设置字体粗度。
     */
    private void resetMethodBold() {
        if (isMethodBold) {
            setMethodBold(Typeface.defaultFromStyle(Typeface.NORMAL));
            isMethodBold = false;
        }
    }

    /**
     * 重置 画笔设置字体粗度。
     */
    private void resetPaintBold() {
        if (isPaintBold) {
            setPaintBold(false);
            isPaintBold = false;
        }
    }

    /**
     * 画笔设置字体粗度。
     *
     * @param bold true：设置画笔。false：复原画笔。
     */
    private void setPaintBold(boolean bold) {
        cpuLabel.getPaint().setFakeBoldText(bold);
        cpuLive.getPaint().setFakeBoldText(bold);
    }

    /**
     * 方法设置字体粗度。
     *
     * @param tf {@link Typeface#NORMAL}{@link Typeface#BOLD}
     */
    private void setMethodBold(Typeface tf) {
        cpuLabel.setTypeface(tf);
        cpuLive.setTypeface(tf);
    }
    //////////////////////////////////////////////////////////////////////

    @Override
    public View getView() {
        return root;
    }

    @Override
    public void onAdjustSize(int windowWidth, int windowHeight) {
        if (DEBUG) {
            Log.e("TAG", "  " + "  " + "  " + " CpuPlugin:onAdjustSize()成功！！！ ");
        }
        viewer.adjustSize(windowWidth,windowHeight);
    }

    @Override
    public void onAsPlugin() {
        if (DEBUG) {
            Log.e("TAG", "  " + "  " + "  " + " CpuPlugin:onAsPlugin()成功！！！ ");
        }
    }

    /**
     * 被解雇时，删除消息。
     */
    @Override
    public void onDismiss() {
        if (DEBUG) {
            Log.e("TAG", "  " + "  " + "  " + " CpuPlugin:onDismiss()成功！！！ ");
        }
        remove_RefreshCpuReadyRate();
    }

    /**
     * 显示时，发送消息。
     */
    @Override
    public void onDisplay() {
        if (DEBUG) {
            Log.e("TAG", "  " + "  " + "  " + " CpuPlugin:onDisplay()！！！");
        }
        remove_RefreshCpuReadyRate();
        handler.sendEmptyMessage(REFRESH_CPU_READY_RATE_MESSAGE);
    }

    /**
     * 隐藏时，删除消息。
     */
    @Override
    public void onHide() {
        if (DEBUG) {
            Log.e("TAG", "  " + "  " + "  " + " CpuPlugin:onHide()！！！");
        }
        remove_RefreshCpuReadyRate();
    }

    /**
     * 删除消息。
     */
    private void remove_RefreshCpuReadyRate() {
        handler.removeMessages(REFRESH_CPU_READY_RATE_MESSAGE);
    }
}

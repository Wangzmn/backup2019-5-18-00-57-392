package zmn.w.uiutility.main_class.window_plugin_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import wclass.android.util.ViewUT;
import wclass.util.AesUT;

/**
 * @作者 做就行了！
 * @时间 2019-02-13下午 10:02
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class CpuViewer {
    public LinearLayout root;
    public TextView cpuLabel;
    public TextView cpuLive;

    //////////////////////////////////////////////////////////////////////
    public CpuViewer(Context context) {
        createView(context);
    }

    //////////////////////////////////////////////////////////////////////
    //----------------------------------------------------------------------
    @SuppressLint({"RtlHardcoded", "SetTextI18n"})
    private void createView(Context context) {
        root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);

        cpuLive = new TextView(context);
        //“%”在最右侧。示例：1%，100%。
        cpuLive.setGravity(Gravity.RIGHT | Gravity.CENTER);
        cpuLive.setIncludeFontPadding(false);

        cpuLabel = new TextView(context);
        //显示在父容器的左上角
        cpuLabel.setGravity(Gravity.LEFT | Gravity.CENTER);
        cpuLabel.setIncludeFontPadding(false);
        cpuLabel.setText("CPU在线");

        root.addView(cpuLabel);
        root.addView(cpuLive);
    }

    public void adjustSize(int windowWidth, int windowHeight) {
        //----------------------------------------------------------------------
        ViewUT.adjustSize(root, windowWidth, windowWidth);
//        ViewUT.adjustSize(root, windowWidth, windowHeight);
        //----------------------------------------------------------------------
        int liveHeight = (int) (windowWidth / (1 + AesUT.GOLDEN_RATIO));
        int liveFontSize = (int) (liveHeight * AesUT.GOLDEN_RATIO);
        int livePadding = (liveHeight - liveFontSize) / 8;
        ViewUT.adjustSize(cpuLive, windowWidth, liveHeight, liveFontSize);
        cpuLive.setPadding(0, 0, livePadding, 0);
        //----------------------------------------------------------------------
        int labelHeight = windowWidth - liveHeight;
        int labelFontSize = (int) (labelHeight * AesUT.GOLDEN_RATIO);
        int labelPadding = (labelHeight - labelFontSize) / 8;
        ViewUT.adjustSize(cpuLabel, windowWidth, labelHeight, labelFontSize);
        cpuLabel.setPadding(labelPadding, 0, 0, 0);
    }
}

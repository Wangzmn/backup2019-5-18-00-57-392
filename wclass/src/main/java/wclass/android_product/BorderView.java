package wclass.android_product;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import wclass.android.util.LayoutParamsUT;
import wclass.android.util.SizeUT;
import wclass.util.ColorUT;

/**
 * @作者 做就行了！
 * @时间 2019-03-27下午 11:20
 * @该类描述： -
 * 1、上下：上白、下黑。
 * 左右：一半透明，一半有颜色。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class BorderView extends LinearLayout {
    private Context context;
    private final int cm;
    private final int mm;
    //----------------------------------------------------------------------
    private LinearLayout line;//有颜色的线
    private View topView;//上半部分：白色。
    private View bottomView;//下半部分：黑色。
    //////////////////////////////////////////////////////////////////////

    /**
     * 构造方法。
     *
     * @param heightPerFromCM 高度为1cm的百分比。
     * @param alpha           透明度。
     * @param left            true：为左边的贴边view。
     */
    public BorderView(Context context, float heightPerFromCM, float alpha, boolean left) {
        super(context);
        this.context = context;
        cm = SizeUT.getCMpixel(context);
        mm = cm / 10;
        createView();
        LayoutParams lineParams = LayoutParamsUT.linearParams(mm, (int) (cm * heightPerFromCM));
        line.setLayoutParams(lineParams);

        topView.setBackgroundColor(ColorUT.WHITE);
        bottomView.setBackgroundColor(ColorUT.BLACK);
        setAlpha(alpha);
        if (left) {
            asLeftBorder();
        } else {
            asRightBorder();
        }
    }

    private void createView() {
        line = new LinearLayout(context);
        line.setOrientation(VERTICAL);
        //----------------------------------------------------------------------
        topView = new View(context);
        bottomView = new View(context);
        LayoutParams subParams = LayoutParamsUT.linearParams(LayoutParams.MATCH_PARENT, 0, 1);
        topView.setLayoutParams(subParams);
        bottomView.setLayoutParams(subParams);
        //----------------------------------------------------------------------
        line.addView(topView);
        line.addView(bottomView);
        //----------------------------------------------------------------------
        addView(line);
    }

    /**
     * 作为左边的贴边view，此时右半部分透明。
     */
    public void asLeftBorder() {
        LayoutParams lp = (LayoutParams) line.getLayoutParams();
        lp.leftMargin = 0;
        lp.rightMargin = mm;
        line.setLayoutParams(lp);
    }

    /**
     * 作为右边的贴边view，此时左半部分透明。
     */
    public void asRightBorder() {
        LayoutParams lp = (LayoutParams) line.getLayoutParams();
        lp.rightMargin = 0;
        lp.leftMargin = mm;
        line.setLayoutParams(lp);
    }

}

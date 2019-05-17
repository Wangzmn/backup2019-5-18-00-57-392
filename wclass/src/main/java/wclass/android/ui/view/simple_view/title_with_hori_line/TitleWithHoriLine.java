package wclass.android.ui.view.simple_view.title_with_hori_line;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import wclass.android.ui.drawable.simple_shape_drawable.ColorView;
import wclass.android.util.LayoutParamsUT;

/**
 * @作者 做就行了！
 * @时间 2019/5/9 0009
 * @使用说明：
 */
public abstract class TitleWithHoriLine<T extends View> extends LinearLayout {
    private Context context;
    private ColorView leftLine;
    private ColorView rightLine;
    private T title;

    /**
     * 标记该类是否初始化。
     */
    boolean init = false;
    //////////////////////////////////////////////////
    public TitleWithHoriLine(Context context) {
        super(context);
        this.context = context;
        //step 默认横向。
        setGravity(Gravity.CENTER);
    }

    public void init() {
        if (init) {
            return;
        }
        initInner();
    }

    /**
     * 获取中间的view。
     */
    public T getTitle() {
        return title;
    }
    //////////////////////////////////////////////////

    /**
     * 获取中间的标题view。
     */
    protected abstract T onCreateMidView(Context context);

    /**
     * 获取线的颜色。
     */
    protected abstract int getLineColor();

    /**
     * 获取线的宽度。
     */
    protected int getLineWidth() {
        return 1;
    }

    /**
     * 获取中间view的左外边距。
     */
    protected int getMidViewLeftMargin() {
        return 0;
    }

    /**
     * 获取中间view的右外边距。
     */
    protected int getMidViewRightMargin() {
        return 0;
    }
    //////////////////////////////////////////////////

    private void initInner() {
        init = true;
        int lineColor = getLineColor();
        leftLine = new ColorView(context, lineColor);
        rightLine = new ColorView(context, lineColor);
        title = onCreateMidView(context);

        LayoutParams leftLP = LayoutParamsUT.linearParams(0, getLineWidth(), 1);
        leftLP.rightMargin = getMidViewLeftMargin();
        LayoutParams rightLP = LayoutParamsUT.linearParams(0, getLineWidth(), 1);
        rightLP.leftMargin = getMidViewRightMargin();
        leftLine.setLayoutParams(leftLP);
        rightLine.setLayoutParams(rightLP);

        addView(leftLine);
        addView(title);
        addView(rightLine);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        init();
    }
}

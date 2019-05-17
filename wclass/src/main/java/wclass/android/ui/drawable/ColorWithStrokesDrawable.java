package wclass.android.ui.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import wclass.android.ui.draw.PaintUT;
import wclass.android.ui.draw.StrokeHelper;
import wclass.android.ui.drawable.base.StrokesDrawable;
import wclass.android.ui.params.RectBoolean;
import wclass.util.MathUT;

/**
 * @作者 做就行了！
 * @时间 2019-03-20上午 12:31
 * @该类描述： -
 * 1、图片大概的样子：外圈描边，内圈单一颜色。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class ColorWithStrokesDrawable extends StrokesDrawable {
    protected final Paint restMidRegionPaint;//绘制中间区域的画笔。
    private float cornerRadiusPerRelativeToMinSide;
    private float[] strokesColorAndWidth;
    //////////////////////////////////////////////////

    /**
     * @param cornerRadiusPerRelativeToMinSide 圆角半径，相对于短边的百分比。
     *                                         警告：该值不能超过0.5。
     * @param strokesColorAndWidth             {@link StrokeHelper#strokesColorAndWidth}
     */
    public ColorWithStrokesDrawable(int mainColor, float cornerRadiusPerRelativeToMinSide, float... strokesColorAndWidth) {
        this.cornerRadiusPerRelativeToMinSide =
                MathUT.limit(cornerRadiusPerRelativeToMinSide,0,0.5f);
        this.strokesColorAndWidth = strokesColorAndWidth;
        restMidRegionPaint = PaintUT.solidPaint();
        restMidRegionPaint.setColor(mainColor);
    }

    public float getCornerRadiusPerRelativeToMinSide() {
        return cornerRadiusPerRelativeToMinSide;
    }

    @Override
    protected float getCornerRadius() {
        return cornerRadiusPerRelativeToMinSide*getMinSide();
    }

    @Override
    protected float[] getStrokesColorAndWidth() {
        return strokesColorAndWidth;
    }

    @Override
    protected void onDrawRestMidRegion(Canvas canvas,
                                       RectF restMidRegionRect,
                                       float restMidRegionCornerRadius) {
        canvas.drawRoundRect(restMidRegionRect, restMidRegionCornerRadius, restMidRegionCornerRadius,
                restMidRegionPaint);
    }

}

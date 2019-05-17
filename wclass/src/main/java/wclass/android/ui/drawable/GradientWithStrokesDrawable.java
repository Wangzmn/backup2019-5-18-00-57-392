package wclass.android.ui.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import wclass.android.ui.LinearGradientAdvancer;
import wclass.android.ui.draw.PaintUT;
import wclass.android.ui.draw.StrokeHelper;
import wclass.android.ui.drawable.base.StrokesDrawable;

/**
 * @作者 做就行了！
 * @时间 2019-03-20上午 12:32
 * @该类描述： -
 * 1、图片大概的样子：外圈描边，中间渐变色 ~
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 * todo
 * 1、垃圾类。
 */
public class GradientWithStrokesDrawable extends StrokesDrawable {
    private static final boolean DEBUG = false;
    //--------------------------------------------------
    private Paint restMidRegionPaint;
    private LinearGradientAdvancer linearGradientAdvancer;
    private final float cornerRadiusPerRelativeToMinSide;
    private final float[] strokesColorAndWidth;
    //////////////////////////////////////////////////

    public GradientWithStrokesDrawable(
            LinearGradientAdvancer linearGradientAdvancer,
            float... strokesColorAndWidth) {
        this(linearGradientAdvancer, 0,
                strokesColorAndWidth);
    }

    /**
     * @param cornerRadiusPerRelativeToMinSide 圆角半径，相对于短边的百分比。
     *                                         警告：该值不能超过0.5。
     * @param strokesColorAndWidth             {@link StrokeHelper#strokesColorAndWidth}
     */
    public GradientWithStrokesDrawable(
            LinearGradientAdvancer linearGradientAdvancer,
            float cornerRadiusPerRelativeToMinSide, float... strokesColorAndWidth) {
        this.linearGradientAdvancer = linearGradientAdvancer;
        this.cornerRadiusPerRelativeToMinSide = cornerRadiusPerRelativeToMinSide;
        this.strokesColorAndWidth = strokesColorAndWidth;
        restMidRegionPaint =   PaintUT.solidPaint();
    }

    /**
     * 重新设置渐变度相关的参数。
     *
     * @param linearGradientAdvancer {@link LinearGradientAdvancer}
     */
    public void setLinearGradientAdvancer(LinearGradientAdvancer linearGradientAdvancer) {
        this.linearGradientAdvancer = linearGradientAdvancer;
        invalidateSelf();
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
        if(DEBUG){
            Log.e("TAG",getClass()+"#onDrawRestMidRegion:" +
                    " restMidRegionCornerRadius = "+restMidRegionCornerRadius
                    +" ," + "restMidRegionRect = "+restMidRegionRect+" 。");
            Log.e("TAG"," restMidRegionCornerRadius = "+restMidRegionCornerRadius);
        }
        if (linearGradientAdvancer != null) {
            restMidRegionPaint.setShader(
                    linearGradientAdvancer.makeLinearGradient(
                            restMidRegionRect.width(),
                            restMidRegionRect.height(),
                            restMidRegionRect.left, restMidRegionRect.top
                    )
            );
        }
        canvas.drawRoundRect(restMidRegionRect, restMidRegionCornerRadius, restMidRegionCornerRadius,
                restMidRegionPaint);
    }

}

package wclass.android.ui.drawable.z_secondary;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import wclass.android.ui.LinearGradientAdvancer;
import wclass.android.ui.draw.PaintUT;
import wclass.android.ui.drawable.GradientWithStrokesDrawable;

/**
 * @作者 做就行了！
 * @时间 2019-03-30下午 10:28
 * @该类描述： -
 * 1、主色为一个圆形，阴影在其右下方。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 * todo
 * 1、方形圆角的阴影部分不好看。
 */
@SuppressWarnings("WeakerAccess")
public class SquareShadowDrawable extends GradientWithStrokesDrawable {
    private static final int OFFSET = 1;
    private boolean needDrawShadow;
    private RectF shadowRect = new RectF();
    private Paint shadowPaint;
    private int diameter;
    private float cornerRadiusPerRelativeToMinSide;

    public SquareShadowDrawable(LinearGradientAdvancer linearGradientAdvancer,
                                float cornerRadiusPerRelativeToMinSide,
                                float[] strokesColorAndWidth) {
        super(linearGradientAdvancer, cornerRadiusPerRelativeToMinSide,
                strokesColorAndWidth);
        this.cornerRadiusPerRelativeToMinSide = cornerRadiusPerRelativeToMinSide;

        int color = 0xffaaaaaa;
        shadowPaint = PaintUT.solidPaint();
        shadowPaint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        /*先画阴影*/
        if (needDrawShadow) {
            onDrawShadow(canvas,shadowRect , shadowPaint, diameter, cornerRadiusPerRelativeToMinSide);
        }
        //--------------------------------------------------
        /*再画主色*/
        super.draw(canvas);
    }

    protected void onDrawShadow(Canvas canvas, RectF shadowRect, Paint shadowPaint, float diameter, float cornerRadiusPerRelativeToMinSide) {
        int radius = (int) (diameter * cornerRadiusPerRelativeToMinSide);
        canvas.drawRoundRect(shadowRect,radius,radius, shadowPaint);
    }

    @Override
    protected Rect getDrawingRect(Rect bounds) {
        /*调整为正方形*/
        int width = bounds.width();
        int height = bounds.height();
        int min = Math.min(width, height);
        bounds.right = bounds.left + min;
        bounds.bottom = bounds.top + min;
        //--------------------------------------------------
        /*阴影的rect*/
        shadowRect.left = bounds.left + OFFSET;
        shadowRect.top = bounds.top + OFFSET;
        shadowRect.right = bounds.right;
        shadowRect.bottom = bounds.bottom;
        diameter = (int) shadowRect.width();
        needDrawShadow = !shadowRect.isEmpty();
        //--------------------------------------------------
        /*主色的rect*/
        bounds.right -= OFFSET;
        bounds.bottom -= OFFSET;
        return bounds;
    }
}

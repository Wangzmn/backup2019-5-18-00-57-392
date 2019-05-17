package wclass.android.ui.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import wclass.android.ui.LinearGradientAdvancer;
import wclass.android.ui.drawable.z_secondary.SquareShadowDrawable;

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
 */
@SuppressWarnings("WeakerAccess")
@Deprecated
public class CircleShadowDrawable extends SquareShadowDrawable {

    public CircleShadowDrawable(LinearGradientAdvancer linearGradientAdvancer,
                                float[] strokesColorAndWidth) {
        super(linearGradientAdvancer, 0.5f,
                strokesColorAndWidth);
    }

    @Override
    protected void onDrawShadow(Canvas canvas, RectF shadowRect,
                                Paint shadowPaint, float diameter, float cornerRadiusPerRelativeToMinSide) {
        int radius = (int) (diameter * 0.5f);
        canvas.drawCircle(radius + shadowRect.left, radius + shadowRect.top,
                radius, shadowPaint);
    }
}

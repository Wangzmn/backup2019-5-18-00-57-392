package wclass.android.ui.drawable;

import android.graphics.drawable.Drawable;

import wclass.android.ui.drawable.base.StrokesDrawable;

/**
 * @作者 做就行了！
 * @时间 2019-03-31下午 11:14
 * @该类描述： -
 * 1、一个描边的{@link StrokesDrawable}
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("WeakerAccess")
public class StrokeDrawable extends StrokesDrawable {
    /**
     * 圆角大小。
     * 该值相对于绘制区域较短的那条边。
     */
    private final float cornerRadiusPerRelativeToMinSide;
    /**
     * 该数组存放着一条描边。
     * 0下标：该描边的颜色。
     * 1下标：该描边的宽度。
     */
    float[] strokeColorAndWidth;

    /**
     * {@link #StrokeDrawable(int, int, float)}
     */
    public StrokeDrawable(int strokeColor, int strokeWidth) {
        this(strokeColor, strokeWidth, 0);
    }

    /**
     * 构造方法。
     *
     * @param strokeColor                      描边的颜色。
     * @param strokeWidth                      描边的宽度。
     * @param cornerRadiusPerRelativeToMinSide 圆角大小。
     *                                         该值相对于绘制区域较短的那条边。
     */
    public StrokeDrawable(int strokeColor, int strokeWidth, float cornerRadiusPerRelativeToMinSide) {
        strokeColorAndWidth = new float[]{strokeColor, strokeWidth};
        this.cornerRadiusPerRelativeToMinSide = cornerRadiusPerRelativeToMinSide;
    }

    /**
     * 改变描边颜色。
     * <p>
     * 警告：如果您需要改变描边宽度，请重新创建一个{@link Drawable}吧。
     *
     * @param strokeColor 描边颜色。
     */
    public void setStrokeColor(int strokeColor) {
        strokeColorAndWidth[0] = strokeColor;
        invalidateSelf();
    }

    @Override
    protected float getCornerRadius() {
        return getMinSide() * cornerRadiusPerRelativeToMinSide;
    }

    @Override
    protected float[] getStrokesColorAndWidth() {
        return strokeColorAndWidth;
    }
}

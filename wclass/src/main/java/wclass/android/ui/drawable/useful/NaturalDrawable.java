package wclass.android.ui.drawable.useful;

import android.graphics.Rect;

import wclass.android.ui.drawable.GradientWithStrokesDrawable;
import wclass.android.util.DrawableUT;
import wclass.util.ColorUT;

/**
 * @作者 做就行了！
 * @时间 2019-03-31下午 3:30
 * @该类描述： -
 * 1、图片样式：两种极其相近的颜色渐变组合而成，主色在中间，渐变色在上和下，
 * 周围伴有一圈主色黑化了一半颜色的描边{@link ColorUT#delValue(int, float)}。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 * todo
 * 1、构造方法参数加入渐变比例。
 */
public class NaturalDrawable extends GradientWithStrokesDrawable {

    /**
     * 该类主要构造方法。
     *
     * @param mainColor 主色
     * @param strokeWidth 描边宽度。
     * @param cornerRadiusPerRelativeToMinSide 圆角半径。
     */
    public NaturalDrawable(int mainColor, int strokeWidth,
                           float cornerRadiusPerRelativeToMinSide) {
        super(DrawableUT.normGradient(mainColor),
                cornerRadiusPerRelativeToMinSide,
                DrawableUT.normStrokeAndWidth(mainColor, strokeWidth));
    }
    @Deprecated
    public NaturalDrawable(int mainColor) {
        this(mainColor, 0);
    }

    @Deprecated
    public NaturalDrawable(int mainColor, float cornerRadiusPerRelativeToMinSide) {
        this(mainColor, cornerRadiusPerRelativeToMinSide, null);
    }

    @Deprecated
    public NaturalDrawable(int mainColor, float cornerRadiusPerRelativeToMinSide,
                           float... strokeColorAndWidth) {
        super(DrawableUT.normGradient(mainColor),
                cornerRadiusPerRelativeToMinSide, strokeColorAndWidth);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
    }
}

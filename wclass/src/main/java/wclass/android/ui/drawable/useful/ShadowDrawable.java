package wclass.android.ui.drawable.useful;

import wclass.android.ui.drawable.CircleShadowDrawable;
import wclass.android.util.DrawableUT;

/**
 * @作者 做就行了！
 * @时间 2019-03-30下午 11:00
 * @该类描述： -
 * 1、该类为{@link CircleShadowDrawable}的简化版。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class ShadowDrawable extends CircleShadowDrawable {
    public ShadowDrawable(int color) {
        super(DrawableUT.normGradient(color),
                DrawableUT.normStrokeAndWidth(color));
    }


}

package product.circle_shadow_drawable;

import android.graphics.Shader;

import wclass.android.ui.LinearGradientAdvancer;
import wclass.android.ui.drawable.CircleShadowDrawable;
import wclass.util.ColorUT;

/**
 * @作者 做就行了！
 * @时间 2019-03-30下午 11:00
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class WhiteShadowCircle extends CircleShadowDrawable {
    public WhiteShadowCircle() {
        super(get(), getFloats());
    }

    private static float[] getFloats() {
        int strokeColor = ColorUT.delValue(COLOR, 0.5f);
        return new float[]{strokeColor, 1};
//        return new float[]{};
    }

    private static final int COLOR = ColorUT.WHITE;
    private static LinearGradientAdvancer get() {
        int dark = ColorUT.delValue(COLOR, 0.05f);
        return new LinearGradientAdvancer(
                0, 0, 0, 1,
                new int[]{dark, COLOR, COLOR, dark},
                new float[]{0, 0.3f, 0.6f, 1}, Shader.TileMode.CLAMP
        );
    }
}

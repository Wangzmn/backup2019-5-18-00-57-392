package wclass.android.util;

import android.graphics.Shader;

import wclass.android.ui.LinearGradientAdvancer;
import wclass.util.ColorUT;

/**
 * @作者 做就行了！
 * @时间 2019-03-31下午 3:26
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class DrawableUT {
    /**
     * 获取默认的{@link LinearGradientAdvancer}。
     * 友情提示：比较漂亮的哦 ~
     *
     * @param mainColor 您的图片的主色。
     * @return {@link LinearGradientAdvancer}
     */
    public static LinearGradientAdvancer normGradient(int mainColor) {
        int dark = ColorUT.delValue(mainColor, 0.05f);
        return new LinearGradientAdvancer(
                0, 0, 0, 1,
                new int[]{dark, mainColor, mainColor, dark},
                new float[]{0, 0.3f, 0.6f, 1}, Shader.TileMode.CLAMP
        );
    }

    /**
     * {@link #normStrokeAndWidth(int, int)}
     */
    public static float[] normStrokeAndWidth(int mainColor) {
        return normStrokeAndWidth(mainColor,1);
    }

    /**
     * 获取一个很实用的描边颜色和宽度。
     *
     * @param mainColor 您的图片的主色。
     * @return 描边颜色和宽度。
     */
    public static float[] normStrokeAndWidth(int mainColor,int strokeWidth) {
        int strokeColor = ColorUT.delValue(mainColor, 0.5f);
        return new float[]{strokeColor, strokeWidth};
    }
}

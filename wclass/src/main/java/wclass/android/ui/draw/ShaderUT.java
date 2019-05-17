package wclass.android.ui.draw;

import android.graphics.LinearGradient;
import android.graphics.Shader;

/**
 * @作者 做就行了！
 * @时间 2019-02-08下午 5:08
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class ShaderUT {

    /**
     * 生成以宽高为参照的百分比渐变{@link LinearGradient}。
     *
     * @param width     图形宽
     * @param height    图形高
     * @param perX1     x1坐标相对于宽的百分比。
     * @param perY1     y1坐标相对于高的百分比。
     * @param perX2     x2坐标相对于宽的百分比。
     * @param perY2     y2坐标相对于高的百分比。
     * @param colors    颜色数量
     * @param positions 与colors一一对应。
     *                  color对应的position，在x1--y1与x2--y2这条线段上的百分比。
     * @param tile      {@link Shader.TileMode}
     * @return {@link LinearGradient}
     */
    public static LinearGradient makeLinearGradient(float width, float height,
                                                    float perX1, float perY1,
                                                    float perX2, float perY2,
                                                    int[] colors, float[] positions,
                                                    Shader.TileMode tile) {
        return new LinearGradient(
                perX1 * width, perY1 * height,
                perX2 * width, perY2 * height,
                colors, positions, tile
        );
    }

    /**
     * 生成以宽高为参照的百分比渐变{@link LinearGradient}。
     *
     * @param width  图形宽
     * @param height 图形高
     * @param perX1  x1坐标相对于宽的百分比。
     * @param perY1  y1坐标相对于高的百分比。
     * @param perX2  x2坐标相对于宽的百分比。
     * @param perY2  y2坐标相对于高的百分比。
     * @param color1 开始颜色
     * @param color2 结束颜色
     * @param tile   {@link Shader.TileMode}
     * @return {@link LinearGradient}
     */
    public static LinearGradient makeLinearGradient(float width, float height,
                                                    float perX1, float perY1,
                                                    float perX2, float perY2,
                                                    int color1, int color2,
                                                    Shader.TileMode tile) {
        return new LinearGradient(
                perX1 * width, perY1 * height,
                perX2 * width, perY2 * height,
                color1, color2, tile
        );
    }

}

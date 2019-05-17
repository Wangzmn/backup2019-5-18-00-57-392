package wclass.android.ui;

import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;

/**
 * @作者 做就行了！
 * @时间 2019-02-08下午 5:13
 * @该类用途： -
 * 1、保存{@link LinearGradient}构造方法的参数，
 * 2、通过{@link #makeLinearGradient(float, float)}快速生成百分比{@link LinearGradient}。
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("ALL")
public class LinearGradientAdvancer {
    /**
     * x1坐标相对于宽的百分比。
     */
    private float perX1;
    /**
     * y1坐标相对于高的百分比。
     */
    private float perY1;
    /**
     * x2坐标相对于宽的百分比。
     */
    private float perX2;
    /**
     * y2坐标相对于高的百分比。
     */
    private float perY2;
    /**
     * 存放的多个颜色，与{@link #positions}一一对应
     */
    private int[] colors;
    /**
     * 存放颜色的所在位置，与{@link #colors}一一对应
     * color对应的position，在x1y1与x2y2这条线段上的百分比。
     */
    private float[] positions;
    /**
     * {@link Shader.TileMode}
     */
    private Shader.TileMode tile;

    /**
     * 生成以宽高为参照的百分比渐变{@link LinearGradient}。
     *
     * @param perX1     {@link #perX1}
     * @param perY1     {@link #perY1}
     * @param perX2     {@link #perX2}
     * @param perY2     {@link #perY2}
     * @param colors    {@link #colors}
     * @param positions {@link #positions}
     * @param tile      {@link Shader.TileMode}
     */
    public LinearGradientAdvancer(float perX1, float perY1,
                                  float perX2, float perY2,
                                  int[] colors, float[] positions,
                                  Shader.TileMode tile) {
        this.perX1 = perX1;
        this.perY1 = perY1;
        this.perX2 = perX2;
        this.perY2 = perY2;
        this.colors = colors;
        this.positions = positions;
        this.tile = tile;
    }

    /**
     * {@link #LinearGradientAdvancer(float, float, float, float, int[], float[], Shader.TileMode)}
     */
    public LinearGradientAdvancer(float perX1, float perY1,
                                  float perX2, float perY2,
                                  int color1, int color2,
                                  Shader.TileMode tile) {
        this(perX1, perY1, perX2, perY2, new int[]{color1, color2}, new float[]{0, 1}, tile);
    }

    /**
     * {@link #makeLinearGradient(float, float, float, float)}
     */
    public LinearGradient makeLinearGradient(float width, float height) {
        return makeLinearGradient(width, height, 0, 0);
    }

    /**
     * 生成以宽高为参照的百分比{@link LinearGradient}。
     *
     * @param width   图形宽
     * @param height  图形高
     * @param offsetX x偏移量
     * @param offsetY y偏移量
     * @return {@link LinearGradient}
     */
    public LinearGradient makeLinearGradient(float width, float height,
                                             float offsetX, float offsetY) {
        return new LinearGradient(
                perX1 * width + offsetX, perY1 * height + offsetY,
                perX2 * width + offsetX, perY2 * height + offsetY,
                colors, positions, tile
        );
    }

    /**
     * {@link LinearGradientAdvancer#makeLinearGradient(float, float, float, float)}
     */
    public LinearGradient makeLinearGradient(Rect rect) {
        int width = rect.width();
        int height = rect.height();
        int offsetX = rect.left;
        int offsetY = rect.top;
        return makeLinearGradient(width, height, offsetX, offsetY);
    }

    /**
     * {@link LinearGradientAdvancer#makeLinearGradient(float, float, float, float)}
     */
    public LinearGradient makeLinearGradient(RectF rect) {
        float width = rect.width();
        float height = rect.height();
        float offsetX = rect.left;
        float offsetY = rect.top;
        return makeLinearGradient(width, height, offsetX, offsetY);
    }

}

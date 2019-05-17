package wclass.util;

/**
 * @作者 做就行了！
 * @时间 2019-02-20下午 5:30
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("all")

public class ColorUT {
    public static final int BLACK = 0xff000000;
    public static final int WHITE = 0xffffffff;
    public static final int RED = 0xffff0000;
    public static final int GREEN = 0xff00ff00;
    public static final int BLUE = 0xff0000ff;
    public static final int TRANSPARENT = 0;
    //----------------------------------------------------------------------
    public static final int ALPHA_MASK = 0xff000000;
    public static final int RED_MASK = 0x00ff0000;
    public static final int GREEN_MASK = 0x0000ff00;
    public static final int BLUE_MASK = 0x000000ff;
    //----------------------------------------------------------------------
    public static final int GREEN_SHIFT = 8;
    public static final int RED_SHIFT = 16;
    public static final int ALPHA_SHIFT = 24;
    //----------------------------------------------------------------------
    private static final int COLOR_FOR_RANDOM = 0xffffff;
    //////////////////////////////////////////////////////////////////////


    /**
     * {@link #randomColor(float)}
     */
    public static int randomColor() {
        return randomColor(1);
    }

    /**
     * 获取随机颜色。
     *
     * @param perAlpha 0-1。
     *                 友情提示：该值越大越不透明。
     * @return
     */
    public static int randomColor(float perAlpha) {
        int alpha = getPerAlpha(perAlpha);
        int color = alpha | ((int) (Math.random() * COLOR_FOR_RANDOM));
        return color;
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 运行于 2019年3月5日15:59:02
     */
    public static int greenToAlpha(float per) {
        return toAlpha(GREEN, per);
    }

    /**
     * 运行于 2019年3月5日15:59:02
     */
    public static int blueToAlpha(float per) {
        return toAlpha(BLUE, per);
    }

    /**
     * 运行于 2019年3月5日15:59:02
     */
    public static int redToAlpha(float per) {
        return toAlpha(RED, per);
    }

    /**
     * 运行于 2019年3月5日15:59:02
     */
    public static int toAlpha(int color, float per) {
        return color & ~ALPHA_MASK | getPerAlpha(per);
    }

    //----------------------------------------------------------------------

    /**
     * 运行于 2019年3月5日15:59:22
     * 合成颜色。
     *
     * @param a 不透明数值。
     * @param r 红色数值。
     * @param g 绿色数值。
     * @param b 蓝色数值。
     * @return 合成后的颜色
     */
    public static int argb(int a, int r, int g, int b) {
        return toAlpha(a) | toRed(r) | toGreen(g) | b;
    }

    //----------------------------------------------------------------------

    /**
     * 运行于 2019年3月5日15:59:28
     * 合成颜色。
     *
     * @param a 不透明百分比。
     * @param r 红色百分比。
     * @param g 绿色百分比。
     * @param b 蓝色百分比。
     * @return 合成颜色。
     */
    public static int argb(float a, float r, float g, float b) {
        return getPerAlpha(a) |
                getPerRed(r) |
                getPerGreen(g) |
                getPerBlue(b);
    }

    /**
     * 运行于 2019年3月5日15:59:02
     */
    public static int getPerBlue(float per) {
        return (int) (per * 255 + 0.5f);
    }

    /**
     * 运行于 2019年3月5日15:59:02
     */
    public static int getPerGreen(float per) {
        return (int) (per * 255 + 0.5f) << GREEN_SHIFT;
    }

    /**
     * 运行于 2019年3月5日15:59:02
     */
    public static int getPerRed(float per) {
        return (int) (per * 255 + 0.5f) << RED_SHIFT;
    }

    /**
     * 运行于 2019年3月5日15:59:02
     */
    public static int getPerAlpha(float per) {
        return ((int) (per * 255 + 0.5f)) << ALPHA_SHIFT;
    }

    //----------------------------------------------------------------------

    /**
     * 运行于 2019年3月4日23:23:40
     *
     * @param color
     * @return
     */
    public static int getAlpha(int color) {
        return (color & ALPHA_MASK) >> ALPHA_SHIFT;
    }

    /**
     * 运行于 2019年3月4日23:23:40
     *
     * @param color
     * @return
     */
    public static int getRed(int color) {
        return (color & RED_MASK) >> RED_SHIFT;
    }

    /**
     * 运行于 2019年3月4日23:23:40
     *
     * @param color
     * @return
     */
    public static int getGreen(int color) {
        return (color & GREEN_MASK) >> GREEN_SHIFT;
    }

    /**
     * 运行于 2019年3月4日23:23:40
     *
     * @param color
     * @return
     */
    public static int getBlue(int color) {
        return color & BLUE_MASK;
    }

    //----------------------------------------------------------------------

    /**
     * 运行于 2019年3月5日15:59:02
     */
    public static int toAlpha(int a) {
        return a << ALPHA_SHIFT;
    }

    /**
     * 运行于 2019年3月4日23:11:19
     *
     * @param r
     * @return
     */
    public static int toRed(int r) {
        return r << RED_SHIFT;
    }

    /**
     * 运行于 2019年3月4日23:11:05
     *
     * @param g
     * @return
     */
    public static int toGreen(int g) {
        return g << GREEN_SHIFT;
    }
    //----------------------------------------------------------------------

    /**
     * 运行于 2019年3月4日23:09:53
     *
     * @param color
     * @param r
     * @return
     */
    public static int reRed(int color, int r) {
        return (color & ~RED_MASK) | toRed(r);
    }

    /**
     * 运行于 2019年3月4日23:10:24
     *
     * @param color
     * @param g
     * @return
     */
    public static int reGreen(int color, int g) {
        return (color & ~GREEN_MASK) | toGreen(g);
    }

    /**
     * 运行于 2019年3月4日23:10:05
     *
     * @param color
     * @param b
     * @return
     */
    public static int reBlue(int color, int b) {
        return (color & ~BLUE_MASK) | b;

    }

    /**
     * 运行于 2019年3月5日15:44:33
     *
     * @param color
     * @param a
     * @return
     */
    public static int reAlpha(int color, int a) {
        return (color & ~ALPHA_MASK) | toAlpha(a);
    }
    //////////////////////////////////////////////////////////////////////
    /*domain 调试完成*/
    //--------------------------------------------------
    /*2019年5月15日23:01:13之后的。*/

    /**
     * 完成于 2019年5月15日23:07:47
     * 降低颜色的饱和度。
     *
     * @param color        颜色。
     * @param deSaturation 降低饱和度。
     *                     @param referColor 靠近这个颜色。
     * @return 降低颜色的饱和度。
     */
    public static int deSaturation(int color, float deSaturation,int referColor) {
        int green = getGreen(color);
        int blue = getBlue(color);
        int red = getRed(color);
        red = addSaturationColor(red, deSaturation, getRed(referColor));
        green = addSaturationColor(green, deSaturation, getGreen(referColor));
        blue = addSaturationColor(blue, deSaturation, getBlue(referColor));
        return (color & ALPHA_MASK) | toRed(red) | toGreen(green) | blue;
    }

    /**
     * 完成于 2019年5月15日23:07:47
     * 增加饱和度
     *
     * @param colorValue
     * @param saturation
     * @param nearToColorValue  靠近这个颜色。
     * @return
     */
    private static int addSaturationColor(int colorValue, float saturation,
                                          int nearToColorValue) {
        return (int) ((nearToColorValue - colorValue) * saturation + 0.5f + colorValue);
    }
    //--------------------------------------------------
    /*2019年5月15日23:01:03之前的。*/
    /**
     * 运行于 2019年3月7日14:04:24
     * <p>
     * 降低颜色的饱和度。
     *
     * @param color        颜色。
     * @param deSaturation 降低饱和度。
     * @return 降低颜色的饱和度。
     */
    public static int deSaturation(int color, float deSaturation) {
        int green = getGreen(color);
        int blue = getBlue(color);
        int red = getRed(color);
        red = addSaturationColor(red, deSaturation);
        green = addSaturationColor(green, deSaturation);
        blue = addSaturationColor(blue, deSaturation);
        return (color & ALPHA_MASK) | toRed(red) | toGreen(green) | blue;
    }

    /**
     * 运行于 2019年3月4日23:00:56
     * <p>
     * 降低红色的饱和度。
     *
     * @param color        颜色。
     * @param deSaturation 降低饱和度。
     * @return 降低红色的饱和度。
     */
    public static int deSaturationRed(int color, float deSaturation) {
        int green = getGreen(color);
        int blue = getBlue(color);
        green = addSaturationColor(green, deSaturation);
        blue = addSaturationColor(blue, deSaturation);
        return reBlue(reGreen(color, green), blue);
    }

    /**
     * 运行于 2019年3月4日23:00:56
     * <p>
     * 降低绿色的饱和度。
     *
     * @param color        颜色。
     * @param deSaturation 降低饱和度。
     * @return 降低绿色的饱和度。
     */
    public static int deSaturationGreen(int color, float deSaturation) {
        int red = getRed(color);
        int blue = getBlue(color);
        red = addSaturationColor(red, deSaturation);
        blue = addSaturationColor(blue, deSaturation);
        return reBlue(reRed(color, red), blue);
    }

    /**
     * 运行于 2019年3月4日23:00:56
     * <p>
     * 降低蓝色的饱和度。
     *
     * @param color        颜色。
     * @param deSaturation 降低饱和度。
     * @return 降低蓝色的饱和度。
     */
    public static int deSaturationBlue(int color, float deSaturation) {
        int red = getRed(color);
        int green = getGreen(color);
        red = addSaturationColor(red, deSaturation);
        green = addSaturationColor(green, deSaturation);
        return reGreen(reRed(color, red), green);
    }
    //--------------------------------------------------
    /**
     * 增加饱和度
     *
     * @param colorValue
     * @param saturation
     * @return
     */
    private static int addSaturationColor(int colorValue, float saturation) {
        return (int) ((255 - colorValue) * saturation + 0.5f + colorValue);
    }

    /**
     * 减少饱和度。
     *
     * @param colorValue
     * @param saturation
     * @return
     */
    private static int deSaturationColor(int colorValue, float saturation) {
        return (int) (colorValue * (1 - saturation) + 0.5f);
    }

    //----------------------------------------------------------------------

    /**
     * 运行于 2019年3月4日23:52:40
     * <p>
     * 通过当前颜色比例转为颜色最大值为value的颜色。
     * 颜色默认最大值255，现在最大值为value。通过比例转换。
     *
     * @param color 颜色
     * @param value 将该值设置为颜色的最大值。
     * @return 重新调整后的颜色。
     */
    public static int reValue(int color, int value) {
        int red = getRed(color);
        int green = getGreen(color);
        int blue = getBlue(color);
        red = toValue(red, value);
        green = toValue(green, value);
        blue = toValue(blue, value);
        return (color & ALPHA_MASK) | toRed(red) | toGreen(green) | blue;
    }

    /**
     * 将color中的每种颜色，分别通过valuePer放大/缩小value。
     *
     * @param color                  颜色
     * @param valuePerRelativeToSelf 将原来的颜色通过该值放大/缩小。
     *                               该值范围：0至1。
     * @return 重新调整后的颜色。
     */
    public static int reValue(int color, float valuePerRelativeToSelf) {
        int red = getRed(color);
        int green = getGreen(color);
        int blue = getBlue(color);
        red *= valuePerRelativeToSelf;
        green *= valuePerRelativeToSelf;
        blue *= valuePerRelativeToSelf;
        red = MathUT.limit(red, 0, 255);
        green = MathUT.limit(green, 0, 255);
        blue = MathUT.limit(blue, 0, 255);

        return (color & ALPHA_MASK) | toRed(red) | toGreen(green) | blue;
    }

    /**
     * 增加每种颜色的数值。
     *
     * @param color                 颜色。
     * @param valuePerRelativeTo255 增加的值为 valuePerRelativeTo255*255。
     * @return 增加数值后的颜色。
     */
    public static int addValue(int color, float valuePerRelativeTo255) {
        int red = getRed(color);
        int green = getGreen(color);
        int blue = getBlue(color);
        int add = (int) (255 * valuePerRelativeTo255 + 0.5f);
        red += add;
        green += add;
        blue += add;
        red = MathUT.limit(red, 0, 255);
        green = MathUT.limit(green, 0, 255);
        blue = MathUT.limit(blue, 0, 255);
        return (color & ALPHA_MASK) | toRed(red) | toGreen(green) | blue;
    }

    /**
     * 运行于 2019年3月31日11:59:38
     *
     * 减少每种颜色的数值。
     *
     * @param color       颜色。
     * @param addPerIn255 减少的值为 addPerIn255*255。
     * @return 减少数值后的颜色。
     */
    public static int delValue(int color, float valuePerRelativeTo255) {
        int red = getRed(color);
        int green = getGreen(color);
        int blue = getBlue(color);
        int del = (int) (255 * valuePerRelativeTo255 + 0.5f);
        red -= del;
        green -= del;
        blue -= del;
        red = MathUT.limit(red, 0, 255);
        green = MathUT.limit(green, 0, 255);
        blue = MathUT.limit(blue, 0, 255);
        return (color & ALPHA_MASK) | toRed(red) | toGreen(green) | blue;
    }

    /**
     * 运行于 2019年3月4日23:14:53
     * <p>
     * 通过当前颜色比例转为最大值为value的颜色。
     * 默认最大值255，现在最大值为value。通过比例转换。
     *
     * @param singleColor 0-255
     * @param value
     * @return
     */
    public static int toValue(int singleColor, int value) {
        return (int) (singleColor / 255f * value + 0.5f);
    }
}

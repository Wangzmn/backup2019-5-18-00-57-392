package wclass.shape_frame.rainbow_circle;


import wclass.ui.Coor;
import wclass.util.CircleUT;
import wclass.util.ColorUT;

/**
 * 完成于 2019年3月9日15:08:49
 *
 * @作者 做就行了！
 * @时间 2019-03-03下午 10:05
 * @该类描述： -
 * 1、为圆形颜色选择器而设计。
 * 2、角度不同，该角度对应的最大饱和度的颜色不同。
 * 3、最外圈颜色深，最里圈颜色浅。
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class RainbowCircle {
    /**
     * {@link RainbowCircle#makeRainbowCircle(int, int, int, float, float, double, double, double, OrderColorMaker, RainbowCircleCallback)}
     */
    public static void makeRainbowCircle
    (int sectionIn60Degree, int saturationCount, int colorMaxValue,
     float offsetPivotX, float offsetPivotY,
     double baseRadius, double radiusIncrement,
     OrderColorMaker orderColorMaker, RainbowCircleCallback callback)

    {
        makeRainbowCircle(sectionIn60Degree, saturationCount, colorMaxValue,
                offsetPivotX, offsetPivotY, 0, baseRadius, radiusIncrement,
                orderColorMaker, callback);
    }

    /**
     * {@link RainbowCircle#makeRainbowCircle(int, int, int, float, float, double, double, double, OrderColorMaker, RainbowCircleCallback)}
     */
    public static void makeRainbowCircle
    (int sectionIn60Degree, int saturationCount,
     float offsetPivotX, float offsetPivotY,
     double offsetDegree,
     double baseRadius, double radiusIncrement,
     OrderColorMaker orderColorMaker, RainbowCircleCallback callback)

    {
        makeRainbowCircle(sectionIn60Degree, saturationCount, 0xff,
                offsetPivotX, offsetPivotY, offsetDegree, baseRadius, radiusIncrement,
                orderColorMaker, callback);
    }

    /**
     * {@link RainbowCircle#makeRainbowCircle(int, int, int, float, float, double, double, double, OrderColorMaker, RainbowCircleCallback)}
     */
    public static void makeRainbowCircle
    (int sectionIn60Degree, int saturationCount,
     float offsetPivotX, float offsetPivotY,
     double baseRadius, double radiusIncrement,
     OrderColorMaker orderColorMaker, RainbowCircleCallback callback)

    {
        makeRainbowCircle(sectionIn60Degree, saturationCount, 0xff,
                offsetPivotX, offsetPivotY, 0, baseRadius, radiusIncrement,
                orderColorMaker, callback);
    }

    /**
     * 生成彩虹色的圆，通过回调通知用户各项信息。
     *
     * @param sectionIn60Degree 把60°分成几段。
     *                          友情提示：因为每个60°是同类的渐变色。
     * @param saturationCount   从圆心至圆边，有色区域的渐变份数。
     * @param colorMaxValue     0-255。将该值设为单一颜色的最大值。
     * @param offsetPivotX      圆心X坐标补偿数值。
     * @param offsetPivotY      圆心Y坐标补偿数值。
     * @param offsetDegree      补偿角度。
     *                          友情提示：从X轴正方向开始，逆时针旋转。
     * @param baseRadius        圆心处的半径。
     *                          友情提示：该局域内为白色。
     * @param radiusIncrement   baseRadius每次向外扩展的长度。
     * @param orderColorMaker   根据角度不同的，颜色生成器。
     * @param callback          回调。（通知用户四边形的坐标、颜色、等。）
     */
    public static void makeRainbowCircle
    (int sectionIn60Degree, int saturationCount,
     int colorMaxValue,
     float offsetPivotX, float offsetPivotY,
     double offsetDegree,
     double baseRadius, double radiusIncrement,
     OrderColorMaker orderColorMaker, RainbowCircleCallback callback) {
        if (sectionIn60Degree < 1) {
            throw new IllegalArgumentException(
                    "请求的sectionIn60Degree = " + sectionIn60Degree + "" +
                            ", sectionIn60Degree不能小于1。");
        }
        if (saturationCount < 1) {
            throw new IllegalArgumentException(
                    "请求的saturationCount = " + saturationCount + "" +
                            ", saturationCount不能小于1。");

        }
        if (baseRadius < 0) {
            throw new IllegalArgumentException(
                    "请求的baseRadius = " + baseRadius + "" +
                            ", baseRadius不能小于0。");

        }
        if (radiusIncrement <= 0) {
            throw new IllegalArgumentException(
                    "请求的radiusIncrement = " + radiusIncrement + "" +
                            ", radiusIncrement不能小于0。");

        }

        //角度较低的半径，该半径上的坐标。
        Coor[] coorsInLoDegreeRadius = new Coor[saturationCount + 1];
        CircleUT.getCoors_forScreen(coorsInLoDegreeRadius, false,
                CircleUT.getArc(offsetDegree),
                baseRadius, radiusIncrement,
                offsetPivotX, offsetPivotY);
        //首次使用的那条半径，该半径上的坐标。
        Coor[] coorsInOriginRadius = Coor.clone(coorsInLoDegreeRadius);
        //角度较高的半径，该半径上的坐标。
        Coor[] coorInHiDegreeRadius = null;

        //60°分成的份数，每一份的角度。
        float subDegree = 60f / sectionIn60Degree;
        //6种类型的颜色区域，这里是最大下标。
        int kindMaxDex = 5;
        //section的最大下标。
        int sectionMaxDex = sectionIn60Degree - 1;
        for (int iKind = 0; iKind <= kindMaxDex; iKind++) {
            //每种类型的起始角度，这里算上补偿角度了。
            double kindDegree = getDegree(iKind) + offsetDegree;
            //S=section。每种类型颜色区域分成多少份。
            for (int iSection = 0; iSection <= sectionMaxDex; iSection++) {
                int colorMaxSaturation;//每个section最大饱和度颜色。
                int colorForEach;//每个4边形的颜色。
                double sectionDegreeMax;//每个section角度较大边的角度。
                //最大饱和度颜色。
                colorMaxSaturation = orderColorMaker.getColor
                        (iKind, iSection, sectionIn60Degree);
                switch (iKind) {
                    //第一种类型。
                    case 0:
                        //不是最后一个。
                        if (iSection != sectionMaxDex) {
                            //取下一个的section起始边的角度。
                            sectionDegreeMax = kindDegree + (iSection + 1) * subDegree;
                        }
                        //最后一个。
                        else {
                            //取下一种类型的起始边角度。
                            sectionDegreeMax = getDegree(iKind + 1)
                                    + offsetDegree;
                        }
                        //----------------------------------------------------------------------
                        //第一个。
                        if (iSection == 0) {
                            //需要创建角度较大的半径上的坐标。
                            coorInHiDegreeRadius = new Coor[saturationCount + 1];
                            CircleUT.getCoors_forScreen(coorInHiDegreeRadius,
                                    false,
                                    CircleUT.getArc(sectionDegreeMax),
                                    baseRadius, radiusIncrement,
                                    offsetPivotX, offsetPivotY);

                        }
                        //不是第一个。
                        else {

                            Coor[] temp = coorsInLoDegreeRadius;
                            coorsInLoDegreeRadius = coorInHiDegreeRadius;
                            CircleUT.getCoors_forScreen(temp, true,
                                    CircleUT.getArc(sectionDegreeMax),
                                    baseRadius, radiusIncrement,
                                    offsetPivotX, offsetPivotY);
                            coorInHiDegreeRadius = temp;
                        }
                        break;


                    //最后一种类型。
                    case 5:
                        //最后一个section。
                        if (iSection == sectionMaxDex) {
                            //取最初的那条半径上的坐标。
                            coorsInLoDegreeRadius = coorInHiDegreeRadius;
                            coorInHiDegreeRadius = coorsInOriginRadius;
                        }
                        //不是最后一个。
                        else {
                            //取下一个的section起始边的角度。
                            sectionDegreeMax = kindDegree +
                                    (iSection + 1) * subDegree;
                            Coor[] temp = coorsInLoDegreeRadius;
                            coorsInLoDegreeRadius = coorInHiDegreeRadius;
                            CircleUT.getCoors_forScreen(temp, true,
                                    CircleUT.getArc(sectionDegreeMax),
                                    baseRadius, radiusIncrement,
                                    offsetPivotX, offsetPivotY);
                            coorInHiDegreeRadius = temp;
                        }
                        break;


                    //不是最后一种类型。
                    default:
                        //最后一个
                        if (iSection == sectionMaxDex) {
                            //取下一种类型的起始边角度，加上补偿角度。
                            sectionDegreeMax = getDegree(iKind + 1)
                                    + offsetDegree;
                            Coor[] temp = coorsInLoDegreeRadius;
                            coorsInLoDegreeRadius = coorInHiDegreeRadius;
                            CircleUT.getCoors_forScreen(temp, true,
                                    CircleUT.getArc(sectionDegreeMax),
                                    baseRadius, radiusIncrement,
                                    offsetPivotX, offsetPivotY);
                            coorInHiDegreeRadius = temp;
                        }
                        //不是最后一个。
                        else {
                            //取下一个的section起始边的角度。
                            sectionDegreeMax = kindDegree
                                    + (iSection + 1) * subDegree;
                            Coor[] temp = coorsInLoDegreeRadius;
                            coorsInLoDegreeRadius = coorInHiDegreeRadius;
                            CircleUT.getCoors_forScreen(temp, true,
                                    CircleUT.getArc(sectionDegreeMax),
                                    baseRadius,
                                    radiusIncrement, offsetPivotX, offsetPivotY);
                            coorInHiDegreeRadius = temp;
                        }
                        break;
                }

                //SS=saturationCount。每份颜色的渐变份数，0是圆心处白色三角形。
                for (int iSaturation = 0; iSaturation <= saturationCount; iSaturation++) {
                    //圆心处。
                    if (iSaturation == 0) {
                        if (baseRadius != 0) {
                            Coor coor3 = coorInHiDegreeRadius[0];
                            Coor coor4 = coorsInLoDegreeRadius[0];
                            int white = ColorUT.WHITE;
                            //请求的不是255色，需要重新调整。
                            if (colorMaxValue != 0xff) {
                                white = ColorUT.reValue(white, colorMaxValue);
                            }
                            callback.onEverybody(new Coor(offsetPivotX, offsetPivotY), new Coor(offsetPivotX, offsetPivotY),
                                    coor3, coor4,
                                    0, baseRadius, white);
                        }
                    }
                    //有色区域。
                    else {
                        //降低饱和度 百分比。越靠近圆心，降低的饱和度越厉害。
                        float perDeSaturation = 1 - (float) iSaturation / saturationCount;
                        colorForEach = ColorUT.deSaturation(colorMaxSaturation, perDeSaturation);
                        //请求的不是255色，需要重新调整。
                        if (colorMaxValue != 0xff) {
                            colorForEach = ColorUT.reValue(colorForEach, colorMaxValue);
                        }
                        double longRadius = baseRadius + iSaturation * radiusIncrement;
                        double shortRadius = longRadius - radiusIncrement;
                        Coor coor1 = coorsInLoDegreeRadius[iSaturation - 1];
                        Coor coor2 = coorInHiDegreeRadius[iSaturation - 1];
                        Coor coor3 = coorInHiDegreeRadius[iSaturation];
                        Coor coor4 = coorsInLoDegreeRadius[iSaturation];
                        callback.onEverybody(coor1, coor2,
                                coor3, coor4,
                                shortRadius, longRadius, colorForEach);
                    }
                }

            }
        }
    }

    public interface RainbowCircleCallback {
        /**
         * 每个等腰梯形的坐标。
         *
         * @param shortCoor1  短边上的点坐标1
         * @param shortCoor2  短边上的点坐标2
         * @param longCoor1   长边上的点坐标1
         * @param longCoor2   长边上的点坐标2
         *                    友情提示：
         *                    以上四个点是顺时针连接起来的等腰梯形。
         *                    ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
         * @param shortRadius 短边距离圆心距离。
         * @param longRadius  长边距离圆心距离
         * @param color       该四边形颜色。
         */
        void onEverybody(Coor shortCoor1, Coor shortCoor2,
                         Coor longCoor1, Coor longCoor2,
                         double shortRadius, double longRadius, int color);
    }

    private static int getDegree(int kindDex) {
        return 60 * kindDex;
    }

}

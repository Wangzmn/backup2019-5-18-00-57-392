package wclass.android.ui.draw;

import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import wclass.android.ui.params.RectBoolean;
import wclass.android.util.RectUT;
import wclass.util.CircleUT;
import wclass.util.MathUT;

/**
 * @作者 做就行了！
 * @时间 2019-02-06下午 4:04
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("WeakerAccess")
public class PathUT {
    private static final boolean DEBUG = false;
    //--------------------------------------------------

    //////////////////////////////////////////////////
    /*domain 圆角矩形*/

    /**
     * {@link #makeCycCornerRect(float, float, float, float, Path, float, float)}
     */
    @Deprecated
    public static void makeCycCornerRect(float width,
                                         float height,
                                         float cornerRadius,
                                         float strokeWidth,
                                         Path path) {
        makeCycCornerRect(width, height, cornerRadius, strokeWidth, path, 0, 0);
    }

    /**
     * 帮助{@link Path}生成圆角矩形。
     *
     * @param width        图形宽
     * @param height       图形高
     * @param cornerRadius 圆角半径
     * @param strokeWidth  描边宽
     * @param path         {@link Path}
     * @param offsetX      x方向偏移
     * @param offsetY      y方向偏移
     */
    @Deprecated
    public static void makeCycCornerRect(float width,
                                         float height,
                                         float cornerRadius,
                                         float strokeWidth,
                                         Path path,
                                         float offsetX,
                                         float offsetY) {
        if (DEBUG) {
            Log.e("TAG", " width " + width);
            Log.e("TAG", " height " + height);
            Log.e("TAG", " cornerRadius " + cornerRadius);
            Log.e("TAG", " strokeWidth " + strokeWidth);
            Log.e("TAG", " offsetX " + offsetX);
            Log.e("TAG", " offsetY " + offsetY);
        }
        //如果是正方形、圆角半径为边长一半，那这就是个圆 ~
        boolean isCircle = width == height
                && MathUT.nearly(width, cornerRadius * 2, 0.01f);
        /**
         * step 解释：
         * 1、真正的描边宽度，是设置的值的一半。
         * 2、这里这里减去一个描边的宽度。
         * 3、宽高的复原过程：
         * 画的时候恢复描边宽的一半，最后offset时再恢复一半。
         */
        width = width - strokeWidth;
        height = height - strokeWidth;

        float min = Math.min(width, height);
        float halfMin = min / 2;
        float halfStrokeWidth = strokeWidth / 2;

        //重置path。
        path.rewind();
        if (isCircle) {
//            Log.e("TAG"," 圆形 ");
            /**
             * todo 是不是cornerRadius先做为XY，再减去halfStrokeWidth？？？
             * 这里这个变量，应该是必须的。
             * 2019年4月19日13:43:59
             *
             */
            float centerXY = cornerRadius;
            //以边的最中间为半径画，这样圆才正常。
            cornerRadius -= halfStrokeWidth;
            //圆角半径不能小于0，不能大于短边的一半。
            cornerRadius = MathUT.limit(cornerRadius, 0, halfMin);

            path.addCircle(cornerRadius, cornerRadius, cornerRadius, Path.Direction.CCW);
        } else {
//            Log.e("TAG"," 正方形 ");

            //圆角半径不能小于0，不能大于短边的一半。
            cornerRadius = MathUT.limit(cornerRadius, 0, halfMin);
            whole(width, height, cornerRadius, path);
        }
        path.offset(halfStrokeWidth + offsetX, halfStrokeWidth + offsetY);
        //这样形成一个完整的闭合的图形，否则会有缺口 ~
        path.close();
//        Log.e("TAG"," isCircle "+isCircle);
    }

    private static void whole(float width, float height, float cornerRadius,
                              Path path) {
        float diameter = cornerRadius * 2;

        path.moveTo(cornerRadius, cornerRadius);
        //左上角圆弧。
        RectF backup = new RectF(0, 0, diameter, diameter);
        path.arcTo(backup,
                180, 90, true);
        //----------------------------------------------------------------------
        //上边直线。
        path.lineTo(width - cornerRadius, 0);
        //----------------------------------------------------------------------
        backup.set(width - diameter, 0, width, diameter);
        //右上角圆弧。
        path.arcTo(backup,
                270, 90, false);
        //----------------------------------------------------------------------
        //右边直线。
        path.lineTo(width, height - cornerRadius);
        //----------------------------------------------------------------------
        backup.set(width - diameter, height - diameter, width, height);
        //右下角圆弧。
        path.arcTo(backup,
                0, 90, false);
        //----------------------------------------------------------------------
        //下边直线。
        path.lineTo(cornerRadius, height);
        //----------------------------------------------------------------------
        backup.set(0, height - diameter, diameter, height);
        //左下角圆弧。
        path.arcTo(backup,
                90, 90, false);
        //----------------------------------------------------------------------
        //左边直线。
        path.lineTo(0, cornerRadius);
    }
    //--------------------------------------------------

    /**
     * 运行于 2019年4月19日15:53:37
     * <p>
     * 绘制圆角矩形。
     *
     * @param width        图形宽
     * @param height       图形高
     * @param cornerRadius 圆角半径
     * @param strokeWidth  描边宽
     * @param path         {@link Path}
     * @param offsetX      x方向偏移
     * @param offsetY      y方向偏移
     * @param rectBoolean  标记rect的四个角是否需要绘制圆角。
     */
    public static void makeCircleCornerRect(float width,
                                            float height,
                                            float cornerRadius,
                                            float strokeWidth,
                                            Path path,
                                            float offsetX,
                                            float offsetY,
                                            RectBoolean rectBoolean) {
        path.rewind();
        /**
         * 解释：
         * 1、真正的描边宽度，是设置的值的一半。
         *    因为：还有一半在绘制区域的外圈。
         * 2、这里减去一个描边的宽度。
         * 3、宽高的复原过程：
         * 画的时候恢复描边宽的一半，最后offset时再恢复一半。
         */
        width = width - strokeWidth;
        height = height - strokeWidth;

        float min = Math.min(width, height);
        float halfMin = min / 2;
        float halfStrokeWidth = strokeWidth / 2;
        cornerRadius = MathUT.limit(cornerRadius, 0, halfMin);
        float diameter = cornerRadius * 2;
        //////////////////////////////////////////////////
        //左上角的圆角的外接矩形。
        RectF reuseRect = new RectF(0, 0, diameter, diameter);
        //需要绘制左上角的圆角。
        if (rectBoolean != null && rectBoolean.needLT) {
            //移动至圆心。
            path.moveTo(cornerRadius, cornerRadius);
            //左上角的圆角。
            path.arcTo(reuseRect,
                    180, 90, true);
        }
        //不需要绘制左上角的圆角，直接移动至矩形的左上角。
        else {
            path.moveTo(0, 0);
        }
        //需要绘制右上角的圆角。
        if (rectBoolean != null && rectBoolean.needRT) {
            //上边直线。
            path.lineTo(width - cornerRadius, 0);
            //右上角的圆角的外接矩形。
            reuseRect.set(width - diameter, 0, width, diameter);
            //右上角的圆角。
            path.arcTo(reuseRect,
                    270, 90, false);
        }
        //不需要绘制右上角的圆角，直接移动至矩形的右上角。
        else {
            path.lineTo(width, 0);
        }

        //需要绘制右下角的圆角。
        if (rectBoolean != null && rectBoolean.needRB) {
            //右边直线。
            path.lineTo(width, height - cornerRadius);
            //右下角的圆角的外接矩形。
            reuseRect.set(width - diameter, height - diameter, width, height);
            //右下角的圆角。
            path.arcTo(reuseRect,
                    0, 90, false);
        }
        //不需要绘制右下角的圆角，直接移动至矩形的右下角。
        else {
            path.lineTo(width, height);
        }

        //需要绘制左下角的圆角。
        if (rectBoolean != null && rectBoolean.needLB) {
            //下方直线。
            path.lineTo(cornerRadius, height);
            //左下角的圆角的外接矩形。
            reuseRect.set(0, height - diameter, diameter, height);
            //左下角的圆角。
            path.arcTo(reuseRect,
                    90, 90, false);
        }
        //不需要绘制左下角的圆角，直接移动至矩形的左下角。
        else {
            path.lineTo(0, height);
        }
        //连接初始点。
        path.close();

        path.offset(halfStrokeWidth + offsetX, halfStrokeWidth + offsetY);
    }
    //--------------------------------------------------

    /**
     * {@link PathUT#makeCircleCornerRect(float, float, float, float, android.graphics.Path, float, float, wclass.android.ui.params.RectBoolean)}
     */
    public static void makeCircleCornerRect(Rect rect,
                                            float cornerRadius,
                                            float strokeWidth,
                                            Path path) {
        makeCircleCornerRect(rect.width(), rect.height(),
                cornerRadius, strokeWidth, path,
                rect.left, rect.top);

    }

    /**
     * {@link PathUT#makeCircleCornerRect(float, float, float, float, android.graphics.Path, float, float, wclass.android.ui.params.RectBoolean)}
     */
    public static void makeCircleCornerRect(Rect rect,
                                            float cornerRadius,
                                            float strokeWidth,
                                            Path path, RectBoolean rectBoolean) {
        makeCircleCornerRect(rect.width(), rect.height(),
                cornerRadius, strokeWidth, path,
                rect.left, rect.top, rectBoolean);
    }

    /**
     * {@link PathUT#makeCircleCornerRect(float, float, float, float, android.graphics.Path, float, float, wclass.android.ui.params.RectBoolean)}
     */
    public static void makeCircleCornerRect(RectF rect,
                                            float cornerRadius,
                                            float strokeWidth,
                                            Path path) {
        makeCircleCornerRect(rect.width(), rect.height(),
                cornerRadius, strokeWidth, path,
                rect.left, rect.top);
    }

    /**
     * {@link PathUT#makeCircleCornerRect(float, float, float, float, android.graphics.Path, float, float, wclass.android.ui.params.RectBoolean)}
     */
    public static void makeCircleCornerRect(RectF rect,
                                            float cornerRadius,
                                            float strokeWidth,
                                            Path path, RectBoolean rectBoolean) {
        makeCircleCornerRect(rect.width(), rect.height(),
                cornerRadius, strokeWidth, path,
                rect.left, rect.top, rectBoolean);
    }

    /**
     * {@link PathUT#makeCircleCornerRect(float, float, float, float, android.graphics.Path, float, float, wclass.android.ui.params.RectBoolean)}
     */
    public static void makeCircleCornerRect(float width,
                                            float height,
                                            float cornerRadius,
                                            float strokeWidth,
                                            Path path,
                                            float offsetX,
                                            float offsetY) {
        makeCircleCornerRect(width, height,
                cornerRadius, strokeWidth, path,
                offsetX, offsetY, RectBoolean.TRUE);
    }
    //////////////////////////////////////////////////

    /**
     * 在绘制区域中生成进度{@link Path}对象。
     *
     * @param bounds   绘制范围。
     * @param path     路径存储。
     * @param progress 进度值。
     *                 该值范围：0至1。
     */
    @SuppressWarnings("DanglingJavadoc")
    public static void progressInBounds(Rect bounds, Path path, float progress) {
        path.rewind();
        if (bounds.isEmpty()) {
            Log.e("TAG","PathUT#progressInBounds:" +
                    " bounds = " + bounds.toString() + "区域为空，无法绘制。");
            return;
        }
        float width = bounds.width();
        float height = bounds.height();
        if (height > width) {
            Log.e("TAG","PathUT#progressInBounds:" +
                    " bounds = " + bounds + "高大于宽，无法绘制。");
            return;
        }
        //防止溢出。
        progress = MathUT.limit(progress, 0, 1);
        //进度的准确数值。
        float progressValue = width * progress;
        float radius = height / 2;
        RectF reuse = new RectF();
        path.rewind();
        //在头端圆角区域，不足圆角。
        if (progressValue < radius) {
            /**
             * 解释：
             * 0、三角形位于y轴正方向的左边，圆心作为三角形的锐角。
             * 1、y轴正方向为三角形的邻边直角边。
             * 2、该变量为三角形的对边直角边。
             * 3、圆的半径为三角形的斜边。
             */
            float triangleOpposite = radius - progressValue;
            double arc = Math.asin(triangleOpposite / radius);
            //y轴上方的左边的三角形，y轴上方与斜边的夹角。
            int degree = CircleUT.toDegree(arc);
            /**
             * 180：x轴负方向。
             * 90-degree：互补的角度。
             */
            int anotherDegree = 90 - degree;
            int startDegree = 180 - anotherDegree;
            path.moveTo(bounds.left + radius, bounds.top + radius);

            RectUT.getLeftSquare(bounds, reuse);
            path.arcTo(reuse, startDegree, anotherDegree * 2,
                    true);
            path.close();
        }

        //在尾端圆角区域，不足圆角。
        else {
            float rightNormalRegion = width - radius;
            if (progressValue > rightNormalRegion) {
                pathStartAndMid(path, bounds, reuse, rightNormalRegion, radius);
                //--------------------------------------------------
                /**
                 * 解释：
                 * 0、三角形位于y轴正方向的右边，圆心作为三角形的锐角。
                 * 1、y轴正方向为三角形的邻边直角边。
                 * 2、该变量为三角形的对边直角边。
                 * 3、圆的半径为三角形的斜边。
                 */
                float triangleOpposite = progressValue - rightNormalRegion;
                /**
                 * y轴正方向右边的三角形，三角形的斜边 与 y轴正方向的夹角的弧度。
                 */
                double arc = Math.asin(triangleOpposite / radius);
                int degree = CircleUT.toDegree(arc);
                path.lineTo(rightNormalRegion, 0);
                //            path.rewind();
                RectUT.getRightSquare(bounds, reuse);
                path.moveTo(reuse.left + radius, reuse.top + radius);
                int startDegree_Top = 270;
                path.arcTo(reuse, startDegree_Top, degree);
                //--------------------------------------------------
                int startDegree_Bottom = 90 - degree;
                path.arcTo(reuse, startDegree_Bottom, degree);
            }
            //在中间区域。
            else {
                pathStartAndMid(path, bounds, reuse, progressValue, radius);
            }
        }
    }

    /**
     * 绘制开始和中间区域。
     */
    private static void pathStartAndMid(Path path, Rect bounds, RectF reuse, float progressValue, float radius) {
        path.moveTo(bounds.left + radius, bounds.top + radius);
        RectUT.getLeftSquare(bounds, reuse);
        path.arcTo(reuse, 90, 180, true);
        path.lineTo(progressValue, 0);
        path.lineTo(progressValue, bounds.height());
        path.close();
    }

    //////////////////////////////////////////////////


}

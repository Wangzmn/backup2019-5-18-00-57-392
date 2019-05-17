package wclass.android.ui.draw;

import android.graphics.Path;

import wclass.enums.Orien4;

/**
 * @作者 做就行了！
 * @时间 2019-04-19下午 2:57
 * @该类描述： -
 * 1、{@link Path}绘制三角形的工具类。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class PathTriangleUT {

    /**
     * {@link #makeSimpleTriangle(float, float, Orien4, float, Path, float, float)}
     */
    public static void makeSimpleTriangle(float width, float height,
                                          Orien4 arrowOrien,
                                          float strokeWidth,
                                          Path path) {

        makeSimpleTriangle(width, height, arrowOrien, strokeWidth,
                path, 0, 0);

    }

    /**
     * 在矩形中绘制一个三角形。
     * 以矩形的一边及其对边的中点，组成这个三角形。
     *
     * @param width       矩形宽。
     * @param height      矩形高。
     * @param arrowOrien  箭头方向（使用哪一边的中点）。
     * @param strokeWidth 描边宽度。
     * @param path        {@link Path}
     * @param offsetX     X方向偏移。
     * @param offsetY     Y方向偏移。
     */
    public static void makeSimpleTriangle(float width, float height,
                                          Orien4 arrowOrien,
                                          float strokeWidth,
                                          Path path,
                                          float offsetX, float offsetY) {
        width = width - strokeWidth;
        height = height - strokeWidth;
        float halfStroke = strokeWidth / 2;
        switch (arrowOrien) {
            case LEFT:
                path.moveTo(0, height / 2);
                path.lineTo(width, 0);
                path.lineTo(width, height);
                path.close();
                break;
            case TOP:
                path.moveTo(width / 2, 0);
                path.lineTo(0, height);
                path.lineTo(width, height);
                path.close();
                break;
            case RIGHT:
                path.moveTo(width, height / 2);
                path.lineTo(0, 0);
                path.lineTo(0, height);
                path.close();
                break;
            case BOTTOM:
                path.moveTo(width / 2, height);
                path.lineTo(0, 0);
                path.lineTo(width, 0);
                path.close();
                break;
            default:
                throw new IllegalStateException();
        }
        path.offset(offsetX + halfStroke, offsetY + halfStroke);

    }
}

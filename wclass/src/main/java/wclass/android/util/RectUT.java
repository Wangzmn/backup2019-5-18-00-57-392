package wclass.android.util;

import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

/**
 * @作者 做就行了！
 * @时间 2019-03-20下午 7:24
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("SameParameterValue")
public class RectUT {
    /**
     * 返回rect较小的边长。
     *
     * @param rect 该rect。
     * @return 返回rect较小的边长。
     */
    public static float min(RectF rect) {
        float width = rect.width();
        float height = rect.height();
        return Math.min(width, height);
    }

    /**
     * 返回rect较大的边长。
     *
     * @param rect 该rect。
     * @return 返回rect较大的边长。
     */
    public static float max(RectF rect) {
        float width = rect.width();
        float height = rect.height();
        return Math.max(width, height);
    }
    //--------------------------------------------------

    /**
     * 返回rect较小的边长。
     *
     * @param rect 该rect。
     * @return 返回rect较小的边长。
     */
    public static int min(Rect rect) {
        int width = rect.width();
        int height = rect.height();
        return Math.min(width, height);
    }

    /**
     * 返回rect较大的边长。
     *
     * @param rect 该rect。
     * @return 返回rect较大的边长。
     */
    public static int max(Rect rect) {
        int width = rect.width();
        int height = rect.height();
        return Math.max(width, height);
    }
    //--------------------------------------------------

    /**
     * {@link RectUT#getCore(Rect, float[])}
     */
    public static float[] getCore(Rect rect) {
        float[] coreXY = new float[2];
        getCore(rect, coreXY);
        return coreXY;
    }

    /**
     * 获取rect的圆心。
     *
     * @param rect   该rect。
     * @param coreXY 圆心的xy。
     */
    public static void getCore(Rect rect, float[] coreXY) {
        coreXY[0] = rect.centerX();
        coreXY[1] = rect.centerY();
    }
    //--------------------------------------------------

    /**
     * {@link RectUT#delPadding(Rect, Rect, View)}
     */
    public static Rect delPadding(Rect viewRect, View view) {
        Rect result = new Rect();
        delPadding(viewRect, result, view);
        return result;
    }

    /**
     * 删除viewRect中的padding。
     *
     * @param viewRect view的区域信息。
     * @param result   删除viewRect中的padding，结果存放在result中。
     * @param view     view。
     */
    public static void delPadding(Rect viewRect, Rect result, View view) {
        result.left = viewRect.left + view.getPaddingLeft();
        result.top = viewRect.top + view.getPaddingTop();
        result.right = viewRect.right - view.getPaddingRight();
        result.bottom = viewRect.bottom - view.getPaddingBottom();
    }

    //////////////////////////////////////////////////


    /**
     * rect外边增加一圈大小。
     *
     * @param src     rect。
     * @param addSize 增加的大小。
     */
    public static void addAround(Rect src, int addSize) {
        addAround(src, addSize, src);
    }

    /**
     * rect外边增加一圈大小。
     *
     * @param src     rect。
     * @param addSize 增加的大小。
     * @param result  调整src后，将结果放入result。
     */
    public static void addAround(Rect src, int addSize, Rect result) {
        result.left = src.left - addSize;
        result.top = src.top - addSize;
        result.right = src.right + addSize;
        result.bottom = src.bottom + addSize;
    }

    /**
     * rect外边增加一圈大小。
     *
     * @param src     rect。
     * @param addSize 增加的大小。
     * @param result  调整src后，将结果放入result。
     */
    public static void addAround(Rect src, int addSize, RectF result) {
        result.left = src.left - addSize;
        result.top = src.top - addSize;
        result.right = src.right + addSize;
        result.bottom = src.bottom + addSize;
    }

    /**
     * rect外边减少一圈大小。
     *
     * @param src     rect。
     * @param delSize 减少的大小。
     */
    public static void delAround(Rect src, int delSize) {
        delAround(src, delSize, src);
    }

    /**
     * rect外边减少一圈大小。
     *
     * @param src     rect。
     * @param delSize 减少的大小。
     * @param result  调整src后，将结果放入result。
     */
    public static void delAround(Rect src, int delSize, Rect result) {
        result.left = src.left + delSize;
        result.top = src.top + delSize;
        result.right = src.right - delSize;
        result.bottom = src.bottom - delSize;
    }


    /**
     * rect外边减少一圈大小。
     *
     * @param src     rect。
     * @param delSize 减少的大小。
     * @param result  调整src后，将结果放入result。
     */
    public static void delAround(Rect src, float delSize, RectF result) {
        result.left = src.left + delSize;
        result.top = src.top + delSize;
        result.right = src.right - delSize;
        result.bottom = src.bottom - delSize;
    }

    /**
     * rect外边增加一圈大小。
     *
     * @param src     rect。
     * @param addSize 增加的大小。
     */
    public static void addAround(RectF src, float addSize) {
        addAround(src, addSize, src);
    }

    /**
     * rect外边减少一圈大小。
     *
     * @param src     rect。
     * @param addSize 减少的大小。
     * @param result  调整src后，将结果放入result。
     */
    public static void addAround(RectF src, float addSize, RectF result) {
        result.left = src.left - addSize;
        result.top = src.top - addSize;
        result.right = src.right + addSize;
        result.bottom = src.bottom + addSize;
    }

    /**
     * 外边减少一圈大小。
     *
     * @param baseRect 以该rect为基础。
     * @param delSize  减少的大小。
     */
    public static void delAround(RectF baseRect, float delSize) {
        baseRect.left += delSize;
        baseRect.top += delSize;
        baseRect.right -= delSize;
        baseRect.bottom -= delSize;
    }
    //////////////////////////////////////////////////

    /**
     * {@link #getLeftSquare(android.graphics.RectF, android.graphics.RectF)}
     */
    public static void getLeftSquare(Rect src, Rect dst) {
        int min = min(src);
        dst.left = src.left;
        dst.top = src.top;
        dst.right = src.left + min;
        dst.bottom = src.top + min;
    }

    /**
     * {@link #getLeftSquare(android.graphics.RectF, android.graphics.RectF)}
     */
    public static void getLeftSquare(Rect src, RectF dst) {
        int min = min(src);
        dst.left = src.left;
        dst.top = src.top;
        dst.right = src.left + min;
        dst.bottom = src.top + min;
    }

    /**
     * 获取src左上角的正方形rect。
     *
     * @param src 源rect。
     * @param dst 目标rect。
     */
    public static void getLeftSquare(RectF src, RectF dst) {
        float min = min(src);
        dst.left = src.left;
        dst.top = src.top;
        dst.right = src.left + min;
        dst.bottom = src.top + min;
    }

    //--------------------------------------------------

    /**
     * 获取src右上角的正方形。
     *
     * @param src 源rect。
     * @param dst 目标rect。
     */
    public static void getRightSquare(Rect src, Rect dst) {
        int min = min(src);
        dst.top = src.top;
        dst.right = src.right;
        dst.bottom = src.top + min;
        dst.left = src.right - min;
    }

    /**
     * {@link RectUT#getRightSquare(android.graphics.Rect, android.graphics.Rect)}
     */
    public static void getRightSquare(Rect src, RectF dst) {
        int min = min(src);
        dst.top = src.top;
        dst.right = src.right;
        dst.bottom = src.top + min;
        dst.left = src.right - min;
    }

    /**
     * {@link RectUT#getRightSquare(android.graphics.Rect, android.graphics.Rect)}
     */
    public static void getRightSquare(RectF src, RectF dst) {
        float min = min(src);
        dst.top = src.top;
        dst.right = src.right;
        dst.bottom = src.top + min;
        dst.left = src.right - min;
    }
    //////////////////////////////////////////////////

    /**
     * 调整横向属性。
     * @param src 他的横向属性。
     * @param addValue 增加的值。
     */
    public static void reHori(Rect src, int addValue){
        src.left+=addValue;
        src.right+=addValue;
    }
    /**
     * 调整横向属性。
     * @param src 他的横向属性。
     * @param addValue 增加的值。
     */
    public static void reHori(RectF src, int addValue){
        src.left+=addValue;
        src.right+=addValue;
    }
    //--------------------------------------------------

    /**
     * 调整纵向属性。
     * @param src 他的纵向属性。
     * @param addValue 增加的值。
     */
    public static void reVerti(Rect src, int addValue){
        src.top+=addValue;
        src.bottom+=addValue;
    }

    /**
     * 调整纵向属性。
     * @param src 他的纵向属性。
     * @param addValue 增加的值。
     */
    public static void reVerti(RectF src, int addValue){
        src.top+=addValue;
        src.bottom+=addValue;
    }
}

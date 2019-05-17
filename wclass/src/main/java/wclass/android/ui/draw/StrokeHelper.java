package wclass.android.ui.draw;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import wclass.android.ui.params.RectBoolean;
import wclass.android.util.RectUT;

/**
 * 完成于 2019年4月6日00:05:37
 *
 * @作者 做就行了！
 * @时间 2019-03-20下午 7:48
 * @该类描述： -
 * 1、生成指定数量描边的路径，方便直接引用。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * 1、通过该方法绘制描边：{@link #drawStrokes(Canvas, Paint)}。
 * 2、如果该变量为true：{@link #hasRestMidRegion}，
 * 则可以执行以下两步：
 * ①获取剩余中间区域的rect：{@link #getRestMidRegionRect()}。
 * ②获取剩余中间区域的4个角的圆角半径：{@link #getRestMidRegionCornerRadius()}。
 * 3、绘制区域中，与描边相关的属性改变时，必须调用{@link #reStroke(Rect)}
 * 或{@link #reStroke(float, float, float, float)}。
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 * 1、可扩展为gradient描边。
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class StrokeHelper {
    private static final boolean DEBUG = true;
    /**
     * 描边的路径。
     */
    private List<Path> paths;
    //--------------------------------------------------
    /**
     * 标记是否需要绘制描边。
     */
    boolean needDrawStroke;
    /**
     * 描边数量。
     */
    private int strokeCount;
    /**
     * 是否有中间绘制区域。
     * <p>
     * 友情提示：整个图形分为描边区域和中间区域。
     * <p>
     * 警告：当该变量为true时，才能使用以下两个属性：
     * {@link StrokeHelper#restMidRegionRect}
     * {@link StrokeHelper#cornerRadius}
     */
    private boolean hasRestMidRegion;
    /**
     * 中间区域圆角的半径。
     */
    private float cornerRadius;
    /**
     * 计算完描边区域后，剩余的中间区域。
     */
    private RectF restMidRegionRect = new RectF();
    //--------------------------------------------------

    /**
     * 绘制描边。
     *
     * @param canvas      画布。
     * @param strokePaint 绘制描边的画笔。
     */
    public void drawStrokes(Canvas canvas, Paint strokePaint) {
        if (DEBUG) {
            Log.e("TAG", getClass() + "#drawStrokes" +
                    "：needDrawStroke = " + needDrawStroke + " 。");
        }
        if (needDrawStroke) {
            if (DEBUG) {
                Log.e("TAG", getClass() + "#drawStrokes" +
                        "：一共" + strokeCount + "个描边。");
            }
            for (int i = 0; i < strokeCount; i++) {
                if (DEBUG) {
                    Log.e("TAG", getClass() + "#drawStrokes" +
                            "：正在绘制第" + i + "个描边。");
                }
                onDrawStroke(canvas, strokePaint, i);
            }
        }
    }

    /**
     * 每个描边绘制时，会调用该方法。
     *
     * @param strokePaint 绘制描边的画笔。
     * @param strokeDex   当前绘制的描边的下标。
     */
    protected void onDrawStroke(Canvas canvas, Paint strokePaint, int strokeDex) {
        drawStroke(canvas, strokePaint, strokeDex);
    }

    /**
     * 绘制指定下标的描边。
     * <p>
     * 警告：只能在{@link Drawable#draw}方法中使用。
     *
     * @param strokePaint 绘制描边的画笔。
     * @param strokeDex   当前绘制的描边的下标。
     */
    public void drawStroke(Canvas canvas, Paint strokePaint, int strokeDex) {
        int strokeColor = getStrokeColor(strokeDex);
        float strokeWidth = getStrokeWidth(strokeDex);
        Path strokePath = paths.get(strokeDex);
        strokePaint.setColor(strokeColor);
        strokePaint.setStrokeWidth(strokeWidth);
        canvas.drawPath(strokePath, strokePaint);
    }

    /**
     * 获取剩余中间区域的rect。
     * 如果您的图形有圆角，那么该path也有圆角。（只要您的条件不要太苛刻。）
     * <p>
     * 警告：
     * 1、只能在{@link Drawable#draw}方法中使用。
     * 2、如果无剩余的中间区域，那么，您传入的path参数重置为初始状态。
     *
     * @param path
     */
    public void getMidRegionPath(Path path) {
        if (hasRestMidRegion) {
            PathUT.makeCircleCornerRect(restMidRegionRect, cornerRadius, 0,
                    path, getCircleCornerParams());
        }else{
            path.rewind();
        }
    }

    //////////////////////////////////////////////////////////////////////
    public StrokeHelper() {
    }
    //--------------------------------------------------

    /**
     * 从外向内的描边们，他们的颜色和宽度。
     * <p>
     * 友情提示：
     * 0、2、4等偶数下标为：描边的颜色。
     * 1、3、5等奇数下标为：描边的宽度。
     * <p>
     * 例:
     * float[]{0xdd000000,1,0xff000000, 2,0xffffffff,5  }
     * 从外往里的第一个描边的颜色0xdd000000、粗度1。
     * 从外往里的第二个描边的颜色0xff000000、粗度2。
     * 从外往里的第三个描边的颜色0xffffffff、粗度5。
     */
    private float[] strokesColorAndWidth;

    /**
     * 获取整个图形的圆角半径，该值为准确的数值，不是相对于谁的百分比！！！
     * <p>
     * 友情提示：
     * 1、该类只能绘制4个圆角一样的矩形。
     */
    protected abstract float getCornerRadius();

    /**
     * 返回值为：从外向内的描边们，他们的颜色和宽度。
     * 如果没有描边，则可以返回null。
     * <p>
     * 友情提示：
     * 0、2、4等偶数下标为：描边的颜色。
     * 1、3、5等奇数下标为：描边的宽度。
     * <p>
     * 例:
     * float[]{0xdd000000,1,0xff000000, 2,0xffffffff,5  }
     * 从外往里的第一个描边的颜色0xdd000000、粗度1。
     * 从外往里的第二个描边的颜色0xff000000、粗度2。
     * 从外往里的第三个描边的颜色0xffffffff、粗度5。
     *
     * @return 多个描边的颜色和宽度。
     */
    protected abstract float[] getStrokesColorAndWidth();

    /**
     * 4个角中，哪些角需要绘制圆角。
     * 默认都需要。
     */
    protected RectBoolean getCircleCornerParams() {
        return RectBoolean.TRUE;
    }
    //////////////////////////////////////////////////

    /**
     * 计算描边们的绘制信息。
     *
     * @param x      绘制区域的x坐标。
     * @param y      绘制区域的y坐标。
     * @param width  绘制区域的宽。
     * @param height 绘制区域的高。
     * @return 所有描边宽的总和。
     */
    private float calculateStroke(float x, float y, float width, float height) {

        //所有描边宽的总和。
        float totalStrokeWidth = 0;
        for (int i = 0; i < strokeCount; i++) {
            float strokeWidth = getStrokeWidth(i);
            Path path = paths.get(i);
            float pureWidth = width - 2 * totalStrokeWidth;
            float pureHeight = height - 2 * totalStrokeWidth;
            //noinspection SuspiciousNameCombination
            PathUT.makeCircleCornerRect(pureWidth,
                    pureHeight,
                    cornerRadius,
                    strokeWidth, path,
                    totalStrokeWidth + x,
                    totalStrokeWidth + y, getCircleCornerParams());

            if (cornerRadius != 0) {
                cornerRadius -= strokeWidth;
                if (cornerRadius < 0) {
                    cornerRadius = 0;
                }
            }
            totalStrokeWidth += strokeWidth;
        }
        return totalStrokeWidth;
    }

    /**
     * 重新计算描边的绘制信息。
     *
     * @param bounds 绘制区域。
     */
    public final void reStroke(Rect bounds) {
        //当前可用的圆角半径。
        cornerRadius = getCornerRadius();
        //空的绘制区域，不需要绘制。
        if (bounds.isEmpty()) {
            if (DEBUG) {
                Log.e("TAG", getClass() + "#redraw" +
                        "：bounds.isEmpty  ");
            }
            flagEmptyBounds();
            return;
        }
        float[] scw = getStrokesColorAndWidth();
        //没有描边，不需要绘制描边。
        if (scw == null) {
            if (DEBUG) {
                Log.e("TAG", getClass() + "#redraw" +
                        "：StrokesColorAndWidth == null  ");
            }
            needDrawStroke = false;
            hasRestMidRegion = true;
            adjustMidRegionRect(bounds, 0);
            return;
        }
        int scwLength = scw.length;
        //描边数量为0，不需要绘制描边。
        if (scwLength == 0) {
            if (DEBUG) {
                Log.e("TAG", getClass() + "#redraw" +
                        "：StrokesColorAndWidthLength == 0  ");
            }
            needDrawStroke = false;
            hasRestMidRegion = true;
            adjustMidRegionRect(bounds, 0);
            return;
        }
        needDrawStroke = true;
        strokesColorAndWidth = scw;
        //描边数量。
        strokeCount = getPairs(scwLength);
        pathsToCount();
        int width = bounds.width();
        int height = bounds.height();
        float totalStrokeWidth = calculateStroke(bounds.left, bounds.top,
                width, height);
        //--------------------------------------------------
        //描边宽度大于总距离的一半，无需绘制。（横向、纵向都得绘制两条边的描边。）
        if (totalStrokeWidth > width / 2 || totalStrokeWidth > height / 2) {
            if (DEBUG) {
                Log.e("TAG", getClass() + "#redraw" +
                        "：totalStrokeWidth > width / 2 || totalStrokeWidth > height / 2  ");
            }
            hasRestMidRegion = false;
        } else {
            hasRestMidRegion = true;
            adjustMidRegionRect(bounds, totalStrokeWidth);
        }
        if (DEBUG) {
            Log.e("TAG", getClass() + "#redraw" +
                    "：needDrawStroke =  " + needDrawStroke + " 。");
            Log.e("TAG", getClass() + "#redraw" +
                    "：hasRestMidRegion = " + hasRestMidRegion + " 。");
            Log.e("TAG", getClass() + "#redraw" +
                    "：strokesColorAndWidthLength = " + scwLength + " 。");
            Log.e("TAG", getClass() + "#redraw" +
                    "：strokeCount = " + strokeCount + " 。");
        }

    }

    /**
     * 补足{@link Path}对象，存放描边路径。
     */
    private void pathsToCount() {
        if (paths == null) {
            paths = new ArrayList<>();
        }
        int size = paths.size();
        if (size < strokeCount) {
            for (int i = size; i < strokeCount; i++) {
                paths.add(new Path());
            }
        }
    }

    /**
     * 调整剩余区域的rect。
     *
     * @param bounds      总的绘制区域。
     * @param strokeWidth 描边宽。
     */
    private void adjustMidRegionRect(Rect bounds, float strokeWidth) {
        RectUT.delAround(bounds, strokeWidth, restMidRegionRect);
    }

    /**
     * 调整剩余区域的rect。
     *
     * @param x           总的rect的x坐标。
     * @param y           总的rect的y坐标。
     * @param width       总的rect的宽。
     * @param height      总的rect的高。
     * @param strokeWidth 描边宽。
     */
    private void adjustRestMidRegionRect(float x, float y,
                                         float width, float height,
                                         float strokeWidth) {
        restMidRegionRect.left = x + strokeWidth;
        restMidRegionRect.top = y + strokeWidth;
        restMidRegionRect.right = restMidRegionRect.left + width - strokeWidth;
        restMidRegionRect.bottom = restMidRegionRect.top + height - strokeWidth;
    }

    /**
     * 标记空的绘制区域。
     */
    private void flagEmptyBounds() {
        needDrawStroke = false;
        hasRestMidRegion = false;
    }

    /**
     * 重新计算描边的绘制信息。
     *
     * @param x      绘制区域x坐标。
     * @param y      绘制区域y坐标。
     * @param width  绘制区域宽。
     * @param height 绘制区域高。
     */
    public void reStroke(float x, float y, float width, float height) {
        /*new*/

        //当前可用的圆角半径。
        cornerRadius = getCornerRadius();
        //空的绘制区域，不需要绘制。
        if (width <= 0 || height <= 0) {
            flagEmptyBounds();
            return;
        }
        float[] scw = getStrokesColorAndWidth();
        //没有描边，不需要绘制描边。
        if (scw == null) {
            needDrawStroke = false;
            hasRestMidRegion = true;
            adjustRestMidRegionRect(x, y, width, height, 0);
            return;
        }
        int scwLength = scw.length;
        //描边数量为0，不需要绘制描边。
        if (scwLength == 0) {
            needDrawStroke = false;
            hasRestMidRegion = true;
            adjustRestMidRegionRect(x, y, width, height, 0);
            return;
        }
        strokesColorAndWidth = scw;
        //描边数量。
        strokeCount = getPairs(scwLength);
        pathsToCount();
        float totalStrokeWidth = calculateStroke(x, y, width, height);
        //--------------------------------------------------
        //描边宽度大于总距离的一半，无需绘制。（横向、纵向都得绘制两条边的描边。）
        if (totalStrokeWidth > width / 2 || totalStrokeWidth > height / 2) {
            hasRestMidRegion = false;
        } else {
            hasRestMidRegion = true;
            adjustRestMidRegionRect(x, y, width, height, totalStrokeWidth);
        }

    }
    //--------------------------------------------------

    /**
     * 获取一共多少对。
     *
     * @param i 该数。
     * @return 一共多少对。
     */
    private int getPairs(int i) {
        return i >> 1;
    }
    //--------------------------------------------------

    /**
     * 获取描边数量。
     */
    private int getStrokeCount() {
        return strokeCount;
    }

    /**
     * 获取指定描边的颜色。
     *
     * @param strokeDex 该描边的下标。
     * @return 指定描边的颜色。
     */
    private int getStrokeColor(int strokeDex) {
        return (int) strokesColorAndWidth[strokeDex * 2];
    }

    /**
     * 获取指定描边的宽。
     *
     * @param strokeDex 该描边的下标。
     * @return 指定描边的宽。
     */
    private float getStrokeWidth(int strokeDex) {
        return strokesColorAndWidth[strokeDex * 2 + 1];
    }

    //////////////////////////////////////////////////

    /**
     * 获取中间绘制区域的圆角半径。
     * {@link StrokeHelper#cornerRadius}
     */
    public float getRestMidRegionCornerRadius() {
        return cornerRadius;
    }

    /**
     * 是否有中间绘制区域。
     * {@link StrokeHelper#hasRestMidRegion}
     */
    public boolean hasRestMidRegion() {
        return hasRestMidRegion;
    }

    /**
     * 获取中间绘制区域的rect。
     * {@link StrokeHelper#restMidRegionRect}
     */
    public RectF getRestMidRegionRect() {
        return restMidRegionRect;
    }

}

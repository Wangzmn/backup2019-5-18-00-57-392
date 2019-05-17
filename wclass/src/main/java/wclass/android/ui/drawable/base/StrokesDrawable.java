package wclass.android.ui.drawable.base;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;

import wclass.android.ui.draw.PaintUT;
import wclass.android.ui.draw.StrokeHelper;
import wclass.android.ui.params.RectBoolean;
import wclass.android.util.RectUT;

/**
 * 完成于 2019年4月6日00:03:17
 *
 * @作者 做就行了！
 * @时间 2019-02-06下午 5:16
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class StrokesDrawable extends Drawable {
    private static final boolean DEBUG = false;
    //////////////////////////////////////////////////
    /**
     * 绘制描边的画笔。
     */
    private Paint strokePaint;
    /**
     * 绘制描边的辅助类。{@link StrokeHelper}。
     * <p>
     * 友情提示：很实用哦 ~
     */
    private StrokeHelper strokeHelper;
    //--------------------------------------------------
    /**
     * 标记是否需要重绘。
     */
    private boolean needDraw;

    /**
     * 绘制区域的rect。
     */
    private Rect drawingRect = new Rect();
    /**
     * 原始的区域。
     */
    private Rect originRect = new Rect();

    /**
     * 绘制区域较短的那条边的大小。
     */
    private int minSide;
    private boolean drawBoundsChanged;
    /**
     * 标记是否需要重新计算描边信息。
     */
    private boolean needRecalculated;

    /**
     * 设置您需要绘制的区域。
     *
     * @param drawingRect1 您的绘制区域。
     */
    public void setDrawingRect(Rect drawingRect1) {
        drawingRect.set(drawingRect1);
        needDraw = true;
        needRecalculated = true;
        invalidateSelf();
    }

    /**
     * 获取原始的绘制区域。
     *
     * @param youRect 你的rect。
     */
    public void getOriginRect(Rect youRect) {
        youRect.set(originRect);
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 构造方法。
     */
    public StrokesDrawable() {
        strokeHelper = new MStrokeHelper();
        strokePaint = onCreateStrokePaint();
    }
    class MStrokeHelper extends StrokeHelper{

        @Override
        protected float getCornerRadius() {
            return StrokesDrawable.this.getCornerRadius();
        }

        @Override
        protected float[] getStrokesColorAndWidth() {
            return StrokesDrawable.this.getStrokesColorAndWidth();
        }

        @Override
        protected void onDrawStroke(Canvas canvas, Paint strokePaint, int strokeDex) {
            StrokesDrawable.this.onDrawStroke(canvas, strokePaint, strokeDex);
        }

        @Override
        protected RectBoolean getCircleCornerParams() {
            return StrokesDrawable.this.getCircleCornerParams();
        }
    }

    /**
     * 4个角中，哪些角需要绘制圆角。
     * 默认都需要。
     */
    protected RectBoolean getCircleCornerParams() {
        return RectBoolean.TRUE;
    }
    //////////////////////////////////////////////////

    /**
     * 重新计算描边的绘制信息信息。
     * <p>
     * 友情提示：如果手动更改了数组：{@link #getStrokesColorAndWidth()}，
     * 必须调用该方法！！！
     */
    public void redraw() {
        needDraw = true;
        needRecalculated = true;
    }
    //--------------------------------------------------

    /**
     * 设置指定描边的颜色。
     * <p>
     * 警告：子类需要实现{@link #getStrokesColorAndWidth()}方法。
     *
     * @param strokeDex 从外向内，描边的下标。
     * @param color     该描边的颜色。
     */
    public final void setStrokeColor(int strokeDex, int color) {
        try {
            getStrokesColorAndWidth()[strokeDex * 2] = color;
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof NullPointerException) {
                throw new IllegalArgumentException("getStrokesColorAndWidth方法" +
                        "获取的数组为null。");
            } else {
                throw new IllegalArgumentException("getStrokesColorAndWidth方法" +
                        "获取的数组为长度为：" + getStrokesColorAndWidth().length + " ," +
                        "请求的下标为：" + strokeDex + " 。");
            }
        }

    }

    /**
     * 设置指定描边的宽度。
     * <p>
     * 警告：子类需要实现{@link #getStrokesColorAndWidth()}方法。
     *
     * @param strokeDex 从外向内，描边的下标。
     * @param width     该描边的宽度。
     */
    public final void setStrokeWidth(int strokeDex, float width) {
        try {
            getStrokesColorAndWidth()[strokeDex * 2 + 1] = width;
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof NullPointerException) {
                throw new IllegalArgumentException("getStrokesColorAndWidth方法" +
                        "获取的数组为null。");
            } else {
                throw new IllegalArgumentException("getStrokesColorAndWidth方法" +
                        "获取的数组为长度为：" + getStrokesColorAndWidth().length + " ," +
                        "请求的下标为：" + strokeDex + " 。");
            }
        }
    }
    //////////////////////////////////////////////////

    /**
     * 获取绘制区域圆角的半径。
     * <p>
     * 友情提示：
     * 1、如果绘制描边，描边也将会有圆角。
     * 2、如果您需要绘制剩余的中间区域，
     * 那么该区域的圆角由您自己控制是否绘制。
     * 99、该方法在绘制期间实时获取，父类不保存该方法获取的数据。
     */
    protected abstract float getCornerRadius();

    /**
     * 返回值为：从外向内的描边们，他们的颜色和宽度。
     * 如果没有描边，则可以返回null。
     * <p>
     * 0、2、4等偶数下标为：描边的颜色。
     * 1、3、5等奇数下标为：描边的宽度。
     * <p>
     * 例:
     * float[]{0xff00ffff,1,0xff00ff00, 2,0xffff0000,5  }
     * 从外往里的第一个描边的颜色为0xff00ffff、粗度为1。
     * 从外往里的第二个描边的颜色为0xff00ff00、粗度为2。
     * 从外往里的第三个描边的颜色为0xffff0000、粗度为5。
     * <p>
     * 友情提示：
     * 99、该方法在绘制期间实时获取，父类不保存该方法获取的数据。
     *
     * @return 存放若干个描边的颜色和宽度的数组，
     * 该数组可以为null、也可以为空。
     */
    protected abstract float[] getStrokesColorAndWidth();

    /**
     * 创建绘制描边的画笔。
     * <p>
     * 友情提示：该方法是在父类的构造方法中调用的。
     * 所以不能使用子类构造方法中的参数。
     *
     * @return 绘制描边的画笔。
     */
    protected Paint onCreateStrokePaint() {
        return PaintUT.strokePaint();
    }

    //////////////////////////////////////////////////

    @SuppressWarnings("NullableProblems")
    @Override
    public void draw(Canvas canvas) {
        preDraw(canvas);
        if (DEBUG) {
            Log.e("TAG", getClass() + "#draw：" +
                    "needDraw = " + needDraw + " ," +
                    "originRect = " + originRect + " ," +
                    "drawingRect" + drawingRect + " 。");
        }
        if (needDraw) {
            if (needRecalculated) {
                needRecalculated = false;
                if (DEBUG) {
                    Log.e("TAG", getClass() + "#draw：" +
                            "needRecalculated = " + needRecalculated + " 。");
                }
                strokeHelper.reStroke(drawingRect);
            }
            strokeHelper.drawStrokes(canvas, strokePaint);
            if (strokeHelper.hasRestMidRegion()) {
                if (DEBUG) {
                    Log.e("TAG", getClass() + "#draw：" +
                            "hasRestMidRegion = " + strokeHelper.hasRestMidRegion() + " 。");
                }
                onDrawRestMidRegion(canvas, strokeHelper.getRestMidRegionRect(),
                        strokeHelper.getRestMidRegionCornerRadius());
            }
        }
    }

    protected void preDraw(Canvas canvas) {
        if(drawBoundsChanged){
            drawBoundsChanged  =false;
            minSide = RectUT.min(originRect);
            /**
             * 让子类设置自己的绘制区域。
             */
            drawingRect.set(getDrawingRect(originRect));
            if (drawingRect.isEmpty()) {
                needDraw = false;
                return;
            }
            needDraw = true;
            needRecalculated = true;
        }
    }

    /**
     * 每个描边绘制时，会调用该方法。
     *
     * @param strokePaint 绘制描边的画笔。
     * @param strokeDex   当前绘制的描边的下标。
     */
    protected void onDrawStroke(Canvas canvas, Paint strokePaint, int strokeDex) {
        preDrawStroke(canvas, strokePaint, strokeDex);
        strokeHelper.drawStroke(canvas, strokePaint, strokeDex);
    }

    /**
     * 每个描边绘制之前，会调用该方法。
     * <p>
     * 该方法在以下方法之前调用：
     * {@link #onDrawStroke(Canvas, Paint, int)}
     *
     * @param strokePaint 绘制描边的画笔。
     * @param strokeDex   当前绘制的描边的下标。
     */
    protected void preDrawStroke(Canvas canvas, Paint strokePaint, int strokeDex) {

    }

    //--------------------------------------------------

    /**
     * drawable的大小改变时，该方法仅仅做个标记，
     * 最后在该方法中处理：{@link #preDraw(Canvas)}。
     *
     * 理由：{@link #draw(Canvas)}之前必定会调用{@link #onBoundsChange(Rect)}。
     * 但是调用几次呢？1次或许多次。
     * 所以{@link #onBoundsChange(Rect)}中不适合逻辑处理，
     * 真正需要计算的那一次是绘制的那一次，
     * 所以在{@link #preDraw(Canvas)}中处理绝对是没错的。
     */
    @Override
    protected void onBoundsChange(Rect bounds) {
        if (DEBUG) {
            Log.e("TAG", getClass()
                    + "#onBoundsChange");
        }
        super.onBoundsChange(bounds);
        originRect.set(bounds);
        drawBoundsChanged = true;
    }

    /**
     * 获取绘制区域较短的那条边。
     * 只能在这个方法之后调用：{@link #onBoundsChange(Rect)}。
     *
     * @return 绘制区域较短的那条边。
     */
    protected int getMinSide() {
        return minSide;
    }

    /**
     * 绘制区域改变之前的回调。
     *
     * @param bounds 绘制区域。
     * @return 剩余的绘制区域。
     */
    protected Rect getDrawingRect(Rect bounds) {
        return bounds;
    }

    /**
     * 绘制剩余的中间区域。
     */
    @SuppressWarnings({"unused", "WeakerAccess"})
    protected void onDrawRestMidRegion(Canvas canvas,
                                       RectF restMidRegionRect,
                                       float restMidRegionCornerRadius) {
        if (DEBUG) {
            Log.e("TAG", getClass()
                    + "#onDrawRestMidRegion");
        }
    }

    //////////////////////////////////////////////////
    @Override
    public void setAlpha(int alpha) {
        strokePaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        strokePaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}

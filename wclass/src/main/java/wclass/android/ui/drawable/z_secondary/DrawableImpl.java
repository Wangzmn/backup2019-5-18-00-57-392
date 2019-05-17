package wclass.android.ui.drawable.z_secondary;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import wclass.android.ui.draw.PaintUT;

/**
 * @作者 做就行了！
 * @时间 2019-03-30下午 5:20
 * @该类描述： -
 * 1、{@link Drawable}简单的实现类。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("WeakerAccess")
public abstract class DrawableImpl extends Drawable {

    /**
     * 父类构造方法中创建的画笔。
     */
    protected Paint paint;
    //--------------------------------------------------
    /**
     * 短边长度。
     * <p>
     * 友情提示：该变量在{@link #onBoundsChange(Rect)}中被赋值。
     */
    protected int minSide;
    protected int width;
    protected int height;
    //--------------------------------------------------

    /**
     * 需要绘制的区域。
     */
    protected Rect bounds = new Rect();

    /**
     * 以{@link #bounds}的左上角为正方形的左上角，
     * 正方形的边长为{@link #bounds}的短边。
     */
//    protected Rect square = new Rect();

    public DrawableImpl() {
        paint = onCreatePaint();
    }

    /**
     * 创建画笔。
     * <p>
     * 警告：该方法被写在了构造方法中！！！
     * 所以：该方法中不能使用子类的构造方法中的参数。
     */
    protected Paint onCreatePaint() {
        return PaintUT.solidPaint();
    }

    /**
     * 获取画笔。
     */
    public Paint getPaint() {
        return paint;
    }

    /**
     * 获取短边长度。
     * <p>
     * 警告：该方法只能在{@link #draw(Canvas)}
     * 中使用。
     */
    public int getMinSide() {
        return minSide;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        width = bounds.width();
        height = bounds.height();
        minSide = Math.min(width, height);
        this.bounds.set(bounds);
//        square.left = this.bounds.left;
//        square.top = this.bounds.top;
//        square.right = this.bounds.left + minSide;
//        square.bottom = this.bounds.top + minSide;
    }

    /**
     * 获取左边的正方形。
     *
     * @param pendingRect 待设置的rect。
     */
    protected void getLeftSquare(Rect pendingRect) {
        pendingRect.left = bounds.left;
        pendingRect.top = bounds.top;
        pendingRect.right = bounds.left + minSide;
        pendingRect.bottom = bounds.top + minSide;
    }

    /**
     * 获取右边的正方形。
     *
     * @param pendingRect 待设置的rect。
     */
    protected void getRightSquare(Rect pendingRect) {
        pendingRect.top = bounds.top;
        pendingRect.right = bounds.right;
        pendingRect.bottom = bounds.bottom;
        pendingRect.left = bounds.right - minSide;
    }

    /**
     * 获取左边的正方形。
     *
     * @param pendingRect 待设置的rect。
     */
    protected void getLeftSquare(RectF pendingRect) {
        pendingRect.left = bounds.left;
        pendingRect.top = bounds.top;
        pendingRect.right = bounds.left + minSide;
        pendingRect.bottom = bounds.top + minSide;
    }

    /**
     * 获取右边的正方形。
     *
     * @param pendingRect 待设置的rect。
     */
    protected void getRightSquare(RectF pendingRect) {
        pendingRect.top = bounds.top;
        pendingRect.right = bounds.right;
        pendingRect.bottom = bounds.bottom;
        pendingRect.left = bounds.right - minSide;
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}

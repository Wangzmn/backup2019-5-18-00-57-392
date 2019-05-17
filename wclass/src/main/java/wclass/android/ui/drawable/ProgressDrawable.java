package wclass.android.ui.drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;

import wclass.android.ui.draw.PathUT;
import wclass.android.ui.drawable.z_secondary.DrawableImpl;
import wclass.util.MathUT;

/**
 * @作者 做就行了！
 * @时间 2019-04-18下午 11:31
 * @该类描述： -
 * 1、横向进度条，满进度时两端都是圆角。
 * 未满进度时：开始端是圆角，进度端为直角。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("WeakerAccess")
public class ProgressDrawable extends DrawableImpl {
    private static final boolean DEBUG = false;
    //////////////////////////////////////////////////
    /**
     * 进度值。
     * 该值范围：0至1。
     */
    private float progress;

    /**
     * 标记绘制区域是否异常.
     */
    private boolean freak;

    /**
     * 存放绘制路径。
     */
    private Path path = new Path();
    //////////////////////////////////////////////////

    /**
     * 构造方法。
     */
    public ProgressDrawable() {
        this(1,0xff00ff00);
    }

    /**
     * 构造方法。
     */
    public ProgressDrawable(int color) {
        this(1,color);
    }
    /**
     * 构造方法。
     *
     * @param progress 当前进度。
     */
    public ProgressDrawable(float progress,int color) {
        this.progress = MathUT.limit(progress, 0, 1);
        paint.setColor(color);
        if (DEBUG) {
            paint.setColor(0xff00ffff);
        }
    }
    //////////////////////////////////////////////////

    /**
     * 设置当前进度。
     *
     * @param progress 当前进度。
     */
    public void setProgress(float progress) {
        progress = MathUT.limit(progress, 0, 1);
        if (this.progress == progress) return;
        this.progress = progress;
        invalidateSelf();
    }

    /**
     * 设置进度的颜色。
     *
     * @param color 进度的颜色。
     */
    public void setColor(int color) {
        paint.setColor(color);
    }

    //////////////////////////////////////////////////

    /**
     * 获取当前进度。
     */
    public float getProgress() {
        return progress;
    }

    /**
     * 获取绘制进度的画笔。
     */
    public Paint getPaint(){
        return paint;
    }
    //////////////////////////////////////////////////
    @Override
    public void draw(Canvas canvas) {
        if (freak) {
            Log.e("TAG", " bounds = " + bounds + " 。高大于宽，无法绘制。");
            return;
        }
        PathUT.progressInBounds(bounds, path, progress);
        canvas.drawPath(path, paint);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        if (height > width) {
            freak = true;
        }
    }
}

package wclass.android.ui.view.progressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import wclass.android.ui.view.mover_view.MoverView;
import wclass.android.ui.draw.DrawableUT;
import wclass.enums.Orien2;
import wclass.util.MathUT;


/**
 * 运行于 2019年3月26日15:27:21
 *
 * @作者 做就行了！
 * @时间 2019-03-23下午 6:26
 * @该类描述： -
 * 1、简单的进度条。
 * 可以通过重写，改变功能。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("unused")
public class ProgressBar extends View {

    private static final boolean DEBUG = true;
    /**
     * 最大进度值。
     */
    private int mMaxProgress;

    /**
     * 当前进度值。
     */
    private int mProgress;

    /**
     * 进度条的方向，横向/纵向。
     */
    private Orien2 mProgressOrien = Orien2.HORIZONTAL;

    /**
     * 默认不开启：从上往下/从左往右绘制进度条。
     * 该值为true时：反方向绘制进度条，从下往上/从右往左开始绘制。
     */
    private boolean isReverseProgress;
    //--------------------------------------------------
    /**
     * 滑道背景图片。
     */
    private Drawable mTrackBgDrawable;
    /**
     * 进度值图片。
     */
    private Drawable mProgressDrawable;
    //--------------------------------------------------
    /**
     * 标记该类是否初始化完毕。
     */
    private boolean init;
    /**
     * drawable的装态。
     * {@link #drawableStateChanged()}时，赋值该变量。
     */
    private int[] drawableState;
    //////////////////////////////////////////////////

    /**
     * {@link #ProgressBar(Context, int)}
     */
    public ProgressBar(Context context) {
        this(context, 100);
    }

    /**
     * 构造方法。
     *
     * @param maxProgress 最大进度值。
     */
    public ProgressBar(Context context, int maxProgress) {
        super(context);
        if (maxProgress < 1) {
            throw new IllegalArgumentException("maxProgress必须大于0。");
        }
        this.mMaxProgress = maxProgress;
    }

    //--------------------------------------------------

    /**
     * 设置进度值。
     *
     * @param progress 当前进度。
     */
    public void setProgress(int progress) {
        progress = MathUT.limit(progress, 0, mMaxProgress);
        if (mProgress == progress) {
            return;
        }
        mProgress = progress;
        optimizeInvalidate();
    }

    /**
     * 获取当前进度值。
     */
    public int getProgress() {
        return mProgress;
    }

    /**
     * 获取最大进度值。
     */
    public int getMaxProgress() {
        return mMaxProgress;
    }

    /**
     * 获取当前进度的float百分比。
     *
     * @return 当前进度的float百分比，范围：0至1。
     */
    public float getProgressPer() {
        return mProgress / (float) mMaxProgress;
    }

    /**
     * 设置进度条的方向。
     * {@link #mProgressOrien}。
     */
    public void setOrien(Orien2 orien) {
        if (mProgressOrien != orien) {
            mProgressOrien = orien;
            optimizeInvalidate();
        }
    }

    /**
     * 获取进度条的方向。
     * {@link #mProgressOrien}。
     */
    public Orien2 getOrien() {
        return mProgressOrien;
    }

    /**
     * 设置是否需要反向绘制进度条。
     * {@link #isReverseProgress}。
     *
     * @param reverseProgress true：反向绘制进度条。
     *                       从下往上/从右往左开始绘制。
     */
    public void setReverseProgress(boolean reverseProgress) {
        if (isReverseProgress != reverseProgress) {
            isReverseProgress = reverseProgress;
            optimizeInvalidate();
        }
    }

    /**
     * 是否是反向绘制的进度条。
     * {@link #isReverseProgress}。
     * <p>
     * return true：反方向开始绘制。
     */
    public boolean isReverseProgress() {
        return isReverseProgress;
    }
    //--------------------------------------------------


    /**
     * 设置滑道图片。
     * {@link #mTrackBgDrawable}。
     */
    public void setDrawableTrackBg(Drawable trackBgDrawable) {
        if (mTrackBgDrawable != trackBgDrawable) {
            DrawableUT.dismiss(this, mTrackBgDrawable);
            DrawableUT.setState(trackBgDrawable, drawableState);
            mTrackBgDrawable = trackBgDrawable;
            if (init) {
                //todo 少写一个tint。
                trackBgDrawable.setBounds(trackBgRect);
                invalidate();
            }
        }
    }

    /**
     * 设置进度图片。
     * {@link #mProgressDrawable}。
     *
     * fix 该方法连接  {@link MoverView}
     */
    public void setDrawableProgress(Drawable trackProgressDrawable) {
        if (mProgressDrawable != trackProgressDrawable) {
            DrawableUT.dismiss(this, mProgressDrawable);
            DrawableUT.setState(trackProgressDrawable, drawableState);
            mProgressDrawable = trackProgressDrawable;
            if (init) {
                //todo 少写一个tint。
                trackProgressDrawable.setBounds(trackProgressCurrRect);
                invalidate();
            }
        }
    }

    /**
     * 优化刷新界面。
     */
    private void optimizeInvalidate() {
        if (init) {
            invalidate();
        }
    }
    //////////////////////////////////////////////////
    /*domain 调整大小相关。*/
    /**
     * 滑道背景的rect。
     */
    private Rect trackBgRect = new Rect();
    /**
     * 进度的最大rect。
     */
    private Rect trackProgressMaxRect = new Rect();
    /**
     * 进度的当前rect。
     */
    private Rect trackProgressCurrRect = new Rect();
    //--------------------------------------------------

    /**
     * {@link #trackBgRect}
     */
    public Rect getTrackBg() {
        return trackBgRect;
    }

    /**
     * {@link #trackProgressMaxRect}
     */
    public Rect getTrackProgressMaxRect() {
        return trackProgressMaxRect;
    }

    /**
     * {@link #trackProgressCurrRect}
     */
    public Rect getTrackProgressCurrRect() {
        return trackProgressCurrRect;
    }

    //--------------------------------------------------
    /*layout中布局。*/

    /**
     * 控件可用的布局区域。
     */
    Rect usableRect = new Rect();

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!changed) {
            return;
        }
        int width = right - left;
        int height = bottom - top;
        usableRect.left = getPaddingLeft();
        usableRect.top = getPaddingTop();
        usableRect.right = width - getPaddingRight();
        usableRect.bottom = height - getPaddingBottom();
        if (DEBUG) {
//            Log.e("TAG"," left = "+left);
//            Log.e("TAG"," top = "+top);
//            Log.e("TAG"," right = "+right);
//            Log.e("TAG"," bottom = "+bottom);
//            Log.e("TAG"," getPaddingLeft() = "+getPaddingLeft());
//            Log.e("TAG"," getPaddingTop() = "+getPaddingTop());
//            Log.e("TAG"," getPaddingRight() = "+getPaddingRight());
//            Log.e("TAG"," getPaddingBottom() = "+getPaddingBottom());
        }

        if (!init) {
            init = true;
        }
        preLayout(usableRect);
        onResizeTrackBg(trackBgRect, usableRect);
        onResizeTrackProgressMaxRect(trackProgressMaxRect, usableRect);

        //调整滑道背景大小。
        if (mTrackBgDrawable != null) {
            mTrackBgDrawable.setBounds(trackBgRect);
        }
    }

    protected void preLayout(Rect usableRect) {
    }

    /**
     * 重新调整滑道背景的rect。
     *
     * @param trackBg    需要调整的滑道背景的rect。
     * @param usableRect 可用布局区域。
     */
    protected void onResizeTrackBg(Rect trackBg, Rect usableRect) {
        trackBg.set(usableRect);
    }

    /**
     * 重新调整进度的最大rect。
     *
     * @param trackProgress 滑道最大进度的rect。
     * @param usableRect    可用布局区域。
     */
    protected void onResizeTrackProgressMaxRect(Rect trackProgress, Rect usableRect) {
        trackProgress.set(usableRect);
    }
    //--------------------------------------------------

    /**
     * 重新调整当前进度的rect。
     *
     * fix 该方法连接  {@link MoverView}
     *
     * @param trackProgressCurrRect 需要调整的当前进度的rect。
     * @param trackProgressMaxRect  最大进度的rect。
     * @param progress              当前进度值。
     * @param maxProgress           最大进度值。
     */
    protected void onResizeTrackProgressCurrRect(Rect trackProgressCurrRect,
                                                 Rect trackProgressMaxRect,
                                                 int progress, int maxProgress) {
        switch (mProgressOrien) {
            case HORIZONTAL:
                resizeProgressForHori(trackProgressCurrRect,
                        trackProgressMaxRect,
                        progress, maxProgress);
                break;
            case VERTICAL:
                resizeProgressForVerti(trackProgressCurrRect,
                        trackProgressMaxRect,
                        progress, maxProgress);
                break;
            default:
                throw new IllegalStateException();
        }
        //--------------------------------------------------
        if (mProgressDrawable != null) {
            mProgressDrawable.setBounds(trackProgressCurrRect);
        }
    }

    /**
     * 调整纵向的当前进度的rect。
     */
    private void resizeProgressForVerti(Rect trackProgressCurrRect, Rect trackProgressMaxRect, int progress, int maxProgress) {

        int height = trackProgressMaxRect.height();
        //进度百分比。
        float per = ((float) progress / maxProgress);
        //反向绘制。从下往上绘制。
        if (isReverseProgress) {
            trackProgressCurrRect.bottom = trackProgressMaxRect.bottom;
            //top通过进度设置。
            trackProgressCurrRect.top =
                    trackProgressMaxRect.bottom - (int) (per * height + 0.5f);


        }
        //正常绘制。从上往下绘制。
        else {
            trackProgressCurrRect.top = trackProgressMaxRect.top;
            //bottom通过进度设置。
            trackProgressCurrRect.bottom = trackProgressMaxRect.top
                    + (int) (per * height + 0.5f);
        }
        //纵向时，横向属性不需要改变。
        trackProgressCurrRect.left = trackProgressMaxRect.left;
        trackProgressCurrRect.right = trackProgressMaxRect.right;
    }

    /**
     * 调整横向的当前进度的rect。
     */
    private void resizeProgressForHori(Rect trackProgressCurrRect,
                                       Rect trackProgressMaxRect,
                                       int progress, int maxProgress) {
        int width = trackProgressMaxRect.width();
        //进度百分比。
        float per = ((float) progress / maxProgress);
        //反向绘制。从右往左绘制。
        if (isReverseProgress) {
            trackProgressCurrRect.right = trackProgressMaxRect.right;
            //left通过进度设置。
            trackProgressCurrRect.left =
                    trackProgressMaxRect.right - (int) (per * width + 0.5f);

        }
        //正常方向绘制。从左往右绘制。
        else {
            trackProgressCurrRect.left = trackProgressMaxRect.left;
            //right需要通过进度设置。
            trackProgressCurrRect.right = trackProgressMaxRect.left
                    + (int) (per * width + 0.5f);
        }
        //横向时，纵向值直接引用。
        trackProgressCurrRect.top = trackProgressMaxRect.top;
        trackProgressCurrRect.bottom = trackProgressMaxRect.bottom;
    }

    /**
     * {@link #onResizeTrackProgressCurrRect(Rect, Rect, int, int)}
     */
    private void rerectProgressRect() {
        onResizeTrackProgressCurrRect(trackProgressCurrRect,
                trackProgressMaxRect,
                mProgress, mMaxProgress);
    }

    //////////////////////////////////////////////////
    /*domain 绘制相关*/

    /**
     * 在该方法中绘制滑道和进度的图片。
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //每次绘制时，设置当前进度的rect。
        rerectProgressRect();
        onDrawTrackBg(canvas, trackBgRect);
        onDrawTrackProgress(canvas, trackProgressCurrRect);
        if (DEBUG) {
            Rect rect = new Rect();
            getDrawingRect(rect);

            Log.e("TAG", " drawingRect = " + rect);
            Log.e("TAG", " usableRect = " + usableRect);
            Log.e("TAG", " trackBgRect = " + trackBgRect);
            Log.e("TAG", " trackProgressCurrRect = " + trackProgressCurrRect);
        }
    }

    /**
     * 在该方法中绘制滑道背景。
     *
     * @param trackBgRect 滑道的rect。
     */
    protected void onDrawTrackBg(Canvas canvas, Rect trackBgRect) {
        if (mTrackBgDrawable != null) {
            mTrackBgDrawable.draw(canvas);
        }
    }

    /**
     * 在该方法中绘制进度。
     *
     * fix 该方法连接  {@link MoverView}
     * @param trackProgressCurrRect 进度的rect。
     *
     */
    protected void onDrawTrackProgress(Canvas canvas, Rect trackProgressCurrRect) {
        if (mProgressDrawable != null) {
            mProgressDrawable.draw(canvas);
        }
    }
    //////////////////////////////////////////////////
    /*domain view的状态相关。*/

    /**
     * view状态改变时，通过该方法通知drawable状态改变。
     * 该方法被优化为：{@link #onDrawableStateChanged(int[])}
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Override
    @Deprecated
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (onDrawableStateChanged(getDrawableState())) {
            invalidate();
        }
    }

    /**
     * 该方法中设置drawable的状态。
     *
     * @param drawableState drawable的状态信息。
     * @return true：需要重新绘图。
     */
    protected boolean onDrawableStateChanged(int[] drawableState) {
        boolean changed = DrawableUT.setState(mTrackBgDrawable, drawableState);
        changed |= DrawableUT.setState(mProgressDrawable, drawableState);
        return changed;
    }
    //////////////////////////////////////////////////
    /*参数获取相关的方法。*/

    public Drawable getTrackBgDrawable() {
        return mTrackBgDrawable;
    }

    public Drawable getProgressDrawable() {
        return mProgressDrawable;
    }
}

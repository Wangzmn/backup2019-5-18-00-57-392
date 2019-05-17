package neww;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import ex.Length;
import wclass.android.ui.view.base_view.UsefulFrameLayout;
import wclass.android.util.AnimatorUT;
import wclass.android.util.DurationUT;
import wclass.android.util.debug.EventUT;
import wclass.android.util.debug.StringUT;
import wclass.enums.Orien5;
import wclass.ui.event_parser.MultiSingleParser;
import wclass.util.MathUT;

/**
 * @作者 做就行了！
 * @时间 2019-05-13下午 11:53
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */

/**
 * todo
 * 1、怎么确定进度的百分比？
 * 想完睡觉。
 */
public abstract class ProgressBar<B extends View, P extends View, T extends View> extends UsefulFrameLayout {
    private static final boolean GESTURE_DEBUG = false;//手势debug。
    private static final boolean METHOD_ORDER_DEBUG = false;//方法排序debug。
    private static final boolean VG_INFO_DEBUG = false;//view信息debug。
    //////////////////////////////////////////////////
    private final Context context;//上下文。
    protected B bgView;//作为滑道背景的view。
    protected P progressView;//作为进度的view。
    protected T thumbView;//作为滑块的view。
    private int progressValueInParent;//在rootView中的进度值。
    private float mProgress;//进度。取值范围：0至1。
    //--------------------------------------------------
    private boolean init;//标记该类是否初始化。
    private MultiSingleParser parser;//手势处理。
    private Length thumbViewRegion = new Length();//滑块的滑动范围。
    private Rect reuse = new Rect();//复用的rect。
    private ValueAnimator animator;//动画对象。
    //////////////////////////////////////////////////

    /**
     * {@link #ProgressBar(Context, float)}
     */
    public ProgressBar(Context context) {
        this(context, 0);
    }

    /**
     * 构造方法。
     *
     * @param context  上下文。
     * @param progress 进度值。
     */
    public ProgressBar(Context context, float progress) {
        super(context);
        this.context = context;
        setProgress(progress);
        parser = new MultiSingleParser(ViewConfiguration.get(context).
                getScaledTouchSlop());
    }

    /**
     * 获取进度。
     */
    public float getProgress() {
        return mProgress;
    }

    /**
     * 设置进度。
     *
     * @param progress 进度。取值范围：0至1。
     */
    public final void setProgress(float progress) {
        progress = MathUT.limit(progress, 0, 1);
        if (mProgress == progress) {
            return;
        }
        mProgress = progress;
        if (init) {
            setProgressValueInParentInner();
            onProgressChanged(progress, progressValueInParent);
        }
    }

    /**
     * 获取当前进度在父容器中的进度值。
     */
    public int getProgressValueInParent() {
        return progressValueInParent;
    }

    //////////////////////////////////////////////////
    public void animToProgress(int toProgress){
        animToProgress(toProgress, DurationUT.ANIM_DURATION);
    }

    public void animToProgress(int toProgress,long duration){
        float currProgress = getProgress();
        float progressCut = toProgress-currProgress;
        if(animator!=null){
            animator.cancel();
        }
        animator = AnimatorUT.forProgressPercentage(duration,
                new AnimatorUT.Update() {
                    @Override
                    public void onUpdate(float progress) {
                        float realTimeProgress = currProgress + (progressCut * progress);
                        setProgress(realTimeProgress);
                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onEnd() {
                        animator = null;
                    }

                    @Override
                    public void onCancel() {
                        animator = null;
                    }
                });
    }
    //////////////////////////////////////////////////
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (METHOD_ORDER_DEBUG) {
            Log.e("TAG", getClass() + "#onLayout:  ");
        }
        if (!init) {
            init();
        }
        int w = getWidth(), h = getHeight();
        onAdjustBgView(bgView, w, h);
        onAdjustProgressView(progressView, w, h);
        onAdjustThumbView(thumbView, w, h);
        thumbViewRegion.setLength(onGetProgressLengthInParent(w, h));
        limitLength();
        if (VG_INFO_DEBUG) {
            Log.e("TAG", getClass() + "#getMeasuredWidth():  " + getMeasuredWidth());
            Log.e("TAG", getClass() + "#getMeasuredHeight():  " + getMeasuredHeight());
        }
        measure(MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY));
        super.onLayout(changed, left, top, right, bottom);
        setProgressValueInParentInner();
        onProgressChanged(mProgress, progressValueInParent);
    }

    /**
     * 设置进度值。
     * 内部方法。
     */
    private void setProgressValueInParentInner() {
        progressValueInParent = (int) (thumbViewRegion.start + (mProgress * thumbViewRegion.getValue()));
    }

    /**
     * 限制滑块的滑动范围。
     */
    private void limitLength() {
        if (thumbViewRegion.start < 0) {
            thumbViewRegion.start = 0;
        }
        int width = getWidth();
        if (thumbViewRegion.end > width) {
            thumbViewRegion.end = width;
        }
        if (thumbViewRegion.end < thumbViewRegion.start) {
            thumbViewRegion.end = thumbViewRegion.start;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (METHOD_ORDER_DEBUG) {
            Log.e("TAG", getClass() + "#onMeasure:  ");
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (METHOD_ORDER_DEBUG) {
            Log.e("TAG", getClass() + "#onDraw:  ");
        }
        super.onDraw(canvas);
    }
    //--------------------------------------------------

    /**
     * 初始化。
     * 获取、添加view。
     */
    private void init() {
        init = true;
        bgView = onCreateBgView(context);
        progressView = onCreateProgressView(context);
        thumbView = onCreateThumbView(context);
        addView(bgView);
        addView(progressView);
        addView(thumbView);
        onCreateViewsFinish();
    }

    /**
     * 创建作为背景的view。
     */
    protected abstract B onCreateBgView(Context context);

    /**
     * 创建作为进度的view。
     */
    protected abstract P onCreateProgressView(Context context);

    /**
     * 创建作为滑块的view。
     */
    protected abstract T onCreateThumbView(Context context);

    /**
     * 创建所有view全部完成时的回调。
     */
    protected abstract void onCreateViewsFinish();

    //--------------------------------------------------

    /**
     * 调整作为背景的view。
     *
     * @param w 容器宽。
     * @param h 容器高。
     */
    protected abstract void onAdjustBgView(B bgView, int w, int h);

    /**
     * 调整作为进度的view。
     *
     * @param w 容器宽。
     * @param h 容器高。
     */
    protected abstract void onAdjustProgressView(P progressView, int w, int h);

    /**
     * 调整作为滑块的view。
     *
     * @param w 容器宽。
     * @param h 容器高。
     */
    protected abstract void onAdjustThumbView(T thumbView, int w, int h);
    //--------------------------------------------------
    //超出则限制。

    /**
     * 获取滑块的滑动范围，该范围以父容器为参照物。
     *
     * @param w 容器宽。
     * @param h 容器高。
     * @return Length.start和Length.end相对于容器的left。
     */
    protected abstract Length onGetProgressLengthInParent(int w, int h);
    //////////////////////////////////////////////////

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (GESTURE_DEBUG) {
            Log.e("TAG", getClass() + "#onTouchEvent:" +
                    " event = " + EventUT.actionToStr(event));
        }

        //step 如果设置了点击事件，那就由他去吧。
        super.onTouchEvent(event);
        handleEvent(event);
        return true;
    }

    /**
     * 进度值发生改变时的回调。
     *
     * 友情提示：此次改变可以由滑动、用户设置产生。
     * {@link #onTouchEvent(MotionEvent)}
     * {@link #setProgress(float)}。
     *
     * @param progress            进度。
     * @param progressValueInRoot 进度在父容器中的进度值。
     */
    protected void onProgressChanged(float progress, int progressValueInRoot) {

    }

    /**
     * step 确定：
     * 1、300毫秒内才能触发点击。
     * 2、
     */
    private void handleEvent(MotionEvent ev) {
        parser.parse(ev);
        int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (VG_INFO_DEBUG) {
                    Log.e("TAG", getClass() + "#:" +
                            "  " + StringUT.vgInfo(this));
                    Log.e("TAG", getClass() + "#getMeasuredWidth():  " + getMeasuredWidth());
                    Log.e("TAG", getClass() + "#getMeasuredHeight():  " + getMeasuredHeight());
                }
                if (GESTURE_DEBUG) {
                    Log.e("TAG", getClass() + "#handleEvent.ACTION_DOWN:" +
                            "  ");
                }
                getParent().requestDisallowInterceptTouchEvent(true);
                onDown();
                break;
            case MotionEvent.ACTION_MOVE:
                //触发移动了。
                boolean moved = parser.getFirstTouchOrien() != Orien5.SITU;
                if (GESTURE_DEBUG) {
                    Log.e("TAG", getClass() + "#handleEvent.ACTION_MOVE:" +
                            " moved = " + moved + " 。");
                }
                if (moved) {
                    //todo thumb应该移动到此位置。
                    int value = (int) (parser.xMove + 0.5f);
                    progressValueInParent = MathUT.limit(value,
                            thumbViewRegion.start, thumbViewRegion.end);
                    mProgress = MathUT.limit(
                            (float) (progressValueInParent - thumbViewRegion.start)
                                    / thumbViewRegion.getValue()
                            , 0, 1);

                    onProgressChanged(mProgress, progressValueInParent);
                    if (GESTURE_DEBUG) {
                        Log.e("TAG", getClass() + "#handleEvent.ACTION_MOVE:" +
                                " progressValueInParent = " + progressValueInParent +
                                ", mProgress = " + mProgress + " 。");
                    }
                }
                onMove();

                break;
            case MotionEvent.ACTION_UP:
                boolean doClick = parser.getFirstTouchOrien() == Orien5.SITU
                        && parser.getTimeDelta_cutDown() < DurationUT.CLICK_LIMIT_TIME;
                if (GESTURE_DEBUG) {
                    Log.e("TAG", getClass() + "#handleEvent.ACTION_UP:" +
                            " doClick = " + doClick + " 。" + "\n" +
                            "bgView = " + StringUT.toStr(bgView) + "\n" +
                            "progressView = " + StringUT.toStr(progressView) + "\n" +
                            "thumbView = " + StringUT.toStr(thumbView));

                    StringUT.toStr(bgView);
                }
                if (doClick) {
                    onClickProtected();
                }
                onUp(doClick);
                break;
            case MotionEvent.ACTION_CANCEL:
                if (GESTURE_DEBUG) {
                    Log.e("TAG", getClass() + "#handleEvent.ACTION_CANCEL:  ");
                }
                onCancel();
                break;
        }
    }

    /**
     * 触摸按下事件。
     */
    protected void onDown() {
        thumbView.setPressed(true);
    }

    /**
     * 触摸移动事件。
     */
    protected void onMove() {

    }

    /**
     * 触摸抬起时。
     *
     * @param click true：此次抬起触发了点击事件。
     */
    protected void onUp(boolean click) {
        thumbView.setPressed(false);
    }

    /**
     * 触发点击事件。
     */
    protected void onClickProtected() {
    }

    /**
     * 触摸取消事件。
     */
    protected void onCancel() {
        thumbView.setPressed(false);
    }

    /**
     * 判断此次事件是否在滑块的view中。
     *
     * @return true：此次触摸事件在滑块中。
     */
    protected boolean isInThumb(MotionEvent ev) {
        thumbView.getHitRect(reuse);
        return reuse.contains(((int) ev.getX()), ((int) ev.getY()));
    }

    /**
     * 不让子控件处理任何事件。
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (GESTURE_DEBUG) {
            Log.e("TAG", getClass() + "#onInterceptTouchEvent:" +
                    " event = " + EventUT.actionToStr(ev));
        }
        return true;
    }

}

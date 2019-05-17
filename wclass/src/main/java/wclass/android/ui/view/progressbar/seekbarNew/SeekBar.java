package wclass.android.ui.view.progressbar.seekbarNew;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;

import wclass.android.ui.view.progressbar.ProgressBar;
import wclass.android.ui.EventTypeConverter;
import wclass.android.util.SizeUT;
import wclass.android.util.TouchEventUT;
import wclass.enums.EventType;
import wclass.enums.Orien2;

/**
 * @作者 做就行了！
 * @时间 2019-03-23下午 10:33
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 * todo
 * 1、添加功能：移动超过worm才能滑动 ~
 */
public abstract class SeekBar extends ProgressBar {
    private static final boolean TOUCH_DEBUG = false;
    private static final boolean DEBUG = false;
    private Rect usableRect;

    //一些笔记
    {

    /*step 确定*/
        {
            /**
             * step 确定：
             * 1、当滑动thumb时，
             *   单独在一个protected方法中设置thumb的rect。
             */
            /**
             * step 调试：
             * 1、取一些苛刻的数值测试。
             */
        }
    }

    private final Context context;
    private final int maxProgress;
    /**
     * true：滑道被触摸了。
     * false：滑道未被触摸。
     */
    private boolean trackTouched;
//    private int height;
//    private int width;

    public SeekBar(Context context) {
        this(context, 100);
    }

    public SeekBar(Context context, int maxProgress) {
        super(context, maxProgress);
        if (maxProgress < 1) {
            throw new IllegalArgumentException("maxProgress必须大于0。");
        }
        this.context = context;
        this.maxProgress = maxProgress;
        //--------------------------------------------------
//        int worm = ViewConfiguration.get(context).getScaledTouchSlop();
        handleEvent = new HandleEvent();
    }
    //////////////////////////////////////////////////
    /*domain 触摸事件相关。*/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        handleEvent.handleEvent(event);
        super.onTouchEvent(event);
        return true;
    }

    /**
     * 设置进度。
     * 将父类方法添加一个非触摸滑动的回调。
     *
     * @param progress 当前进度。
     */
    @Override
    public void setProgress(int progress) {
        super.setProgress(progress);
        if (cb != null) {
            cb.onSeeking(SeekBar.this, false, getProgress(), -1);
        }
    }

    private void setProgressByTouch(int progress, int progressStart) {
        super.setProgress(progress);
        if (cb != null) {
            cb.onSeeking(SeekBar.this, true, getProgress(), progressStart);
        }
    }

    private Callback cb;

    public void setCallback(Callback cb) {
        this.cb = cb;
    }

    public interface Callback {
        /**
         * SeekBar滑动中。
         *
         * @param seekBar       {@link SeekBar}
         * @param touch         true：该回调来自用户触摸。
         * @param progress      当前进度。
         * @param progressStart 开始时的滑动值。
         *                      警告：只有在touch为true时，
         *                      才能使用该值。
         */
        void onSeeking(SeekBar seekBar, boolean touch, int progress, int progressStart);

        /**
         * SeekBar开始滑动时。
         *
         * @param seekBar  {@link SeekBar}
         * @param progress 当前进度。
         */
        void onStartSeek(SeekBar seekBar, int progress);

        /**
         * SeekBar结束滑动时。
         *
         * @param seekBar       {@link SeekBar}
         * @param progress      当前进度。
         * @param progressStart 开始滑动时的滑动值。
         */
        void onEndSeek(SeekBar seekBar, int progress, int progressStart);

        /**
         * SeekBar取消滑动时。
         *
         * @param seekBar       {@link SeekBar}
         * @param progress      当前进度。
         * @param progressStart 开始时的滑动值。
         */
        void onCancelSeek(SeekBar seekBar, int progress, int progressStart);
    }

    boolean multiTouchCancelSeek;
    HandleEvent handleEvent;
/**
 * todo
 * 1、按下指定区域才可以滑动。
 * 3、再整理一下。
 */
    /**
     * todo 2019年3月26日23:29:19
     * todo 明天测试 ！！！
     * <p>
     * cancel时传入按下时的进度。
     */
    class HandleEvent {

        boolean canSeek;
        float[] perXY = new float[2];
        int progressStart;

        void handleEvent(MotionEvent ev) {
            int actionMasked = ev.getActionMasked();
            EventType type = EventTypeConverter.convert(actionMasked);
            if (type == EventType.DOWN) {
                /**
                 * 警告：移动view时，不能用getXY！！！
                 * 这里不移动view，可以用。
                 */
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                canSeek = thumbRect.contains(x, y) || trackRegionRect.contains(x, y);
            }
            if (!canSeek) {
                return;
            }
            TouchEventUT.getPerInBounds(ev, getTrackProgressMaxRect(), perXY);
            if (TOUCH_DEBUG) {
                Log.e("TAG", " perX = " + perXY[0]);
                Log.e("TAG", " perY = " + perXY[1]);
            }
            switch (type) {
                case DOWN:
                    setProgress();
                    progressStart = getProgress();
                    notifyOnStartSeek();
                    break;
                case MOVE:
                    setProgress();
                    break;
                case UP:
                    notifyOnEndSeek();
                    break;
                case POINTER_DOWN:
                    //取消滑动。
                    if (multiTouchCancelSeek) {
                        canSeek = false;
                        notifyOnCancelSeek();
                    }
                    //当成move处理。
                    else {
                        setProgress();
                    }
                    break;
                case POINTER_UP:
                    break;
                case NO_POINTER:
                    break;
                case EXIT_WITH_NO_POINTER:
                    notifyOnCancelSeek();
                    break;
            }
        }

        private void notifyOnCancelSeek() {
            if (cb != null) {
                cb.onCancelSeek(SeekBar.this, getProgress(), progressStart);
            }
        }

        private void notifyOnEndSeek() {
            if (cb != null) {
                cb.onEndSeek(SeekBar.this, getProgress(), progressStart);
            }
        }

        private void notifyOnStartSeek() {
            if (cb != null) {
                cb.onStartSeek(SeekBar.this, progressStart);
            }
        }

        private void setProgress() {
            switch (getOrien()) {
                case HORIZONTAL:
                    int progressX = (int) (getMaxProgress() * perXY[0] + 0.5f);
                    if (isReverseProgress()) {
                        progressX = getMaxProgress() - progressX;
                    }
                    setProgressByTouch(progressX, progressStart);
                    break;
                case VERTICAL:
                    int progressY = (int) (getMaxProgress() * perXY[1] + 0.5f);
                    if (isReverseProgress()) {
                        progressY = getMaxProgress() - progressY;
                    }
                    setProgressByTouch(progressY, progressStart);
                    break;
                default:
                    throw new IllegalStateException();
            }
        }
    }
    //////////////////////////////////////////////////

    /**
     * 滑道被触摸时。
     * <p>
     * 友情提示：子类可以在该方法中设置track图片的状态。
     *
     * @param touch true：被触摸了。
     *              false：未被触摸
     */
    protected void onTrackTouchStateChange(boolean touch) {

    }
    //////////////////////////////////////////////////

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (trackTouched) {
            trackTouched = false;
            onTrackTouchStateChange(false);
        }
    }

    private boolean init;
    //////////////////////////////////////////////////
    /*domain 绘制相关。*/
    private Drawable thumbDrawable;
    private Rect thumbRect = new Rect();

    public void setDrawableThumb(Drawable thumbDrawable) {
        this.thumbDrawable = thumbDrawable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //父类先画滑道背景、进度。
        super.onDraw(canvas);
        //todo 这里可以优化
        if (thumbDrawable != null) {
            onRerectThumb(thumbRect, thumbInfo, getProgressPer(), getOrien(), isReverseProgress());
            thumbDrawable.setBounds(thumbRect);
            thumbDrawable.draw(canvas);
        }
    }
    //////////////////////////////////////////////////
    /*layout*/

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        //--------------------------------------------------
    }
    //////////////////////////////////////////////////
    /*domain 调整大小相关。*/

    @Override
    protected void preLayout(Rect usableRect) {
        super.preLayout(usableRect);
//        if(DEBUG){
//            Log.e("TAG"," usableRect = "+usableRect);
//        }
        if (!init) {
            init = true;
            init();
        }
        /**
         * 先更新thumb滑块的信息类，再调用父类的onSize。
         * 因为父类会在onSizeChanged中调整滑道背景和滑道进度的属性。
         */
        thumbInfo = thumbInfoMaker.make(this, usableRect);
        trackRegionFrameMaker.make(trackRegionRect, thumbInfo, this, usableRect);
    }

    /**
     * 重新调整滑块的rect。
     *
     * @param thumbRect      待调整的滑块的rect。
     * @param thumbInfo      滑块信息类。
     * @param progressPer    进度百分比。
     * @param orien          SeekBar的方向。
     * @param isReverseProgress 进度是否是反向绘制。
     */
    protected void onRerectThumb(Rect thumbRect, ThumbInfo thumbInfo, float progressPer,
                                 Orien2 orien, boolean isReverseProgress) {

        int left, right, top, bottom;
        switch (orien) {
            case HORIZONTAL:
                int dist = (int) (progressPer * thumbInfo.maxDragDist + 0.5f);
                //横向时，top、bottom直接确定。
                top = thumbInfo.minY;
                bottom = top + thumbInfo.height;
                //反向绘制。
                if (isReverseProgress) {
                    dist = thumbInfo.maxDragDist - dist;
                }
                left = thumbInfo.minX + dist;
                right = left + thumbInfo.width;
                break;
            case VERTICAL:
                int dist2 = (int) (progressPer * thumbInfo.maxDragDist + 0.5f);
                //竖向时：left、right直接确定。
                left = thumbInfo.minX;
                right = left + thumbInfo.width;
                //反向绘制。
                if (isReverseProgress) {
                    dist2 = thumbInfo.maxDragDist - dist2;
                }
                top = thumbInfo.minY + dist2;
                bottom = top + thumbInfo.height;
                break;
            default:
                throw new IllegalStateException();
        }
        thumbRect.left = left;
        thumbRect.right = right;
        thumbRect.top = top;
        thumbRect.bottom = bottom;
        if(DEBUG){
//            Log.e("TAG"," progressPer = "+progressPer);
            Rect rect = new Rect();
            getDrawingRect(rect);
            Log.e("TAG"," drawingRect = "+rect);
            Log.e("TAG"," thumbRect = "+thumbRect);
//            Log.e("TAG",thumbInfo.toString());
        }
    }


    /**
     * 调整滑道背景的rect。
     *
     * @param trackBg    需要调整的滑道背景的rect。
     * @param usableRect
     */
    @Override
    protected void onResizeTrackBg(Rect trackBg, Rect usableRect) {
        trackBgFrameMaker.make(trackBg, thumbInfo,
                this, usableRect);
    }

    /**
     * 调整滑道背景最大进度的rect。
     *
     * @param trackProgress 滑道最大进度的rect。
     * @param usableRect
     */
    @Override
    protected void onResizeTrackProgressMaxRect(Rect trackProgress, Rect usableRect) {
        trackMaxProgressFrameMaker.make(trackProgress, thumbInfo,
                this, usableRect);
    }

    /**
     * 调整当前进度的rect。
     * 友情提示：只要是平滑的进度，该方法不用重写！
     *
     * @param trackProgressCurrRect 需要调整的当前进度的rect。
     * @param trackProgressMaxRect  最大进度的rect。
     * @param progress              当前进度值。
     * @param maxProgress           最大进度值。
     */
    @Override
    protected void onResizeTrackProgressCurrRect(Rect trackProgressCurrRect,
                                                 Rect trackProgressMaxRect,
                                                 int progress, int maxProgress) {
        super.onResizeTrackProgressCurrRect(trackProgressCurrRect,
                trackProgressMaxRect,
                progress, maxProgress);
    }

    //////////////////////////////////////////////////

    /**
     * 初始化主要成员！！！
     */
    private void init() {
        if (thumbInfoMaker == null) {
            thumbInfoMaker = new ThumbInfoMakerImpl();
        }
        if (trackRegionFrameMaker == null) {
            trackRegionFrameMaker = new FrameMakerImpl();
        }
        if (trackBgFrameMaker == null) {
            //将滑道背景设置为2像素。
            trackBgFrameMaker = new FrameMakerImpl(2);
        }
        if (trackMaxProgressFrameMaker == null) {
            int mm = SizeUT.getMMpixel(context);
            //将滑道进度的宽度设置为一毫米。
            trackMaxProgressFrameMaker = new FrameMakerImpl(mm);
        }

    }

    //////////////////////////////////////////////////
    /*domain 主要组件的属性*/
    private ThumbInfoMaker thumbInfoMaker;
    /**
     * 生成滑道的触摸区域。
     * 友情提示：手指触摸该区域，或者触摸thumb，才可移动thumb。
     */
    private FrameMaker trackRegionFrameMaker;
    /**
     * 滑道上的背景图片区域。
     */
    private FrameMaker trackBgFrameMaker;
    /**
     * 滑道上的进度图片区域。
     */
    private FrameMaker trackMaxProgressFrameMaker;

    /**
     * thumb信息类。
     * 该变量赋值时机：{@link #onSizeChanged(int, int, int, int)}。
     */
    private ThumbInfo thumbInfo;
    /**
     * 滑道触摸区域。
     * {@link #trackRegionFrameMaker}。
     */
    private Rect trackRegionRect = new Rect();
    //--------------------------------------------------

    /**
     * 设置滑块信息的生成器。
     *
     * @param thumbInfoMaker {@link ThumbInfoMaker}
     */
    public void setThumbInfoMaker(ThumbInfoMaker thumbInfoMaker) {
        this.thumbInfoMaker = thumbInfoMaker;
    }

    /**
     * 设置触摸区域的rect的生成器。
     *
     * @param trackRegionFrameMaker {@link FrameMaker}
     */
    public void setTrackRegionFrameMaker(FrameMaker trackRegionFrameMaker) {
        this.trackRegionFrameMaker = trackRegionFrameMaker;
    }

    /**
     * 设置滑道背景的rect的生成器。
     *
     * @param trackBgFrameMaker {@link FrameMaker}
     */
    public void setTrackBgFrameMaker(FrameMaker trackBgFrameMaker) {
        this.trackBgFrameMaker = trackBgFrameMaker;
    }

    /**
     * 设置最大进度的rect的生成器。
     *
     * @param trackProgressFrameMaker {@link FrameMaker}
     */
    public void setTrackMaxProgressFrameMaker(FrameMaker trackProgressFrameMaker) {
        this.trackMaxProgressFrameMaker = trackProgressFrameMaker;
    }

    //////////////////////////////////////////////////
    /*引导类型的类*/

    /**
     * 轮廓区域生成器。
     */
    public interface FrameMaker {
        /**
         * 根据参数信息生成rect。
         *
         * @param pending    调整这个rect。
         * @param info       {@link ThumbInfo}
         * @param seekBar    seekBar。
         * @param usableRect 可用的区域。
         */
        void make(Rect pending, ThumbInfo info, SeekBar seekBar, Rect usableRect);
    }

    /**
     * 滑块信息生成器。
     * {@link ThumbInfo}。
     */
    public interface ThumbInfoMaker {
        /**
         * 生成{@link ThumbInfo}。
         *
         * @param seekBar    seekBar。
         * @param usableRect 可用的区域。
         * @return {@link ThumbInfo}
         */
        ThumbInfo make(SeekBar seekBar, Rect usableRect);
    }

}

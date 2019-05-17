package wclass.android.ui.view.base_view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import wclass.android.util.SizeUT;
import wclass.enums.Orien2;
import wclass.enums.Result;
import wclass.ui.event_parser.MultiSingleParser;
import wclass.util.MathUT;

import static wclass.android.ui.view.base_view.UsefulScrollViewGroup.TouchScrollStrategy.CANT_TOUCH_SCROLL;
import static wclass.android.ui.view.base_view.UsefulScrollViewGroup.TouchScrollStrategy.CAN_TOUCH_SCROLL;
import static wclass.android.ui.view.base_view.UsefulScrollViewGroup.TouchScrollStrategy.PENDING;

/**
 * @作者 做就行了！
 * @时间 2019-04-09下午 9:18
 * @该类描述： -
 * 1、该类提供一些方法，辅助测量子view、获取子view参数。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 * //todo bug
 * 1、模拟器中滑动卡在一个位置，之后就不滑动了，而且不走onNoTouchScroll。
 */
@SuppressWarnings("DanglingJavadoc")
public abstract class UsefulScrollViewGroup extends UsefulViewGroup {
    private static final boolean DEBUG = true;
    //////////////////////////////////////////////////
    /*滑动处理。2019年4月29日18:00:40*/
    private VelocityTracker vt;
    private MultiSingleParser parser;
    private Scroller scroller;
    //--------------------------------------------------
    /**
     * 标记：是否为非触摸滑动。
     */
    private boolean isNoTouchScroll;
    private int screenWidth;

    //--------------------------------------------------
    /*方便子类，为子类提供的几个滑动方法。*/

    /**
     * {@link #smoothScrollTo(int, int)}
     */
    protected void smoothScrollTo(int destination) {
        smoothScrollTo(destination, 200);
    }

    /**
     * 平滑的滑动至目标滑动值。
     *
     * @param destination 目标滑动值。
     *                    友情提示：
     *                    如果是横向滑动，那么该值为目标scrollX。
     *                    如果是纵向滑动，那么该值为目标scrollY。
     * @param duration    滑动的持续时间。
     */
    protected void smoothScrollTo(int destination, int duration) {
        switch (getScrollOrien()) {
            case HORIZONTAL:
                smoothScrollBy(destination - getScrollX(), duration);
                break;
            case VERTICAL:
                smoothScrollBy(destination - getScrollY(), duration);
                break;
            default:
                throw new IllegalStateException();
        }

    }

    /**
     * {@link #smoothScrollBy(int, int)}
     */
    protected void smoothScrollBy(int delta) {
        smoothScrollBy(delta, 200);
    }

    /**
     * 平滑的滑动指定的距离。
     *
     * @param delta    滑动的距离。
     *                 友情提示：
     *                 如果是横向滑动，那么该值为X方向滑动的距离。
     *                 如果是纵向滑动，那么该值为Y方向滑动的距离。
     * @param duration 滑动的持续时间。
     */
    protected void smoothScrollBy(int delta, int duration) {
        switch (getScrollOrien()) {
            case HORIZONTAL:
                scroller.startScroll(getScrollX(), getScrollY(), delta, 0, duration);
                break;
            case VERTICAL:
                scroller.startScroll(getScrollX(), getScrollY(), 0, delta, duration);
                break;
            default:
                throw new IllegalStateException();
        }
        onNoTouchScroll_start();

        isNoTouchScroll = true;
        invalidate();
    }

    //////////////////////////////////////////////////
    public boolean isNoTouchScrolling() {
        return isNoTouchScroll;
    }

    public boolean isTouchScrolling() {
        return isInTouchMode() && touchScrollStrategy == CAN_TOUCH_SCROLL;
    }

    public boolean isScrolling() {
        return isTouchScrolling() || isNoTouchScrolling();
    }
    //////////////////////////////////////////////////
    /*触摸滑动。*/

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
//        if(DEBUG){
//            Log.e("TAG",getClass()+"#onTouchEvent:  ");
//        }
        int actionMasked = ev.getActionMasked();
        handleEventForScroll(ev, actionMasked);
        return true;
    }

    /**
     * fix 返回true时，就到自己的touchEvent中了。
     *
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onInterceptTouchEvent:  ");
        }
        boolean b = super.onInterceptTouchEvent(ev);
        int actionMasked = ev.getActionMasked();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                if (isNoTouchScroll) {
                    touchScrollStrategy = CAN_TOUCH_SCROLL;
                } else {

                    touchScrollStrategy = PENDING;
                }
                vt.clear();
                //标记为：不是非触摸滑动。
                isNoTouchScroll = false;
                //isNoTouchScroll拦截了，不需要下方代码。
//                scroller.forceFinished(true);
                break;
        }
        if (needScroll()) {
            b |= handleEventForScroll(ev, actionMasked);
        }
        return b;
    }

    protected boolean needScroll() {
        return true;
    }

    private boolean handleEventForScroll(MotionEvent ev, int actionMasked) {
        switch (touchScrollStrategy) {
            case PENDING:
                if (DEBUG) {
                    Log.e("TAG", getClass() + "#:PENDING  ");
                }
                //只要子view请求一次，就都走这里了。
                if (isChildRequestEvent()) {
                    touchScrollStrategy = CANT_TOUCH_SCROLL;
                    break;
                }
                parser.parse(ev);
                vt.addMovement(ev);
                //横向大幅移动，竖向小幅移动，此时触发滑动。
                Result result = verifyCanScroll(parser);
                switch (result) {
                    case TRUE:
                        if (lastAskChildNeedEventBeforeTouchScroll(ev)) {
                            if (DEBUG) {
                                Log.e("TAG", getClass() + "#:CANT_TOUCH_SCROLL  ");
                            }
                            touchScrollStrategy = CANT_TOUCH_SCROLL;
                        }
                        //item不需要事件，由自己处理滑动操作。
                        else {
                            if (DEBUG) {
                                Log.e("TAG", getClass() + "#:CAN_TOUCH_SCROLL  ");
                            }
                            touchScrollStrategy = CAN_TOUCH_SCROLL;
                            onTouchScroll_start(parser, ev);
                            //step 触发滑动，返回true，由自己处理事件。
                            return true;
                        }
                        break;
                    case FALSE:
                        if (DEBUG) {
                            Log.e("TAG", getClass() + "#:CANT_TOUCH_SCROLL  ");
                        }
                        touchScrollStrategy = CANT_TOUCH_SCROLL;
                        break;
                    case PENDING:
                        if (DEBUG) {
                            Log.e("TAG", getClass() + "#:PENDING  ");
                        }
                        break;
                }
                break;

            case CAN_TOUCH_SCROLL:
                parser.parse(ev);
                vt.addMovement(ev);
                onSetTouchScrollValue(parser);
                onTouchScroll_ing(parser, ev);

                if (actionMasked == MotionEvent.ACTION_UP) {
                    onTouchScroll_finish(parser, ev);
                    onTouchScroll_finishAndDoFling(parser, vt, ev);
                }

                //step 滑动中，返回true，由自己处理事件。
                return true;

            case CANT_TOUCH_SCROLL:
                if (DEBUG) {
                    Log.e("TAG", getClass() + "#:CANT_TOUCH_SCROLL  ");
                }
                break;

            default:
                throw new IllegalStateException();
        }
        return false;
    }

    //--------------------------------------------------
    /*非触摸滑动。*/
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (isNoTouchScroll) {
            if (scroller.computeScrollOffset()) {
                invalidate();
                onSetNoTouchScrollValue(scroller);
                onNoTouchScroll_ing();
            } else {
                isNoTouchScroll = false;
                onNoTouchScroll_finish();
            }
        }
    }

    protected void onSetNoTouchScrollValue(Scroller scroller) {
        switch (getScrollOrien()) {
            case HORIZONTAL:
                scrollTo(scroller.getCurrX(), getScrollY());
                break;
            case VERTICAL:
                scrollTo(getScrollX(), scroller.getCurrY());
                break;
            default:
                throw new IllegalStateException();
        }

    }
    //--------------------------------------------------

    /**
     * 实时滑动。
     */
    protected void onSetTouchScrollValue(MultiSingleParser parser) {
        switch (getScrollOrien()) {
            case HORIZONTAL:
                int delta = (int) (parser.getScrollDeltaX_cutMove() + 0.5f);
                int scrollX = getScrollX() + delta;
                scrollX = onLimitScrollValue(scrollX);
                //横向滑动时，设置X值。
                scrollTo(scrollX, getScrollY());
                break;
            case VERTICAL:
                int delta2 = (int) (parser.getScrollDeltaY_cutMove() + 0.5f);
                int scrollY = delta2 + getScrollY();
                scrollY = onLimitScrollValue(scrollY);
                //纵向滑动时，设置Y值
                scrollTo(getScrollX(), scrollY);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    protected int onLimitScrollValue(int scrollValue) {
        return MathUT.limitMin(scrollValue, 0);
    }

    //--------------------------------------------------
    /*滑动时的回调。*/
    protected void onNoTouchScroll_start() {
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onNoTouchScroll_start:  ");
        }
    }

    protected void onNoTouchScroll_ing() {
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onNoTouchScroll_ing:  ");
        }
    }

    protected void onNoTouchScroll_finish() {
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onNoTouchScroll_finish:  ");
        }
    }

    protected void onTouchScroll_start(MultiSingleParser parser, MotionEvent ev) {

        if (DEBUG) {
            Log.e("TAG", getClass() + "#onTouchScroll_start:  ");
        }
    }

    protected void onTouchScroll_ing(MultiSingleParser parser, MotionEvent ev) {
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onTouchScroll_ing:  ");
        }
    }

    protected void onTouchScroll_finish(MultiSingleParser parser, MotionEvent ev) {

        if (DEBUG) {
            Log.e("TAG", getClass() + "#onTouchScroll_finish:  ");
        }
    }

    protected void onTouchScroll_finishAndDoFling(MultiSingleParser parser, VelocityTracker vt, MotionEvent ev) {
        if (DEBUG) {
            vt.computeCurrentVelocity(1000);
            Log.e("TAG", getClass() + "#onTouchScroll_finishAndDoFling:" +
                    " 速率 = " + vt.getXVelocity() + " 。" +
                    " screenWidth = " + screenWidth + " 。");
        }
    }

    /**
     * 验证是否可以滑动。
     * <p>
     * 友情提示：子类可以重写触发条件。
     * 不过，好像没必要重写了。
     *
     * @param parser 触摸事件记录点信息的类。
     * @return true：可以进入滑动。
     */
    protected Result verifyCanScroll(MultiSingleParser parser) {
        switch (getScrollOrien()) {
            case HORIZONTAL:
                //纵向大幅移动，不能横向滑动。
                if (!parser.isWormY()) {

                    return Result.FALSE;
                }
                //横向大幅移动，触发横向滑动。
                if (!parser.isWormX()) {
                    return Result.TRUE;
                }
                //待验证。
                else {
                    return Result.PENDING;
                }

            case VERTICAL:
                //横向大幅移动，不能纵向滑动。
                if (!parser.isWormX()) {
                    return Result.FALSE;
                }
                //纵向大幅移动，触发纵向滑动。
                if (!parser.isWormY()) {
                    return Result.TRUE;
                }
                return Result.PENDING;
            default:
                throw new IllegalStateException();
        }
    }

    /**
     * 触发触摸滑动之前，最后一次询问子view，是否需要自己处理事件。
     * 如果返回false，此次触摸事件子view将不再收到触摸事件。
     *
     * @return true：子view需要自己处理事件。
     */
    protected boolean lastAskChildNeedEventBeforeTouchScroll(MotionEvent ev) {
        return false;
    }

    /**
     * 获取ViewGroup的滑动方向。
     */
    protected abstract Orien2 getScrollOrien();
    //--------------------------------------------------
    /**
     * 触摸事件的策略标记。
     */
    private TouchScrollStrategy touchScrollStrategy = PENDING;

    /**
     * 触摸滑动策略。
     */
    enum TouchScrollStrategy {
        PENDING,//待验证。
        CAN_TOUCH_SCROLL,//可以滑动。
        CANT_TOUCH_SCROLL;//不可以滑动。
    }

    //--------------------------------------------------
    /*构造方法中初始化的滑动组件。*/

    protected Scroller onCreateScroller() {
        return new Scroller(getContext());
    }

    protected VelocityTracker onCreateVT() {
        return VelocityTracker.obtain();
    }

    protected MultiSingleParser onCreateParser() {
        switch (getScrollOrien()) {
            case HORIZONTAL:
                //横向滑动时，纵向条件放宽一点。
                return new MultiSingleParser(
                        ViewConfiguration.get(getContext()).getScaledTouchSlop()
                        , 1, 3);

            case VERTICAL:
                //纵向滑动时，横向条件放宽一点。
                return new MultiSingleParser(
                        ViewConfiguration.get(getContext()).getScaledTouchSlop()
                        , 3, 1);
            default:
                throw new IllegalStateException();
        }
    }

    //////////////////////////////////////////////////
    public UsefulScrollViewGroup(Context context) {
        this(context, null);
    }

    public UsefulScrollViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UsefulScrollViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initScrollComponent();
        screenWidth = SizeUT.getScreenWidth(context);
    }

    private void initScrollComponent() {
        scroller = onCreateScroller();
        vt = onCreateVT();
        parser = onCreateParser();
    }
}

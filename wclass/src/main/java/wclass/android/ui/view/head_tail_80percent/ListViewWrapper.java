package wclass.android.ui.view.head_tail_80percent;

import android.content.Context;
import android.util.Log;
import android.view.View;

import wclass.android.util.SizeUT;
import wclass.android.z_pending_class.HandlerClassUT;
import wclass.ui.event_parser.MultiSingleParser;
import wclass.util.MathUT;

/**
 * @作者 做就行了！
 * @时间 2019-04-15下午 2:40
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class ListViewWrapper extends BaseListViewWrapper {
    private static final boolean DEBUG = true;
    //////////////////////////////////////////////////
    /**
     * 该控件就是被包装的控件，给他加头部的刷新控件。
     */
    private View listView;
    /**
     * 作为滑动阻力。
     */
    private float friction = 0.4f;

    /**
     * 头部滑动时，溢出的总距离。
     * 该值为正数。
     */
    int mHeadSumOverflowAbsDelta;

    public ListViewWrapper(Context context) {
        super(context);
    }

    //--------------------------------------------------

    public void setListView(View listView) {
        this.listView = listView;
    }

    public View getListView() {
        return listView;
    }
    //////////////////////////////////////////////////

    /**
     * 头部滑动中，手指抬起时的回调。
     * <p>
     * 用户可以根据sumOverflowAbsDelta的值判断，：
     * 1、改变刷新控件动画。
     * 2、是否需要执行刷新操作。
     * <p>
     * 友情提示：子类可以重写该方法。
     *
     * @param sumOverflowAbsDelta 头部滑动时，溢出的总距离。
     */
    protected void onHeadThumbUp(int sumOverflowAbsDelta) {
        if (DEBUG) {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    scrollTo00();
                    invalidate();
                }
            };
            HandlerClassUT.removeAll();
            HandlerClassUT.postDelay_forTest(run);
        }
    }
    //--------------------------------------------------

    /**
     * 头部滑动时，实时通知溢出的总距离。
     * <p>
     * 刷新模式两种类型：
     * 1、root控件往下滑动。
     * 2、只是刷新控件往下滑动。
     * <p>
     * 该方法中可以自定义这两种类型。
     * <p>
     * 友情提示：子类可以重写该方法。
     *
     * @param sumOverflowAbsDelta 头部滑动时，溢出的总距离。
     */
    protected void onHeadingOverflow(int sumOverflowAbsDelta) {
        scrollTo(getScrollX(), -sumOverflowAbsDelta);
    }

    /**
     * 头部滑动中，限制溢出的总距离。
     * <p>
     * 返回值可以取决于头部刷新控件的高度。
     *
     * @param sumOverflowAbsDelta 头部滑动时，溢出的总距离。
     * @return 头部滑动中，限制溢出的总距离。
     */
    protected int limitHeadGoingDelta(int sumOverflowAbsDelta) {
        if (DEBUG) {

            int cm = SizeUT.getCMpixel(getContext());
            if (sumOverflowAbsDelta > cm * 2) {
                sumOverflowAbsDelta = cm * 2;
            }
        }
        //这里没做限制，直接返回。
        return sumOverflowAbsDelta;
    }
    //////////////////////////////////////////////////
    @Override
    protected State getState() {
        if (listView == null) {
            return State.NORMAL;
        }
        //--------------------------------------------------
        /*新写法：*/
        //不可以往之前滑动。此时已经滑动至head了。
        if (!listView.canScrollVertically(-1)) {
            return State.HEAD_GOING;
        }
        //不可以往之后滑动。此时已经滑动至tail了。
        else if (!listView.canScrollVertically(1)) {
            return State.TAIL_GOING;
        }
        return State.NORMAL;

        //--------------------------------------------------
        /*旧写法：*/
//
//        ScrollState scrollState = ScrollState.fromRecyclerView(rv.getScrollState());
//        switch (scrollState) {
//            case IDLE:
//                break;
//
//            case TOUCH_SCROLL:
//            case CODE_SCROLL:
//                //不可以往之前滑动。此时已经滑动至head了。
//                if (!rv.canScrollVertically(-1)) {
//                    return State.HEAD_GOING;
//                }
//                //不可以往之后滑动。此时已经滑动至tail了。
//                else if (!rv.canScrollVertically(1)) {
//                    return State.TAIL_GOING;
//                }
//                break;
//
//            default:
//                throw new IllegalStateException();
//        }
//        return State.NORMAL;
    }

    @Override
    protected void onHeadBegin() {
        mHeadSumOverflowAbsDelta = 0;
    }

    @Override
    protected void onHeadGoing(float absDelta) {
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onHeadGoing:" +
                    "  ");
        }
        int delta = toFrictionDelta(absDelta);
        mHeadSumOverflowAbsDelta += delta;
        mHeadSumOverflowAbsDelta = limitHeadGoingDelta(mHeadSumOverflowAbsDelta);
        onHeadingOverflow(mHeadSumOverflowAbsDelta);
    }

    @Override
    protected void onHeadBacking(float absDelta) {
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onHeadBacking");
        }

        if (mHeadSumOverflowAbsDelta == 0) {
            return;
        }

        mHeadSumOverflowAbsDelta -= absDelta;
        mHeadSumOverflowAbsDelta = MathUT.limitMin(mHeadSumOverflowAbsDelta, 0);
        scrollTo(getScrollX(), -mHeadSumOverflowAbsDelta);
    }

    @Override
    protected void onHeadUp() {
        onHeadThumbUp(mHeadSumOverflowAbsDelta);
    }
    //////////////////////////////////////////////////

    protected float getScrollDelta(MultiSingleParser multiSingleParser) {
        return multiSingleParser.getScrollDeltaY_cutMove();
    }

    /**
     * 将实时的溢出的距离转为伴有阻力因素的距离。
     *
     * @param overflowAbsDelta 头部滑动时，实时的溢出的距离。
     * @return 伴有阻力因素的距离。
     */
    protected int toFrictionDelta(float overflowAbsDelta) {
        return (int) (overflowAbsDelta * friction + 0.5f);
    }
}

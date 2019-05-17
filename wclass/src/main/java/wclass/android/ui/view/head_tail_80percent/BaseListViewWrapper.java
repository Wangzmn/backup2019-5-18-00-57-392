package wclass.android.ui.view.head_tail_80percent;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Scroller;

import wclass.android.ui.EventTypeConverter;
import wclass.android.util.debug.EventUT;
import wclass.enums.EventType;
import wclass.ui.event_parser.MultiSingleParser;

/**
 * @作者 做就行了！
 * @时间 2019-04-14下午 2:37
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
 * 1、可以考虑继承FrameLayout。
 * 2、列表容器放在FrameLayout最下层。
 */
public abstract class BaseListViewWrapper extends FrameLayout {

    private static final boolean DEBUG = true;
    //////////////////////////////////////////////////

    /**
     * 标记滑动时处于的状态。
     */
    private State state = State.NORMAL;

    /**
     * 标记是否刚刚执行了拦截。
     */
    private boolean justIntercept = false;

    Scroller scroller;
    MultiSingleParser multiSingleParser;

    //////////////////////////////////////////////////
    public BaseListViewWrapper(Context context) {
        super(context);
        multiSingleParser = new MultiSingleParser(ViewConfiguration.get(
                context).getScaledTouchSlop());
        scroller = new Scroller(context);
    }
    //////////////////////////////////////////////////

    protected void scrollTo00() {
        scrollTo00(200);
    }

    protected void scrollTo00(int duration) {
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        scroller.startScroll(scrollX, scrollY, -scrollX, -scrollY, duration);
    }
    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            invalidate();
        }
    }
    //////////////////////////////////////////////////
    /**
     * 获取子view的滑动时状态，判断用户是否滑动至头尾。
     * {@link State}。
     */
    protected abstract State getState();

    /**
     * 获取实时的滑动距离。
     *
     * @param multiSingleParser 通过该对象获取滑动数值。
     * @return 获取滑动的距离。
     */
    protected abstract float getScrollDelta(MultiSingleParser multiSingleParser);

    /**
     * 滑动的状态分析。
     */
    public enum State {
        HEAD_GOING,//在头部滑动中。
        TAIL_GOING,//在尾部滑动中。
        NORMAL;//正常滑动中。
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (state) {
            case HEAD_GOING:
                handleHeadGoing(event);
                break;
            case TAIL_GOING:
                handleTailGoing(event);
                break;
            case NORMAL:
                return false;
        }
        return true;
    }

    /**
     * 处理在尾部的滑动。
     */
    private void handleTailGoing(MotionEvent event) {
        pre(event);
        EventType type = EventUT.convert(event);
        multiSingleParser.parse(event);
        switch (type) {
            case MOVE:
                float delta = getScrollDelta(multiSingleParser);
                //往之前滑动。
                if (delta < 0) {
                    onTailBacking(-delta);
                }
                //往之后滑动。
                else if (delta > 0) {
                    onTailGoing(delta);
                }
                //没有滑动。（原因：此次事件由其他触摸点触发。）
                else {

                }
                break;
            case UP:
                onTailUp();
                break;
        }
    }

    /**
     * 处理在头部的滑动。
     */
    private void handleHeadGoing(MotionEvent event) {
        pre(event);
        EventType type = EventUT.convert(event);
        multiSingleParser.parse(event);
        switch (type) {
            case MOVE:
                float delta = getScrollDelta(multiSingleParser);
                //往之前滑动。
                if (delta < 0) {
                    onHeadGoing(-delta);
                }
                //往之后滑动。
                else if (delta > 0) {
                    onHeadBacking(delta);
                }
                //没有滑动。（原因：此次事件由其他触摸点触发。）
                else {

                }
                break;
            case UP:
                onHeadUp();
                break;
        }
    }
    //////////////////////////////////////////////////

    /**
     * 首次拦截事件时，将该事件定义为DOWN。
     */
    private void pre(MotionEvent event) {
        if (justIntercept) {
            justIntercept = false;
            //隐患：假的DOWN事件，用户不能从event中获取点坐标。
            event.setAction(MotionEvent.ACTION_DOWN);
        }
    }

    /**
     * 在该方法中做拦截判断。
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean b = super.onInterceptTouchEvent(ev);
        int actionMasked = ev.getActionMasked();
        EventType type = EventTypeConverter.convert(actionMasked);
        switch (type) {
            case DOWN:
                justIntercept = false;
                state = State.NORMAL;
                break;
            case MOVE:
                state = getState();
                switch (state) {
                    case HEAD_GOING:

                        if (DEBUG) {
                            Log.e("TAG", getClass() + "#onInterceptTouchEvent:" +
                                    " HEAD_GOING ");
                        }
                        b = true;
                        justIntercept = true;
                        onHeadBegin();
                        break;
                    case TAIL_GOING:

                        if (DEBUG) {
                            Log.e("TAG", getClass() + "#onInterceptTouchEvent:" +
                                    " TAIL_GOING ");
                        }
                        justIntercept = true;
                        b = true;
                        onTailStart();
                        break;
                    case NORMAL:
                        break;
                    default:
                        throw new IllegalStateException();
                }

                break;
        }
        return b;
    }
    //////////////////////////////////////////////////

    /**
     * 在头部滑动中 开始。
     */
    protected void onHeadBegin() {

    }

    /**
     * 在头部继续滑动中。
     *
     * @param absDelta 滑动距离。
     */
    protected void onHeadGoing(float absDelta) {

    }

    /**
     * 在头部往回滑动中。
     *
     * @param absDelta 滑动距离。
     */
    protected void onHeadBacking(float absDelta) {

    }

    /**
     * 在头部滑动中抬起。
     */
    protected void onHeadUp() {

    }

    /**
     * 在尾部滑动中 开始。
     */
    protected void onTailStart() {

    }

    /**
     * 在尾部继续滑动中。
     *
     * @param absDelta 滑动距离。
     */
    protected void onTailGoing(float absDelta) {

    }

    /**
     * 在尾部往回滑动中。
     *
     * @param absDelta 滑动距离。
     */
    protected void onTailBacking(float absDelta) {

    }

    /**
     * 在尾部滑动中抬起。
     */
    protected void onTailUp() {

    }
}

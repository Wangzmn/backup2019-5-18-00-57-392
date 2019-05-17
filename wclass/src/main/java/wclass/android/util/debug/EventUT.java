package wclass.android.util.debug;

import android.view.MotionEvent;

import wclass.enums.EventType;

/**
 * @作者 做就行了！
 * @时间 2019-04-14下午 10:04
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class EventUT {

    /**
     * 将{@link MotionEvent}的action状态，转为字符串。
     */
    public static String actionToStr(MotionEvent ev) {
        return convert(ev).toString();
    }
    //--------------------------------------------------
    /**
     * 将{@link MotionEvent}的actionMasked转为我的enum类型。
     *
     * @param actionMasked {@link MotionEvent#getActionMasked()}
     * @return {@link EventType}
     */
    public static EventType convert(int actionMasked) {
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                return EventType.DOWN;

            case MotionEvent.ACTION_MOVE:
                return EventType.MOVE;

            case MotionEvent.ACTION_UP:
                return EventType.UP;

            case MotionEvent.ACTION_POINTER_DOWN:
                return EventType.POINTER_DOWN;

            case MotionEvent.ACTION_POINTER_UP:
                return EventType.POINTER_UP;

            case MotionEvent.ACTION_CANCEL:
                return EventType.EXIT_WITH_NO_POINTER;
            default:
                return EventType.NO_POINTER;
        }
    }

    /**
     * {@link EventUT#convert(int)}
     */
    public static EventType convert(MotionEvent ev) {
        int actionMasked = ev.getActionMasked();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                return EventType.DOWN;

            case MotionEvent.ACTION_MOVE:
                return EventType.MOVE;

            case MotionEvent.ACTION_UP:
                return EventType.UP;

            case MotionEvent.ACTION_POINTER_DOWN:
                return EventType.POINTER_DOWN;

            case MotionEvent.ACTION_POINTER_UP:
                return EventType.POINTER_UP;

            case MotionEvent.ACTION_CANCEL:
                return EventType.EXIT_WITH_NO_POINTER;
            default:
                return EventType.NO_POINTER;
        }
    }
}
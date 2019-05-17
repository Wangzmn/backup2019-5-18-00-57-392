package wclass.android.ui;

import android.view.MotionEvent;

import wclass.enums.EventType;

/**
 * @作者 做就行了！
 * @时间 2018-12-26下午 5:12
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 */
public class EventTypeConverter {
    /**
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
}
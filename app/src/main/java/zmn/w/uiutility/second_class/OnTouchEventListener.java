package zmn.w.uiutility.second_class;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public interface OnTouchEventListener {

    /**
     * {@link View#onTouchEvent(MotionEvent)}
     */
    boolean onTouchEvent(MotionEvent event);

    /**
     * {@link ViewGroup#onInterceptTouchEvent(MotionEvent)}
     */
    boolean onInterceptTouchEvent(MotionEvent ev);
}
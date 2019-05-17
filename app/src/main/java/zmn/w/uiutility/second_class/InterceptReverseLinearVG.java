package zmn.w.uiutility.second_class;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import wclass.android.ui.view.ReverseLinearLayout;

/**
 * @作者 做就行了！
 * @时间 2019-03-01下午 4:02
 * @该类用途： -
 * 1、可拦截事件的{@link ReverseLinearLayout}
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class InterceptReverseLinearVG extends ReverseLinearLayout {

    public InterceptReverseLinearVG(@NonNull Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return cb != null && cb.onInterceptTouchEvent(ev) ||
                super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean b = super.onTouchEvent(event);
        if (cb != null) {
            b |= cb.onTouchEvent(event);
        }
        return b;
    }

    OnTouchEventListener cb;
    public void setOnInterceptTouchEventListener(OnTouchEventListener cb) {
        this.cb = cb;
    }
}
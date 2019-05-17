package wclass.android.ui.view.base_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

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
 */
@SuppressWarnings("DanglingJavadoc")
public class UsefulViewGroup extends ViewGroup {
    /*ViewGroup子类通用代码。
    子view请求事件。2019年4月29日18:00:31*/
    /**
     * 标记：子view是否请求触摸事件。
     */
    private boolean childRequestEvent;

    /**
     * 该变量作用：防止熄屏时手指按下期间的开屏直接滑动。
     * 如果是这种情况，那么此次touchEvent是没有down事件的。
     */
    private boolean needDownEvent;

    public boolean isChildRequestEvent() {
        return childRequestEvent;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        childRequestEvent |= disallowIntercept;
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            needDownEvent = true;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            needDownEvent = false;
            childRequestEvent = false;
        }
        /**
         * 警告：此次事件未经过down事件！！！
         * 这是一次不正常的事件！！！直接返回！！！
         */
        if (needDownEvent) {
            return true;
        }

        return super.dispatchTouchEvent(ev);
    }

    //////////////////////////////////////////////////
//    boolean init = false;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w <= 0 || h <= 0) {
            return;
        }
//        if (!init) {
//            init = true;
//            onInit(w, h);
//        }
        onSizeChangedSafely(w, h);
    }

//    /**
//     * 当容器有大小时，该方法会被调用。
//     * <p>
//     * 警告：永远只会被调用一次！！！
//     *
//     * @param w 容器宽。
//     * @param h 容器高。
//     */
//    protected void onInit(int w, int h) {
//
//    }

    protected void onSizeChangedSafely(int w, int h) {

    }
    //////////////////////////////////////////////////
    /** {@link #onLayout}中，可使用方法。*/

    /**
     * 获取布局时可用的宽。
     */
    protected int getUsableWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    /**
     * 获取布局时可用的高。
     */
    protected int getUsableHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }
    //--------------------------------------------------
    /**{@link #onMeasure}中，可使用方法。*/
    /**
     * 测量指定的孩子。
     */
    protected void measureChild(View child) {
        measureChild(child, getMeasuredWidthAndState(), getMeasuredHeightAndState());
    }

    /**
     * 包括margin在内测量孩子的宽高，每个孩子独立测量，互不影响。
     */
    protected void measureChildrenWithMarginsSelfish() {
        int widthMeasureSpec = getMeasuredWidthAndState();
        int heightMeasureSpec = getMeasuredHeightAndState();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getLayoutParams() instanceof MarginLayoutParams) {
                measureChildWithMargins(child, widthMeasureSpec, 0,
                        heightMeasureSpec, 0);
            } else {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }
        }
    }
    //////////////////////////////////////////////////

    /**
     * 获取子view布局时的宽。
     *
     * @param child 子view。
     * @return 获取子view布局时的宽。
     */
    protected int getLayoutWidth(View child) {
        ViewGroup.LayoutParams childParams = child.getLayoutParams();
        if (childParams instanceof MarginLayoutParams) {
            MarginLayoutParams childParams1 = (MarginLayoutParams) childParams;
            return child.getMeasuredWidth() + childParams1.leftMargin + childParams1.rightMargin;
        } else {
            return child.getMeasuredWidth();
        }
    }

    /**
     * 获取子view布局时的高。
     *
     * @param child 子view。
     * @return 获取子view布局时的宽。
     */
    protected int getLayoutHeight(View child) {
        ViewGroup.LayoutParams childParams = child.getLayoutParams();
        if (childParams instanceof MarginLayoutParams) {
            MarginLayoutParams childParams1 = (MarginLayoutParams) childParams;
            return child.getMeasuredHeight() + childParams1.topMargin + childParams1.bottomMargin;
        } else {
            return child.getMeasuredHeight();
        }
    }

    //////////////////////////////////////////////////
    public UsefulViewGroup(Context context) {
        super(context);
    }

    public UsefulViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UsefulViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}

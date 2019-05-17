package wclass.android.ui.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

/**
 * @时间 2019-03-01下午 3:51
 * @该类用途： -
 * 1、通过该方法设置从左/右开始布局：{@link View#setLayoutDirection(int)}。
 * 2、通过该方法设置从上/下开始布局：{@link #setLayoutVerticalFromLastChild}。
 * @注意事项： -
 * 1、如果有分割线，可能会布局异常！！！
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class ReverseLinearLayout extends LinearLayout {

    /**
     * 纵向布局时，从最后一个孩子开始，从前往后布局。
     */
    boolean layoutVerticalFromLastChild;

    public ReverseLinearLayout(Context context) {
        super(context);
        setOrientation(VERTICAL);
    }

    /**
     * 纵向布局时，从最后一个孩子开始，从前往后布局。
     */
    public boolean isLayoutVerticalFromLastChild() {
        return layoutVerticalFromLastChild;
    }

    /**
     * 纵向布局时，从最后一个孩子开始，从前往后布局。
     *
     * @param layoutVerticalFromLastChild true：启动该功能。
     */
    public void setLayoutVerticalFromLastChild(boolean layoutVerticalFromLastChild) {
        this.layoutVerticalFromLastChild = layoutVerticalFromLastChild;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getOrientation() == VERTICAL && layoutVerticalFromLastChild) {
            layoutVerticalFromFirstChild(l, t, r, b);
        } else {
            super.onLayout(changed, l, t, r, b);
        }
    }

    void layoutVerticalFromFirstChild(int left, int top, int right, int bottom) {
//        Log.e("TAG", " layoutVerticalFromFirstChild ");
        final int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();

        int childTop;
        int childLeft;

        // Where right end of child should go
        final int width = right - left;
        int childRight = width - paddingRight;

        // Space available for child
        int childSpace = width - paddingLeft - paddingRight;

        final int count = getChildCount();

        final int majorGravity = mGravity & Gravity.VERTICAL_GRAVITY_MASK;
        final int minorGravity = mGravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;

        switch (majorGravity) {
            case Gravity.BOTTOM:
                // mTotalLength contains the padding already
                int verticalTotalLength = getVerticalTotalLength();
                childTop = paddingTop + bottom - top - verticalTotalLength;
                break;

            // mTotalLength contains the padding already
            case Gravity.CENTER_VERTICAL:
                int verticalTotalLength1 = getVerticalTotalLength();
                childTop = paddingTop + (bottom - top - verticalTotalLength1) / 2;
                break;

            case Gravity.TOP:
            default:
                childTop = paddingTop;
                break;
        }

        for (int i = count - 1; i >= 0; i--) {
            final View child = getChildAt(i);
            if (child != null) {
                if (child.getVisibility() != GONE) {
                    final int childWidth = child.getMeasuredWidth();
                    final int childHeight = child.getMeasuredHeight();

                    final LayoutParams lp =
                            (LayoutParams) child.getLayoutParams();

                    int gravity = lp.gravity;
                    if (gravity < 0) {
                        gravity = minorGravity;
                    }
                    final int layoutDirection = getLayoutDirection();
                    final int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
                    switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
                        case Gravity.CENTER_HORIZONTAL:
                            childLeft = paddingLeft + ((childSpace - childWidth) / 2)
                                    + lp.leftMargin - lp.rightMargin;
                            break;

                        case Gravity.RIGHT:
                            childLeft = childRight - childWidth - lp.rightMargin;
                            break;

                        case Gravity.LEFT:
                        default:
                            childLeft = paddingLeft + lp.leftMargin;
                            break;
                    }

                    childTop += lp.topMargin;
                    setChildFrame(child, childLeft, childTop,
                            childWidth, childHeight);
                    childTop += childHeight + lp.bottomMargin;

                }
            }
        }
    }

    private int getVerticalTotalLength() {
        int totalLength = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            LayoutParams lp = (LayoutParams) v.getLayoutParams();
            totalLength += lp.height + lp.topMargin + lp.bottomMargin;
        }
        return totalLength;
    }

    private int mGravity = Gravity.START | Gravity.TOP;

    public void setGravity(int gravity) {
        super.setGravity(gravity);
        if (mGravity != gravity) {
            if ((gravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) == 0) {
                gravity |= Gravity.START;
            }

            if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == 0) {
                gravity |= Gravity.TOP;
            }

            mGravity = gravity;
        }
    }

    public void setHorizontalGravity(int horizontalGravity) {
        super.setHorizontalGravity(horizontalGravity);
        final int gravity = horizontalGravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        if ((mGravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) != gravity) {
            mGravity = (mGravity & ~Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) | gravity;
        }
    }

    public void setVerticalGravity(int verticalGravity) {
        super.setVerticalGravity(verticalGravity);
        final int gravity = verticalGravity & Gravity.VERTICAL_GRAVITY_MASK;
        if ((mGravity & Gravity.VERTICAL_GRAVITY_MASK) != gravity) {
            mGravity = (mGravity & ~Gravity.VERTICAL_GRAVITY_MASK) | gravity;
        }
    }

    private void setChildFrame(View child, int left, int top, int width, int height) {
        child.layout(left, top, left + width, top + height);
    }

}

package wclass.android.ui.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import wclass.android.util.debug.LogUT;
import wclass.android.util.LayoutParamsUT;
import wclass.android.util.ViewUT;

/**
 * @作者 做就行了！
 * @时间 2019-04-17下午 11:15
 * @该类描述： -
 * 1、横向LinearLayout，
 *    一个有权重的子view将其他的子view推向最左和最右边。
 * 2、该类还未完善。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public abstract class LinearLayoutSimple extends LinearLayout {
    private static final boolean DEBUG = true;

    /**
     * 问题：
     * 1、view还没有设置内容。
     * 2、view还没有调整大小。
     */
    public LinearLayoutSimple(Context context, int leftCount, int rightCount) {
        super(context);

        midChild = new View(context);
        LayoutParams p = LayoutParamsUT.linearParamsWidthWeight();
        midChild.setLayoutParams(p);
        //先添加左边childs。
        for (int i = 0; i < leftCount; i++) {
            ImageView child = new ImageView(context);
            onLeftSetContent(child, i);
            leftChilds.add(child);
            addView(child);
        }
        //撑起来。
        addView(midChild);
        //再添加右边childs。
        for (int i = 0; i < rightCount; i++) {
            ImageView child = new ImageView(context);
            onRightSetContent(child, i);
            rightChilds.add(child);
            addView(child);
        }
    }

    /**
     * 左边子view设置内容。
     *
     * @param child     子view。
     * @param dexInLeft 该view在左边的下标。
     */
    protected abstract void onLeftSetContent(ImageView child, int dexInLeft);

    /**
     * 右边子view设置内容。
     *
     * @param child      子view。
     * @param dexInRight 该view在右边的下标。
     */
    protected abstract void onRightSetContent(ImageView child, int dexInRight);

    /**
     * 左边子view调整大小。
     *
     * @param child     子view。
     * @param dexInLeft 该view在左边的下标。
     */
    protected void onAdjustLeftsSize(ImageView child, int dexInLeft) {
        int asSize = getUsableHeight();
        int pad = asSize / 20;
        child.setPadding(pad, pad, pad, pad);
        ViewUT.adjustSize(child, asSize, asSize);
    }

    /**
     * 右边子view调整大小。
     *
     * @param child      子view。
     * @param dexInRight 该view在右边的下标。
     */
    protected void onAdjustRightsSize(ImageView child, int dexInRight) {
        int asSize = getUsableHeight();
        int pad = asSize / 20;
        child.setPadding(pad, pad, pad, pad);
        ViewUT.adjustSize(child, asSize, asSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onMeasure:" +
                    " width = " + MeasureSpec.getSize(widthMeasureSpec) +
                    " height = " + MeasureSpec.getSize(heightMeasureSpec));
        }
    }

    List<ImageView> leftChilds = new ArrayList<>();
    List<ImageView> rightChilds = new ArrayList<>();
    View midChild;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        onMeasureChilds(changed);
//        onMeasureChilds(getMeasuredWidthAndState(),getMeasuredHeightAndState());
        super.onLayout(changed, l, t, r, b);
        if (DEBUG) {
            for (int i = 0; i < getChildCount(); i++) {
                View c = getChildAt(i);
                LogUT.XYWH(c, getClass() + "#onLayout:" + " child" + i);
            }
        }
    }

    /**
     * layout之前测量孩子。
     *
     * @param changed 容器大小相关属性是否改变。
     */
    private void onMeasureChilds(boolean changed) {
        if (!changed) {
            return;
        }
        int measuredWidthAndState = getMeasuredWidthAndState();
        int measuredHeightAndState = getMeasuredHeightAndState();
        if (leftChilds != null) {

            for (int i = 0; i < leftChilds.size(); i++) {
                ImageView child = leftChilds.get(i);
                onAdjustLeftsSize(child, i);
                ViewGroup.LayoutParams p = child.getLayoutParams();
                child.measure(p.width, p.height);
            }
        }
        if (rightChilds != null) {
            for (int i = 0; i < rightChilds.size(); i++) {
                ImageView child = rightChilds.get(i);
                onAdjustRightsSize(child, i);
                ViewGroup.LayoutParams p = child.getLayoutParams();
                child.measure(p.width, p.height);
            }
        }
    }

    /**
     * 获取可用高。
     */
    protected int getUsableHeight() {
        return getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
    }
}

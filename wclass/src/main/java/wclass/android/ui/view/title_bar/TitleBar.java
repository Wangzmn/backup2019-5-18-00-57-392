package wclass.android.ui.view.title_bar;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import wclass.android.ui.view.base_view.UsefulViewGroup;
import wclass.android.util.LayoutUT;
import wclass.android.util.ViewUT;
import wclass.android.util.debug.LogUT;
import wclass.common.WH;
import wclass.enums.Vertical3;

/**
 * @作者 做就行了！
 * @时间 2019-05-05下午 4:17
 * @该类描述： -
 * 1、大概样子：
 * ①横向布局的容器。
 * ②左边可以放多个view。这些view的顺序为：从左到右，下标从0递增。
 * ③右边可以放多个view。这些view的顺序为：从右到左，下标从0递增。
 * ④中间放一个view。中间的这个view，可以作为显示标题的容器。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public abstract class TitleBar extends UsefulViewGroup {

    private static final boolean DEBUG = true;
    private Context context;

    /**
     * 左边的items。
     */
    private List<View> lefts = new ArrayList<>();

    /**
     * 右边的items。
     */
    private List<View> rights = new ArrayList<>();

    /**
     * 中间的item。
     * 友情提示：可以为null。
     */
    private View mid;
    //////////////////////////////////////////////////
    public TitleBar(Context context) {
        super(context);
        this.context = context;
    }
    boolean init = false;
    public void init(){
        if(init){
            return;
        }
        initInner();
    }

    //////////////////////////////////////////////////
    /**
     * 在该方法中创建所有view。
     * {@link #onGetMidMenu(Context)}
     * {@link #onGetLeftMenu(Context, int)}
     * {@link #onGetRightMenu(Context, int)}
     * 在以上方法中返回他们。
     *
     * @param context 上下文。
     */
    protected abstract void onCreateViews(Context context);

    /**
     * 获取中间的控件。
     * <p>
     * 友情提示：可以返回null。
     *
     * @param context 上下文。
     * @return 中间的控件。
     */
    protected abstract View onGetMidMenu(Context context);

    /**
     * 获取左边的控件。
     * <p>
     * 友情提示：可以返回null。
     *
     * @param context  上下文。
     * @param position 从左到右排序的下标。
     * @return 左边的控件。
     */
    protected abstract View onGetLeftMenu(Context context, int position);

    /**
     * 获取右边的控件。
     * <p>
     * 友情提示：可以返回null。
     *
     * @param context  上下文。
     * @param position 从右到左排序的下标。
     * @return 右边的控件。
     */
    protected abstract View onGetRightMenu(Context context, int position);

    //////////////////////////////////////////////////

    /**
     * titleLayout每次大小改变时，会调用该方法。
     * <p>
     * 友情提示：
     * 1、可以在该方法中调整子view的大小。
     * 2、如果{@link #leftsRightsSameSize()}返回true，
     * 那么左右两侧的item大小将采用{@link #getSameSize()}的大小。
     * 也就是说，在该方法调整左右两侧的view的大小是无效的。
     *  @param w           他的宽。
     * @param h           他的高。
     */
    protected abstract void onSizeChangeSafely2(int w, int h);

    protected abstract void onAdjustViews(int w, int h) ;

    //////////////////////////////////////////////////

    /**
     * 获取左边控件的数量。
     */
    protected abstract int getLeftMenuCount();

    /**
     * 获取右边控件的数量。
     */
    protected abstract int getRightMenuCount();

    /**
     * 获取垂直方向的布局锚点。
     */
    protected Vertical3 getVerticalType() {
        return Vertical3.MID;
    }

    //////////////////////////////////////////////////

    /**
     * 获取位于左右两边的item，他们之间的间隙。
     */
    protected int getItemGap() {
        return 0;
    }

    /**
     * 最左和最右两端是否有间隙。
     *
     * @return true：最左和最右也有间隙。
     */
    protected boolean isLeftAndRightHasGap() {
        return false;
    }

    /**
     * 位于左右两边的item是否一样大小。
     * <p>
     * 警告：如果返回true，请重写{@link #getSameSize()}方法。
     *
     * @return true：他们一样大小。
     */
    protected boolean leftsRightsSameSize() {
        return false;
    }

    /**
     * 获取位于左右两边的item的宽高。
     * 警告：重写{@link #leftsRightsSameSize()}后，该方法才生效。
     */
    protected WH getSameSize() {
        throw new IllegalStateException("如果您重写了leftsRightsSameSize()方法，" +
                "请重写getSameSize()方法。");
    }

    //////////////////////////////////////////////////

    private void initInner() {
        init = true;
        onCreateViews(context);
        int leftMenuCount = getLeftMenuCount();
        for (int i = 0; i < leftMenuCount; i++) {
            View leftMenu = onGetLeftMenu(context, i);
            lefts.add(leftMenu);
            addView(leftMenu);
        }
        int rightMenuCount = getRightMenuCount();
        for (int i = 0; i < rightMenuCount; i++) {
            View rightMenu = onGetRightMenu(context, i);
            rights.add(rightMenu);
            addView(rightMenu);
        }
        View midMenu = onGetMidMenu(context);
        if (midMenu != null) {
            mid = midMenu;
            addView(mid);
        }
        onCreateViewsFinish();
    }

    /**
     * 所有view初始化完成后，的回调。
     */
    protected void onCreateViewsFinish() {
    }

    //////////////////////////////////////////////////

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        //初始化该类。
        init();
    }

    @Override
    protected final void onSizeChangedSafely(int w, int h) {
        super.onSizeChangedSafely(w, h);
        onSizeChangeSafely2(w, h);
        onAdjustViews(w,h);
        boolean same = leftsRightsSameSize();
        if (same) {
            WH wh = getSameSize();
            for (int i = 0; i < lefts.size(); i++) {
                View child = lefts.get(i);
                ViewUT.adjustSize(child, wh.w, wh.h);
            }
            for (int i = 0; i < rights.size(); i++) {
                View child = rights.get(i);
                ViewUT.adjustSize(child, wh.w, wh.h);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //布局之前测量。
        measureChildrenWithMarginsSelfish();
        Vertical3 verti = getVerticalType();
        int w = getWidth();
        int h = getHeight();
        layoutLeftssss(verti, w, h);
        layoutRightssss(verti, w, h);
        layoutMidMenu(verti, w, h);
    }

    /**
     * 布局左边的控件。
     */
    private void layoutLeftssss(Vertical3 verti, int w, int h) {
        int layoutLeft = getStartLayoutLeft();
        int layoutTop;
        switch (verti) {
            case TOP:
                layoutTop = getPaddingTop();
                for (int i = 0; i < lefts.size(); i++) {
                    View child = lefts.get(i);
                    int childLayoutWidth = getLayoutWidth(child);
                    LayoutUT.layout(child, layoutLeft, layoutTop);
                    layoutLeft = getNextLayoutLeft(layoutLeft, childLayoutWidth);
                }
                break;
            case MID:
                for (int i = 0; i < lefts.size(); i++) {
                    View child = lefts.get(i);
                    int childLayoutWidth = getLayoutWidth(child);
                    int childLayoutHeight = getLayoutHeight(child);
                    layoutTop = (h - childLayoutHeight) / 2;
                    LayoutUT.layout(child, layoutLeft, layoutTop);
                    layoutLeft = getNextLayoutLeft(layoutLeft, childLayoutWidth);
                }
                break;
            case BOTTOM:
                int layoutBottom = h - getPaddingBottom();
                for (int i = 0; i < lefts.size(); i++) {
                    View child = lefts.get(i);
                    int childLayoutWidth = getLayoutWidth(child);
                    int childLayoutHeight = getLayoutHeight(child);
                    layoutTop = layoutBottom - childLayoutHeight;
                    LayoutUT.layout(child, layoutLeft, layoutTop);
                    layoutLeft = getNextLayoutLeft(layoutLeft, childLayoutWidth);
                }
                break;
            default:
                throw new IllegalStateException();
        }

        if (DEBUG) {
            for (int i = 0; i < lefts.size(); i++) {
                String str = getClass() + "#layoutLeftssss:" +
                        " child" + i + ":";
                LogUT.XYWH(lefts.get(i), str);
            }
        }

    }

    /**
     * 布局右边的控件。
     */
    private void layoutRightssss(Vertical3 verti, int w, int h) {
        int layoutLeft;
        int layoutRight = getStartLayoutRight(w);
        int layoutTop;
        switch (verti) {
            case TOP:
                layoutTop = getPaddingTop();
                for (int i = 0; i < rights.size(); i++) {
                    View child = rights.get(i);
                    int childLayoutWidth = getLayoutWidth(child);
                    int childLayoutHeight = getLayoutHeight(child);
                    layoutLeft = layoutRight - childLayoutWidth;
                    LayoutUT.layout(child, layoutLeft, layoutTop);
                    layoutRight = getNextLayoutRight(layoutLeft);
                }
                break;
            case MID:
                for (int i = 0; i < rights.size(); i++) {
                    View child = rights.get(i);
                    int childLayoutWidth = getLayoutWidth(child);
                    int childLayoutHeight = getLayoutHeight(child);
                    layoutLeft = layoutRight - childLayoutWidth;
                    layoutTop = (h - childLayoutHeight) / 2;
                    LayoutUT.layout(child, layoutLeft, layoutTop);
                    layoutRight = getNextLayoutRight(layoutLeft);
                }
                break;
            case BOTTOM:
                int layoutBottom = h - getPaddingBottom();
                for (int i = 0; i < rights.size(); i++) {
                    View child = rights.get(i);
                    int childLayoutWidth = getLayoutWidth(child);
                    int childLayoutHeight = getLayoutHeight(child);
                    layoutLeft = layoutRight - childLayoutWidth;
                    layoutTop = layoutBottom - childLayoutHeight;

                    LayoutUT.layout(child, layoutLeft, layoutTop);
                    layoutRight = getNextLayoutRight(layoutLeft);
                }
                break;
            default:
                throw new IllegalStateException();
        }
    }

    /**
     * 布局中间的控件。
     */
    private void layoutMidMenu(Vertical3 verti, int w, int h) {
        if (mid != null) {
            int layoutWidth = getLayoutWidth(mid);
            int layoutLeft = (w - layoutWidth) / 2;
            int layoutTop;
            switch (verti) {
                case TOP:
                    layoutTop = getPaddingTop();
                    LayoutUT.layout(mid, layoutLeft, layoutTop);
                    break;
                case MID:
                    int childLayoutHeight = getLayoutHeight(mid);
                    layoutTop = (h - childLayoutHeight) / 2;
                    LayoutUT.layout(mid, layoutLeft, layoutTop);
                    break;
                case BOTTOM:
                    int childLayoutHeight2 = getLayoutHeight(mid);
                    int layoutBottom = h - getPaddingBottom();
                    layoutTop = layoutBottom - childLayoutHeight2;
                    LayoutUT.layout(mid, layoutLeft, layoutTop);
                    break;
            }
        }
    }

    //////////////////////////////////////////////////

    /**
     * 获取左侧items，开始布局时的layoutLeft。
     */
    private int getStartLayoutLeft() {
        boolean has = isLeftAndRightHasGap();
        int gap = 0;
        if (has) {
            gap = getItemGap();
        }
        return getPaddingLeft() + gap;
    }

    /**
     * 获取右侧items，开始布局时的layoutRight。
     */
    private int getStartLayoutRight(int w) {
        boolean has = isLeftAndRightHasGap();
        int gap = 0;
        if (has) {
            gap = getItemGap();
        }
        return w - getPaddingRight() - gap;
    }

    /**
     * 获取下一个item的layoutLeft。
     *
     * @param layoutLeft       当前item的layoutLeft。
     * @param childLayoutWidth item的layoutWidth。
     * @return 获取下一个item的layoutLeft。
     */
    private int getNextLayoutLeft(int layoutLeft, int childLayoutWidth) {
        return layoutLeft + childLayoutWidth + getItemGap();
    }

    /**
     * 获取下一个item的layoutRight。
     *
     * @param layoutLeft 当前item的layoutLeft。
     * @return 获取下一个item的layoutLeft。
     */
    private int getNextLayoutRight(int layoutLeft) {
        return layoutLeft - getItemGap();
    }

}

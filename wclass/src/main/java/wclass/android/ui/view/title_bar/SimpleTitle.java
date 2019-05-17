package wclass.android.ui.view.title_bar;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import wclass.android.util.DebugUT;
import wclass.android.util.ViewUT;
import wclass.common.WH;

/**
 * @作者 做就行了！
 * @时间 2019-05-07下午 5:08
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public abstract class SimpleTitle<T extends View> extends TitleBar {
    private static final boolean DEBUG = true;
    //////////////////////////////////////////////////
    private final Context context;
    private final int leftCount;
    private final int rightCount;

    private T title;
    private List<ImageView> lefts = new ArrayList<>();
    private List<ImageView> rights = new ArrayList<>();

    /**
     * 存放左右方view的宽高。
     */
    private WH wh = new WH();

    public SimpleTitle(Context context, int leftCount, int rightCount) {
        super(context);
        this.context = context;
        this.leftCount = leftCount;
        this.rightCount = rightCount;
        init();
    }

    /**
     * 获取指定的左边的view。
     * @param position 该view的下标。
     * @return 指定的左边的view。
     */
    protected ImageView getLeftView(int position) {
        return lefts.get(position);
    }

    /**
     * 获取指定的右边的view。
     * @param position 该view的下标。
     * @return 指定的右边的view。
     */
    protected ImageView getRightView(int position) {
        return rights.get(position);
    }

    /**
     * 获取标题控件。
     */
    protected T getTitle(){
        return title;
    }

    /**
     * 获取左边view的数量。
     */
    protected int getLeftCount() {
        return leftCount;
    }

    /**
     * 获取右边view的数量。
     */
    protected int getRightCount() {
        return rightCount;
    }

    //////////////////////////////////////////////////

    /**
     * 子类可以在该方法中设置view的context。
     *
     * @param context 上下文。
     */
    @Override
    protected void onCreateViews(Context context) {
        for (int i = 0; i < leftCount; i++) {
            ImageView iv = new ImageView(context);
            lefts.add(iv);
            if (DEBUG) {
                DebugUT.randomBG(iv);
            }
        }

        for (int i = 0; i < rightCount; i++) {
            ImageView iv = new ImageView(context);
            rights.add(iv);
            if (DEBUG) {
                DebugUT.randomBG(iv);
            }
        }

        title = onCreateTitle(context);

        if (DEBUG) {
            DebugUT.randomBG(title);
        }
    }

    /**
     * 创建标题控件，并返回。
     *
     * 友情提示：可以返回null。
     */
    protected abstract T onCreateTitle(Context context);

    @Override
    protected void onSizeChangeSafely2(int w, int h) {

    }

    @Override
    protected void onAdjustViews(int w, int h) {
        wh.set(getSize(w, h));
        if (DEBUG) {
            ViewUT.adjustSize(title, wh.h*2, wh.h);
        }
    }

    /**
     * 子类可以重写左右方view的大小。
     *
     * @param w           他的宽。
     * @param h           他的高。
     * @return 左右方view的大小
     */
    protected WH getSize(int w, int h) {
        int pt = getPaddingTop();
        int pb = getPaddingBottom();
        int size = h - pt - pb;
        return new WH(size, size);
    }

    //////////////////////////////////////////////////
    @Override
    protected View onGetMidMenu(Context context) {
        return title;
    }

    @Override
    protected View onGetLeftMenu(Context context, int position) {
        return lefts.get(position);
    }

    @Override
    protected View onGetRightMenu(Context context, int position) {
        return rights.get(position);
    }

    @Override
    protected boolean leftsRightsSameSize() {
        return true;
    }

    @Override
    protected int getItemGap() {
        return wh.h / 4;
    }

    @Override
    protected WH getSameSize() {
        return wh;
    }

    @Override
    protected int getLeftMenuCount() {
        return leftCount;
    }

    @Override
    protected int getRightMenuCount() {
        return rightCount;
    }
}

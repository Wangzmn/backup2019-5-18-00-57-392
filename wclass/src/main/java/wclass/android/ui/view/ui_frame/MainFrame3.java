package wclass.android.ui.view.ui_frame;

import android.content.Context;
import android.view.View;

import wclass.android.ui.view.base_view.UsefulLinearLayout;
import wclass.android.util.ViewUT;

/**
 * @作者 做就行了！
 * @时间 2019-05-09下午 3:18
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public abstract class MainFrame3 extends UsefulLinearLayout {

    public View head;//头部控件
    public View mid;//中间控件
    public View tail;//尾部控件

    /**
     * 标记是否初始化。
     */
    private boolean init;

    public MainFrame3(Context context) {
        super(context);
        setOrientation(VERTICAL);
        init();
    }
    //////////////////////////////////////////////////


    /**
     * 在该方法中创建views，通过以下方法返回这些views：
     * {@link #onGetHead()}
     * {@link #onGetMid()}
     * {@link #onGetTail()}
     */
    protected abstract void onCreateViews();

    /**
     * 获取head。
     */
    protected abstract View onGetHead();

    /**
     * 获取mid。
     */
    protected abstract View onGetMid();

    /**
     * 获取tail。
     */
    protected abstract View onGetTail();

    /**
     * 获取head的高度。
     */
    protected abstract int getHeadHeight();

    /**
     * 获取mid的高度。
     */
    protected int getMidHeight() {
        return 10;
    }

    /**
     * 获取tail的高度。
     *
     * @return tail的高度。
     */
    protected abstract int getTailHeight();

    //////////////////////////////////////////////////

    /**
     * 每次父容器大小改变时，会调用该方法。
     *
     * @param root  父容器。
     * @param rootW 父容器宽。
     * @param rootH 父容器高。
     */
    public void onSizeChangedSafely(MainFrame3 root, int rootW, int rootH) {
    }

    /**
     * 每次父容器大小改变后，会调用该方法。
     *
     * @param root  父容器。
     * @param rootW 父容器宽。
     * @param rootH 父容器高。
     */
    public void onAdjustViews(MainFrame3 root, int rootW, int rootH) {
        ViewUT.adjustSize(root.head, rootW, getHeadHeight());
        ViewUT.adjustSize(root.mid, rootW, getMidHeight());
        ViewUT.adjustSize(root.tail, rootW, getTailHeight());
    }

    //////////////////////////////////////////////////

    @Override
    protected void onSizeChangedSafely(int w, int h) {
        super.onSizeChangedSafely(w, h);
        //adapter收到onSizeChanged之前，必须先创建views。
        onSizeChangedSafely(this, w, h);
        onAdjustViews(this, w, h);
    }

    private void init() {
        onCreateViews();
        head = onGetHead();
        mid = onGetMid();
        tail = onGetTail();
        addView(head);
        if (mid != null) {
            addView(mid);
        }
        addView(head);
    }

}

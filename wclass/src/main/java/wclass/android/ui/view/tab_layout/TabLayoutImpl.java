package wclass.android.ui.view.tab_layout;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import wclass.android.ui.view.mover_view.MoverView;
import wclass.android.util.ViewUT;
import wclass.util.ColorUT;

/**
 * @作者 做就行了！
 * @时间 2019/4/3 0003
 * @使用说明：
 */
public class TabLayoutImpl extends TabLayout {

    /**
     * todo 待解决：
     * 1、选中的tab改变时，可以自定义指示器的滑动动画。
     */
    //////////////////////////////////////////////////
    private static final boolean DEBUG = true;
    /**
     * 作为indicator的容器。
     */
    private MoverView moverView;
    private final Context context;//上下文。
    //--------------------------------------------------
    private AdapterII adapter;
    //////////////////////////////////////////////////

    public TabLayoutImpl(Context context) {
        super(context);
        this.context = context;
    }
    //////////////////////////////////////////////////

    @Override
    public void setAdapter(Adapter adapter) {
        throw new IllegalStateException();
    }

    public void setAdapter(AdapterII adapter) {
        super.setAdapter(adapter);
        this.adapter = adapter;
    }


    @SuppressWarnings("WeakerAccess")
    public static abstract class AdapterII<T extends View>
            extends Adapter<T> {
        /**
         * 指示器图片的容器view。
         *
         * fix：moverView的rect如何调整。
         */
        private MoverView moverView;

        /**
         * 获取指示器占位大小。
         */
        public abstract int getIndicatorHolderSize();

        @Override
        public TabLayoutImpl getTabLayout() {
            return (TabLayoutImpl) super.getTabLayout();
        }

        /**
         * 调整{@link MoverView}中，指示器图片的rect。
         * @param moverView {@link MoverView}
         * @param selectedTabDex 选中的tab的下标。
         */
        public void onReRectIndicator(MoverView moverView, int selectedTabDex) {
            TabLayout tabLayout = getTabLayout();
            TabInfo selectedTabInfo = tabLayout.getTabInfo(selectedTabDex);
            Rect rect = selectedTabInfo.rect;
            int left = rect.left;
            int right = rect.right;
            moverView.reRect(left, 0, right, moverView.getHeight());
        }

        @Override
        public void onSelectedChanged(int newTabDex, int oldTabDex, boolean fromTouch) {
            super.onSelectedChanged(newTabDex, oldTabDex, fromTouch);
            TabLayout tabLayout = getTabLayout();
            TabInfo newTabInfo = tabLayout.getTabInfo(newTabDex);
            Rect rect = newTabInfo.rect;
            MoverView moverView = getIndicatorBox();
            moverView.animReRect(rect.left, 0, rect.right, moverView.getHeight());
        }

        public MoverView onCreateIndicator() {
            moverView = new MoverView(getContext());
            return moverView;
        }

        public MoverView getIndicatorBox() {
            return moverView;
        }
    }

    //////////////////////////////////////////////////

    @Override
    protected void onInit() {
        super.onInit();
        //--------------------------------------------------
        /*创建指示器的容器，并添加至ViewGroup。*/
        moverView = adapter.onCreateIndicator();
        addView(moverView);
        //--------------------------------------------------
        if (DEBUG) {
            moverView.setBackgroundColor(ColorUT.BLUE);
            moverView.setMoverDrawable(new ColorDrawable(ColorUT.RED));
            Log.e("TAG", getClass() + "#onInit:" +
                    " 结束。 ");
        }
    }

    @Override
    protected void onLayoutOptimize() {
        super.onLayoutOptimize();
        onLayoutMover();
    }

    protected void onLayoutMover() {
        if (moverView == null) {
            return;
        }
        /*布局下划线容器。*/
        int indicatorHolderSize = adapter.getIndicatorHolderSize();
        int width = getWidth();
        int height = getHeight();

        int left = 0;
        //放在ViewGroup的底部。
        int top = height - indicatorHolderSize;
        int right = width;
        int bottom = height;

        ViewUT.adjustSize(moverView, width, indicatorHolderSize);
        measureChild(moverView);
        moverView.layout(left, top, right, bottom);
        //--------------------------------------------------
        /*设置下划线drawable的rect。*/
        TabInfo selectedTab = getSelectedTabInfo();
        adapter.onReRectIndicator(moverView, selectedTab.dex);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (DEBUG) {
            if (moverView == null) {
                return super.dispatchTouchEvent(ev);
            }
            Rect rect = new Rect();
            moverView.getHitRect(rect);
            Log.e("TAG", " moverViewRect = " + rect);
            Log.e("TAG", " moverDrawableRect = "
                    + moverView.getMoverDrawableRect());
        }
        return super.dispatchTouchEvent(ev);
    }

}

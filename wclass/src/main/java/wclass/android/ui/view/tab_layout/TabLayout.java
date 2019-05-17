package wclass.android.ui.view.tab_layout;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import wclass.android.ui.view.base_view.UsefulViewGroup;
import wclass.android.util.ViewUT;
import wclass.common.WH;
import wclass.common.XY;
import wclass.enums.Orien2;

/**
 * @作者 做就行了！
 * @时间 2019-04-03上午 11:20
 * @该类描述： -
 * 1、多标签控件。
 * 点击标签时，选中该标签，并取消选中其他标签。
 * 2、设计思路：
 * ①就把该类设计为不可滑动的类！！！
 * ②tabs大小全部一致！！！
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
//public abstract class TabLayout<T extends View> extends UsefulViewGroup {
public abstract class TabLayout extends UsefulViewGroup {
    private static final boolean DEBUG = true;
    //////////////////////////////////////////////////

    /**
     * 记录被选中的tab。
     * <p>
     * 友情提示：{@link #onLayoutOptimize()}中首次被赋值。
     */
    private TabInfo selectedTabInfo;

    /**
     * fix：该变量有点蠢，目前的作用是：记录用户选中了哪个tab。
     */
    private int selectedTabDex = -1;

    /**
     * 存放TabInfo的集合类。
     */
    private List<TabInfo> tabs = new ArrayList<>();
    //////////////////////////////////////////////////
    /*new：2019年4月22日22:39:42*/
    /**
     * 标记：TabLayout是否初始化。
     */
    private boolean init;
    /**
     * 标记：用户是否请求布局。
     */
    private boolean requestLayout;
    /**
     * 标记：是否由自己处理触摸事件。
     */
    private boolean selfEvent;
    /**
     * 标记：此次触摸事件是否消费了down事件。
     *
     * 友情提示：
     * 1、只有消费了down事件才能执行move、up事件。
     * 2、防止熄屏时触摸，之后开启屏幕的滑动。
     */
    private boolean needDownEvent = true;
    //////////////////////////////////////////////////

    /**
     * 构造方法。
     *
     * @param context 上下文。
     */
    public TabLayout(Context context) {
        super(context);
    }
    //////////////////////////////////////////////////

    /**
     * 设置适配器。
     * TODO: 2019年4月23日23:01:26。{@link Adapter}该类需要完善。
     *
     * @param adapter {@link Adapter}
     */
    public void setAdapter(Adapter adapter) {
        if (this.adapter != null) {
            throw new IllegalStateException("已经设置了adapter，不能重复设置。");
        }
        this.adapter = adapter;
    }

    //////////////////////////////////////////////////
    /*获取区域。*/

    /**
     * 获取指定下标的tab。
     *
     * @param dex tab的下标。
     * @return 指定下标的tab。
     */
    public View getTab(int dex) {
        return getTabInfo(dex).tab;
    }

    /**
     * 获取指定下标的tabInfo。
     *
     * @param dex tab的下标。
     * @return 指定下标的tabInfo。
     */
    public TabInfo getTabInfo(int dex) {
        return tabs.get(dex);
    }

    /**
     * 获取被选中的tab。
     */
    public TabInfo getSelectedTabInfo() {
        return selectedTabInfo;
    }

    //////////////////////////////////////////////////

    /**
     * 请求重新布局tabs。
     */
    public void requestLayout() {
        super.requestLayout();
        requestLayout = true;
    }

    /**
     * 1、onSize中重新布局后，将标记请求的变量赋值为false。
     * 2、请求布局时、容器size改变时，都需要重新布局 ~ ！！！
     */
    private Adapter adapter;
    WH tabWH = new WH();
    XY startXY = new XY();

    @Override
    protected void onSizeChangedSafely(int w, int h) {
        super.onSizeChangedSafely(w, h);
        requestLayout = false;
        if (!init) {//fix 下方代码中还引用此变量。
            adapter.setTabLayout(this);
            onInit();
        }
        adapter.onSizeChangedSafely(w, h);
        onLayoutOptimize();
    }

    /**
     * 警告：子父类！！！都在该方法中执行初始化工作！！！
     */
    protected void onInit() {
        int tabCount = adapter.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            View view = adapter.onCreateTabView(i, getContext());
            addView(view);
            tabs.add(new TabInfo(view, i));
        }
    }

    /**
     * 警告：子父类都在该方法中布局！！！
     */
    @SuppressWarnings("unchecked")
    protected void onLayoutOptimize() {
        tabWH.set(adapter.getTabWH());
        startXY.set(adapter.getLayoutStartXY());
        Orien2 orien = adapter.getOrien();
        switch (orien) {
            case HORIZONTAL:
                layoutTabsForHori();
                break;
            case VERTICAL:
                layoutTabsForVerti();
                break;
        }

        if (!init) {
            init = true;
            if (selectedTabDex == -1) {
                selectedTabDex = 0;
            }
            selectedTabInfo = tabs.get(selectedTabDex);
            ViewUT.setSelectedEX(selectedTabInfo.tab, true);
            adapter.onSelectedTab(selectedTabInfo.tab, selectedTabDex);
        }
    }

    private void layoutTabsForVerti() {
        int usedHeight = (int) startXY.y;
        for (int i = 0; i < tabs.size(); i++) {
            TabInfo tabInfo = tabs.get(i);
            View tab = tabInfo.tab;
            ViewUT.adjustSize(tab, tabWH.w, tabWH.h);
            measureChild(tab);
            int left = (int) startXY.x;
            int right = left + tabWH.w;
            int top = usedHeight + adapter.getSpaceBefore(i);
            int bottom = top + tabWH.h;
            tab.layout(left, top, right, bottom);
            tabInfo.set(left, top, right, bottom);
            usedHeight = bottom;
            adapter.onLayoutTabFinish(tab, i);
        }
    }

    private void layoutTabsForHori() {
        int usedWidth = (int) startXY.x;
        for (int i = 0; i < tabs.size(); i++) {
            TabInfo tabInfo = tabs.get(i);
            View tab = tabInfo.tab;
            ViewUT.adjustSize(tab, tabWH.w, tabWH.h);
            measureChild(tab);
            int left = usedWidth + adapter.getSpaceBefore(i);
            int right = left + tabWH.w;
            int top = (int) startXY.y;
            int bottom = top + tabWH.h;
            tab.layout(left, top, right, bottom);
            tabInfo.set(left, top, right, bottom);
            usedWidth = right;
            adapter.onLayoutTabFinish(tab, i);
        }
    }
    //--------------------------------------------------

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //容器大小没改变，但是用户请求重新布局了。
        if (requestLayout) {
            requestLayout = false;
            onLayoutOptimize();
        }
    }

    //////////////////////////////////////////////////
    @SuppressWarnings("WeakerAccess")
    public static abstract class Adapter<T extends View> {
        TabLayout tabLayout;

        public void setTabLayout(TabLayout tabLayout) {
            this.tabLayout = tabLayout;
        }

        public TabLayout getTabLayout() {
            return tabLayout;
        }

        public Context getContext() {
            return getTabLayout().getContext();
        }
        //////////////////////////////////////////////////

        public abstract void onSizeChangedSafely(int w, int h);

        public void onSelectedChanged(int newTabDex, int oldTabDex, boolean fromTouch) {

        }

        public abstract void onSelectedTab(T t, int dex);

        public abstract int getTabCount();

        public abstract T onCreateTabView(int dex, Context context);

        public abstract WH getTabWH();

        //////////////////////////////////////////////////
        public int getSpaceBefore(int dex) {
            return 0;
        }

        public XY getLayoutStartXY() {
            return new XY();
        }

        public Orien2 getOrien() {
            return Orien2.HORIZONTAL;
        }

        public void onLayoutTabFinish(T tab, int dex) {

        }
    }

    //////////////////////////////////////////////////


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

        if (hasWindowFocus) {
            needDownEvent = true;
        }
    }

    /**
     * down事件时，点击到的tabView控件。
     */
    private int downTabDex;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean b = super.dispatchTouchEvent(ev);
        int actionMasked = ev.getActionMasked();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                if (DEBUG) {
                    Log.e("TAG", getClass() + "#dispatchTouchEvent,ACTION_DOWN:" +
                            " 事件是否被消费：" + b + " 。");
                }
                needDownEvent = false;
                if (!b) {
                    b = true;
                    selfEvent = true;
                    downTabDex = getTabPositionByEv(ev);
                } else {
                    selfEvent = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (DEBUG) {
                    Log.e("TAG", getClass() + "#dispatchTouchEvent,ACTION_UP:" +
                            " 事件是否被消费：" + b + " 。");
                }
                if (needDownEvent) {
                    return b;
                }
                if (selfEvent && downTabDex != -1) {
                    View tab = tabs.get(downTabDex).tab;
                    tab.getHitRect(reuse);
                    if (reuse.contains(((int) ev.getX()), (int) ev.getY())) {
                        if (DEBUG) {
                            Log.e("TAG", getClass() + "#dispatchTouchEvent ACTION_UP:" +
                                    " containsDownTabDex = " + downTabDex + " 。");
                        }
                        //todo 触发了selected。
                        onSelected(tab, downTabDex, true);
                    }
                }
                break;
        }
        return b;
    }

    private void onSelected(View tab, int dex, boolean fromTouch) {
        setSelectedTabInner(dex, fromTouch);
    }

    /**
     * 复用的rect。
     * <p>
     * 1、获取tab的hitRect。
     * 2、
     */
    Rect reuse = new Rect();

    /**
     * 通过触摸事件，获取点击的tab。
     *
     * @return 点击到的tab的下标。
     */
    private int getTabPositionByEv(MotionEvent ev) {
        for (int i = 0; i < tabs.size(); i++) {
            View tab = tabs.get(i).tab;
            tab.getHitRect(reuse);
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            if (DEBUG) {
                Log.e("TAG", getClass() + "#getTabPositionByEv,tab+" + i +
                        ": " + "hitRect = " + reuse + " 。" +
                        "ev.x = " + x +
                        ", ev.y = " + y);
            }
            if (reuse.contains(x, y)) {
                if (DEBUG) {
                    Log.e("TAG", getClass() + "#getTabPositionByEv:" +
                            " 找到了被点击的tab，dex = " + i + " 。");
                }
                return i;
            }
        }
        return -1;
    }


    /**
     * tab被点击时的处理方法。
     *
     * @param dex tab的下标。
     */
    private void onClick(int dex) {
        setSelectedTabInner(dex, true);
    }

    /**
     * 将指定下标的tab设置为选中状态。
     *
     * @param dex 将该下标的tab设置为选中状态。
     */
    public void setSelectedTabInfo(int dex) {
        setSelectedTabInner(dex, false);
    }

    /**
     * 将指定下标的tab设置为选中状态。
     *
     * @param dex       将该下标的tab设置为选中状态。
     * @param fromTouch true：此次操作来自用户触摸事件。
     *                  false：此次操作来自代码设置。
     */
    private void setSelectedTabInner(int dex, boolean fromTouch) {
        TabInfo newTab = tabs.get(dex);
        //选中的tab未改变，直接返回。
        if (newTab == selectedTabInfo) {
            return;
        }

        TabInfo oldTab = selectedTabInfo;
        selectedTabInfo = newTab;
        selectedTabDex = newTab.dex;

        //只有之前选中了，该值才不为null。
        ViewUT.setSelectedEX(oldTab.tab, false);
        ViewUT.setSelectedEX(newTab.tab, true);
        onSelectedChanged(newTab, oldTab, fromTouch);
        adapter.onSelectedTab(newTab.tab, dex);
        adapter.onSelectedChanged(newTab.dex, oldTab.dex, fromTouch);

    }

    /**
     * 每当重新布局结束时，会调用该方法。
     */
    protected void onLayoutFinish() {

    }

    /**
     * 选中新的tab时，会调用该方法。
     *
     * @param newTab    现在被选中的tab。
     * @param oldTab    之前被选中的tab。
     * @param fromTouch true：此次操作来自用户触摸事件。
     *                  false：此次操作来自代码设置。
     */
    protected void onSelectedChanged(TabInfo newTab, TabInfo oldTab, boolean fromTouch) {
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onSelectedChanged");
        }
    }

    //////////////////////////////////////////////////

    /**
     * 存放每个tab信息的类。
     */
    @SuppressWarnings("WeakerAccess")
    public class TabInfo {
        public View tab;
        public int dex;//tab的下标。
        public Rect rect = new Rect();//tab的rect。

        TabInfo(View tab, int dex) {
            this.tab = tab;
            this.dex = dex;
        }

        void set(int left, int top, int right, int bottom) {
            rect.set(left, top, right, bottom);
        }
    }
}

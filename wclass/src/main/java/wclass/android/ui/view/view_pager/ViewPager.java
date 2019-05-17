package wclass.android.ui.view.view_pager;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import wclass.android.ui.view.base_view.UsefulScrollViewGroup;
import wclass.android.util.debug.StringUT;
import wclass.enums.Orien2;
import wclass.ui.event_parser.MultiSingleParser;
import wclass.util.MathUT;

/**
 * @作者 做就行了！
 * @时间 2019-05-02上午 12:44
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 * todo
 * 1、添加功能：最多保留多少个item。
 */
@SuppressWarnings("unchecked")
public class ViewPager extends UsefulScrollViewGroup {
    private static final boolean DEBUG = true;
    private static final boolean DETACH_DEBUG = false;
    //////////////////////////////////////////////////

    /**
     * 上下文。
     */
    private final Context mContext;
    /**
     * 适配器类。{@link Adapter}
     */
    private Adapter mAdapter;
    /**
     * 可见的item的下标。
     */
    private int mSelectedPosition = -1;
    private int mOldPosition = -1;
    /**
     * 存放ViewGroup中的item的view。
     */
    private SparseArray<ItemInfo> items = new SparseArray<>();
    //--------------------------------------------------
    /**
     * 标记：该类是否初始化。
     */
    private boolean init = false;
    /**
     * ViewPager的宽。
     */
    private int w;
    /**
     * ViewPager的高。
     */
    private int h;
    /**
     * 开始滑动时，记录滑动值。
     */
    private int startScrollX;
    /**
     * 开始滑动时，记录X方向的最大滑动值。
     */
    private int maxScrollXAtTouchScrollStart;
    /**
     * item同时存在的数量。
     */
    private int mCacheCount;
    //////////////////////////////////////////////////

    /**
     * 构造方法。
     */
    public ViewPager(Context context, Adapter mAdapter) {
        this(context, mAdapter, 3);
    }

    /**
     * 构造方法。
     *
     * @param context    上下文。
     * @param cacheCount item同时存在的数量。
     */
    public ViewPager(Context context, Adapter adapter, int cacheCount) {
        super(context);
        mContext = context;
        mAdapter = adapter;

        mAdapter.setViewPager(this);
        setCacheCount(cacheCount);
    }
    //////////////////////////////////////////////////

    //    /**
//     * 设置适配器。
//     *
//     * @param adapter {@link Adapter}
//     */
//    public void setAdapter(Adapter adapter) {
//        if (mAdapter != null) {
//            throw new IllegalStateException("不能重复设置adapter。");
//        }
//        mAdapter = adapter;
//        adapter.setViewPager(this);
//    }
    public Adapter getAdapter() {
        return mAdapter;
    }

    /**
     * 获取主要的可见的item的下标。
     */
    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    /**
     * 获取旧的选中的item的下标。
     */
    public int getOldSelectedPosition() {
        return mOldPosition;
    }
    //--------------------------------------------------

    /**
     * 除了可见的item，将其他item从ViewGroup中删除。
     */
    public void detachOther() {
        for (int i = items.size() - 1; i >= 0; i--) {
            int key = items.keyAt(i);
            if (mSelectedPosition != key) {
                detachItem(key);
            }
        }
    }

    public void setCacheCount(int cacheCount) {
        mCacheCount = MathUT.limitMin(cacheCount, 1);
    }
    //--------------------------------------------------

    /**
     * 显示指定位置的item。
     *
     * @param position item的下标。
     */
    public void showPosition(int position) {
        if (!init) {
            setMainPosition(position);
            return;
        }
        if (attachItemChecked(position, true)) {
            int scrollX = _getScrollXForPosition(mSelectedPosition);
            setScrollX(scrollX);
            //step 直接显示时，直接取消附着。
            detach(true);
        }
    }

    /**
     * 直接显示上一个下标的item。
     */
    public void showPrePosition() {
        if (!init) {
            return;
        }
        int position = mSelectedPosition - 1;
        if (attachItemChecked(position, true)) {
            int scrollX = _getScrollXForPosition(position);
            setScrollX(scrollX);
            //step 直接显示时，直接取消附着。
            detach(true);
        }
    }

    /**
     * 直接显示下一个下标的item。
     */
    public void showNextPosition() {
        if (!init) {
            return;
        }
        int position = mSelectedPosition + 1;
        if (attachItemChecked(position, true)) {
            int scrollX = _getScrollXForPosition(position);
            setScrollX(scrollX);
            //step 直接显示时，直接取消附着。
            detach(true);
        }
    }

    /**
     * 滑动至上一个下标的item。
     */
    public void scrollToPrePosition(int duration) {
        if (!init) {
            return;
        }
        int position = mSelectedPosition - 1;
        if (attachItemChecked(position, true)) {
            int scrollX = _getScrollXForPosition(position);
            smoothScrollTo(scrollX, duration);
            /**
             * step 取消附着item在{@link #onNoTouchScroll_finish()}方法中。
             */
        }
    }

    /**
     * 滑动至下一个下标的item。
     */
    public void scrollToNextPosition(int duration) {
        if (!init) {
            return;
        }
        int position = mSelectedPosition + 1;
        if (attachItemChecked(position, true)) {
            int scrollX = _getScrollXForPosition(position);
            smoothScrollTo(scrollX, duration);
            /**
             * step 取消附着item在{@link #onNoTouchScroll_finish()}方法中。
             */
        }
    }
    //////////////////////////////////////////////////

    @Override
    protected int onLimitScrollValue(int scrollValue) {
        return MathUT.limit(scrollValue, 0, maxScrollXAtTouchScrollStart);
    }

    @Override
    protected void onTouchScroll_start(MultiSingleParser parser, MotionEvent ev) {
        super.onTouchScroll_start(parser, ev);
        //记录开始滑动时的scrollX。
        startScrollX = getScrollX();
        //记录scrollX的最大值。
        maxScrollXAtTouchScrollStart = mAdapter.getScrollXForPosition(
                mAdapter.getItemCount() - 1, this);
    }

    /**
     * 标记：true，往之后滑动。
     */
    boolean arrowToLast = true;

    @Override
    protected void onTouchScroll_finishAndDoFling(MultiSingleParser parser, VelocityTracker vt, MotionEvent ev) {
        super.onTouchScroll_finishAndDoFling(parser, vt, ev);
        int finishScrollX = getScrollX();
        arrowToLast = finishScrollX > startScrollX;
        int position = mAdapter.getAutoScrollToPosition(mSelectedPosition,
                startScrollX, finishScrollX, vt);
        if (attachItemChecked(position, true)) {
            if (DEBUG) {
                smoothScrollTo(mAdapter.getScrollXForPosition(position,
                        this), 2000);
            } else {
                smoothScrollTo(mAdapter.getScrollXForPosition(position,
                        this));
            }
        }
    }
    //--------------------------------------------------

    /**
     * todo 其实先attach再detach也挺好。
     */
    @Override
    protected void onNoTouchScroll_finish() {
        super.onNoTouchScroll_finish();
        //缓存的item的数量大于1个时，就往滑动的方向添加1个。
        if (mCacheCount > 1) {
            if (arrowToLast) {
                attachItemChecked(mSelectedPosition + 1, false);
            } else {
                attachItemChecked(mSelectedPosition - 1, false);
            }
        }

        //往之后滑动，取消附着头方向的item。
        if (arrowToLast) {
            detach(true);
        }
        //往之前滑动，取消附着尾方向的item。
        else {
            detach(false);
        }
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onNoTouchScroll_finish:" +
                    " 主item的key = " + mSelectedPosition + " 。" +
                    StringUT.toStr(items) + " 。");
        }
    }

    @Override
    protected boolean needScroll() {
        return mAdapter.needScroll();
    }
    //////////////////////////////////////////////////

    @Override
    protected void onSizeChangedSafely(int w, int h) {
        super.onSizeChangedSafely(w, h);
        this.w = w;
        this.h = h;
        mAdapter.onSizeChangedSafely(this, w, h);
        if (!init) {
            init = true;
            init();
        } else {
            for (int i = 0; i < items.size(); i++) {
                mAdapter.onRelayoutAfterSizeChanged(items.get(i).view, i, w, h);
            }
        }
    }

    public void setMainPosition(int selectedPosition) {
        mSelectedPosition = MathUT.limit(selectedPosition, 0,
                mAdapter.getItemCount() - 1);
    }

    /**
     * 初始化。
     */
    private void init() {
        if (mSelectedPosition == -1) {
            mSelectedPosition = 0;
        }
        attachItemForInit(mSelectedPosition);
        mAdapter.onSelectedPosition(mSelectedPosition);
        switch (mCacheCount) {
            case 1:
                break;
            case 2:
                attachItemForInit(mSelectedPosition + 1);
                break;
            default:
                attachItemForInit(mSelectedPosition - 1);
                attachItemForInit(mSelectedPosition + 1);
                break;
        }
    }

    /**
     * 直接将item添加至ViewGroup。
     * 初始化时使用的方法。
     *
     * @param position item的下标。
     */
    private void attachItemForInit(int position) {
        if (!verifyDex(position)) {
            return;
        }
        addItem(position);
    }

    /**
     * 将item添加至ViewGroup。
     *
     * @param position item的下标。
     */
    private void addItem(int position) {
        View view = _getView(position);
        items.put(position, new ItemInfo(view, position));
        addView(view);
        mAdapter.onMeasureItem(view, position, this);
        mAdapter.onLayoutItem(view, position, this);
    }

    /**
     * 将item添加至容器。
     * <p>
     * 友情提示：添加至容器之前，检查是否已经添加至容器。
     *
     * @param position item的下标。
     * @return true：item已经添加至容器。
     * false：item未添加至容器，item下标异常。
     */
    private boolean attachItemChecked(int position, boolean isMainPosition) {
        if (!verifyDex(position)) {
            return false;
        }
        //item不存在时，需要添加。
        if (!verifyExist(position)) {
            addItem(position);
        }
        if (isMainPosition) {
            mOldPosition = mSelectedPosition;
            mSelectedPosition = position;
            mAdapter.onSelectedPosition(mSelectedPosition);
        }
        return true;
    }

    //todo 打印集合类中每个item的position。

    private void detach(boolean fromHead) {
        detach(items.size() - mCacheCount, fromHead);
    }

    /**
     * 取消附着指定数量的item。
     *
     * @param count     取消附着的item的数量。
     * @param fromFirst true：从第一个开始取消附着。
     *                  false：从最后一个开始取消附着。
     */
    private void detach(int count, boolean fromFirst) {
        for (int i = 0; i < count; i++) {
            if (fromFirst) {
                int key = items.keyAt(0);
                if (key != mSelectedPosition) {
                    detachItem(key);
                }
                //头部第一个为主要的item。此时转为尾部取消附着。
                else {
                    fromFirst = false;
                    key = items.keyAt(items.size() - 1);
                    if (key != mSelectedPosition) {
                        detachItem(key);
                    }
                }
            }
            //从尾部取消附着。
            else {
                int key = items.keyAt(items.size() - 1);
                if (key != mSelectedPosition) {
                    detachItem(key);
                }
                //尾部最后一个为主要的item。此时转为头部取消附着。
                else {
                    fromFirst = true;
                    key = items.keyAt(0);
                    if (key != mSelectedPosition) {
                        detachItem(key);
                    }
                }

            }
        }
    }

    //--------------------------------------------------

    /**
     * 取消附着指定的item。
     *
     * @param detachPosition item的下标。
     */
    private void detachItem(int detachPosition) {
        if (DETACH_DEBUG) {
            Log.e("TAG", getClass() + "#detachItem开始:" +
                    " detachPosition = " + detachPosition + " 。" +
                    " 当前item数量为：" + items.size() + " 。");
        }
        ItemInfo itemInfo = items.get(detachPosition);
        items.remove(detachPosition);//集合类中删除item。
        removeView(itemInfo.view);//ViewGroup中删除item。

        //通知adapter。
        mAdapter.onDetachView(itemInfo.view,
                detachPosition);
        if (DETACH_DEBUG) {
            Log.e("TAG", getClass() + "#detachItem结束:" +
                    " 主item的key = " + mSelectedPosition + " 。" +
                    StringUT.toStr(items) + " 。");
        }
    }
    //--------------------------------------------------

    /**
     * 验证下标是否正常。
     *
     * @param position 下标。
     * @return true：下标正常。
     */
    private boolean verifyDex(int position) {
        return position >= 0 && position < mAdapter.getItemCount();
    }

    /**
     * 验证item是否存在。
     *
     * @param position item的下标。
     * @return true：item已经存在ViewGroup中。
     */
    private boolean verifyExist(int position) {
        return items.get(position) != null;
    }
    //--------------------------------------------------

    private int _getScrollXForPosition(int position) {
        return mAdapter.getScrollXForPosition(position, this);
    }

    private View _getView(int position) {
        return mAdapter.createView(mContext, position, w, h);
    }
    //////////////////////////////////////////////////

    class ItemInfo {
        View view;
        int position;

        ItemInfo(View view, int position) {
            this.view = view;
            this.position = position;
        }
    }

    @SuppressWarnings("WeakerAccess")
    public static abstract class Adapter<T extends View> {
        /**
         * {@link ViewPager}
         * 该变量只能在{@link Adapter}的方法中使用。
         */
        private ViewPager mViewPager;

        public final ViewPager getViewPager() {
            return mViewPager;
        }

        /**
         * 设置ViewPager。
         * 友情提示：由ViewPager类调用。
         *
         * @param viewPager viewPager。
         */
        private void setViewPager(ViewPager viewPager) {
            if (mViewPager != null) {
                throw new IllegalStateException("不能重复设置viewPager。");
            }
            mViewPager = viewPager;
        }
        //////////////////////////////////////////////////

        /**
         * 获取item的view。
         *
         * @param context  上下文。
         * @param position item的下标。
         * @param pageW    viewPager的宽。
         * @param pageH    viewPager的高。
         * @return item的view。
         */
        public abstract T createView(Context context, int position, int pageW, int pageH);


        /**
         * 当viewPager大小改变时，会收到此回调。
         *
         * @param viewPager viewPager。
         * @param w         viewPager的宽。
         * @param h         viewPager的高。
         */
        public void onSizeChangedSafely(ViewPager viewPager, int w, int h) {
        }

        /**
         * 测量item。
         *
         * @param item      测量该item。
         * @param position  item的下标。
         * @param viewPager viewPager。
         */
        public void onMeasureItem(T item, int position, ViewPager viewPager) {
            viewPager.measureChild(item);
        }

        /**
         * 布局item。
         * <p>
         * 友情提示：默认实现了一种常用的布局方式。
         *
         * @param view      item的view。
         * @param position  item的下标。
         * @param viewPager viewPager。
         */
        public void onLayoutItem(T view, int position, ViewPager viewPager) {
            int scrollX = getScrollXForPosition(position, viewPager);
            view.layout(scrollX, 0,
                    scrollX + view.getMeasuredWidth(), view.getMeasuredHeight());
        }

        /**
         * 取消附着item。
         * <p>
         * 友情提示：取消附着后，item的view就已经脱离了ViewGroup。
         *
         * @param view     item的view。
         * @param position item的下标。
         */
        public abstract void onDetachView(T view, int position);

        //////////////////////////////////////////////////
        /*domain 自己看着办吧，重写吧。*/

        /**
         * 获取item布局时所在的X方向的滑动值。
         *
         * @param position item的下标。
         * @return item的view所在的X方向的滑动值。
         */
        public int getScrollXForPosition(int position, ViewPager viewPager) {
            return position * viewPager.getWidth();
        }

        /**
         * 获取item的数量。
         */
        public abstract int getItemCount();

        /**
         * 当容器触发滑动之前，最后一次询问子view是否需要此次touchEvent。
         *
         * @param ev 事件。
         * @return true：子view需要自己处理事件。false：子view不需要处理事件。
         */
        public boolean lastAskChildNeedEventBeforeScroll(MotionEvent ev) {
            return false;
        }

        /**
         * 根据当前的滑动值，计算出需要自动滑动到的item的position。
         * <p>
         * 友情提示：默认提供了一个常用的算法。
         *
         * @param selectedPosition 滑动开始时的item的下标。
         * @param startScrollX     开始滑动时的scrollX。
         *                         友情提示：
         *                         该值有可能为，非触摸滑动中手指触摸时，
         *                         该值为此时的scrollX。
         * @param finishScrollX    结束时的scrollX。
         * @param vt               速率。
         * @return 根据当前的滑动值，计算出需要自动滑动到的item的position。
         */
        public int getAutoScrollToPosition(int selectedPosition, int startScrollX, int finishScrollX, VelocityTracker vt) {
            ViewPager viewPager = getViewPager();
            int positionX = getScrollXForPosition(selectedPosition, getViewPager());
            int cutScrollX = finishScrollX - positionX;
            //往之后。
            if (cutScrollX > 0) {
                int nextPosition = selectedPosition + 1;
                if (nextPosition >= getItemCount()) {
                    return selectedPosition;
                }
                int scrollX = getScrollXForPosition(nextPosition, viewPager);
                //先通过滑动距离判断。
                if (finishScrollX > scrollX - viewPager.getWidth() / 2) {
                    return nextPosition;
                }
                //再通过速率判断。
                else {
                    vt.computeCurrentVelocity(1000);
                    float xVelocity = vt.getXVelocity();
                    //往之后滑动时，速率是负的，需要纠正一下。
                    if (-xVelocity > viewPager.getWidth()) {
                        //触发下一页。
                        return nextPosition;
                    }
                    //没触发页滑动，执行复位滑动。
                    else {
                        return selectedPosition;
                    }
                }
            }
            //往之前。
            else {
                int lastPosition = selectedPosition - 1;
                if (lastPosition < 0) {
                    return selectedPosition;
                }
                int scrollX = getScrollXForPosition(lastPosition, getViewPager());
                //先通过滑动距离判断。
                if (finishScrollX < scrollX + viewPager.getWidth() / 2) {
                    return lastPosition;
                }
                //再通过速率判断。
                else {
                    vt.computeCurrentVelocity(1000);
                    float xVelocity = vt.getXVelocity();
                    if (xVelocity > viewPager.getWidth()) {
                        //触发上一页。
                        return lastPosition;
                    }
                    //没触发页滑动，执行复位滑动。
                    else {
                        return selectedPosition;
                    }
                }

            }
        }

        /**
         * 主要item的下标改变时的回调。
         *
         * @param mainPosition 主要item的新下标。
         */
        public void onSelectedPosition(int mainPosition) {
        }

        /**
         * ViewPager大小改变时，通知重新布局item。
         *
         * @param item     item的view。
         * @param position item的下标。
         * @param w        ViewPager的宽。
         * @param h        ViewPager的高。
         */
        public void onRelayoutAfterSizeChanged(T item, int position, int w, int h) {
        }

        /**
         * ViewPager是否需要滑动。
         */
        public boolean needScroll() {
            return true;
        }
    }

    //////////////////////////////////////////////////
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected Orien2 getScrollOrien() {
        return Orien2.HORIZONTAL;
    }
}

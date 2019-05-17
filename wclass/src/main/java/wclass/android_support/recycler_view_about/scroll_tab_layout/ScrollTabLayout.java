package wclass.android_support.recycler_view_about.scroll_tab_layout;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import wclass.android_support.recycler_view_about.RecyclerViewII;
import wclass.android_support.recycler_view_about.UsefulItemOnTouchListener;
import wclass.android.ui.view.base_view.UsefulLinearLayout;
import wclass.android.ui.view.mover_view.MoverView;
import wclass.android.util.SizeUT;
import wclass.android.util.ViewUT;
import wclass.common.WH;
import wclass.util.MathUT;

/**
 * @作者 做就行了！
 * @时间 2019-04-24下午 4:03
 * @该类描述： -
 * 1、该类为可滑动的tabLayout。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("unchecked")
public class ScrollTabLayout extends UsefulLinearLayout {
    private static final boolean DEBUG = false;
    private static final boolean ANIM_DEBUG = false;
    //////////////////////////////////////////////////
    /**
     * 上下文。
     */
    private Context context;
    /**
     * 指示器所在的容器。
     */
    private MoverView moverView;
    /**
     * tab所在的容器。
     */
    private RecyclerView recyclerView;
    /**
     * 适配器类。{@link Adapter}
     */
    private Adapter adapter;
    /**
     * 被选中的tab的下标。
     */
    private int mSelectedPosition = -1;
    /**
     * tab所在的容器的宽高。
     */
    private WH recyclerViewWH = new WH();
    /**
     * 指示器所在的容器的宽高。
     */
    private WH moverWH = new WH();
    //--------------------------------------------------
    /**
     * 标记：该类是否初始化。
     */
    private boolean init;

    /**
     * 复用rect。用于获取item的rect。
     */
    Rect reuse = new Rect();
    //////////////////////////////////////////////////

    /**
     * 构造方法。
     *
     * @param context 上下文。
     * @param adapter 适配器。
     */
    public ScrollTabLayout(Context context, Adapter adapter) {
        super(context);
        if (adapter == null) {
            throw new IllegalStateException
                    ("请先设置adapter。请调用本类方法：setAdapter() 。");
        }
        this.context = context;
        this.adapter = adapter;
        setOrientation(VERTICAL);
    }


    /**
     * 获取选中的tab的position。
     */
    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    /**
     * 将指定position设置为选中状态。
     *
     * @param newSelectedPosition 指定的position。
     */
    public void setSelectedPosition(int newSelectedPosition) {
        if (!init) {
            this.mSelectedPosition = MathUT.limit(newSelectedPosition,
                    0, adapter.getItemCount() - 1);
        } else {
            newSelectedPosition = MathUT.limit(newSelectedPosition, 0,
                    adapter.getItemCount() - 1);
            if (newSelectedPosition != mSelectedPosition) {
                onSelectedItemChanged(newSelectedPosition, mSelectedPosition);
            }
        }
    }

    /**
     * 指定的position的tab是否被选中。
     *
     * @param position 指定的position的tab。
     * @return true：该position的tab被选中了。false：反之。
     */
    public boolean isSelectedPosition(int position) {
        return position == mSelectedPosition;
    }

    /**
     * root容器有宽高时，初始化该类。
     * fix:初始化时，未通知用户tab的选中状态。
     */
    private void init() {
        if (mSelectedPosition == -1) {
            mSelectedPosition = 0;
        }

        adapter.setScrollTabLayout(this);
        moverView = onCreateMoverView(context);
        recyclerView = onCreateRecyclerView(context, adapter);
        addView(recyclerView);
        addView(moverView);
        recyclerView.addOnItemTouchListener(
                new UsefulItemOnTouchListener(recyclerView) {
                    @Override
                    protected void onClick(ViewHolder vh) {
                        super.onClick(vh);
                        int layoutPosition = vh.getLayoutPosition();
                        if (layoutPosition != mSelectedPosition) {
                            onSelectedItemChanged(vh, mSelectedPosition);
                        }
                    }
                });
        recyclerView.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        ViewHolder viewHolder = recyclerView.
                                findViewHolderForLayoutPosition(mSelectedPosition);
                        if (viewHolder != null) {
                            onMoverRerect(viewHolder);
                        } else {
                            moverView.reHori(0, 0);
                        }
                    }
                });

    }

    /**
     * tab的选中状态发生改变时，回收到此回调。
     *
     * @param selectedVh          选中的tab的holder。
     * @param oldSelectedPosition 旧的被选中的tab的下标。
     */
    @SuppressWarnings("unchecked")
    protected void onSelectedItemChanged(ViewHolder selectedVh,
                                         int oldSelectedPosition) {
        mSelectedPosition = selectedVh.getLayoutPosition();
        onMoverAnimRerect(selectedVh);
        adapter.onSelectedStateChanged(selectedVh,
                selectedVh.getLayoutPosition(), true);
        //--------------------------------------------------
        //查询之前选中的tab是否可见。
        ViewHolder vhOldPosi = recyclerView.
                findViewHolderForLayoutPosition(oldSelectedPosition);
        if (vhOldPosi != null) {
            adapter.onSelectedStateChanged(vhOldPosi, oldSelectedPosition, false);
        }
    }

    /**
     * tab的选中状态发生改变时，回收到此回调。
     *
     * @param newSelectedPosition 新的被选中的tab的下标。
     * @param oldSelectedPosition 旧的被选中的tab的下标。
     */
    protected void onSelectedItemChanged(int newSelectedPosition,
                                         int oldSelectedPosition) {
        ViewHolder vhNewPosi = recyclerView.findViewHolderForLayoutPosition(newSelectedPosition);
        ViewHolder vhOldPosi = recyclerView.findViewHolderForLayoutPosition(oldSelectedPosition);
        if (vhNewPosi != null) {
            adapter.onSelectedStateChanged(vhNewPosi, newSelectedPosition, true);
            onMoverAnimRerect(vhNewPosi);
        }
        if (vhOldPosi != null) {
            adapter.onSelectedStateChanged(vhOldPosi, oldSelectedPosition, false);
        }
    }

    /**
     * 动画改变指示器的位置。
     * <p>
     * todo 可以让子类重写。
     *
     * @param selectedVh
     */
    private void onMoverAnimRerect(ViewHolder selectedVh) {
        View itemView = selectedVh.itemView;
        itemView.getHitRect(reuse);
        moverView.prepareHori(moverViewDstRect.left, moverViewDstRect.right)
                .animReHori(reuse.left, reuse.right);
        moverViewDstRect.set(reuse);
        if (ANIM_DEBUG) {
            Log.e("TAG", getClass() + "#onMoverRerect:" +
                    " itemViewHitRect = " + reuse + " 。");
            moverView.getHitRect(reuse);
            Log.e("TAG", getClass() + "#:" +
                    " moverViewRect = " + reuse + " 。");
            Log.e("TAG", getClass() + "#:" +
                    " moverViewDrawable = " + moverView.getMoverDrawableRect());
        }
    }

    Rect moverViewDstRect = new Rect();

    /**
     * {@link RecyclerView}加载完毕后，onScroll的监听中回收到一次回调，
     * 此时可以在onScroll的方法中初始化内容。
     */
    private void onMoverRerect(ViewHolder selectedVh) {
        View itemView = selectedVh.itemView;
        itemView.getHitRect(reuse);
        moverView.reHori(reuse.left, reuse.right);
        moverViewDstRect.set(reuse);
    }

    /**
     * 当该类有大小时，进行初始化。
     */
    @Override
    protected void onSizeChangedSafely(int w, int h) {
        super.onSizeChangedSafely(w, h);
        if (!init) {
            init = true;
            init();
        }
        adapter.onSizeChangedSafely(w, h);
        recyclerViewWH.set(adapter.onAdjustRecyclerViewSize(w, h));
        moverWH.set(adapter.onAdjustMoverViewSize(w, h));

        ViewUT.adjustSize(recyclerView, recyclerViewWH.w, recyclerViewWH.h);
        ViewUT.adjustSize(moverView, moverWH.w, moverWH.h);
    }

    /**
     * 获取适配器。
     */
    public Adapter getAdapter() {
        return adapter;
    }

    /**
     * 创建tab的容器。
     */
    protected RecyclerViewII onCreateRecyclerView(Context context, Adapter adapter) {
        RecyclerViewII recyclerViewII = new RecyclerViewII(context, adapter);
        return recyclerViewII;
    }

    /**
     * 创建指示器的容器。
     */
    protected MoverView onCreateMoverView(Context context) {
        MoverView moverView = new MoverView(context);
        moverView.setMoverDrawable(new ColorDrawable(0xffff8800));
        return moverView;
    }

    /**
     * 获取tab的容器的宽高。
     */
    public WH getRecyclerViewWH() {
        return recyclerViewWH;
    }

    /**
     * 获取指示器容器的宽高。
     */
    public WH getMoverWH() {
        return moverWH;
    }

    /**
     * 适配器类。
     */
    @SuppressWarnings("WeakerAccess")
    public static abstract class Adapter<T extends ViewHolder>
            extends RecyclerViewII.Adapter<T> {
        ScrollTabLayout scrollTabLayout;//父容器。

        /**
         * 获取父容器。
         */
        public ScrollTabLayout getScrollTabLayout() {
            return scrollTabLayout;
        }

        /**
         * 父容器绑定adapter时，由父容器调用。
         *
         * @param scrollTabLayout 父容器。
         */
        public void setScrollTabLayout(ScrollTabLayout scrollTabLayout) {
            if (this.scrollTabLayout != null) throw new IllegalStateException("不能重复设置RootLayout。");
            this.scrollTabLayout = scrollTabLayout;
        }

        private Context getContext() {
            return scrollTabLayout.getContext();
        }
        //////////////////////////////////////////////////

        /**
         * 父容器大小改变时的回调。
         *
         * @param rootW 父容器的宽。
         * @param rootH 父容器的高。
         */
        public abstract void onSizeChangedSafely(int rootW, int rootH);
        //--------------------------------------------------

        /**
         * 调整item容器的大小。
         * <p>
         * 警告：默认item的容器高度为：父容器高度 减 指示器高度。
         *
         * @param rootW 父容器的宽。
         * @param rootH 父容器的高。
         * @return item容器的宽高。
         */
        public WH onAdjustRecyclerViewSize(int rootW, int rootH) {
            int height = rootH - SizeUT.getMMpixel(getContext());
            return new WH(rootW, height);
        }

        /**
         * 调整指示器的容器的大小。
         * <p>
         * 警告：默认指示器的高度为1毫米。
         *
         * @param rootW 父容器的宽。
         * @param rootH 父容器的高。
         * @return 指示器的容器的宽高。
         */
        public WH onAdjustMoverViewSize(int rootW, int rootH) {
            int mm = SizeUT.getMMpixel(getContext());
            return new WH(rootW, mm);
        }
        //////////////////////////////////////////////////

        /**
         * 创建{@link ViewHolder}。
         *
         * @param recyclerViewWidth  tab的容器的宽。
         * @param recyclerViewHeight tab的容器的高。
         * @return tab的viewHolder。
         */
        public abstract T onCreateTabHolder(Context context, int recyclerViewWidth,
                                            int recyclerViewHeight);

        /**
         * 将tab的holder与position绑定。
         *  @param holder   tab的holder。
         * @param position tab所在的下标。
         * @param selected 该tab是否被选中。
         * @param parent
         */
        public abstract void onBindTabHolder(T holder, int position, boolean selected, RecyclerView parent);
        //--------------------------------------------------

        /**
         * 当tab的选中状态发生改变时，回收到此回调。
         *
         * @param holder   tab的holder。
         * @param position tab所在的position。
         * @param selected tab是否被选中。
         */
        public void onSelectedStateChanged(T holder, int position, boolean selected) {
            if (DEBUG) {
                Log.e("TAG",getClass()+"#onSelectedStateChanged:" +
                        " position"+position+"是否选中："+selected+" 。");
            }
        }

        //////////////////////////////////////////////////

        /**
         * 给子类提供的便捷的方法。
         * <p>
         * 警告：如果使用该方法，请重写{@link #onCreateTabView(Context, int, int)}方法。
         *
         * @param recyclerViewWidth  tab所在容器的宽。
         * @param recyclerViewHeight tab所在容器的高。
         * @return 创建的默认的viewHolder。
         */
        protected final ViewHolder tabHolder(Context context, int recyclerViewWidth, int recyclerViewHeight) {
            return new ViewHolder(
                    onCreateTabView(context, recyclerViewWidth, recyclerViewHeight)
            ) {
            };
        }

        /**
         * 通过该方法创建tabView。
         * <p>
         * 该方法配合{@link Adapter#tabHolder(Context, int, int)}使用。
         *
         * @return tabView。
         */
        protected View onCreateTabView(Context context, int recyclerViewWidth, int recyclerViewHeight) {
            throw new IllegalStateException("请重写该方法");
        }

        @NonNull
        @Override
        public final T onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ScrollTabLayout stl = getScrollTabLayout();
            WH rWH = stl.getRecyclerViewWH();
            return onCreateTabHolder(getContext(), rWH.w, rWH.h);
        }

        @Override
        public final void onBindViewHolder(@NonNull T holder, int position) {
            ScrollTabLayout stl = getScrollTabLayout();
            onBindTabHolder(holder, position,
                    stl.isSelectedPosition(position), stl.getRecyclerView());
        }
    }
    public MoverView getMoverView(){
        return moverView;
    }
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
}

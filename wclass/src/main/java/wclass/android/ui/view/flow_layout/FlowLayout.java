package wclass.android.ui.view.flow_layout;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import wclass.common.Holder;
import wclass.android.ui.view.base_view.UsefulViewGroup;


/**
 * 加油吧，你是最最最最有个性的 ~ ！！！
 *
 * @作者 做就行了！
 * @时间 2019-04-09下午 8:46
 * @该类描述： -
 * 1、从第一行开始放，放不下了换行继续放。
 * 2、该类的子类{@link AnimFlowLayout}
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * 1、添加item：
 * {@link #addItem(View)}
 * 2、将item插入两个位置之间：
 * {@link #insertItem(View, int)}
 * 3、删除item：
 * {@link #removeItem(View)}{@link #removeItem(int)}{@link #removeItems(List)}
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("DanglingJavadoc")
public class FlowLayout extends UsefulViewGroup {
    private static final boolean DEBUG = false;
    private static final boolean ITEM_RECT_DEBUG = true;
    //////////////////////////////////////////////////
    /**
     * 标记是否布局完毕。
     */
    private boolean layouted;
    /**
     * 存放所有item的集合类。
     * <p>
     * 警告：
     * 1、该类只布局该集合类中的items！！！
     * 2、只有该类布局完毕时（显示在屏幕中时），才能使用集合类中的item信息类！！！
     */
    private List<ItemHolder> itemHolders = new ArrayList<>();
    //////////////////////////////////////////////////

    /**
     * @param context
     */
    public FlowLayout(Context context) {
        super(context);
    }

    /**
     * 获取item的总数量。
     */
    public int getItemCount() {
        return itemHolders.size();
    }

    /**
     * 获取所有item的holder。
     * <p>
     * 警告：
     * 1、该类只布局该集合类中的items！！！
     * 2、只有该类布局完毕时（显示在屏幕中时），才能使用集合类中的item信息类！！！
     */
    public List<ItemHolder> getItemHolders() {
        return itemHolders;
    }

    /**
     * 添加item。
     *
     * @param item 该item。
     */
    public void addItem(View item) {
        if (DEBUG) {
            Log.e("TAG", getClass() + "#addItem(View item)添加前:" +
                    " holderSize = " + itemHolders.size() + " 。");
        }
        addView(item);//添加至viewGroup。
        optimizeAddInList(item);//添加至本类的集合类中。
        int position = itemHolders.size() - 1;
        /**
         * 更新每个item的参数信息。
         * 友情提示：控件显示时才能更新，不然跳过。
         */
        updateItemParams(position);
        //已经布局了，直接布局新的item。
        if (layouted) {
            ItemHolder holder = itemHolders.get(position);
            holder.layout();
        }
        onAddLastItem(position, layouted);
        if (DEBUG) {
            Log.e("TAG", getClass() + "#addItem(View item)添加后:" +
                    " holderSize = " + itemHolders.size() + " 。");
        }
    }

    /**
     * 将item插入指定下标。
     *
     * @param item     新的item。
     * @param position 该item的下标。
     */
    public void insertItem(View item, int position) {
        if (DEBUG) {
            Log.e("TAG", getClass() + "#insertItem:" +
                    " position = " + position + " 。");
            Log.e("TAG", getClass() + "#insertItem(View item, int position)插入前:" +
                    " holderSize = " + itemHolders.size() + " 。");
        }
        addView(item);//添加至viewGroup。
        optimizeAddInList(item, position);//添加至本类的集合类中。
        updateItemParams(position);

        //已经布局了，直接布局新的item。
        if (layouted) {
            ItemHolder holder = itemHolders.get(position);
            holder.layout();
        }
        onInsertItem(position, layouted);
        if (DEBUG) {
            Log.e("TAG", getClass() + "#insertItem(View item, int position)插入后:" +
                    " holderSize = " + itemHolders.size() + " 。");
        }
    }

    /**
     * 删除指定下标的item。
     *
     * @param item 待删除的item的view。
     */
    public void removeItem(View item) {
        int position = removeInHolders(item);
        if (DEBUG) {
            Log.e("TAG", getClass() + "#removeItem(View item):" +
                    " position = " + position + " 。");
            Log.e("TAG", getClass() + "#removeItem(View item)删除前:" +
                    " holderSize = " + itemHolders.size() + " 。");
        }
        //找到了待删除的item。
        if (position != -1) {
            updateItemParams(position);
            onRemoveItem(item, position, layouted);
        }
        if (DEBUG) {
            Log.e("TAG", getClass() + "#removeItem(View item)删除后:" +
                    " holderSize = " + itemHolders.size() + " 。");
        }
    }

    /**
     * 删除指定下标的item。
     *
     * @param position 待删除的item的下标。
     */
    public void removeItem(int position) {
        if (DEBUG) {
            Log.e("TAG", getClass() + "#removeItem(int position):" +
                    " position = " + position + " 。");
            Log.e("TAG", getClass() + "#removeItem(int position)删除前:" +
                    " holderSize = " + itemHolders.size() + " 。");
        }
        ItemHolder holder = itemHolders.remove(position);
        //找到了待删除的item。
        if (holder != null) {
            updateItemParams(position);
            onRemoveItem(holder.view, position, layouted);
        }
        if (DEBUG) {
            Log.e("TAG", getClass() + "#removeItem(int position)删除后:" +
                    " holderSize = " + itemHolders.size() + " 。");
        }

    }

    /**
     * 删除集合类中的items。
     *
     * @param removes 待删除的集合类中的items。
     */
    public void removeItems(List<View> removes) {
        if (removes == null || removes.size() == 0) {
            return;
        }
        //从ViewGroup中删除。
        for (int i = 0; i < removes.size(); i++) {
            View remove = removes.get(i);
            removeView(remove);
        }
        //从集合类中删除
        int minDex = removeInHolders(removes);
        //找到了被删除的item中，最小下标的item的下标。
        if (minDex != -1) {
            updateItemParams(minDex);
            onRemoveItems(removes, minDex, layouted);
        } else {
            Log.e("TAG", getClass() + "#removeItems:" +
                    " 错误：未找到待删除的最小dex的item。 ");
        }
    }
    //--------------------------------------------------
    /*处理新的item。*/

    /**
     * 优化添加新的item至本类的集合类中。
     * {@link #toHolder(View)}
     *
     * @param newItem  新的item。
     * @param position 该item的下标。
     */
    private void optimizeAddInList(View newItem, int position) {
        ItemHolder holder = toHolder(newItem);
        itemHolders.add(position, holder);
    }

    /**
     * 优化添加新的item至本类的集合类中。
     * {@link #toHolder(View)}
     *
     * @param newItem 新的item。
     */
    private void optimizeAddInList(View newItem) {
        ItemHolder holder = toHolder(newItem);
        itemHolders.add(holder);
    }

    /**
     * 将新的item放入holder中。
     *
     * @param newItem 新的item。
     * @return holder。
     */
    private ItemHolder toHolder(View newItem) {
        ItemHolder holder;
        //已经布局完毕，直接测量子view。
        if (layouted) {
            measureChild(newItem);
            holder = new ItemHolder(newItem, newItem.getMeasuredWidth()
                    , newItem.getMeasuredHeight()
                    , newItem.getLayoutParams());
        }
        //控件还没有显示。
        else {
            holder = new ItemHolder(newItem);
        }
        return holder;
    }
    //////////////////////////////////////////////////

    /**
     * 添加最后一个item时会被调用。
     * 友情提示：已经布局完毕时，可通过方法获取item信息{@link #getItemHolders()}。
     *
     * @param position 新的item的下标。
     * @param layouted true：已经布局完毕。
     *                 false：还没有布局。
     */
    protected void onAddLastItem(int position, boolean layouted) {
    }

    /**
     * 插入一个item时会被调用。
     * 友情提示：已经布局完毕时，可通过方法获取item信息{@link #getItemHolders()}。
     *
     * @param position 新的item的下标。
     * @param layouted true：已经布局完毕。
     *                 false：还没有布局。
     */
    protected void onInsertItem(int position, boolean layouted) {
        if (!layouted) {
            return;
        }

        //直接布局。
        for (int i = position + 1; i < itemHolders.size(); i++) {
            ItemHolder holder = itemHolders.get(i);
            holder.layout();
        }
    }

    /**
     * 删除item时会被调用。
     * <p>
     * 警告：必须在该方法中{@link #removeView}。
     *
     * @param pendingRemove 待删除的item。
     * @param position      该item的位置。
     * @param layouted      true：已经布局完毕。
     *                      false：还没有布局。
     */
    protected void onRemoveItem(View pendingRemove, int position, boolean layouted) {
        if (!layouted) {
            return;
        }
        removeView(pendingRemove);

        //直接布局。
        for (int i = position; i < itemHolders.size(); i++) {
            ItemHolder holder = itemHolders.get(i);
            holder.layout();
        }
    }

    /**
     * 删除items时会被调用。
     * <p>
     * 警告：必须在该方法中通过{@link #removeView}方法删除pendingRemoves。
     *
     * @param pendingRemoves 待删除的items。
     *                       警告：这里用的是用户传入的集合类，
     *                       如果有什么问题，是用户自己的事 ~！！！
     * @param minDex         下标最小的item的下标。
     * @param layouted       true：已经布局完毕。
     *                       false：还没有布局。
     */
    protected void onRemoveItems(List<View> pendingRemoves, int minDex, boolean layouted) {
        if (!layouted) {
            return;
        }
        //直接从ViewGroup中删除这些view。
        for (View pendingRemove : pendingRemoves) {
            removeView(pendingRemove);
        }

        //直接布局。
        for (int i = minDex; i < itemHolders.size(); i++) {
            ItemHolder holder = itemHolders.get(i);
            holder.layout();
        }
    }
    //////////////////////////////////////////////////

    /**
     * 删除items，并获取待删除的items中，最小下标的item的下标。
     *
     * @param removes 待删除的items。
     * @return 待删除的items中最小下标的item的下标。
     */
    private int removeInHolders(List<View> removes) {
        int min = -1;
        for (int j = removes.size() - 1; j >= 0; j--) {
            View remove = removes.get(j);
            for (int i = itemHolders.size() - 1; i >= 0; i--) {
                ItemHolder holder = itemHolders.get(i);
                if (holder.view == remove) {
                    //必须将-1覆盖为找到的这个item的下标，不然-1总是最小。
                    if (min == -1) {
                        min = i;
                    }
                    itemHolders.remove(i);
                    min = Math.min(i, min);
                    break;
                }
            }
        }
        return min;
    }

    /**
     * 从本类的集合类中删除。
     *
     * @param item 待删除。
     * @return 待删除的item的下标。
     */
    private int removeInHolders(View item) {
        int size = itemHolders.size();
        for (int i = 0; i < size; i++) {
            ItemHolder holder = itemHolders.get(i);
            if (holder.view == item) {
                itemHolders.remove(i);
                return i;
            }
        }
        return -1;
    }

    //////////////////////////////////////////////////

    /**
     * 每次布局前调用该方法。
     * <p>
     * 友情提示：您可以在该方法中清除一些与布局冲突的状态。
     */
    protected void preLayout() {

    }

    @SuppressWarnings("all")
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        /*确定*/

        int childCount = itemHolders.size();
        if (childCount == 0) {
            return;
        }

        //如果条件满足，则执行完全的重新布局。
        if (!changed) {
            return;
        }
        layouted = true;
        preLayout();
        measureChildrenWithMarginsSelfish();
        updateItemParams(0, true);
        if (DEBUG) {
            Log.e("TAG", " childCount = " + childCount);
        }
    }

    /**
     * {@link FlowLayout#updateItemParams(int, boolean)}
     */
    private void updateItemParams(int fromPosition) {
        updateItemParams(fromPosition, false);
    }

    /**
     * 更新每个item的参数信息。
     * 友情提示：控件显示时才能更新，不然跳过。
     *
     * @param fromPosition 从这个下标开始，往后更新。
     * @param needLayout   是否需要layout。
     */
    private void updateItemParams(int fromPosition, boolean needLayout) {
        if (!layouted || fromPosition < 0) return;
        int usableWidth = getUsableWidth();
        RowInfo rowInfo = correctRowInfoWithBefore(fromPosition - 1);
        if (ITEM_RECT_DEBUG) {
            Log.e("TAG", " firstRowInfo = " + rowInfo);
        }
        int paddingLeft = getPaddingLeft();
        for (int i = fromPosition; i < itemHolders.size(); i++) {
            ItemHolder holder = itemHolders.get(i);
            View child = holder.view;
            ViewGroup.LayoutParams p = child.getLayoutParams();
            int cw = child.getMeasuredWidth();
            int ch = child.getMeasuredHeight();

            int cLayoutWidth = getLayoutWidth(child);
            /**
             * 已使用的行宽+待布局的child的宽，大于可使用的行宽。
             * 此时应该换行。
             * 该行至少有一个item。
             */
            if (rowInfo.itemCount != 0
                    && rowInfo.usedWidth + cLayoutWidth > usableWidth) {
                if (ITEM_RECT_DEBUG) {
                    Log.e("TAG", " rowInfo = " + rowInfo.toString());
                }
                rowInfo = new RowInfo(rowInfo.rowDex + 1,
                        rowInfo.rowBottom());
                rowInfo.usedWidth = paddingLeft;
            }
            //fix 这里没布局。
//            child.layout(0, 0, cw, ch);
            //step1
            holder.re(cw, ch, p, rowInfo.usedWidth, rowInfo.rowTop);
            //step2
            updateRowInfoByAdd(rowInfo, holder);
            //step3
            holder.rowInfo = rowInfo;
            itemHolders.set(i, holder);
            if (needLayout) {
                holder.layout();
            }
            if (ITEM_RECT_DEBUG) {
                Log.e("TAG", " holder" + i +
                        " = " + holder);
            }
        }
        if (ITEM_RECT_DEBUG) {
            Log.e("TAG", " rowInfo = " + rowInfo.toString());
        }
    }

    /**
     * 添加每一行添加item后，更新行信息类。
     *
     * @param rowInfo {@link RowInfo}。
     * @param holder  新添加的item。
     */
    private void updateRowInfoByAdd(RowInfo rowInfo, ItemHolder holder) {
        rowInfo.itemCount++;
        rowInfo.rowH = Math.max(rowInfo.rowH, holder.layH());//取该行最高的child的高。
        rowInfo.usedWidth = holder.layRight();
    }

    /**
     * 从指定item开始，往之前找相同行的item，并更新行信息{@link RowInfo}。
     *
     * @param fromPosition 从指定item开始，往之前找。
     * @return 行信息类。
     */
    private RowInfo correctRowInfoWithBefore(int fromPosition) {
        //下标不正常，创建默认的rowInfo。
        if (fromPosition < 0) {
            RowInfo rowInfo = new RowInfo(0, getPaddingTop());
            rowInfo.usedWidth = getPaddingLeft();
            return rowInfo;
        }
        //往之前寻找与该item在同一行的item。
        ItemHolder holder = itemHolders.get(fromPosition);
        RowInfo rowInfo = holder.rowInfo;

        int maxHeight = 0;//该行的高度。取决于最高的那一个item的高度。
        int count = 1;//该行的item的数量。数量中包含请求的position。

        //往之前找。
        for (int i = fromPosition; i >= 0; i--) {
            ItemHolder holderI = itemHolders.get(i);
            RowInfo rowInfoI = holderI.rowInfo;
            //item都在同一行时，高度取最大。
            if (rowInfoI == rowInfo) {
                //取高度最高的那一个item的高度。
                maxHeight = Math.max(maxHeight, holderI.layH());
                count++;
            }
            //item不再同一行，找完了。
            else {
                break;
            }
        }
        rowInfo.itemCount = count;
        rowInfo.rowH = maxHeight;
        rowInfo.usedWidth = holder.layRight();
        return rowInfo;
    }

    //////////////////////////////////////////////////

    /**
     * 该类用途：记录每个item的信息。
     */
    @SuppressWarnings("WeakerAccess")
    public class ItemHolder extends Holder {
        /**
         * item所在行的行信息。
         */
        RowInfo rowInfo;

        public ItemHolder(View view) {
            super(view);
        }

        public ItemHolder(View view, int viewW, int viewH, ViewGroup.LayoutParams p1) {
            super(view, viewW, viewH, p1);
        }

        public ItemHolder(View view, int viewW, int viewH, ViewGroup.LayoutParams p1, int layX, int layY) {
            super(view, viewW, viewH, p1, layX, layY);
        }
    }

    /**
     * 每个item所在行的行信息。
     */
    @SuppressWarnings("WeakerAccess")
    public class RowInfo {
        final int rowDex;//行所在总行数中的下标。
        final int rowTop;//该行的top所在坐标。
        /**
         * 行高。
         * （最高的那个item，包含item的topMargin和bottomMargin。）
         */
        public int rowH;
        public int itemCount;//该行item数量。
        public int usedWidth;//该行已使用的长度。（包含父容器的leftPadding。）

        public RowInfo(int rowDex1, int rowTop1) {
            rowDex = rowDex1;
            rowTop = rowTop1;
        }

        /**
         * 获取行的bottom坐标。
         * <p>
         * 友情提示：该属性也是下一行的top坐标。
         */
        public int rowBottom() {
            return rowTop + rowH;
        }

        @Override
        public String toString() {
            String s = "rowDex = " + rowDex + "" +
                    ", rowTop = " + rowTop +
                    ", rowH = " + rowH +
                    ", itemCount = " + itemCount +
                    ", usedWidth = " + usedWidth + " 。";

            return s;
        }
    }

}

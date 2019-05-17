package wclass.android.ui.view.flow_layout;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.util.List;

import wclass.android.encapsulation.anims_helper.AnimsHelper;
import wclass.android.encapsulation.anims_helper.listeners.AlphaListener;
import wclass.android.encapsulation.anims_helper.listeners.XYListener;

/**
 * @作者 做就行了！
 * @时间 2019-04-12下午 11:49
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class AnimFlowLayout extends FlowLayout {
    private static final boolean DEBUG = false;

    /**
     * 动画辅助类。
     * <p>
     * 友情提示：超实用哦 ~
     */
    private AnimsHelper animsHelper = new AnimsHelper();

    /**
     * 构造方法。
     */
    public AnimFlowLayout(Context context) {
        super(context);
    }

    protected void preLayout() {
        if (DEBUG) {
            Log.e("TAG", getClass() + "#preLayout");
        }
        animsHelper.cleanAnim();
    }

    @Override
    protected void onAddLastItem(int position, boolean layouted) {
        if (!layouted) return;
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onAddLastItem  ");
        }
        ItemHolder holder = getItemHolders().get(position);
        View item = holder.view;
        animsHelper.alphaIn(item, new AlphaListener(1));
    }

    @Override
    protected void onInsertItem(int position, boolean layouted) {
        if (!layouted) return;
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onInsertItem  ");
        }
        List<ItemHolder> holders = getItemHolders();
        ItemHolder holder = holders.get(position);
        View item = holder.view;
        animsHelper.alphaIn(item, new AlphaListener(1));
        for (int i = position + 1; i < holders.size(); i++) {
            holder = holders.get(i);
            item = holder.view;
            animsHelper.xy(item, holder.viewX(), holder.viewY(),
                    new XYListener(holder.viewX(), holder.viewY()));
        }
    }

    @Override
    protected void onRemoveItem(View pendingRemove, int position, boolean layouted) {
        if (!layouted) return;
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onRemoveItem  ");
        }
        //直接从ViewGroup中删除view。
        removeView(pendingRemove);
        List<ItemHolder> holders = getItemHolders();
        for (int i = position; i < holders.size(); i++) {
            ItemHolder holder = holders.get(i);
            View item = holder.view;
            animsHelper.xy(item, holder.viewX(), holder.viewY(),
                    new XYListener(holder.viewX(), holder.viewY()));
        }

    }

    @Override
    protected void onRemoveItems(List<View> pendingRemoves, int minDex, boolean layouted) {
        if (!layouted) return;
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onRemoveItems  ");
        }
        //直接从ViewGroup中删除views。
        for (View pendingRemove : pendingRemoves) {
            removeView(pendingRemove);
        }

        //后边的往前移动。
        List<ItemHolder> holders = getItemHolders();
        for (int i = minDex; i < holders.size(); i++) {
            ItemHolder holder = holders.get(i);
            View item = holder.view;
            animsHelper.xy(item, holder.viewX(), holder.viewY(),
                    new XYListener(holder.viewX(), holder.viewY()));
        }
    }
}

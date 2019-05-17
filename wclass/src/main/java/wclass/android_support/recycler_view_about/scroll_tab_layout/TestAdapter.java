package wclass.android_support.recycler_view_about.scroll_tab_layout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import wclass.android.util.ViewUT;

/**
 * @作者 做就行了！
 * @时间 2019/4/25 0025
 * @使用说明：
 */
public class TestAdapter extends ScrollTabLayout.Adapter {

    private static final boolean DEBUG = true;
    private int count;
    private int rootW;
    private int rootH;

    public TestAdapter(int count) {
        this.count = count;
    }

    @Override
    public void onSizeChangedSafely(int rootW, int rootH) {
        this.rootW = rootW;
        this.rootH = rootH;
    }

    @Override
    public RecyclerView.ViewHolder onCreateTabHolder(Context context, int recyclerViewWidth, int recyclerViewHeight) {
        return tabHolder(context, recyclerViewWidth, recyclerViewHeight);
    }

    @Override
    public void onBindTabHolder(RecyclerView.ViewHolder holder, int position, boolean selected,RecyclerView parent) {
        String s = selected ? "选中" : "未选中";
        ((TextView) holder.itemView).setText(s + position);
    }

    @Override
    public int getItemCount() {
        return count;
    }

    @Override
    public View onCreateTabView(Context context, int recyclerViewWidth,
                                int recyclerViewHeight) {
        TextView tv = new TextView(context);
        ViewUT.adjustSize(tv, 100, 90);
        return tv;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }
}

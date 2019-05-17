package wclass.android_support.recycler_view_about.item_decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @作者 做就行了！
 * @时间 2019-04-18下午 3:25
 * @该类描述： -
 * 1、{@link RecyclerView}中，模拟item的外边距。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class ItemDecorationMockMargin extends RecyclerView.ItemDecoration {

    private int asMargin;

    public ItemDecorationMockMargin(int asMargin) {
        if (asMargin < 0) {
            throw new IllegalArgumentException();
        }
        this.asMargin = asMargin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(asMargin, asMargin, asMargin, asMargin);
    }
}

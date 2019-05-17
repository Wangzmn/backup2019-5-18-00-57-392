package wclass.android_support.recycler_view_about.item_decoration;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @作者 做就行了！
 * @时间 2019-04-18下午 3:51
 * @该类描述： -
 * 1、横向{@link GridLayoutManager}中使用，
 * 2、只有item之间有间距。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class GridItemDecorationOnlyGap_Hori extends RecyclerView.ItemDecoration {
    private int rows;
    private int spaceBetweenItems;

    public GridItemDecorationOnlyGap_Hori(int rows, int spaceBetweenItems) {
        if (rows < 1) {
            throw new IllegalArgumentException();
        }
        if (spaceBetweenItems < 0) {
            throw new IllegalArgumentException();
        }
        this.rows = rows;
        this.spaceBetweenItems = spaceBetweenItems;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemPosition = parent.getChildLayoutPosition(view);
        int itemCount = parent.getAdapter().getItemCount();
        int lastColumnFirstPosition = itemCount - (itemCount % rows);

        //第一列，left=0。
        if (itemPosition < rows) {
            outRect.left = 0;
        } else {
            outRect.left = spaceBetweenItems;
        }

        //最后一列，right=0。
        if (itemPosition >= lastColumnFirstPosition) {
            outRect.right = 0;
        } else {
            outRect.right = spaceBetweenItems;
        }

        //第一行，top=0。
        if (itemPosition % rows == 0) {
            outRect.top = 0;
        } else {
            outRect.top = spaceBetweenItems;
        }

        //最后一行，bottom=0。
        if (itemPosition % rows == rows - 1) {
            outRect.bottom = 0;
        } else {
            outRect.bottom = spaceBetweenItems;
        }
    }
}

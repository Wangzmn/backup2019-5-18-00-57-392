package wclass.android_support.recycler_view_about.item_decoration;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @作者 做就行了！
 * @时间 2019-04-18下午 3:58
 * @该类描述： -
 * 1、纵向{@link GridLayoutManager}中使用。
 * 2、只有item之间有间距。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class GridItemDecorationOnlyGap_Verti extends RecyclerView.ItemDecoration {

    private final int columns;
    private final int spaceBetweenItems;

    public GridItemDecorationOnlyGap_Verti(int columns, int spaceBetweenItems) {
        if (columns < 1) {
            throw new IllegalArgumentException();
        }
        if (spaceBetweenItems < 0) {
            throw new IllegalArgumentException();
        }
        this.columns = columns;
        this.spaceBetweenItems = spaceBetweenItems;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int itemPosition = parent.getChildLayoutPosition(view);
        int itemCount = parent.getAdapter().getItemCount();
        int lastRowFirstPosition = itemCount - (itemCount % columns);
        //第一行，top=0。
        if(itemPosition<columns){
            outRect.top = 0;
        }else{
            outRect.top = spaceBetweenItems;
        }

        //最后一行，bottom=0。
        if(itemPosition>=lastRowFirstPosition){
            outRect.bottom = 0;
        }else{
            outRect.bottom = spaceBetweenItems;

        }

        //第一列，left=0。
        if(itemPosition%columns == 0){
            outRect.left = 0;
        }else{
            outRect.left = spaceBetweenItems;

        }

        //最后一列，right=0。
        if(itemPosition%columns == columns-1){
            outRect.right = 0;
        }else{
            outRect.right = spaceBetweenItems;

        }
    }
}

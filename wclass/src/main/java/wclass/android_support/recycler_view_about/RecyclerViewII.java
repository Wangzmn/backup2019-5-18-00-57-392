package wclass.android_support.recycler_view_about;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import wclass.enums.Orien2;

/**
 * @作者 做就行了！
 * @时间 2019-04-18下午 1:33
 * @该类描述： -
 * 1、该类为{@link RecyclerView}简单的封装。
 * 2、该类只适合简单的列表显示。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public  class RecyclerViewII extends RecyclerView {
    private final RecyclerView.Adapter adapter;

    public RecyclerViewII(Context context, Adapter adapter) {
        super(context);
        this.adapter = adapter;
        setLayoutManager(adapter.getLayoutManager(context));
        setAdapter(adapter);
    }

    //////////////////////////////////////////////////
    @SuppressWarnings("WeakerAccess")
    public static abstract class Adapter<T extends ViewHolder> extends RecyclerView.Adapter<T> {

        public LayoutManager getLayoutManager(Context context) {
            return new LinearLayoutManager(context,
                    LinearLayoutManager.HORIZONTAL, false);
        }

        public Orien2 getOrien() {
            return Orien2.HORIZONTAL;
        }
    }

}

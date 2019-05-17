package wclass.android_support.recycler_view_about;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

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
 * 1、2019年4月25日19:12:48
 * @待解决： -
 */
public abstract class SimpleRecyclerView<ItemView extends View> extends RecyclerView {
    public SimpleRecyclerView(Context context) {
        super(context);
        onSetLayoutManager(context, getOrien());
        setAdapter(new MyAdapter());
    }

    /**
     * 子类可以把它写成{@link GridLayoutManager}
     *
     * @param orien 容器滑动方向。
     */
    protected void onSetLayoutManager(Context context, Orien2 orien) {
        LinearLayoutManager llm;
        switch (orien) {
            case HORIZONTAL:
                llm = new
                        LinearLayoutManager
                        (context, LinearLayoutManager.HORIZONTAL, false);
                break;
            case VERTICAL:
                llm = new
                        LinearLayoutManager
                        (context, LinearLayoutManager.VERTICAL, false);
                break;
            default:
                throw new IllegalStateException();
        }
        setLayoutManager(llm);
    }

    /**
     * 获取item的数量。
     */
    protected abstract int getItemCount();

    /**
     * 创建item的view。
     *
     * @param parent 父容器。
     * @return item的view。
     */
    protected abstract ItemView onCreateItemView(SimpleRecyclerView parent);

    /**
     * item的view绑定指定position。
     *
     * @param itemView item的view
     * @param position item的view绑定该position。
     */
    protected abstract void onBindPosition(ItemView itemView, int position);

    /**
     * 获取布局方向。
     */
    protected abstract Orien2 getOrien();

    //////////////////////////////////////////////////
    class MyAdapter extends Adapter<MyAdapter.MyHolder> {
        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemView itemView = onCreateItemView(SimpleRecyclerView.this);
            return new MyHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {
            onBindPosition(holder.view, position);
        }

        @Override
        public void onViewAttachedToWindow(@NonNull MyHolder holder) {
            super.onViewAttachedToWindow(holder);
            SimpleRecyclerView.this.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(@NonNull MyHolder holder) {
            super.onViewDetachedFromWindow(holder);
            SimpleRecyclerView.this.onViewDetachedFromWindow(holder);

        }

        @Override
        public int getItemCount() {
            return SimpleRecyclerView.this.getItemCount();
        }

        class MyHolder extends ViewHolder {
            ItemView view;

            MyHolder(ItemView itemView) {
                super(itemView);
                view = itemView;
            }
        }
    }

    protected void onViewDetachedFromWindow(ViewHolder holder) {
    }

    protected void onViewAttachedToWindow(ViewHolder holder) {
    }

}

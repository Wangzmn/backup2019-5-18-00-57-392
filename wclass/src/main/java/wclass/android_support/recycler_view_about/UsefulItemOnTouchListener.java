package wclass.android_support.recycler_view_about;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import wclass.android.util.DebugUT;
import wclass.android.util.debug.EventUT;

/**
 * @作者 做就行了！
 * @时间 2019-04-24下午 10:47
 * @该类描述： -
 * 1、给{@link RecyclerView}设置触摸监听，触发点击时，发出回调。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("WeakerAccess")
public class UsefulItemOnTouchListener implements RecyclerView.OnItemTouchListener {
    private static final boolean DEBUG = false;
    //////////////////////////////////////////////////
    private RecyclerView rv;

    /**
     * 标记recyclerView的item是否请求事件。
     */
    private boolean childRequest = false;

    public UsefulItemOnTouchListener(RecyclerView rv) {
        this.rv = rv;
        gestureDetector = new GestureDetector(rv.getContext()
                , new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                //找到了item&&recyclerView不是滑动状态。
                if (child != null && canHandleUpEvent()) {
                    RecyclerView.ViewHolder vh = rv.getChildViewHolder(child);
                    onClick(vh);
                }
                if (DEBUG) {
                    String s = DebugUT.toStr_pointersCoor(e);
                    Log.e("TAG", getClass() + "#onSingleTapUp:  " + s + " 。"
                            + " 是否找到child：" + (child != null) + " 。");
                }
                return true;
            }
        });
    }

    private boolean canHandleUpEvent() {
        if (DEBUG) {
            Log.e("TAG", getClass() + "#canHandleUpEvent:" +
                            " recyclerView滑动状态 = " + (rv.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) + " 。"
//                    + " childRequest = " + childRequest + " 。"
            );
        }
        return rv.getScrollState() == RecyclerView.SCROLL_STATE_IDLE;
    }

    protected void onClick(RecyclerView.ViewHolder vh) {
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onClick:" +
                    " 被点击的itemPosition = " + vh.getLayoutPosition() + " 。");
        }
    }

    private GestureDetector gestureDetector;

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onInterceptTouchEvent: "
                    + EventUT.actionToStr(e));
        }
        boolean intercept = false;
        int actionMasked = e.getActionMasked();
        switch (actionMasked) {
            //按下时重置标记。
            case MotionEvent.ACTION_DOWN:
                childRequest = false;
                break;
            case MotionEvent.ACTION_UP:
                //抬起时，如果item还没有请求，则拦截最后一次的up事件。
                if (!childRequest) {
                    intercept = true;
                }
                break;
        }
        //item没有请求时才让gesture处理事件。
        if (!childRequest) {
            gestureDetector.onTouchEvent(e);
        }
        return intercept;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onTouchEvent: " +
                    "" + EventUT.actionToStr(e));
        }
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onRequestDisallowInterceptTouchEvent:" +
                    " disallowIntercept = " + disallowIntercept + " 。");
        }
        //只要有一个item请求，此次事件就由item处理。
        childRequest |= disallowIntercept;
    }
}

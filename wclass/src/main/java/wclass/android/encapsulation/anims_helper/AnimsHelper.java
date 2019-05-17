package wclass.android.encapsulation.anims_helper;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import wclass.android.encapsulation.anims_helper.listeners.BaseListener;
import wclass.util.MathUT;

/**
 * @作者 做就行了！
 * @时间 2019-04-12下午 2:03
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class AnimsHelper {
    private static final boolean DEBUG = false;
    //////////////////////////////////////////////////

    private int duration = 200;//默认动画持续时间。
    private List<View> animings = new ArrayList<>();
    private boolean cleaning;

    public AnimsHelper() {
        if (DEBUG) {
            duration = 2000;
        }
    }

    public AnimsHelper(int duration) {
        this.duration = MathUT.limitMin(duration, 0);
        if (DEBUG) {
            this.duration = 2000;
        }
    }
    //--------------------------------------------------

    public void removeInList(View view) {
        if (cleaning) return;
        for (int i = animings.size() - 1; i >= 0; i--) {
            View viewI = animings.get(i);
            if (viewI == view) {
                view.animate().cancel();
                animings.remove(i);
                return;
            }
        }
    }

    public void addInList(View view) {
        if (view == null) return;
//        if (animings.contains(view)) return;
        animings.add(view);
    }
    //////////////////////////////////////////////////

    /**
     * 清除所有记录的动画。
     */
    public void cleanAnim() {
        cleaning = true;
        for (int i = 0; i < animings.size(); i++) {
            View animing = animings.get(i);
            animing.animate().cancel();
        }
        animings.clear();
        cleaning = false;
    }

    //////////////////////////////////////////////////
    /*位移动画。*/
    public void xy(View view, float dstX, float dstY, BaseListener l) {
        xy(view, dstX, dstY, duration, l);
    }

    public void xy(View view, float dstX, float dstY, long duration) {
        xy(view, dstX, dstY, duration, null);
    }

    public void xy(View view, float dstX, float dstY, long duration, BaseListener l) {
        forL(view, l);
        view.animate().x(dstX).y(dstY).setDuration(duration).setListener(l)
                .start();
        if (DEBUG) {
//            Log.e("TAG", " dstX = " + dstX);
//            Log.e("TAG", " dstY = " + dstY);
//            Log.e("TAG", " duration = " + duration);
        }
    }

    //--------------------------------------------------
    /*透明度进入动画。*/
    public void alphaIn(View view, long duration) {
        alphaIn(view, duration, null);
    }

    public void alphaIn(View view, BaseListener l) {
        alphaIn(view, duration, l);
    }

    public void alphaIn(View view, long duration, BaseListener l) {
        forL(view, l);
        view.setAlpha(0);
        view.animate().alpha(1).setDuration(duration).setListener(l)
                .start();

        if (DEBUG) {
            Log.e("TAG", getClass() + "#alphaIn  ");
        }
//        view.animate().alpha(0).setDuration(duration).setListener(l)
//                .start();
    }
    //--------------------------------------------------
    /*透明度退出动画。*/

    public void alphaOut(View view, long duration) {
        alphaOut(view, duration, null);
    }

    public void alphaOut(View view, BaseListener l) {
        alphaOut(view, duration, l);

    }

    public void alphaOut(View view, long duration, BaseListener l) {
        forL(view, l);
        view.animate().alpha(0).setDuration(duration).setListener(l)
                .start();
    }
    //--------------------------------------------------

    /**
     * 把 动画的view 和 动画中的view的集合类 传给监听者。
     *
     * @param view 动画的view
     * @param l    监听者
     */
    private void forL(View view, BaseListener l) {
        if (l == null) return;
        l.set(this, view);
    }
}

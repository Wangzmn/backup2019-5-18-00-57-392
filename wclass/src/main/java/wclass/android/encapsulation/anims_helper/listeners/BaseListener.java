package wclass.android.encapsulation.anims_helper.listeners;

import android.animation.Animator;
import android.view.View;

import wclass.android.encapsulation.anims_helper.AnimsHelper;

@SuppressWarnings("WeakerAccess")
public abstract class BaseListener implements Animator.AnimatorListener {

    /**
     * 警告：只能在{@link Animator.AnimatorListener}回调中使用该变量！！！
     */
    protected View view;

    /**
     * 警告：只能在{@link Animator.AnimatorListener}回调中使用该变量！！！
     */
    protected AnimsHelper animsHelper;
    //////////////////////////////////////////////////

    public BaseListener() {
    }

    /**
     * 由{@link AnimsHelper}调用。
     *
     * @param animsHelper 动画辅助类。
     * @param view        监听的view。
     */
    public void set(AnimsHelper animsHelper, View view) {
        this.animsHelper = animsHelper;
        this.view = view;
    }
    //////////////////////////////////////////////////

    @Override
    public void onAnimationStart(Animator animation) {
        animsHelper.addInList(view);
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        animsHelper.removeInList(view);
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        animsHelper.removeInList(view);
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
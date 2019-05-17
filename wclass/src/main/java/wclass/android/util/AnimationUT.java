package wclass.android.util;

import android.view.View;

/**
 * @作者 做就行了！
 * @时间 2019-03-18下午 10:57
 * @该类描述： -
 * 1、封装{@link View}简单的动画。
 *
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("all")
public class AnimationUT {

    /**
     * 进入时的缩放动画。
     *
     * @param v view。
     */
    public static void scaleAnimIn(View v) {
        scaleAnim(v, 1, DurationUT.ANIM_DURATION);
    }

    /**
     * 退出时的缩放动画。
     *
     * @param v view。
     */
    public static void scaleAnimOut(View v) {
        scaleAnim(v, 0, DurationUT.ANIM_DURATION);
    }

    /**
     * 动画缩放至指定数值。
     *
     * @param v       view。
     * @param toScale 目标缩放比例。
     */
    public static void scaleAnim(View v, float toScale) {
        scaleAnim(v, toScale, DurationUT.ANIM_DURATION);
    }

    /**
     * 动画缩放至指定数值。
     *
     * @param v        view。
     * @param toScale  目标缩放比例。
     * @param duration 该动画的持续时长。
     */
    public static void scaleAnim(View v, float toScale, long duration) {
        v.animate().setDuration(duration)
                .scaleX(toScale).scaleY(toScale).start();
    }

}

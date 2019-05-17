package wclass.android.util;

import android.animation.Animator;
import android.animation.ValueAnimator;

/**
 * @作者 做就行了！
 * @时间 2019-03-18下午 11:04
 * @该类描述： -
 * 1、对{@link ValueAnimator}简单的封装。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings({"unused", "Convert2Lambda"})
public class AnimatorUT {
    public interface Update {

        void onUpdate(float progress);
        /**
         * 动画开始时。
         */
        void onStart();

        /**
         * 动画结束时。
         */
        void onEnd();

        /**
         * 动画取消时。
         */
        void onCancel();
    }

    /**
     * 该动画是为了获取进度百分比。
     * {@link Update#onUpdate(float)}的值为：0至1。
     *
     * @param duration 动画时长。
     * @param update   回调。
     * @return 该动画执行者。
     * 友情提示：该动画已经启动了。
     */
    public static ValueAnimator forProgressPercentage(long duration, Update update) {
        ValueAnimator va = ValueAnimator.ofFloat(duration);
        va.setDuration(duration);
        va.start();
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                update.onStart();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                update.onEnd();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                update.onCancel();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                float per = value / duration;
                update.onUpdate(per);
            }
        });
        return va;
    }

    /**
     * 该动画是为了获取进度对应的准确数值。
     * {@link Update#onUpdate(float)}的值为：from 至 to。
     *
     * @param from     起始数值。
     * @param to       目标数值。
     * @param duration 动画时长。
     * @param update   回调。
     * @return 该动画执行者。
     * 友情提示：该动画已经启动了。
     */
    public static ValueAnimator forProgressValue(float from, float to, long duration, Update update) {
        float cut = to - from;
        ValueAnimator va = ValueAnimator.ofFloat(duration);
        va.setDuration(duration);
        va.start();
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                update.onStart();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                update.onEnd();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                update.onCancel();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                float per = value / duration;
                update.onUpdate(from + per * cut);
            }
        });
        return va;
    }
}

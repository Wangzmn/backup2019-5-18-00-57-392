package wclass.android.encapsulation.anims_helper.listeners;

import android.animation.Animator;

/**
 * @作者 做就行了！
 * @时间 2019-04-12下午 3:01
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class AlphaListener extends BaseListener {
    private float finalAlpha;//取消动画时，alpha属性设置为该值。

    /**
     * 构造方法。
     *
     * @param finalAlpha 取消动画时，alpha属性设置为该值。
     */
    public AlphaListener(float finalAlpha) {
        this.finalAlpha = finalAlpha;
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        view.setAlpha(finalAlpha);
    }
}

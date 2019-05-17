package wclass.android.ui.drawable.useful;

import android.graphics.drawable.GradientDrawable;

/**
 * @作者 做就行了！
 * @时间 2019-04-02下午 11:59
 * @该类描述： -
 * 1、该类为{@link GradientDrawable}简单的封装。
 * 2、该类简化为设置4个角的圆角半径。
 *   （GradientDrawable要设置8个参数。）
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("WeakerAccess")
public class SimpleGradientDrawable extends GradientDrawable {
    private float[] radii;

    public SimpleGradientDrawable() {
    }

    public SimpleGradientDrawable(float[] fourCornerRadius) {
        setFourCornersRadius(fourCornerRadius);
    }

    public SimpleGradientDrawable(Orientation orientation, int[] colors) {
        super(orientation, colors);
    }

    /**
     * 设置四个角的半径。
     *
     * @param fourCornerRadius 长度为4。
     *               四个角顺序：从左上角开始，顺时针旋转，依次为每个角的半径。
     */
    public void setFourCornersRadius(float[] fourCornerRadius) {
        if (fourCornerRadius == null) {
            throw new IllegalStateException("radius数组不能为空，请认真一点 ~");
        }
        int length = fourCornerRadius.length;
        if (length != 4) {
            throw new IllegalStateException("radius数组长度为4，" +
                    "请求的radius数组长度为：" + length + "，请认真一点 ~");
        }
        if (radii == null) {
            radii = new float[8];
        }
        radii[0] = radii[1] = fourCornerRadius[0];
        radii[2] = radii[3] = fourCornerRadius[1];
        radii[4] = radii[5] = fourCornerRadius[2];
        radii[6] = radii[7] = fourCornerRadius[3];
        setCornerRadii(radii);
    }
}

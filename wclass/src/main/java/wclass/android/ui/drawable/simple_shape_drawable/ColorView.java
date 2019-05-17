package wclass.android.ui.drawable.simple_shape_drawable;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

/**
 * @作者 做就行了！
 * @时间 2019-04-20下午 3:14
 * @该类描述： -
 * 1、自定义颜色的view。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class ColorView extends View {
    private final int color;

    public ColorView(Context context, int color) {
        super(context);
        this.color = color;
        setBackground(new ColorDrawable(color));
    }

    public int getColor() {
        return color;
    }
}

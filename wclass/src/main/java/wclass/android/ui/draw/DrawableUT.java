package wclass.android.ui.draw;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * @作者 做就行了！
 * @时间 2019-03-26下午 3:50
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class DrawableUT {
    public static boolean setState(Drawable drawable, int[] drawableState) {
        return drawable != null
                && drawable.isStateful()
                && drawable.setState(drawableState);
    }

    public static void dismiss(View view, Drawable drawable) {
        if (drawable != null) {
            drawable.setCallback(null);
            view.unscheduleDrawable(drawable);
        }
    }
}

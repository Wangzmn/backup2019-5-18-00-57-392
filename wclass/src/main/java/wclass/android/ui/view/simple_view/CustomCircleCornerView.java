package wclass.android.ui.view.simple_view;

import android.content.Context;
import android.view.View;

import wclass.android.ui.drawable.simple_shape_drawable.CircleCornerDrawable;
import wclass.android.ui.params.RectBoolean;

/**
 * @作者 做就行了！
 * @时间 2019-04-20下午 11:00
 * @该类描述： -
 * 1、自定义4个角，哪些角是圆角的view。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class CustomCircleCornerView extends View {
    private int color;
    private RectBoolean rectBoolean;

    /**
     * @param context     上下文。
     * @param rectBoolean 标记哪个角绘制成圆角。
     */
    public CustomCircleCornerView(Context context, int color, RectBoolean rectBoolean) {
        super(context);
        this.color = color;
        this.rectBoolean = rectBoolean;
        setBackground(new CircleCornerDrawable(color, rectBoolean));
    }

    public void reDrawable(int color, RectBoolean rectBoolean) {
        this.color = color;
        this.rectBoolean = rectBoolean;
        setBackground(new CircleCornerDrawable(color, rectBoolean));
    }

}

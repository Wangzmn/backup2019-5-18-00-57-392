package wclass.android.ui.drawable.simple_shape_drawable;

import android.graphics.Canvas;
import android.graphics.Path;

import wclass.android.ui.draw.PathUT;
import wclass.android.ui.drawable.z_secondary.DrawableImpl;
import wclass.android.ui.params.RectBoolean;

/**
 * @作者 做就行了！
 * @时间 2019-04-20下午 2:43
 * @该类描述： -
 * 1、自定义4个角，哪些角是圆角的drawable。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class CircleCornerDrawable extends DrawableImpl {

    private RectBoolean rectBoolean;
    private Path path = new Path();

    public CircleCornerDrawable(int color, RectBoolean rectBoolean) {
        this.rectBoolean = rectBoolean;
        paint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {
        PathUT.makeCircleCornerRect(bounds, minSide / 2f, 0, path, rectBoolean);
        canvas.drawPath(path,paint);
    }
}

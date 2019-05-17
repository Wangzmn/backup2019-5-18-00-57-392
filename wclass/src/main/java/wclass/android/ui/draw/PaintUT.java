package wclass.android.ui.draw;

import android.graphics.Paint;

/**
 * @作者 做就行了！
 * @时间 2019-03-30下午 5:25
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class PaintUT {
    /**
     * 生成一个实体色的画笔，并返回。
     */
    public static Paint solidPaint() {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeWidth(0);//防止溢出绘制。
        return paint;
    }

    public static Paint strokePaint(){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setDither(true);
        return paint;
    }
}

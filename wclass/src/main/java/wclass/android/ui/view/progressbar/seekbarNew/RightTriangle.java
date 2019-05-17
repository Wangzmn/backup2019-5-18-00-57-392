package wclass.android.ui.view.progressbar.seekbarNew;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import wclass.android.ui.draw.PathTriangleUT;
import wclass.enums.Orien4;
import wclass.util.ColorUT;

/**
 * @作者 做就行了！
 * @时间 2019-03-18下午 10:15
 * @该类描述： -
 * 1、一个简单的三角形，它的箭头方向朝右，该三角形以矩形为轮廓。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class RightTriangle extends Drawable {
    private static final int STROKE_COUNT = 1;
    private static final int STROKE_WIDTH = 1;
    private Paint mainPaint;//主色画笔。
    private Path mainPath;//主色绘制区域。
    //----------------------------------------------------------------------
    private Paint strokePaint;//描边画笔。
    /**
     * 顺序：从里到外。
     * 描边路径。
     */
    private Path[] paths = new Path[STROKE_COUNT];
    /**
     * 顺序：从里到外。
     * 描边颜色。
     */
    @Deprecated
    private int[] colors = new int[]{ ColorUT.BLACK};
    //--------------------------------------------------
//    int strokeWidth;

    //////////////////////////////////////////////////
    public RightTriangle(int mainColor) {
        double v = 1 - 1 / Math.E;
        colors[0] = ColorUT.reValue(mainColor, (int) (v*255));
        //--------------------------------------------------
        mainPaint = new Paint();
        mainPath = new Path();
        strokePaint = new Paint();

        mainPaint.setStrokeJoin(Paint.Join.ROUND);
        mainPaint.setStyle(Paint.Style.FILL);
        mainPaint.setDither(true);
        mainPaint.setAntiAlias(true);
        mainPaint.setColor(mainColor);

        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(STROKE_WIDTH);
        strokePaint.setDither(true);
        strokePaint.setAntiAlias(true);

        for (int i = 0; i < STROKE_COUNT; i++) {
            paths[i] = new Path();
        }
    }

    @Override
    public void draw( Canvas canvas) {
        canvas.drawPath(mainPath, mainPaint);

        for (int i = 0; i < STROKE_COUNT; i++) {
            Path path = paths[i];
            strokePaint.setColor(colors[i]);
            canvas.drawPath(path, strokePaint);
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        float width1 = bounds.width();
        float height1 = bounds.height();
        //----------------------------------------------------------------------
        /*求上下角的直角边*/
        //上、下角的弧度。
        //noinspection SuspiciousNameCombination
        double arcForVerti = Math.atan2(width1, height1 / 2);
        double arcFor90 = Math.PI / 2;
        double arcForFaceTo = arcForVerti / 2;
        double arcForCos = arcFor90 - arcForFaceTo;
        double sin = Math.sin(arcForCos);
        double cos = Math.cos(arcForCos);
        double hy = STROKE_WIDTH / cos;
        double hyForVerti = hy * sin;//Y方向
        //----------------------------------------------------------------------
        /*求右角的斜边*/
        //noinspection UnnecessaryLocalVariable
        double arcForRight = arcForVerti;
        double cosForRight = Math.cos(arcForRight);
        double hyForRight = STROKE_WIDTH / cosForRight;//右边
        //----------------------------------------------------------------------
        width1 = (float) (width1 - STROKE_COUNT * (STROKE_WIDTH + hyForRight)
        );
        height1 = (float) (height1 - hyForVerti * STROKE_COUNT * 2);
        float offsetX1 = bounds.left + STROKE_COUNT * STROKE_WIDTH;
        float offsetY1 = (float) (bounds.top + STROKE_COUNT * hyForVerti);
        mainPath.rewind();
        PathTriangleUT.makeSimpleTriangle(width1, height1
                , Orien4.RIGHT, 0, mainPath,
                offsetX1, offsetY1);

        //----------------------------------------------------------------------

        for (int i = 0; i < STROKE_COUNT; i++) {
            Path path = paths[i];
            path.rewind();
            width1 = (float) (width1 + STROKE_WIDTH + hyForRight);
            height1 = (float) (height1 + hyForVerti * 2);
            offsetX1 = offsetX1 - STROKE_WIDTH;
            offsetY1 = (float) (offsetY1 - hyForVerti);
            PathTriangleUT.makeSimpleTriangle(width1, height1
                    , Orien4.RIGHT, STROKE_WIDTH, path,
                    offsetX1, offsetY1);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mainPaint.setAlpha(alpha);
        strokePaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter( ColorFilter colorFilter) {
        mainPaint.setColorFilter(colorFilter);
        strokePaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}

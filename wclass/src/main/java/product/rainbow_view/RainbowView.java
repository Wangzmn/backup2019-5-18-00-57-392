package product.rainbow_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

import wclass.shape_frame.rainbow_circle.OrderColorMaker;
import wclass.shape_frame.rainbow_circle.RGBColorMaker;
import wclass.shape_frame.rainbow_circle.RainbowCircle;
import wclass.ui.Coor;
import wclass.util.ColorUT;
import wclass.util.MathUT;

/**
 * @作者 做就行了！
 * @时间 2019/3/28 0028
 * @使用说明：
 */
@SuppressWarnings("Convert2Lambda")
public class RainbowView extends View {

    /**
     * 用来存放像素。
     */
    private Bitmap bitmap;
    private Canvas mCanvas;
    private Paint mPaint;
    private Path path = new Path();
    //--------------------------------------------------
    /*待赋值*/
    private float pivotRadius;
    private float radiusIncrement;
    private float offsetPivotX = 0;
    private float offsetPivotY = 0;
    //--------------------------------------------------
    /*domain 用户可自定义。*/

    /**
     * 0至255。
     */
    private int colorMaxValue = 255;
    /**
     * 设置该值的时候需要重新计算。
     */
    private int saturationCount = 8;
    private int sectionIn60Degree = 8;
    private int offsetDegree = 0;
    private OrderColorMaker orderColorMaker = new RGBColorMaker();
    /**
     * 标记该类是否初始化完毕。
     */
    private boolean init;
    //--------------------------------------------------

    public int getColorMaxValue() {
        return colorMaxValue;
    }

    public int getSaturationCount() {
        return saturationCount;
    }

    public int getSectionIn60Degree() {
        return sectionIn60Degree;
    }

    public int getOffsetDegree() {
        return offsetDegree;
    }


    //--------------------------------------------------

    /**
     * 设置单个色的最大值。
     *
     * @param colorMaxValue 单个色的最大值。
     *                      友情提示：该值为0至255。
     */
    public void setColorMaxValue(int colorMaxValue) {
        if (this.colorMaxValue == colorMaxValue) {
            return;
        }
        colorMaxValue = MathUT.limit(colorMaxValue, 0, 255);
        this.colorMaxValue = colorMaxValue;
        invalidateByUserSet();
    }

    private void invalidateByUserSet() {
        if (init) {
            invalidate();
            needRedraw = true;
        }
    }

    /**
     * 60度分成几份。
     *
     * @param sectionIn60Degree 60度分成几份。
     */
    public void setSectionIn60Degree(int sectionIn60Degree) {
        if (this.sectionIn60Degree == sectionIn60Degree) {
            return;
        }
        if (sectionIn60Degree < 1) {
            sectionIn60Degree = 1;
        }
        this.sectionIn60Degree = sectionIn60Degree;
        invalidateByUserSet();
    }

    /**
     * 设置补偿角度。
     *
     * @param offsetDegree 补偿角度。
     *                     友情提示：该角度为逆时针旋转。
     */
    public void setOffsetDegree(int offsetDegree) {
        if (this.offsetDegree == offsetDegree) {
            return;
        }
        this.offsetDegree = offsetDegree;
        invalidateByUserSet();
    }

    /**
     * {@link OrderColorMaker}
     *
     * @param orderColorMaker
     */
    public void setOrderColorMaker(OrderColorMaker orderColorMaker) {
        if (this.orderColorMaker == orderColorMaker) {
            return;
        }
        this.orderColorMaker = orderColorMaker;
        invalidateByUserSet();
    }

    public void setSaturationCount(int saturationCount) {
        saturationCount = MathUT.limit(saturationCount, 1, Integer.MAX_VALUE);
        if (this.saturationCount == saturationCount) {
            return;
        }
        this.saturationCount = saturationCount;
        adjustArgument(getWidth(), getHeight());
        invalidateByUserSet();
    }

    //////////////////////////////////////////////////
    public int getPixel(int x, int y) throws Exception {
        try {
            return bitmap.getPixel(x, y);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //////////////////////////////////////////////////
    private boolean needRedraw;

    public RainbowView(Context context) {
        super(context);
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(0);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != 0 && h != 0) {
            needRedraw = true;
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            if (!init) {
                init = true;
                mCanvas = new Canvas(bitmap);
            } else {
                mCanvas.setBitmap(bitmap);
            }
            adjustArgument(w, h);
        }
    }

    private void adjustArgument(int w, int h) {
        float shortSide = Math.min(w, h);
        float radius = shortSide / 2;
        float saturationSize = radius / (saturationCount + 0.5f);
        offsetPivotX = offsetPivotY = radius;
        pivotRadius = saturationSize / 2;
        radiusIncrement = saturationSize;
    }
    //////////////////////////////////////////////////
    /*domain touchEvent处理。*/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int action = event.getAction();
        int x = (int) (event.getX() + 0.5f);
        int y = (int) (event.getY() + 0.5f);
        int pixel;
        try {
            pixel = getPixel(x, y);
        } catch (Exception e) {
            pixel = 0;
            e.printStackTrace();
        }
        boolean hasPixel;
        if (pixel == 0) {
            hasPixel = false;
        } else {
            hasPixel = true;
            //像素有时会出现透明度，这里设置为不透明。
            pixel = ColorUT.toAlpha(pixel, 1);
        }
        cb.onTouch(hasPixel,pixel);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                cb.onDown(hasPixel, pixel);
                break;
            case MotionEvent.ACTION_MOVE:
                cb.onMove(hasPixel, pixel);
                break;
            case MotionEvent.ACTION_UP:
                cb.onUp(hasPixel, pixel);
                break;
        }
        return true;
    }

    public void setCallback(Callback cb) {
        this.cb = cb;
    }

    Callback cb;

    public interface Callback {

        void onTouch(boolean hasPixel, int pixel);

        void onDown(boolean hasPixel, int pixel);

        void onMove(boolean hasPixel, int pixel);

        void onUp(boolean hasPixel, int pixel);
    }

    //////////////////////////////////////////////////
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
            if (needRedraw) {
                needRedraw = false;
                //先用自己的画布画，用于保存像素。
                RainbowCircle.makeRainbowCircle(sectionIn60Degree,
                        saturationCount, colorMaxValue, offsetPivotX, offsetPivotY,
                        offsetDegree, pivotRadius, radiusIncrement,
                        orderColorMaker, new RainbowCircle.RainbowCircleCallback() {
                            @Override
                            public void onEverybody(Coor shortCoor1, Coor shortCoor2, Coor longCoor1, Coor longCoor2,
                                                    double shortRadius, double longRadius, int color) {
                                path.moveTo(shortCoor1.x, shortCoor1.y);
                                path.lineTo(shortCoor2.x, shortCoor2.y);
                                path.lineTo(longCoor1.x, longCoor1.y);
                                path.lineTo(longCoor2.x, longCoor2.y);
                                path.close();
                                mPaint.setColor(color);
                                mCanvas.drawPath(path, mPaint);
                                path.rewind();
                            }
                        });
            }
            //再画到屏幕上。
            canvas.drawBitmap(bitmap, 0, 0, mPaint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

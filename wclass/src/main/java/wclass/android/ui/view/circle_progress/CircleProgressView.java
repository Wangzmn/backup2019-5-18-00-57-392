package wclass.android.ui.view.circle_progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

import wclass.android.ui.draw.PaintUT;
import wclass.android.util.RectUT;
import wclass.android.util.SizeUT;
import wclass.util.CircleUT;
import wclass.util.MathUT;

/**
 * @作者 做就行了！
 * @时间 2019-04-04下午 10:42
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class CircleProgressView extends View {
    private static final boolean DEBUG = true;
    /**
     * 标记是否需要绘制。
     */
    private boolean needDraw;

    /**
     * view的可见区域。
     * <p>
     * 1、该属性是通过该方法获取的：
     * {@link View#getDrawingRect(Rect)}
     */
    Rect visualRect = new Rect();

    /**
     * view的绘制区域。
     * <p>
     * 无padding的{@link #visualRect}。
     */
    Rect drawRect = new Rect();
    //--------------------------------------------------

    /**
     * 默认的进度轨道描边的宽度。
     */
    private float defaultTrackStrokeWidth;
    /**
     * 默认的进度的描边的宽度。
     */
    private float defaultProgressStrokeWidth;
    /**
     * 标记该类是否初始化。
     */
    private boolean init;
    //////////////////////////////////////////////////
    /**
     * 补偿角度。
     * <p>
     * 友情提示：从X轴正方向，顺时针开始算。
     * <p>
     * 这里的默认值为往回90度，也就是Y轴正方向。
     */
    int offsetDegree = -90;
    /**
     * 圆形轨道的描边宽度。
     */
    float trackStrokeWidth = -1;
    /**
     * 圆形进度的描边宽度。
     */
    float progressStrokeWidth = -1;
    /**
     * 进度轨道的颜色。
     */
    int trackColor = 0xffeeeeee;
    /**
     * 进度的颜色。
     */
    int progressColor = 0xff75f834;
    /**
     * 当前进度值。该值范围：0至1。
     */
    float progress;
    //--------------------------------------------------
    /**
     * 绘制进度的画笔。
     */
    Paint progressPaint;
    /**
     * 绘制背景的画笔。
     */
    Paint bgPaint;
    //--------------------------------------------------
    /**
     * 圆心坐标。
     * 0下标：x坐标。
     * 1下标：y坐标。
     */
    float[] coreXY = new float[2];

    /**
     * 圆的半径。
     */
    float radius;

    /**
     * 圆所在的rect。
     * <p>
     * 该对象由以下两个组合而成：
     * {@link #coreXY}和{@link #radius}。
     * <p>
     * 友情提示：不包含圆的描边宽度。
     */
    RectF circleRect = new RectF();
    //--------------------------------------------------
    /**
     * 绘制当前进度的path。
     */
    Path progressPath = new Path();

    /**
     * {@link CircleUtil}
     */
    CircleUtil circleUtil;
    //////////////////////////////////////////////////

    /**
     * 构造方法。
     *
     * @param context 上下文。
     */
    public CircleProgressView(Context context) {
        super(context);
        progressPaint = onCreateProgressPaint();
        bgPaint = onCreateTrackBgPaint();
        int mm = SizeUT.getMMpixel(context);
        defaultTrackStrokeWidth = mm / 2;
        defaultProgressStrokeWidth = mm * 2;
        circleUtil = new CircleUtil();
        if (DEBUG) {
            int mm2 = SizeUT.getMMpixel(context);
            trackStrokeWidth = mm2;
            progressStrokeWidth = 4 * mm2;
            trackColor = 0xffaaaaaa;
            progressColor = 0xff75f834;
        }
    }
    //--------------------------------------------------

    /**
     * 设置当前进度。
     *
     * @param progress 当前进度。
     *                 该值为：0至1。
     */
    public void setProgress(float progress) {
        if (DEBUG) {
            Log.e("TAG", " setProgress: " + progress);
        }
        progress = MathUT.limit(progress, 0, 1);
        if (progress == this.progress) {
            return;
        }
        this.progress = progress;
        invalidate();
    }

    /**
     * {@link #offsetDegree}
     */
    public void setOffsetDegree(int offsetDegree) {
        this.offsetDegree = offsetDegree;
    }

    /**
     * {@link #trackStrokeWidth}
     */
    public void setTrackBgStrokeWidth(float trackBgWidth) {
        this.trackStrokeWidth = trackBgWidth;
        correctTrackBgWidth();
    }

    /**
     * {@link #progressStrokeWidth}
     */
    public void setTrackProgressStrokeWidth(float trackProgressWidth) {
        this.progressStrokeWidth = trackProgressWidth;
        correctTrackProgressWidth();
    }

    /**
     * {@link #trackColor}
     */
    public void setTrackColor(int trackColor) {
        this.trackColor = trackColor;
    }

    /**
     * {@link #progressColor}
     */
    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    //--------------------------------------------------

    /**
     * 获取当前进度。
     * {@link #progress}
     */
    public float getProgress() {
        return progress;
    }


    /**
     * {@link #offsetDegree}
     */
    public int getOffsetDegree() {
        return offsetDegree;
    }

    /**
     * {@link #trackStrokeWidth}
     */
    public float getTrackBgStrokeWidth() {
        correctTrackBgWidth();
        return trackStrokeWidth;
    }

    /**
     * {@link #progressStrokeWidth}
     */
    public float getTrackProgressStrokeWidth() {
        correctTrackProgressWidth();
        return progressStrokeWidth;
    }

    /**
     * {@link #trackColor}
     */
    public int getTrackColor() {
        return trackColor;
    }


    /**
     * {@link #progressColor}
     */
    public int getProgressColor() {
        return progressColor;
    }

    /**
     * {@link CircleUtil}
     */
    public CircleUtil getCircleUtil() {
        return circleUtil;
    }


    /**
     * 获取view的可见区域。
     */
    protected Rect getVisualRect() {
        return visualRect;
    }

    /**
     * 获取view的绘制区域。
     * <p>
     * 无padding的{@link #visualRect}。
     */
    protected Rect getDrawRect() {
        return drawRect;
    }

    /**
     * {@link #progressPaint}
     */
    protected Paint getTrackProgressPaint() {
        return progressPaint;
    }

    /**
     * {@link #bgPaint}
     */
    protected Paint getTrackBgPaint() {
        return bgPaint;
    }

    /**
     * 运行于 2019年4月5日21:39:55
     * <p>
     * 圆的信息类。
     * <p>
     * 警告：只有布局完毕后，才能使用该类的方法！！！
     */
    @SuppressWarnings("WeakerAccess")
    public class CircleUtil {

        /**
         * 内圈圆边上的坐标。
         * {@link CircleUtil#getCoorByRadius(float)}
         */
        public PointF getCoorOnInnerRound() {
            return getCoorByRadius(radius - progressStrokeWidth / 2);

        }

        /**
         * 外圈圆边上的坐标。
         * {@link CircleUtil#getCoorByRadius(float)}
         */
        public PointF getCoorOnOuterRound() {
            return getCoorByRadius(radius + progressStrokeWidth / 2);

        }

        /**
         * 位于圆描边中间的坐标。
         * {@link CircleUtil#getCoorByRadius(float)}
         */
        public PointF getCoorOnRound() {
            return getCoorByRadius(radius);
        }

        /**
         * 通过当前进度的值的弧度值，再根据半径长度，计算出坐标。
         *
         * @param radius 半径。
         * @return 通过当前进度的值的弧度值，再根据半径长度，计算出的坐标。
         */
        public PointF getCoorByRadius(float radius) {
            /**
             * 1、为什么拿360减？
             * 因为：该类中的角度都是顺时针算的，
             * 而弧度计算是以X轴正方向开始，逆时针开始算的。
             * todo
             * 添加功能：正反向都可以绘制。
             */
            float degree = 360 - (offsetDegree + progress * 360);
            double arc = CircleUT.getArc(degree);
            double cos = Math.cos(arc);
            double sin = Math.sin(arc);
            //通过余弦，求x坐标。
            float x = (float) (radius * cos);
            //通过正弦，求y坐标。
            float y = (float) (radius * sin);
            //圆心x坐标+坐标系中的x坐标。
            x += coreXY[0];
            /**
             * 圆心y坐标-坐标系中的y坐标。
             *
             * 1、为什么是减？
             * 因为：坐标系中的y越往上越大，
             * 而屏幕中的y越往上越小，
             * 所以拿屏幕中的y减去直角坐标系中的y。
             */
            y = coreXY[1] - y;
            return new PointF(x, y);
        }

        /**
         * 圆心坐标。
         * {@link CircleProgressView#coreXY}
         */
        public float[] getCoreXY() {
            return coreXY;
        }

        /**
         * 圆的半径。
         * {@link CircleProgressView#radius}
         */
        public float getRadius() {
            return radius;
        }

        /**
         * 圆所在的rect。
         * {@link CircleProgressView#circleRect}
         */
        public RectF getCircleRect() {
            return circleRect;
        }
    }

    //////////////////////////////////////////////////

    @Override
    public void draw(Canvas canvas) {
        if (DEBUG) {
            Log.e("TAG", " draw:begin ");
        }

        super.draw(canvas);

        if (DEBUG) {
            Log.e("TAG", " draw:finish ");
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (DEBUG) {
            Log.e("TAG", " onLayout:begin");
        }
        if (!init) {
            init = true;
            init();
        }
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            getDrawingRect(visualRect);
            RectUT.delPadding(visualRect, drawRect, this);
        }
        if (drawRect.isEmpty()) {
            needDraw = false;
            return;
        }
        needDraw = true;
        preReCircle(changed);
        onAdjustCircleCoreXY(coreXY);
        radius = onReRadius();
        float coreX = coreXY[0];
        float coreY = coreXY[1];
        circleRect.left = coreX - radius;
        circleRect.top = coreY - radius;
        circleRect.right = coreX + radius;
        circleRect.bottom = coreY + radius;
        if (DEBUG) {
            Log.e("TAG", " onLayout:finish");
        }
    }

    private void init() {
        correctTrackBgWidth();
        correctTrackProgressWidth();
    }

    /**
     * 纠正进度滑道的描边宽度。
     */
    private void correctTrackBgWidth() {
        if (trackStrokeWidth < 0) {
            trackStrokeWidth = defaultTrackStrokeWidth;
        }

    }

    /**
     * 纠正进度的描边宽度。
     */
    private void correctTrackProgressWidth() {
        if (progressStrokeWidth < 0) {
            progressStrokeWidth = defaultProgressStrokeWidth;
        }
    }

    /**
     * 调整圆之前，该方法会被调用。
     *
     * @param changed 布局是否改变过。
     *                true：布局改变过。
     *                false：布局未改变过。
     */
    protected void preReCircle(boolean changed) {

    }

    /**
     * 该方法中调整圆的半径。
     *
     * @return 圆的半径。
     */
    protected float onReRadius() {
        return RectUT.min(drawRect) / 2f - progressStrokeWidth / 2f;
    }

    /**
     * 调整圆心坐标。
     *
     * @param coreXY 圆心坐标。
     *               {@link #coreXY}
     */
    protected void onAdjustCircleCoreXY(float[] coreXY) {
        Rect drawRect = getDrawRect();
        RectUT.getCore(drawRect, coreXY);
    }

    public interface Maker {

        /**
         * 调整圆之前，该方法会被调用。
         * 友情提示：大小改变时、padding改变时，被视为改变布局。
         *
         * @param changed 布局是否改变过。
         *                true：布局改变过。
         *                false：布局未改变过。
         */
        void preReCircle(boolean changed);

        /**
         * 调整圆心的坐标。
         *
         * @param coreXY 圆心坐标。
         *               {@link #coreXY}
         */
        void onAdjustCircleCoreXY(float[] coreXY);

        /**
         * 调整圆的半径。
         *
         * @return 圆的半径。
         */
        float onReRadius();
    }

    //////////////////////////////////////////////////
    /*绘制相关。*/
    @Override
    protected void onDraw(Canvas canvas) {
        if (DEBUG) {
            Log.e("TAG", " onDraw ");
        }
        super.onDraw(canvas);
        if (!needDraw) {
            return;
        }
        preDrawCircle(canvas);
        onDrawTrack(canvas, bgPaint, coreXY, radius, trackStrokeWidth, trackColor);
        onDrawProgress(canvas, progressPaint, progressPath, progress, offsetDegree,
                coreXY, circleRect, progressStrokeWidth, progressColor);
    }

    /**
     * 绘制圆的轨道背景。
     *
     * @param bgPaint      进度轨道的画笔。
     * @param coreXY       圆心的坐标。
     *                     {@link #coreXY}
     * @param radius       圆的半径
     * @param trackBgWidth 进度轨道描边的宽度。
     * @param trackBgColor 进度轨道描边的颜色。
     */
    private void onDrawTrack(Canvas canvas, Paint bgPaint, float[] coreXY,
                             float radius, float trackBgWidth, int trackBgColor) {
        /*画进度轨道。*/
        bgPaint.setStrokeWidth(trackBgWidth);
        bgPaint.setColor(trackBgColor);
        canvas.drawCircle(coreXY[0], coreXY[1], radius, bgPaint);
    }

    /**
     * 正式绘制圆的进度。
     *
     * @param progressPaint      进度的画笔。
     * @param progressPath       路径对象。
     *                           你看着办吧，都传给你了。
     * @param progress           进度。该值为：0至1。
     * @param offsetDegree       补偿角度。
     *                           友情提示：坐标系X轴正方向开始，顺时针开始算。
     * @param coreXY             圆心的坐标。
     *                           {@link #coreXY}
     * @param circleRect         圆所在的rect。
     * @param trackProgressWidth 进度的描边宽度。
     * @param trackProgressColor 进度的描边颜色。
     */
    protected void onDrawProgress(Canvas canvas, Paint progressPaint, Path progressPath,
                                  float progress, int offsetDegree,
                                  float[] coreXY, RectF circleRect,
                                  float trackProgressWidth, int trackProgressColor) {
        /*画进度。*/
        progressPaint.setStrokeWidth(trackProgressWidth);
        progressPaint.setColor(trackProgressColor);
        float sweepDegree = progress * 360;
        if (sweepDegree == 360) {
            sweepDegree = 359;
        }
        progressPath.rewind();
        progressPath.moveTo(coreXY[0], coreXY[1]);
        progressPath.arcTo(circleRect, offsetDegree, sweepDegree, true);
        canvas.drawPath(progressPath, progressPaint);
    }

    /**
     * 绘制圆之前，该方法会被调用。
     */
    protected void preDrawCircle(Canvas canvas) {

    }
    //////////////////////////////////////////////////

    /**
     * 创建进度的画笔。
     * <p>
     * 友情提示：该方法是在构造方法中调用的 ~
     */
    protected Paint onCreateProgressPaint() {
        Paint paint = PaintUT.strokePaint();
        paint.setStrokeCap(Paint.Cap.ROUND);
        return paint;
    }

    /**
     * 创建进度轨道的画笔。
     * <p>
     * 友情提示：该方法是在构造方法中调用的 ~
     */
    protected Paint onCreateTrackBgPaint() {
        return PaintUT.strokePaint();
    }
}

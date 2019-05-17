package wclass.android.ui.view.circle_progress.surround_circle_progress_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import wclass.android.ui.view.circle_progress.CircleProgressView;
import wclass.android.ui.drawable.useful.NaturalDrawable;
import wclass.android.util.SizeUT;
import wclass.common.SizeMaker;
import wclass.util.ColorUT;

/**
 * @作者 做就行了！
 * @时间 2019-04-05下午 5:38
 * @该类描述： -
 * 1、该类全称：SurroundCircleProgressView
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 * todo
 * 1、注释~！！！！
 * 2、黑白圆圈！！！
 */
public class SurroundCPView extends CircleProgressView {

    private static final boolean DEBUG = true;
    /**
     * 标记是否初始化完毕。
     */
    private boolean init;
    private final int mm;

    /**
     * 构造方法。
     *
     * @param context 上下文。
     */
    public SurroundCPView(Context context) {
        super(context);
        mm = SizeUT.getMMpixel(context);
        if(DEBUG){
            setPlanetDrawable(new NaturalDrawable(ColorUT.RED,
                    0.5f));
        }
    }

    int[] WH = new int[2];

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        asInit();
    }

    @Override
    protected void preReCircle(boolean changed) {
        super.preReCircle(changed);
        if (planetDrawable == null) {
            return;
        }
        if (planetMaker == null) {
            planetMaker = new PlanetSizeMaker();

        }
        planetMaker.make(WH, this);

    }

    @Override
    protected float onReRadius() {
        float radius = super.onReRadius();
        if (planetDrawable != null) {
            int w = WH[0];
            int h = WH[1];
            radius -= Math.max(w, h);
        }
        return radius;
    }

    SizeMaker<SurroundCPView> planetMaker;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (planetDrawable != null) {
            onDrawPlanet(canvas, planetDrawable, WH, getCircleUtil());
        }
    }

    Rect planetRect = new Rect();

    /**
     * 绘制行星的图片。
     *
     * @param planetDrawable 行星的图片。
     * @param planetWH       行星的宽高。
     *                       {@link SurroundCPView#WH}
     * @param circleUtil     {@link CircleUtil}
     */
    private void onDrawPlanet(Canvas canvas, Drawable planetDrawable,
                              int[] planetWH, CircleUtil circleUtil) {
        //planet的圆心，在大圆的描边的中间位置。
        PointF coor = circleUtil.getCoorOnRound();
        int w = planetWH[0];
        int h = planetWH[1];
        planetRect.left = (int) (coor.x - w / 2 + 0.5f);
        planetRect.top = (int) (coor.y - h / 2 + 0.5f);
        planetRect.right = planetRect.left + w;
        planetRect.bottom = planetRect.top + h;

        planetDrawable.setBounds(planetRect);
        planetDrawable.draw(canvas);
    }

    /**
     * 设置行星的图片。
     *
     * @param planetDrawable 行星的图片。
     */
    public void setPlanetDrawable(Drawable planetDrawable) {
        this.planetDrawable = planetDrawable;
    }

    /**
     * 行星的图片。
     */
    Drawable planetDrawable;

    /**
     * 初始化必要的对象。
     */
    private void asInit() {
    }

}

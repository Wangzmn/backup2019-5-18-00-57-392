package wclass.android.ui.drawable.stagger_color_drawable;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import wclass.shape_frame.StaggerColorGrid;

/**
 * @作者 做就行了！
 * @时间 2019-04-02下午 1:57
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("WeakerAccess")
public class StaggerColorGridHelper {
    private float subWidth;//孩子宽。
    private float subHeight;//孩子高。
    private int columnCount;//列数。
    private int rowCount;//行数。
    /**
     * {@link Strategy}
     */
    private Strategy strategy = Strategy.FULL_AUTO;
    //////////////////////////////////////////////////
    private int firstColor;//第一个颜色。
    private int secondColor;//第二个颜色。
    private RectF usableRect;
    private Canvas canvas;
    private Paint paint;
    //////////////////////////////////////////////////

    /**
     * 构造方法。
     * @param firstColor 第一个颜色。
     * @param secondColor 第二个颜色。
     * @param strategy {@link Strategy}
     */
    public StaggerColorGridHelper(int firstColor, int secondColor,
                                  float subWidth, float subHeight,
                                  RectF usableRect, Paint paint, Strategy strategy) {
        this.firstColor = firstColor;
        this.secondColor = secondColor;
        this.subWidth = subWidth;
        this.subHeight = subHeight;
        this.usableRect = usableRect;
        this.paint = paint;
        this.strategy = strategy;
    }

    private final StaggerColorGrid.Callback<Canvas> callback = new StaggerColorGrid.Callback<Canvas>() {
        @Override
        public void on(Canvas canvas, int color, float left, float top, float right, float bottom) {
            if (right > usableRect.right) {
                right = usableRect.right;
            }
            if (bottom > usableRect.bottom) {
                bottom = usableRect.bottom;
            }
            paint.setColor(color);
            canvas.drawRect(left, top, right, bottom, paint);
        }
    };
    //--------------------------------------------------
    public void draw(Canvas canvas) {
        StaggerColorGrid.make(canvas,
                firstColor, secondColor, subWidth, subHeight,
                columnCount, rowCount,
                usableRect.left, usableRect.top,
                callback);

    }

    //--------------------------------------------------
    public void calculate() {
        float rootWidth = usableRect.width();
        float rootHeight = usableRect.height();
        float columns = rootWidth / subWidth;
        float rows = rootHeight / subHeight;
        switch (strategy) {
            case NORMAL:
                //向上取整。
                columnCount = (int) Math.ceil(columns);
                rowCount = (int) Math.ceil(rows);

                this.subWidth = subWidth;
                this.subHeight = subHeight;
                break;
            case FULL_TO_BIG:
                columnCount = (int) columns;
                rowCount = (int) rows;

                this.subWidth = rootWidth / columnCount;
                this.subHeight = rootHeight / rowCount;
                break;
            case FULL_TO_SMALL:
                //向上取整。
                columnCount = (int) Math.ceil(columns);
                rowCount = (int) Math.ceil(rows);

                this.subWidth = rootWidth / columnCount;
                this.subHeight = rootHeight / rowCount;
                break;
            case FULL_AUTO:
                //数量为四舍五入。
                columnCount = (int) (columns + 0.5f);
                rowCount = (int) (rows + 0.5f);

                this.subWidth = rootWidth / columnCount;
                this.subHeight = rootHeight / rowCount;
                break;
            default:
                throw new IllegalStateException();
        }


    }

}

package wclass.android.ui.drawable.stagger_color_drawable;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import wclass.android.ui.drawable.z_secondary.DrawableImpl;

/**
 * @作者 做就行了！
 * @时间 2019-04-02下午 3:00
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class StaggerColorDrawable extends DrawableImpl {
    private final RectF usableRect = new RectF();
    private final StaggerColorGridHelper staggerColorGridHelper;

    public StaggerColorDrawable(int firstColor, int secondColor,
                                float subWidth, float subHeight,
                                Strategy strategy) {
        staggerColorGridHelper = new StaggerColorGridHelper(
                firstColor, secondColor,
                subWidth, subHeight,
                usableRect, paint, strategy);
    }

    @Override
    public void draw(Canvas canvas) {
        staggerColorGridHelper.draw(canvas);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        usableRect.set(bounds);
        staggerColorGridHelper.calculate();
    }
}

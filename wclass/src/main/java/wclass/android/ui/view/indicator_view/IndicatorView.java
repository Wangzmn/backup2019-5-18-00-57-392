package wclass.android.ui.view.indicator_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import wclass.android.ui.drawable.useful.NaturalDrawable;
import wclass.android.ui.view.mover_view.MoverView;
import wclass.util.MathUT;

/**
 * @作者 做就行了！
 * @时间 2019-04-07上午 11:39
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 * bug记录
 * 1、normal绘制了。
 * 2、selected没有绘制。
 */
public abstract class IndicatorView extends MoverView {
    private static final boolean DEBUG = true;
    private static final int COLOR_4_NORM = 0xffff7634;
    private static final int COLOR_4_SELECT = 0xffff0000;
    //    private static final int COLOR_4_NORM = 0xffdddddd;
//    private static final int COLOR_4_SELECT = 0xffff7634;

    private int indiCount;
    private int selectDex;
    //--------------------------------------------------
    /**
     * 选中的点 的drawable。
     */
    Drawable standerDrawable;
    /**
     * 所有 常态的点 的drawable。
     */
    List<Drawable> normerDrawables;


    /**
     * {@link IndicatorView#IndicatorView(android.content.Context, int, android.graphics.drawable.Drawable, wclass.android.ui.view.indicator_view.IndicatorView.NormerDrawableMaker)}
     */
    public IndicatorView(Context context, int indiCount) {

        this(context, indiCount, COLOR_4_SELECT, COLOR_4_NORM);
    }

    /**
     * {@link IndicatorView#IndicatorView(android.content.Context, int, android.graphics.drawable.Drawable, wclass.android.ui.view.indicator_view.IndicatorView.NormerDrawableMaker)}
     */
    public IndicatorView(Context context, int indiCount,
                         int selectedColor, int normerColor) {
        this(context, indiCount, new NaturalDrawable(selectedColor,
                        0.5f),
                new NormerDrawableMaker() {
                    @Override
                    public Drawable getNormerDrawable(int dex) {
                        return new NaturalDrawable(normerColor,
                                0.5f);
                    }
                });
    }


    /**
     * 构造方法。
     *  @param context 上下文。
     * @param indiCount 指示器点的数量。
     * @param selectedDrawable 选中的点 的drawable。
     * @param m {@link NormerDrawableMaker}
     */
    public IndicatorView(Context context, int indiCount, Drawable selectedDrawable,
                         NormerDrawableMaker m) {
        super(context);
        this.indiCount = indiCount;
        this.standerDrawable = selectedDrawable;
        normerDrawables = new ArrayList<>(indiCount);
        for (int i = 0; i < indiCount; i++) {
            normerDrawables.add(m.getNormerDrawable(i));
        }
        //设置可移动的图片。
        setMoverDrawable(selectedDrawable);
    }
    //////////////////////////////////////////////////

    /**
     * 获取 常态的点 的接口。
     *
     * 通过该接口获取指示器点的数量 个drawable。
     */
    public interface NormerDrawableMaker {
        /**
         * 获取常态点的drawable。
         *
         * @param dex 常态的点 的下标。
         * @return 常态的点的drawable。
         */
        Drawable getNormerDrawable(int dex);
    }

    //////////////////////////////////////////////////x

    /**
     * 指示器显示指定下标的点。
     *
     * @param dex 选中的点 的下标。
     */
    public void indicate(int dex) {
        dex = MathUT.limit(dex, 0, indiCount - 1);
        selectDex = dex;
        Rect standerRect = getStanderRect(dex, getNormerRect(dex));
        reRect(standerRect);
    }

    /**
     * 动画版的{@link #indicate(int)}。
     */
    public void animIndicate(int dex) {
        animIndicate(dex, 200);
    }

    /**
     * 动画版的{@link #indicate(int)}。
     */
    public void animIndicate(int dex, int duration) {
        dex = MathUT.limit(dex, 0, indiCount - 1);
        selectDex = dex;
        Rect standerRect = getStanderRect(dex, getNormerRect(dex));
        if (DEBUG) {
            Log.e("TAG", getClass() + "#animIndicate:" +
                    " selectDex = " + selectDex + " ," +
                    " standerRect = " + standerRect + " 。");
        }
        animReRect(standerRect, duration);
    }
    //--------------------------------------------------

    /**
     * 获取 选中的点 的下标。
     */
    public int getSelectDex() {
        return selectDex;
    }

    //////////////////////////////////////////////////

    /**
     * 让子类调整所有 常态的点 的rect。
     * <p>
     * 友情提示：该方法中可以使用以下方法
     * {@link View#getWidth()}{@link View#getHeight()}。
     *
     * @param changed    布局是否发生改变。
     * @param usableRect 父容器可用的布局范围。
     */
    protected abstract void onCalculateNormersRect(boolean changed, Rect usableRect);

    /**
     * 让子类调整 选中的点 的rect。
     * <p>
     * 友情提示：该方法中可以使用以下方法
     * {@link View#getWidth()}{@link View#getHeight()}。
     *
     * @param changed    布局是否发生改变。
     * @param usableRect
     * @param selectDex  选中的点的下标。
     */
    protected abstract void onCalculateStanderRect(boolean changed, Rect usableRect, int selectDex);


    /**
     * 在该方法中调整 常态的点 的rect。
     *
     * @param changed 布局是否发生改变。
     */
    protected abstract void onApplyNormerRect(boolean changed);

    /**
     * 该方法中调整 选中的点的rect。
     * 通过以下方法设置选中的点的rect：
     * {@link #reRect}{@link #setStanderRect(Rect)}。
     *
     * @param changed 布局是否发生改变。
     * @param dex     选中的点的下标。
     */
    protected abstract void onApplyStanderRect(boolean changed, int dex);

    /**
     * 通过下标获取 常态的点。
     *
     * @param dex 常态的点 的下标。
     * @return 指定下标的 常态的点。
     */
    protected abstract Rect getNormerRect(int dex);

    /**
     * 获取指定的 选中的点 的rect。
     *
     * @param dex 该点的下标。
     * @return 该下标 常态的点 的rect。
     */
    protected abstract Rect getStanderRect(int dex, Rect normerRect);
    //////////////////////////////////////////////////
    /**
     * 获取 常态的点 的drawable。
     *
     * @param dex 常态点的下标。
     * @return 常态的点 的drawable。
     */
    protected Drawable getNormerDrawable(int dex) {
        return normerDrawables.get(dex);
    }

    /**
     * 获取选中的点的drawable。
     */
    protected Drawable getStanderDrawable() {
        return standerDrawable;
    }

    /**
     * 设置选中的点的rect。
     *
     * @param rect 选中的点的rect。
     */
    protected void setStanderRect(Rect rect) {
        reRect(rect);
    }
    //--------------------------------------------------

    @Override
    protected void onCalculateAllDrawing(boolean changed) {
        super.onCalculateAllDrawing(changed);
        Rect usableRect = getUsableRect();
        onCalculateNormersRect(changed, usableRect);
        onCalculateStanderRect(changed, usableRect, selectDex);
    }

    @Override
    protected void onApplyDrawingParams(boolean changed) {
        super.onApplyDrawingParams(changed);
        onApplyNormerRect(changed);
        onApplyStanderRect(changed, selectDex);
    }

    /**
     * 绘制 选中的点 之前，绘制 常态点。
     */
    @Override
    protected void preDrawMover(Canvas canvas) {
        super.preDrawMover(canvas);
        preDrawNormers(canvas);
        onDrawNormers(canvas);
    }

    /**
     * 该方法在以下方法之前调用：
     * {@link #onDrawNormers(Canvas)}
     */
    protected void preDrawNormers(Canvas canvas) {
    }

    /**
     * 该方法中绘制所有 常态的点。
     */
    protected void onDrawNormers(Canvas canvas) {
        for (int i = 0; i < indiCount; i++) {
            onDrawNormer(canvas, i);
        }
    }

    /**
     * 该方法中绘制指定下标的 常态的点。
     *
     * @param dex 点的下标。
     */
    protected void onDrawNormer(Canvas canvas, int dex) {
        Drawable d = getNormerDrawable(dex);
        d.draw(canvas);
    }
}

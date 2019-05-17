package wclass.android.ui.view.mover_view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;

import wclass.android.ui.view.base_view.BaseCustomView;
import wclass.android.util.AnimatorUT;

/**
 * @作者 做就行了！
 * @时间 2019-04-03下午 2:50
 * @该类描述： -
 * 1、一个图片可以显示在view中的任何位置。
 * 2、通过动画改变图片位置的方法：
 * {@link #animReHori}{@link #animReVerti}。
 * （其实还可以直接进行rect整体变化的，我先不写了。）
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 * todo
 * 1、动画时设置bounds写在draw方法中，动画中只做标记作用。
 */
public class MoverView extends BaseCustomView {
    private static final boolean DRAW_DEBUG = false;
    private static final boolean ANIM_DEBUG = false;
    //////////////////////////////////////////////////
    /**
     * 可移动图片的rect。
     */
    Rect moverDrawableRect = new Rect();
    /**
     * 可移动的图片。
     */
    private Drawable moverDrawable;
    /**
     * onDraw方法中，判断是否需要调整drawable的rect。
     */
    private boolean needReRect = false;
    /**
     * 标记：{@link MoverView#needReRect}是否需要设置为false。
     */
    private boolean needReset = false;

    //////////////////////////////////////////////////
    public MoverView(Context context) {
        super(context);
    }
    //////////////////////////////////////////////////

    /**
     * 获取moverDrawable的rect。
     */
    public Rect getMoverDrawableRect() {
        return moverDrawableRect;
    }
    //////////////////////////////////////////////////

    @Override
    protected void onDrawAll(Canvas canvas) {
        super.onDrawAll(canvas);
        if (moverDrawable != null) {
            //需要重新设置rect。
            if (needReRect) {
                onDrawStepAdjustDrawableRect(moverDrawableRect);
                moverDrawable.setBounds(moverDrawableRect);
                if(needReset){
                    needReRect = false;
                }
            }
            preDrawMover(canvas);
            onDrawMover(canvas, moverDrawable);
        }
    }

    /**
     * 绘制阶段最后一次调整rect。
     * @param moverDrawableRect 移动的图片的rect。
     */
    protected void onDrawStepAdjustDrawableRect(Rect moverDrawableRect) {
        moverDrawableRect.top = 0;
        moverDrawableRect.bottom= getHeight();
    }

    /**
     * 该方法中绘制moverDrawable。
     */
    protected void onDrawMover(Canvas canvas, Drawable moverDrawable) {
        if (DRAW_DEBUG) {
            Log.e("TAG",getClass()+"#onDrawMover:  ");
        }
        moverDrawable.draw(canvas);
    }

    /**
     * 绘制moverDrawable之前，该方法被调用。
     */
    protected void preDrawMover(Canvas canvas) {

    }

    //--------------------------------------------------

    /**
     * 设置moverDrawable的rect，并刷新显示。
     */
    private void setMoverBoundsOptimize() {
        if (moverDrawable != null) {
            needReRect = true;
            cleanAnim();
            invalidate();
        }
    }

    //////////////////////////////////////////////////
    /*动画相关。*/
    /**
     * 操作moverDrawable rect的动画对象。
     */
    private ValueAnimator valueAnimator;

    /**
     * 清除动画。
     */
    protected void cleanAnim() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator = null;
        }
    }

    /**
     * {@link MoverView#animReHori(int, int, long)}
     */
    public void animReHori(int dstLeft, int dstRight) {
        animReHori(dstLeft, dstRight, 200);
    }

    /**
     * 动画改变moverDrawable rect的横向属性。
     *
     * @param dstLeft  目标left。
     * @param dstRight 目标right。
     * @param duration 动画持续时间。
     */
    protected void animReHori(int dstLeft, int dstRight, long duration) {
        if(ANIM_DEBUG){
            duration = 2000;
        }
        int startLeft = moverDrawableRect.left;
        int startRight = moverDrawableRect.right;
        int leftCut = dstLeft - startLeft;
        int rightCut = dstRight - startRight;
        flagAnimStart();
        valueAnimator = AnimatorUT.forProgressPercentage(duration, new AnimatorUT.Update() {
            @Override
            public void onUpdate(float progress) {
                int currLeft = (int) (startLeft + progress * leftCut + 0.5f);
                int currRight = (int) (startRight + progress * rightCut + 0.5f);
                moverDrawableRect.left = currLeft;
                moverDrawableRect.right = currRight;
                invalidate();
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onEnd() {
                flagAnimFinish();
            }

            @Override
            public void onCancel() {
                flagAnimCancel();
            }
        });
    }

    /**
     * {@link MoverView#animReVerti(int, int, long)}
     */
    protected void animReVerti(int dstTop, int dstBottom) {
        animReVerti(dstTop, dstBottom, 200);
    }

    /**
     * 动画改变moverDrawable rect的纵向属性。
     *
     * @param dstTop    目标top。
     * @param dstBottom 目标bottom。
     * @param duration  动画持续时间。
     */
    protected void animReVerti(int dstTop, int dstBottom, long duration) {
        if(duration<0){
            duration = 0;
        }
        int startTop = moverDrawableRect.top;
        int startBottom = moverDrawableRect.bottom;
        int topCut = dstTop - startTop;
        int bottomCut = dstBottom - startBottom;
        flagAnimStart();
        valueAnimator = AnimatorUT.forProgressPercentage(duration, new AnimatorUT.Update() {
            @Override
            public void onUpdate(float progress) {
                int currTop = (int) (startTop + progress * topCut + 0.5f);
                int currBottom = (int) (startBottom + progress * bottomCut + 0.5f);
                moverDrawableRect.top = currTop;
                moverDrawableRect.bottom = currBottom;
                invalidate();
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onEnd() {
                flagAnimFinish();
            }

            @Override
            public void onCancel() {
                flagAnimCancel();
            }
        });
    }

    private void flagAnimStart() {
        cleanAnim();
        needReRect = true;
        needReset = false;
    }

    private void flagAnimFinish() {
        valueAnimator = null;
        needReset = true;
        if(ANIM_DEBUG){
            Log.e("TAG",getClass()+"#flagAnimFinish:" +
                    " moverDrawableRect = "+moverDrawableRect+" 。");
        }
    }

    private void flagAnimCancel() {
        needReset = true;
    }

    /**
     * 动画改变moverDrawable rect。
     *
     * @param dstRect 目标rect属性。
     */
    protected void animReRect(Rect dstRect) {
        animReRect(dstRect, 200);
    }

    /**
     * 动画改变moverDrawable rect。
     *
     * @param dstRect  目标rect属性。
     * @param duration 动画持续时间。
     */
    protected void animReRect(Rect dstRect, long duration) {
        animReRect(dstRect.left, dstRect.right, dstRect.top, dstRect.bottom,
                duration);
    }

    /**
     * 动画改变moverDrawable rect。
     *  @param dstLeft   目标left。
     * @param dstTop    目标top。
     * @param dstRight  目标right。
     * @param dstBottom 目标bottom。
     */
    public void animReRect(int dstLeft, int dstTop, int dstRight,
                           int dstBottom) {
        animReRect(dstLeft, dstRight, dstTop, dstBottom, 200);
    }

    /**
     * 动画改变moverDrawable rect。
     *
     * @param dstLeft   目标left。
     * @param dstRight  目标right。
     * @param dstTop    目标top。
     * @param dstBottom 目标bottom。
     * @param duration  动画持续时间。
     */
    protected void animReRect(int dstLeft, int dstRight,
                              int dstTop, int dstBottom, long duration) {
        if(duration<0){
            duration = 0;
        }
        int startLeft = moverDrawableRect.left;
        int startRight = moverDrawableRect.right;
        int startTop = moverDrawableRect.top;
        int startBottom = moverDrawableRect.bottom;

        int leftCut = dstLeft - startLeft;
        int rightCut = dstRight - startRight;
        int topCut = dstTop - startTop;
        int bottomCut = dstBottom - startBottom;
        flagAnimStart();
        valueAnimator = AnimatorUT.forProgressPercentage(duration, new AnimatorUT.Update() {
            @Override
            public void onUpdate(float progress) {
                int currLeft = (int) (startLeft + progress * leftCut + 0.5f);
                int currRight = (int) (startRight + progress * rightCut + 0.5f);
                int currTop = (int) (startTop + progress * topCut + 0.5f);
                int currBottom = (int) (startBottom + progress * bottomCut + 0.5f);
                moverDrawableRect.left = currLeft;
                moverDrawableRect.right = currRight;
                moverDrawableRect.top = currTop;
                moverDrawableRect.bottom = currBottom;
                invalidate();
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onEnd() {
                flagAnimFinish();
            }

            @Override
            public void onCancel() {
                flagAnimCancel();
            }
        });
    }
    //////////////////////////////////////////////////

    /**
     * 获取moverDrawable。
     */
    protected Drawable getMoverDrawable() {
        return moverDrawable;
    }

    /**
     * 获取moverDrawable的rect的left。
     */
    protected int left() {
        return moverDrawableRect.left;
    }

    /**
     * 获取moverDrawable的rect的top。
     */
    protected int top() {
        return moverDrawableRect.top;
    }

    /**
     * 获取moverDrawable的rect的right。
     */
    protected int right() {
        return moverDrawableRect.right;
    }

    /**
     * 获取moverDrawable的rect的bottom。
     */
    protected int bottom() {
        return moverDrawableRect.bottom;
    }
    //--------------------------------------------------
    /*设置相关。*/

    /**
     * 设置moverDrawable的图片。
     *
     * @param drawable 图片。
     */
    public void setMoverDrawable(Drawable drawable) {
        this.moverDrawable = drawable;
    }


    /**
     * 重新调整moverDrawable的rect。
     */
    public void reRect(Rect rect) {
        moverDrawableRect.set(rect);
        setMoverBoundsOptimize();
    }

    /**
     * 重新调整moverDrawable的rect。
     */
    public void reRect(int left,int top,int right,int bottom) {

        moverDrawableRect.set(left,top,right,bottom);
        setMoverBoundsOptimize();
    }

    /**
     * 重新调整moverDrawable rect的left属性。
     * <p>
     * 调用完请必须调用{@link MoverView#apply()}刷新。
     *
     * @param left 设置rect的left。
     * @return this
     */
    protected MoverView reLeft(int left) {
        moverDrawableRect.left = left;
        return this;
    }


    /**
     * 重新调整moverDrawable rect的top属性。
     * <p>
     * 调用完请必须调用{@link MoverView#apply()}刷新。
     *
     * @param top 设置rect的top。
     * @return this
     */
    protected MoverView reTop(int top) {
        moverDrawableRect.top = top;
        return this;
    }


    /**
     * 重新调整moverDrawable rect的right属性。
     * <p>
     * 调用完请必须调用{@link MoverView#apply()}刷新。
     *
     * @param right 设置rect的right。
     * @return this
     */
    protected MoverView reRight(int right) {
        moverDrawableRect.right = right;
        return this;
    }

    /**
     * 重新调整moverDrawable rect的bottom属性。
     * <p>
     * 调用完请必须调用{@link MoverView#apply()}刷新。
     *
     * @param bottom 设置rect的bottom。
     * @return this
     */
    protected MoverView reBottom(int bottom) {
        moverDrawableRect.bottom = bottom;
        return this;
    }

    /**
     * 重新调整横向的绘制区域。
     *
     * @param left  moverDrawable rect的left属性。
     * @param right moverDrawable rect的right属性。
     */
    public void reHori(int left, int right) {
        moverDrawableRect.left = left;
        moverDrawableRect.right = right;
        apply();
    }

    /**
     * 重新调整纵向的绘制区域。
     *
     * @param top    moverDrawable rect的top属性。
     * @param bottom moverDrawable rect的bottom属性。
     */
    public void reVerti(int top, int bottom) {
        moverDrawableRect.top = top;
        moverDrawableRect.bottom = bottom;
        apply();
    }
    //////////////////////////////////////////////////
    public MoverView prepareHori(int left, int right) {
        moverDrawableRect.left = left;
        moverDrawableRect.right = right;
        return this;
    }
    public MoverView prepareVerti(int top, int bottom) {
        moverDrawableRect.top = top;
        moverDrawableRect.bottom = bottom;
        return this;
    }
    //////////////////////////////////////////////////

    /**
     * 应用moverDrawable的rect。
     */
    public void apply() {
        setMoverBoundsOptimize();
    }
}

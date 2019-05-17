//package wclass.android.ui.view.rover_view;
//
//import android.animation.ValueAnimator;
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Rect;
//import android.graphics.drawable.Drawable;
//import android.util.Log;
//
//import wclass.android.ui.view.base_view.BaseCustomView;
//import wclass.android.util.AnimatorUT;
//
///**
// * @作者 做就行了！
// * @时间 2019-04-03下午 2:50
// * @该类描述： -
// * 1、一个图片可以显示在view中的任何位置。
// * 2、通过动画改变图片位置的方法：
// * {@link #animReHori}{@link #animReVerti}。
// * （其实还可以直接进行rect整体变化的，我先不写了。）
// * @名词解释： -
// * @该类用途： -
// * @注意事项： -
// * @使用说明： -
// * @思维逻辑： -
// * @优化记录： -
// * @待解决： -
// * todo
// * 1、2019年4月21日00:29:41
// * view的大小改变时，drawable的rect如何调整？？？
// */
//public class MoverViewOld extends BaseCustomView {
//    private static final boolean DEBUG = true;
//    /**
//     * 漫游图片的rect。
//     */
//    Rect drawableRect = new Rect();
//    /**
//     * 漫游的图片。
//     */
//    private Drawable moverDrawable;
//    private boolean needReRect = true;
//
//    //////////////////////////////////////////////////
//    public MoverViewOld(Context context) {
//        super(context);
//    }
//    //////////////////////////////////////////////////
//
//    /**
//     * 获取moverDrawable的rect。
//     */
//    public Rect getMoverDrawableRect() {
//        return drawableRect;
//    }
//    //////////////////////////////////////////////////
//
//    @Override
//    protected void onApplyDrawingParams(boolean changed) {
//        super.onApplyDrawingParams(changed);
//        onApplyMoverRect(changed, needReRect);
//    }
//
//    protected void onApplyMoverRect(boolean changed, boolean needReRect) {
//        if (changed || needReRect) {
//            moverDrawable.setBounds(drawableRect);
//        }
//    }
//
//    @Override
//    protected void onDrawAll(Canvas canvas) {
//        super.onDrawAll(canvas);
//        if (moverDrawable != null) {
//            preDrawMover(canvas);
//            onDrawMover(canvas, moverDrawable);
//        }
//    }
//
//    /**
//     * 该方法中绘制moverDrawable。
//     */
//    protected void onDrawMover(Canvas canvas, Drawable moverDrawable) {
//        if (DEBUG) {
//            Log.e("TAG", getClass() + "#onDrawMover");
//        }
//        moverDrawable.draw(canvas);
//    }
//
//    /**
//     * 绘制moverDrawable之前，该方法被调用。
//     */
//    protected void preDrawMover(Canvas canvas) {
//
//    }
//
//    //--------------------------------------------------
//
//    /**
//     * 设置moverDrawable的rect，并刷新显示。
//     */
//    private void setMoverBoundsOptimize() {
//        if (moverDrawable != null) {
//            cleanAnim();
//            needReRect = true;
//            invalidate();
//        }
//    }
//
//    //////////////////////////////////////////////////
//    /*动画相关。*/
//    /**
//     * 操作moverDrawable rect的动画对象。
//     */
//    private ValueAnimator valueAnimator;
//
//    /**
//     * 清除动画。
//     */
//    protected void cleanAnim() {
//        if (valueAnimator != null) {
//            valueAnimator.cancel();
//        }
//    }
//
//    /**
//     * {@link MoverViewOld#animReHori(int, int, long)}
//     */
//    public void animReHori(int dstLeft, int dstRight) {
//        animReHori(dstLeft, dstRight, 200);
//    }
//
//    /**
//     * 动画改变moverDrawable rect的横向属性。
//     *
//     * @param dstLeft  目标left。
//     * @param dstRight 目标right。
//     * @param duration 动画持续时间。
//     */
//    public void animReHori(int dstLeft, int dstRight, long duration) {
//
//        int startLeft = drawableRect.left;
//        int startRight = drawableRect.right;
//        int leftCut = dstLeft - startLeft;
//        int rightCut = dstRight - startRight;
//        valueAnimator = AnimatorUT.forProgressPercentage(duration, new AnimatorUT.Update() {
//            @Override
//            public void onUpdate(float progress) {
//                int currLeft = (int) (startLeft + progress * leftCut + 0.5f);
//                int currRight = (int) (startRight + progress * rightCut + 0.5f);
//                drawableRect.left = currLeft;
//                drawableRect.right = currRight;
//                needReRect = true;
//                invalidate();
//            }
//
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onEnd() {
//                valueAnimator = null;
//                needReRect = false;
//            }
//
//            @Override
//            public void onCancel() {
//                valueAnimator = null;
//                needReRect = false;
//            }
//        });
//    }
//
//    /**
//     * {@link MoverViewOld#animReVerti(int, int, long)}
//     */
//    public void animReVerti(int dstTop, int dstBottom) {
//        animReVerti(dstTop, dstBottom, 200);
//    }
//
//    /**
//     * 动画改变moverDrawable rect的纵向属性。
//     *
//     * @param dstTop    目标top。
//     * @param dstBottom 目标bottom。
//     * @param duration  动画持续时间。
//     */
//    public void animReVerti(int dstTop, int dstBottom, long duration) {
//        int startTop = drawableRect.top;
//        int startBottom = drawableRect.bottom;
//        int topCut = dstTop - startTop;
//        int bottomCut = dstBottom - startBottom;
//        valueAnimator = AnimatorUT.forProgressPercentage(duration, new AnimatorUT.Update() {
//            @Override
//            public void onUpdate(float progress) {
//                int currTop = (int) (startTop + progress * topCut + 0.5f);
//                int currBottom = (int) (startBottom + progress * bottomCut + 0.5f);
//                drawableRect.top = currTop;
//                drawableRect.bottom = currBottom;
//                needReRect = true;
//                invalidate();
//            }
//
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onEnd() {
//                valueAnimator = null;
//                needReRect = false;
//            }
//
//            @Override
//            public void onCancel() {
//                valueAnimator = null;
//                needReRect = false;
//            }
//        });
//    }
//
//    //////////////////////////////////////////////////
//
//    /**
//     * 获取moverDrawable。
//     */
//    public Drawable getMoverDrawable() {
//        return moverDrawable;
//    }
//
//    /**
//     * 获取moverDrawable的rect的left。
//     */
//    public int left() {
//        return drawableRect.left;
//    }
//
//    /**
//     * 获取moverDrawable的rect的top。
//     */
//    public int top() {
//        return drawableRect.top;
//    }
//
//    /**
//     * 获取moverDrawable的rect的right。
//     */
//    public int right() {
//        return drawableRect.right;
//    }
//
//    /**
//     * 获取moverDrawable的rect的bottom。
//     */
//    public int bottom() {
//        return drawableRect.bottom;
//    }
//    //--------------------------------------------------
//    /*设置相关。*/
//
//    /**
//     * 设置moverDrawable的图片。
//     *
//     * @param drawable 图片。
//     */
//    public void setMoverDrawable(Drawable drawable) {
//        this.moverDrawable = drawable;
//    }
//
//
//    /**
//     * 重新调整moverDrawable的rect。
//     */
//    public void reRect(Rect rect) {
//        drawableRect.set(rect);
//        setMoverBoundsOptimize();
//    }
//    /**
//     * 重新调整moverDrawable的rect。
//     */
//    public void reRect(int left,int top,int right,int bottom) {
//        drawableRect.set(left,top,right,bottom);
//        setMoverBoundsOptimize();
//    }
//
//
//    /**
//     * 重新调整moverDrawable rect的left属性。
//     * <p>
//     * 调用完请必须调用{@link MoverViewOld#apply()}刷新。
//     *
//     * @param left 设置rect的left。
//     * @return this
//     */
//    public MoverViewOld reLeft(int left) {
//        drawableRect.left = left;
//        return this;
//    }
//
//
//    /**
//     * 重新调整moverDrawable rect的top属性。
//     * <p>
//     * 调用完请必须调用{@link MoverViewOld#apply()}刷新。
//     *
//     * @param top 设置rect的top。
//     * @return this
//     */
//    public MoverViewOld reTop(int top) {
//        drawableRect.top = top;
//        return this;
//    }
//
//
//    /**
//     * 重新调整moverDrawable rect的right属性。
//     * <p>
//     * 调用完请必须调用{@link MoverViewOld#apply()}刷新。
//     *
//     * @param right 设置rect的right。
//     * @return this
//     */
//    public MoverViewOld reRight(int right) {
//        drawableRect.right = right;
//        return this;
//    }
//
//    /**
//     * 重新调整moverDrawable rect的bottom属性。
//     * <p>
//     * 调用完请必须调用{@link MoverViewOld#apply()}刷新。
//     *
//     * @param bottom 设置rect的bottom。
//     * @return this
//     */
//    public MoverViewOld reBottom(int bottom) {
//        drawableRect.bottom = bottom;
//        return this;
//    }
//
//    /**
//     * 重新调整横向的绘制区域。
//     *
//     * @param left  moverDrawable rect的left属性。
//     * @param right moverDrawable rect的right属性。
//     */
//    public void reHori(int left, int right) {
//        drawableRect.left = left;
//        drawableRect.right = right;
//        apply();
//    }
//
//    /**
//     * 重新调整纵向的绘制区域。
//     *
//     * @param top    moverDrawable rect的top属性。
//     * @param bottom moverDrawable rect的bottom属性。
//     */
//    public void reVerti(int top, int bottom) {
//        drawableRect.top = top;
//        drawableRect.bottom = bottom;
//        apply();
//    }
//
//    /**
//     * 应用moverDrawable的rect。
//     */
//    public void apply() {
//        setMoverBoundsOptimize();
//    }
//
//}

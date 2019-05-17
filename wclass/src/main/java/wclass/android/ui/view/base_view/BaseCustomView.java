package wclass.android.ui.view.base_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import wclass.android.util.RectUT;

/**
 * @作者 做就行了！
 * @时间 2019-04-07下午 5:36
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class BaseCustomView extends View {
    private boolean mLayoutChanged;

    /**
     * 可用的布局空间。
     */
    Rect usableRect = new Rect();

    public BaseCustomView(Context context) {
        super(context);
    }

    public BaseCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseCustomView(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //////////////////////////////////////////////////

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(w==0||h==0){
            return;
        }
        onSizeChangedSafely(w,h);
    }

    protected void onSizeChangedSafely(int w, int h){

    }
    //////////////////////////////////////////////////
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        onInit();
        mLayoutChanged |= changed;
        getDrawingRect(usableRect);
        RectUT.delPadding(usableRect, usableRect, this);
        onCalculateAllDrawing(mLayoutChanged);
    }

    protected void onInit() {

    }

    /**
     * 获取可用的布局空间。
     */
    public Rect getUsableRect() {
        return usableRect;
    }
    //--------------------------------------------------

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        onApplyDrawingParams(mLayoutChanged);
        onDrawAll(canvas);
        mLayoutChanged = false;
    }

    /**
     * 在该方法中应用所有的绘制参数。
     * @param changed 布局是否改变。
     *                友情提示：容器宽高、padding改变时，
     *                被视为容器布局改变。
     */
    protected void onApplyDrawingParams(boolean changed) {

    }

    /**
     * 绘制之前统一计算。
     *
     * @param changed 布局是否改变。
     *                友情提示：容器宽高、padding改变时，
     *                被视为容器布局改变。
     */
    protected void onCalculateAllDrawing(boolean changed) {
    }

    /**
     * 请在该方法中绘制您需要的内容。
     */
    protected void onDrawAll(Canvas canvas) {
    }
}

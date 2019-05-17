package wclass.common;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;

/**
 * todo 将该类独立出来。（rowInfo是子类的属性。）
 */
@SuppressWarnings("WeakerAccess")
public class Holder {
    /*保留。*/
    public View view;//holder持有的view。
    public int viewW, viewH;//view的宽高。
    //view的上下左右外边距。
    public int leftMargin, rightMargin, topMargin, bottomMargin;
    //--------------------------------------------------
    /**
     * 布局时的left。
     */
    int layLeft;

    /**
     * 布局时的top。
     */
    int layTop;
    //////////////////////////////////////////////////
    /*构造方法。*/

    /**
     * {@link Holder#Holder(View, int, int, LayoutParams, int, int)}
     */
    public Holder(View view) {
        this.view = view;
    }

    /**
     * {@link Holder#Holder(View, int, int, LayoutParams, int, int)}
     */
    public Holder(View view, int viewW, int viewH, LayoutParams p1) {
        this(view, viewW, viewH, p1, 0, 0);
    }

    /**
     * 构造方法。
     *
     * @param view  holder持有的view。
     * @param viewW view的宽。
     * @param viewH view的高。
     * @param p1    view的布局参数。
     * @param layX  布局时的X。该属性包含view的left外边距。
     * @param layY  布局时的X。该属性包含view的top外边距。
     */
    public Holder(View view, int viewW, int viewH, LayoutParams p1
            , int layX, int layY) {
        this.view = view;
        constructor(viewW, viewH, p1, layX, layY);
    }

    private void constructor(int viewW, int viewH, LayoutParams p1, int layX, int layY) {
        if (p1 instanceof MarginLayoutParams) {
            MarginLayoutParams p = (MarginLayoutParams) p1;
            leftMargin = p.leftMargin;
            rightMargin = p.rightMargin;
            topMargin = p.topMargin;
            bottomMargin = p.bottomMargin;
        }
        this.viewW = viewW;
        this.viewH = viewH;
        layLeft = layX;
        layTop = layY;
    }

    //--------------------------------------------------

    /**
     * 重新构造该类。
     * 相当于构造方法。
     */
    public void re(int viewW, int viewH, LayoutParams p1
            , int layX, int layY) {
        constructor(viewW, viewH, p1, layX, layY);
    }

    /**
     * 设置布局时的坐标。
     *
     * @param layX 布局时的X坐标，也就是left属性。
     * @param layY 布局时的Y坐标，也就是top属性。
     */
    public void setLayoutXY(int layX, int layY) {
        layLeft = layX;
        layTop = layY;
    }

    //////////////////////////////////////////////////
    @Override
    public String toString() {
        String s = "\nlayLeft = " + layLeft + "" +
                " ,layTop = " + layTop + "" +
                " ,layW = " + layW() + "" +
                " ,layH = " + layH() + "" +
                " ,layRight = " + layRight() + "" +
                " ,layBottom = " + layBottom() + " ," + "\n" +
                "viewX = " + viewX() + "" +
                " ,viewY = " + viewY() + "" +
                " ,viewW = " + viewW + "" +
                " ,viewH = " + viewH + " 。";
        return s;
    }

    //--------------------------------------------------

    /**
     * 布局item。
     */
    public final void layout() {
        view.layout(viewX(), viewY(),viewX()+viewW,viewY()+viewH);
    }
    //--------------------------------------------------

    /**
     * view的X坐标。
     */
    public final int viewX() {
        return layLeft() + leftMargin;
    }

    /**
     * view的Y坐标。
     */
    public final int viewY() {
        return layTop() + topMargin;
    }
    //--------------------------------------------------

    /**
     * 布局时的 Left。
     */
    public final int layLeft() {
        return layLeft;
    }

    /**
     * 布局时的 Top。
     */
    public final int layTop() {
        return layTop;
    }

    /**
     * 布局时的 Right。
     */
    public final int layRight() {
        return layLeft + layW();
    }

    /**
     * 布局时的 Bottom。
     */
    public final int layBottom() {
        return layTop + layH();
    }

    /**
     * 布局时的宽。
     */
    public final int layW() {
        return viewW + leftMargin + rightMargin;
    }

    /**
     * 布局时的高。
     */
    public final int layH() {
        return viewH + topMargin + bottomMargin;
    }

}
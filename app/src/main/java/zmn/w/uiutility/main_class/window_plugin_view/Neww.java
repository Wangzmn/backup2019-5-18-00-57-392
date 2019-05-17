package zmn.w.uiutility.main_class.window_plugin_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;

import wclass.y_marks.Study;
import wclass.android.util.ViewUT;
import zmn.w.uiutility.second_class.InterceptLinearVG;

/**
 * @作者 做就行了！
 * @时间 2019-02-16下午 5:38
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 * 1、①第一个触摸点不管怎么移动，第二个触摸点只要符合条件，
 * 就能进入moveWin状态。
 * ②第二个触摸点在范围内触摸，才能触发移动。
 * ③之后的触摸点当成move处理。
 * ④双指只要不内滑就能触发移动。
 * 2、双指内滑时，添加一个功能。
 */
@SuppressWarnings({"unused", "WeakerAccess"})
@SuppressLint("RtlHardcoded")
@Study
public abstract class Neww<T> {
    LinearLayout root;
    LinearLayout child;
    private Context
            context;
    private final int menuCount = 1;
    private int orien;

    public Neww(Context context,Habit habit) {
        this.context = context;
        createView();
        setHabit(habit);
    }

    private void createView() {
        root = new InterceptLinearVG(context);
        root.setOrientation(LinearLayout.HORIZONTAL);
        root.setGravity(Gravity.LEFT | Gravity.BOTTOM);
    }
    enum Habit {
        LEFT_VERTI,
        LEFT_HORI,
        RIGHT_VERTI,
        RIGHT_HORI
    }
    public void setHabit(Habit Habit){
        switch (Habit) {
            case LEFT_VERTI:
                leftVertical();
                break;
            case LEFT_HORI:
                leftHorizontal();
                break;
            case RIGHT_VERTI:
                rightVertical();
                break;
            case RIGHT_HORI:
                rightHorizontal();
                break;
        }
    }
    public void leftVertical(){
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        child.setOrientation(LinearLayout.VERTICAL);
        child.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
    }
    public void leftHorizontal(){
        root.setOrientation(LinearLayout.HORIZONTAL);
        root.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        child.setOrientation(LinearLayout.VERTICAL);
        child.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
    }
    public void rightVertical(){
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        child.setOrientation(LinearLayout.VERTICAL);
        child.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
    }
    public void rightHorizontal(){
        root.setOrientation(LinearLayout.HORIZONTAL);
        root.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        child.setOrientation(LinearLayout.HORIZONTAL);
        child.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
    }

    public void adjustSize(int baseSize) {
        int bigSize = (menuCount + 1) * baseSize;
        switch (orien) {
            case LinearLayout.HORIZONTAL:
                ViewUT.adjustSize(root, bigSize, baseSize);
                break;
            case LinearLayout.VERTICAL:
                ViewUT.adjustSize(root,  baseSize, bigSize);
                break;

        }
        int width = (menuCount + 1) * baseSize;
        ViewUT.adjustSize(root, width, baseSize);
    }

    protected abstract void onExtend();

    protected abstract void onFold();

    protected abstract void onHide();

    protected abstract void getXonScreen();

    protected abstract void getYonScreen();

    protected abstract void onMoveWin();
}

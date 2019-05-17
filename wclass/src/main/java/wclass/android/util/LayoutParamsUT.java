package wclass.android.util;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import wclass.enums.LayoutGravity;

/**
 * @作者 做就行了！
 * @时间 2019-05-15下午 4:52
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 * todo
 */
@SuppressWarnings("WeakerAccess")
public class LayoutParamsUT {

    public static final int MATCH = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int WRAP = ViewGroup.LayoutParams.WRAP_CONTENT;
    //////////////////////////////////////////////////////////////////////
    /*domain 已检查。*/

    /**
     * 生成LinearLayout参数。
     *
     * @param width  宽。
     * @param height 高。
     * @return LinearLayout参数。
     */
    public static LinearLayout.LayoutParams linearParams(int width, int height) {
        return new LinearLayout.LayoutParams(width, height);
    }

    /**
     * 生成LinearLayout参数。
     *
     * @param width  宽。
     * @param height 高。
     * @param weight 权重。
     * @return LinearLayout参数。
     */
    public static LinearLayout.LayoutParams linearParams(int width, int height, float weight) {
        LinearLayout.LayoutParams p = linearParams(width, height);
        p.weight = weight;
        return p;
    }

    public static LinearLayout.LayoutParams linearParamsWidthWeight() {
        return new LinearLayout.LayoutParams(0, 1, 0.00001f);
    }

    public static LinearLayout.LayoutParams linearParamsHeightWeight() {
        return new LinearLayout.LayoutParams(1, 0, 1);
    }

    //////////////////////////////////////////////////
    /*domain frameLayoutParams*/
    public static FrameLayout.LayoutParams frameParams(int width, int height
            , int marginL, int marginT, int marginR, int marginB) {
        FrameLayout.LayoutParams lp = frameParams(width, height);
        lp.setMargins(marginL, marginT, marginR, marginB);
        return lp;
    }

    public static FrameLayout.LayoutParams frameParamsMatchParent(
            int marginL, int marginT, int marginR, int marginB) {
        return frameParams(MATCH, MATCH, marginL, marginT, marginR, marginB);
    }

    public static FrameLayout.LayoutParams frameParamsMatchParent() {
        return frameParams(MATCH, MATCH);
    }
    /**
     * @param width
     * @param height
     * @param gravity {@link Gravity}
     * @return
     */
    public static FrameLayout.LayoutParams frameParams(int width, int height, int gravity) {
        FrameLayout.LayoutParams p = frameParams(width, height);
        p.gravity = gravity;
        return p;
    }

    public static FrameLayout.LayoutParams frameParams(int width, int height) {
        return new FrameLayout.LayoutParams(width, height);
    }

    //--------------------------------------------------
    /*ViewGroupParams*/
    public static ViewGroup.LayoutParams paramsWrapperContent() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static ViewGroup.LayoutParams paramsMatchParent() {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
    }
    /**
     * 获取 布局参数。
     *
     * @param width  宽
     * @param height 高
     * @return 布局参数
     */
    public static ViewGroup.LayoutParams params(int width, int height) {
        return new ViewGroup.LayoutParams(width, height);
    }
    //////////////////////////////////////////////////

    public static void setGravity(LinearLayout root, LayoutGravity layoutGravity) {
        switch (layoutGravity) {
            case LEFT_TOP:
                root.setGravity(Gravity.LEFT | Gravity.TOP);
                break;
            case LEFT_BOTTOM:
                root.setGravity(Gravity.LEFT | Gravity.BOTTOM);
                break;
            case RIGHT_TOP:
                root.setGravity(Gravity.RIGHT | Gravity.TOP);
                break;
            case RIGHT_BOTTOM:
                root.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
                break;
        }
    }

    public static void setGravity(RelativeLayout root, LayoutGravity layoutGravity) {
        switch (layoutGravity) {
            case LEFT_TOP:
                root.setGravity(Gravity.LEFT | Gravity.TOP);
                break;
            case LEFT_BOTTOM:
                root.setGravity(Gravity.LEFT | Gravity.BOTTOM);
                break;
            case RIGHT_TOP:
                root.setGravity(Gravity.RIGHT | Gravity.TOP);
                break;
            case RIGHT_BOTTOM:
                root.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
                break;
        }
    }
}

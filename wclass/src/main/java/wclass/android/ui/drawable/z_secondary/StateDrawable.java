package wclass.android.ui.drawable.z_secondary;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * @作者 做就行了！
 * @时间 2019-03-20下午 3:35
 * @该类描述： -
 * 1、该类为{@link StateListDrawable}的封装。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class StateDrawable extends StateListDrawable {

    public void addState_Normal_Touch_Disable(Drawable drawableNormal,
                                              Drawable drawableTouch,
                                              Drawable drawableDisable){
        addState(new int[]{-android.R.attr.state_enabled},drawableDisable);
        addState(new int[]{android.R.attr.state_pressed},drawableTouch);
        addState(new int[]{},drawableNormal);

    }

    public void addState_Normal_Touch(Drawable drawableNormal, Drawable drawableTouch){
        addState(new int[]{android.R.attr.state_pressed},drawableTouch);
        addState(new int[]{},drawableNormal);
    }

    public void addState_Selected(Drawable drawableSelected){
        addState(new int[]{android.R.attr.state_single},drawableSelected);
    }
}

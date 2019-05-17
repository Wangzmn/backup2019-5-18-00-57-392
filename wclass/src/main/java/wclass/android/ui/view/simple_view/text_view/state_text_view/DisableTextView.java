package wclass.android.ui.view.simple_view.text_view.state_text_view;

import android.content.Context;

import wclass.android.util.StateUT;

/**
 * @作者 做就行了！
 * @时间 2019-05-10下午 11:14
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class DisableTextView extends StateTextView {
    public DisableTextView(Context context, int disableColor, int normColor) {
        super(context, StateUT.make_Disable_Norm(disableColor, normColor));
    }

    public DisableTextView(Context context, int disableColor,
                           int pressColor, int normColor) {
        super(context, StateUT.make_Disable_Press_Norm
                (disableColor, pressColor, normColor));
    }
}

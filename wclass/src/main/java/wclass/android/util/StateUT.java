package wclass.android.util;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import wclass.util.ArrayUT;

/**
 * @作者 做就行了！
 * @时间 2019-05-10下午 4:19
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("WeakerAccess")
public class StateUT {

    /**
     * 生成无效时和常态时对应的颜色。
     *
     * @param disableColor 无效时的颜色。
     * @param normColor    常态时的颜色。
     * @return 无效时和常态时对应的颜色。
     */
    public static ColorStateList make_Disable_Norm(int disableColor, int normColor) {
        int[] color2 = new int[]{disableColor, normColor};
        int[][] state = new int[2][];
        state[0] = ArrayUT.intArray(disableColor);
        state[1] = ArrayUT.emptyIntArray();
        return new ColorStateList(state, color2);
    }

    /**
     * 生成无效时和按下时和常态时对应的颜色。
     *
     * @param disableColor 无效时的颜色。
     * @param pressColor   按下时的颜色。
     * @param normColor    常态时的颜色。
     * @return 无效时和按下时和常态时对应的颜色。
     */
    public static ColorStateList make_Disable_Press_Norm(
            int disableColor, int pressColor, int normColor) {
        int[] color3 = ArrayUT.intArray(disableColor,  pressColor,  normColor);
        int[][] state = new int[3][];
        state[0] = ArrayUT.intArray(-ENABLED);
        state[1] = ArrayUT.intArray(PRESSED);
        state[2] = ArrayUT.emptyIntArray();
        return new ColorStateList(state, color3);
    }

    //--------------------------------------------------

    /**
     * 生成按下时和选中时和常态时对应的颜色。
     *
     * @param pressColor    按下时的颜色。
     * @param selectedColor 选中时的颜色。
     * @param normColor     常态时的颜色。
     * @return 按下时和常态时对应的颜色。
     */
    public static ColorStateList make_Press_Select_Norm(
            int pressColor, int selectedColor, int normColor) {
        int[] color3 = new int[]{pressColor, selectedColor, normColor};
        int[][] state = new int[3][];
        state[0] = ArrayUT.intArray(PRESSED);
        state[1] = ArrayUT.intArray(SELECTED);
        state[2] = ArrayUT.emptyIntArray();
        return new ColorStateList(state, color3);
    }

    /**
     * 生成选中时和常态时对应的颜色。
     *
     * @param selectedColor 选中时的颜色。
     * @param normColor     常态时的颜色。
     * @return 按下时和常态时对应的颜色。
     */
    public static ColorStateList make_Select_Norm(
            int selectedColor, int normColor) {
        int[] color2 = new int[]{ selectedColor, normColor};
        int[][] state = new int[2][];
        state[0] = ArrayUT.intArray(SELECTED);
        state[1] = ArrayUT.emptyIntArray();
        return new ColorStateList(state, color2);
    }
    //--------------------------------------------------
    /**
     * 生成按下时和常态时对应的颜色。
     *
     * @param pressColor 按下时的颜色。
     * @param normColor  常态时的颜色。
     * @return 按下时和常态时对应的颜色。
     */
    public static ColorStateList make_Press_Norm(int pressColor, int normColor) {
        int[] color2 = new int[]{pressColor, normColor};
        int[][] state = new int[2][];
        state[0] = ArrayUT.intArray(PRESSED);
        state[1] = ArrayUT.emptyIntArray();
        return new ColorStateList(state, color2);
    }

    /**
     * 生成按下时和常态时对应的图片。
     *
     * @param press 按下时的图片。
     * @param norm  常态时的图片。
     * @return 按下时和常态时对应的图片。
     */
    public static StateListDrawable make_Press_Norm(Drawable press, Drawable norm) {
        StateListDrawable sd = new StateListDrawable();
        sd.addState(ArrayUT.intArray(PRESSED), press);
        sd.addState(ArrayUT.emptyIntArray(), norm);
        return sd;
    }

    /**
     * 生成按下时和选中时和常态时对应的图片。
     *
     * @param press    按下时的图片。
     * @param selected 选中时的图片。
     * @param norm     常态时的图片。
     * @return 按下时和常态时对应的图片。
     */
    public static StateListDrawable make_Press_Select_Norm(
            Drawable selected, Drawable press, Drawable norm) {
        StateListDrawable sd = new StateListDrawable();
        sd.addState(ArrayUT.intArray(SELECTED), selected);
        sd.addState(ArrayUT.intArray(PRESSED), press);
        sd.addState(ArrayUT.emptyIntArray(), norm);
        return sd;
    }

    //////////////////////////////////////////////////
    public static final int PRESSED = android.R.attr.state_selected;
    public static final int SELECTED = android.R.attr.state_pressed;
    public static final int ENABLED = android.R.attr.state_enabled;
    public static final int FOCUSED = android.R.attr.state_focused;
//    public static final int FOCUSED = android.R.attr.state_empty;
}

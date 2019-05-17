package wclass.android.util;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import wclass.enums.Level3;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static wclass.android.util.LayoutParamsUT.params;

/**
 * @作者 做就行了！
 * @时间 2019-03-20下午 11:24
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 * todo
 * 1、把LayoutParams类型的方法提取出去。
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@SuppressLint("RtlHardcoded")
public class ViewUT {

    /**
     * 将控件大小设置为和父容器一样大。
     *
     * @param view 该控件。
     */
    public static void toMatchParent(View view) {
        adjustSize(view, MATCH_PARENT, MATCH_PARENT);
    }

    /**
     * 将控件大小设置为和父容器一样大。
     *
     * @param view 该控件。
     */
    public static void toMatchParent(TextView view, int fountSize) {
        adjustSize(view, MATCH_PARENT, MATCH_PARENT, fountSize);
    }

    public static void toWrapContent(View view) {
        adjustSize(view, WRAP_CONTENT, WRAP_CONTENT);
    }

    public static void toWrapContent(TextView view, int fountSize) {
        adjustSize(view, WRAP_CONTENT, WRAP_CONTENT, fountSize);
    }
    //////////////////////////////////////////////////

    /**
     * 调整view的大小。
     *
     * @param view   该view
     * @param width  宽
     * @param height 高
     */
    public static void adjustSize(View view, int width, int height) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp == null) {
            lp = params(width, height);
        } else {
            lp.width = width;
            lp.height = height;
        }
        view.setLayoutParams(lp);
    }

    /**
     * 调整view的大小、字体大小。
     *
     * @param tv       该view
     * @param width    宽
     * @param height   高
     * @param fontSize 字体大小
     */
    public static void adjustSize(TextView tv, int width, int height, int fontSize) {
        adjustSize(tv, width, height);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
    }

    /**
     * 调整view的大小。
     *
     * @param view         该view。
     * @param w            宽。
     * @param h            高。
     * @param leftMargin   左外边距。
     * @param topMargin    上外边距。
     * @param rightMargin  右外边距。
     * @param bottomMargin 下外边距。
     */
    public static void adjustSize(View view, int w, int h
            , int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        ViewGroup.MarginLayoutParams ml =
                new ViewGroup.MarginLayoutParams(w, h);
        ml.leftMargin = leftMargin;
        ml.topMargin = topMargin;
        ml.rightMargin = rightMargin;
        ml.bottomMargin = bottomMargin;
        view.setLayoutParams(ml);
    }

    /**
     * 调整字体大小。
     *
     * @param fontSize 字体大小。（单位：像素。）
     */
    public static void adjustFontSize(TextView tv, int fontSize) {
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
    }


    /**
     * 设置字体粗度。
     *
     * @param level 粗度等级。
     */
    public static void setFondBold(TextView tv, Level3 level) {

        switch (level) {
            case NORMAL:
                tv.getPaint().setFakeBoldText(false);
                tv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case BETTER:
                tv.getPaint().setFakeBoldText(true);
                tv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                break;
            case BEST:
                tv.getPaint().setFakeBoldText(false);
                tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                break;
        }
    }

    public static void setSelectedEX(View view, boolean selected) {
        view.setSelected(selected);
        if (view instanceof ViewGroup) {
            ViewGroup root = (ViewGroup) view;
            int childCount = root.getChildCount();
            if (childCount != 0) {
                for (int i = 0; i < childCount; i++) {
                    View child = root.getChildAt(i);
                    setSelectedEX(child, selected);
                }
            }
        }
    }
}
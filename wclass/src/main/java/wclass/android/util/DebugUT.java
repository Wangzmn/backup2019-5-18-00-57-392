package wclass.android.util;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import wclass.android.util.debug.StringUT;
import wclass.util.ColorUT;

/**
 * @作者 做就行了！
 * @时间 2019-03-11下午 4:31
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class DebugUT {

    //////////////////////////////////////////////////

    /**
     * 将view和他的孩子全部设置成随机背景色。
     *
     * @param view 该view。
     */
    public static void randomBG(View view) {
        view.setBackgroundColor(ColorUT.randomColor());
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            int childCount = vg.getChildCount();
            if (childCount != 0) {
                for (int i = 0; i < childCount; i++) {
                    View child = vg.getChildAt(i);
                    randomBG(child);
                }
            }
        }
    }

    /**
     * 打印{@link MotionEvent}所有pointer的id、coor。
     */
    public static String toStr_pointersCoor(MotionEvent ev) {
        int count = ev.getPointerCount();
        StringBuilder sum = new StringBuilder("[触摸点数量为：" + count + " 。\n");
        for (int i = 0; i < count; i++) {
            float x = ev.getX(i);
            float y = ev.getY(i);
            String sub = "触摸点" + i+"：" +
                    "id = " +ev.getPointerId(i)+
                    ", x = " + x +
                    ", y = " + y + " 。";
            if (i != count - 1) {
                sub += "\n";
            }
            sum.append(sub);
        }
        sum.append("]");
        return sum.toString();
    }

}

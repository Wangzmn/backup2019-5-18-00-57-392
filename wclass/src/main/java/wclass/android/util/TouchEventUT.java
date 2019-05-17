package wclass.android.util;

import android.graphics.Rect;
import android.view.MotionEvent;

/**
 * @作者 做就行了！
 * @时间 2019-03-26下午 10:39
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class TouchEventUT {


    /**
     * 在bounds中，获取x/y相对于bounds宽/高的百分比，
     * 并把x百分比和y百分比放入pers数组中。
     *
     * @param event  触摸事件。
     * @param bounds 在该范围中。
     * @param pers   x/y，相对于宽/高，的百分比。
     *               x/y，范围为：0至1。
     */
    public static void getPerInBounds(MotionEvent event, Rect bounds, float[] pers) {
        float x = event.getX();
        float y = event.getY();

        int width = bounds.width();
        int height = bounds.height();

        x = x - bounds.left;
        y = y - bounds.top;

        float xPer = x / width;
        float yPer = y / height;

        if (xPer < 0) {
            xPer = 0;
        } else if (xPer > 1) {
            xPer = 1;
        }
        if (yPer < 0) {
            yPer = 0;
        } else if (yPer > 1) {
            yPer = 1;
        }
        pers[0] = xPer;
        pers[1] = yPer;
    }

}

package wclass.android.util;

import android.view.View;
import android.view.ViewGroup;

/**
 * @作者 做就行了！
 * @时间 2019-05-07上午 12:25
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class LayoutUT {

    public static void layout(View view, int leftTopX, int leftTopY) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        int offsetX = 0;
        int offsetY = 0;
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams lpp = (ViewGroup.MarginLayoutParams) lp;
            offsetX = lpp.leftMargin;
            offsetY = lpp.topMargin;
        }
        leftTopX += offsetX;
        leftTopY += offsetY;
        view.layout(leftTopX, leftTopY,
                leftTopX + view.getMeasuredWidth(),
                leftTopY + view.getMeasuredHeight());
    }
}

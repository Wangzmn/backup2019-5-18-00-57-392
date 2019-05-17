package wclass.android.util.debug;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * @作者 做就行了！
 * @时间 2019-05-16下午 5:11
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class StringUT {

    public static String vgInfo(ViewGroup root) {
        StringBuilder sum = new StringBuilder("rootView:");
        String vgs = toStr(root);
        sum.append(vgs);
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            String vs = "\n" + "子view" + i + ":" + toStr(child);
            sum.append(vs);
        }
        return sum.toString();
    }

    public static String toStr(SparseArray sa) {
        StringBuilder sum = new StringBuilder("[数量为：" + sa.size() + " 。" +
                "键值为：");
        int size = sa.size();
        for (int i = 0; i < size; i++) {
            sum.append(sa.keyAt(i));
            if (i != size - 1) {
                sum.append(", ");
            }
        }
        sum.append(" ]");
        return sum.toString();
    }

    public static String toStr(View view) {
        String s = "[" +
                " x = " + view.getX() +
                ", y = " + view.getY() +
                ", width = " + view.getWidth() +
                ", height = " + view.getHeight() +
                " ]";
        return s;
    }
}

package wclass.android.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import wclass.android.util.ViewUT;

import static wclass.android.util.DebugUT.randomBG;

/**
 * 完成于 2019年3月18日17:32:22
 *
 * @作者 做就行了！
 * @时间 2019-03-18下午 3:57
 * @该类描述： -
 * 1、作为容器控件的itemView。
 * 2、大概样子：一个正方形中，上方显示一个图标，下方显示文字。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("unused")
public class AppItemView {
    private static final boolean DEBUG = false;
    //////////////////////////////////////////////////////////////////////
    private LinearLayout root;//根view。
    private ImageView iconView;//根view中，上方的图标控件。
    private TextView nameView;//根view中，下方的文字控件。

    //////////////////////////////////////////////////////////////////////
    public AppItemView(Context context, int size) {
        root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);

        iconView = new ImageView(context);
        nameView = new TextView(context);
        nameView.setIncludeFontPadding(false);
        nameView.setGravity(Gravity.CENTER);
        nameView.setLines(1);
        nameView.setEllipsize(TextUtils.TruncateAt.MIDDLE);

        root.addView(iconView);
        root.addView(nameView);

        if (DEBUG) {
            randomBG(root);
        }
        adjustSize(size);
    }

    //////////////////////////////////////////////////////////////////////

    /**
     * 调整控件的大小，包括根view、子view。
     *
     * @param size 需要设置的大小。
     */
    public void adjustSize(int size) {
        ViewUT.adjustSize(root, size, size);
        //------------------------------------------------------------
        int padding = size / 20;
        root.setPadding(padding, padding, padding, padding);
        //----------------------------------------------------------------------

        int childSize = size - (padding * 2);
        int iconHeight = childSize * 2 / 3;
        int nameHeight = childSize - iconHeight;
        //----------------------------------------------------------------------
        ViewUT.adjustSize(iconView, childSize, iconHeight);
        //----------------------------------------------------------------------

        ViewUT.adjustSize(nameView, childSize, nameHeight,
                childSize / 5);
    }

    //////////////////////////////////////////////////////////////////////
    public LinearLayout getRoot() {
        return root;
    }

    public ImageView getIconView() {
        return iconView;
    }

    public TextView getNameView() {
        return nameView;
    }
}

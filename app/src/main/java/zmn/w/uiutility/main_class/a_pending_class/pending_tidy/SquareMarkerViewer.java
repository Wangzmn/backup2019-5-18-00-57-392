package zmn.w.uiutility.main_class.a_pending_class.pending_tidy;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import wclass.y_marks.PendingExtend;
import wclass.android.util.ViewUT;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * @作者 做就行了！
 * @时间 2019-03-19下午 2:40
 * @该类描述： -
 * 1、有角标的itemView。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 * 1、该类还需要整理。
 */
@SuppressWarnings("all")
@PendingExtend("实时调节margin。")
public class SquareMarkerViewer {
    private boolean hasMargin;
    private float iconMarginPerRelativeToParent;
    public FrameLayout root;
    public ImageView icon;
    public ImageView corner;
    private float cornerPerRelativeToParent;//角标的大小，占父容器的百分比。

    public SquareMarkerViewer(Context context) {
        this(context, 1 / 3f, 0);
    }
    public SquareMarkerViewer(float iconMarginPerRelativeToParent,
                              Context context) {
        this(context, 1 / 3f, iconMarginPerRelativeToParent);
    }

    public SquareMarkerViewer(Context context, float cornerPerRelativeToParent,
                              float iconMarginPerRelativeToParent) {
        this.cornerPerRelativeToParent = cornerPerRelativeToParent;
        if (iconMarginPerRelativeToParent != 0) {
            hasMargin = true;
            this.iconMarginPerRelativeToParent = iconMarginPerRelativeToParent;
        }
        createView(context);
    }

    protected void onAdjustCornerSize(int width) {
        int width1 = (int) (cornerPerRelativeToParent * width);
        ViewUT.adjustSize(corner,
                width1,
                width1
        );
    }

    protected void onAdjustIconMargin(int w, int h, int margin) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) icon.getLayoutParams();
        lp.setMargins(margin, margin, margin, margin);
        //----------------------------------------------------------------------
//        /*new */
//        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) icon.getLayoutParams();
//        int i = w - 2 * margin;
//        lp.width = i;
//        lp.height= i;
//        icon.setLayoutParams(lp);

    }

    private void createView(Context context) {
        root = new FrameLayout(context) {
            @Override
            protected void onSizeChanged(int w, int h, int oldw, int oldh) {
                super.onSizeChanged(w, h, oldw, oldh);
                onAdjustCornerSize(w);
                if (hasMargin) {
                    onAdjustIconMargin(w,h,(int) (w * iconMarginPerRelativeToParent));
                }
            }
        };
        //----------------------------------------------------------------------
        /*添加图标控件。*/
        icon = new ImageView(context);
        FrameLayout.LayoutParams iconParams = new FrameLayout.LayoutParams
                (MATCH_PARENT, MATCH_PARENT);
        icon.setLayoutParams(iconParams);
        //----------------------------------------------------------------------
//        /*new*/
//        icon = new ImageView(context);
//        FrameLayout.LayoutParams iconParams = new FrameLayout.LayoutParams
//                (0, 0);
//        iconParams.gravity = Gravity.CENTER;
//        icon.setLayoutParams(iconParams);
        //----------------------------------------------------------------------
        /*添加角标控件。*/
        corner = new ImageView(context);
        FrameLayout.LayoutParams cornerParams = new FrameLayout.LayoutParams
                (0, 0, Gravity.TOP | Gravity.RIGHT);
        corner.setLayoutParams(cornerParams);
        corner.setVisibility(View.INVISIBLE);
        //----------------------------------------------------------------------
        root.addView(icon);
        root.addView(corner);
    }

    public void displayRoot() {
        root.setVisibility(View.VISIBLE);
    }

    public void hideRoot() {
        root.setVisibility(View.INVISIBLE);
    }

    /**
     * 检查于 2019年2月3日23:44:53
     * <p>
     * 显示角标。
     */
    public void displayCorner() {
        corner.setVisibility(View.VISIBLE);
    }

    /**
     * 检查于 2019年2月3日23:44:53
     * <p>
     * 隐藏角标。
     */
    public final void hideCorner() {
        corner.setVisibility(View.INVISIBLE);
    }

    /**
     * 检查于 2019年2月3日23:44:53
     * <p>
     * 重置角标缩放大小。
     */
    public final void setCornerScale(float scale) {
        corner.setScaleX(scale);
        corner.setScaleY(scale);
    }
}
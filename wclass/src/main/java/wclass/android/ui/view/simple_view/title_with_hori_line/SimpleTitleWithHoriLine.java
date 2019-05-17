package wclass.android.ui.view.simple_view.title_with_hori_line;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import wclass.android.util.SizeUT;
import wclass.android.util.ViewUT;

/**
 * @作者 做就行了！
 * @时间 2019-05-10上午 12:18
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class SimpleTitleWithHoriLine extends TitleWithHoriLine<TextView> {
    private Context context;
    private int lineColor;
    private int fontColor;
    private int pixFontSize;
    private int lineWidth;
    private int leftMargin;
    private int rightMargin;
    private TextView title;
    private String content;

    public SimpleTitleWithHoriLine(Context context,String content) {
        super(context);
        // 字体大小为2.5毫米。
        int fs = (int) (SizeUT.getMMpixel(context) * 2.5f);
        this.context = context;
        this.lineColor = 0xffF6F6F6;
        this.fontColor = 0xff000000;
        this.pixFontSize = fs;
        lineWidth = 1;
        leftMargin = fs;
        rightMargin = fs;
        this.content = content;
        ViewUT.adjustSize(this, ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        init();
    }

    /**
     * 构造方法。
     *
     * @param context     上下文。
     * @param lineColor   线的颜色。
     * @param lineWidth   线的宽。
     * @param fontColor   字体颜色。
     * @param pixFontSize 字体大小。（单位：像素。）
     * @param leftMargin  中间view的左外边距。
     * @param rightMargin 中间view的右外边距。
     */
    public SimpleTitleWithHoriLine(Context context,
                                   int lineColor, int lineWidth,
                                   int fontColor, int pixFontSize,
                                   int leftMargin, int rightMargin,
                                   String content) {
        super(context);
        if (pixFontSize <= 0) {
            throw new IllegalStateException("pixFontSize不能小于0。");
        }
        if (lineWidth <= 0) {
            throw new IllegalStateException("lineWidth不能小于0。");
        }
        if (leftMargin <= 0) {
            throw new IllegalStateException("leftMargin不能小于0。");
        }
        if (rightMargin <= 0) {
            throw new IllegalStateException("rightMargin不能小于0。");
        }
        this.context = context;
        this.lineColor = lineColor;
        this.fontColor = fontColor;
        this.pixFontSize = pixFontSize;
        this.lineWidth = lineWidth;
        this.leftMargin = leftMargin;
        this.rightMargin = rightMargin;
        this.content = content;
        init();
    }

    @Override
    protected TextView onCreateMidView(Context context) {
        title = new TextView(context);
        title.setTextColor(fontColor);
        title.setText(content);
        ViewUT.toWrapContent(title, pixFontSize);
        return title;
    }

    @Override
    protected int getLineWidth() {
        return lineWidth;
    }

    @Override
    protected int getLineColor() {
        return lineColor;
    }

    protected int getMidViewLeftMargin() {
        return leftMargin;
    }

    protected int getMidViewRightMargin() {
        return rightMargin;
    }
}

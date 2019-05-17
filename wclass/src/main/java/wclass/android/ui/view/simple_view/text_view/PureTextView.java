package wclass.android.ui.view.simple_view.text_view;

import android.content.Context;
import android.widget.TextView;

import wclass.android.util.ViewUT;

/**
 * @作者 做就行了！
 * @时间 2019-05-09下午 10:59
 * @该类描述： -
 * 1、简单的只显示文字的textView。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class PureTextView extends TextView {
    public PureTextView(Context context, String content,
                        int pixFontSize, int fontColor) {
        super(context);
        setIncludeFontPadding(false);
        setTextColor(fontColor);
        setText(content);
        ViewUT.toWrapContent(this,pixFontSize);
    }
}

package layout;

import android.content.Context;
import android.widget.TextView;

import wclass.android.ui.view.title_bar.SimpleTitle;
import wclass.android.util.ViewUT;

/**
 * @作者 做就行了！
 * @时间 2019-05-09下午 5:22
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class LoginTitle extends SimpleTitle<TextView> {

    private TextView title;

    public LoginTitle(Context context) {
        super(context,1,0);
    }

    @Override
    protected TextView onCreateTitle(Context context) {
        return new TextView(context);
    }
    @Override
    protected void onCreateViewsFinish() {
        super.onCreateViewsFinish();
        title = getTitle();
        title.setText("我就是标题");
        title.setIncludeFontPadding(false);
    }

    @Override
    protected void onAdjustViews(int w, int h) {
        super.onAdjustViews(w, h);
        ViewUT.toWrapContent(title,h/2);
    }

}

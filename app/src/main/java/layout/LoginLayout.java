package layout;

import android.content.Context;
import android.view.View;

import wclass.android.ui.view.ui_frame.MainFrame2;

/**
 * @作者 做就行了！
 * @时间 2019-05-09下午 4:25
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class LoginLayout extends MainFrame2 {
    LoginTitle  loginTitle;

    public LoginLayout(Context context) {
        super(context);
    }

    @Override
    protected void onCreateViews() {

    }

    @Override
    protected View onGetHead() {
        return null;
    }

    @Override
    protected View onGetTail() {
        return null;
    }

    @Override
    protected int getHeadHeight() {
        return 0;
    }

    @Override
    protected int getTailHeight() {
        return 0;
    }

}

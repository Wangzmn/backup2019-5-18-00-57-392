package wclass.android.ui.view.title_bar.test;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import wclass.android.ui.view.title_bar.TitleBar;
import wclass.enums.Vertical3;
import wclass.android.util.DebugUT;
import wclass.android.util.ViewUT;
import wclass.common.WH;

/**
 * @作者 做就行了！
 * @时间 2019/5/6 0006
 * @使用说明：
 */
public class AdapterTest extends TitleBar {

    private TextView midMenu;

    TextView l0;
    TextView l1;
    TextView l2;

    TextView r0;
    TextView r1;

    public AdapterTest(Context context) {
        super(context);
    }

    @Override
    public boolean leftsRightsSameSize() {
        return true;
    }

    @Override
    public WH getSameSize() {
        return new WH(70, 70);
    }

    @Override
    public int getItemGap() {
        return 50;
    }

    @Override
    public void onSizeChangeSafely2(int w, int h) {
        int pad = 10;
        setPadding(pad, pad, pad, pad);
        sssss(30, h, midMenu, "我是mid");

        sssss(60, h, l0, "我是left" + 0);
        sssss(30, h, l1, "我是left" + 1);
        sssss(30, h, l2, "我是left" + 2);

        sssss(30, h, r0, "我是right" + 0);
        sssss(60, h, r1, "我是right" + 1);
    }

    @Override
    protected void onAdjustViews(int w, int h) {

    }

    private void sssss(int subW, int h, TextView tv, String s) {
        ViewUT.adjustSize(tv, subW, h / 2
//                , 10, 10, 10, 10
        );
        tv.setIncludeFontPadding(false);
        tv.setText(s);
        DebugUT.randomBG(tv);
    }

    @Override
    public void onCreateViews(Context context) {
        midMenu = new TextView(context);
        l0 = new TextView(context);
        l1 = new TextView(context);
        l2 = new TextView(context);
        r0 = new TextView(context);
        r1 = new TextView(context);
    }

    @Override
    public View onGetMidMenu(Context context) {
        return midMenu;
    }

    @Override
    public View onGetLeftMenu(Context context, int position) {
        switch (position) {
            case 0:
                return l0;
            case 1:
                return l1;
            case 2:
                return l2;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public View onGetRightMenu(Context context, int position) {
        switch (position) {
            case 0:
                return r0;
            case 1:
                return r1;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public int getLeftMenuCount() {
        return 3;
    }

    @Override
    public int getRightMenuCount() {
        return 2;
    }

    @Override
    public Vertical3 getVerticalType() {
//        return Vertical3.TOP;
        return Vertical3.BOTTOM;
    }
}

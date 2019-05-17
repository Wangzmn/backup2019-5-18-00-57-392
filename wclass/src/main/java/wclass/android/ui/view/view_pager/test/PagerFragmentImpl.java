package wclass.android.ui.view.view_pager.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import wclass.android.ui.view.view_pager.PagerFragment;
import wclass.android.util.DebugUT;
import wclass.android.util.ViewUT;

/**
 * @作者 做就行了！
 * @时间 2019/5/4 0004
 * @使用说明：
 */
public class PagerFragmentImpl extends PagerFragment<TextView> {
    private int number;

    public PagerFragmentImpl() {
    }

    @SuppressLint("ValidFragment")
    public PagerFragmentImpl(int number) {
        this.number = number;
    }

    @Override
    protected TextView onCreateViewOptimize(Context context, ViewGroup parent, Bundle savedInstanceState) {
        int pageW = parent.getWidth();
        int pageH = parent.getHeight();
        TextView tv = new TextView(context);
        tv.setText("我是第"+number+"个item。");
//        ViewUT.toMatchParent(tv,pageW/9);
        ViewUT.adjustSize(tv,pageW,pageH,pageW/9);
        DebugUT.randomBG(tv);
//        Log.e("TAG",getClass()+"#getView:" +
//                " position "+number+
//                " pageW "+pageW+
//                " pageH "+pageH        );
        return tv;
    }

    @Override
    protected String getSubName() {
        return "第"+number+"个fragment。";
    }

    @Override
    protected void onAdjustViewState(ViewGroup parent, TextView view, Bundle savedInstanceState) {

    }
}

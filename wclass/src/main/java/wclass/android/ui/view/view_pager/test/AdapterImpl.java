package wclass.android.ui.view.view_pager.test;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import wclass.android.ui.view.view_pager.ViewPager;
import wclass.android.util.DebugUT;
import wclass.android.util.ViewUT;

/**
 * @作者 做就行了！
 * @时间 2019/5/1 0001
 * @使用说明：
 */
public class AdapterImpl extends ViewPager.Adapter<TextView> {
    private int count;

    public AdapterImpl(int count) {
        this.count = count;
    }

    @Override
    public TextView createView(Context context, int position, int pageW, int pageH) {
        TextView tv = new TextView(context);
        tv.setText("我是第"+position+"个item。");
        ViewUT.adjustSize(tv,pageW,pageH,pageW/9);
        DebugUT.randomBG(tv);
        Log.e("TAG",getClass()+"#getView:" +
                " position "+position+
                " pageW "+pageW+
                " pageH "+pageH        );
        return tv;
    }

    @Override
    public void onDetachView(TextView view, int position) {

    }

    @Override
    public int getItemCount() {
        return count;
    }

}

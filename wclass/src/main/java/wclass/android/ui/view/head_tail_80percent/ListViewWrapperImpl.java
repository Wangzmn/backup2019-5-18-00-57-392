package wclass.android.ui.view.head_tail_80percent;

import android.content.Context;

/**
 * @作者 做就行了！
 * @时间 2019-04-16下午 11:52
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class ListViewWrapperImpl extends ListViewWrapper {
    public ListViewWrapperImpl(Context context) {
        super(context);
    }

    Plugin mPlugin;

    public void setPlugin(Plugin plugin) {
        if (mPlugin != null) {
            mPlugin.onDestroy();
        }

        if (plugin != null) {
            plugin.onBindListViewWrapper(this);
        }

        mPlugin = plugin;
    }

    @Override
    protected void onHeadBegin() {
        super.onHeadBegin();
        if (mPlugin != null) {
            mPlugin.onHeadBegin();
        }
    }

    @Override
    protected void onHeadingOverflow(int sumOverflowAbsDelta) {
        if (mPlugin != null) {
            mPlugin.onHeadingOverflow(sumOverflowAbsDelta);
        }
    }

    @Override
    protected void onHeadThumbUp(int sumOverflowAbsDelta) {
        if (mPlugin != null) {
            mPlugin.onHeadThumbUp(sumOverflowAbsDelta);
        }
    }

    @Override
    protected int limitHeadGoingDelta(int sumOverflowAbsDelta) {
        if (mPlugin != null) {
            mPlugin.limitHeadGoingSumDelta(sumOverflowAbsDelta);
        }
        return 0;
    }
}

package wclass.android.ui.view.view_pager.test;

import android.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import wclass.android.ui.view.view_pager.FragmentAdapter;
import wclass.android.ui.view.view_pager.PagerFragment;

/**
 * @作者 做就行了！
 * @时间 2019/5/5 0005
 * @使用说明：
 */
public class FragmentAdapterImpl extends FragmentAdapter {

    private List<PagerFragment> frags = new ArrayList<>();

    public FragmentAdapterImpl(FragmentManager fragmentManager,int count) {
        super(fragmentManager);
        for (int i = 0; i < count; i++) {
            frags.add(new PagerFragmentImpl(i));
        }
    }

    @Override
    protected PagerFragment getPagerFragment(int position) {
        return frags.get(position);
    }

    @Override
    public int getItemCount() {
        return frags.size();
    }
}

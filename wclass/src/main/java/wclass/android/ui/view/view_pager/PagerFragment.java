package wclass.android.ui.view.view_pager;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @作者 做就行了！
 * @时间 2019-05-04下午 4:18
 * @该类描述： -
 * 1、可以提前创建view的fragment ~！！！
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public abstract class PagerFragment<T extends View> extends Fragment {
    private static final boolean DEBUG = true;
    //////////////////////////////////////////////////
    private T mView;

    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if (DEBUG) {
            Log.e("TAG", getClass() + "#onCreateView:  " + getSubName());
        }
        if (mView != null) {
            onAdjustViewState(container, mView, savedInstanceState);
            return mView;
        } else {
            return mView = onCreateViewOptimize(inflater.getContext(), container, savedInstanceState);
        }
    }

    /**
     * 该方法由外界创建后调用一次！！！
     *
     * @param context 上下文。
     * @param parent  fragment持有的view，的容器。
     * @return
     */
    public final T generateView(Context context, ViewGroup parent) {
        if (mView != null) {
            return mView;
        }
        return mView = onCreateViewOptimize(context, parent, null);
    }

    /**
     * 由子类创建view。
     *
     * @param context            上下文。
     * @param parent             父容器。
     * @param savedInstanceState 您保存的状态。
     *                           友情提示：可能为null哦。
     * @return 子类创建的view
     */
    protected abstract T onCreateViewOptimize(Context context, ViewGroup parent, Bundle savedInstanceState);

    /**
     * 由子类调整view至保存的状态。
     *
     * @param parent             父容器。
     * @param view               view。
     * @param savedInstanceState 您保存的状态。
     */
    protected abstract void onAdjustViewState(ViewGroup parent, T view, Bundle savedInstanceState);

    /**
     * 将fragment标记为是否可见。
     *
     * @param visible true：可见。false：fragment不可见。
     */
    public void setVisibility(boolean visible) {
        setMenuVisibility(visible);
        setUserVisibleHint(visible);
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
//                try {
//                    if (mView.getVisibility() != View.VISIBLE) {
//                        mView.setVisibility(View.VISIBLE);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    throw new IllegalStateException("请在与用户交互以后，再调用该方法。");
//                }
            onViewVisible();
        } else {
//                try {
//                    if (mView.getVisibility() != View.INVISIBLE) {
//                        mView.setVisibility(View.INVISIBLE);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    throw new IllegalStateException("请在与用户交互以后，再调用该方法。");
//                }
            onViewInvisible();
        }
    }

    @Nullable
    @Override
    public View getView() {
        return mView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        try {
//            mView.setEnabled(isVisibleToUser);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new IllegalStateException("请在与用户交互以后，再调用该方法。");
//        }
        if (isVisibleToUser) {
            onUserVisible();
        } else {
            onUserInvisible();
        }
    }

    protected void onViewVisible() {
    }

    protected void onViewInvisible() {
    }

    protected void onUserVisible() {
    }

    protected void onUserInvisible() {
    }

    @Override
    public void onResume() {
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onResume:  " + getSubName());
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onPause:  " + getSubName());
        }
        super.onPause();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onCreate:  " + getSubName());
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onViewCreated:  " + getSubName());
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        mView = null;
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onDestroy:  " + getSubName());
        }
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (DEBUG) {
            Log.e("TAG", getClass() + "#onConfigurationChanged:  " + getSubName());
        }
        super.onConfigurationChanged(newConfig);
    }

    protected String getSubName() {
        return "";
    }
}

package wclass.android_support;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import wclass.android.app.AppInfo;
import wclass.android.ui.view.AppItemView;
import wclass.android.util.DebugUT;
import wclass.android.z_pending_class.AppQueryUT;
import wclass.android.util.LayoutParamsUT;
import wclass.android.util.ViewUT;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * @作者 做就行了！
 * @时间 2019-03-18下午 3:45
 * @该类描述： -
 * 1、加载所有App图标的容器，点击发出回调。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("unused")
public class AppsContainerViewer {
    private static final boolean DEBUG = true;
    private static final int DATA_UPDATE_FINISH = 0;
    //////////////////////////////////////////////////////////////////////
    private Context context;
    private Adapter adapter;
    private Callback callback;
    //--------------------------------------------------
    private LinearLayout root;//根view。
    private ProgressBar progressBar;//圆形进度条。
    private RecyclerView rv;//apps的容器。
    private ImageView ivRefresh;//刷新按钮。
    private ImageView ivToStart;//滚动至最前 按钮。
    private ImageView ivToEnd;//滚动至最后 按钮。
    private ImageView ivExit;//退出按钮。
    //--------------------------------------------------
    private boolean isRefresh = false;//true：当前为加载等待状态。

    //////////////////////////////////////////////////////////////////////
    public AppsContainerViewer(Context context, int screenShortWidth,
                               Callback callback) {

    }

    /**
     * 构造方法。
     *
     * @param context          上下文。
     * @param screenShortWidth 屏幕较短边的宽度。
     * @param rv               用户指定的{@link RecyclerView}
     * @param callback         事件的回调。
     */
    public AppsContainerViewer(Context context, int screenShortWidth,
                               RecyclerView rv, Callback callback) {
        this.context = context;
        this.rv = rv;
        createView();
        this.callback = callback;
        adapter = new Adapter(context, AppQueryUT.getAppInfos(context),
                screenShortWidth / 5) {
            @Override
            void onItemClick(AppInfo appInfo) {
                callback.onItemClick(appInfo);
            }
        };
        int rows = 4;
        this.rv.setAdapter(adapter);
        this.rv.setLayoutManager(new GridLayoutManager(context, rows,
                LinearLayoutManager.HORIZONTAL, false));
        this.rv.getLayoutManager().setAutoMeasureEnabled(false);
        this.rv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adjustSize(screenShortWidth);

        if (DEBUG) {
            DebugUT.randomBG(root);
            setPending(true);
        }
    }

    private void adjustSize(int screenShortWidth) {
        int i = (int) ((float) screenShortWidth / 5 * 4);
        ViewUT.adjustSize(root, screenShortWidth, i);
    }

    public interface Callback {

        /**
         * app被点击时的回调。
         *
         * @param appInfo
         */
        void onItemClick(AppInfo appInfo);

        /**
         * 点击退出按钮时的回调。
         */
        void onExit();
    }
    //////////////////////////////////////////////////////////////////////
    /**
     * 数据加载完的通知。
     */
    @SuppressLint("HandlerLeak")
    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case DATA_UPDATE_FINISH:
                        /**
                         * 1、先滑动至头部。
                         * 2、再更新。
                         */
                        isRefresh = false;
                        setPending(false);
                        setEnable(true);
                        rv.scrollToPosition(0);
                        adapter.notifyDataSetChanged();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 滚动至最前。
     */
    private void forToStart() {
        rv.smoothScrollToPosition(0);
    }

    /**
     * 滚动至最后。
     */
    private void forToEnd() {
        rv.smoothScrollToPosition(adapter.getItemCount() - 1);
    }

    /**
     * 退出。
     */
    private void forExit() {
        h.removeCallbacksAndMessages(null);
        callback.onExit();
    }

    /**
     * 刷新按钮。
     */
    private void forRefresh() {
        if (!isRefresh) {
            isRefresh = true;
            setPending(true);
            setEnable(false);
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    adapter.setData(AppQueryUT.getAppInfos(context));
                    h.sendEmptyMessage(DATA_UPDATE_FINISH);
                }
            }.start();
        }
    }
    //--------------------------------------------------

    /**
     * 设置这些按钮的可用状态：刷新、滚动至最前、滚动至最后。
     *
     * @param b true：按钮可用。
     *          false：按钮不可用。
     */
    private void setEnable(boolean b) {
        ivRefresh.setEnabled(b);
        ivToStart.setEnabled(b);
        ivToEnd.setEnabled(b);
        rv.setEnabled(b);
    }

    /**
     * @param pending true：显示progressBar。
     *                false：隐藏progressBar。
     */
    private void setPending(boolean pending) {
        if (pending) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    //--------------------------------------------------

    /**
     * 初始化控件。
     */
    private void createView() {
        root = new LinearLayout(context);
        root.setOrientation(LinearLayout.HORIZONTAL);
        //--------------------------------------------------
        LinearLayout edits = new LinearLayout(context);
        edits.setOrientation(LinearLayout.VERTICAL);
        //高--匹配父容器。宽--权重为1。
        LinearLayout.LayoutParams p = LayoutParamsUT.linearParams(0, MATCH_PARENT,
                1);
        edits.setLayoutParams(p);
        root.addView(edits);
        //--------------------------------------------------
        /*ImageViews*/
        //所有imageView，宽--匹配父容器，高--权重为1。
        LinearLayout.LayoutParams p1 = LayoutParamsUT.linearParams(MATCH_PARENT
                , 0, 1);
        ivRefresh = new ImageView(context);
        ivToStart = new ImageView(context);
        ivToEnd = new ImageView(context);
        ivExit = new ImageView(context);
        ivRefresh.setClickable(true);
        ivToStart.setClickable(true);
        ivToEnd.setClickable(true);
        ivExit.setClickable(true);
        ivRefresh.setLayoutParams(p1);
        ivToStart.setLayoutParams(p1);
        ivToEnd.setLayoutParams(p1);
        ivExit.setLayoutParams(p1);
        edits.addView(ivRefresh);
        edits.addView(ivToStart);
        edits.addView(ivToEnd);
        edits.addView(ivExit);
        //--------------------------------------------------
        FrameLayout rvParent = new FrameLayout(context); //高--匹配父容器。宽--权重为4。
        LinearLayout.LayoutParams p2 = LayoutParamsUT.linearParams(0, MATCH_PARENT,
                4);
        rvParent.setLayoutParams(p2);
        //--------------------------------------------------
        if (rv == null) {
            rv = new RecyclerView(context);
        }
        ViewUT.adjustSize(rv, MATCH_PARENT, MATCH_PARENT);
        rvParent.addView(rv);
        //--------------------------------------------------
        progressBar = new ProgressBar(context);
        progressBar.setVisibility(View.INVISIBLE);
        FrameLayout.LayoutParams p3 = LayoutParamsUT.frameParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER);
        progressBar.setLayoutParams(p3);
        rvParent.addView(progressBar);
        //--------------------------------------------------
        root.addView(rvParent);
        //--------------------------------------------------
        setClickListener();
        //--------------------------------------------------


    }

    @SuppressWarnings("Convert2Lambda")
    private void setClickListener() {
        ivRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forRefresh();
            }
        });
        ivToStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forToStart();
            }
        });
        ivToEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forToEnd();
            }
        });
        ivExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forExit();
            }
        });
    }


    public ImageView getIvRefresh() {
        return ivRefresh;
    }

    public ImageView getIvToStart() {
        return ivToStart;
    }

    public ImageView getIvToEnd() {
        return ivToEnd;
    }

    public ImageView getIvExit() {
        return ivExit;
    }

    public RecyclerView getRv() {
        return rv;
    }

    public LinearLayout getRoot() {
        return root;
    }
    //////////////////////////////////////////////////////////////////////

    static abstract class Adapter extends RecyclerView.Adapter<Adapter.VH> {
        private Context context;
        private int itemSize;//item的大小。
        private List<AppInfo> appInfos;//apps数据信息集合类。

        //////////////////////////////////////////////////////////////////////
        Adapter(Context context, List<AppInfo> appInfos, int itemSize) {
            this.context = context;
            this.appInfos = appInfos;
            this.itemSize = itemSize;
        }

        /**
         * app被点击时的回调。
         *
         * @param appInfo app信息。
         */
        abstract void onItemClick(AppInfo appInfo);

        public void setData(List<AppInfo> data) {
            appInfos = data;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            AppItemView a = new AppItemView(context, itemSize);
            return new VH(a);
        }

        @SuppressWarnings("Convert2Lambda")
        @Override
        public void onBindViewHolder(VH holder, int position) {
            AppInfo appInfo = appInfos.get(position);
            holder.setIcon(appInfo.icon)
                    .setAppName(appInfo.appName);
            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(appInfo);
                }
            });
        }

        @Override
        public int getItemCount() {
            return appInfos.size();
        }

        class VH extends RecyclerView.ViewHolder {
            LinearLayout parent;
            ImageView iconView;
            TextView nameView;

            VH(AppItemView appItemView) {
                super(appItemView.getRoot());
                parent = appItemView.getRoot();
                iconView = appItemView.getIconView();
                nameView = appItemView.getNameView();
            }

            VH setIcon(Drawable icon) {
                iconView.setImageDrawable(icon);
                return this;
            }

            void setAppName(String appName) {
                nameView.setText(appName);
            }

        }
    }
}

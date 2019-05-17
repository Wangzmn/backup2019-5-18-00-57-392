//package zmn.w.uiutility.main_class.window_plugin.editPlugin;
//
//import android.content.Context;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//
//import wclass._marks.Study;
//import wclass._marks.Ugly_Method;
//import wclass.android.android.ui.ViewUT;
//import zmn.w.uiutility.R;
//import zmn.w.uiutility.main_class.Prefer;
//import wclass.android.android.app.AppInfo;
//import zmn.w.uiutility.main_class.window_plugin.WindowPlugin;
//
//import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
//import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
//
///**
// * 运行于 2019年2月14日23:20:12
// *
// * @作者 做就行了！
// * @时间 2019-02-13下午 4:47
// * @该类用途： -
// * @注意事项： -
// * @使用说明： -
// * @思维逻辑： -
// * @优化记录： -
// * @待解决： -
// */
//public class EditPlugin implements WindowPlugin {
//    private static final boolean DEBUG = true;
//    public static final int DATA_UPDATE_FINISH = 0;
//    private EditPluginCallback callback;
//    ImageView ivRefresh;
//    ImageView ivToStart;
//    ImageView ivToEnd;
//    ImageView ivExit;
//    RecyclerView rv;
//    LinearLayout root;
//    @Study
//    private ProgressBar progressBar;
//    Adapter adapter;
//    Prefer prefer;
//
//    public EditPlugin(Context context, EditPluginCallback callback) {
//        prefer = Prefer.getInstance();
//        createView(context);
////        adjustSize(prefer.size);
//        this.callback = callback;
//        adapter = new Adapter(context, prefer.width/5, new Adapter.Callback() {
//            @Override
//            public void onItemClick(AppInfo appInfo) {
//                callback.onItemClick(appInfo);
//            }
//        });
//        rv.setAdapter(adapter);
//        int rows = 4;
//        rv.setLayoutManager(new GridLayoutManager(context, rows,
//                LinearLayoutManager.HORIZONTAL, false));
//        rv.getLayoutManager().setAutoMeasureEnabled(false);
//        rv.setOverScrollMode(View.OVER_SCROLL_NEVER);
//    }
//
//    @Override
//    public View getView() {
//        return root;
//    }
//
//    @Ugly_Method
//    @Override
//    public void onAdjustSize(int windowWidth, int windowHeight) {
//        adjustSize(windowWidth,windowHeight);
//    }
//
//    @Override
//    public void onAsPlugin() {
//
//    }
//
//    @Override
//    public void onDismiss() {
//
//    }
//
//    @Override
//    public void onDisplay() {
//
//    }
//
//    @Override
//    public void onHide() {
//
//    }
//
//    //----------------------------------------------------------------------
//    public interface EditPluginCallback {
//        void onItemClick(AppInfo appInfo);
//
//        void onExit();
//    }
//    //////////////////////////////////////////////////////////////////////
//
//    public void adjustSize(int windowWidth, int windowHeight) {
//        ViewUT.adjustSize(root,windowWidth, windowHeight);
//    }
//
//    /**
//     * 运行于 2019年2月15日11:06:33
//     */
//    Handler h = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    /**
//                     * 1、先滑动至头部。
//                     * 2、再更新。
//                     */
//                    isRefresh = false;
//                    pending(false);
//                    setEnable(true);
//                    rv.scrollToPosition(0);
//                    adapter.notifyDataSetChanged();
//                    break;
//                case 1:
//                    break;
//            }
//        }
//    };
//    boolean isRefresh = false;
//
//    private void forToStart() {
//        rv.smoothScrollToPosition(0);
//
//    }
//
//    private void forToEnd() {
//        rv.smoothScrollToPosition(adapter.getItemCount() - 1);
//    }
//
//    private void forExit() {
//        h.removeCallbacksAndMessages(null);
//        callback.onExit();
//    }
//
//    /**
//     * 刷新按钮。
//     */
//    private void forRefresh() {
//        if (!isRefresh) {
//            isRefresh = true;
//            pending(true);
//            setEnable(false);
//            new Thread() {
//                @Override
//                public void run() {
//                    super.run();
//                    adapter.setData(prefer.getNewApps());
//                    h.sendEmptyMessage(DATA_UPDATE_FINISH);
//                }
//            }.start();
//        }
//    }
//
//    private void setEnable(boolean b) {
//        ivRefresh.setEnabled(b);
//        ivToStart.setEnabled(b);
//        ivToEnd.setEnabled(b);
//    }
//
//    private void pending(boolean pending) {
//        if (pending) {
//            progressBar.setVisibility(View.VISIBLE);
//        } else {
//            progressBar.setVisibility(View.INVISIBLE);
//        }
//    }
//
//    /**
//     * 初始化控件。
//     */
//    private void createView(Context context) {
//        root = new LinearLayout(context);
//        root.setOrientation(LinearLayout.HORIZONTAL);
//        //----------------------------------------------------------------------
//        LinearLayout edits = new LinearLayout(context);
//        edits.setOrientation(LinearLayout.VERTICAL);
//        //高--匹配父容器。宽--权重为1。
//        LinearLayout.LayoutParams p = ViewUT.linearParams(0, MATCH_PARENT,
//                1);
//        edits.setLayoutParams(p);
//        root.addView(edits);
//        //----------------------------------------------------------------------
//        /*ImageViews*/
//        //所有imageView，宽--匹配父容器，高--权重为1。
//        LinearLayout.LayoutParams p1 = ViewUT.linearParams(MATCH_PARENT
//                , 0, 1);
//        ivRefresh = new ImageView(context);
//        ivToStart = new ImageView(context);
//        ivToEnd = new ImageView(context);
//        ivExit = new ImageView(context);
//        ivRefresh.setClickable(true);
//        ivToStart.setClickable(true);
//        ivToEnd.setClickable(true);
//        ivExit.setClickable(true);
//        ivRefresh.setLayoutParams(p1);
//        ivToStart.setLayoutParams(p1);
//        ivToEnd.setLayoutParams(p1);
//        ivExit.setLayoutParams(p1);
//        edits.addView(ivRefresh);
//        edits.addView(ivToStart);
//        edits.addView(ivToEnd);
//        edits.addView(ivExit);
//        //----------------------------------------------------------------------
//        FrameLayout rvParent = new FrameLayout(context); //高--匹配父容器。宽--权重为4。
//        LinearLayout.LayoutParams p2 = ViewUT.linearParams(0, MATCH_PARENT,
//                4);
//        rvParent.setLayoutParams(p2);
//        //----------------------------------------------------------------------
//        rv = (RecyclerView) LayoutInflater.from(context).inflate(R.layout.recyclerview, null);
//        ViewUT.adjustSize(rv, MATCH_PARENT, MATCH_PARENT);
//        rvParent.addView(rv);
//        //----------------------------------------------------------------------
//        progressBar = new ProgressBar(context);
//        progressBar.setVisibility(View.INVISIBLE);
//        FrameLayout.LayoutParams p3 = ViewUT.frameParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER);
//        progressBar.setLayoutParams(p3);
//        rvParent.addView(progressBar);
//        //----------------------------------------------------------------------
//        root.addView(rvParent);
//        //----------------------------------------------------------------------
//        setClickListener();
//        //----------------------------------------------------------------------
//
//        if (DEBUG) {
//            ivRefresh.setBackgroundColor(0x550000ff);
//            ivToStart.setBackgroundColor(0x5500ff00);
//            ivToEnd.setBackgroundColor(0x550000ff);
//            ivExit.setBackgroundColor(0x5500ff00);
//            rv.setBackgroundColor(0x55ff0000);
//            pending(true);
//        }
//    }
//
//    private void setClickListener() {
//        ivRefresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                forRefresh();
//            }
//        });
//        ivToStart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                forToStart();
//            }
//        });
//        ivToEnd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                forToEnd();
//            }
//        });
//        ivExit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                forExit();
//            }
//        });
//    }
//
//
//    public ImageView getIvRefresh() {
//        return ivRefresh;
//    }
//
//    public ImageView getIvToStart() {
//        return ivToStart;
//    }
//
//    public ImageView getIvToEnd() {
//        return ivToEnd;
//    }
//
//    public ImageView getIvExit() {
//        return ivExit;
//    }
//
//    public RecyclerView getRv() {
//        return rv;
//    }
//
//    public LinearLayout getRoot() {
//        return root;
//    }
//}

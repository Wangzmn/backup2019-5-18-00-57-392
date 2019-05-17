package zmn.w.uiutility.main_class.window_plugin_view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import wclass.android.app.AppInfo;
import wclass.android.util.JumpPageUT;
import wclass.android_support.AppsContainerViewer;
import wclass.android.util.AnimationUT;
import wclass.android.util.AnimatorUT;
import wclass.android.encapsulation.DataSave;
import wclass.android.util.DurationUT;
import wclass.android.ui.WindowParamsUT;
import wclass.android.util.LayoutParamsUT;
import wclass.android.util.ViewUT;
import wclass.enums.LayoutGravity;
import wclass.enums.Orien2;
import wclass.util.ColorUT;
import zmn.w.uiutility.R;
import zmn.w.uiutility.main_class.Prefer;
import zmn.w.uiutility.main_class.a_pending_class.pending_tidy.SquareMarkerViewer;
import zmn.w.uiutility.main_class.window.Window;
import zmn.w.uiutility.second_class.InterceptReverseLinearVG;
import zmn.w.uiutility.second_class.OnTouchEventListener;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * @作者 做就行了！
 * @时间 2019-02-13下午 10:15
 * @该类用途： -
 * @注意事项： -
 * 1、先{@link #adjustSize}
 * 后{@link #setCoor}
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("WeakerAccess")
@SuppressLint("RtlHardcoded")
public class PuppetViewer implements OnTouchEventListener {
    private static final boolean DEBUG = false;
    private static final boolean ANIM_DEBUG = false;
    //----------------------------------------------------------------------
    private final long animDuration = DurationUT.ANIM_DURATION;
    //    private final  long animDuration = 5000;
    private Context context;
    /**
     * todo
     * 1、将prefer换成自己的配置类！！！
     */
    private Prefer prefer;
    //----------------------------------------------------------------------
    private static final int ANIM_FINISH_MESSAGE = 0;//handler消息类型。
    /**
     * menuRoot的参数。
     */
    private final LinearLayout.LayoutParams menuRootParams =
            LayoutParamsUT.linearParams(0, 0);
    /**
     * menus的参数。
     */
    private final LinearLayout.LayoutParams childParams =
            LayoutParamsUT.linearParams(0, 0);
    //----------------------------------------------------------------------

    private List<MenuWrapper> menuWrappers = new ArrayList<>();
    private int menuTotalCount;//menu总数量。
    private int menuUsingCount;//正在使用的menu数量。
    private MenuWrapper selectedMenu;//被选中的menu。
    private boolean isMenuEdit = false;//是否是编辑模式。
    //----------------------------------------------------------------------
    private boolean isAniming = false;//是否是动画状态。
    private boolean isIntercept = false;//是否拦截事件。（按下时如果执行动画了，则拦截此次事件。）
    //////////////////////////////////////////////////////////////////////
    public InterceptReverseLinearVG root;//rootView
    private InterceptReverseLinearVG menuRoot;

    private ValueAnimator va;//menuRoot的value动画。
    private boolean isExtended;//是否为展开状态。
    private Orien2 orien;//布局方向。
    private int baseSize;//基础大小。
    private LayoutGravity layoutGravity;
    private int statusBarHeight;
    //--------------------------------------------------
    DataSave dataSave;
    //////////////////////////////////////////////////////////////////////
    public PuppetViewer(Context context,
                        View mainView,
                        int menuCount) {
        this.context = context;
        prefer = Prefer.getInstance();
        //--------------------------------------------------
        dataSave = new DataSave(context,Prefer.TABLE_NAME);
        //--------------------------------------------------
        menuTotalCount = prefer.maxCount - 1;
        menuUsingCount = menuCount;
        mainView.setLayoutParams(childParams);
        this.isExtended = true;
        //----------------------------------------------------------------------
        root = new InterceptReverseLinearVG(context);
        root.setOnInterceptTouchEventListener(this);
        //----------------------------------------------------------------------
        menuRoot = new InterceptReverseLinearVG(context);
        menuRoot.setLayoutParams(menuRootParams);
        menuRoot.addView(mainView);
        menuRoot.setBackground(prefer.getWinBG());
        initMenuAbout();
        //----------------------------------------------------------------------
        root.addView(menuRoot);
        //----------------------------------------------------------------------
//        setOrien(orien);
//        setGravity(layoutGravity);
        //----------------------------------------------------------------------

        editWin = new Window(context) {
            @Override
            public boolean onInterceptTouchEvent(MotionEvent ev) {
                return false;
            }

            @Override
            public boolean onTouchEvent(MotionEvent event) {
                return false;
            }
        };
        WindowManager.LayoutParams lp = WindowParamsUT.makeDefaultLeftBottomParams();
        lp.width = prefer.width;
        lp.height = prefer.width / 5 * 4;
        editWin.setLayoutParams(lp);
        editWin.setCoor(0, prefer.baseSize);
        //------------------------------------------------------------
        RecyclerView rv = (RecyclerView) LayoutInflater.from(context).inflate(R.layout.recyclerview, null);

        appsViewer = new AppsContainerViewer(
                context, prefer.width, rv, new AppsContainerViewer.Callback() {
            @Override
            public void onItemClick(AppInfo appInfo) {
                applyAppToMenu(appInfo);
            }

            @Override
            public void onExit() {
                doExitEditMode();
            }
        });
        editWin.getRoot().addView(appsViewer.getRoot());
    }

    AppsContainerViewer appsViewer;
    Window editWin;
    //----------------------------------------------------------------------

    /**
     * menu被长按时。
     */
    private void onEnterEditMode(MenuWrapper menuWrapper) {
        if (DEBUG) {
            Log.e("TAG", "  " + "  " + " MenuViewer.onLongClick。 ");
        }
        enterEditMode();
        selectedMenu = menuWrapper;
        setSelected(menuWrapper, true);
        //----------------------------------------------------------------------
        editWin.displayWindow();
    }

    //----------------------------------------------------------------------
    public void resetState() {
        if (isMenuEdit) {
            doExitEditMode();
        }
    }

    private void doExitEditMode() {
        editWin.hideWindow();
        exitEditMode();
    }
    //////////////////////////////////////////////////////////////////////

    public void setCoor(int x, int y) {
        root.setX(x);
        root.setY(y);
    }

    public void dispatchTouchEvent(MotionEvent ev) {
        root.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public void setIntercept(boolean intercept) {
        isIntercept = intercept;
    }

    /**
     * 检查于 2019年2月14日13:14:58
     * <p>
     * 友情提示：该方法可处理公共事件，一旦触发事件，可以返回true进行拦截。
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isIntercept = isAniming;
                break;
        }
        return isIntercept;
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 请求root重新布局。
     */
    public void requestLayout() {
        root.requestLayout();
    }

    /**
     * 友情提示：设置习惯时，无需更新坐标。
     */
    @SuppressWarnings("JavaDoc")
    public void setGravity(LayoutGravity layoutGravity) {
        this.layoutGravity = layoutGravity;
        if (DEBUG) {
            Log.e("TAG", this.getClass() + "：layoutGravity = " + layoutGravity.toString());
        }
        switch (layoutGravity) {
            case LEFT_TOP:
                root.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                menuRoot.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                menuRoot.setLayoutVerticalFromLastChild(false);
                break;
            case LEFT_BOTTOM:
                root.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                menuRoot.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                menuRoot.setLayoutVerticalFromLastChild(true);
                break;
            case RIGHT_TOP:
                root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                menuRoot.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                menuRoot.setLayoutVerticalFromLastChild(false);
                break;
            case RIGHT_BOTTOM:
                root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                menuRoot.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                menuRoot.setLayoutVerticalFromLastChild(true);
                break;
        }
        LayoutParamsUT.setGravity(menuRoot, layoutGravity);
        LayoutParamsUT.setGravity(root, layoutGravity);
    }

    /**
     * 友情提示：设置布局方向时，需要更新坐标。
     *
     * @param orien 布局方向。
     */
    public void setOrien(Orien2 orien) {
        this.orien = orien;
        switch (orien) {
            case HORIZONTAL:
                root.setOrientation(LinearLayout.HORIZONTAL);
                menuRoot.setOrientation(LinearLayout.HORIZONTAL);
                break;
            case VERTICAL:
                root.setOrientation(LinearLayout.VERTICAL);
                menuRoot.setOrientation(LinearLayout.VERTICAL);
                break;
        }
    }
    //----------------------------------------------------------------------

    /**
     * 调整控件大小。
     * <p>
     * todo 加入{@link Prefer#isExtended}参数
     *
     * @param baseSize
     */
    public void adjustSize(int baseSize, int totalSize) {
        this.baseSize = baseSize;

        if (va != null) {
            va.cancel();
        }
        adjustRoot(baseSize, totalSize);
        adjustMenuRoot(baseSize, totalSize);
        childParams.width = baseSize;
        childParams.height = baseSize;
    }

    private void adjustRoot(int baseSize, int totalSize) {
        switch (orien) {
            case HORIZONTAL:
                ViewUT.adjustSize(root, totalSize, baseSize);
                break;
            case VERTICAL:
                ViewUT.adjustSize(root, baseSize, totalSize);
                break;
        }
    }

    /**
     * 调整menuRoot大小
     *
     * @param baseSize 基准大小。
     */
    private void adjustMenuRoot(int baseSize, int totalSize) {
        totalSize = isExtended
                ? totalSize : baseSize;
        switch (orien) {
            case HORIZONTAL:
                ViewUT.adjustSize(menuRoot, totalSize, baseSize);
                break;
            case VERTICAL:
                ViewUT.adjustSize(menuRoot, baseSize, totalSize);
                break;
        }
    }

    //----------------------------------------------------------------------


    /**
     * 运行于 2019年2月28日18:18:48
     * <p>
     * 展开所有menu。
     */
    public void doExtend() {
        if (isExtended) {
            if (ANIM_DEBUG) {
                Log.e("TAG", " doExtend()：当前为展开状态，无需执行展开动画。 ");
            }
            return;
        }
        isExtended = true;
        //----------------------------------------------------------------------
        int from, to;
        switch (orien) {
            case HORIZONTAL:
                from = menuRoot.getWidth();
                to = root.getWidth();
                break;
            case VERTICAL:
                from = menuRoot.getHeight();
                to = root.getHeight();
                break;
            default:
                throw new IllegalStateException();
        }
        if (ANIM_DEBUG) {
            Log.e("TAG", "doExtend()：即将执行展开动画。 from =  " + from + ", to =  " + to + " 。  " + "  ");
        }
        doAnim(from, to);
    }

    /**
     * 运行于 2019年2月28日18:18:48
     * <p>
     * 收缩所有menu。
     */
    public void doFold() {
        if (!isExtended) {
            if (ANIM_DEBUG) {
                Log.e("TAG", " doFold()：当前为收起状态，无需执行收起动画。 ");
            }
            return;
        }
        isExtended = false;
        //----------------------------------------------------------------------
        int from;
        switch (orien) {
            case HORIZONTAL:
                from = menuRoot.getWidth();
                break;
            case VERTICAL:
                from = menuRoot.getHeight();
                break;
            default:
                throw new IllegalStateException();
        }
        int to = baseSize;
        if (ANIM_DEBUG) {
            Log.e("TAG", "doFold()：即将执行收起动画。 from =  " + from + ", to =  " + to + " 。  " + "  ");
        }
        doAnim(from, to);
    }

    private void doAnim(int from, int to) {
        if (va != null) {
            va.cancel();
        }
        va = AnimatorUT.forProgressValue(from, to,
                animDuration,
                new AnimatorUT.Update() {
                    @Override
                    public void onStart() {
                        if (ANIM_DEBUG) {
                            Log.e("TAG", " 动画开始了。 ");
                        }
                        isAniming = true;
                    }

                    @Override
                    public void onEnd() {
                        if (ANIM_DEBUG) {
                            Log.e("TAG", " 动画结束了。 ");
                        }
                        isAniming = false;
                        va = null;
                    }

                    @Override
                    public void onCancel() {
                        if (ANIM_DEBUG) {
                            Log.e("TAG", " 动画取消了了。 ");
                        }
                        isAniming = false;
                        va = null;
                    }

                    @Override
                    public void onUpdate(float progress) {
                        switch (orien) {
                            case HORIZONTAL:
                                menuRootParams.width = (int) progress;
                                break;
                            case VERTICAL:
                                menuRootParams.height = (int) progress;
                                break;
                        }
                        //fix 显示不正常时，换成requestLayout。
//                        root.forceLayout();
                        root.requestLayout();
//                        menuRoot.forceLayout();
//                        menuRoot.requestLayout();
                    }
                });
    }

    //////////////////////////////////////////////////////////////////////
    /*domain 数据保存相关*/
    private Drawable getIcon(String pkName) {
        try {
            return context.getPackageManager().getApplicationIcon(pkName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getPkName(Context context, int dex) {
        return dataSave.getString(menuToStr(dex),null);
    }

    /**
     * 给menu应用app信息。
     *
     * @param appInfo app信息
     */
    public void applyAppToMenu(AppInfo appInfo) {
        if (selectedMenu != null) {
            selectedMenu.appInfo = appInfo;
            selectedMenu.setIcon();
            selectedMenu.mv.displayCorner();
            dataSave.putString(menuToStr(selectedMenu.menuDex),appInfo.pkName);
        }
    }

    private String menuToStr(int dex) {
        return "menu" + dex;
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 检查于 2019年2月19日00:58:59
     * 初始化每个menu的数据信息，包括：控件、数据。
     */
    private void initMenuAbout() {
        for (int i = 0; i < menuTotalCount; i++) {
            addMenu(i);
        }
    }

    /**
     * 检查于 2019年2月19日00:58:59
     *
     * @param i
     */
    private void addMenu(int i) {
        String pkName = getPkName(context, i);
        Drawable icon = getIcon(pkName);
        SquareMarkerViewer mv = new SquareMarkerViewer(
                1 / 20f, context) {
            @Override
            protected void onAdjustIconMargin(int w, int h, int margin) {
                super.onAdjustIconMargin(w, h, margin);
            }
        };
        MenuWrapper menuWrapper = new MenuWrapper(i, new AppInfo(pkName, null, icon), mv);
        menuWrappers.add(menuWrapper);
        addMenuView(menuWrapper, mv);
    }

    /**
     * 检查于 2019年2月19日00:59:15
     *
     * @param menuWrapper
     * @param mv
     */
    private void addMenuView(MenuWrapper menuWrapper, SquareMarkerViewer mv) {
        //todo menu图标未设置。
        menuWrapper.setBackground();
        menuWrapper.setIcon();
        mv.corner.setBackgroundColor(ColorUT.BLACK);
        mv.root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!isMenuEdit) {
                    PuppetViewer.this.onEnterEditMode(menuWrapper);
                }
                return true;
            }
        });
        mv.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DEBUG) {
                    int number = menuWrapper.menuDex + 1;
                    Log.e("TAG", " 第" + number + "个孩子被点击。");
                }
                if (isMenuEdit) {
                    onMenuSelect(menuWrapper);
                } else {
                    onMenuClick(menuWrapper);
                }
            }
        });
        mv.corner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCornerClick(menuWrapper);
            }
        });

        mv.root.setLayoutParams(childParams);
        if (DEBUG) {
            mv.root.setBackgroundColor(ColorUT.BLUE);
        }
        menuRoot.addView(mv.root);
    }

    /**
     * 检查于 2019年2月14日16:11:28
     * <p>
     * 将menu数量增加/减少至指定数量。
     * <p>
     * 警告：调用该方法后，需要调用{@link #adjustSize}调整大小。
     * fix 使劲调试该方法！！！！！！
     *
     * @param count 将menu数量增加/减少至该数量
     */
    @SuppressWarnings("unused")
    public void menuToCount(int count) {
        if (count < 1) {
            throw new IllegalStateException("数量只能大于0。");
        }
        //小于总数量时，隐藏就行
        if (count <= menuTotalCount) {
            //需要减少，隐藏他们之间的。
            if (count < menuUsingCount) {
                //count是第count+1个item的下标。
                for (int i = count; i < menuUsingCount; i++) {
                    menuWrappers.get(i).mv.hideRoot();
                }
            }
            //需要增加，显示他们之间的。
            else {
                //usingCount是第usingCount+1个item的下标。
                for (int i = menuUsingCount; i < count; i++) {
                    menuWrappers.get(i).mv.displayRoot();
                }
            }
            menuUsingCount = count;
        }
        //扩展的数量大于当前总数。
        else {
            //显示已隐藏的。
            for (int i = menuUsingCount; i < menuTotalCount; i++) {
                menuWrappers.get(i).mv.displayRoot();
            }
            //添加至count数量的menu。
            for (int i = menuTotalCount; i < count; i++) {
                addMenu(i);
            }
            menuUsingCount = menuTotalCount = count;
        }
    }
    //////////////////////////////////////////////////////////////////////


    /**
     * 检查于 2019年2月19日01:00:15
     * <p>
     * menu被点击时。
     */
    private void onMenuClick(MenuWrapper menuWrapper) {
        //appInfo
        if (menuWrapper.getAppInfo()) {
            String string = menuWrapper.appInfo.pkName.toString();
            JumpPageUT.jumpToActivity(context, string);
            if (DEBUG) {
                Log.e("TAG", " 启动app：" +
                        string + "成功。 ");
            }
        }
        //加号
        else {
            if (DEBUG) {
                Log.e("TAG", " 点击加号成功。 ");
            }
            enterEditMode();
            selectedMenu = menuWrapper;
            setSelected(menuWrapper, true);
        }
    }

    /**
     * 检查于 2019年2月19日01:00:15
     * <p>
     * menu被选中时。
     */
    private void onMenuSelect(MenuWrapper menuWrapper) {
        if (menuWrapper != selectedMenu) {
            if (DEBUG) {
                Log.e("TAG", "  " + "  " + "  " + " Select成功。 ");
            }
            setSelected(selectedMenu, false);
            setSelected(menuWrapper, true);
            selectedMenu = menuWrapper;
        }
    }

    /**
     * 角标被点击时，清除menu。
     */
    private void onCornerClick(MenuWrapper menuWrapper) {
        if (DEBUG) {
            Log.e("TAG", " 点击角标成功。 ");
        }
        SquareMarkerViewer mv = menuWrapper.mv;
        mv.hideCorner();
        menuWrapper.setIcon();
        //todo 删除本地数据
    }

    /**
     * 检查于 2019年2月19日01:00:15
     *
     * @param menuWrapper
     * @param selected
     */
    private void setSelected(MenuWrapper menuWrapper, boolean selected) {
        menuWrapper.mv.root.setSelected(selected);
    }
    //////////////////////////////////////////////////////////////////////
    /*domain 编辑模式相关*/
    /**
     * 检查于 2019年2月14日13:18:16
     */
    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ANIM_FINISH_MESSAGE:
                    isAniming = false;
                    break;
            }
        }
    };

    /**
     * 运行于 2019年2月15日11:19:12
     * <p>
     * 进入编辑模式，执行的动画。
     */
    public void enterEditMode() {
        if (isMenuEdit) {
            return;
        } else {
            isMenuEdit = true;
        }
        for (int i = 0; i < menuWrappers.size(); i++) {
            MenuWrapper menuWrapper = menuWrappers.get(i);
            SquareMarkerViewer mv = menuWrapper.mv;
            if (menuWrapper.getAppInfo() || DEBUG) {
                mv.displayCorner();
                mv.setCornerScale(0);
                AnimationUT.scaleAnimIn(mv.corner);
            }
            AnimationUT.scaleAnim(mv.icon, 0.8f);
        }
        h.removeMessages(ANIM_FINISH_MESSAGE);
        h.sendEmptyMessageDelayed(ANIM_FINISH_MESSAGE, DurationUT.ANIM_DURATION);
    }

    /**
     * 运行于 2019年2月15日11:19:12
     * <p>
     * 退出编辑模式，执行的动画。
     */
    public void exitEditMode() {
        if (!isMenuEdit) {
            return;
        }
        isMenuEdit = false;
        setSelected(selectedMenu, false);
        for (int i = 0; i < menuUsingCount; i++) {
            MenuWrapper menuWrapper = menuWrappers.get(i);
            SquareMarkerViewer mv = menuWrapper.mv;
            if (menuWrapper.getAppInfo()) {
                AnimationUT.scaleAnimOut(mv.corner);
            }
            AnimationUT.scaleAnimIn(mv.icon);
        }
        h.removeMessages(ANIM_FINISH_MESSAGE);
        h.sendEmptyMessageDelayed(ANIM_FINISH_MESSAGE, DurationUT.ANIM_DURATION);
    }

    //////////////////////////////////////////////////////////////////////

    /**
     * 检查于 2019年2月14日13:18:34
     */
    class MenuWrapper {
        int menuDex;
        AppInfo appInfo;
        SquareMarkerViewer mv;

        MenuWrapper(int menuDex, AppInfo appInfo, SquareMarkerViewer mv) {
            this.menuDex = menuDex;
            this.appInfo = appInfo;
            this.mv = mv;
        }

        boolean getAppInfo() {
            return appInfo.pkName != null;
        }

        void setBackground() {
            mv.root.setBackground(prefer.getMenuTouchDrawable());
        }

        void setIcon() {
            if (appInfo != null) {
                mv.icon.setImageDrawable(appInfo.icon);
            } else {
                //todo 设置加号。
            }
        }
    }
    //////////////////////////////////////////////////////////////////////
    /*domain DEBUG*/

    private void toHide() {
        for (int i = 0; i < menuUsingCount; i++) {
            MenuWrapper menuWrapper = menuWrappers.get(i);
            menuWrapper.mv.root.setVisibility(INVISIBLE);
        }
    }

    private void toDisplay() {
        for (int i = 0; i < menuUsingCount; i++) {
            MenuWrapper menuWrapper = menuWrappers.get(i);
            menuWrapper.mv.root.setVisibility(VISIBLE);
        }
    }
}

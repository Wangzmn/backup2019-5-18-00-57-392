package zmn.w.uiutility.main_class;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.FrameLayout;

import ex.GravityUT;
import ex.WindowParamsUT;
import wclass.android.util.JumpPageUT;
import wclass.android.util.DurationUT;
import wclass.android.device.ScreenOrienChangeNotify;
import wclass.android.ui.EventTypeConverter;
import wclass.enums.EventType;
import wclass.enums.LayoutGravity;
import wclass.enums.Orien2;
import wclass.ui.event_parser.SimplePointer;
import wclass.util.ColorUT;
import zmn.w.uiutility.main_class.role.Puppet;
import zmn.w.uiutility.main_class.window.Window;
import zmn.w.uiutility.main_class.window_plugin_view.PuppetViewer;

/**
 * @作者 做就行了！
 * @时间 2019-02-19下午 4:49
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class Director {
    private static final boolean DEBUG = false;
    private static final boolean COOR_DEBUG = false;
    private static final boolean MENU_FUNCTION_DEBUG = false;
    //----------------------------------------------------------------------
    Prefer prefer;
    /**
     * todo
     * 主类。
     * 1、包含空的window，没有plugin。
     * 2、将window的touch事件，与显示连接在一起，
     * 直接引用显示的控件就行，不需要接口连接。
     * 3、根据mainView的手势改变window的大小。
     * todo
     * 1、窗口更新坐标时，rootView 获取rect是否正常？？？？
     */
    public Window hand;//手势窗口。
    public Window puppet;//木偶窗口。
    PuppetViewer puppetViewer;//木偶view
    FrameLayout orderView;//手势view。

    //----------------------------------------------------------------------

    /**
     * todo 特殊状况时，重置该变量。
     */
    private boolean isOrder;

    SimplePointer pointer;
    HandleEvent handleEvent;
    private Context context;

    //////////////////////////////////////////////////////////////////////
    public Director(Context context) {
        this.context = context.getApplicationContext();
        prefer = Prefer.getInstance();
        //----------------------------------------------------------------------
        handleEvent = new HandleEvent();
        int scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        pointer = new SimplePointer(scaledTouchSlop);
        //----------------------------------------------------------------------
        orderView = new FrameLayout(context);
        orderView.setOnTouchListener(handleEvent);
        //----------------------------------------------------------------------

        puppetViewer = new PuppetViewer(context,
                orderView, prefer.gridCount - 1);
        //----------------------------------------------------------------------

//        hand.getRoot().setBackgroundColor(ColorUT.greenToAlpha(0.5f));
        puppet = new Puppet(context, puppetViewer.root);
        //----------------------------------------------------------------------
        puppet.displayWindow();
        //todo 监听写在哪个类？
        prefer.getScreenOrienObserver().addObserver(new ScreenOrienChangeNotify.OnScreenOrienChangeListener() {

            @Override
            public void onHoriScreen() {
                prefer.toScreenHori();
                setCoor_byGridLeftTop(prefer.horiX, prefer.horiY);
                if (COOR_DEBUG) {
                    Log.e("TAG", " 屏幕状态：横屏。x = " + prefer.horiX + ", " +
                            "y = " + prefer.horiY + " 。");
                }
            }

            @Override
            public void onVertiScreen() {
                prefer.toScreenVerti();
                setCoor_byGridLeftTop(prefer.vertiX, prefer.vertiY);
                if (COOR_DEBUG) {
                    Log.e("TAG", " 屏幕状态：竖屏。x = " + prefer.vertiX + ", " +
                            "y = " + prefer.vertiY + " 。");
                }
            }
        });
        prefer.director = this;
        hand = new WindowImpl(context);
        WindowManager.LayoutParams lp = WindowParamsUT.params_noFocuse_imAlt();
        GravityUT.toLT(lp);
        hand.setLayoutParams(lp);
        hand.displayWindow();
        hand.getRoot().setBackgroundColor(ColorUT.redToAlpha(0f));


        adjustAll();
        orderView.setBackgroundColor(ColorUT.blueToAlpha(0));
        if (DEBUG || COOR_DEBUG) {
            hand.getRoot().setBackgroundColor(ColorUT.redToAlpha(0.5f));
            orderView.setBackgroundColor(ColorUT.blueToAlpha(0.5f));
        }

    }

    public void adjustAll() {
        setLayoutGravity(prefer.layoutGravity);
        setOrien(prefer.orien);
        adjustSize(prefer.baseSize, prefer.totalSize);
        setCoor_byGridLeftTop(prefer.vertiX, prefer.vertiY);
    }

    /**
     * fix
     * 1、RIGHT都有问题：
     * ①window显示在左侧。
     * ②puppet显示在右侧，且只显示最后一个图标。
     */
    public void setLayoutGravity(LayoutGravity layoutGravity) {
        puppetViewer.setGravity(layoutGravity);
    }

    /**
     * 设置并保存坐标。
     *
     * @param windowX 窗口左上角x。
     * @param windowY 窗口左上角y。
     */
    public void setAndSaveCoor_byWindowLeftTop(int windowX, int windowY) {
        setCoor_byWindowLeftTop(windowX, windowY);
        if (!prefer.isExtended) {
            prefer.saveCoor_byGridLeftTop(hand.getX(), hand.getY());
            return;
        }
        switch (prefer.orien) {
            case HORIZONTAL:
                switch (prefer.layoutGravity) {
                    case LEFT_TOP:
                    case LEFT_BOTTOM:
                        prefer.saveCoor_byGridLeftTop(hand.getX(), hand.getY());
                        break;

                    case RIGHT_TOP:
                    case RIGHT_BOTTOM:
                        prefer.saveCoor_byGridLeftTop(hand.getX() + getMenusSize(),
                                hand.getY());
                        break;
                }
                break;
            case VERTICAL:
                switch (prefer.layoutGravity) {

                    case LEFT_TOP:
                    case RIGHT_TOP:
                        prefer.saveCoor_byGridLeftTop(hand.getX(), hand.getY());
                        break;

                    case LEFT_BOTTOM:
                    case RIGHT_BOTTOM:
                        prefer.saveCoor_byGridLeftTop(hand.getX(),
                                hand.getY() + getMenusSize());
                        break;
                }
                break;
        }
    }

    /**
     * 设置坐标。
     *
     * @param windowX 窗口左上角x。
     * @param windowY 窗口左上角y。
     */
    public void setCoor_byWindowLeftTop(int windowX, int windowY) {
        if (!prefer.isExtended) {
            setCoor_byGridLeftTop(windowX, windowY);
        }
        //展开时
        else {
            switch (prefer.orien) {
                case HORIZONTAL:
                    switch (prefer.layoutGravity) {
                        case LEFT_TOP:
                        case LEFT_BOTTOM:
                            setCoor_byGridLeftTop(windowX, windowY);
                            break;

                        case RIGHT_TOP:
                        case RIGHT_BOTTOM:
                            windowX = limitX(windowX);
                            windowY = limitY(windowY);
                            hand.applyCoor(windowX, windowY);
                            puppetViewer.setCoor(windowX, windowY);
                            break;
                    }
                    break;

                case VERTICAL:
                    switch (prefer.layoutGravity) {
                        case LEFT_TOP:
                        case RIGHT_TOP:
//                            Log.e("TAG"," case LEFT_TOP:case RIGHT_TOP: ");
                            setCoor_byGridLeftTop(windowX, windowY);
                            break;

                        case LEFT_BOTTOM:
                        case RIGHT_BOTTOM:
//                            Log.e("TAG"," case LEFT_BOTTOM:case RIGHT_BOTTOM: ");
                            windowX = limitX(windowX);
                            windowY = limitY(windowY);
                            hand.applyCoor(windowX, windowY);
                            puppetViewer.setCoor(windowX, windowY);
                            break;
                    }
                    break;
            }
        }
    }

    /**
     * 设置并保存坐标。
     *
     * @param gridX 主方格左上角X坐标。
     * @param gridY 主方格左上角Y坐标。
     */
    public void setAndSaveCoor_byGridLeftTop(int gridX, int gridY) {
        setCoor_byGridLeftTop(gridX, gridY);
        prefer.saveCoor_byGridLeftTop(hand.getX(), hand.getY());
    }

    /**
     * 设置坐标。
     *
     * @param gridX 主方格左上角X坐标。
     * @param gridY 主方格左上角Y坐标。
     */
    public void setCoor_byGridLeftTop(int gridX, int gridY) {
//        Log.e("TAG", "应用之前 x = " + gridX + ", y = " + gridY + " 。");
        if (prefer.isExtended) {
            switch (prefer.orien) {
                case HORIZONTAL:
                    switch (prefer.layoutGravity) {
                        case LEFT_TOP:
                        case LEFT_BOTTOM:
                            gridX = limitX(gridX);
                            gridY = limitY(gridY);
                            hand.applyCoor(gridX, gridY);
                            puppetViewer.setCoor(gridX, gridY);
                            break;

                        case RIGHT_TOP:
                        case RIGHT_BOTTOM:
                            int menusSize = getMenusSize();
                            gridX = limitX(gridX - menusSize);
                            gridY = limitY(gridY);
                            hand.applyCoor(gridX, gridY);
                            puppetViewer.setCoor(gridX, gridY);
                            break;
                    }
                    break;

                case VERTICAL:
                    switch (prefer.layoutGravity) {
                        case LEFT_TOP:
                        case RIGHT_TOP:
                            gridX = limitX(gridX);
                            gridY = limitY(gridY);
                            hand.applyCoor(gridX, gridY);
                            puppetViewer.setCoor(gridX, gridY);
                            break;

                        case LEFT_BOTTOM:
                        case RIGHT_BOTTOM:
                            int menusSize = getMenusSize();
                            gridX = limitX(gridX);
                            gridY = limitY(gridY - menusSize);
                            hand.applyCoor(gridX, gridY);
                            puppetViewer.setCoor(gridX, gridY);
                            break;
                    }
                    break;
            }
            return;
        }
        gridX = limitX(gridX);
        gridY = limitY(gridY);
        Log.e("TAG", "应用之后 x = " + gridX + ", y = " + gridY + " 。");
        hand.applyCoor(gridX, gridY);
        switch (prefer.orien) {
            case HORIZONTAL:
                switch (prefer.layoutGravity) {
                    //横向悬浮窗、从左边开始布局时，直接设置。
                    case LEFT_TOP:
                    case LEFT_BOTTOM:
                        puppetViewer.setCoor(gridX, gridY);
                        break;

                    case RIGHT_TOP:
                    case RIGHT_BOTTOM:
                        puppetViewer.setCoor(gridX - getMenusSize(), gridY);
                        break;
                }
                break;

            case VERTICAL:
                switch (prefer.layoutGravity) {
                    //纵向悬浮窗，从上方开始布局时，直接设置。
                    case LEFT_TOP:
                    case RIGHT_TOP:
                        puppetViewer.setCoor(gridX, gridY);
                        break;

                    case RIGHT_BOTTOM:
                    case LEFT_BOTTOM:
                        puppetViewer.setCoor(gridX, gridY - getMenusSize());
                        break;
                }
                break;
        }
    }

    int currWidth, currHeight;

    public void reSize() {
        adjustSize(prefer.baseSize, prefer.totalSize);
    }

    public void adjustSize(int baseSize, int totalSize) {
        if (!prefer.isExtended) {
            currWidth = currHeight = baseSize;
            hand.applySize(baseSize, baseSize);
        } else {
            switch (prefer.orien) {
                case HORIZONTAL:
                    currWidth = totalSize;
                    currHeight = baseSize;
                    break;
                case VERTICAL:
                    currWidth = baseSize;
                    currHeight = totalSize;
                    break;
            }
            hand.applySize(currWidth, currHeight);
        }
        puppetViewer.adjustSize(baseSize, totalSize);
    }

    /**
     * @param x
     * @return
     */
    private int limitX(int x) {
        if (x < 0) {
            return 0;
        }

        int i = prefer.currWidth - currWidth;
        if (x > i) {
            return i;
        }
        return x;
    }

    private int limitY(int y) {
        if (y < 0) {
            return 0;
        }

        int i = prefer.currHeight - currHeight - prefer.statusBarHeight;
//        Log.e("TAG", " i = " + i + ", prefer.currHeight = " + prefer.currHeight + " 。");
        if (y > i) {
            return i;
        }
        return y;
    }

    //----------------------------------------------------------------------
    public void adjustGridCount(int count) {
        puppetViewer.menuToCount(--count);
    }

    //////////////////////////////////////////////////////////////////////

    /**
     * 需要{@link #adjustAll()}
     */

    public void setOrien(Orien2 orien) {
        puppetViewer.setOrien(orien);
    }
    //----------------------------------------------------------------------

    /**
     * 不需要{@link #adjustAll()}
     */
    //////////////////////////////////////////////////////////////////////
    public PuppetViewer getPuppetViewer() {
        return puppetViewer;
    }
    //////////////////////////////////////////////////////////////////////
    /**
     * 手势触发的功能。
     */
    Function function = Function.PENDING;

    enum Function {
        PENDING,//待验证
        NULL,//无功能
        FOR_MOVE_WIN,//移动窗口
        FOR_MENU,//显示menu
        FOR_FREE//隐藏贴边
    }

    /**
     * todo 根据布局方向改变window坐标。
     */
    private void doMenuFunction() {
        puppetViewer.resetState();
        //menu为展开状态。
        if (prefer.isExtended) {
            if (MENU_FUNCTION_DEBUG) {
                Log.e("TAG", " menu当前为展开状态，执行收起动作。 ");
            }
            prefer.isExtended = false;
            puppetViewer.doFold();
            currWidth = prefer.baseSize;
            currHeight = prefer.baseSize;
            hand.applySize(currWidth, currHeight);
            switch (prefer.orien) {
                case HORIZONTAL:
                    switch (prefer.layoutGravity) {
                        //横向悬浮窗、从右边开始布局时，x坐标往右平移menus的总宽度。
                        case RIGHT_TOP:
                        case RIGHT_BOTTOM:
                            if (MENU_FUNCTION_DEBUG) {
                                Log.e("TAG", " 收起动作：横向、RIGHT时。 ");
                            }
                            //往右平移menu的总宽度。
                            int menusSize = getMenusSize();
                            hand.applyCoor(hand.getX() + menusSize, hand.getY());
                            break;
                    }
                    break;
                case VERTICAL:
                    switch (prefer.layoutGravity) {
                        //纵向悬浮窗、从底边开始布局时，y坐标往下平移menus的总宽度。
                        case LEFT_BOTTOM:
                        case RIGHT_BOTTOM:
                            if (MENU_FUNCTION_DEBUG) {
                                Log.e("TAG", " 收起动作：纵向、BOTTOM时。 ");
                            }
                            int menusSizee = getMenusSize();
                            hand.applyCoor(hand.getX(), hand.getY() + menusSizee);
                            break;
                    }
                    break;
            }
        }
        //menu为收起状态。
        //todo
        else {
            if (MENU_FUNCTION_DEBUG) {
                Log.e("TAG", " menu当前为收起状态，执行展开动作。 ");
            }
            prefer.isExtended = true;
            puppetViewer.doExtend();

            switch (prefer.orien) {
                case HORIZONTAL:
                    currWidth = prefer.totalSize;
                    currHeight = prefer.baseSize;
                    hand.applySize(currWidth, currHeight);
                    switch (prefer.layoutGravity) {
                        //横向悬浮窗、从左边开始布局时，直接设置。
                        case LEFT_TOP:
                        case LEFT_BOTTOM:
                            if (MENU_FUNCTION_DEBUG) {
                                Log.e("TAG", " 展开动作：横向、LEFT时。 ");
                            }
                            int x = hand.getX();
                            int y = hand.getY();
                            setCoor_byWindowLeftTop(x, y);
                            break;
                        //横向悬浮窗、从右边开始布局时，x坐标往左平移menus的总宽度。
                        case RIGHT_TOP:
                        case RIGHT_BOTTOM:
                            if (MENU_FUNCTION_DEBUG) {
                                Log.e("TAG", " 展开动作：横向、RIGHT时。 ");
                            }
                            //往右平移menus的总宽度。
                            int menusSize = getMenusSize();
                            int x1 = hand.getX() - menusSize;
                            int y1 = hand.getY();
                            setCoor_byWindowLeftTop(x1, y1);
                            break;
                    }
                    break;
                case VERTICAL:
                    currWidth = prefer.baseSize;
                    currHeight = prefer.totalSize;
                    hand.applySize(currWidth, currHeight);
                    switch (prefer.layoutGravity) {
                        //纵向悬浮窗、从上边开始布局时，直接设置。
                        case LEFT_TOP:
                        case RIGHT_TOP:
                            if (MENU_FUNCTION_DEBUG) {
                                Log.e("TAG", " 展开动作：纵向、TOP时。 ");
                            }
                            int x = hand.getX();
                            int y = hand.getY();
                            setCoor_byWindowLeftTop(x, y);
                            break;
                        //纵向悬浮窗、从底边开始布局时，y坐标往上平移menus的总宽度。
                        case LEFT_BOTTOM:
                        case RIGHT_BOTTOM:
                            if (MENU_FUNCTION_DEBUG) {
                                Log.e("TAG", " 展开动作：纵向、BOTTOM时。 ");
                            }
                            //往上平移menus的总宽度。
                            int menusSizee = getMenusSize();
                            int x1 = hand.getX();
                            int y1 = hand.getY() - menusSizee;
                            setCoor_byWindowLeftTop(x1, y1);
                            break;
                    }
                    break;
            }
        }

    }

    /**
     * 获取所有menu的总大小。
     * @return
     */
    private int getMenusSize() {
        return prefer.totalSize - prefer.baseSize;
    }

    class HandleEvent implements View.OnTouchListener {
        int recordWinX;//记录的窗口X坐标。
        int recordWinY;//记录的窗口Y坐标。
        private int halfCm = prefer.cm / 2;
        /**
         * 用于判断是否你想（逆向）滑动。
         */
        private float recordCoor;
        private boolean firstPointer;//是否是第一个点按下时。
        private long lastClickTime;//最后一次点击时间
        private boolean needRecord;//是否需要重新记录窗口坐标。

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent ev) {
            int actionMask = ev.getActionMasked();
            EventType type = EventTypeConverter.convert(actionMask);
            pointer.parseEvent(type, ev.getRawX(), ev.getRawY());
            switch (type) {
                case DOWN:
                    if (DEBUG) {
                        Log.e("TAG", Director.class + "：指令view被点击。  ");
                    }
                    function = Function.PENDING;
                    recordWinX = hand.getX();
                    recordWinY = hand.getY();
                    //记录按下时的横向坐标。
                    firstPointer = true;
                    break;
                case POINTER_DOWN:
                    if (firstPointer) {
                        firstPointer = false;
                        function = Function.FOR_MOVE_WIN;
                        break;
                    }
                    int dex = ev.getActionIndex();
                    if (dex == 0) {
                        needRecord = true;
                    }
                    break;
                case POINTER_UP:
                    int dexUp = ev.getActionIndex();
                    if (dexUp == 0) {
                        needRecord = true;
                    }
                    break;
                case MOVE:
                    switch (function) {
                        case PENDING:
                            switch (pointer.getFirstTouchOrien()) {
                                case LEFT:
                                    function = Function.FOR_FREE;
                                    recordCoor = pointer.xMove;
                                    break;
                                case RIGHT:
                                    if (DEBUG) {
                                        Log.e("TAG", " touchFirstOrien = RIGHT。 " + "  ");
                                    }
                                    function = Function.FOR_MENU;
                                    recordCoor = pointer.xMove;
                                    break;
                                case TOP:
                                    break;
                                case BOTTOM:
                                    break;
                            }
                            break;
                        case FOR_MOVE_WIN:
                            if (needRecord) {
                                recordWinX = hand.getX();
                                recordWinY = hand.getY();
                                pointer.recordXY();
                                needRecord = false;
                            }
                            float deltaX_cutRecord = pointer.getDeltaX_cutRecord();
                            float deltaY_cutRecord = pointer.getDeltaY_cutRecord();
                            int x = (int) (recordWinX + deltaX_cutRecord);
                            int y = (int) (recordWinY + deltaY_cutRecord);
                            setCoor_byWindowLeftTop(x, y);
                            break;
                        case FOR_MENU:
                            float yCut = pointer.getAbsDeltaY_cutDown();
                            //纵向超过半厘米，不触发任何功能。
                            if (yCut > halfCm) {
                                if (DEBUG) {
                                    Log.e("TAG", " Y方向大幅度移动，取消任何功能。 " + "  " + "  " + "  ");
                                }
                                function = Function.NULL;
                            } else {

                                float xMove = pointer.xMove;
                                if (xMove > recordCoor) {
                                    recordCoor = pointer.xMove;
                                }
                                //往回滑动超过worm距离时，不触发任何功能。
                                else if (recordCoor - xMove
                                        > pointer.worm) {
                                    if (DEBUG) {
                                        Log.e("TAG", " 往回滑动，取消任何功能。 " + "  " + "  " + "  ");
                                    }
                                    function = Function.NULL;
                                }

                            }
                            break;
                        case FOR_FREE:
//                                if (pointer.xMove - recordCoor > pointer.worm) {
//                                }
                            break;
                        case NULL:
                            break;
                    }
                    break;
                case UP:
                    switch (function) {
                        case PENDING:
                            long time = System.currentTimeMillis();

                            //触发点击。
                            if (time - pointer.timeDown < DurationUT.CLICK_LIMIT_TIME) {
                                //触发双击。
                                if (time - lastClickTime < DurationUT.CLICK_LIMIT_TIME) {
                                    if (DEBUG) {
                                        Log.e("TAG", " 触发双击。 ");
                                    }
                                    lastClickTime = 0;
                                    JumpPageUT.jumpToLastActivity(context);
                                } else {
                                    lastClickTime = time;
                                    if (DEBUG) {
                                        Log.e("TAG", " 触发点击。 ");
                                    }
                                }
                            }
                            break;
                        case NULL:
                            break;
                        case FOR_MOVE_WIN:
                            int y = hand.getY();
                            int x = hand.getX();
                            if (COOR_DEBUG) {
                                Log.e("TAG", " 移动窗口抬起时，窗口坐标：" +
                                        " x = " + x + ", y = " + y + " 。");
                            }
                            setAndSaveCoor_byWindowLeftTop(x, y);
                            if (COOR_DEBUG) {
                                Log.e("TAG", " 移动窗口抬起时，数据存储坐标：" +
                                        " x = " + prefer.getSaveX() + ", y = " + prefer.getSaveY() + " 。");
                            }
                            break;
                        case FOR_MENU:
                            doMenuFunction();
                            break;
                        case FOR_FREE:
                            break;
                    }
                    break;
                case NO_POINTER:
                    break;
                case EXIT_WITH_NO_POINTER:
                    break;
            }
            return true;
        }
    }


    class WindowImpl extends Window {
        /**
         * 构造方法。
         *
         * @param context
         */
        public WindowImpl(Context context) {
            super(context);
        }


        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
//            Log.e("TAG", "  " + "  " + "  " + " onInterceptTouchEvent ");
            return false;
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (DEBUG) {
                        Log.e("TAG", Director.class + "：指令窗口被点击。  ");
                    }
                    isOrder = false;
                    break;
            }
            //点击了指令view时，只给指令view传递。
            if (isOrder) {
                Log.e("TAG", "  ");
                orderView.dispatchTouchEvent(ev);
            }
            //展开时，给所有孩子传递。
            else if (prefer.isExtended) {
                puppetViewer.dispatchTouchEvent(ev);
            }
            //收起时，只给
            else {
                if (DEBUG) {
                    Log.e("TAG", " 传递事件：收起时，只传递给指令view。 ");
                }
                orderView.dispatchTouchEvent(ev);
            }
            return true;
        }

    }
}

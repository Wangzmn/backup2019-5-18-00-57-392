package zmn.w.uiutility.main_class;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import wclass.android.ui.drawable.ColorWithStrokesDrawable;
import wclass.android.app.AppInfo;
import wclass.android.z_pending_class.AppQueryUT;
import wclass.android.encapsulation.DataSave;
import wclass.android.device.ScreenSwitchNotify;
import wclass.android.device.ScreenOrienChangeNotify;
import wclass.android.util.SizeUT;
import wclass.enums.LayoutGravity;
import wclass.enums.Orien2;
import zmn.w.uiutility.main_class.a_pending_class.pending_tidy.SquareMarkerViewer;

/**
 * @作者 做就行了！
 * @时间 2019-02-27上午 12:43
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 * 1、{@link SquareMarkerViewer#icon}的实时margin问题。
 */
@SuppressWarnings("WeakerAccess")
public class Prefer {
    private static final boolean DEBUG = false;
    public static final String TABLE_NAME = "config";
    private static Prefer prefer;
    //----------------------------------------------------------------------
    private Context context;
    ScreenOrienChangeNotify screenOrienChangeObserver;
    ScreenSwitchNotify screenSwitchNotify;
    /**
     * 存放app信息：图标、应用名、应用包名。
     */
    private final ArrayList<AppInfo> appInfos = new ArrayList<>();
    //----------------------------------------------------------------------
    /*数据存储*/

    public int horiX;//横屏时x坐标。
    public int horiY;//横屏时y坐标。
    public int vertiX;//竖屏时x坐标。
    public int vertiY;//竖屏时y坐标。
    //----------------------------------------------------------------------
    /*todo */
    public int baseSize;//小方格基础大小。
    public int gridCount;//小方格当前数量。
    /**
     * 悬浮窗总大小。
     * 该值为{@link #gridCount}和{@link #baseSize}相乘的结果。
     * <p>
     * 该变量变量存在的意义：
     * 1、当悬浮窗匹配屏幕宽时，
     * gridCount*baseSize得到的大小不一定等于屏幕宽，
     * 该变量负责正确的匹配屏幕宽。
     * 2、方便引用。
     */
    public int totalSize;
    /**
     * 正常时的{@link #totalSize}
     */
    int normalTotalSize;
//    int normalGridCount;
    //----------------------------------------------------------------------
    /*次要*/
    public int cm;
    public long cpuRefreshInterval = 1000;//cpu刷新间隔。
    //----------------------------------------------------------------------
    /*参考用*/
    public int width;//手机屏幕宽。
    private int height;//手机屏幕高。
    public int currWidth;//手机当前屏幕宽。屏幕旋转时更新该值。
    public int currHeight;//手机当前屏幕高。屏幕旋转时更新该值。
    public int statusBarHeight;//状态栏高度。

    public int maxCount;//最多能放多少个格子。
    public int minCount;//最少能放多少个格子。
    private final int minBaseSize;//最小基础大小。
    private final int maxBaseSize;//最大基础大小。
    //----------------------------------------------------------------------
    /*暂时*/
    boolean isExtended = true;
    public Director director;

    //////////////////////////////////////////////////////////////////////
    /*domain 构造方法*/
    @SuppressWarnings("SuspiciousNameCombination")
    private Prefer(Context context) {
        this.context = context.getApplicationContext();
        updateCurrWH();
        if (currWidth < currHeight) {
            width = currWidth;
            height = currHeight;
        } else {
            width = currHeight;
            height = currWidth;
        }
        screenOrienChangeObserver = new ScreenOrienChangeNotify(context) {
            @Override
            protected void onNotifyHoriScreen() {
                updateCurrWH();
                super.onNotifyHoriScreen();
            }

            @Override
            protected void onNotifyVertiScreen() {
                updateCurrWH();
                super.onNotifyVertiScreen();
            }
        };
        screenSwitchNotify = new ScreenSwitchNotify(context) {
            @Override
            protected void onNotifyScreenOFF() {
                super.onNotifyScreenOFF();
            }

            @Override
            protected void onNotifyScreenON() {
                super.onNotifyScreenON();
            }
        };
        cm = SizeUT.getCMpixel(context);
        //----------------------------------------------------------------------
        minCount = width / cm;
        maxBaseSize = width / minCount;
        int halfCMpixel = cm / 2;
        maxCount = width / halfCMpixel;
        minBaseSize = width / maxCount;
        //----------------------------------------------------------------------
        statusBarHeight = SizeUT.getStatusBarHeight(context);
        //----------------------------------------------------------------------
        dataSave = new DataSave(context, TABLE_NAME);
        initDataFromLocal();
        //----------------------------------------------------------------------
        totalSize = baseSize * gridCount;
        for_whenMatchScreenWidthAndHasStatusBar();
        normalTotalSize = totalSize;
    }

    public DataSave dataSave;

    //////////////////////////////////////////////////////////////////////
    /*domain 本地存储*/
    private static final String HORI_X = "horiX";
    private static final String HORI_Y = "horiY";
    private static final String VERTI_X = "vertiX";
    private static final String VERTI_Y = "vertiY";
    private static final String BASE_SIZE = "baseSize";
    private static final String GRID_COUNT = "gridCount";
    private static final String ORIEN = "orien";
    private static final String LAYOUT_GRAVITY = "layoutGravity";

    //fix 待废弃
    Orien2 orien;
    //fix 待废弃
    LayoutGravity layoutGravity;

    private void initDataFromLocal() {
        horiX = dataSave.getInt(HORI_X, 0);
        vertiX = dataSave.getInt(VERTI_X, 0);
        horiY = dataSave.getInt(HORI_Y, (width - baseSize));
        vertiY = dataSave.getInt(VERTI_Y, (height - baseSize));
        //----------------------------------------------------------------------
        int defaultSize = width / 6;
        baseSize = dataSave.getInt(BASE_SIZE, defaultSize);
        //----------------------------------------------------------------------
        gridCount = dataSave.getInt(GRID_COUNT, 5);
        //----------------------------------------------------------------------
        int gravity = dataSave.getInt(LAYOUT_GRAVITY, 1);
        layoutGravity = LayoutGravity.fromInt(gravity);
        int orienn = dataSave.getInt(ORIEN, 1);
        orien = Orien2.fromInt(orienn);
        //----------------------------------------------------------------------
        if (DEBUG) {
            Log.e("TAG", this.getClass() + "：layoutGravity = " + layoutGravity.toString()
                    + ", orien = " + orien.toString() + " 。");
        }
    }

    //----------------------------------------------------------------------

    /**
     * 保存悬浮窗口gravity。
     *
     */
    public void saveLayoutGravity() {
        int gravity = layoutGravity.toInt();
        dataSave.putInt(LAYOUT_GRAVITY, gravity);
    }

    /**
     * 保存悬浮窗方向。
     */
    public void saveOrien() {
        int orienn = orien.toInt();
        dataSave.putInt(ORIEN, orienn);
    }

    //----------------------------------------------------------------------
    public void saveCoor_byGridLeftTop(int x, int y) {
        //纵向
        if (currWidth < currHeight) {
            saveVertiCoor(x, y);
        }
        //横向
        else {
            saveHoriCoor(x, y);
        }
    }

    private void saveHoriCoor(int x, int y) {
        horiX = x;
        horiY = y;
        dataSave.putInt(HORI_X, x);
        dataSave.putInt(HORI_Y, y);
    }

    private void saveVertiCoor(int x, int y) {
        vertiX = x;
        vertiY = y;
        dataSave.putInt(VERTI_X, x);
        dataSave.putInt(VERTI_Y, y);
    }

    //----------------------------------------------------------------------
    public void saveBaseSize(int baseSize) {
        this.baseSize = baseSize;
        dataSave.putInt(BASE_SIZE, baseSize);
    }
    //----------------------------------------------------------------------

    public void saveGridCount(int count) {
        this.gridCount = count;
        dataSave.putInt(GRID_COUNT, count);
    }
    //////////////////////////////////////////////////////////////////////
    /*domain 计算个数、大小*/

    /**
     * 悬浮窗匹配屏幕宽时，小方格数量+1。
     */
    public boolean addGridCount_forMatchScreenWidth() {
        int count = prefer.gridCount;
        if (count >= prefer.maxCount) {
            return false;
        }
        count++;
        gridToCount_forMatchScreenWidth(count);
        return true;
    }

    /**
     * 悬浮窗匹配屏幕宽时，小方格数量-1。
     */
    public boolean delGridCount_forMatchScreenWidth() {
        int count = gridCount;
        if (count <= minCount) {
            return false;
        }
        count--;
        gridToCount_forMatchScreenWidth(count);
        return true;
    }

    /**
     * 将小方格数量设置为指定数量。
     */
    private void gridToCount_forMatchScreenWidth(int count) {
        int baseSize = width / count;
        saveBaseSize(baseSize);
        saveGridCount(count);
        totalSize = width;
        normalTotalSize = totalSize;
        for_whenMatchScreenWidthAndHasStatusBar();

        director.adjustGridCount(count);
        director.adjustSize(baseSize, totalSize);
        reCoor();
    }
    //----------------------------------------------------------------------
    /*domain 特殊条件：竖向悬浮窗、横屏时、有状态栏。此时计算小方格数量、总大小。*/
    /**
     * 竖向悬浮窗、横屏时、有状态栏，grid数量。
     */
    int gridCount_whenMatchScreenWidthAndHasStatusBar;
    /**
     * 竖向悬浮窗、横屏时、有状态栏，展开时大小。
     */
    int totalSize_whenMatchScreenWidthAndHasStatusBar;

    /**
     * 竖向悬浮窗、横屏时、有状态栏时，
     * 计算{@link #gridCount_whenMatchScreenWidthAndHasStatusBar}
     * 和{@link #totalSize_whenMatchScreenWidthAndHasStatusBar}。
     */
    private void for_whenMatchScreenWidthAndHasStatusBar() {
        int usable = width - statusBarHeight;
        for (int i = gridCount; i > 0; i--) {
            int totalSize = i * baseSize;
            if (totalSize < usable) {
                gridCount_whenMatchScreenWidthAndHasStatusBar = i;
                totalSize_whenMatchScreenWidthAndHasStatusBar = totalSize;
                return;
            }
        }
        //走到这里时，说明什么。。。
        gridCount_whenMatchScreenWidthAndHasStatusBar = 1;
        totalSize_whenMatchScreenWidthAndHasStatusBar = baseSize;
    }
    //////////////////////////////////////////////////////////////////////
    /**/

    /**
     * 获取apps信息。
     */
    @SuppressWarnings("unchecked")
    public List<AppInfo> getAppInfos() {
        if (appInfos.size() == 0) {
            return getNewApps();
        }
        return (List<AppInfo>) appInfos.clone();
    }

    /**
     * 获取最新的apps信息。
     */
    @SuppressWarnings("unchecked")
    public List<AppInfo> getNewApps() {
        synchronized (appInfos) {
            appInfos.clear();
            AppQueryUT.getAppInfos(context,appInfos);
            return (List<AppInfo>) appInfos.clone();
        }
    }

    //----------------------------------------------------------------------
    /*domain 计算大小。*/
    public int calculateSize(int count) {
        int size = width / count;
        int remainder = width % count;
        float scale = (float) width / (width - remainder);
        return (int) (size * scale);
    }

    //----------------------------------------------------------------------
    public Context context() {
        return context;
    }
    //----------------------------------------------------------------------

    public ScreenOrienChangeNotify getScreenOrienObserver() {
        return screenOrienChangeObserver;
    }

    public ScreenSwitchNotify getScreenSwitchObserver() {
        return screenSwitchNotify;
    }

    //////////////////////////////////////////////////////////////////////
    private void updateCurrWH() {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        currWidth = dm.widthPixels;
        currHeight = dm.heightPixels;
    }


    public static void init(Context context) {
            prefer = new Prefer(context);
    }

    public static Prefer getInstance() {
        return prefer;
    }

    //////////////////////////////////////////////////////////////////////
    /*domain 横竖屏切换时调用的方法*/
    public void toScreenHori() {
        if (normalTotalSize == totalSize_whenMatchScreenWidthAndHasStatusBar) {
            return;
        }
        totalSize = totalSize_whenMatchScreenWidthAndHasStatusBar;
        gridCount = gridCount_whenMatchScreenWidthAndHasStatusBar;
        director.adjustSize(baseSize, totalSize);
    }

    public void toScreenVerti() {
        if (normalTotalSize == totalSize_whenMatchScreenWidthAndHasStatusBar) {
            return;
        }
        totalSize = normalTotalSize;
        director.adjustSize(baseSize, totalSize);
    }
    //////////////////////////////////////////////////////////////////////
    /*domain 调整baseSize*/

    /**
     * 运行于 2019年3月1日22:08:26
     * <p>
     * 减少小方格基础大小。{@link #baseSize}
     * <p>
     * 友情提示：可以直接缩小，不需要复杂的约束。
     */
    public void delSize(int delSize) {
        if (delSize <= 0) throw new AssertionError("别手贱。");
        int baseSize = this.baseSize;
        if (baseSize == minBaseSize) {
            return;
        }
        baseSize -= delSize;
        if (baseSize < minBaseSize) {
            baseSize = minBaseSize;
        }
        saveBaseSize(baseSize);
        totalSize = baseSize * gridCount;
        normalTotalSize = totalSize;
        for_whenMatchScreenWidthAndHasStatusBar();

        director.adjustSize(baseSize, totalSize);
        reCoor();
    }

    /**
     * 运行于 2019年3月1日22:08:26
     * <p>
     * 增加小方格基础大小。{@link #baseSize}
     */
    public void addSize(int addSize) {
        if (addSize <= 0) throw new AssertionError("别手贱。");
        int baseSize = this.baseSize;
        if (baseSize == maxBaseSize || totalSize == width) {
            return;
        }
        baseSize += addSize;
        //超出屏幕宽了。则通过宽计算基础大小。
        totalSize = gridCount * baseSize;
        if (totalSize > width) {
            baseSize = width / gridCount;
            totalSize = width;
        }
        saveBaseSize(baseSize);
        normalTotalSize = totalSize;
        for_whenMatchScreenWidthAndHasStatusBar();

        director.adjustSize(baseSize, totalSize);
        reCoor();
    }
    //----------------------------------------------------------------------

    /**
     * 运行于 2019年3月1日22:08:26
     * <p>
     * 设置悬浮窗口的gravity。
     *
     * @param layoutGravity gravity
     */
    public void setLayoutGravity(LayoutGravity layoutGravity) {
        this.layoutGravity = layoutGravity;
        director.setLayoutGravity(layoutGravity);
        reCoor();
        saveLayoutGravity();
    }

    /**
     * 运行于 2019年3月1日22:08:26
     * <p>
     * <p>
     * 设置为横向/纵向 悬浮窗口。
     *
     * @param orien 布局方向。
     */
    public void setOrien(Orien2 orien) {
        this.orien = orien;
        director.setOrien(orien);
        director.reSize();
        reCoor();
        saveOrien();
        if (DEBUG) {
            Log.e("TAG", director.hand.toStr_windowInfo());
        }
    }

    /**
     * 运行于 2019年3月1日22:07:37
     * <p>
     * 根据gravity，重新设置坐标。
     */
    private void reCoor() {
        if (DEBUG) {
            switch (layoutGravity) {
                case LEFT_TOP:
                    director.setAndSaveCoor_byGridLeftTop(0, 0);
                    break;
                case LEFT_BOTTOM:
                    director.setAndSaveCoor_byGridLeftTop(0, prefer.currHeight);
                    break;
                case RIGHT_TOP:
                    director.setAndSaveCoor_byGridLeftTop(prefer.currWidth, 0);
                    break;
                case RIGHT_BOTTOM:
                    director.setAndSaveCoor_byGridLeftTop(prefer.currWidth, prefer.currHeight);
                    break;
            }
            if (DEBUG) {
                Log.e("TAG", director.hand.toStr_windowInfo());
            }
            return;
        }
        switch (layoutGravity) {
            case LEFT_TOP:
            case LEFT_BOTTOM:
                director.setAndSaveCoor_byGridLeftTop(0, prefer.currHeight);
                break;
            case RIGHT_TOP:
            case RIGHT_BOTTOM:
                director.setAndSaveCoor_byGridLeftTop(prefer.currWidth, prefer.currHeight);
                break;
        }
    }

    //////////////////////////////////////////////////////////////////////
    public Drawable getMenuTouchDrawable() {
        return null;
    }

    public Drawable getDefaultAddIcon() {
        return null;
    }

    public Drawable getMenuWinLabelDrawable() {
        return null;
    }

    public ColorWithStrokesDrawable bg_translucentWhite =
            new ColorWithStrokesDrawable(
            0x22ffffff, 1 / 10f,
            0xdd000000, 1, 0xff000000, 1, 0xffffffff, 1
    );
    public ColorWithStrokesDrawable bg_translucentBlue = new ColorWithStrokesDrawable(
            0x550000ff, 1 / 10f,
            0xdd000000, 1, 0xff000000, 1, 0xffffffff, 1
    );
    public ColorWithStrokesDrawable bg_translucentBlack = new ColorWithStrokesDrawable(
            0x88000000, 1 / 10f,
            0xdd000000, 1, 0xff000000, 1, 0xffffffff, 1
    );

    public Drawable getWinBG() {
        return bg_translucentWhite;
    }

    /**
     * 获取屏幕方向状态。
     */
    public Orien2 getScreenOrien() {
        //竖屏
        if(currWidth<currHeight){
            return Orien2.VERTICAL;
        }
        //横屏
        else{
            return Orien2.HORIZONTAL;
        }
    }

    public int getSaveX() {
        switch (getScreenOrien()) {
            case HORIZONTAL:
                return horiX;
            case VERTICAL:
                return vertiX;
            default:
                throw new IllegalStateException();
        }
    }

    public int getSaveY() {
        switch (getScreenOrien()) {
            case HORIZONTAL:
                return horiY;
            case VERTICAL:
                return vertiY;
            default:
                throw new IllegalStateException();
        }
    }

    //////////////////////////////////////////////////////////////////////

}

package wclass.ui.event_parser;

import wclass.enums.Orien5;

/**
 * @作者 做就行了！
 * @时间 2019-04-14下午 4:44
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
/**
 * @param <C> 子类类型。
 */
@SuppressWarnings({"WeakerAccess", "unused", "DanglingJavadoc", "unchecked", "JavaDoc"})
public class PointerCodes<C extends PointerCodes> implements Cloneable {


    /**
     * {@link #PointerCodes(boolean, int, float, float)}
     */
    public PointerCodes(int worm) {
        this(false, worm);
    }

    /**
     * {@link #PointerCodes(boolean, int, float, float)}
     */
    public PointerCodes(int worm, float perH, float perV) {
        this(false, worm, perH, perV);
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * {@link #PointerCodes(boolean, int, float, float)}
     */
    public PointerCodes(boolean needRecordActTime, int worm) {
        this.needRecordActTime = needRecordActTime;
        setWorm(worm);
    }

    /**
     * 构造方法。设置触发移动的最短距离。
     *
     * @param needRecordActTime true：记录此次事件的时间。
     * @param worm              触发移动的最短距离。
     *                          android平台获取方式：
     *                          ViewConfiguration.get(context).getScaledTouchSlop()。
     * @param perH              横向worm的百分比
     * @param perV              纵向worm的百分比
     */
    public PointerCodes(boolean needRecordActTime, int worm, float perH, float perV) {
        this.needRecordActTime = needRecordActTime;
        this.worm = worm;
        setWormH(perH);
        setWormV(perV);
    }
    //----------------------------------------------------------------------

    public void reset() {
        timeDown = timeRecord = 0;
        xDown = yDown
                = xMove = yMove
                = xMoveOld = yMoveOld
                = xRecord = yRecord
                = 0;
        firstOrien5 = Orien5.SITU;
        verifyFirstTouchOrienFinished = false;

        if (needRecordActTime) {
            xRecord_ = 0;
            yRecord_ = 0;
            timeRecordPrivate = 0;
        }
    }
    //////////////////////////////////////////////////////////////////////
    /*step 主要方法！！！*/

    /**
     * 子类可在DOWN事件时通过该方法记录事件信息。
     *
     * @param x x坐标
     * @param y y坐标
     */
    protected final void onDownEvent(float x, float y) {
        xDown = xMove = xMoveOld = xRecord = x;
        yDown = yMove = yMoveOld = yRecord = y;
        timeDown = now();
        firstOrien5 = Orien5.SITU;
        verifyFirstTouchOrienFinished = false;

        if (needRecordActTime) {
            xRecord_ = x;
            yRecord_ = y;
            timeRecordPrivate = timeDown;
        }
    }
/**
 * {@link PointerCodes#onDownEvent(float, float)}
 * {@link PointerCodes#onMoveEvent(float, float)}
 */
    /**
     * 子类可在MOVE事件时通过该方法记录事件信息。
     *
     * @param x x坐标
     * @param y y坐标
     */
    protected final void onMoveEvent(float x, float y) {
        xMoveOld = xMove;
        yMoveOld = yMove;
        xMove = x;
        yMove = y;
        verifyFirstWay();

        if (needRecordActTime) {
            verifySuspend();
        }
    }
    //////////////////////////////////////////////////////////////////////
    /*step 功能：滑动时记录时间。*/

    /**
     * 判断是否暂停滑动过。
     * <p>
     * 详述：以调用该方法时 的时间为参照，判断之前的一段时间是否暂停滑动过。
     *
     * @param suspendNeedTime 触发暂停滑动的时间
     * @return true：暂停滑动过。
     */
    public final boolean isSuspend(long suspendNeedTime) {
        assert needRecordActTime :
                "开启“needRecordActTime”功能后，才能使用该方法。";
        return now() - timeRecordPrivate > suspendNeedTime;
    }

    /**
     * 是否需要 记录此次事件的时间。
     * <p>
     * 友情提示：
     * 开启后可使用验证是否暂停滑动的方法：{@link #isSuspend(long)}。
     */
    @Deprecated
    private boolean needRecordActTime;
    private float xRecord_;//记录x坐标。
    private float yRecord_;//记录y坐标。
    private long timeRecordPrivate;//记录时间。
    //----------------------------------------------------------------------

    /**
     * 滑动时，记录时间。
     */
    private void verifySuspend() {
        //判断当前坐标与记录坐标的差是否大与worm。
        if (absKoWormH(xMove - xRecord_) || absKoWormV(yMove - yRecord_)) {
            xRecord_ = xMove;
            yRecord_ = yMove;
            timeRecordPrivate = now();
        }
    }
    //////////////////////////////////////////////////////////////////////
    /*step 功能：验证第一次触摸滑动的方向。*/

    /**
     * 是否需要 验证第一次触摸滑动的方向。
     */
    private boolean verifyFirstTouchOrienFinished = false;

    /**
     * 第一次触摸方向，{@link Orien5}。
     */
    private Orien5 firstOrien5 = Orien5.SITU;

    /**
     * 获取此次触摸事件第一次滑动方向。
     *
     * @return 此次触摸事件第一次滑动方向
     */
    public final Orien5 getFirstTouchOrien() {
        return firstOrien5;
    }

    /**
     * 滑动触发方向时的坐标。
     * <p>
     * 友情提示：横向时记录x坐标，纵向时记录y坐标。
     */
    private float firstWayCoor;

    public float getFirstWayCoor() {
        return firstWayCoor;
    }

    /**
     * 分析第一次触摸方向。
     */
    private void verifyFirstWay() {
        if (verifyFirstTouchOrienFinished) {
            return;
        }
        float xCut = getDeltaX_cutDown();
        float yCut = getDeltaY_cutDown();

        float xAbs = abs(xCut);
        float yAbs = abs(yCut);

        //x方向距离大，先验证x方向。
        if(xAbs>yAbs){
            //横向
            if (xAbs > wormH) {
                //朝左
                if (xCut < 0) {
                    firstOrien5 = Orien5.LEFT;
                }
                //朝右
                else {
                    firstOrien5 = Orien5.RIGHT;
                }
                firstWayCoor = xMove;
                verifyFirstTouchOrienFinished = true;
            }
        }
        //y方向距离大，验证y方向。
        else{
            //朝上
            if (yCut < 0) {
                firstOrien5 = Orien5.TOP;
            }
            //朝下
            else {
                firstOrien5 = Orien5.BOTTOM;
            }
            firstWayCoor = yMove;
            verifyFirstTouchOrienFinished = true;
        }

    }
    //////////////////////////////////////////////////////////////////////

    @Override
    public C clone() throws CloneNotSupportedException {
        return (C) super.clone();
    }
    //////////////////////////////////////////////////////////////////////
    /**
     * 按下时的时间。
     */
    public long timeDown;

    /**
     * 记录的当前时间。通常用于之后的比较操作。
     * 通过{@link #recordTime()}&{@link #record()}记录当前时间。
     */
    public long timeRecord;

    /**
     * 按下时的x。
     */
    public float xDown;

    /**
     * 按下时的y。
     */
    public float yDown;

    /**
     * 实时的x。
     */
    public float xMove;

    /**
     * 实时的y。
     */
    public float yMove;

    /**
     * 被替换的{@link #xMove}
     */
    public float xMoveOld;

    /**
     * 被替换的{@link #yMove}
     */
    public float yMoveOld;

    /**
     * 通过{@link #recordXY()}记录{@link #xMove}&{@link #yMove}。
     */
    public float xRecord;
    public float yRecord;

    /**
     * 触发移动的最短距离。
     * 通过{@link #setWormH}{@link #setWormV}自定义这个距离。
     */
    public float worm;

    /**
     * 横向 触发移动的最短距离。
     * {@link #wormH}参照{@link #worm}进行设置。
     */
    public float wormH;

    /**
     * 纵向 触发移动的最短距离。
     * {@link #wormV}参照{@link #worm}进行设置。
     */
    public float wormV;

    //////////////////////////////////////////////////////////////////////
    /**
     * step 记录当前坐标、时间，用于之后的对比。
     * 建议把这部分功能留给用户使用。
     * 不建议包装类中使用这部分功能。
     */
    /*检查于 2018-12-26 00:28:31*/

    /**
     * 同时记录当前坐标和时间。
     */
    public final void record() {
        recordXY();
        recordTime();
    }

    /**
     * 记录当前坐标，方便与之后的坐标进行对比。
     */
    public final void recordXY() {
        xRecord = xMove;
        yRecord = yMove;
    }

    /**
     * 记录指定坐标，方便与之后的坐标进行对比。
     */
    public final void recordXY(float x,float y) {
        xRecord = x;
        yRecord = y;
    }

    /**
     * 记录时间。
     */
    public final void recordTime() {
        timeRecord = now();
    }

    /**
     * @return 当前x坐标与记录的坐标之差。
     */
    public final float getDeltaX_cutRecord() {
        return xMove - xRecord;
    }

    /**
     * @return 当前y坐标与记录的坐标之差。
     */
    public final float getDeltaY_cutRecord() {
        return yMove - yRecord;
    }

    /**
     * @return 当前x坐标与记录的坐标之差 的绝对值。
     */
    public final float getAbsDeltaX_cutRecord() {
        return abs(xMove - xRecord);
    }

    /**
     * @return 当前y坐标与记录的坐标之差 的绝对值。
     */
    public final float getAbsDeltaY_cutRecord() {
        return abs(yMove - yRecord);
    }
    //////////////////////////////////////////////////////////////////////
    /*step 限制的移动距离相关。*/
    /*检查于 2018年12月26日00:32:39*/

    /**
     * @see #worm
     * 该方法会重置{@link #wormH}&{@link #wormV}。
     */
    public final C setWorm(float worm) {
        this.worm = worm;
        wormH = worm;
        wormV = worm;
        return (C) this;
    }

    /**
     * 调整触发移动的最小距离。
     *
     * @param per 缩放比例
     */
    public final C setWormH(float per) {
        checkPer(per);
        wormH = (worm * per);
        return (C) this;
    }

    /**
     * 调整触发移动的最小距离。
     *
     * @param per 缩放比例
     */
    public final C setWormV(float per) {
        checkPer(per);
        wormV = (int) (worm * per);
        return (C) this;
    }

    private void checkPer(float per) {
        if (per <= 0) {
            throw new IllegalArgumentException
                    ("请求的缩放比例为：" + per + "。" +
                            "缩放比例必须大于0！");
        }
    }

    /**
     * {@link #worm}
     */
    public final float getWorm() {
        return worm;
    }

    /**
     * {@link #wormH}
     */
    public final float getWormH() {
        return wormH;
    }

    /**
     * {@link #wormV}
     */
    public final float getWormV() {
        return wormV;
    }

    /**
     * @return true：是小幅移动。false：反之。
     */
    public final boolean isWorm() {
        return isWormX() && isWormY();
    }

    /**
     * @return true：x方向是小幅移动。false：反之。
     */
    public final boolean isWormX() {
        return getAbsDeltaX_cutDown() < wormH;
    }

    /**
     * @return true：y方向是小幅移动。false：反之。
     */
    public final boolean isWormY() {
        return getAbsDeltaY_cutDown() < wormV;
    }

    /**
     * @return true：相对于记录的坐标 是小幅移动。false：反之。
     */
    public final boolean isWormForRecord() {
        return isWormXForRecord() && isWormYForRecord();
    }

    /**
     * @return true：x方向相对于记录的坐标 是小幅移动。false：反之。
     */
    public final boolean isWormXForRecord() {
        return getAbsDeltaX_cutRecord() < wormH;
    }

    /**
     * @return true：y方向相对于记录的坐标 是小幅移动。false：反之。
     */
    public final boolean isWormYForRecord() {
        return getAbsDeltaY_cutRecord() < wormV;
    }

    //////////////////////////////////////////////////////////////////////
    /*step 绝对值相关*/
    /*检查于 2018年12月26日00:26:53*/

    /**
     * {@link #abs(double)}
     */
    public final int abs(int a) {
        return Math.abs(a);
    }

    /**
     * {@link #abs(double)}
     */
    public final long abs(long a) {
        return Math.abs(a);
    }

    /**
     * {@link #abs(double)}
     */
    public final float abs(float a) {
        return Math.abs(a);
    }

    /**
     * 求a的绝对值。
     *
     * @param a 这就是a
     * @return a的绝对值
     */
    public final double abs(double a) {
        return Math.abs(a);
    }
    //----------------------------------------------------------------------
    /*step 绝对值和worm进行比较*/
    /*检查于 2018年12月26日00:26:14*/

    /**
     * 判断 a的绝对值 是否大于{@link #worm}
     *
     * @param a 这就是a
     * @return true：a的绝对值 较大。false：较小。
     */
    public final boolean absKoWorm(double a) {
        return abs(a) > worm;
    }

    /**
     * 判断 a的绝对值 是否大于{@link #wormH}。
     *
     * @param a 这就是a
     * @return true：a的绝对值 较大。false：较小。
     */
    public final boolean absKoWormH(double a) {
        return abs(a) > wormH;
    }

    /**
     * 判断 a的绝对值 是否大于{@link #wormV}。
     *
     * @param a 这就是a
     * @return true：a的绝对值 较大。false：较小。
     */
    public final boolean absKoWormV(double a) {
        return abs(a) > wormV;
    }

    //////////////////////////////////////////////////////////////////////
    /*step 各种差值*/
    /*检查于 2018年12月26日00:25:09*/

    /**
     * @return 相对于xDown，x方向移动的距离
     */
    public final float getDeltaX_cutDown() {
        return xMove - xDown;
    }

    /**
     * @return 相对于yDown，y方向移动的距离
     */
    public final float getDeltaY_cutDown() {
        return yMove - yDown;
    }

    /**
     * @return 相对于xMoveOld，x方向移动的距离
     */
    public final float getDeltaX_cutMove() {
        return xMove - xMoveOld;
    }

    /**
     * @return 相对于yMoveOld，y方向移动的距离
     */
    public final float getDeltaY_cutMove() {
        return yMove - yMoveOld;
    }

    /**
     * @return 滚动时，相对于xDown，x方向移动的距离
     */
    public final float getScrollDeltaX_cutDown() {
        return xDown - xMove;
    }

    /**
     * @return 滚动时，相对于yDown，y方向移动的距离
     */
    public final float getScrollDeltaY_cutDown() {
        return yDown - yMove;
    }

    /**
     * @return 滚动时，相对于xMoveOld，x方向移动的距离
     */
    public final float getScrollDeltaX_cutMove() {
        return xMoveOld - xMove;
    }

    /**
     * @return 滚动时，相对于yMoveOld，y方向移动的距离
     */
    public final float getScrollDeltaY_cutMove() {
        return yMoveOld - yMove;
    }
    //----------------------------------------------------------------------

    /**
     * @return x方向移动的距离，取绝对值。
     */
    public final float getAbsDeltaX_cutDown() {
        return abs(xMove - xDown);
    }

    /**
     * @return y方向移动的距离，取绝对值。
     */
    public final float getAbsDeltaY_cutDown() {
        return abs(yMove - yDown);
    }
    //////////////////////////////////////////////////////////////////////
    /*step 时间相关*/
    /*检查于 2018年12月26日00:25:09*/

    /**
     * @return 获取当前时间。
     */
    public final long now() {
        return System.currentTimeMillis();
    }

    /**
     * @return 返回 与按下时间的 时间差。
     */
    public final long getTimeDelta_cutDown() {
        return now() - timeDown;
    }

    /**
     * @return 返回 与记录时间的 时间差。
     */
    public final long getTimeDelta_cutRecord() {
        return now() - timeRecord;
    }

    //////////////////////////////////////////////////////////////////////
    /*domain DEBUG*/
    public String toStr_downXY() {
        String s = "[xDown = " + xDown +
                ", yDown = " + yDown + "]";
        return s;
    }

    public String toStr_moveXY() {
        String s = "[xMove = " + xMove +
                ", yMove = " + yMove + "]";
        return s;
    }
}

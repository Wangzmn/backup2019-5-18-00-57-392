package wclass.z_pending_class.scroller;

import android.widget.Scroller;

import wclass.y_marks.Ugly_Class;
import wclass.y_marks.Ugly_Method;
import wclass.util.AnimUT;
import wclass.strategy.interpolator.JInterpolator;
import wclass.util.MathUT;

/**
 * @作者 做就行了！
 * @时间 2019-01-07下午 11:20
 * @该类用途： -
 * 1、处理非触摸时的滑动。
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @待解决： -
 * 1、{@link #flingToAfter(int, int, int)}{@link #flingToBefore(int, int, int)}
 * 中，limit限制了距离时，duration应该适当减少。
 * 2、{@link #getDuration(int)}
 * 通过速率获取的时长，应该通过e为底的指数级别的缩减
 */
@SuppressWarnings("WeakerAccess")
@Ugly_Class
public class JScroller {
    private static final float SCROLL_FRICTION = 0.015f;
    //----------------------------------------------------------------------
    private long mStartTime;//开始时间

    private int mStartValue;//开始时的数值
    private int mFinalValue;//结束时的数值
    /**
     * 通过{@link #computeValue()}计算出的当前数值。
     */
    private int mCurrValue;
    /**
     * 已消耗的持续时间。
     */
    private long mConsumeDuration;
    /**
     * 持续时间。
     */
    private float mDuration;
    /**
     * 是否结束。
     */
    private boolean mFinish = true;
    /**
     * 是否暂停。
     * 通过{@link #pause()}暂停。
     * 通过{@link #continuee()}继续进行。
     */
    private boolean mPause;
    //----------------------------------------------------------------------
    /**
     * 最后一次{@link #computeValue()}的时间。
     */
    private long lastTime;
    //----------------------------------------------------------------------
    //////////////////////////////////////////////////////////////////////
    /**
     * 每英寸像素点。pixel per inch。
     */
    private float ppi;
    //----------------------------------------------------------------------
    JInterpolator defaultInterpolator;
    JInterpolator ingInterpolator;
    Scroller s;

    {

    }

    public JScroller(float ppi) {
        this.ppi = ppi;
    }

    public JScroller(float ppi, JInterpolator interpolator) {
        this.ppi = ppi;
        defaultInterpolator = interpolator;
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 检查于 2019年1月15日00:55:42
     * <p>
     * 暂停计算数值。通过{@link #continuee()}方法继续进行。
     * <p>
     * 友情提示：
     * 1、通过{@link #continuee()}方法继续进行后，可无缝衔接的计算数值。
     */
    public void pause() {
        if (!mFinish && !mPause) {
            mPause = true;
        }
    }

    /**
     * 检查于 2019年1月15日00:55:42
     * <p>
     * 继续计算数值。通过{@link #pause()}暂停后，可使用该方法继续进行。
     */
    public void continuee() {
        if (!mFinish && mPause) {
            mPause = false;
            lastTime = currTime();
        }
    }
    //----------------------------------------------------------------------

    /**
     * 检查于 2019年1月15日00:55:42
     * <p>
     * 用户自由控制是否停止计算。
     * <p>
     * 警告：
     * 1、通过该方法停止开启后，停止时消耗的时间也将纳入计算需求。
     * 2、通过该方法停止后，无法获取正确的状态信息。
     * 如果你需要此需求，请使用{@link #abortAnimation()}方法。
     *
     * @param finish true：停止计算。false：开启计算。
     */
    public void forceFinish(boolean finish) {
        mFinish = finish;
    }

    /**
     * 检查于 2019年1月15日00:55:35
     * <p>
     * 停止计算数值，并将当前的数值作为最终的数值。
     * <p>
     * 友情提示：通过该方法停止后，可获取正确的状态信息。
     */
    public void abortAnimation() {
        mFinish = true;
        mFinalValue = mCurrValue;
        mDuration = mConsumeDuration;
    }
    //////////////////////////////////////////////////////////////////////
    /*step 主要方法*/

    /**
     * 检查于 2019年1月15日00:56:12
     * <p>
     * 获取当前数值。
     * 友情提示：通过{@link #computeValue()}方法计算得出。
     */
    public int getCurrValue() {
        return mCurrValue;
    }

    /**
     * 检查于 2019年1月14日23:31:48
     * <p>
     * 计算当前数值。
     * <p>
     * 友情提示：如果返回true，则可通过{@link #getCurrValue()}获取此次计算得到的数值。
     *
     * @return true：持续时间中，计算出结果了。
     * false：持续时间结束了。
     */
    public boolean computeValue() {
        if (mFinish || mPause) {
            return false;
        }

        long now = currTime();
        long timeCut = now - lastTime;
        //当前持续时间
        long currDuration = mConsumeDuration + timeCut;

        //结束了
        /**
         * 如果用户手贱直接调用{@link #forceFinish(boolean)}，
         * 则会走该分支。
         */
        if (currDuration >= mDuration) {
            mCurrValue = mFinalValue;
            mFinish = true;
        }
        //未结束
        else {
            mCurrValue = getProgressValue(currDuration / mDuration);
            lastTime = now;
        }
        mConsumeDuration = currDuration;
        return true;
    }

    /**
     * 检查于 2019年1月14日23:59:11
     * <p>
     * 获取当前速率。
     *
     * @return 当前速率。
     * 返回值<0时：往之前。
     * 返回值>0时：往之后。
     */
    public int getCurrVelocity() {
        if (mFinish || mPause) {
            return 0;
        }
        long now = currTime();
        long timeCut = now - lastTime;
        //当前持续时间
        float currDuration = mConsumeDuration + timeCut;

        //结束了
        if (currDuration >= mDuration) {
            return 0;
        }

        //当前时间进度百分比
        int per;
        per = MathUT.toPercentageInt(currDuration / mDuration);

        //起点、终点、距离。
        int distanceO, distanceD, distanceCut;
        distanceO = getProgressValue(per / 100f);
        distanceD = getProgressValue((per + 1) / 100f);
        //距离
        distanceCut = distanceD - distanceO;
        //时间
        float centiDuration = mDuration / 100;
        //速度=距离/时间
        return (int) (distanceCut / centiDuration);
    }
    //----------------------------------------------------------------------

    /**
     * 获取当前进度值。
     *
     * @param per 进度百分比
     * @return 当前进度值
     */
    private int getProgressValue(float per) {
        //起始值+进度值
        return (int) (mStartValue +
                (mFinalValue - mStartValue) * defaultInterpolator.getInterpolation(per));
    }

    //----------------------------------------------------------------------
    private long currTime() {
        return System.currentTimeMillis();
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 检查于 2019年1月23日17:29:04
     * <p>
     * {@link #smoothScroll(int, int, long, JInterpolator)}
     */
    public void smoothScroll(int startValue, int finalValue) {
        smoothScroll(startValue, finalValue, AnimUT.DEFAULT_DURATION);
    }

    /**
     * 检查于 2019年1月23日17:29:04
     * <p>
     * {@link #smoothScroll(int, int, long, JInterpolator)}
     */
    public void smoothScroll(int startValue, int finalValue, long duration) {
        smoothScroll(startValue, finalValue, duration, defaultInterpolator);
    }

    /**
     * 检查于 2019年1月23日17:29:04
     * <p>
     * {@link #smoothScroll(int, int, long, JInterpolator)}
     */
    public void smoothScroll(int startValue, int finalValue, JInterpolator interpolator) {
        smoothScroll(startValue, finalValue, AnimUT.DEFAULT_DURATION, interpolator);
    }

    /**
     * 检查于 2019年1月23日17:29:40
     *
     * @param startValue 开始时的数值
     * @param finalValue 结束时的数值
     * @param duration   持续时间
     */
    public void smoothScroll(int startValue, int finalValue, long duration, JInterpolator interpolator) {
        if (duration < 0) {
            throw new IllegalArgumentException("请求的duration为：" + duration + " 。");
        }
        mStartValue = startValue;
        mFinalValue = finalValue;
        mDuration = duration;
        ingInterpolator = interpolator;
        flagStart();
    }
    //----------------------------------------------------------------------

    /**
     * 往之前快速滑动。数值是减少的。
     *
     * @param velocity   速率。（该值为正数）
     * @param startValue 开始数值。
     * @param limitValue 限制数值。
     */
    public void flingToBefore(int velocity, int startValue, int limitValue) {
        if (limitValue > startValue) {
            throw new IllegalArgumentException("限制数值必须小于当前数值。"
                    + "velocity = " + velocity
                    + "，startValue = " + startValue
                    + "，limitValue = " + limitValue + " 。");
        }
        velocity = Math.abs(velocity);
        long duration = getDuration(velocity);
        mStartValue = startValue;
        //总距离=速度*时长+初始值。
        int value = (int) (velocity / 2 * duration) + startValue;
        if (value < limitValue) {
            //todo 总距离被限制了，持续时间缩减多少合适？
            mFinalValue = limitValue;

        } else {
            mFinalValue = value;
            mDuration = duration;
        }
        flagStart();
    }

    /**
     * 往之后快速滑动。数值是增加的。
     *
     * @param velocity   速率。（该值为正数）
     * @param startValue 开始数值。
     * @param limitValue 限制数值。
     */
    public void flingToAfter(int velocity, int startValue, int limitValue) {
        if (limitValue < startValue) {
            throw new IllegalArgumentException("限制数值必须大于当前数值。"
                    + "velocity = " + velocity
                    + "，startValue = " + startValue
                    + "，limitValue = " + limitValue + " 。");
        }
        velocity = Math.abs(velocity);
        mDuration = getDuration(velocity);
        mStartValue = startValue;
        //总距离=速度*时长+初始值。
        int finalValueCompute = (int) (velocity / 2 * mDuration) + startValue;
        mFinalValue = finalValueCompute > limitValue ? limitValue : finalValueCompute;
        flagStart();
    }
    //----------------------------------------------------------------------

    /**
     * 标记为开始状态。
     */
    private void flagStart() {
        mFinish = false;
        mPause = false;
        mStartTime = lastTime = currTime();
    }

    //----------------------------------------------------------------------
    /*fix 未完成*/
    @Ugly_Method
    private long getDuration(int velocity) {
        return (long) (velocity / getFrictionVelocity(SCROLL_FRICTION));
    }

    @SuppressWarnings("SameParameterValue")
    private float getFrictionVelocity(float friction) {
//        return PhysicalUT.GRAVITY_EARTH
//                * RatioUT.METER_2_INCH
//                * ppi
//                * friction;
        return Integer.MAX_VALUE;
    }
    //////////////////////////////////////////////////////////////////////

    public long getStartTime() {
        return mStartTime;
    }

    public int getStartValue() {
        return mStartValue;
    }

    public int getFinalValue() {
        return mFinalValue;
    }

    public long getConsumeDuration() {
        return (long) mConsumeDuration;
    }

    public long getDuration() {
        return (long) mDuration;
    }

    //----------------------------------------------------------------------
    public boolean isFinish() {
        return mFinish;
    }

    public boolean isPause() {
        return !mFinish && mPause;
    }
}
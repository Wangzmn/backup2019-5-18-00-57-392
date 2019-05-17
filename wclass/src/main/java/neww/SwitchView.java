package neww;

import android.content.Context;
import android.widget.ImageView;

import ex.Length;
import wclass.android.ui.drawable.ColorWithStrokesDrawable;
import wclass.android.ui.drawable.ProgressDrawable;
import wclass.android.util.LayoutParamsUT;
import wclass.android.util.SizeUT;
import wclass.android.util.ViewUT;
import wclass.util.ColorUT;

/**
 * @作者 做就行了！
 * @时间 2019-05-15下午 5:07
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class SwitchView extends SimpleProgressBar {
    private ColorWithStrokesDrawable bgPic;//滑道图片。
    private ProgressDrawable progressPic;//进度图片。
    private ColorWithStrokesDrawable thumbPic;//滑块图片。
    private int margin;//作为进度view的外边距。
    boolean mON;//标记是否是开启状态。
    private Length thumbRegion = new Length();//滑块的移动范围。该值相对于父容器。

    public SwitchView(Context context) {
        super(context);
        int pad = SizeUT.getMMpixel(context) / 2;
        if (pad < 4) {
            pad = 4;
        }
        this.margin = pad / 2;
        int quarterWidth = pad / 4;
        int innerWidth = pad - quarterWidth * 3;
        int outColor = 0xff555555;
        int innerColor = 0xffcccccc;
        int outColorAfter = ColorUT.deSaturation(outColor, 0.2f, innerColor);
        int innerColorBefore = ColorUT.deSaturation(outColor, 0.6f, innerColor);
        bgPic = new ColorWithStrokesDrawable(ColorUT.WHITE,
                0.5f,
                outColor, quarterWidth,
                outColorAfter, quarterWidth,
                innerColorBefore, quarterWidth,
                innerColor, innerWidth);
        progressPic = new ProgressDrawable(ColorUT.GREEN);
        int thumbStrokeColor = 0xffaaaaaa;
        thumbPic = new ColorWithStrokesDrawable(ColorUT.WHITE,
                0.5f, thumbStrokeColor, margin);
    }

    //////////////////////////////////////////////////

    /**
     * 是否为开启状态。
     */
    public boolean isON() {
        return mON;
    }

    /**
     * {@link #setON(boolean, boolean)}
     */
    public void setON(boolean on) {
        setON(on, false);
    }

    /**
     * 设置开启状态。
     *
     * @param on   true：设置为开启状态。false：设置为关闭状态。
     * @param anim true：改变状态时执行动画。false：反之。
     */
    public void setON(boolean on, boolean anim) {
        if (on) {
            toON(false, anim);
        } else {
            toOFF(false, anim);
        }
    }

    Callback cb;//回调。

    /**
     * 设置回调。
     */
    public void setCallback(Callback cb) {
        this.cb = cb;
    }

    /**
     * 回调接口。
     */
    public interface Callback {
        /**
         * 状态改变为开启状态。
         *
         * @param fromTouch true：此次状态改变来自触摸。
         *                  false：来自方法设置。
         */
        void ON(boolean fromTouch);

        /**
         * 状态改变为关闭状态。
         *
         * @param fromTouch true：此次状态改变来自触摸。
         *                  false：来自方法设置。
         */
        void OFF(boolean fromTouch);

        /**
         * 点击触发的开启状态。
         * <p>
         * 如果返回true，则进入开启状态。否则状态不会改变。
         */
        boolean clickToON();

        /**
         * 点击触发的关闭状态。
         * <p>
         * 如果返回true，则进入关闭状态。否则状态不会改变。
         */
        boolean clickToOFF();
    }
    //////////////////////////////////////////////////

    /**
     * 将当前状态设置为开启状态。
     *
     * @param fromTouch true：此次操作来自触摸。
     * @param anim      是否需要动画执行此次状态改变。
     */
    private void toON(boolean fromTouch, boolean anim) {
        //当前为关闭状态，可以开启。
        if (!mON) {
            mON = true;
            //需要动画。
            if (anim) {
                animToProgress(1);
            }
            //不需要动画。
            else {
                setProgress(1);
            }
            cb.ON(fromTouch);
        }
        //当前为开启状态，不需要改变状态。
        else {
            //来此触摸操作，执行复位。
            if (fromTouch) {
                animToProgress(1);
            }
        }
    }

    /**
     * 将当前状态设置为关闭状态。
     *
     * @param fromTouch true：此次操作来自触摸。
     * @param anim      是否需要动画执行此次状态改变。
     */
    private void toOFF(boolean fromTouch, boolean anim) {
        //当前为开启状态，可以关闭。
        if (mON) {
            mON = false;
            //需要动画。
            if (anim) {
                animToProgress(0);
            }
            //不需要动画。
            else {
                setProgress(0);
            }
            cb.OFF(fromTouch);
        }
        //当前为关闭状态，无需改变状态。
        else {
            //此次来自触摸滑动，执行复位。
            if (fromTouch) {
                animToProgress(0);
            }
        }
    }

    private void animON() {
        mON = true;
        animToProgress(1);
    }

    private void animOFF() {
        mON = false;
        animToProgress(0);
    }

    //////////////////////////////////////////////////
    @Override
    protected void onUp(boolean click) {
        super.onUp(click);
        if (!click) {
            float p = getProgress();
            //关。
            if (p < 0.5f) {
                toOFF(true, true);
            }
            //开。
            else {
                toON(true, true);
            }
        }
    }

    //////////////////////////////////////////////////
    @Override
    protected void onClickProtected() {
        super.onClickProtected();
        //当前为开启状态，执行关闭操作。
        if (mON) {
            //有回调。
            if (cb != null) {
                //如果用户需要关，那么关闭。否则不关闭。
                if (cb.clickToOFF()) {
                    animOFF();
                }
            }
            //没设置回调，关闭。
            else {
                animOFF();
            }
        }
        //当前为关闭状态，执行开启操作。
        else {
            //有回调。
            if (cb != null) {
                //如果用户需要开启，那么开启。否则不开启。
                if (cb.clickToON()) {
                    animON();
                }
            }
            //没设置回调，开启。
            else {
                animON();
            }
        }
    }

    @Override
    protected Length onGetProgressLengthInParent(int w, int h) {
        int start = h / 2;
        int end = w - h / 2;
        thumbRegion.setLength(start, end);
        return thumbRegion;
    }

    @Override
    protected void onProgressChanged(float progress, int progressValueInRoot) {
        float progressInSelf = getProgressInSelf();
        progressPic.setProgress(progressInSelf);
        thumbView.setX(progressValueInRoot - getHotSpotInSelf(thumbView));
    }

    @Override
    protected void onSetBgViewPic(ImageView bgView) {
        bgView.setBackground(bgPic);
    }

    @Override
    protected void onSetProgressViewPic(ImageView progressView) {
//        pd = new ProgressDrawable(0, ColorUT.GREEN);
        progressView.setBackground(progressPic);
    }

    @Override
    protected void onSetThumbViewPic(ImageView thumbView) {
        thumbView.setBackground(thumbPic);
    }

    @Override
    protected void onAdjustBgView(ImageView bgView, int w, int h) {
        int margin = 1;
        bgView.setLayoutParams(LayoutParamsUT.frameParamsMatchParent(
                margin, margin, margin, margin
        ));
    }

    @Override
    protected void onAdjustProgressView(ImageView progressView, int w, int h) {
        progressView.setLayoutParams(LayoutParamsUT.frameParamsMatchParent(
                margin, margin, margin, margin
        ));
    }

    @Override
    protected void onAdjustThumbView(ImageView thumbView, int w, int h) {
        ViewUT.adjustSize(thumbView, h, h);
    }
}

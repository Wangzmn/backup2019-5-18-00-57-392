package wclass.android.ui.view.progressbar.seekbarNew;

import android.graphics.Rect;

import wclass.android.util.SizeUT;


/**
 * @作者 做就行了！
 * @时间 2019-03-25下午 4:51
 * @该类描述： -
 * 1、轮廓生成器。
 * 根据参数内容生成轮廓。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class FrameMakerImpl implements SeekBar.FrameMaker {

    /**
     * 滑道的长度方向，两端是否有内边距。
     */
    private boolean lengthEndHasPadding;

    /**
     * 滑道的宽度方向，track的最大宽度。
     */
    private int maxTrackWidth;

    /**
     * {@link #FrameMakerImpl(boolean, int)}
     */
    public FrameMakerImpl() {
        this(true, 0);
    }

    /**
     * {@link #FrameMakerImpl(boolean, int)}
     */
    public FrameMakerImpl(int maxTrackWidth) {
        this(true, maxTrackWidth);
    }

    /**
     * {@link #FrameMakerImpl(boolean, int)}
     */
    public FrameMakerImpl(boolean lengthEndHasPadding) {
        this(lengthEndHasPadding, 0);
    }

    /**
     * 构造方法。
     *
     * @param lengthEndHasPadding 长度方向，两端是否有内边距。
     * @param maxTrackWidth       宽度方向，track的最大宽度。
     */
    public FrameMakerImpl(boolean lengthEndHasPadding, int maxTrackWidth) {
        if (maxTrackWidth < 1) {
            maxTrackWidth = -1;
        }
        this.lengthEndHasPadding = lengthEndHasPadding;
        this.maxTrackWidth = maxTrackWidth;
    }

    @Override
    public void make(Rect pending, ThumbInfo info, SeekBar seekBar, Rect usableRect) {
        int left, right, top, bottom;

        int trackWidth;
        int halfTrackWidth;

        //用户设置了。
        if (maxTrackWidth != -1) {
            trackWidth = maxTrackWidth;
            halfTrackWidth = trackWidth / 2;
        }
        //用户未设置trackWidth，使用默认的trackWidth。
        else {
            halfTrackWidth = SizeUT.getMMpixel(seekBar.getContext());
            //宽度设置为两毫米。
            trackWidth = halfTrackWidth * 2;
        }
        switch (seekBar.getOrien()) {
            case HORIZONTAL:
                //长度两端有内边距。
                if (lengthEndHasPadding) {
                    left = info.coreMinX;
                    right = info.coreMinX + info.maxDragDist;
                }
                //长度两端无内边距。
                else {
                    left = usableRect.left;
                    right = usableRect.right;
                }
                //高最大为2毫米。
                top = info.coreMinY - halfTrackWidth;
                bottom = top + trackWidth;

                if (top < 0) {
                    top = 0;
                }
                if (bottom > usableRect.bottom) {
                    bottom = usableRect.bottom;
                }
                break;
            case VERTICAL:
                //长度两端有内边距。
                if (lengthEndHasPadding) {
                    top = info.coreMinY;
                    bottom = info.coreMinX + info.maxDragDist;
                }
                //长度两端无内边距。
                else {
                    top = usableRect.top;
                    bottom = usableRect.bottom;
                }
                //宽最大为2毫米。
                left = info.coreMinX - halfTrackWidth;
                right = left + trackWidth;
                if (left < 0) {
                    left = 0;
                }
                if (right > usableRect.right) {
                    right = usableRect.right;
                }
                break;
            default:
                throw new IllegalStateException();
        }

        pending.left = left;
        pending.right = right;
        pending.top = top;
        pending.bottom = bottom;
    }
}

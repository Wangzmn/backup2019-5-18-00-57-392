package wclass.android.ui.view.progressbar.seekbarNew;

import android.content.Context;
import android.graphics.Rect;

import wclass.android.util.SizeUT;


/**
 * @作者 做就行了！
 * @时间 2019-03-24下午 11:56
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class ThumbInfoMakerImpl implements SeekBar.ThumbInfoMaker {

    @Override
    public ThumbInfo make(SeekBar seekBar, Rect usableRect) {
        int seekBarWidth = usableRect.width();
        int seekBarHeight = usableRect.height();

        int minSide = Math.min(seekBarWidth, seekBarHeight);
        Context context = seekBar.getContext();
        int thumbMaxSize = SizeUT.getCMpixel(context) / 2;
        int thumbSize = Math.min(minSide, thumbMaxSize);
        int maxDragDist;
        int minY;
        int minX;
        switch (seekBar.getOrien()) {
            case HORIZONTAL:
                maxDragDist = seekBarWidth - thumbSize;
                minX = usableRect.left;
                minY = (seekBarHeight - thumbSize) / 2;
                break;
            case VERTICAL:
                maxDragDist = seekBarHeight - thumbSize;
                minY = usableRect.top;
                minX = (seekBarWidth - thumbSize) / 2;
                break;
            default:
                throw new IllegalStateException();
        }
        int coreMinX = minX + thumbSize / 2;
        int coreMinY = minY + thumbSize / 2;
        return new ThumbInfo(thumbSize, thumbSize,
                minX, minY,
                coreMinX, coreMinY,
                maxDragDist);
    }
}

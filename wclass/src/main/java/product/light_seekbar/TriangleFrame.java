package product.light_seekbar;


import android.graphics.Rect;

import wclass.android.ui.view.progressbar.seekbarNew.SeekBar;
import wclass.android.ui.view.progressbar.seekbarNew.ThumbInfo;
import wclass.android.util.SizeUT;
import wclass.util.MathUT;

/**
 * @作者 做就行了！
 * @时间 2019-03-27下午 2:10
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class TriangleFrame implements SeekBar.ThumbInfoMaker {

    @Override
    public ThumbInfo make(SeekBar seekBar, Rect usableRect) {
        /**
         * 指针核心位置在最中间。
         */
        int seekBarWidth = usableRect.width();
        int seekBarHeight = usableRect.height();

        int cm = SizeUT.getCMpixel(seekBar.getContext());
        //一半父容器的宽。
        int halfRootWidth = seekBarWidth / 2;
        int width = halfRootWidth;
        width = MathUT.limitMax(width, (int) (cm / 3f * 2));
        //核心位置在中间。
        int minX = usableRect.left + halfRootWidth - width;
        int minY = usableRect.top;

        int dragDist = seekBarHeight - width;
        int coreX = minX + width;
        //舍去小数后，位置偏上。
        int coreY = usableRect.top + width / 2;
        return new ThumbInfo(
                width, width, minX, minY, coreX, coreY, dragDist);

    }
}

package product.light_seekbar;

import android.graphics.Rect;

import wclass.android.ui.view.progressbar.seekbarNew.SeekBar;
import wclass.android.ui.view.progressbar.seekbarNew.ThumbInfo;
import wclass.android.util.SizeUT;
import wclass.util.MathUT;


/**
 * @作者 做就行了！
 * @时间 2019-03-27下午 2:29
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class BgFrame implements SeekBar.FrameMaker {
    @Override
    public void make(Rect pending, ThumbInfo info, SeekBar seekBar,
                     Rect usableRect) {
        int left, right, top, bottom;
        int rootWidth = usableRect.width();
        int width = (int) (rootWidth / 3f * 2);
        int cm = SizeUT.getCMpixel(seekBar.getContext());
        width = MathUT.limitMax(width, cm);

        left = usableRect.left+(rootWidth-width)/2;
        right = left+width;
        top = info.coreMinY;
        bottom = info.coreMinY + info.maxDragDist;
        pending.left = left;
        pending.right = right;
        pending.top = top;
        pending.bottom = bottom;
    }
}

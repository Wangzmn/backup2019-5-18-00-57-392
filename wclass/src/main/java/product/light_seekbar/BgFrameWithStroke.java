package product.light_seekbar;

import android.graphics.Rect;

import wclass.android.ui.view.progressbar.seekbarNew.SeekBar;
import wclass.android.ui.view.progressbar.seekbarNew.ThumbInfo;


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
public class BgFrameWithStroke extends BgFrame {
    @Override
    public void make(Rect pending, ThumbInfo info, SeekBar seekBar, Rect usableRect) {
        super.make(pending, info, seekBar, usableRect);
        pending.bottom += 2;
        pending.top -= 2;
    }
}

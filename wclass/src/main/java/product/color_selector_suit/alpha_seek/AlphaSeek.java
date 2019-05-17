package product.color_selector_suit.alpha_seek;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;

import wclass.android.ui.view.progressbar.seekbarNew.SeekBar;
import wclass.android.ui.view.progressbar.seekbarNew.ThumbInfo;
import wclass.android.ui.drawable.useful.ShadowDrawable;
import wclass.android.ui.drawable.useful.NaturalDrawable;
import wclass.util.ColorUT;

/**
 * @作者 做就行了！
 * @时间 2019-03-31下午 3:38
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class AlphaSeek extends SeekBar {
    public AlphaSeek(Context context) {
        super(context,255);
        setTrackRegionFrameMaker(new FrameMaker() {
            @Override
            public void make(Rect pending, ThumbInfo info, SeekBar seekBar, Rect usableRect) {
                pending.set(usableRect);
            }
        });
        setDrawableThumb(new ShadowDrawable(ColorUT.WHITE));
        setDrawableTrackBg(new ColorDrawable(0xffaaaaaa));
        setDrawableProgress(new NaturalDrawable(0xff898ef5
        ,0.5f));

    }
}

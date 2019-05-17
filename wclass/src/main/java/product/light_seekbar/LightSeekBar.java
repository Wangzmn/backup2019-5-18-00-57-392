package product.light_seekbar;

import android.content.Context;

import wclass.android.ui.view.progressbar.seekbarNew.RightTriangle;
import wclass.android.ui.view.progressbar.seekbarNew.SeekBar;
import wclass.android.ui.drawable.ready.White2BlackDrawableNew;
import wclass.enums.Orien2;
import wclass.util.ColorUT;

/**
 * @作者 做就行了！
 * @时间 2019-03-27下午 2:46
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class LightSeekBar extends SeekBar {

    public LightSeekBar(Context context) {
        super(context, 255);
        setThumbInfoMaker(new TriangleFrame());
        BgFrameWithStroke withStroke = new BgFrameWithStroke();
        setTrackBgFrameMaker(withStroke);
        BgFrame frame = new BgFrame();
        setTrackRegionFrameMaker(frame);
        setTrackMaxProgressFrameMaker(frame);

        setDrawableThumb(new RightTriangle(ColorUT.RED));
        setDrawableTrackBg(new White2BlackDrawableNew());
        setOrien(Orien2.VERTICAL);
        setReverseProgress(true);
    }
}

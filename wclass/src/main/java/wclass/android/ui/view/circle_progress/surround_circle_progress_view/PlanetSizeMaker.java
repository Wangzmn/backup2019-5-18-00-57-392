package wclass.android.ui.view.circle_progress.surround_circle_progress_view;

import wclass.android.util.SizeUT;
import wclass.common.SizeMaker;

/**
 * @作者 做就行了！
 * @时间 2019/4/5 0005
 * @使用说明：
 */
public class PlanetSizeMaker implements SizeMaker<SurroundCPView> {
    @Override
    public void make(int[] pendingWH, SurroundCPView surroundCPView) {
        int mm = SizeUT.getMMpixel(surroundCPView.getContext());
        int wh = mm * 6;
        pendingWH[0] = wh;
        pendingWH[1] = wh;
    }
}

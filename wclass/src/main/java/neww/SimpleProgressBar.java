package neww;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

/**
 * @作者 做就行了！
 * @时间 2019-05-14下午 11:51
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public abstract class SimpleProgressBar extends ProgressBar<ImageView, ImageView, ImageView> {

    public SimpleProgressBar(Context context) {
        super(context);
    }

    protected abstract void onSetBgViewPic(ImageView bgView);

    protected abstract void onSetProgressViewPic(ImageView progressView);

    protected abstract void onSetThumbViewPic(ImageView thumbView);

    //--------------------------------------------------
    @Override
    protected ImageView onCreateBgView(Context context) {
        return new ImageView(context);
    }

    @Override
    protected ImageView onCreateProgressView(Context context) {
        return new ImageView(context);
    }

    @Override
    protected ImageView onCreateThumbView(Context context) {
        return new ImageView(context);
    }

    @Override
    protected void onCreateViewsFinish() {
        onSetBgViewPic(bgView);
        onSetProgressViewPic(progressView);
        onSetThumbViewPic(thumbView);
    }

    /**
     * 获取thumbView核心点 相对于自身left的距离。
     *
     * @return thumbView核心点 相对于自身left的距离。
     */
    protected int getHotSpotInSelf(View thumbView) {
        return thumbView.getWidth() / 2;
    }

    /**
     * 获取progressView中的进度。
     *
     * @return progressView中的进度。
     */
    protected float getProgressInSelf() {
        float progressValue = getProgressValueInParent() - progressView.getLeft();
        return progressValue / progressView.getWidth();
    }
}

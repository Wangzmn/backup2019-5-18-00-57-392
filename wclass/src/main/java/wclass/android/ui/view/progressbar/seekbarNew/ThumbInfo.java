package wclass.android.ui.view.progressbar.seekbarNew;

/**
 * @作者 做就行了！
 * @时间 2019-03-25下午 4:35
 * @该类描述： -
 * 1、拇指滑块的信息类。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("WeakerAccess")
public class ThumbInfo {
    public int width;//滑块宽。
    public int height;//滑块高。
    public int minX;//滑块最小X坐标。
    public int minY;//滑块最小Y坐标。
    public int coreMinX;//滑块核心位置最小X坐标。
    public int coreMinY;//滑块核心位置最小Y坐标。
    public int maxDragDist;//滑块最长滑动距离。

    public ThumbInfo(int thumbWidth, int thumbHeight,
                     int minX, int minY,
                     int coreMinX, int coreMinY,
                     int maxDragDist) {
        this.width = thumbWidth;
        this.height = thumbHeight;
        this.minX = minX;
        this.minY = minY;
        this.coreMinX = coreMinX;
        this.coreMinY = coreMinY;
        this.maxDragDist = maxDragDist;
    }

    @Override
    public String toString() {
        String s ="[ThumbInfo：" + " width = " + width + "\n" +
                " height = " + height + "\n" +
                " minX = " + minX + "\n" +
                " minY = " + minY + "\n" +
                " coreMinX = " + coreMinX + "\n" +
                " coreMinY = " + coreMinY + "\n" +
                " maxDragDist = " + maxDragDist+" 。ThumbInfo]";
        return s;
    }
}

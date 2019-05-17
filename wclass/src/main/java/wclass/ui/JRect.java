package wclass.ui;

/**
 * @作者 Administrator
 * @时间 2018/12/16 0016 下午 4:08
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 */
@SuppressWarnings("unused")
public class JRect {
    public int left;
    public int right;
    public int top;
    public int bottom;

    public JRect() {

    }

    public JRect(int left, int top, int right, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    /**
     * 检查于 2019年1月7日23:05:06
     * <p>
     * 自身是否与指定的rect相交。
     *
     * @param rect 指定的rect。
     * @return true：自身与指定的rect相交。
     */
    public boolean intersects(JRect rect) {
        return left < rect.right
                && right > rect.left
                && top < rect.bottom
                && bottom > rect.top;
    }

    public boolean contains(JRect rect) {
        return left >= rect.left
                && right <= rect.right
                && top >= rect.top
                && bottom <= rect.bottom;
    }
}
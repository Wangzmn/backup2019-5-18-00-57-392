package wclass.common;

/**
 * @作者 做就行了！
 * @时间 2019-04-21下午 11:38
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("WeakerAccess")
public class XY {
    public float x;
    public  float y;

    public XY() {
    }

    public XY(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void set(XY xy) {
        x = xy.x;
        y = xy.y;
    }
}

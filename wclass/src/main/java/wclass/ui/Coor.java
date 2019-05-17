package wclass.ui;

/**
 * @作者 做就行了！
 * @时间 2019-03-09下午 3:13
 * @该类描述： -
 * 1、坐标类。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("WeakerAccess")
public class Coor implements Cloneable {
    public float x;
    public float y;

    public Coor(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Coor() {}

    @Override
    public Coor clone() throws CloneNotSupportedException {
        return (Coor) super.clone();
    }

    /**
     * 克隆坐标数组。
     *
     * @param coors 坐标数组。
     * @return 克隆出的坐标数组。
     */
    public static Coor[] clone(Coor[] coors) {
        try {
            int length = coors.length;
            Coor[] coors2 = new Coor[length];
            for (int i = 0; i < length; i++) {
                Coor coor = coors[i];
                if (coor != null) {
                    coors2[i] = coor.clone();
                }
            }
            return coors2;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    /**
     * 运行于 2019年3月7日23:52:32
     */
    @Override
    public String toString() {
        String s = "[x = " + x +
                ", y = " + y +
                ", 斜边 = " + Math.hypot(x, y) +
                ", 角度 = " + Math.toDegrees(Math.atan2(y, x)) +
                "。";
        return s;
    }
}

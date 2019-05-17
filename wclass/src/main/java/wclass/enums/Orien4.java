package wclass.enums;

/**
 * @作者 做就行了！
 * @时间 2019-03-17下午 4:28
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public enum Orien4 {
    LEFT,
    TOP,
    RIGHT,
    BOTTOM;

    public static Orien4 fromInt(int i) {
        switch (i) {
            default:
                System.err.printf(Orien4.class.getCanonicalName()
                        + "#fromInt()，" +
                        "请求的参数为：" + i + " 。" +
                        "无法匹配switch选项，" +
                        "已使用默认方案：int值为1转为LEFT。");
            case 1:
                return LEFT;
            case 2:
                return TOP;
            case 3:
                return RIGHT;
            case 4:
                return BOTTOM;
        }

    }

    public int toInt() {
        switch (this) {
            case LEFT:
                return 1;
            case TOP:
                return 2;
            case RIGHT:
                return 3;
            case BOTTOM:
                return 4;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case LEFT:
                return "LEFT";
            case TOP:
                return "TOP";
            case RIGHT:
                return "RIGHT";
            case BOTTOM:
                return "BOTTOM";
            default:
                throw new IllegalStateException();
        }
    }
}

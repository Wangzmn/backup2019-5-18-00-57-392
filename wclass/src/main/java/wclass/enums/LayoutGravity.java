package wclass.enums;

/**
 * @作者 做就行了！
 * @时间 2019-02-19上午 12:51
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("unused")
public enum LayoutGravity {
    LEFT_TOP,
    LEFT_BOTTOM,
    RIGHT_TOP,
    RIGHT_BOTTOM;

    public static LayoutGravity fromInt(int i) {
        switch (i) {
            default:
                System.err.printf(LayoutGravity.class.getCanonicalName()
                        + "#fromInt()，请求的参数为：" + i + " 。" +
                        "无法匹配switch选项，" +
                        "已使用默认方案：int值1转为LEFT_TOP。");
            case 1:
                return LEFT_TOP;
            case 2:
                return LEFT_BOTTOM;
            case 3:
                return RIGHT_TOP;
            case 4:
                return RIGHT_BOTTOM;
        }

    }

    public int toInt() {
        switch (this) {
            case LEFT_TOP:
                return 1;
            case LEFT_BOTTOM:
                return 2;
            case RIGHT_TOP:
                return 3;
            case RIGHT_BOTTOM:
                return 4;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case LEFT_TOP:
                return "LEFT_TOP";
            case LEFT_BOTTOM:
                return "LEFT_BOTTOM";
            case RIGHT_TOP:
                return "RIGHT_TOP";
            case RIGHT_BOTTOM:
                return "RIGHT_BOTTOM";
            default:
                throw new IllegalStateException();
        }
    }
}

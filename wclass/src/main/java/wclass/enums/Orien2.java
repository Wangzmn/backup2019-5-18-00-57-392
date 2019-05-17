package wclass.enums;

/**
 * @作者 做就行了！
 * @时间 2018-12-27下午 6:25
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 */
public enum Orien2 {
    HORIZONTAL,//横向
    VERTICAL;//纵向

    public static Orien2 fromInt(int i) {
        switch (i) {
            default:
                System.err.printf(Orien2.class.getCanonicalName()
                        + "#fromInt()，请求的参数为：" + i + " 。" +
                        "无法匹配switch选项，" +
                        "已使用默认方案：int值1转为HORIZONTAL。");
            case 1:
                return HORIZONTAL;
            case 2:
                return VERTICAL;
        }
    }

    public int toInt() {
        switch (this) {
            case HORIZONTAL:
                return 1;
            case VERTICAL:
                return 2;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case HORIZONTAL:
                return "HORIZONTAL";
            case VERTICAL:
                return "VERTICAL";
            default:
                throw new IllegalStateException();
        }
    }
}
package wclass.enums;

/**
 * @作者 做就行了！
 * @时间 2019-04-16下午 1:22
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public enum ScrollState {
    IDLE,
    TOUCH_SCROLL,
    CODE_SCROLL;

    public static ScrollState fromRecyclerView(int scrollState) {
        switch (scrollState) {
            default:
                System.err.println("请求的scrollState值为："+scrollState+", 使用默认值0状态为IDLE。");
            case 0:
                return IDLE;
            case 1:
                return TOUCH_SCROLL;
            case 2:
                return CODE_SCROLL;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case IDLE:
                return "IDLE";
            case TOUCH_SCROLL:
                return "TOUCH_SCROLL";
            case CODE_SCROLL:
                return "CODE_SCROLL";
            default:
                throw new IllegalStateException();
        }
    }
}

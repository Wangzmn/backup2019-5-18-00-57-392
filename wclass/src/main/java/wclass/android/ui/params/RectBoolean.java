package wclass.android.ui.params;

/**
 * @作者 做就行了！
 * @时间 2019-04-19下午 4:32
 * @该类描述： -
 * 1、标识矩形的四个角。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("WeakerAccess")
public class RectBoolean {
    public static final RectBoolean TRUE =
            new RectBoolean(true, true, true, true);
    public static final RectBoolean FALSE =
            new RectBoolean(false, false, false, false);
    public static final RectBoolean LT =
            new RectBoolean(true, false, false, false);
    public static final RectBoolean LB =
            new RectBoolean(false, false, false, true);
    public static final RectBoolean RT =
            new RectBoolean(false, true, false, false);
    public static final RectBoolean RB =
            new RectBoolean(false, false, true, false);
    public static final RectBoolean L =
            new RectBoolean(true, false, false, true);
    public static final RectBoolean R =
            new RectBoolean(false, true, true, false);
    //////////////////////////////////////////////////
    public final boolean needLT;
    public final boolean needRT;
    public final boolean needRB;
    public final boolean needLB;

    public RectBoolean(boolean LT, boolean RT, boolean RB, boolean LB) {
        needLT = LT;
        needRT = RT;
        needRB = RB;
        needLB = LB;
    }
}

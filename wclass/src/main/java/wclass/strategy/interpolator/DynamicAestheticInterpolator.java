package wclass.strategy.interpolator;

/**
 * @作者 做就行了！
 * @时间 2019-01-09下午 4:21
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * 1、
 * 将总进度分成section份数。
 * 0 至 1/section：加速增加。
 * 1/section 至 1：减速增加。
 *
 * todo
 * 1、用户设置指定n/section 加速、减速。
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class DynamicAestheticInterpolator implements JInterpolator {
    private static final double E_RECIPROCAL = (float) (1 / Math.E);
    //----------------------------------------------------------------------
    private final double section;//份数
    private final double sectionReciprocal;//份数的倒数
    private final float sleekScale;//圆滑放大的比例。
    private final float complement;//用该数补足至1。
    //////////////////////////////////////////////////////////////////////

    /**
     * {@link #DynamicAestheticInterpolator(double)}
     */
    public DynamicAestheticInterpolator() {
        this(4);
    }

    /**
     * 构造方法。
     * <p>
     * 将总进度分成section份数。
     * 0 至 1/section：加速增加。
     * 1/section 至 1：减速增加。
     *
     * @param section 将总进度分成section份数。
     */
    public DynamicAestheticInterpolator(double section) {
        if (section <= 1) {
            throw new IllegalArgumentException("section只能大于1。" +
                    "请求的section为：" + section + " 。");
        }
        this.section = section;
        sectionReciprocal = 1/section;
        float maxProgress = getAestheticProgress(1);
        sleekScale = 1 / maxProgress;
        complement = 1 - maxProgress * sleekScale;
    }

    @Override
    public float getInterpolation(float progress) {
        if (progress < sectionReciprocal) {
            return getAestheticProgress(progress);
        }
        progress = getAestheticProgress(progress);
        return progress * sleekScale + complement;
    }
    //////////////////////////////////////////////////////////////////////
    /*step 主逻辑*/

    private float getAestheticProgress(float progress) {
        double currSection = progress * section;
        if (currSection < 1) {
            return (float) (currSection - (1 - Math.exp(-currSection)));
        } else {
            double per = (1 - Math.exp(1 - currSection));
            return (float) (E_RECIPROCAL + per * (1 - E_RECIPROCAL));
        }
    }
}
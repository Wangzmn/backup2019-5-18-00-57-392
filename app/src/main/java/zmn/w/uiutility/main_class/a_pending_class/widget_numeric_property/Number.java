package zmn.w.uiutility.main_class.a_pending_class.widget_numeric_property;

/**
 * @作者 做就行了！
 * @时间 2019-03-23下午 9:23
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Number {
    /**
     * 绝对数值。
     * 友情提示：该值可以为负数。
     */
    private float value;//绝对数值。
    private float per;//百分比数值。
    private Relative perRelative;//per相对于谁。
    //////////////////////////////////////////////////
    /*domain 构造方法。*/

    /**
     * 仅设置绝对数值。
     * 友情提示：该值可以为负数。
     *
     * @param value 绝对数值。
     */
    public Number(float value) {
        this.value = value;
    }

    /**
     * 仅设置百分比数值。
     *
     * @param per         百分比数值。
     * @param perRelative per相对于谁。
     */
    public Number(float per, Relative perRelative) {
        this.per = per;
        this.perRelative = perRelative;
    }

    /**
     * 组合设置数值。绝对数值+百分比数值。
     *
     * @param value       绝对数值。
     * @param per         百分比数值。
     * @param perRelative per相对于谁。
     */
    public Number(float value, float per, Relative perRelative) {
        this.value = value;
        this.per = per;
        this.perRelative = perRelative;
    }
    //////////////////////////////////////////////////

//    public float get(int parentWidth,int parentHeight) {
//
//    }
    /**
     * 获取数值。
     * <p>
     * 警告：使用该方法前，必须调用{@link #setParent}方法设置父容器信息。
     *
     * @return 具体数值。
     */
    public float get(int parentWidth,int parentHeight) {
        try {
            if (perRelative != null) {
                switch (perRelative) {
                    case PARENT_WIDTH:
                        return value + per * parentWidth;
                    case PARENT_HEIGHT:
                        return value + per * parentWidth;
                    default:
                        throw new IllegalStateException();
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new NullPointerException("未调用setParentInfo()，请先调用。");
        }
        return value;
    }

    private ParentInfo parentInfo;

    /**
     * 设置该类相对于谁。
     * <p>
     * 警告：哪个类引用{@link Number},哪个类必须调用该方法。
     *
     * @param parentInfo 父容器信息类。
     */
    public void setParent(ParentInfo parentInfo) {
        this.parentInfo = parentInfo;
    }

}

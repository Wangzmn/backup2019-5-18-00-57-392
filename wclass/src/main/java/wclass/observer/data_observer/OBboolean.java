package wclass.observer.data_observer;

/**
 * @作者 做就行了！
 * @时间 2019-03-24下午 12:51
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
public class OBboolean extends OBobject<Boolean> {
    public OBboolean(Boolean value) {
        super(value);
        check(value);
    }

    @Override
    public void set(Boolean neww) {
        check(neww);
        if (neww.booleanValue() != value.booleanValue()) {
            Boolean old = value;
            value = neww;
            invalidate(value, old);
            notifyDataChange(value, old);
        }
    }

    private void check(Boolean value) {
        if (value == null) {
            throw new NullPointerException("value不能为null。");
        }
    }
}

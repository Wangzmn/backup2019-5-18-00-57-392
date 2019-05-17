package wclass.observer.data_observer;

/**
 * @作者 做就行了！
 * @时间 2019-03-24下午 12:35
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class OBobject<T> extends DataObservable<T> {
    protected T value;

    public OBobject(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T neww) {
        if (neww != value) {
            T old = value;
            value = neww;
            invalidate(value, old);
            notifyDataChange(value, old);
        }
    }

    @SuppressWarnings("WeakerAccess")
    protected void invalidate(T neww, T old) {

    }
}

package wclass.observer.data_observer;

/**
 * @作者 做就行了！
 * @时间 2019-03-24下午 12:24
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public interface DataObserver<T> {
    void onDataChange(T neww, T old);
}

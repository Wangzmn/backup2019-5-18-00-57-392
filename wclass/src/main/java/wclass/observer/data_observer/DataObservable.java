package wclass.observer.data_observer;

import wclass.observer.Observable;

/**
 * @作者 做就行了！
 * @时间 2019-03-24下午 12:28
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
public class DataObservable<T> extends Observable<DataObserver<T>> {

    protected void notifyDataChange(T neww, T old) {
        for (DataObserver<T> observer : getObservers()) {
            observer.onDataChange(neww, old);
        }
    }

}

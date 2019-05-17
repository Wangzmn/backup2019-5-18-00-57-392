package wclass.observer.message_observer;

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
public class Notifier extends Observable<Receiver>{

    public void dispatchMessage() {
        for (Receiver observer : getObservers() ) {
            observer.onReceive();
        }
    }

}

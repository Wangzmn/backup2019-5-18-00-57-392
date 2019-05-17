package wclass.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者 做就行了！
 * @时间 2019-03-24下午 2:22
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */

/**
 * @param <T> 观察者。
 */
@SuppressWarnings("ALL")
public class Observable<T> {
    /**
     * 观察者们 ~
     */
    private List<T> observers;

    //////////////////////////////////////////////////
    protected List<T> getObservers() {
        preUseList();
        return observers;
    }

    public int getObserverCount() {
        preUseList();
        return observers.size();
    }

    //--------------------------------------------------
    public void addObserver(T observer) {
        preUseList();
        if (observers.contains(observer)) {
            return;
        }
        observers.add(observer);
    }

    public boolean removeObserver(T observer) {
        preUseList();
        return observers.remove(observer);
    }

    public boolean containsObserver(T observer) {
        preUseList();
        return observers.contains(observer);
    }

    public void clearObservers() {
        preUseList();
        observers.clear();
    }
    //////////////////////////////////////////////////

    @SuppressWarnings("unchecked")
    private void preUseList() {
        if (observers == null) {
            observers = onCreateObserverList();
        }
    }
    //////////////////////////////////////////////////

    protected List onCreateObserverList() {
        return new ArrayList<>();
    }

}

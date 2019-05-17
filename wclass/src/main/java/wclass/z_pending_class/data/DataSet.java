package wclass.z_pending_class.data;

import java.util.TreeSet;

/**
 *
 * @作者 做就行了！
 * @时间 2018-11-24下午 3:57
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public interface DataSet<T> {

    /**
     * 获取头部数据。
     */
    int from();

    /**
     * 获取尾部数据。
     */
    int to();

    /**
     * 是否包含指定位置的数据。
     * <p>
     * 友情提示：与{@link #getDirectly(int)}配合使用。
     *
     * @param position 数据的位置
     * @return true：包含指定位置的数据
     */
    boolean contains(int position);

    /**
     * 获取指定位置的数据。
     * <p>
     * 友情提示：
     * 1、每次获取都会检查position，所以效率稍差。
     *
     * @param position 数据的位置
     * @return 指定位置的数据
     */
    T get(int position);

    /**
     * 获取指定位置的数据。
     * <p>
     * 友情提示：
     * 1、每次获取不会检查position，这是不安全的获取方式。
     * 2、配合{@link #contains(int)}使用，以确定是否为有效的position。
     * @param position 数据的位置
     * @return 指定位置的数据
     */
    T getDirectly(int position);

    /**
     * 遍历全部数据，通过回调通知用户。
     *
     * @param diListener 数据信息回调接口
     */
    void travel(DataInfoListener<T> diListener);

    /**
     * 遍历指定范围的数据，通过回调通知用户。
     *
     * @param fromPosition 数据范围的起始位置
     * @param toPosition   数据范围的终点位置
     * @param diListener   数据信息回调接口
     */
    void travel(int fromPosition, int toPosition, DataInfoListener<T> diListener);
    //----------------------------------------------------------------------

    /**
     * 交换两个数据的位置。
     *
     * @param position1   数据1的位置
     * @param position2   数据2的位置
     * @param ddcListener 数据下标改变的回调
     */
    void swap(int position1, int position2, DataDexChangeListener<T> ddcListener);

    /**
     * 将内部数据A 插入至数据B位置，他们之间的数据下标会发生位移。
     *
     * @param innerPosition 内部数据A的位置
     * @param toPosition    数据B的位置
     * @param ddcListener   数据位置改变的回调
     */
    void insertInner(int innerPosition, int toPosition, DataDexChangeListener<T> ddcListener);

    /**
     * 将 外部数据 插入至 数据A的位置，A之前的数据会往之前移动一格。
     *
     * @param toPosition  数据A的位置
     * @param data        外部数据
     * @param ddcListener 数据位置改变的回调
     */
    void insertOuterIndentBefore(int toPosition, T data, DataDexChangeListener<T> ddcListener);

    /**
     * 将 外部数据 插入至 数据A的位置，A之后的数据会往之后移动一格。
     *
     * @param toPosition  数据A的位置
     * @param data        外部数据
     * @param ddcListener 数据位置改变的回调
     */
    void insertOuterIndentAfter(int toPosition, T data, DataDexChangeListener<T> ddcListener);
    //----------------------------------------------------------------------

    /**
     * 删除 指定位置 的数据。
     *
     * @param position 被删除的数据 的位置。
     * @return 被删除的数据
     */
    T remove(int position);

    /**
     * 删除 指定位置 的数据。
     *
     * @param position    被删除的数据 的位置。
     * @param ddcListener 数据位置改变的回调
     * @return 被删除的数据
     */
    T remove(int position, DataDexChangeListener<T> ddcListener);


    /**
     * 删除 指定位置 的数据。
     *
     * @param positions 被删除的数据 的位置。
     */
    void remove(TreeSet<Integer> positions);

    /**
     * 删除 指定位置 的数据。
     *
     * @param positions  被删除的数据 的位置。
     * @param drListener 数据被删除的回调
     */
    void remove(TreeSet<Integer> positions, DataRemovedListener<T> drListener);

    /**
     * 删除 指定位置 的数据。
     *
     * @param positions   被删除的数据 的位置。
     * @param ddcListener 数据位置改变的回调
     */
    void remove(TreeSet<Integer> positions, DataDexChangeListener<T> ddcListener);

    /**
     * 删除 指定位置 的数据。
     *
     * @param positions   被删除的数据 的位置。
     * @param drListener  数据被删除的回调
     * @param ddcListener 数据位置改变的回调
     */
    void remove(TreeSet<Integer> positions, DataRemovedListener<T> drListener, DataDexChangeListener<T> ddcListener);

    /**
     * 删除全部数据。
     */
    void removeAll();

    /**
     * 删除全部数据。
     *
     * @param drListener 数据被删除的回调
     */
    void removeAll(DataRemovedListener<T> drListener);
    //----------------------------------------------------------------------

    /**
     * 所有数据往之前移动。
     *
     * @param delta 数据移动的距离
     */
    void migrateToBefore(int delta);

    /**
     * 所有数据往之前移动。
     *
     * @param delta       数据移动的距离
     * @param ddcListener 数据下标改变时 的回调
     */
    void migrateToBefore(int delta, DataDexChangeListener<T> ddcListener);

    /**
     * 所有数据往之后移动。
     *
     * @param delta 数据移动的距离
     */
    void migrateToAfter(int delta);

    /**
     * 所有数据往之后移动。
     *
     * @param delta       数据移动的距离
     * @param ddcListener 数据下标改变时 的回调
     */
    void migrateToAfter(int delta, DataDexChangeListener<T> ddcListener);

}

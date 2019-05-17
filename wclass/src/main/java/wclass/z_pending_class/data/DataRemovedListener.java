package wclass.z_pending_class.data;

/**
 * @作者 做就行了！
 * @时间 2018-11-05下午 2:47
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 */
public interface DataRemovedListener<V> {
    void onDataRemoved(int position, V val);
}

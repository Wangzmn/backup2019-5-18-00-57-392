package wclass.z_pending_class.data;

import wclass.y_marks.Ugly_ReturnValue;

/**
 * @作者 做就行了！
 * @时间 2018-11-28下午 11:13
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 */
public interface DataInfoListener<V> {

    /**！
     * 通过{@link DataSet#travel}遍历、通知数据的信息。
     * 当返回为false时，不需要继续接收下一个数据的信息回调。
     *
     * @param position 数据所在下标
     * @param val      数据
     * @param number   排在一群数据中的第几位。（从0开始计数）
     *                 number=-1时，该数据为发起者。
     * @return true：需要继续接收下一个数据的信息回调。
     * false：不需要继续接收下一个数据的信息回调。
     */
    @Ugly_ReturnValue
    boolean onDataInfo(int position, V val, int number);
}

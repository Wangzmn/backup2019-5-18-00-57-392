package wclass.z_pending_class.data;

/**
 * @作者 做就行了！
 * @时间 2018/11/22 0022
 * @使用说明：
 */
public interface DataDexChangeListener<V> {
    /**
     * 数据下标改变的通知。
     *
     * @param positionNew 数据的新下标
     * @param positionOld 数据的旧下标
     * @param val         数据
     * @param number      排在一群数据中的第几位。（从0开始计数）
     *                    number=-1时，该数据为发起者。
     */
    void onDateDexChanged(int positionNew, int positionOld, V val, int number);
}

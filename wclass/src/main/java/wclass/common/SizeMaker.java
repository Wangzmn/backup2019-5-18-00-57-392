package wclass.common;

/**
 * @作者 做就行了！
 * @时间 2019-04-05下午 5:44
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@Deprecated
public interface SizeMaker<T> {
    /**
     * @param pendingWH 待设置的宽高数组。
     *                  0下标：宽。
     *                  1下标：高。
     * @param t         你爱咋咋滴。
     */
    void make(int[] pendingWH, T t);
}

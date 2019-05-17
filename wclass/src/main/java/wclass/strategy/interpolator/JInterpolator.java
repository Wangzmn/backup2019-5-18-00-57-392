package wclass.strategy.interpolator;

/**
 * @作者 做就行了！
 * @时间 2019/1/8 0008 下午 2:45
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 */
public interface JInterpolator {
    /**
     * 获取修改后的进度值。
     *
     * @param progress 当前进度。该值在0至1之间。
     * @return 修改后的进度值。
     */
    float getInterpolation(float progress);
}
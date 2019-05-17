package wclass.android.ui.view.head_tail_80percent;

/**
 * @作者 做就行了！
 * @时间 2019-04-16下午 11:06
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public interface Plugin {
    void onBindListViewWrapper(ListViewWrapper ctrl);
    void onDestroy();

    void onHeadBegin();

    void onHeadingOverflow(int sumOverflowAbsDelta);

    void onHeadThumbUp(int sumOverflowAbsDelta);

    int limitHeadGoingSumDelta(int sumOverflowAbsDelta);
}

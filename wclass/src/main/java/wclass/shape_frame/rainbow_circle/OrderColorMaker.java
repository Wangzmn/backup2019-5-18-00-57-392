package wclass.shape_frame.rainbow_circle;


/**
 * 完成于 2019年3月9日15:10:46
 *
 * @作者 做就行了！
 * @时间 2019-03-06下午 11:21
 * @该类描述： -
 * 1、为圆形颜色选择器而设计。
 * @名词解释： -
 * 0、全部按顺时针算。
 * 1、maxColorKind：颜色种类的数量，数量为6。
 * 解释：把圆分成6个扇形，每个扇形中是同一类的颜色。
 * 为什么分成6份？因为红色占120°，其中一半是绿色增长，其中一半是蓝色减少。
 * 60°就是把一个主色的增色区域和减色区域分开。
 * 2、colorKindDex：当前颜色种类所在maxColorKind中的下标。
 * 3、maxSection：每种颜色种类分成该值的份数。
 * 4、sectionDex：当前份所在maxSection中的下标。
 * 5、bright是什么？
 * 把该值当作主色的最大值。
 * 通常情况下255为颜色的最大值，
 * 就是将bright作为最大值。
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("WeakerAccess")
public abstract class OrderColorMaker {

    /**
     * 根据颜色种类以及颜色所在份数，获取最大饱和度的颜色。
     *
     * @param colorKindDex 颜色种类所在所有种类中的下标。
     * @param sectionDex   该份颜色在总份数中的下标。
     * @return 根据颜色种类以及颜色所在份数，获取最大饱和度的颜色。
     */
    public abstract int getColor(int colorKindDex, int sectionDex,
                                    int maxSection);


    /**
     * 运行于 2019年3月7日14:03:53
     * <p>
     * 获取section在总section中的百分比位置。
     *
     * @param colorKindDex 颜色种类所在所有种类中的下标。
     * @param sectionDex   该份颜色在总份数中的下标。
     * @param maxSection   把60°分成的颜色份数。
     * @return section在总section中的百分比位置。
     */
    protected float getSectionPer(int colorKindDex,
                                  int sectionDex,
                                  int maxSection) {
        switch (colorKindDex) {
            //偶数递减。（起始色递减。）
            case 0:
            case 2:
            case 4:
                return 1 - ((float) sectionDex) / maxSection;

            case 1:
            case 3:
            case 5:
                return ((float) sectionDex) / maxSection;
            //奇数递增。（尾色递增。）
            default:
                throw new IllegalStateException(
                        getStrForErrorKindDex(colorKindDex));
        }
    }

    protected String getStrForErrorKindDex(int colorKindDex) {
        return "colorKind种类最多为6种，最大下标为5。" +
                "请求的、错误的colorKindDex为：" + colorKindDex + " 。";
    }
}

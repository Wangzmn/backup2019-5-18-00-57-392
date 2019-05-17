package wclass.shape_frame.rainbow_circle;


import wclass.util.ColorUT;

/**
 * @作者 做就行了！
 * @时间 2019-03-08下午 10:08
 * @该类描述： -
 * 1、从X轴正方向，逆时针旋转，每120°分别是：红色、蓝色、绿色。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class RBGColorMaker extends OrderColorMaker {

    @Override
    public int getColor(int colorKindDex,
                           int sectionDex, int maxSection) {

        int color;
        float per = getSectionPer(colorKindDex, sectionDex, maxSection);
        switch (colorKindDex) {
            case 0://100%红，绿减少。
                color = ColorUT.argb(1, 1, per, 0);
                break;
            case 1://100%红，蓝增加。
                color = ColorUT.argb(1, 1, 0, per);
                break;

            case 2://100%蓝，红减少。
                color = ColorUT.argb(1, per, 0, 1);
                break;
            case 3://100%蓝，绿增加。
                color = ColorUT.argb(1, 0, per, 1);
                break;

            case 4://100%绿，蓝减少。
                color = ColorUT.argb(1, 0, 1, per);
                break;
            case 5://100%绿，红增加。
                color = ColorUT.argb(1, per, 1, 0);
                break;
            default:
                throw new IllegalStateException(
                        getStrForErrorKindDex(colorKindDex));
        }
        return color;
    }
}

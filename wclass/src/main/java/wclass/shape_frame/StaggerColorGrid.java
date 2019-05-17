package wclass.shape_frame;

/**
 * @作者 做就行了！
 * @时间 2019-03-10上午 12:13
 * @该类描述： -
 * 1、两种颜色的格子交错布局，构成的图像。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class StaggerColorGrid {

    /**
     * 通过各项参数，回调各种信息（布局位置、颜色）。
     *
     * @param firstColor  第一种颜色。
     * @param secondColor 第二种颜色。
     * @param subWidth    小方块宽。
     * @param subHeight   小方块高。
     * @param columnCount 列数。
     * @param rowCount    行数。
     * @param cb          回调。
     */
    public static <T> void make(T t,int firstColor, int secondColor,
                            float subWidth, float subHeight,
                            int columnCount, int rowCount,
                            float offsetX, float offsetY,
                            Callback<T> cb) {
        for (int rowDex = 0; rowDex < rowCount; rowDex++) {
            for (int columnDex = 0; columnDex < columnCount; columnDex++) {
                int color = getColor(firstColor, secondColor, rowDex, columnDex);
                float left = columnDex * subWidth + offsetX;
                float top = rowDex * subHeight + offsetY;
                float right = left + subWidth;
                float bottom = top + subHeight;
                cb.on(t,color, left, top, right, bottom);
            }
        }

    }

    /**
     * 回调接口。
     */
    public interface Callback<T> {
        void on(T t,int color, float left, float top, float right, float bottom);
    }

    /**
     * 根据对应的行数、列数，获取对应的颜色。
     */
    private static int getColor(int firstColor, int secondColor,
                                int rowDex, int columnDex) {
        int odevityRow = rowDex % 2;
        int odevityColumn = columnDex % 2;
        int color;
        //行下标为偶数。
        if (odevityRow == 0) {
            //列下标为偶数。
            //step 使用第一种颜色
            if (odevityColumn == 0) {
                color = firstColor;
            }
            //列下标为奇数。
            //step 使用第二种颜色。
            else {
                color = secondColor;
            }

        }
        //行下标为奇数。
        else {
            //列下标为奇数。
            //step 使用第一种颜色
            if (odevityColumn == 1) {
                color = firstColor;

            }
            //列下标为偶数。
            //step 使用第二种颜色。
            else {
                color = secondColor;
            }

        }
        return color;
    }

}

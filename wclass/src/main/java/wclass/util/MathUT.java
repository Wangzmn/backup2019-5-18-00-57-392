package wclass.util;

/**
 * @作者 做就行了！
 * @时间 2019-03-19下午 3:13
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class MathUT {

    /**
     * 限制value的值。
     *
     * @param value 限制该value。
     * @param min   value的最小值。
     * @param max   value的最大值。
     * @return 限制后的value。
     */
    public static float limit(float value, float min, float max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        }
        return value;
    }

    /**
     * 限制value的值。
     *
     * @param value 限制该value。
     * @param max   value的最大值。
     * @return 限制后的value。
     */
    public static float limitMax(float value, float max) {
        if (value > max) {
            return max;
        }
        return value;
    }

    /**
     * 限制value的值。
     *
     * @param value 限制该value。
     * @param min   value的最小值。
     * @return 限制后的value。
     */
    public static float limitMin(float value, float min) {
        if (value < min) {
            return min;
        }
        return value;
    }
    //--------------------------------------------------

    /**
     * 限制value的值。
     *
     * @param value 限制该value。
     * @param max   value的最大值。
     * @return 限制后的value。
     */
    public static int limitMax(int value, int max) {
        if (value > max) {
            return max;
        }
        return value;
    }

    /**
     * 限制value的值。
     *
     * @param value 限制该value。
     * @param min   value的最小值。
     * @return 限制后的value。
     */
    public static int limitMin(int value, int min) {
        if (value < min) {
            return min;
        }
        return value;
    }

    /**
     * 限制value的值。
     *
     * @param value 限制该value。
     * @param min   value的最小值。
     * @param max   value的最大值。
     * @return 限制后的value。
     */
    public static int limit(int value, int min, int max) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        }
        return value;
    }

    /**
     * 检查于 2019年3月19日15:13:38
     * <p>
     * 指定底数，指定结果，求指数（在结果数值中，至少包含多少个底数相乘）。
     *
     * @param num            结果数值
     * @param rightShiftBits 将结果数值转为二进制，往右移动几位。
     *                       该值是2的幂。
     * @return 指定底数，指定结果，求指数。
     */
    public static int log2s(int num, int rightShiftBits) {
        int count = 0;
        for (; ; ) {
            num = num >> rightShiftBits;
            if (num != 0) {
                count++;
            } else {
                return count;
            }
        }
    }

    /**
     * 检查于 2019年3月19日15:14:25
     * <p>
     * 从i开始，向上找到一个数，他为2的幂。
     *
     * @param i 以i值为起点。
     * @return 从i开始，向上找到一个数，他为2的幂。
     */
    public static int roundUpToPowerOfTwo(int i) {
        i--;
        i |= i >>> 1;
        i |= i >>> 2;
        i |= i >>> 4;
        i |= i >>> 8;
        i |= i >>> 16;
        return ++i;
    }

    /**
     * 将百分比数字转换为百分号左边的整数字。
     *
     * @param per 百分比数字。
     * @return 将百分比数字转换为百分号左边的整数字。
     */
    public static int toPercentageInt(double per) {
        return (int) (per * 100);
    }

    /**
     * 将该数转为偶数。
     *
     * @param i 该数。
     * @return 将该数转为偶数。
     */
    public static int to2s(int i) {
        if (i % 2 != 0) {
            return i - 1;
        }
        return i;
    }

    /**
     * 判断两个浮点数数是否接近。
     *
     * @param num1   数字1。
     * @param num2   数字2。
     * @param absCut 参考值。
     *               如果浮点数之差的绝对值大于该值，被视为不近似。
     * @return true：两个浮点数近似。
     * false：不近似。
     */
    public static boolean nearly(float num1, float num2, float absCut) {
        return Math.abs(num1 - num2) < absCut;
    }
}

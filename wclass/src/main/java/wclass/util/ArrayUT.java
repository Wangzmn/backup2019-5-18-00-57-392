package wclass.util;

import java.util.Iterator;
import java.util.Set;

/**
 * @作者 做就行了！
 * @时间 2018-11-24下午 5:18
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 */
public class ArrayUT {

    public static int[] intArray(int... i) {
        return i;
    }

    public static int[] emptyIntArray() {
        return new int[]{};
    }

    //--------------------------------------------------
    public static long[] longArray(long... i) {
        return i;
    }

    public static float[] floatArray(float... i) {
        return i;
    }

    public static double[] doubleArray(double... i) {
        return i;
    }
    //////////////////////////////////////////////////
    /**
     * 将int类型set集合转为int类型array数组。
     * @param intSet int类型set集合。
     * @return 将int类型set集合转为int类型array数组。
     */
    public static int[] toArray(Set<Integer> intSet) {
        int[] orderArray = new int[intSet.size()];
        int arrayDex = 0;
        for (Integer anIntSet : intSet) {
            orderArray[arrayDex++] = anIntSet;
        }
        return orderArray;
    }
}

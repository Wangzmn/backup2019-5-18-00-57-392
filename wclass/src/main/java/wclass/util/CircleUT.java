package wclass.util;

import wclass.ui.Coor;

/**
 * @作者 做就行了！
 * @时间 2019-03-07下午 9:55
 * @该类描述： -
 * 1、与圆相关的函数。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("WeakerAccess")
public class CircleUT {

    /**
     * {@link #getCoors_forScreen(Coor[], boolean, double, double, double, float, float)}
     */
    public static void getCoors_forScreen(Coor[] coors, double arc,
                                          double asHypotenuse, double hypoIncrement,
                                          float offsetX, float offsetY) {
        getCoors_forScreen(coors, false, arc,
                asHypotenuse, hypoIncrement,
                offsetX, offsetY);
    }

    /**
     * {@link #getCoors_forScreen(Coor[], boolean, double, double, double, float, float)}
     */
    public static void getCoors_forScreen(Coor[] coors, double arc,
                                          double asHypotenuse, double hypoIncrement) {
        getCoors_forScreen(coors, false, arc,
                asHypotenuse, hypoIncrement,
                0, 0);
    }

    /**
     * {@link #getCoors_forScreen(Coor[], boolean, double, double, double, float, float)}
     */
    public static void getCoors_forScreen(Coor[] coors, boolean isReuseCoors,
                                          double arc,
                                          double asHypotenuse, double hypoIncrement) {
        getCoors_forScreen(coors, isReuseCoors, arc,
                asHypotenuse, hypoIncrement,
                0, 0);
    }

    /**
     * 运行于 2019年3月7日17:52:42
     * <p>
     * 根据每次斜边长度和弧度获取屏幕中坐标，并将它们依次放入数组。
     *
     * @param coors         存放坐标的数组。
     *                      警告：不能为null。
     * @param isReuseCoors  true：coors数组中有对象。
     *                      false：coors数组中没有对象。
     * @param arc           弧度。
     * @param asHypotenuse  作为斜边。
     *                      友情提示：从该参数上，获取一次坐标。
     * @param hypoIncrement 斜边每次增加的长度，同时获取一次坐标。
     */
    public static void getCoors_forScreen(Coor[] coors, boolean isReuseCoors,
                                          double arc,
                                          double asHypotenuse, double hypoIncrement,
                                          float offsetX, float offsetY) {
        if (isReuseCoors) {

            try {
                Coor coor = coors[0];
                getCoor_forScreen(arc, asHypotenuse, coor, offsetX, offsetY);

                for (int i = 1; i < coors.length; i++) {
                    Coor coor1 = coors[i];
                    getCoor_forScreen(arc, asHypotenuse + i * hypoIncrement,
                            coor1, offsetX, offsetY);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                throw new NullPointerException("coors[]中的某个元素为null，不可复用。");
            }
        } else {
            Coor coor1 = new Coor();
            getCoor_forScreen(arc, asHypotenuse, coor1, offsetX, offsetY);
            coors[0] = coor1;

            for (int i = 1; i < coors.length; i++) {
                Coor coor = new Coor();
                getCoor_forScreen(arc, asHypotenuse + i * hypoIncrement,
                        coor, offsetX, offsetY);
                coors[i] = coor;
            }

        }
    }

    /**
     * {@link #getCoor_forScreen(double, double, Coor, float, float)}
     */
    public static void getCoor_forScreen(double arc, double asHypotenuse, Coor coor) {
        getCoor_forScreen(arc, asHypotenuse, coor, 0, 0);
    }

    /**
     * 根据斜边和弧度获取屏幕中的坐标。
     *
     * @param arc          弧度。
     * @param asHypotenuse 作为斜边。
     * @return 坐标。
     */
    public static void getCoor_forScreen(double arc, double asHypotenuse, Coor coor,
                                         float offsetX, float offsetY) {
        double x = offsetX + Math.cos(arc) * asHypotenuse;
        double y = offsetY - Math.sin(arc) * asHypotenuse;
        coor.x = (float) x;
        coor.y = (float) y;
    }

    /**
     * 运行于 2019年3月8日00:30:20
     * <p>
     * 根据角度获取弧度 ~！
     *
     * @param degree
     * @return
     */
    public static double getArc(double degree) {
        return degree / 360 * 2 * Math.PI;
    }

    public static int toDegree(double arc) {
        double sumArc = 2 * Math.PI;
        return (int) (arc / sumArc * 360 + 0.5f);
    }
}

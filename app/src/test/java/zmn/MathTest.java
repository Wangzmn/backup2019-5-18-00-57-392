package zmn;

import org.junit.Test;

import wclass.util.CircleUT;

/**
 * @作者 做就行了！
 * @时间 2019/3/18 0018
 * @使用说明：
 */
public class MathTest {
    int strokeWidth = 10;
    double width = 100;
    double height = 100;
    double arc = CircleUT.getArc(60);
    double sin = Math.sin(arc);
    @Test
    public void method2() {
        //上、下角的弧度。
        double arcForVerti = Math.atan2(width, height / 2);
        double arcFor90 = Math.PI / 2;
        double arcForSin = arcForVerti / 2;
        double arcForCos = arcFor90 - arcForSin;
        double sin = Math.sin(arcForCos);
        double cos = Math.cos(arcForCos);
        double hy = strokeWidth / cos;
        double otherSide = hy * sin;//Y方向
        //----------------------------------------------------------------------
        double arcForRight = arcForVerti;
        //右角一半的弧度。
        double cosForRight = Math.cos(arcForRight);
        double hyForRight = strokeWidth / cosForRight;//右边
        
    }
    @Test
    public void method() {
        double arc = CircleUT.getArc(60);
        double sin = Math.sin(arc);
        double cos= Math.cos(arc);
        double a1 = Math.atan2(0, 100);
        double a2 = Math.atan2(100, 100);
        double a3 = Math.atan2(100, 0);
        double a4 = Math.atan2(100, -100);

    }
}

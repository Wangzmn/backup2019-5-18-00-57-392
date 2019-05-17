package ex;


import android.view.Gravity;
import android.view.WindowManager;

/**
 * @作者 做就行了！
 * @时间 2019-05-18上午 12:31
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class GravityUT {
    public static void toLT(WindowManager.LayoutParams lp){
        lp.gravity = Gravity.LEFT|Gravity.TOP;
    }
    public static void toLB(WindowManager.LayoutParams lp){
        lp.gravity = Gravity.LEFT|Gravity.BOTTOM;
    }
    public static void toRT(WindowManager.LayoutParams lp){
        lp.gravity = Gravity.RIGHT|Gravity.TOP;
    }
    public static void toRB(WindowManager.LayoutParams lp){
        lp.gravity = Gravity.RIGHT|Gravity.BOTTOM;
    }
}

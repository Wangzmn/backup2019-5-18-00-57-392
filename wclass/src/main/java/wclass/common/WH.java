package wclass.common;

/**
 * @作者 做就行了！
 * @时间 2019-04-21上午 12:15
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("WeakerAccess")
public class WH {
    public int w;
    public int h;

    public WH() {
    }

    public WH(int w, int h) {
        this.w = w;
        this.h = h;
    }

    public void set(WH wh) {
        w = wh.w;
        h = wh.h;
    }

    public void set(int w,int h){
        this.w = w;
        this.h = h;
    }
}

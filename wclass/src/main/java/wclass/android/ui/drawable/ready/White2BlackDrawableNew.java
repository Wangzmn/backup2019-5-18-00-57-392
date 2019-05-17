package wclass.android.ui.drawable.ready;

import android.graphics.Shader;

import wclass.android.ui.LinearGradientAdvancer;
import wclass.android.ui.drawable.GradientWithStrokesDrawable;
import wclass.util.ColorUT;

/**
 * @作者 做就行了！
 * @时间 2019-03-31下午 11:07
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class White2BlackDrawableNew extends GradientWithStrokesDrawable {
    public White2BlackDrawableNew() {
        super(get(), new float[]{ ColorUT.BLACK,1f} );
    }
    private static LinearGradientAdvancer get(){
        return new LinearGradientAdvancer(0,0,0,1,
                ColorUT.WHITE, ColorUT.BLACK, Shader.TileMode.CLAMP);
    }

}

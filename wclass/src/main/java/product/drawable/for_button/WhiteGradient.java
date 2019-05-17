package product.drawable.for_button;

import android.graphics.Shader;

import wclass.android.ui.LinearGradientAdvancer;
import wclass.android.ui.drawable.GradientWithStrokesDrawable;

/**
 * @作者 做就行了！
 * @时间 2019-03-29下午 11:18
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class WhiteGradient extends GradientWithStrokesDrawable {
    public WhiteGradient() {
        super(new LinearGradientAdvancer(0,0,0,1,
                        new int[]{0xffeeeeee,0xfff6f6f6,0xfff6f6f6,0xffeeeeee,},
                        new float[]{0,0.3f,0.6f,1} ,
                        Shader.TileMode.CLAMP),
                1/8f,
                new float[]{0xffbbbbbb,2}
        );
    }
}

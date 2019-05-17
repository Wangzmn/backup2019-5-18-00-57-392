package zmn.w.uiutility.main_class.a_pending_class.color_selector_frame;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

import wclass.android.util.DebugUT;
import wclass.android.util.ViewUT;

/**
 * @作者 做就行了！
 * @时间 2019-03-11下午 4:24
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class ColorSelector {
    public FrameLayout root;
    FrameLayout circleColor;

    SeekBar lumi;

    SeekBar opacity;
    ImageView currColor;
    Button confirm;
    Button cancel;
    private Context context;
    //----------------------------------------------------------------------
    ColorSelectorFrame frame;
    //////////////////////////////////////////////////////////////////////

    public ColorSelector(Context context) {
        this.context = context;
        createView();
        a = new AAA();
        frame = new ColorSelectorFrame(a);
        int rootSize = 500;
        frame.retune(rootSize);
        ViewUT.adjustSize(root, rootSize, rootSize);

        //----------------------------------------------------------------------
        DebugUT.randomBG(root);

    }
    private void createView() {
        root = new FrameLayout(context);
        circleColor = new FrameLayout(context);
        //--------------------------------------------------
//        lumi = new SeekBar(context);
        lumi = new SeekBar(context);
        //--------------------------------------------------
        opacity = new SeekBar(context);
        currColor = new ImageView(context);
        confirm = new Button(context);
        cancel = new Button(context);
        //----------------------------------------------------------------------
        root.addView(circleColor);
        root.addView(lumi);
        root.addView(opacity);
        root.addView(currColor);
        root.addView(confirm);
        root.addView(cancel);

    }
    Canvas canvas;

    AAA a;

    class AAA implements ColorSelectorFrame.On {

        @Override
        public void onCircleColor(float width, float height, float x, float y) {
            ViewUT.adjustSize(circleColor, (int) width, (int) height);
            circleColor.setX(x);
            circleColor.setY(y);
        }

        @Override
        public void onLumiSeekbar(float width, float height, float x, float y) {
            ViewUT.adjustSize(lumi, (int) width, (int) height);
            lumi.setX(x);
            lumi.setY(y);
        }

        @Override
        public void onOpacitySeekbar(float width, float height, float x, float y) {
            ViewUT.adjustSize(opacity, (int) width, (int) height);
            opacity.setX(x);
            opacity.setY(y);
        }

        @Override
        public void onCurrColor(float width, float height, float x, float y) {
            ViewUT.adjustSize(currColor, (int) width, (int) height);
            currColor.setX(x);
            currColor.setY(y);
        }

        @Override
        public void onConfirmBtn(float width, float height, float x, float y) {
            ViewUT.adjustSize(confirm, (int) width, (int) height);
            confirm.setX(x);
            confirm.setY(y);
        }

        @Override
        public void onCancelBtn(float width, float height, float x, float y) {
            ViewUT.adjustSize(cancel, (int) width, (int) height);
            cancel.setX(x);
            cancel.setY(y);
        }
    }

}

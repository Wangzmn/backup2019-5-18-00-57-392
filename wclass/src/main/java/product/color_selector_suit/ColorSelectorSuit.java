package product.color_selector_suit;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import product.seekbar_infoview.InfoView;
import product.color_selector_suit.alpha_seek.AlphaSeek;
import product.light_seekbar.LightSeekBar;
import wclass.android.ui.view.progressbar.seekbarNew.SeekBar;
import product.rainbow_view.RainbowView;
import wclass.android.ui.drawable.stagger_color_drawable.StaggerColorDrawable;
import wclass.android.ui.drawable.stagger_color_drawable.Strategy;
import wclass.android.util.LayoutParamsUT;
import wclass.android.util.SizeUT;
import wclass.android.util.ViewUT;
import wclass.util.ColorUT;

/**
 * @作者 做就行了！
 * @时间 2019-03-29下午 1:25
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class ColorSelectorSuit extends FrameLayout {
    private static final boolean DEBUG = false;
    Frame frame;//整体结构。

    /**
     * {@link Frame}的回调类。
     */
    Resize resize;
    private Context context;
    private ColorDrawable currDrawable;
    /**
     * 触摸{@link RainbowView}选中的颜色。
     */
    private int selectColor;

    /**
     * {@link RainbowView}是否是。。。处女。
     */
    private boolean cherry =true;

    public ColorSelectorSuit(Context context) {
        super(context);
        this.context = context;
        frame = new Frame();
        resize = new Resize();
        frame.setCb(resize);
        createView();
        addView(A);
        addView(B);
        addView(C);
        setListener();
    }

    private void setListener() {
        lightSeek.setCallback(new SeekBar.Callback() {
            @Override
            public void onSeeking(SeekBar seekBar, boolean touch,
                                  int progress, int progressStart) {
                lightInfoView.setNumberText(progress);
                if (!touch) {
                    return;
                }
                rainbow.setColorMaxValue(progress);
            }

            @Override
            public void onStartSeek(SeekBar seekBar, int progress) {

            }

            @Override
            public void onEndSeek(SeekBar seekBar, int progress, int progressStart) {

            }

            @Override
            public void onCancelSeek(SeekBar seekBar, int progress, int progressStart) {

            }
        });
        alphaSeek.setCallback(new SeekBar.Callback() {
            @Override
            public void onSeeking(SeekBar seekBar, boolean touch, int progress, int progressStart) {
                alphaInfoView.setNumberText(progress);
                if (!touch) {
                    return;
                }
                if (cherry) {
                    return;
                }

                int color = ColorUT.toAlpha(currDrawable.getColor(),
                        progress / 255f);
                selectColor = color;
                currDrawable.setColor(color);
            }

            @Override
            public void onStartSeek(SeekBar seekBar, int progress) {

            }

            @Override
            public void onEndSeek(SeekBar seekBar, int progress, int progressStart) {

            }

            @Override
            public void onCancelSeek(SeekBar seekBar, int progress, int progressStart) {

            }
        });
        rainbow.setCallback(new RainbowView.Callback() {
            @Override
            public void onTouch(boolean hasPixel, int pixel) {
                if (!hasPixel) {
                    return;
                }
                if (cherry) {
                    cherry = false;
                }
                currDrawable.setColor(pixel);
                selectColor = pixel;
                alphaInfoView.setNumberText(255);
                alphaSeek.setProgress(255);
            }

            @Override
            public void onDown(boolean hasPixel, int pixel) {

            }

            @Override
            public void onMove(boolean hasPixel, int pixel) {

            }

            @Override
            public void onUp(boolean hasPixel, int pixel) {

            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!changed) {
            return;
        }
        int w = right - left;
        int h = bottom - top;
        int min = Math.min(w, h);
        frame.resize(min);
    }

    //////////////////////////////////////////////////
    /*控件初始化，及大小调整相关。*/
    //上半部分容器。
    LinearLayout A;
    //彩虹色圆形。
    RainbowView rainbow;
    //将A2控件推到右边的控件。
    View space;
    //存放lightInfoView和lightSeek。
    LinearLayout A2;
    //显示rainbow明暗度的控件。
    InfoView lightInfoView;
    //控制rainbow明暗度的seek。
    LightSeekBar lightSeek;
    //--------------------------------------------------
    //下半部分左边容器。
    LinearLayout B;
    //显示currColor透明度的控件。
    InfoView alphaInfoView;
    //存放currColor和alphaSeek。
    LinearLayout B2;
    /**
     * 显示当前颜色的控件。
     * 1、背景是灰黑间隔的网格。
     * 2、前景是ColorDrawable。
     */
    ImageView currColor;
    //控制currColor控件透明度的seek。
    AlphaSeek alphaSeek;
    //--------------------------------------------------
    //右下角两个按钮的容器。
    LinearLayout C;
    Button cancel;//取消按钮。
    Button confirm;//确定按钮。

    //--------------------------------------------------
    private void createView() {
        A = new LinearLayout(context);
        A.setOrientation(LinearLayout.HORIZONTAL);
        rainbow = new RainbowView(context);
        LinearLayout.LayoutParams params = LayoutParamsUT.
                linearParams(0, 1, 1);
        space = new View(context);
        space.setLayoutParams(params);
        A2 = new LinearLayout(context);
        A2.setOrientation(LinearLayout.VERTICAL);
        lightInfoView = new InfoView(context);
        lightInfoView.setWordText("明暗度");
        lightSeek = new LightSeekBar(context);
        LinearLayout.LayoutParams lightSeekP = LayoutParamsUT.linearParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        lightSeek.setLayoutParams(lightSeekP);
        lightSeek.setProgress(255);
        A2.addView(lightInfoView);
        A2.addView(lightSeek);

        A.addView(rainbow);
        A.addView(space);
        A.addView(A2);
        //--------------------------------------------------
        B = new LinearLayout(context);
        B.setOrientation(LinearLayout.HORIZONTAL);

        alphaInfoView = new InfoView(context);
        alphaInfoView.setWordText("透明度");
        B2 = new LinearLayout(context);
        B2.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams pB2 = LayoutParamsUT.linearParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        B2.setLayoutParams(pB2);

        LinearLayout.LayoutParams pCurr = LayoutParamsUT.linearParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 2);
        LinearLayout.LayoutParams pAlphaSeek = LayoutParamsUT.linearParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        currColor = new ImageView(context);
        currColor.setLayoutParams(pCurr);
        int cm = SizeUT.getCMpixel(context);
        int fiv = cm / 5;
        currColor.setBackground(new StaggerColorDrawable(
                0xff000000, 0xffffffff,
                fiv, fiv, Strategy.FULL_AUTO
        ));
        currDrawable = new ColorDrawable(0);
        currColor.setImageDrawable(currDrawable);

        alphaSeek = new AlphaSeek(context);
        alphaSeek.setLayoutParams(pAlphaSeek);
        alphaSeek.setProgress(255);
        int pad = 1;
        alphaSeek.setPadding(pad, pad, pad, pad);

        B2.addView(currColor);
        B2.addView(alphaSeek);

        B.addView(alphaInfoView);
        B.addView(B2);
        //--------------------------------------------------
        C = new LinearLayout(context);
        C.setOrientation(LinearLayout.VERTICAL);
        cancel = new Button(context);
        confirm = new Button(context);
        LinearLayout.LayoutParams paramsInC = LayoutParamsUT.linearParams(LayoutParams.MATCH_PARENT
                , 0, 1);
        cancel.setLayoutParams(paramsInC);
        confirm.setLayoutParams(paramsInC);
        C.addView(cancel);
        C.addView(confirm);
        //--------------------------------------------------\
    }

    class Resize implements Frame.Callback {
        float gap = 0;

        @Override
        public void on(int baseSize, float aes, float aes_another) {
            gap = baseSize / 100;
        }

        @Override
        public void onA(float width, float height, float x, float y) {
            if (DEBUG) {
                Log.e("TAG", " width = " + width);
                Log.e("TAG", " height = " + height);
                Log.e("TAG", " gap = " + gap);
                Log.e("TAG", " x = " + x);
                Log.e("TAG", " y = " + y);
            }
            //root宽高，各减两个gap。
            //root内部，下方有两个gap的间距。
            int rootWidth = (int) getWidth(width);
            int rootHeight = (int) getHeight(height);
            //xy，各增加一个gap距离。
            x = getX(x);
            y = getY(y);

            ViewUT.adjustSize(A, rootWidth, rootHeight);
            A.setX(x);
            A.setY(y);

            int gap = (int) Resize.this.gap;
            //rainbowView和A2的高
            int H = rootHeight - 2 * gap;
            //infoView的宽高。
            //rainbowView和seek之间有一个spaceView的间距，现在又多了个gap间距。
            int WH = rootWidth - rootHeight - gap;

            int rainbowWidth, rainbowHeight;
            rainbowWidth = rainbowHeight = H;
            ViewUT.adjustSize(rainbow, rainbowWidth, rainbowHeight);
            ViewUT.adjustSize(lightInfoView, WH, WH);
            //step 不用调整lightSeek大小，他自己自动调节。

        }

        @Override
        public void onB(float width, float height, float x, float y) {
            //高度减一个gap。
            int rootHeight = (int) (height - gap);
            //宽度减两个gap。
            int rootWidth = (int) getWidth(width);
            //x，右移一个gap。
            x = getX(x);
            ViewUT.adjustSize(B, rootWidth, rootHeight);
            B.setX(x);
            B.setY(y);

            //infoView的宽高。
            int WH = rootHeight;

            //step currColor和B2不用调节。
            ViewUT.adjustSize(alphaInfoView, WH, WH);
            //step 不用调节B2，B2的参数自带自动调整。
        }

        @Override
        public void onC(float width, float height, float x, float y) {
            //step C中的view自动调整。
            //两个按钮的root，宽高只减一个gap。
            int rootWidth = (int) (width - gap);
            int rootHeight = (int) (height - gap);
            ViewUT.adjustSize(C, rootWidth, rootHeight);
            C.setX(x);
            C.setY(y);
            //step 不用调整两个按钮。
        }

        private float getY(float y) {
            return y + gap;
        }

        private float getX(float x) {
            return x + gap;
        }

        private float getHeight(float height) {
            return height - 2 * gap;
        }

        private float getWidth(float width) {
            return width - 2 * gap;
        }
    }
}

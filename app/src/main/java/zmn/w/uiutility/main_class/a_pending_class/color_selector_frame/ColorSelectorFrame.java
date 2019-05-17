package zmn.w.uiutility.main_class.a_pending_class.color_selector_frame;

import wclass.util.AesUT;

/**
 * @作者 做就行了！
 * @时间 2019-03-09下午 10:29
 * @该类描述： -
 * 1、颜色选择器的布局，包括大小、坐标。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class ColorSelectorFrame {
    float rootSize;//控件Z：正方形的根容器。大小。
    float circleColorWidth, circleColorHeight;//控件A：供选色的圆形。大小。
    float lumiSeekbarWidth, lumiSeekbarHeight;//控件C：明暗度滑动条。大小。
    float opacitySeekbarWidth, opacitySeekbarHeight;//控件D：不透明度调节滑动条。大小。
    float currColorWidth, currColorHeight;//控件E：当前选中的颜色。大小。
    float confirmBtnWidth, confirmBtnHeight;//控件F：确定按钮。大小。
    float cancelBtnWidth, cancelBtnHeight;//控件G：取消按钮。大小。

    float circleColorX, circleColorY;
    float lumiSeekbarX, lumiSeekbarY;
    float opacitySeekbarX, opacitySeekbarY;
    float currColorX, currColorY;
    float confirmBtnX, confirmBtnY;
    float cancelBtnX, cancelBtnY;

    /**
     * 构造方法。
     *
     * @param on 负责回调（大小、坐标）。
     */
    public ColorSelectorFrame(On on) {
        this.on = on;
    }

    public interface On {
        void onCircleColor(float width, float height, float x, float y);

        void onLumiSeekbar(float width, float height, float x, float y);

        void onOpacitySeekbar(float width, float height, float x, float y);

        void onCurrColor(float width, float height, float x, float y);

        void onConfirmBtn(float width, float height, float x, float y);

        void onCancelBtn(float width, float height, float x, float y);
    }

    On on;


    public void retune(float rootSize) {
        retune2(rootSize);
        on.onCircleColor(circleColorWidth, circleColorHeight, circleColorX, circleColorY);
        on.onLumiSeekbar(lumiSeekbarWidth, lumiSeekbarHeight, lumiSeekbarX, lumiSeekbarY);
        on.onOpacitySeekbar(opacitySeekbarWidth, opacitySeekbarHeight, opacitySeekbarX, opacitySeekbarY);
        on.onCurrColor(currColorWidth, currColorHeight, currColorX, currColorY);
        on.onConfirmBtn(confirmBtnWidth, confirmBtnHeight, confirmBtnX, confirmBtnY);
        on.onCancelBtn(cancelBtnWidth, cancelBtnHeight, cancelBtnX, cancelBtnY);
    }

    private void retune2(float rootSize) {
        float space = rootSize / 50;
        this.rootSize = rootSize;
        //作为A的大小。
        float aesRoot = rootSize * AesUT.GOLDEN_RATIO;
        //作为C宽+D宽。
        float aesRootLo = rootSize - aesRoot;
        //作为F和G的高。
        float aes_aesRootLo = aesRootLo * AesUT.GOLDEN_RATIO;

        circleColorWidth = circleColorHeight = aesRoot - 2 * space;
        //----------------------------------------------------------------------
        lumiSeekbarWidth = opacitySeekbarWidth = (aesRootLo - 3 * space) / 2;
        lumiSeekbarHeight = opacitySeekbarHeight = aesRoot - 2 * space;
        //----------------------------------------------------------------------
        confirmBtnHeight = cancelBtnHeight = aes_aesRootLo;
        confirmBtnWidth = cancelBtnWidth = (aesRootLo - 3 * space) / 2;
        //----------------------------------------------------------------------
        currColorWidth = aesRoot - 2 * space;
        currColorHeight = aes_aesRootLo;
        //////////////////////////////////////////////////////////////////////
        /*设置坐标*/
        circleColorX = circleColorY = space;
        //----------------------------------------------------------------------
        lumiSeekbarX = aesRoot + space;
        lumiSeekbarY = space;
        //----------------------------------------------------------------------
        opacitySeekbarX = lumiSeekbarX + lumiSeekbarWidth + space;
        opacitySeekbarY = space;
        //----------------------------------------------------------------------

        float heightForBottomCtrl = aesRoot + aesRootLo / 2 - aes_aesRootLo / 2;
        currColorX = space;
        currColorY = heightForBottomCtrl;
        //----------------------------------------------------------------------
        confirmBtnX = aesRoot + space;
        //（aes_aesRootLo / 2，把按钮往上移动自己高的一半的距离。）
        float btnY = heightForBottomCtrl;
        confirmBtnY = btnY;
        //----------------------------------------------------------------------
        cancelBtnX = confirmBtnX + confirmBtnWidth + space;
        cancelBtnY = btnY;
    }

}

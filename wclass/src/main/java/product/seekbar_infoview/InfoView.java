package product.seekbar_infoview;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

import wclass.android.util.LayoutParamsUT;
import wclass.android.util.ViewUT;
import wclass.enums.Level3;

/**
 * @作者 做就行了！
 * @时间 2019-03-29下午 10:08
 * @该类描述： -
 * 1、一个LinearLayout中，
 * 上方一个textView，下方一个textView，上方显示3个文字，下方显示3个数字。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class InfoView extends LinearLayout {

    private static final boolean DEBUG = true;
    public TextView wordTv;//显示汉字的tv。
    public TextView numberTv;//显示数字的tv。
    private int mNumber;
    private String mWord;

    public InfoView(Context context) {
        super(context);
        setOrientation(VERTICAL);
        wordTv = new TextView(context) {
            @Override
            protected void onSizeChanged(int w, int h, int oldw, int oldh) {
                super.onSizeChanged(w, h, oldw, oldh);
                onAdjustWordTvFontSize(this, w, h);
            }
        };
        numberTv = new TextView(context) {
            @Override
            protected void onSizeChanged(int w, int h, int oldw, int oldh) {
                super.onSizeChanged(w, h, oldw, oldh);
                onAdjustNumberTvFontSize(this, w, h);
            }
        };
        wordTv.setIncludeFontPadding(false);
        numberTv.setIncludeFontPadding(false);
        wordTv.setGravity(Gravity.CENTER);
        numberTv.setGravity(Gravity.CENTER);
        //--------------------------------------------------
        /*高度比例绝对没有问题。*/
        LayoutParams wordP = LayoutParamsUT.linearParams(ViewGroup.LayoutParams.MATCH_PARENT
                , 0, 1);
        LayoutParams valueP = LayoutParamsUT.linearParams(ViewGroup.LayoutParams.MATCH_PARENT
                , 0, 1);
        wordTv.setLayoutParams(wordP);
        numberTv.setLayoutParams(valueP);
        //--------------------------------------------------

        addView(wordTv);
        addView(numberTv);
        int pad = 2;
        setPadding(pad, pad, pad, pad);
        if (DEBUG) {
            setWordText("透明度");
            setNumberText(255);
        }
    }

    public void setWordText(String word) {
        if (Objects.equals(word, mWord)) {
            return;
        }
        mWord = word;
        wordTv.setText(word);
    }

    public void setNumberText(int number) {
        if (number == mNumber) {
            return;
        }
        this.mNumber = number;
        numberTv.setText(number + "");
    }

    public void setWordFondBold(Level3 level) {
        ViewUT.setFondBold(wordTv, level);
    }

    public void setValueFondBold(Level3 level) {
        ViewUT.setFondBold(numberTv, level);
    }

    /**
     * 这里能显示3个汉字。
     *
     * @param wordTv 显示汉字的textView。
     * @param w      textView的宽。
     * @param h      textView的高。
     */
    protected void onAdjustWordTvFontSize(TextView wordTv, int w, int h) {
        ViewUT.adjustFontSize(wordTv, w / 3);
    }

    /**
     * 这里只能显示3个数字。
     *
     * @param numberTv 显示数字的textView。
     * @param w        textView的宽。
     * @param h        textView的高。
     */
    protected void onAdjustNumberTvFontSize(TextView numberTv, int w, int h) {
        ViewUT.adjustFontSize(numberTv, w / 2);
    }

}

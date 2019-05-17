package wclass.android.ui.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import wclass.android.util.LayoutParamsUT;
import wclass.android.util.SizeUT;
import wclass.android.util.ViewUT;

/**
 * @作者 做就行了！
 * @时间 2019-02-15下午 11:47
 * @该类用途： -
 * 1、自定义按钮数量的LinearLayout。
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class Buttons extends LinearLayout {
    private static final boolean DEBUG = false;
    LayoutParams subLayoutParams = LayoutParamsUT.linearParams(
            0, LayoutParams.MATCH_PARENT, 1
    );
    private Context context;
    List<Button> buttons = new ArrayList<>();

    //////////////////////////////////////////////////
    public Button getButton(int number) {
        return buttons.get(number - 1);
    }

    /**
     * 构造方法
     *
     * @param context     上下文。
     * @param listener    按钮点击的回调。
     * @param buttonCount 按钮数量。
     * @param buttonName  按钮上显示的文字，与按钮排列的顺序一一对应。
     */
    public Buttons(Context context,
                   OnClickListener listener,
                   int buttonCount, String... buttonName) {
        super(context);
        this.listener = listener;
        this.context = context;
        ViewGroup.LayoutParams lp = LayoutParamsUT.params(
                LayoutParams.MATCH_PARENT, SizeUT.getCMpixel(context));
        setLayoutParams(lp);
        //----------------------------------------------------------------------
        if (buttonCount < 1) {
            throw new IllegalStateException("至少一个按钮。");
        }
        addButton(buttonCount);
        //----------------------------------------------------------------------
        if (buttonName != null) {
            setButtonName(buttonName);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);
            ViewUT.adjustFontSize(button, h / 4);
            if (DEBUG) {
                Log.e("TAG", this.getClass() + "：调整一次字体大小。  ");
            }
        }
    }

    private void addButton(int count) {
        for (int i = 1; i <= count; i++) {
            Button btn = getBtn();
            btn.setIncludeFontPadding(false);
            int finalI = i;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v, finalI);
                }
            });
            addView(btn);
            buttons.add(btn);
        }
    }

    private void setButtonName(String[] buttonName) {
        int size = buttons.size();
        int length = buttonName.length;
        size = Math.min(size, length);
        for (int i = 0; i < size; i++) {
            Button button = buttons.get(i);
            button.setText(buttonName[i]);
        }
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 使用相同参数的按钮。
     */
    private Button getBtn() {
        Button b = new Button(context);
        b.setLayoutParams(subLayoutParams);
        return b;
    }

    //////////////////////////////////////////////////////////////////////
    OnClickListener listener;

    public interface OnClickListener {
        void onClick(View view, int number);
    }


}

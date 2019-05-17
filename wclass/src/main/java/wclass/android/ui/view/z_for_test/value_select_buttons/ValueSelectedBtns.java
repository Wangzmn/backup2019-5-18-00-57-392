package wclass.android.ui.view.z_for_test.value_select_buttons;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import wclass.android.ui.view.Buttons;

/**
 * @作者 做就行了！
 * @时间 2019-03-28下午 9:24
 * @该类描述： -
 * 1、该类创建了一个LinearLayout，其中5个按钮。
 * 可以通过{@link #getButtons()}获取view。
 * ①点击1至5个按钮：value分别设置为：1、2、5、10、50。
 * 可以通过{@link #getValue()}获取该值。
 * ②第6个按钮将{@link #value}转为相反数。
 * ③第7个按钮用来显示当前信息。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("WeakerAccess")
public class ValueSelectedBtns {
    private static final String CURR = "当前值为：";
    private Buttons buttons;
    private Button contentButton;
    private int value = 1;

    /**
     * 构造方法。
     *
     * @param context 上下文。
     */
    public ValueSelectedBtns(Context context) {
        buttons = new Buttons(context, new Handle(), 7,
                "val=1", "val=2", "val=5", "val=10",
                "val=50", "相反数", "1");
        contentButton = buttons.getButton(7);
    }


    public int getValue() {
        return value;
    }

    public Buttons getButtons() {
        return buttons;
    }


    private void setText() {
        contentButton.setText(value+"");
    }
    private class Handle implements Buttons.OnClickListener {
        @Override
        public void onClick(View view, int number) {
            switch (number) {
                case 1:
                    value = 1;
                    break;
                case 2:
                    value = 2;
                    break;
                case 3:
                    value = 5;
                    break;
                case 4:
                    value = 10;
                    break;
                case 5:
                    value = 50;
                    break;
                case 6:
                    value = -value;
                    break;
            }
            //不是最后一个按钮。
            if (number != 7) {
                setText();
            }
        }
    }
}

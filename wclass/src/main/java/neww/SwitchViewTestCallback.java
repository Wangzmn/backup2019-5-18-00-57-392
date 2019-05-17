package neww;

import android.util.Log;

/**
 * @作者 做就行了！
 * @时间 2019/5/17 0017
 * @使用说明：
 */
public class SwitchViewTestCallback implements SwitchView.Callback {
    @Override
    public void ON(boolean fromTouch) {
        Log.e("TAG", getClass() + "#ON:  ");
    }

    @Override
    public void OFF(boolean fromTouch) {
        Log.e("TAG", getClass() + "#OFF:  ");
    }

    @Override
    public boolean clickToON() {
        boolean canON = System.currentTimeMillis() % 2 == 0;
        Log.e("TAG", getClass() + "#clickToON:" +
                " canON = "+canON+" 。");
        return canON;
    }

    @Override
    public boolean clickToOFF() {
        boolean canOFF = System.currentTimeMillis() % 2 == 0;
        Log.e("TAG", getClass() + "#clickToOFF:" +
                " canOFF = "+canOFF+" 。");
        return canOFF;
    }
}

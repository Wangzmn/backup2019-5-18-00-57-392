package zmn.app;

import android.app.Application;

import zmn.w.uiutility.main_class.Prefer;

/**
 * @作者 做就行了！
 * @时间 2019-05-17下午 10:59
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Prefer.init(this);
    }
}

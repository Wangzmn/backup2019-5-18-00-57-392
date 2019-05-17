package zmn.w.uiutility;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import wclass.android.util.SizeUT;
import zmn.w.uiutility.main_class.Director;
import zmn.w.uiutility.main_class.Prefer;
import zmn.w.uiutility.main_class.role.Commander;
import zmn.w.uiutility.main_class.window.Window;

/**
 * @作者 做就行了！
 * @时间 2019/1/30 0030
 * @使用说明：
 */
public class ServiceMe extends Service {
    private boolean isON;
    private int meterPixel;
    private int worm;
    private Prefer prefer;
    Commander commander;
    Window menuer;
    private int cm;
    private int cpuBaseSize;
    private int menuBaseSize;
    Director director;
    @Override
    public void onCreate() {
        super.onCreate();

        Prefer.init(this);
        prefer = Prefer.getInstance();
        cm = SizeUT.getCMpixel(this);
        cpuBaseSize = cm;
        menuBaseSize = cm;
        director = new Director(this.getApplicationContext());
//        old();
    }

    private void old() {
        prefer = Prefer.getInstance();
        worm = ViewConfiguration.get(this).getScaledTouchSlop();
        meterPixel = SizeUT.getCMpixel(this);
        menuer.displayWindow();
        commander = new Commander(this.getApplicationContext());
        commander.displayWindow();
        View v = commander.getRoot().getChildAt(0);
        ViewGroup.LayoutParams lp = v.getLayoutParams();
        FrameLayout.LayoutParams lpp = new FrameLayout.LayoutParams(lp);
        lpp.gravity = Gravity.LEFT|Gravity.BOTTOM;
        v.setLayoutParams(lpp);
//        prefer.cpuWin = commander;
//        prefer.menuWin = menuer;
    }
    //////////////////////////////////////////////////////////////////////

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return
                super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        commander.detachFromWindow();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //////////////////////////////////////////////////////////////////////
}

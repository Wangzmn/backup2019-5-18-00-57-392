package zmn.w.uiutility.main_class.role;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import wclass.android.ui.EventTypeConverter;
import wclass.android.util.SizeUT;
import wclass.enums.EventType;
import wclass.enums.Level3;
import wclass.ui.event_parser.SimplePointer;
import wclass.util.ColorUT;
import zmn.w.uiutility.main_class.Prefer;
import zmn.w.uiutility.main_class.window.Window;
import wclass.android.ui.WindowParamsUT;
import zmn.w.uiutility.main_class.window_plugin.CpuPlugin;

/**
 * @作者 做就行了！
 * @时间 2019-01-28下午 10:52
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("FieldCanBeLocal")
public class Commander extends Window {
    private final int meterPixel;
    private final int worm;
    private WindowManager.LayoutParams lp;
    private Prefer prefer;
    CpuPlugin cpuPlugin;

    public Commander(Context context) {
        super(context);
        prefer = Prefer.getInstance();
        meterPixel = SizeUT.getCMpixel(context);
        worm = ViewConfiguration.get(context).getScaledTouchSlop();
        setLayoutParams(WindowParamsUT.makeDefaultLeftBottomParams());
        //todo 设置Preference的回调
        initMe();
    }

    private void initMe() {
        setOnTouchListener(new HandleEvent(false, worm));
        int width = meterPixel / 2;
        setSize(width, width);
        cpuPlugin = new CpuPlugin(context);
        cpuPlugin.setFondBold(Level3.BETTER);
        cpuPlugin.setBackground(prefer.bg_translucentBlack);
        cpuPlugin.setTextColor(ColorUT.WHITE);
        addPlugin(cpuPlugin);
    }

    class HandleEvent extends SimplePointer implements View.OnTouchListener {
        HandleEvent(boolean needRecordActTime, int worm) {
            super(needRecordActTime, worm);
        }

        int downX;
        int downY;

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int actionMask = event.getActionMasked();
            EventType type = EventTypeConverter.convert(actionMask);
            parseEvent(type, event.getRawX(), event.getRawY());
            switch (actionMask) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:

                    break;
                case MotionEvent.ACTION_UP:
                    break;

            }
            switch (type) {
                case DOWN:
                    downX = getX();
                    downY = getY();
                    break;
                case POINTER_DOWN://次点落下时，当作MOVE事件。
                case MOVE:
                    int x = downX + (int) getDeltaX_cutDown();
                    int y = downY + -(int)getDeltaY_cutDown();
                    if (x < 0) {
                        x = 0;
                    }
                    if (y < 0) {
                        y = 0;
                    }
                    applyCoor(x, y);
                    break;
                case UP:
                    break;
                //----------------------------------------------------------------------
                case POINTER_UP:
                    break;
                case NO_POINTER:
                    break;
                case EXIT_WITH_NO_POINTER:
                    break;
            }
            return false;
        }
    }

    //////////////////////////////////////////////////////////////////////
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}

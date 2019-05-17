package wclass.ui.event_parser;

import wclass.enums.EventType;

/**
 * @作者 做就行了！
 * @时间 2018-09-25下午 11:41
 * @该类用途：
 * @注意事项：
 * @使用说明：
 */
@SuppressWarnings("unused")
public class SimplePointer extends PointerCodes<SimplePointer>
        implements Cloneable {

    public SimplePointer(int worm) {
        super(worm);
    }

    public SimplePointer(int worm, float perH, float perV) {
        super(worm, perH, perV);
    }


    public SimplePointer(boolean needRecordActTime, int worm) {
        super(needRecordActTime, worm);
    }

    public SimplePointer(boolean needRecordActTime, int worm, float perH, float perV) {
        super(needRecordActTime, worm, perH, perV);
    }
    //----------------------------------------------------------------------

    /**
     * @return 没有需要处理的内部对象，直接返回本体。
     */
    @Override
    public SimplePointer clone() throws CloneNotSupportedException {
        return super.clone();
    }
    //////////////////////////////////////////////////////////////////////
    /*step 主要方法！！！*/

    /**
     * 请在触摸事件中，调用该方法。
     *
     * @param type      事件类型。{@link EventType}
     * @param x         触摸点x坐标
     * @param y         触摸点y坐标
     */
    public void parseEvent(EventType type, float x, float y) {
        switch (type) {
            case DOWN:
                onDownEvent(x, y);
                break;
            case POINTER_DOWN://次点落下时，当作MOVE事件。
            case MOVE:
                onMoveEvent(x, y);
                break;
        }
    }
    //////////////////////////////////////////////////////////////////////
    /*step DEBUG*/
    public String toStr_mainInfo(){
        //输出样本：[x=75, y=52]
        return "[x="+xMove+", y="+yMove+"]";
    }
}

package wclass.ui.event_parser;


import wclass.enums.EventType;

/**
 * @作者 做就行了！
 * @时间 2018-12-29上午 12:41
 * @该类用途： -
 * @注意事项： -
 * 1、
 * @使用说明： -
 * @思维逻辑： -
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class SpecificPointer extends PointerCodes<SpecificPointer>
        implements Cloneable {

    public int id;//触摸点ID。
    //////////////////////////////////////////////////////////////////////
    /*step 构造方法*/

    public SpecificPointer(int worm) {
        super(worm);
    }

    public SpecificPointer(int worm, float perH, float perV) {
        super(worm, perH, perV);
    }

    public SpecificPointer(boolean needRecordActTime, int worm) {
        super(needRecordActTime, worm);
    }

    public SpecificPointer(boolean needRecordActTime, int worm, float perH, float perV) {
        super(needRecordActTime, worm, perH, perV);
    }
    //----------------------------------------------------------------------

    /**
     * @return 没有需要处理的内部对象，直接返回本体。
     */
    @Override
    public SpecificPointer clone() throws CloneNotSupportedException {
        return super.clone();
    }
    //////////////////////////////////////////////////////////////////////
    /*step 主要方法！！！*/

    /**
     * 请在触摸事件中，调用该方法。
     *
     * @param type      事件类型。{@link EventType}
     * @param pointerID 触摸点ID
     * @param x         触摸点x坐标
     * @param y         触摸点y坐标
     */
    public void parseEvent(EventType type,
                           int pointerID,
                           float x, float y) {
        switch (type) {
            case DOWN:
            case POINTER_DOWN:
                onDownEvent(x, y);
                id = pointerID;
                break;
            //----------------------------------------------------------------------
            case MOVE:
                if (pointerID != id) {
                    throw new IllegalStateException("两次记录的pointerID不同，无法记录。" +
                            "该类记录的pointerID为：" + id + "。\n"
                            + "请求的、错误的pointerID为：" + pointerID + "。");
                }
                onMoveEvent(x, y);
                break;
        }
    }
    //////////////////////////////////////////////////////////////////////
    /*step DEBUG*/

    public String toStr_mainInfo(){
        //输出样本：[id=1, x=75, y=52]
        return "[id="+id+", x="+xMove+", y="+yMove+"]";
    }

}

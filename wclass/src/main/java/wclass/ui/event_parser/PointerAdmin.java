package wclass.ui.event_parser;


import java.util.ArrayList;
import java.util.List;

import wclass.y_marks.Percent_80;
import wclass.enums.EventType;

/**
 * 完成于 2019年1月7日18:08:51
 * --------------------------------------------------
 * todo
 * 1、
 * --------------------------------------------------
 *
 * @作者 做就行了！
 * @时间 2018-12-27下午 4:35
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @todo 1、测试该类的触摸点下标，是否与各个平台一致。
 */
@Percent_80
@Deprecated
@SuppressWarnings({"DanglingJavadoc", "unused"})
public class PointerAdmin {
    public EventType eventType;
    /*step 2018年12月26日22:39:20*/
    /*step 新*/
    private final SpecificPointer sampleParser;//fix

    /**
     * 触摸点集合类。
     * <p>
     * 触摸点的下标，相当于触摸点按下的先后顺序。
     */
    private List<SpecificPointer> pointers = new ArrayList<>();
    private int lastID = -1;//内部获取的最后一个触摸点ID。
    private int lastDex = -1;//此次事件发生时，该触摸点在集合类中的下标。
    private SpecificPointer lastPointer;//内部获取的最后一个触摸点。
    private boolean lessPointer;//记录当前触摸事件是否减少过触摸点。
    //////////////////////////////////////////////////////////////////////
    /*step 构造方法*/

    public PointerAdmin(SpecificPointer sampleParser) {
        this.sampleParser = sampleParser;
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 获取此次事件类型。
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * 获取此次事件的触摸点。
     */
    public SpecificPointer getPointer() {
        return lastPointer;
    }

    /**
     * 获取当前触摸点编号。（从0按顺序排列）
     *
     * @return 当前触摸点编号。
     * 返回-1时，当前为无效的触摸点。
     */
    public int getPointerDex() {
        return lastDex;
    }

    /**
     * 获取当前触摸点编号。（从1按顺序排列）
     *
     * @return 当前触摸点编号。
     * 返回0时，当前为无效的触摸点。
     */
    public int getPointerNumber() {
        return lastDex + 1;
    }

    public int getNumInTotalPointer() {
        return numInTotalPointer;
    }

    /**
     * 当前是否是第一个触摸点。
     *
     * @return true：当前是第一个触摸点。
     */
    public boolean isFirstPointer() {
        return lastDex == 0;
    }

    /**
     * 当前是否是最后一个按下的触摸点。
     *
     * @return true：当前是最后一个触摸点。
     */
    public boolean isLastPointer() {
        return lastDex == pointers.size() - 1;
    }
    //----------------------------------------------------------------------

    /**
     * 检查于 2019年1月7日17:58:57
     * <p>
     * 获取最后一个按下的触摸点。
     *
     * @return 最后一个按下的触摸点
     */
    public final SpecificPointer getLastPointer() {
        try {
            return pointers.get(pointers.size() - 1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(getDetailMessage());
        }
    }

    /**
     * 检查于 2019年1月7日17:58:57
     * <p>
     * 获取第一个按下的触摸点。
     *
     * @return 第一个按下的触摸点
     */
    public final SpecificPointer getFirstPointer() {
        try {
            return pointers.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(
                    getDetailMessage());
        }
    }
    //----------------------------------------------------------------------

    /**
     * 检查于 2019年1月7日17:58:57
     * <p>
     * 获取当前触摸点的数量。
     * <p>
     * 友情提示：UP事件后，集合类中始终有一个触摸点。
     *
     * @return 此时集合类中触摸点的数量
     */
    public final int getPointerCount() {
        return pointers.size();
    }

    //////////////////////////////////////////////////////////////////////
    /**
     * 当前触摸点，在所有的触摸点中（包括抬起的），是第几个按下的。
     */
    int numInTotalPointer;

    /**
     * 检查于 2018年12月27日01:46:56
     * <p>
     * 主要方法！解析此次触摸事件。
     * <p>
     * 警告：触摸点DOWN/POINTER_DOWN时，必须调用该方法，否则会导致空指针异常。
     *
     * @param type      事件类型。{@link EventType}
     * @param pointerID 触摸点ID
     * @param x         触摸点x坐标
     * @param y         触摸点y坐标
     * @return 触发此次事件的触摸点
     */
    public SpecificPointer parseEvent(EventType type,
                                      int pointerID,
                                      float x, float y) {
//        Log.e("TAG","  "+" parseEvent() "+" pointerID "+pointerID);
        eventType = type;
        SpecificPointer p;
        switch (type) {
            /*产生新触摸点，需要创建*/
            case DOWN:
                pointers.clear();
                p = createNew(type, pointerID, x, y);
                numInTotalPointer = 1;
                break;
            case POINTER_DOWN:
                if (lessPointer) {
                    pointers.remove(lastDex);
                    lessPointer = false;
                }
                p = createNew(type, pointerID, x, y);
                numInTotalPointer += 1;
                break;
            //----------------------------------------------------------------------
            /*已存在的触摸点滑动时，直接获取就行。*/
            case MOVE:
                if (lessPointer) {
                    pointers.remove(lastDex);
                    lessPointer = false;
                    //fix 这里会走到IllegalStateException
                    //原因：pointerID本身就不正确。
                    p = getPointer_private(pointerID);

                } else {
                    p = getPointerOptimize_private(pointerID);
                }
                p.parseEvent(type, pointerID, x, y);
                break;
            //----------------------------------------------------------------------
            /*非最后一个触摸点抬起*/
            case POINTER_UP:
                if (lessPointer) {
                    pointers.remove(lastDex);
                    p = getPointer_private(pointerID);
                } else {
                    lessPointer = true;
                    p = getPointerOptimize_private(pointerID);
                }
                break;
            case UP:
                if (lessPointer) {
                    pointers.remove(lastDex);
                    p = getPointer_private(pointerID);
                } else {
                    p = getPointerOptimize_private(pointerID);
                }
                if (pointers.size() != 1) {
                    throw new IllegalStateException("UP事件时，剩余触摸点数量异常。" +
                            "错误的触摸点数量为：" + pointers.size() + " 。");
                }
                break;
            case NO_POINTER:
                return null;
            default:
                return null;
//                throw new IllegalStateException();
        }
        return p;
    }

    /**
     * 创建新的触摸点。
     */
    private SpecificPointer createNew(EventType type, int pointerID, float x, float y) {
        SpecificPointer p;
        try {
            p = sampleParser.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new IllegalStateException(sampleParser.getClass().getCanonicalName() + "克隆失败。");
        }
        p.parseEvent(type, pointerID, x, y);

        lastID = pointerID;
        lastDex = pointers.size();
        lastPointer = p;

        pointers.add(p);
        return p;
    }

    //////////////////////////////////////////////////////////////////////

    /**
     * 检查于 2019年1月7日17:58:57
     * <p>
     * 判断该触摸点是否是最后一个按下的触摸点。
     *
     * @param p 该触摸点
     * @return true：该点为最后一个按下的触摸点。
     */
    public final boolean isLastPointer(SpecificPointer p) {
        try {
            return pointers.get(pointers.size() - 1) == p;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(getDetailMessage());
        }
    }

    /**
     * 检查于 2019年1月7日17:58:57
     * <p>
     * 判断该触摸点是否是最后一个按下的触摸点。
     *
     * @param pointerID 该触摸点ID
     * @return true：该点为最后一个按下的触摸点。
     */
    public final boolean isLastPointer(int pointerID) {
        try {
            return pointers.get(pointers.size() - 1).id == pointerID;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(getDetailMessage());
        }
    }
    //----------------------------------------------------------------------

    /**
     * 检查于 2019年1月7日17:58:57
     * <p>
     * 判断该触摸点是否是第一个按下的触摸点。
     *
     * @param p 该触摸点
     * @return true：该点为第一个按下的触摸点。
     */
    public final boolean isFirstPointer(SpecificPointer p) {
        try {
            return pointers.get(0) == p;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(getDetailMessage());
        }
    }

    /**
     * 检查于 2019年1月7日17:58:57
     * <p>
     * 判断该触摸点是否是第一个按下的触摸点。
     *
     * @param pointerID 该触摸点的ID
     * @return true：该点为第一个按下的触摸点。
     */
    public final boolean isFirstPointer(int pointerID) {
        try {
            return pointers.get(0).id == pointerID;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(getDetailMessage());
        }
    }
    //----------------------------------------------------------------------

    /**
     * 检查于 2019年1月7日17:58:57
     * <p>
     * 获取触摸点在集合类中的下标。
     *
     * @param pointerID 该触摸点的ID
     * @return 触摸点在集合类中的下标。
     * 未找到时，返回-1。
     */
    public final int getPointerDex(int pointerID) {
        for (int i = 0, size = pointers.size(); i < size; i++) {
            SpecificPointer p = pointers.get(i);
            if (p.id == pointerID) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 检查于 2019年1月7日17:58:57
     * <p>
     * 获取触摸点在集合类中的下标。
     *
     * @param p 该触摸点
     * @return 触摸点在集合类中的下标。
     * 未找到时，返回-1。
     */
    public final int getPointerDex(SpecificPointer p) {
        for (int i = 0, size = pointers.size(); i < size; i++) {
            SpecificPointer p2 = pointers.get(i);
            if (p2 == p) {
                return i;
            }
        }
        return -1;
    }
    //////////////////////////////////////////////////////////////////////
    /*step 获取触摸点*/

    /**
     * 检查于 2019年1月7日17:58:57
     * <p>
     * 获取指定ID的触摸点。
     *
     * @param pointerID 该触摸点的ID
     * @return 指定ID的触摸点。
     * 没找到时，返回null。
     */
    public final SpecificPointer getPointerByID(int pointerID) {
        for (SpecificPointer p : pointers) {
            if (p.id == pointerID) {
                return p;
            }
        }
        return null;
    }

    /**
     * 检查于 2019年1月7日17:58:57
     * <p>
     * 根据触摸点的下标，获取触摸点。
     *
     * @param dex 触摸点在集合类中的下标。
     * @return 指定下标的触摸点
     */
    public final SpecificPointer getPointerByDex(int dex) {
        try {
            return pointers.get(dex);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("请求的、错误的触摸点下标为：" + dex + " 。" +
                    "当前触摸点的数量为：" + pointers.size() + " 。");
        }
    }
    //----------------------------------------------------------------------

    private String getDetailMessage() {
        return "当前没有触摸点。请在触摸事件时调用该方法。";
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 内部优化，获取触摸点。
     *
     * @param pointerID 触摸点ID
     * @return 指定ID的触摸点。（获取不到时，说明用户操作失误。）
     */
    private SpecificPointer getPointerOptimize_private(int pointerID) {
        if (pointerID == lastID) {
            return lastPointer;
        } else {
            for (int i = 0, size = pointers.size(); i < size; i++) {
                SpecificPointer p = pointers.get(i);
                if (p.id == pointerID) {
                    lastID = pointerID;
                    lastPointer = p;
                    lastDex = i;
                    return p;
                }
            }
        }
        throw new IllegalStateException(getDetailMessage(pointerID));
    }

    private SpecificPointer getPointer_private(int pointerID) {
        for (int i = 0, size = pointers.size(); i < size; i++) {
            SpecificPointer p = pointers.get(i);
            if (p.id == pointerID) {
                lastID = pointerID;
                lastPointer = p;
                lastDex = i;
                return p;
            }
        }
        throw new IllegalStateException(getDetailMessage(pointerID));
    }

    /**
     * 内部优化，获取触摸点下标。（第几个触摸点-1。）
     *
     * @param pointerID 触摸点ID
     * @return 指定ID的触摸点所在的下标。（获取不到时，说明用户操作失误。）
     */
    private int getPointerDexInternal_assert(int pointerID) {
        for (int i = 0, size = pointers.size(); i < size; i++) {
            SpecificPointer p = pointers.get(i);
            if (p.id == pointerID) {
                lastDex = i;
                return i;
            }
        }
        throw new IllegalStateException(getDetailMessage(pointerID));
    }

    //----------------------------------------------------------------------
    private String getDetailMessage(int pointerID) {
        return "请求的、错误的pointerID为：" + pointerID + " 。" +
                "原因：DOWN/POINTER_DOWN事件时，" +
                "未调用该类的parse()方法，导致该触摸点未被记录。";
    }

    //////////////////////////////////////////////////////////////////////
    /*step DEBUG*/
    public String toStr_pointersInfo() {
        int size = pointers.size();
        StringBuilder stringBuilder = new StringBuilder(
                "屏幕中的触摸点个数为：" + size + "个。");
        for (int i = 0; i < size; i++) {
            SpecificPointer p = pointers.get(i);
            String each = "\n" +
                    "第" + (i + 1) + "个触摸点：" + p.toStr_mainInfo();
            stringBuilder.append(each);
        }
        return stringBuilder.toString();
    }

    public String toStr_idAndDex() {
        String s = "[pointers.size = " +pointers.size()+
                ", lastID = " + lastID +
                ", lastDex = " + lastDex + "]";
        return s;
    }
}

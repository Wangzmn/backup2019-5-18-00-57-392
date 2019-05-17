package wclass.z_pending_class.velocity;

import java.util.ArrayList;
import java.util.List;

import wclass.ui.event_parser.PointerAdmin;
import wclass.ui.event_parser.SpecificPointer;
import wclass.enums.EventType;
import wclass.HEC.SimpleRecycler;

/**
 * 完成于 2019年1月14日00:23:53
 *
 * @作者 做就行了！
 * @时间 2019/1/6 0006 下午 5:02
 * @该类用途： -
 * 1、用于计算点击/触摸事件的速率。
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * 【2019年1月22日00:16:07】
 * ①加入：DEBUG调试内容。
 * ②加入：分别记录X/Y方向。
 * @待解决： -
 * 1、以下方法有点蠢。
 * {@link JVelocityTracker#onCreateSimpleVelocityTracker()}
 * {@link JVelocityTracker#onCreateSimpleRecycler()}
 * {@link JVelocityTracker#onCreateList()}
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class JVelocityTracker {
    private final static boolean DEBUG = false;
    //----------------------------------------------------------------------
    /**
     * 作为样本，用于clone。
     */
    private final SimpleVelocityTracker sampleSimpleVelocityTracker;

    /**
     * {@link PointerNode}循环池。
     */
    private final SimpleRecycler<PointerNode> recycler;
    //----------------------------------------------------------------------
    /*step 需要重置的成员变量*/
    /**
     * 存放每个触摸点信息。
     */
    private final List<PointerNode> pointerNodes;

    /**
     * 记录当前是否丢失触摸点。
     * 用于下一次事件的判断处理。
     */
    private boolean lessPointer = false;

    /**
     * 内部优化使用的变量。最后一个获取的触摸点ID。
     * 可通过该ID快速找到对应的触摸点及其下标。
     */
    private int lastID;

    /**
     * 内部优化使用的变量。最后一个获取的触摸点在数组中的下标。
     */
    private int lastDex;
    /**
     * 内部优化使用的变量。最后一个获取的触摸点。
     */
    private PointerNode lastPointerNode;
    private boolean needY;
    private boolean needX;
    //////////////////////////////////////////////////////////////////////

    public JVelocityTracker() {
        this(true, true);
    }

    /**
     * 检查于 2019年1月22日01:15:06
     * <p>
     * 构造方法。
     *
     * @param needX 是否需要验证验证X方向。
     * @param needY 是否需要验证验证Y方向。
     */
    public JVelocityTracker(boolean needX, boolean needY) {
        this.needX = needX;
        this.needY = needY;

        sampleSimpleVelocityTracker = onCreateSimpleVelocityTracker();
        recycler = onCreateSimpleRecycler();
        recycler.setDefaultDataGenerator(new SimpleRecycler.DefaultDataGenerator<PointerNode>() {
            @Override
            public PointerNode onCreate() {
                try {
                    PointerNode p = new PointerNode();
                    if(needX){
                        p.velocityRecorderX = sampleSimpleVelocityTracker.clone();
                    }
                    if(needY){
                        p.velocityRecorderY = sampleSimpleVelocityTracker.clone();
                    }
                    return p;
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                    throw new IllegalStateException(
                            sampleSimpleVelocityTracker.getClass().getCanonicalName()
                                    + "克隆异常。");
                }
            }
        });
        pointerNodes = onCreateList();
    }

    //----------------------------------------------------------------------
    /*缺点：子类不能使用构造方法中的变量 创建这些对象。*/

    protected SimpleVelocityTracker onCreateSimpleVelocityTracker() {
        return new SimpleVelocityTracker();
    }

    protected SimpleRecycler onCreateSimpleRecycler() {
        return new SimpleRecycler();
    }

    protected List onCreateList() {
        return new ArrayList<>();
    }
    //////////////////////////////////////////////////////////////////////
    /*step 主要方法*/

    /**
     * 分析此次事件。
     */
    public void parse(PointerAdmin pointerAdmin) {
        EventType eventType = pointerAdmin.getEventType();
        SpecificPointer specificPointer = pointerAdmin.getPointer();
        PointerNode pointerNode;
        int id = specificPointer.id;
        switch (eventType) {
            case DOWN:
                recycleAll();
                lessPointer = false;
                pointerNode = createNew(id);

                if (DEBUG) {
                    printForDebug("DOWN事件，按下触摸点");
                }
                break;
            case POINTER_DOWN:
                if (lessPointer) {
                    recycleLast();
                    lessPointer = false;

                    if (DEBUG) {
                        printForDebug("POINTER_DOWN事件，处理之前抬起的触摸点");
                    }
                }
                pointerNode = createNew(id);
                if (DEBUG) {
                    printForDebug("POINTER_DOWN事件，按下触摸点");
                }
                break;
            case MOVE:
                if (lessPointer) {
                    recycleLast();
                    lessPointer = false;
                    pointerNode = get(id);

                    if (DEBUG) {
                        printForDebug("MOVE事件，处理之前抬起的触摸点");
                    }
                } else {
                    pointerNode = getOptimize(id);

                    if (DEBUG) {
                        printForDebug("MOVE事件");
                    }
                }
                break;
            case POINTER_UP://抬起时，不需要计算速率，直接返回。
                if (lessPointer) {
                    recycleLast();
                    get(id);

                    if (DEBUG) {
                        printForDebug("POINTER_UP事件，处理之前抬起的触摸点");
                    }
                } else {
                    lessPointer = true;
                    getOptimize(id);

                    if (DEBUG) {
                        printForDebug("POINTER_UP事件，抬起的触摸点");
                    }
                }
                return;
            case UP://抬起时，不需要计算速率，直接返回。
                if (lessPointer) {
                    recycleLast();
                    get(id);

                    if (DEBUG) {
                        printForDebug("UP事件，处理之前抬起的触摸点");
                    }
                } else {
                    getOptimize(id);//检查id。

                    if (DEBUG) {
                        printForDebug("UP事件，抬起的触摸点");
                    }
                }
                return;
            case NO_POINTER:
                if (DEBUG) {
                    printForDebug("NO_POINTER事件，最近的触摸点");
                }
                return;
            case EXIT_WITH_NO_POINTER:
                if (DEBUG) {
                    printForDebug("EXIT_WITH_NO_POINTER事件，最近的触摸点");
                }
                return;
            default:
                throw new IllegalStateException();
        }
        if (needX) {
            pointerNode.velocityRecorderX.parse(eventType, specificPointer.xMove);
        }
        if (needY) {
            pointerNode.velocityRecorderY.parse(eventType, specificPointer.yMove);
        }
    }

    private void printForDebug(String s) {
        System.out.println(
                "id = " + lastID + "，" +
                "dex = " + lastDex + "，"
                + "pointerCount = " + pointerNodes.size() + " 。" +
                "（" + s + "）"
        );
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 检查于 2019年1月14日00:16:43
     * <p>
     * 计算当前触摸点，X方向单位时间的速度。
     *
     * @param units 单位时间。单位：毫秒。
     * @return 当前触摸点，X方向单位时间的速度。
     */
    public int computeVelocityX(int units) {
        if (!needX) {
            return 0;
        }
        try {
            return lastPointerNode.velocityRecorderX.computeVelocity(units);
        } catch (NullPointerException e) {
            System.err.println(str_getWhenDown());
            return 0;
        }
    }

    /**
     * 检查于 2019年1月14日00:16:43
     * <p>
     * 计算指定ID的触摸点，X方向单位时间的速度。
     *
     * @param pointerID 触摸点的ID
     * @param units     单位时间。（单位：毫秒）
     * @return 指定ID的触摸点，X方向单位时间的速度。
     */
    public int computeVelocityX(int pointerID, int units) {
        if (!needX) {
            return 0;
        }
        try {
            return getDirect(pointerID).velocityRecorderX.computeVelocity(units);
        } catch (Exception e) {
            System.err.println(str_errPointerID(pointerID));
            return 0;
        }
    }

    /**
     * 检查于 2019年1月14日00:16:43
     * <p>
     * 计算当前触摸点，Y方向单位时间的速度。
     *
     * @param units 单位时间。单位：毫秒。
     * @return 当前触摸点，Y方向单位时间的速度。
     */
    public int computeVelocityY(int units) {
        if (!needY) {
            return 0;
        }
        try {
            return lastPointerNode.velocityRecorderY.computeVelocity(units);
        } catch (NullPointerException e) {
            System.err.println(str_getWhenDown());
            return 0;
        }
    }

    /**
     * 检查于 2019年1月14日00:16:43
     * <p>
     * 计算指定ID的触摸点，Y方向单位时间的速度。
     *
     * @param pointerID 触摸点的ID
     * @param units     单位时间。（单位：毫秒）
     * @return 指定ID的触摸点，Y方向单位时间的速度。
     */
    public int computeVelocityY(int pointerID, int units) {
        if (!needY) {
            return 0;
        }
        try {
            return getDirect(pointerID).velocityRecorderY.computeVelocity(units);
        } catch (Exception e) {
            System.err.println(str_errPointerID(pointerID));
            return 0;
        }
    }

    /**
     * 检查于 2019年1月14日00:08:38
     * <p>
     * 直接获取触摸点。
     *
     * @param pointerID 触摸点的ID。
     * @return 指定ID的触摸点
     */
    private PointerNode getDirect(int pointerID) {
        for (PointerNode pointerNode : pointerNodes) {
            if (pointerNode.id == pointerID) {
                return pointerNode;
            }
        }
        return null;
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 检查于 2019年1月14日00:07:56
     * <p>
     * 循环所有。
     */
    private void recycleAll() {
        for (int i = pointerNodes.size() - 1; i >= 0; i--) {
            recycler.recycle(pointerNodes.remove(i));
        }
    }

    /**
     * 检查于 2019年1月21日23:11:45
     * <p>
     * 循环最后一个。
     */
    private void recycleLast() {
        try {
            recycler.recycle(pointerNodes.remove(lastDex));
        } catch (Exception e) {
            System.err.print("pointer数量为：" + pointerNodes.size() + "，请求的下标为：" + lastDex + " 。");
        }
    }

    /**
     * 检查于 2019年1月22日00:15:04
     * <p>
     * 创建新的触摸点。
     *
     * @param pointerID 触摸点ID
     * @return 新的触摸点。
     */
    private PointerNode createNew(int pointerID) {
        PointerNode pointerNode = recycler.get();
        pointerNode.id = pointerID;

        lastPointerNode = pointerNode;
        lastID = pointerID;
        lastDex = pointerNodes.size();//先记录下标。
        pointerNodes.add(pointerNode);//后添加至集合类。
        return pointerNode;
    }

    /**
     * 检查于 2019年1月22日00:15:04
     * <p>
     * 通过优化的方式获取触摸点，并记录触摸点信息。
     *
     * @param pointerID 触摸点的ID。
     * @return 指定ID的触摸点
     */
    private PointerNode getOptimize(int pointerID) {
        if (pointerID == lastID) {
            if (DEBUG) {
                System.err.println("优化获取成功。");
            }
            return lastPointerNode;
        }
        for (int i = 0; i < pointerNodes.size(); i++) {
            PointerNode pointerNode = pointerNodes.get(i);
            if (pointerNode.id == pointerID) {
                lastPointerNode = pointerNode;
                lastDex = i;
                lastID = pointerID;
                if (DEBUG) {
                    System.err.println("遍历获取成功。");
                }
                return pointerNode;
            }
        }
        throw new IllegalStateException(str_recordAtDown(pointerID));
    }

    /**
     * 检查于 2019年1月22日00:15:04
     * <p>
     * 获取触摸点，并记录触摸点信息。
     *
     * @param pointerID 触摸点的ID。
     * @return 指定ID的触摸点
     */
    private PointerNode get(int pointerID) {
        for (int i = 0; i < pointerNodes.size(); i++) {
            PointerNode pointerNode = pointerNodes.get(i);
            if (pointerNode.id == pointerID) {
                lastPointerNode = pointerNode;
                lastDex = i;
                lastID = pointerID;
                return pointerNode;
            }
        }
        throw new IllegalStateException(str_recordAtDown(pointerID));
    }

    //////////////////////////////////////////////////////////////////////
    private String str_recordAtDown(int pointerID) {
        return str_errPointerID(pointerID) +
                "原因：未在DOWN/POINTER_DOWN时调用“JVelocityTracker.parse”方法，" +
                "请通过该方法记录触摸点。";
    }

    private String str_errPointerID(int pointerID) {
        return "请求的、错误的pointerID为：" + pointerID + " 。";
    }

    private String str_getWhenDown() {
        return "请在点击/触摸事件中调用该方法。";
    }
    //////////////////////////////////////////////////////////////////////

    class PointerNode {
        int id;
        SimpleVelocityTracker velocityRecorderX;
        SimpleVelocityTracker velocityRecorderY;

        PointerNode() {
        }
    }
}
package wclass.z_pending_class.velocity;

import wclass.enums.EventType;
import wclass.enums.Where3;

/**
 * 完成于 2019年1月20日23:23:54
 * <p>
 * --------------------------------------------------
 *
 * @作者 做就行了！
 * @时间 2019/1/6 0006 下午 1:59
 * @该类用途： -
 * 1、记录滑动速率，不分横向还是纵向。
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * 1、2019年1月20日23:08:31：
 * ①加入DEBUG调试内容。
 * @待解决： -
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class SimpleVelocityTracker implements Cloneable {
    private static final boolean DEBUG = false;
    private static final String LF = "\n";
    //----------------------------------------------------------------------
    private static final long SUSPEND_NEED_TIME_MILLI = 150;//触发悬停的默认时间。
    private static final int MILLI_2_NANO = 1000000;
    //----------------------------------------------------------------------
    private long suspendNeedTime_Nano;//触发悬停所需时间。
    //----------------------------------------------------------------------
    private Where3 where = Where3.SITU;//往哪个方向滑动。

    private long startTime;//一段距离开始时记录的时间。
    private long lastTime = 1;//一段距离最后记录的时间。

    private float startCoor;//一段距离的开始坐标。
    private float endCoor;//一段距离的结束坐标。
    //////////////////////////////////////////////////////////////////////
    /*step 构造方法*/

    /**
     * {@link #SimpleVelocityTracker(long)}
     */
    public SimpleVelocityTracker() {
        this(SUSPEND_NEED_TIME_MILLI);
    }

    /**
     * 构造方法。
     *
     * @param suspendNeedTime_Milli 触发悬停所需时间。单位：毫秒。
     */
    public SimpleVelocityTracker(long suspendNeedTime_Milli) {
        this.suspendNeedTime_Nano = suspendNeedTime_Milli * MILLI_2_NANO;
    }

    //----------------------------------------------------------------------

    @Override
    public SimpleVelocityTracker clone() throws CloneNotSupportedException {
        return (SimpleVelocityTracker) super.clone();
    }

    //----------------------------------------------------------------------
    public void reset() {
        where = Where3.SITU;//往哪个方向滑动。

        startTime = 0;
        lastTime = 1;

        startCoor = 0;
        endCoor = 0;
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 计算单位时间的速度。
     *
     * @param units 单位时间。单位：毫秒。
     * @return 单位时间的速度
     */
    public final int computeVelocity(long units) {
        if (units <= 0) {
            throw new IllegalArgumentException("unit必须大于0。");
        }
        switch (where) {
            case BEFORE:
                //被除数不可能为0
                long cutTime = lastTime - startTime;
                return (int) ((startCoor - endCoor) / cutTime * MILLI_2_NANO * units);
            case AFTER:
                //被除数不可能为0
                long cutTime2 = lastTime - startTime;
                return (int) ((endCoor - startCoor) / cutTime2 * MILLI_2_NANO * units);
            case SITU:
                return 0;
            default:
                throw new IllegalStateException();
        }
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 重新记录数据。（坐标、时间、滑动方向。）
     * <p>
     * 友情提示：事件开始时、反方向滑动时、暂停滑动时，都会触发该方法，用于重新记录。
     *
     * @param curr 当前坐标。
     */
    private void record(float curr) {
        startCoor = endCoor = curr;
        startTime = currNanoTime();
        lastTime = startTime + 1;
        where = Where3.SITU;
    }

    /**
     * 将该方法放入 鼠标/触摸 类型的事件中。
     *
     * @param type     事件类型。
     * @param currCoor 当前坐标。
     */
    public final void parse(EventType type, float currCoor) {
        switch (type) {
            case DOWN:
            case POINTER_DOWN:
                //step 事件刚开始，重置。
                if (DEBUG) {
                    System.out.print("事件刚开始，重置。");
                }
                record(currCoor);
                break;
            case MOVE:
                switch (where) {
                    case BEFORE:
                        //step 反方向时：重置。
                        if (currCoor > startCoor) {
                            if (DEBUG) {
                                System.out.print("BEFORE时，往之后滑动：重置。");
                            }
                            record(currCoor);
                        } else {
                            long timeNow = currNanoTime();
                            //step 暂停滑动时，重置。
                            if (timeNow - lastTime > suspendNeedTime_Nano) {
                                if (DEBUG) {
                                    System.out.print("BEFORE时，暂停滑动时：重置。");
                                }
                                record(currCoor);
                            } else {
                                if (DEBUG) {
                                    System.out.print("BEFORE时，正常滑动中。");
                                }
                                startCoor = currCoor;
                                lastTime = timeNow;
                            }
                        }
                        break;
                    case AFTER:
                        //step 反方向时：重置。
                        if (currCoor < endCoor) {
                            if (DEBUG) {
                                System.out.print("AFTER时，往之前滑动：重置。");
                            }
                            record(currCoor);
                        } else {
                            long timeNow = currNanoTime();
                            //step 暂停滑动时，重置。
                            if (timeNow - lastTime > suspendNeedTime_Nano) {
                                if (DEBUG) {
                                    System.out.print("AFTER时，暂停滑动时，重置。");
                                }
                                record(currCoor);
                            } else {
                                if (DEBUG) {
                                    System.out.print("AFTER时，正常滑动中。");
                                }
                                endCoor = currCoor;
                                lastTime = timeNow;
                            }
                        }
                        break;
                    case SITU:
                        if (currCoor > startCoor) {
                            if (DEBUG) {
                                System.out.print("SITU时，往之后滑动。");
                            }
                            where = Where3.AFTER;
                            endCoor = currCoor;
                            lastTime = currNanoTime();
                        } else {
                            if (DEBUG) {
                                System.out.print("SITU时，往之前滑动。");
                            }
                            where = Where3.BEFORE;
                            startCoor = currCoor;
                            lastTime = currNanoTime();
                        }
                        break;
                }

                break;
            case POINTER_UP:
            case UP:
            case NO_POINTER:
            case EXIT_WITH_NO_POINTER:
            default:
                break;
        }
    }

    /**
     * 获取纳秒级别以CPU为参照的时间。
     *
     * 友情提示：该时间以CPU为参照，并不是真正的时间。
     */
    private long currNanoTime() {
        return System.nanoTime();
    }
}
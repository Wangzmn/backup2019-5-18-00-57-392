package wclass.ui.event_parser;


import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import wclass.android.ui.EventTypeConverter;
import wclass.enums.EventType;

/**
 * @作者 做就行了！
 * @时间 2019-04-14下午 4:44
 * @该类描述： -
 * 1、把所有次点当成move。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class MultiSingleParser extends PointerCodes<MultiSingleParser> {
    private int lastID;

    public MultiSingleParser(int worm) {
        super(worm);
    }

    public MultiSingleParser(int worm, float perH, float perV) {
        super(worm, perH, perV);
    }

    private int getDexById(MotionEvent ev, int id) {
        for (int i = 0; i < ev.getPointerCount(); i++) {
            int pointerId = ev.getPointerId(i);
            if (pointerId == id) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 按先后顺序存放id。
     */
    List<Integer> ids = new ArrayList<>();

    public void parse(MotionEvent ev) {
        int actionMasked = ev.getActionMasked();
        EventType type = EventTypeConverter.convert(actionMasked);

        switch (type) {
            /**
             * 1、记录点的id。
             * 2、重置点的坐标。
             * 3、并且记录最后一个点id，方便MOVE中通过id获取坐标。
             */
            case DOWN://todo 如果是假的DOWN，获取不正确了。
                ids.clear();
                int acdex = ev.getActionIndex();
                int ptid = ev.getPointerId(acdex);
                lastID = ptid;
                ids.add(ptid);
                //todo 集合类中添加此id。
                onDownEvent(ev.getX(acdex), ev.getY(acdex));
                break;

            /**
             * 次点落下时，当成move，并且记录lastID。
             */
            case POINTER_DOWN:
                int acdex1 = ev.getActionIndex();
                int ptid1 = ev.getPointerId(acdex1);
                lastID = ptid1;
                ids.add(ptid1);
                onMoveEvent(ev.getX(acdex1), ev.getY(acdex1));
                break;

            case MOVE:
                int dex = getDexById(ev, lastID);
                if (dex == -1) {
                    //最后一个id居然没有下标，不应该吧。
                    return;
                }
                onMoveEvent(ev.getX(dex), ev.getY(dex));
                break;

            /**
             * 1、删除指定id，如果是最后一个点，那么找倒数第二个点。
             */
            case POINTER_UP:
                int acdex2 = ev.getActionIndex();
                int ptid2 = ev.getPointerId(acdex2);
                //最后一个点抬起。
                if (ptid2 == lastID) {
                    //从集合类删除最后一个点。
                    ids.remove(ids.size() - 1);
                    //再从集合类获取最后一个点。
                    lastID = ids.get(ids.size() - 1);
                    //记录当前lastID这个店的坐标信息。
                    int dex2 = getDexById(ev, lastID);
                    onMoveEvent(ev.getX(dex2), ev.getY(dex2));
                    //todo 从集合类中删除。
                    //todo 之后，集合类最后一个id设置为lastID。
                }
                //非最后一个点抬起。
                else {
                    //直接从集合类删除
                    removeIDInList(ptid2);
                }
                break;
            case UP:
                break;
            case NO_POINTER:
                break;
            case EXIT_WITH_NO_POINTER:
                break;
        }

    }

    /**
     * 从集合类中删除指定的id。
     *
     * @param id 该id。
     */
    private void removeIDInList(int id) {
        for (int i = 0; i < ids.size(); i++) {
            if (ids.get(i) == id) {
                ids.remove(i);
                return;
            }
        }
    }


}

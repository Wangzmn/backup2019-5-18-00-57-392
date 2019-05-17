package wclass.HEC;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import wclass.z_pending_class.data.DataDexChangeListener;
import wclass.z_pending_class.data.DataInfoListener;
import wclass.z_pending_class.data.DataRemovedListener;
import wclass.z_pending_class.data.DataSet;
import wclass.z_pending_class.data.DataGenerator;
import wclass.util.ArrayUT;
import wclass.util.MathUT;

/**
 * todo 优化
 * 1、使用自己的treeMap 获取小于等于key的val！！！
 * 2、换个类名
 * ---------------------------------------------------
 * {@link DataSet}：运行于 2018年12月7日18:00:04
 * ---------------------------------------------------
 * 内存泄漏问题：
 * 1、{@link #remove(TreeSet, DataRemovedListener, DataDexChangeListener)}：已检查。
 * 2、{@link #remove(int, DataDexChangeListener)}：已检查。
 * 3、{@link #removeAll(DataRemovedListener)}：已检查。
 * 4、{@link #removeHead()}：已检查。
 * 4、{@link #removeTail()}：已检查。
 * ---------------------------------------------------
 *
 * @作者 做就行了！
 * @时间 2018-11-21下午 4:45
 * @该类用途： -
 * 1、该类为连续的自然数队列。
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @常见问题： -
 * 1、{@link NullPointerException}
 * 解决方案：{@link #init}
 */
@SuppressWarnings("JavaDoc")
public class NaturalQueue<V>
        implements
        Train<V> {
    private static final boolean DEBUG = false;
    private static final String LF = "\n";
    private static final String VERTI_DIVIDE = "|";
    //----------------------------------------------------------------------
//    private static final int LINK_SIZE = 4;//小链表大小
    private static final int LINK_SIZE = 8;//小链表大小
    //    private static final int LINK_SIZE = 16;//小链表大小
    //    private static final int LINK_SIZE = 32;//小链表大小。32效率极差。
    private static final int SHIFT_COUNT = MathUT.log2s(LINK_SIZE, 1);//
    private static final int REST_MASK = LINK_SIZE - 1;//用于取余数
    private static final int STRING_WIDTH = 6;//表格显示时，字符串宽
    //----------------------------------------------------------------------
    /*数据引用*/
    private Node<V> head;//数据的开头
    private Node<V> tail;//数据的结尾
    private boolean isInit;//该类是否初始化
    private int size;//已存放数据的数量

    //treeMap不适合增删频繁的情况。
//    private TreeMap<Integer, Node<V>> indexedMap = new TreeMap<>();//索引存放处
//    private HashMap<Integer, Node<V>> indexedMap = new HashMap<>();//索引存放处
//    private IntMap<Node<V>> indexedMap = new IntMap<>();
    private Map<Integer, Node<V>> indexedMap;
    //----------------------------------------------------------------------
    private SimpleRecycler<Node<V>> recycler;//缓存池
    //----------------------------------------------------------------------
    private DataGenerator<V> dataGenerator;//获取指定位置数据

    //////////////////////////////////////////////////////////////////////
    public NaturalQueue() {
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 运行于 2018年12月5日00:26:00
     * <p>
     * 创建循环器。
     *
     * @return 循环器。
     */
    protected SimpleRecycler onCreateRecycler() {
        return new SimpleRecycler<>();
    }

    /**
     * 运行于 2018年12月5日00:26:00
     * <p>
     * 创建存放索引的map。
     *
     * @return 存放索引的map
     */
    protected Map onCreateIndexedMap() {
        return new IntMap() {
            @Override
            protected int getHash(int key) {
                return key >> SHIFT_COUNT;
            }
        };
    }
    //----------------------------------------------------------------------

    /**
     * 运行于 2018年12月4日16:34:10
     * <p>
     * 创建小链表，并返回小链表的头节点。
     */
    private Node<V> getSmallLink() {
        Node<V> asHead = new Node<>();
        Node<V> asTail = asHead;
        for (int i = 0; i < REST_MASK; i++) {
            Node<V> node = new Node<>();
            asTail.putNext(node);
            asTail = node;
        }
        asHead.asHeadAddTail(asTail);
        return asHead;
    }

    //////////////////////////////////////////////////////////////////////
    /*step 实现相关*/

    /**
     * 运行于 2018年11月28日16:23:56
     * <p>
     * 获取初始化状态。
     *
     * @return true：初始化完毕。false：反之。
     */
    @Override
    public boolean isInit() {
        return isInit;
    }

    /**
     * {@link NaturalQueue#init(int, DataGenerator)}
     */
    @Override
    public void init(DataGenerator<V> dataGenerator) {
        init(0, dataGenerator);
    }

    /**
     * 待调试
     * 初始化工作。
     *
     * @param position             以该位置为原点，加载数据。
     * @param dataGenerator 用于创建、获取指定位置的数据
     */
    @SuppressWarnings("unchecked")
    @Override
    public void init(int position, DataGenerator<V> dataGenerator) {
        init(position, position, dataGenerator);
    }

    @SuppressWarnings("all")
    @Override
    public void init(int fromPosition, int toPosition, DataGenerator<V> dataGenerator) {
        if (fromPosition > toPosition) {
            throw new IllegalStateException("fromPosition必须小于toPosition。");
        }

        if (isInit) {
            removeAll();
        }

        this.dataGenerator = dataGenerator;
        indexedMap = onCreateIndexedMap();
        recycler = onCreateRecycler();
        recycler.setDefaultDataGenerator(new SimpleRecycler.DefaultDataGenerator<Node<V>>() {
            @Override
            public Node<V> onCreate() {
                return getSmallLink();
            }
        });
        //----------------------------------------------------------------------
        initImpl(fromPosition, toPosition);
    }

    //----------------------------------------------------------------------

    /**
     * 运行于 2018年12月4日17:02:01
     * <p>
     * 获取指定位置的数据。
     * <p>
     * 友情提示：
     * 1、每次获取都会检查position，所以效率稍差。
     *
     * @param position 数据的位置
     * @return 指定位置的数据
     */
    public V get(int position) {
        return getNode(position).val;
    }

    /**
     * 获取指定位置的数据。
     * <p>
     * 友情提示：
     * 1、每次获取不会检查position，这是不安全的获取方式，
     * 除非你很确定是有效的position。
     *
     * @param position 数据的位置
     * @return 指定位置的数据
     */
    @Override
    public V getDirectly(int position) {
        return getDirectNode(position).val;
    }


    /**
     * 运行于 2018年11月28日14:54:24
     * <p>
     * 添加数据至头部。
     *
     * @throws NullPointerException 请调用{@link #init}初始化。
     */
    @Override
    public void addHead() {
        Node<V> futureNext = head;
        int futureKey = futureNext.key - 1;
        if (futureKey < 0) {
            System.err.println("键值不能小于0。");
            return;
//            throw new IllegalStateException(toStr_bounds() + "，key不能小于零。");
        }

        Node<V> futureHead = addBeforeParagraph(futureKey, futureNext);
        futureHead.val = dataGenerator.onGenerateData(futureKey);
        head = futureHead;

        size++;
    }

    /**
     * 运行于 2018年11月28日14:54:24
     * <p>
     * 添加数据至尾部。
     *
     * @throws NullPointerException 请调用{@link #init}初始化。
     */
    @Override
    public void addTail() {
        Node<V> futurePre = tail;
        int futureKey = futurePre.key + 1;

        Node<V> futureTail = addAfterParagraph(futureKey, futurePre);
        futureTail.val = dataGenerator.onGenerateData(futureKey);
        tail = futureTail;

        size++;
    }

    @Override
    public void addHead(int count) {
        int curr = 0;
        while (canAddHead() && curr < count) {
            addHead();
            curr++;
        }
    }

    @Override
    public void addTail(int count) {
        int curr = 0;
        while (canAddTail() && curr < count) {
            addTail();
            curr++;
        }
    }

    /**
     * 运行于 2018年11月28日15:46:28
     *
     * @return 被删除的数据
     * @throws NullPointerException 请调用{@link #init}初始化。
     */
    @Override
    public V removeHead() {
        check();

        Node<V> out = head;
        V val = out.val;
        head = head.next;
        handleRemoveHead(out, indexedMap);

        size--;
        return val;
    }

    /**
     * 运行于 2018年11月28日15:46:28
     *
     * @return 被删除的数据
     * @throws NullPointerException 请调用{@link #init}初始化。
     */
    @Override
    public V removeTail() {
        check();

        Node<V> out = tail;
        V val = out.val;
        tail = tail.pre;
        handleRemoveTail(out, indexedMap);

        size--;
        return val;
    }
    //////////////////////////////////////////////////////////////////////
    /*step 参数设置*/

    /**
     * 调整缓存大小。
     *
     * @param smallLinkTempCapacity 缓存的小链表数量。（每个小链表可以保存8个数据）
     */
    public void adjustTempCapacity(int smallLinkTempCapacity) {
        recycler.recapacity(smallLinkTempCapacity);
    }

    public SimpleRecycler getRecycler() {
        return recycler;
    }

    public Map getIndexedMap() {
        return indexedMap;
    }
    //////////////////////////////////////////////////////////////////////
    /*todo 写的不好*/
    public int size() {
        return size;
    }
    //////////////////////////////////////////////////////////////////////
    /*step 数据的交换、删除*/

    /**
     * 运行于 2018年11月28日23:42:22
     * <p>
     * 交换两个位置的数据。
     *
     * @param mainPosition   主position
     * @param secondPosition 次position
     * @param ddcListener    数据下标改变的回调
     */
    public void swap(int mainPosition, int secondPosition, DataDexChangeListener<V> ddcListener) {
        Node<V> main = getNode(mainPosition);
        Node<V> second = getNode(secondPosition);

        if (ddcListener != null) {
            ddcListener.onDateDexChanged(second.key, main.key, main.val, -1);
            ddcListener.onDateDexChanged(main.key, second.key, second.val, 0);
        }

        V mainVal = main.val;
        main.val = second.val;
        second.val = mainVal;
    }

    /**
     * todo
     * <p>
     * 将内部数据A 插入至数据B的位置，他们之间的数据下标会发生位移。
     *
     * @param innerPosition 内部数据A的位置
     * @param toPosition    数据B的位置
     * @param ddcListener   数据位置改变的回调
     */
    @SuppressWarnings("DanglingJavadoc")
    public void insertInner(int innerPosition, int toPosition, DataDexChangeListener<V> ddcListener) {
        Node<V> innerNode = getNode(innerPosition);
        Node<V> toNode = getNode(toPosition);

        if (innerPosition < toPosition) {
            int innerKey = innerNode.key;
            V innerVal = innerNode.val;
            /**
             * inner+1 至 to，往前移动一格。
             * 最后，inner数据放入to。
             */
            Node<V> toNodeNext = toNode.next;
            int number = 0;
            for (Node<V> node = innerNode.next, pre = innerNode;
                 node != toNodeNext;
                 pre = node, node = node.next) {
                int key = node.key;
                V val = node.val;

                if (ddcListener != null) {
                    ddcListener.onDateDexChanged(pre.key, key, val, number++);
                }

                pre.val = val;
            }

            if (ddcListener != null) {
                ddcListener.onDateDexChanged(toNode.key, innerKey, innerVal, -1);
            }
            toNode.val = innerVal;

        } else if (innerPosition > toPosition) {
            /**
             * inner数据放入to，to至inner-1旧数据 往后移动一格。
             */
            int outKey = toNode.key;
            V outVal = toNode.val;

            V val = innerNode.val;

            if (ddcListener != null) {
                ddcListener.onDateDexChanged(toNode.key, innerNode.key, val, -1);
            }
            toNode.val = val;
            //----------------------------------------------------------------------
            Node<V> innerNodeNext = innerNode.next;
            int number = 0;
            for (Node<V> node = toNode.next; node != innerNodeNext; node = node.next) {
                if (ddcListener != null) {
                    ddcListener.onDateDexChanged(node.key, outKey, outVal, number++);
                }
                val = node.val;
                node.val = outVal;

                outKey = node.key;
                outVal = val;
            }

        } else {
            throw new IllegalArgumentException(
                    "插入内部数据时，该数据的key不能与目标位置的key相同！！！"
                            + "\n" + "innerKey = " + innerPosition
                            + "\n" + "toKey = " + toPosition);
        }

    }

    /**
     * 运行于 2018年11月29日00:03:27
     * <p>
     * 将 外部数据 插入至 数据A的位置，A及之前的数据会往之前移动一格。
     *
     * @param toPosition  数据A的位置
     * @param data        外部数据
     * @param ddcListener 数据位置改变的回调
     */
    public void insertOuterIndentBefore(int toPosition, V data, DataDexChangeListener<V> ddcListener) {
        Node<V> futureNext = head;
        int futureKey = futureNext.key - 1;
        //到头了，抛出异常。
        if (futureKey < 0) {
            throw new IllegalStateException(toStr_bounds() + "，无法往之前缩进排列。");
        }
        Node<V> toNode = getNode(toPosition);
        //----------------------------------------------------------------------

        //保存旧key
        int outKey = toNode.key;
        //保存旧val
        V outVal = toNode.val;

        //替换为新val，不需要通知。
        toNode.val = data;
        //----------------------------------------------------------------------

        Node<V> firstPre = futureNext.pre;
        int number = toPosition - futureNext.key;
        //从toNode.pre 至 head
        for (Node<V> node = toNode.pre; node != firstPre; node = node.pre) {
            if (ddcListener != null) {
                ddcListener.onDateDexChanged(node.key, outKey, outVal, number--);
            }
            data = node.val;
            node.val = outVal;

            outKey = node.key;
            outVal = data;
        }

        Node<V> futureHead = addBeforeParagraph(futureKey, futureNext);
        futureHead.val = outVal;
        if (ddcListener != null) {
            ddcListener.onDateDexChanged(futureHead.key, outKey, outVal, number);
        }
        head = futureHead;

        size++;
    }

    /**
     * 运行于 2018年11月29日00:03:46
     * <p>
     * 将 外部数据 插入至 数据A的位置，A及之后的数据会往之后移动一格。
     *
     * @param toPosition  数据A的位置
     * @param data        外部数据
     * @param ddcListener 数据位置改变的回调
     */
    public void insertOuterIndentAfter(int toPosition, V data, DataDexChangeListener<V> ddcListener) {
        Node<V> toNode = getNode(toPosition);

        int outKey = toNode.key;
        V outVal = toNode.val;

        toNode.val = data;

        Node<V> futurePre = tail;
        Node<V> lastNext = futurePre.next;

        int number = 0;
        for (Node<V> node = toNode.next; node != lastNext; node = node.next) {
            if (ddcListener != null) {
                ddcListener.onDateDexChanged(node.key, outKey, outVal, number++);
            }
            data = node.val;
            node.val = outVal;

            outKey = node.key;
            outVal = data;
        }
        int futureKey = futurePre.key + 1;
        Node<V> futureTail = addAfterParagraph(futureKey, futurePre);
        futureTail.val = outVal;
        if (ddcListener != null) {
            ddcListener.onDateDexChanged(futureTail.key, outKey, outVal, number);
        }
        tail = futureTail;

        size++;
    }

    /**
     * {@link #remove(int, DataDexChangeListener)}
     */
    @Override
    public V remove(int position) {
        return remove(position, null);
    }

    /**
     * 运行于 2018年12月7日23:23:45
     * <p>
     * 删除指定位置的数据，之后的数据往前缩进。
     *
     * @param position    被删除的数据 的位置。
     * @param ddcListener 数据位置改变的回调
     * @return 被删除的数据
     */
    @Override
    public V remove(int position, DataDexChangeListener<V> ddcListener) {
        check();

        Node<V> pNode = getNode(position);
        V removed = pNode.val;

        Node<V> futureNext = this.tail;
        Node<V> futureNextNext = futureNext.next;
        int number = 0;
        for (Node<V> node = pNode.next, pre = pNode; node != futureNextNext; pre = node, node = node.next) {
            V val = node.val;
            if (ddcListener != null) {
                ddcListener.onDateDexChanged(pre.key, node.key, val, number++);
            }
            pre.val = val;
        }
        this.tail = futureNext.pre;
        handleRemoveTail(futureNext, indexedMap);

        size--;
        return removed;
    }

    /**
     * {@link #remove(TreeSet, DataRemovedListener, DataDexChangeListener)}
     */
    @Override
    public void remove(TreeSet<Integer> positions) {
        remove(positions, null, null);
    }

    /**
     * {@link #remove(TreeSet, DataRemovedListener, DataDexChangeListener)}
     */
    @Override
    public void remove(TreeSet<Integer> positions, DataRemovedListener<V> drListener) {
        remove(positions, drListener, null);
    }

    /**
     * {@link #remove(TreeSet, DataRemovedListener, DataDexChangeListener)}
     */
    @Override
    public void remove(TreeSet<Integer> positions, DataDexChangeListener<V> ddcListener) {
        remove(positions, null, ddcListener);
    }

    /**
     * 运行于 2018年11月29日15:17:51
     * <p>
     * 删除 指定位置 的数据。
     *
     * @param positions   被删除的数据 的位置。
     * @param drListener  数据被删除的回调
     * @param ddcListener 数据位置改变的回调
     */
    @SuppressWarnings("DanglingJavadoc")
    @Override
    public void remove(TreeSet<Integer> positions
            , DataRemovedListener<V> drListener
            , DataDexChangeListener<V> ddcListener) {
        int[] ps = ArrayUT.toArray(positions);
        int removedCount = ps.length;
        //被删除的最小下标
        int loDex = ps[0];
        //被删除的最大下标
        int hiDex = ps[removedCount - 1];

        Node<V> loNode = getNode(loDex);
        Node<V> hiNode = getNode(hiDex);

        if (removedCount >= size) {
            String prompt = toStr_bounds() + "，请求删除的数量为：" + removedCount + " 。" +
                    "请使用removeAll()方法，删除所有数据。";
            throw new IllegalArgumentException(prompt);
        }

        //之后的数据填入该节点，填入一次，pit=pit.next。
        Node<V> pit = loNode;
        //----------------------------------------------------------------------
        /**lo 至 hi 的数据往lo方向缩进。
         * 期间，如果node.key是被删除的下标，则通知删除，否则通知dexChange。
         */
        int dexInArray = 0;//“ps”数组中的下标
        int currRemovedDex = loDex;//待比较的 被删除的下标。每匹配到一次，使用“ps”数组中的下一个值。
        Node<V> hiNext = hiNode.next;

        int number = 0;
        for (Node<V> node = loNode; node != hiNext; node = node.next) {
            int key = node.key;
            V val = node.val;

            if (dexInArray < removedCount && key == currRemovedDex) {
                notifyRemoved(drListener, node);

                dexInArray++;
                if (dexInArray < removedCount) {
                    currRemovedDex = ps[dexInArray];
                }
            } else {
                if (ddcListener != null) {
                    ddcListener.onDateDexChanged(pit.key, key, val, number++);
                }
                pit.val = val;
                pit = pit.next;
            }
        }
        //----------------------------------------------------------------------
        /**
         * (hi+1) 至 tail 的数据往pit方向缩进
         */
        Node<V> tail = this.tail;
        //hiNode不是最后一个时
        if (hiNode != tail) {
            Node<V> tailNext = tail.next;
            for (Node<V> node = hiNext; node != tailNext; node = node.next) {
                int key = node.key;
                V val = node.val;

                if (ddcListener != null) {
                    ddcListener.onDateDexChanged(pit.key, key, val, number++);
                }
                pit.val = val;
                pit = pit.next;
            }
        }
        //----------------------------------------------------------------------
        /**
         * 一个一个删除多余的node，结束。
         */
        Node<V> futureTail = pit.pre;
        Node<V> node = tail;
        while (node != futureTail) {
            //先保存pre，再处理node。（如果先处理node，则会导致nullPointer）
            Node<V> nodePre = node.pre;
            handleRemoveTail(node, indexedMap);
            node = nodePre;
        }
        this.tail = futureTail;

        size -= removedCount;
    }

    @Override
    public void removeAll() {
        removeAll(null);
    }

    /**
     * 运行于 2018年11月29日16:04:16
     * <p>
     * 删除全部数据，并将该类标记为 未初始化状态。
     *
     * @param dataRemovedListener 数据删除时的回调
     */
    @Override
    public void removeAll(DataRemovedListener<V> dataRemovedListener) {
        Node<V> node = head;
        while (node != null) {
            Node<V> nodeNext = node.next;
            if (nodeNext != null) {
                if (node.key != -1) {
                    if (dataRemovedListener != null) {
                        notifyRemoved(dataRemovedListener, node);
                    }
                    //删除全部时，直接清空indexedMap，不需要一个一个删除。
                    handleRemoveHead(node, null);
                }
            }
            //node为最后一个小链表尾时
            else {
                node.clearData();
                Node<V> smallLinkHead = node.getHead();
                recycler.recycle(smallLinkHead);
                break;
            }

            node = nodeNext;
        }
        head = tail = null;
        isInit = false;
        indexedMap.clear();
    }

    //////////////////////////////////////////////////////////////////////
    /*step 主要*/

    /**
     * 运行于 2018年11月28日23:21:58
     * <p>
     * 获取头数据所在下标。
     *
     * @return 头数据所在下标
     */
    @Override
    public int from() {
        return head.key;
    }

    /**
     * 运行于 2018年11月28日23:21:58
     * <p>
     * 获取尾数据所在下标。
     *
     * @return 尾数据所在下标
     */
    @Override
    public int to() {
        return tail.key;
    }

    @Override
    public boolean contains(int position) {
        return position >= head.key && position <= tail.key;
    }
    //----------------------------------------------------------------------

    /**
     * 运行于 2018年11月29日16:06:52
     * <p>
     * 遍历全部数据，通过回调通知用户。
     *
     * @param diListener 数据信息回调接口
     */
    @Override
    public void travel(DataInfoListener<V> diListener) {
        travelImpl(head, tail, diListener);
    }

    /**
     * 运行于 2018年12月4日16:59:59
     * <p>
     * 遍历指定范围的数据，通过回调通知用户。
     *
     * @param fromPosition 数据范围的起始位置
     * @param toPosition   数据范围的终点位置
     * @param diListener   数据信息回调接口
     */
    @Override
    public void travel(int fromPosition, int toPosition, DataInfoListener<V> diListener) {
        if (fromPosition > toPosition) {
            throw new IllegalStateException("toPosition必须大于fromPosition。" +
                    "请求的fromPosition = " + fromPosition
                    + "，toPosition = " + toPosition + " 。");
        }
        //fix
        travelImpl(getNode(fromPosition), getNode(toPosition), diListener);
    }

    /**
     * 运行于 2018年12月4日17:00:09
     */
    private void travelImpl(Node<V> fromNode, Node<V> toNode, DataInfoListener<V> diListener) {
        boolean need = true;
        Node<V> tailNext = toNode.next;
        int number = 0;
        for (Node<V> node = fromNode; node != tailNext && need; node = node.next) {
            need = diListener.onDataInfo(node.key, node.val, number++);
        }
    }

    //----------------------------------------------------------------------
    /*待调试*/

    @Override
    public boolean canAddHead() {
        return head.key > 0;
    }

    /**
     * 友情提示：重写吧。
     */
    @Override
    public boolean canAddTail() {
        return true;
    }

    /**
     * 友情提示：重写吧。
     */
    @Override
    public boolean needAddHead() {
        return false;
    }

    /**
     * 友情提示：重写吧。
     */
    @Override
    public boolean needAddTail() {
        return false;
    }
    //----------------------------------------------------------------------

    @Override
    public void reInit(int position) {
        reInit(position, null);
    }

    @Override
    public void reInit(int fromPosition, int toPosition) {
        reInit(fromPosition, toPosition, null);
    }

    @Override
    public void reInit(int position, DataRemovedListener<V> dataRemovedListener) {
        reInit(position, position, dataRemovedListener);
    }

    /**
     * 运行于 2018年12月6日23:29:55
     * <p>
     * 重新初始化，加载指定范围的数据。
     *
     * @param fromPosition 起点数据的下标
     * @param toPosition   终点数据的下标
     */
    @Override
    public void reInit(int fromPosition, int toPosition, DataRemovedListener<V> dataRemovedListener) {
        if (!isInit) {
            throw new IllegalStateException("未初始化，不能使用“reInit()”方法。");
        }
        removeAll(dataRemovedListener);
        initImpl(fromPosition, toPosition);
    }

    /**
     * 初始化工作。
     *
     * @param position 以该位置为原点，初始化数据。
     */
    private void initImpl(int position) {
        Node<V> node = recycler.get();
        int rest = position & REST_MASK;
        //小链表头的key。
        int integerKey = position & ~REST_MASK;
        //小链表头，加入索引。
        indexedMap.put(integerKey, node);
        //找到key对应的节点
        node = getByRest(node, rest);
        node.key = position;
        node.val = dataGenerator.onGenerateData(position);

        head = tail = node;
        size = 1;

        isInit = true;
    }

    /**
     * 初始化，加载指定范围的数据。
     *
     * @param fromPosition 起始下标
     * @param toPosition   终点下标。
     */
    private void initImpl(int fromPosition, int toPosition) {
        initImpl(fromPosition);
        while (tail.key < toPosition) {
            addTail();
        }
    }
    //////////////////////////////////////////////////////////////////////
    /*step 群体移动*/

    /**
     * 运行于 2018年11月29日17:05:23
     * <p>
     * 所有数据往之前移动。
     *
     * @param delta 数据移动的距离
     */
    @Override
    public void migrateToBefore(int delta) {
        migrateToBefore(delta, null);
    }

    /**
     * 运行于 2018年11月29日17:05:23
     * <p>
     * 所有数据往之前移动。
     *
     * @param delta       数据移动的距离
     * @param ddcListener 数据下标改变时 的回调
     */
    @SuppressWarnings("DanglingJavadoc")
    @Override
    public void migrateToBefore(int delta, DataDexChangeListener<V> ddcListener) {
        if (delta <= 0) {
            throw new IllegalArgumentException(toStr_bounds() + "，移动的距离必须大于0。" +
                    "请求的delta为：" + delta + " 。");
        }
        /**
         * delta小于两个小链表长度 同时delta小于数据长度，此时使用旧链表扩充方法移动数据。
         */
        //futureHead永远在head之前
        //futureTail永远在tail之前
        //所以：tail.next不可能为null。
        if (delta <= size / 2) {
            migrateBeforeToRegroup(delta, ddcListener);
        } else {
            migrateBeforeToNewLink(delta, ddcListener);
        }
    }

    /**
     * 运行于 2018年11月29日17:05:23
     * <p>
     * 所有数据往之后移动。
     *
     * @param delta 数据移动的距离
     */
    @Override
    public void migrateToAfter(int delta) {
        migrateToAfter(delta, null);
    }

    /**
     * 运行于 2018年11月29日17:05:23
     * <p>
     * 所有数据往之后移动。
     *
     * @param delta       数据移动的距离
     * @param ddcListener 数据下标改变时 的回调
     */
    @SuppressWarnings("DanglingJavadoc")
    @Override
    public void migrateToAfter(int delta, DataDexChangeListener<V> ddcListener) {
        if (delta <= 0) {
            throw new IllegalArgumentException("移动的距离必须大于0。");
        }
        /**
         * delta小于两个小链表长度 同时delta小于数据长度，此时使用旧链表扩充方法移动数据。
         */
        if (delta <= size / 2) {
            migrateAfterToRegroup(delta, ddcListener);
        } else {
            migrateAfterToNewLink(delta, ddcListener);
        }
    }

    /**
     * 运行于 2018年11月29日17:00:28
     * <p>
     * 所有数据往之前移动，通过转移至新链表的方式。
     *
     * @param delta       需要移动的距离
     * @param ddcListener 数据下标改变时 的回调
     */
    @SuppressWarnings("DanglingJavadoc")
    private void migrateBeforeToNewLink(int delta, DataDexChangeListener<V> ddcListener) {
        //清空索引。重新放入索引。
        indexedMap.clear();
        //----------------------------------------------------------------------
        Node<V> head = this.head;
        Node<V> tail = this.tail;

        int headKey = head.key;
        int futureHeadKey = headKey - delta;
        if (futureHeadKey < 0) {
            throw new IllegalArgumentException(toStr_bounds() + "，目标位置超出0下标。" +
                    "请求的delta为：" + delta + " 。");
        }
        //----------------------------------------------------------------------
        //获取新链表
        Node<V> smallLinkHead = recycler.get();
        int rest = futureHeadKey & REST_MASK;
        int integerKey = futureHeadKey & ~REST_MASK;
        indexedMap.put(integerKey, smallLinkHead);
        //找到新head
        Node<V> futureHead = getByRest(smallLinkHead, rest);
        //给新head赋值
        V headVal = head.val;
        futureHead.key = futureHeadKey;
        futureHead.val = headVal;
        int number = 0;
        if (ddcListener != null) {
            ddcListener.onDateDexChanged(futureHead.key, headKey, headVal, number++);
        }
        //----------------------------------------------------------------------
        /*转移数据*/

        //将要添加的node的key
        int futureTailNextKey = futureHeadKey + 1;
        //新head作为新tail
        Node<V> futureTail = futureHead;
        //不超过tail
        Node<V> tailNext = tail.next;
        //将head.next 至tail，依次放入新链表
        for (Node<V> node = head.next; node != tailNext; node = node.next) {
            int key = node.key;
            V val = node.val;

            futureTail = addAfterParagraph(futureTailNextKey, futureTail);
            futureTail.val = val;
            if (ddcListener != null) {
                ddcListener.onDateDexChanged(futureTail.key, key, val, number++);
            }
            futureTailNextKey++;
        }

        this.head = futureHead;
        this.tail = futureTail;
    }

    /**
     * 运行于 2018年11月29日17:00:28
     * <p>
     * 所有数据往之前移动，通过扩充旧链表的方式。
     *
     * @param delta       需要移动的距离
     * @param ddcListener 数据下标改变时 的回调
     */
    @SuppressWarnings("DanglingJavadoc")
    private void migrateBeforeToRegroup(int delta, DataDexChangeListener<V> ddcListener) {
        Node<V> head = this.head;
        Node<V> tail = this.tail;
        int headKey = head.key;
        int futureHeadKey = headKey - delta;
        if (futureHeadKey < 0) {
            throw new IllegalArgumentException(toStr_bounds() + "，目标位置超出0下标。" +
                    "请求的delta为：" + delta + " 。");
        }
        //----------------------------------------------------------------------
        //往之前扩充链表。
        Node<V> futureHead = head;
        for (int i = headKey - 1; i >= futureHeadKey; i--) {
            futureHead = addBeforeParagraph(i, futureHead);
        }
        //----------------------------------------------------------------------
        /*将数据往之前移动*/

        //从这个节点，依次放入head至tail中的数据，放一次pit=pit.next。
        Node<V> pit = futureHead;
        //不超过tail
        Node<V> tailNext = tail.next;
        //必定进入for循环一次！！！
        //for循环结束后，pit为futureTail.next。
        /**
         * 从前往后移动，每移动一次，number++。
         */
        int number = 0;
        for (Node<V> node = head; node != tailNext; node = node.next) {
            int key = node.key;
            V val = node.val;
            if (ddcListener != null) {
                ddcListener.onDateDexChanged(pit.key, key, val, number++);
            }
            pit.val = val;
            pit = pit.next;
        }
        Node<V> futureTail = pit.pre;
        //----------------------------------------------------------------------
        //最后回收tail至futureTail+1
        Node<V> node = tail;
        while (node != futureTail) {
            Node<V> nodePre = node.pre;
            handleRemoveTail(node, indexedMap);
            node = nodePre;
        }

        this.head = futureHead;
        this.tail = futureTail;
    }

    /**
     * 运行于 2018年11月29日17:00:28
     * <p>
     * 所有数据往之后移动，通过转移至新链表的方式。
     *
     * @param delta       需要移动的距离
     * @param ddcListener 数据下标改变时 的回调
     */
    @SuppressWarnings("DanglingJavadoc")
    private void migrateAfterToNewLink(int delta, DataDexChangeListener<V> ddcListener) {
        //清空索引，放入新链表。
        indexedMap.clear();

        Node<V> head = this.head;
        Node<V> tail = this.tail;
        int tailKey = tail.key;
        int futureTailKey = tailKey + delta;
        //----------------------------------------------------------------------
        /*找到futureTail*/
        Node<V> smallLinkHead = recycler.get();
        int rest = futureTailKey & REST_MASK;
        int integerKey = futureTailKey & ~REST_MASK;
        indexedMap.put(integerKey, smallLinkHead);

        Node<V> futureTail = getByRest(smallLinkHead, rest);
        V tailVal = tail.val;
        futureTail.key = futureTailKey;
        futureTail.val = tailVal;
        /**
         * 最大下标。
         * 从后往前移动，每移动一次，number--。
         * 移动结束后number=-1.
         */
        int number = size - 1;
        if (ddcListener != null) {
            ddcListener.onDateDexChanged(futureTail.key, tailKey, tailVal, number--);
        }
        //----------------------------------------------------------------------
        /*tail.pre至head，分别在futureTail之前。*/
        int futureHeadPreKey = futureTailKey - 1;
        Node<V> futureHead = futureTail;
        Node<V> headPre = head.pre;
        for (Node<V> node = tail.pre; node != headPre; node = node.pre) {
            int key = node.key;
            V val = node.val;

            futureHead = addBeforeParagraph(futureHeadPreKey, futureHead);
            futureHead.val = val;
            if (ddcListener != null) {
                ddcListener.onDateDexChanged(futureHead.key, key, val, number--);
            }
            futureHeadPreKey--;
        }

        this.head = futureHead;
        this.tail = futureTail;

    }

    /**
     * 运行于 2018年11月29日17:00:28
     * <p>
     * 所有数据往之后移动，通过扩充旧链表的方式。
     *
     * @param delta       需要移动的距离
     * @param ddcListener 数据下标改变时 的回调
     */
    @SuppressWarnings("DanglingJavadoc")
    private void migrateAfterToRegroup(int delta, DataDexChangeListener<V> ddcListener) {
        Node<V> head = this.head;
        Node<V> tail = this.tail;
        int tailKey = tail.key;
        int futureTailKey = tailKey + delta;
        //----------------------------------------------------------------------
        /*往之后扩充链表，并找到futureTail*/
        Node<V> futureTail = tail;
        for (int i = tailKey + 1; i <= futureTailKey; i++) {
            futureTail = addAfterParagraph(i, futureTail);
        }
        //----------------------------------------------------------------------
        /**
         * 将tail至head数据，依次放入toNode（futureTail），
         * 放入一次，toNode=toNode.pre，最后的toNode是futureHead.pre。
         */
        Node<V> toNode = futureTail;
        Node<V> headPre = head.pre;

        /**
         * 最大下标。
         * 尾部移动一次，该值减一。
         * 执行完for后，number=-1。
         */
        int number = size - 1;
        for (Node<V> node = tail; node != headPre; node = node.pre) {
            int key = node.key;
            V val = node.val;

            if (ddcListener != null) {
                ddcListener.onDateDexChanged(toNode.key, key, val, number--);
            }
            toNode.val = val;
            toNode = toNode.pre;
        }
        Node<V> futureHead = toNode.next;
        //----------------------------------------------------------------------
        /*回收head至futureHead-1*/
        Node<V> node = head;
        while (node != futureHead) {
            //先保存pre，再处理node。（如果先处理node，则会导致nullPointer）
            Node<V> nodeNext = node.next;
            handleRemoveHead(node, indexedMap);
            node = nodeNext;
        }
        this.head = futureHead;
        this.tail = futureTail;
    }

    //////////////////////////////////////////////////////////////////////
    /*step 细节*/

    /**
     * 运行于 2018年12月4日14:31:36
     * <p>
     * 获取指定位置的数据节点。
     * <p>
     * 友情提示：
     * 1、每次获取都会检查position，所以效率稍差。
     *
     * @param position 数据节点的位置
     * @return 指定位置的数据节点
     */
    private Node<V> getNode(int position) {
        if (!contains(position)) {
            throw new IllegalArgumentException(prompt_OutOfBounds(position));
        }
        return getDirectNode(position);
    }

    /**
     * 运行于 2018年11月29日15:24:29
     * <p>
     * 获取指定位置的数据节点。
     * <p>
     * 友情提示：
     * 1、每次获取不会检查position，这是不安全的获取方式，
     * 除非你很确定是有效的position。
     *
     * @param position 数据节点的位置
     * @return 指定位置的数据节点
     */
    private Node<V> getDirectNode(int position) {
        int rest = position & REST_MASK;
        int integerKey = position & ~REST_MASK;
        Node<V> node = indexedMap.get(integerKey);
        node = getByRest(node, rest);
        return node;
    }

    /**
     * 运行于 2018年11月29日15:24:29
     *
     * <p>
     * 通过余数取出对应的节点。
     *
     * @param node 节点
     * @param rest 之后的第几个节点
     * @return 对应的节点
     */
    private Node<V> getByRest(Node<V> node, int rest) {
        try {
            switch (rest) {
                case 0:
                    return node;
                case 1:
                    return node.next;
                case 2:
                    return node.next.next;
                case 3:
                    return node.next.next.next;
                case 4:
                    return node.next.next.next.next;
                case 5:
                    return node.next.next.next.next.next;
                case 6:
                    return node.next.next.next.next.next.next;
                case 7:
                    return node.next.next.next.next.next.next.next;
                case 8:
                    return node.next.next.next.next.next.next.next
                            .next;
                case 9:
                    return node.next.next.next.next.next.next.next
                            .next.next;
                case 10:
                    return node.next.next.next.next.next.next.next
                            .next.next.next;
                case 11:
                    return node.next.next.next.next.next.next.next
                            .next.next.next.next;
                case 12:
                    return node.next.next.next.next.next.next.next
                            .next.next.next.next.next;
                case 13:
                    return node.next.next.next.next.next.next.next
                            .next.next.next.next.next.next;
                case 14:
                    return node.next.next.next.next.next.next.next
                            .next.next.next.next.next.next.next;
                case 15:
                    return node.next.next.next.next.next.next.next
                            .next.next.next.next.next.next.next.next;
            }
        } catch (Exception e) {
            int count = 0;
            while (node != null) {
                node = node.next;
                count++;
            }
            String s = "请求的rest = " + rest + ", 第" + (count + 1) + "个Node为null。";
            throw new IllegalStateException(s);
        }
        throw new IllegalStateException("余数不能大于 " + REST_MASK + " 。"
                + "请求的rest = " + rest + " 。");
    }

    //----------------------------------------------------------------------

    /**
     * 运行于 2018年11月29日15:25:54
     * <p>
     * 添加头部小链表。
     *
     * @param key  键值。
     *             通过该键值获取余数，为{@link #REST_MASK}时，头部添加新链表，
     *             并设置该链表的头为索引。
     * @param head 当前头节点。
     * @return head.pre
     */
    private Node<V> addBeforeParagraph(int key, Node<V> head) {
        int rest = key & REST_MASK;

        Node<V> headPre;
        //余数最大，需要链接新的小链表，并把链表头放入treeMap。
        if (rest == REST_MASK) {
            Node<V> smallLinkHead = recycler.get();
            Node<V> smallLinkTail = smallLinkHead.getTail();
            smallLinkTail.key = key;
            head.putPre(smallLinkTail);

            //作为索引
            int integerKey = key & ~REST_MASK;
            indexedMap.put(integerKey, smallLinkHead);

            headPre = smallLinkTail;
        } else {
            headPre = head.pre;
            headPre.key = key;
        }
        return headPre;
    }

    /**
     * 运行于 2018年11月29日15:25:54
     * <p>
     * 添加尾部小链表。
     *
     * @param tailNextKey  键值。
     *             通过该键值获取余数，为0时，尾部添加新链表，
     *             并设置该链表的头为索引。
     * @param currTail 当前尾节点。
     * @return tail.next
     */
    private Node<V> addAfterParagraph(int tailNextKey, Node<V> currTail) {
        int rest = tailNextKey & REST_MASK;

        Node<V> tailNext;
        //余数为0时，需要链接新的小链表，并把链表头放入treeMap。
        if (rest == 0) {
            tailNext = recycler.get();
            currTail.putNext(tailNext);

            //作为索引
            indexedMap.put(tailNextKey, tailNext);
            tailNext.key = tailNextKey;
        } else {
            tailNext = currTail.next;
            tailNext.key = tailNextKey;
        }
        return tailNext;
    }

    /**
     * 运行于 2018年11月29日16:19:41
     * <p>
     * 删除头时，根据键值判断是否需要删除索引、回收小链表。
     *
     * @param oldHead 旧的头节点。
     */
    private void handleRemoveHead(Node<V> oldHead, Map<Integer, Node<V>> indexedMap) {
        int key = oldHead.key;
        oldHead.clearData();
        int rest = key & REST_MASK;

        //余数为最大时（链表尾），删除小链表。
        if (rest == REST_MASK) {
            Node<V> next = oldHead.next;
            next.pre = null;
            oldHead.next = null;

            Node<V> asHead = oldHead.getHead();
            recycler.recycle(asHead);

            if (indexedMap != null) {
                //链表头的key
                int integerKey = key & ~REST_MASK;
                this.indexedMap.remove(integerKey);
            }
        }
    }

    /**
     * 运行于 2018年11月29日16:19:41
     * <p>
     * 删除尾时，根据键值判断是否需要删除索引、回收小链表。
     *
     * @param oldTail 旧的尾节点。
     */
    private void handleRemoveTail(Node<V> oldTail, Map<Integer, Node<V>> indexedMap) {
        int key = oldTail.key;
        oldTail.clearData();
        int rest = key & REST_MASK;

        //余数为0时（链表头），删除小链表。
        if (rest == 0) {
            Node<V> pre = oldTail.pre;
            pre.next = null;
            oldTail.pre = null;
            recycler.recycle(oldTail);

            if (indexedMap != null) {
                indexedMap.remove(key);
            }
        }
    }
    //----------------------------------------------------------------------
    /*复用最多*/

    /**
     * 运行于 2018年11月28日16:32:49
     * <p>
     * 通知删除。
     */
    private void notifyRemoved(DataRemovedListener<V> drListener, Node<V> node) {
        if (drListener != null) {
            drListener.onDataRemoved(node.key, node.val);
        }
    }

    //////////////////////////////////////////////////////////////////////
    /*step 调试相关*/
    /*约束，检查。*/

    /**
     * todo 改成size。
     */
    private void check() {
        if (size == 1) {
            throw new IllegalStateException(prompt_LastNode());
        }
    }
    //----------------------------------------------------------------------
    /*提示信息*/

    /**
     * 字符串：至少保留一个数据。
     */
    private String prompt_LastNode() {
        return toStr_bounds() + "，请使用removeAll()删除最后的数据。";
    }

    /**
     * 字符串：请求下标超出数据有效范围。
     */
    private String prompt_OutOfBounds(int position) {
        return toStr_bounds() + "，请求的参数为：position = " + position + " 。";
    }

    //////////////////////////////////////////////////////////////////////
    /*step 打印所有节点（包括没放入数据的 最头/尾。*/
    /*运行于 2018年11月28日18:13:30*/

    /**
     * 字符串：数据有效范围。
     */
    public String toStr_bounds() {
        return "[有效position为：from = " + head.key + ", to = " + tail.key + "]";
    }

    /**
     * 字符串：主要信息。
     */
    public String toStr_summary() {
        return toStr_bounds()//数据有效范围
                + LF + toStr_indexedMap()//treeMap中的索引
                + LF + toStr_recyclerSize();//缓存了几个小链表
    }

    /**
     * 字符串：循环池当前大小。（缓存了几个小链表）
     */
    public String toStr_recyclerSize() {
        return "recycler.size = " + recycler.size();

    }

    /**
     * 字符串：循环池详细信息。
     */
    public String toStr_recycler() {
        return "recycler:" + recycler.toString();
    }

    /**
     * 字符串：索引信息。
     */
    public String toStr_indexedMap() {
        Set<Integer> keys = indexedMap.keySet();
        StringBuilder s = new StringBuilder("indexedMap:" + "[索引为：");
        Iterator<Integer> iterator = keys.iterator();
        while (iterator.hasNext()) {
            Integer key = iterator.next();
            s.append(key);
            if (iterator.hasNext()) {
                s.append(", ");
            }
        }
        s.append("]");
        return s.toString();
    }

    /**
     * 字符串：以表格形式显示节点内容。
     */
    public String toStr_nodeInfo() {
        Node<V> first = getFirstNode();
        StringBuilder ss = new StringBuilder();
        if (first.key < 0) {
            ss.append(getSeparator()).append(LF);
        }
        while (first != null) {
            ss.append(getFormatStr(first.key, first.val != null));
            first = first.next;
        }

        if (!isTailLastInSmallLink()) {
            ss.append(VERTI_DIVIDE);
        }
        ss.append(LF)
                .append(getSeparator())
                .append(LF);

        return ss.toString();
    }

    /**
     * 字符串：键值、val信息。
     */
    private String getFormatStr(int key, boolean hasVal) {
        if (key < 0) {
            return VERTI_DIVIDE + format(key + "") + format(hasVal);
        } else {
            int rest = key % 8;
            String ss = VERTI_DIVIDE + format(key + "") + format(hasVal);
            if (rest == 7) {
                ss = ss + VERTI_DIVIDE;
            } else if (rest == 0) {
                ss = LF + getSeparator() + LF + ss;
            }
            return ss;
        }
    }

    /**
     * 打印出与表格等宽的大分割线。
     */
    private String getSeparator() {
        return getSeparator((STRING_WIDTH * 2 + 1) * LINK_SIZE + 1);
    }

    /**
     * 打印出指定数量的小分割线。
     *
     * @noinspection SameParameterValue
     */
    private String getSeparator(int count) {
        StringBuilder separator = new StringBuilder();
        for (int i = 0; i < count; i++) {
            separator.append("-");
        }
        return separator.toString();
    }

    /**
     * 数字等宽显示。
     */
    private String format(String s) {
        return String.format("%" + STRING_WIDTH + "s", s);
    }

    /**
     * 数据信息等宽显示。
     */
    private String format(boolean b) {
        String s;
        if (b) {
            s = "have";
        } else {
            s = "none";
        }
        return String.format("%" + STRING_WIDTH + "s", s);
    }

    /**
     * 获取总链表中的第一个节点。
     */
    private Node<V> getFirstNode() {
        Node<V> head = this.head;
        while (head.pre != null) {
            head = head.pre;
        }
        return head;
    }

    /**
     * {@link #tail}是否是 总链表的最后一个节点。
     */
    private boolean isTailLastInSmallLink() {
        return (tail.key & REST_MASK) == REST_MASK;
    }
    //////////////////////////////////////////////////////////////////////
    public final class Node<V> {
        public int key = -1;//键值
        public V val;//携带的数据
        //----------------------------------------------------------------------
        public Node<V> pre;//前一个node
        public Node<V> next;//下一个node
        //----------------------------------------------------------------------
        public Node<V> head;//头尾之间的node 该属性为null。
        public Node<V> tail;//头尾之间的node 该属性为null。
        //////////////////////////////////////////////////////////////////////

        public Node() {
        }

        public Node(int key, V val) {
            this.key = key;
            this.val = val;
        }

//    public void remake(int key, V val) {
//        this.key = key;
//        this.val = val;
//    }

        public void clearData() {
            key = -1;
            val = null;
        }

        public void clearLinked() {
            pre = next = head = tail = null;
        }

        //////////////////////////////////////////////////////////////////////

        /**
         * 一组小链表中，自身作为小链表的头，将参数作为小链表的尾。
         * @param node 作为小链表的尾的node节点。
         */
        public void asHeadAddTail(Node<V> node) {
            head = this;
            tail = node;

            node.head = this;
            node.tail = node;
        }

        public Node<V> getHead() {
            return head;
        }

        public Node<V> getTail() {
            return tail;
        }

        //////////////////////////////////////////////////////////////////////
        public V pollVal() {
            V val = this.val;
            this.val = null;
            return val;
        }

        //////////////////////////////////////////////////////////////////////
        public void putPre(Node<V> node) {
            pre = node;
            node.next = this;
        }

        public void putNext(Node<V> node) {
            next = node;
            node.pre = this;
        }

        //////////////////////////////////////////////////////////////////////

        /**
         * todo 待优化。 先写成互相赋值为null。
         *
         * @return
         */
        public Node<V> pollPre() {
            Node<V> pre = this.pre;
            this.pre = null;
            pre.next = null;
            return pre;
        }

        public Node<V> pollNext() {
            Node<V> next = this.next;
            this.next = null;
            next.pre = null;
            return next;
        }

        //////////////////////////////////////////////////////////////////////
    }


}

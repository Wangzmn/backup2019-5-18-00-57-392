package wclass.HEC;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import wclass.util.MathUT;


/**
 * @作者 做就行了！
 * @时间 2019-04-29上午 12:03
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * 1、2019年4月29日00:03:25，小修改。
 * @待解决： -
 */
@SuppressWarnings({"WeakerAccess", "unused", "DanglingJavadoc"})
public class IntMap<V> extends AbstractMap<Integer, V> implements Cloneable {
    private static final boolean DEBUG = false;
    private static final String LF = "\n";
    //----------------------------------------------------------------------
    private static final int MINIMUM_CAPACITY = 4;//最小容量
    private static final int MAXIMUM_CAPACITY = 1 << 30;//最大容量
    //----------------------------------------------------------------------
    Node<V>[] table;//存放数据的数组。
    private int maxDex = -1;//数组最大下标。
    private int size;//当前数据的数量。
    private int threshold;//阈值。达到该值扩充数组。
    private int modCount;//修改次数。
    private EntrySet entrySet;//键值对
    //----------------------------------------------------------------------
    private float thresholdPer;//阈值百分比。通过该值设置threshold = 数组总大小*thresholdPer。
    //////////////////////////////////////////////////////////////////////
    /*step 构造方法*/
    /*运行于 2018年12月5日23:08:08*/

    /**
     * {@link IntMap#IntMap(int, float)}
     */
    public IntMap() {
        this(MINIMUM_CAPACITY);
    }

    /**
     * {@link IntMap#IntMap(int, float)}
     */
    public IntMap(int capacity) {
        this(capacity, 1);
    }

    /**
     * {@link IntMap#IntMap(int, float)}
     */
    public IntMap(float thresholdPer) {
        this(MINIMUM_CAPACITY, thresholdPer);
    }

    /**
     * 构造方法。
     *
     * @param capacity     存放数据的数组大小
     * @param thresholdPer 阈值百分比。
     *                     当前容量占总容量的百分比，达到该值时进行容量扩充。
     */
    public IntMap(int capacity, float thresholdPer) {
        if (thresholdPer <= 0) {
            throw new IllegalArgumentException("thresholdPer只能大于零。" +
                    "请求的thresholdPer为：" + thresholdPer + " 。");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity只能大于零。" +
                    "请求的capacity为：" + capacity + " 。");
        }
        this.thresholdPer = thresholdPer;
        capacity = MathUT.roundUpToPowerOfTwo(capacity);
        makeTable(capacity);
    }
    //----------------------------------------------------------------------
    /*运行于 2018年12月5日23:13:01*/

    /**
     * {@link IntMap#IntMap(Map, float)}
     */
    public IntMap(Map<Integer, V> map) {
        this(map, 1f);
    }

    /**
     * 构造方法。
     *
     * @param thresholdPer 阈值百分比。
     *                     当前容量占总容量的百分比，达到该值时进行容量扩充。
     */
    @SuppressWarnings("unchecked")
    public IntMap(Map<Integer, V> map, float thresholdPer) {
        if (thresholdPer <= 0) {
            throw new IllegalArgumentException("thresholdPer只能大于零。" +
                    "请求的、错误的thresholdPer为：" + thresholdPer + " 。");
        }
        this.thresholdPer = thresholdPer;
        if (map != null) {
            knownPut(map);
        } else {
            makeTable(MINIMUM_CAPACITY);
        }
    }

    /**
     * 运行于 2018年12月5日23:13:56
     * <p>
     * 可以确定表大小的 put方法。
     *
     * @param map 通过该map创建intMap
     */
    @SuppressWarnings({"unchecked", "WhileLoopReplaceableByForEach"})
    private void knownPut(Map<Integer, V> map) {
        //数据至少占 thresholdPer百分比，数据/thresholdPer 得到最小的总大小。
        int capacityNotTwo = (int) (map.size() / thresholdPer + 0.5f);
        int capacity = MathUT.roundUpToPowerOfTwo(capacityNotTwo);
        Node<V>[] table = makeTable(capacity);
        Iterator<Entry<Integer, V>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<Integer, V> entry = iterator.next();
            int key = entry.getKey();
            V val = entry.getValue();

            int hash = getHash(key);
            int dex = hash & maxDex;

            //从自己的数组中取出指定下标的单向链表。
            Node<V> linkedHead = table[dex];
            Node<V> futureHead = new Node<>(key, val, hash);
            //如果指定下标的没有链表，则直接将新的节点放入该下标。
            if (linkedHead == null) {
                table[dex] = futureHead;
            }
            /**
             * 如果数组的指定下标有链表，就将新的node作为链表头，
             * 将旧的链表放在它的后边。
             */
            else {
                futureHead.next = linkedHead;
                table[dex] = futureHead;
            }
            size++;
        }
    }

    /**
     * fix 这里写的不好。2019年4月28日23:54:53
     * <p>
     * 使用新的数组存放数据，再处理一些细节。
     *
     * @param size 请求大小。
     *             警告：size为2的幂。
     */
    @SuppressWarnings("unchecked")
    private Node<V>[] makeTable(int size) {
        table = new Node[size];
        maxDex = size - 1;
        threshold = (int) (size * thresholdPer);
        return table;
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 运行于 2018年12月4日17:39:01
     * <p>
     * 通过key获取hash值。
     * <p>
     * 友情提示：方便用户手动更改hash值，优化存取速度。
     *
     * @param key key
     * @return hash值
     */
    protected int getHash(int key) {
        return key;
    }
    //////////////////////////////////////////////////////////////////////
    /*step 调试相关*/

    /**
     * 运行于 2018年12月4日16:21:51
     * <p>
     * 字符串：map信息 + 数组中的细节信息。
     */
    public String detail() {
        //1、table大小。
        //2、链表内容。
        StringBuilder sSum = new StringBuilder(
                "capacity = " + (maxDex + 1)
                        + ", size = " + size
                        + ", modCount = " + modCount
                        + ", threshold = " + threshold
                        + ", thresholdPer = " + String.format("%.2f", thresholdPer)
                        + LF
        );
        String sLineHead = "下标";
        for (int i = 0; i <= maxDex; i++) {
            if (table[i] == null) {
                continue;
            }
            String sI = detail(i);
            if (i != maxDex) {
                sI += LF;
            }
            sSum.append(sI);
        }

        return sSum.toString();
    }

    /**
     * 运行于 2018年12月4日16:23:16
     * <p>
     * 字符串：数组中，指定下标的信息。
     */
    public String detail(int position) {
        String sLineHead = "下标";
        StringBuilder ss = new StringBuilder(sLineHead + position + "：");
        Node<V> node = table[position];
        if (node != null) {
            do {
                ss.append(node.detail()).append(" ");
                node = node.next;
            } while (node != null);
        } else {
            ss.append("null");
        }
        return ss.toString();
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 运行于 2018年12月5日23:13:36
     * <p>
     * 克隆自己。
     *
     * @return 一模一样的自己
     */
    @SuppressWarnings("unchecked")
    @Override
    public IntMap<V> clone() throws CloneNotSupportedException {
        IntMap<V> result = (IntMap<V>) super.clone();
        result.size = 0;
        result.threshold = 0;
        result.modCount = 0;
        result.entrySet = null;
        result.knownPut(this);
        return result;
//        Node<V>[] table;//存放数据的数组。
//        private int maxDex = -1;//数组最大下标。
//        private int size;//当前数据的数量。
//        private int threshold;//阈值。达到该值扩充数组。
//        private int modCount;//修改次数。
//        private EntrySet entrySet;//键值对
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 运行于 2018年12月4日03:01:25
     * <p>
     * 通过键值放入数据。
     *
     * @param key 键值
     * @param val 数据
     * @return 如果key已存在，则放入新的数据，并返回旧的数据。
     */
    public V put(Integer key, V val) {
        int hash = getHash(key);
        int dex = hash & maxDex;
        Node<V> node = table[dex];
        if (node == null) {
            table[dex] = new Node<>(key, val, hash);
        } else {
            do {
                //链表中已存在该键值的node
                if (node.key == key) {

                    V oldVal = node.val;
                    node.val = val;
                    return oldVal;
                }
                //键值不同时，连接在后。
                else if (node.next == null) {
                    node.next = new Node<>(key, val, hash);
                    break;
                }

                node = node.next;

            } while (true);
        }
        size++;
        modCount++;
        if (size > threshold) {
            doubleCapacity();
        }

        return null;
    }

    /**
     * 运行于 2018年12月4日03:30:01
     * <p>
     * 通过键值获取数据。
     *
     * @param key 键值
     * @return 键值对应的数据
     */
    @Override
    public V get(Object key) {
        Node<V> node = getNode((Integer) key);
        return node != null ? node.val : null;
    }

    /**
     * 运行于 2018年12月5日23:12:45
     * <p>
     * 删除指定键值的数据。
     *
     * @param key 键值
     * @return 键值对应的数据
     */
    @Override
    public V remove(Object key) {
        int hash = getHash((Integer) key);
        int dex = hash & maxDex;
        Node<V>[] table = this.table;
        Node<V> linkedHead = table[dex];
        if (linkedHead == null) {
            return null;
        }
        Node<V> next = linkedHead.next;
        if (next == null) {
            //找到了
            if (linkedHead.key == (Integer) key) {
                table[dex] = null;
                size--;
                modCount++;
                return linkedHead.val;
            } else {
                return null;
            }
        }

        Node<V> node = next;
        Node<V> pre = linkedHead;
        do {
            //找到了
            if (node.key == (Integer) key) {
                V val = node.val;
                pre.next = node.next;
                size--;
                modCount++;
                return val;
            }
            pre = node;
            node = node.next;
        } while (node != null);

        return null;
    }

    /**
     * 运行于 2018年12月4日03:32:39
     * <p>
     * map中是否包含指定键值。
     *
     * @param key 键值
     * @return true：map中有该键值的数据。
     */
    @Override
    public boolean containsKey(Object key) {
        return getNode((Integer) key) != null;
    }

    /**
     * 运行于 2018年12月4日03:34:43
     * <p>
     * 是否包含指定数据。
     *
     * @param value 数据
     * @return true：map中包含指定数据。
     */
    @Override
    public boolean containsValue(Object value) {
        for (int i = 0; i <= maxDex; i++) {
            for (Node<V> node = table[i]; node != null; node = node.next) {
                if (node.val == value) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 运行于 2018年12月4日16:20:22
     * <p>
     * 获取键值对set。
     *
     * @return 键值对set。
     */
    @Override
    public Set<Entry<Integer, V>> entrySet() {
        return entrySet != null ? entrySet : (entrySet = new EntrySet());
    }
    //----------------------------------------------------------------------

    /**
     * 运行于 2018年12月4日16:26:46
     * 当前存放的数据数量。
     *
     * @return 当前存放的数据数量
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * 运行于 2018年12月4日03:43:24
     * <p>
     * 清空数据。
     */
    @Override
    public void clear() {
        if (size != 0) {
            Arrays.fill(table, null);
            size = 0;
            modCount++;
        }
    }

    public void reset() {
        size = 0;
        modCount = 0;
        entrySet = null;
        makeTable(MINIMUM_CAPACITY);
    }

    /**
     * 运行于 2018年12月4日16:27:43
     * <p>
     * 容器是否为空。
     *
     * @return true：容器为空。
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    //----------------------------------------------------------------------

    /**
     * 运行于 2018年12月4日16:23:53
     * <p>
     * 获取指定键值对应的键值对。
     *
     * @param key 键值
     * @return 键值对应的键值对
     */
    private Node<V> getNode(int key) {
        int hash = getHash(key);
        int dex = hash & maxDex;
        for (Node<V> node = table[dex]; node != null; node = node.next) {
            //链表中已存在该键值的node
            if (node.key == key) {
                return node;
            }
        }
        return null;
    }
    //////////////////////////////////////////////////////////////////////

    @SuppressWarnings("TypeParameterHidesVisibleType")
    class Node<V> implements Entry<Integer, V>, Cloneable {
        int key;
        V val;
        int hash;
        Node<V> next;

        Node(int key, V val, int hash) {
            this.key = key;
            this.val = val;
            this.hash = hash;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Node<V> clone() throws CloneNotSupportedException {
            Node<V> result = (Node<V>) super.clone();
            if (next != null) {
                result.next = next.clone();
            }
            return result;
        }
        //////////////////////////////////////////////////////////////////////

        public void putNext(Node<V> node) {
            next = node;
        }

        String detail() {
            String divide = ", ";
            String sKey = "key = " + key;
            String sVal = "val = ";
            String sHash = "hash = " + hash;
            if (val != null) {
                sVal += "have";
            } else {
                sVal += "null";
            }
            //输出样本：[key = 21, val = null, hash = 21]
            return "[" + sKey + divide + sVal + divide + sHash + "]";
        }

        //////////////////////////////////////////////////////////////////////
        @Override
        public Integer getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return val;
        }

        @Override
        public V setValue(V o) {
            throw new IllegalStateException("不可设置我的value。");
        }
    }

    class EntrySet extends AbstractSet<Entry<Integer, V>> {
        @Override
        public Iterator<Entry<Integer, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public int size() {
            return size;
        }
    }

    /**
     * 运行于 2018年12月4日16:32:40
     */
    class EntryIterator implements Iterator<Entry<Integer, V>> {
        int modRecord;
        int currDex;

        Node<V> last;
        Node<V> next;

        public EntryIterator() {
            for (int i = 0; i <= maxDex; i++) {
                Node<V> node = table[i];
                if (node != null) {
                    next = node;
                    currDex = i;
                    break;
                }
            }
            this.modRecord = modCount;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public Entry<Integer, V> next() {
            if (next == null) {
                throw new IllegalStateException("next为null，无法获取。");
            }
            last = next;

            next = next.next;
            if (next == null) {
                for (int i = currDex + 1; i <= maxDex; i++) {
                    Node<V> node = table[i];
                    if (node != null) {
                        next = node;
                        currDex = i;
                        break;
                    }
                }
            }

            return last;
        }

        @Override
        public void remove() {
            if (last == null) {
                throw new IllegalStateException("next为null，无法删除。");
            }
            if (modRecord != modCount) {
                throw new ConcurrentModificationException();
            }
            IntMap.this.remove(last.key);
            last = null;
            modRecord = modCount;
        }
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 运行于 2018年12月4日03:01:38
     */
    @SuppressWarnings({"unchecked", "DanglingJavadoc", "ForLoopReplaceableByForEach"})
    private void doubleCapacity() {
        Node<V>[] oldTable = table;
        int oldSize = oldTable.length;
        //防止超出
        if (oldSize == MAXIMUM_CAPACITY) {
            return;
        }
        int newSize = oldSize * 2;
        Node<V>[] newTable = makeTable(newSize);
        //table最大下标       ：0000 1111······保留key的4位作为下标。
        //table.length（size）：0001 0000······size所在位
        //容量翻倍后的最大下标 ：0001 1111······容量翻倍后，size所在位影响新下标。
        //查看高1位是否有1，有则移动至数组的后半部分。
        for (int i = 0; i < oldSize; i++) {
            Node<V> linkedHead = oldTable[i];
            if (linkedHead == null) {
                continue;
            }
            Node<V> next = linkedHead.next;
            int dexInNew = linkedHead.hash & maxDex;
            newTable[dexInNew] = linkedHead;
            if (next == null) {
                continue;
            }

            //是否是前半部分
            boolean wasFrontPart = dexInNew < oldSize;
            //另一部分的连接处
            Node<V> otherPartBreakNode = null;
            Node<V> node = next;
            Node<V> pre = linkedHead;
            do {
                dexInNew = node.hash & maxDex;
                //当前单向链表在前半部分
                if (wasFrontPart) {

                    //当前节点也在前半部分
                    if (dexInNew < oldSize) {
                        pre = node;
                        node = node.next;
                        continue;
                    }

                }
                //当前单向链表在后半部分
                else {
                    //当前节点也在后半部分
                    if (dexInNew >= oldSize) {
                        pre = node;
                        node = node.next;
                        continue;
                    }
                }
                /**
                 * 转移至另一区域。
                 */

                pre.next = null;
                //另一部分有连接处
                if (otherPartBreakNode != null) {
                    otherPartBreakNode.next = node;
                }
                //没有连接处，直接放入数组。
                else {
                    newTable[dexInNew] = node;
                }
                otherPartBreakNode = pre;
                wasFrontPart = !wasFrontPart;

                pre = node;
                node = node.next;
            } while (node != null);

        }


    }

}

package wclass.HEC;


import wclass.y_marks.Ugly_ToString;

/**
 * @作者 做就行了！
 * @时间 2019-04-30下午 5:57
 * @该类描述： -
 * 1、可复用的数据的存放类。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * 1、2019年4月30日18:01:30。
 * @待解决： -
 */
@SuppressWarnings("WeakerAccess,unused")
public class SimpleRecycler<T> {
    private static final String SIMPLE_NAME = SimpleRecycler.class.getSimpleName();
    private static final String LF = "\n";
    /**
     * 默认的循环池大小。
     */
    private static final int LIMIT_SIZE = 5;
    //////////////////////////////////////////////////
    /**
     * 缓存数据的最大容量。
     */
    private int capacity;

    /**
     * 存放缓存数据的容器。
     */
    private TempBox<T> tempBox;

    /**
     * 没有缓存数据时，该类会从接口中获取一次数据，并返回给用户。
     */
    private DefaultDataGenerator<T> defaultDataGenerator;
    //////////////////////////////////////////////////////////////////////

    /**
     * {@link #SimpleRecycler(int)}
     */
    public SimpleRecycler() {
        this(LIMIT_SIZE);
    }

    /**
     * 构造方法：创建一个有大小限制的循环池。
     *
     * @param capacity 备用item数量的限制大小
     */
    public SimpleRecycler(int capacity) {
        tempBox = new TempBox<>();
        this.capacity = capacity;
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * fix 字符串格式不漂亮。
     */
    @Ugly_ToString
    @Override
    public String toString() {
        return "[#" + SIMPLE_NAME + ": "
                + "int capacity = " + capacity
                + LF + "TempBox<T> tempBox = " + tempBox.toString()
                + " #" + SIMPLE_NAME + "]";
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 设置默认数据的生成器。
     *
     * @param listener 默认数据的生成器。
     */
    public void setDefaultDataGenerator(DefaultDataGenerator<T> listener) {
        if (defaultDataGenerator != null) {
            throw new IllegalStateException("不能重复设置OnCreateListener。");
        }
        defaultDataGenerator = listener;
    }

    /**
     * 默认数据的生成器。
     * 如果该类没有缓存的数据，则从该接口中获取默认的数据。
     *
     * @param <T> 数据类型。
     */
    public interface DefaultDataGenerator<T> {
        T onCreate();
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 执行循环操作。
     *
     * @param item 将被循环的item
     */
    public void recycle(T item) {
        //不能循环操作，则返回。
        if (item == null || !isFree()) {
            return;
        }
        tempBox.put(item);
    }

    /**
     * 获取一个备用item。
     *
     * @return 备用item
     */
    public T get() {
        try {
            //没有，则创建一个。
            if (isEmpty()) {
                return defaultDataGenerator.onCreate();
            }
            //有，则取出一个
            return tempBox.poll();
        } catch (NullPointerException e) {
            throw new IllegalStateException("请调用方法“setDefaultDataGenerator()”。");
        }
    }

    //----------------------------------------------------------------------

    /**
     * {@link  TempBox#recapacity(int)}
     */
    public void recapacity(int capacity) {
        tempBox.recapacity(capacity);
        this.capacity = capacity;
    }

    /**
     * 恢复初始化状态。
     */
    public void clear() {
        tempBox.clear();
    }

    /**
     * 备用item数量是否为空。
     *
     * @return true：数量为空。false：数量不为空。
     */
    public boolean isEmpty() {
        return tempBox.isEmpty();
    }

    /**
     * 当前数量在限制范围内，则可以继续执行循环操作。
     *
     * @return true：可以继续循环操作。false：不能再循环操作了。
     */
    public boolean isFree() {
        return tempBox.size() < capacity;
    }

    /**
     * 获取当前数量。
     */
    public int size() {
        return tempBox.size();
    }

    /**
     * 获取总容量。
     */
    public int capacity() {
        return capacity;
    }

}

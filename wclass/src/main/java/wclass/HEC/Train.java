package wclass.HEC;


import wclass.z_pending_class.data.DataRemovedListener;
import wclass.z_pending_class.data.DataSet;
import wclass.z_pending_class.data.DataGenerator;

/**
 * @作者 做就行了！
 * @时间 2018-11-30上午 12:28
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @noinspection unused
 */
public interface Train<T> extends DataSet<T> {

    /**
     * 添加头。
     */
    void addHead();

    /**
     * 添加尾。
     */
    void addTail();

    /**
     * 添加头。
     *
     * @param count 添加头的次数。
     */
    void addHead(int count);

    /**
     * 添加尾。
     *
     * @param count 添加尾的次数。
     */
    void addTail(int count);

//    /**
//     * 一直添加头，直至不需要再添加头。
//     */
//    default void attemptAddHeads() {
//        while (needAddHead()) {
//            addHead();
//        }
//    }
//
//    /**
//     * 一直添加尾，直至不需要再添加尾。
//     */
//    default void attemptAddTails() {
//        while (needAddTail()) {
//            addTail();
//        }
//    }
//
//    /**
//     * 一直添加头尾，直至不需要再添加头尾。
//     */
//    default void attemptAddRounds() {
//        attemptAddHeads();
//        attemptAddTails();
//    }

    /**
     * 是否可以添加头部数据。
     * <p>
     * 友情提示：只要不超过数据最小下标，就可以添加。
     *
     * @return true：可以添加头部数据。
     */
    boolean canAddHead();

    /**
     * 是否可以添加尾部数据。
     * <p>
     * 友情提示：只要不超过数据最大下标，就可以添加。
     *
     * @return true：可以添加尾部数据。
     */
    boolean canAddTail();

    /**
     * 是否需要添加头部数据。
     *
     * @return true：需要。false：不需要。
     */
    boolean needAddHead();

    /**
     * 是否需要添加尾部数据。
     *
     * @return true：需要。false：不需要。
     */
    boolean needAddTail();

    /**
     * 删除头。
     */
    T removeHead();

    /**
     * 删除尾。
     */
    T removeTail();
    //----------------------------------------------------------------------

    /**
     * @return true：已初始化。false：未初始化。
     */
    boolean isInit();

    /**
     * {@link Train#init(int, DataGenerator)}
     */
    void init(DataGenerator<T> dataGenerator);

    /**
     * 初始化指定位置的数据，并把他作为原点。
     *
     * @param position             以该位置为原点，初始化数据。
     * @param dataGenerator 用于创建、获取指定位置的数据
     */
    void init(int position, DataGenerator<T> dataGenerator);

    void init(int fromPosition, int toPosition, DataGenerator<T> dataGenerator);

    /**
     * {@link Train#reInit(int, DataRemovedListener)}
     */
    void reInit(int position);

    void reInit(int fromPosition, int toPosition);

    /**
     * 初始化指定位置的数据，并把他作为原点。
     *
     * @param position 以该位置为原点，初始化数据。
     */
    void reInit(int position, DataRemovedListener<T> dataRemovedListener);

    void reInit(int fromPosition, int toPosition, DataRemovedListener<T> dataRemovedListener);
}

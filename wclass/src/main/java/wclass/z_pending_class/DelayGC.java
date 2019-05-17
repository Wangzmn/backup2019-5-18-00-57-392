package wclass.z_pending_class;

/**
 * 完成于 2019年2月1日23:08:35
 *
 * @作者 做就行了！
 * @时间 2019-01-31下午 9:48
 * @该类用途： -
 * 1、延迟将持有的“T”赋值为null。
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * 1、通过{@link Object#wait()}实现延迟GC。
 * 当子线程“wait”时，主线程便可拿到“lock”，
 * 通过{@link #GCing}判断是子线程中的哪个“wait”，
 * 之后执行对应的方法。
 * @优化记录： -
 * 【2019年2月1日23:09:09】
 * ①加入DEBUG。
 * ②运行调试完毕。
 * @待解决： -
 */
@SuppressWarnings("ALL")
public abstract class DelayGC<T> {
//    private static final long TEN_MINUTE = 5000;
        private static final long TEN_MINUTE = 60 * 10 * 1000;
    private static final boolean DEBUG = false;
    //----------------------------------------------------------------------
    private final boolean mAutoGC;//是否执行自动GC。
    private Thread thread;//在该线程中处理延迟回收。
    /**
     * 延迟回收的延迟时间。
     */
    private long mDelay;
    /**
     * 保存的对象。
     */
    private T t;

    public DelayGC(long delaySeconds) {
        this(delaySeconds, true);
    }

    /**
     * 构造方法。
     *
     * @param delaySeconds 延迟多少秒执行GC。
     * @param autoGC 是否执行自动GC。
     *               友情提示：每次{@link #get()}后便执行GC。
     */
    public DelayGC(long delaySeconds, boolean autoGC) {
        this.mDelay = delaySeconds * 1000;
        this.mAutoGC = autoGC;
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 没有则创建。
     */
    protected abstract T onCreate();
    //////////////////////////////////////////////////////////////////////
    /*domain 异步相关*/

    private final Object lock = new Object();//锁。
    private boolean GCing;//是否在GC途中。
    private boolean request;//true：有意请求GC。
    /**
     * 处理{@link #t}的策略。
     */
    private Strategy strategy = Strategy.GC;
    //----------------------------------------------------------------------

    /**
     * 运行于 2019年2月2日00:18:02
     * <p>
     * 直接获取。
     */
    public T getDirect() {
        return t;
    }

    /**
     * 运行于 2019年2月2日00:18:02
     * <p>
     * 获取。
     */
    public final T get() {
        synchronized (lock) {
            //自动GC。
            if (mAutoGC) {
                //GC途中。策略设置为：重新计时，唤醒线程。
                if (GCing) {
                    strategy = Strategy.RESTART;
                    lock.notify();
                    if (DEBUG) {
                        System.out.println(" get(),autoGC=true,GCing=true,即将重新执行GC !!! " + "  ");
                    }
                }
                //未执行GC。直接唤醒线程。
                else {
                    if (thread == null) {
                        if (DEBUG) {
                            System.out.println(" get(),autoGC=true,GCing=false,thread==null,即将执行GC !!! " + "  ");
                        }
                        newThread();
                    } else {
                        if (DEBUG) {
                            System.out.println(" get(),autoGC=true,GCing=false,即将执行GC !!! " + "  ");
                        }
                        request = true;
                        lock.notify();
                    }
                }
            }
            //手动GC。
            else {
                //GC途中。策略设置为：取消延迟GC。
                if (GCing) {
                    strategy = Strategy.CANCEL;
                    if (DEBUG) {
                        System.out.println(" get(),autoGC=false,GCing=true,strategy=Strategy.CANCEL !!! " + "  ");
                    }
                }
                //未执行GC。不用管。
                else {
                    if (DEBUG) {
                        System.out.println(" get(),autoGC=false,GCing=false !!! " + "  ");
                    }
                }
            }
            if (t != null) {
                if (DEBUG) {
                    System.out.println(" get(),t!=null,return t !!! " + "  ");
                }
                if (DEBUG) {
                    System.out.println(" -------------------------------------------------- " + "  ");
                }
                return t;
            } else {
                if (DEBUG) {
                    System.out.println(" get(),t==null,return t=onCreate() !!! " + "  ");
                }
                if (DEBUG) {
                    System.out.println(" -------------------------------------------------- " + "  ");
                }
                return t = onCreate();
            }
        }
    }

    /**
     * 运行于 2019年2月2日00:18:02
     * <p>
     * 执行延迟GC。
     * <p>
     * 警告：如果不需要延迟GC，你还用该类干什么？
     */
    public void GC() {
        synchronized (lock) {
            //GC途中。策略设置为：重新计时，并唤醒线程。
            if (GCing) {
                if (DEBUG) {
                    System.out.println(" GC(),GCing=true,即将重新执行GC !!! " + "  ");
                }
                strategy = Strategy.RESTART;
                lock.notify();
            } else {
                //第一次GC。创建thread对象，并启动线程。
                if (thread == null) {
                    if (DEBUG) {
                        System.out.println(" GC(),GCing=false,thread==null,即将执行GC !!! " + "  ");
                    }
                    //step 创建、startThread就行了。
                    newThread();
                }
                //未执行延迟GC。直接唤醒线程，执行延迟、GC。
                else {
                    if (DEBUG) {
                        System.out.println(" GC(),GCing=false,即将执行GC !!! " + "  ");
                    }
                    request = true;
                    lock.notify();
                }
            }
        }
        if (DEBUG) {
            System.out.println(" -------------------------------------------------- " + "  ");
        }
    }

    /**
     * 运行于 2019年2月2日00:18:02
     */
    private void newThread() {
        thread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    while (true) {
                        synchronized (lock) {
                            GCing = true;
                            //step 此时，其他线程可获取lock。
                            if (DEBUG) {
                                System.out.println(" run(),lock.wait(mDelay) !!! " + "  ");
                            }
                            lock.wait(mDelay);

                            switch (strategy) {
                                case GC:
                                    if (DEBUG) {
                                        System.out.println(" run(),case GC: !!! " + "  ");
                                    }
                                    t = null;
                                    break;
                                case RESTART:
                                    if (DEBUG) {
                                        System.out.println(" run(),case RESTART: !!! " + "  ");
                                    }
                                    strategy = Strategy.GC;
                                    continue;
                                case CANCEL:
                                    if (DEBUG) {
                                        System.out.println(" run(),case CANCEL: !!! " + "  ");
                                    }
                                    strategy = Strategy.GC;
                                    break;
                            }

                            GCing = false;
                            //step 此时，其他线程可获取lock。
                            if (DEBUG) {
                                System.out.println(" run(),lock.wait(TEN_MINUTE) !!! " + "  ");
                            }
                            lock.wait(TEN_MINUTE);
                            if (request) {
                                request = false;
                                if (DEBUG) {
                                    System.out.println(" run(),if (request) !!! " + "  ");
                                }
                                if (DEBUG) {
                                    System.out.println(" -------------------------------------------------- " + "  ");
                                }
                                continue;
                            }
                            //走到这里，线程就结束了。
                            thread = null;
                            if (DEBUG) {
                                System.out.println(" run(),Thread正常死亡 !!! " + "  ");
                            }
                            if (DEBUG) {
                                System.out.println(" -------------------------------------------------- " + "  ");
                            }
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        if (DEBUG) {
            System.out.println(" newThread() !!! " + "  ");
        }
        if (DEBUG) {
            System.out.println(" -------------------------------------------------- " + "  ");
        }
    }

    //////////////////////////////////////////////////////////////////////
    enum Strategy {
        GC,//执行回收操作。
        CANCEL,//取消回收操作。
        RESTART,//重新开始计时，再回收。
    }
}

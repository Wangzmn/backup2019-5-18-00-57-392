package wclass.platform.linux.cpu;

import java.util.ArrayList;
import java.util.List;

/**
 * 完成于 2019年2月16日16:14:22
 *
 * @作者 做就行了！
 * @时间 2019-02-16下午 4:14
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "unused"})
public class CpuInfo {
    private static final boolean DEBUG = false;
    /**
     * cpu总个数。
     */
    private final int cpuCount;
    /**
     * 所有cpu的频率总和。
     */
    private int totalMaxHz;
    //----------------------------------------------------------------------
    /**
     * 等待获取的cpus。
     */
    private List<Integer> pendingMaxHzCpu = new ArrayList<>();
    /**
     * 标记 {@link #totalMaxHz}是否获取完毕。
     * 用于优化获取{@link #totalMaxHz}。
     */
    private boolean isTotalMaxHzCompleted;
    //----------------------------------------------------------------------
    private long lastCpuAllTime;
    private long lastCpuIdleValue;
    //----------------------------------------------------------------------
    public String eachCpuCurrMaxHz;

    public CpuInfo() {
        cpuCount = CpuUT.getCpuCount();
        for (int i = 0; i < cpuCount; i++) {
            pendingMaxHzCpu.add(i);
        }
    }
    //////////////////////////////////////////////////////////////////////
    /*step 确定*/

    /**
     * 检查于 2019年1月27日18:21:11
     * <p>
     * 获取使用率。
     *
     * 警告：返回值最大为“99”。
     */
    public int getUsageRatePercentage() {
        List<String> list = CpuUT.getCpuTotalInfo();
        long currCpuIdle = getCpuIdleTime(list);//当前idle
        long currCpuAllTime = getCpuAllTime(list);//当前时间

        long idleCut = currCpuIdle - lastCpuIdleValue;//与上一次的idle差
        float allTimeCut = currCpuAllTime - lastCpuAllTime;//所有cpu经过的时间

        //这里最低1%空闲，也就是最高99%使用率。
        //“100%”太占空间了。
        float per = Math.min(Math.max(0.01f, idleCut / allTimeCut), 1);

        lastCpuIdleValue = currCpuIdle;
        lastCpuAllTime = currCpuAllTime;

        if(DEBUG){
            String msg = "  " + "  " + " (int) ((1 - per) * 100) = " + (int) ((1 - per) * 100);
            System.out.println(msg);
        }
        return (int) ((1 - per) * 100);
    }

    /**
     * 运行于 2019年2月16日00:27:33
     * <p>
     * 获取开核率。
     */
    public int getReadyRatePercentage() {
        float curTotalHzs = 0;
        for (int i = 0; i < cpuCount; i++) {
            curTotalHzs += CpuUT.getCurHzFor(i);
        }
        if(DEBUG){
            String msg1 = " curTotalHzs = " + curTotalHzs;
            String msg2 = " totalMaxHz = " + totalMaxHz;
            String msg3 = " curTotalHzs / totalMaxHz * 100 = " + (curTotalHzs / totalMaxHz * 100);
            System.out.println(msg1);
            System.out.println(msg2);
            System.out.println(msg3);
        }
        return (int) (curTotalHzs / getTotalMaxHz() * 100);
    }

    /**
     * 运行于 2019年2月16日00:27:33
     * <p>
     * 获取所有CPU的总频率。
     * <p>
     * 警告：只能获取工作时的CPU的总频率，
     * 只调用一次该方法时，获取不准确。
     * 想得到准确的数值，必须持续获取。
     *
     * @return 所有CPU的总频率。
     */
    public final int getTotalMaxHz() {
        //可以优化获取。
        if (isTotalMaxHzCompleted) {
            return totalMaxHz;
        }
        //遍历未成功获取的cpu。
        int size = pendingMaxHzCpu.size();
        for (int i = size - 1; i >= 0; i--) {
            int hz = CpuUT.getMaxHzFor(pendingMaxHzCpu.get(i));
            //成功获取。
            if (hz != 0) {
                totalMaxHz += hz;
                pendingMaxHzCpu.remove(i);
            }
        }
        //所有cpu都获取成功时，标记优化。
        if (pendingMaxHzCpu.size() == 0) {
            isTotalMaxHzCompleted = true;
        }
        return totalMaxHz;
    }

    /**
     * 获取cpu总个数。
     */
    public final int getCpuCount() {
        return cpuCount;
    }
    //////////////////////////////////////////////////////////////////////

    /**
     * 获得cpu空闲时间
     */
    private long getCpuIdleTime(List<String> list) {
        return Long.parseLong(list.get(4));
    }

    /**
     * 获得cpu工作总时间
     */
    private long getCpuAllTime(List<String> list) {
        long time = 0;
        //0下标为“cpu[0-9]”。
        for (int i = 1; i < list.size(); i++) {
            time += Long.parseLong(list.get(i));
        }
        return time;
    }

}

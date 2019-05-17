package wclass.platform.linux.cpu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import wclass.y_marks.Study;

/**
 * 完成于 2019年2月16日16:14:06
 * @作者 做就行了！
 * @时间 2019-02-16下午 4:14
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings({"WeakerAccess", "Convert2Lambda", "unused"})
public class CpuUT {
    /**
     * 指定的cpu最大频率目录。
     */
    public static final String MAX_FREQ = "cpuinfo_max_freq";

    /**
     * 指定的cpu最小频率目录。
     */
    public static final String MIN_FREQ = "cpuinfo_min_freq";

    /**
     * 指定的cpu当前频率目录。
     */
    public static final String CUR_FREQ = "scaling_cur_freq";
    //////////////////////////////////////////////////////////////////////

    /**
     * 检查于 2019年1月28日00:22:48
     * <p>
     * 获取cpu的数量。
     */
    public static int getCpuCount() {
        try {
            //指定目录下
            File dir = new File("/sys/devices/system/cpu/");
            //符合的子目录数量
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                @Study
                public boolean accept(File pathname) {
                    return Pattern.matches("cpu[0-9]", pathname.getName());//匹配cpu0-9;
                }
            });
            return files.length;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;//默认返回1个
        }
    }

    /**
     * 检查于 2019年1月28日00:22:48
     * <p>
     * 获取CPU的最大频率（单位KHZ）。
     * <p>
     * 缺陷：只能获得工作中的核心的频率。
     */
    public static int getMaxHzFor(int cpuNum) {
        return getTypeHzFor(cpuNum, MAX_FREQ);
    }

    /**
     * 检查于 2019年1月28日00:22:48
     * <p>
     * 获取CPU的最小频率（单位KHZ）。
     * <p>
     * 缺陷：只能获得工作中的核心的频率。
     */
    public static int getMinHzFor(int cpuNum) {
        return getTypeHzFor(cpuNum, MIN_FREQ);
    }

    /**
     * 运行于 2019年2月16日00:29:27
     * <p>
     * 获取cpu的当前频率（单位KHZ）。
     * <p>
     * 缺陷：只能获得工作中的核心的频率。
     */
    public static int getCurHzFor(int cpuNum) {
        return getTypeHzFor(cpuNum, CUR_FREQ);
    }

    /**
     * 运行于 2019年2月16日00:29:27
     * <p>
     * 获取指定类型的cpu频率。
     */
    public static int getTypeHzFor(int num, String type) {
        String result = "0";
        String path = "/sys/devices/system/cpu/cpu" + num + "/cpufreq/" + type;
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            result = text.trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(result);
    }

    /**
     * 检查于 2019年1月28日00:20:09
     * <p>
     * 获取最详细的cpu信息。
     * <p>
     * 0下标：cpu的总信息。
     * 其他下标：运行中的cpu信息。
     */
    public static List<List<String>> getCpusInfo() {
        FileReader fr = null;
        BufferedReader buffer = null;
        List<List<String>> allInfo = new ArrayList<>();
        try {
            fr = new FileReader("/proc/stat");
            buffer = new BufferedReader(fr);
            String line;
            while (true) {
                line = buffer.readLine();
                //读到数据，并且已“cpu”开头。
                if (line != null && line.startsWith("cpu")) {
                    List<String> info = new ArrayList<>();
                    @Study
                    StringTokenizer tokenizer = new StringTokenizer(line);//将信息放入集合类
                    //行集合类 添加 每个字段。
                    while (tokenizer.hasMoreElements()) {
                        String value = tokenizer.nextToken();
                        info.add(value);
                    }
                    //总集合类 添加 行集合类。
                    allInfo.add(info);
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (buffer != null) {
                    buffer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return allInfo;
    }

    /**
     * 检查于 2019年1月28日00:19:13
     * <p>
     * 获取cpu的总信息。
     */
    public static List<String> getCpuTotalInfo() {
        FileReader fr = null;
        BufferedReader buffer = null;
        List<String> sections = new ArrayList<>();
        try {
            fr = new FileReader("/proc/stat");
            buffer = new BufferedReader(fr);
            String lineStr;
            lineStr = buffer.readLine();
            //读到数据，并且已“cpu”开头。
            if (lineStr != null && lineStr.startsWith("cpu")) {
                @Study
                StringTokenizer tokenizer = new StringTokenizer(lineStr);//将信息放入集合类
                while (tokenizer.hasMoreElements()) {
                    String section = tokenizer.nextToken();
                    sections.add(section);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (buffer != null) {
                    buffer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sections;
    }
}

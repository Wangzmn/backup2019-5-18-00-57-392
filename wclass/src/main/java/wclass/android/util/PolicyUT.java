package wclass.android.util;

import android.app.admin.DevicePolicyManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import java.util.List;

/**
 * @作者 做就行了！
 * @时间 2019-02-24上午 12:18
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
public class PolicyUT {
    /**
     * 判断 “设备管理权限” 是否开启
     */
    public static boolean isON_device(Context context,String className) {
        DevicePolicyManager policyManager;
        ComponentName componentName;
        //获得 设备管理 权限
        policyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        //获得目标类的 组件名称
        componentName = new ComponentName(context.getApplicationContext(),className);
        // 判断该组件是否有 设备管理 的权限
        return policyManager != null && policyManager.isAdminActive(componentName);
    }

    /**
     * 判断 “最上层显示权限” 是否开启
     * 小于23不用判断
     */
    public static boolean isON_overlay(Context context) {
        return !(Build.VERSION.SDK_INT >= 23 &&
                !Settings.canDrawOverlays(context.getApplicationContext()));
    }
    /**
     * 判断 “有权查看使用权限的应用” 是否开启
     * 小于22不用判断
     */
    public static boolean checkUserInfo(Context context) {
        long ts = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = null;
        //22
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            usageStatsManager = (UsageStatsManager) context.getApplicationContext()
                    .getSystemService(Context.USAGE_STATS_SERVICE);
        }
        List<UsageStats> queryUsageStats = null;
        //21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (usageStatsManager != null) {
                queryUsageStats = usageStatsManager.queryUsageStats(
                        UsageStatsManager.INTERVAL_BEST, 0, ts);
            }
        }
        return !(queryUsageStats == null || queryUsageStats.isEmpty());
    }
}


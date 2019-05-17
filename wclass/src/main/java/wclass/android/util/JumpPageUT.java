package wclass.android.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @作者 做就行了！
 * @时间 2019-02-15上午 11:32
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("unused")
public class JumpPageUT {

    private static final int BEFORE_TIME = 60;

    /**
     * 启动至 “最上层显示权限” 界面。
     *
     * @param activity 启动界面时，所在的activity。
     * @param request_code 返回activity时，收到的请求码。
     */
    public static void jumpToOverlayPage(Activity activity, int request_code) {
        if (Build.VERSION.SDK_INT >= 23) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + activity.getApplicationContext().getPackageName()));
            try {
                activity.startActivityForResult(intent, request_code);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 启动至 “用户信息查看权” 界面
     */
    public static void jumpToUserInfoPage(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//Ver 21
            try {
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
//                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS,
//                        Uri.parse("package:" + activity.
//                                getApplicationContext().getPackageName()));
                activity.startActivityForResult
                        (intent, requestCode);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 启动上一个app。
     *
     * @param context 上下文。
     */
    public static void jumpToLastActivity(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            jumpToLastActivity_forHighVersion(context);
        } else {
            jumpToLastActivity_forLowVersion(context);
        }
    }

    /**
     * 启动上一个app。
     * <p>
     * 友情提示：打开状态栏，有时状态栏中的App也算上一个任务。
     * <p>
     * 警告：
     * 1、安卓4.0及以下可使用该方法。
     * 2、请使用application级别的上下文，此时用的是系统级别的app栈。
     *
     * @param context 上下文。
     */
    public static void jumpToLastActivity_forLowVersion(Context context) {
        try {
            ActivityManager activityManager =
                    (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            assert activityManager != null;
            List<ActivityManager.RecentTaskInfo> appTask =
                    activityManager.getRecentTasks(2, 1);
            Intent intent = appTask.get(1).baseIntent;
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动上一个app。
     * <p>
     * 友情提示：打开状态栏，有时状态栏中的App也算上一个任务。
     * <p>
     * 警告：
     * 1、安卓5.0及以上才能使用该方法。
     * 2、请使用application级别的上下文，此时用的是系统级别的app栈。
     *
     * @param context 上下文。
     */
    @SuppressLint("NewApi")
    public static void jumpToLastActivity_forHighVersion(Context context) {
        UsageStatsManager statsManager =
                (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        if (statsManager == null) return;

        long now = System.currentTimeMillis();
        //获取现在 至 多少秒之前，这段时间的状态。
        List<UsageStats> stats = statsManager.queryUsageStats
                (UsageStatsManager.INTERVAL_BEST, now - BEFORE_TIME * 1000, now);

        if (stats == null || stats.isEmpty()) return;
        //获得最近的两个APP
        int first = 0;
        int second = 0;
        int size = stats.size();
        for (int i = 1; i < size; i++) {
            long lastTimeUsed = stats.get(i).getLastTimeUsed();
            if (lastTimeUsed
                    > stats.get(first).getLastTimeUsed()) {
                second = first;
                first = i;
            } else if (lastTimeUsed
                    > stats.get(second).getLastTimeUsed()) {
                second = i;
            }
        }
        if (first == second) {
            return;
        }
        jumpToActivity(context, stats.get(second)
                .getPackageName());
    }

    @SuppressWarnings("ConstantConditions")
    public static void jumpToActivity(Context c, String activity) {
        try {
            Intent intent = c.getPackageManager().getLaunchIntentForPackage(activity);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            c.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

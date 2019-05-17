package wclass.android.z_pending_class;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import wclass.android.app.AppInfo;

/**
 * @作者 做就行了！
 * @时间 2019-03-18下午 2:59
 * @该类描述： -
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("WeakerAccess")
public class AppQueryUT {

    /**
     * 查询可启动的activity包名。
     */
    public static List<ResolveInfo> getAppResolveInfos(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        return context.getPackageManager()
                .queryIntentActivities(intent, 0);
    }

    /**
     * {@link #getAppInfos(Context, List)}
     */
    public static List<AppInfo> getAppInfos(Context context) {
        List<AppInfo> appInfos = new ArrayList<>();
        getAppInfos(context, appInfos);
        return appInfos;
    }

    /**
     * 获取App的所有信息。
     *
     * @param context 上下文。
     * @return App的所有信息。
     */
    public static void getAppInfos(Context context, List<AppInfo> appInfos) {
        if (appInfos == null) {
            appInfos = new ArrayList<>();
        }
        List<ResolveInfo> l = AppQueryUT.getAppResolveInfos(context);
        PackageManager pm = context.getPackageManager();
        for (int i = 0; i < l.size(); i++) {
            ResolveInfo ri = l.get(i);
            String pkName = ri.activityInfo.packageName;
            CharSequence appName = ri.loadLabel(pm);
            String appNameStr = null;
            if (appName != null) {
                appNameStr = appName.toString();
            }
            Drawable icon = ri.loadIcon(pm);
            appInfos.add(new AppInfo(pkName, appNameStr, icon));
        }
    }
}

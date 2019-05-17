package wclass.android.app;

import android.graphics.drawable.Drawable;

/**
 * @作者 做就行了！
 * @时间 2019-02-08下午 11:50
 * @该类用途： -
 * 1、存储App的基本信息（包名、应用名、图标）。
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("WeakerAccess")
public class AppInfo {
    public String pkName;//包名
    public String appName;//应用名
    public Drawable icon;//图标

    public AppInfo(String pkName, String appName, Drawable icon) {
        this.pkName = pkName;
        this.appName = appName;
        this.icon = icon;
    }
}
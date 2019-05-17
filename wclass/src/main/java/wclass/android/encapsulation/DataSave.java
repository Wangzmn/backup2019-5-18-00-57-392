package wclass.android.encapsulation;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @作者 做就行了！
 * @时间 2019-03-18下午 11:09
 * @该类描述： -
 * 1、该类为：对{@link SharedPreferences}简单的封装。
 * @名词解释： -
 * @该类用途： -
 * @注意事项： -
 * @使用说明： -
 * @思维逻辑： -
 * @优化记录： -
 * @待解决： -
 */
@SuppressWarnings("unused")
public class DataSave {
    private final SharedPreferences sharedPreferences;

    /**
     * 构造方法。
     *
     * @param context   上下文。
     * @param tableName 保存数据的表名。
     */
    public DataSave(Context context, String tableName) {
        sharedPreferences = context.getSharedPreferences(tableName, Context.MODE_PRIVATE);
    }

    //////////////////////////////////////////////////////////////////////
    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value).apply();
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value).apply();
    }

    public void putFloat(String key, float value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value).apply();
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value).apply();
    }

    //////////////////////////////////////////////////////////////////////
    public String getString(String key, String defaultValue) {
        try {
            return sharedPreferences.getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            remove(key);
            return defaultValue;
        }
    }

    public int getInt(String key, int defaultValue) {
        try {
            return sharedPreferences.getInt(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            remove(key);
            return defaultValue;
        }
    }

    public float getFloat(String key, float defaultValue) {
        try {
            return sharedPreferences.getFloat(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            remove(key);
            return defaultValue;
        }
    }

    public long getLong(String key, long defaultValue) {
        try {
            return sharedPreferences.getLong(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            remove(key);
            return defaultValue;
        }
    }

    //////////////////////////////////////////////////////////////////////
    public void remove(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key).apply();
    }

    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }
}

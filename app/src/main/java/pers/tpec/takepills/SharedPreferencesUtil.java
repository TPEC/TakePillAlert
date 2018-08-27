package pers.tpec.takepills;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

public class SharedPreferencesUtil {
    private static String SP_NAME = "TakePills";

    public static void setSpName(final String spName) {
        SP_NAME = spName;
    }

    public static void save(Context context, String name, Map<String, ?> data) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        for (Map.Entry<String, ?> entry : data.entrySet()) {
            if (entry.getValue() instanceof Integer) {
                editor.putInt(entry.getKey(), (Integer) entry.getValue());
            } else if (entry.getValue() instanceof Long) {
                editor.putLong(entry.getKey(), (Long) entry.getValue());
            } else if (entry.getValue() instanceof Float) {
                editor.putFloat(entry.getKey(), (Float) entry.getValue());
            } else if (entry.getValue() instanceof Boolean) {
                editor.putBoolean(entry.getKey(), (Boolean) entry.getValue());
            } else if (entry.getValue() instanceof String) {
                editor.putString(entry.getKey(), (String) entry.getValue());
            }
        }
        editor.apply();
    }

    public static void save(Context context, Map<String, ?> data) {
        save(context, SP_NAME, data);
    }

    public static Map<String, ?> loadAll(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getAll();
    }

    public static Map<String, ?> loadAll(Context context) {
        return loadAll(context, SP_NAME);
    }

    public static SharedPreferences getSP(Context context, String name) {
        return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getSP(Context context) {
        return getSP(context, SP_NAME);
    }

    public static void clear(Context context, String name) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    public static void clear(Context context) {
        clear(context, SP_NAME);
    }
}

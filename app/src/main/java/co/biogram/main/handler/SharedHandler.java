package co.biogram.main.handler;

import android.content.Context;
import android.content.SharedPreferences;

import co.biogram.main.App;

public class SharedHandler
{
    private static final String PreferenceFile = "BioGram";
    private static SharedPreferences Shared;

    public static void SetLong(String Key, Long Value)
    {
        Shared = App.GetContext().getSharedPreferences(PreferenceFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor Editor = Shared.edit();
        Editor.putLong(Key, Value);
        Editor.apply();
    }

    public static Long GetLong(String Key)
    {
        Shared = App.GetContext().getSharedPreferences(PreferenceFile, Context.MODE_PRIVATE);
        return Shared.getLong(Key, 0L);
    }

    public static void SetBoolean(String Key, boolean Value)
    {
        Shared = App.GetContext().getSharedPreferences(PreferenceFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor Editor = Shared.edit();
        Editor.putBoolean(Key, Value);
        Editor.apply();
    }

    public static boolean GetBoolean(String Key)
    {
        Shared = App.GetContext().getSharedPreferences(PreferenceFile, Context.MODE_PRIVATE);
        return Shared.getBoolean(Key, false);
    }

    public static void SetString(String Key, String Value)
    {
        Shared = App.GetContext().getSharedPreferences(PreferenceFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor Editor = Shared.edit();
        Editor.putString(Key, Value);
        Editor.apply();
    }

    public static String GetString(String Key)
    {
        Shared = App.GetContext().getSharedPreferences(PreferenceFile, Context.MODE_PRIVATE);
        return Shared.getString(Key, "");
    }

    public static void SetInt(String Key, int Value)
    {
        Shared = App.GetContext().getSharedPreferences(PreferenceFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor Editor = Shared.edit();
        Editor.putInt(Key, Value);
        Editor.apply();
    }

    public static int GetInt(String Key)
    {
        Shared = App.GetContext().getSharedPreferences(PreferenceFile, Context.MODE_PRIVATE);
        return Shared.getInt(Key, 0);
    }
}

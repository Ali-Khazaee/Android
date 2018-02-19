package co.biogram.main.handler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedHandler
{
    @SuppressLint("StaticFieldLeak")
    private static volatile Context context;

    public static void SetUp(Context c)
    {
        context = c;
    }

    public static void SetString(String Key, String Value)
    {
        SharedPreferences Shared = context.getSharedPreferences("BioGram", Context.MODE_PRIVATE);
        SharedPreferences.Editor Editor = Shared.edit();
        Editor.putString(Key, Value);
        Editor.apply();
    }

    public static String GetString(String Key)
    {
        SharedPreferences Shared = context.getSharedPreferences("BioGram", Context.MODE_PRIVATE);
        return Shared.getString(Key, "");
    }

    public static String GetString(String Key, String Default)
    {
        SharedPreferences Shared = context.getSharedPreferences("BioGram", Context.MODE_PRIVATE);
        return Shared.getString(Key, Default);
    }

    public static void SetBoolean(String Key, boolean Value)
    {
        SharedPreferences Shared = context.getSharedPreferences("BioGram", Context.MODE_PRIVATE);
        SharedPreferences.Editor Editor = Shared.edit();
        Editor.putBoolean(Key, Value);
        Editor.apply();
    }

    public static boolean GetBoolean(String Key)
    {
        SharedPreferences Shared = context.getSharedPreferences("BioGram", Context.MODE_PRIVATE);
        return Shared.getBoolean(Key, false);
    }
}

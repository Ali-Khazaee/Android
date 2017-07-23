package co.biogram.main.handler;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedHandler
{
    public static void SetString(Context context, String Key, String Value)
    {
        SharedPreferences Shared = context.getSharedPreferences("BioGram", Context.MODE_PRIVATE);
        SharedPreferences.Editor Editor = Shared.edit();
        Editor.putString(Key, Value);
        Editor.apply();
    }

    public static String GetString(Context context, String Key)
    {
        SharedPreferences Shared = context.getSharedPreferences("BioGram", Context.MODE_PRIVATE);
        return Shared.getString(Key, "");
    }

    public static void SetBoolean(Context context, String Key, boolean Value)
    {
        SharedPreferences Shared = context.getSharedPreferences("BioGram", Context.MODE_PRIVATE);
        SharedPreferences.Editor Editor = Shared.edit();
        Editor.putBoolean(Key, Value);
        Editor.apply();
    }

    public static boolean GetBoolean(Context context, String Key)
    {
        SharedPreferences Shared = context.getSharedPreferences("BioGram", Context.MODE_PRIVATE);
        return Shared.getBoolean(Key, false);
    }
}

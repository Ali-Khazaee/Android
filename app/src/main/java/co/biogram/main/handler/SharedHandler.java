package co.biogram.main.handler;

import android.content.Context;
import android.content.SharedPreferences;

import co.biogram.main.App;

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

    public static void SetString(Context context, String Key, String Value, String File)
    {
        SharedPreferences Shared = context.getSharedPreferences(File, Context.MODE_PRIVATE);
        SharedPreferences.Editor Editor = Shared.edit();
        Editor.putString(Key, Value);
        Editor.apply();
    }

    public static String GetString(Context context, String Key, String File)
    {
        SharedPreferences Shared = context.getSharedPreferences(File, Context.MODE_PRIVATE);
        return Shared.getString(Key, "");
    }

    public static void SetBoolean(Context context, String Key, boolean Value)
    {
        SharedPreferences Shared = App.GetContext().getSharedPreferences("BioGram", Context.MODE_PRIVATE);
        SharedPreferences.Editor Editor = Shared.edit();
        Editor.putBoolean(Key, Value);
        Editor.apply();
    }

    public static boolean GetBoolean(Context context, String Key)
    {
        SharedPreferences Shared = App.GetContext().getSharedPreferences("BioGram", Context.MODE_PRIVATE);
        return Shared.getBoolean(Key, false);
    }

    public static void SetBoolean(Context context, String Key, boolean Value, String File)
    {
        SharedPreferences Shared = App.GetContext().getSharedPreferences(File, Context.MODE_PRIVATE);
        SharedPreferences.Editor Editor = Shared.edit();
        Editor.putBoolean(Key, Value);
        Editor.apply();
    }

    public static boolean GetBoolean(Context context, String Key, String File)
    {
        SharedPreferences Shared = App.GetContext().getSharedPreferences(File, Context.MODE_PRIVATE);
        return Shared.getBoolean(Key, false);
    }













    public static void SetBoolean(String Key, boolean Value)
    {
        SharedPreferences Shared = App.GetContext().getSharedPreferences("BioGram", Context.MODE_PRIVATE);
        SharedPreferences.Editor Editor = Shared.edit();
        Editor.putBoolean(Key, Value);
        Editor.apply();
    }

    public static boolean GetBoolean(String Key)
    {
        SharedPreferences Shared = App.GetContext().getSharedPreferences("BioGram", Context.MODE_PRIVATE);
        return Shared.getBoolean(Key, false);
    }

    public static void SetString(String Key, String Value)
    {
        SharedPreferences Shared = App.GetContext().getSharedPreferences("BioGram", Context.MODE_PRIVATE);
        SharedPreferences.Editor Editor = Shared.edit();
        Editor.putString(Key, Value);
        Editor.apply();
    }

    public static String GetString(String Key)
    {
        SharedPreferences Shared = App.GetContext().getSharedPreferences("BioGram", Context.MODE_PRIVATE);
        return Shared.getString(Key, "");
    }
}

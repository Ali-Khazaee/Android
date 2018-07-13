package co.biogram.main.handler;

import android.util.Log;

public class Analyze
{
    public static void Log(String Tag, Exception e)
    {
        String Message = (">> Packet\n");
        Message += (e.getMessage());
        Message += (">> Stack Trace\n");
        Message += (Log.getStackTraceString(e));
        Message += ("\n");

        Log.e(Tag, Message);
    }

    public static void Log(String Tag, String Message)
    {
        Log.e(Tag, Message);
    }

    public static void Debug(String Tag, String Message)
    {
        Log.e(Tag, Message);
    }
}

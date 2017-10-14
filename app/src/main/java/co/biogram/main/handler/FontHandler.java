package co.biogram.main.handler;

import android.content.Context;
import android.graphics.Typeface;

public class FontHandler
{
    private static Typeface TypeFontCache;

    public static Typeface GetTypeface(Context context)
    {
        if (TypeFontCache == null)
            TypeFontCache = Typeface.createFromAsset(context.getAssets(), "iran-sans.ttf");

        return TypeFontCache;
    }
}
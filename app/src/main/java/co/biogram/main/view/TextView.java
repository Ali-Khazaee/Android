package co.biogram.main.view;

import android.content.Context;
import android.graphics.Typeface;

import co.biogram.main.handler.FontHandler;
import co.biogram.main.handler.MiscHandler;

public class TextView extends android.widget.TextView
{
    private TextView(Context context)
    {
        super(context);
    }

    public TextView(Context context, boolean IsBold)
    {
        this(context);

        if (MiscHandler.IsFa())
        {
            if (IsBold)
                setTypeface(FontHandler.GetTypeface(context), Typeface.BOLD);
            else
                setTypeface(FontHandler.GetTypeface(context));
        }
        else if (IsBold)
            setTypeface(null, Typeface.BOLD);
    }
}

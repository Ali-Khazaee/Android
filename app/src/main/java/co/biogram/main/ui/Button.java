package co.biogram.main.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import co.biogram.main.handler.FontHandler;
import co.biogram.main.handler.MiscHandler;

public class Button extends android.widget.Button
{
    private Button(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public Button(Context context, boolean IsBold)
    {
        this(context, null, android.R.attr.borderlessButtonStyle);

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

package co.biogram.main.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

import co.biogram.main.R;
import co.biogram.main.handler.FontHandler;

public class TextView extends android.widget.TextView
{
    private TextView(Context context)
    {
        super(context);
    }

    public TextView(Context context, float size, boolean isBold)
    {
        this(context);

        if (isBold)
            setTypeface(FontHandler.GetTypeface(context), Typeface.BOLD);
        else
            setTypeface(FontHandler.GetTypeface(context));

        setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        setTextColor(ContextCompat.getColor(context, R.color.White));
    }

    private void setTextSize(int unit, float size) { }
}

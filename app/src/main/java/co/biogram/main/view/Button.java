package co.biogram.main.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;

import co.biogram.main.R;
import co.biogram.main.handler.FontHandler;
import co.biogram.main.handler.MiscHandler;

public class Button extends android.widget.Button
{
    private Button(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public Button(Context context, float size, boolean isBold)
    {
        this(context, null, android.R.attr.borderlessButtonStyle);

        if (isBold)
            setTypeface(FontHandler.GetTypeface(context), Typeface.BOLD);
        else
            setTypeface(FontHandler.GetTypeface(context));

        setAllCaps(false);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        setTextColor(ContextCompat.getColor(context, R.color.White));
    }

    private void setAllCaps(boolean allCaps) { }
    private void setTextSize(int unit, float size) { }
}
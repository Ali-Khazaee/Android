package co.biogram.main.ui.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;

import co.biogram.main.R;
import co.biogram.main.handler.Misc;

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
            setTypeface(Misc.GetTypeface(), Typeface.BOLD);
        else
            setTypeface(Misc.GetTypeface());

        setAllCaps(false);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        setTextColor(ContextCompat.getColor(context, R.color.TextDark));
        setPadding(0, Misc.IsFa() ? 0 : Misc.ToDP(3), 0, 0);
    }
}

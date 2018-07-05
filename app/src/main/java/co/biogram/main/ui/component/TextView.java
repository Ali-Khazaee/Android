package co.biogram.main.ui.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;

import co.biogram.main.handler.Misc;

@SuppressLint("AppCompatCustomView")
public class TextView extends android.widget.TextView
{
    public TextView(Context context)
    {
        this(context, null, 0);
    }

    public TextView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public TextView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setTypeface(Misc.GetTypeface());
    }
}

package co.biogram.main.misc;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class StatefulLayout extends RelativeLayout
{
    public StatefulLayout(Context context)
    {
        this(context, null, 0);
    }

    public StatefulLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public StatefulLayout(Context context, AttributeSet attrs, int style)
    {
        super(context, attrs, style);
    }

    public void ShowLoading()
    {
        removeAllViews();
    }

    public void ShowEmpty()
    {
        removeAllViews();
    }

    public void ShowOffline()
    {
        removeAllViews();
    }

    public void ShowTryAgain()
    {
        removeAllViews();
    }

    public void ShowNoPermission()
    {
        removeAllViews();
    }
}

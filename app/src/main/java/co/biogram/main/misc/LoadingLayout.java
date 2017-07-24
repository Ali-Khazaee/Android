package co.biogram.main.misc;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class LoadingLayout extends RelativeLayout
{
    public LoadingLayout(Context context)
    {
        this(context, null, 0);
    }

    public LoadingLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int style)
    {
        super(context, attrs, style);
    }

    public void ShowLoading()
    {

    }

    public void ShowEmpty()
    {

    }

    public void ShowOffline()
    {

    }

    public void ShowTryAgain()
    {

    }
}

package co.biogram.main.handler;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class BaseView
{
    private View Base;
    private Bundle Arguments;

    public BaseView()
    {

    }

    public BaseView(Bundle args)
    {
        Arguments = args;
    }

    public View GetView()
    {
        return Base;
    }

    public View CreateView(Context context)
    {
        return null;
    }

    public void OnResume()
    {

    }

    public void OnPause()
    {

    }

    public boolean OnBackPressed()
    {
        return true;
    }
}

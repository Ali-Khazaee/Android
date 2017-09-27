package co.biogram.fragment;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import co.biogram.main.handler.MiscHandler;

public class FragmentBase
{
    private FragmentActivity activity;
    private View ViewMain;

    protected void SetRootView(View view)
    {
        ViewMain = view;
    }

    View GetView()
    {
        return ViewMain;
    }

    void SetActivity(FragmentActivity a)
    {
        activity = a;
    }

    protected FragmentActivity GetActivity()
    {
        return activity;
    }

    public void OnCreate() { }

    public void OnResume() { }

    public void OnPause() { }

    void OnDestroy()
    {
        if (ViewMain != null)
        {
            ViewGroup Parent = (ViewGroup) ViewMain.getParent();

            if (Parent != null)
            {
                try
                {
                    Parent.removeView(ViewMain);
                }
                catch (Exception e)
                {
                    MiscHandler.Debug("FragmentBase-OnDestroy: " + e.toString());
                }
            }
        }

        ViewMain = null;
    }

    public void OnActivityResult(int RequestCode, int ResultCode, Intent intent) { }
}

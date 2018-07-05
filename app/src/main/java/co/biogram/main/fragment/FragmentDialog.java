package co.biogram.main.fragment;

import android.view.View;
import android.view.ViewGroup;

import co.biogram.main.handler.Misc;

public abstract class FragmentDialog
{
    public FragmentActivity Activity;
    public View ViewMain;

    public abstract void OnCreate();

    public void OnDestroy()
    {
        if (ViewMain != null)
        {
            ViewGroup Parent = (ViewGroup) ViewMain.getParent();

            if (Parent != null)
                Parent.removeView(ViewMain);

            ViewMain = null;
        }
    }
}

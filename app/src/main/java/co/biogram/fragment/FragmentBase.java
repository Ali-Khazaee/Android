package co.biogram.fragment;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

public class FragmentBase
{
    FragmentActivity Activity;
    protected View ViewMain;
    protected int D56;
    String Tag;

    public FragmentActivity GetActivity()
    {
        return Activity;
    }

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

    public void OnCreate() { }

    public void OnResume() { }

    public void OnPause() { }

    public void OnActivityResult(int RequestCode, int ResultCode, Intent intent) { }
}

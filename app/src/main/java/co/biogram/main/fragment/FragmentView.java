package co.biogram.main.fragment;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

public class FragmentView
{
    FragmentActivity Activity;
    public View ViewMain;
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

    public void OnOpen() { }

    public void OnCreate() { }

    public void OnResume() { }

    public void OnPause() { }

    public void OnActivityResult(int RequestCode, int ResultCode, Intent intent) { }
}

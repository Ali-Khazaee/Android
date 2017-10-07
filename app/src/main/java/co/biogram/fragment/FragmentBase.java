package co.biogram.fragment;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

public class FragmentBase
{
    protected FragmentActivity Activity;
    protected View ViewMain;
    protected String Tag;

    protected FragmentActivity GetActivity()
    {
        return Activity;
    }

    void OnDestroy()
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

    public void OnPermissionResult(int RequestCode, String[] Permissions, int[] GrantResults) { }
}

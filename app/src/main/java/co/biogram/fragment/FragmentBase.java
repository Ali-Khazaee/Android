package co.biogram.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class FragmentBase
{
    private Activity activity;
    private View ViewMain;

    protected void SetRootView(View view)
    {
        ViewMain = view;
    }

    View GetView()
    {
        return ViewMain;
    }

    void SetActivity(Activity a)
    {
        activity = a;
    }

    protected Activity GetActivity()
    {
        return activity;
    }

    public void OnCreate() { }

    public void OnDestroy() { }

    public void OnHide() { }

    public void OnResume() { }

    public void OnPause() { }

    public void OnActivityResult(int RequestCode, int ResultCode, Intent intent) { }

    public void OnBackPressed() { }
}

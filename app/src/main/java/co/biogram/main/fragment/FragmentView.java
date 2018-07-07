package co.biogram.main.fragment;

import android.view.View;
import android.content.Intent;

public abstract class FragmentView
{
    public long Time = System.currentTimeMillis();
    public FragmentActivity Activity;
    public View ViewMain;
    public String Tag;

    public abstract void OnCreate();

    public void OnResume() { }

    public void OnOpen() { }

    public void OnPause() { }

    public void OnDestroy() { }

    public void OnActivityResult(int RequestCode, int ResultCode, Intent intent) { }
}

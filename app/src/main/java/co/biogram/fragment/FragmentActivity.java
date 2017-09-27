package co.biogram.fragment;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

public class FragmentActivity extends Activity
{
    private FragmentManager Manager;
    private ViewGroup ViewGroupMain;

    public FragmentManager GetManager()
    {
        if (Manager == null)
            Manager = new FragmentManager(this);

        return Manager;
    }

    public ViewGroup GetContentView()
    {
        return ViewGroupMain;
    }

    public void SetContentView(ViewGroup v)
    {
        ViewGroupMain = v;
        setContentView(ViewGroupMain);
    }

    @Override
    public void onBackPressed()
    {
        if (Manager.HandleBack())
            super.onBackPressed();
    }

    @Override
    public void onTrimMemory(int level)
    {
        if (level >= TRIM_MEMORY_BACKGROUND)
            Manager.OnLowMemory();

        super.onTrimMemory(level);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Manager.OnPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Manager.OnResume();
    }

    @Override
    public void onActivityResult(int RequestCode, int ResultCode, Intent intent)
    {
        super.onActivityResult(RequestCode, ResultCode, intent);
        Manager.OnActivityResult(RequestCode, ResultCode, intent);
    }
}

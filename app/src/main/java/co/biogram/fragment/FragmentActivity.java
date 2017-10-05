package co.biogram.fragment;

import android.app.Activity;
import android.content.Intent;

public class FragmentActivity extends Activity
{
    private FragmentManager Manager;

    public FragmentManager GetManager()
    {
        if (Manager == null)
            Manager = new FragmentManager(this);

        return Manager;
    }

    @Override
    public void onBackPressed()
    {
        if (Manager.HandleBack())
            super.onBackPressed();
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

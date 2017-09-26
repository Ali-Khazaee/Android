package co.biogram.view;

import android.app.Activity;
import android.content.Intent;
import android.view.ViewGroup;

public class ViewActivity extends Activity
{
    private ViewManager Manager = new ViewManager(this);

    public ViewManager GetManager()
    {
        return Manager;
    }

    @Override
    public void onBackPressed()
    {
        Manager.OnBackPressed();

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

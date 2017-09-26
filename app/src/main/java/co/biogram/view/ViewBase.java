package co.biogram.view;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class ViewBase
{
    private View ViewMain;

    protected void SetRootView(View view)
    {
        ViewMain = view;
    }

    public View GetView()
    {
        return ViewMain;
    }

    public void OnOpen()
    {

    }

    public void OnClose()
    {

    }

    public void OnRemove()
    {

    }

    public void OnResume()
    {

    }

    public void OnPause()
    {

    }

    public void OnBackPressed()
    {

    }

    public void OnActivityResult(int RequestCode, int ResultCode, Intent intent)
    {

    }
}

package co.biogram.view;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Stack;

public class ViewManager
{
    final private List<ViewBase> ListViewBase = new Stack<>();
    final private Activity activity;

    private ViewBase ActiveViewBase;

    public ViewManager(Activity a)
    {
        activity = a;
    }

    public void Open(ViewBase viewBase)
    {
        activity.setContentView(viewBase.GetView());

        //ListViewBase.add(viewBase);

        ActiveViewBase = viewBase;
        ActiveViewBase.OnOpen();
    }

    public void Close()
    {
        if (ActiveViewBase != null)
            ActiveViewBase.OnClose();
    }

    public void Remove()
    {
        if (ActiveViewBase != null)
            ActiveViewBase.OnRemove();
    }

    void OnResume()
    {
        if (ActiveViewBase != null)
            ActiveViewBase.OnResume();
    }

    void OnPause()
    {
        if (ActiveViewBase != null)
            ActiveViewBase.OnPause();
    }

    void OnBackPressed()
    {
        if (ActiveViewBase != null)
            ActiveViewBase.OnBackPressed();
    }

    void OnActivityResult(int RequestCode, int ResultCode, Intent intent)
    {
        if (ActiveViewBase != null)
            ActiveViewBase.OnActivityResult(RequestCode, ResultCode, intent);
    }

    boolean HandleBack()
    {
        return true;
    }
}

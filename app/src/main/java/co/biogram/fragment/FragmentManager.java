package co.biogram.fragment;

import android.app.Activity;
import android.content.Intent;

import java.util.Stack;

public class FragmentManager
{
    final private Stack<FragmentBase> FragmentBaseList = new Stack<>();
    final private Activity activity;

    private FragmentBase FragmentBaseActive;

    FragmentManager(Activity a)
    {
        activity = a;
    }

    public void Create(FragmentBase fragmentBase)
    {
        FragmentBaseActive = fragmentBase;
        FragmentBaseActive.SetActivity(activity);
        FragmentBaseActive.OnCreate();

        activity.setContentView(fragmentBase.GetView());
    }

    void OnResume()
    {
        if (FragmentBaseActive != null)
            FragmentBaseActive.OnResume();
    }

    void OnPause()
    {
        if (FragmentBaseActive != null)
            FragmentBaseActive.OnPause();
    }

    void OnBackPressed()
    {
        if (FragmentBaseActive != null)
            FragmentBaseActive.OnBackPressed();
    }

    void OnActivityResult(int RequestCode, int ResultCode, Intent intent)
    {
        if (FragmentBaseActive != null)
            FragmentBaseActive.OnActivityResult(RequestCode, ResultCode, intent);
    }

    boolean HandleBack()
    {
        if (FragmentBaseActive != null)
            FragmentBaseActive.OnDestroy();

        if (FragmentBaseActive != null)
            FragmentBaseActive.OnHide();

        return true;
    }
}

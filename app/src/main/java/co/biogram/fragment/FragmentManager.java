package co.biogram.fragment;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class FragmentManager
{
    final private List<FragmentBase> FragmentBaseList = new ArrayList<>();
    final private FragmentActivity activity;

    private FragmentBase FragmentBaseActive;

    FragmentManager(FragmentActivity a)
    {
        activity = a;
    }

    public void Create(FragmentBase fragmentBase)
    {
        FragmentBaseActive = fragmentBase;
        FragmentBaseActive.SetActivity(activity);
        FragmentBaseActive.OnCreate();

        activity.GetContentView().addView(FragmentBaseActive.GetView());

        FragmentBaseList.add(FragmentBaseActive);

        if (FragmentBaseList.size() >= 4)
        {
            FragmentBaseList.get(1).OnDestroy();
            FragmentBaseList.remove(1);
        }
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

    void OnActivityResult(int RequestCode, int ResultCode, Intent intent)
    {
        if (FragmentBaseActive != null)
            FragmentBaseActive.OnActivityResult(RequestCode, ResultCode, intent);
    }

    boolean HandleBack()
    {
        if (FragmentBaseList.size() > 1)
        {
            FragmentBaseList.get(FragmentBaseList.size() - 1).OnDestroy();
            FragmentBaseList.remove(FragmentBaseList.size() - 1);

            FragmentBaseActive = FragmentBaseList.get(FragmentBaseList.size() - 1);
            FragmentBaseActive.OnResume();

            return false;
        }

        return true;
    }

    void OnLowMemory()
    {
        if (FragmentBaseList.size() <= 2)
            return;

        FragmentBaseList.get(1).OnDestroy();
        FragmentBaseList.remove(1);
    }
}

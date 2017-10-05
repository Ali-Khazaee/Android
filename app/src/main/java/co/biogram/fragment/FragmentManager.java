package co.biogram.fragment;

import android.content.Intent;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class FragmentManager
{
    final private List<FragmentBase> FragmentBaseList = new ArrayList<>();
    final private FragmentActivity Activity;

    private FragmentBase ActiveFragment;

    FragmentManager(FragmentActivity a)
    {
        Activity = a;
    }

    public void OpenView(FragmentBase Fragment, int ID, String Tag)
    {
        ActiveFragment = Fragment;
        ActiveFragment.Tag = Tag;
        ActiveFragment.Activity = Activity;
        ActiveFragment.OnCreate();
        ActiveFragment.OnResume();

        FrameLayout FrameLayoutMain = (FrameLayout) Activity.findViewById(ID);
        FrameLayoutMain.addView(ActiveFragment.ViewMain);

        FragmentBaseList.add(ActiveFragment);
    }

    boolean HandleBack()
    {
        return true;
    }

    void OnResume()
    {
        if (ActiveFragment != null)
            ActiveFragment.OnResume();
    }

    void OnPause()
    {
        if (ActiveFragment != null)
            ActiveFragment.OnPause();
    }

    void OnActivityResult(int RequestCode, int ResultCode, Intent intent)
    {
        if (ActiveFragment != null)
            ActiveFragment.OnActivityResult(RequestCode, ResultCode, intent);
    }
}

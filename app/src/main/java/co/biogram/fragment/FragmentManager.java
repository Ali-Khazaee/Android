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

    public FragmentBase FindByTag(String Tag)
    {
        for (FragmentBase Fragment : FragmentBaseList)
            if (Fragment.Tag.equals(Tag))
                return Fragment;

        return null;
    }

    public void OpenView(FragmentBase Fragment, int ID, String Tag)
    {
        if (FragmentBaseList.size() > 1)
            FragmentBaseList.get(FragmentBaseList.size() - 1).OnPause();

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
        if (FragmentBaseList.size() > 1)
        {
            FragmentBaseList.get(FragmentBaseList.size() - 1).OnPause();
            FragmentBaseList.get(FragmentBaseList.size() - 1).OnDestroy();
            FragmentBaseList.remove(FragmentBaseList.size() - 1);

            ActiveFragment = FragmentBaseList.get(FragmentBaseList.size() - 1);
            ActiveFragment.OnResume();

            return false;
        }

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

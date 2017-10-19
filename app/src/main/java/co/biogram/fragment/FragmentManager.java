package co.biogram.fragment;

import android.content.Intent;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import co.biogram.main.handler.MiscHandler;

public class FragmentManager
{
    final private List<FragmentBase> FragmentList = new ArrayList<>();
    final private FragmentActivity Activity;

    private FragmentBase Fragment;

    FragmentManager(FragmentActivity a)
    {
        Activity = a;
    }

    public void OpenView(FragmentBase fragment, int ID, String Tag)
    {
        if (FragmentList.size() > 1)
            FragmentList.get(FragmentList.size() - 1).OnPause();

        Fragment = fragment;
        Fragment.Tag = Tag;
        Fragment.D56 = MiscHandler.ToDimension(Activity, 56);
        Fragment.Activity = Activity;
        Fragment.OnCreate();
        Fragment.OnResume();

        FrameLayout FrameLayoutMain = (FrameLayout) Activity.findViewById(ID);
        FrameLayoutMain.addView(Fragment.ViewMain);

        FragmentList.add(Fragment);
    }

    boolean HandleBack()
    {
        if (FragmentList.size() > 1)
        {
            int I = FragmentList.size() - 1;

            FragmentList.get(I).OnPause();
            FragmentList.get(I).OnDestroy();
            FragmentList.remove(I);

            Fragment = FragmentList.get(FragmentList.size() - 1);
            Fragment.OnResume();

            return false;
        }

        return true;
    }

    public FragmentBase FindByTag(String Tag)
    {
        for (FragmentBase Fragment : FragmentList)
            if (Fragment.Tag.equals(Tag))
                return Fragment;

        return null;
    }

    void OnResume()
    {
        if (Fragment != null)
            Fragment.OnResume();
    }

    void OnPause()
    {
        if (Fragment != null)
            Fragment.OnPause();
    }

    void OnActivityResult(int RequestCode, int ResultCode, Intent intent)
    {
        if (Fragment != null)
            Fragment.OnActivityResult(RequestCode, ResultCode, intent);
    }
}

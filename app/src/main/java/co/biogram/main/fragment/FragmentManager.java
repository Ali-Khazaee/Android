package co.biogram.main.fragment;

import android.content.Intent;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

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
        Fragment.Activity = Activity;
        Fragment.OnCreate();
        Fragment.OnResume();

        if (Fragment.ViewMain != null)
        {
            RelativeLayout RelativeLayoutMain = (RelativeLayout) Activity.findViewById(ID);
            RelativeLayoutMain.addView(Fragment.ViewMain);
        }

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

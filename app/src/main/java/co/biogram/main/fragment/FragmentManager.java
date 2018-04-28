package co.biogram.main.fragment;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class FragmentManager
{
    private ArrayList<FragmentView> FragmentList = new ArrayList<>();
    private FragmentActivity Activity;
    private FragmentView Fragment;

    FragmentManager(FragmentActivity a)
    {
        Activity = a;
    }

    public void OpenView(FragmentView Frag, int ID, String Tag)
    {
        if (Fragment != null && Tag.equals(Fragment.Tag))
        {
            Fragment.OnOpen();
            return;
        }

        if (FragmentList.size() > 0)
        {
            FragmentView Frag2 = FragmentList.get(FragmentList.size() - 1);

            Frag2.OnPause();

            if (Frag2.ViewMain != null && Frag2.ViewMain.getVisibility() == View.VISIBLE)
                Frag2.ViewMain.setVisibility(View.GONE);
        }

        Fragment = Frag;
        Fragment.Tag = Tag;
        Fragment.Activity = Activity;
        Fragment.OnCreate();
        Fragment.OnResume();

        if (Fragment.ViewMain != null)
        {
            RelativeLayout RelativeLayoutMain = Activity.findViewById(ID);
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

            if (Fragment.ViewMain != null && Fragment.ViewMain.getVisibility() == View.GONE)
                Fragment.ViewMain.setVisibility(View.VISIBLE);

            return false;
        }

        return true;
    }

    @Nullable
    public FragmentView FindByTag(String Tag)
    {
        for (FragmentView Fragment : FragmentList)
            if (Fragment.Tag.equals(Tag))
                return Fragment;

        return null;
    }

    void OnResume()
    {
        if (Fragment != null)
        {
            if (Fragment.ViewMain != null && Fragment.ViewMain.getVisibility() == View.GONE)
                Fragment.ViewMain.setVisibility(View.VISIBLE);

            Fragment.OnResume();
        }
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

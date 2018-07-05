package co.biogram.main.fragment;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

import co.biogram.main.R;

public class FragmentManager
{
    private ArrayList<FragmentDialog> FragmentDialogList = new ArrayList<>();
    private ArrayList<FragmentView> FragmentViewList = new ArrayList<>();
    private FragmentActivity Activity;
    private FragmentView CurrentFrag;

    FragmentManager(FragmentActivity a)
    {
        Activity = a;
    }

    public void OpenDialog(FragmentDialog Dialog)
    {
        Dialog.Activity = Activity;
        Dialog.OnCreate();

        if (Dialog.ViewMain != null)
        {
            FrameLayout FrameLayoutMain = Activity.findViewById(R.id.ContainerFull);
            FrameLayoutMain.addView(Dialog.ViewMain);
        }

        FragmentDialogList.add(Dialog);
    }

    public void OpenView(FragmentView FragView, String Tag)
    {
        if (CurrentFrag != null && CurrentFrag.Tag.equals(Tag))
        {
            CurrentFrag.OnOpen();
            return;
        }

        if (FragmentViewList.size() > 0)
        {
            FragmentView LastFrag = FragmentViewList.get(FragmentViewList.size() - 1);

            LastFrag.OnPause();

            if (LastFrag.ViewMain != null && LastFrag.ViewMain.getVisibility() == View.VISIBLE)
                LastFrag.ViewMain.setVisibility(View.GONE);
        }

        CurrentFrag = FragView;
        CurrentFrag.Tag = Tag;
        CurrentFrag.Activity = Activity;
        CurrentFrag.OnCreate();
        CurrentFrag.OnResume();

        if (CurrentFrag.ViewMain != null)
        {
            FrameLayout FrameLayoutMain = Activity.findViewById(R.id.ContainerFull);
            FrameLayoutMain.addView(CurrentFrag.ViewMain);
        }

        FragmentViewList.add(CurrentFrag);
    }

    public void DismissDialog()
    {
        if (FragmentDialogList.size() > 0)
        {
            FragmentDialogList.get(FragmentDialogList.size() - 1).OnDestroy();
            FragmentDialogList.remove(FragmentDialogList.size() - 1);
        }
    }

    boolean HandleBack()
    {
        if (FragmentDialogList.size() > 0)
        {
            FragmentDialogList.get(FragmentDialogList.size() - 1).OnDestroy();
            FragmentDialogList.remove(FragmentDialogList.size() - 1);
            return false;
        }

        if (FragmentViewList.size() > 1)
        {
            FragmentView Frag = FragmentViewList.get(FragmentViewList.size() - 1);
            Frag.OnPause();
            Frag.OnDestroy();

            if (Frag.ViewMain != null)
            {
                ViewGroup Parent = (ViewGroup) Frag.ViewMain.getParent();

                if (Parent != null)
                    Parent.removeView(Frag.ViewMain);

                Frag.ViewMain = null;
            }

            FragmentViewList.remove(FragmentViewList.size() - 1);

            CurrentFrag = FragmentViewList.get(FragmentViewList.size() - 1);
            CurrentFrag.OnResume();

            if (CurrentFrag.ViewMain != null && CurrentFrag.ViewMain.getVisibility() == View.GONE)
                CurrentFrag.ViewMain.setVisibility(View.VISIBLE);

            return false;
        }

        return true;
    }

    void OnResume()
    {
        if (CurrentFrag != null)
        {
            if (CurrentFrag.ViewMain != null && CurrentFrag.ViewMain.getVisibility() == View.GONE)
                CurrentFrag.ViewMain.setVisibility(View.VISIBLE);

            CurrentFrag.OnResume();
        }
    }

    void OnPause()
    {
        if (CurrentFrag != null)
            CurrentFrag.OnPause();
    }

    void OnActivityResult(int RequestCode, int ResultCode, Intent intent)
    {
        if (CurrentFrag != null)
            CurrentFrag.OnActivityResult(RequestCode, ResultCode, intent);
    }
}

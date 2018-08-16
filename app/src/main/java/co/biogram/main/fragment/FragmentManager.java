package co.biogram.main.fragment;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import co.biogram.main.R;

import java.util.ArrayList;

public class FragmentManager {
    private ArrayList<FragmentView> FragmentList = new ArrayList<>();
    private FragmentActivity Activity;
    private FragmentView CurrentFrag;

    FragmentManager(FragmentActivity a) {
        Activity = a;
    }

    public void OpenView(FragmentView Frag, String Tag, boolean Full) {
        if (CurrentFrag != null && CurrentFrag.Tag.equals(Tag)) {
            CurrentFrag.OnOpen();
            return;
        }

        // TODO If Exist ReOpen FindByTag(Tag);

        if (FragmentList.size() > 0) {
            FragmentView Frag2 = FragmentList.get(FragmentList.size() - 1);

            Frag2.OnPause();

            if (Frag2.ViewMain != null && Frag2.ViewMain.getVisibility() == View.VISIBLE)
                Frag2.ViewMain.setVisibility(View.GONE);
        }

        CurrentFrag = Frag;
        CurrentFrag.Tag = Tag;
        CurrentFrag.Activity = Activity;
        CurrentFrag.OnCreate();
        CurrentFrag.OnResume();

        if (CurrentFrag.ViewMain != null) {
            FrameLayout FrameLayoutMain = Activity.findViewById(Full ? R.id.ContainerFull : R.id.Container);
            FrameLayoutMain.addView(CurrentFrag.ViewMain);
        }

        FragmentList.add(CurrentFrag);
    }

    boolean HandleBack() {
        if (FragmentList.size() > 1) {
            FragmentView Frag = FragmentList.get(FragmentList.size() - 1);
            Frag.OnPause();
            Frag.OnDestroy();

            if (Frag.ViewMain != null) {
                ViewGroup Parent = (ViewGroup) Frag.ViewMain.getParent();

                if (Parent != null)
                    Parent.removeView(Frag.ViewMain);

                Frag.ViewMain = null;
            }

            FragmentList.remove(FragmentList.size() - 1);

            CurrentFrag = FragmentList.get(FragmentList.size() - 1);
            CurrentFrag.OnResume();

            if (CurrentFrag.ViewMain != null && CurrentFrag.ViewMain.getVisibility() == View.GONE)
                CurrentFrag.ViewMain.setVisibility(View.VISIBLE);

            return false;
        }

        return true;
    }

    @Nullable
    public FragmentView FindByTag(String Tag) {
        for (FragmentView Frag : FragmentList)
            if (Frag.Tag.equals(Tag))
                return Frag;

        return null;
    }

    void OnResume() {
        if (CurrentFrag != null) {
            if (CurrentFrag.ViewMain != null && CurrentFrag.ViewMain.getVisibility() == View.GONE)
                CurrentFrag.ViewMain.setVisibility(View.VISIBLE);

            CurrentFrag.OnResume();
        }
    }

    void OnPause() {
        if (CurrentFrag != null)
            CurrentFrag.OnPause();
    }

    void OnActivityResult(int RequestCode, int ResultCode, Intent intent) {
        if (CurrentFrag != null)
            CurrentFrag.OnActivityResult(RequestCode, ResultCode, intent);
    }
}

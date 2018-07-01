package co.biogram.main.ui.social;

import android.view.View;
import co.biogram.main.R;
import co.biogram.main.fragment.FragmentView;

public class Profile_Private_UI extends FragmentView
{
    @Override
    public void OnCreate()
    {
        View view = View.inflate(Activity, R.layout.social_profile_private, null);

        ViewMain = view;
    }
}

package co.biogram.main.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentProfile;
import co.biogram.main.fragment.FragmentMoment;
import co.biogram.main.check.FragmentTabCategory;
import co.biogram.main.check.FragmentTabFriend;
import co.biogram.main.check.FragmentTabNotification;
import co.biogram.main.handler.MiscHandler;

public class ActivityMain extends AppCompatActivity
{
    private ImageView ImageViewMoment;
    private ImageView ImageViewHome;
    private ImageView ImageViewCategory;
    private ImageView ImageViewNotification;
    private ImageView ImageViewProfile;

    private FragmentManager FragManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageViewMoment = (ImageView) findViewById(R.id.ImageViewMoment);
        ImageViewHome = (ImageView) findViewById(R.id.ImageViewHome);
        ImageViewCategory = (ImageView) findViewById(R.id.ImageViewCategory);
        ImageViewNotification = (ImageView) findViewById(R.id.ImageViewNotification);
        ImageViewProfile = (ImageView) findViewById(R.id.ImageViewProfile);

        ImageViewMoment.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { ChangeFragment(1, false); } });
        ImageViewHome.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { ChangeFragment(2, false); } });
        ImageViewCategory.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { ChangeFragment(3, false); } });
        ImageViewNotification.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { ChangeFragment(4, false); } });
        ImageViewProfile.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { ChangeFragment(5, false); } });

        FragManager = getSupportFragmentManager();

        ChangeFragment(getIntent().getIntExtra("Tab", 5), false);
    }

    @Override
    public void onBackPressed()
    {
        List<Fragment> FragList = FragManager.getFragments();

        if (FragList != null)
        {
            for (Fragment Frag : FragList)
            {
                if (Frag != null)
                {
                    MiscHandler.Log("Size: " + FragList.size() + " - " + Frag.getClass().getSimpleName() + " - " + Frag.isVisible());
                }
            }
        }

        if (FragManager.getBackStackEntryCount() > 1)
            super.onBackPressed();

        for (Fragment fragment : FragManager.getFragments())
        {
            if (fragment != null && fragment.isVisible())
            {
                switch (fragment.getClass().getSimpleName())
                {
                    case "FragmentMoment":       ChangeFragment(1, true); break;
                    case "FragmentHome":         ChangeFragment(2, true); break;
                    case "FragmentCategory":     ChangeFragment(3, true); break;
                    case "FragmentNotification": ChangeFragment(4, true); break;
                    case "FragmentProfile":      ChangeFragment(5, true); break;
                }
            }
        }
    }

    private void ChangeFragment(int Tab, boolean Continue)
    {
        findViewById(R.id.LinearLayoutMenu).setVisibility(View.VISIBLE);
        ImageViewMoment.setImageResource(R.drawable.ic_moment_gray);
        ImageViewHome.setImageResource(R.drawable.ic_home_gray);
        ImageViewCategory.setImageResource(R.drawable.ic_category_gray);
        ImageViewNotification.setImageResource(R.drawable.ic_notification_gray);
        ImageViewProfile.setImageResource(R.drawable.ic_profile_gray);

        switch (Tab)
        {
            case 1: ImageViewMoment.setImageResource(R.drawable.ic_moment_black);             break;
            case 2: ImageViewHome.setImageResource(R.drawable.ic_home_black);                 break;
            case 3: ImageViewCategory.setImageResource(R.drawable.ic_category_black);         break;
            case 4: ImageViewNotification.setImageResource(R.drawable.ic_notification_black); break;
            case 5: ImageViewProfile.setImageResource(R.drawable.ic_profile_black);           break;
        }

        if (Continue)
            return;

        Fragment fragment = new FragmentMoment();

        switch (Tab)
        {
            case 2: fragment = new FragmentTabFriend();       break;
            case 3: fragment = new FragmentTabCategory();     break;
            case 4: fragment = new FragmentTabNotification(); break;
            case 5: fragment = new FragmentProfile();         break;
        }

        FragManager.beginTransaction().add(R.id.FrameLayoutContainer, fragment, fragment.getClass().getSimpleName()).addToBackStack(fragment.getClass().getSimpleName()).commit();
    }
}

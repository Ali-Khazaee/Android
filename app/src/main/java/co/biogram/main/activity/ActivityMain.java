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

        ImageViewMoment.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { ChangeTab(1); } });
        ImageViewHome.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { ChangeTab(2); } });
        ImageViewCategory.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { ChangeTab(3); } });
        ImageViewNotification.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { ChangeTab(4); } });
        ImageViewProfile.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { ChangeTab(5); } });

        FragManager = getSupportFragmentManager();

        ChangeTab(getIntent().getIntExtra("Tab", 3));
    }

    @Override
    public void onBackPressed()
    {
        if (FragManager.getBackStackEntryCount() == 1)
        {
            finish();
            return;
        }

        if (FragManager.getBackStackEntryCount() > 1)
            super.onBackPressed();

        for (Fragment fragment : FragManager.getFragments())
        {
            if (fragment != null && fragment.isVisible())
            {
                switch (fragment.getClass().getSimpleName())
                {
                    case "FragmentMoment":       ChangeTab(1); break;
                    case "FragmentHome":         ChangeTab(2); break;
                    case "FragmentCategory":     ChangeTab(3); break;
                    case "FragmentNotification": ChangeTab(4); break;
                    case "FragmentProfile":      ChangeTab(5); break;
                }
            }
        }
    }

    private void ChangeTab(int Tab)
    {
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

        Fragment SelectedFragment = new FragmentMoment();

        switch (Tab)
        {
            case 2: SelectedFragment = new FragmentTabFriend();       break;
            case 3: SelectedFragment = new FragmentTabCategory();     break;
            case 4: SelectedFragment = new FragmentTabNotification(); break;
            case 5: SelectedFragment = new FragmentProfile();         break;
        }

        Fragment FoundFragment = FragManager.findFragmentByTag(SelectedFragment.getClass().getSimpleName());

        if (FoundFragment != null)
        {
            for (Fragment Frag : FragManager.getFragments())
            {
                if (Frag != null && Frag != FoundFragment)
                {
                    FragManager.beginTransaction().hide(Frag).commit();
                }
            }

            FragManager.beginTransaction().show(FoundFragment).commit();
            return;
        }

        FragManager.beginTransaction().add(R.id.FrameLayoutContainer, SelectedFragment, SelectedFragment.getClass().getSimpleName()).addToBackStack(SelectedFragment.getClass().getSimpleName()).commit();
    }
}

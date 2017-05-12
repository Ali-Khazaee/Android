package co.biogram.main.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentProfile;
import co.biogram.main.fragment.FragmentMoment;
import co.biogram.main.check.FragmentTabCategory;
import co.biogram.main.check.FragmentTabFriend;
import co.biogram.main.check.FragmentTabNotification;
import co.biogram.main.handler.MiscHandler;

public class ActivityMain extends FragmentActivity
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

        Context context = this;

        RelativeLayout Root = new RelativeLayout(context);
        Root.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        RelativeLayout.LayoutParams LinearLayoutMenuParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 57));
        LinearLayoutMenuParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        LinearLayout LinearLayoutMenu = new LinearLayout(context);
        LinearLayoutMenu.setLayoutParams(LinearLayoutMenuParam);
        LinearLayoutMenu.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutMenu.setId(MiscHandler.GenerateViewID());

        Root.addView(LinearLayoutMenu);

        View ViewLine = new View(context);
        ViewLine.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1)));
        ViewLine.setBackgroundResource(R.color.Gray2);

        LinearLayoutMenu.addView(ViewLine);

        RelativeLayout.LayoutParams LinearLayoutTabParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 57));
        LinearLayoutTabParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        LinearLayout LinearLayoutTab = new LinearLayout(context);
        LinearLayoutTab.setLayoutParams(LinearLayoutTabParam);
        LinearLayoutTab.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutTab.setBackgroundResource(R.color.White5);

        LinearLayoutMenu.addView(LinearLayoutTab);

        ImageViewMoment = new ImageView(context);
        ImageViewMoment.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        ImageViewMoment.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewMoment.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        ImageViewMoment.setImageResource(R.drawable.ic_moment_gray);
        ImageViewMoment.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { ChangeTab(1); } });

        LinearLayoutTab.addView(ImageViewMoment);

        ImageViewHome = new ImageView(context);
        ImageViewHome.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        ImageViewHome.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewHome.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        ImageViewHome.setImageResource(R.drawable.ic_home_gray);
        ImageViewHome.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { ChangeTab(2); } });

        LinearLayoutTab.addView(ImageViewHome);

        ImageViewCategory = new ImageView(context);
        ImageViewCategory.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        ImageViewCategory.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewCategory.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        ImageViewCategory.setImageResource(R.drawable.ic_category_gray);
        ImageViewCategory.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { ChangeTab(3); } });

        LinearLayoutTab.addView(ImageViewCategory);

        ImageViewNotification = new ImageView(context);
        ImageViewNotification.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        ImageViewNotification.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewNotification.setPadding(MiscHandler.ToDimension(context, 17), MiscHandler.ToDimension(context, 17), MiscHandler.ToDimension(context, 17), MiscHandler.ToDimension(context, 17));
        ImageViewNotification.setImageResource(R.drawable.ic_notification_gray);
        ImageViewNotification.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { ChangeTab(4); } });

        LinearLayoutTab.addView(ImageViewNotification);

        ImageViewProfile = new ImageView(context);
        ImageViewProfile.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        ImageViewProfile.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewProfile.setPadding(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 15));
        ImageViewProfile.setImageResource(R.drawable.ic_profile_gray);
        ImageViewProfile.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { ChangeTab(5); } });

        LinearLayoutTab.addView(ImageViewProfile);

        RelativeLayout.LayoutParams FrameLayoutContentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        FrameLayoutContentParam.addRule(RelativeLayout.ABOVE, LinearLayoutMenu.getId());

        FrameLayout FrameLayoutContent = new FrameLayout(context);
        FrameLayoutContent.setLayoutParams(FrameLayoutContentParam);
        FrameLayoutContent.setId(R.id.ActivityMainContentContainer);

        Root.addView(FrameLayoutContent);

        FrameLayout FrameLayoutFull = new FrameLayout(context);
        FrameLayoutFull.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        FrameLayoutFull.setId(R.id.ActivityMainFullContainer);

        Root.addView(FrameLayoutFull);

        setContentView(Root);

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

        FragManager.beginTransaction().add(R.id.ActivityMainContentContainer, SelectedFragment, SelectedFragment.getClass().getSimpleName()).addToBackStack(SelectedFragment.getClass().getSimpleName()).commit();
    }
}

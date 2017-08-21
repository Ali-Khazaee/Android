package co.biogram.main.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;

import co.biogram.main.R;
import co.biogram.main.fragment.CategoryFragment;
import co.biogram.main.fragment.MomentFragment;
import co.biogram.main.fragment.PostFragment;
import co.biogram.main.fragment.ProfileFragment;
import co.biogram.main.fragment.InboxFragment;
import co.biogram.main.fragment.NotificationFragment;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.service.NotificationService;
import me.leolin.shortcutbadger.ShortcutBadger;

public class MainActivity extends FragmentActivity
{
    private ImageView ImageViewMoment;
    private ImageView ImageViewInbox;
    private ImageView ImageViewCategory;
    private ImageView ImageViewNotification;
    private ImageView ImageViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Context context = this;

        RelativeLayout RelativeLayoutMain = new RelativeLayout(context);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        RelativeLayout.LayoutParams LinearLayoutMenuParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 57));
        LinearLayoutMenuParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        LinearLayout LinearLayoutMenu = new LinearLayout(context);
        LinearLayoutMenu.setLayoutParams(LinearLayoutMenuParam);
        LinearLayoutMenu.setOrientation(LinearLayout.VERTICAL);
        LinearLayoutMenu.setId(MiscHandler.GenerateViewID());

        RelativeLayoutMain.addView(LinearLayoutMenu);

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

        ImageViewInbox = new ImageView(context);
        ImageViewInbox.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        ImageViewInbox.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewInbox.setPadding(MiscHandler.ToDimension(context, 17), MiscHandler.ToDimension(context, 17), MiscHandler.ToDimension(context, 17), MiscHandler.ToDimension(context, 17));
        ImageViewInbox.setImageResource(R.drawable.ic_inbox_gray);
        ImageViewInbox.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { ChangeTab(2); } });

        LinearLayoutTab.addView(ImageViewInbox);

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
        FrameLayoutContent.setId(R.id.MainActivityContentContainer);

        RelativeLayoutMain.addView(FrameLayoutContent);

        FrameLayout FrameLayoutFull = new FrameLayout(context);
        FrameLayoutFull.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        FrameLayoutFull.setId(R.id.MainActivityFullContainer);

        RelativeLayoutMain.addView(FrameLayoutFull);

        setContentView(RelativeLayoutMain);

        ChangeTab(getIntent().getIntExtra("Tab", 1));
    }

    @Override
    public void onResume()
    {
        super.onResume();
        ShortcutBadger.removeCount(this);
        registerReceiver(BroadcastReceiverNotification, new IntentFilter(NotificationService.BROADCAST_ACTION_NEW));

        if (getIntent().getIntExtra("Type", 0) == 1)
        {
            if (SharedHandler.GetString(this, "Username").equals(getIntent().getStringExtra("Data")))
                return;

            Bundle bundle = new Bundle();
            bundle.putString("Username", getIntent().getStringExtra("Data"));

            Fragment fragment = new ProfileFragment();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("ProfileFragment").commit();
        }

        if (getIntent().getIntExtra("Type", 0) == 2)
        {
            Bundle bundle = new Bundle();
            bundle.putString("PostID", getIntent().getStringExtra("Data"));

            Fragment fragment = new PostFragment();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("PostFragment").commit();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        unregisterReceiver(BroadcastReceiverNotification);
    }

    @Override
    public void onAttachFragment(Fragment fragment)
    {
        super.onAttachFragment(fragment);

        FragmentManager FragManager = getSupportFragmentManager();

        if (FragManager.getBackStackEntryCount() > 3)
        {
            List<Fragment> FragList = FragManager.getFragments();

            if (FragList != null)
            {
                for (Fragment frag : FragList)
                {
                    if (frag != null)
                    {
                        FragManager.beginTransaction().remove(fragment).commit();
                        break;
                    }
                }
            }
        }
    }

    private final BroadcastReceiver BroadcastReceiverNotification = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equalsIgnoreCase(NotificationService.BROADCAST_ACTION_NEW))
                ImageViewNotification.setImageResource(R.drawable.ic_notification_red);
        }
    };

    private void ChangeTab(int Tab)
    {
        ImageViewMoment.setImageResource(R.drawable.ic_moment_gray);
        ImageViewInbox.setImageResource(R.drawable.ic_inbox_gray);
        ImageViewCategory.setImageResource(R.drawable.ic_category_gray);
        ImageViewNotification.setImageResource(R.drawable.ic_notification_gray);
        ImageViewProfile.setImageResource(R.drawable.ic_profile_gray);

        switch (Tab)
        {
            case 1: ImageViewMoment.setImageResource(R.drawable.ic_moment_black);             break;
            case 2: ImageViewInbox.setImageResource(R.drawable.ic_inbox_black);               break;
            case 3: ImageViewCategory.setImageResource(R.drawable.ic_category_black);         break;
            case 4: ImageViewNotification.setImageResource(R.drawable.ic_notification_black); break;
            case 5: ImageViewProfile.setImageResource(R.drawable.ic_profile_black);           break;
        }

        Fragment SelectedFragment = new MomentFragment();

        switch (Tab)
        {
            case 2: SelectedFragment = new InboxFragment();        break;
            case 3: SelectedFragment = new CategoryFragment();     break;
            case 4: SelectedFragment = new NotificationFragment(); break;
            case 5:
                Bundle bundle = new Bundle();
                bundle.putBoolean("HideBack", true);

                SelectedFragment = new ProfileFragment();
                SelectedFragment.setArguments(bundle);
            break;
        }

        FragmentManager FragManager = getSupportFragmentManager();
        List<Fragment> FragList = FragManager.getFragments();

        if (FragList != null)
        {
            for (Fragment fragment : FragList)
            {
                if (fragment != null)
                {
                    FragManager.beginTransaction().remove(fragment).commit();
                }
            }
        }

        FragManager.beginTransaction().add(R.id.MainActivityContentContainer, SelectedFragment).commit();
    }
}

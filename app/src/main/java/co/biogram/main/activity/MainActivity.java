package co.biogram.main.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
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
import co.biogram.main.handler.UpdateHandler;
import co.biogram.main.service.NotificationService;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MainActivity extends FragmentActivity
{
    private boolean IsActive = true;
    private boolean IsNotification = false;

    private ImageView ImageViewMoment;
    private ImageView ImageViewInbox;
    private ImageView ImageViewCategory;
    private ImageView ImageViewNotification;
    private ImageView ImageViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        final Context context = this;

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
        RelativeLayoutMain.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if (!IsActive || UpdateHandler.IsUpdateAvailable(context))
                    return;

                final Dialog DialogUpdate = new Dialog(MainActivity.this);
                DialogUpdate.requestWindowFeature(Window.FEATURE_NO_TITLE);
                DialogUpdate.setCancelable(false);

                RelativeLayout LinearLayoutMain = new RelativeLayout(context);
                LinearLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 150)));
                LinearLayoutMain.setBackgroundResource(R.color.White);
                LinearLayoutMain.setClickable(true);

                RelativeLayout RelativeLayoutHeader = new RelativeLayout(context);
                RelativeLayoutHeader.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56)));
                RelativeLayoutHeader.setBackgroundResource(R.color.White5);
                RelativeLayoutHeader.setId(MiscHandler.GenerateViewID());

                LinearLayoutMain.addView(RelativeLayoutHeader);

                RelativeLayout.LayoutParams ImageViewBackParam = new RelativeLayout.LayoutParams(MiscHandler.ToDimension(context, 56), MiscHandler.ToDimension(context, 56));
                ImageViewBackParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                ImageView ImageViewBack = new ImageView(context);
                ImageViewBack.setPadding(MiscHandler.ToDimension(context, 13), MiscHandler.ToDimension(context, 13), MiscHandler.ToDimension(context, 13), MiscHandler.ToDimension(context, 13));
                ImageViewBack.setScaleType(ImageView.ScaleType.FIT_CENTER);
                ImageViewBack.setLayoutParams(ImageViewBackParam);
                ImageViewBack.setImageResource(R.drawable.ic_close_blue);
                ImageViewBack.setId(MiscHandler.GenerateViewID());
                ImageViewBack.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { DialogUpdate.dismiss(); } });

                RelativeLayoutHeader.addView(ImageViewBack);

                RelativeLayout.LayoutParams TextViewTitleParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewTitleParam.addRule(RelativeLayout.CENTER_VERTICAL);
                TextViewTitleParam.setMargins(MiscHandler.ToDimension(context, 15), 0, 0, 0);

                TextView TextViewTitle = new TextView(context);
                TextViewTitle.setLayoutParams(TextViewTitleParam);
                TextViewTitle.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewTitle.setText(getString(R.string.MainActivityUpdate));
                TextViewTitle.setTypeface(null, Typeface.BOLD);
                TextViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

                RelativeLayoutHeader.addView(TextViewTitle);

                RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 1));
                ViewLineParam.addRule(RelativeLayout.BELOW, RelativeLayoutHeader.getId());

                View ViewLine = new View(context);
                ViewLine.setLayoutParams(ViewLineParam);
                ViewLine.setBackgroundResource(R.color.Gray2);
                ViewLine.setId(MiscHandler.GenerateViewID());

                LinearLayoutMain.addView(ViewLine);

                RelativeLayout.LayoutParams TextViewDescriptionParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                TextViewDescriptionParam.setMargins(MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 5), MiscHandler.ToDimension(context, 15), MiscHandler.ToDimension(context, 5));
                TextViewDescriptionParam.addRule(RelativeLayout.BELOW, ViewLine.getId());

                TextView TextViewDescription = new TextView(context);
                TextViewDescription.setLayoutParams(TextViewDescriptionParam);
                TextViewDescription.setTextColor(ContextCompat.getColor(context, R.color.Black));
                TextViewDescription.setText(getString(R.string.MainActivityUpdateDescription));
                TextViewDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewDescription.setId(MiscHandler.GenerateViewID());

                LinearLayoutMain.addView(TextViewDescription);

                RelativeLayout.LayoutParams TextViewUpdateParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, MiscHandler.ToDimension(context, 56));
                TextViewUpdateParam.addRule(RelativeLayout.BELOW, TextViewDescription.getId());

                TextView TextViewUpdate = new TextView(context);
                TextViewUpdate.setLayoutParams(TextViewUpdateParam);
                TextViewUpdate.setBackgroundResource(R.color.BlueLight);
                TextViewUpdate.setTextColor(ContextCompat.getColor(context, R.color.White));
                TextViewUpdate.setText(getString(R.string.MainActivityUpdateNow));
                TextViewUpdate.setGravity(Gravity.CENTER);
                TextViewUpdate.setTypeface(null, Typeface.BOLD);
                TextViewUpdate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                TextViewUpdate.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        File ApkFile = new File(Environment.getExternalStorageDirectory(), ("biogram.apk"));
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(ApkFile), "application/vnd.android.package-archive");
                        startActivity(intent);
                    }
                });

                LinearLayoutMain.addView(TextViewUpdate);

                DialogUpdate.setContentView(LinearLayoutMain);
                DialogUpdate.show();
            }
        }, 3000);

        setContentView(RelativeLayoutMain);

        ChangeTab(getIntent().getIntExtra("Tab", 1));
    }

    @Override
    public void onResume()
    {
        super.onResume();
        IsActive = true;

        ShortcutBadger.removeCount(this);
        registerReceiver(BroadcastReceiverNotification, new IntentFilter(NotificationService.BROADCAST_ACTION_NEW));

        if (getIntent() == null)
            return;

        if (getIntent().getIntExtra("Type", 0) == 1)
        {
            if (SharedHandler.GetString(this, "Username").equals(getIntent().getStringExtra("Data")))
                return;

            Bundle bundle = new Bundle();
            bundle.putString("Username", getIntent().getStringExtra("Data"));

            Fragment fragment = new ProfileFragment();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("ProfileFragment").commitAllowingStateLoss();
        }

        if (getIntent().getIntExtra("Type", 0) == 2)
        {
            Bundle bundle = new Bundle();
            bundle.putString("PostID", getIntent().getStringExtra("Data"));

            Fragment fragment = new PostFragment();
            fragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction().add(R.id.MainActivityFullContainer, fragment).addToBackStack("PostFragment").commitAllowingStateLoss();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        IsActive = true;
        unregisterReceiver(BroadcastReceiverNotification);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        // No call for super() Bug on API Level > 11
    }

    private final BroadcastReceiver BroadcastReceiverNotification = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equalsIgnoreCase(NotificationService.BROADCAST_ACTION_NEW))
            {
                IsNotification = true;

                if (SharedHandler.GetBoolean(context, "VibrateNotification"))
                {
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(200);
                }

                ImageViewNotification.setImageResource(R.drawable.ic_notification_red);
            }
        }
    };

    private void ChangeTab(int Tab)
    {
        ImageViewMoment.setImageResource(R.drawable.ic_moment_gray);
        ImageViewInbox.setImageResource(R.drawable.ic_inbox_gray);
        ImageViewCategory.setImageResource(R.drawable.ic_category_gray);

        if (IsNotification)
            ImageViewNotification.setImageResource(R.drawable.ic_notification_red);
        else
            ImageViewNotification.setImageResource(R.drawable.ic_notification_gray);

        ImageViewProfile.setImageResource(R.drawable.ic_profile_gray);

        switch (Tab)
        {
            case 1: ImageViewMoment.setImageResource(R.drawable.ic_moment_black);             break;
            case 2: ImageViewInbox.setImageResource(R.drawable.ic_inbox_black);               break;
            case 3: ImageViewCategory.setImageResource(R.drawable.ic_category_black);         break;
            case 4: ImageViewNotification.setImageResource(R.drawable.ic_notification_black); IsNotification = false; break;
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
                    FragManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
                }
            }
        }

        FragManager.beginTransaction().add(R.id.MainActivityContentContainer, SelectedFragment).commitAllowingStateLoss();
    }
}

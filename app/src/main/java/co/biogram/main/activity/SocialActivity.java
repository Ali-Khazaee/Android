package co.biogram.main.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import co.biogram.main.fragment.FragmentActivity;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.R;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.social.InboxUI;
import co.biogram.main.ui.social.MomentUI;
import co.biogram.main.ui.social.NotificationUI;
import co.biogram.main.ui.social.PostUI;
import co.biogram.main.ui.social.ProfileUI;

public class SocialActivity extends FragmentActivity
{
    private boolean NotificationEnable = false;

    private ImageView ImageViewInbox;
    private ImageView ImageViewMoment;
    private ImageView ImageViewNotification;
    private ImageView ImageViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        RelativeLayout RelativeLayoutMain = new RelativeLayout(this);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setBackgroundResource(Misc.IsDark() ? R.color.GroundDark : R.color.GroundWhite);

        RelativeLayout.LayoutParams LinearLayoutMenuParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(56));
        LinearLayoutMenuParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        LinearLayout LinearLayoutMenu = new LinearLayout(this);
        LinearLayoutMenu.setLayoutParams(LinearLayoutMenuParam);
        LinearLayoutMenu.setBackgroundResource(Misc.IsDark() ? R.color.ActionBarDark : R.color.ActionBarWhite);
        LinearLayoutMenu.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayoutMenu.setId(Misc.generateViewId());

        RelativeLayoutMain.addView(LinearLayoutMenu);

        ImageViewInbox = new ImageView(this);
        ImageViewInbox.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        ImageViewInbox.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewInbox.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        ImageViewInbox.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { ChangePage(1); } });

        LinearLayoutMenu.addView(ImageViewInbox);

        ImageViewMoment = new ImageView(this);
        ImageViewMoment.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        ImageViewMoment.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewMoment.setPadding(Misc.ToDP(17), Misc.ToDP(17), Misc.ToDP(17), Misc.ToDP(17));
        ImageViewMoment.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { ChangePage(2); } });

        LinearLayoutMenu.addView(ImageViewMoment);

        ImageView ImageViewChat = new ImageView(this);
        ImageViewChat.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        ImageViewChat.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewChat.setImageResource(R.drawable.chat_gray);
        ImageViewChat.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        ImageViewChat.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { /* TODO Change Activity To Chat */ } });

        LinearLayoutMenu.addView(ImageViewChat);

        ImageViewNotification = new ImageView(this);
        ImageViewNotification.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        ImageViewNotification.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewNotification.setPadding(Misc.ToDP(17), Misc.ToDP(17), Misc.ToDP(17), Misc.ToDP(17));
        ImageViewNotification.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { ChangePage(3); } });

        LinearLayoutMenu.addView(ImageViewNotification);

        ImageViewProfile = new ImageView(this);
        ImageViewProfile.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        ImageViewProfile.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ImageViewProfile.setPadding(Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15), Misc.ToDP(15));
        ImageViewProfile.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { ChangePage(4); } });

        LinearLayoutMenu.addView(ImageViewProfile);

        RelativeLayout.LayoutParams ViewLineParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Misc.ToDP(1));
        ViewLineParam.addRule(RelativeLayout.ABOVE, LinearLayoutMenu.getId());

        View ViewLine = new View(this);
        ViewLine.setLayoutParams(ViewLineParam);
        ViewLine.setId(Misc.generateViewId());
        ViewLine.setBackgroundResource(Misc.IsDark() ? R.color.LineDark : R.color.LineWhite);

        RelativeLayoutMain.addView(ViewLine);

        RelativeLayout.LayoutParams FrameLayoutContentParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        FrameLayoutContentParam.addRule(RelativeLayout.ABOVE, ViewLine.getId());

        RelativeLayout RelativeLayoutContent = new RelativeLayout(this);
        RelativeLayoutContent.setLayoutParams(FrameLayoutContentParam);
        RelativeLayoutContent.setId(R.id.Container);

        RelativeLayoutMain.addView(RelativeLayoutContent);

        RelativeLayout RelativeLayoutContentFull = new RelativeLayout(this);
        RelativeLayoutContentFull.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutContentFull.setId(R.id.ContainerFull);

        RelativeLayoutMain.addView(RelativeLayoutContentFull);

        setContentView(RelativeLayoutMain);

        ChangePage(getIntent().getIntExtra("Tab", 4));
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (getIntent() == null)
            return;

        /*if (getIntent().getBooleanExtra("VideoCancel", false))
        {
            AndroidNetworking.forceCancel(getIntent().getStringExtra("VideoID"));

            NotificationManager Notify = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Notify != null)
                Notify.cancel(getIntent().getIntExtra("VideoID", 0));

            return;
        }*/

        switch (getIntent().getIntExtra("Type", 0))
        {
            case 1:
            {
                String Username = getIntent().getStringExtra("Data");
                GetManager().OpenView(new ProfileUI(), R.id.Container, "ProfileUI");
            }
            break;
            case 2:
            {
                String PostID = getIntent().getStringExtra("Data");
                GetManager().OpenView(new PostUI(PostID), R.id.Container, "PostUI");
            }
            break;
        }
    }

    private void ChangePage(int Page)
    {
        ImageViewInbox.setImageResource(R.drawable.inbox_gray);
        ImageViewMoment.setImageResource(R.drawable.moment_gray);
        ImageViewNotification.setImageResource(NotificationEnable ? R.drawable.notification_dot_gray : R.drawable.notification_gray);
        ImageViewProfile.setImageResource(R.drawable.profile_gray);

        String Tag;
        FragmentView Fragment;

        switch (Page)
        {
            default:
                Tag = "InboxUI";
                Fragment = new InboxUI();
                ImageViewInbox.setImageResource(R.drawable.inbox_blue);
                break;
            case 2:
                Tag = "MomentUI";
                Fragment = new MomentUI();
                ImageViewMoment.setImageResource(R.drawable.moment_blue);
                break;
            case 3:
                Tag = "NotificationUI";
                Fragment = new NotificationUI();
                NotificationEnable = false;
                ImageViewNotification.setImageResource(R.drawable.notification_gray);
                break;
            case 4:
                Tag = "ProfileUI";
                Fragment = new ProfileUI();
                ImageViewProfile.setImageResource(R.drawable.profile_blue);
                break;
        }

        GetManager().OpenView(Fragment, R.id.Container, Tag);
    }
}

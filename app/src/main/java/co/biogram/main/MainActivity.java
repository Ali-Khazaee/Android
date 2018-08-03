package co.biogram.main;

import android.os.Build;
import android.os.Bundle;
import android.widget.FrameLayout;

import co.biogram.main.handler.Misc;
import co.biogram.main.fragment.FragmentActivity;
import co.biogram.main.ui.social.Profile_Private_UI;

public class MainActivity extends FragmentActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getActionBar() != null)
            getActionBar().hide();

        if (Build.VERSION.SDK_INT > 20)
            getWindow().setStatusBarColor(Misc.Attr(R.attr.StatusColor));

        FrameLayout FrameLayoutMain = new FrameLayout(this);
        FrameLayoutMain.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        FrameLayoutMain.setBackgroundColor(Misc.Attr(R.attr.BgColor));
        FrameLayoutMain.setId(R.id.ContainerFull);

        setContentView(FrameLayoutMain);


        if (Misc.GetString("Key", "").equals(""))
        {

        }

        GetManager().OpenView(new Profile_Private_UI(), "Profile_Private_UI");

        FrameLayoutMain.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Misc.Toast("Salam Khobi ? chetori che mikoni che khabara? chi shode az invara gozari delo harja mikhaiy mibari");
            }
        }, 2000);

    }
}


/*
public class SocialActivity extends FragmentActivity
{
    private boolean NotificationAvailable = false;

    private ImageView ImageViewInbox;
    private ImageView ImageViewMoment;
    private ImageView ImageViewChat;
    private ImageView ImageViewNotification;
    private ImageView ImageViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Locale locale = new Locale("fa");
        Locale.setDefault(locale);

        Configuration Config = new Configuration();
        Config.locale = locale;

        getBaseContext().getResources().updateConfiguration(Config, getBaseContext().getResources().getDisplayMetrics());

        EmojiManager.install(new IosEmojiProvider());

        setTheme(Misc.GetBoolean("ThemeDark") ? R.style.AppThemeDark : R.style.AppThemeLight);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_activity);

        ImageViewInbox = findViewById(R.id.ImageViewInbox);
        ImageViewInbox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangePage(1);
            }
        });

        ImageViewMoment = findViewById(R.id.ImageViewMoment);
        ImageViewMoment.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangePage(2);
            }
        });

        ImageViewChat = findViewById(R.id.ImageViewChat);
        ImageViewChat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangePage(3);
            }
        });

        ImageViewNotification = findViewById(R.id.ImageViewNotification);
        ImageViewNotification.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangePage(4);
            }
        });

        ImageViewProfile = findViewById(R.id.ImageViewProfile);
        ImageViewProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ChangePage(5);
            }
        });

        ChangePage(5);
    }

    private void ChangePage(int Page)
    {
        ImageViewInbox.setImageResource(R.drawable.z_social_activity_inbox);
        ImageViewMoment.setImageResource(R.drawable.z_social_activity_moment);
        ImageViewNotification.setImageResource(NotificationAvailable ? R.drawable.z_social_activity_notification_available : R.drawable.z_social_activity_notification);
        ImageViewProfile.setImageResource(R.drawable.z_social_activity_profile);

        String Tag;
        FragmentView Fragment;

        switch (Page)
        {
            case 1:
                Tag = "InboxUI";
                Fragment = new InboxUI();
                ImageViewInbox.setImageResource(R.drawable.z_social_activity_inbox_blue);
                break;
            case 2:
                Tag = "MomentUI";
                Fragment = new MomentUI();
                ImageViewMoment.setImageResource(R.drawable.z_social_activity_moment_blue);
                break;
            case 3:
                Tag = "Chat_ListUI";
                Fragment = new Contact_List_UI();
                ImageViewChat.setImageResource(R.drawable.z_social_activity_chat);
                break;
            case 4:
                Tag = "NotificationUI";
                Fragment = new NotificationUI();
                NotificationAvailable = false;
                ImageViewNotification.setImageResource(R.drawable.z_social_activity_notification_blue);
                break;
            default:
                Tag = "Profile_UI";
                Fragment = new Profile_UI();
                ImageViewProfile.setImageResource(R.drawable.z_social_activity_profile_blue);
                break;
        }

        GetManager().OpenView(Fragment, Tag, false);
    }
}
*/

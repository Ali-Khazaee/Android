package co.biogram.main.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import co.biogram.main.R;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.chat.Chat_List_UI;
import co.biogram.main.ui.social.InboxUI;
import co.biogram.main.ui.social.MomentUI;
import co.biogram.main.ui.social.NotificationUI;
import co.biogram.main.ui.social.Profile_UI;
import co.biogram.main.fragment.FragmentView;
import co.biogram.main.fragment.FragmentActivity;

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
        setTheme(Misc.GetTheme());

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

        ImageViewProfile = findViewById(R.id.CircleImageViewProfile);
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
                Tag = "Chat_List_UI";
                Fragment = new Chat_List_UI();
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

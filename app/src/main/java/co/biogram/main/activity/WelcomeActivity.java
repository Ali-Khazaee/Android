package co.biogram.main.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.FrameLayout;

import co.biogram.main.fragment.FragmentActivity;
import co.biogram.main.R;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.ui.welcome.WelcomeUI;

public class WelcomeActivity extends FragmentActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (SharedHandler.GetBoolean(this, "IsLogin"))
        {
            SharedHandler.SetBoolean(this, "IsLogin", false);

            if (SharedHandler.GetString(this, "Activity").equals("Chat"))
                startActivity(new Intent(this, ChatActivity.class));
            else
                startActivity(new Intent(this, SocialActivity.class));

            finish();
            return;
        }

        if (Build.VERSION.SDK_INT > 20)
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.BlueLight));

        FrameLayout FrameLayoutMain = new FrameLayout(this);
        FrameLayoutMain.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        FrameLayoutMain.setId(R.id.WelcomeActivityContainer);

        setContentView(FrameLayoutMain);

        GetManager().OpenView(new WelcomeUI(), R.id.WelcomeActivityContainer, "WelcomeUI");
    }
}

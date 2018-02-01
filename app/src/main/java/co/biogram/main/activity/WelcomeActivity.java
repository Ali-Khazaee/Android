package co.biogram.main.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.WindowManager;
import android.widget.RelativeLayout;

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
            if (SharedHandler.GetString(this, "Activity").equals("Chat"))
                startActivity(new Intent(this, ChatActivity.class));
            else
                startActivity(new Intent(this, SocialActivity.class));

            finish();
            return;
        }

        if (Build.VERSION.SDK_INT > 20)
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.PrimaryColor));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        RelativeLayout RelativeLayoutMain = new RelativeLayout(this);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setId(R.id.ContainerFull);

        setContentView(RelativeLayoutMain);

        GetManager().OpenView(new WelcomeUI(), R.id.ContainerFull, "WelcomeUI");
    }
}

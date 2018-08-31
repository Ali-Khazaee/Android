package co.biogram.main.activity;

import android.content.res.Configuration;
import android.os.Bundle;

import java.util.Locale;

import co.biogram.main.R;
import co.biogram.main.fragment.FragmentActivity;
import co.biogram.main.ui.welcome.WelcomeUI;

public class WelcomeActivity extends FragmentActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Locale locale = new Locale("fa");
        Locale.setDefault(locale);

        Configuration Config = new Configuration();
        Config.locale = locale;

        getBaseContext().getResources().updateConfiguration(Config, getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.welcome_activity);

        GetManager().OpenView(new WelcomeUI(), "ss", false);

        //        if (Misc.GetBoolean("IsLogin"))
        //        {
        //            if (Misc.GetString("Activity").equals("Chat"))
        //                startActivity(new Intent(this, Message_UI.class));
        //            else
        //                startActivity(new Intent(this, SocialActivity.class));
        //
        //            finish();
        //            return;
        //        }
        //
        //        if (Build.VERSION.SDK_INT > 20)
        //            getWindow().setStatusBarColor(Misc.Color(R.color.Primary));
        //
        //        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //
        //        RelativeLayout RelativeLayoutMain = new RelativeLayout(this);
        //        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        //        RelativeLayoutMain.setId(R.id.ContainerFull);
        //
        //        setContentView(RelativeLayoutMain);
        //
        //        GetManager().OpenView(new WelcomeUI(), "ss", false);
    }
}

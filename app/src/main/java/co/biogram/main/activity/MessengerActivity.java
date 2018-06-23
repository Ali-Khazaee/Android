package co.biogram.main.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;

import co.biogram.main.R;
import co.biogram.main.handler.Misc;
import co.biogram.main.ui.messenger.Contact_List_UI;
import co.biogram.main.fragment.FragmentActivity;

import java.util.Locale;

public class MessengerActivity extends FragmentActivity
{
    @Override
    protected void onCreate(Bundle bundle)
    {
        Locale locale = new Locale("fa");
        Locale.setDefault(locale);

        Configuration Config = new Configuration();
        Config.locale = locale;

        getBaseContext().getResources().updateConfiguration(Config, getBaseContext().getResources().getDisplayMetrics());

        super.onCreate(bundle);

        setTheme(Misc.GetTheme());

        if (getActionBar() != null)
            getActionBar().hide();

        if (Build.VERSION.SDK_INT > 20)
            getWindow().setStatusBarColor(Misc.Attr(this, R.attr.StatusColor));

        FrameLayout FrameLayoutMain = new FrameLayout(this);
        FrameLayoutMain.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        FrameLayoutMain.setId(R.id.ContainerFull);

        setContentView(FrameLayoutMain);

        GetManager().OpenView(new Contact_List_UI(), "Contact_List_UI", true);
    }
}

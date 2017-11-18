package co.biogram.main.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

        AccountManager accountManager = (AccountManager) this.getSystemService(Context.ACCOUNT_SERVICE);
        Account account = new Account("Biogram", "co.biogram.main.service.AuthenticatorService");

        if (accountManager.addAccountExplicitly(account, null, null))
        {
            // Inform the system that this account supports sync
            ContentResolver.setIsSyncable(account, "auth", 1);
            // Inform the system that this account is eligible for auto sync when the network is up
            ContentResolver.setSyncAutomatically(account, "auth2", true);
            // Recommend a schedule for automatic synchronization. The system may modify this based
            // on other scheduled syncs and network utilization.
            ContentResolver.addPeriodicSync(account, "auth3", new Bundle(), 60);

        }



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
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.BlueLight));

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        RelativeLayout RelativeLayoutMain = new RelativeLayout(this);
        RelativeLayoutMain.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        RelativeLayoutMain.setId(R.id.WelcomeActivityContainer);

        setContentView(RelativeLayoutMain);

        GetManager().OpenView(new WelcomeUI(), R.id.WelcomeActivityContainer, "WelcomeUI");
    }
}

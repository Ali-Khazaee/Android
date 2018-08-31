package co.biogram.main.notification.helpers;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import co.biogram.main.App;

/**
 * Created by sohrab on 7/17/18.
 */

public class DismissPendingIntentBroadCast implements PendingIntentNotification
{
    private final Bundle mBundle;
    private final int mIdentifier;

    public DismissPendingIntentBroadCast(Bundle bundle, int identifier)
    {
        this.mBundle = bundle;
        this.mIdentifier = identifier;
    }

    @Override
    public PendingIntent onSettingPendingIntent()
    {
        Intent clickIntentBroadcast = new Intent();
        clickIntentBroadcast.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        clickIntentBroadcast.setPackage(App.getContext().getPackageName());
        if (mBundle != null)
        {
            clickIntentBroadcast.putExtras(mBundle);
        }

        return PendingIntent.getBroadcast(App.getContext(), mIdentifier, clickIntentBroadcast, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
package co.biogram.main.ui.notification.helpers;

/**
 * Created by sohrab on 7/17/18.
 */

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import co.biogram.main.App;

public class ClickPendingIntentActivity implements PendingIntentNotification
{
    private final Class<?> mActivity;
    private final Bundle mBundle;
    private final int mIdentifier;

    public ClickPendingIntentActivity(Class<?> activity, Bundle bundle, int identifier)
    {
        this.mActivity = activity;
        this.mBundle = bundle;
        this.mIdentifier = identifier;
    }

    @Override
    public PendingIntent onSettingPendingIntent()
    {
        Intent clickIntentActivity = new Intent(App.getContext(), mActivity);
        clickIntentActivity.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        clickIntentActivity.setPackage(App.getContext().getPackageName());

        if (mBundle != null)
        {
            clickIntentActivity.putExtras(mBundle);
        }
        return PendingIntent.getActivity(App.getContext(), mIdentifier, clickIntentActivity, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
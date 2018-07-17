package co.biogram.main.ui.notification.helpers;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import co.biogram.main.App;

/**
 * Created by sohrab on 7/17/18.
 */

public class DismissPendingIntentActivity implements PendingIntentNotification
{
    private final Class<?> mActivity;
    private final Bundle mBundle;
    private final int mIdentifier;

    public DismissPendingIntentActivity(Class<?> activity, Bundle bundle, int identifier)
    {
        this.mActivity = activity;
        this.mBundle = bundle;
        this.mIdentifier = identifier;
    }

    @Override
    public PendingIntent onSettingPendingIntent()
    {
        Intent dismissIntentActivity = new Intent(App.getContext(), mActivity);
        dismissIntentActivity.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        dismissIntentActivity.setPackage(App.getContext().getPackageName());
        if (mBundle != null)
        {
            dismissIntentActivity.putExtras(mBundle);
        }

        return PendingIntent.getActivity(App.getContext(), mIdentifier, dismissIntentActivity, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
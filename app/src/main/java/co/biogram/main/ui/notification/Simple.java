package co.biogram.main.ui.notification;

import android.support.v4.app.NotificationCompat;

/**
 * Created by sohrab on 7/17/18.
 */

public class Simple extends Builder
{
    public Simple(NotificationCompat.Builder builder, int identifier, String tag)
    {
        super(builder, identifier, tag);
    }

    @Override
    public void build()
    {
        super.build();
        super.notificationNotify();
    }
}
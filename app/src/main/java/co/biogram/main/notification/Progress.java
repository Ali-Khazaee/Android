package co.biogram.main.notification;

import android.support.v4.app.NotificationCompat;
import co.biogram.main.App;

/**
 * Created by sohrab on 7/17/18.
 */

public class Progress extends Builder
{
    public Progress(NotificationCompat.Builder builder, int identifier, String tag)
    {
        super(builder, identifier, tag);
    }

    @Override
    public void build()
    {
        super.build();
        super.notificationNotify();
    }

    public Progress update(int identifier, int progress, int max, boolean indeterminate)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(App.getContext());
        builder.setProgress(max, progress, indeterminate);

        notification = builder.build();
        notificationNotify(identifier);
        return this;
    }

    public Progress value(int progress, int max, boolean indeterminate)
    {
        builder.setProgress(max, progress, indeterminate);
        return this;
    }

}
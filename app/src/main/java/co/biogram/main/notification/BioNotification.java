package co.biogram.main.notification;

import android.app.NotificationManager;
import android.content.Context;

/**
 * Created by sohrab on 7/17/18.
 */

public class BioNotification
{
    private static final String TAG = BioNotification.class.getSimpleName();
    public static BioNotification mSingleton = null;
    public final Context mContext;
    public boolean shutdown;

    public BioNotification(Context context)
    {
        this.mContext = context;
    }

    public static BioNotification with(Context context)
    {
        if (mSingleton == null)
        {
            synchronized (BioNotification.class)
            {
                if (mSingleton == null)
                {
                    mSingleton = new Contractor(context).build();
                }
            }
        }
        return mSingleton;
    }

    public Load load()
    {
        return new Load();
    }

    public void cancel(int identifier)
    {
        NotificationManager notifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notifyManager.cancel(identifier);
    }

    public void cancel(String tag, int identifier)
    {
        NotificationManager notifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notifyManager.cancel(tag, identifier);
    }

    public void shutdown()
    {
        if (this == mSingleton)
        {
            throw new UnsupportedOperationException("Default singleton instance cannot be shutdown.");
        }
        if (shutdown)
        {
            return;
        }
        shutdown = true;
    }

    private static class Contractor
    {
        private final Context mContext;

        public Contractor(Context context)
        {
            if (context == null)
            {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.mContext = context.getApplicationContext();
        }

        public BioNotification build()
        {
            return new BioNotification(mContext);
        }
    }
}

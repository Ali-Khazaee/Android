package co.biogram.main.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;

import co.biogram.main.R;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.SharedHandler;
import co.biogram.main.handler.URLHandler;

public class NotificationService extends Service
{
    private Runnable runnable;
    private Handler handler;

    @Override
    public void onCreate()
    {
        MiscHandler.Debug("NotificationService Started");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_back_white)
                .setContentTitle("Notifications Example")
                .setContentText("This is a test notification");

        Intent notificationIntent = new Intent(this, NotificationService.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

        handler = new Handler();
        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                AndroidNetworking.post(URLHandler.GetURL("NotificationData"))
                .addHeaders("TOKEN", SharedHandler.GetString(NotificationService.this, "TOKEN"))
                .build()
                .getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        handler.postDelayed(runnable, 5000);
                    }

                    @Override
                    public void onError(ANError anError)
                    {
                        handler.postDelayed(runnable, 5000);
                    }
                });
            }
        };

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public void onDestroy()
    {
        MiscHandler.Debug("NotificationService Destroyed");
    }
}

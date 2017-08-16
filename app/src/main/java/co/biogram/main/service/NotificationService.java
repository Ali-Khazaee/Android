package co.biogram.main.service;

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

import org.json.JSONArray;
import org.json.JSONObject;

import co.biogram.main.R;
import co.biogram.main.activity.ActivityMain;
import co.biogram.main.handler.MiscHandler;
import co.biogram.main.handler.SharedHandler;

import me.leolin.shortcutbadger.ShortcutBadger;

public class NotificationService extends Service
{
    public static final String BROADCAST_ACTION_NEW = "co.biogram.NotificationService.New";

    private Runnable runnable;
    private Handler handler;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        final Context context = NotificationService.this;

        handler = new Handler();
        runnable = new Runnable()
        {
            @Override
            public void run()
            {
                if (SharedHandler.GetBoolean(context, "Notification"))
                {
                    handler.postDelayed(runnable, 5000);
                    return;
                }

                AndroidNetworking.post(MiscHandler.GetRandomServer("NotificationService"))
                .addHeaders("TOKEN", SharedHandler.GetString(context, "TOKEN"))
                .build()
                .getAsString(new StringRequestListener()
                {
                    @Override
                    public void onResponse(String Response)
                    {
                        try
                        {
                            JSONObject Result = new JSONObject(Response);

                            if (Result.getInt("Message") == 1000 && !Result.getString("Result").equals(""))
                            {
                                int Count = 0;
                                JSONArray ResultList = new JSONArray(Result.getString("Result"));

                                for (int I = 0; I < ResultList.length(); I++)
                                {
                                    JSONObject Notification = ResultList.getJSONObject(I);

                                    int Type = 2;
                                    String Data = Notification.getString("PostID");
                                    String Message = Notification.getString("Username") + " ";

                                    switch (Notification.getInt("Type"))
                                    {
                                        case 1: Message += context.getString(R.string.NotificationFragmentPostTag);          break;
                                        case 2: Message += context.getString(R.string.NotificationFragmentPostLike);         break;
                                        case 3:
                                            Type = 1;
                                            Data = Notification.getString("Username");
                                            Message += context.getString(R.string.NotificationFragmentFollow);
                                        break;
                                        case 4: Message += context.getString(R.string.NotificationFragmentCommentLike);      break;
                                        case 5: Message += context.getString(R.string.NotificationFragmentComment);          break;
                                        case 6: Message += context.getString(R.string.NotificationFragmentCommentTag);       break;
                                        case 7: Message += context.getString(R.string.NotificationFragmentUnfollow);         break;
                                    }

                                    Count++;
                                    CreateNotification(context, Message, Data, Type);
                                }

                                if (Count > 0)
                                {
                                    ShortcutBadger.applyCount(context, Count);
                                    sendBroadcast(new Intent(BROADCAST_ACTION_NEW));
                                }
                            }
                        }
                        catch (Exception e)
                        {
                            MiscHandler.Debug("NotificationService-RequestNotification: " + e.toString());
                        }

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

        handler.postDelayed(runnable, 5000);

        return START_STICKY;
    }

    private void CreateNotification(Context context, String Message, String Data, int Type)
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
        .setSmallIcon(R.drawable.ic_back_white)
        .setContentTitle("Biogram")
        .setContentText(Message);

        Intent intent = new Intent(context, ActivityMain.class);
        intent.putExtra("Data", Data);
        intent.putExtra("Type", Type);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        NotificationManager Manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Manager.notify(MiscHandler.GenerateViewID(), builder.build());
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}

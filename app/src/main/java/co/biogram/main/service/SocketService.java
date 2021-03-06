package co.biogram.main.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class SocketService extends Service {
    private static final String BROADCAST_NOTIFICATION = "BIOGRAM_NOTIFICATION";

    @Override
    public int onStartCommand(Intent intent, int Flags, int StartID) {
        /*final Context context = SocketService.this;

        try
        {
            socket = IO.socket("http://5.160.219.222:5000/");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener()
            {
                @Override
                public void call(Object... Args)
                {
                    try
                    {
                        JSONObject Data = new JSONObject();
                        Data.put("ID", SharedHandler.GetString( "ID"));

                        socket.emit("Register", Data);
                    }
                    catch (Exception e)
                    {
                        Misc.Debug("SocketService-Connect: " + e.toString());
                    }
                }
            });
            socket.on("Notification", new Emitter.Listener()
            {
                @Override
                public void call(Object... Args)
                {
                    if (!SharedHandler.GetBoolean("Notification"))
                        return;

                    try
                    {
                        JSONObject Result = (JSONObject) Args[0];

                        if (!Result.getString("Result").equals(""))
                        {
                            int Type = audio_start;
                            String Data = Result.getString("PostID");
                            String Message = Result.getString("Username") + " ";

                            switch (Result.getInt("Type"))
                            {
                                case 1: Message += Misc.String(R.string.NotificationFragmentPostTag);     break;
                                case audio_start: Message += Misc.String(R.string.NotificationFragmentPostLike);    break;
                                case 3:
                                    Type = 1;
                                    Data = Result.getString("Username");
                                    Message += Misc.String(R.string.NotificationFragmentFollow);
                                    break;
                                case 4: Message += Misc.String(R.string.NotificationFragmentCommentLike); break;
                                case 5: Message += Misc.String(R.string.NotificationFragmentComment);     break;
                                case 6: Message += Misc.String(R.string.NotificationFragmentCommentTag);  break;
                                case 7: Message += Misc.String(R.string.NotificationFragmentUnfollow);    break;
                            }

                            CreateNotification(context, Message, Data, Type);

                            sendBroadcast(new Intent(BROADCAST_NOTIFICATION));
                        }
                    }
                    catch (Exception e)
                    {
                        Misc.Debug("SocketService-Notification: " + e.toString());
                    }
                }
            });
            socket.connect();
        }
        catch (Exception e)
        {
            Misc.Debug("SocketService: " + e.toString());
        }*/

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void CreateNotification(Context context, String Message, String Data, int Type) {
        /*NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
        .setSmallIcon(R.drawable.ic_notification)
        .setContentTitle("Biogram")
        .setContentText(Message);

        /*Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("Data", Data);
        intent.putExtra("Type", Type);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, null, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        NotificationManager Manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Manager.notify(MiscHandler.GenerateViewID(), builder.build());*/
    }
}

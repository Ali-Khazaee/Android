package co.biogram.main.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;

import co.biogram.main.R;

/**
 * Created by sohrab on 7/13/18.
 */

public class NotificationService extends Service {


    private static NotificationCompat.Builder baseNotificationBuilder(Context context, String title, String message, @Nullable Class<?> cls) {

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.z_social_activity_notification_blue)
                .setContentTitle(title)
                .setContentText(message)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));


        PendingIntent pendingIntent;
        if (cls != null) {
            final Intent intent = new Intent();
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            final Intent intent = new Intent(context, cls.getClass());
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        notification.setContentIntent(pendingIntent);
        notification.setAutoCancel(true);
        return notification;

    }

    private static NotificationCompat.Builder baseMessageNotificationBuilder(Context context, @Nullable Class<?> cls) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.z_social_activity_notification_blue)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        PendingIntent pendingIntent;
        if (cls != null) {
            final Intent intent = new Intent();
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            final Intent intent = new Intent(context, cls.getClass());
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        notification.setContentIntent(pendingIntent);
        notification.setAutoCancel(true);
        return notification;
    }

//    private static NotificationCompat.Builder baseMessageNotificationBuilder(Context context, @Nullable Class<?> cls, ArrayList<UserMessage> userMessages) {
//        NotificationCompat.Builder notification = baseMessageNotificationBuilder(context, cls);
//
//        NotificationCompat.MessagingStyle messageStyle = new NotificationCompat.MessagingStyle().setConversationTitle();
//
//        for (UserMessage userMessage : userMessages) {
//            for (String message : userMessage.Messages) {
//                notification.a
//            }
//        }
//    }

    private static NotificationCompat.Builder baseNotificationBuilder(Context context, String title, String message, String color, @Nullable Class<?> cls) {

        NotificationCompat.Builder notification = baseNotificationBuilder(context, title, message, cls);
        notification.setColor(Color.parseColor(color));

        return notification;

    }

    private static NotificationCompat.Builder baseNotificationBuilder(Context context, String title, String message, int color, @Nullable Class<?> cls) {

        NotificationCompat.Builder notification = baseNotificationBuilder(context, title, message, cls);
        notification.setColor(color);

        return notification;

    }


    public static NotificationCompat.Builder buildNotification(Context context, String title, String message, Bitmap icon) {

        NotificationCompat.Builder notification = baseNotificationBuilder(context, title, message, null);

        notification.setLargeIcon(icon);

        return notification;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class UserMessage {
        public String Username;
        public ArrayList<String> Messages;

        public UserMessage(String username, ArrayList<String> messages) {
            Username = username;
            Messages = messages;
        }
    }
}

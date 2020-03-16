package com.maha.uds.Chat;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.maha.uds.Chat.ChatActivity;
import com.maha.uds.R;


public class MyNotificationManager {

    /** MyNotificationManager Channel Variables **/
    public static final String NOTIFICATION_CHANNEL_ID = "MyCarTracksID";
    public static final String NOTIFICATION_CHANNEL_NAME = "MyCarTracksName";
    public static final String NOTIFICATION_CHANNEL_DESCRIPTION = "MyCarTracksDescription";


    /** Creating Channel for Notifications **/
    public static void createChannelAndHandleNotifications(Context context) {
        try{
            Log.d("CodeTracing","createChannelAndHandleNotifications");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        NOTIFICATION_CHANNEL_ID,
                        NOTIFICATION_CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);
                channel.setShowBadge(true);

                NotificationManager notificationManager =  context.getSystemService(NotificationManager.class);
                assert notificationManager !=null;
                notificationManager.createNotificationChannel(channel);
            }

        }catch (Exception ex) {
            Log.d("CodeTracing","createChannelAndHandleNotifications  "+ex.getMessage());
        }
    }

    /** Method to send notification, Arguments (MyNotificationManager Title, MyNotificationManager Message,
     *  The Context of the BroadcastReceiver) **/
    public static void sendNotification(String title, String message, Context context) {

        /** Serious of intents that starts when notification is pressed **/
        Intent intent = new Intent(context, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(context, 563,
                intent, PendingIntent.FLAG_ONE_SHOT);


        /** Creating the notification **/
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setSound(soundUri)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
    }


}

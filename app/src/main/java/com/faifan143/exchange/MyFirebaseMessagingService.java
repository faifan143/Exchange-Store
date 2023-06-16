package com.faifan143.exchange;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        // TODO: Send the token to your server.
        SharedPreferences sharedPref = getSharedPreferences("my_preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("MyFCMToken",token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage == null) {
            return;
        }

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification == null) {
            return;
        }

        String title = notification.getTitle();
        String body = notification.getBody();

        String CHANNEL_ID = "HEADS_UP_NOTIFICATION";
        NotificationChannel notificationChannel = new NotificationChannel(
                CHANNEL_ID,
                "HEADS_UP_NOTIFICATION",
                NotificationManager.IMPORTANCE_HIGH
        );
        getSystemService(NotificationManager.class).createNotificationChannel(notificationChannel);

        Notification.Builder notificationBuilder = new Notification.Builder(
                this,CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true);

        NotificationManagerCompat.from(this).notify(1, notificationBuilder.build());
    }

}

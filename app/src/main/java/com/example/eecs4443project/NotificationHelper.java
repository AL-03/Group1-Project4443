package com.example.eecs4443project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {

    public static final String CHANNEL_ID = "reminder_channel";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "Reminders";
            String description = "Reminder notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager manager =
                    context.getSystemService(NotificationManager.class);

            manager.createNotificationChannel(channel);
        }
    }

    public static void showNotification(Context context,
                                        String title,
                                        String message,
                                        int id) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_dialog_info)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

        NotificationManagerCompat.from(context)
                .notify(id, builder.build());
    }
}
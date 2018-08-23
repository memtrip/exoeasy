package com.memtrip.sample_java;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.memtrip.exoeasy.sample_java.R;

class AppNotificationChannel {

    private final Context context;
    private final NotificationManager notificationManager;

    AppNotificationChannel(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    void create() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel(
                    context.getString(R.string.app_notification_channel_id),
                    context.getString(R.string.app_notification_channel_name),
                    NotificationManager.IMPORTANCE_LOW));
        }
    }
}

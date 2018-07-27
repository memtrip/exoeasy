package com.memtrip.exoeasy.sample

import android.app.NotificationManager
import android.app.NotificationChannel
import android.content.Context
import android.os.Build

class AppNotificationChannel(
    private val context: Context,
    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
) {

    fun create() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(NotificationChannel(
                context.getString(R.string.app_notification_channel_id),
                context.getString(R.string.app_notification_channel_name),
                NotificationManager.IMPORTANCE_LOW))
        }
    }
}

package com.memtrip.exoeasy.notification

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.memtrip.exoeasy.AudioResource

import com.memtrip.exoeasy.broadcast.AudioState

class StreamingNotificationFactory<A : AudioResource>(
    private val config: NotificationConfig,
    private val audioStateRemoteView: AudioStateRemoteView<A>,
    private val context: Context
) {

    fun update(intent: Intent) {
        notificationManager.notify(EXTRA_PLAYER_NOTIFICATION_TYPE, create(AudioState.playerState(intent)))
    }

    private fun create(audioState: AudioState): Notification {
        return NotificationCompat.Builder(context, config.channelId)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setSmallIcon(config.statusBarIcon)
            .setCustomContentView(audioStateRemoteView.render(audioState))
            .setContentIntent(audioStateRemoteView.destination.createActivityIntent())
            .setOngoing(true)
            .build()
    }

    private val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val EXTRA_PLAYER_NOTIFICATION_TYPE = 0x1337
    }
}
package com.memtrip.exoeasy.notification

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.memtrip.exoeasy.AudioResource

import com.memtrip.exoeasy.broadcast.AudioState

/**
 * Copyright 2013-present memtrip LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class StreamingNotificationFactory<A : AudioResource>(
    private val config: NotificationConfig,
    private val audioStateRemoteView: AudioStateRemoteView<A>,
    private val context: Context,
    private val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
) {

    fun update(intent: Intent) {
        if (config.showNotification) {
            notificationManager.notify(EXTRA_PLAYER_NOTIFICATION_TYPE, create(AudioState.playerState(intent)))
        }
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

    companion object {
        private const val EXTRA_PLAYER_NOTIFICATION_TYPE = 0x1337
    }
}
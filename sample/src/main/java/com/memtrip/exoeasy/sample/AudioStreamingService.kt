package com.memtrip.exoeasy.sample

import android.app.Activity

import com.memtrip.exoeasy.notification.Destination
import com.memtrip.exoeasy.notification.NotificationConfig
import com.memtrip.exoeasy.player.StreamingService
import com.memtrip.exoeasy.notification.AudioStateRemoteView

import com.memtrip.exoeasy.remoteview.PlayPauseStopRemoteView
import kotlin.reflect.KClass

class AudioStreamingService : StreamingService<HttpAudioResource>() {

    override fun audioResourceIntent(): HttpAudioResourceIntent = HttpAudioResourceIntent()

    override fun notificationConfig(): NotificationConfig {
        return NotificationConfig(
            true,
            getString(R.string.app_notification_channel_id),
            android.R.drawable.ic_media_play)
    }

    override fun audioStateRemoteView(
        destination: Destination<HttpAudioResource>
    ): AudioStateRemoteView<HttpAudioResource> {
        return PlayPauseStopRemoteView(this, destination)
    }

    override fun activityDestination(): KClass<out Activity> {
        return AudioPlayingActivity::class
    }
}
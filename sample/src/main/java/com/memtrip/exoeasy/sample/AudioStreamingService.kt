package com.memtrip.exoeasy.sample

import android.app.Activity

import com.memtrip.exoeasy.notification.Destination
import com.memtrip.exoeasy.notification.NotificationConfig
import com.memtrip.exoeasy.player.StreamingService
import com.memtrip.exoeasy.notification.PlayBackStateRemoteView

import com.memtrip.exoeasy.remoteview.PlayPauseStopRemoteView
import kotlin.reflect.KClass

class AudioStreamingService : StreamingService<HttpAudioResource>() {

    override fun audioResourceIntent(): HttpAudioResourceIntent = HttpAudioResourceIntent()

    override fun notificationConfig(): NotificationConfig {
        return NotificationConfig(
            false)
    }

    override fun playBackStateRemoteView(
        destination: Destination<HttpAudioResource>
    ): PlayBackStateRemoteView<HttpAudioResource> {
        return PlayPauseStopRemoteView(this, destination)
    }

    override fun activityDestination(): KClass<out Activity> {
        return AudioPlayingActivity::class
    }
}
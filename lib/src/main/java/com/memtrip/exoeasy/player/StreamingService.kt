package com.memtrip.exoeasy.player

import android.app.Activity
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.memtrip.exoeasy.AudioResource
import com.memtrip.exoeasy.AudioResourceIntent
import com.memtrip.exoeasy.NotificationInfo
import com.memtrip.exoeasy.broadcast.BroadcastOnPlayerStateChanged
import com.memtrip.exoeasy.notification.Destination
import com.memtrip.exoeasy.notification.NotificationConfig

import com.memtrip.exoeasy.notification.StreamingNotificationFactory
import com.memtrip.exoeasy.notification.PlayBackStateRemoteView
import kotlin.reflect.KClass

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
abstract class StreamingService<A : AudioResource> : Service() {

    private lateinit var playerFactory: PlayerFactory<A>
    private lateinit var becomingNoisyInterrupt: InterruptBecomingNoisy
    private lateinit var audioFocusInterrupt: InterruptAudioFocus

    private var player: Player? = null

    /**
     * Used to provide ExoPlayer with; `url`, `userAgent` and `trackProgress`
     */
    protected abstract fun audioResourceIntent(): AudioResourceIntent<A>

    /**
     * Configure the playback notification
     */
    protected open fun notificationConfig(): NotificationConfig {
        return NotificationConfig(false)
    }

    /**
     * Provide a custom RemoteView for the playback notification, one of the preset PlayBackStateRemoteView
     * implementations can also be used.
     * @see com.memtrip.exoeasy.remoteview.PlayPauseRemoteView
     * @see com.memtrip.exoeasy.remoteview.PlayPauseStopRemoteView
     */
    protected open fun playBackStateRemoteView(
        destination: Destination<A>
    ): PlayBackStateRemoteView<A> {
        throw IllegalStateException("playBackStateRemoteView() must be overridden in " +
            "com.memtrip.exoeasy.player.StreamingService " +
            "when you have showNotification set to true in NotificationConfig")
    }

    /**
     * The destination activity to navigate to when the playback notification is selected
     */
    protected open fun activityDestination(): KClass<out Activity> {
        throw IllegalStateException("activityDestination() must be overridden in " +
            "com.memtrip.exoeasy.player.StreamingService " +
            "when you have showNotification set to true in NotificationConfig")
    }

    /**
     * Configure use your own instance of ExoPlayer
     */
    protected open fun exoPlayer(): ExoPlayer {
        return ExoPlayerFactory.newSimpleInstance(
            DefaultRenderersFactory(this),
            DefaultTrackSelector(AdaptiveTrackSelection.Factory(DefaultBandwidthMeter())),
            DefaultLoadControl())
    }

    /**
     * Configure your own instance of MediaSource
     */
    protected open fun mediaSource(
        url: String,
        userAgent: String,
        onPlayerStateChanged: OnPlayerStateChanged
    ): MediaSource {
        return ExtractorMediaSource(
            Uri.parse(url),
            DefaultHttpDataSourceFactory(userAgent, null),
            DefaultExtractorsFactory(),
            Handler(Looper.getMainLooper()),
            ExtractorMediaSource.EventListener {
                onPlayerStateChanged.onBufferingError(it)
            })
    }

    override fun onCreate() {
        super.onCreate()

        playerFactory = PlayerFactory(this)

        becomingNoisyInterrupt = InterruptBecomingNoisy({
            let { player }?.pause()
        }, application).register()

        audioFocusInterrupt = InterruptAudioFocus({
            let { player }?.pause()
        }, application).register()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        onIntentReceived(intent)
        return Service.START_NOT_STICKY
    }

    private fun onIntentReceived(intent: Intent) {

        val notificationInfo = audioResourceIntent().notificationInfo(intent)

        val audioResource = audioResourceIntent().get(intent)

        val onPlayerStateChanged = BroadcastOnPlayerStateChanged(
            audioResource.url,
            StreamingNotificationFactory(
                notificationConfig(), {
                    playBackStateRemoteView(destination(
                        audioResource,
                        audioResourceIntent(),
                        notificationInfo).value)
                },
                this),
            LocalBroadcastManager.getInstance(this),
            Handler(Looper.getMainLooper()))

        player = playerFactory.get(
            audioResource,
            player,
            exoPlayer(),
            mediaSource(audioResource.url, audioResource.userAgent, onPlayerStateChanged),
            onPlayerStateChanged
        )

        PlayBackAction.perform(player!!, intent)
    }

    private fun destination(
        audioResource: A,
        audioResourceIntent: AudioResourceIntent<A>,
        info: NotificationInfo
    ): Lazy<Destination<A>> {
        return lazy { Destination(
            audioResource,
            audioResourceIntent,
            info,
            activityDestination(),
            this.javaClass.kotlin,
            this) }
    }

    override fun onDestroy() {
        release()
        super.onDestroy()
    }

    private fun release() {
        becomingNoisyInterrupt.unregister()
        audioFocusInterrupt.unregister()
        let { player }?.release()
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        stopSelf()
        super.onTaskRemoved(rootIntent)
    }

    override fun onBind(intent: Intent) = null
}
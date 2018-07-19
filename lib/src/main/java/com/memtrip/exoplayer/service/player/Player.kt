package com.memtrip.exoplayer.service.player

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper

import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player

import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource

import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector

import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.memtrip.exoplayer.service.BuildConfig

internal class Player constructor(
    internal val streamUrl: String,
    onPlayerStateListener: OnPlayerStateChanged,
    context: Context,
    private val player: ExoPlayer = ExoPlayerFactory.newSimpleInstance(
        DefaultRenderersFactory(context),
        DefaultTrackSelector(AdaptiveTrackSelection.Factory(DefaultBandwidthMeter())),
        DefaultLoadControl()
    ),
    progressTick: PlayerProgressTick = PlayerProgressTick(player, onPlayerStateListener),
    eventListener: PlayerEventListener = PlayerEventListener(
        onPlayerStateListener,
        progressTick
    ),
    private val mediaSource: MediaSource = ExtractorMediaSource(
        Uri.parse(streamUrl),
        DefaultHttpDataSourceFactory(
    BuildConfig.APPLICATION_ID + "/" + BuildConfig.VERSION_NAME,
    null
        ),
        DefaultExtractorsFactory(),
        Handler(Looper.getMainLooper()),
        ExtractorMediaSource.EventListener {
            onPlayerStateListener.onBufferingError(it)
        }
    )
) {

    private var prepared: Boolean = false

    init {
        eventListener.onStop = {
            prepared = false
        }
        player.addListener(eventListener)
    }

    internal fun play() {
        if (!prepared) {
            player.prepare(mediaSource)
            prepared = true
        }

        player.playWhenReady = true
    }

    internal fun pause() {
        player.playWhenReady = false
    }

    internal fun seek(progress: Int) {
        val seekPosition = progress * player.duration / 100
        player.seekTo(seekPosition)
    }

    internal fun release() {
        player.stop()
        player.release()
    }
}
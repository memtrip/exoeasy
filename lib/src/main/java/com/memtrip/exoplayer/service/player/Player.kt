package com.memtrip.exoplayer.service.player

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper

import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player

import com.google.android.exoplayer2.SimpleExoPlayer

import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource

import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector

import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.memtrip.exoplayer.service.BuildConfig

internal class Player constructor(
    streamUrl: String,
    onPlayerStateListener: OnPlayerStateChanged,
    context: Context,
    eventListener: Player.EventListener = PlayerEventListener(onPlayerStateListener),
    private val player: SimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
        DefaultRenderersFactory(context),
        DefaultTrackSelector(AdaptiveTrackSelection.Factory(DefaultBandwidthMeter())),
        DefaultLoadControl()
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

    init {
        player.addListener(eventListener)
    }

    internal fun prepare() {
        player.prepare(mediaSource)
        player.playWhenReady = true
    }

    internal fun release() {
        player.stop()
        player.release()
    }

    internal fun pause() {
        player.playWhenReady = false
    }
}
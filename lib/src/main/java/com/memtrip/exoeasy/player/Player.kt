package com.memtrip.exoeasy.player

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper

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

internal class Player constructor(
    internal val config: PlayerConfig,
    onPlayerStateListener: OnPlayerStateChanged,
    context: Context,
    private val exoPlayer: ExoPlayer = ExoPlayerFactory.newSimpleInstance(
        DefaultRenderersFactory(context),
        DefaultTrackSelector(AdaptiveTrackSelection.Factory(DefaultBandwidthMeter())),
        DefaultLoadControl()),
    private val progressTracker: PlayerProgressTracker = PlayerProgressTracker(
        config.trackProgress,
        config.streamUrl,
        context),
    progressTick: PlayerProgressTick = PlayerProgressTick(
        exoPlayer,
        progressTracker,
        onPlayerStateListener),
    eventListener: PlayerEventListener = PlayerEventListener(
        onPlayerStateListener,
        progressTick),
    private val mediaSource: MediaSource = ExtractorMediaSource(
        Uri.parse(config.streamUrl),
        DefaultHttpDataSourceFactory(config.userAgent, null),
        DefaultExtractorsFactory(),
        Handler(Looper.getMainLooper()),
        ExtractorMediaSource.EventListener {
            onPlayerStateListener.onBufferingError(it)
        })
) {

    private var prepared: Boolean = false

    init {
        eventListener.onStop = {
            progressTracker.clear()
            prepared = false
        }

        exoPlayer.addListener(eventListener)
    }

    internal fun play() {
        if (!prepared) {
            exoPlayer.prepare(mediaSource)
            prepared = true

            val duration = progressTracker.currentProgress(config.streamUrl)
            if (duration > 0) {
                exoPlayer.seekTo(progressTracker.currentProgress(config.streamUrl))
            }
        }

        exoPlayer.playWhenReady = true
    }

    internal fun pause() {
        exoPlayer.playWhenReady = false
    }

    internal fun seek(percentage: Int) {
        val seekPosition = percentage * exoPlayer.duration / 100
        exoPlayer.seekTo(seekPosition)
    }

    internal fun stop() {
        exoPlayer.stop()
        prepared = false
    }

    internal fun release() {
        exoPlayer.stop()
        exoPlayer.release()
    }
}
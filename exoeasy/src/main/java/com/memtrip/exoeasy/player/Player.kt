package com.memtrip.exoeasy.player

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper

import com.google.android.exoplayer2.ExoPlayer

import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource

import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.memtrip.exoeasy.AudioResource

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
internal class Player constructor(
    internal val audioResource: AudioResource,
    onPlayerStateListener: OnPlayerStateChanged,
    context: Context,
    private val exoPlayer: ExoPlayer,
    private val mediaSource: MediaSource = ExtractorMediaSource(
        Uri.parse(audioResource.url),
        DefaultHttpDataSourceFactory(audioResource.userAgent, null),
        DefaultExtractorsFactory(),
        Handler(Looper.getMainLooper()),
        ExtractorMediaSource.EventListener {
            onPlayerStateListener.onBufferingError(it)
        }),
    private val progressTracker: PlayerProgressTracker = PlayerProgressTracker(
        audioResource.trackProgress,
        audioResource.url,
        context),
    progressTick: PlayerProgressTick = PlayerProgressTick(
        exoPlayer,
        progressTracker,
        onPlayerStateListener),
    private val eventListener: PlayerEventListener = PlayerEventListener(
        onPlayerStateListener,
        progressTick)
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

            val duration = progressTracker.currentProgress(audioResource.url)
            if (duration > 0) {
                exoPlayer.seekTo(progressTracker.currentProgress(audioResource.url))
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

    internal fun seek(positionInMs: Long) {
        exoPlayer.seekTo(positionInMs)
    }

    internal fun tickle() {
        eventListener.onPlayerStateChanged(exoPlayer.playWhenReady, exoPlayer.playbackState)
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
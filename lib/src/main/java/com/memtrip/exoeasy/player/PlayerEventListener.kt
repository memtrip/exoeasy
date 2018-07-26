package com.memtrip.exoeasy.player

import com.google.android.exoplayer2.ExoPlaybackException

import com.google.android.exoplayer2.Player as PlayerState

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
internal class PlayerEventListener constructor(
    private val onPlayerStateListener: OnPlayerStateChanged,
    private val playerProgressTick: PlayerProgressTick
) : PlayerEventListenerAdapter {

    internal lateinit var onStop: () -> Unit

    override fun onLoadingChanged(isLoading: Boolean) {
        if (isLoading) {
            onPlayerStateListener.onBuffering()
        }
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {

        playerProgressTick.stop()

        if (playbackState == PlayerState.STATE_BUFFERING) {
            onPlayerStateListener.onBuffering()
        } else {
            if (playbackState == PlayerState.STATE_READY) {
                if (playWhenReady) {
                    playerProgressTick.start()
                    onPlayerStateListener.onPlay()
                } else {
                    onPlayerStateListener.onPause()
                }
            } else if (playbackState == PlayerState.STATE_ENDED) {
                onStop()
                onPlayerStateListener.onCompleted()
            } else {
                onStop()
                onPlayerStateListener.onProgress(0, 0, 0)
                onPlayerStateListener.onStop()
            }
        }
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        onPlayerStateListener.onBufferingError(error)
    }
}
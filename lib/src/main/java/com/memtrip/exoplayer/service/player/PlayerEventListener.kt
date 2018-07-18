package com.memtrip.exoplayer.service.player

import com.google.android.exoplayer2.ExoPlaybackException

internal class PlayerEventListener constructor(
    private val onPlayerStateListener: OnPlayerStateChanged
) : PlayerEventListenerAdapter {

    override fun onLoadingChanged(isLoading: Boolean) {
        if (isLoading) {
            onPlayerStateListener.onBuffering()
        }
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == com.google.android.exoplayer2.Player.STATE_BUFFERING) {
            onPlayerStateListener.onBuffering()
        } else {
            if (playbackState == com.google.android.exoplayer2.Player.STATE_READY) {
                if (playWhenReady) {
                    onPlayerStateListener.onPlay()
                } else {
                    onPlayerStateListener.onPause()
                }
            } else if (playbackState == com.google.android.exoplayer2.Player.STATE_ENDED) {
                onPlayerStateListener.onCompleted()
            } else {
                onPlayerStateListener.onStop()
            }
        }
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        onPlayerStateListener.onBufferingError(error)
    }
}
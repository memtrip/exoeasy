package com.memtrip.exoplayer.service.player

import com.google.android.exoplayer2.ExoPlaybackException

import com.google.android.exoplayer2.Player as PlayerState

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
                onPlayerStateListener.onCompleted()
            } else {
                onStop()
                onPlayerStateListener.onStop()
            }
        }
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        onPlayerStateListener.onBufferingError(error)
    }
}
package com.memtrip.exoeasy.broadcast

import android.content.Intent

sealed class AudioState {
    object Buffering : AudioState()
    object Play : AudioState()
    object Pause : AudioState()
    object Stop : AudioState()
    object Completed : AudioState()
    data class Progress(
        val percentage: Int,
        val currentPosition: Long,
        val duration: Long
    ) : AudioState()
    data class BufferingError(
        val throwable: Throwable
    ) : AudioState()

    companion object {
        fun playerState(intent: Intent): AudioState = when (BroadcastOnPlayerStateChanged.broadcastType(intent)) {
            BroadcastType.BUFFERING -> AudioState.Buffering
            BroadcastType.PLAY -> AudioState.Play
            BroadcastType.PAUSE -> AudioState.Pause
            BroadcastType.STOP -> AudioState.Stop
            BroadcastType.COMPLETED -> AudioState.Completed
            BroadcastType.PROGRESS -> {
                AudioState.Progress(
                    BroadcastOnPlayerStateChanged.progressPercentage(intent),
                    BroadcastOnPlayerStateChanged.progressPosition(intent),
                    BroadcastOnPlayerStateChanged.progressDuration(intent))
            }
            BroadcastType.BUFFERING_ERROR -> {
                AudioState.BufferingError(BroadcastOnPlayerStateChanged.bufferError(intent))
            }
        }
    }
}
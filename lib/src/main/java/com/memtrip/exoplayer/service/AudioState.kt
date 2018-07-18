package com.memtrip.exoplayer.service

sealed class AudioState {
    object Buffering : AudioState()
    object Play : AudioState()
    object Pause : AudioState()
    object Stop : AudioState()
    object Completed : AudioState()
    data class Progress(
        val percentage: Int,
        val currentPosition: Int,
        val duration: Int
    ) : AudioState()
    data class BufferingError(
        val throwable: Throwable
    ) : AudioState()
}
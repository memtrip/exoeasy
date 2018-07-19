package com.memtrip.exoeasy

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
}
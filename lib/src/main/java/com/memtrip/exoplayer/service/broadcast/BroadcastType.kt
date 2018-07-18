package com.memtrip.exoplayer.service.broadcast

internal enum class BroadcastType {
    BUFFERING,
    PLAY,
    PAUSE,
    STOP,
    COMPLETED,
    PROGRESS,
    BUFFERING_ERROR
}
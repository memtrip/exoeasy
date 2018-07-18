package com.memtrip.exoplayer.service.player

internal interface OnPlayerStateChanged {
    fun onBuffering()
    fun onPlay()
    fun onPause()
    fun onStop()
    fun onCompleted()
    fun onProgress(percentage: Int, currentPosition: Int, duration: Int)
    fun onBufferingError(throwable: Throwable)
}
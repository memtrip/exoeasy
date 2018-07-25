package com.memtrip.exoeasy.player

interface OnPlayerStateChanged {
    fun onBuffering()
    fun onPlay()
    fun onPause()
    fun onStop()
    fun onCompleted()
    fun onProgress(percentage: Int, currentPosition: Long, duration: Long)
    fun onBufferingError(throwable: Throwable)
}
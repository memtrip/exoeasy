package com.memtrip.exoplayer.service.player

import android.os.Handler
import com.google.android.exoplayer2.ExoPlayer

internal class PlayerProgressTick(
    private val player: ExoPlayer,
    private val onPlayerStateChanged: OnPlayerStateChanged,
    private val handler: Handler = Handler()
) {

    private var inProgress: Boolean = false

    internal fun start() {
        inProgress = true
        tick()
    }

    internal fun stop() {
        inProgress = false
    }

    private fun tick() {
        if (inProgress) {
            report()
            handler.postDelayed({
                tick()
            }, FRAME_RATE)
        }
    }

    private fun report() {
        val duration = (player.duration / 1000)
        val position = (player.currentPosition / 1000)
        val percentage: Float = position.toFloat() / duration.toFloat() * 100

        onPlayerStateChanged.onProgress(percentage.toInt(), position, duration)
    }

    companion object {
        const val FRAME_RATE: Long = 1000
    }
}
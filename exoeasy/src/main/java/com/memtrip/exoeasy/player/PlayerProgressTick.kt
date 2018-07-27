package com.memtrip.exoeasy.player

import android.os.Handler
import com.google.android.exoplayer2.ExoPlayer

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
internal class PlayerProgressTick(
    private val player: ExoPlayer,
    private val progressTracker: PlayerProgressTracker,
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
            progressTracker.track(player.currentPosition)
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
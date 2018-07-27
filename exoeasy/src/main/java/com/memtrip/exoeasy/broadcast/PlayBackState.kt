package com.memtrip.exoeasy.broadcast

import android.content.Intent

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
sealed class PlayBackState {
    object Buffering : PlayBackState()
    object Play : PlayBackState()
    object Pause : PlayBackState()
    object Stop : PlayBackState()
    object Completed : PlayBackState()
    data class Progress(
        val percentage: Int,
        val currentPosition: Long,
        val duration: Long
    ) : PlayBackState()
    data class BufferingError(
        val throwable: Throwable
    ) : PlayBackState()

    companion object {
        fun playerState(intent: Intent): PlayBackState = when (BroadcastOnPlayerStateChanged.broadcastType(intent)) {
            BroadcastType.BUFFERING -> PlayBackState.Buffering
            BroadcastType.PLAY -> PlayBackState.Play
            BroadcastType.PAUSE -> PlayBackState.Pause
            BroadcastType.STOP -> PlayBackState.Stop
            BroadcastType.COMPLETED -> PlayBackState.Completed
            BroadcastType.PROGRESS -> {
                PlayBackState.Progress(
                    BroadcastOnPlayerStateChanged.progressPercentage(intent),
                    BroadcastOnPlayerStateChanged.progressPosition(intent),
                    BroadcastOnPlayerStateChanged.progressDuration(intent))
            }
            BroadcastType.BUFFERING_ERROR -> {
                PlayBackState.BufferingError(BroadcastOnPlayerStateChanged.bufferError(intent))
            }
        }
    }
}
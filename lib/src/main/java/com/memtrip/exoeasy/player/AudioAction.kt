package com.memtrip.exoeasy.player

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
enum class AudioAction {
    PLAY,
    PAUSE,
    STOP,
    SEEK,
    TICKLE;

    companion object {
        private const val EXTRA_AUDIO_ACTION_TYPE = "EXTRA_AUDIO_ACTION_TYPE"
        private const val EXTRA_AUDIO_SEEK_PERCENTAGE = "EXTRA_AUDIO_SEEK_PERCENTAGE"

        internal fun perform(player: Player, intent: Intent) {
            return when (audioAction(intent)) {
                PLAY -> player.play()
                PAUSE -> player.pause()
                STOP -> player.stop()
                SEEK -> player.seek(audioSeekPercentage(intent))
                TICKLE -> player.tickle()
            }
        }

        private fun audioAction(intent: Intent): AudioAction {
            return intent.getSerializableExtra(EXTRA_AUDIO_ACTION_TYPE) as AudioAction
        }

        private fun audioSeekPercentage(intent: Intent): Int {
            return intent.getIntExtra(EXTRA_AUDIO_SEEK_PERCENTAGE, 0)
        }

        /**
         * Public api
         */
        fun play(intent: Intent): Intent = with(intent) {
            putExtra(EXTRA_AUDIO_ACTION_TYPE, PLAY)
        }

        fun pause(intent: Intent): Intent = with(intent) {
            putExtra(EXTRA_AUDIO_ACTION_TYPE, PAUSE)
        }

        fun stop(intent: Intent): Intent = with(intent) {
            putExtra(EXTRA_AUDIO_ACTION_TYPE, STOP)
        }

        fun seek(percentage: Int, intent: Intent): Intent = with(intent) {
            putExtra(EXTRA_AUDIO_ACTION_TYPE, SEEK)
            putExtra(EXTRA_AUDIO_SEEK_PERCENTAGE, percentage)
        }

        fun tickle(intent: Intent): Intent = with(intent) {
            putExtra(EXTRA_AUDIO_ACTION_TYPE, TICKLE)
        }
    }
}
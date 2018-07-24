package com.memtrip.exoeasy.player

import android.content.Intent

enum class AudioAction {
    PLAY,
    PAUSE,
    STOP,
    SEEK;

    companion object {
        private const val EXTRA_AUDIO_ACTION_TYPE = "EXTRA_AUDIO_ACTION_TYPE"
        private const val EXTRA_AUDIO_SEEK_PERCENTAGE = "EXTRA_AUDIO_SEEK_PERCENTAGE"

        internal fun perform(player: Player, intent: Intent): Unit = when (audioAction(intent)) {
            PLAY -> player.play()
            PAUSE -> player.pause()
            STOP -> player.stop()
            SEEK -> player.seek(audioSeekPercentage(intent))
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
    }
}
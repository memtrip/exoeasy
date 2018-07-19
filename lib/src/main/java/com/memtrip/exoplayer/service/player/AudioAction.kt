package com.memtrip.exoplayer.service.player

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
            AudioAction.PLAY -> player.play()
            AudioAction.PAUSE -> player.pause()
            AudioAction.STOP -> player.release()
            AudioAction.SEEK -> player.seek(audioSeekPercentage(intent))
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
            putExtra(EXTRA_AUDIO_ACTION_TYPE, AudioAction.PLAY)
        }

        fun pause(intent: Intent): Intent = with(intent) {
            putExtra(EXTRA_AUDIO_ACTION_TYPE, AudioAction.PAUSE)
        }

        fun stop(intent: Intent): Intent = with(intent) {
            putExtra(EXTRA_AUDIO_ACTION_TYPE, AudioAction.STOP)
        }

        fun seek(percentage: Int, intent: Intent): Intent = with(intent) {
            putExtra(EXTRA_AUDIO_ACTION_TYPE, AudioAction.SEEK)
            putExtra(EXTRA_AUDIO_SEEK_PERCENTAGE, percentage)
        }
    }
}
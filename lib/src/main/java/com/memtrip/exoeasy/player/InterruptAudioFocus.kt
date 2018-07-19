package com.memtrip.exoeasy.player

import android.media.AudioManager
import android.content.Context

@Suppress("DEPRECATION")
internal class InterruptAudioFocus constructor(
        private val interruptAudio: InterruptAudio,
        context: Context,
        private val audioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
) : AudioManager.OnAudioFocusChangeListener {

    fun register(): InterruptAudioFocus {
        audioManager.requestAudioFocus(this,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN)
        return this
    }

    fun unregister() {
        audioManager.abandonAudioFocus(this)
    }

    override fun onAudioFocusChange(focusChange: Int) {
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS || focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
            interruptAudio()
        }
    }
}